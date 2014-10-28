package com.lkb.hbase;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 创建时间：2014-10-21 下午2:40:09
 * 项目名称：LKB
 * @author Jerry Sun
 * @version 1.0
 * @since JDK 1.6
 * 文件名称：IHbaseDao.java
 * 类说明：HBase的DAO操作接口
 */
public interface IHbaseDao {
		
		/**
		* <p>Title: createHTable</p>
		* <p>Description: 创建表</p>
		* @author Jerry Sun
		* @param tableName 表名
		* @param columns family name组成的数组
		* @throws IOException
		*/
		void createHTable(String tableName, String[] columns);

		
		/**
		* <p>Title: createHTable</p>
		* <p>Description: 创建表</p>
		* @author Jerry Sun
		* @param tableName 表名
		* @throws IOException
		*/
		void createHTable(String tableName);

		
		/**
		* <p>Title: insertAndUpdate</p>
		* <p>Description: 插入或更新数据</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param row	行
		* @param family	列簇
		* @param map	请使用BeanUtil.transBean2Map将普通javabean转换为map
		* @param dataType 数据类型
		* 		（bill:通话账单  	
		* 		  billinfo:通话记录
		* 		  message:短信记录 	
		* 		  flow:流量账单
		* 		  flowlist:流量详单  ）
		* @throws IOException
		*/
		void insertAndUpdate(String tableName, String row, String family, String dataType, Object obj);
		
		/**
		* <p>Title: insertAndUpdate</p>
		* <p>Description: 批量插入或更新数据</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param row	行
		* @param family	列簇
		* @param dataType 数据类型
		* 		（bill:通话账单  	
		* 		  billinfo:通话记录
		* 		  message:短信记录 	
		* 		  flow:流量账单
		* 		  flowlist:流量详单  ）
		* @param list	
		*/
		void insertAndUpdate(String tableName, String row, String family, String dataType, List<Object> list);
		
		/**
		* <p>Title: insertAndUpdate</p>
		* <p>Description: 批量插入或更新数据</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param row	行
		* @param family	列簇
		* @param list	
		*/
		void insertAndUpdate(String tableName, String row, String family, List<Object> list);
		
		/**
		* <p>Title: insertAndUpdate</p>
		* <p>Description: </p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param row	行
		* @param family	列簇
		* @param qualifier	列名
		* @param value	列值
		*/
		void insertAndUpdate(String tableName, String row, String family,  
	            String qualifier, String value);  

		/**
		* <p>Title: removeFamily</p>
		* <p>Description: 删除列名(也就是删除family.属于修改表结构的操作.调用的时候请慎重)</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param colName	列名
		* @throws IOException
		*/
		void removeFamily(String tableName, String colName);

		/**
		* <p>Title: deleteColumn</p>
		* <p>Description: 删除某个family下的某个列的某行</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param rowID	行键
		* @param colName	列簇
		* @param cluster	列名
		* @throws IOException
		*/
		void deleteColumn(String tableName, String rowID, String colName,
				String cluster);

		/**
		* <p>Title: getValue</p>
		* <p>Description: 获取某一行,某一列簇中的某一列的值</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param rowID	行键
		* @param colName	列簇
		* @param cluster	列名
		* @return
		* @throws IOException
		*/
		String getValue(String tableName, String rowID, String colName,
				String cluster);

		/**
		* <p>Title: getColumnValue</p>
		* <p>Description: 获取某一列的所有行值</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param colName	列簇名
		* @param cluster	列名
		* @return
		* @throws IOException
		*/
		Map<String, String> getColumnValue(String tableName, String colName,
				String cluster);
		
		/**
		* <p>Title: getValueScan</p>
		* <p>Description: hbase根据rowkey的过滤查询</p>
		* @author Jerry Sun
		* @param tableName	表名
		* @param start_rowkey	开始key
		* @param stop_rowkey	结束key
		* @return
		*/
		List<Map<String, String>> getValueScan(String tableName, String start_rowkey,  
	            String stop_rowkey);

}
