  <%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
	<head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>提供信息的网站</title>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
				<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
		
		<link rel="stylesheet" type="text/css" href="/${ctx}/css/base.css">
		<link rel="stylesheet" type="text/css" href="/${ctx}/css/providewebsite.css">
    </head>
    <body>
    	<div class="base-topbar">
    		<p class="base-topbar-inner">
    			你好，<span id="userName">${loginName}</span>！<a href="/${ctx}/lg/loginout.html">退出</a>
    		</p>
    	</div>
    	<div class="base-main">
    		<div class="base-title clearfix">
    			<h1>提供信息的网站<font style="color:red;">（请在授权的网站前打勾）</font></h1>
    		</div>
			<form class="base-form providewebsite-form"  action="/${ctx}/entranceAll.html"   method="post">
				<div class="base-fieldset clearfix cbWebsiteName">
					<span class="base-fieldset-title">
						<label>电商</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames" value="putong_jd" cblink="cbWebsiteName-jd"/><span>京东</span>
						<input type="checkbox" name="domainNames" value="putong_taobao" cblink="cbWebsiteName-taobao"/><span>淘宝</span>
					</span>
				</div>
				<div class="base-fieldset clearfix cbPhoneNum">
					<span class="base-fieldset-title">
						<label>手机号</label>
					</span>
					<input class="providewebsite-checkbox" type="checkbox" name="domainNames" value="phone" cblink="cbPhoneNum-phone"/>
					<input class="base-fieldset-input providewebsite-input" name="phone" needcheck="phone" checktype="input"  value="${phone }"/>
				</div>
				<div class="base-fieldset clearfix cbXuexin">
					<span class="base-fieldset-title">
						<label>学信</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames" cblink="cbXuexin-xuexin"  value="xuexin"/><span>学信网</span>
					</span>
				</div>
				<div class="base-fieldset clearfix cbEmail">
					<span class="base-fieldset-title">
						<label>邮箱</label>
					</span>
					<input class="providewebsite-checkbox" type="checkbox" name="domainNames" value="email" cblink="cbEmail-email"/>
					<input id="email" name="email" class="base-fieldset-input providewebsite-input" needcheck="email" checktype="input" value="${email }"/>
				</div>
				<div class="base-fieldset clearfix cbZhengxin">
					<span class="base-fieldset-title">
						<label>征信</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames" cblink="cbZhengxin-zhengxin" value="zhengxin"/><span>征信报告</span>
					</span>
				</div>
				<div class="base-fieldset clearfix cbShebao">
					<span class="base-fieldset-title">
						<label>社保</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames" cblink="cbShebao-shebao"  value="shebao" /><span>社保</span>
					</span>
				</div>
				<h2>您最近一个月提交的网站如下，如果有新的信息，可继续填写</h2>
				<c:if test="${not empty dsshow}">
				<div class="base-fieldset clearfix cbWebsiteName">
					<span class="base-fieldset-title">
						<label>电商</label>
					</span>
					<c:if test="${not empty jdshow}">
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames2" value="jd-select" cblink="cbWebsiteName-jd"/><span>京东</span>
					</span>
					<div class="diy_select providewebsite-select2">
						<c:forEach var="user" items="${jdList}" begin="0" end="1" step="1">
         							<c:set var="jdName" value="${user.loginName}"/>
									</c:forEach>
									
						<input type="hidden" name="jd-select"  class="diy_select_input" value="${jdName}"/>
						<div class="diy_select_txt">${jdName}</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	 <c:forEach var="user" items="${jdList}">
						 	<li value="${user.loginName}">${user.loginName}</li>
						 	</c:forEach>
						</ul>
					</div>
					</c:if>
					<c:if test="${not empty taobaoshow}">
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames2" value="taobao-select" cblink="cbWebsiteName-taobao"/><span>淘宝</span>
					</span>
					<div class="diy_select providewebsite-select2">
						<c:forEach var="user" items="${taobaoList}" begin="0" end="1" step="1">
         							<c:set var="taobaoName" value="${user.loginName}"/>
									</c:forEach>
						<input type="hidden" name="taobao-select" class="diy_select_input" value="${taobaoName}"/>
						<div class="diy_select_txt">
							${taobaoName}
							</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	<c:forEach var="user" items="${taobaoList}">
						 	<li value="${user.loginName}">${user.loginName}</li>
						 	</c:forEach>
						</ul>
					</div>
					</c:if>
				</div>
				</c:if>

				<c:if test="${not empty phoneshow2}">
				<div class="base-fieldset clearfix cbPhoneNum">
					<span class="base-fieldset-title">
						<label>手机号</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames2" value="phone2" cblink="cbPhoneNum-phone"/><span>手机</span>
					</span>
					<div class="diy_select providewebsite-select2">
					<c:forEach var="user" items="${phoneList}" begin="0" end="1" step="1">
         							<c:set var="phoneName" value="${user.loginName}"/>
									</c:forEach>
									
						<input type="hidden" name="phone2" class="diy_select_input" value="${phoneName}"/>
						<div class="diy_select_txt">${phoneName}</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	 <c:forEach var="user" items="${phoneList}">
						 	<li value="${user.loginName}">${user.loginName}</li>
						 	</c:forEach>
						</ul>
					</div>
				</div>
				</c:if>

				<c:if test="${not empty xuexinshow}">
				<div class="base-fieldset clearfix cbXuexin">
					<span class="base-fieldset-title">
						<label>学信</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames2" value="xuexin2" cblink="cbXuexin-xuexin"/><span>学信网</span>
					</span>
					<div class="diy_select providewebsite-select2">
					<c:forEach var="user" items="${xuexinList}" begin="0" end="1" step="1">
         							<c:set var="xuexinName" value="${user.loginName}"/>
									</c:forEach>
									
						<input type="hidden" name="xuexin2" class="diy_select_input" value="${xuexinName}"/>
						<div class="diy_select_txt">${xuexinName}</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	 <c:forEach var="user" items="${xuexinList}">
						 	<li value="${user.loginName}">${user.loginName}</li>
						 	</c:forEach>
						</ul>
					</div>
				</div>
				</c:if>

				<c:if test="${not empty emailshow2}">
				<div class="base-fieldset clearfix cbEmail">
					<span class="base-fieldset-title">
						<label>邮箱</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames2" value="email2" cblink="cbEmail-email"/><span>邮箱地址</span>
					</span>
					<div class="diy_select providewebsite-select2">
					
					<c:forEach var="email" items="${emailList}" begin="0" end="1" step="1">
         							<c:set var="emailName" value="${email}"/>
									</c:forEach>
					
						<input type="hidden" name="email2" class="diy_select_input" value="${emailName }"/>
						<div class="diy_select_txt">${emailName }</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	<c:forEach var="email" items="${emailList}">
						 	<li value="${email}">${email}</li>
						 	</c:forEach>
						</ul>
					</div>
				</div>
				</c:if>


				<c:if test="${not empty zhengxinshow}">
				<div class="base-fieldset clearfix cbZhengxin">
					<span class="base-fieldset-title">
						<label>征信</label>
					</span>
					<span class="providewebsite-rightarea">
						<input type="checkbox" name="domainNames2" value="zhengxin2" cblink="cbZhengxin-zhengxin"/><span>征信报告</span>
					</span>
					<div class="diy_select providewebsite-select2">
					<c:forEach var="user" items="${zhengxinList}" begin="0" end="1" step="1">
         							<c:set var="zhengxinName" value="${user.loginName}"/>
									</c:forEach>
									
						<input type="hidden" name="zhengxin2" class="diy_select_input" value="${zhengxinName}"/>
						<div class="diy_select_txt">${zhengxinName}</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						 	 <c:forEach var="user" items="${zhengxinList}">
						 	<li value="${user.loginName}">${user.loginName}</li>
						 	</c:forEach>
						</ul>
					</div>
				</div>
				</c:if>

				
				<c:if test="${not empty shebaoshow2}">
				<div class="base-fieldset clearfix"> 
					<span class="base-fieldset-title">
						<label>社保 </label>
					</span>
					<span class="providewebsite-rightarea cbShebao">
						<input type="checkbox" name="domainNames2" value="shebao2"  cblink="cbShebao-shebao"/><span>社保</span>
					</span>
					<div class="diy_select providewebsite-select2">
					<c:forEach var="user" items="${shebaoList}" begin="0" end="1" step="1">
         							<c:set var="shebaoName" value="${user.loginName}"/>
									</c:forEach>
									
						<input type="hidden" name="shebao2" class="diy_select_input" value="${shebaoName}"/>
						<div class="diy_select_txt">${shebaoName}</div>
						<div class="diy_select_btn"></div>
						<ul class="diy_select_list">
						
						 	<c:forEach var="user" items="${shebaoList}">
						 	  <li value="${user.loginName}">${user.loginName}</li>
						 	</c:forEach>
						</ul>
					</div>
					
				</div>
				</c:if>

				
				<div class="base-fieldset base-form-submit">
					<input id="submitBtn" type="submit" value="下一步" class="base-btn base-fieldset-input" trigger="base-btn-hover"/>
				</div>
				<p class="base-from-console">
					<span class="base-from-wrd"></span>
				</p>
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
				if(!checkForm()){
					return false;
				} else {
					form.submit();
				}
			});
			$.AutoComplete('#email');

			//select单选关联
			$('[cblink]').on('click', function(){
				var obj = $(this);
	            if (obj.attr('checked') == undefined) {
	                obj.attr('checked', false);
	            } else {
	            	var cblink = obj.attr('cblink');
					var parentList = $('.'+cblink.split('-')[0]);
					var cb, parent;
	                for (i = 0; i < parentList.length; i++) {
	                	parent = $(parentList[i]);
	                	cb = parent.find('[cblink=\''+cblink+'\']');
		                if (cb[0] != obj[0]) cb.attr('checked', false);
		                else cb.attr('checked', true);
	                }    
	            }
			});

			//验证方法
			function checkForm(){
				var checkedNum = 0;
				var result = true;
				initCheck();
				$('input[type=\'checkbox\']').each(function(){
					var obj = $(this);
					if(obj.attr('checked') == 'checked'){
						++checkedNum;
						var cbValue = obj.attr('value');
						var parent = obj.parents('.base-fieldset');
						var linkObj = parent.find('[name=\''+cbValue+'\']');
						if(linkObj.length != 0){
							linkObj.each(function(){
								var thisObj = $(this);
								if(thisObj.attr('value') == ''){
									consoleNotnull(parent, '请完善选取的信息');
									result = false;
								}
							});
						}
					}

				});
				if(checkedNum == 0){/* 没有checkbox被选中 */
					consoleMsg('没有可以提交的信息，请勾选至少一项信息');
					result = false;
				}
				return result;
			}

			function initCheck(){
				$('.base-fieldset').removeClass('fc-parent-error');
				$('.base-from-console').css('display','none');
			}

			function consoleMsg(msg){
				$('.base-from-console').css('display','block');
				$('.base-from-wrd').html(msg);
			}

			function consoleNotnull(obj, msg){
				obj.addClass('fc-parent-error');
				if(obj.find('p.base-alert-error').length == 0){
					obj.append($('<p class="base-alert-error"></p>'));
				}
				obj.find('p.base-alert-error').html(msg);
			}
		</script>
    </body>
</html>