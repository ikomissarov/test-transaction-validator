package com.example.testtransactionvalidator.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    @NotNull
    private UUID id;
    @NotEmpty
    private String iban;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    private Instant createdAt;
}
