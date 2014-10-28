<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>银行账单</title>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<link rel="stylesheet" type="text/css" href="/${ctx }/css/base.css">
    </head>
    <body>
    	<div class="base-topbar">
    		<p class="base-topbar-inner">
    			你好，<span id="userName">${loginName}</span>！<a href="/${ctx}/lg/loginout.html">退出</a>
    		</p>
    	</div>
    	<div class="base-main">
    		<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>银行账单</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>银行账单</h1>
    		</div>
    		<div class="clearfix">
    	
    		<c:if test="${mailafter eq '163.com'}">
				<c:set var="result" value="163"/>
				<c:set var="currentNet" value="163邮箱"/>			
			</c:if>	
			<c:if test="${mailafter eq '126.com'}">
				<c:set var="result" value="126"/>
				<c:set var="currentNet" value="126邮箱"/>		
			</c:if>	
			<c:if test="${mailafter eq 'yeah.net'}">
				<c:set var="result" value="yeah"/>
				<c:set var="currentNet" value="yeah邮箱"/>		
			</c:if>	
			<c:if test="${mailafter eq 'sohu.com'}">
				<c:set var="result" value="sohu"/>
				<c:set var="currentNet" value="sohu邮箱"/>		
			</c:if>	
			<c:if test="${mailafter eq '139.com'}">
				<c:set var="result" value="139"/>
				<c:set var="currentNet" value="139邮箱"/>		
			</c:if>	
			<c:if test="${mailafter eq '21cn.com'}">
				<c:set var="result" value="21cn"/>
				<c:set var="currentNet" value="21cn邮箱"/>		
			</c:if>	
		
				<form class="base-form base-leftmain" action="entranceFourth.html" method="post">
				 <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
					<h2 class="base-form-title">${currentNet}</h2>
					<div class="base-fieldset clearfix">
						<span class="base-fieldset-title">
							<label>邮箱名</label>
						</span>
						<span class="base-form-phone">${email}</span>
						<input type="hidden" name="${result}_userName" id="${result}_userName"  value="${email}" />
					</div>
					<div class="base-fieldset login-fieldset clearfix">
						<span class="base-fieldset-title login-fieldset-title">
							<label>密码</label>
						</span>
						<input type="password" class="w173 base-fieldset-input" checktype="input"  name="${result}_password" id="${result}_password" notnull/>
						<!-- <a class="base-forgetpwd">忘记密码？</a> -->
						<p class="base-alert-error" id="${result}_password_tip">密码不能为空</p>
					</div>
					<div class="base-fieldset base-form-submit">
						<input type="button" value="上一步" id="lastBtn"   class="base-btn w120" trigger="base-btn-hover"/>
						<input id="submitBtn"  value="下一步"  onclick="checkField();return false;" type="submit" class="base-btn w120 base-nextstep" trigger="base-btn-hover"/>
					</div>
					
					<p class="base-from-console" id="message" style="display:none;">
						<span class="base-from-wrd">正在登陆中，请稍等...</span>
					</p>
					
					
				</form>
				<jsp:include page="/common/part.jsp" flush="true"/>

			</div>
    	</div>
    	<script type="text/javascript" src="/${ctx}/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript">
			var form = $('.base-form');
			var formCheck = form.formCheck();
			
			$(function(){
				$('#lastBtn').click(function(){
					history.go(-1);
				});
			});
			var phoneFlag = true;
			
			
			
			function cancel(a){
				$("#"+a+"_div").hide();
				$("#"+a+"_hidden").attr("value","true");
				if(checkAllhidden()==true){
					//shutdown();
				}
				
			}
			

			


			function checkField(){ 
			
				var flag = true;
				
				$("input[id$='_password']").each(function () { 
					var password = this.name; 
					var passwordValue = this.value; 
			        if (passwordValue=='')
					{
						flag = false;
						$("#"+password+"_tip").attr("style","display:block;");
					}else{		
						$("#"+password+"_tip").attr("style","display:none;");
					}    
			            
			    
			        }); 

				if(flag == false){
					return false;
				}
				
				
				
				
				if(flag==true){
					$("#message").attr("style","display:block");
		    		$("#message").show(200,notifyAll);
		    		
					
					return false;
				}
				
			}
			
			
			function checkAllhidden(){
				var flag = true;
				var count = 0;
				$("input[id$='_hidden']").each(function () { 
					var authName = this.name; 
					var value = $("#"+authName).attr("value");
					if(value.indexOf("false")>=0){
						count=count+1;			
						}    
			        });
				if(count>0){
					flag = false;
				}
				return flag;
				
			}
			
			function notifyAll(){
				

				$("input[id$='_userName']").each(function () { 
					var userName = this.name; 
					var pres = userName.split("_userName");
					var pre=pres[0];
		

			        var userNameValue = this.value; 
			        var passwordValue = $("#"+pre+"_password").val();
			      
			
			 
			        var vertifyUrl='/${ctx}/'+pre+'_vertifyLogin.html';
			
			        $.ajax({
					     type:'post',
				         async:false,                                                            
					     url:vertifyUrl,
					     data:{userName:userNameValue,password:passwordValue,random2:Math.random()},
					     success : function(data) {
					    	 	var flag = data.flag;
					    	 	if(flag =='true' && pre.indexOf("_dianxin") < 0){
					    	 	
					    	 	
					    	 		window.location.href="/${ctx}/entranceZhengxin.html";
					    	 		
					    	 		
					    	 	}else{
					    	 		
						    	 	 parseFlag=false;
						    	
						    	 	 			$("#message").text("密码错误");
								    	 	 var url = data.url;
								    	 	 if(url !='none'){
								    	 		$("#"+pre+"_authcode_img").attr("src",'/${ctx}'+url+"?random="+Math.random());
								    			$("#"+pre+"_authcode").show();
								    	 	 }
								    	 	
						    	 	}
						    	 },
					    	 error:function(){
					    		
					    		 parseFlag=false;
					    	 }
					     });
			        
			        
			        });
				
			}


			
			
		</script>
    </body>
</html>