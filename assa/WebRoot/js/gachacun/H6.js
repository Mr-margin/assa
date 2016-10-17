var app={};
var duo=[];
var pkid;
$(document).ready(function() {
  $("#add_gcc").hide();
  $("#xg_gcc").hide();
});
var metTable_bxbxxb = $('#metTable_bxbxxb');
var chaxun = {};//存储表格查询参数


$(function () {
    //添加帮扶人
    $("#add_gcc_button").click(function(){
    	$("#add_gcc").show();
    	$("#xg_gcc").hide();
    	document.getElementById("add_gcc").scrollIntoView();
    });
    //修改帮扶人
    $("#update_gcc_button").click(function(){
    	$("#add_gcc").hide();
    	var row = getSelectedRow(metTable_bxbxxb);//必须确认先选中一条白细胞数据
    	if (typeof row != "undefined") {
    		pkid=row.pkid;
    		 
    		updateFzr(row.pkid);
    		
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
    });
    /**
     * 修改保存
     */
    $("#xg_bfr_xx").click(function(){
    	var row = getSelectedRow(metTable_bxbxxb);//必须确认先选中一条白细胞数据
    	if (typeof row != "undefined") {
    		pkid=row.pkid;
    		
    		updateBfHou(row.pkid);
    		
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
    });
    //删除帮扶人
    $("#delete_gcc_button").click(function(){
    	$("#add_gcc").hide();
    	$("#xg_gcc").hide();
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
	        	deleteFzr(row.pkid);
	        });
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
    });
    //增加用户信息
    $("#znegjia_jilu_button").click(function () {
		zengjia_add();
	}); 
    //点击复选框
	$("#add_hz_biaozhun input").click(function(){
		var tal_type=this;
		if(this.checked){
			$("#add_hz_biaozhun input").each(function(){
				this.checked=false;
			});
			this.checked=true;
		}
		//表现方式
		$("#add_hz_biaozhun input").each(function(i){
			if(this==tal_type){
				if(this.checked){
					app.indxs=i;
				}else{
					app.indxs=null;
				}
			}
		});
		
	});
	
    
	//查询按钮
    $('#cha_button').click(function () {
    	chaxun.cha_gcc_fzr = $("#cha_gcc_fzr").val();
    	chaxun.cha_gcc_phone = $("#cha_gcc_phone").val();
    	chaxun.cha_gcc_cun = $("#cha_gcc_cun").val();
    	chaxun.cha_sumuxiang = $("#cha_sumuxiang").val();
    	chaxun.cha_gachacun = $("#cha_gachacun").val();
    	chaxun.cha_sbbz = $("#cha_sbbz").val();
    	chaxun.cha_pksx = $("#cha_pksx").val();
    	chaxun.cha_zpyy = $("#cha_zpyy").val();
    	chaxun.cha_hzmz = $("#cha_hzmz").val();
    	chaxun.cha_pkrk = $("#cha_pkrk").val();
    	chaxun.cha_ydbq = $("#cha_ydbq").val();
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
    });
    
    //清空查询
    $('#close_cha_button').click(function () {
    	$("#cha_gcc_fzr").val("");
    	$("#cha_gcc_phone").val("");
    	$("#cha_gcc_cun").val("");
    	$('#chauxnshiousuo').click();
    	chaxun = {};
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
    });

    //添加户主
    $("#add_hz").click(function(){
    	addHzxinxi();
    });
//    showpooruser();
	gachacun_initialization();
});

function showpooruser(){
	
	$.ajax({
	    url: "/assa/getPoorUsder_List.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    success: function (data) {
	    	
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	})
}


