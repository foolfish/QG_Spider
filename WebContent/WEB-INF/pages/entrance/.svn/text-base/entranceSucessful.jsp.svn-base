<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>网站登录</title>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<meta name="viewport" content="width=device-width, user-scalable=no , initial-scale=1.0">
<link rel="stylesheet" href="/${ctx }/css/base.css" type="text/css" media="screen"/>
<link rel="stylesheet" href="/${ctx }/css/success.css" type="text/css" media="screen"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="/${ctx }/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/${ctx }/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/${ctx }/js/jquery.json-2.2.min.js"></script>
</head>
<body>
   	<div class="base-topbar">
   		<p class="base-topbar-inner">
   			你好，<span id="userName">${loginName}</span>！<a href="/${ctx}/lg/loginout.html">退出</a>
   		</p>
   	</div>
    	<div class="base-main">
    		<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>成功</span>
    		</p>
    		<div class="success-title clearfix">
    			<h1>成功</h1>
    		</div>
    		<div class="clearfix">
				<p class="success-info">您的信息正在收集中，请耐心等待，您的报告编号是<span class="success-info-reportnum">${markId}</span>，继续努力！</p>
				<div class="success-submit">
					<input type="button" value="返回首页" class="success-btn success-btn-back" trigger="success-btn-hover" onclick="javasript:location.href='/${ctx}/entrance.html'"/>
					<input type="button" value="退出" class="success-btn success-btn-quit base-nextstep" trigger="success-btn-hover" onclick="javascript:location.href='/${ctx}/lg/loginout.html'"/>
				</div>
			</div>
    	</div>
    </body>
</html>