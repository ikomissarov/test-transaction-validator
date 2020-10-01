package com.example.testtransactionvalidator.service;

import static com.example.testtransactionvalidator.service.TransactionService.isAmountTooBig;
import static com.example.testtransactionvalidator.service.TransactionService.isFrequencyTooHigh;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.testtransactionvalidator.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransactionServiceTest {

    @Test
    void testAmountTooBig() {
        Transaction suspiciousTransaction = forAmount("100");
        Transaction nonSuspiciousTransaction = forAmount("10");

        List<Transaction> existingTransactions = List.of(
            forAmount("2"),
            forAmount("4"),
            forAmount("6")
        );

        assertThat(isAmountTooBig(suspiciousTransaction, existingTransactions)).isTrue();
        assertThat(isAmountTooBig(nonSuspiciousTransaction, existingTransactions)).isFalse();
        assertThat(isAmountTooBig(suspiciousTransaction, Collections.emptyList())).isFalse();
    }

    @Test
    void testFrequencyTooHigh() {
        Instant now = Instant.now();
        List<Transaction> evenlyDistributedTransactions = List.of(
            forCreatedAt(now),
            forCreatedAt(now.minus(30, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(60, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(90, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(120, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(150, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(180, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(210, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(240, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(270, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(300, ChronoUnit.DAYS))
        );

        assertThat(isFrequencyTooHigh(evenlyDistributedTransactions)).isFalse();

        List<Transaction> notEvenlyDistributedTransactions = List.of(
            forCreatedAt(now),
            forCreatedAt(now.minus(30, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(60, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(90, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(120, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(150, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(180, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(210, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(240, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(270, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(300, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(1, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(2, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(3, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(4, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(5, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(6, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(7, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(8, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(9, ChronoUnit.DAYS)),
            forCreatedAt(now.minus(10, ChronoUnit.DAYS))
        );

        assertThat(isFrequencyTooHigh(notEvenlyDistributedTransactions)).isTrue();

        assertThat(isFrequencyTooHigh(Collections.emptyList())).isFalse();
    }

    static Transaction forAmount(String amount) {
        return Transaction.builder()
            .amount(new BigDecimal(amount))
            .build();
    }

    static Transaction forCreatedAt(Instant createdAt) {
        return Transaction.builder()
            .createdAt(createdAt)
            .build();
    }
}