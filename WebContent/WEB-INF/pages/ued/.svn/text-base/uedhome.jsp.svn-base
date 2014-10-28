<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    
	<jsp:include page="/common/domain.jsp" flush="true"/> 
	
    <title>量化派</title>

    <!-- Bootstrap core CSS -->
    <link href="/${ctx}/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template
    <link href="/${ctx}/navbar-fixed-top.css" rel="stylesheet">
	-->
	
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/${ctx}/js/jquery-1.11.0.min.js"></script>
    <script src="/${ctx}/bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/${ctx}/bootstrap/assets/js/ie10-viewport-bug-workaround.js"></script>
    
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]>
	<script src="/${ctx}/bootstrap/assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/${ctx}/bootstrap/assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript">
    function myload() {
    	$('#myTab a').click(function (e) {
  			e.preventDefault()
  			$(this).tab('show')
		})
		$('#myTab a').hover(function (e) {
  			//e.preventDefault()
  			$(this).tab('show')
		})

	}
    </script>
  </head>

  <body onload="myload()">

    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">量化派</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">首页</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">运营商<span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="#">中国电信</a></li>
                <li><a href="#">中国移动</a></li>
                <li><a href="#">中国移动</a></li>               
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">银行<span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li class="dropdown-header">国有银行</li>
                <li><a href="#">工商银行</a></li>
                <li><a href="#">建设银行</a></li>
                <li><a href="#">农业银行</a></li>
                 <li><a href="#">中国银行</a></li>
                <li class="divider"></li>
                <li class="dropdown-header">商业银行</li>
                <li><a href="#">招商银行</a></li>
                <li><a href="#">北京银行</a></li>
              </ul>
            </li>

          </ul>
          <ul class="nav navbar-nav navbar-right">
             <li><a>你好，<label id="userName">anyfly</label >！</a></li>  
            <li><a href="/LKB/lg/loginout.html">退出</a></li>           
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
	<br>
    <div class="container">

      <!-- Main component for a primary marketing message or call to action -->
      <div class="jumbotron">
       <ul class="nav nav-tabs" role="tablist" id="myTab">
  		<li role="presentation" class="active" data-toggle="tab"><a href="#telcom">运营商类</a></li>
  		<li role="presentation" data-toggle="tab"><a href="#bank">银行类</a></li>
  		<li role="presentation" data-toggle="tab"><a href="#eCommerce">电商类</a></li>
  		<li role="presentation" data-toggle="tab"><a href="#others">综合类</a></li>
  	   </ul>
  	   <br>
  	   <!-- Tab panes -->
<div class="tab-content">
  <div class="tab-pane active" id="telcom">
  <div class="row">
  <div class="col-sm-6 col-md-4">
    <div class="thumbnail">
      <a href="javascript:;" onclick="openMyDialog(0)">
		<img src="/${ctx}/img/icon_0_0_3.png" alt="电信"></a>
      <div class="caption">
        <span>中国电信</span>
        <h6>导入中国电信手机用户的余额，帐单等</h6>       
      </div>
    </div>
  </div>
  <div class="col-sm-6 col-md-4">
    <div class="thumbnail">
    <a href="#" onclick="openMyDialog(1)">
      <img src="/${ctx}/img/icon_0_0_2.png" alt="移动"></a>
      <div class="caption">
        <span>中国移动</span>
        <h6>导入中国移动手机用户的余额，帐单等</h6>       
      </div>
    </div>
  </div>
  <div class="col-sm-6 col-md-4">
    <div class="thumbnail">
    <a href="#" onclick="openMyDialog(2)">
      <img src="/${ctx}/img/icon_0_0_1.png" alt="联通"></a>
      <div class="caption">
        <span>中国联通</span>
        <h6>导入中国联通手机用户的余额，帐单等</h6>       
      </div>
    </div>
  </div>

</div>
  </div>
  <div class="tab-pane" id="bank">
  <div class="row">
  <div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_1.png" alt="建行">
      <div class="caption">
        <span>建设银行</span>            
      </div>
    </div>
  </div>
  <div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_2.png" alt="工行">
      <div class="caption">
        <span>工商银行</span>            
      </div>
    </div>
  </div>
<div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_3.png" alt="中行">
      <div class="caption">
        <span>中国银行</span>            
      </div>
    </div>
  </div>
<div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_4.png" alt="农行">
      <div class="caption">
        <span>农业银行</span>            
      </div>
    </div>
  </div>
  </div>
  </div>
  <div class="tab-pane" id="eCommerce">
    <div class="row">
  	<div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/ec/icon_0_0_1.png" alt="京东">
      <div class="caption">
        <span>京东</span>            
      </div>
    </div>
  </div>
