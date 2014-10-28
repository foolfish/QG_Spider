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
	<div class="grid" id="grid-six">
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
						<li class="active three">
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
							<label for="new_password">
								<span class="star">*</span>
								新的登录密码
							</label>
							<div class="control">
								<input type="password" id="new_password" name="new_password" />
							</div>
							<div class="tip">
								<span class="icon" id="passwordIcon"></span>
								<span class="infor" id="passwordMsg" >
									</span>
							</div>
						</div>
						<div class="group">
							<label for="new_password2">
								<span class="star">*</span>
								确认新密码
							</label>
							<div class="control">
								<input type="password" id="new_password2" name="new_password2" />
							</div>
							<div class="tip">
								<span class="icon" id="password2Icon"></span>
								<span class="infor" id="password2Msg"></span>
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
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript">
			$("#passwordMsg").text("6-20位字符，可使用字母，数字的组合，不建议使用纯数字字母，区分大小写！");
			var flag = false;
			var reg_pass = /^(\w){6,20}$/;
			function checkMethod(password,type){
				if( password == "" ) {
					$("#"+type+"Icon").attr("class","icon caveat");
				 	$("#"+type+"Msg").text("请填写密码！");
				 	return false;
				}else if( !reg_pass.exec(password) ) {
					$("#"+type+"Icon").attr("class","icon wrong");
					$("#"+type+"Msg").text("密码格式不正确，请重新输入！  6-20位字符，可使用字母，数字的组合，不建议使用纯数字字母，区分大小写！");
					return false;
				} else {
					$("#"+type+"Icon").attr("class","icon right");
					$("#"+type+"Msg").text("");
					return true;
				}
			}
			function checkField(){ 	
				var password = $("#new_password").val();
				var password2 = $("#new_password2").val();
				var check = checkMethod(password2,"password2");
				if(check){
					if(password!=password2){
						$("#password2Icon").attr("class","icon wrong");
						$("#password2Msg").text("两次输入的密码不一致！");
					}else{
						$.post("/${ctx}/lg/backpwdForm.html?random="+Math.random(), {new_password:password}, function(data){
						  var result = data.flag;
			   		      if(result=="1"){
			   		    	  window.location.href="/${ctx}/lg/backpwd_suc.html";		
			   		      }else{
			   		    	  window.location.href="/${ctx}/lg/backpwd.html";
			   		      }
			   	    });
					}
				}
			}
			
			$("#new_password").on("blur", function () {
				var password = $("#new_password").val().trim();	
				checkMethod(password,"password");
			});
		</script>
    </body>
</html>