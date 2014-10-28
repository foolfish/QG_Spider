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
					
					<c:set var="currentNet" value="中国电信"/>
        			<c:set var="url" value="${putong_sh_dianxin_url}"/>
        			<c:set var="result" value="fuwu_sh_dianxin"/>

        			<form id="form1" class="base-form" action="">
						<h2 class="base-form-title">中国电信：</h2>
						
						  <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
						
						<div class="base-fieldset clearfix">
							<span class="base-fieldset-title">
								<label>手机号</label>
							</span>
							<span class="base-form-phone">
								${phone}
								<input type="hidden" name="phone" id="phone"  value="${phone}" />
							</span>
						</div>
						<div style="padding-left: 120px">
							<label id="${result}_message" class="error-tip" >密码错误，请重新填写</label>
							<label id="password_tip" class="error-tip" >请输入密码</label>
						</div>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>服务密码</label>
							</span>
							<input class="base-fieldset-input w173" name="password" id="password" size="30" type="password"/>
							<a class="base-forgetpwd" href="http://sh.189.cn/service/AccountManageAction.do?method=init&quickChannel=mobileResetPassWord" target="_blank">忘记密码</a>
						</div>
							<div style="padding-left: 120px">
						<label id="authcode_tip" class="error-tip" >请输入验证码</label>
						<label id="authcode_message" class="error-tip" >密码或验证码错误</label>
						</div>
						<div class="base-fieldset clearfix">
							<span class="base-fieldset-title">
								<label>验证码</label>
							</span>
							
							<input class="base-fieldset-input w173" name="authCode" id="authCode" type="text"/>
		  					<label class="base-form-checkimglab">
							<img  onclick="getAuth()" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"/>
		  					</label>
		  					<label class="base-form-changelab">
		  						看不清？<a class="" href="javascript:getAuth();">换一张</a>
		  					</label>
						</div>
						
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn"  type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" onclick="checkField();return false;" trigger="base-btn-hover"/>
						</div>
						<p class="base-from-console">
							<span class="base-from-wrd" id="message" style="display:none;">正在登陆中，请稍等。。。</span>
						</p>
					</form>
					<form id="form2" class="base-form base-element-hidden" action="">
					<input  name="isSecond" id="isSecond" type="hidden"/>
				<input  name="spid" id="spid" type="hidden"/>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>请输入计算结果</label>
							</span>
							
							<input class='base-fieldset-input w173' name='sh_dianxin_authcode_Name' id='sh_dianxin_authcode_Name' type='text' />
							<img src="" id='dianxin_authcode_img' style='height:30px;width:60px;position: relative;top: 8px;'    onclick='getAuth2()'/>
						</div>

						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>手机验证码</label>
							</span>
							<input name="smsCode" id="smsCode"  class="base-fieldset-input w173"/>
							<a href="javascript:void(0);" class="base-forgetpwd" onclick="ss();">获取动态密码</a>	
						</div>
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn" type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" trigger="base-btn-hover" onclick="checkField();return false;"/>
						</div>
						<p class="base-from-console">
							<span class="base-from-wrd" id="message" style="display:none;">正在加载动态密码，请稍等...</span>
							<span class="base-from-wrd" id="error_message" style="display:none;">页面错误,请刷新重试</span>
						</p>
					</form>
					</div>
<script type="text/javascript">

$(document).ready(function(){
	$("#isSecond").val("");
	getAuth();
});

function getAuth(){
	var phoneValue = $("#phone").val();
	$.ajax({
	     type:'post',
         async:false,
	     url:'/${ctx}/putong_sh_dx_GetAuth.html',
	     data:{userName:phoneValue},
	     success : function(data) {
	    	 	var url = data.url;
	    	 	if(url != 'none'){				    	
	    	 		$("#authcode_img").attr("src",'${imgPath}'+url+"?l="+new Date().getTime());
	    		}
	    	 },
  	 error:function(){					    		
			     }
	     });
	
}


function getAuth2(){
	var phoneValue = $("#phone").val();
	$.ajax({
	     type:'post',
         async:false,
	     url:'/${ctx}/putong_sh_dx_smsimg.html',
	     data:{userName:phoneValue},
	     success : function(data) {
	    	 	var url = data.url;
	    	 	if(url != 'none'){				    	
	    	 		$("#dianxin_authcode_img").attr("src",'${imgPath}'+url+"?l="+new Date().getTime());
	    		}
	    	 },
  	 error:function(){					    		
			     }
	     });
	
}




