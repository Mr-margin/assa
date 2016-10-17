var app={};
var duo=[];
var checked;
var zname;
$(document).ready(function() {
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式
	teshu_xiqian($('#cha_qixian'),$('#add_qixian'));
	chaxun.cha_qixian = jsondata.company.xian_id ;
	$('#exportExcel_all_dengdai').hide();
	$(".input-group.date").datepicker({
		todayBtn: "linked",
        keyboardNavigation: !1,
        forceParse: !1,
        calendarWeeks: !0,
        autoclose: !0
    });
	
});
var metTable_bxbxxb = $('#metTable_bxbxxb');
var chaxun = {};//存储表格查询参数

//特殊处理的旗县加载下拉菜单
function teshu_xiqian(str,str1){
	var qixian;
	
	var type = jsondata.company_map.com_type;
	var val = jsondata.company;
	$.ajax({
	    url: "/assa/getSaveQixianController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
        },
	    success: function (data) {
	    	qixian='<option>请选择</option>';
//	    	if(type=="单位"){
//	    		if(val.com_level == "1"){
//	    			qixian += '<option value="4">鄂尔多斯市</option>';
//	    		}
//	    	}else  if(type=="管理员"){
//	    		qixian += '<option value="4">鄂尔多斯市</option>';
//	    	}
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
	    },
	    error: function () { 
	    }  
	
	});
}

