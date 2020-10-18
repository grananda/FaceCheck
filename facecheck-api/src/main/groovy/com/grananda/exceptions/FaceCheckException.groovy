package com.grananda.exceptions

abstract class FaceCheckException extends RuntimeException {

    FaceCheckException() {
    }

    FaceCheckException(String message) {
        super(message)
    }

    FaceCheckException(String message, Throwable cause) {
        super(message, cause)
    }
}
