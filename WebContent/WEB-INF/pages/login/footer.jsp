<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="footer" >
	<div class="context">
		<div class="row">
			<div class="col-5">
				<p>
					<span>地 址：</span>北京市中关村丹棱街5号微软亚太研发集团2号楼2层创投孵化器
				</p>
			</div>
			<div class="col-3">
				<p>
					<span>电 话：</span>(010) 85890318
				</p>
			</div>
			<div class="col-4">
				<p>
					<span>邮 编：</span>100080
				</p>
			</div>
		</div>
		<div class="row row-last">
			<div class="col-5">
				<p>
					<span>技术支持：</span>help@quantgroup.cn
				</p>
			</div>
			<div class="col-3">
				<p>
					<span class="ls">职位申请简历投递：</span>hr@quantgroup.cn
				</p>
			</div>
			<div class="col-4">
				<p>
					<span>@2014</span>quantgroup.cn 京ICP备14008441号
				</p>
			</div>
		</div>
	</div>
	<div class="footer-bg"></div>
</div>
<script type="text/javascript">
var imgload = function (imgs) {
	var length = imgs.length;
	var i = 0;
	for ( ; i < length; i++ ) {
		var img = new Image();
		img.src = imgs[i];
		img.onload = function () {
			return;
		};
	}
};
function Foot () {
	Foot.second = $(".header .down .for-down");
};

Foot.prototype.hide = function () {
	Foot.second.fadeOut('fast');
};

Foot.prototype.show = function () {
	Foot.second.fadeIn('fast');
};

var foot = new Foot();

function Find () {
	Find.count = false;
	Find.downs = $("form").find('.down');
	Find.down = $("form .down");
	Find.second = $("form .down .for-down");
	Find.old_label_list = new Array();
}

Find.prototype.hide = function () {
	if( !Find.count ) {
		Find.down.removeClass('active'); 
		Find.second.fadeOut('fast');
	}
	Find.count = false;
	
};

Find.prototype.show = function () {
	Find.count = true;
	var down = Find.downs.index($(this));
	if( !$(this).hasClass('active') ) {
		Find.downs.each(function (index) {
			if( index != down ) {
				$(this).removeClass('active');
				$(this).children('.for-down').fadeOut('fast');
			}
		});
		$(this).addClass('active');
		$(this).children('.for-down').fadeIn('fast');
	} else {//如果已经显示，隐藏
		$(this).removeClass('active');
		$(this).children('.for-down').fadeOut('fast');
	}
};

Find.prototype.fordown = function () {
	$(this).parent().parent().children("label").text($(this).text());
	$(this).parent().parent().parent().children("input").attr('value',$(this).attr('value'));
};

Find.prototype.saveold = function () {
	Find.downs.each(function (index) {
		Find.old_label_list.push($(this).children('label').text());
	});
};

Find.prototype.turnold = function () {
	Find.downs.each(function (index) {
		$(this).children('label').text(Find.old_label_list[index]);
		$(this).parent().children('input').val("");
	});
};

var find = new Find();

find.saveold();

function hideTwo () {
	foot.hide();
	find.hide();
}

$(document)
	.on('mouseleave', '.header .down', foot.hide)
	.on('mouseenter', '.header .down', foot.show)
	.on('click', find.hide)
	.on('click', '#modal-taobao .down', find.show)
	.on('click', '#modal-taobao .down li', find.fordown);
</script>