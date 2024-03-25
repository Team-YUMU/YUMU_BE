package com.yumu.yumu_be.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    String key();   //락 이름
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;   //시간 단위 - 초
    long waitTime() default 5000L;     //락을 획득하기 위한 대기 시간
    long leaseTime() default 2000L;    //락을 임대하는 시간
}
