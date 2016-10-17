$(function(){
	//初始化表格
	metTable_initialization();
	shoubangfucun();
	
	//validate实时验证
	$("#add_bfdw_form").validate({
		onfocusout: function(element){
			$(element).valid();
		}
	});  
	//为旗县下拉框赋值
	teshu_xiqian($('#cha_qixian'),$('#add_qixian'),$('#up_qixian'));
	//点击标题缩放查询框
	$('#chauxnshiousuo_title').click(function () {
		$('#chauxnshiousuo').click();
	});
	//点击查询按钮
	$("#cha_button").click(function(){
		chaxun.cha_qixian = $("#cha_qixian").val();
		chaxun.cha_bfdw=$("#cha_bfdw").val();
		chaxun.cha_ldxm=$("#cha_ldxm").val();
		chaxun.cha_lddh=$("#cha_lddh").val();
		chaxun.cha_sbfc=$("#cha_sbfc").val();
		$metTable.bootstrapTable('destroy');//销毁表格数据
		metTable_initialization();//初始化
	});
	//点击查询清空按钮
	$('#close_cha_button').click(function () {
		var val = jsondata.company;
		if(val.com_level == "1"){
			$("#cha_qixian").val("请选择");
		}
		$("#cha_bfdw").val("");
		$("#cha_ldxm").val("");
		$("#cha_lddh").val("");
		$("#cha_sbfc").val("");
		$('#chauxnshiousuo').click();
		$("#cha_button").click();
		$metTable.bootstrapTable('destroy');//销毁表格数据
		metTable_initialization();//初始化
	});
	//点击添加按钮
	$("#add_bfdw_button").click(function(){
		$("#add_bf").show();
		$("#up_bf").hide();
		//shoubangfucun();
		document.getElementById("add_bf").scrollIntoView();//定位到添加帮扶单位界面
	});
	//点击修改按钮
	$("#update_bfdw_button").click(function(){
		var row = getSelectedRow();//获取选中的行
    	if (typeof row != "undefined") {
    		$("#add_bf").hide();
    		$("#up_bf").show();
    		$("#up_bfdw_mc").val(row.v1),
    		$("#up_bfdw_ldxm").val(row.v3),
    		$("#up_bfdw_dz").val(row.v2),
    		$("#up_bfdw_lddh").val(row.v4)
    		
    		var val = jsondata.company;
    		if(val.com_level == "1"){
    			$("#up_qixian").val(row.sys_company_id);
    		}
    		if (typeof row.com_name != "undefined") {
    			$("#cha_gcc_up").html('<option  value="'+row.v5+'">'+row.com_name+'</option>');
    		}
    		
    		
    		xiugaiID=row.pkid;
    		//shoubangfucun();
    		document.getElementById("up_bf").scrollIntoView();//定位到添加帮扶单位界面
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
	});
	//点击删除按钮
	$("#delete_bfdw_button").click(function(){
		var row = getSelectedRow(metTable);//必须确认先选中一条
		if (typeof row != "undefined") {
			swal({
				title: "您确定要删除 "+row.v1+" 吗",
				text: "删除后将无法恢复，请谨慎操作！",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "删除",
				closeOnConfirm: false
			},
			function() {
				deleteBfdw(row.pkid);
			});
		}else{
			toastr["info"]("info", "必须选择一条记录");
		}
	});
	
});
var xiugaiID="";
function shoubangfucun(){
	
	//添加帮扶单位-受帮扶的嘎查村
	if(jsondata.company_map.com_type=="单位"){
		var type = jsondata.company_map.com_type;
		var val = jsondata.company;
		var qixian;
		if(val.com_level == "1"){
			getinfo_xiqian($('#cha_qx'));
			getinfo_xiqian($('#cha_qx_up'));
		}else if(val.com_level == "2"){
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$('#cha_qx_up').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			getinfo_xiang(jsondata.company.xian,$('#cha_smx'));
			getinfo_xiang(jsondata.company.xian,$('#cha_smx_up'));
		}else if(val.com_level == "3"){
			$('#cha_qx').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$('#cha_qx_up').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx_up").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			teshu_cun(jsondata.company.xiang,$('#cha_gcc'));
			teshu_cun(jsondata.company.xiang,$('#cha_gcc_up'));
		}else if(val.com_level == "4"){
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
			$('#cha_qx_up').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx_up").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc_up").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
		}
	}else if(jsondata.company_map.com_type=="管理员"){
		getinfo_xiqian($('#cha_qx'));
		getinfo_xiqian($('#cha_qx_up'));
	}else{
		$("#chauxn_quanxian").hide();
	}
}

