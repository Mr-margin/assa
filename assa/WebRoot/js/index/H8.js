var app={};
var all_pkid;
$(document).ready(function() {
	document.getElementById('metTable_bxbxxb').scrollIntoView();
	if(jsondata.company_map.com_type=="单位"){
		var type = jsondata.company_map.com_type;
		var val = jsondata.company;
		var qixian;
		if(val.com_level == "1"){
			getinfo_xiqian($('#cha_qx'));
		}else if(val.com_level == "2"){
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			getinfo_xiang(jsondata.company.xian,$('#cha_smx'));
			chaxun.cha_qx = jsondata.company.xian;
		}else if(val.com_level == "3"){
			$('#cha_qx').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			getinfo_cun(jsondata.company.xiang,$('#cha_gcc'));
			chaxun.cha_qx = jsondata.company.xian;
			chaxun.cha_smx = jsondata.company.xiang;
		}else if(val.com_level == "4"){
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
			chaxun.cha_qx = jsondata.company.xian;
			chaxun.cha_smx = jsondata.company.xiang;
			chaxun.cha_gcc = jsondata.company.cun;
		}
	}else if(jsondata.company_map.com_type=="管理员"){
		getinfo_xiqian($('#cha_qx'));
	}else{
		$("#chauxn_quanxian").hide();
	}
	
});
var metTable_bxbxxb = $('#metTable_bxbxxb');
$(function () {

	gachacun_initialization();
});

//贫困户初始化
function gachacun_initialization(){
	metTable_bxbxxb.bootstrapTable({
		method: 'POST',
		height: "597",
		url: "/assa/getRecordController.do",
		dataType: "json",//从服务器返回的数据类型	
		striped: true,	 //使表格带有条纹
		pagination: true,	//在表格底部显示分页工具栏
		pageSize: 10,	//页面大小
		pageNumber: 1,	//页数
		pageList: [10, 20, 50, 100],
		showToggle: true,   //名片格式
		showColumns: true, //显示隐藏列  
		toolbar: "#exampleToolbar_st",
		iconSize: "outline",
        icons: {
            refresh: "glyphicon-repeat",
            toggle: "glyphicon-list-alt",
            columns: "glyphicon-list"
        },
		showRefresh: true,  //显示刷新按钮
		singleSelect: true,//复选框只能选择一条记录
		search: true,//是否显示右上角的搜索框
		clickToSelect: true,//点击行即可选中单选/复选框
		sidePagination: "server",//表格分页的位置 client||server
		queryParams: queryParams_bxbxxb, //参数
		queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
		silent: true,  //刷新事件必须设置
		contentType: "application/x-www-form-urlencoded",	//请求远程数据的内容类型。
		onLoadError: function (data) {
			metTable_bxbxxb.bootstrapTable('removeAll');
			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		},
	});
}


function queryParams_bxbxxb(params) {  //配置参数
	var temp = {};
	temp.pageSize = params.limit;
	temp.pageNumber = params.offset;
	temp.search = params.search;
	return temp;
}
