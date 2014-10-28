<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>社保登录</title>
<jsp:include page="/common/domain.jsp" flush="true"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

		<c:if test="${not empty shanghai_shebao_url}">
	        <c:set var="currentNet" value="上海社保"/>
	        <c:set var="url" value="${shanghai_shebao_url}"/>    
	        <c:set var="yonghuming" value="身份证号"/>   
	        <c:set var="result" value="shanghai_shebao"/>  
	        
	        	<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>${currentNet}登录</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>${currentNet}登录</h1>
    		</div>
    		<div class="clearfix">
				<form class="base-form base-leftmain" action="">
				<p class="base-login-alert">如无账号，请先到<a href="http://www.12333sh.gov.cn/200912333/2009wsbs/grbs/shbx/01/200909/t20090917_1085042.shtml"  target="_blank">上海市人力资源和社会保障局</a>获取密码</p>
				
					<div class="base-fieldset clearfix">
						<span class="base-fieldset-title">
							<label>身份证</label>
						</span>
						<input  name="${result}_userName" id="${result}_userName" class="base-fieldset-input" checktype="input" value="${shebao}" notnull/>
						<p class="base-alert-error" id="${result}_userName_tip">${yonghuming}不能为空</p>
					</div>
					<div class="base-fieldset login-fieldset clearfix">
						<span class="base-fieldset-title login-fieldset-title">
							<label>密码</label>
						</span>
						<input type="password" needcheck  name="${result}_password" class="base-fieldset-input" id="${result}_password" checktype="input" notnull/>
						<!--<a class="base-forgetpwd">忘记密码？</a>-->
						<p class="base-alert-error" id="${result}_password_tip">密码不能为空</p>
					</div>
					
			
			<%-- 		<div class="base-fieldset clearfix" id="${result}_authcode"  name="${result}_authcode"  <c:if test="${url eq 'none'}">style="display:none;"</c:if>
					<c:if test="${url ne 'none'}">style="display:block;"</c:if> >
						<span class="base-fieldset-title">
							<label>验证码</label>
						</span>
						<input name="authcode_Name" id="authcode_Name"  class="w173 base-fieldset-input" needcheck="checkNum" checktype="input" notnull/>
	  					<label class="base-form-checkimglab">
	  						<img src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="${result}_authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"  onclick="changeAuth('${result}')"/>
	  					</label>
	  					<label class="base-form-changelab">
	  						看不清？<a class="" href="javascript:changeAuth('${result}');">换一张</a>
	  					</label>
	  					<p class="base-alert-error" id="authcode_Name_tip">验证码不能为空</p>
					</div> --%>
					<div class="base-fieldset base-form-submit">
						<input type="button" value="上一步" id="lastBtn" class="base-btn w120" trigger="base-btn-hover"/>
						<input id="submitBtn"  value="下一步"  onclick="checkField();return false;" type="submit"  class="base-btn w120 base-nextstep" trigger="base-btn-hover"/>
					</div>
					<p class="base-from-console" id="message" style="display:none;">
						<span class="base-from-wrd">正在登陆中，请稍等...</span>
					</p>
				</form>
				<jsp:include page="/common/part.jsp" flush="true"/>
			</div>
			
			
        </c:if>	
        
 
         
        <c:if test="${not empty shenzhen_shebao_url}">
	        <c:set var="currentNet" value="深圳社保"/>
	        <c:set var="url" value="${shenzhen_shebao_url}"/> 
	        <c:set var="yonghuming" value="身份证号"/>   
	        <c:set var="result" value="shenzhen_shebao"/>     
	        
	        	<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>${currentNet}登录</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>${currentNet}登录</h1>
    		</div>
    		<div class="clearfix">
				<form class="base-form base-leftmain" action="">
				<p class="base-login-alert">如无账号，请先到<a href="https://e.szsi.gov.cn/siservice/#"  target="_blank">深圳市人力社会保险个人网页（请使用IE打开）</a>注册</p>
					<div class="base-fieldset clearfix">
						<span class="base-fieldset-title">
							<label>身份证</label>
						</span>
						<input  name="shenzhen_shebao_userName" id="${result}_userName" class="base-fieldset-input" checktype="input" value="${shebao}" onblur="checkInitAuth();" notnull/>
						<p class="base-alert-error" id="${result}_userName_tip">${yonghuming}不能为空</p>
						<input type="hidden" name="shenzhen_shebao_userName1" id="shenzhen_shebao_userName1" value=""/>
					</div>
					<div class="base-fieldset login-fieldset clearfix">
						<span class="base-fieldset-title login-fieldset-title">
							<label>密码</label>
						</span>
						<input type="password" needcheck  name="${result}_password" class="base-fieldset-input" id="${result}_password" checktype="input" notnull/>
						<!--<a class="base-forgetpwd">忘记密码？</a>-->
						<p class="base-alert-error" id="${result}_password_tip">密码不能为空</p>
					</div>
					
			
					<div class="base-fieldset clearfix" id="shenzhen_shebao_authcode"  name="shenzhen_shebao_authcode"  <c:if test="${url eq 'none'}">style="display:none;"</c:if>
					<c:if test="${url ne 'none'}">style="display:block;"</c:if> >
						<span class="base-fieldset-title">
							<label>验证码</label>
						</span>
						<input name="authcode_Name" id="authcode_Name"  class="w173 base-fieldset-input" needcheck="checkNum" checktype="input" notnull/>
	  					<label class="base-form-checkimglab">
	  						<img src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="shenzhen_shebao_authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"  onclick="changeAuth()"/>
	  					</label>
	  					<label class="base-form-changelab">
	  						看不清？<a class="" href="javascript:changeAuth();">换一张</a>
	  					</label>
	  					<p class="base-alert-error" id="authcode_Name_tip">验证码不能为空</p>
					</div>
					<div class="base-fieldset base-form-submit">
						<input type="button" value="上一步" id="lastBtn" class="base-btn w120" trigger="base-btn-hover"/>
						<input id="submitBtn"  value="下一步"  onclick="checkField();return false;" type="submit"  class="base-btn w120 base-nextstep" trigger="base-btn-hover"/>
					</div>
					<p class="base-from-console" id="message" style="display:none;">
						<span class="base-from-wrd">正在登陆中，请稍等...</span>
					</p>
				</form>
				<jsp:include page="/common/part.jsp" flush="true"/>
			</div>
			
			 
        </c:if>
      

    	
    	</div>
    	<script type="text/javascript" src="/${ctx}/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript">
			var form = $('.base-form');
			var formCheck = form.formCheck();
			$(document).ready(function(){
				//如果值不等于空就默认初始化代码
				if("${shebao}"!=""){
					checkInitAuth();
				}else{
					if($("#shenzhen_shebao_userName").length>0){
						$("#shenzhen_shebao_userName").val("");
						$("#authcode_Name").val("");
					}else if($("#shanghai_shebao_userName").length>0){
						$("#shanghai_shebao_userName").val("");
					}
					
				}
			});
			var imgurl ="";
			function checkInitAuth(){
				var timestamp=new Date().getTime();
				var userNameValue = $("#shenzhen_shebao_userName").val();
				var userName1 = $("#shenzhen_shebao_userName1").val();
				if(userNameValue != userName1){	
					$("#shenzhen_shebao_userName1").val(userNameValue);
					if(userNameValue.length>3){
					$.ajax({
					     type:'post',
				         async:true,
					     url:'/${ctx}/shebao/shenzhen/first.html',
					     data:{random2:Math.random(),"loginName":userNameValue},
					     success : function(data) {
					    	 if(data.url!="none"){
						    	 	imgurl = data.url;
						    	 	changeAuth();
					    	 }
					    	 	
					    }, 
					   error:function(){	
						   $("#message").text("系統错误，请重试！");
			    		
							 }
					   });
					}
				}
			}
			
		function changeAuth(){
			 $('#shenzhen_shebao_authcode').show();
			 $('#shenzhen_shebao_authcode_img').attr("src",imgurl+"?l="+new Date().getTime());
		}

	$(function(){
		$('#lastBtn').click(function(){
			history.go(-1);
		});
	});
	var phoneFlag = true;
	
	
	
	function cancel(a){
		$("#"+a+"_div").hide();
		$("#"+a+"_hidden").attr("value","true");
		
		
	}
	
	
	
	


	function checkField(){ 
	
		var flag = true;
		$("input[id$='_userName']").each(function () { 
			var userName = this.name; 
	        var userValue = this.value; 
	        
	        var pres = userName.split("_userName");
			var pre=pres[0];
			$("#"+pre+"_message").hide();
			
	       
	         if (userValue=='')
					{
						flag = false;
						$("#"+userName+"_tip").attr("style","display:block;");
					}else{			
						$("#"+userName+"_tip").attr("style","display:none;");
					}    
			            
			        });
		if(flag == false){
			return false;
		}

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
		
		
		
		$("div[id$='_authcode']").each(function () { 
			var jd_style = $(this).attr("style");
			var authcode="";
			if(jd_style.indexOf("none")==-1){
				authcode= $('#authcode_Name').val();
				if (authcode=='')
				{
			
					flag = false;
					$("#authcode_Name_tip").attr("style","display:block;");
				}else{
					$("#authcode_Name_tip").attr("style","display:none;");
				}
				
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
	
	
	function notifyAll(){
		

		$("input[id$='_userName']").each(function () { 
			var userName = this.name; 
			var pres = userName.split("_userName");
			var pre=pres[0];

	        var userNameValue = this.value; 
	        var passwordValue = $("#"+pre+"_password").val();

	        var authcodeValue = $("#authcode_Name").val();
	        
	        
	        var vertifyUrl='/${ctx}/'+pre+'_vertifyLogin.html';
	
	        if(pre=='shenzhen_shebao'){
	        	vertifyUrl='/${ctx}/shebao/shenzhen/vertifyLogin.html'; 
	        }
	        
	        $.ajax({
			     type:'post',
		         async:false,                                                            
			     url:vertifyUrl,
			     data:{"loginName":userNameValue,password:passwordValue,authcode:authcodeValue,random2:Math.random()},
			     success : function(data) {
			    	 	var flag = data.state;
			    	 	if(flag){
			    	 	
			    			window.location.href="/${ctx}/entranceSucessful.html";
			    	 		
			    	 		
			    	 	}else{
			    	 			var msg = data.msg;
			    	 			parseFlag=false;
			    	 	
			    	 			  $("#message").text(msg);
					    	 	 var url = data.url;
					    	 	 if(url !='none'){
					    	 		imgurl = data.url;
						    	 	changeAuth();
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