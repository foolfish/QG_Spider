<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!doctype html>
<html>
<head>
	<jsp:include page="/common/domain.jsp" flush="true"/> 
	<meta charset="utf-8">
	<title>注册</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="stylesheet" type="text/css" href="/${ctx}/css/g_main.css">
</head>
<body>
    <jsp:include page="header.jsp" flush="false"/>
	<div class="grid" id="grid-three">
		<div class="container">
			<div class="row head">
				<div class="col-12">
					<ul class="nav-ul">
						<li class="active first">
							<a href="javascript:" style="text-indent:50px;">
								
								用户注册
							</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="row feat">
				<div class="col-12">
					<form class="base-form" action="/${ctx}/lg/registerForm.html" method="post">
						<div class="group">
							<label for="username">用户名</label>
							<div class="control">
								<input type="text" id="username" name="username" placeholder="手机号码" />
							</div>
							<div class="tip">
								<span class="icon"></span>
								<span class="infor"></span>
							</div>
							<div class="pic">
								<span class="name"></span>
							</div> 
						</div>
						<div class="group">
							<label for="msgNumber">
								手机验证码
								<a href="javascript:sendmsg();" class="get-code" id="send_msg">
									获取验证码
								</a>
								<a href="javascript:" class="get-code2">
									获取验证码
									<span id="wait"></span>
								</a>
							</label>
							<div class="control">
								<input type="text" id="msgNumber" name="msgNumber" />
							</div>
							<div class="tip">
								<span class="icon"></span>
								<span class="infor"></span>
							</div>
						</div>
						<div class="group">
							<label for="password">登陆密码</label>
							<div class="control">
								<input type="password" id="password" name="password" placeholder="6-20位字符" />
							</div>
							<div class="tip">
								<span class="icon"></span>
								<span class="infor"></span>
							</div>
							<div class="pic">
								<span class="pass"></span>
							</div> 
						</div>
						<div class="group">
							<label for="again">确认密码</label>
							<div class="control">
								<input type="password" id="again" />
							</div>
							<div class="tip">
								<span class="icon"></span>
								<span class="infor"></span>
							</div>
						</div>
						<div class="group">
							<label for="code">邀请码</label>
							<div class="control">
								<input type="text" id="code" name="code"/>
							</div>
							<div class="tip">
								<span class="icon"></span>
								<span class="infor"></span>
							</div>
						</div>
						<div class="group">
							<label></label>
							<div class="control">
								<input type="checkbox" value="checked" id="check"/>
								<label for="check">
									我以阅读并同意
									<a href="javascript:showtip();" class="sign-tip">
										《注册协议》
									</a>
									条款
								</label>
							</div>
							<div class="tip for-check">
								<span class="icon"></span>
								<span class="infor"></span>
							</div>
						</div>
						<div class="group">
							<button type="button" class="sign" id="submitBtn">
								注册
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		
	</div>
	<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>
	<div class="modal" id="modal-sign">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-head">
					<h4>量科邦用户注册协议</h4>
					<a href="javascript:closetip();">
						<img src="/${ctx}/img/pro-031.png" alt="" 
						class="cross" style="position: absolute;top: 17px;right: 13px;"/>
					</a>
				</div>
				<div class="modal-body">
					<div class="article">
			            <p>
			                <strong class="fs12">为使用本网站提供的服务，请您仔细阅读并遵守《网站服务协议》。请您审慎阅读，充分理解协议各条款的内容，特别是免除责任的条款。如您点击本页面的同意选项，即视为您已阅读协议条款，同意并接受协议条款的约束。 </strong>
			            </p>

			            <p>
			                <strong class="fs12">特别提示：本网站提供的服务不属于《征信业管理条例》规定的征信业务，仅限于对特定客户自愿提供的数据进行整理、加工，并向该特定客户指定的特定机构提供咨询服务、提交工作成果，不代替特定机构的征信工作和业务判断。</strong>
			            </p>

			            <p>
			                <strong class="fs12">风险提示：您在本网站提供的个人信息存在泄露风险，可能会被不法分子利用，造成您的损失。但本网站将采取尽可能的安全措施，保障您的个人信息不被泄露。</strong>
			            </p>

			            <p>
			                <strong class="fs12">如您点击本页面的同意选项，即视为您已经了解上述风险，并同意向本网站提交您的个人信息。</strong>
			            </p>
			            <br>

			            <p align="center" style="font-size: 22px;">
			                <strong>网站服务协议</strong>
			                <br>

			            </p><p>为明确双方的权利和义务，规范双方的行为，【量科邦】（以下简称“本网站”）与申请使用本网站提供服务的客户（以下简称“客户”）本着平等互利的原则，就本网站相关服务事宜达成以下协议：</p>
			            <h6 style="font-size: 14px;">
			                <strong>第一条 服务内容</strong>
			            </h6>

			            <p>本网站根据客户授权，取得客户根据本网站要求登录的相关账户中的个人信息（包括但不限于【量科邦】），并使用该信息向客户指定的第三方提供相应的信息评估报告。</p>
			            <h6 style="font-size: 14px;">
			                <strong>第二条 本网站的权利和义务</strong>
			            </h6>
			            <h6>(一)主要权利</h6>
			            <ol>
			                <li>本网站有权要求客户提供真实、准确、完整、有效的个人信息；</li>
			                <li>为保障和提高服务的安全性、可靠性和方便性，本网站有权定期或者不定期对本网站进行维护、升级和改造；</li>
			                <li>
			                    本网站有权依照法律、法规、规章以及其他规范性文件或者业务需要对本网站提供的服务内容、操作流程等进行调整，涉及费用和客户权利义务变更的调整，将于正式对外公告后施行。自公告施行之日公告内容构成对本协议的有效修改和补充。如果客户不同意接受本网站的调整内容，客户有权向本网站申请终止相关服务，但在申请终止相关服务之前客户使用本网站服务的，仍然应当遵守相关调整内容。客户既不申请终止服务，又不遵守本网站调整内容的，本网站有权选择终止本协议；
			                </li>
			                <li>本网站有权根据客户的授权，取得客户相关的个人信息，并使用该信息制作个人信息评估报告，并向客户指定的第三方提供该报告。</li>
			            </ol>
			            <h6>(二)主要义务</h6>
			            <ol>
			                <li>接受符合条件的客户的申请，为客户办理注册手续；</li>
			                <li>根据客户授权，制作针对客户的个人信息评估报告，并向客户指定的第三方提供该报告；</li>
			                <li>
			                    对客户提交的个人信息保密，除以下情况外不向任何第三方提供客户信息：
			                    <ol>
			                        <li>经客户许可同意；</li>
			                        <li>出于提供本协议项下服务的需要；</li>
			                        <li>根据法律、法规、规章或者规范性文件的要求，向行政、司法机关履行披露义务；</li>
			                        <li>根据公共利益的需要。</li>
			                    </ol>
			                </li>
			                <li>维护本网站服务的稳定性，对因网络程序、网络设备线路等原因导致服务无法正常提供时，应及时发出公告通知客户，并及时完成相应维修，保障本网站的正常运作。</li>
			            </ol>
			            <h6 style="font-size: 14px;">
			                <strong>第三条 客户的权利和义务</strong>
			            </h6>
			            <h6>(一)主要权利</h6>
			            <ol>
			                <li>提出注册申请，并在协议生效后依据本协议享受相关服务；</li>
			                <li>有权修改在本网站的账户、密码及其他相关信息；</li>
			                <li>授权本网站根据本协议获取个人信息，并利用相关信息制作个人信息评估报告，并向客户指定的第三方及时提交报告。</li>
			            </ol>
			            <h6>(二)主要义务</h6>
			            <ol>
			                <li>保证提交的个人信息的真实、准确、完整、有效；</li>
			                <li>妥善保存本网站账户密码，不对外泄露相关密码；</li>
			                <li>授权本网站根据本协议获取制作个人信息评估报告所需的信息，使用该等信息制作相关报告，并向客户指定的第三方提交该报告。</li>
			            </ol>
			            <h6 style="font-size: 14px;">
			                <strong>第四条 客户授权</strong>
			            </h6>
			            <h6>(一)在本协议生效时，即视为客户已经授权本网站通过本网站的服务获得客户本人的个人信息，并授权本网站利用该等信息制作关于客户的个人信息评估报告。</h6>
			            <h6>(二)在本协议生效时，即视为客户授权本网站向客户指定的第三方提交前款所述个人信息评估报告。</h6>
			            <h6 style="font-size: 14px;">
			                <strong>第五条 本网站承诺</strong>
			            </h6>
			            <h6>(一)本网站将不保存客户除本网站账户以外的任何账户的密码；</h6>
			            <h6>(二)本网站承诺不向客户指定的第三方外任何机构组织及个人提供该客户的个人信息评估报告。</h6>
			            <h6 style="font-size: 14px;">
			                <strong>第六条 违约责任</strong>
			            </h6>

			            <p>
			                <strong>协议一方违反本协议规定的义务，应对遵守协议的一方承担违约责任，并赔偿相应损失。 </strong>
			            </p>
			            <h6 style="font-size: 14px;">
			                <strong>第七条 不可抗力及其他免责事由</strong>
			            </h6>
			            <h6>(一)不可抗力</h6>

			            <p>
			                 不可抗力包括但不限于自然灾害如洪水、地震、瘟疫流行和风暴等以及社会事件如战争、动乱、政府行为等不能预见、不能克服并不能避免且对一方或双方造成重大影响的客观事件。由于上述情况造成的网站服务中断，本网站不承担责任。</p>
			            <h6>(二)本网站其他免责事由</h6>
			            <ol>
			                <li>受到计算机病毒、木马或其他恶意程序、黑客攻击的破坏；</li>
			                <li>客户或本网站的电脑软件、系统、硬件和通信线路出现故障；</li>
			                <li>客户提供虚假信息；</li>
			                <li>客户操作不当或指示不明确；</li>
			                <li>因客户原因泄露客户在本网站账户密码；</li>
			                <li>本网站对客户与第三方机构之间的纠纷不承担任何责任；</li>
			                <li>其他本网站无法控制或者合理预见的情形。</li>
			            </ol>
			            <h6 style="font-size: 14px;">
			                <strong>第八条 协议的生效和终止</strong>
			            </h6>

			            <p>本协议自客户点击同意选项并完成注册手续后生效。协议的任何条款因任何原因被确认无效时，均不影响协议其他条款的效力。</p>

			            <p>本协议在具备以下条件之一时终止：</p>
			            <ol>
			                <li>客户提供虚假信息；</li>
			                <li>客户申请终止网站服务或者办理完成网站账户注销手续；</li>
			                <li>本协议约定的一方或双方有权终止本协议的其他情形；</li>
			                <li>法律、法规、规章或者规范性文件规定的应当终止的情形。</li>
			            </ol>
			            <h6 style="font-size: 14px;">
			                <strong>第九条 法律适用与争议解决</strong>
			            </h6>

			            <p>本协议的成立、生效、履行和解释均适用中华人民共和国法律。</p>

			            <p>本协议中未尽事宜应依照法律、法规、规章或者规范性文件的规定办理。</p>

			            <p>协议双方在履行协议的过程中发生争议时，应当协商解决，协商不成时，任何一方均有权向本网站公司所在地有管辖权的人民法院提起诉讼。</p>
			        </div>
				</div>
				<div class="modal-foot">
					<div class="border">
						<a href="javascript:hidetip();">同意并继续</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-bg"></div>
    	
		<script type="text/javascript" src="/${ctx}/js/common.js"></script>
		<script type="text/javascript" src="/${ctx}/js/formCheck.js"></script>
		<script type="text/javascript" src="/${ctx}/js/dialog.js"></script>
		<script type="text/javascript" src="/${ctx}/js/selectTool.js"></script>
		<script type="text/javascript" src="/${ctx}/js/autoComplete.js"></script>
		<script type="text/javascript">
		
		$("#check").attr("checked",false);
		
		$("body").height() > $(window).height() ?
				$(".modal-bg").height($("body").height()) :
				$(".modal-bg").height($(window).height());
				
		$(".base-form").find("input").each(function () {
			$(this).val("");
		});
		
		
		var list = [0,0,0,0,0,0,0];
		var reg_phone = /^1[3|4|5|8][0-9]\d{8}$/; 
		//var reg_phone=/^(\w){6,20}$/; 
		var reg_pass = /^(\w){6,20}$/;
		var second = 60;
		var wait;
		var time;
		var phoneIsOk = false;
		/* var callNumber;
		var ischange = true;
		var msgRightNumber; */
		function sendMsgAction(){
			var userName = $("#username").val().trim();
			
			$(".get-code").css("display","none");
			$(".get-code2").css("display","inline-block");
				$("#wait").text(second);
			wait = setInterval(function(){
				second--;
				$("#wait").text(second);
			},1000);
			time = setTimeout(function () {
				clearInterval(wait);
				$(".get-code2").css("display","none");
				$(".get-code").css("display","inline-block");
				second = 60;
			},61000);
			
			$.get("/${ctx}/lg/sendSms.html?random="+Math.random(), {loginName:userName}, function(data){
				var result = data.sendSms_flag;
				if(result !="1"){
					clearInterval(wait);
					$(".get-code2").css("display","none");
					$(".get-code").css("display","inline-block");
					second = 60;
					$("#send_msg").text("发送失败，请点击重试");
				} else {
					
					/* ischange = true; */
				}
				
			});
		}
		function sendmsg() {
			if(phoneIsOk){
				//alert(phoneIsOk);
				sendMsgAction();
			}else{
				var isphone = false;
				var userName = $("#username").val().trim();	
				if( userName == "" ) {
					caveat($("#username"), "请输入你的11位手机号");
					isphone =  false;
				}else if( !reg_phone.exec(userName) ) {
					wrong($("#username"), "你的手机号码不正确，请重新输入");
					isphone =  false;
				}else {
					$.get("checkLoginName.html?random="+Math.random(),{'userName' : userName},function(data){
						var result = data.loginflag;
						if( !result ) {
							has = false;
							wrong($("#username"), "你的手机号码不正确，请重新输入");
							isphone = false;
						} else {
							right($("#username"));
							isphone = true;
						}
						//alert(isphone);
						if(isphone==true){
							sendMsgAction();
						}
					});
				}
			}
		}
		
		function showtip() {
			$("#modal-sign").css("display", "block");
			$(".modal-bg").css("display", "block");
		}
		
		function closetip() {
			$("#modal-sign").css("display", "none");
			$(".modal-bg").css("display", "none");
		}
		
		
		function hidetip() {
			$("#modal-sign").css("display", "none");
			$(".modal-bg").css("display", "none");
			$("#check").prop("checked","checked");
			right($("#check"));
			list[6] = 1;
		}
		
		function right (node) {
			node.parent().next().children(".icon").removeClass("caveat wrong right").addClass("right");
			node.parent().next().children(".infor").text("");
		}
		function wrong (node, text) {
			node.parent().next().children(".icon").removeClass("caveat wrong right").addClass("wrong");
			node.parent().next().children(".infor").text(text);
		}
		function caveat (node, text) {
			node.parent().next().children(".icon").removeClass("caveat wrong right").addClass("caveat");
			node.parent().next().children(".infor").text(text);
		}

	
		
		$("#username").on("blur", function () {
			var userName = $("#username").val().trim();	
			if( userName == "" ) {
				caveat($("#username"), "请输入你的11位手机号");
				return false;
			}else if( !reg_phone.exec(userName) ) {
				wrong($("#username"), "你的手机号码不正确，请重新输入");
				list[1] = 2;
				return false;
			}else {
				$.get("/${ctx}/lg/checkLoginName.html?random="+Math.random(),{'userName' : userName},function(data){
					
					var result = data.loginflag;
					if( result==1 ) {
						has = false;
						wrong($("#username"), "您的手机号已注册，请直接登录！");
						list[1] = 2;
						return false;
					} else {
						right($("#username"));
						list[1] = 1;
						/* if( callNumber == undefined ) { 
							callNumber = $("#username").val().trim();
						} else if( callNumber != $("#username").val().trim() ) {
							ischange = true;
							callNumber = $("#username").val().trim();
						}
						 */
						phoneIsOk =true;
						return true;
					}

				});
			}
		});
		/* function check_username() {
			var userName = $("#username").val().trim();	
			if( userName == "" ) {
				caveat($("#username"), "请输入你的11位手机号");
				return false;
			}else if( !reg_phone.exec(userName) ) {
				wrong($("#username"), "你的手机号码不正确，请重新输入");
				list[1] = 2;
				return false;
			}else {
				$.get("checkLoginName.html?random="+Math.random(),{'userName' : userName},function(data){
					var result = data.loginflag;
					if( !result ) {
						has = false;
						wrong($("#username"), "你的手机号码不正确，请重新输入");
						list[1] = 2;
						return false;
					} else {
						right($("#username"));
						list[1] = 1;
						return true;
					}

				});
			} */
			
		
			
		$("#msgNumber").on("blur", function () {
			var userName = $("#username").val().trim();
			var msgNumber = $("#msgNumber").val().trim();
			if(  msgNumber == "" ) {
				caveat($("#msgNumber"), "请填写手机验证码");
				return false;
				/* if( callNumber != undefined && ischange ) */
			} else  {
				
				$.get("/${ctx}/lg/checkSms.html?random="+Math.random(), {loginName:userName,msgNumber:msgNumber}, function(data){
		   		     var result = data.checkSms_flag;
		   		     console.log(result);
		   		     if( result == "2" ) {
		   		    	wrong($("#msgNumber"), "短信验证码错误，请重新填写！");
		   		    	list[2] = 2;
		   		    	return false;
		   		     } else if(result=="0"){
		   		     	wrong($("#msgNumber"), "请填写手机号码！");
		   		    	list[2] = 2;
		   		    	return false;
		   		     }else if(result=="3"){
		   		     	wrong($("#msgNumber"), "请先发送手机验证码！");
		   		    	list[2] = 2;
		   		    	return false;
		   		     }else {
		   		    	right($("#msgNumber"));
		   		    	/* msgRightNumber = msgNumber;
		   		    	ischange = false; */
		   		    	list[2] = 1;
		   		     }
			   	});
				
			}
			/* else {
				if( msgRightNumber == msgNumber ) {
					right($("#msgNumber"));
	   		    	list[2] = 1;
	   		    	console.log(ischange+"four");
				} else {
					wrong($("#msgNumber"), "短信验证码错误，请重新填写！");
	   		    	list[2] = 2;
	   		    	console.log(ischange+"five");
				}
			} */
		});
		
		$("#password").on("blur", function () {
			var password = $("#password").val().trim();	
			if( password == "" ) {
				caveat($("#password"), "6-20位字符，可使用字母，数字的组合");
				return false;
			}else if( !reg_pass.exec(password) ) {
				wrong($("#password"), "密码格式不正确，请重新输入，6-20位字符，可使用字母，数字的组合");
				list[3] = 2;
				return false;
			} else {
				right($("#password"));
				list[3] = 1;
			}
		});
		
		$("#again").on("blur", function () {
		
			var password = $("#password").val().trim();	
			if( $("#again").val().trim() == "" ) {
				caveat($("#again"), "请填写确认 密码");
				return false;
			} else if( password != $("#again").val().trim() ) {
				wrong($("#again"), "两次密码不一致");
				list[4] = 2;
				return false;
			} else {
				right($("#again"));
				list[4] = 1;
			}
		});
		
		$("#code").on("blur", function () {
			var yqma = $("#code").val().trim();
			if( $("#code").val().trim() == "" ) {
				caveat($("#code"), "请填写邀请码");
				return false;
			} else {
				 $.get("/${ctx}/lg/checkMa.html?random="+Math.random(),{'yqma' : yqma},function(data){
						var result = data.result;
						if(result == "1"){
							wrong($("#code"), "邀请码错误");
							list[5] = 2;
							return false;
						}else if(result == "2"){
							caveat($("#code"), "该邀请码已注册");
							return false;
						}else{
							right($("#code"));
							list[5] = 1;
						}
						
					}); 
			}
		});
		var check = 0;
		$("#check").on("click", function () {
			if( !check ) {
				$("#check").prop("checked",true);
				check = 1;
				list[6] = 1;
				right($("#check"));
			} else {
				$("#check").prop("checked",false);
				check = 0;
				list[6] = 0;
				caveat($("#check"), "是否阅读并同意条款");
			}
			
			
		});

		//表单提交
		var form = $('.base-form');
		
		$('#submitBtn').on('click', function(){
			var num = 0;
			for(var i = 0; i < 7; i++ ) {
				if(list[i] == 1) {
					num++;
				} else if(list[i] == 2) {
					
				} else if(i == 1) {
					caveat($("#username"), "请输入你的11位手机号");
				} else if(i == 2) {
					caveat($("#msgNumber"), "请填写手机验证码");
				} else if(i == 3) {
					caveat($("#password"), "6-20位字符，可使用字母，数字的组合，不建议使用纯数字字母");
				} else if(i == 4) {
					caveat($("#again"), "请填写确认 密码");
				} else if(i == 5) {
					caveat($("#code"), "请填写邀请码");
				} else if(i == 6) {
					caveat($("#check"), "是否阅读并同意条款");
				}
			}
			if( num == 6) {
				form.submit();
			}
			
		});
		
		var imgs = ["/${ctx}/img/pro-029.png","/${ctx}/img/pro-030.png","/${ctx}/img/pro-021.png"];
		imgload(imgs);	
			
		</script>
    </body>
</html>