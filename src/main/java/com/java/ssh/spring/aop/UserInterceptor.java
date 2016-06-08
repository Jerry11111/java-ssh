package com.java.ssh.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class UserInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation arg0) throws Throwable {
		try {
//			if (arg0.getMethod().getName().equals("printUser")) {
//				Object[] args = arg0.getArguments();
//				System.out.println("user:" + args[0]);
//				arg0.getArguments()[0] = "hello!";
//			}
			System.out.println(String.format("intercept before[%s] ", arg0.getMethod().getName()));
			Object res = arg0.proceed();
			System.out.println(String.format("intercept after[%s %s] ", arg0.getMethod().getName(), res));
			return res;
		} catch (Exception e) {
			throw e;
		}
	}
}
