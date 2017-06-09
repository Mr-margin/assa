$.validator.setDefaults({
    highlight: function(e) {
        $(e).closest(".form-group").removeClass("has-success").addClass("has-error")
    },
    success: function(e) {
        e.closest(".form-group").removeClass("has-error").addClass("has-success")
    },
    errorElement: "span",
    errorPlacement: function(e, r) {
        e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});
$.validator.addMethod("chinese", function(value, element) {
	var chinese = /^[\u4e00-\u9fa5]+$/;
	return this.optional(element) || (chinese.test(value));
}, "只能输入中文");
$.validator.addMethod("qingxuanze", function(value, element) {
	//var valtext = '请选择';
	var valtext = value != '请选择';
	return this.optional(element) || (valtext);
}, "必选选择");

var number;
var tjian;
$(document).ready(function(){
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式
	
	getinfo_ready($("#cha_minzu"));
	getinfo_ready($("#huzhu #v11"));
	getinfo_ready($("#jiatingchengyuan #v11"));
	getinfo_ready_guanxi($("#jiatingchengyuan #v10"));
	getinfo_ready_zaixiaosheng($("#jiatingchengyuan #v13"));
	getinfo_ready_pinkunhushuxing($("#huzhu #v22"));
	getinfo_ready_wenhuachengdu($("#jiatingchengyuan #v12"));
	getinfo_ready_wenhuachengdu($("#huzhu #v12"));
	getinfo_ready_zaixiaosheng($("#huzhu #v13"));
	getinfo_ready($('#cha_mz'));
	
	getInfo_ready_zhipinyuanyin($('#cha_zpyy'));

	if(jsondata.company_map.com_type=="单位"){
		var type = jsondata.company_map.com_type;
		var val = jsondata.company;
		var qixian;
		if(val.com_level == "1"){
			getinfo_xiqian($('#cha_qx2'));
			getinfo_xiqian($('#cha_qx'));
			getinfo_xiqian($('#cha_qx_add'));
		}else if(val.com_level == "2"){
			$("#metTable_bxbxxb #v3").attr("data-visible","false");
			
			$('#cha_qx2').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			getinfo_xiang(jsondata.company.xian,$('#cha_smx2'));
			
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			getinfo_xiang(jsondata.company.xian,$('#cha_smx'));
			
			$('#cha_qx_add').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			getinfo_xiang(jsondata.company.xian,$('#cha_smx_add'));
			
			chaxun.cha_qx = jsondata.company.xian;
		}else if(val.com_level == "3"){
			$("#metTable_bxbxxb #v3").attr("data-visible","false");
			$("#metTable_bxbxxb #v4").attr("data-visible","false");
			
			$('#cha_qx2').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx2").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			getinfo_cun(jsondata.company.xiang,$('#cha_gcc2'));
			
			$('#cha_qx').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			getinfo_cun(jsondata.company.xiang,$('#cha_gcc'));
			
			$('#cha_qx_add').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx_add").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			getinfo_cun(jsondata.company.xiang,$('#cha_gcc_add'));
			
			chaxun.cha_qx = jsondata.company.xian;
			chaxun.cha_smx = jsondata.company.xiang;
		}else if(val.com_level == "4"){
			$("#metTable_bxbxxb #v3").attr("data-visible","false");
			$("#metTable_bxbxxb #v4").attr("data-visible","false");
			$("#metTable_bxbxxb #v5").attr("data-visible","false");
			
			$('#cha_qx2').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx2").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc2").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
			
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
			
			$('#cha_qx_add').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx_add").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc_add").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
			
			chaxun.cha_qx = jsondata.company.xian;
			chaxun.cha_smx = jsondata.company.xiang;
			chaxun.cha_gcc = jsondata.company.cun;
		}
	}else if(jsondata.company_map.com_type=="管理员"){
		getinfo_xiqian($('#cha_qx2'));
		getinfo_xiqian($('#cha_qx'));
		getinfo_xiqian($('#cha_qx_add'));
	}else{
		$("#chauxn_quanxian").hide();
	}
	
	$("#addForm").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
//	var col_account=jsondata.Login_map.col_account;//用户名
//	if(col_account!="100000"){
//		$("#add_hu_but")
//	}
	
});

