<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
String.prototype.trim=function(){ return this.replace(/(^\s*)|(\s*$)/g, ""); };
</script>
<div class="header">
	<div class="container">
		<div class="row" style="overflow: visible;">
			<%boolean islogin= request.getSession().getAttribute("currentUser")!=null&&!((String)request.getSession().getAttribute("currentUser")).equals(""); 
				request.setAttribute("islogin", islogin);
			%>
			<c:choose><c:when test="${islogin eq true}">
		
				<div class="col-6" onclick="javascript:location.href='/${ctx}/lg/arc_all.html'" style="padding-left:0px;">
				<h1></h1>
				<div class="logo"></div>
				</div>
			<div class="col-4" style="width:390px;">
			
				<strong>您好！<%= islogin==true?(String)request.getSession().getAttribute("loginName")+",":""%>欢迎来到量化派！</strong>
			</div>
			<div class="col-2" style="width:110px;">
				<a href="/${ctx}/lg/loginout.html">
					<span class="link">[退出]</span>
				</a>
				<span class="i-gray"></span>
				<div class="down">
					<label class="link">设置</label>
					<ul class="for-down">
						<li><a href="/${ctx}/lg/resetpwd.html">修改密码</a></li>
					</ul>
				</div>
			</div>
			</c:when><c:otherwise>
		
			<div class="col-6" onclick="javascript:location.href='/${ctx}/lg/arc_all.html'" style="padding-left:0px;">
					<h1></h1>
					<div class="logo"></div>
				</div>
				<div class="col-4"></div>
				<div class="col-2">
					<a href="/${ctx}/lg/login.html">
						<span class="link" style="color:#000;font-size:17px;">登录</span>
					</a>
					<span>|</span>
					<a href="/${ctx}/lg/register.html">
						<span class="link" style="color:#000;font-size:17px;">注册</span>
					</a>
				</div>
			</c:otherwise></c:choose> 
			
		
			
			
				<div style="clear:both"></div>
				
		</div>
	</div>

		<div class="head-back"></div>
		
</div>