//特殊处理的旗县加载下拉菜单
function teshu_xiqian(str,str1,str2){
	var qixian;
	var type = jsondata.company_map.com_type;
	var val = jsondata.company;
	$.ajax({
		url: "/assa/getSaveQixianController.do",
		type: "POST",
		async:false,
		dataType:"json",
		success: function (data) {
			qixian='<option>请选择</option>';
//			if(type=="单位"){
//				if(val.com_level == "1"){
//					qixian += '<option value="4">鄂尔多斯市</option>';
//				}
//			}else  if(type=="管理员"){
//				qixian += '<option value="4">鄂尔多斯市</option>';
//			}
			qixian += '<option value="4">鄂尔多斯市</option>';
			$.each(data,function(i,item){
				if(type=="单位"){
					if(val.com_level == "1"){
						qixian += '<option  value="'+item.pkid+'">'+item.com_name+'</option>';
					}else if(val.com_level == "2"){
						if(val.xian==item.com_name){
							qixian='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
							qixian += '<option value="4">鄂尔多斯市</option>';
						}
					}else if(val.com_level == "3"){
						if(val.xian==item.com_name){
							qixian='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
							qixian += '<option value="4">鄂尔多斯市</option>';
						}
					}else if(val.com_level == "4"){
						if(val.xian==item.com_name){
							qixian='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
							qixian += '<option value="4">鄂尔多斯市</option>';
						}
					}
				}else if(type=="管理员"){
					qixian += '<option  value="'+item.pkid+'">'+item.com_name+'</option>';
				}

			});
			$(str).html(qixian);
			$(str1).html(qixian);
			$(str2).html(qixian);
		},
		error: function () { 
		}  

	});
}
//特殊处理的村加载下拉菜单
function teshu_cun(chongmingle,str){
	var cun;
	var type = jsondata.company_map.com_type;
	var val = jsondata.company;
	
	$.ajax({
	    url: "/assa/getXiangController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	str:chongmingle,
        },
	    success: function (data) {
	    	cun='<option>请选择</option>';
	    	$.each(data,function(i,item){
	    		if(type=="单位"){
	    			if(val.com_level == "1"){
	    				cun+='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    			}else if(val.com_level == "2"){
	    				cun+='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    			}else if(val.com_level == "3"){
	    				cun+='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    			}else if(val.com_level == "4"){
	    				if(val.cun==item.com_name){
	    					cun='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    				}
	    			}
	    		}else if(type=="管理员"){
	    			cun+='<option value="'+item.pkid+'">'+item.com_name+'</option>';
	    		}
	    	});
	    	str.html(cun);
	    },
	    error: function () { 
	    }  
	});
}

//添加帮扶单位
function addBfdw(){
	$.ajax({
		url: "/assa/addBfdw.do",
		type: "POST",
		async:false,
		dataType:"json",
		data:{
			add_bfdw_mc:$("#add_bfdw_mc").val(),
			add_qixian:$("#add_qixian").val(),
			add_bfdw_ldxm:$("#add_bfdw_ldxm").val(),
			add_bfdw_dz:$("#add_bfdw_dz").val(),
			add_bfdw_lddh:$("#add_bfdw_lddh").val(),
			cha_gcc:$("#cha_gcc").val()
		},
		success: function (data) {
			if(data==1){
				toastr["success"]("success", "添加新帮扶单位成功");
				//销毁现有表格数据
				$metTable.bootstrapTable('destroy');
				//重新初始化数据
				metTable_initialization();
				$("#add_bf").hide();
				shuaxin_add();
			}else{
				toastr["warning"]("warning", "添加失败，检查数据后重试");
			}
		},
		error: function () { 
			toastr["error"]("error", "服务器异常");
		}  

	});
}
//修改帮扶单位
function upBfdw(){
	$.ajax({
		url: "/assa/upBfdw.do",
		type: "POST",
		async:false,
		dataType:"json",
		data:{
			pkid:xiugaiID,
			up_bfdw_mc:$("#up_bfdw_mc").val(),
			up_qixian:$("#up_qixian").val(),
			up_bfdw_ldxm:$("#up_bfdw_ldxm").val(),
			up_bfdw_dz:$("#up_bfdw_dz").val(),
			up_bfdw_lddh:$("#up_bfdw_lddh").val(),
			up_cha_gcc:$("#cha_gcc_up").val()
		},
		success: function (data) {
			if(data==1){
				toastr["success"]("success", "添加新帮扶单位成功");
				//销毁现有表格数据
				$metTable.bootstrapTable('destroy');
				//重新初始化数据
				metTable_initialization();
				$("#up_bf").hide();
				shuaxin();
				
			}else{
				toastr["warning"]("warning", "添加失败，检查数据后重试");
			}
		},
		error: function () { 
			toastr["error"]("error", "服务器异常");
		}  

	});
}

function quxiao_add(){
	$("#add_bf").hide();
	$("#up_bf").hide();
}

