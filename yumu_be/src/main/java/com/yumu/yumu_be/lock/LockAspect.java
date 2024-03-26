package com.yumu.yumu_be.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.yumu.yumu_be.lock.DistributeLock)")
    public Object redissonLock(final ProceedingJoinPoint joinPoint) throws  Throwable {
        //어노테이션으로 설정한 distributeLock 가져옴
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock annotation = method.getAnnotation(DistributeLock.class);

        //Lock 고유 키 값 - 메서드명{auctionId}
        String key = method.getName() + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.key());

        RLock lock = redissonClient.getLock(key);

        try {
            boolean lockable = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());
            if (!lockable) {
                return false;
            }
            return aopForTransaction.proceed(joinPoint);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException();
        } finally {
            lock.unlock();
        }
    }
}
