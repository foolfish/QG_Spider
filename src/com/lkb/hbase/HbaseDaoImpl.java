package com.lkb.hbase;

import java.io.IOException;  
import java.util.ArrayList;
import java.util.HashMap;  
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;  
import java.util.Map.Entry;
  
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;  
import org.apache.hadoop.hbase.HColumnDescriptor;  
import org.apache.hadoop.hbase.HTableDescriptor;  
import org.apache.hadoop.hbase.KeyValue;  
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;  
import org.apache.hadoop.hbase.client.Get;  
import org.apache.hadoop.hbase.client.HBaseAdmin;  
import org.apache.hadoop.hbase.client.HTable;  
import org.apache.hadoop.hbase.client.Put;  
import org.apache.hadoop.hbase.client.Result;  
import org.apache.hadoop.hbase.client.ResultScanner;  
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * 创建时间：2014-10-21 下午2:46:03
 * 项目名称：LKB
 * @author Jerry Sun
 * @version 1.0
 * @since JDK 1.6
 * 文件名称：HbaseDaoImpl.java
 * 类说明：HBase的Dao操作具体实现，有待完善
 */
public class HbaseDaoImpl implements IHbaseDao {
	private static Logger logger = Logger.getLogger(HbaseDaoImpl.class);
	
	public final static String COLENDCHAR = String  
            .valueOf(KeyValue.COLUMN_FAMILY_DELIMITER);// ":" 列簇和列之间的分隔符  
  
	 private static Configuration conf;  
	 private static HBaseAdmin admin;  
	    
