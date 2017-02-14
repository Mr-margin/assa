
$(document).ready(function(){
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式	
})
$(function () {
	//$("#show-content").height(document.body.scrollHeight-170);//当中间部分内容过少时，人为增加高度，设置为一屏
	$("#anniu_1").html('<button type="button" class="btn btn-primary btn-xs" onclick="guobiao1()" style="font-size: 14px;">国家级贫困人口</button>'+
							'<div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" '+
							'class="btn btn-outline btn-primary btn-xs" onclick="shibiao_1()" style="font-size: 14px;">市级低收入人口</button>'+
							'');//市级或国家级贫困户选择按钮显示
//	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式	
	$("#anniu_3").html('<button type="button" class="btn btn-primary btn-xs" onclick="tuxing()" style="font-size: 14px;">图形</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-outline btn-primary btn-xs" onclick="biaoge()" style="font-size: 14px;">表格</button>');//表格或图形切换按钮显示
	if(jsondata==null){//未登录
		com_level="1";
		com_name="鄂尔多斯市";
	}else{//登录成功
		//判断层级
		com_level=jsondata.company.com_level;//用户层级
		if(com_level=="1"){//市
			code="shi";
			com_name=jsondata.company.com_name;//名称
			bg_bt="旗县";
			bg_zj="全市";
		}else if(com_level=="2"){//旗县
			code=jsondata.company.com_code;
			com_name=jsondata.company.com_name;//名称
			bg_bt="苏木乡镇";
			bg_zj="全旗";
		}else if(com_level=="3"){//乡镇
			code=jsondata.company.com_code;
			com_name=jsondata.company.com_name;//名称
			bg_bt="嘎查村";
			bg_zj="全乡";
		}else if(com_level=="4"){//村
			code=jsondata.company.xiang_code
			com_name=jsondata.company.xiang;//名称
			bg_bt="嘎查村";
			bg_zj="全乡";
		}
	}
	show_jiatingguimo();//默认显示家庭人口规模
	$("#mapChart").show();//显示图形
	$("#tableChart").hide();//隐藏表格
	$("#anniu_2 .iCheck-helper").click(function(){
		qhxs();
	});
	$("#anniu_2 label").click(function(){
		qhxs();
	});
	
});

var zi_id="";
var H3_2_tblx="jtgm";//图表类型
var H3_2_tblx_2="鄂尔多斯市";//图标子类型
var H3_2_bzlx="国家级贫困人口";//标准类型
var code;//行政编码
var com_level;//层级
var com_name;//名称
var shujv;//数据 这是统一往后台传的参数
var bg_bt="";//表格的表头
var bg_zj="";//表格里的总计
var mokuai_name;
var option = {  //饼状图对象
		title : {	//标题
			text:'', //com_name+'-贫困人口年人均收入',
			x:'left'	// 水平安放位置，默认为左对齐
		},
		tooltip : {	//提示框组件相关的行为，必须引入提示框组件后才能使用。
			trigger: 'item',	//数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
			formatter: ""//"{a} <br/>{b} : {c}人 ({d}%)"	//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
		},
		series : [
		          {
		        	  name: '',
		        	  type: 'pie',
		        	  radius : '55%',
		        	  center: ['50%', '60%'],
		        	  itemStyle:{ 
		        		  normal:{ 
		        			  label:{ 
		        				  show: true, 
		        				  formatter: ''//'{b} : {c}人 ({d}%)' 
		        			  }, 
		        			  labelLine :{show:true} 
		        		  } 
		        	  } 
		          }
		          ]
	};
