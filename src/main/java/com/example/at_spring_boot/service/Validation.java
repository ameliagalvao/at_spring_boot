package com.example.at_spring_boot.service;

public final class Validation {
    private Validation() {}

    public static void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
    }

    public static void requireEmail(String email, String message) {
        requireNonBlank(email, message);
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonNull(Object value, String message) {
        if (value == null) throw new IllegalArgumentException(message);
    }
}
