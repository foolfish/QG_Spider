<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="/common/domain.jsp" flush="true"/> 
		<title>电商登陆</title>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<link rel="stylesheet" type="text/css" href="/${ctx }/css/base.css">
		<link rel="stylesheet" type="text/css" href="/${ctx }/css/dianshanglogin.css">
		<script type="text/javascript" src="/${ctx }/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/${ctx }/js/common.js"></script>
		<script type="text/javascript" src="/${ctx }/js/formCheck.js"></script>
    </head>
    <body>
    	<div class="base-topbar">
    		<p class="base-topbar-inner">
    			你好，<span id="userName">${loginName}</span>！<a href="/${ctx}/lg/loginout.html">退出</a>
    		</p>
    	</div>
    	<div class="base-main">
    		<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>电商登陆页</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>电商类</h1>
    		</div>
    		<div class="clearfix">
				<form class="base-form base-leftmain" action="" method="post">
					
					<c:if test="${not empty putong_jd_url}">
						<jsp:include page="/ds/jd.jsp" flush="true"/> 
					</c:if>
					
					<c:if test="${not empty putong_taobao_url}">
						<jsp:include page="/ds/taobao.jsp" flush="true"/> 
					</c:if>
					
				
					<div class="base-fieldset base-form-submit">
							<input type="button" value="上一步" id="lastBtn"   class="base-btn w120" trigger="base-btn-hover"/>
							<input id="submitBtn" type="button"  onclick="checkField();return false;" value="下一步" class="base-btn w120 base-nextstep" trigger="base-btn-hover"/>
							
						</div>
						<div id="message" style="display:none;"  align="center">正在登陆中，请稍等...</div>
				</form>	
					
				
				
				<jsp:include page="/common/part.jsp" flush="true"/> 
				</div>
			</div>
    	</div>
		
		<script type="text/javascript">
			$(function(){
				$('#lastBtn').click(function(){
					history.go(-1);
				});
			});
			
		
			 var form = $('.base-form');
			 var formCheck = form.formCheck();
				
function checkField(){ 
	
	
	if($("#putong_jd_hidden").length>0){
		checkJDpre();
	}
	if($("#putong_taobao_hidden").length>0){
		checkTAOBAOpre();
	}
}
function returnURL(){
	var putong_jd_hidden = $("#putong_jd_hidden").val();
	var putong_taobao_hidden = $("#putong_taobao_hidden").val();
	var isSecond = $("#isSecond").val();
	 
	if($("#putong_jd_hidden").length>0 && putong_jd_hidden=='0'){
		return false;
	}else if($("#putong_taobao_hidden").length>0 && putong_taobao_hidden=='0'){
		return false;
	}else if($("#putong_taobao_hidden").length>0 && putong_taobao_hidden=='1' && isSecond=='1'){
		return false;
	}else{
		window.location.href="/${ctx}/entrancePhone.html";
	}
}
				
				$(function(){
			document.onkeydown = function(e){ 
   				 var ev = document.all ? window.event : e;
    			if(ev.keyCode==13) {
           			$('#submitBtn').click();//处理事件

     			}	
			}
		});  		
				

		</script>
    </body>
</html>