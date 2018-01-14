package com.java.ssh.pool2;

import java.util.UUID;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class StringFactory implements PooledObjectFactory<String> {
	public StringFactory() {
		System.out.println("init string factory..");
	}

	public void activateObject(PooledObject<String> pool) throws Exception {
		// TODO Auto-generated method stub

	}

	public void destroyObject(PooledObject<String> pool) throws Exception {
		String str = pool.getObject();
		if (str != null) {
			str = null;
			System.out.println(str + " destroy...");
		}
	}

	public PooledObject<String> makeObject() throws Exception {
		String i = UUID.randomUUID().toString();
		System.out.println("make " + i + " success...");
		return new DefaultPooledObject<String>(i);
	}

	public void passivateObject(PooledObject<String> pool) throws Exception {
		// do nothing
	}

	public boolean validateObject(PooledObject<String> pool) {
		return true;
	}

	public static void test() {
		GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
		conf.setMaxTotal(10);
		GenericObjectPool<String> pool = new GenericObjectPool<String>(new StringFactory(), conf);
		for (int i = 0; i < 15; i++) {
			System.out.println(i + ":");
			try {
				String str = pool.borrowObject();
				System.out.println(str);
				//pool.returnObject(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pool.close();
	}

	public static void main(String[] args) {
		test();
	}

}
