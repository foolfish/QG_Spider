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
<script type="text/javascript" src="/${ctx}/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/${ctx}/js/jquery-migrate-1.2.1.min.js"></script>
 <script type="text/javascript" src="/${ctx}/js/highcharts.js"></script>
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
	.left{float:left;width:60%;}
	.right{float:right;width:40%;}
</style>
<body>


  	<input type="hidden" id="jdJson" value="${jdJson}" />
  	<input type="hidden" id="shebaoJson" value="${shebaoJson}" />
  	<input type="hidden" id="alipayJson" value="${alipayJson}" />
     <div class="container">
		<div class="top-side">
			<h4>用户基本信息</h4>
			<div class="user-info">
			<div class="left">
			<ul>
					
					<li><label>真实姓名 ：</label><span >${user.realName}</span></li>
					<li><label >身份证：</label><span >${user.idcard}</span></li>
					<li><label >用户地址  ：</label><span>${user.addr}</span></li>
				</ul>
			</div>
			<div class="right">
			<ul>
					<li><label >性别：</label><span >${user.sex}</span></li>
					<li><label>手机号 ：</label><span >${user.phone}</span></li>
					<li><label >邮件：</label><span > ${user.email}</span></li>
					
				</ul>
			</div>	
			</div>
			
		</div>
			
			
		<div class="bottom-side">
			
			<c:if test="${not empty jdList}">
			<div id="containers1" style="min-width:200px;height:400px"></div>
			<div class="bottom-side1">
			<br><h4>用户订单信息</h4>
			<div class="order-info">
			<table width="100%" height="118" border="1" cellpadding="0" cellspacing="0" >
			<tr>
			    <th>订单ID</th>
			    <th>商品名称</th>
			    <th>收货人</th>
			    <th>订单金额</th>
			    <th>购买方式</th>
			    <th>下单时间</th>
			    <th>订单状态</th>
			    <th>订单来源</th>
			  </tr>
			  <c:forEach var="order" items="${jdList}"> 
			  <tr>
			    <td>${order.orderId}</td>
			    <td>${order.productNames}</td>
			    <td>${order.receiver}</td>
			    <td>${order.money}</td>
			    <td>${order.buyway} </td>
			    <td>${order.buyTime}</td>
			    <td>${order.orderstatus}</td>
			    <td>${order.ordersource}</td>
			  </tr>
			  </c:forEach>
			</table>			
			</div>
			</div>
			</c:if>
			
			
			
			
			<c:if test="${not empty shebaoList}">
			<div id="containers2" style="min-width:200px;height:400px"></div>
			<div class="bottom-side1">
			<br><h4>用户社保</h4>
			<div class="order-info">
			<table width="100%" height="118" border="1" cellpadding="0" cellspacing="0" >
			<tr>
			    <th>年月</th>
			    <th>缴费基数</th>
			    <th>养老保险个人缴费额</th>
			    <th>医疗保险个人缴费额</th>
			    <th>失业保险个人缴费额</th>
			   	 	 	
			  </tr>
			  <c:forEach var="shebao" items="${shebaoList}"> 
			  <tr>
			    <td><fmt:formatDate value="${shebao.payTime}"  pattern="yyyy-MM" /> </td>
			    <td>${shebao.payBase} </td>
			    <td>${shebao.payFeedPerson} </td>
			    <td>${shebao.payMedPerson} </td>
			    <td>${shebao.payUmemplyPerson}  </td>

			  </tr>
			  </c:forEach>
			</table>			
			</div>
			</div>
			</c:if>
			
			<c:if test="${not empty alipayList}">
			<div id="containers3" style="min-width:200px;height:400px"></div>
			<div class="bottom-side1">
			<br><h4>支付宝支付列表</h4>
			<div class="order-info">
			<table width="100%" height="118" border="1" cellpadding="0" cellspacing="0" >
			<tr>
			    <th>支付时间</th>
			    <th>支付类型</th>
			    <th>支付金额</th>
			    <th>接收人</th>
				<th>交易状态</th>
			  </tr>
			  <c:forEach var="order" items="${alipayList}"> 
			  <tr>
			    <td>${order.payTime}</td>
			    <td>${order.tradeType}</td>
			    <td>${order.amount}</td>
			    <td>${order.receiverName}</td>
 				<td>${order.status}</td>

			  </tr>
			  </c:forEach>
			</table>			
			</div>
			</div>
			</c:if>
			
			</div>
			</div>

        <br><br><br>
  
    

