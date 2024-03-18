package com.yumu.yumu_be.valid.annotation;

import com.yumu.yumu_be.valid.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)              //필드에 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME)     //런타임 중 적용, 유지
@Constraint(validatedBy = PasswordValidator.class)  //유효성 검증 로직 지정
public @interface PasswordCheck {

    String message() default "유효한 비밀번호 형식이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
