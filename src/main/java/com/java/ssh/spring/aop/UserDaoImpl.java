package com.java.ssh.spring.aop;

import org.apache.ibatis.session.SqlSession;

public class UserDaoImpl implements IUserDao {

	protected SqlSession sqlSessionProxy;
	

	public void setSqlSessionProxy(SqlSession sqlSessionProxy) {
		this.sqlSessionProxy = sqlSessionProxy;
	}

	public User getById(Long id) {
		IUserMapper mapper = sqlSessionProxy.getMapper(IUserMapper.class);
		return mapper.getById(id);
	}

	@Override
	public boolean add(User t) {
		IUserMapper mapper = sqlSessionProxy.getMapper(IUserMapper.class);
		return mapper.add(t) > 0 ? true : false;
	}

	@Override
	public boolean update(User t) {
		IUserMapper mapper = sqlSessionProxy.getMapper(IUserMapper.class);
		return mapper.update(t) > 0 ? true : false;
	}

	@Override
	public boolean del(User t) {
		IUserMapper mapper = sqlSessionProxy.getMapper(IUserMapper.class);
		return mapper.del(t) > 0 ? true : false;
	}

}
