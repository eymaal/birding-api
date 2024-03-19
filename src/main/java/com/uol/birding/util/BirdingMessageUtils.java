package com.uol.birding.util;

import org.springframework.http.ResponseEntity;

public class BirdingMessageUtils {
    public static ResponseEntity createErrorResponse(String message) {
        return ResponseEntity
                .badRequest()
                .body(BirdingMessage.builder().message(message).build());
    }
}
