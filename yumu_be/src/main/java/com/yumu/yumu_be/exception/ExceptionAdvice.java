package com.yumu.yumu_be.exception;

import com.yumu.yumu_be.exception.api.RestApiException;
import com.yumu.yumu_be.exception.api.Status;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Log4j2
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestApiException badRequestException(BadRequestException e) {
        if (e instanceof BadRequestException.DuplicatedEmailException) {
            return new RestApiException(Status.DUPLICATED_EMAIL);
        } else if (e instanceof BadRequestException.DuplicatedNicknameException) {
            return new RestApiException(Status.DUPLICATED_NICKNAME);
        } else if (e instanceof BadRequestException.DuplicatedMemberException) {
            return new RestApiException(Status.DUPLICATED_MEMBER);
        } else if (e instanceof BadRequestException.InvalidEmailException) {
            return new RestApiException(Status.INVALID_EMAIL);
        } else if (e instanceof BadRequestException.InvalidPasswordException) {
            return new RestApiException(Status.INVALID_PASSWORD);
        } else if (e instanceof BadRequestException.InvalidNicknameException) {
            return new RestApiException(Status.INVALID_NICKNAME);
        }
        log.info("Bad Request Error = {}", e.getMessage());
        return new RestApiException(Status.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestApiException notFoundException(NotFoundException e) {
        if (e instanceof NotFoundException.NotFoundMemberException) {
            return new RestApiException(Status.NOT_FOUND_MEMBER);
        } else if (e instanceof NotFoundException.NotFoundAccessTokenException) {
            return new RestApiException(Status.NOT_FOUND_ACCESS_TOKEN);
        } else if (e instanceof NotFoundException.NotFoundRefreshTokenException) {
            return new RestApiException(Status.NOT_FOUND_REFRESH_TOKEN);
        }else if (e instanceof NotFoundException.NotFoundAuctionException) {
            return new RestApiException(Status.NOT_FOUND_AUCTION);
        }else if (e instanceof NotFoundException.NotFoundReceiveTypeException) {
            return new RestApiException(Status.NOT_FOUND_RECEIVE_TYPE);
        }
        log.info("Not Found Error = {}", e.getMessage());
        return new RestApiException(Status.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestApiException forbiddenException(ForbiddenException e) {
        if (e instanceof ForbiddenException.InvalidMemberException) {
            return new RestApiException(Status.INVALID_MEMBER);
        } else if (e instanceof ForbiddenException.InvalidAccessTokenException) {
            return new RestApiException(Status.INVALID_ACCESS_TOKEN);
        } else if (e instanceof ForbiddenException.InvalidRefreshTokenException) {
            return new RestApiException(Status.INVALID_REFRESH_TOKEN);
        } else if (e instanceof ForbiddenException.NonAuthorityMemberException) {
            return new RestApiException(Status.NON_AUTHORITY_MEMBER);
        }
        log.info("Forbidden Error = {}", e.getMessage());
        return new RestApiException(Status.FORBIDDEN);
    }


}