var option_map = {
					title: {//标题
						text : '',//com_name+'-'+shujv+'人均年收入人数'
					},
					tooltip : {//提示框组件相关的行为，必须引入提示框组件后才能使用。
						trigger: 'item'//数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
					},
					dataRange: {//感觉这个是和visualMap一样，对范围进行确定。\
						show:true,//是否显示范围条
						left: 'right',
						color:["#F40000","#F53D00","#F57B00","#F4B800","#FFFFBE"],
						text:['高','低'],// 文本，默认为数值文本
						calculable : true//是否启用值域漫游，即是否有拖拽用的手柄，以及用手柄调整选中范围。
					},
					series : [{
						name: '',// shujv+'人数',//这里是鼠标放在上面，出现的提示。
						type: 'map',
						scaleLimit:{max:2, min:1},//滚轮缩放的极限控制，通过min, max最小和最大的缩放值，默认的缩放为1。
						mapType:'', //map_name,
						selectedMode : 'single',//图例选择的模式，默认开启图例选择，可以设成 false 关闭。除此之外也可以设成 'single' 或者 'multiple' 使用单选或者多选模式。
						itemStyle:{//地图区域的多边形 图形样式，有 normal 和 emphasis 两个状态，normal 是图形正常的样式，emphasis 是图形高亮的样式，比如鼠标悬浮或者图例联动高亮的时候会使用 emphasis 作为图形的样式。
							normal:{label:{show:true}},
							emphasis:{label:{show:true}}
						},
						itemStyle: {
							normal: {
								label: {
									show: true,
									textStyle: {color: "black"},//地图上的字体
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
var option_tiao = {
	    title: {
	        text: '',
	        x:'left'
	    },
	    legend: {
	        data:['健康','疾病'],
	        x:'right'
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer : {// 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'// 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    xAxis: {
	    	 axisLabel:{//坐标轴文本标签选项
                   interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
                 rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
                 margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
	           },
	        data:''
	    },
	    yAxis: {
	    	
	    },
	    series: [
	        {
	        name: '健康',
	        type: 'bar',
	        data: ''
	    },
	    {
	        name: '疾病',
	        type: 'bar',
	        data: '',
	    }
	    ]
	};
//点击 国标 按钮
function guobiao1(){
	
	$("#anniu_1").html('<button type="button" class="btn btn-primary btn-xs" onclick="guobiao1()" style="font-size: 14px;">国家级贫困人口</button>'+
						'<div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" '+
						'class="btn btn-outline btn-primary btn-xs" onclick="shibiao_1()" style="font-size: 14px;">市级低收入人口</button>');
//	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式	
	H3_2_bzlx="国家级贫困人口";
	qhxs();//仍然显示之前的主条件，子条件所要显示的东西
//	$("#anniu_1 input[name='v1'][value='"+year+"']").prop("checked","true");
//	$("#anniu_1 input[name='v1'][value='"+year+"']").parent(".iradio_square-green").addClass("checked");
}

//点击 市标 按钮
function shibiao_1(){
	$("#anniu_1").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 14px;" onclick="guobiao1()">国家级贫困人口</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-primary btn-xs" style="font-size: 14px;" onclick="shibiao1()">市级低收入人口</button>');
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式	
	H3_2_bzlx="市级低收入人口";
	qhxs();//仍然显示之前的主条件，子条件所要显示的东西
//	$("#anniu_1 input[name='v1'][value='"+year+"']").prop("checked","true");
//	$("#anniu_1 input[name='v1'][value='"+year+"']").parent(".iradio_square-green").addClass("checked");
}

//点击图形按钮
function tuxing(){
	$("#anniu_3").html('<button type="button" class="btn btn-primary btn-xs" onclick="tuxing()" style="font-size: 14px;">图形</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-outline btn-primary btn-xs" onclick="biaoge()" style="font-size: 14px;">表格</button>');
	$("#mapChart").show();//显示图形
	$("#tableChart").hide();//隐藏表格
	qhxs();//仍然显示之前的主条件，子条件所要显示的东西
}

//点击表格按钮
function biaoge(){
	$("#anniu_3").html('<button type="button" class="btn btn-outline btn-primary btn-xs" onclick="tuxing()" style="font-size: 14px;">图形</button><div style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div><button type="button" class="btn btn-primary btn-xs" onclick="biaoge()" style="font-size: 14px;">表格</button>');
	$("#mapChart").hide();//隐藏图形
	$("#tableChart").show();//显示表格
	qhxs();//仍然显示之前的主条件，子条件所要显示的东西
}

//点击国标，市标，图形，表格后，仍然显示之前的主条件，子条件所要显示的东西
function qhxs(){
	if(H3_2_tblx=="zpyy"){//致贫原因
		zhipinyuanyin(H3_2_tblx_2);
	}else if(H3_2_tblx=="nlfz"){//年龄分组
		show_nianlingjiegou(H3_2_tblx_2,zi_id);
	}else if(H3_2_tblx=="whcd"){//文化程度
		wenhuachengdu(H3_2_tblx_2);
	}else if(H3_2_tblx=="jkzk"){//健康状况
		jiankangzhuangk();
	}else if(H3_2_tblx=="jtgm"){//家庭规模
		renkouguimo(H3_2_tblx_2);
	}else if(H3_2_tblx=="lsbl"){//落实帮扶措施_主
		bangfubili(H3_2_tblx_2,zi_id);
	}else if(H3_2_tblx=="bffzr"){//帮扶责任人
		bangfuzerenren()
	}else if(H3_2_tblx=="jtsz"){//家庭收支_主
		jiatingshouzhi(H3_2_tblx_2,zi_id);
	}else if(H3_2_tblx=="zcgzd"){//驻村工作队
		zhucungzd();
	}
}
//树形导航栏
$("#treeview11").treeview({
	color: "#428bca",
	data: [{
		text: "<a id='jtgm_tree' onclick='show_jiatingguimo();'>家庭规模<a>",
	},
	{
		text: "<a id='jkzk_tree' onclick='show_jiankangzhuangkuang();'>健康状况<a>",
	},
	{
		text: "<a id='whcd_tree' onclick='show_wenhuachegndu();'>文化程度<a>",
	},
	{
		text: "<a id='nlfz_tree' onclick='show_nianlingfenuz();'>年龄结构<a>",
	},
	{
		text: "<a id='jtsz_tree' onclick='jiatingshouzhizu();'>家庭收支<a>",
	},
	],
})
$("#treeview22").treeview({
	color: "#428bca",
	data: [{
		text: "<a id='zpyy_tree' onclick='show_zhipinyuanyin();'>致贫原因<a>",
	},
	],
})
$("#treeview33").treeview({
	color: "#428bca",
	data: [{
		text: "<a id='lsbfbl_tree' onclick='showlsbfbl();'>落实帮扶比例<a>",
	},
	{
		text: "<a id='zcgzd_tree' onclick='showzcgzd();'>驻村工作队<a>",
	},
	{
		text: "<a id='bffzr_tree' onclick='showbffzr();'>帮扶责任人<a>",
	},
	],
})
//没有数据的暂时清空
function qingkong(){
	H3_2_tblx="";//清空图标类型
	H3_2_tblx_2="";//清空图标类型子类型
	$("#html_zicaidan").html('<h3>暂无数据</h3>');
	
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	myChart.showLoading();//此方法是显示加载动画效果
	$.getJSON('mapData/eerduosi/ordos.json', function (geoJson) {//获取已经定义好的json
	    myChart.hideLoading();//隐藏加载动画
	    echarts.registerMap('ordos', geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
	     option = {
				title: {//标题
					text : '暂无数据'
				},
				tooltip : {//提示框组件相关的行为，必须引入提示框组件后才能使用。
					trigger: 'item'//数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
				},
				series : [{
					name: '暂无数据',//这里是鼠标放在上面，出现的提示。
					type: 'map',
//					roam:true,//是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
					scaleLimit:{max:2, min:1},//滚轮缩放的极限控制，通过min, max最小和最大的缩放值，默认的缩放为1。
					mapType: 'ordos',
					selectedMode : 'single',//图例选择的模式，默认开启图例选择，可以设成 false 关闭。除此之外也可以设成 'single' 或者 'multiple' 使用单选或者多选模式。
					itemStyle:{//地图区域的多边形 图形样式，有 normal 和 emphasis 两个状态，normal 是图形正常的样式，emphasis 是图形高亮的样式，比如鼠标悬浮或者图例联动高亮的时候会使用 emphasis 作为图形的样式。
						normal:{label:{show:true}},
						emphasis:{label:{show:true}}
					},
					itemStyle: {
						normal: {
							label: {
								show: true,
								textStyle: {color: "black"},//地图上的字体
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
					data:[
					      {name : '杭锦旗', 		value : 0},
					      {name : '达拉特旗', 	value : 0},
					      {name : '东胜区', 	    value : 0},
					      {name : '乌审旗', 		value : 0},
				          {name : '准格尔旗', 	value : 0},
				          {name : '鄂托克前旗', 	value : 0},
				          {name : '鄂托克旗', 	value : 0},
				          {name : '伊金霍洛旗', 	value : 0},
					      {name : '康巴什新区', 	value : 0}
					      ]
				}]
        	};
	    myChart.setOption(option);
	});
}

//致贫原因
function show_zhipinyuanyin(){
	$("#html_zicaidan").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="因病" onclick="zhipinyuanyin(this.value)">因病致贫</button>&nbsp;&nbsp;&nbsp;&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="缺劳动力"  onclick="zhipinyuanyin(this.value)">缺劳动力</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="因学"  onclick="zhipinyuanyin(this.value)">因学致贫</button>'
			+'&nbsp;&nbsp;&nbsp;&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="缺资金"  onclick="zhipinyuanyin(this.value)">缺资金</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="因灾"  onclick="zhipinyuanyin(this.value)">因灾致贫</button>'
			+'&nbsp;&nbsp;&nbsp;&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"  value="缺技术" onclick="zhipinyuanyin(this.value)">缺技术</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="因残"  onclick="zhipinyuanyin(this.value)">因残致贫</button>'
			+'&nbsp;&nbsp;&nbsp;&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="缺土地"  onclick="zhipinyuanyin(this.value)">缺土地</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="交通条件落后"  onclick="zhipinyuanyin(this.value)">交通条件落后</button>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="缺水"  onclick="zhipinyuanyin(this.value)">缺水</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="自身发展动力不足" onclick="zhipinyuanyin(this.value)">自身发展动力不足</button>');
	H3_2_tblx="zpyy";//赋值图标类型为 致贫原因
	H3_2_tblx_2="";//清空图标类型子类型
	shujv="zpyy";
	zhipinyuanyin(shujv);
}

//年龄分组
function show_nianlingfenuz(){
	if(com_level=="1"){
		$("#html_zicaidan").html('<h5>按地区：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="康巴什新区" onclick="show_nianlingjiegou(this.value)">康巴什新区</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="准格尔旗"onclick="show_nianlingjiegou(this.value)">准格尔旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克前旗" onclick="show_nianlingjiegou(this.value)">鄂托克前旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克旗" onclick="show_nianlingjiegou(this.value)">鄂托克旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="伊金霍洛旗" onclick="show_nianlingjiegou(this.value)">伊金霍洛旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="达拉特旗" onclick="show_nianlingjiegou(this.value)">达拉特旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="杭锦旗" onclick="show_nianlingjiegou(this.value)">杭锦旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="乌审旗" onclick="show_nianlingjiegou(this.value)">乌审旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="东胜区" onclick="show_nianlingjiegou(this.value)">东胜区</button>'
				+'<div>&nbsp;</div>'
				+'<h5>按年龄段：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="10岁以下" onclick="show_nianlingjiegou(this.value,this.id)">10岁以下</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="10~20岁" onclick="show_nianlingjiegou(this.value,this.id)">10~20岁</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="20~30岁" onclick="show_nianlingjiegou(this.value,this.id)">20~30岁</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="30~40岁" onclick="show_nianlingjiegou(this.value,this.id)">30~40岁</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="40~50岁" onclick="show_nianlingjiegou(this.value,this.id)">40~50岁</button>'
				+'&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="50岁及以上" onclick="show_nianlingjiegou(this.value,this.id)">50~60岁</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="60岁及以上" onclick="show_nianlingjiegou(this.value,this.id)">60岁以上</button>');

	}else{
		$("#html_zicaidan").html(
				'<h5>按年龄段：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="10岁以下" onclick="show_nianlingjiegou(this.value,this.id)">10岁以下</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="10~20岁" onclick="show_nianlingjiegou(this.value,this.id)">10~20岁</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="20~30岁" onclick="show_nianlingjiegou(this.value,this.id)">20~30岁</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="30~40岁" onclick="show_nianlingjiegou(this.value,this.id)">30~40岁</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="40~50岁" onclick="show_nianlingjiegou(this.value,this.id)">40~50岁</button>'
				+'&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="50岁及以上" onclick="show_nianlingjiegou(this.value,this.id)">50~60岁</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="60岁及以上" onclick="show_nianlingjiegou(this.value,this.id)">60岁以上</button>');
	}
	H3_2_tblx="nlfz";//赋值图标类型为 家庭规模
	H3_2_tblx_2="";//清空图标类型子类型
	shujv="level-1";//层级
	zi_id="";//子类模块的id
	show_nianlingjiegou(shujv,zi_id);
}

//文化程度
function show_wenhuachegndu(){
	$("#html_zicaidan").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="" onclick="wenhuachengdu(this.value)">未知</button>'
			+'&nbsp;&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="学龄前儿童" onclick="wenhuachengdu(this.value)">学龄前儿童</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="小学" onclick="wenhuachengdu(this.value)">小学</button>'
			+'&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="初中" onclick="wenhuachengdu(this.value)">初中</button>'
			+'&nbsp;'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="高中" onclick="wenhuachengdu(this.value)">高中</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="大专" onclick="wenhuachengdu(this.value)">大专及以上</button>'
			+'<div>&nbsp;</div>'
			+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;"value="文盲" onclick="wenhuachengdu(this.value)">文盲或半文盲</button>');
	H3_2_tblx="whcd";//赋值图标类型为 文化程度
	H3_2_tblx_2="";//清空图标类型子类型
	shujv="whcd";
	wenhuachengdu(shujv);
}

//健康状况
function show_jiankangzhuangkuang(){
	$("#html_zicaidan").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" onclick="show_jiankangzhuangkuang()">健康状况</button>');
	H3_2_tblx="jkzk";//赋值图标类型为 健康状况
	H3_2_tblx_2="";//清空图标类型子类型
	jiankangzhuangk();
}

//家庭规模
function show_jiatingguimo(){
	if(com_level=="1"){
		$("#html_zicaidan").html('<h5>按地区：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="康巴什新区" onclick="renkouguimo(this.value)">康巴什新区</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="准格尔旗"onclick="renkouguimo(this.value)">准格尔旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克前旗" onclick="renkouguimo(this.value)">鄂托克前旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克旗" onclick="renkouguimo(this.value)">鄂托克旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="伊金霍洛旗" onclick="renkouguimo(this.value)">伊金霍洛旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="达拉特旗" onclick="renkouguimo(this.value)">达拉特旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="杭锦旗" onclick="renkouguimo(this.value)">杭锦旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="乌审旗" onclick="renkouguimo(this.value)">乌审旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="东胜区" onclick="renkouguimo(this.value)">东胜区</button>'
				+'<div>&nbsp;</div>'
				+'<h5>按每户人口：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="3" onclick="renkouguimo(this.value)">3人</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="4" onclick="renkouguimo(this.value)">4人</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="5" onclick="renkouguimo(this.value)">5人</button>');
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="准格尔旗"onclick="renkouguimo(this.value)">准格尔旗</button>'
//		+'&nbsp;&nbsp;'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="达拉特旗" onclick="renkouguimo(this.value)">达拉特旗</button>'
//		+'<div>&nbsp;</div>'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克前旗" onclick="renkouguimo(this.value)">鄂托克前旗</button>'
//		+'&nbsp;&nbsp;'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克旗" onclick="renkouguimo(this.value)">鄂托克旗</button>'
//		+'<div>&nbsp;</div>'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="伊金霍洛旗" onclick="renkouguimo(this.value)">伊金霍洛旗</button>'
//		+'&nbsp;&nbsp;'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="东胜区" onclick="renkouguimo(this.value)">东胜区</button>'
//		+'<div>&nbsp;</div>'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="杭锦旗" onclick="renkouguimo(this.value)">杭锦旗</button>'
//		+'&nbsp;&nbsp;'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="乌审旗" onclick="renkouguimo(this.value)">乌审旗</button>'
//		+'<div>&nbsp;</div>'
//		+'<h5>按每户人口：</h5>'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="3" onclick="renkouguimo(this.value)">3人</button>'
//		+'&nbsp;&nbsp;'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="4" onclick="renkouguimo(this.value)">4人</button>'
//		+'&nbsp;&nbsp;'
//		+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="5" onclick="renkouguimo(this.value)">5人</button>');
	}else{
		$("#html_zicaidan").html(
				'<h5>按每户人口：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="3" onclick="renkouguimo(this.value)">3人</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="4" onclick="renkouguimo(this.value)">4人</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="5" onclick="renkouguimo(this.value)">5人</button>');
	}
	H3_2_tblx="jtgm";//赋值图标类型为 家庭规模
	H3_2_tblx_2="";//清空图标类型子类型
	shujv="level-1";
	renkouguimo(shujv);
}

//落实帮扶责任人
function show_luoshibangfuzerenren(){
	H3_2_tblx="lsbfzrr";
	H3_2_tblx_2="";
}

//制定帮扶计划
function show_zhidingbangfujihua(){
	H3_2_tblx="zdbfjh";
	H3_2_tblx_2="";
}

//落实帮扶措施
function show_luoshibangfucuoshi(){
	H3_2_tblx="lsbfcs";
	H3_2_tblx_2="";
}

//帮扶责任人
function showbffzr(){
	$("#html_zicaidan").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" onclick="showbffzr()">帮扶责任人</button>');
	H3_2_tblx="bffzr";//赋值图标类型为 帮扶责任人
	H3_2_tblx_2="";//清空图标类型子类型
	bangfuzerenren();
}

//驻村工作队
function showzcgzd(){
	$("#html_zicaidan").html('<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" onclick="showzcgzd()">驻村工作队</button>');
	H3_2_tblx="zcgzd";//赋值图标类型为 帮扶责任人
	H3_2_tblx_2="";//清空图标类型子类型
	zhucungzd();
}

//加载体现致贫原因图
function zhipinyuanyin(shujv){
	
	var mapdatajson;//定义地图JSON
	var map_name;//定义地图名称
	mokuai_name="zpyy"
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	H3_2_tblx_2=shujv;
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	var datas;
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	if(shujv=="zpyy"){
		map_url="/assa/H3_pie_All.do";//按照级别来划分url，来判断是病状还是直线
	}else{
		map_url="/assa/H3_map_All.do";//按照级别来划分url，来判断是病状还是地图，1是地图。
	}
	$.ajax({
		url: map_url,
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			shujv:shujv,
			leixing:H3_2_bzlx,
			mokuai_name : mokuai_name,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			if(shujv=="zpyy"){
				tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">致贫原因</th><th style="width: 40%" class="text-center">人数</th>';//拼接表格标题
				$.each(data, function(i,item) { //循环添加表格数据
					tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
					zongji=parseInt(item.value)+parseInt(zongji);//计算总计
				});
				tables+='<tr><td class="text-center">总计</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
				tables+='<tbody></tbody></table>';
				$("#tableChart").html(tables); 
				option.title.text=com_name+'-致贫原因构成情况';//标题名称
				option.series[0].name=''+shujv+'';//系列名称
				option.series[0].data=data;//数据赋值，从数据库取出的数据赋值
				option.tooltip.formatter="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
				option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
				myChart.setOption(option);

			}else{
				if(data=="0"){
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">人数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'致贫-暂无数据';
						option_map.series[0].mapType=map_name;
						option_map.series[0].data = data;
						myChart.setOption(option_map);
					});
				}else{
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">人数</th>';//拼接表格标题
					$.each(data, function(i,item) { //循环添加表中数据
						tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji); //计算总计
					});
					tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'致贫';//标题名称
						option_map.series[0].name=shujv+'致贫人数';
						option_map.series[0].data=data;
						option_map.series[0].mapType=map_name;
						var min = 0,max = 0;	//计算地图中下标最大值最小值
						$.each(data, function(i,item) {
			 	    		if(item.value>max){
			 	    			max = item.value;
			 	    		}
			 	    		if(item.value<min){
			 	    			min = item.value;
			 	    		}
			 	    	});
			 	    	option_map.dataRange.max = max;
			 	    	option_map.dataRange.min = min;
						myChart.setOption(option_map);
					});
				}
				
			}
		},
		error: function () { 
		} 
	})
}

//加载年龄结构
function show_nianlingjiegou(shujv,id){
	H3_2_tblx="nlfz";
	H3_2_tblx_2=shujv;
	zi_id=id;
	var mapdatajson;//定义地图JSON
	var map_name;//定义地图名称
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	mokuai_name="nljg";
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	if(id!="1"){
		map_url="/assa/H3_pie_All.do";//按照级别来划分url，来判断是病状还是直线
	}else{
		map_url="/assa/H3_map_All.do";//按照级别来划分url，来判断是病状还是地图，1是地图。
	}
	$.ajax({
		url:map_url,
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			shujv:shujv,
			leixing:H3_2_bzlx,
			mokuai_name : mokuai_name,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			if (id!="1") {
				if(data[0]==null){//没有数据
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">人数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(com_name!="level-1"){
						shujv=com_name;
					}
					option.title.text=''+com_name+'-贫困人口年龄结构---暂无数据'; //标题名称
					option.series[0].name=''+shujv+''; //系列名称
					option.series[0].data=[{value:0, name:'0-10岁'},{value:0, name:'10-20岁'},{value:0, name:'20-30岁'},{value:0, name:'30-40岁'},{value:0, name:'40-50岁'},{value:0, name:'50岁以上'},];//饼状图数据赋值
					option.tooltip.formatte="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);
				}else{//有数据
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">年龄分组</th><th style="width: 40%" class="text-center">人数</th>';//表格标题
					$.each(data, function(i,item) { //循环添加表格内容
						tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji); //计算总计
					});
					tables+='<tr><td class="text-center">总计</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv=="level-1"){
						shujv=com_name;
					}
					option.title.text=shujv+'-贫困人口年龄结构';//标题名称
					option.series[0].name=shujv;//系列名称
					option.series[0].data=data;//数据赋值，从数据库取出的数据赋值
					option.tooltip.formatte="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);
				}
			} else {
				if(data=="0"){//如果没有数据
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+地区+'</th><th style="width: 40%" class="text-center">人数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'人数-暂无数据';
						option_map.series[0].mapType=map_name;
						option_map.series[0].data = data;
						myChart.setOption(option_map);
					});
				}else{//有数据的情况
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">人数</th>';
					$.each(data, function(i,item) {
						tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji);
					});
					tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'人数';//标题名称
						option_map.series[0].name=shujv+'人数';
						option_map.series[0].data=data;
						option_map.series[0].mapType=map_name;
						var min = 0,max = 0;
			 	    	$.each(data, function(i,item) {
			 	    		if(item.value>max){
			 	    			max = item.value;
			 	    		}
			 	    		if(item.value<min){
			 	    			min = item.value;
			 	    		}
			 	    	});
			 	    	option_map.dataRange.max = max;
			 	    	option_map.dataRange.min = min;
						myChart.setOption(option_map);
					});
				}
			}
		},
		error: function () {
		} 
	})
}

