package com.easy.framework.aop;


import android.util.Log;

import com.easy.utils.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.concurrent.TimeUnit;

@Aspect
public class CostTimeAop {

    private static final String TAG = "CostTime";

    /**
     * Around 切点前后执行
     * 匹配所有@MethodTime
     * execution(<修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
     *
     * @param pjd
     * @return
     */
    @Around("execution(@com.easy.framework.aop.CostTime * *(..))")
    public Object aroundMethod(ProceedingJoinPoint pjd) {
        Object result;
        long startTime = System.nanoTime();
        try {
            //执行目标方法
            result = pjd.proceed();
            Log.d(TAG, StringUtils.buildString(AopUtils.getMethodInfo(pjd), " --->",
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime), "ms [",
                    "thread ", Thread.currentThread().getName(), "]"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
