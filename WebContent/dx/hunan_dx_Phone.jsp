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
        			<c:set var="url" value="${putong_hunan_dianxin_url}"/>
        			<c:set var="result" value="putong_hunan_dianxin_url"/>
        			<form id="form1" class="base-form" action="">
						<h2 class="base-form-title">中国电信：</h2>
						  <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
						  <input type="hidden" name="resultCode" id="resultCode" value=""/>
						<div class="base-fieldset clearfix">
							<span class="base-fieldset-title">
								<label>手机号</label>
							</span>
							<span class="base-form-phone">
								${phone}
								<input type="hidden" name="phone" id="phone"  value="${phone}" />
							</span>
						</div>
						<div style="padding-left: 120px" style="display:none">
							<label id="${result}_message" class="error-tip" >网站密码错误，请重新填写</label>
							<label id="password_tip" class="error-tip" >请输入网站密码</label>
						</div>
						<div class="base-fieldset login-fieldset clearfix" style="display:none">
							<span class="base-fieldset-title login-fieldset-title">
								<label>网站密码</label>
							</span>
							<input class="base-fieldset-input w173" name="wzpassword" id="wzpassword" size="30" type="password"/>
							<a class="base-forgetpwd" href="https://bj.ac.10086.cn/ac/html/register.html" target="_blank">注册获取密码</a>
						</div>
						
						<div style="padding-left: 120px">
							<label id="${result}_message1" class="error-tip" >服务密码错误，请重新填写</label>
							<label id="password_tip1" class="error-tip" >请输入服务密码</label>
						</div>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>服务密码</label>
							</span>
							<input class="base-fieldset-input w173" name="fwpassword" id="fwpassword" size="30" type="password"/>
							<a class="base-forgetpwd" href="http://sn.189.cn/service/passmanage/init.action" target="_blank">忘记密码</a>
						</div>
					   <div style="padding-left: 120px">
						<label id="authcode_tip" class="error-tip" >请输入验证码</label>
						<label id="authcode_message" class="error-tip" >验证码错误</label>
						</div>
						<div class="base-fieldset clearfix" id="authCodeDiv" style="display:none">
							<span class="base-fieldset-title">
								<label>验证码</label>
							</span>
							
							<input class="base-fieldset-input w173" name="authCode" id="authCode" type="text"/>
		  					<label class="base-form-checkimglab">
							<img onclick="getAuth()" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"/>
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
							<span class="base-from-wrd" id="message" style="display:none;"></span>
						</p>
					</form>
					<form id="form2" class="base-form base-element-hidden" action="">
					<input  name="isSecond" id="isSecond" type="hidden"/>
				    <input  name="spid" id="spid" type="hidden"/>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>手机验证码</label>
							</span>
							<input name="smsCode" id="smsCode"  class="base-fieldset-input w173"/>
							<a href="javascript:void(0);" class="base-forgetpwd" onclick="getSms();">获取动态密码</a>(十分钟内不能再次产生密码)
						</div>
						
						<div class="base-fieldset clearfix" style="display:none">
							<span class="base-fieldset-title">
								<label>验证码</label>
							</span>
							
							<input class="base-fieldset-input w173" name="smsAuthCode" id="smsAuthCode" type="text"/>
		  					<label class="base-form-checkimglab">
		  					<img  onclick="getDyAuth()" src="<c:if test="${url ne 'none'}">/${imgPath}${url}</c:if>" id="smsAuthcode_img" style="height:30px;width:60px;position: relative;top: 8px;"/>
							</label>
		  					<label class="base-form-changelab">
		  						看不清？<a class="" href="javascript:getDyAuth();">换一张</a>
		  					</label>
						</div>
						<label style="padding-left: 130px;color:red;display:none">请输入验证码</label>
						
						
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
var issubmit = false;
$(document).ready(function(){
	getAuth();
});
var auth_index = 1;
function getAuth(){
	
	$.ajax({
	     type:'post',
         async:true,
         data:{userName:'${phone}', index:auth_index},
	     url:'/${ctx}/putong_hunan_dianxin_GetAuth.html',
	     success : function(data) {
	    	 	var url = data.url;
	    	 	if(url != 'none'){				    	
	    	 		$("#authCodeDiv").show();
	    	 		$("#authcode_img").attr("src",'${imgPath}/img/authcode/' + url + "?d=" + new Date().getTime());
	    	 		$("#resultCode").val("1");
	    		}else{
	    			$("#resultCode").val("0");
	    		}
	    	 	auth_index++;
	    	 },
  	 	error:function(){		
  	 		$("#resultCode").val("0");			    		
	 	}
    });
	
}