//加载健康状况图
function jiankangzhuangk(){
	H3_2_tblx_2=shujv;
	var tables="";//对表格赋值的变量
	var zongji=0;//健康
	var zongji_2=0;//疾病
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	$.ajax({
		url:"/assa/getPicture_Line.do",
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			leixing:H3_2_bzlx,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			var count=[];
			var count2=[];
			var count3=[];
			tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 50%" class="text-center">'+bg_bt+'</th><th style="width:25%" class="text-center">健康人数</th><th style="width:25%" class="text-center">疾病人数</th>';
			if(H3_2_bzlx=="国家级贫困人口"){
				$.each(data,function(i,indx){
					if(indx.jk=="0"&&indx.jb=="0"){
						
					}else{
						tables+='<tr><td class="text-center">'+indx.name+'</td><td class="text-center"><span class="c_green">'+indx.jk+'</span></td><td class="text-center"><span class="c_green">'+indx.jb+'</span></td></tr>';
					}
					count3[i]=indx.name;
					count[i]=indx.jk;
					count2[i]=indx.jb;
					
					zongji=parseInt(indx.jk)+parseInt(zongji);
					zongji_2=parseInt(indx.jb)+parseInt(zongji_2);
				});
				tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td><td class="text-center" ><span class="c_green">'+zongji_2+'</span></td></tr>';
				tables+='<tbody></tbody></table>';
				$("#tableChart").html(tables);
			}else{
				$.each(data,function(i,indx){
					if(indx.jk=="0"&&indx.jb=="0"){
						
					}else{
						tables+='<tr><td class="text-center">'+indx.name+'</td><td class="text-center"><span class="c_green">'+indx.jk+'</span></td><td class="text-center"><span class="c_green">'+indx.jb+'</span></td></tr>';	
					}
					count3[i]=indx.name;
					count[i] =indx.jk;
					count2[i] =indx.jb;
					
					zongji=parseInt(indx.jk)+parseInt(zongji);
					zongji_2=parseInt(indx.jb)+parseInt(zongji_2);
				});
				tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td><td class="text-center" ><span class="c_green">'+zongji_2+'</span></td></tr>';
				tables+='<tbody></tbody></table>';
				$("#tableChart").html(tables);
			}
			option_tiao.title.text=com_name+'-贫困人口健康状况';
			option_tiao.xAxis.data=count3;
			option_tiao.series[0].data=count;
			option_tiao.series[1].data=count2;
			myChart.setOption(option_tiao);
		},
		error: function () { 
		} 
	})
}

