<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>网站登录</title>
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

	
    
         
          <c:set var="result" value="xuexin"/>
          <c:set var="url" value="${xuexin_url}"></c:set>

    	<div class="base-main">
    		<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>学信网登录</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>学信网登录</h1>
    	
    		</div>
    		<div class="clearfix">
    		 <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
    		 	<p class="base-from-console">
					<span class="base-from-wrd dianshang-console" id="${result}_message" style="display:none;" >用户名或密码错误，请重新填写</span>
				</p>
				<form class="base-form base-leftmain"  action="entranceFourth.html" method="post" >
				<input type="hidden" name="resultCode" id="result_xuexin_code" value="0"/>
				<input type="hidden" name="imgcode" id="imgcode_xuexin" value=""/>
				
				<p class="base-login-alert">如无账号，请先到<a href="http://my.chsi.com.cn/archive/index.jsp"  target="_blank">学信网站</a>注册</p>
					<div class="base-fieldset clearfix">
						<span class="base-fieldset-title">
							<label>用户名</label>
						</span>
						<input needcheck="userName2" checktype="input"  class="base-fieldset-input" name="${result}_userName" id="${result}_userName" notnull value="${xuexin}" onblur="checkAuth();"/>
						<p class="base-alert-error" id="${result}_userName_tip">用户名不能为空</p>
						<input type="hidden" name="xuexin_userName1" id="xuexin_userName1" value=""/>
					</div>
					<div class="base-fieldset login-fieldset clearfix">
						<span class="base-fieldset-title login-fieldset-title">
							<label>密码</label>
						</span>
						<input type="password" checktype="input"   class="base-fieldset-input" name="${result}_password" id="${result}_password" notnull/>
						<a href="http://my.chsi.com.cn/archive/index.jsp" class="base-forgetpwd" target="_blank">忘记密码？</a>
						<p class="base-alert-error" id="${result}_password_tip">密码不能为空</p>
					</div>
					<div class="base-fieldset clearfix" id="${result}_authcode" <c:if test="${url eq 'none'}">style="display:none;"</c:if>  >
						<span class="base-fieldset-title">
							<label>验证码</label>
						</span>
						<input  class="w173 base-fieldset-input" needcheck="checkNum"  name="${result}_authcode_Name" id="${result}_authcode_Name" checktype="input" notnull/>
	  					<label class="base-form-checkimglab">
	  						<img src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="${result}_authcode_img" style="height:60px;width:180px;position: relative;top: 0px;"  onclick="changeAuth()"/>
	  					</label>
	  					<label class="base-form-changelab">
	  						看不清？<a class="" href="javascript:changeAuth();">换一张</a>
	  					</label>
						<p class="base-alert-error" id="${result}_authcode_tip">验证码不能为空</p>
					</div>
					
					<div class="base-fieldset base-form-submit">
						<input type="button" value="上一步" id="lastBtn"  class="base-btn w120" trigger="base-btn-hover"/>
						<input id="submitBtn" value="下一步" onclick="checkField();return false;" type="submit" class="base-btn w120 base-nextstep" trigger="base-btn-hover"/>
					</div>
					
					<p class="base-from-console" id="message" style="display:none;">
						<span class="base-from-wrd">正在登陆中，请稍等...</span>
					</p>
				</form>
				<input type="hidden" name="imgcode" id="imgcode" value=""/>
				<jsp:include page="/common/part.jsp" flush="true"/>
			</div>
    	</div>
    	<script type="text/javascript" src="/${ctx}/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript">
			var form = $('.base-form');
			var formCheck = form.formCheck();
		
			$(document).ready(function(){
				//如果值不等于空就默认初始化代码
				if("${xuexin }"!=""){
					checkAuth();
				}
			});
			//是不是在未异步执行完之前点击下一步了
			var sub_jd_code = true;
			/*检查是否需要展示验证码*/
			
			var imgurl = "";
			function checkAuth(){
				var timestamp=new Date().getTime();
				var userNameValue = $("#xuexin_userName").val();
				var userName1 = $("#xuexin_userName1").val();
				if(userNameValue != userName1){	
					$("#xuexin_userName1").val(userNameValue);
					if(userNameValue.length>3){
					$.ajax({
					     type:'post',
				         async:false,
					     url:'/${ctx}/putongXxFirst.html',
					     data:{random2:Math.random(),"loginName":userNameValue},
					     success : function(data) {
					    	 if(data.url!="none"){
					    	 	 imgurl = data.url;
						    	 changeAuth();
					    	 }
					    	 $("#result_xuexin_code").val("1");
				    			if(!sub_jd_code){
				    				checkField();
				    			}
					    }, 
					   error:function(){	
						   $("#result_xuexin_code").val("1");
			    			if(!sub_jd_code){
			    				checkField();
			    			}
							 }
					   });
					}
				}
			}
			
			function changeAuth(){
			var id = "${result}_authcode";
				$("#"+id).show();
				$("#xuexin_authcode_img").attr("src",imgurl+"?time="+new Date().getTime());
				
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
				$("#message").html("<span class='base-from-wrd'>正在登陆中，请稍等...</span>");
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
						var pres = password.split("_password");
						var pre=pres[0];
					
						$("#"+password+"_tip").attr("style","display:none;");
					}    
			            
			        }); 

				
				
				if(flag == false){
					return false;
				}
				
				
				
				$("input[id$='_authcode']").each(function () { 
					var authName = this.name; 
					var jd_style = $("#"+authName).attr("style");
					var authcode="";
					if(jd_style.indexOf("none")==-1){
						authcode= $('#'+authName+'_Name').val();
						if (authcode=='')
						{
							flag = false;
							$("#"+authName+"_tip").attr("style","display:block;");
						}else{
							$("#"+authName+"_tip").attr("style","display:none;");
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
			     
			        var authcodeValue = $("#"+pre+"_authcode_Name").val();
			        
			     
			        
			        var vertifyUrl='/${ctx}/'+pre+'_vertifyLogin.html';
			        $.ajax({
					     type:'post',
				         async:false,                                                            
					     url:vertifyUrl,
					     data:{loginName:userNameValue,password:passwordValue,"resultCode":1,authcode:authcodeValue,random2:Math.random()},
					     success : function(data) {
					    	 	var flag = data.state;
					    	 	if(flag){
					    	 		window.location.href="/${ctx}/entranceEmail.html";
					    	 		
					    	 	}else{			    	 		
						    	 	 parseFlag=false;
						    	 	$("#message").text("用户名或密码错误");
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