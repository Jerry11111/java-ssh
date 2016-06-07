package com.java.ssh.spring.aop;



public interface IUserMapper{
	
	public User getById(Long id);
	
	public int add(User t);
	
	public int update(User t);
	
	public int del(User t);

}
