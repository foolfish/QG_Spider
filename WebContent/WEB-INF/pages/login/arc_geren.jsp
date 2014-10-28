<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>


		<div class="section hide" id="sec-three">
			<form>
				<div class="group">
					<label for="realname">输入姓名</label>
					<div class="control">
						<input type="text" id="realname" value="${user.realName}"/>
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group">
					<label for="idcard">
						输入身份证号
					</label>
					<div class="control">
						<input type="text" id="idcard" value="${user.idcard}" />
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group">
					<label for="email">输入电子邮箱</label>
					<div class="control">
						<input type="text" id="email" value="${user.email}"/>
					</div>
					<div class="tip">
						<span class="icon"></span>
					</div>
				</div>
				<div class="group wtip">
					<label class="wrong-tip"></label>
				</div>
				<div class="group">
					<button type="button" class="done" onclick="checkFiled_geren();">
						确认
					</button>
				</div>
			</form>
		</div>
		<script>
			 
			function checkFiled_geren(){
				var realname = $("#realname").val();
				if(realname==''){
			   		gg_wrong($("#sec-three"),"姓名不能为空");
					return false;
				}
				var idcard = $("#idcard").val();
				if(idcard==''){
			   		gg_wrong($("#sec-three"),"身份证号不能为空");
					return false;
				}
				 var reg_card = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
  				 if(reg_card.test(idcard) === false) {  
  					gg_wrong($("#sec-three"), "您输入的身份证号码不正确!");
      				 return  false;  
  				 }  
				var email = $("#email").val();
				if(email==''){
			   		gg_wrong($("#sec-three"),"电子邮箱不能为空");
					return false;
				}
				
				$.get('/${ctx}/bc/saveBaseInfos.html?random='+Math.random(),{'realname' : realname,'idcard':idcard,'email':email},function(data){
					var flag = data.flag;
					if(flag == '1'){
						 $('#modal-com').removeClass("show").addClass("hide");
						 $('.modal-bg-two').css("display", "block");				
						 act("geren","gr","个人信息填写完成！");
					}
				});
				
				
				
			}
			
		
		
		</script>
</html>	

