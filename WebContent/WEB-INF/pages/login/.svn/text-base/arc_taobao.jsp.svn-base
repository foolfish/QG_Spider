<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>
		<c:set var="fparam" value="taobao"/>
		<input type="hidden" id="${fparam}locationUrl" value="/${ctx}/ds/taobao"/>
		<div class="section hide" id="sec-five">
			<form>
				<div class="group">
					<label for="username2">用户名</label>
					<div class="control">
						<input type="text" id="${fparam}loginName" placeholder="请输入淘宝账号" onblur="getAuths('${fparam}');"/>
						<input type="hidden" id="${fparam}loginName_temp" />
						<input type="hidden" id="${fparam}isShowImg" value="0"/>
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group">
					<label for="password2">
						密码
						<a href="http://110.taobao.com/account/forget_passwd.htm" target="_target" class="forget">
							忘记密码
						</a>
					</label>
					<div class="control">
						<input type="password" id="${fparam}password" />
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group" id="${fparam}isAuthcode">
					<label for="authcode">
						验证码
					</label>
					<div class="row">
						<div class="col-3">
							<div class="control">
								<input type="text" id="${fparam}authcode" value=""/>
							</div>
						</div>
						<div class="col-4">
							<div class="pic">
								<img src="" alt="" id="${fparam}imgUrl"/>
							</div>
						</div>
						<div class="col-4">
							<div class="pic">
								<a href="javascript:getAuths('${fparam}');">
									<strong>换一张</strong>
								</a>
							</div>
						</div>
					</div>
				</div>
				<div class="group wtip">
					<label class="wrong-tip"></label>
				</div>
				<div class="group">
					<button type="button" class="done" id="${fparam}login">
						确认
					</button>
				</div>
			</form>
		</div>
		
		<div class="modal hide" id="modal-taobao">
		<div class="modal-dialog" id="dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4>安全验证</h4>
					<a href="javascript:closetaobao();">
						<img src="/${ctx}/img/pro-031.png" alt="" class="cross"/>
					</a>	
				</div>
				<div class="modal-body">
					<form>
						<div class="group">
							<label for="id1">手机短信</label>
							<div class="control">
								
								<div class="down">
									<label class="showin" id="${fparam}phonetext">请选择</label>
									<img src="/${ctx}/img/a-007.png" class="arrow" alt="" />
									<ul class="for-down">
										<!--<li value="1" class="li-ff" ></li>
										<li value="2">15859075047</li>-->
									</ul>
								</div>
							</div>
							<div class="tip" style="left:190px;">
								<a  class="get-code" style="top:-27px;margin-left:0px;" id="${fparam}sendPhoneCode">
									获取手机口令
								</a>
							</div>				
						</div>
						<div class="group">
							<label for="id2">口令</label>
							<div class="control">
								<input type="text" id="${fparam}phoneCode" name="phoneCode" style="height: 39px;width:177px;"/>
								<input type="hidden" id="${fparam}phone" />
							</div>
						</div>
						<div class="group wtip">
							<label class="wrong-tip"></label>
						</div>
						<div class="group">
							<button type="button" class="done" id="${fparam}login2">
								确认
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

		
		
