package com.lkb.util.httpclient.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**基础类
 * 内部为list和map集
 * @author fastw
 * @date	2014-9-17 上午12:41:53
 */
public class SimpleData  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5841908082433969822L;
	protected Logger logger = LoggerFactory.getLogger(SimpleData.class);
	private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
	private LinkedList linkedList;
	private Lock lock = new ReentrantLock();

	public SimpleData() {
	}
	public SimpleData(LinkedList list) {
		linkedList  = list;
	}

	/**
	 * @param key
	 * @param value 如果为空不存储
	 * @return
	 */
	public SimpleData put(String key, Object value) {
		if (value != null) {
			lock.lock();
			try{
				if(value!=null){
					this.dataMap.put(key, value);	
				}
			}finally{
				lock.unlock();
			}
		}
		return this;
	}
	public void initLinkedList(){
		if(linkedList==null){
			linkedList = new LinkedList();
		}
	}
	private void initMap(){
		
	}
	
	/**返回兼职存储的对象
	 * @return
	 */
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public SimpleData add(Object t) {
		Args.notNull(t,"Object 对象");
		initLinkedList();
		this.linkedList.add(t);
		return this;
	}
	public SimpleData addFirst(Object t) {
		Args.notNull(t,"Object 对象");
		initLinkedList();
		this.linkedList.addFirst(t);
		return this;
	}
	public SimpleData addAll(Collection c)  {
		Args.notNull(c,"Collection 对象");
		initLinkedList();
		this.linkedList.addAll(c);
		return this;
	}
	public LinkedList getList(){
		return this.linkedList;
	}
	public Object getFirst(){
		Args.notNull(this.linkedList,"linkedList 对象");
		return this.linkedList.getFirst();
	}
	public Object getLast(){
		Args.notNull(this.linkedList,"linkedList 对象");
		return this.linkedList.getLast();
	}
	public Object removeFirst(){
		Args.notNull(this.linkedList,"linkedList 对象");
		return this.linkedList.removeFirst();
	}
	public Object removeList(){
		Args.notNull(this.linkedList,"linkedList 对象");
		return this.linkedList.removeLast();
	}
	public int size(){
		Args.notNull(this.linkedList,"linkedList 对象");
		return this.linkedList.size();
	}
	public Object get(String key) {
		return this.dataMap.get(key);
	}
	public Double getDouble(String key) {
		return (Double)this.dataMap.get(key);
	}


	public Number getNumber(String key) {
		return (Number) this.dataMap.get(key);
	}


	public Integer getInteger(String key) {
		return (Integer) this.dataMap.get(key);
	}

	public String getString(String key) {
		return (String) this.dataMap.get(key);
	}

	public String[] getStringArray(String key) {
		return (String[]) this.dataMap.get(key);
	}

	public Date getDate(String key) {
		return (Date) this.dataMap.get(key);
	}

	public Boolean getBoolean(String key) {
		return (Boolean) this.dataMap.get(key);
	}
	public List getList(String key) {
		return (List) this.dataMap.get(key);
	}
	public Set getSet(String key) {
		return (Set) this.dataMap.get(key);
	}
	public void remove(String key){
		this.dataMap.remove(key);
	}
	public void clear() {
		lock.lock();
		try{
			if(this.dataMap!=null){
				this.dataMap.clear();	
			}
			if(this.linkedList!=null){
				this.linkedList.clear();
			}
		}finally{
			lock.unlock();
		}
		
	}
	public boolean isEmpty() {
		if(this.dataMap!=null&&this.dataMap.isEmpty()&&this.linkedList!=null&&this.linkedList.isEmpty()){
			return true;
		}
		return false;
	}

	

	
	
}
