<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 

					<input type="hidden" name="putong_jd_hidden" id="putong_jd_hidden" value="0"/>
					<!-- 验证码地址 -->
					<input type="hidden" name="imgcode_jd" id="imgcode_jd" value=""/>
					<!--异步是否响应成功 -->
					<input type="hidden" name="result_jd_code" id="result_jd_code" value="0"/>
					<div class="wrapper" id="putong_jd_div"><!--  dianshang-logined -->
						<div class="base-form-login dianshang-pice">
							<h2 class="base-form-title">京东</h2>
							<p class="base-from-console">
								<span class="base-from-wrd dianshang-console" id="putong_jd_message" style="display:none;">用户名或密码错误，请重新填写</span>
							</p>
							<div class="base-fieldset clearfix">
								<span class="base-fieldset-title">
									<label>用户名</label>
								</span>
								<input name="putong_jd_userName" id="putong_jd_userName"  checktype="input" class="base-fieldset-input"  notnull value="${jdvalue }" onblur="checkAuth();"/>
								<p class="base-alert-error" id="putong_jd_userName_tip">用户名不能为空</p>
									<input type="hidden" name="putong_jd_userName1" id="putong_jd_userName1" value=""/>
							</div>
							<div class="base-fieldset login-fieldset clearfix">
								<span class="base-fieldset-title login-fieldset-title">
									<label>密码</label>
								</span>
								<input type="password" name="putong_jd_password" id="putong_jd_password"  checktype="input" class="base-fieldset-input"  notnull/>
								<a href="http://safe.jd.com/findPwd/index.action" class="base-forgetpwd" target="_blank">忘记密码？</a>
								<p class="base-alert-error" id="putong_jd_password_tip">密码不能为空</p>
							</div>
							
							<div class="base-fieldset clearfix"  id="putong_jd_authcode" <c:if test="${putong_jd_url eq 'none'}">style="display:none;"</c:if>>
								<span class="base-fieldset-title">
									<label>验证码</label>
								</span>
								<input class="w173 base-fieldset-input"  name="putong_jd_authcode_Name" id="putong_jd_authcode_Name"   needcheck="checkNum" notnull/>
			  					<label class="base-form-checkimglab">
			  						<img src="<c:if test="${putong_jd_url ne 'none'}">${imgPath}${putong_jd_url}</c:if>" id="putong_jd_authcode_img" style="height: 42px;width: 138px;position: relative;top: -2px;" onclick="checkAuth();"/>
			  					</label>
			  					<label class="base-form-changelab">
			  						看不清？<a class="" href="javascript:checkAuth();">换一张</a>
			  					</label>
			  					<p class="base-alert-error" id="putong_jd_authcode_tip" name="putong_jd_authcode_tip">验证码不能为空</p>
							</div>
						
						</div>
						<div class="dianshang-form-success">
							<p><span>京东已登录，开始收集信息</span></p>
						</div>
					</div>
					
					
<script type="text/javascript">
		//是不是在未异步执行完之前点击下一步了
		var sub_jd_code = true;
		/*检查是否需要展示验证码*/
	function checkAuth(){
			var timestamp=new Date().getTime();
			var userNameValue = $("#putong_jd_userName").val();
			var putong_jd_userName1 = $("#putong_jd_userName1").val();
		//	if(userNameValue != putong_jd_userName1){	
			//	$("#putong_jd_userName1").val(userNameValue);
			//if(userNameValue.length>3){
				$.ajax({
				     type:'post',
				     url:'/${ctx}/jd/init.html',
				     data:{random2:Math.random(),"loginName":userNameValue},
				     success : function(data) {
				    	 if(data.url!="none"){
				    	 	    $("#putong_jd_authcode").show();
	 							$('#putong_jd_authcode_img').attr("src",data.url+"?l="+new Date().getTime());
				    	 }
				    	 $("#result_jd_code").val("1");
			    			if(!sub_jd_code){
			    				checkField();
			    			}
				    }, 
				   error:function(){	
					   $("#result_jd_code").val("1");
		    			if(!sub_jd_code){
		    				checkField();
		    			}
						 }
				   });
				//}
			//}
		}
	
				function checkJDpre(){
					var putong_jd_hidden = $("#putong_jd_hidden").val();
					if(putong_jd_hidden.length>0 && putong_jd_hidden=='0'){
						checkJD();
					}
					
				}

			
				function checkJD(){
						var flag = true;
					     var userValue = $("#putong_jd_userName").val();
				        if (userValue=='')
						{
							flag = false;
							$("#putong_jd_userName_tip").attr("style","display:block;");							
						}else{			
							$("#putong_jd_userName_tip").attr("style","display:none;");
						}  
					    
						if(flag == false){
							return false;
						}

					
						var passwordValue = $("#putong_jd_password").val();
				        if (passwordValue=='')
						{
							flag = false;
							$("#putong_jd_password_tip").attr("style","display:block;");
						}else{							
							$("#putong_jd_password_tip").attr("style","display:none;");	
						} 
				            
				       
					
					if(flag == false){
						return false;
					}
					
					var jd_style = $("#putong_jd_authcode").attr("style");
					if(jd_style.indexOf("none")==-1){
						var authcode= $('#putong_jd_authcode_Name').val();
						if (authcode!='' && typeof(authcode)!="undefined" && authcode.length!=0){							
							$('#putong_jd_authcode_tip').attr("style","display:none;");
						}else{
							flag = false;
							$('#putong_jd_authcode_tip').attr("style","display:block;");
						}							
					}
					
					if(flag == false){
						return false;
					}
					if($("#result_jd_code").val()=='0'){
						$("#putong_taobao_message").text("正在努力登录中，请稍等……").show();
						sub_taobao_code = false;
						return;
					}
					if(flag==true){
						$("#putong_jd_message").text("正在努力登录中，请稍等……").show();
						 jdnotify();
						return false;
					}
				}
				


			function jdnotify(){
				        var userNameValue = $("#putong_jd_userName").val();
				        var passwordValue = $("#putong_jd_password").val();			      
				        var authcodeValue = $("#putong_jd_authcode_Name").val();
				        var resultCode = $("#result_jd_code").val();
				    
				        var vertifyUrl='/${ctx}/jd/vertifyLogin.html';
				        $.ajax({
						     type:'post',
					         async:false,                                                            
						     url:vertifyUrl,
						     data:{"loginName":userNameValue,"password":passwordValue,"authcode":authcodeValue,random2:Math.random()},
						     success : function(data) {
						    	 	var flag = data.state;
						    	 	if(flag ==true){			    
						    	 			$("#putong_jd_hidden").attr("value","1");
							    	 		$("#putong_jd_div").addClass("dianshang-logined");
							    	 		returnURL();
	
						    	 	}else{		 
						    	 			
						    	 			 $("#putong_jd_message").text(data.msg).show();
								    	 	 var url = data.url;
								    	 	 if(url !='none'){
								    	 		$("#putong_jd_authcode").show();
	 											$('#putong_jd_authcode_img').attr("src",data.url+"?l="+new Date().getTime());
								    	 	 }	 
						    	 	}
						    	 },
						    	 error:function(){
						    		
						    		 parseFlag=false;
						    	 }
						     });
				        
				        
				        }

		</script>