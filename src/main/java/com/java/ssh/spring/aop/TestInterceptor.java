package com.java.ssh.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInterceptor {

	public static void main(String[] args) {
		test2();
	}

	public static void test() {
		// ApplicationContext ctx = new
		// FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"com/java/ssh/spring/aop/applicationContext-aop.xml");

		UserService us = (UserService) ctx.getBean("userServiceImpl");
		us.printUser("shawn");

	}
	public static void test2() {
		// ApplicationContext ctx = new
		// FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"com/java/ssh/spring/aop/applicationContext-aop.xml");
		
		UserService us = (UserService) ctx.getBean("userServiceImpl");
		us.saveUser("shawn");
		
	}
}
