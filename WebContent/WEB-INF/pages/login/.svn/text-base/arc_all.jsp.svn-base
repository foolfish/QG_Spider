<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<jsp:include page="/common/domain.jsp" flush="true"/> 
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		<title>用户授权</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<link rel="stylesheet" type="text/css" href="/${ctx}/css/g_main.css">
		<script src="/${ctx}/js/jquery-1.9.1.min.js"></script>
		<script>
		var pax = 0;
		var pay = 0;
		var position = 0;
		var state_list = [0,{"zhengxin":0},{"xuexin":0},{"geren":0},{"jingdong":0},{"taobao":0},{"shebao":0},{"shouji":0}];
		var str_list = [0,"zhengxin","xuexin","geren","jingdong","taobao","shebao","shouji"];
		$.get("/${ctx}/getCheckedPart.html?random="+Math.random(), {userName:""}, function(data){
			/* String phoneFlag = "0";//手机号数据源
			String jingDongFlag = "0";//京东数据源
			String taoBaoFlag = "0";//淘宝数据源
			String xueXinFlag = "0";//学信数据源
			String zhengXinFlag = "0";//征信数据源
			String baseFlag = "0";//个人信息数据源
			String shebaoFlag = "0";//社保数据源 */

			if(  data.zhengXinFlag == "1" ) {
				state_list[1]["zhengxin"] = 1;
			}
			if(  data.xueXinFlag == "1" ) {
				state_list[2]["xuexin"] = 1;
			}
			if(  data.baseFlag == "1" ) {
				state_list[3]["geren"] = 1;
			}
			if(  data.jingDongFlag == "1" ) {
				state_list[4]["jingdong"] = 1;
			}
			if(  data.taoBaoFlag == "1" ) {
				state_list[5]["taobao"] = 1;
			}
			if(  data.shebaoFlag == "1" ) {
				state_list[6]["shebao"] = 1;
			}
			if(  data.phoneFlag == "1" ) {
				state_list[7]["shouji"] = 1;
			}
			count_red();
	   	});
		
		var g_data = 0;
		var old_data = 0;
		var add_data;
		
		function count_red () {
			var g_ii;
			var g_count = 0;
			for( g_ii = 1; g_ii < 8; g_ii++) {
				if( state_list[g_ii][str_list[g_ii]] == 1 ) {
					$(".pro").eq(g_ii*2-1).removeClass("hide").addClass("show");
					g_count++;
				}
			}
			
			for( g_ii = 1; g_ii <= g_count; g_ii++) {
				$(".red"+g_ii+"").removeClass("hide").addClass("show");
			}
			$.get('/${ctx}/getPart.html?random='+Math.random(),function(data){
				old_data = data;
				if( data != 0 ) {
					add_data = setInterval(function () {
						if( g_data == old_data ) {
							clearInterval(add_data);
							g_data = old_data;
						} else {
							g_data++;
							$("#percent_center").text(g_data+"%");
						}
					}, 50);
				}
				
			});
		}
		
		 var g_make ;
		 function gg_wrong (node,text) {
			clearTimeout(g_make);
			node.find(".wrong-tip").text(text);
			node.find(".wtip").slideDown("fast");
			g_make = setTimeout(function () {
				node.find(".wtip").slideUp("fast");
			}, 2000);
		 }
		
		</script>
