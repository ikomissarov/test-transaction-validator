package com.example.testtransactionvalidator.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationDto {
    private boolean valid;
    private Set<ValidationCode> failedValidations;
}
