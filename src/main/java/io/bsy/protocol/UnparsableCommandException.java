package io.bsy.protocol;

public class UnparsableCommandException extends RuntimeException {

    UnparsableCommandException(String msg) {
        super(msg);
    }
}
