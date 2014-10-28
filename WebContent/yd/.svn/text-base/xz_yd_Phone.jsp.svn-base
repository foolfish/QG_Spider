<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>
<style>
.error-tip {
display: inline-block;
margin-left: 10px;
color: #F71717;
background: #FFEBEB;
border: 1px solid #ffbdbe;
padding: 4px;
border-radius: 3px;
display:none;
}
</style>
	
	
	
	
					<c:set var="currentNet" value="中国移动"/>
        			<c:set var="url" value="${putong_xz_yidong_url}"/>
        			<c:set var="result" value="putong_xz_yidong"/>
        			<form id="form1" class="base-form" action="">
        					<input type="hidden" name="resultCode" id="result_xzyd_code" value="0"/>
							<input type="hidden" name="imgcode" id="imgcode_xzyd" value=""/>
							
						    <input type="hidden" name="putong_xz_yidong_userName1" id="putong_xz_yidong_userName1" value=""/>
						<h2 class="base-form-title">中国移动：</h2>
						
						  <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
						
						<div class="base-fieldset clearfix">
							<span class="base-fieldset-title">
								<label>手机号</label>
							</span>
							<span class="base-form-phone">
								${phone}
								<input type="hidden" name="${result}_userName" id="${result}_userName"  value="${phone}" />
							</span>
						</div>
						
						<div style="padding-left: 120px">
							<label id="${result}_password_tip" class="error-tip" >请输入密码</label>
							<label id="password_tip" class="error-tip" >请输入密码</label>
						</div>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>服务密码</label>
							</span>
							<input class="base-fieldset-input w173" name="password" id="password" size="30" type="password"/>
							<a href="https://nm.ac.10086.cn/" target="_blank" class="base-forgetpwd">忘记密码</a>
						</div>
						
						<div style="padding-left: 120px">
						<label id="authcode_tip" class="error-tip" >请输入验证码</label>
						<label id="authcode_message" class="error-tip" >验证码错误</label>
						</div>
						<div class="base-fieldset clearfix"  id="${result}_authcode" 
				<c:if test="${url ne 'none'}">style="display:block;"</c:if>>
							<span class="base-fieldset-title">
								<label>验证码</label>
							</span>
							
							<input class="base-fieldset-input w173" name="authCode" id="authCode" type="text"/>
		  					<label class="base-form-checkimglab">
							<img  onclick="checkAuth(1)" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="putong_xz_yidong_authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"/>
						
		  					</label>
		  					<label class="base-form-changelab">
		  						看不清？<a class="" href="javascript:checkAuth(1);">换一张</a>
		  					</label>
						</div>
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn"  type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" onclick="checkField();return false;" trigger="base-btn-hover"/>
						</div>
						<!-- <p class="base-from-console" id="message" style="display: none">
							<span class="base-from-wrd">正在加载验证码，请稍等...</span>
						</p> -->				
						<p class="base-from-console">
							<span class="base-from-wrd" id="message" style="display:none;">正在登陆中，请稍等。。。</span>
						</p>
					</form>
					<form id="form2" class="base-form base-element-hidden" action="">
						<input  name="isSecond" id="isSecond" type="hidden"/>
					    <input  name="spid" id="spid" type="hidden"/>
					    <input type="hidden" name="msgMonthIndex" id="msgMonthIndex" value=""/>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>手机验证码</label>
							</span>
							<input name="smsCode" id="smsCode"  class="base-fieldset-input w173"/>
							<a href="javascript:void(0);" class="base-forgetpwd" onclick="getydDynpass();">获取动态密码</a>	
						</div>
					
						
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn" type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" trigger="base-btn-hover" onclick="checkField();return false;"/>
						</div>
						<p class="base-from-console">
							<span class="base-from-wrd" id="_dongtai_password_message_tip" style="display:none;"></span>
							<span class="base-from-wrd" id="error_message" style="display:none;">页面错误,请刷新重试</span>
						</p>
					</form>
					
					
</div>        			
<script type="text/javascript">


$(document).ready(function(){
	//如果值不等于空就默认初始化代码
	if("${phone}"!=""){
		checkAuth('');
	}
});

/*检查是否需要展示验证码*/
function checkAuth(isReAuthcode){
	var timestamp=new Date().getTime();
	var phone = '${phone}';
	$.ajax({
		     type:'post',
	         async:false,
		     url:'/${ctx}/putong_xz_yidong_GetAuth.html',
		     data:{random2:Math.random(),"phone":phone,"isReAuthcode":isReAuthcode},
		     success : function(data) {
		    	
		    	 if(data.url!="none"){
			    	  var url = data.url+"?time="+timestamp;
			    	  $('#putong_xz_yidong_authcode_img').attr("src","${imgPath}/img/authcode/"+url);
			    }
		    	
		    }, 
		   error:function(){	
	
			}
		   });
		
	
}