<body>
	<jsp:include page="header.jsp" flush="false"/>
	<div class="find" style="height:45px;">
	</div>
	<div class="grid" id="grid-nine">
		<div class="container">
			<div class="row feat">
				<div class="col-8" style="margin-left:-76px;">
					<img src="/${ctx}/img/pro3-2.png" alt="" class="shou2"/>
					<img src="/${ctx}/img/pro3-1.png" alt="" class="shou1"/>
					<div class="section"></div>
					<div class="pro zx1 hide bg"></div>
					<div class="pro zx2 hide"></div>
					<div class="pro xx1 hide bg"></div>
					<div class="pro xx2 hide"></div>
					<div class="pro gr1 hide bg"></div>
					<div class="pro gr2 hide"></div>
					<div class="pro jd1 hide bg"></div>
					<div class="pro jd2 hide"></div>
					<div class="pro tb1 hide bg"></div>
					<div class="pro tb2 hide"></div>
					<div class="pro sb1 hide bg"></div>
					<div class="pro sb2 hide"></div>
					<div class="pro sj1 hide bg"></div>
					<div class="pro sj2 hide"></div>
					<div class="pro_nei yuan"></div>
					<div class="pro_nei white"></div>
					<div class="for_font">
						<span>已完成</span>
						<span id="percent_center">0%</span>
					</div>
					<div class="finger"></div>
				</div>
				<div class="col-4">
					<div class="section show" id="sec-zero">
						<h1 style="font-size:18px;width:326px;">请您点击左侧圆形部分授权个人信息！</h1>
					</div>
					<jsp:include page="arc_zhengxin.jsp" flush="true"/>
					<jsp:include page="arc_xuexin.jsp" flush="true"/>
					<jsp:include page="arc_geren.jsp" flush="true"/>
					<jsp:include page="arc_jingdong.jsp" flush="true"/>
					<jsp:include page="arc_taobao.jsp" flush="true"/>
					<jsp:include page="arc_shebao.jsp" flush="true"/>
					<jsp:include page="arc_shouji.jsp" flush="true"/>
					
				</div>
			</div>
			<div class="modal hide" id="modal-submit">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h4>关于登录</h4>
							<a href="javascript:closesubmit();">
								<img src="/${ctx}/img/pro-031.png" alt="" class="cross"/>
							</a>	
						</div>
						<div class="modal-body">		
							<form>
								<div class="group gg-first">
									<img src="/${ctx}/img/pro-029.png" alt="" class="gg-img" />
									<label for="com" id="groupId">淘宝登录成功！</label>
								</div>
								<div class="group">
									<button type="button" class="done done-first" onclick="saveReport();">
										提交报告
									</button>
									<button type="button" class="done done-last" onclick="closesubmit();">
										继续填写报告
									</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="modal hide" id="modal-succ">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h4>报告提交</h4>
							<a href="javascript:closesucc();">
								<img src="/${ctx}/img/pro-031.png" alt="" class="cross"/>
							</a>	
						</div>
						<div class="modal-body">
							<form>
								<div class="group gg-first">
									<img src="/${ctx}/img/pro-029.png" alt="" class="gg-img" />
									<label class="gg_label1">报告提交成功！有效期为<span>30天！</span></label>
								</div>
								<div class="group">
									<label class="gg_pro">请注意</label>
									<label class="gg_pro">你只完成报告的<span id="percent">25%</span></label>
									<label class="gg_pro">报告完成度越高</label>
									<label class="gg_pro">更有利于您的申请贷款</label>
								</div>
								<div class="group">
									<button type="button" class="done" onclick="closesucc();">
										继续填写报告
									</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
	
			<div class="modal-bg-two"></div>
		</div>
	</div>
	<jsp:include page="footer.jsp" flush="false"></jsp:include>
	<script>
	if($(window).height() >= 700) {
		$("#grid-nine").height($(window).height()-137);
	}
	var imgs = ["/${ctx}/img/pro3-1.png","/${ctx}/img/pro3-2.png","/${ctx}/img/a-007.png",
	            "/${ctx}/img/pro-031.png","/${ctx}/img/pro4-2.png",
	            "/${ctx}/img/pro2-3.png","/${ctx}/img/pro2-4.png","/${ctx}/img/pro2-8.png",
	            "/${ctx}/img/pro2-9.png","/${ctx}/img/pro2-10.png","/${ctx}/img/pro2-11.png",
	            "/${ctx}/img/pro2-12.png","/${ctx}/img/pro2-13.png","/${ctx}/img/pro2-16.png",
	            "/${ctx}/img/pro2-17.png","/${ctx}/img/pro2-19.png","/${ctx}/img/pro2-20.png",
	            "/${ctx}/img/pro2-22.png","/${ctx}/img/pro2-23.png"];
	imgload(imgs);
		
	$(document).ready(function () {
		
		

		var window_width = $(window).width();
		$(window).resize(function () {
			window_width = $(window).width();
		});
		
		for( var i = 1; i < 8; i++) {
				 
			if( state_list[i][str_list[i]] == 1 ) {
				$(".pro").eq(i*2-1).removeClass("hide").addClass("show");
				console.log(i);
			}
		}
		var isclear = 0;
		$(".shou1").animate({
			left:"455px",
			top:"245px"
		},1600,function () {
			gg_clear();
			$(".shou2").fadeIn();
			var count = 0;
			var arc_shan = setInterval(function () {
				if( !count ) {
					gg_clear();
					$(".shou2").fadeOut(400);
					count = 1;
				}else {
					gg_clear();
					$(".shou2").fadeIn(400);
					count = 0;
				}
			},800);
			
			setTimeout(function () {
				clearInterval(arc_shan);
				$(".shou2").fadeOut(250);
				$(".shou1").fadeOut(250);
			},4000,function () {
				$(".shou2").removeClass("hide").addClass("show");
				$(".shou1").removeClass("hide").addClass("show");
			});

		});
		
		$(document).one("click",function () {
			isclear = 1;
		});
		
		function gg_clear () {
			if( isclear ) {
				$(".shou2").addClass("hide");
				$(".shou1").addClass("hide");
			}
		}
		
		var f_count = false;
		var g_index = 0;
		
		$(document).on("mousemove", ".finger", f_move)
				   .on("mouseleave", ".finger", f_leave)
				   .on("mousedown", ".finger", f_down);
		
		function show_img3 () {
			if( f_count ) {
				for( var i = 1; i < 8; i++) {
					$(".pro").eq(i*2-2).removeClass("show").addClass("hide");
					if( state_list[i][str_list[i]] == 2 ) {
						state_list[i][str_list[i]] = 0;
					}
				}
				
				g_section($("#sec-zero"));
				g_index = 0;
				position = 0;
			}
			f_count = false;
		}
		
		function f_move (e) {
			if( window_width >= 1200 ) {
				pax = e.pageX-parseInt((window_width-1200)/2) -75;
				pay = e.pageY - 130;
			}else {
				pax = e.pageX - 75;
				pay = e.pageY -130;
			}
			pax = pax - 79;
			pay = pay - 91;
			down(pax, pay, show_img2);
		}
		
		function f_leave (e) {
			f_count = true;
			for( var i = 1; i < 8; i++) {
				if(state_list[i][str_list[i]] != 2){
					$(".pro").eq(i*2-2).removeClass("show").addClass("hide");
				}
			}
		}
		
		function f_down (e) {
			if( window_width >= 1200 ) {
				pax = e.pageX-parseInt((window_width-1200)/2) -75;
				pay = e.pageY - 130;
			}else {
				pax = e.pageX - 75;
				pay = e.pageY -130;
			}
			pax = pax - 79;
			pay = pay - 91;
			down(pax, pay, show_img);
		}
		
		
		function down (pax, pay, callback) {
			
			if ( (pax - 230)*(pax - 230) + (pay - 230)*(pay - 230) <= 204*204 ){
				if( (pax - 230)*(pax - 230) + (pay - 230)*(pay - 230) > 94*94 ){
				
					if( pay < (pax-230)*(230-30)/(231-230) + 230 &&
						pay < (pax-230)*(130-230)/(400-230) + 230 ) {
						position = 1;
					}

					if( pay > (pax-230)*(130-230)/(400-230) + 230  &&
						pay < (pax-230)*(322-230)/(410-230) + 230 ) {
						position = 2;
					}

					if( pay > (pax-230)*(322-230)/(410-230) + 230  &&
						pay > (pax-230)*(420-230)/(177-230) + 230 ) {
						position = 3;
					}

					if( pay < (pax-230)*(420-230)/(177-230) + 230  &&
						pay > (pax-230)*(378-230)/(94-230) + 230 ) {
						position = 4;
					}

					if( pay < (pax-230)*(378-230)/(94-230) + 230  &&
						pay > (pax-230)*(290-230)/(40-230) + 230 ) {
						position = 5;
					}

					if( pay < (pax-230)*(290-230)/(40-230) + 230  &&
						pay > (pax-230)*(130-230)/(55-230) + 230 ) {
						position = 6;
					}

					if( pay < (pax-230)*(130-230)/(55-230) + 230  &&
						pay > (pax-230)*(230-40)/(231-230) + 230 ) {
						position = 7;
					}
					
					callback();
				}
			}
			
		}
		
		function show_img () {
			for( var i = 1; i < 8; i++) {
					$(".pro").eq(i*2-2).removeClass("show").addClass("hide");
					state_list[i][str_list[i]] = 0;
				if( position == i ) {
					$(".pro").eq(i*2-2).removeClass("hide").addClass("show");
					state_list[i][str_list[i]] = 2;
				}
			}
			$("#sec-zero").removeClass("show").addClass("hide");
			$("#sec-one").removeClass("show").addClass("hide");
			$("#sec-two").removeClass("show").addClass("hide");
			$("#sec-three").removeClass("show").addClass("hide");
			$("#sec-four").removeClass("show").addClass("hide");
			$("#sec-five").removeClass("show").addClass("hide");
			$("#sec-six").removeClass("show").addClass("hide");
			$("#sec-seven").removeClass("show").addClass("hide");

			if( position == 1 ) {
				
				g_section($("#sec-one"));
				g_index = 1;
			}

			if( position == 2 ) {
				
				g_section($("#sec-two"));
				g_index = 2;
			}

			if( position == 3 ) {
				
				g_section($("#sec-three"));
				g_index = 3;
			}

			if( position == 4 ) {
				
				g_section($("#sec-four"));
				g_index = 4;
			}

			if( position == 5 ) {
				
				g_section($("#sec-five"));
				g_index = 5;
			}

			if( position == 6 ) {
				g_section($("#sec-six"));
				g_index = 6;
			}

			if( position == 7 ) {
				g_section($("#sec-seven"));
				g_index = 7;
			}
			
			position = 0;
		}
		
		function g_section (node) {
			if( position == g_index ) {
				node.removeClass("hide").addClass("show");
				return;
			}
			var node_after = $("#sec-zero");
			if( g_index == 0 ) { node_after = $("#sec-zero"); }		
			if( g_index == 1 ) { node_after = $("#sec-one"); }
			if( g_index == 2 ) { node_after = $("#sec-two"); }
			if( g_index == 3 ) { node_after = $("#sec-three"); }
			if( g_index == 4 ) { node_after = $("#sec-four"); }
			if( g_index == 5 ) { node_after = $("#sec-five"); }
			if( g_index == 6 ) { node_after = $("#sec-six"); }
			if( g_index == 7 ) { node_after = $("#sec-seven"); }

			node_after.clearQueue();
			node_after.stop();
			node_after.removeClass("hide").addClass("show");
			node_after.animate({
				left:"-377px"
			},500, function () {
				node_after.removeClass("show").addClass("hide");
				node_after.css("left", "0px");
			});
			
			node.clearQueue();
			node.stop();
			node.css("left", "377px");
			node.removeClass("hide").addClass("show");
			node.animate({
				left:"0px"
			},500,function () {
				node.css("left", "0px");
			});
		}
		
		
		function show_img2 () {
			for( var i = 1; i < 8; i++) {
				if(state_list[i][str_list[i]] != 2){
					$(".pro").eq(i*2-2).removeClass("show").addClass("hide");
				}
				if( position == i ) {	
					if(state_list[i][str_list[i]] != 2) {
						$(".pro").eq(i*2-2).removeClass("hide").addClass("show");
					}
				}
			}
			
			position = 0;
		}
	});
	
	function add () {
		if( g_data == 0 ) {
			add_data = setInterval(function () {
				if( g_data == old_data ) {
					clearInterval(add_data);
					g_data = old_data;
				} else {
					g_data++;
					$("#percent_center").text(g_data+"%");
				}
			}, 50);
		} else {
			add_data = setInterval(function () {
				if( g_data == old_data ) {
					clearInterval(add_data);
					g_data = old_data;
				} else {
					g_data++;
					$("#percent_center").text(g_data+"%");
				}
			}, 50);
		}
	}
	
	

	function saveReport(){
		
		$.get('/${ctx}/getPart.html?random='+Math.random(),function(data){
			old_data = data;
			$("#percent").text(data+"%");
		});
		$("#modal-submit").attr("class","modal hide");
		$("#modal-succ").attr("class","modal show");
	
	}
	
	function closesubmit(){
		$.get('/${ctx}/getPart.html?random='+Math.random(),function(data){
			old_data = data;
			add();
		});
		$("#modal-submit").attr("class","modal hide");
		$('.modal-bg-two').css("display", "none");
		
	}
	function closesucc(){
		$("#modal-succ").attr("class","modal hide");
		$('.modal-bg-two').css("display", "none");
		
		add();
	}
	function closetaobao(){
		$("#modal-taobao").attr("class","modal hide");
		$('.modal-bg-two').css("display", "none");
	}
	function closecom(){
		$("#modal-com").removeClass("show").addClass("hide");
		$('.modal-bg-two').css("display", "none");
	}


	function act(source,type,content){
		 $('.modal-bg-two').css("display", "block");
		for( var g_i = 1; g_i < 8; g_i++ ) {
			 if( str_list[g_i] == source ){
				 state_list[g_i][str_list[g_i]] = 1;
			 }
		 }
		 $("."+type+"1").removeClass("show").addClass("hide");
		 $("."+type+"2").removeClass("hide").addClass("show");
		 $("#groupId").text(content);
		 $("#modal-submit").attr("class","modal show");
	}
	
	</script>
</body>
</html>	