	    static {
	    	conf = HBaseConfiguration.create();
	    	/*conf.set("hbase.zookeeper.property.clientPort", "2181");  
	    	conf.set("hbase.zookeeper.quorum", "hd1,hd3,hd4");  
	    	conf.set("hbase.master", "hd1:600000");
	    	conf.set("hbase.rootdir", "hdfs://HD1:9000/hbase");*/
	    	
	    	conf.set("hbase.rootdir", "hdfs://HD1:9000/hbase");
			conf.set("hbase.zookeeper.quorum", "HD1,HD3,HD4");
			
	    	try {
				admin = new HBaseAdmin(conf);
			} catch (MasterNotRunningException e) {
				logger.error(e);
			} catch (ZooKeeperConnectionException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
    
	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public HBaseAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(HBaseAdmin admin) {
		this.admin = admin;
	}

	@Override
	public void createHTable(String tableName, String[] columns) {
		try {  
	           if (admin.tableExists(tableName))  
	               return;// 判断表是否已经存在  
	           HTableDescriptor htdesc = this.createHTDesc(tableName);  
	           for (int i = 0; i < columns.length; i++) {  
	               String colName = columns[i];  
	               this.addFamily(htdesc, colName, false);  
	           }  
	           admin.createTable(htdesc);  
	        } catch (IOException e) {  
	            logger.error("hbase创建表失败!", e);  
	        }  
	}

	@Override
	public void createHTable(String tableName) {
		try {  
            if (admin.tableExists(tableName))  
                return;// 判断表是否已经存在  
            HTableDescriptor htdesc = this.createHTDesc(tableName);  
            admin.createTable(htdesc);  
        } catch (IOException e) {  
        	logger.error("hbase创建表失败!", e); 
        }
	}

	@Override
	public void insertAndUpdate(String tableName, String row, String family, String dataType, Object obj) {
		HTable table = null;
		try {
			Map<String, Object> map = BeanUtil.transBean2Map(obj);
			JSONObject jso = new JSONObject(map);
			table = this.getHTable(tableName);  
			Put p = new Put(Bytes.toBytes(row)); 
/*			for (Entry<String, Object> entry : map.entrySet()) {
				p.add(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), Bytes  
						.toBytes(entry.getValue().toString())); 
			}*/
			p.add(Bytes.toBytes(family), Bytes.toBytes(dataType), Bytes.toBytes(jso.toString()));
			table.put(p);
		} catch (Exception e) {
			logger.error("hbase获取"+tableName+"表失败!", e); 
		}finally{
			try {
				table.close();
			} catch (IOException e) {
				logger.error("HTable关闭失败!", e);
			}
		}
	}
	
	@Override
	public void insertAndUpdate(String tableName, String row, String family, String dataType, List<Object> list) {
		if(list != null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				Map<String, Object> map = BeanUtil.transBean2Map(obj);
				JSONObject jso = new JSONObject(map);
				try {
					HTable table = this.getHTable(tableName);  
					Put p = new Put(Bytes.toBytes(row)); 
					/*for (Entry<String, Object> entry : map.entrySet()) {
						p.add(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue().toString())); 
					}*/
					p.add(Bytes.toBytes(family), Bytes.toBytes(dataType), Bytes.toBytes(jso.toString()));
					table.put(p);
				} catch (Exception e) {
					logger.error("hbase获取"+tableName+"表失败!", e); 
				}
			}
		}
	}
	
	@Override
	public void insertAndUpdate(String tableName, String row, String family,  List<Object> list) {
		if(list != null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				Map<String, Object> map = BeanUtil.transBean2Map(obj);
				try {
					HTable table = this.getHTable(tableName);  
					Put p = new Put(Bytes.toBytes(row)); 
					for (Entry<String, Object> entry : map.entrySet()) {
						p.add(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue().toString())); 
					}
					table.put(p);
				} catch (Exception e) {
					logger.error("hbase获取"+tableName+"表失败!", e); 
				}
			}
		}
	}
	
	public void insertAndUpdate(String tableName, String row, String family,  
	            String qualifier, String value){  
	    try {
	    	HTable table = this.getHTable(tableName);  
		    Put p = new Put(Bytes.toBytes(row));  
		    p.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes  .toBytes(value));  
		    table.put(p); 
		} catch (Exception e) {
			logger.error("hbase获取"+tableName+"表失败!", e); 
		} 
	} 

	@Override
	public void removeFamily(String tableName, String colName) {
		try {  
            String tmp = this.fixColName(colName);  
            if (admin.isTableAvailable(tableName))  
                admin.disableTable(tableName);  
            this.admin.deleteColumn(tableName, tmp);  
            this.admin.enableTable(tableName);  
        } catch (IOException e) {  
        	logger.error("hbase删除列簇失败!", e); 
        }  
	}

	@Override
	public void deleteColumn(String tableName, String rowID, String colName,
			String cluster) {
		try {  
            Delete del = new Delete(rowID.getBytes());  
            if (cluster == null || "".equals(cluster))  
//                del.deleteColumn(colName.getBytes());
            	return;
            else  
                del.deleteColumn(colName.getBytes(), cluster.getBytes());  
            HTable hTable = this.getHTable(tableName);  
            hTable.delete(del);  
        } catch (IOException e) {  
        	logger.error("hbase删除列失败!", e);   
        } 
	}

	@Override
	public String getValue(String tableName, String rowID, String colName,
			String cluster) {
		String v = null;
		try {  
            HTable hTable = this.getHTable(tableName);  
            Get get = new Get(rowID.getBytes());  
            Result result = hTable.get(get);  
            byte[] b = result.getValue(colName.getBytes(), cluster.getBytes());  
            if (b == null) {
            	v = new String("");
            }else{
            	v = new String(b);
            }
        } catch (IOException e) {  
        	logger.error("hbase获取某列失败!", e);  
        }
		return v;
	}

	@Override
	public Map<String, String> getColumnValue(String tableName, String colName,
			String cluster) {
		Map<String, String> resultMap = null;
		ResultScanner scanner = null;  
        try {  
            HTable hTable = this.getHTable(tableName);  
            scanner = hTable.getScanner(colName.getBytes(), cluster.getBytes());  
            Result rowResult = scanner.next();  
            resultMap = new HashMap<String, String>();  
            String row;  
            while (rowResult != null) {  
                row = new String(rowResult.getRow());  
                resultMap.put(row, new String(rowResult.getValue(colName  
                        .getBytes(), cluster.getBytes())));  
                rowResult = scanner.next();  
            }  
        } catch (IOException e) {  
        	logger.error("hbase获取某列所有行失败!", e);    
        } finally {  
            if (scanner != null) {  
                scanner.close();// 一定要关闭  
            }  
        } 
		return resultMap;  
	}

	/**
	* <p>Title: addFamily</p>
	* <p>Description: 给表添加列,此时不带列族</p>
	* @author Jerry Sun
	* @param htdesc	表的描述
	* @param colName	列名
	* @param readonly	是否只读
	*/
	private void addFamily(HTableDescriptor htdesc, String colName,  
            final boolean readonly) {  
        htdesc.addFamily(this.createHCDesc(colName));  
        htdesc.setReadOnly(readonly);  
    }
	
	/**
	* <p>Title: createHCDesc</p>
	* <p>Description: 创建列的描述,添加后，该列会有一个冒号的后缀，用于存储(列)族, 将来如果需要扩展，那么就在该列后加入(列)族</p>
	* @author Jerry Sun
	* @param colName	列名
	* @return	列的描述
	*/
	private HColumnDescriptor createHCDesc(String colName) {  
        String tmp = this.fixColName(colName);  
        byte[] colNameByte = Bytes.toBytes(tmp);  
        return new HColumnDescriptor(colNameByte);  
    }
	
	/**
	* <p>Title: fixColName</p>
	* <p>Description: 针对hbase的列的特殊情况进行处理,列的情况: course: or course:math, 就是要么带列族，要么不带列族(以冒号结尾)</p>
	* @author Jerry Sun
	* @param colName	列名
	* @param cluster	列簇
	* @return
	*/
	private String fixColName(String colName, String cluster) {  
        if (cluster != null && cluster.trim().length() > 0  
                && colName.endsWith(cluster)) {  
            return colName;  
        }  
        String tmp = colName;  
        int index = colName.indexOf(COLENDCHAR);  
        // int leng = colName.length();  
        if (index == -1) {  
//            tmp += COLENDCHAR;  
        }  
        // 直接加入列族  
        if (cluster != null && cluster.trim().length() > 0) {  
            tmp += cluster;  
        }  
        return tmp;  
    }
	
	private String fixColName(String colName) {  
        return this.fixColName(colName, null);  
    }  
	
	/**
	* <p>Title: createHTDesc</p>
	* <p>Description: 创建表的描述</p>
	* @author Jerry Sun
	* @param tableName	表名
	* @return
	*/
	private HTableDescriptor createHTDesc(final String tableName) {  
        return new HTableDescriptor(tableName);  
    }
	
	/**
	* <p>Title: getHTable</p>
	* <p>Description: 取得某张表</p>
	* @author Jerry Sun
	* @param tableName
	* @return
	* @throws IOException
	*/
	private HTable getHTable(String tableName) throws IOException {  
        try {  
            return new HTable(conf, tableName);  
        } catch (IOException e) {  
            throw e;  
        }  
    }

	@Override
	public List<Map<String, String>> getValueScan(String tableName,
			String start_rowkey, String stop_rowkey) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(start_rowkey));  
        scan.setStopRow(Bytes.toBytes(stop_rowkey));  
        ResultScanner rs = null;  
        try {  
        	HTable table = this.getHTable(tableName);  
            rs = table.getScanner(scan);  
            for (Result r : rs) {  
            	Map<String, String> map = new LinkedHashMap<String, String>();
                for (KeyValue kv : r.list()) { 
                	 String key = Bytes.toStringBinary(kv.getQualifier());  
                     String value = Bytes.toStringBinary(kv.getValue()); 
                     map.put(key, value);
                     map.put("rowKey", Bytes.toStringBinary(kv.getRow()));
                }  
                list.add(map);
            }  
        } catch (IOException e) {
        	logger.error("hbase过滤查询出错!", e); 
		} finally {  
            rs.close();  
        }  
		return list;
	}
	
	
}
