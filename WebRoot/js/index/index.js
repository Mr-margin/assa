$(function () {
	if(jsondata==null){//未登录
		com_level="1";
	}else{//登录成功
		com_level=jsondata.company.com_level;//用户层级
//		$("#gonggao").modal();
	}
	
	guobiao();//所有业务数据，默认国标

	//加载国标市标按钮
	$("#anniu_1").html('<button type="button" class="btn btn-primary btn-xs" onclick="guobiao1()" style="font-size: 14px;">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-outline btn-primary btn-xs" onclick="shibiao_1()" style="font-size: 14px;">市级低收入人口</button>');
	$("#anniu_3").html('<button type="button" class="btn btn-primary btn-xs" onclick="guobiao1()" style="font-size: 14px;">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-outline btn-primary btn-xs" onclick="shibiao_1()" style="font-size: 14px;">市级低收入人口</button><H5 style="display:inline; margin-left: 500px;">统计时间截止到：<code id="atime"></code></H5>');
	
	//validate实时验证
	$("#changepassword_form").validate({
		onfocusout: function(element){
			$(element).valid();
		}
	});
	//获取时间
	time();
	//点击登陆悬浮按钮，弹出登录框
	$("#tc").click(function(){
		$("#gray").show();
		$("#popup").show();//查找ID为popup的DIV show()显示#gray
		tc_center();
	});
//	var iTop = (window.screen.height-30-300)/2; //获得窗口的垂直位置;  
//	var iLeft = (window.screen.width-10-400)/2; //获得窗口的水平位置; 
//	window.open ("gonggao.html", "公告通知", "height=400,width=400,top="+iTop+",left="+iLeft+",menubar=no, scrollbars=no, resizable=no,location=no, status=no,titlebar=no"); 
});

var com_level;//用户层级
var code;//用户code
var gors;//国标或者市标
/**
 * 显示刷新时间
 */
function time(){
	var data = ajax_async_t("/assa/time_data.do", {}, "text");
	var time = data;
	$("#atime").html(time);
}

function guobiao1(){//控制点击 国标 按钮后的显隐
	$("#anniu_1").html('<button type="button" class="btn btn-primary btn-xs" onclick="guobiao1()" style="font-size: 14px;">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-outline btn-primary btn-xs" onclick="shibiao_1()" style="font-size: 14px;">市级低收入人口</button>');
	$("#anniu_3").html('<button type="button" class="btn btn-primary btn-xs" onclick="guobiao1()" style="font-size: 14px;">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-outline btn-primary btn-xs" onclick="shibiao_1()" style="font-size: 14px;">市级低收入人口</button><H5 style="display:inline; margin-left: 500px;">统计时间截止到：<code id="atime"></code></H5>');
	guobiao();
}
function shibiao_1(){//控制点击 市标 按钮后的显隐
	$("#anniu_1").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 14px;" onclick="guobiao1()">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-primary btn-xs" style="font-size: 14px;" onclick="shibiao1()">市级低收入人口</button>');
	$("#anniu_3").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 14px;" onclick="guobiao1()">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-primary btn-xs" style="font-size: 14px;" onclick="shibiao1()">市级低收入人口</button><H5 style="display:inline; margin-left: 500px;">统计时间截止到：<code id="atime"></code></H5>');
	shibiao();
}


function guobiao(){//点击国标
		//判断层级
		time();
		if(com_level=="1"){//市
			code="shi";
			pkid = 4;
		}else if(com_level=="2"){//旗县
			code=jsondata.company.com_code;
			pkid = jsondata.company.pkid;
		}else if(com_level=="3"){//乡镇
			code=jsondata.company.com_code;
			pkid = jsondata.company.pkid;
		}else if(com_level=="4"){//村
			code=jsondata.company.xiang_code
			pkid = jsondata.company.pkid;
		}
	
	shuaxin();
	gors="国家级贫困人口";
	zongbiao(code,gors,pkid);//总表
	index_map(code,gors);//加载工作首页地图
	xiafangbiaoge(code,gors,pkid);//加载下方表格
}
function shibiao(code){//点击市标
		time();
		//判断层级
		if(com_level=="1"){//市
			code="shi";
			pkid = 4;
		}else if(com_level=="2"){//旗县
			code=jsondata.company.com_code;
			pkid = jsondata.company.pkid;
		}else if(com_level=="3"){//乡镇
			code=jsondata.company.com_code;
			pkid = jsondata.company.pkid;
		}else if(com_level=="4"){//村
			code=jsondata.company.xiang_code
			pkid = jsondata.company.pkid;
		}
	
	shuaxin();//刷新
	gors="市级低收入人口";
	zongbiao(code,gors,pkid);//总表
	index_map(code,gors);//加载工作首页地图
	xiafangbiaoge(code,gors,pkid);//加载下方表格
}

