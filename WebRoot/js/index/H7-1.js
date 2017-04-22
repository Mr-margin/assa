$(function() {
    $('#treeview').treeview({
    	color: "#428bca",
    	multiSelect: false,
    	highlightSelected: true,
    	data: show_tree(),
    	onNodeSelected: function(e, o) {
            click_tree(o);
        }
    });
    
    com_level=jsondata.company.com_level;//用户层级
    code=jsondata.company.com_code;//行政编码
    com_name=jsondata.company.com_name;//名称
    data_info(jsondata.company.pkid)//数据初始化，传进用户id
    
    //标题赋值
    $('#title_name').html(com_name);
    
    //validate实时验证
    $("#ydbq_form").validate({
		onfocusout: function(element){
			$(element).valid();
		}
	});
    
    //点击保存按钮
    $("#save_button").click(function(){
    	var validator = $("#ydbq_form").validate();
    	if(validator.form()){
    		updata(qt_pkid,$("#a1_b1").val(),$("#a1_b2").val(),$("#a1_b3").val(),$("#a1_b4").val(),
    					   $("#a2_b1").val(),$("#a2_b2").val(),$("#a2_b3").val(),$("#a2_b4").val(),
    					   $("#a3_b1").val(),$("#a3_b2").val(),$("#a3_b3").val(),$("#a3_b4").val());
    	}
    });
    
});

var code;//行政编码
var com_level;//层级
var com_name;//名称
var datas;//从后台获取的表格数据
var qt_pkid;//company_id

toastr.options = {//弹出提示框
		"closeButton": true,
		"debug": false,
		"progressBar": false,
		"positionClass": "toast-top-right",
		"onclick": null,
		"showDuration": "400",
		"hideDuration": "1000",
		"timeOut": "6000",
		"extendedTimeOut": "1000",
		"showEasing": "swing",
		"hideEasing": "linear",
		"showMethod": "fadeIn",
		"hideMethod": "fadeOut"
}

//节点点击事件
function click_tree(val){
	$('#title_name').html(val.text);
	shuaxin();
	data_info(val.pkid);
}

//刷新
function shuaxin(){
	$("#a1_b1").val("");
	$("#a1_b2").val("");
	$("#a1_b3").val("");
	$("#a1_b4").val("");
	$("#a2_b1").val("");
	$("#a2_b2").val("");
	$("#a2_b3").val("");
	$("#a2_b4").val("");
	$("#a3_b1").val("");
	$("#a3_b2").val("");
	$("#a3_b3").val("");
	$("#a3_b4").val("");
}

//保存方法
function updata(pkid,a11,a12,a13,a14,a21,a22,a23,a24,a31,a32,a33,a34,a41,a42,a43,a44){
	$.ajax({
		url:"/assa/upydbqdata.do",
		type:"POST",
		async:false,
		dataType:"json",
		data:{
			pkid:pkid,
			a11:a11, a12:a12, a13:a13, a14:a14,
			a21:a21, a22:a22, a23:a23, a24:a24,
			a31:a31, a32:a32, a33:a33, a34:a34,
			a41:a41, a42:a42, a43:a43, a44:a44
		},
		success: function(data){
			if(data=="1"){
				toastr["success"]("修改成功", "数据修改成功");
			}else{
				toastr["error"]("数据保存失败，请检查数据后重试");
			}
			
		},
		erro: function () { 
	    	toastr["error"]("错误", "网络异常");
	    }  
	});
}

//数据初始化
function data_info(pkid){
	qt_pkid=pkid;
	$.ajax({  		       
	    url: "/assa/getydbqData.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{
	    	pkid: pkid
	    },
	    success: function (data) {
	    	$.each(data, function(i,item) {
	    		if(item.place_type.toString()=="1"){
	    			$("#a1_b1").val(item.start_place);
	    			$("#a1_b2").val(item.speed_proportion);
	    			$("#a1_b3").val(item.project_proportion);
	    			$("#a1_b4").val(item.capital);
	    		}else if(item.place_type.toString()=="2"){
	    			$("#a2_b1").val(item.start_place);
	    			$("#a2_b2").val(item.speed_proportion);
	    			$("#a2_b3").val(item.project_proportion);
	    			$("#a2_b4").val(item.capital);
	    		}else{
	    			$("#a3_b1").val(item.start_place);
	    			$("#a3_b2").val(item.speed_proportion);
	    			$("#a3_b3").val(item.project_proportion);
	    			$("#a3_b4").val(item.capital);
	    		}
	    	});
	    	
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//加载行政区划树
function show_tree(){
	var treeData;
	$.ajax({  		       
	    url: "/assa/getTreeData.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    success: function (data) {
	    	
	    	if (typeof data != "undefined") {
	    		treeData = data;
	    	}else{
	    		toastr["error"]("error", "服务器异常");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	return treeData;
}