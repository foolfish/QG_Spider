<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>


	<div class="section hide" id="sec-seven">
		<form>
			<div class="group">
				<label for="uedPhone">手机号码</label>
				<div class="control">
					<input type="text" id="uedPhone" placeholder="请输入实名认证的手机号码" onblur="getAuth()"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group has-error" style="display:none" id="phoneno_tip"></div>
			<div class="group">
				<label for="uedPassword">
					<font id="passName">密码</font>
					<a href="javascript:" class="forget" id="forgetPassUrl" target="_blank" style="display:none">
						忘记密码
					</a>
				</label>
				<div class="control">
					<input type="password"  id="uedPassword"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group has-error" style="display:none" id="passowrd_tip"></div>
			<div class="group" id="authCodeDiv" style="display:none">
				<label for="authCode">
					验证码
				</label>
				<div class="row">
					<div class="col-3">
						<div class="control">
							<input type="text"  name="authCode" id="authCode"/>
						</div>
					</div>
					<div class="col-4">
						<div class="pic">
						    <img onclick="getAuth()" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="authcode_img"/>
						</div>
					</div>
					<div class="col-4">
						<div class="pic">
							<a href="javascript:getAuth();">
								<strong>换一张</strong>
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="group wtip">
				<label class="wrong-tip"></label>
			</div>
			<div class="group has-error" style="display:none" id="authCode_tip"></div>
  		    <div id="message" style="display:none;" class="group has-success" errMsg="true"></div>
			<div class="group">
				<button type="button" class="done" onclick="checkField();return false;">
					确认
				</button>
			</div>
			<div class="group last-group" style="display:none;">
				<strong>
					还没有注册<span>密码</span>？&nbsp;立即
				</strong>
				<a href="" class="re" target ="_target" id="shouji_register">注册</a>
			</div>
			  <input type="hidden" name="resultCode" id="resultCode" value=""/>
  			  <input type="hidden" name="isSecond" id="isSecond" value=""/>
		</form>
	</div>

	<div class="modal hide" id="modal-com">
		<div class="modal-dialog" id="dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4>手机验证</h4>
					<a href="javascript:closecom();">
						<img src="/${ctx}/img/pro-031.png" alt="" class="cross"/>
					</a>	
				</div>
				<div class="modal-body">
					<form>
						<div class="group" id="authCodeDiv2" style="display:none">
							<label for="authCode2">
								验证码
							</label>
							<div class="control">
								<input type="text" name="authCode2" id="authCode2"/>
							</div>
							<div class="pic-reg">
								<img onclick="getAuth(false, 2)" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="authcode_img2"/>
							</div>
							<div class="tip">
								<a class="" href="javascript:getAuth(false, 2);">
									<strong class="change">换一张</strong>
								</a>
							</div>	
						</div>
						 <div class="group has-error" style="display:none" id="authCode_tip2"></div>
						<div class="group">
							<label for="reg2">请输入手机验证码</label>
							<div class="control">
								<input type="text" name="smsCode" id="smsCode"/>
							</div>
							<div class="tip">
								<a href="javascript:void(0);" class="get-code" onclick="getSms();">
									获取动态密码
								</a>
							</div>				
						</div>
						<div class="group wtip">
							<label class="wrong-tip"></label>
						</div>
						<div class="group has-error" style="display:none" id="smsCode_tip"></div>
  						<div id="message2" style="display:none;" class="form-group has-success" errMsg="true"></div>
						<div class="group">
							<button type="button" class="done" onclick="checkField();return false;">
								确认
							</button>
						</div>
						 <input type="hidden" name="resultCode2" id="resultCode2" value=""/>
					</form>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
var issubmit = false;
function firstStep() {
	$("#isSecond").val("0");
	$("#secondForm").hide();
	$("#firstForm").show();
	getAuth();
	$("div[errMsg=true]").hide();
}
function nextStep() {
	$("#isSecond").val("1");
	$("div[errMsg=true]").hide();
	  //$('.modal').modal('hide');
	  $('#modal-com').removeClass("hide").addClass("show");
	  $('.modal-bg-two').css("display", "block");
	$("#firstForm").hide();
	//getAuth();
}

