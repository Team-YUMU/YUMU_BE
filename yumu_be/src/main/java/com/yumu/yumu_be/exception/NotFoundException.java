package com.yumu.yumu_be.exception;

public class NotFoundException extends RuntimeException {
    public static class NotFoundMemberException extends NotFoundException {}
    public static class NotFoundAccessTokenException extends NotFoundException {}
    public static class NotFoundRefreshTokenException extends NotFoundException {}
}