function checkField(){ 
	var flag = true;
	if ("" == $("#resultCode").val()) {
		return;
	}
	if(flag==true){
		 var isSecond = $("#isSecond").val();
			if(isSecond!=null && isSecond=="1"){
				$("#message").text("正在努力登录中，请稍等……");
				$("#message").show(500,secondSubmit);
			}else{
				$("#message").text("正在努力登录中，请稍等……");
				$("#message").show(500,firstSubmit);
			}
	}
}

function firstSubmit(){
	if (issubmit) {
		return;
	}
	issubmit = true;
	var vertifyUrl='/${ctx}/putong_hunan_dianxin_vertifyLogin.html';
	var phoneValue = $("#phone").val();
    //var wzpassword = $("#wzpassword").val();
    var fwpassword = $("#fwpassword").val();
    var authCode = $("#authCode").val();
    var checkCode = $("#resultCode").val();
    
    $("#password_tip").hide();
    $("#password_tip1").hide();
     $("#authcode_tip").hide();
     $("#authcode_message").hide();
     $("#message").text("");
    /* if(wzpassword==''||wzpassword== undefined){
     $("#password_tip").show();
    	return false;
    } */
    if(fwpassword==''||fwpassword== undefined){
     	$("#password_tip1").show();
  	    return false;
    }
    if (checkCode == "1" && (authCode==''||authCode== undefined)){
     $("#authcode_tip").show();
    	return false;
    }
   
    $.ajax({
	     type:'post',
         async:false,                                                            
	     url:vertifyUrl,
	     data:{phone:phoneValue,fwpassword:fwpassword,authCode:authCode},
	     success : function(data) {
	    	// $("#message").hide();

	    	 if(data.flag=='1' && data.flag1=='false'){
	    		 $("#message").show();
				 $("#message").text(data.reMsg);
				 alert("获取部分信息失败.");
				  location.reload() ;
				  issubmit = false;
				 //getAuth();
			 }else if(data.flag=='1' || data.flag=='999'){
				 $("#page_first").hide();
		    	 $("#form2").show();
		    	  $("#form1").hide();
		    	 //$("#smsAuthcode_img").attr("src",'${imgPath}'+data.url+"?l="+new Date().getTime());
		    	 $("#isSecond").val("1");
		    	 issubmit = false;
		    	 //getSms();
		    	 //$("#spid").val(data.spid);
			     //window.location.href="/${ctx}/entranceEmail.html";
			 }else{
				 if (data.errMsg) {
					 alert(data.errMsg);
				 } else {
				 	alert("登录失败,密码或验证码错误.");
				 }
				 issubmit = false;
					$("#message").attr("style","dislpay:none;");
					//getAuth();
			  }
	      },
	    	 error:function(){
	    		$("#message").show();
	    		$("#message").text(data.reMsg);
	    	 }
	     });
}
function getSms(){

	var phoneValue = $("#phone").val();
	$.ajax({
	     type:'post',
       async:false,
	     url:'/${ctx}/putong_hunan_dianxin_GetSms.html',
	     data:{phone:phoneValue},
	     success : function(data) {
	    	 	if(data.flag=='1'){
					alert("获取随机验证码成功.");
			    }else if(data.flag=='-1'){
			    	alert("对不起，随机验证码请求次数过多，请明日再试.");
			    	window.location.href="/${ctx}/entranceEmail.html";
			    }else{
			    	alert("获取随机验证码失败.");
				}
	    	 },
  	 error:function(){					    		
			     }
	     });
	
}
function secondSubmit(){
	if (issubmit) {
		return;
	}
	issubmit = true;
	var vertifyUrl='/${ctx}/dongtai_hunan_dianxin_vertifyLogin.html';
	var smsCode = $("#smsCode").val();
    //var smsAuthCode = $("#smsAuthCode").val();
    //var spid = $("#spid").val();
    var phone = $("#phone").val();
	$.ajax({
	     type:'post',
         async:false,                                                            
	     url:vertifyUrl,
	     data:{smsCode:smsCode,phone:phone},
	     success : function(data) {
	    	 $("#message").hide();
	    	 if(data.flag=='1' && data.flag1=='false'){
	    		 $("#message").show();
				 $("#message").text(data.reMsg);
				 issubmit = false;
				 getDyAuth();
			 }else if(data.flag=='1'){
			     window.location.href="/${ctx}/entranceEmail.html";
			 } else{
				 alert("登录失败,动态密码错误.");
				 issubmit = false;
				 //getDyAuth();
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