</body>
<script type="text/javascript">
	var objs =$("#jdJson").val();
	if(objs!=''){
		var emailArray=objs.split("|");
		var allTime = $.trim(emailArray[0]);
		var datas2 = $.trim(emailArray[1]);
		var strJSON = "{categories:"+allTime+"}"; 
		var obj1 = eval( "(" + strJSON + ")" );// 	
		var strJSON2 = "[{name:'JD',data:"+datas2+"}]";  
		var obj21 = eval( "(" + strJSON2 + ")" );// 

		$(function () {
			$('#containers1').highcharts({
	    	        chart: {
	    	            type: 'column'
	    	        },
	    	        title: {
	    	            text: '每个月的消费情况'
	    	        },
	    	        subtitle: {
	    	            text: '来源：jd'
	    	        },
	    	        xAxis:obj1
	    	        ,
	    	        yAxis: {
	    	            min: 0,
	    	            title: {
	    	                text: '人民币 (元)'
	    	            }
	    	        },
	    	        tooltip: {
	    	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	    	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	    	                '<td style="padding:0 10px"><b>￥{point.y:1f} 元</b></td></tr>',
	    	            footerFormat: '</table>',
	    	            shared: true,
	    	            useHTML: true
	    	        },
	        plotOptions: {
	            column: {
	                pointPadding: 0,
	                borderWidth: 0
	            }
	        },
	        series: obj21
	    });
	});	
	}
	
	
	var objs2 =$("#shebaoJson").val();
	if(objs2!=''){
		var emailArray=objs2.split("|");
		var allTime = $.trim(emailArray[0]);
		var datas2 = $.trim(emailArray[1]);
		var strJSON = "{categories:"+allTime+"}"; 
		var obj1 = eval( "(" + strJSON + ")" );// 	
		var strJSON2 = "[{name:'社保',data:"+datas2+"}]";  
		var obj22 = eval( "(" + strJSON2 + ")" );// 

		$(function () {
			$('#containers2').highcharts({
	    	        chart: {
	    	            type: 'column'
	    	        },
	    	        title: {
	    	            text: '每个月的社保情况'
	    	        },
	    	        subtitle: {
	    	            text: '来源：社保'
	    	        },
	    	        xAxis:obj1
	    	        ,
	    	        yAxis: {
	    	            min: 0,
	    	            title: {
	    	                text: '人民币 (元)'
	    	            }
	    	        },
	    	        tooltip: {
	    	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	    	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	    	                '<td style="padding:0 10px"><b>￥{point.y:1f} 元</b></td></tr>',
	    	            footerFormat: '</table>',
	    	            shared: true,
	    	            useHTML: true
	    	        },
	        plotOptions: {
	            column: {
	                pointPadding: 0,
	                borderWidth: 0
	            }
	        },
	        series: obj22 
	    });
	});	
	}
	
	
	var objs3 =$("#alipayJson").val();
	if(objs3!=''){
		var emailArray=objs3.split("|");
		var allTime = $.trim(emailArray[0]);
		var datas2 = $.trim(emailArray[1]);
		var strJSON = "{categories:"+allTime+"}"; 
		var obj1 = eval( "(" + strJSON + ")" );
		var strJSON2 = "[{name:'支付宝',data:"+datas2+"}]";  
		var obj23 = eval( "(" + strJSON2 + ")" );// 

		$(function () {
			$('#containers3').highcharts({
	    	        chart: {
	    	            type: 'column'
	    	        },
	    	        title: {
	    	            text: '每个月的支付情况'
	    	        },
	    	        subtitle: {
	    	            text: '来源：支付宝'
	    	        },
	    	        xAxis:obj1
	    	        ,
	    	        yAxis: {
	    	            min: 0,
	    	            title: {
	    	                text: '人民币 (元)'
	    	            }
	    	        },
	    	        tooltip: {
	    	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	    	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	    	                '<td style="padding:0 10px"><b>￥{point.y:1f} 元</b></td></tr>',
	    	            footerFormat: '</table>',
	    	            shared: true,
	    	            useHTML: true
	    	        },
	        plotOptions: {
	            column: {
	                pointPadding: 0,
	                borderWidth: 0
	            }
	        },
	        series: obj23 
	    });
	});	
	}
	
  </script>
</html>

