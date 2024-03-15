package com.example.atmdemoappforoasis.exception;

public record ValidationError(
        String field,
        String message
) {
}