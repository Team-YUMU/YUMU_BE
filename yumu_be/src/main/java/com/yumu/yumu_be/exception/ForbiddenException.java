package com.yumu.yumu_be.exception;

public class ForbiddenException extends RuntimeException {
    public static class InvalidMemberException extends ForbiddenException {}
    public static class InvalidAccessTokenException extends ForbiddenException {}
    public static class InvalidRefreshTokenException extends ForbiddenException {}
    public static class NonAuthorityMemberException extends ForbiddenException {}
}