//加载工作首页地图
function index_map(code,gors){
	if(code=="150622001000"||code=="150622002000"||code=="150622003000"||code=="150622004000"){
		$("#mapChart").html('<div style="padding-left: 150px;padding-top: 150px;"><h3>暂无 '+jsondata.company.com_name+'-'+gors+' 地图</h3></div>');
	}else{
		var mapdatajson;//定义地图JSON
		var map_name;//定义地图名称
		if(code=="shi"){//如果层级为1，那么加载鄂尔多斯市地图
			mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
			map_name='ordos';//地图名称
		}else{//层级不为1，那么动态加载地图
			mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
			map_name=code;//地图名称
		}

		var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
		myChart.showLoading();//此方法是显示加载动画效果
		$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
		    myChart.hideLoading();//隐藏加载动画
		    echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
		    $.ajax({
		    	url:"/assa/getindex_table11.do",
		 	    type: "POST",
		 	    async:false,
		 	    dataType: "json",
		 	    data:{
		 	    	gors:gors,
		 	    	code:code,
		 	    },
		 	    success: function (data) {
		 	    	var option = {
		 					title: {//标题
		 						text : ''
		 					},
		 					tooltip : {//提示框组件相关的行为，必须引入提示框组件后才能使用。
		 						trigger: 'item'//数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
		 					},
		 					dataRange: {//感觉这个是和visualMap一样，对范围进行确定。
		 						show:true,//是否显示范围条
		 						left: 'right',
		 				        min: 0,
		 				        max: 3100,
//		 				        color:["#BADB58"," #C9E47D","#D8EB9A","#E3F5B9","#F2FBDC"],
		 				        color:["#F40000","#F53D00","#F57B00","#F4B800","#FFFFBE"],
		 				        text:['高','低'],           // 文本，默认为数值文本
		 				        calculable : true//是否启用值域漫游，即是否有拖拽用的手柄，以及用手柄调整选中范围。
		 				    },
		 					series : [{
		 						name: '贫困户数(户)',//这里是鼠标放在上面，出现的提示。
		 						type: 'map',
		 						//roam:true,//是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
		 						scaleLimit:{max:2, min:1},//滚轮缩放的极限控制，通过min, max最小和最大的缩放值，默认的缩放为1。
		 						mapType: map_name,
		 						selectedMode : 'single',//图例选择的模式，默认开启图例选择，可以设成 false 关闭。除此之外也可以设成 'single' 或者 'multiple' 使用单选或者多选模式。
		 						itemStyle:{//地图区域的多边形 图形样式，有 normal 和 emphasis 两个状态，normal 是图形正常的样式，emphasis 是图形高亮的样式，比如鼠标悬浮或者图例联动高亮的时候会使用 emphasis 作为图形的样式。
		 							normal:{label:{show:true}},
		 							emphasis:{label:{show:true}}
		 						},
		 						itemStyle: {
		 							normal: {
		 								label: {
		 									show: true,
		 									textStyle: {color: "black"}//地图上的字体
		 								},
		 								areaStyle : "#E0ECF6",
		 								borderColor: "#E0ECF6",
		 								borderWidth: "2.0",
		 								color: "#6CAFED"
		 							},
		 							emphasis: {
		 								label: {
		 									show: true,
		 									textStyle: {color: "black"}
		 								},
		 								borderColor: "#fff",
		 								borderWidth: "1.0",
		 								color: "#BADB58"
		 							}
		 						},
		 						data:[]
		 					}]
		 	        	};
		 	    	option.series[0].data = data;
		 	    	var min = 0,max = 0;
		 	    	$.each(data, function(i,item) {
		 	    		if(item.value*1>max){
		 	    			max = item.value;
		 	    		}
		 	    		if(item.value<min){
		 	    			min = item.value;
		 	    		}
		 	    	});
		 	    	option.dataRange.max = max;
		 	    	option.dataRange.min = min;
		 		    myChart.setOption(option);
		 	    },
		 	    error: function () { 
		 	    	toastr["warning"]("warning", "失败，检查数据后重试");
		 	    } 
			})
		    
		});
	}
	
}

