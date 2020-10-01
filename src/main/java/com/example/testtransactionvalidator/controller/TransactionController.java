package com.example.testtransactionvalidator.controller;

import com.example.testtransactionvalidator.dto.TransactionDto;
import com.example.testtransactionvalidator.dto.ValidationDto;
import com.example.testtransactionvalidator.model.Transaction;
import com.example.testtransactionvalidator.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @ApiOperation(value = "Validate a transaction")
    @PostMapping("/validate")
    public ValidationDto validate(@RequestBody @Valid TransactionDto transactionDto) {
        return service.validate(
            Transaction.builder()
                .id(transactionDto.getId())
                .iban(transactionDto.getIban())
                .amount(transactionDto.getAmount())
                .createdAt(transactionDto.getCreatedAt())
                .build());
    }
}
