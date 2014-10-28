<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 

			<c:set var="locationUrl" value="/${ctx}/ds/taobao"/>
			<input type="hidden" name="putong_taobao_hidden" id="putong_taobao_hidden" value="0"/>
			<input type="hidden" name="taobao_isSecond" id="taobao_isSecond" value="0"/>
		
			<!--异步是否响应成功 -->
			
					<div class="wrapper" id="putong_taobao_div">
						<div class="base-form-login dianshang-pice">
							<h2 class="base-form-title dianshang-title2">淘宝</h2>
							<p class="base-from-console">
								<span class="base-from-wrd dianshang-console" id="putong_taobao_message"  style="display:none;">用户名或密码错误，请重新填写</span>
							</p>
							<div class="base-fieldset clearfix" id="taobaoUserName">
								<span class="base-fieldset-title">
									<label>用户名</label>
								</span>
								<input checktype="input"  name="putong_taobao_userName" id="putong_taobao_userName" class="base-fieldset-input"   notnull value="${taobaovalue}" onblur="checkAuth_taobao();"/>
								<p class="base-alert-error" id="putong_taobao_userName_tip">用户名不能为空</p>
								<input type="hidden" name="taobaoName" id="taobaoName" value=""/>
							</div>
							<div class="base-fieldset login-fieldset clearfix"  id="taobaoPassword">
								<span class="base-fieldset-title login-fieldset-title">
									<label>密码</label>
								</span>
								<input type="password" name="putong_taobao_password" id="putong_taobao_password" class="base-fieldset-input"  checktype="input" notnull/>
								<a href="http://110.taobao.com/account/forget_passwd.htm" class="base-forgetpwd" target="_blank">忘记密码？</a>
								<p class="base-alert-error" id="putong_taobao_password_tip">密码不能为空</p>
							</div>
							<div class="base-fieldset clearfix" id="putong_taobao_authcode" <c:if test="${putong_taobao_url eq 'none'}">style="display:none;"</c:if>>
								<span class="base-fieldset-title">
									<label>验证码</label>
								</span>
								<input class="w173 base-fieldset-input" name="putong_taobao_authcode_Name" id="putong_taobao_authcode_Name"  needcheck="checkNum" checktype="input" notnull/>
			  					<label class="base-form-checkimglab">
			  						<img src="" id="putong_taobao_authcode_img" style="height:43px;width:119px;position: relative;top: -4px;" onclick="checkAuth_taobao()"/>
			  					</label>
			  					<label class="base-form-changelab">
			  						看不清？<a class="" href="javascript:checkAuth_taobao()">换一张</a>
			  					</label>
			  					<p class="base-alert-error" id="putong_taobao_authcode_tip"  name="putong_taobao_authcode_tip">验证码不能为空</p>
							</div>
						</div>
						<div class="dianshang-form-success">
							<p><span>淘宝已登录，开始收集信息</span></p>
						</div>
						<input  name="taobao_isSecond" id="taobao_isSecond" type="hidden"/>
						<div id="taobao2" style="display:none;">
								<div class="base-fieldset login-fieldset clearfix" style="height: 80px;">
									<span class="base-fieldset-title login-fieldset-title" style="height: 80px;">
										<label style="line-height: 80px;">动态密码</label>
									</span>
									<p style="line-height: 20px;margin-bottom: 10px;">
										<label>
											请输入该手机动态密码: 
											<select id="phone">
												
											</select>
										</label>
									</p>
									<input type="text" name="taobao2_dyn" id="taobao2_dyn" class="base-fieldset-input"  checktype="input" notnull/>
									<a href="javascript:void(0);" class="base-forgetpwd" onclick="getSms();">获取动态密码</a>	
									<p class="base-alert-error" id="taobao2_dyn_tip">动态口令密码不能为空</p>
								</div>
							</div>
						
						
					</div>
					
			
<script type="text/javascript">
	
$(document).ready(function(){
	$("#taobao_isSecond").val("0");
	var taobaovalue_ss = "${taobaovalue}";
	//如果值不等于空就默认初始化代码
	if(taobaovalue_ss!=""){
		checkAuth_taobao();
	}
});

/*发短信验证*/
function getSms(){
	var phone = $('#phone option:selected').val(); 
	var loginName = $('#putong_taobao_userName').val(); 
	
	$.ajax({
	     type:'post',
	     url:'${locationUrl}/sendMsg.html',
	     data:{"phone":phone,"loginName":loginName,random2:Math.random()},
	     success : function(data) {
	     			
	    		 alert(data.errorMsg);
	     
	      }, 
	   error:function(){}
	 });
	
	
}
//是不是在未异步执行完之前点击下一步了
var sub_taobao_code = true;
/*检查是否需要展示验证码*/
function checkAuth_taobao(){
	var userNameValue =  $("#putong_taobao_userName").val();	
	var taobaoName = $("#taobaoName").val();
//	if(userNameValue != taobaoName){		
		if(userNameValue.length>3){
				$.ajax({
				     type:'post',
				     url:'${locationUrl}/init.html',
				     data:{"loginName":userNameValue,random2:Math.random()},
				     success : function(data) {
				    	 	var url = data.url;
				    	 	if(url != ''){
				    	 		$("#putong_taobao_authcode").show();
				    	 		$("#putong_taobao_authcode_img").attr("src",url+"?time="+new Date().getTime());
				    	 	}else{
				    			$("#putong_taobao_authcode").hide();
				    	 	}
				    	 	$("#result_taobao_code").val("1");
				    			if(!sub_taobao_code){
				    				checkField();
				    			}
				    	 },
				    	 error:function(){	
				    		 $("#result_taobao_code").val("1");
				    		 if(!sub_taobao_code){
				    			 checkField(); 
				    		 }
						  }
				     });					
		//}
		$("#taobaoName").val(userNameValue);
	}
	
}