function changeAuth(){
	$("#putong_xz_yidong_authcode_img").attr("src",$("#imgcode_xzyd").val()+"&time="+new Date().getTime());	
}


	
	function getydDynpass(){
		var flag = true; 
		if(flag == true){
			var telno = "${phone}";
		
			
			$.ajax({
			     type:'post',
		         async:false,
			     url:'/${ctx}/xz_yidong_sendCard.html',
			     data:{phone:telno,random2:Math.random()},
			     success : function(data) {
					var message='';
				       if(data.flag=='true'){
				    	   message=data.message;
						}else{
						   message=data.message;
				
					   }
			        
			          $('#_dongtai_password_message_tip').text(message).show();
			          setTimeout("hide_()", 5000 );
			    	 
			    	 },
			    	 error:function(){					    		
					     }
			     });	
		}
	}
	
	function hide_(){
		$('#_dongtai_password_message_tip').hide();
	}

	
	function checkFwmm(type){
		if(type==1){
			var servicepw = $('#dongtai_xz_yidong_password').val();
			if (servicepw.length != 6) {
				$("#dongtai_xz_yidong_password_tip").text('请输入正确的6位动态密码！').show();		
				return false;
			} else if (isNaN(servicepw)) {
				$("#dongtai_xz_yidong_password_tip").text('您输入的动态密码不正确！').show();		
				return false;
			}
			return true;
		}else if(type==2){
			var servicepw = $('#fuwu_xz_yidong_password').val();
			if (servicepw.length != 6) {
				$("#fuwu_xz_yidong_password_tip").text('请输入正确的6位服务密码！').show();		
				return false;
			} else if (isNaN(servicepw)) {
				$("#fuwu_xz_yidong_password_tip").text('您输入的服务密码不正确！').show();		
				return false;
			}
			return true;
		}
		
	}
	

	function checkField(){ 
		var flag = true;
		if(flag==true){
			 var isSecond = $("#isSecond").val();
			 $("#message").hide();
			if(isSecond!=null && isSecond=="1"){
				//alert(1);
				 $("#message").show();
				$("#message").text("正在努力登录中，请稍等……");
				$("#message").show("500",secondSubmit);
				//secondSubmit();
			}else{
				//alert(2);
				$("#message").text("正在努力登录中，请稍等……");
				$("#message").show(500,firstSubmit);
			}
			
			
			return false;
		}
	}
	
	function firstSubmit(){

		var vertifyUrl='/${ctx}/fuwu_xz_yidong_vertifyLogin.html';
		var phoneValue = '${phone}';
	    var passwordValue = $("#password").val();
	    var authCode = $("#authCode").val();
	    
	    
	     $("#password_tip").hide();
	     $("#authcode_tip").hide();
	     $("#authcode_message").hide();
	     $("#message").text("");
	    if(passwordValue==''||passwordValue== undefined){
	        $("#password_tip").show();
	    	return false;
	    }              
	    var style = $("#putong_xz_yidong_authcode").attr("style");
	    if(style!='display:none;'){
	    	 if (authCode==''||authCode== undefined){
	 	        $("#authcode_tip").show();
	 	    	return false;
	 	    }
	    }
	  
	    $("#message").attr("style","dislpay:block;");
	    $.ajax({
		     type:'post',
	         async:false,                                                            
		     url:vertifyUrl,	     
		     data:{loginName:phoneValue,password:passwordValue,"resultCode":1,authcode:authCode},
		     success : function(data) {
		    	 $("#message").hide();
		    	// alert(data.flag)
		    	 if(data.flag=='true'){
			    	// alert(1);
				    	 $("#form2").show();
				    	  $("#form1").hide();
				    	  $("#isSecond").val("1");
				    	
				    	 
				    }else{
				    	var message = data.message;
				    	checkAuth('1');
				    	$("#putong_xz_yidong_authcode").show();
						$("#message").text(message).show();
					}
		    	},
		    	 error:function(){		    		
		    		 parseFlag=false;
		    	 }
		     });
		
	}
	function secondSubmit(){
		var vertifyUrl='/${ctx}/dongtai_xz_yidong_vertifyLogin.html';
		var smsCode = $("#smsCode").val();
	
		
	    var phone = '${phone}';
	 
		 $("#error_message").hide();
		 if(smsCode==''||smsCode== undefined){
			 $("#error_message").text('手机验证码不能为空！').show();
	 	     return false;
	    } 
		
		$.ajax({
		     type:'post',
	         async:false,                                                            
		     url:vertifyUrl,
		     data:{dtpassword:smsCode,phone:phone},
		     success : function(data) {
		    	 $("#message").hide();
		    	 if(data.flag=='true'){
					 window.location.href="/${ctx}/entranceEmail.html";
				 }else{
					 $("#_dongtai_password_message_tip").text(data.message);
		    		 $("#_dongtai_password_message_tip").show();
		    	}
				  
		    	},
		    	 error:function(){
		    		$("#message").show();
		    		$("#message").text(data.reMsg);
		    		
		    	 }
		     });
	}
	
			
</script>
</html>