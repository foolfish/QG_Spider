<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>

		<div class="section hide" id="sec-two">
			<form>
				<div class="group">
					<label for="xuexin_userName1">用户名</label>
					<div class="control">
						<input type="text" id="xuexin_userName1" name="xuexin_userName1" placeholder="请输入学信账号" onblur="checkAuth_xuexin()" />
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group">
					<label for="password">
						密码
						<a href="http://my.chsi.com.cn/archive/index.jsp" target="_blank" class="forget">
							忘记密码
						</a>
					</label>
					<div class="control">
						<input type="password" id="xuexin_password" name="xuexin_password"/>
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group" id="xuexin_authimg" style="display:none;">
					<label for="authcode">
						验证码
					</label>
					<div class="row">
						<div class="col-3">
							<div class="control">
								<input type="text" id="xuexin_authcode" name="xuexin_authcode"/>
							</div>
						</div>
						<div class="col-4">
							<div class="pic">
								<img src="<c:if test="${url ne ''}">${url}</c:if>" id="xuexin_authcode_img"  onclick="checkAuth_xuexin()" />
							</div>
						</div>
						<div class="col-4">
							<div class="pic">
								<a href="javascript:checkAuth_xuexin();">
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
					<button type="button" class="done" onclick="checkField_xuexin();return false;">
						确认
					</button>
				</div>
				<div class="group last-group">
					<strong>
						还没有学信账号？&nbsp;立即
					</strong>
					<a href="http://my.chsi.com.cn/archive/index.jsp" target="_blank" class="re">免费注册</a>
				</div>
			</form>
		</div>

		<script type="text/javascript">
			var xuexin_imgurl = "";
			function checkAuth_xuexin(){
				var timestamp=new Date().getTime();
				var userNameValue = $("#xuexin_userName1").val();
				if(userNameValue.length>3){
				$.ajax({
				     type:'post',
			         async:false,
				     url:'/${ctx}/xuexin/init.html',
				     data:{random2:Math.random(),"loginName":userNameValue},
				     success : function(data) {
				    	 if(data.url!="none"&&data.url!=""){
				    	 	 xuexin_imgurl = data.url;
				    	 	//$("#sec-two .wrong-tip").text(xuexin_imgurl);
					   		$("#sec-two .wtip").slideDown("slow");
					   		setTimeout(function () {
					   			$("#sec-two .wtip").slideUp("slow");
					   		}, 2000);
					    	 changeAuth_xuexin();
				    	 }
				    }, 
				   });
				}
			}
			function changeAuth_xuexin(){
				$("#xuexin_authimg").show();
				$("#xuexin_authcode_img").attr("src",xuexin_imgurl+"?time="+new Date().getTime());
				
			}

			function checkField_xuexin(){ 
	    		$("#message").show(200,notifyAll_xuexin);
			}
			function notifyAll_xuexin(){
				var userNameValue = $("#xuexin_userName1").val(); 
		        var passwordValue = $("#xuexin_password").val();
		        var authcodeValue = $("#xuexin_authcode").val();
		        var verifyUrl='/${ctx}/xuexin/verifyLogin.html';
		        $.ajax({
				     type:'post',
			         async:false,                                                            
				     url:verifyUrl,
				     data:{loginName:userNameValue,password:passwordValue,"resultCode":1,authcode:authcodeValue,random2:Math.random()},
				     success : function(data) {
				    	 	var flag = data.status;
				    	 	var errorMsg = data.errorMsg;
				    	 	if(flag){
								act("xuexin","xx","学信网授权成功！");
				    	 	}else{			    	 		
						   		gg_wrong($("#sec-two"),errorMsg);
							    var url = data.url;
							    if(url !='none'&&url!=""){
							       xuexin_imgurl = data.url;
							    	changeAuth_xuexin();
							    }
					    	 }
					    	 },
				     });
				
			}
			
			
		</script>
</html>	