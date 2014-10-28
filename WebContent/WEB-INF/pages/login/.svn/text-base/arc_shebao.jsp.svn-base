<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<html>
	<script src="/${ctx}/js/cascading/jquery.provincesCity.js"></script>
	<div class="section hide" id="sec-six">
		<form>
			<div class="group">
				<label class="shebao">社保所在地</label>
				<div class="control for-shebao" id="city_5">
				</div>
				
				<div style="clear:both"></div>
			</div>
			<div class="group" id="shebao_idCard1">
				<label for="shebao_idCard">身份证</label>
				<div class="control">
					<input type="text" id="shebao_idCard" name="shebao_idCard" placeholder="请输入身份证号码" onblur="shebao_checkInitAuth()" value=""/>
					<input type="hidden" id="shebao_isShowImg" value="0"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group" id="shebao_name1" style="display:none;">
				<label for="shebao_name">社保姓名</label>
				<div class="control">
					<input type="text" id="shebao_name" name="shebao_name" placeholder="请输入社保姓名" />
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group" id="shebao_num1" style="display:none;">
				<label for="shebao_num">社保号</label>
				<div class="control">
					<input type="text" id="shebao_num" name="shebao_num" placeholder="请输入社保号"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group">
				<label for="shebao_password">
					密码
				</label>
				<div class="control">
					<input type="password" id="shebao_password" name="shebao_password"/>
				</div>
				<div class="tip">
					<span class="icon"></span>
				</div>
			</div>
			<div class="group" id="shebao_authcode1" style="display:none;">
				<label for="shebao_authcode">
					验证码
				</label>
				<div class="row">
					<div class="col-3">
						<div class="control">
							<input type="text" id="shebao_authcode" name="shebao_authcode"/>
						</div>
					</div>
					<div class="col-4">
						<div class="pic">
							<img src="" id="shebao_authcode_img"  onclick="shebao_checkInitAuth()"/>
						</div>
					</div>
					<div class="col-4">
						<div class="pic">
							<a href="javascript:shebao_checkInitAuth();" >
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
				<button type="button" class="done" onclick="checkField_shebao();return false;">
					确认
				</button>
			</div>
			<div class="group last-group">
				<strong>
					还没有社保账户？&nbsp;立即
				</strong>
				
				<a href="" class="re" target ="_target" id="shebao_register" onclick="javascript:return shebao_register();">免费注册</a>
				<input type="hidden" id="shebao_register_value" value=""/>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		function shebao_register(){
			if(shebao_verifyParam()){
				var shebao_register_value = $("#shebao_register_value").val();
				if(shebao_register_value==''||shebao_register_value ==undefined){
						printErrorMsg("抱歉,注册地址为空,请手动到相应的社保网站进行注册!");
				}else{
					$("#shebao_register").attr("href",shebao_register_value);
					return true;
				}
			}
			return false;
		}
		function printErrorMsg(msg){
		   	gg_wrong($("#sec-six"),msg);
		}
		
		
		function shebao_checkInitAuth(){
			var province = $("#shebao_province").val();
		    var city = $("#shebao_city").val();
		    if(shebao_verifyParam()){
		    	 
			$.ajax({
			     type:'post',
			     url:'/${ctx}/shebao/init.html',
			     data:{random2:Math.random(),city:city,province:province},
			     success : function(data) {
			     	/* var inputParams = data.inputParams;
			     	  var strs= new Array();
			     	 strs= inputParams.split(",");
					 for (var i=0;i<strs.length ;i++ ){ 
						if(strs[i].equals("name")){
							 $('#shebao_name').show();
						}else if(strs[i].equals("tsin")){
							 $('#shebao_num').show();
						}else if(strs[i].equals("idCard")){
							 $('#shebao_idCard').show();
						}else {
						}
					  } */ 
			    	 if(data.url==""){
			    	 	 $('#shebao_authcode1').hide();
			    	 	 $("#shebao_isShowImg").val("0");
			    	 }else{
			    	 	shebao_changeAuth(data.url);
			    	 	 $("#shebao_isShowImg").val("1");
			    	 }
			    	 $("#shebao_register_value").val(data.registerUrl);
			    }, 
			   error:function(){	
				  printErrorMsg("服务器繁忙,请稍后再试!");
					 }
			   });
		    }
		}
		
		function shebao_verifyParam(index){
			var province = $("#shebao_province").val();
		    var city = $("#shebao_city").val();
			if(province==''||province=='请选择'){
				printErrorMsg("请选择省市");
				return false;
			}
			if(city==''||city=='请选择'){
				printErrorMsg("请选择省市");
				return false;
			}
			if(index==1){
				var idCard = $("#shebao_idCard").val();
				if(idCard==""||idCard == undefined){
					printErrorMsg("请输入身份证号");
					return false;
				}
				 var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
  				 if(reg.test(idCard) === false) {  
      					printErrorMsg("您输入的身份证号码不正确!");
      				 return  false;  
  				 }  
				
		    	var password = $("#shebao_password").val();
		    	if(password==''||password==undefined){
		    		printErrorMsg("请输入密码");
					return false;
		    	}
		    	var isShowImg = $("#shebao_isShowImg").val();
		    	if(isShowImg=="1"){
		    		var authcode = $("#shebao_authcode").val();
		    		if(password==''||password==undefined){
		    			printErrorMsg("请输入验证码!");
						return false;
		    		}
		    	}
			}
			return true;
		}
		function shebao_changeAuth(url){
			 $('#shebao_authcode1').show();
			 $('#shebao_authcode_img').attr("src",url+"?l="+new Date().getTime());
		}

		function checkField_shebao(){ 
    		$("#message").show(200,notifyAll_shebao);
		}
		function notifyAll_shebao(){
	        var userNameValue = $("#shebao_idCard").val();
	        var passwordValue = $("#shebao_password").val();
	        var authcodeValue = $("#shebao_authcode").val();
	       	var province = $("#shebao_province").val();
	        var city = $("#shebao_city").val();
	        var shebao_num = $("#shebao_num").val();
	        var shebao_name = $("#shebao_name").val();
	        
	        if(shebao_verifyParam(1)){
	      	 var verifyUrl='/${ctx}/shebao/verifyLogin.html';
	       	 $.ajax({
			     type:'post',
		         async:false,                                                            
			     url:verifyUrl,
			     data:{idCard:userNameValue,password:passwordValue,authcode:authcodeValue,random2:Math.random(),province:province,city:city,shebao_name:shebao_name,shebao_num:shebao_num},
			     success : function(data) {
			    	 	var status = data.status;
			    	 	if(status==1){
							 act("shebao","sb","社保授权成功！");
			    	 	}else{
		    	 			 printErrorMsg(data.errorMsg);
				    	 	 if(data.url==""){
					    	 	 $('#shebao_authcode1').hide();
					    	 	  $("#shebao_isShowImg").val("0");
					    	 }else{
					    	 	shebao_changeAuth(data.url);
					    	 	 $("#shebao_isShowImg").val("1");
					    	 }
			    	 	}
			    	 	 $("#shebao_register_value").val(data.registerUrl);
			    	 },
			    	 error:function(){
			    		 printErrorMsg("服务器繁忙,请稍后再试!");
			    	 }
			     });
	        }
	      
		}
			
			//调用插件
	$(function(){
		$("#city_5").ProvinceCity();
	});
		</script>
</html>	