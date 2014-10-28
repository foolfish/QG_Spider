<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>基本信息</title>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
				<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
		<link rel="stylesheet" type="text/css" href="/${ctx}/css/base.css">
		<link rel="stylesheet" type="text/css" href="/${ctx}/css/baseinfo.css">
    </head>
    <body>
    	<div class="base-topbar">
    		<p class="base-topbar-inner">
    			你好，<span id="userName">${loginName}</span>！<a href="/${ctx}/lg/loginout.html">退出</a>
    		</p>
    	</div>
    	<div class="base-main baseinfo">
    		<div class="base-title clearfix">
    			<h1>基本信息<font color="red">(请填写正确信息，否则一旦查实与事实不符，您的信用等级将会降低)</font></h1>
    		</div>
			<form class="base-form" action="/${ctx}/saveBaseInfo.html" method="post" >
				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title" require>
						<label>姓名</label>
					</span>
					<input needcheck="userName" checktype="input" name="realname" class="base-fieldset-input"  id="realname" value="${realName}" notnull/>
				</div>
				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title" require>
						<label>身份证</label>
					</span>
					<input needcheck="userId"  name="idcard" id="idcard" size="30"  class="base-fieldset-input"  value="${idcard}" checktype="input" notnull/>
				</div>
				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title" require>
						<label>邮箱</label>
					</span>
					<input id="email" needcheck="email" name="email"  value="${email}" class="base-fieldset-input"  checktype="input" notnull/>
				</div>
				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title">
						<label>QQ</label>
					</span>
					<input needcheck="QQ" checktype="input"  name="qq" id="qq" class="base-fieldset-input"  size="30" value="${qq}"/>
				</div>

				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title" require>
						<label>社保所在地</label>
					</span>
					<div class="diy_select" checktype="select" notnull>
						<input type="hidden" name="shebao" value="${shebao}" class="diy_select_input" />
						<div class="diy_select_txt">
			
						<c:if test="${shebao eq 'shanghai_shebao'}">上海社保</c:if>
						<c:if test="${shebao eq 'shenzhen_shebao'}">深圳社保</c:if>
						<c:if test="${shebao eq 'qita_shebao'}">其他</c:if>
						<c:if test="${(shebao ne 'shanghai_shebao') && (shebao ne 'shenzhen_shebao') && (shebao ne 'qita_shebao')}"> --请选择--</c:if>
						</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	<li value="shanghai_shebao">上海社保</li>
						 	<li value="shenzhen_shebao">深圳社保</li>
						 	<li value="qita_shebao">其他</li>
						</ul>
					</div>
				</div>
				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title" require>
						<label>住房产权</label>
					</span>
					<div class="diy_select" checktype="select" notnull>
						<input type="hidden" name="hourse"  value="${hourse}" class="diy_select_input"/>
						<div class="diy_select_txt">
						<c:if test="${hourse eq '1'}">完全产权房</c:if>
						<c:if test="${hourse eq '2'}">按揭购房</c:if>
						<c:if test="${hourse eq '3'}">经济适用房</c:if>
						<c:if test="${hourse eq '4'}">租房</c:if>
						<c:if test="${hourse eq '5'}">和亲戚合住</c:if>
						<c:if test="${hourse eq '6'}">无房</c:if>
				<c:if test="${(hourse ne '1') && (hourse ne '2') && (hourse ne '3')&& (hourse ne '4')&& (hourse ne '5')&& (hourse ne '6')}">--请选择--</c:if>
					

						</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">					 	
						 	<li value = "1" >完全产权房</li>        
							<li value = "2" >按揭购房</li>        
							<li value = "3" >经济适用房</li>  
							<li value = "4" >租房</li>    
							<li value = "5" > 和亲戚合住 </li>    
							<li value = "6" > 无房</li>      

						</ul>
					</div>
				</div>
				<div class="base-fieldset clearfix">
					<span class="base-fieldset-title" require>
						<label>车辆情况</label>
					</span>
					<div class="diy_select" checktype="select" notnull>
						<input type="hidden" name="cars"  value="${cars}" class="diy_select_input"/>
						<div class="diy_select_txt">
						<c:if test="${cars eq '1'}">完全产权营运车</c:if>
						<c:if test="${cars eq '2'}">按揭营运车</c:if>
						<c:if test="${cars eq '3'}">完全产权轿车</c:if>
						<c:if test="${cars eq '4'}">按揭轿车</c:if>
						<c:if test="${cars eq '5'}">无 </c:if>
	<c:if test="${(cars ne '1') && (cars ne '2') && (cars ne '3')&& (cars ne '4')&& (cars ne '5')}">--请选择--</c:if>
						</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
							<li value = "1" >完全产权营运车</li>        
							<li value = "2" >按揭营运车</li>        
							<li value = "3" >完全产权轿车</li>  
							<li value = "4" >按揭轿车</li>    
							<li value = "5" > 无 </li>      
						</ul>
					</div>
				</div>
				<div class="base-fieldset base-form-submit">
					<input id="submitBtn" type="button" value="保存" class="base-btn" trigger="base-btn-hover"/>
				</div>
			
			</form>
    	</div>
    	<script type="text/javascript" src="/${ctx}/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript" src="/${ctx}/js/selectTool.js"></script>
		<script type="text/javascript" src="/${ctx}/js/autoComplete.js"></script>
		<script type="text/javascript">
			var form = $('.base-form');
			var formCheck = form.formCheck();
			$('#submitBtn').on('click', function(){
				if(!formCheck.checkForm()){
					return;
				} else {
					form.submit();
				}
			});
			$.AutoComplete('#email');
		</script>
    </body>
</html>