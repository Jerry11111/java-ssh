package com.java.ssh.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoTest {

	public static void main(String[] args) {
		test();
	}

	public static void test() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"com/java/ssh/spring/aop/applicationContext-tx.xml");

		IUserDao us = (IUserDao) ctx.getBean("userDao");
		User user=new User();
		user.setUsername("TEST");
		user.setNickname("TEST");
		user.setTruename("TEST");
		user.setSex((short)0);
		user.setPassword("123456");
		user.setStatus(User.Status.NORMAL.value);
		boolean res = us.add(user);
		System.out.println(String.format("addUse [%b]", res));

	}
}
