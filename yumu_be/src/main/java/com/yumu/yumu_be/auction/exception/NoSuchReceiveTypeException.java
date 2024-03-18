package com.yumu.yumu_be.auction.exception;

public class NoSuchReceiveTypeException extends RuntimeException{
    private static final String NO_SUCH_RECEIVE_TYPE_MESSAGE = "[ERROR] 잘못된 수령 방법입니다.";

    public NoSuchReceiveTypeException() {
        super(NO_SUCH_RECEIVE_TYPE_MESSAGE);
    }
}
