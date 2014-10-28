	/* 公用js工具 pjk */
	;(function(){
		$(document).ready(function(){
			//hover工具
			$('[trigger]').on('mouseover', function(){
				var thisObj = $(this),
					triggerName = thisObj.attr('trigger');
				thisObj.addClass(triggerName);
			}).on('mouseleave', function(){
				var thisObj = $(this),
					triggerName = thisObj.attr('trigger');
				thisObj.removeClass(triggerName);
			});

			//查看密码
			$('.base-icon-keyboard').mousedown(function(){
				var inputObj = $(this).siblings('[type=\'password\']');
				var objClass = inputObj.attr('class');
				inputObj.after($('<input type="text" needcheck="rePassword"/>').addClass(objClass).val(inputObj.val()));
				inputObj.css('display', 'none');
			}).mouseup(function(){
				var inputObj = $(this).siblings('[type=\'password\']');
				$(this).prev('[type=\'text\']').remove();
				inputObj.css('display', 'inline');
			});

		});
	})(window.jquery);
	