package com.easy.demo.aop;


import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MethodAop {

    private static final String TAG = "MethodAop";

    @Pointcut("execution(@com.easy.demo.aop.TestAop * *(..))")
    public void executionActivityOn() {

    }

    @Before("executionActivityOn()")
    public void executionBefore1(JoinPoint joinPoint) {
        Log.d(TAG, "切点之前执行方式1");
    }

    @Before("execution(@com.easy.demo.aop.TestAop * *(..)")
    public void executionBefore2(JoinPoint joinPoint) {
        Log.d(TAG, "切点之前执行方式2");
    }

    @After("executionActivityOn()")
    public void executionAfter(JoinPoint joinPoint) {
        Log.d(TAG, "切点之后执行");
    }

    @AfterReturning("executionActivityOn()")
    public void executionAfterReturn(JoinPoint joinPoint) {
        Log.d(TAG, "切点方法返回结果之后执行");
    }

    @AfterThrowing("executionActivityOn()")
    public void executionAfterThrow(JoinPoint joinPoint) {
        Log.d(TAG, "切点抛出异常时执行");
    }
}