/*淘宝登陆判断*/
function checkTAOBAOpre(){
	var taobao_isSecond = $("#taobao_isSecond").val();
	var putong_taobao_hidden = $("#putong_taobao_hidden").val();
	if(putong_taobao_hidden.length>0 && putong_taobao_hidden=='0'){
		checkTAOBAO(); //淘宝正常流程
	}else if(putong_taobao_hidden.length>0 && putong_taobao_hidden=='1' && taobao_isSecond == '1'){
		checkDynTabobao(); //有动态密码
	}
	
}

				
/*有动态密码*/
function checkDynTabobao(){
	var taobao2_dyn = $("#taobao2_dyn").val();
	var loginName = $('#putong_taobao_userName').val(); 
	$.ajax({
	     type:'post',
         async:false,
	     url:'${locationUrl}/vertify_sendMsg.html',
	     data:{"phoneCode":taobao2_dyn,loginName:loginName,random2:Math.random()},
	     success : function(data) {
	    	 	var flag2 = data.status;
	    	 	if(flag2==1){
	    	 		alert("短信验证成功！");
		    	 		$("#putong_taobao_div").addClass("dianshang-logined");
	    	 			$("#taobao_isSecond").val("2");
	    	 			returnURL();
	    	 	}else{
	    	 		alert("短信验证失败，请重新输入或者发送新短信！");
	    	 	}
	    	 },
	    	 error:function(){					    		
			     }
	     });
}
				
/*淘宝普通检查*/				
function checkTAOBAO(){
	     var flag = true;
	     var userValue = $("#putong_taobao_userName").val();
        if (userValue=='')
		{
			flag = false;
			$("#putong_taobao_userName_tip").attr("style","display:block;");							
		}else{			
			$("#putong_taobao_userName_tip").attr("style","display:none;");
		}  
	    
		if(flag == false){
			return false;
		}

	
		var passwordValue = $("#putong_taobao_password").val();
        if (passwordValue=='')
		{
			flag = false;
			$("#putong_taobao_password_tip").attr("style","display:block;");
		}else{							
			$("#putong_taobao_password_tip").attr("style","display:none;");	
		} 
            
       
	
	if(flag == false){
		return false;
	}
	
	var taobao_style = $("#putong_taobao_authcode").attr("style");
	if(taobao_style.indexOf("none")==-1){
		var authcode= $('#putong_taobao_authcode_Name').val();
		if (authcode!='' && typeof(authcode)!="undefined" && authcode.length!=0){							
			$('#putong_taobao_authcode_tip').attr("style","display:none;");
		}else{
			flag = false;
			$('#putong_taobao_authcode_tip').attr("style","display:block;");
		}							
	}
	
	if(flag == false){
		return false;
	}
	if($("#result_taobao_code").val()=='0'){
		$("#putong_taobao_message").text("正在努力登录中，请稍等……").show();
		sub_taobao_code = false;
		return;
	}
	if(flag==true){
		$("#putong_taobao_message").text("正在努力登录中，请稍等……").show();
   		$("#putong_taobao_message").show(200,taobaonotify);
		return false;
	}
}
				

/*淘宝正常登陆*/
function taobaonotify(){
		
	        var userNameValue = $("#putong_taobao_userName").val();
	        var passwordValue = $("#putong_taobao_password").val();			      
	        var authcodeValue = $("#putong_taobao_authcode_Name").val();
	        
	    
	        var vertifyUrl='${locationUrl}/vertifyLogin.html';
	        $.ajax({
			     type:'post',
		         async:true,                                                            
			     url:vertifyUrl,
			     data:{loginName:userNameValue,password:passwordValue,authcode:authcodeValue,random2:Math.random()},
			     success : function(data) {
			    	 	var state = data.status;
			    	 	if(state==1){			    
			    	 		$("#putong_taobao_hidden").attr("value","1");
				    	 		$("#putong_taobao_div").addClass("dianshang-logined");
								returnURL();
			    	 	}else{		 
			    	 		if(data.phone!=''){
		    	 				$("#taobaoUserName").attr("style","display:none;");
		    	 				$("#taobaoPassword").attr("style","display:none;");
		    	 				$("#putong_taobao_authcode").attr("style","display:none;");
		    	 				$("#taobao2").attr("style","display:block;");
		    	 				
		    	 				$("#putong_taobao_message").text("").attr("style","display:none;");
		    	 				
		    	 				var phone = data.phone;
		    	 			
		    	 				$("#phone").append('<option value="'+phone+'">'+phone+'</option>');
		    	 				$("#taobao_isSecond").val("1");
		    	 				
		    	 			}else{
			    	 			$("#putong_taobao_message").text(data.errorMsg).show();
					    		var url = data.url;
					    		if(url !=''){
					    	 		$("#putong_taobao_authcode").show();
				    	 			$("#putong_taobao_authcode_img").attr("src",url+"?time="+new Date().getTime());
					    		}	
					    	} 
			    	 	}
			    	 },
			    	 error:function(){
			    		
			    		
			    	 }
			     });
	        
	        
	        }
		
		</script>