
var metTable_g = $('#metTable_g');
var metTable_s = $('#metTable_s');
var metTable_b = $('#metTable_b');
$(function () {
	get_tj_xiqian();
	
	$("#qu_g").change(function () {
		if($("#qu_g").val()==4){
			$("#sum_g").html("<option>请选择</option>");
		}else{
			get_tj_xiang($("#qu_g").val(),$('#sum_g'));
		}
		
		metTable_g.bootstrapTable('destroy');//销毁现有表格数据
		guoding_initialization();//重新初始化数据
	});
	$("#sum_g").change(function () {
		metTable_g.bootstrapTable('destroy');//销毁现有表格数据
		guoding_initialization();//重新初始化数据
	});
	$("#year_g").change(function () {
		metTable_g.bootstrapTable('destroy');//销毁现有表格数据
		guoding_initialization();//重新初始化数据
	});
	$("#year_s").change(function () {
		metTable_s.bootstrapTable('destroy');//销毁现有表格数据
		shiding_initialization();//重新初始化数据
	});
	$("#qu_s").change(function () {
		if($("#qu_s").val()==4){
			$("#sum_s").html("<option>请选择</option>");
		}else{
			get_tj_xiang($("#qu_s").val(),$('#sum_s'));
		}
		metTable_s.bootstrapTable('destroy');//销毁现有表格数据
		shiding_initialization();//重新初始化数据
	});
	$("#sum_s").change(function () {
		metTable_s.bootstrapTable('destroy');//销毁现有表格数据
		shiding_initialization();//重新初始化数据
	});
	$("#year_bfr").change(function(){
		metTable_b.bootstrapTable('destroy');//销毁现有表格数据
		bangfuren_initialization();
	});
	time();
	guoding_initialization();
	shiding_initialization();
	bangfuren_initialization();
});


