<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
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
	<div class="grid" id="grid-seven">
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
						<li class="second">
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
						<li class="active four">
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
				<div class="col-1">
					<img src="../img/pro-009.png" alt="" />
				</div>
				<div class="col-4">
					<h4>恭喜您，新登录密码设置成功！</h4>
					<p>今后将使用新密码来登陆账户，请牢记。</p>
					<!-- <strong>
						<span>您可能需要：</span>
						<a href="login.html">重新登录</a>
					</strong> -->
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>
		<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript">
		if($(window).height() >= 700) {
			$("#grid-seven").height($(window).height()-190);
		}
		
		$('.backpwd-form').formCheck();
			function checkField(){ 	
			var userName = $("#userName").val();
			var new_password = $("#new_password").val();
			$.post("/${ctx}/lg/backpwdForm.html?random="+Math.random(), {userName:userName,new_password:new_password}, function(data){
				  var result = data.flag;
	   		      if(result=="1"){
	   		    	  window.location.href="/${ctx}/lg/login.html";		
	   		      }else{
	   		    	  $("#message").html('<span id="formConsole" class="login-form-error base-error">用户名或者密码错误！</span>').show();
	   		      }
	   	    });
		}
		</script>
    </body>
</html>