<script>
		
		function getJObj(id){
			return  $("#"+id);
		}
		function getJObjVal(id){
			return getJObj(id).val();
		}
		function setObjHide(id){
			getJObj(id).hide();
		}
		function setObjShow(id){
				getJObj(id).show();
		}
		function isShowImg(id){
			var s = getJObjVal(id+"isShowImg");
			if(s=="0"){
				return false;
			}else{
				return true;
			}
		}
		//"1"或者"0"
		function setShowImg(fparam,str){
			getJObj(fparam+"isShowImg").val(str);
		}
		function getAuths(fparam){
				var userNameValue =  getJObjVal(fparam+"loginName");
				var loginName_temp = getJObjVal(fparam+"loginName_temp");
				var url = getJObjVal(fparam+"locationUrl");
				if(userNameValue.length>3){
					$.ajax({
				    	 type:'post',
				    	 url:url+'/init.html',
				     	data:{"loginName":userNameValue,random2:Math.random()},
				     	success : function(data) {
				    	 	showImgs(fparam,data.url);
				    	 },
				    	 error:function(){	
						 }
				     });					
					$("#loginName_temp").val(userNameValue);
				}
			}
			function showImgs(fparam,url){
				if(url!=''&&url!=undefined){
					setObjShow(fparam+"isAuthcode");
					getJObj(fparam+"imgUrl").attr("src",url+"?time="+new Date().getTime());
					setShowImg(fparam,"1");
			     }else{
			    	 hideImg(fparam);
			     }
			}
			function hideImg(fparam){
				setObjHide(fparam+"isAuthcode");
				setShowImg(fparam,"0");
			}
			
			$(document).ready(function(){
				 getJObj("${fparam}login").click(function(){
   							login("${fparam}");
 				 });
				 getJObj("${fparam}login2").click(function(){
   							login2("${fparam}");
 				 });
				 getJObj("${fparam}sendPhoneCode").click(function(){
   						sendPhoneCode("${fparam}");
 				 });
 				 hideImg("${fparam}");
			});

			function g_wrong (node, text) {
				clearTimeout(g_make);
				if( node == "jingdong" ) {
					$("#sec-four .wrong-tip").text(text);
			   		$("#sec-four .wtip").slideDown("fast");
			   		g_make = setTimeout(function () {
			   			$("#sec-four .wtip").slideUp("fast");
			   		}, 2000);
				} else if ( node == "taobao" ) {
					$("#sec-five .wrong-tip").text(text);
			   		$("#sec-five .wtip").slideDown("fast");
			   		g_make = setTimeout(function () {
			   			$("#sec-five .wtip").slideUp("fast");
			   		}, 2000);
				}
				
			}
			function taobao_wrong (text) {
				$("#modal-taobao .wrong-tip").text(text);
		   		$("#modal-taobao .wtip").slideDown("slow");
		   		setTimeout(function () {
		   			$("#modal-taobao .wtip").slideUp("slow");
		   		}, 2000);
				
			}
			function login(fparam){
			var loginName = getJObjVal(fparam+"loginName");
			var password = getJObjVal(fparam+"password");
			var authcode = getJObjVal(fparam+"authcode");
			
			if(loginName=='' || loginName== undefined){
				g_wrong(fparam, "请输入登陆账号!");
				return false;
			}
			if(password=='' || password== undefined){
		   		g_wrong(fparam, "请输入密码!");
				return false;
			}
			if(isShowImg(fparam)){
				if(authcode==''||authcode == undefined){
			   		g_wrong(fparam, "请填写验证码!");
					return false;
				}
			}
			var url = getJObjVal(fparam+"locationUrl");
			$.ajax({
				type : 'post',
				async : false,
				url : url+"/verifyLogin.html",
				data : {
					"loginName" : loginName,
					"password" : password,
					"authcode": authcode,
					random2:Math.random()
				},
				success : function(data) {
					if (data.status == 1){
						if(fparam=='taobao'){
							act("taobao","tb","淘宝授权成功！");
						}else{
						    act("jingdong","jd","京东授权成功！");
						}
						
					}else{
						if(data.phone!=''&&data.phone!=undefined){
						
							
							$(".modal-bg-two").css("display","block");//遮罩层
							
							$("#modal-taobao").removeClass("hide").addClass("show");//显示第二个
							//setObjShow("modal-taobao");//显示第二个
							
							getJObj(fparam+"phone").val(data.phone);
							getJObj(fparam+"phonetext").text(data.phone);
							hideImg(fparam);
						}else{
							g_wrong(fparam, data.errorMsg);
							showImgs(fparam,data.url);
						}
					}
				
				},
				error : function() {
			   		g_wrong(fparam, "服务器繁忙,请稍后再试!");
				}
			});
			
		}
		function sendPhoneCode(fparam){
				var phone = getJObjVal(fparam+"phone"); 
				var loginName = getJObjVal(fparam+"loginName"); 
				var url = getJObjVal(fparam+"locationUrl");
				$.ajax({
	   				  type:'post',
	     			 url:url+'/sendMsg.html',
	    			 data:{"phone":phone,"loginName":loginName,random2:Math.random()},
	     			 success : function(data) {
	     					 if(data.status!=1){
	     				   		taobao_wrong(data.errorMsg);
	     					 }else{
	     						 taobao_wrong("发送成功!");
	     					 }
	     			 }, 
	  				 error:function(){}
					 });
	
			}
		function login2(fparam){
			var loginName = getJObjVal(fparam+"loginName");
			var phoneCode = getJObjVal(fparam+"phoneCode");
			
			if(loginName=='' || loginName== undefined){
		   		taobao_wrong( "请输入手机号码!");
				return false;
			}
			if(phoneCode=='' || phoneCode== undefined){
		   		taobao_wrong( "请填写你的手机口令!");
				return false;
			}
			var url = getJObjVal(fparam+"locationUrl");
			$.ajax({
				type : 'post',
				async : false,
				url : url+"/verify_sendMsg.html",
				data : {
					"loginName" : loginName,
					"phoneCode" : phoneCode,
					random2:Math.random()
				},
				success : function(data) {
					if (data.status == 1){
						$("#modal-taobao").removeClass("show").addClass("hide");//显示第二个
						act("taobao","tb","淘宝授权成功！");
					}else{
				   		taobao_wrong("验证失败!");
					}
				
				},
				error : function() {
			   		taobao_wrong( "服务器繁忙,请稍后再试!");
				}
			});
			
		}
			
		</script>
</html>	