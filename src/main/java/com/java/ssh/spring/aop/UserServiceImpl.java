package com.java.ssh.spring.aop;

public class UserServiceImpl implements UserService {
	public void printUser(String user) {
		System.out.println(String.format("printUser [%s]", user));
	}
	public void saveUser(String user) {
		System.out.println(String.format("saveUser [%s]", user));
	}
}