//家庭规模
function renkouguimo(shujv){
	H3_2_tblx_2=shujv;
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	mokuai_name="jtgm"
	var mapdatajson;//定义地图JSON
	var map_name;//定义地图名称
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	if(shujv=="3"||shujv=="4"||shujv=="5"){
		map_url="/assa/H3_map_All.do";//按照级别来划分url，来判断是病状还是地图，1是地图。
	}else{
		map_url="/assa/H3_pie_All.do";//按照级别来划分url，来判断是病状还是直线
	}
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	$.ajax({
		url: map_url,//"/assa/H3_2echarts_5.do",
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			shujv:shujv,
			leixing:H3_2_bzlx,
			mokuai_name : mokuai_name,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			if(shujv=="3"||shujv=="4"||shujv=="5"){
				tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">户数</th>';
				$.each(data, function(i,item) {
					tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
					zongji=parseInt(item.value)+parseInt(zongji);
				});
				tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
				tables+='<tbody></tbody></table>';
				$("#tableChart").html(tables);
				myChart.showLoading();//此方法是显示加载动画效果
				$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
					myChart.hideLoading();//隐藏加载动画
					echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
					option_map.title.text=com_name+'-'+shujv+'人家庭数';//标题名称
					option_map.series[0].name=shujv+'人数';
					option_map.series[0].data=data;
					option_map.series[0].mapType=map_name;
					var min = 0,max = 0;
		 	    	$.each(data, function(i,item) {
		 	    		if(item.value>max){
		 	    			max = item.value;
		 	    		}
		 	    		if(item.value<min){
		 	    			min = item.value;
		 	    		}
		 	    	});
		 	    	option_map.dataRange.max = max;
		 	    	option_map.dataRange.min = min;
					myChart.setOption(option_map);		
				});
			}else{
				if(data=="0"){
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">户数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					option.title.text=shujv+'-贫困户的家庭规模-暂无数据'; //标题名称
					option.series[0].name=shujv+'贫困户的家庭规模'; //系列名称
					option.series[0].data=data;//饼状图数据赋值
					option.tooltip.formatte="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);		
				}else{
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">家庭人口数</th><th style="width: 40%" class="text-center">户数</th>';
					$.each(data, function(i,item) {
						tables+='<tr><td class="text-center">'+item.name+' 人</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji);
					});
					tables+='<tr><td class="text-center">总计</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv=="level-1"){
						shujv=com_name;
					}
					option.title.text=shujv+'-贫困户的家庭规模';//标题名称
					option.series[0].name=shujv+'-贫困户的家庭规模';//系列名称
					option.series[0].data=data;//数据赋值，从数据库取出的数据赋值
					option.tooltip.formatte="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);
				}
			}
		},
		error: function () { 
		} 
	})
	
}

