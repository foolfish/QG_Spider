<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="/common/domain.jsp" flush="true"/> 
    <title>量化派</title>
    <script type="text/javascript" src="/${ctx }/js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="/${ctx }/js/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="/${ctx }/js/jquery.json-2.2.min.js"></script>
    <script type="text/javascript" src="js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">
    function myload() {
		var tb = document.getElementById('warning');
	     var rowNum=tb.rows.length;
	     for (var i=0;i<rowNum;i++)
	     {
	         tb.deleteRow(i);
	         rowNum=rowNum-1;
	         i=i-1;
	     }
		$.ajax({
	   	 	 type:'post',
      		 data:{"flag":"1"},
	     	 url:'/${ctx}/warning/getWarnings.html',
	         success : function(data) {
	         	var html = [];
         		if(true){
         			var d = data.warningList;
			        html.push('<tr><td width="300" align="center">编号</td><td width="300" align="center">用户id</td><td width="250" align="center">时间</td><td width="250" align="center">错误名称</td><td width="100" align="center">是否解决</td><td width="100" align="center">选择</td></tr>');
			        for (var i = 0; i < d.length; i++){
			        	var idDeal="";
			        	if(d[i].flag=="1"){
			        		idDeal="否";
			        	}else{
			        		idDeal="是";
			        	}
			        	var time = new Date(d[i].cTime);
			        	var baseTime = time.toLocaleString();
			            html.push('<tr><td align="center">' + d[i].id + '</td><td align="center">' + d[i].currentId + '</td><td align="center">' + baseTime + '</td><td align="center">' + d[i].ptype + '</td><td align="center">' + idDeal + '</td><td align="center"><input type="radio" value="' + d[i].id + '" name="id">&nbsp;&nbsp;&nbsp;<input type="button" value="修改" onclick="changeWarning();"></td></tr>');
			       		
			        }
         		}else{
         			html.push('<tr><td>暂时没有程序错误！</td></tr>');
         		}
         		$('#warning').prepend(html);
	    	 },
  		      error:function(){	  alert("访问出错！");}
	  	   });
	  	   
	  	 setTimeout("myload()",1000*60);//60秒刷新一次
	}
	function changeWarning() {
		 var value="";
		  var radio=document.getElementsByName("id");
		  for(var i=0;i<radio.length;i++){
			if(radio[i].checked==true){
			  value=radio[i].value;
			  break;
			}
		  }
		$.ajax({
	   	 	 type:'post',
      		 data:{"id":value},
	     	 url:'/${ctx}/warning/updateWarning.html',
	         success : function(data) {
	         	window.location.href="/${ctx}/warning.html";
	    	 },
  		      error:function(){	  alert("访问出错！");}
	  	   });
	}
    </script>
  </head>
  <body onload="myload()">
		<div id="baseTable">
			<form>
			<table id = "warning" border="1">
				<%-- <c:forEach items="${returnMap.warningList}" var="item">
		            <tr>
		            	<!-- 
		            	private String id;
		            	private Date cTime;//当前时间
						private String currentId;//当前用户ID
						private int flag; //是否出现问题0代表正常，1代表出现问题
						private String ptype;//报警类型 
						-->
			            <td>${item.id}</td>
			            <td>${item.currentId}</td>
			            <td>${item.cTime}</td>
			            <td>${item.ptype}</td>
			            <td>${item.flag}</td>
		            </tr>
		        </c:forEach> --%>
			</table>	
		</div>
   
  </body>
</html>