//帮扶人初始化
function gachacun_initialization(){
	metTable_bxbxxb.bootstrapTable({
		method: 'POST',
		height: "506",
		url: "/assa/getGachacunController.do",
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
			toastr["error"]("error", "服务器异常");
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
    
    temp.cha_gcc_cun = chaxun.cha_gcc_cun;
    temp.cha_gcc_phone = chaxun.cha_gcc_phone;
    temp.cha_gcc_fzr = chaxun.cha_gcc_fzr;
//    temp.cha_qixian = chaxun.cha_qixian;
//    temp.cha_sumuxiang = chaxun.cha_sumuxiang;
//    temp.cha_gachacun = chaxun.cha_gachacun;
//    temp.cha_sbbz = chaxun.cha_sbbz;
//    temp.cha_pksx = chaxun.cha_pksx;
//    temp.cha_zpyy = chaxun.cha_zpyy;
//    temp.cha_hzmz = chaxun.cha_hzmz;
//    temp.cha_pkrk = chaxun.cha_pkrk;
//    temp.cha_ydbq = chaxun.cha_ydbq;
    return temp;
}

var currentId = 1;//记录当前加载队列的下一个ID
//增加样本信息
function zengjia_add(){
	if(currentId<10){
		var zengjiahtml = $("#znegjia").html();
		var reg=new RegExp("_0","g"); //创建正则RegExp对象  
		var newstr=zengjiahtml.replace(reg,"_"+currentId);
		newstr += "<div class=\"form-group\"><div class=\"col-sm-11\"></div><div class=\"col-sm-1\">";
		newstr += "<button type=\"button\" class=\"btn btn-primary btn-xs\" style='margin-top: -560px;margin-left: -950px;' onClick=\"jainshao_add("+currentId+");\">减少</button></div></div>";
		$("#znegjia_"+currentId).html(newstr);
//		$("#add_id").html(newstr);
		currentId++;
		$(".input-group.date").datepicker({
			todayBtn: "linked",
	        keyboardNavigation: !1,
	        forceParse: !1,
	        calendarWeeks: !0,
	        autoclose: !0
	    });
	}
}
//减少
function jainshao_add(id){
	$("#znegjia_"+id).html(" ");
	currentId--;
}
/**
 * 添加嘎查村负责人
 */
function addGccxinxi(){
	var xm=$("#add_gcc_xm").val();
	var phone=$("#add_gcc_phone").val();
	var cun=$("#add_gcc_cun").val();
	var zhanghao=$("#add_gcc_zhanghao").val();
	var mima=$("#add_gcc_mima").val();
	$.ajax({
	    url: "/assa/getAddGRController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	xm:xm,phone:phone,cun:cun,zhanghao:zhanghao,mima:mima,
	    	
        },
	    success: function (data) {
	    	 $("#add_gcc").hide();
	    	  $("#xg_gcc").hide();
	    	//销毁现有表格数据
	    	metTable_bxbxxb.bootstrapTable('destroy');
	    	//重新初始化数据
	    	gachacun_initialization();
	    	toastr["success"]("success", "新数据添加");
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
	
}
/**
 * 修改前帮扶人
 */
function updateFzr(pkid){
	 $("#xg_gcc").show();
	 document.getElementById("xg_gcc").scrollIntoView();
	
	$.ajax({
	    url: "/assa/getUpdateSaveFzr.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
        },
	    success: function (data) {
	    	$('#xg_gcc_xm').val(data[0].v1);
	    	$('#xg_gcc_phone').val(data[0].v2);
	    	$('#xg_bf_cun').val(data[0].v5);
	    	$('#xg_gcc_zhanghao').val(data[1].col_account);
	    	$('#xg_gcc_mima').val(data[1].col_password);
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
	
}
/**
 * 修改后
 * @param pkid
 */
function updateBfHou(pkid){
	var xm=$("#xg_gcc_xm").val();
	var phone=$("#xg_gcc_phone").val();
	var cun=$("#xg_bf_cun").val();
	var zhanghao=$('#xg_gcc_zhanghao').val();
	var mima =$('#xg_gcc_mima').val();
	$.ajax({
	    url: "/assa/getUpDateFzrController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,xm:xm,phone:phone,cun:cun,zhanghao:zhanghao,mima:mima,
        },
	    success: function (data) {
	    	//销毁现有表格数据
    		metTable_bxbxxb.bootstrapTable('destroy');
    		//重新初始化数据
    		gachacun_initialization();
	    	 $("#add_gcc").hide();
	    	 $("#xg_gcc").hide();
	    	 $('#xg_gcc_xm').val("");
	    	$('#xg_gcc_phone').val("");
	    	$('#xg_bf_cun').val("");
	    	$('#xg_gcc_zhanghao').val("");
	    	$('#xg_gcc_mima').val("");
	    	 toastr["success"]("success", "修改成功");
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
function deleteFzr(pkid){
	$.ajax({
	    url: "/assa/getDeleteFzrController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
        },
	    success: function (data) {
	    	toastr["success"]("success", "数据已删除");
    		swal("删除成功！", "您已经永久删除了这条信息。", "success");
    		//销毁现有表格数据
    		metTable_bxbxxb.bootstrapTable('destroy');
    		//重新初始化数据
    		gachacun_initialization();
	    	 $("#add_gcc").hide();
	    	 $("#xg_gcc").hide();
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
}