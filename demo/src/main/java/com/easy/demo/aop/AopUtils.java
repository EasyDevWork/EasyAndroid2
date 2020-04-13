package com.easy.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class AopUtils {

    public static String getMethodInfo(ProceedingJoinPoint pjd) {
        MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(className).append(".");
        keyBuilder.append(methodName).append("(");

        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        if (parameterNames != null && parameterNames.length > 0) {
            for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
                keyBuilder.append(parameterTypes[i].getSimpleName()).append(" ").append(parameterNames[i]);
                if (i + 1 != methodSignature.getParameterNames().length) {
                    keyBuilder.append(",");
                }
            }
        }
        keyBuilder.append(")");
        return keyBuilder.toString();
    }
}