var metTable_bxbxxb = $('#metTable_bxbxxb');
var chaxun = {};//存储表格查询参数

$(function () {
	//查询按钮
    $('#cha_button').click(function () {
    	chaxun.cha_qx = $("#cha_qx").val();
    	chaxun.cha_smx = $("#cha_smx").val();
    	chaxun.cha_gcc = $("#cha_gcc").val();
    	chaxun.cha_sbbz = $("#cha_sbbz").val();
    	chaxun.cha_init_flag = $("#init_flag").val();
    	chaxun.cha_pksx = $("#cha_pksx").val();
    	chaxun.cha_zpyy = $("#cha_zpyy").val();
    	chaxun.cha_mz = $("#cha_mz").val();
    	chaxun.cha_renkou = $("#cha_renkou").val();
    	chaxun.cha_bfdw = $("#cha_bfdw").val();
    	chaxun.cha_bfzrr = $("#cha_bfzrr").val();
    	chaxun.cha_banqian = $("#cha_banqian").val();
    	
    	chaxun.cha_v6 = $("#cha_v6").val();
    	chaxun.cha_v8 = $("#cha_v8").val();
    	chaxun.cha_v8_1 = $("#cha_v8_1").val();
    	chaxun.cha_year = $("#cha_year").val();//年份
    	chaxun.data_year = $("#data_year").val();//年份
    	$("#tab_jbqk").hide();//基本情况
    	$("#tab_dqszh").hide();//当前收支
    	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
    	$("#tab_bfcsh").hide();//帮扶措施
    	$("#tab_zfqk").hide();//帮扶成效
    	$("#tab_bfcx").hide();//帮扶成效
    	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
    	
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
//      	setTimeout(function(){
//    		var str = gnRole();
//    		str=str.replace('undefined',"");
//    		if(str!=""){
//    			var arr=str.split(',');
//    			for(var i=0;i<arr.length;i++){
//    				$("."+arr[i]).css("display","","text-align","center");
//    			}
//    		}
//    	},100);
    });
    
    //清空查询
    $('#close_cha_button').click(function () {
    	$("#cha_qx").val("请选择");
    	$("#cha_smx").val("请选择");
    	$("#cha_gcc").val("请选择");
    	$("#cha_sbbz").val("请选择");
    	$("#init_flag").val("请选择");
    	$("#cha_pksx").val("请选择");
    	$("#cha_zpyy").val("请选择");
    	$("#cha_mz").val("请选择");
    	$("#cha_renkou").val("请选择");
    	$("#cha_bfdw").val("");
    	$("#cha_bfzrr").val("");
    	$("#cha_v6").val();
    	$("#cha_v8").val();
    	$("#cha_v8_1").val("请选择");
    	$("#cha_banqian").val("请选择");
    	$("#cha_year").val("2017");
    	$("#data_year").val("2017");
    	$('#chauxnshiousuo').click();
    	chaxun = {};
    	
    	$("#tab_jbqk").hide();//基本情况
    	$("#tab_dqszh").hide();//当前收支
    	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
    	$("#tab_bfcsh").hide();//帮扶措施
    	$("#tab_zfqk").hide();//帮扶成效
    	$("#tab_bfcx").hide();//帮扶成效
    	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
    	
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
    });
	gachacun_initialization();
	
	
	$('#save_hu').click(function () {
		//提交表单进行验证
		var validator = $("#addForm").validate();
		if(validator.form()){
			add_hu($("#cha_qx_add").val(), $("#cha_smx_add").val(), $("#cha_gcc_add").val(), $("#huname_add").val());
		}
    });
	
	
	$('#del_hu').click(function () {
    	
		var row = metTable_bxbxxb.bootstrapTable('getSelections');
		if (row.length>0 ) {
			var str = "";
			$.each(row,function(i,item){
				str += item.pkid+",";
 			});
			str = str.substring(0,str.length-1);
			
			swal({
	            title: "您确定要删除选定的贫困户吗？",
	            text: "这不是脱贫操作，删除后将无法恢复，请谨慎！",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "删除",
	            closeOnConfirm: false
	        },
	        function() {
	            del_hu_st(str);
	        });
		}
    });
});

