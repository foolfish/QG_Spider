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
						
						  <input type="hidden" name="${result}_hidden" id="${result}_hidden" value="false"/>
						  <input type="hidden" name="jl_authimg" id="jl_authimg" />
						<li id="jl_first">
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
							<a class="base-forgetpwd" href="http://www.jl.10086.cn/service/operate/password/ResetPwdAgreement.jsp" target="_blank">忘记密码</a>
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
						<div class="base-fieldset base-form-submit">
							<input id="lastBtn"  type="button" value="上一步" class="base-btn w120" trigger="base-btn-hover"/>
							<input id="saveBtn" type="button" value="下一步" class="base-btn w120 base-nextstep" onclick="checkField();return false;" trigger="base-btn-hover"/>
						</div>
						<p class="base-from-console">
							<span class="base-from-wrd" id="message" style="display:none;"></span>
						</p>
						</li>
					</form>
					</div>
			
			
			<!-- 六个月 -->
			<% 			
				java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy.MM");
				java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMM");
				Calendar calendar = Calendar.getInstance(); 
				calendar.add(Calendar.MONTH, 0);
				Date date = calendar.getTime();
				String date1 = format1.format(date);  
				String date2 = format2.format(date);        
			     %>
				<li id="jl_second1" style="display: none;">
				<input  name="isSecond" id="isSecond" type="hidden"/>
				<div class="control-line" >
					<label class="control-label" for="user_tel">查询<%=date1%>月份话费详单</label>
					<div class="control">
					</div>
				</div>				
				<input name="date1" id="date1" type="hidden" value="<%=date2%>">
				<div class="control-line">
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode1" id="smsCode1" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
				</div>
				</li>
				
				<%
							calendar.add(Calendar.MONTH, -1);
						    date = calendar.getTime();
							date1 = format1.format(date);  
							date2 = format2.format(date);
				 %>
				<li id="jl_second2" style="display: none;">
				<div class="control-line" >
				<label class="control-label" for="user_tel">查询<%=date1%>月份话费详单</label>
				</div>				
				<input name="date2" id="date2" type="hidden" value="<%=date2%>">
				<div class="control-line" >
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode2" id="smsCode2" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
				</div>
				</li>
				<%
							calendar.add(Calendar.MONTH, -1);
						    date = calendar.getTime();
							date1 = format1.format(date);  
							date2 = format2.format(date);
				 %>
				<li id="jl_second3" style="display: none;">
				<div class="control-line" >
				<label class="control-label" for="user_tel">查询<%=date1%>月份话费详单</label>
				</div>				
				<input name="date3" id="date3" type="hidden" value="<%=date2%>">
				<div class="control-line" >
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode3" id="smsCode3" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
				</div>
				</li>
				<%
							calendar.add(Calendar.MONTH, -1);
						    date = calendar.getTime();
							date1 = format1.format(date);  
							date2 = format2.format(date);
				 %>
				<li id="jl_second4" style="display: none;">
				<div class="control-line" >
				<label class="control-label" for="user_tel">查询<%=date1%>月份话费详单</label>
				</div>				
				<input name="date4" id="date4" type="hidden" value="<%=date2%>">
				<div class="control-line" >
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode4" id="smsCode4" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
				</div>
				</li>
				<%
							calendar.add(Calendar.MONTH, -1);
						    date = calendar.getTime();
							date1 = format1.format(date);  
							date2 = format2.format(date);
				 %>				
				<li id="jl_second5" style="display: none;">
				<input name="date5" id="date5" type="hidden" value="<%=date2%>">
				<div class="control-line" >
				<label class="control-label" for="user_tel">查询<%=date1%>月份话费详单</label>
				</div>
				<div class="control-line" >
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode5" id="smsCode5" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
				</div>
				</li>
				<%
							calendar.add(Calendar.MONTH, -1);
						    date = calendar.getTime();
							date1 = format1.format(date);  
							date2 = format2.format(date);
				 %>
				<li id="jl_second6" style="display: none;">
				<input name="date6" id="date6" type="hidden" value="<%=date2%>">
				<div class="control-line" >
				<label class="control-label" for="user_tel">查询<%=date1%>月份话费详单</label>
				</div>
				<div class="control-line" >
						<label class="control-label" for="user_tel">手机验证码：</label>
					<div class="control">
						<input class="input-normal" name="smsCode6" id="smsCode6" size="30" type="text"/>
			       		<td>
			       		<input class="btn btn-small btn-normal btn-primary" name="" type="button" value="获取动态密码" onclick="getSms();"/>
			       		</td>
					</div>
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
	     url:'/${ctx}/putong_jl_yidong_GetAuth.html',
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
	var flag = true;
	if(flag==true){
		$("#message").text("正在努力登录中，请稍等……");
		$("#message").show(500,firstSubmit);
	}
}

function firstSubmit(){
	var vertifyUrl='/${ctx}/putong_jl_yidong_vertifyLogin.html';
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
             if(data.flag=='1'){
			     $("#jl_second1").show();
			     $("#jl_first").hide();
			     $("#isSecond").val("1");
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
			  }else if(data.flag=='5'){
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

function secondSubmit(id){
	var vertifyUrl='/${ctx}/dongtai_jl_yd_vertifyLogin.html';
	var smsCode = $("#smsCode"+id).val();
    var dates = $("#date"+id).val();
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
	     data:{smsCode:smsCode,phone:phone,wzpassword:wzpassword,dates:dates,types:types},
	     success : function(data) {
	             $("#message").hide();
                 if(data.flag=='1'){
				     if(id=="6"){
				    	 window.location.href="/${ctx}/entranceEmail.html";
				     }else{
				         $("#isSecond").val(parseInt(id)+1);
				         $("#jl_second"+id).hide();
				         var ids = parseInt(id)+1;
				         $("#jl_second"+ids).show();
				         $("#message").text("获取成功，请您填写下个月的查询密码!");
				     }
				 } else if(data.flag=="2"){
				    alert("登录失败,请退出后重新登录.");
				 }else if(data.flag=="3"){
					 alert("业务类别有误!");
				 }else if(data.flag=="4"){
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
	     url:'/${ctx}/putong_jl_yd_GetSms.html',
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