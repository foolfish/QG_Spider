<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报告</title>
<jsp:include page="/common/domain.jsp" flush="true"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="/${ctx }/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/${ctx }/js/jquery-migrate-1.2.1.min.js"></script>
</head>
<style>
	body,.container{
		width:100%;
		margin:0;
		padding:0;
		font-family: "微软雅黑";
	}
	h4{
		width: 100%;
		text-align: center;
	}
	h5{
		width: 100%;
		text-align: center;
	}
	.top-side,.bottom-side{
		clear:both;
		position: relative;
		width:90%;
		margin:0 auto;
		min-width: 960px;
	}
	.user-info{
		float:left;
		width:100%;
		border:2px solid #B1CDE3;
	
	}
	.user-info ul li{
		list-style-type: none;
		height:50px;
		line-height: 50px;
	}
	label[for=phonenumber]{
		margin-left:60%;
	}
	.bottom-side1 table {
		border: 1px solid #B1CDE3;
		padding:0; 
		margin:0 auto;
		border-collapse: collapse;
	}
		
	.bottom-side1 td {
		border: 1px solid #B1CDE3;
		background: #fff;
		font-size:15px;
		padding: 3px 3px 3px 8px;
		color: #000;
	}
	th{
	border-width: 1px;
	padding: 2px;
	border-style: solid;
	border-color: #B1CDE3;
	background-color: #d4d5d6;
	}	
	td{
	text-align: center;
	}	
	.left{float:left;width:60%;}
	.right{float:right;width:40%;}
</style>
<body>
	
     <div class="container">
		<div class="top-side">
			<h4>个人信用报告</h4>
			<h>报告编号：${user.markId} &nbsp;&nbsp;&nbsp;	报告时间：<fmt:formatDate value="${user.modifyDate}"  type="both"/></h>		
		
						<h5>查询信息</h3>
			<table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
			<tr>
			    <th>姓名</th>
			    <th>性别</th>
			    <th>证件类型</th>
			    <th>证件号码</th>
			    <th>是否实名认证</th>
			    <th>常用手机号</th>
			    
	
			  </tr>
			  <tr>
			    <td>${user.realName}</td>
			    <td>${identify.sex}</td>
			    <td>身份证</td>
			    <td>${user.idcard}</td>
			    <td>${isRealName}</td>
			    <td>${phones}</td>
			  </tr>
			  

			  
			  
			  </table>		
			<h4>一 个人基本信息 </h4>
				<table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
			<tr>
			    <th>出生年月</th>
			    <th>职业</th>
			    <th>是否有子女</th>
			    <th>年龄</th>
			    <th>工作地</th>
			    <th>健康状况</th>
			    <th>学历</th>
			    <th>职工性质</th>
			    <th>户籍</th>
			    <th>婚姻状况</th>
	
			  </tr>
			  <tr>
			    <td>${identify.birthday}</td>
			    <td>${somethingMap.zhiye}</td>
			    <td></td>
			    <td>${identify.age}</td>
			    <td>${workLocation}</td>
			    <td></td>
			    <td>${somethingMap.xueli}</td>
			    <td>${somethingMap.worknature}</td>
			    <td>${identify.city}</td>
			    <td>${somethingMap.marry}</td>
			  </tr>
			  

			  
			  
			  </table>	

		
			  	

				<h5>(二) 收入储蓄汇总</h5>
				<h5> 收入汇总</h5>
		    <table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
			<tr>
				<th>编号</th>
			    <th>收入总金融</th>
			    <th>次数</th>	
			    <th>月均收入</th>	
			    <th>来源</th>			  
			  </tr>
			  
			  <c:if test="${not empty incomelist}">
			   <c:forEach var="income" items="${incomelist}" varStatus="status"> 
			  <tr>
			    <td>${status.index + 1}</td>
			    <td>${income.amount}</td>
			    <td>${income.acount}</td>
			    <td>${income.perAmount}</td>    	
			    <td>${income.source}</td>			
			  </tr>
			  </c:forEach>
			  </c:if>
			</table>
			
				<h5> 储蓄汇总</h5>
		    <table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
			<tr>
				<th>编号</th>
			    <th>收入总金融</th>
			    <th>次数</th>	
			    <th>来源</th>			  
			  </tr>
			  
			
			</table>
			
			
			<h5>(三) 消费汇总</h4>
		    <table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
			<tr>
				<th>编号</th>
			    <th>消费总金额</th>
			    <th>次数</th>	
			    <th>来源</th>			  
			  </tr>
			  
			  
			  <c:if test="${not empty consumptionlist}">
			   <c:forEach var="consumption" items="${consumptionlist}" varStatus="status"> 
			  <tr>
			    <td>${status.index + 1}</td>
			    <td>${fn:replace(consumption.amount,'-','')}</td>
			    <td>${consumption.acount}</td>	
			    <td>${consumption.source}</td>			
			  </tr>
			  </c:forEach>
			  </c:if>
			</table>
			
			<br>
			<h4>三 交易行为</h4>		
			<h5>按人名分类</h5>
			<table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
				<tr>
				<th>编号</th>
			    <th>人名</th>
			    <th>消费总金额</th>
			    <th>消费次数</th>
			    <th>来源</th>
			  </tr>
			  
			  
			   <c:if test="${not empty tranlist}">
			   <c:forEach var="tran" items="${tranlist}" varStatus="status"> 
			  <tr>
			    <td>${status.index + 1}</td>
			    <td>${tran.receiver}</td>	
			    <td>${tran.money}</td>
			    <td>${tran.acount}</td>
				<td>${tran.ordersource}</td>
			  </tr>
			  </c:forEach>
			  </c:if>
			  </table>
			  
			  <h5>按地址分类</h5>
				<table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
				<tr>
				<th>编号</th>
			    <th>地址</th>
			    <th>消费总金额</th>
			    <th>消费次数</th>
			    <th>来源</th>

			  </tr>
			   <c:if test="${not empty tranAddrlist}">
			   <c:forEach var="tran" items="${tranAddrlist}" varStatus="status"> 
			  <tr>
			    <td>${status.index + 1}</td>
			    <td style="text-align: left;">${tran.recevierAddr}</td>
			    <td>${tran.money}</td>
			    <td>${tran.acount}</td>
				<td>${tran.ordersource}</td>
			  </tr>
			  </c:forEach>
			  </c:if>
			  
			
			  </table>	
			
			
				<h5>电话记录</h5>
		    <table width="100%" height="58" border="1" cellpadding="0" cellspacing="0" >
			<tr>
				<th>编号</th>
			    <th>号码</th>
			    <th>归属地	</th>
			    <th>联系次数</th>	
			    <th>联系时间</th>	
			    <th>主叫次数</th>	
			    <th>被叫次数</th>		
		  
			  </tr>
			  
			  <c:if test="${not empty phoneList}">
			   <c:forEach var="phone" items="${phoneList}" varStatus="status"> 
			  <tr>
			    <td>${status.index + 1}</td>
			    <td>${phone.phone}</td>
			    <td>${phone.place}</td>
			    <td>${phone.total}</td>    	
			    <td>${phone.totaltimes}秒</td>
			    <td>${phone.zhujiao}</td>	
			    <td>${phone.beijiao}</td>				
			  </tr>
			  </c:forEach>
			  </c:if>
			</table>					
		</div>		
        <br><br><br>
</body>

</html>

