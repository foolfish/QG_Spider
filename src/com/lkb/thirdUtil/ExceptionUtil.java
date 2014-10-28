package com.lkb.thirdUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

/*
 * 返回异常具体信息
 * */
public class ExceptionUtil {
	public String getTrace(Throwable t) {
		StringWriter stringWriter= new StringWriter();
		PrintWriter writer= new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer= stringWriter.getBuffer();
		return buffer.toString();
	}
}