<div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/ec/icon_0_0_2.png" alt="淘宝">
      <div class="caption">
        <span>淘宝</span>            
      </div>
    </div>
  </div>
  </div>
  </div>
  <div class="tab-pane" id="others">
  <div class="row">
  <div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_1.png" alt="学信">
      <div class="caption">
        <span>学信网</span>            
      </div>
    </div>
  </div>
  <div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_1.png" alt="征信报告">
      <div class="caption">
        <span>征信报告</span>            
      </div>
    </div>
  
  </div>
  <div class="col-sm-3 col-md-2">
    <div class="thumbnail">
      <img src="/${ctx}/img/bank/icon_0_0_1.png" alt="社保">
      <div class="caption">
        <span>社保</span>            
      </div>
    </div>
  </div>
  </div>

  </div>
</div>
      </div>

    </div> <!-- /container -->

    <div class="footer">
      <div class="container">
        <p class="text-muted">量科邦保留所有版权.</p>
      </div>
    </div>
    
    <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
    <div class="modal-header active">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title">中国电信</h4>
      </div>
      <div class="modal-body">
  <form role="form" id="firstForm">
  <div class="form-group">
    <label class="sr-only" for="exampleInputEmail1">Phone Number</label>
    <input type="tel" class="form-control" id="uedPhone" placeholder="手机号码" onblur="getAuth()">
    <span class="help-block">注意：请输入实名认证的手机号码.</span>
  </div>
  <div class="form-group has-error" style="display:none" id="phoneno_tip"></div>
  <div class="form-group">
    <label class="sr-only" for="exampleInputPassword1">Password</label>
    <input type="password" class="form-control" id="uedPassword" placeholder="服务密码">
    <span class="help-block" id="forgetPassUrl">注意：忘记密码可点击“找回密码”</span>
  </div>
  <div class="form-group has-error" style="display:none" id="passowrd_tip"></div>
  <div class="form-group row" id="authCodeDiv" style="display:none">
	<label class="sr-only">验证码</label>
	 <div class="col-xs-4">
	<input class="form-control" name="authCode" id="authCode" type="text" placeholder="验证码"/>
	</div>
    <span><img onclick="getAuth()" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="authcode_img" style="height:30px;width:60px;position: relative;top: 8px;"/>
	看不清？<a class="" href="javascript:getAuth();">换一张</a></span>
  </div>
  <div class="form-group has-error" style="display:none" id="authCode_tip"></div>
  <div id="message" style="display:none;" class="form-group has-success"></div>
  <button type="button" class="btn btn-primary btn-lg btn-block" onclick="checkField();return false;">登录</button>
  <input type="hidden" name="resultCode" id="resultCode" value=""/>
  <input type="hidden" name="isSecond" id="isSecond" value=""/>
  </form>
  
  <form role="form" id="secondForm" style="display:none">
  <div class="form-group row" id="authCodeDiv2" style="display:none">
	<label class="sr-only">验证码</label>
	<div class="col-xs-4"><input class="form-control" name="authCode2" id="authCode2" type="text" placeholder="验证码"/></div>
    <span><img onclick="getAuth(false, 2)" src="<c:if test="${url ne 'none'}">${imgPath}${url}</c:if>" id="authcode_img2" style="height:30px;width:60px;position: relative;top: 8px;"/>
	看不清？<a class="" href="javascript:getAuth(false, 2);">换一张</a></span>
  </div>
  <div class="form-group has-error" style="display:none" id="authCode_tip2"></div>
  <div class="form-group row">
	<label class="sr-only">手机验证码</label>
	<div class="col-lg-6"><input class="form-control" name="smsCode" id="smsCode" type="text" placeholder="手机验证码"/></div>
	<span><a href="javascript:void(0);" class="base-forgetpwd" onclick="getSms();">重新获取</a></span>
  </div>
  <div class="form-group has-error" style="display:none" id="smsCode_tip"></div>
  <div id="message2" style="display:none;" class="form-group has-success"></div>
  <div class="form-group">
  <button type="button" class="btn btn-default btn-lg" onclick="firstStep()">上一步</button>
  <button type="button" class="btn btn-primary btn-lg" onclick="checkField();return false;">完成</button>
  </div>
  <input type="hidden" name="resultCode2" id="resultCode2" value=""/>
  </form>
</div>
<!--
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
      -->
    </div>
  </div>