function del_hu_st(str){
	$.ajax({  		       
	    url: "/assa/getDelHu.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	str:str
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "删除贫困户");
	    		swal("删除成功！", "您已经永久删除了这些贫困户。", "success");
	    		
	    		metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
	    		gachacun_initialization();//重新初始化数据
	    	}else{
	    		toastr["warning"]("warning", "操作失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//添加新的贫困户
function add_hu(qx,xaing,cun,huname){
	
	$.ajax({  		       
	    url: "/assa/getAddHu.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	qx:qx,
	    	xaing:xaing,
	    	cun:cun,
	    	huname:huname
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "补充新贫困户");
	    		
	    		metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
	    		gachacun_initialization();//重新初始化数据
	    		
	    		$("#huname_add").val("");
	    	}else{
	    		toastr["warning"]("warning", "操作失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	$("#close_hu").click();
}


//帮扶人初始化
function gachacun_initialization(){
	if($('#data_year').val()=='2016'){
		$('#fpsc_update_tools').hide();
		$("#huzhu #save").hide();
		$("#add_new_jiatingchngyuan2").hide();
		$("#is_2016").show();
		$("#is_2017").hide();
	/*	$("th[data-field=entry_year]").hide();*/
	}else{
		$('#fpsc_update_tools').show();
		//$("#huzhu #save").show();
		//$("#add_new_jiatingchngyuan").show();
		if(jsondata.company.com_level=="3"||jsondata.company.com_level=="4"){
			$("#add_new_jiatingchngyuan2").hide();
		}else{
			$("#add_new_jiatingchngyuan2").show();
		}
		$("#is_2017").show();
		$("#is_2016").hide();
		
	}
	if(jsondata.company.com_level=="1"||jsondata.company.com_level=="2"){
		$("#data_contoller").show();
	}else{
		$("#data_contoller").hide();
	}
	metTable_bxbxxb.bootstrapTable({
		method: 'POST',
		height: "597",
		url: "/assa/getPoorUserController.do",
		dataType: "json",//从服务器返回的数据类型
		striped: true,	 //使表格带有条纹
		pagination: true,	//在表格底部显示分页工具栏
		pageSize: 10,	//页面大小
		pageNumber: 1,	//页数
		pageList: [10, 20, 50, 100],
		showToggle: true,   //名片格式
		showColumns: true, //显示隐藏列  
		toolbar: "#fpsc_update_tools",
		iconSize: "outline",
        icons: {
            refresh: "glyphicon-repeat",
            toggle: "glyphicon-list-alt",
            columns: "glyphicon-list"
        },
		showRefresh: true,  //显示刷新按钮
		singleSelect: false,//复选框只能选择一条记录
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
    
    temp.cha_qx = chaxun.cha_qx;
    temp.cha_smx = chaxun.cha_smx;
    temp.cha_gcc = chaxun.cha_gcc;
    temp.cha_sbbz = chaxun.cha_sbbz;
    temp.cha_pksx = chaxun.cha_pksx;
    temp.cha_zpyy = chaxun.cha_zpyy;
    temp.cha_mz = chaxun.cha_mz;
    temp.cha_renkou = chaxun.cha_renkou;
    temp.cha_bfdw = chaxun.cha_bfdw;
    temp.cha_bfzrr = chaxun.cha_bfzrr;
    temp.cha_banqian = chaxun.cha_banqian;
    
    temp.cha_v6 = chaxun.cha_v6;
    temp.cha_v8 = chaxun.cha_v8;
    temp.cha_v8_1 = chaxun.cha_v8_1;
    temp.cha_year = chaxun.cha_year;
    temp.data_year = chaxun.data_year;
    temp.cha_init_flag = chaxun.cha_init_flag;
    return temp;
}
