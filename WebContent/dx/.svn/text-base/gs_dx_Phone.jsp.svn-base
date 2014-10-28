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
        			<c:set var="url" value="${putong_gs_dianxin_url}"/>
        			<c:set var="result" value="putong_gs_dianxin"/>
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
							<a class="base-forgetpwd" href="http://service.tj.10086.cn/app?service=page/protectmail.MailAnswer&listener=initPage" target="_blank">忘记密码</a>
						</div>
						<div style="padding-left: 120px">
						<label id="authcode_tip" class="error-tip" >请输入验证码</label>
						<label id="authcode_message" class="error-tip" >验证码错误</label>
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
							<span class="base-from-wrd" id="message" style="display:none;"></span>
						
						</p>
						
					</form>
					
					</div>
        			
        			
        			
<script type="text/javascript">



$(document).ready(function(){
	getAuth();
});


function getAuth(){
	$.ajax({
	     type:'post',
       async:false,
	     url:'/${ctx}/putong_gs_dianxin_GetAuth.html',
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




function checkField(){ 
	var flag = true;
	if(flag==true){
		$("#putong_gs_dianxin_message").text("正在努力登录中，请稍等……");
		$("#putong_gs_dianxin_message").show(500,firstSubmit);
	}
}

function firstSubmit(){
	var vertifyUrl='/${ctx}/putong_gs_dianxin_vertifyLogin.html';
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
	 $.ajax({
	     type:'post',
       	 async:false,                                                            
	     url:vertifyUrl,
	     data:{phone:phoneValue,password:passwordValue,authCode:authCode},
	     success : function(data) {
			   if(data.flag=='1'){
				   window.location.href="/${ctx}/entranceEmail.html";
				}else if(data.flag=='-1'){
					alert("验证码输入错误.");
					
					getAuth();
					 $("#putong_gs_dianxin_message").hide();
				}else if(data.flag=='-2'){
					alert("服务密码输入错误.");
					getAuth();
					 $("#putong_gs_dianxin_message").hide();
				}else{
					alert("登录失败,服务密码或验证码错误.");
					getAuth();
					 $("#putong_gs_dianxin_message").hide();
				}
	    	},
	    	 error:function(){
	    		parseFlag=false;
	    	 }
	     });
  
}


	
	
</script>
</html>