</div>
<script type="text/javascript">
var issubmit = false;
var modal_title = ["中国电信", "中国移动", "中国联通"];
function openMyDialog(t) {
  $('.modal-header .modal-title').text(modal_title[t]);
  //$("#authCodeDiv").hide();
  $("#message").hide();
  //$('.modal').modal('hide');
  $('.modal').modal({
  	keyboard: true
  })
}
var auth_index = 1;
var isAuth = false;
function showMessage(data, t) {
	if (data.errorMsg && data.errorMsg != "") {
		var mess = $("#message" + (t && t > 0 ? t+"" : ""));
		mess.html("<code>" + data.errorMsg + "</code>");
		//mess.addClass("label-danger");
		mess.show();
	}
}
function firstStep() {
	$("#isSecond").val("0");
	$("#secondForm").hide();
	$("#firstForm").show();
	getAuth();
}
function nextStep() {
	$("#isSecond").val("1");
	$("#secondForm").show();
	$("#firstForm").hide();
	//getAuth();
}

function getAuth(isCheckField, at){
	if (isAuth) {
		return;
	}
	var suffix = "";
	if (at) {
		suffix = at + "";
	} 
	isAuth = true;
	var phone = $("#uedPhone").val();
	if (phone == "") { isAuth = false;return;}
	$.ajax({
	     type:'post',
         async:true,
         data:{phone:phone, index:auth_index},
	     url: at ? '/${ctx}/pt_ued_phone_img2.html' : '/${ctx}/pt_ued_phone_img.html',
	     success : function(data) {
	     		if (data.title && data.title != "") {
					$('.modal-header .modal-title').html("<code>" + data.title + "</code>");
				}
	     		if(data.forgetPassUrl && data.forgetPassUrl !=""){
	     			$("#forgetPassUrl").html('注意：忘记密码可点击<a href="'+ data.forgetPassUrl +'" target="blank_">找回密码</a>');
	     		}
	     		if(data.passName && data.passName !=""){
	     			$("#uedPassword").attr("placeholder",data.passName);
	     		}

	     		
				if (data.success) {
					var url = data.imgUrl;
					showImg(url, suffix, isCheckField)
				} 
				showMessage(data, at);
				isAuth = false;
				auth_index++;
			},
			error : function() {
				$("#resultCode").val("0");
				showMessage(data, at);
				isAuth = false;
			}
		});

	}
	function showImg(url, suffix, isCheckField) {
		if (url == null) {
			return;
		}
			
				if (url && url != 'none' && url != 'null' && url != '') {
						$("#authCodeDiv" + suffix).show();
						$("#authcode_img" + suffix).attr("src", "${imgPath}/img/icon_1.jpg");
						//setTimeout(function() {
					    $("#authcode_img" + suffix).attr("src", '' + url + "?d=" + new Date().getTime());
						//}, 100);

						$("#resultCode" + suffix).val("1");
					} else {
						$("#resultCode" + suffix).val("0");
						document.getElementById("authCode" + suffix).value = "";
						//alert($("#authCode").val());
						$("#authCodeDiv" + suffix).hide();
						if (isCheckField) {
							checkField();
						}
					}
	}
	function checkField() {
		var flag = true;
		$("input[id$='_password']").each(function () { 
			var password = this.name; 
			var passwordValue = this.value; 
	        if (passwordValue=='')
			{
				flag = false;
				$("#"+password+"_tip").show();
				$("#"+password).addClass("error");
			}else{
					$("#"+password+"_tip").hide();
					$("#"+password).removeClass("error");
			}    
	        });
		if ("" == $("#resultCode").val()) {			
			var mess = $("#message");
			mess.text("正在加载验证码，请稍等……");
			mess.show();
			getAuth(true);
			mess.hide(3000);
			return;
		}
		if (flag == true) {
			$("#message").hide();
			var isSecond = $("#isSecond").val();
			if (isSecond != null && isSecond == "1") {
				$("#message").text("正在努力登录中，请稍等……");
				$("#message").show(500, secondSubmit);
			} else {
				$("#message").text("正在努力登录中，请稍等……");
				$("#message").show(500, firstSubmit);
			}
		}
	}
	var secondSms = false;
	function firstSubmit() {
		if (issubmit) {
			return;
		}
		issubmit = true;
		var vertifyUrl = '/${ctx}/pt_ued_phone_login.html';
		var phoneValue = $("#uedPhone").val();
		//var wzpassword = $("#wzpassword").val();
		var fwpassword = $("#uedPassword").val();
		var authCode = $("#authCode").val();
		var checkCode = $("#resultCode").val();

		$("#password_tip").hide();
		$("#password_tip1").hide();
		$("#authcode_tip").hide();
		$("#authcode_message").hide();
		$("#message").text("");
		if(phoneValue=='' || phoneValue== undefined){
		    $("#phoneno_tip").text("请输入手机号码");
		    $("#phoneno_tip").show();
		    issubmit = false;
			return false;
		} else {
			$("#phoneno_tip").hide();
		}
		if (fwpassword == '' || fwpassword == undefined) {
			$("#passowrd_tip").text("请输入服务密码");
			$("#passowrd_tip").show();
			issubmit = false;
			return false;
		} else {
			$("#passowrd_tip").hide();
		}
		
		if (checkCode == "1" && (authCode == '' || authCode == undefined)) {
			$("#authCode_tip").text("请输入验证码");
			$("#authCode_tip").show();
			issubmit = false;
			return false;
		} else {
			$("#authCode_tip").hide();
		}

		$.ajax({
			type : 'post',
			async : false,
			url : vertifyUrl,
			data : {
				phone : phoneValue,
				fwpassword : fwpassword,
				authCode : authCode
			},
			success : function(data) {
				// $("#message").hide();
				if (data.success) {
					/* if (data.status == 1){//} && data.flag1 == 'false') {
						alert("获取部分信息失败.");
						//location.reload();
						//getAuth();
					} else */
					if (data.status == 1){//} || data.flag == 999) {
						issubmit = false;
						if (data.result) {
							finish();
						} else {
							$("#isSecond").val("1");
							$("#secondForm").show();
							$("#firstForm").hide();
							var url = data.imgUrl;
							if (url && url != 'none' && url != 'null') {
								getAuth(true, 2);
								$("#resultCode2").val(1);
								$("#authCodeDiv2").show();
								//secondSms = true;
							} else {
								$("#authCodeDiv2").hide();
							}
						}
						//getSms();
						//$("#spid").val(data.spid);
						//window.location.href="/${ctx}/entranceEmail.html";
					} else {
						if (data.errorMsg) {
						} else {
							alert("登录失败,密码或验证码错误.");
						}
						showImg(data.imgUrl, "", false)
						$("#resultCode").val()
						//$("#message").attr("style", "dislpay:none;");
						getAuth();
					}
				}
				showMessage(data);
				issubmit = false;
			},
			error : function() {
				showMessage(data);
				issubmit = false;
			}
		});
	}
	function getSms(){
		var checkCode = $("#resultCode2").val();
		var authCode = '';
		if (checkCode == "1") {
			authCode = $("#authCode2").val();
			if ((authCode == '' || authCode == undefined)) {
				$("#authCode_tip2").text("请输入验证码");
				$("#authCode_tip2").show();
				return false;
			}
		}
		var phoneValue = $("#uedPhone").val();
		$.ajax({
	     	type:'post',
       		async:false,
	     	url:'/${ctx}/pt_ued_phone_sms.html',
	     	data:{phone:phoneValue, authCode:authCode},
	     	success : function(data) {
	     		if (data.success) {
					if (data.status == 1) {
						alert("获取手机验证码成功.");
					} else {
						if (data.errorMsg) {
						} else {
							alert("获取手机验证码失败.");
						}
					}
				}
				showMessage(data, 2);
	    	 },
  	 		error:function(){}
	     });
	}
	function secondSubmit(){
		if (issubmit) {
			return;
		}
		issubmit = true;
		var vertifyUrl='/${ctx}/pt_ued_phone_service.html';
		var smsCode = $("#smsCode").val();
		var checkCode = $("#resultCode2").val();
		var authCode = $("#authCode2").val();
		if (checkCode == "1" && (authCode == '' || authCode == undefined)) {
			$("#authCode_tip2").text("请输入验证码");
			$("#authCode_tip2").show();
			return false;
		}
		if ((smsCode == '' || smsCode == undefined)) {
			$("#smsCode_tip").text("请输入手机验证码");
			$("#smsCode_tip").show();
			return false;
		}
	    //var smsAuthCode = $("#smsAuthCode").val();
	    //var spid = $("#spid").val();
	    var phone = $("#uedPhone").val();
		$.ajax({
		     type:'post',
	         async:false,                                                            
		     url:vertifyUrl,
		     data:{smsCode:smsCode,phone:phone,authCode:authCode},
		     success : function(data) {
			     
		    	 //$("#message").hide();		    	 
		    	 if (data.success) {
						if (data.status == 1 && data.flag1 == 'false') {
							alert("获取部分信息失败.");
							location.reload();
							//getAuth();
						} else if (data.status == 1 || data.flag == 999) {							
							issubmit = false;
							if (data.result) {
								finish();
							} else {
								
							}
							
						} else {
							if (data.errorMsg) {
							} else {
								alert("登录失败,密码或验证码错误.");
							}
							
							
							//$("#message").attr("style", "dislpay:none;");
							//getAuth();
						}
					}
		    		var url = data.imgUrl;
		    		if (url && url != 'none' && url != 'null') {
						getAuth(true, 2);
					} 
					showMessage(data, 2);
					issubmit = false;
				  
		    	},
		    	 error:function(){
		    		 showMessage(data, 2);
					 issubmit = false;
		    	 }
		     });
	}
	function finish() {
	    $('.modal').modal('hide');
		//location.reload();
		window.location.href="/${ctx}/pt_ued_fnc_list.html";
	}	
</script>
  </body>
</html>
