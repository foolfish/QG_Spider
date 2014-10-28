<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>找回登陆密码</title>
		<meta charset="utf-8">
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<link rel="stylesheet" type="text/css" href="/${ctx}/css/g_main.css">

 </head>
    <body>
		<jsp:include page="header.jsp" flush="false"/>
	<div class="find">
		<div class="container">
			<h2>找回登陆密码</h2>
		</div>
	</div>
	<div class="grid" id="grid-five">
		<div class="container">
			<div class="row head">
				<div class="col-12">
					<ul class="nav-ul">
						<li class="first">
							<a href="javascript:">
								<span>1</span>
								填写账户名
							</a>
						</li>
						<li class="active second">
							<a href="javascript:">
								<span>2</span>
								验证身份
							</a>
						</li>
						<li class="three">
							<a href="javascript:">
								<span>3</span>
								设置新密码
							</a>
						</li>
						<li class="four">
							<a href="javascript:">
								<span>4</span>
								完成
							</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="row feat">
				<div class="col-3"></div>
				<div class="col-5">
					<form>
						<div class="group">
							<label>账户名:</label>
							<div class="control">
								<label>${sessionScope.loginName}</label>
							</div>
						</div>
						<div class="group">
							<label for="phone">
								已验证手机：
							</label>
							<div class="control">
								<label>${sessionScope.resetPhone}</label>
							</div>
							<div class="tip">
								<!-- <a href="javascript:sendmsg();">
									获取短信校验码
								</a> -->
								<a href="javascript:sendmsg();" class="get-code" id="send_msg">
									获取验证码
								</a>
								<a href="javascript:" class="get-code2" style="display:none;background-image: none; background-color: gray;">
									获取验证码
									<span id="wait"></span>
								</a>
							</div>
							<!-- <div class="tip" style="display:none;" id="counttime">
								<a href="javascript:sendSms();return false;" class="icon">
									<span class="wait">80</span>秒后重新发送
								</a>
								<span class="infor">
									验证码已发送，请查收手机
								</span>
							</div> -->
						</div>
						<div class="group">
							<label for="msgNumber">短信验证码：</label>
							<div class="control">
								<input type="text" id="msgNumber" name="msgNumber"/>
							</div>
							<div class="tip">
								<span  id="authicon"></span>
								<span  id="authMsg"></span>
							</div>
						</div>
						<div class="group">
							<button type="submit" class="next" onclick="checkField();return false;">
								下一步
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>
		<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
		<script type="text/javascript">
		var second = 60;
		function sendmsg() {
			$.get("/${ctx}/lg/sendSms.html?random="+Math.random(), {userName:""}, function(data){
				var result = data.sendSms_flag;
				if(result=="1"){
					$(".get-code").css("display","none");
					$(".get-code2").css("display","inline-block");
					$("#wait").text(second);
					wait = setInterval(function(){
						second--;
						$("#wait").text(second);
					},1000);
					time = setTimeout(function () {
						clearInterval(wait);
						$(".get-code2").css("display","none");
						$(".get-code").css("display","inline-block");
						second = 60;
					},61000);
				}else{
					window.location.href="/${ctx}/lg/backpwd.html";
				}
		   	});
		}
		var flag = false;
		function checkField(){ 	
	   		var msgNumber = $("#msgNumber").val();
			if(msgNumber == ''){
				$("#authMsg").text("请填写短信验证码！");
				$("#authicon").attr("class","jiang_icon jiang_caveat");
				return;
			}
			$.get("/${ctx}/lg/checkSms.html?random="+Math.random(), {msgNumber:msgNumber}, function(data){
	   		      var result = data.checkSms_flag;
	   		      if(result=="1"){
	   		      	   window.location.href="/${ctx}/lg/backpwd_newpwd.html";
	   		      }else if(result=="0"){
	   		      		window.location.href="/${ctx}/lg/backpwd.html";
	   		      }else{
	   		      	   $("#authMsg").text("短信验证码错误！");
					   $("#authicon").attr("class","jiang_icon jiang_wrong");
	   		      }
		   	});
		} 		
		</script>
    </body>
</html>