<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>
		<c:set var="fparam" value="zhengxin"/>
		<input type="hidden" id="${fparam}locationUrl" value="/${ctx}/zhengxin"/>
	<div class="section hide" id="sec-one">
		<form>
			<div class="group">
				<label for="username">用户名</label>
				<div class="control">
					<input type="text" id="${fparam}loginName"  onblur="getAuths('${fparam}');"/>
					<input type="hidden" id="${fparam}loginName_temp" />
					<input type="hidden" id="${fparam}isShowImg" value="0"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group">
				<label for="password">
					密码
					<a href="https://ipcrs.pbccrc.org.cn/"  target="_target" class="forget">
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
			<div class="group">
				<label for="code">查询码</label>
				<div class="control">
					<input type="text" id="${fparam}code" />
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group"  id="${fparam}isAuthcode">
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
			<div class="group last-group">
				<strong>
					还没有征信账号？&nbsp;立即
				</strong>
				<a href="https://ipcrs.pbccrc.org.cn/" target="_target" class="re">免费注册</a>
			</div>
		</form>
	</div>
	
	<script>
	
	
		$(document).ready(function(){
			 getJObj("${fparam}login").click(function(){
						loginZhengxin("${fparam}");
		 	});
		hideImg("${fparam}");
	});
	function loginZhengxin(fparam){
			var loginName = getJObjVal(fparam+"loginName");
			var password = getJObjVal(fparam+"password");
			var spassword = getJObjVal(fparam+"code");
			var authcode = getJObjVal(fparam+"authcode");
			
			if(loginName=='' || loginName== undefined){
		   		gg_wrong($("#sec-one"),"请输入手机号码");
		   		return false;
			}
			if(password=='' || password== undefined){
		   		gg_wrong($("#sec-one"),"请输入密码");
				return false;
			}
			if(spassword=='' || spassword== undefined){
		   		gg_wrong($("#sec-one"),"请输入征信查询码");
				return false;
			}
			if(isShowImg(fparam)){
				if(authcode==''||authcode == undefined){
			   		gg_wrong($("#sec-one"),"请填写验证码");
					return false;
				}
			}
			var url = getJObjVal(fparam+"locationUrl");
			$.ajax({
				type : 'post',
				async : false,
				url : url+"/verifyLogin.html",
				data : {
					"loginName" : loginName,
					"password" : password,
					"spassword":spassword,
					"authcode": authcode,
					random2:Math.random()
				},
				success : function(data) {
					if (data.status == 1){
						 act("zhengxin","zx","征信填写完成！");
					}else{
					   		gg_wrong($("#sec-one"),data.errorMsg);
							showImgs(fparam,data.url);
					}
				
				},
				error : function() {
			   		gg_wrong($("#sec-one"),"服务器繁忙,请稍后再试");
				}
			});
	
	
	}
	</script>
</html>	