//文化程度 
function wenhuachengdu(shujv){
	H3_2_tblx_2=shujv;
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	var mapdatajson;//定义地图JSON
	var map_name;//定义地图名称
	mokuai_name="whcd";
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	if(shujv=="whcd"){
		map_url="/assa/H3_pie_All.do";//按照级别来划分url，来判断是病状还是直线
	}else{
		map_url="/assa/H3_map_All.do";//按照级别来划分url，来判断是病状还是地图，1是地图。
	}
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	$.ajax({
		url : map_url,
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			shujv:shujv,
			leixing:H3_2_bzlx,
			mokuai_name : mokuai_name,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			if(shujv=="whcd"){
				tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">文化程度</th><th style="width: 40%" class="text-center">人数</th>';
				$.each(data, function(i,item) {
					tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
					zongji=parseInt(item.value)+parseInt(zongji);
				});
				tables+='<tr><td class="text-center">总计</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
				tables+='<tbody></tbody></table>';
				$("#tableChart").html(tables);
				option.title.text=''+com_name+'- 贫困人口文化程度组成';//标题名称
				option.series[0].name=''+shujv+'';//系列名称
				option.series[0].data=data;//数据赋值，从数据库取出的数据赋值
				option.tooltip.formatter="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
				option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';
				myChart.setOption(option);
			}else{//子条件下
				if(data=="0"){//没有数据
					//对表格进行赋值
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">数据</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+"-"+shujv+"-暂无数据";
						option_map.series[0].mapType=map_name;
						option_map.series[0].data = data;
						myChart.setOption(option_map);
					});
				}else{//有数据
					//对表格进行赋值	
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">户数</th>';
					$.each(data, function(i,item) {
						if(item.value=="0"){
						}else{
							tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
							zongji=parseInt(item.value)+parseInt(zongji);
						}
					});
					tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv==""){
						shujv="未知"
					}
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'人均年收入人数';//标题名称
						option_map.series[0].name=shujv+'人数';
						option_map.series[0].data=data;
						option_map.series[0].mapType=map_name;
						var min = 0,max = 0;
			 	    	$.each(data, function(i,item) {
			 	    		if(item.value>max){
			 	    			max = item.value;
			 	    		}
			 	    		if(item.value<min){
			 	    			min = item.value;
			 	    		}
			 	    	});
			 	    	option_map.dataRange.max = max;
			 	    	option_map.dataRange.min = min;
						myChart.setOption(option_map);
					});
				}
			}
		},
		error: function () { 
		} 
	})
}