//首页总表
function zongbiao(code,gors,pkid){
	var aa;//name
	var dd;//户数
	var cc;//未脱贫人数
	var ycc;//已脱贫人数
	var last_dd;//去年户数
	var last_cc;//去年未脱贫人数
	var last_ycc;//去年已脱贫人数
	var hha;//统计名称
	var hhh=0;//总户数
	var hhr=0;//未脱贫总人数
	var yhhr=0;//已脱贫总人数
	var last_hhh=0;//去年总户数
	var last_hhr=0;//去年总人数
	var last_yhhr=0;//去年总人数
	if(com_level==1){//当为不同用户的时候，表头改变。
		$("#index_yi_title").html("旗县");
		hha="全市";
	}else if(com_level==2){
		$("#index_yi_title").html("苏木乡镇");
		hha="全旗";
	}else if(com_level==3){
		$("#index_yi_title").html("嘎查村");
		hha="全乡";
	}else if(com_level==4){
		$("#index_yi_title").html("嘎查村");
		hha="全乡";
	}
	$.ajax({  
	    url:"/assa/getindex_table1.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{
	    	gors:gors,
	    	code:code,
	    	pkid:pkid
        },
	    success: function (data) {
	    	if(data==0){
	    		$("#shangfangzongbiao").html('<h3>暂无数据</h3>');
	    	}else{
	    		var html="";
		    	$.each(data, function(i,item) {
		    		aa=item.v3;//name
		    		
		    		if(aa.indexOf("村村")>-1){//截取字符串
		    			if(aa.substring(aa.length-2,aa.length)=='委会'){
		    				aa = aa.substring(0,aa.length-3);
		    			}else if(aa.indexOf("村民委员会")>-1){
		    				aa = aa.substring(0,aa.length-5);
		    			}
		    		}else{
		    			if(aa.substring(aa.length-2,aa.length)=='委会'){
		    				aa = aa.substring(0,aa.length-2);
		    			}else if(aa.indexOf("村民委员会")>-1){
		    				aa = aa.substring(0,aa.length-4);
		    			}
		    		}
		    		if(item.count=="0" || item.count == undefined){//户数
		    			dd='<span class="c_red">0</span>';
		    		}else{
		    			dd='<span class="c_green">'+item.count+'</span>';
		    			hhh=parseInt(item.count)+parseInt(hhh);
		    		}
		    		//去年户数
//		    		alert(item.count1)
		    		if ( item.count1 == "0"  ){
		    			last_dd='<span class="c_red">0</span>';
		    		}else{
		    			last_dd='<span class="c_green">'+item.count1+'</span>';
		    			last_hhh=parseInt(item.count1)+parseInt(last_hhh);
		    		}
		    		if(item.sum=="0" || item.sum == undefined){//未脱贫人数
		    			cc='<span class="c_red">0</span>';
		    		}else{
		    			cc='<span class="c_green">'+item.sum+'</span>';
		    			hhr=parseInt(item.sum)+parseInt(hhr);
		    		}
		    		if(item.sum2=="0" || item.sum2 == undefined){//已脱贫人数
		    			ycc='<span class="c_red">0</span>';
		    		}else{
		    			ycc='<span class="c_green">'+item.sum2+'</span>';
		    			yhhr=parseInt(item.sum2)+parseInt(yhhr);
		    		}
		    		//去年人数
		    		if(item.sum1=="0"){//未脱贫人数
		    			last_cc='<span class="c_red">0</span>';
		    		}else{
		    			last_cc='<span class="c_green">'+item.sum1+'</span>';
		    			last_hhr=parseInt(item.sum1)+parseInt(last_hhr);
		    		}
		    		if(item.sum3=="0" || item.sum3 == undefined){//已脱贫人数
		    			last_ycc='<span class="c_red">0</span>';
		    		}else{
		    			last_ycc='<span class="c_green">'+item.sum3+'</span>';
		    			last_yhhr=parseInt(item.sum3)+parseInt(last_yhhr);
		    		}
		    		html+='<tr><td class="text-center">'+aa+'</td><td class="text-center" >'+dd+'</td><td class="text-center" >'+cc+'</td><td class="text-center" >'+ycc+'</td><td class="text-center" >'+last_dd+'</td><td class="text-center" >'+last_cc+'</td><td class="text-center" >'+last_ycc+'</td></tr>';
		    		
		    	});
		    	html+='<tr><td class="text-center">'+hha+'</td><td class="text-center" ><span class="c_green">'+hhh+'</span></td><td class="text-center" ><span class="c_green">'+hhr+'</span></td><td class="text-center" ><span class="c_green">'+yhhr+'</span></td><td class="text-center" ><span class="c_green">'+last_hhh+'</span></td><td class="text-center" ><span class="c_green">'+last_hhr+'</span></td><td class="text-center" ><span class="c_green">'+last_yhhr+'</span></td></tr>';
		    	$("#shangfangzongbiao").html(html);
	    	}
	    },
	    error: function () { 
//	    	alert("错误");
	    } 
	})
}
	    		

