/*
*@author C.Enzo
*@desc   javascript for popup
*/
$(function(){
	$("#serviceTap").bind("click", function(){
		$(".overlay").show();
		$(".popup").show();
	});
	$(".overlay").bind("click", function(){
		$(".overlay").hide();
		$(".popup").hide();
	});
	$("#agreeId").bind("click", function(){
		$(".overlay").hide();
		$(".popup").hide();
	});
	
	
});