//帮扶责任人
function bangfuzerenren(){
	H3_2_tblx_2="";
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	var mapdatajson;//定义地图JSON
	mokuai_name="bffzr"
	var map_name;//定义地图名称
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	var data = JSON.parse(ajax_async_t("/assa/H3_map_All.do", {shujv:shujv,leixing:H3_2_bzlx,mokuai_name : mokuai_name,year:$('#anniu_2 input[name="v1"]:checked ').val()})); //调用ajax通用方法
	if(data=="0"){//没有数据
		//对表格进行赋值
		tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">数据</th>';
		tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
		tables+='<tbody></tbody></table>';
		$("#tableChart").html(tables);
		myChart.showLoading();//此方法是显示加载动画效果
		$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
			myChart.hideLoading();//隐藏加载动画
			echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
			option_map.title.text=com_name+"-帮扶责任人-暂无数据";
			option_map.series[0].mapType=map_name;
			option_map.series[0].data = data;
			myChart.setOption(option_map);
		});
	}else{//有数据
		//对表格进行赋值	
		tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">人数</th>';
		$.each(data, function(i,item) {
			if(item.value=="0"){
			}else{
				tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
				zongji=parseInt(item.value)+parseInt(zongji);
			}
		});
		tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
		tables+='<tbody></tbody></table>';
		$("#tableChart").html(tables);
		
		myChart.showLoading();//此方法是显示加载动画效果
		$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
			myChart.hideLoading();//隐藏加载动画
			echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
			option_map.title.text= com_name+'-帮扶责任人-人数';//标题名称
			option_map.series[0].name='帮扶责任人-人数';
			option_map.series[0].data=data;
			option_map.series[0].mapType=map_name;
			var min = 0,max = 0;
 	    	$.each(data, function(i,item) {
 	    		if(item.value>max){
 	    			max = item.value;
 	    		}
 	    		if(item.value<min){
 	    			min = item.value;
 	    		}
 	    	});
 	    	option_map.dataRange.max = max;
 	    	option_map.dataRange.min = min;
			myChart.setOption(option_map);
		});
	}
}

//驻村工作队
function zhucungzd(){
	H3_2_tblx_2="";
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	var mapdatajson;//定义地图JSON
	mokuai_name="zcgzd";
	var map_name;//定义地图名称
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	var data = JSON.parse(ajax_async_t("/assa/H3_map_All.do", {shujv:shujv,leixing:H3_2_bzlx,mokuai_name : mokuai_name,year:$('#anniu_2 input[name="v1"]:checked ').val()})); //调用ajax通用方法
	if(data=="0"){//没有数据
		//对表格进行赋值
		tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">数据</th>';
		tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
		tables+='<tbody></tbody></table>';
		$("#tableChart").html(tables);
		myChart.showLoading();//此方法是显示加载动画效果
		$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
			myChart.hideLoading();//隐藏加载动画
			echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
			option_map.title.text=com_name+"-驻村工作队-暂无数据";
			option_map.series[0].name='驻村工作队数';
			option_map.series[0].mapType=map_name;
			option_map.series[0].data = data;
			myChart.setOption(option_map);
		});
	}else{//有数据
		//对表格进行赋值	
		tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">有驻村工作队的村数</th>';
		$.each(data, function(i,item) {
			if(item.value=="0"){
			}else{
				tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
				zongji=parseInt(item.value)+parseInt(zongji);
			}
		});
		tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
		tables+='<tbody></tbody></table>';
		$("#tableChart").html(tables);
		
		myChart.showLoading();//此方法是显示加载动画效果
		$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
			myChart.hideLoading();//隐藏加载动画
			echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
			option_map.title.text=com_name+'-有驻村工作队的村数'//标题名称
			option_map.series[0].name='有驻村工作队的村数';
			option_map.series[0].data=data;
			option_map.series[0].mapType=map_name;
			var min = 0,max = 0;
 	    	$.each(data, function(i,item) {
 	    		if(item.value>max){
 	    			max = item.value;
 	    		}
 	    		if(item.value<min){
 	    			min = item.value;
 	    		}
 	    	});
 	    	option_map.dataRange.max = max;
 	    	option_map.dataRange.min = min;
			myChart.setOption(option_map);
		});
	}
}

