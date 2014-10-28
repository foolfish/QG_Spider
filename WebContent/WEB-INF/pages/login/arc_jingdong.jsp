<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>
		<c:set var="fparam" value="jingdong"/>
		<input type="hidden" id="${fparam}locationUrl" value="/${ctx}/jd"/>
	<div class="section hide" id="sec-four">
		<form>
			<div class="group">
				<label for="username2">用户名</label>
				<div class="control">
					<input type="text" id="${fparam}loginName" placeholder="请输入京东账号" onblur="getAuths('${fparam}');"/>
					<input type="hidden" id="${fparam}loginName_temp" />
					<input type="hidden" id="${fparam}isShowImg" value="0"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group">
				<label for="password2">
					密码
					<a href="http://safe.jd.com/findPwd/index.action" target="_target" class="forget">
						忘记密码
					</a>
				</label>
				<div class="control">
					<input type="password" id="${fparam}password" />
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group" id="${fparam}isAuthcode">
				<label for="authcode">
					验证码
				</label>
				<div class="row">
					<div class="col-3">
						<div class="control">
							<input type="text" id="${fparam}authcode" />
						</div>
					</div>
					<div class="col-4">
						<div class="pic">
							<img src="" alt="" id="${fparam}imgUrl" />
						</div>
					</div>
					<div class="col-4">
						<div class="pic">
							<a href="javascript:getAuths('${fparam }');">
								<strong>换一张</strong>
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="group wtip">
					<label class="wrong-tip"></label>
				</div>
			<div class="group">
				<button type="button" class="done" id="${fparam}login">
					确认
				</button>
			</div>
		</form>
	</div>
	
	<script>
		$(document).ready(function(){
			 getJObj("${fparam}login").click(function(){
						login("${fparam}");
		 	});
		hideImg("${fparam}");
	});
	
	</script>
	
</html>	