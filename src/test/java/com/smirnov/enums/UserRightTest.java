package com.smirnov.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserRightTest {

    @Test
    void getTextRequest_user() {
        String text = "message";
        String textResult = "message";

        assertEquals(textResult, UserRight.ROLE_USER.getTextRequest(text));
    }

    @Test
    void getTextRequest_operator() {
        String text = "message message";
        String textResult = "m-e-s-s-a-g-e- -m-e-s-s-a-g-e";

        assertEquals(textResult, UserRight.ROLE_OPERATOR.getTextRequest(text));
    }

    @Test
    void getTextRequest_operator_NPE() {
        String text = null;


        assertThrows(NullPointerException.class, () -> UserRight.ROLE_OPERATOR.getTextRequest(text));
    }
}