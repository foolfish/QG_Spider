/**
 * fastw
 * @example  $("#test").ProvinceCity();
 * @params   暂无
 */
$.fn.ProvinceCity = function(){
	var _self = this;
	//定义3个默认值
	_self.data("province",["请选择", "请选择"]);
	_self.data("city1",["请选择", "请选择"]);
	//插入3个空的下拉框
	_self.append("<select id='shebao_province'></select>");
	_self.append("<select id='shebao_city'></select>");
	//分别获取3个下拉框
	var $sel1 = _self.find("select").eq(0);
	var $sel2 = _self.find("select").eq(1);
	//默认省级下拉
	if(_self.data("province")){
		$sel1.append("<option value='"+_self.data("province")[1]+"'>"+_self.data("province")[0]+"</option>");
	}
	$.each( GP , function(index,data){
		$sel1.append("<option value='"+data[0]+"'>"+data[1]+"</option>");
	});
	//默认的1级城市下拉
	if(_self.data("city1")){
		$sel2.append("<option value='"+_self.data("city1")[1]+"'>"+_self.data("city1")[0]+"</option>");
	}
	
	//省级联动 控制
	var index1 = "" ;
	$sel1.change(function(){
		//清空其它2个下拉框
		$sel2[0].options.length=0;
		index1 = this.selectedIndex;
		if(index1==0){	//当选择的为 “请选择” 时
			if(_self.data("city1")){
				$sel2.append("<option value='"+_self.data("city1")[1]+"'>"+_self.data("city1")[0]+"</option>");
			}
			
		}else{
			$.each( GT[index1-1] , function(index,data){
				$sel2.append("<option value='"+data[0]+"'>"+data[1]+"</option>");
				shebao_checkInitAuth();
			});
		}
	}).change();
	//1级城市联动 控制
	var index2 = "" ;
	$sel2.change(function(){
		shebao_checkInitAuth();
	});
	return _self;
};

/********** 省级数据 **********/
var GP =[['guangdong','广东省'],['shanghai','上海市']];
/********** 市级数据 **********/
var GT = [[['shenzhen','深圳']],[['shanghai','上海']]];
