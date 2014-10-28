package com.lkb.thirdUtil;

import java.util.Comparator;
import java.util.Map;

public class MyComparator  implements Comparator {

	   public int compare(Object o1, Object o2) {
	       Map m1=(Map)o1;
	       Map m2=(Map)o2;
	       if(Integer.parseInt(m1.get("total").toString())<Integer.parseInt(m2.get("total").toString()))
	               return 1;
	          if(Integer.parseInt(m1.get("total").toString())==Integer.parseInt(m2.get("total").toString()))
	              return 0;
	           else
	               return -1;
	       }
          
	  }