//落实帮扶比例
function showlsbfbl(){
	if(com_level=="1"){
		$("#html_zicaidan").html('<h5>按地区：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="康巴什新区" onclick="bangfubili(this.value)">康巴什新区</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="准格尔旗"onclick="bangfubili(this.value)">准格尔旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克前旗" onclick="bangfubili(this.value)">鄂托克前旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克旗" onclick="bangfubili(this.value)">鄂托克旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="伊金霍洛旗" onclick="bangfubili(this.value)">伊金霍洛旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="达拉特旗" onclick="bangfubili(this.value)">达拉特旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="杭锦旗" onclick="bangfubili(this.value)">杭锦旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="乌审旗" onclick="bangfubili(this.value)">乌审旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="东胜区" onclick="bangfubili(this.value)">东胜区</button>'
				+'<div>&nbsp;</div>'
				+'<h5>帮扶落实比例：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="落实完成" onclick="bangfubili(this.value,this.id)">落实完成</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="落实未完成" onclick="bangfubili(this.value,this.id)">落实未完成</button>');
	}else{
		$("#html_zicaidan").html(
				'<h5>帮扶落实比例：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="落实完成" onclick="bangfubili(this.value,this.id)">落实完成</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="落实未完成" onclick="bangfubili(this.value,this.id)">落实未完成</button>');	
	}

	H3_2_tblx="lsbl";//赋值图标类型为 家庭规模
	H3_2_tblx_2="";//清空图标类型子类型
	shujv="level-1";
	zi_id="";
	bangfubili(shujv,zi_id);
}

//家庭收支
function jiatingshouzhizu(){
	if(com_level=="1"){
		$("#html_zicaidan").html('<h5>按地区：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="康巴什新区" onclick="jiatingshouzhi(this.value)">康巴什新区</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="准格尔旗"onclick="jiatingshouzhi(this.value)">准格尔旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克前旗" onclick="jiatingshouzhi(this.value)">鄂托克前旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="鄂托克旗" onclick="jiatingshouzhi(this.value)">鄂托克旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="伊金霍洛旗" onclick="jiatingshouzhi(this.value)">伊金霍洛旗</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="达拉特旗" onclick="jiatingshouzhi(this.value)">达拉特旗</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="杭锦旗" onclick="jiatingshouzhi(this.value)">杭锦旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="乌审旗" onclick="jiatingshouzhi(this.value)">乌审旗</button>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" value="东胜区" onclick="jiatingshouzhi(this.value)">东胜区</button>'
				+'<div>&nbsp;</div>'
				+'<h5>人年均纯收入：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="0-1000" onclick="jiatingshouzhi(this.value,this.id)">0~1000</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="1000-2000" onclick="jiatingshouzhi(this.value,this.id)">1000~2000</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="2000-3000" onclick="jiatingshouzhi(this.value,this.id)">2000~3000</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="3000-4000" onclick="jiatingshouzhi(this.value,this.id)">3000~4000</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="4000-5000" onclick="jiatingshouzhi(this.value,this.id)">4000~5000</button>'
				+'&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="5000及以上" onclick="jiatingshouzhi(this.value,this.id)">5000及以上</button>');
	}else{
		$("#html_zicaidan").html(
				'<h5>人年均纯收入：</h5>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="0-1000" onclick="jiatingshouzhi(this.value,this.id)">0~1000</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="1000-2000" onclick="jiatingshouzhi(this.value,this.id)">1000~2000</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="2000-3000" onclick="jiatingshouzhi(this.value,this.id)">2000~3000</button>'
				+'&nbsp;&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="3000-4000" onclick="jiatingshouzhi(this.value,this.id)">3000~4000</button>'
				+'<div>&nbsp;</div>'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="4000-5000" onclick="jiatingshouzhi(this.value,this.id)">4000~5000</button>'
				+'&nbsp;'
				+'<button type="button" class="btn btn-outline btn-primary btn-xs" style="font-size: 13px;" id="1" value="5000及以上" onclick="jiatingshouzhi(this.value,this.id)">5000及以上</button>');
	}
	H3_2_tblx="jtsz";//赋值图标类型为 家庭规模
	H3_2_tblx_2="";//清空图标类型子类型
	shujv="level-1";//层级
	zi_id="";//子类模块的id
	jiatingshouzhi(shujv,zi_id);
}

//家庭收支-主条件
function jiatingshouzhi(shujv,id){
	H3_2_tblx_2=shujv;
	zi_id=id;
	mokuai_name="jiatingshouzhi";
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	var mapdatajson;//定义地图JSON
	var map_name;//定义地图名称
	var map_url;
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	if(id!="1"){
		map_url="/assa/H3_pie_All.do";//按照级别来划分url，来判断是病状还是直线
	}else{
		map_url="/assa/H3_map_All.do";//按照级别来划分url，来判断是病状还是地图，1是地图。
	}
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	$.ajax({
		url:map_url,
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			shujv:shujv,
			leixing:H3_2_bzlx,
			mokuai_name:mokuai_name,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			if (id!="1") {
				if(data=="0"){
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">人数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv=="level-1"){
						shujv=com_name;
					}
					option.title.text=''+shujv+'-贫困人口人均收入---暂无数据'; //标题名称
					option.series[0].name=''+shujv+''; //系列名称
					option.series[0].data=[{value:0, name:'00-1000'},{value:0, name:'1000-2000'},{value:0, name:'2000-3000'},{value:0, name:'3000-4000'},{value:0, name:'4000-5000'},{value:0, name:'5000以上'},];//饼状图数据赋值
					option.tooltip.formatter="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);
				}else{	
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">收入分组</th><th style="width: 40%" class="text-center">人数</th>';
					$.each(data, function(i,item) {
						tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji);
					});
					tables+='<tr><td class="text-center">总计</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv=="level-1"){
						shujv=com_name;
					}
					option.title.text=shujv+'-贫困人口年人均收入';//标题名称
					option.series[0].name=shujv;//系列名称
					option.series[0].data=data;//数据赋值，从数据库取出的数据赋值
					option.tooltip.formatter="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';
					myChart.setOption(option);
				}
			}else{
				if(data=="0"){//如果没有数据
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">人数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'人均年收入人数-暂无数据';
						option_map.series[0].mapType=map_name;
						option_map.series[0].data = data;
						myChart.setOption(option_map);
					});
				}else{//有数据的情况
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">人数</th>';
					$.each(data, function(i,item) {
						tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji);
					});
					tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					myChart.showLoading();//此方法是显示加载动画效果
					$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
						myChart.hideLoading();//隐藏加载动画
						echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
						option_map.title.text=com_name+'-'+shujv+'人均年收入人数';//标题名称
						option_map.series[0].name=shujv+'人数';
						option_map.series[0].data=data;
						option_map.series[0].mapType=map_name;
						var min = 0,max = 0;
			 	    	$.each(data, function(i,item) {
			 	    		if(item.value>max){
			 	    			max = item.value;
			 	    		}
			 	    		if(item.value<min){
			 	    			min = item.value;
			 	    		}
			 	    	});
			 	    	option_map.dataRange.max = max;
			 	    	option_map.dataRange.min = min;
						myChart.setOption(option_map);
					});
				}
			}
		},
		error: function () { 
		} 
	})
}


