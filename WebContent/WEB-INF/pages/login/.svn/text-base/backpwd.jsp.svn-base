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
	<div class="grid" id="grid-four">
		<div class="container">
			<div class="row head">
				<div class="col-12">
					<ul class="nav-ul">
						<li class="active first">
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
							<label for="username">账户名</label>
							<div class="control">
								<input type="text" id="username" name="username" placeholder="请输入您的用户名" />
							</div>
							<div class="tip">
								<span class="icon" id="usericon"></span>
								<span class="infor" id="userMsg"></span>
							</div>
						</div>
						<div class="group">
							<label for="authcode">
								验证码
							</label>
							<div class="control">
								<input type="text" id="authcode" name="authcode"/>
							</div>
							<div class="tip">
								<span class="icon" id="authicon"></span>
								<span class="infor" id="authMsg"></span>
							</div>
							<div class="pic">
								<img id="authImg" onclick="javascript:refresh(this);" src="/${ctx}/imageServlet" alt="验证码""/>
								<a href="javascript:refresh(this);">
									<strong>换一张</strong>
								</a>
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
		<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>
		
		<script type="text/javascript">
		//$('#username').val("");
		//$('#authcode').val("");
		function refresh(obj) {
			$("#authImg").attr("src","/${ctx}/imageServlet?"+Math.random());
		}
		var flag1 = false;
		var flag2 = false;
		var flag = false;
		//用户名验证
		$('#username').on('blur', function checkUser(){
			var loginName = $('#username').val();
			if(loginName == ''){
				$("#userMsg").text("请填写用户名！");
				$("#usericon").attr("class","icon caveat");
				return;
			}
			$.get('/${ctx}/lg/checkLoginName.html?random='+Math.random(),{'userName' : loginName},function(data){
				 var result = data.loginflag;
				if(result == "0"){
					$("#userMsg").text("此用户名不存在！");
					$("#usericon").attr("class","icon wrong");
					flag1 = false;
				}else{
					$("#usericon").attr("class","icon right");
					flag1 = true;
				}
			});
		});
		
		function checkField(){ 	
			var username = $('#username').val();
			if(flag1==true){
				var authcode = $('#authcode').val();
				if(authcode == ''){
					$("#authMsg").text("请填写验证码！");
					$("#authicon").attr("class","icon caveat");
					return;
				}
				$.get('checkAuthCode.html?random='+Math.random(),{'authcode' : authcode},function(data){
					var result = data.authflag;				 
					if(result == "0"){
						$("#authMsg").text("验证码错误！");
						$("#authicon").attr("class","icon wrong");
					}else{
						$.get("putLoginNameSession.html?random="+Math.random(),{loginName : username},function(data){
							 var result = data.flag;
							if(result == "1"){
								$('#username').val("");
								$('#authcode').val("");
								window.location.href="/${ctx}/lg/backpwd_msgcheck.html";
							}else{
								$("#authMsg").text("登录异常，请重试！");
								$("#authicon").attr("class","icon wrong");
							}
			   			});
					}
				});
			}else{
				if(username == ''){
					$("#userMsg").text("请填写用户名！");
					$("#usericon").attr("class","icon caveat");
				}else{
					$("#userMsg").text("此用户名不存在！");
					$("#usericon").attr("class","icon wrong");
				}
			}
			}
		 var imgs = ["/${ctx}/img/pro-005.png"];
			imgload(imgs);	
		</script>
    </body>
</html>