//刷新
function shuaxin_add(){
	$("#add_bfdw_mc").val("");//帮扶单位名称
	$("#add_bfdw_ldxm").val("");//分管领导姓名
	$("#add_bfdw_dz").val("");//单位地址
	$("#add_bfdw_lddh").val("");//分管领导电话
	var val = jsondata.company;
	if(val.com_level == "1"){
		$("#add_qixian").val("请选择");
		$("#cha_qx").val("请选择");
		$("#cha_smx").val("请选择");
		$("#cha_gcc").val("请选择");
	}else if(val.com_level == "2"){
		$("#cha_smx").val("请选择");
		$("#cha_gcc").val("请选择");
	}else if(val.com_level == "3"){
		$("#cha_gcc").val("请选择");
	}
}

function shuaxin(){
	$("#up_bfdw_mc").val("");
    $("#up_bfdw_ldxm").val("");
    $("#up_bfdw_dz").val("");
    $("#up_bfdw_lddh").val("");
	//shoubangfucun();
    var val = jsondata.company;
	if(val.com_level == "1"){
		$("#up_qixian").val("请选择");
		$("#cha_qx_up").val("请选择");
		$("#cha_smx_up").val("请选择");
		$("#cha_gcc_up").val("请选择");
	}else if(val.com_level == "2"){
		$("#cha_smx_up").val("请选择");
		$("#cha_gcc_up").val("请选择");
	}else if(val.com_level == "3"){
		$("#cha_gcc_up").val("请选择");
	}
	
	//teshu_xiqian($('#cha_qixian'),$('#add_qixian'),$('#up_qixian'));
}
//删除帮扶单位
function deleteBfdw(pkid){
	$.ajax({
		url: "/assa/getDeleteBfdw.do",
		type: "POST",
		async:false,
		dataType:"json",
		data:{
			pkid:pkid
		},
		success: function (data) {
			if(data==1){
				//销毁现有表格数据
				$metTable.bootstrapTable('destroy');
				//重新初始化数据
				metTable_initialization();
				toastr["success"]("success", "数据已删除");
				swal("删除成功！", "您已经永久删除了这条信息。", "success");
				$("#add_bf").hide();
				$("#up_bf").hide();
			}else{
				toastr["warning"]("warning", "删除失败，检查数据后重试");
			}
		},
		error: function () { 
			toastr["error"]("error", "服务器异常");
		}  

	});
}
var $metTable = $('#metTable');
var chaxun = {};
//帮扶单位表格数据初始化
function metTable_initialization(){
	$metTable.bootstrapTable({
		method: 'POST',
		height: "506",
		url: "/assa/get_bfdw_List.do",
		dataType: "json",//从服务器返回的数据类型
		striped: true,	 //使表格带有条纹
		pagination: true,	//在表格底部显示分页工具栏
		pageSize: 10,	//页面大小
		pageNumber: 1,	//页数
		pageList: [10, 20, 50, 100],
		//idField: "ProductId",  //标识哪个字段为id主键
		showToggle: true,   //名片格式
		//cardView: false,//设置为True时显示名片（card）布局
		showColumns: false, //显示隐藏列  
		toolbar: "#exampleToolbar_st",
		iconSize: "outline",
		icons: {
			refresh: "glyphicon-repeat",
			toggle: "glyphicon-list-alt",
			columns: "glyphicon-list"
		},
		showRefresh: true,  //显示刷新按钮
		singleSelect: true,//复选框只能选择一条记录
		search: false,//是否显示右上角的搜索框
		clickToSelect: true,//点击行即可选中单选/复选框
		sidePagination: "server",//表格分页的位置 client||server
		queryParams: queryParams, //参数
		queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
		//toolbar: "#toolbar", //设置工具栏的Id或者class
//		columns: column, //列
		silent: true,  //刷新事件必须设置
		contentType: "application/x-www-form-urlencoded",	//请求远程数据的内容类型。
		onLoadError: function (data) {
			$metTable.bootstrapTable('removeAll');
//			toastr["error"]("error", "服务器异常");
			toastr["warning"]("warning", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			var d_number = row.d_number;
			$('.success').removeClass('success');
			$($element).addClass('success');

		},
	});
}

//配置参数
function queryParams(params) {  
	var temp = {};
	temp.pageSize = params.limit;
	temp.pageNumber = params.offset;
//	temp.search = params.search;

	temp.cha_qixian = chaxun.cha_qixian;
	temp.cha_bfdw = chaxun.cha_bfdw;
	temp.cha_ldxm = chaxun.cha_ldxm;
	temp.cha_lddh = chaxun.cha_lddh;
	temp.cha_sbfc = chaxun.cha_sbfc;

	return temp;
}

//获取选中行数据
function getSelectedRow() {
	var index = $metTable.find('tr.success').data('index');
	return $metTable.bootstrapTable('getData')[index];
}
