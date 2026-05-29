package com.example.xdlearnbackend.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 日志切面
 * 用于记录方法调用日志信息，包括时间、类名、方法名、参数等信息
 *
 * @author 木又
 * @date 2026/5/15 14:20
 */
@Aspect
@Slf4j
public class LogAspect {
    /**
     * 前置通知，在方法执行之前执行，用于记录方法调用日志信息
     *
     * @param joinPoint 连接点
     */

    @Before("execution(* com.example.xdlearnbackend.controller.*.*(..)) " +
            "|| execution(* com.example.xdlearnbackend.service.*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint){
        //获取当前时间
        String currentTime = LocalDateTime.now().toString();

        //获取方法签名
        String methodName = joinPoint.getSignature().getName();

        //获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();

        //获取参数
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);

        //打印日志
        log.info("方法调用日志--时间：{}，类名： {}，方法名： {}，参数： {}", currentTime, className, methodName, argsString);
    }
}
