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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="/${ctx }/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/${ctx }/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/${ctx }/js/jquery.json-2.2.min.js"></script>
</head>
    <body>
    	<div class="base-topbar">
    		<p class="base-topbar-inner">
    			你好，<span id="userName">${loginName }</span>！<a href="/${ctx}/lg/loginout.html">退出</a>
    		</p>
    	</div>
    	<div class="base-main">
    		<p class="base-path">
    			<span><a href="/${ctx}/entrance.html">首页</a></span>><span>话费详单</span>
    		</p>
    		<div class="base-title clearfix">
    			<h1>话费详单</h1>
    		</div>
    		<div class="clearfix">
    			<div class="base-leftmain">
   				<!-- 联通 -->
  				<c:if test="${not empty putong_sh_liantong_url}">
					<jsp:include page="/lt/sh_lt_Phone.jsp" flush="true"/>		
				</c:if>
  				
	  			<!-- 上海 -->
				<c:if test="${not empty putong_sh_dianxin_url}">
			        <jsp:include page="/dx/sh_dx_Phone.jsp" flush="true"/> 
		        </c:if>      
				<c:if test="${not empty putong_sh_yidong_url}">
					<jsp:include page="/yd/sh_yd_Phone.jsp" flush="true"/>		
				</c:if>
			
				
				<!-- 江西 -->
				<c:if test="${not empty putong_jx_yidong_url}">
					<jsp:include page="/yd/jx_yd_Phone.jsp" flush="true"/>		
				</c:if>
				
			
				
				<!-- 重庆-->
				<c:if test="${not empty putong_cq_dianxin_url}">
					<jsp:include page="/dx/cq_dx_Phone.jsp" flush="true"/>		
				</c:if>
				
			
				<!-- 北京电信 -->
				<c:if test="${not empty putong_bj_dianxin_url}">
					<jsp:include page="/dx/bj_dx_Phone.jsp" flush="true"/>		
				</c:if>
				
				<!-- 北京移动-->
				<c:if test="${not empty putong_bj_yidong_url}">
					<jsp:include page="/yd/bj_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 广东电信-->
				<c:if test="${not empty putong_gd_dianxin_url}">
					<jsp:include page="/dx/gd_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 湖南电信-->
				<c:if test="${not empty putong_hunan_dianxin_url}">
					<jsp:include page="/dx/hunan_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 海南电信-->
				<c:if test="${not empty putong_hainan_dianxin_url}">
					<jsp:include page="/dx/hainan_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 河北电信-->
				<c:if test="${not empty putong_hebei_dianxin_url}">
					<jsp:include page="/dx/hebei_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 辽宁移动-->
				<c:if test="${not empty putong_ln_yidong_url}">
					<jsp:include page="/yd/ln_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 河南移动-->
				<c:if test="${not empty putong_hen_yidong_url}">
					<jsp:include page="/yd/hen_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 湖北移动-->
				<c:if test="${not empty putong_hub_yidong_url}">
					<jsp:include page="/yd/hub_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 黑龙江移动-->
				<c:if test="${not empty putong_hlj_yidong_url}">
					<jsp:include page="/yd/hlj_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 黑龙江电信-->
				<c:if test="${not empty putong_hlj_dianxin_url}">
					<jsp:include page="/dx/hlj_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 黑龙江电信-->
				<c:if test="${not empty putong_hub_dianxin_url}">
					<jsp:include page="/dx/hub_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 辽宁电信-->
				<c:if test="${not empty putong_ln_dianxin_url}">
					<jsp:include page="/dx/ln_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 福建电信-->
				<c:if test="${not empty putong_fj_dianxin_url}">
					<jsp:include page="/dx/fj_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 福建移动-->
				<c:if test="${not empty putong_fj_yidong_url}">
					<jsp:include page="/yd/fj_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 浙江移动-->
				<c:if test="${not empty putong_zj_yidong_url}">
					<jsp:include page="/yd/zj_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 陕西移动-->
				<c:if test="${not empty putong_sx_yidong_url}">
					<jsp:include page="/yd/sx_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 陕西移动-->
				<c:if test="${not empty putong_sx_dianxin_url}">
					<jsp:include page="/dx/sx_dx_Phone.jsp" flush="true"/>		
				</c:if>
				
				<!-- 江苏 -->
				<c:if test="${not empty putong_js_yidong_url}">
					<jsp:include page="/yd/js_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<c:if test="${not empty putong_js_dianxin_url}">
					<jsp:include page="/dx/js_dx_Phone.jsp" flush="true"/>		
				</c:if>
				
				<!-- 浙江 -->
				<c:if test="${not empty putong_zj_dianxin_url}">
					<jsp:include page="/dx/zj_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 广东移动 -->
				<c:if test="${not empty putong_gd_yidong_url}">
					<jsp:include page="/yd/gd_yd_Phone.jsp" flush="true"/>		
				</c:if>
    			<!-- 天津移动 -->
				<c:if test="${not empty putong_tj_yidong_url}">
					<jsp:include page="/yd/tj_yd_Phone.jsp" flush="true"/>		
				</c:if>
    			<!-- 天津电信 -->
				<c:if test="${not empty putong_tj_dianxin_url}">
					<jsp:include page="/dx/tj_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 甘肃电信 -->
				<c:if test="${not empty putong_gs_dianxin_url}">
					<jsp:include page="/dx/gs_dx_Phone.jsp" flush="true"/>		
				</c:if>
    			<!-- 四川电信 -->
				<c:if test="${not empty putong_sc_dianxin_url}">
					<jsp:include page="/dx/sc_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 山东电信 -->
				<c:if test="${not empty putong_sd_dianxin_url}">
					<jsp:include page="/dx/sd_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 四川移动 -->
				<c:if test="${not empty putong_sc_yidong_url}">
					<jsp:include page="/yd/sc_yd_Phone.jsp" flush="true"/>		
				</c:if>
				
				<!-- 新疆移动 -->
				<c:if test="${not empty putong_xj_yidong_url}">
					<jsp:include page="/yd/xj_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 广西电信-->
				<c:if test="${not empty putong_gx_dianxin_url}">
					<jsp:include page="/dx/gx_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 新疆电信-->
				<c:if test="${not empty putong_xinjiang_dianxin_url}">
					<jsp:include page="/dx/xinjiang_dx_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 山东移动 -->
				<c:if test="${not empty putong_sd_yidong_url}">
					<jsp:include page="/yd/sd_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 重庆移动 -->
				<c:if test="${not empty putong_cq_yidong_url}">
					<jsp:include page="/yd/cq_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 内蒙古移动 -->
				<c:if test="${not empty putong_nmg_yidong_url}">
					<jsp:include page="/yd/nmg_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 西藏移动 -->
				<c:if test="${not empty putong_xz_yidong_url}">
					<jsp:include page="/yd/xz_yd_Phone.jsp" flush="true"/>		
				</c:if>
				<!-- 河北移动 -->
				<c:if test="${not empty putong_heb_yidong_url}">
					<jsp:include page="/yd/heb_yd_Phone.jsp" flush="true"/>		
				</c:if>
				
				
				<jsp:include page="/common/part.jsp" flush="true"/>
			</div>
    	</div>
    	<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="js/formCheck.js"></script>
		<script type="text/javascript">
			var form1 = $('#form1');
			var formCheck1 = form1.formCheck();
			$('#submitBtn1').on('click', function(){
				if(!formCheck1.checkForm()){
					return;
				} else {
					var password = $('#password').val();
					$.get('checkLoginName.html?random='+Math.random(), {'password' : password}, function(data){
						if(data == 0) {//进入 动态密码
							$('#form1').addClass('base-element-hidden');
							$('#form2').removeClass('base-element-hidden');
						} else {
							$('#form1').submit();
						}
					});
				}
			});

			//验证码验证
			$('#checkNum').on('blur', function(){
				var checkNum = $('#checkNum').val();
				if(checkNum == ''){return;}
				$.get('checkLoginName.html?random='+Math.random(),{'checkNum' : checkNum},function(data){
					if(data == 0){
						formCheck1.alertError($('#checkNum'), '验证码错误'); 
					}
				});
			});

			var form2 = $('#form2');
			var formCheck2 = form2.formCheck();
			$('#submitBtn1').on('click', function(){
				if(!formCheck2.checkForm()){
					return;
				} else {
					form2.submit();
				}
			});

			$('#upstepBtn2').on('click', function(){
				$('#form1').removeClass('base-element-hidden');
				$('#form2').addClass('base-element-hidden');
			});

			//手机验证码验证
			$('#checkMsg').on('blur', function(){
				var checkMsg = $('#checkMsg').val();
				if(checkMsg == ''){return;}
				$.get('checkLoginName.html?random='+Math.random(),{'checkMsg' : checkMsg},function(data){
					if(data == 0){
						formCheck2.alertError($('#checkMsg'), '手机验证码错误'); 
					}
				});
			});
			
			
			
	$(function(){
		$('#lastBtn').click(function(){
			history.go(-1);
		});
	});
	
	
	function cancel(a){
		window.location.href="/${ctx}/entranceEmail.html";		
	}
	
	//ptype:1代表移动；2代表联通，3代表电信
	function checkTelno(idName,ptype){
		var telno = $('#'+idName+'_userName').val();
		
		if (telno.length != 11) {			
			$("#"+idName+"_userName_tip").text('请输入11位手机号码').show();				
			return false;
		}
		if (isNaN(telno)) {			
			$("#"+idName+"_userName_tip").text('手机号码必须是11位数字').show();				
			return false;
		}
		
		var mol="134,135,182,136,137,138,139,150,151,152,157,158,159,183,187,188,147,184";
		
		if(ptype==2){
			mol="130,131,132,155,156,185,186";
		}
		if(ptype==3){
			mol="133,153,180,181,189";
		}
		var mo2 = telno.substring(0, 3);
		if (mol.indexOf(mo2)==-1){
			$("#"+idName+"_userName_tip").text("请输入正确的手机号码").show();
			return false;
		}
		return true;
	}
	
	function checkAllhidden(){
		var flag = true;
		var count = 0;
		$("input[id$='_hidden']").each(function () { 
			var authName = this.name; 
			var value = $("#"+authName).attr("value");
			if(value.indexOf("false")>=0){
				count=count+1;			
				}    
	        });
		if(count>0){
			flag = false;
		}
		return flag;
		
	}
	$(function(){
		document.onkeydown = function(e){ 
				 var ev = document.all ? window.event : e;
			if(ev.keyCode==13) {
       			$('#saveBtn').click();//处理事件
 			}	
		}
	});  		
			
	
	
	
		</script>
    </body>
</html>