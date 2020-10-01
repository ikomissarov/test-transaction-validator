package com.example.testtransactionvalidator.service;

import com.example.testtransactionvalidator.dao.TransactionRepository;
import com.example.testtransactionvalidator.dto.ValidationCode;
import com.example.testtransactionvalidator.dto.ValidationDto;
import com.example.testtransactionvalidator.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    //todo move all constants to config properties
    private static final double AMOUNT_TIMES_THRESHOLD = 10;
    private static final Period FREQUENCY_PERIOD = Period.parse("P30D");
    private static final int FREQUENCY_NUMBER_OF_PERIODS = 10;
    private static final double FREQUENCY_TIMES_THRESHOLD = 10;

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public ValidationDto validate(Transaction transaction) {
        Set<ValidationCode> failedValidations = new HashSet<>();

        List<Transaction> existingTransactions = transactionRepository.findAllByIban(transaction.getIban());
        if (isAmountTooBig(transaction, existingTransactions)) {
            failedValidations.add(ValidationCode.TOO_BIG_AMOUNT);
        }
        if (isFrequencyTooHigh(existingTransactions)) {
            failedValidations.add(ValidationCode.TOO_HIGH_FREQUENCY);
        }

        List<String> blackListedAccounts = transactionRepository.findBlackListedAccounts();
        if (blackListedAccounts.contains(transaction.getIban())) {
            failedValidations.add(ValidationCode.BLACKLISTED_ACCOUNT);
        }

        log.info("Failed validations: {}", failedValidations);
        return ValidationDto.builder()
            .valid(true)
            .failedValidations(failedValidations)
            .build();
    }

    static boolean isAmountTooBig(Transaction transaction, List<Transaction> existingTransactions) {
        if (existingTransactions.isEmpty()) {
            //todo probably would make sense to return false also if we have too few transactions
            return false;
        }
        BigDecimal sum = existingTransactions.stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageAmount = sum.divide(BigDecimal.valueOf(existingTransactions.size()), RoundingMode.HALF_EVEN);
        return transaction.getAmount().divide(averageAmount, RoundingMode.DOWN).doubleValue() > AMOUNT_TIMES_THRESHOLD;
    }

    static boolean isFrequencyTooHigh(List<Transaction> existingTransactions) {
        if (existingTransactions.isEmpty()) {
            //todo probably would make sense to return false also if we have too few transactions
            return false;
        }
        NavigableMap<Instant, List<Transaction>> transactionsByTime = new TreeMap<>();
        Instant now = Instant.now();
        for (int i = 1; i <= FREQUENCY_NUMBER_OF_PERIODS; i++) {
            transactionsByTime.put(now.minus(FREQUENCY_PERIOD.multipliedBy(i)), new ArrayList<>());
        }
        for (Transaction existingTransaction : existingTransactions) {
            Instant key = transactionsByTime.lowerKey(existingTransaction.getCreatedAt());
            if (key != null) {
                transactionsByTime.get(key).add(existingTransaction);
            }
        }
        Instant currentPeriod = transactionsByTime.lastKey();
        int currentPeriodFrequency = transactionsByTime.remove(currentPeriod).size() + 1;

        double averageFrequency = transactionsByTime.values().stream()
            .mapToInt(Collection::size)
            .summaryStatistics()
            .getAverage();

        return currentPeriodFrequency / averageFrequency > FREQUENCY_TIMES_THRESHOLD;
    }
}