var auth_index = 1;
var isAuth = false;
function showMessage(data, t) {
	if (data.errorMsg && data.errorMsg != "") {
		/* var mess = $("#message" + (t && t > 0 ? t+"" : ""));
		mess.html("<code  style=\"color: red;\">" + data.errorMsg + "</code>"); */
		//mess.addClass("label-danger");
		//<code style="color: red;">手机号码错误</code>
		/* mess.show(); */
		gg_wrong($("#sec-seven"),data.errorMsg);
	}
}
function showMessage2(data, t) {
	if (data.errorMsg && data.errorMsg != "") {
		gg_wrong($("#modal-com"),data.errorMsg);
	}
}
function getAuth(isCheckField, at){
	if (isAuth) {
		return;
	}
	var suffix = "";
	if (at) {
		suffix = at + "";
	} 
	isAuth = true;
	var phone = $("#uedPhone").val();
	if (phone == "") { isAuth = false;return;}
	$("div[errMsg=true]").hide();
	$.ajax({
	     type:'post',
         async:true,
         data:{phone:phone, index:auth_index},
	     url: at ? '/${ctx}/pt_ued_phone_img2.html' : '/${ctx}/pt_ued_phone_img.html',
	     success : function(data) {
	     		if (data.title && data.title != "") {
					$('.modal-header .modal-title').html("<code>" + data.title + "</code>");
				}
	     		if(data.forgetPassUrl && data.forgetPassUrl !=""){
	     			var furl = $("#forgetPassUrl");
	     			furl.attr("href", data.forgetPassUrl);
	     			furl.show();
	     			$("#shouji_register").attr("href", data.forgetPassUrl);
	     		}
	
	     		if(data.passName && data.passName !=""){
	     			$("#passName").text(data.passName);
	     			$(".last-group span").text(data.passName);
	     			$(".last-group").css("display","block");
	     		} else {
	     			$("#passName").text("密码");
	     			$(".last-group span").text("密码");
	     			$(".last-group").css("display","block");
	     		}
	     		
				if (data.success) {
					var url = data.imgUrl;
					showImg(url, suffix, isCheckField)
				} 
				showMessage(data, at);
				isAuth = false;
				auth_index++;
			},
			error : function() {
				$("#resultCode").val("0");
				showMessage(data, at);
				isAuth = false;
			}
		});

	}
	function showImg(url, suffix, isCheckField) {
		if (url == null) {
			return;
		}
			
				if (url && url != 'none' && url != 'null' && url != '') {
						$("#authCodeDiv" + suffix).show();
						$("#authcode_img" + suffix).attr("src", "${imgPath}/img/icon_1.jpg");
						//setTimeout(function() {
					    $("#authcode_img" + suffix).attr("src", '' + url + "?d=" + new Date().getTime());
						//}, 100);

						$("#resultCode" + suffix).val("1");
					} else {
						$("#resultCode" + suffix).val("0");
						document.getElementById("authCode" + suffix).value = "";
						//alert($("#authCode").val());
						$("#authCodeDiv" + suffix).hide();
						if (isCheckField) {
							checkField();
						}
					}
	}
	function checkField() {
		var flag = true;
		/* $("#uedPassword").each(function () { 
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
	        }); */
		if ("" == $("#resultCode").val()) {		
/* 			
			var mess = $("#message");
			mess.text("正在加载验证码，请稍等……");
			mess.slideDown("fast");;
			getAuth(true);
			g_make = setTimeout(function () {
				mess.slideUp("fast");
			}, 2000); */
			gg_wrong($("#sec-seven"),"请输入手机号码");
			return;
		}
		if (flag == true) {
			$("#message").hide();
			var isSecond = $("#isSecond").val();
			if (isSecond != null && isSecond == "1") {
				/* $("#message").text("正在努力登录中，请稍等……"); */
				/* $("#message").show(500, secondSubmit); */
				secondSubmit();
			} else {
				/* $("#message").text("正在努力登录中，请稍等……"); */
				/* $("#message").show(500, firstSubmit); */
				firstSubmit();
			}
		}
	}
	var secondSms = false;
	function firstSubmit() {
		if (issubmit) {
			return;
		}
		issubmit = true;
		var verifyUrl = '/${ctx}/pt_ued_phone_login.html';
		var phoneValue = $("#uedPhone").val();
		//var wzpassword = $("#wzpassword").val();
		var fwpassword = $("#uedPassword").val();
		var authCode = $("#authCode").val();
		var checkCode = $("#resultCode").val();

		$("#password_tip").hide();
		$("#password_tip1").hide();
		$("#authcode_tip").hide();
		$("#authcode_message").hide();
		$("#message").text("");
		if(phoneValue=='' || phoneValue== undefined){
		    /* $("#phoneno_tip").text("请输入手机号码");
		    $("#phoneno_tip").show(); */
		    gg_wrong($("#sec-seven"),"请输入手机号码");
		    issubmit = false;
			return false;
		} else {
			/* $("#phoneno_tip").hide(); */
		}
		if (fwpassword == '' || fwpassword == undefined) {
			/* $("#passowrd_tip").text("请输入服务密码");
			$("#passowrd_tip").show(); */
			gg_wrong($("#sec-seven"),"请输入服务密码");
			issubmit = false;
			return false;
		} else {
			/* $("#passowrd_tip").hide(); */
		}
		
		if (checkCode == "1" && (authCode == '' || authCode == undefined)) {
			/* $("#authCode_tip").text("请输入验证码");
			$("#authCode_tip").show(); */
			gg_wrong($("#sec-seven"),"请输入验证码");
			issubmit = false;
			return false;
		} else {
			/* $("#authCode_tip").hide(); */
		}
		$("div[errMsg=true]").hide();
		$.ajax({
			type : 'post',
			async : false,
			url : verifyUrl,
			data : {
				phone : phoneValue,
				fwpassword : fwpassword,
				authCode : authCode
			},
			success : function(data) {
				// $("#message").hide();
				if (data.success) {
					/* if (data.status == 1){//} && data.flag1 == 'false') {
						alert("获取部分信息失败.");
						//location.reload();
						//getAuth();
					} else */
					if (data.status == 1){//} || data.flag == 999) {
						issubmit = false;
						if (data.result) {
							finish();
						} else {
							$("#isSecond").val("1");
							 $('#modal-com').removeClass("hide").addClass("show");
							 $('.modal-bg-two').css("display", "block");
							$("#firstForm").hide();
							var url = data.imgUrl;
							if (url && url != 'none' && url != 'null') {
								getAuth(true, 2);
								$("#resultCode2").val(1);
								$("#authCodeDiv2").show();
								//secondSms = true;
							} else {
								$("#authCodeDiv2").hide();
							}
						}
						//getSms();
						//$("#spid").val(data.spid);
						//window.location.href="/${ctx}/entranceEmail.html";
					} else {
						if (data.errorMsg) {
						} else {
							gg_wrong($("#sec-seven"),"登录失败,密码或验证码错误");
						}
						showImg(data.imgUrl, "", false)
						$("#resultCode").val()
						//$("#message").attr("style", "dislpay:none;");
						getAuth();
					}
				}
				showMessage(data);
				issubmit = false;
			},
			error : function() {
				showMessage(data);
				issubmit = false;
			}
		});
	}
	function getSms(){
		var checkCode = $("#resultCode2").val();
		var authCode = '';
		if (checkCode == "1") {
			authCode = $("#authCode2").val();
			if ((authCode == '' || authCode == undefined)) {
				$("#authCode_tip2").text("请输入验证码");
				$("#authCode_tip2").show();
				return false;
			}
		}
		var phoneValue = $("#uedPhone").val();
		$("div[errMsg=true]").hide();
		$.ajax({
	     	type:'post',
       		async:false,
	     	url:'/${ctx}/pt_ued_phone_sms.html',
	     	data:{phone:phoneValue, authCode:authCode},
	     	success : function(data) {
	     		if (data.success) {
					if (data.status == 1) {
						gg_wrong($("#modal-com"),"获取手机验证码成功");
					} else {
						if (data.errorMsg) {
						} else {
							gg_wrong($("#modal-com"),"获取手机验证码失败");
						}
					}
				}
				if (data.errorMsg && data.errorMsg != "") {
					clearTimeout(g_make);
					$("#modal-com").find(".wrong-tip").text(data.errorMsg);
					$("#modal-com").find(".wtip").slideDown("fast");
					g_make = setTimeout(function () {
						$("#modal-com").find(".wtip").slideUp("fast");
					}, 10000);
				}
	    	 },
  	 		error:function(){}
	     });
	}
	function secondSubmit(){
		if (issubmit) {
			return;
		}
		issubmit = true;
		var verifyUrl='/${ctx}/pt_ued_phone_service.html';
		var smsCode = $("#smsCode").val();
		var checkCode = $("#resultCode2").val();
		var authCode = $("#authCode2").val();
		if (checkCode == "1" && (authCode == '' || authCode == undefined)) {
			/* $("#authCode_tip2").text("请输入验证码");
			$("#authCode_tip2").show(); */
			gg_wrong($("#modal-com"),"请输入验证码");
			return false;
		}
		if ((smsCode == '' || smsCode == undefined)) {
			/* $("#smsCode_tip").text("请输入手机验证码");
			$("#smsCode_tip").show(); */
			gg_wrong($("#modal-com"),"请输入手机验证码");
			return false;
		}
	    //var smsAuthCode = $("#smsAuthCode").val();
	    //var spid = $("#spid").val();
	    var phone = $("#uedPhone").val();
	    $("div[errMsg=true]").hide();
		$.ajax({
		     type:'post',
	         async:false,                                                            
		     url:verifyUrl,
		     data:{smsCode:smsCode,phone:phone,authCode:authCode},
		     success : function(data) {
			     
		    	 //$("#message").hide();		    	 
		    	 if (data.success) {
						if (data.status == 1 && data.flag1 == 'false') {
							/* alert("获取部分信息失败."); */
							gg_wrong($("#modal-com"),"获取部分信息失败");
							location.reload();
							//getAuth();
						} else if (data.status == 1 || data.flag == 999) {							
							issubmit = false;
							if (data.result) {
								finish();
							} else {
								
							}
							
						} else {
							if (data.errorMsg) {
							} else {
								/* alert("登录失败,密码或验证码错误."); */
								gg_wrong($("#modal-com"),"登录失败,密码或验证码错误");
							}
							
							
							//$("#message").attr("style", "dislpay:none;");
							//getAuth();
						}
					}
		    		var url = data.imgUrl;
		    		if (url && url != 'none' && url != 'null') {
						getAuth(true, 2);
					} 
					showMessage2(data, 2);
					issubmit = false;
				  
		    	},
		    	 error:function(){
		    		 showMessage2(data, 2);
					 issubmit = false;
		    	 }
		     });
	}
	function finish() {
		 $('#modal-com').removeClass("show").addClass("hide");
		
		 act("shouji","sj","手机运营商授权成功！");
		 count_red();

		//location.reload();
		//window.location.href="/${ctx}/pt_ued_fnc_list.html";
	}	
</script>
</html>	