function checkField(){ 
	var flag = true;
	if(flag==true){
		 var isSecond = $("#isSecond").val();
		if(isSecond!=null && isSecond=="1"){
			$("#message").text("正在努力登录中，请稍等……");
			$("#message").show(500,secondSubmit);
		}else{
			$("#message").text("正在努力登录中，请稍等……");
			$("#message").show(500,firstSubmit);
		}
		
		
		return false;
	}
}
	
function showMessage(data, mess) {
	if (data.errMsg && data.errMsg != "") {
		//var mess = $("#message" + (t && t > 0 ? t+"" : ""));
		alert(data.errMsg);
		//mess.show();
	} else {
		alert(mess);
	}
}
function firstSubmit(){
    var vertifyUrl='/${ctx}/putong_sh_dx_vertifyLogin.html';
    var phoneValue = $("#phone").val();
    var passwordValue = $("#password").val();
    var authCode = $("#authCode").val();
         $("#password_tip").hide();
     $("#authcode_tip").hide();
     $("#authcode_message").hide();
     $("#message").text("");
    if(passwordValue==''||passwordValue== undefined){
     $("#password_tip").show();
    	return false;
    }else if (authCode==''||authCode== undefined){
     $("#authcode_tip").show();
    	return false;
    }
    $("#message").attr("style","dislpay:block;");
    $("#message").text("正在努力登录中，请稍等……");

    $.ajax({
	     type:'post',
         async:false,                                                            
	     url:vertifyUrl,
	     data:{userName:phoneValue,password:passwordValue,authcode:authCode},
	     success : function(data) {
	    	           $("#message").hide();
	    	       if(data.flag=='1'){
			    	 $("#page_first").hide();
			    	 $("#form2").show();
			    	  $("#form1").hide();
			    	  getAuth2();
			    	 $("#isSecond").val("1");
			    	 $("#spid").val(data.spid);
			
			    }else{
					// $("#putong_sh_dianxin_message").show();
					showMessage(data, "登录失败,服务密码或验证码错误.");
					getAuth();
				}
	    	},
	    	 error:function(){
	    		
	    		 parseFlag=false;
	    	 }
	     });
	
}



	function ss(){          
		var authValue = $("#sh_dianxin_authcode_Name").val();
		var phoneValue = $("#phone").val();
		if(authValue==''||authValue== undefined){
			alert("请先输入计算结果");
		}else{
			$.ajax({
			     type:'post',
			     async:false,
			     url:'/${ctx}/putong_sh_dx_sms.html',
			     data:{phone:phoneValue,authValue:authValue,random2:Math.random()},
			     success : function(data) {
			    	 if(data.flag=='1'){
			    		 //alert("计算结果输入错误！"); 
			    		 alert("发送短信成功，请查收！"); 
			    	 }else{
			    		 //alert("发送短信成功，请查收！");  
			    		 showMessage(data, "计算结果输入错误！");
			    	 }
	
			     },
			    error:function(){					    		
					     }
			     });
		}
						
	}
	
	function secondSubmit(){
		var phoneValue = $("#phone").val();
		var dxpass = $("#smsCode").val();
		var dxauth = $("#sh_dianxin_authcode_Name").val();
		if(dxpass==''||dxpass== undefined){
			alert("请先输入动态密码");
			return false;
		}
		if(dxauth==''||dxauth== undefined){
			alert("请先输入计算结果");			
			return false;
		}
		
		var url2 = "/${ctx}/putong_sh_dx_reqService.html";
 		$.ajax({
		     type:'post',
		     async:false,
		     url:url2,   
		     data:{phone:phoneValue,dxpass:dxpass,dxauth:dxauth,random2:Math.random()},
		     success : function(data) {
		    	 	var flag = data.flag;
		    	 	if(flag =='1'){		    	 		
		    	 		window.location.href="/${ctx}/entranceEmail.html";
		    	 	}else{
					 showMessage(data,"登录失败,随机短信码错误，请重新输入.");
		    	 	}
		    	 },
		    	 error:function(){	
		    		 parseFlag=false;
				     }
		     });	

		
		
	}
</script>
</html>