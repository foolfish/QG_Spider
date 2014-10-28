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
				<c:set var="currentNet" value="中国联通"/>
        		<c:set var="url" value="${putong_sh_liantong_url}"/>
        		<c:set var="result" value="fuwu_sh_liantong"/>
				<form id="form1" class="base-form" action="">
						<h2 class="base-form-title">中国联通 </h2>
						
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
							<label class="error-tip" id="password_message_tip" style="display:none;"></label>
							<label id="${result}_message" class="error-tip" >密码错误，请重新填写</label>
							<label id="${result}_message_overload" class="error-tip" >登录密码出错已达上限，请<a href="https://uac.10010.com/cust/resetpwd/inputName" target="_blank" style="color: #36c;cursor: pointer;text-decoration:underline;">找回登录密码</a>后登录，或3小时后重试。</label>
							<label id="${result}_password_tip" class="error-tip" >请输入密码</label>
						</div>
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>服务密码</label>
							</span>
							<input class="base-fieldset-input w173" name="${result}_password" id="${result}_password" size="30" type="password"/>
							<a class="base-forgetpwd" href="https://uac.10010.com/cust/resetpwd/inputName" target="_blank">忘记密码</a>
						</div>
						
						<div class="base-fieldset clearfix" id="${result}_authcode" <c:if test="${url eq 'none'}">style="display:none;"</c:if>
				<c:if test="${url ne 'none'}">style="display:block;"</c:if>>
							<span class="base-fieldset-title">
								<label>验证码</label>
							</span>
							
							<input class="base-fieldset-input w173" name="${result}_authcode_Name" id="${result}_authcode_Name" type="text"/>
							
		  					<label class="base-form-checkimglab">
		  						<img src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="${result}_authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"   onclick="changeCode()"/>
		  						<div id="${result}_authcode_tip" class="error-tip" style="display:none;">验证码</div>
		  					</label>
		  					<label class="base-form-changelab">
		  						看不清？<a class="" onclick="changeCode()">换一张</a>
		  					</label>
						</div>
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn"  type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" onclick="checkField();return false;" trigger="base-btn-hover"/>
						</div>
						
					</form>
					<form id="form2" class="base-form base-element-hidden" action="">
						<div class="base-fieldset login-fieldset clearfix">
							<span class="base-fieldset-title login-fieldset-title">
								<label>手机验证码</label>
							</span>
							<input id="checkMsg" type="password" needcheck class="base-fieldset-input w173"/>
							<a class="base-forgetpwd">获取动态密码</a>	
						</div>
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn" type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" trigger="base-btn-hover" onclick="checkField();return false;"/>
						</div>
						<p class="base-from-console">
							<span class="base-from-wrd" id="message" style="display:none;">正在加载动态密码，请稍等...</span>
						</p>
					</form>
					</div>
					
<script type="text/javascript">
function changeCode(){
		var userNameValue = $("input[id$='_userName']").val();
		$.ajax({
		     type:'post',
		     url:'/${ctx}/sh_checkNeedAuthCode.html',
		     data:{loginName:userNameValue},
		     success : function(data) {
		    	 if(data.url!="none"){
					var id = "${result}_authcode";
					$("#"+id).show();	
					$('#fuwu_sh_liantong_authcode_img').attr("src",data.url+"?l="+new Date());
		    	 }
		    },
		    	 
		   error:function(){					    		
				 }
		     });
		
	}
	var phoneFlag = true;
	var phoneFlaglian = true;
	function getDynpasslian(){
		var flag = checkTelno('dongtai_sh_liantong',2); 
		if(flag == true){
			var telno = $('#dongtai_sh_liantong_userName').val();
			$.ajax({
			     type:'post',
		         async:false,
			     url:'/${ctx}/sh_sendCardLian.html',
			     data:{phone:telno,random2:Math.random()},
			     success : function(data) {
			          $('#dongtai_sh_liantong_userName_tip').text("短信已发送，60秒后可重新获取").show();
			    	  if(result!=0){
			    		  phoneFlaglian = false;
			    	  }
			    	 },
			    	 error:function(){					    		
					     }
			     });	
		}
	}
	
	$(document).ready(function(){
		var userNameValue =  $("#fuwu_sh_liantong_userName").val();
		$("#fuwu_sh_liantong_userName_tip").hide();
			$.ajax({
			     type:'post',
		         async:true,
			     url:'/${ctx}/sh_checkNeedAuthCode.html',
			     data:{loginName:userNameValue,ptype:'01'},
			     success : function(data) {
			    	 	var url = data.url;
			    	 	if(url != 'none'){				    	
			    	 		var id = "${result}_authcode";
					$("#"+id).show();	
					$('#fuwu_sh_liantong_authcode_img').attr("src",data.url+"?l="+new Date());
			    	 	}
			    	 },
		    	 error:function(){					    		
					     }
			     });		 
	});
	


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
				$("#"+userName+"_tip").show();
				$("#"+userName).addClass("error");
			}else{			
				 if(pre=='fuwu_sh_liantong'||pre=='dongtai_sh_liantong'){
					flag = checkTelno(pre,2);
					if(flag==true){
						$("#"+userName+"_tip").hide();
						$("#"+userName).removeClass("error");
					}
				}else{
					$("#"+userName+"_tip").hide();
					$("#"+userName).removeClass("error");
				}
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
				$("#"+password+"_tip").show();
				$("#"+password).addClass("error");
			}else{
					$("#"+password+"_tip").hide();
					$("#"+password).removeClass("error");
			}    
	        }); 
		if(phoneFlaglian==false){
			$('#dongtai_sh_liantong_userName_tip').text("请输入正确的手机号，并点击获取动态密码").show();
			flag = false;
		}
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
					$("#"+authName+"_tip").show();
				}
				
			}
	        });
		if(flag == false){
			return false;
		}
		if(flag==true){
			$("#message").text("正在努力登录中，请稍等……");
    		$("#message").show(500,notifyAll);
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
	        var authcodeValue = $("#"+pre+"_authcode_Name").val();
	        var dtpassword = '';
	        var vertifyUrl='/${ctx}/'+pre+'_vertifyLogin.html';
	        $.ajax({
			     type:'post',
		         async:false,                                                            
			     url:vertifyUrl,
			     data:{loginName:userNameValue,password:passwordValue,dtpassword:dtpassword,"resultCode":1,authcode:authcodeValue,random2:Math.random()},
			     success : function(data) {
			     		var msg = data.msg;
			     		var state = data.state;
			    	 	if(state ==true){
			    	 		var currentDivName = '';
			    	 		if(pre.indexOf("_liantong") >= 0){
			    	 			currentDivName = '中国联通';
			    	 		}
			    	 		$("#"+pre+"_div").hide();
			    	 		$("#"+pre+"_div").text(currentDivName+"已登录,开始收集信息");
			    	 		$("#"+pre+"_div").show();
			    	 		$("#"+pre+"_hidden").attr("value","true");
			    	 	}else{
			    	 	 	parseFlag=false;
		    	 			 $("#password_message_tip").text(msg);
				        	 $("#password_message_tip").show();
				    	 	 var url = data.url;
				    	 	 if(url !='none'){
									var id = "${result}_authcode";
									$("#"+id).show();	
									$('#fuwu_sh_liantong_authcode_img').attr("src",data.url+"?l="+new Date());
				    	 	 }
			    	 	}
			    	 },
			    	 error:function(){
			    		 parseFlag=false;
			    	 }
			     });
	        });
		if(checkAllhidden() == true){		
			window.location.href="/${ctx}/entranceEmail.html";
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
</script>
</html>