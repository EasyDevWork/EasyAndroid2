package com.easy.demo.aop;

import android.util.Log;
import android.view.View;

import com.easy.demo.R;
import com.easy.utils.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Calendar;

/**
 * 防止View被连续点击,间隔时间600ms
 */
@Aspect
public class SingleClickAop {
    static int TIME_TAG = R.id.click_id;
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private static final String TAG = "SingleClick";

    @Pointcut("execution(@com.easy.demo.aop.SingleClick * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs())
            if (arg instanceof View) view = (View) arg;
        if (view != null) {
            Object tag = view.getTag(TIME_TAG);
            long lastClickTime = ((tag != null) ? (long) tag : 0);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long between = currentTime - lastClickTime;
            if (between > MIN_CLICK_DELAY_TIME) {//过滤掉600毫秒内的连续点击
                view.setTag(TIME_TAG, currentTime);
                joinPoint.proceed();//执行原方法
                Log.d(TAG, StringUtils.buildString(AopUtils.getMethodInfo(joinPoint), " --->", Calendar.getInstance().getTimeInMillis() - currentTime, "ms 点击有效"));
            } else {
                Log.d(TAG, StringUtils.buildString(AopUtils.getMethodInfo(joinPoint), " --->", between, "ms 点击无效"));
            }
        }
    }
}
