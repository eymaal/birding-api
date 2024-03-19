package com.uol.birding.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class BirdingMessageUtilsTest {

    @Test
    void createErrorResponseTest() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,
                BirdingMessageUtils.createErrorResponse("testMessage").getStatusCode());
    }
}
