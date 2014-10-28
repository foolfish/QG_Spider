<%@ page contentType="text/html;charset=utf-8" language="java" import="java.util.Calendar,java.util.Date;"  %>
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
        			<c:set var="url" value="${putong_jl_yidong_url}"/>
        			<c:set var="result" value="putong_jl_yidong"/>
        			<form id="form1" class="base-form" action="">
        			
						<h2 class="base-form-title">中国移动：</h2>
						<input type="hidden" id="dongtai" value="1"/>
						  <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
						  <input type="hidden" name="jl_authimg" id="jl_authimg" />
						<div id="jl_first">
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
							<label id="password_message1" class="error-tip" >服务密码错误，请重新填写</label>
							<label id="password_tip1" class="error-tip" >请输入服务密码</label>
						</div>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>服务密码</label>
							</span>
							<input class="base-fieldset-input w173" name="fwpassword" id="fwpassword" size="30" type="password"/>
							<a class="base-forgetpwd" href="http://www.xj.10086.cn/app?service=page/personalinfo.ForgotPwd&listener=initPage" target="_blank">忘记密码</a>
						</div>
					   <div style="padding-left: 120px">
						<label id="authcode_tip" class="error-tip" >请输入验证码</label>
						<label id="authcode_message" class="error-tip" >验证码错误</label>
						</div>
						<div class="base-fieldset clearfix" style="display:none;" id="authcode_value">
							<span class="base-fieldset-title">
								<label>验证码</label>
							</span>
							
							<input class="base-fieldset-input w173" name="authCode" id="authCode" type="text"/>
		  					<label class="base-form-checkimglab">
							<img  onclick="changeAuth()" src="" id="authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"/>
		  					</label>
		  					<label class="base-form-changelab">
		  						看不清？<a class="" href="javascript:changeAuth();">换一张</a>
		  					</label>
						</div>
						</div>
				<!-- 查询详单动态码 -->
				<li id="jl_second1" style="display: none;">
				<input  name="isSecond" id="isSecond" type="hidden"/>
				<div class="control-line" >
					<label class="control-label" for="user_tel">查询话费详单</label>
					<div class="control">
					</div>
				</div>				
				<div class="control-line">
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode" id="smsCode" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
				</div>
				</li>
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn"  type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" onclick="checkField();return false;" trigger="base-btn-hover"/>
						</div>
						<p class="base-from-console">
							<span class="base-from-wrd" id="message" style="display:none;"></span>
						</p>
						
					</form>
					</div>
	
		
				
<script type="text/javascript">

$(document).ready(function(){   
	getAuth();
});


function getAuth(){	
	var phone = $("#phone").val();
	$.ajax({
	     type:'post',
       async:false,
	     url:'/${ctx}/putong_xj_yidong_GetAuth.html',
	     data:{random2:Math.random(),"phone":phone},
	     success : function(data) {
	    	 	var url = data.url;
	    	 	if(url != 'none'){	
	    	 		$("#authcode_value").show();
	    	 	$("#jl_authimg").val(data.url);
	    	 		$("#authcode_img").attr("src",'${imgPath}'+url+"?l="+new Date().getTime());
	    		}
	    	 },
  	 error:function(){					    		
			     }
	     });
	
}

function changeAuth(){

	//var url = $("#jl_authimg").val();
	//$("#authcode_img").attr("src",url+"?l="+new Date().getTime());
	getAuth();
}



function checkField(){ 
	var flag = $("#dongtai").val();
	if(flag=='1'){		
		$("#message").text("正在努力登录中，请稍等……");
		$("#message").show(500,firstSubmit);		
	}else if(flag=='2'){
		$("#message").text("正在努力验证中，请稍等……");
		$("#message").show(500,secondSubmit);
	}
}

function firstSubmit(){
	var vertifyUrl='/${ctx}/putong_xj_yidong_vertifyLogin.html';
	var phoneValue = $("#phone").val();
    var fwpassword = $("#fwpassword").val();
    var authCode = $("#authCode").val();
    $.ajax({
	     type:'post',
         async:false,                                                            
	     url:vertifyUrl,
	     data:{phone:phoneValue,fwpassword:fwpassword,authCode:authCode},
	     success : function(data) {
	    	 $("#message").hide();
	    	 alert(data.flag);
             if(data.flag=='1'){
            	 alert("22");
            	 $("#jl_first").hide();
			     $("#jl_second1").show();			     
			     $("#isSecond").val("1");
			     $("#dongtai").val("2");
			     //window.location.href="/${ctx}/entranceEmail.html";
			 }else if(data.flag=='2'){
				 $("#authcode_message").show();
				  $("#password_message1").hide();
			//	 $("#authcode_message").html('验证码错误，请重新填写.');
				 getAuth();
			  }else if(data.flag=='4'){
			     $("#password_message1").show();
			     $("#authcode_message").hide();
			//	 $("#putong_jl_yidong_message").html('账号密码错误，请重新填写.');
				 getAuth();		  
			  }else if(data.flag=='3'){
			     $("#putong_jl_yidong_message").show();
				 $("#putong_jl_yidong_message").html('系统繁忙，请稍后再试.');
				 getAuth();		  
			  }else{
			     $("#password_message1").show();
				 $("#password_message1").html('账号密码错误，请重新填写.');
				 getAuth();				  
			  }
	      },
	    	 error:function(){
	    		$("#message").show();
	    		$("#message").text(data.reMsg);
	    	 }
	     });
	
}

function secondSubmit(){
	var vertifyUrl='/${ctx}/dongtai_xj_yd_vertifyLogin.html';
	var smsCode = $("#smsCode").val();
	var phone = $("#phone").val();
    var wzpassword = $("#wzpassword").val();
    var types = $("#isSecond").val();
    if($.trim(smsCode)==""){
       $("#message").text("");
       alert("请填写动态密码!");
       return false;
    }
    $.ajax({
	     type:'post',
         async:false,                                                            
	     url:vertifyUrl,
	     data:{smsCode:smsCode,phone:phone,wzpassword:wzpassword,types:types},
	     success : function(data) {
	             $("#message").hide();
                 if(data.flag=='1'){
				    window.location.href="/${ctx}/entranceEmail.html";
				 }else if(data.flag=="2"){
				     alert("动态密码错误请重新获取，认真填写!");
				 }else{
				     alert("系统错误，请联系管理员!");
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
	     url:'/${ctx}/putong_xj_yd_GetSms.html',
	     data:{phone:phoneValue},
	     success : function(data) {
	    	 	if(data.msgCode=='1'){
					alert("获取随机验证码成功.");
			    }else if(data.msgCode=='2'){
			    	alert("请您耐心等待，30秒后再次点击.");
				}else{
			    	alert("获取随机验证码失败.");
				}
	    	 },
  	 error:function(){					    		
			     }
	     });
}	
	
	
	
</script>
</html>