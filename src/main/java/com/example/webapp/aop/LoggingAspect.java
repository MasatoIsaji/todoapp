package com.example.webapp.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggingAspect {

	/**
	 * Controllerのログ出力
	 * 開始・終了時にメソッド名と引数を出力
	 * @param joinPoint 起動メソッド
	 * @return メソッド実行結果
	 * @throws Throwable メソッド実行時の例外
	 */
	@Around("within(com.example.webapp.controller.*)")
	public Object controllerStartEndLog(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		log.info("==== start : Controller method : {}", methodName);
		log.debug("args: {}", Arrays.toString(args));

		Object result = joinPoint.proceed();

		log.info("==== end : Controller method : {}", methodName);
		log.debug("==== return: {}", result);

		return result;
	}

	/**
	 * Serviceのログ出力
	 * 開始・終了時にメソッド名と引数を出力
	 * @param joinPoint 起動メソッド
	 * @return メソッド実行結果
	 * @throws Throwable メソッド実行時の例外
	 */
	@Around("within(com.example.webapp.service.*.*)")
	public Object serviceStartEndLog(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		log.info("==== start : Service method : {}", methodName);
		log.debug("args: {}", Arrays.toString(args));

		Object result = joinPoint.proceed();

		log.info("==== end : Service method : {}", methodName);
		log.debug("==== return: {}", result);

		return result;
	}

}
