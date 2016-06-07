package com.java.ssh.spring.aop;

import java.sql.Timestamp;



public class User {
	
	private Long userId;
	private String username;
	private String nickname;
	private String truename;
	private String password;
	private Timestamp birth;
	private Short sex;
	private Timestamp createTime;
	private Short status;
	
	
	
	public enum Status{
		NORMAL((short)0),DISABLED((short)1),DELETED((short)2);
		public final Short value;
		private Status(Short value){
			this.value=value;
		}
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getBirth() {
		return birth;
	}
	public void setBirth(Timestamp birth) {
		this.birth = birth;
	}
	public Short getSex() {
		return sex;
	}
	public void setSex(Short sex) {
		this.sex = sex;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username
				+ ", nickname=" + nickname + ", truename=" + truename
				+ ", password=" + password + ", birth=" + birth + ", sex="
				+ sex + ", createTime=" + createTime + ", status=" + status
				+ "]";
	}
	
	
	
	
	

}
