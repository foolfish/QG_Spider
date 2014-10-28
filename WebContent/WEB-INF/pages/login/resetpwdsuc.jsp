<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<html>
<head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>修改密码</title>
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
			<h2>修改密码</h2>
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
								设置新密码
							</a>
						</li>
						<li class="active second">
							<a href="javascript:">
								<span>2</span>
								完成
							</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="row feat">
				<div class="col-3"></div>
				<div class="col-1">
					<img src="${imgPath}/img/pro-009.png" alt="" />
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
	<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>	
		
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script>
		if($(window).height() >= 700) {
			$("#grid-seven").height($(window).height()-190);
		}
		
		</script>
    </body>
</html>