//落实帮扶比例
function bangfubili(shujv,id){
	H3_2_tblx_2=shujv;
	zi_id=id;
	mokuai_name="lsbfcs";
	var tables="";//对表格赋值的变量
	var zongji=0;//表格总计
	var mapdatajson;//定义地图JSON
	var map_name;//定义地图名称
	if(com_level=="1"){//如果层级为1，那么加载鄂尔多斯市地图
		mapdatajson='mapData/eerduosi/ordos.json';//地图JSON
		map_name='ordos';//地图名称
	}else{//层级不为1，那么动态加载地图
		mapdatajson='mapData/eerduosi/'+code+'.json';//地图JSON
		map_name=code;//地图名称
	}
	if(id!="1"){
		map_url="/assa/H3_pie_All.do";//按照级别来划分url，来判断是病状还是直线
	}else{
		map_url="/assa/H3_map_All.do";//按照级别来划分url，来判断是病状还是地图，1是地图。
	}
	var myChart = echarts.init(document.getElementById('mapChart'));//声明id为mapChart的div为图形dom
	$.ajax({
		//url:"/assa/H3_2echarts_bfcsbl.do",
		url : map_url,
		type: "POST",
		async:false,
		dataType: "json",
		data:{
			shujv:shujv,
			leixing:H3_2_bzlx,
			mokuai_name : mokuai_name,
			year:$('#anniu_2 input[name="v1"]:checked ').val()
		},
		success: function (data) {
			if(id!="1"){
				if(data=="0"){
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">类型</th><th style="width: 40%" class="text-center">人数</th>';
					tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv=="level-1"){
						shujv=com_name;
					}
					option.title.text=''+shujv+'-落实帮扶比例---暂无数据'; //标题名称
					option.series[0].name=''+shujv+''; //系列名称
					option.series[0].data=[{value:0, name:'落实帮扶完成'},{value:0, name:'落实帮扶未完成'}];//饼状图数据赋值
					option.tooltip.formatte="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);		
				}else{	
					tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">落实帮扶</th><th style="width: 40%" class="text-center">人数</th>';
					$.each(data, function(i,item) {
						tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
						zongji=parseInt(item.value)+parseInt(zongji);
					});
					tables+='<tr><td class="text-center">总计</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
					tables+='<tbody></tbody></table>';
					$("#tableChart").html(tables);
					if(shujv=="level-1"){
						shujv=com_name;
					}
					option.title.text=shujv+'-落实帮扶比例';//标题名称
					option.series[0].name=shujv;//系列名称
					option.series[0].data=data;//数据赋值，从数据库取出的数据赋值
					option.tooltip.formatte="{a} <br/>{b} : {c}人 ({d}%)";//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					option.series[0].itemStyle.normal.label.formatter='{b} : {c}人 ({d}%)';//数据格式化方法，a（系列名称），b（数据项名称），c（数值）, d（百分比）。
					myChart.setOption(option);
					}
				}else{
					if(data=="0"){//如果没有数据
						tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+地区+'</th><th style="width: 40%" class="text-center">人数</th>';
						tables+='<tr><td class="text-center">暂无数据</td><td class="text-center" ><span class="c_green">暂无数据</span></td></tr>';
						tables+='<tbody></tbody></table>';
						$("#tableChart").html(tables);
						myChart.showLoading();//此方法是显示加载动画效果
						$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
							myChart.hideLoading();//隐藏加载动画
							echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
							option_map.title.text=com_name+'-'+shujv+'人数-暂无数据';
							option_map.series[0].mapType=map_name;
							option_map.series[0].data = data;
							myChart.setOption(option_map);
						});
					}else{//有数据的情况
						tables+='<table class="table table-hover margin bottom"><thead><tr><th style="width: 60%" class="text-center">'+bg_bt+'</th><th style="width: 40%" class="text-center">人数</th>';
						$.each(data, function(i,item) {
							tables+='<tr><td class="text-center">'+item.name+'</td><td class="text-center" ><span class="c_green">'+item.value+'</span></td></tr>';
							zongji=parseInt(item.value)+parseInt(zongji);
						});
						tables+='<tr><td class="text-center">'+bg_zj+'</td><td class="text-center" ><span class="c_green">'+zongji+'</span></td></tr>';
						tables+='<tbody></tbody></table>';
						$("#tableChart").html(tables);
						myChart.showLoading();//此方法是显示加载动画效果
						$.getJSON(mapdatajson, function (geoJson) {//获取已经定义好的json
							myChart.hideLoading();//隐藏加载动画
							echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
							echarts.registerMap(map_name, geoJson);//注册可用的地图，必须在包括 geo 组件或者 map 图表类型的时候才能使用
							option_map.title.text=com_name+'-'+shujv+'人数';//标题名称
							option_map.series[0].name=shujv+'人数';
							option_map.series[0].data=data;
							option_map.series[0].mapType=map_name;
							var min = 0,max = 0;
				 	    	$.each(data, function(i,item) {
				 	    		if(item.value>max){
				 	    			max = item.value;
				 	    		}
				 	    		if(item.value<min){
				 	    			min = item.value;
				 	    		}
				 	    	});
				 	    	option_map.dataRange.max = max;
				 	    	option_map.dataRange.min = min;
							myChart.setOption(option_map);
						});
					}
				}
		},
		error: function () { 
		} 
	})
}

