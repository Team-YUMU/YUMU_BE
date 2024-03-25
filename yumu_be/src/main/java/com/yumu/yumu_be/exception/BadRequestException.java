package com.yumu.yumu_be.exception;


public class BadRequestException extends RuntimeException {
    public static class DuplicatedEmailException extends BadRequestException {}
    public static class DuplicatedNicknameException extends BadRequestException {}
    public static class DuplicatedMemberException extends BadRequestException {}
    public static class InvalidEmailException extends BadRequestException {}
    public static class InvalidPasswordException extends BadRequestException {}
    public static class InvalidNicknameException extends BadRequestException {}
    public static class NotMatchPasswordException extends BadRequestException {}
    public static class AlreadySignupKakaoException extends BadRequestException {}
    public static class LowBidPriceException extends BadRequestException {}
}
