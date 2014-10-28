package com.lkb.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/*
 * 
 * 生成随机值,去掉 o 1 l
 * */
public class YqRandom {
	private static String[] source = {"0","2","3","4","5","6","7","8","9","a","b",
		"c","d","e","f","g","h","i","j","k","m","n","p","q","r","s","t","u","v","w","x","y","z"};
	
	/*
	 * 生成N位的随机值
	 * */
	public String getRandom(int n){
		String random = "";
		for(int i=0;i<n;i++){
			Random rand = new Random();
			int randNum = rand.nextInt(source.length);
			random += source[randNum];
		}
		
		return random;
	}
	/*
	 * 获得m个随机值,每个随机值的长度是n
	 * */
	public Set getSet(int m,int n){
		Set set = new HashSet();
		while(set.size()<m){
			String random = getRandom(n);
			set.add(random);
		}
		return set;
		
	}
	
	public static void main(String[] args){
		YqRandom yqRandom = new YqRandom();
		Set set = yqRandom.getSet(100,8);
		Iterator it = set.iterator();
		while(it.hasNext()){
			String value = it.next().toString();
			System.out.println(value);
		}		
	}
}