//刷新首页所有表格
function shuaxin(){
	$("span[class='c_green']").closest('td[class="text-center"]').html('<span class="c_red">0</span>');
}

//下方的表格
function xiafangbiaoge(code,gors,pkid){
	var aa;//名称
	var bb;//人数
	var cc;//户数
	var last_bb;//去年人数
	var last_cc;//去年户数
	var dd;//落实帮扶责任人
	var ee;//制定帮扶计划
	var ff;//帮扶措施
	var gg;
	var hha;//统计名称
	var hh1="0";//全市-户数
	var hh2="0";//全市-人数
	var last_hh1="0";//去年全市-户数
	var last_hh2="0";//去年全市-人数
	var hh3="0";//全市-落实帮扶责任人
	var hh4="0";//全市-制定帮扶计划
	var hh5="0";//全市-帮扶措施
	var hh6="0";
	
	if(com_level==1){//当为不同用户的时候，表头改变。
		$("#index_er_title").html("旗县");
		hha="全市";
	}else if(com_level==2){
		$("#index_er_title").html("苏木乡镇");
		hha="全旗";
	}else if(com_level==3){
		$("#index_er_title").html("嘎查村");
		hha="全乡";
	}else if(com_level==4){
		$("#index_er_title").html("嘎查村");
		hha="全乡";
	}
	$.ajax({  
	    url:"/assa/getindex_table2.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{
	    	gors:gors,
	    	code:code,
	    	pkid:pkid
        },
	    success: function (data) {
	    	if(data==0){
	    		$("#xiafangzongbiao").html('<h3>暂无数据</h3>');
	    	}else{
	    		var html="";
		    	$.each(data, function(i,item) {
		    		aa=item.b1;
		    		if(aa.indexOf("村村")>-1){
		    			if(aa.substring(aa.length-2,aa.length)=='委会'){
		    				aa = aa.substring(0,aa.length-3);
		    			}else if(aa.indexOf("村民委员会")>-1){
		    				aa = aa.substring(0,aa.length-5);
		    			}
		    		}else{
		    			if(aa.substring(aa.length-2,aa.length)=='委会'){
		    				aa = aa.substring(0,aa.length-2);
		    			}else if(aa.indexOf("村民委员会")>-1){
		    				aa = aa.substring(0,aa.length-4);
		    			}
		    		}
		    		//户数
		    		if(item.b2=="0"||item.b2==null){
		    			cc='<span class="c_red">0</span>';
		    		}else{
		    			cc='<span class="c_green">'+item.b2+'</span>';
		    			hh1=parseInt(item.b2)+parseInt(hh1);
		    		}
		    		//去年户数
		    		if(item.b22=="0"||item.b22==null){
		    			last_cc='<span class="c_red">0</span>';
		    		}else{
		    			last_cc='<span class="c_green">'+item.b22+'</span>';
		    			last_hh1=parseInt(item.b22)+parseInt(last_hh1);
		    		}
		    		//人数
		    		if(item.b3=="0"||item.b3==null){
		    			bb='<span class="c_red">0</span>';
		    		}else{
		    			bb='<span class="c_green">'+item.b3+'</span>';
		    			hh2=parseInt(item.b3)+parseInt(hh2);
		    		}
		    		//去年人数
		    		if(item.b33=="0"||item.b33==null){
		    			last_bb='<span class="c_red">0</span>';
		    		}else{
		    			last_bb='<span class="c_green">'+item.b33+'</span>';
		    			last_hh2=parseInt(item.b33)+parseInt(last_hh2);
		    		}
		    		//落实帮扶责任人
		    		if(item.b10=="0"||item.b10==null){
		    			dd='<span class="c_red">0</span>';
		    		}else{
		    			dd='<span class="c_green">'+item.b10+'</span>';
		    			hh3=parseInt(item.b10)+parseInt(hh3);
		    		}
		    		//制定帮扶计划
		    		if(item.b11=="0"||item.b11==null){
		    			ee='<span class="c_red">0</span>';
		    		}else{
		    			ee='<span class="c_green">'+item.b11+'</span>';
		    			hh4=parseInt(item.b11)+parseInt(hh4);
		    		}
		    		//帮扶措施
		    		if(item.b12=="0"||item.b12==null){
		    			ff='<span class="c_red">0</span>';
		    		}else{
		    			ff='<span class="c_green">'+item.b12+'</span>';
		    			hh5=parseInt(item.b12)+parseInt(hh5);
		    		}
		    		//走访记录
		    		if(item.b13=="0"||item.b13==null){
		    			gg='<span class="c_red">0</span>';
		    		}else{
		    			gg='<span class="c_green">'+item.b13+'</span>';
		    			hh6=parseInt(item.b13)+parseInt(hh6);
		    		}
		    		//循环赋值
		    		html+='<tr><td class="text-center">'+aa+'</td><td class="text-center">'+cc+'</td><td class="text-center">'+bb+'</td><td class="text-center">'+last_cc+'</td><td class="text-center">'+last_bb+'</td><td class="text-center">'+dd+'</td><td class="text-center">'+ee+'</td><td class="text-center">'+ff+'</td><td class="text-center">'+gg+'</td></tr>';
		    	});
		    	if(hh1=="0"||hh1==null){
	    			cc='<span class="c_red">0</span>';
	    		}else{
	    			cc='<span class="c_green">'+hh1+'</span>';
	    		}
		    	if(hh2=="0"||hh2==null){
	    			bb='<span class="c_red">0</span>';
	    		}else{
	    			bb='<span class="c_green">'+hh2+'</span>';
	    		}
		    	if(last_hh1=="0"||last_hh1==null){
	    			last_cc='<span class="c_red">0</span>';
	    		}else{
	    			last_cc='<span class="c_green">'+last_hh1+'</span>';
	    		}
		    	if(last_hh2=="0"||last_hh2==null){
	    			last_bb='<span class="c_red">0</span>';
	    		}else{
	    			last_bb='<span class="c_green">'+last_hh2+'</span>';
	    		}
		    	if(hh3=="0"||hh3==null){
	    			dd='<span class="c_red">0</span>';
	    		}else{
	    			dd='<span class="c_green">'+hh3+'</span>';
	    		}
		    	if(hh4=="0"||hh4==null){
	    			ee='<span class="c_red">0</span>';
	    		}else{
	    			ee='<span class="c_green">'+hh4+'</span>';
	    		}
		    	if(hh5=="0"||hh5==null){
	    			ff='<span class="c_red">0</span>';
	    		}else{
	    			ff='<span class="c_green">'+hh5+'</span>';
	    		}
		    	if(hh6=="0"||hh6==null){
	    			gg='<span class="c_red">0</span>';
	    		}else{
	    			gg='<span class="c_green">'+hh6+'</span>';
	    		}
	    		
	    		//下方的表格总计
	    		html+='<tr><td class="text-center">'+hha+'</td><td class="text-center">'+cc+'</td><td class="text-center">'+bb+'</td><td class="text-center">'+last_cc+'</td><td class="text-center">'+last_bb+'</td><td class="text-center">'+dd+'</td><td class="text-center">'+ee+'</td><td class="text-center">'+ff+'</td><td class="text-center">'+gg+'</td></tr>';
	    		//对下方的表格赋值
	    		$("#xiafangzongbiao").html(html);
	    	}
	    },
	    error: function () { 
//	    	alert("错误");
	    } 
	})
}