$(function () {
    //添加帮扶人
    $("#add_bf_button").click(function(){
    	$("#add_Form").find("input").each(function(){
    		var id = $(this).attr("id");
  		  	$("#"+id).val("");
	    });
    	$("#v1").val("请选择");
    	$("#add_qixian").val("请选择");
    	$("#v3").val("请选择");
    	getbangfuren("请选择");
    	
    	$("#add_bf").show();
    	$("#add_bf #xia_title").html("添加帮扶人");
    	document.getElementById("add_bf").scrollIntoView();
    });
    
    
    //修改帮扶人
    $("#update_bf_button").click(function(){
    	var row = getSelectedRow(metTable_bxbxxb);//必须确认先选中一条白细胞数据
    	if (typeof row != "undefined") {
    		updateBfxinxi(row.pkid);
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
    });
    
  //根据条件导出报表
    $('#export_button').click(function(){
    	
    	var cha_bfdw = $("#cha_bfdw").val();
    	var cha_bfr = $("#cha_bfr").val();
    	var cha_qixian = $("#cha_qixian").val();
    	var cha_dh = $("#cha_dh").val();
    	var cha_juese = $("#cha_juese").val();
    	var cha_v3 = $("#cha_v3").val();
    	$.ajax({  		       
    	    url: "/assa/exportTerm.do",
    	    type: "POST",
    	    dataType: "json",
    	    data:{
    	    	cha_bfdw : cha_bfdw,
    	    	cha_bfr : cha_bfr,
    	    	cha_qixian : cha_qixian,
    	    	cha_dh : cha_dh,
    	    	cha_juese : cha_juese,
    	    	cha_v3 : cha_v3
            },
            async:true,
    	    dataType: "json",
    	    beforeSend: function(){
    	    	$('#export_button').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#export_button').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.location.href=data.path;
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
           
    	});
    });

    //删除帮扶人
    $("#delete_bf_button").click(function(){
    	var row = getSelectedRow(metTable_bxbxxb);//必须确认先选中一条白细胞数据
    	if (typeof row != "undefined") {
    		swal({
	            title: "您确定要删除这条信息吗",
	            text: "删除后将无法恢复，请谨慎操作！",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "删除",
	            closeOnConfirm: false
	        },
	        function() {
	        	deleteBfr(row.pkid);
	        });
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
    });
    
	//查询按钮
    $('#cha_button').click(function () {
    	chaxun.cha_bfdw = $("#cha_bfdw").val();
    	chaxun.cha_bfr = $("#cha_bfr").val();
    	chaxun.cha_qixian = $("#cha_qixian").val();
    	chaxun.cha_dh = $("#cha_dh").val();
    	chaxun.cha_juese = $("#cha_juese").val();
    	chaxun.cha_v3 = $("#cha_v3").val();
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	pooruser_initialization();//重新初始化数据
    });
    
    //清空查询
    $('#close_cha_button').click(function () {
    	$("#cha_bfdw").val("");
    	$("#cha_bfr").val("");
    	var val = jsondata.company;
		if(val.com_level == "1"){
			$("#cha_qixian").val("请选择");
		}
    	$("#cha_juese").val("请选择");
    	$("#cha_dh").val("");
    	$("#cha_v3").val("请选择");
    	$('#chauxnshiousuo').click();
    	chaxun = {};
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	pooruser_initialization();//重新初始化数据
    });

	pooruser_initialization();
});

//加载帮扶人单位
function getbangfuren(test_ren){
	if(test_ren!="请选择"){
		var $i;
		$i = $('#add_bf #ad_bf_dw').bsSuggest("destroy");
		$("#add_bf #ad_bf_dw").removeAttr("data-id");
		$('#add_bf #ad_bf_dw').val("");
		var bsSuggest = $("#add_bf #ad_bf_dw").bsSuggest({
			url: "/assa/get_bangfudanwei_h5.do?sys_company_id="+test_ren,//请求数据的 URL 地址
			idField: 'pkid',//每组数据的哪个字段作为 data-id，优先级高于 indexId 设置（推荐）
			keyField: 'v1',//每组数据的哪个字段作为输入框内容，优先级高于 indexKey 设置（推荐）
			effectiveFields: ['v1','v3','v4','com_name'],//有效显示于列表中的字段，非有效字段都会过滤，默认全部，对自定义getData方法无效
			effectiveFieldsAlias: {'v1':'单位','v3':'分管领导','v4':'联系方式','com_name':'对口嘎查村'},//有效字段的别名对象，用于 header 的显示
			showHeader: false,//是否显示选择列表的 header。为 true 时，有效字段大于一列则显示表头
			//showBtn: true,	//是否显示下拉按钮
			autoDropup: true	//选择菜单是否自动判断向上展开。设为 true，则当下拉菜单高度超过窗体，且向上方向不会被窗体覆盖，则选择菜单向上弹出
//				allowNoKeyword: false,	//是否允许无关键字时请求数据
//				multiWord: false,               //以分隔符号分割的多关键字支持
//			    separator: ',',                 //多关键字支持时的分隔符，默认为半角逗号
		}).on('onDataRequestSuccess', function (e, result) {
	        console.log('onDataRequestSuccess: ', result);
	    }).on('onSetSelectValue', function (e, keyword, data) {
	        console.log('onSetSelectValue: ', keyword, data);
	    }).on('onUnsetSelectValue', function () {
	        console.log("onUnsetSelectValue");
	    });
	}else{
		var $i;
		$i = $('#add_bf #ad_bf_dw').bsSuggest("destroy");
		$('#add_bf #ad_bf_dw').val("");
		$("#add_bf #ad_bf_dw").removeAttr("data-id");
	}
}


//帮扶人初始化
function pooruser_initialization(){
	metTable_bxbxxb.bootstrapTable({
		method: 'POST',
		height: "506",
		url: "/assa/getAssistUsder_List.do",
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
		search: false,//是否显示右上角的搜索框
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
		}
	});
}


function queryParams_bxbxxb(params) {  //配置参数
	var temp = {};
    temp.pageSize = params.limit;
    temp.pageNumber = params.offset;
    temp.search = params.search;
    
    temp.cha_bfdw = chaxun.cha_bfdw;
    temp.cha_bfr = chaxun.cha_bfr;
    temp.cha_juese = chaxun.cha_juese;
    temp.cha_qixian = chaxun.cha_qixian;
    temp.cha_dh = chaxun.cha_dh;
    temp.cha_v3 = chaxun.cha_v3;
    return temp;
}

/**
 * 添加帮扶人信息
 */
function addBfxinxi(){
	
	if($("#add_bf #ad_bf_dw").attr("data-id")!=undefined&&$("#add_bf #ad_bf_dw").attr("data-id")!=""){
		var form_val = JSON.stringify(getFormJson("#add_Form"));//表单数据字符串
		$.ajax({
		    url: "/assa/getAddAssistController.do",
		    type: "POST",
		    async:false,
		    dataType:"json",
		    data:{
		    	dwid: $("#add_bf #ad_bf_dw").attr("data-id"),
		    	form_val: form_val
	        },
		    success: function (data) {
		    	
		    	if (data == "1") {
		    		toastr["success"]("success", "帮扶责任人维护成功");
		    		
		    		$("#add_bf").hide();
			    	$("#xg_bf").hide();
			    	
			    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
			    	pooruser_initialization();//重新初始化数据
			    	$("#add_Form").find("input").each(function(){
			    		var id = $(this).attr("id");
			  		  	$("#"+id).val("");
				    });
			    	$("#v1").val("请选择");
			    	$("#add_qixian").val("请选择");
			    	$("#v3").val("请选择");
			    	getbangfuren("请选择");
		    	}else{
		    		toastr["warning"]("warning", "操作失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		
		});
	}else{
		toastr["info"]("info", "必须指定帮扶人单位");
	}
}
/**
 * 修改前帮扶人
 */
function updateBfxinxi(pkid){
	$("#add_bf").show();
 	$("#add_bf #xia_title").html("修改帮扶人信息");
 	document.getElementById("add_bf").scrollIntoView();
	
	$.ajax({
	    url: "/assa/getUpdateSave.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
        },
	    success: function (data) {
	    	
	    	$('#add_bf #pkid').val(data.bangfu.pkid);
	    	$('#add_bf #col_name').val(data.bangfu.col_name);
	    	if(data.bangfu.v1==undefined||data.bangfu.v1==""||data.bangfu.v1==null){
	    		$('#add_bf #v1').val("请选择");
	    	}else{
	    		$('#add_bf #v1').val(data.bangfu.v1);
	    	}
	    	$('#add_bf #v2').val(data.bangfu.v2);
	    	if(data.bangfu.v3==undefined||data.bangfu.v3==""||data.bangfu.v3==null){
	    		$('#add_bf #v3').val("请选择");
	    	}else{
	    		$('#add_bf #v3').val(data.bangfu.v3);
	    	}
	    	$('#add_bf #v4').val(data.bangfu.v4);
	    	$('#add_bf #v5').val(data.bangfu.v5);
	    	$('#add_bf #v6').val(data.bangfu.v6);
	    	if(data.bangfu.qixian==undefined||data.bangfu.qixian==""||data.bangfu.qixian==null){
	    		$('#add_bf #add_qixian').val("请选择");
	    	}else{
	    		$('#add_bf #add_qixian').val(data.bangfu.qixian);
	    		getbangfuren(data.bangfu.qixian)
	    	}
	    	$('#add_bf #ad_bf_dw').val(data.bangfu.v11);
	    	$('#add_bf #ad_bf_dw').attr("data-id",data.bangfu.dwid);
	    	$('#add_bf #telephone').val(data.bangfu.telephone);
	    	$('#add_bf #col_post').val(data.bangfu.col_post);
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

/**
 * 删除帮扶人
 * @param pkid
 */
function deleteBfr(pkid){
	$.ajax({
	    url: "/assa/getDeleteBfr.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "数据已删除");
	    		swal("删除成功！", "您已经永久删除了这条信息。", "success");
	    		metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
	    		pooruser_initialization();//重新初始化数据
	    	}else{
	    		toastr["warning"]("warning", "操作失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
}

//取消修改或者添加
function quxiao_add(){
	$("#add_bf").hide();
	$("#add_Form").find("input").each(function(){
		var id = $(this).attr("id");
		  	$("#"+id).val("");
	});
	$("#v1").val("请选择");
	$("#add_qixian").val("请选择");
	$("#v3").val("请选择");
	getbangfuren("请选择");
}
