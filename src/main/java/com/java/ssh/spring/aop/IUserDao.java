package com.java.ssh.spring.aop;



public interface IUserDao{
	
	public User getById(Long id);
	
	public boolean add(User t);
	
	public boolean update(User t);
	
	public boolean del(User t);
	

}
