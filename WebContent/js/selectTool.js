	/* 下拉框工具 pjk */
	function diy_select(){this.init.apply(this,arguments)};
    diy_select.prototype={
    	 init:function(opt){
    	 	this.setOpts(opt);
    	 	this.o=this.getByClass(this.opt.TTContainer,document,'div');//容器
    	 	this.b=this.getByClass(this.opt.TTDiy_select_btn);//按钮
    	 	this.t=this.getByClass(this.opt.TTDiy_select_txt);//显示
    	 	this.l=this.getByClass(this.opt.TTDiv_select_list);//列表容器
    	 	this.ipt=this.getByClass(this.opt.TTDiy_select_input);//列表容器
    	 	this.lengths=this.o.length;
    	 	this.showSelect();
    	 },
    	 addClass:function(o,s){//添加class
    	 	o.className = o.className ? o.className+' '+s:s;
    	 },
    	 removeClass:function(o,st){//删除class
    	 	var reg=new RegExp('\\b'+st+'\\b');
    	 	o.className=o.className ? o.className.replace(reg,''):'';
    	 },
    	 addEvent:function(o,t,fn){
    	 	return o.addEventListener ? o.addEventListener(t,fn,false):o.attachEvent('on'+t,fn);
    	 },
    	 showSelect:function(){
    	 	var This=this;
    	 	var iNow=0;
    	 	this.addEvent(document,'click',function(){
    	 		for(var i=0;i<This.lengths;i++){
    	 			This.removeClass(This.o[i], 'fc-input-hover');
    	 			This.l[i].style.display='none';
    	 		}
    	 	});

    	 	for(var i=0;i<this.lengths;i++){
    	 		this.l[i].index=this.b[i].index=this.t[i].index=i;
    	 		this.t[i].onclick=this.b[i].onclick=function(ev){
    	 			var e=window.event || ev;
    	 			var index=this.index;
    	 			var hover_className = 'fc-input-hover';
    	 			var parent = this.parentNode;
    	 			parent.className.indexOf(hover_className) == -1 ? This.addClass(parent, hover_className) : This.removeClass(parent, hover_className); 
    	 			This.item=This.l[index].getElementsByTagName('li');
	    	 		This.l[index].style.display= This.l[index].style.display=='block' ? 'none' :'block';
	    	 		for(var j=0;j<This.lengths;j++){
	    	 			if(j!=index){
	    	 				This.l[j].style.display='none';
	    	 			}
	    	 		}
	    	 		This.addClick(This.item);
	    	 		e.stopPropagation ? e.stopPropagation() : (e.cancelBubble=true); //阻止冒泡
    	 		}
    	 	}
    	 },
    	 addClick:function(o){
    	 	if(o.length>0){
    	 		var This=this;
    	 		for(var i=0;i<o.length;i++){
    	 			o[i].onmouseover=function(){
    	 				This.addClass(this,This.opt.TTFcous);
    	 			}
    	 			o[i].onmouseout=function(){
    	 				This.removeClass(this,This.opt.TTFcous);
    	 			}
    	 			o[i].onclick=function(){
    	 				var index=this.parentNode.index;//获得列表
    	 				This.t[index].innerHTML=this.innerHTML.replace(/^\s+/,'').replace(/\s+&/,'');
    	 				This.ipt[index].value=this.getAttribute('value');
    	 				This.l[index].style.display='none';
    	 				var hover_className = 'fc-input-hover';
    	 				This.removeClass(this.parentNode.parentNode, hover_className);
    	 			}
    	 		}
    	 	}
    	 },
    	 getByClass:function(s,p,t){
    	 	var reg=new RegExp('\\b'+s+'\\b');
    	 	var aResult=[];
    	 	var aElement=(p||document).getElementsByTagName(t || '*');

    	 	for(var i=0;i<aElement.length;i++){
    	 		if(reg.test(aElement[i].className)){
    	 			aResult.push(aElement[i])
    	 		}
    	 	}
    	 	return aResult;
    	 },

    	 setOpts:function(opt){ 
    	 	this.opt={
    	 		 TTContainer:'diy_select',//控件的class
    	 		 TTDiy_select_input:'diy_select_input',//用于提交表单的class
    	 		 TTDiy_select_txt:'diy_select_txt',//diy_select用于显示当前选中内容的容器class
    	 		 TTDiy_select_btn:'diy_select_btn',//diy_select的打开按钮
    	 		 TTDiv_select_list:'diy_select_list',//要显示的下拉框内容列表class
    	 		 TTFcous:'focus'//得到焦点时的class
    	 	}
    	 	for(var a in opt){
    	 		this.opt[a]=opt[a] ? opt[a]:this.opt[a];
    	 	}
    	 }
    }