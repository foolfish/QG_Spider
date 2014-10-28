package com.lkb.util.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**java對象序列化
 * @author fastw
 * @version 1.0 2014-7-3 00:54:14    
 */
public class SerializeUtil {
	
	public static Logger log = Logger.getLogger(SerializeUtil.class);
	/***
	 * 序列化對象
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			baos.flush();
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				baos.close();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				ois.close();
				bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
}
