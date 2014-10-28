package com.lkb.hbase;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * 创建时间：2014-10-21 上午11:07:37
 * 项目名称：LKB
 * @author Jerry Sun
 * @version 1.0
 * @since JDK 1.6
 * 文件名称：BeanUtil.java
 * 类说明：
 */
public class BeanUtil {
	private static Logger logger = Logger.getLogger(BeanUtil.class);
	
	/**
	* <p>Title: transBean2Map</p>
	* <p>Description: 将一个bean转换为map</p>
	* @author Jerry Sun
	* @param obj
	* @return
	*/
	public static Map<String, Object> transBean2Map(Object obj){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Class type = obj.getClass(); 
			BeanInfo beanInfo = Introspector.getBeanInfo(type); 
			PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
			for (int i = 0; i< propertyDescriptors.length; i++) { 
				PropertyDescriptor descriptor = propertyDescriptors[i]; 
				String propertyName = descriptor.getName(); 
				if (!propertyName.equals("class")) { 
					Method readMethod = descriptor.getReadMethod(); 
					Object result = readMethod.invoke(obj, new Object[0]); 
					if (result != null) { 
						map.put(propertyName, result); 
					} else { 
						map.put(propertyName, ""); 
					} 
				} 
			} 
		} catch (Exception e) {
			logger.error("bean2map出错!", e);
		}
        return map; 
	}
	
	/*public static Object transMap2Bean(Class type, Map map) { 
		Object obj = null;
        try {
        	BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性 
        	obj = type.newInstance(); // 创建 JavaBean 对象 

            // 给 JavaBean 对象的属性赋值 
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
            for (int i = 0; i< propertyDescriptors.length; i++) { 
                PropertyDescriptor descriptor = propertyDescriptors[i]; 
                String propertyName = descriptor.getName(); 

                if (map.containsKey(propertyName)) { 
                    //  try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。 
                    Object value = map.get(propertyName); 

                    Object[] args = new Object[1]; 
                    args[0] = value; 

                    descriptor.getWriteMethod().invoke(obj, args); 
                } 
            } 
		} catch (Exception e) {
			logger.error("map2bean出错!", e);
		}
		return obj; 
    } */
}
