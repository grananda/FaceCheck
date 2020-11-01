package com.grananda.exchange

import groovy.transform.TupleConstructor

@TupleConstructor
final class ErrorResponse {
    String message

    ErrorResponse(String message) {
        this.message = message
    }

    static String getInstance(String message) {
        new ErrorResponse(message)
    }
}