function get_tj_xiqian(){
	var qixian;
	$.ajax({
	    url: "/assa/getSaveQixianController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
        },
	    success: function (data) {
	    	qixian='<option value="4">鄂尔多斯市</option>';
	    	$.each(data,function(i,item){
	    		qixian+='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    	});
	    	$("#qu_g").html(qixian);
	    	$("#qu_s").html(qixian);
	    	//$("#qu_b").html(qixian);
	    },
	    error: function () { 
	    }  
	});
}
/**
 * 获取乡
 */
function get_tj_xiang(chongmingle,str){
	var xiang;
	$.ajax({
	    url: "/assa/getXiangController_id.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	str:chongmingle,
        },
	    success: function (data) {
	    	xiang='<option>请选择</option>';
	    	$.each(data,function(i,item){
	    		xiang+='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    	});
	    	str.html(xiang);
	    },
	    error: function () { 
	    }  
	});
}

/**
 * 显示刷新时间
 */
function time(year_g){
	var data = ajax_async_t("/assa/time_data.do", {year:year_g}, "text");
	if(data!="0"){
		var time = data;
		$(".time").html(time);
	}
}
/**
 * 校验别人是否在进行点击刷新操作
 */

function isOperation(){
	var data = ajax_async_t("/assa/isOperation.do", {}, "text");
	return data;
}

function H5_6sxanniu(){
	var data=isOperation();
	var year_g=$("#year_g").val();
	if(data=="1"){
		$('#shuaxin_time').hide();
		$('#exportExcel_all_dengdai').show();
		$('#shuaxin_time1').hide();
		$('#exportExcel_all_dengdai1').show();
		$.ajax({  		       
			url: "/assa/updata_xinbiao.do",
			type: "POST",
			async:true,
			dataType: "text",
			data:{year:year_g},
			success: function (data) {
				if(data==1){
					metTable_g.bootstrapTable('destroy');//销毁现有表格数据
					guoding_initialization();//重新初始化数据
					metTable_s.bootstrapTable('destroy');//销毁现有表格数据
					shiding_initialization();//重新初始化数据
					time(year_g);
					$('#shuaxin_time').show();
					$('#exportExcel_all_dengdai').hide();
					$('#shuaxin_time1').show();
					$('#exportExcel_all_dengdai1').hide();
				}else{
					
				}
			},
			error: function () { 
				toastr["error"]("error", "服务器异常");
			}  
		});
	}else if(data=="0"){
		toastr["info"]("", "刷新正在被操作，请稍后再点击刷新！");
	}

}

//国定标准
function guoding_initialization(){
	metTable_g.bootstrapTable({
		method: 'POST',
		url: "/assa/getCountry_Stat.do",
		dataType: "json",//从服务器返回的数据类型
		striped: true,	 //使表格带有条纹
		pagination: false,	//在表格底部显示分页工具栏
		showToggle: false,   //名片格式
		showColumns: false, //显示隐藏列  
		showRefresh: false,  //显示刷新按钮
		singleSelect: false,//复选框只能选择一条记录
		search: false,//是否显示右上角的搜索框
		clickToSelect: false,//点击行即可选中单选/复选框
		sidePagination: "server",//表格分页的位置 client||server
		queryParams: queryParams_g, //参数
		queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
		silent: true,  //刷新事件必须设置
		contentType: "application/x-www-form-urlencoded",	//请求远程数据的内容类型。
		onLoadError: function (data) {
			metTable_g.bootstrapTable('removeAll');
			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		}
	});
}

function queryParams_g(params) {  //配置参数
	var temp = {};
	temp.type = "国家级贫困人口";
	temp.sum_name=$("#sum_g option:selected").text();
    temp.qu_g = $("#qu_g").val();
    temp.sum_g = $("#sum_g").val();
    temp.order=params.order;
    temp.sort=params.sort;
    temp.year=$("#year_g").val();
    return temp;
}

//市定标准
function shiding_initialization(){
	metTable_s.bootstrapTable({
		method: 'POST',
		url: "/assa/getCountry_Stat.do",
		dataType: "json",//从服务器返回的数据类型
		striped: true,	 //使表格带有条纹
		pagination: false,	//在表格底部显示分页工具栏
		showToggle: false,   //名片格式
		showColumns: false, //显示隐藏列  
		showRefresh: false,  //显示刷新按钮
		singleSelect: false,//复选框只能选择一条记录
		search: false,//是否显示右上角的搜索框
		clickToSelect: false,//点击行即可选中单选/复选框
		sidePagination: "server",//表格分页的位置 client||server
		queryParams: queryParams_s, //参数
		queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
		silent: true,  //刷新事件必须设置
		contentType: "application/x-www-form-urlencoded",	//请求远程数据的内容类型。
		onLoadError: function (data) {
			metTable_s.bootstrapTable('removeAll');
			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		}
	});
}

function queryParams_s(params) {  //配置参数
	var temp = {};
	temp.type = "市级低收入人口";
	if($("#sum_g option:selected").text()!=null&&$("#sum_g option:selected").text()!="请选择"){
		temp.sum_name=$("#sum_g option:selected").text();
	}else{
		temp.sum_name=$("#sum_s option:selected").text();
	}

    temp.qu_g = $("#qu_s").val();
    temp.sum_g = $("#sum_s").val();
    temp.order=params.order;
    temp.sort=params.sort;
    temp.year=$("#year_s").val();
    return temp;
}

function bangfuren_initialization(){
	metTable_b.bootstrapTable({
		method: 'POST',
		url: "/assa/getBangFu_Stat.do",
		dataType: "json",//从服务器返回的数据类型
		striped: true,	 //使表格带有条纹
		pagination: false,	//在表格底部显示分页工具栏
		showToggle: false,   //名片格式
		showColumns: false, //显示隐藏列  
		showRefresh: false,  //显示刷新按钮
		singleSelect: false,//复选框只能选择一条记录
		search: false,//是否显示右上角的搜索框
		clickToSelect: false,//点击行即可选中单选/复选框
		sidePagination: "server",//表格分页的位置 client||server
		queryParams: queryParams_b, //参数
		queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
		silent: true,  //刷新事件必须设置
		contentType: "application/x-www-form-urlencoded",	//请求远程数据的内容类型。
		onLoadError: function (data) {
			metTable_b.bootstrapTable('removeAll');
			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		}
	});
}

function queryParams_b(params) {  //配置参数
	var temp = {};
	 temp.order=params.order;
	 temp.sort=params.sort;
	 temp.year=$("#year_bfr").val();
    return temp;
}
