<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<html>
<head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<meta charset="utf-8">
<title>用户登录</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<link rel="stylesheet" type="text/css" href="/${ctx}/css/g_main.css">
<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
</head>
   <body>
	<jsp:include page="header.jsp" flush="false"/>
	<div id="grid-first">
		<div class="container">
			<div class="row">
				<div class="col one">
					<div class="index-img-zero">
						<img src="/${ctx}/img/pro4-6.jpg" alt="" />
					</div>
					<div class="index-img">
						<img src="/${ctx}/img/pro4-3.jpg" alt="" />
					</div>
					<div class="index-img" style="display:none;">
						<img src="/${ctx}/img/pro4-4.jpg" alt="" />
					</div>
					<div class="index-img" style="display:none;">
						<img src="/${ctx}/img/pro4-5.jpg" alt="" />
					</div>
					<div class="index-img" style="display:none;">
						<img src="/${ctx}/img/pro4-7.jpg" alt="" />
					</div>
				</div>
				<div class="col two">
					<form action="">
						<div class="group">
							<label for="username">用户名</label>
							<div class="control">
								<input type="text" id="username" placeholder="请输入您的用户名" />
							</div>
						</div>
						<div class="group">
							<label for="password">密码</label>
							<div class="control">
								<input type="password" id="password" placeholder="请输入您的密码" />

								<a href="backpwd.html" class="forget"><span>&gt;</span>忘记密码</a>
							</div>
						</div>
						<div class="group wtip">
							<label class="wrong-tip"></label>
						</div>
						<div class="group">
							<button class="login" id="login" onclick="checkField();return false;">
								登录
							</button>
						</div>
						<div class="group last-group">
							<strong>
								还没有账户？&nbsp;立即
							</strong>
							<a href="register.html" class="re">免费注册</a>
						</div>
					</form>
				</div>
				
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>
	<script>
	var reg_pass = /^(\w){6,20}$/;
	if($(window).height() >= 700) {
		 $("#grid-first").height($(window).height()-111);
	}
	
	 
	 var index_img = 1;
	 setInterval(function () {
		 $(".index-img").each(function (i) {
			 $(this).fadeOut(500);
			 if( index_img == i ) {
				 $(this).fadeIn(500);
			 }
		 });
		 if( index_img == 3 ) {
			 index_img = 0;
		 } else {
			 index_img++;
			 
		 }
	 },2400);

	 var g_make ;
	 function g_wrong (node,text) {
		clearTimeout(g_make);
		node.find(".wrong-tip").text(text);
		node.find(".wtip").slideDown("fast");
		g_make = setTimeout(function () {
			node.find(".wtip").slideUp("fast");
		}, 2000);
	 }
	 
	 /* String.prototype.trim=function(){ return this.replace(/(^\s*)|(\s*$)/g, ""); }; */
	
		function checkField(){
			var name=$("#username").val();
			var word=$("#password").val();
			if(name.trim()==""){
				g_wrong($("#grid-first"),"请填写用户名");
				$("#username").focus();
				return false;
			}
			if(word.trim()==""){
				g_wrong($("#grid-first"),"请填写密码");
				$("#password").focus();
				return false;
			}
			$.post("/${ctx}/lg/loginForm.html?random="+Math.random(), {userName:name,password:word}, function(data){
				  var result = data.flag;
	   		      if(result=="1"){
	   		    	  window.location.href="/${ctx}/lg/arc_all.html";		
	   		      }else if(result=="3"){
	   		    	 $("#message").html('<span id="formConsole" class="login-form-error base-error">邀请码一天有效期已过，请重新注册！</span>').show();
	   		      }else{
	   		    	  //$("#message").html('<span id="formConsole" class="login-form-error base-error">用户名或者密码错误！</span>').show();
					g_wrong($("#grid-first"),"用户名或密码填写错误");
	   		      }
	   	    });
		}

		</script>
</body>
</html>	