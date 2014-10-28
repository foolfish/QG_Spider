	/* 表单验证工具 pjk */
	;(function(){
		$.fn.formCheck = function(options){
			var regexList = {
				'loginName' : {
					regex : /^[a-zA-Z0-9_\u4e00-\u9fa5]{4,16}$/,
					alert : '4-16位字符，可包含中文，英文，数字和“_”。注册完成后不可修改',
					error : '用户名格式错误，请输入正确的用户名',
					notnull : '用户名不能为空'
				},
				'phone' : {
					regex : /^1[3|4|5|8][0-9]\d{4,8}$/,
					alert : '请输入您常用的手机号码',
					error : '手机号码格式错误，请输入正确的11位手机号码，如：18701623512',
					notnull : '手机号不能为空'
				},
				'checkMsg' : {
					regex : /^[a-zA-Z0-9]+$/,
					alert : '',
					error : '请输入正确的手机验证码，如收不到验证码，请与客服联系',
					notnull : '手机验证码不能为空'
				},
				'checkYqm' : {
					regex : /^[a-zA-Z0-9]+$/,
					alert : '',
					error : '请输入正确的邀请码',
					notnull : '邀请码不能为空'
				},
				'password' : {
					regex : /^[1-9a-zA-Z`~!@#$%^&\*()-_=+\{\}\[\]\\\;\'\"\,\.\<\>\?\/]{6,18}$/,
					alert : '密码须为6-18位英文字母、数字和符号(不包括空格)',
					error : '登录密码格式错误，请输入正确的登录密码',
					notnull : '密码不能为空'
				},
				'rePassword' : {
					regex : /^[1-9a-zA-Z`~!@#$%^&\*()-_=+\{\}\[\]\\\;\'\"\,\.\<\>\?\/]{6,18}$/,
					alert : '请再次输入登录密码',
					error : '登录密码与再次输入密码不一致,请重新输入',
					notnull : '请输入确认密码'
				},
				'checkNum' : {
					regex : /^\S+[a-zA-Z0-9]$/,
					alert : '',
					error : '请输入正确的验证码',
					notnull : '验证码不能为空'
				},
				'userName' : {
					regex : /^[a-zA-Z \u4e00-\u9fa5]{2,15}$/,
					alert : '请输入您的真实姓名',
					error : '姓名不符合规范，请输入2~15个字的中文或英文',
					notnull : '姓名不能为空'
				},
				'userId' : {
					regex : /^(\d{6})(18|19|20)?(\d{2})([01]\d)([0123]\d)(\d{3})(\d|X)?$/,
					alert : '请输入您的身份证号码',
					error : '请输入有效的身份证号码',
					notnull : '身份证号码不能为空'
				},
				'email' : {
					regex : /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/,
					alert : '请输入您的常用电子邮箱',
					error : '请输入有效的eamil',
					notnull : '邮箱不能为空'
				},
				'QQ' : {
					regex : /^\d{5,10}$/,
					alert : '请输入您的QQ号',
					error : '请输入正确的QQ号',
					notnull : 'QQ号不能为空'
				},
				'userName2' : {
					regex : /^\S+$/,
					alert : '请输入征信网站注册账号',
					error : '请输入登录名',
					notnull : '登录名不能为空'
				},
				'userId2' : {
					regex : /^(\d{6})(18|19|20)?(\d{2})([01]\d)([0123]\d)(\d{3})(\d|X)?$/,
					alert : '请输入征信网站注册的身份证号',
					error : '请输入有效身份证号',
					notnull : '身份证不能为空'
				}
			}

			//下拉框初始化
			if(typeof diy_select != 'undefined') {
				var TTDiy_select = new diy_select({  //参数可选
					TTContainer:'diy_select',//控件的class
					TTDiy_select_input:'diy_select_input',//用于提交表单的class
					TTDiy_select_txt:'diy_select_txt',//diy_select用于显示当前选中内容的容器class
					TTDiy_select_btn:'diy_select_btn',//diy_select的打开按钮
					TTDiv_select_list:'diy_select_list',//要显示的下拉框内容列表class
					TTFcous:'focus'//得到焦点时的class
				});
			}
			
			var obj = $(this),
				form_classname = 'base-form',
				fieldset_classname = 'base-fieldset',
				fieldset_title_classname = 'base-fieldset-title',
				fieldset_input_classname = 'base-fieldset-input';
				fieldset_trigger_classname = 'fc-parent-trigger',
				fieldset_error_classname = 'fc-parent-error',
				alert_classname = 'base-alert',
				error_classname = 'base-alert-error',
				input_hover = 'fc-input-hover',
				input_right = 'fc-input-right',
				input_error = 'fc-input-error';
			if(options && options.addRegex){
				$.extend(true, regexList, options.addRegex);
			}

			if(obj){
				init(obj);
			} else {
				alert('无内容，初始化失败');
				return;
			}

			return {
				'checkForm' : checkForm,
				'alertError' : checkDefult,
				'checkFormNotNull' : checkFormNotNull
			};

			function init(obj){
				//obj.attr('onsubmit','return $(\'this\').formCheck().checkform(this)');
				obj.find('.'+fieldset_title_classname+'[require]').each(function(){
					thisObj = $(this);
					//var star = $('<span class="base-fieldset-require">*</span>');
					//thisObj.prepend(star);
				});
				//obj.find('input[type!=\'checkbox\']').addClass(fieldset_input_classname);
				obj.find('[checktype]').each(function(){
					var thisObj = $(this),
						checkName = thisObj.attr('needcheck'),
						checkType = thisObj.attr('checktype'),
						notNull = thisObj.attr('notnull');
					if(typeof checkName != 'undefined' && typeof checkType != 'undefined' || typeof notNull != 'undefined'){
						putLayout(thisObj, checkName);
					}
					
					if(typeof checkType == 'undefined' || checkType == ''){
						return;
					} else {
						thisObj.on('focus', function(){
							thisObj.removeClass(input_hover+' '+input_right+' '+input_error).addClass(input_hover);
							if(checkType == 'checkbox'){
								thisObj.removeClass(input_hover);
							}
							var parentObj = $(this).parents('.'+fieldset_classname);
							parentObj.removeClass(fieldset_error_classname).find('.'+alert_classname).stop().fadeIn(300);
						}).on('blur', function(){
							var element = $(this);
							element.parents('.'+fieldset_classname).find('.'+alert_classname).css('display', 'none');
							if(checkType == 'input'){
								checkRegex(checkName, element);
							}
						});

						if(checkType == 'select'){
							thisObj.children().on('click', function(){
								thisObj.removeClass(input_error);
								var parentObj = $(this).parents('.'+fieldset_classname);
								parentObj.removeClass(fieldset_error_classname);
							});
						}
					}
				});
			}

			function checkForm(){
				var thisObj = obj;
				var result = true;
				thisObj.find('[checktype][notnull]').each(function(){
					var obj = $(this),
						checkType = obj.attr('checktype');
						
					if(checkType == 'input') {//如果是输入框
						var result2 = checkRegex(checkName = obj.attr('needcheck'), obj);
						result = result && result2;
					} else if(checkType == 'select') {
						var result2 = checkSelect(obj);
						result = result && result2;
					} else if(checkType == 'checkbox') {
						var result2 = checkCheckbox(obj);
						result = result && result2;
					}
				});
				return result;
			}
			
			function checkFormNotNull(){
				var thisObj = obj;
				var result = true;
				thisObj.find('[checktype=\'input\']').each(function(){
					var obj = $(this),
						checkType = obj.attr('checktype');
					var result2 = (obj.val() != '');
					result = result && result2;
				});
				return result;
			}

			function putLayout(obj, checkName){
				var parentObj = obj.parents('.'+fieldset_classname);
				if(parentObj.find('p.base-alert-error').length == 0){
					parentObj.append($('<p class="base-alert-error"></p>'));
				}
				var regexObj = regexList[checkName];
				if(typeof regexObj == 'undefined'){
					return;
				}
				var alertMsg = regexObj.alert;
				if(alertMsg != '' && parentObj.find('p.base-alert').length == 0){
					var alertElement = $('<p class="base-alert"></p>').append('<span class="base-alert-trigger"></span>')
						.append($('<span></span>').html(alertMsg));
					parentObj.append(alertElement);
				}
			}

			function checkRegex(checkName, obj){
				var value = obj.val(),
					result = false,
					errorMsg;
				var notNull = obj.attr('notnull');
				if(typeof notNull == 'undefined'){//可以为空
					if(value == ''){//无内容 则取消焦点
						checkBack(obj);
						return true;
					} else {//有内容 需要进行判断是否正则
						if(typeof checkName != 'undefined' && checkName != ''){//需要正则匹配
							result = testRegex(checkName, obj);
							if(!result) {errorMsg = regexList[checkName].error;}
						} else {//无正则匹配
							checkBack(obj);
							return true;
						}
					}
				} else {//不能为空
					if(value == ''){
						if(typeof checkName != 'undefined' && checkName != ''){//有匹配的语句
							var regexObj = regexList[checkName];
							errorMsg = regexObj.notnull;
							result = false;
						} else {//无匹配
							errorMsg = '不能为空';
							result = false;
						}
						
					} else {
						if(typeof checkName != 'undefined' && checkName != ''){//需要正则匹配
							result = testRegex(checkName, obj);
							if(!result) {errorMsg = regexList[checkName].error;}
						} else {//无正则匹配
							checkBack(obj);
							return true;
						}
					}
				}
				result ? checkPass(obj) : checkDefult(obj, errorMsg);
				return result;
			}

			function testRegex(checkName, obj){
				var result;
				var regexObj = regexList[checkName];
				if(!regexObj){//无对应正则 返回
					return false;
				} else {
					//重复查证密码
					var value = obj.val();
					if(checkName == 'rePassword'){
						result = (value == obj.closest('.'+form_classname).find('[needcheck=\'password\'][pwdlink=\''+obj.attr('pwdlink')+'\']').val());
					} else {
						var regex = regexObj.regex;
						result = regex.test(value);
					}
				}
				return result;
			}

			function checkSelect(obj){
				var value = obj.find('input[type=\'hidden\']').attr('value');
				if(value == ''){
					checkDefult(obj, '请完善信息');
					return false;
				} else {
					return true;
				}
			}

			function checkCheckbox(obj){
				var result = 0;
				obj.parent().children('input[type=\'checkbox\']').each(function(){
					var thisObj = $(this);
					if(thisObj.attr('checked') == 'checked'){
						++result;
					}
				});
				if(result > 0){
					return true;
				} else {
					checkDefult(obj, '请完善信息');
					return false;
				}
			}

			function checkPwd(obj){
				var checkName = obj.attr('needcheck');
				var msg = regexList[checkName].error;
				var inputList = obj.closest('.'+form_classname).find('[pwdlink=\''+obj.attr('pwdlink')+'\']'),
					pwd_one = inputList[0],
					pwd_two = inputList[1];
				if(pwd_one != pwd_two){
					checkDefult(pwd_two, msg);
				}
			}

			function checkPass(obj){
				obj.removeClass(input_hover+' '+input_right+' '+input_error).addClass(input_right);
			}

			function checkDefult(obj, msg){
				obj.removeClass(input_hover+' '+input_right+' '+input_error).addClass(input_error);
				var parentObj = obj.parents('.'+fieldset_classname);
				parentObj.addClass(fieldset_error_classname).find('.'+error_classname).html(msg);
			}

			function checkBack(obj){
				obj.removeClass(input_hover+' '+input_right+' '+input_error);
			}

		}
	})(window.jquery);