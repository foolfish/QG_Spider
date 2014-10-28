<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="/common/domain.jsp" flush="true"/> 
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<title>中国人民银行征信中心登录</title>
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
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>中国人民银行征信中心登录</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>中国人民银行征信中心登录</h1>
    		</div>
    		<div class="clearfix">


         <c:set var="chaxunma" value="查询码"/>
          <c:set var="result" value="zhengxin"/>
         <c:set var="url" value="${zhengxin_url}"></c:set>

				<form class="base-form base-leftmain" action="">
	            <input type="hidden" name="imgcode" id="imgcode" value=""/>
				<input type="hidden" name="resultCode" id="result_zhengxin_code" value="0"/>
				<p class="base-login-alert">如无账号，请先到<a href="https://ipcrs.pbccrc.org.cn/"  target="_blank">征信网站</a>注册</p>
					<div class="base-fieldset clearfix">
						<span class="base-fieldset-title">
							<label>登录名</label>
						</span>
						<input needcheck="userName2" checktype="input"  class="base-fieldset-input" name="${result}_userName" id="${result}_userName" value="${zhengxin}" onblur="checkAuth();" notnull/>
						<p class="base-alert-error" id="${result}_userName_tip">用户名不能为空</p>
						<input type="hidden" name="zhengxin_userName1" id="zhengxin_userName1" value=""/>
					</div>
					<div class="base-fieldset login-fieldset clearfix">
						<span class="base-fieldset-title login-fieldset-title">
							<label>密码</label>
						</span>
						<input type="password"  checktype="input"  class="base-fieldset-input" name="zhengxin_password" id="zhengxin_password"  notnull/>
						<a class="base-forgetpwd" href="https://ipcrs.pbccrc.org.cn/"  target="_blank">忘记密码？</a>
						<p class="base-alert-error" id="zhengxin_password_tip">密码不能为空</p>
					</div>
					<div class="base-fieldset login-fieldset clearfix">
						<span class="base-fieldset-title login-fieldset-title">
							<label>查询码</label>
						</span>
						<input  name="zhengxin_chaxunma_password"  class="base-fieldset-input" id="zhengxin_chaxunma_password" checktype="input" notnull/>
						<p class="base-alert-error" id="zhengxin_chaxun_password_tip">查询码不能为空</p>
					</div>
				
					
					<div class="base-fieldset clearfix" id="${result}_authcode" <c:if test="${url eq 'none'}">style="display:none;"</c:if>  >
						<span class="base-fieldset-title">
							<label>验证码</label>
						</span>
						<input  class="w173 base-fieldset-input" needcheck="checkNum"  name="${result}_authcode_Name" id="${result}_authcode_Name" checktype="input" notnull/>
	  					<label class="base-form-checkimglab">
	  						<img src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="${result}_authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"  onclick="changeAuth()"/>
	  					</label>
	  					<label class="base-form-changelab">
	  						看不清？<a class="" href="javascript:changeAuth();">换一张</a>
	  					</label>
						<p class="base-alert-error" id="${result}_authcode_tip">验证码不能为空</p>
					</div>
					<div class="base-fieldset base-form-submit">
						<input type="button" value="上一步" class="base-btn w120"  id="lastBtn"  trigger="base-btn-hover"/>
						<input id="submitBtn" onclick="checkField();return false;" type="submit" value="下一步" class="base-btn w120 base-nextstep" trigger="base-btn-hover"/>
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
			clearForm();
			var form = $('.base-form');
			var formCheck = form.formCheck();
			
			$(document).ready(function(){
				//如果值不等于空就默认初始化代码
				if("${zhengxin }"!=""){
					checkAuth();
				}
			});
			/*检查是否需要展示验证码*/
			
			var imgurl = "";
			function checkAuth(){
				var timestamp=new Date().getTime();
				var userNameValue = $("#zhengxin_userName").val();
				var userName1 = $("#zhengxin_userName1").val();
				if(userNameValue != userName1){	
					$("#zhengxin_userName1").val(userNameValue);
					if(userNameValue.length>3){
					$.ajax({
					     type:'post',
				         async:true,
					     url:'/${ctx}/putongZxFirst.html',
					     data:{random2:Math.random(),"loginName":userNameValue},
					     success : function(data) {
					    	 if(data.url!="none"){
						    	  var url = data.url;
						    	  imgurl = url;
						          changeAuth();
					    	 }
					    
					    }, 
					    error:function(){	
						  
						 }
					   });
					}
				}
			}
			function changeAuth(){
				var id = "${result}_authcode";
			    $('#'+id).show();
				$('#zhengxin_authcode_img').attr("src",imgurl+"?l="+new Date().getTime());				         
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
		if(checkAllhidden()==true){
			//shutdown();
		}
		
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
				flag = true;
				$("#"+userName+"_tip").attr("style","display:none;");
			}    
	            
	        });
		if(flag == false){
			return false;
		}
		
	
			var passwordValue = $("#zhengxin_password").val();
	        if (passwordValue=='')
			{

				flag = false;
				$("#zhengxin_password_tip").attr("style","display:block;");
			}else{
				flag = true;
				$("#zhengxin_password_tip").attr("style","display:none;");

			}    
	    
		if(flag == false){
			return false;
		}
		var chaxunma = $("#zhengxin_chaxunma_password").val();
		if(chaxunma == ''){
			flag = false;
			$("#zhengxin_chaxun_password_tip").attr("style","display:block;");
		}else{
			$("#zhengxin_chaxun_password_tip").attr("style","display:none;");
		}
		

		if(flag == false){
			return false;
		}
		
	
	var authcode= $('#zhengxin_authcode_Name').val();
	if (authcode=='')
		{
			flag = false;
			$("#zhengxin_authcode_tip").attr("style","display:block;");
		}else{
			$("#zhengxin_authcode_tip").attr("style","display:none;");
		}


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
	      
	        var chaxumaValue = $("#"+pre+"_chaxunma_password").val();
	        var authcodeValue = $("#"+pre+"_authcode_Name").val();
	        
	     
	        
	        var vertifyUrl='/${ctx}/'+pre+'_vertifyLogin.html';
	        $.ajax({
			     type:'post',
		         async:true,                                                            
			     url:vertifyUrl,
			     data:{loginName:userNameValue,password:passwordValue,"spassword":chaxumaValue,"resultCode":1,authcode:authcodeValue,random2:Math.random()},
			     success : function(data) {
			    	 	var flag = data.state;
			    	 	if(flag){
								window.location.href="/${ctx}/entranceShebao.html";
			    	 	}else{			    	 		
				    	 	 	 parseFlag=false;
				    	 		 $("#message").text(data.msg);				    	 			 
				    	 		 changeAuth();  
				    	 		
				    	 	}
				    	 },
			    	 error:function(){
			    		
			    		 parseFlag=false;
			    	 }
			     });
	        
	        
	        });
	
	}

	function clearForm(){
		$("#zhengxin_userName").val("");
		$("#zhengxin_chaxunma_password").val("");
	}

		</script>
    </body>
</html>