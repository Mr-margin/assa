$(function () {
	$("#bangfuhoushouru_Form").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
	$("#bangfuhouzhichu_Form").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
	
	//保存收入
    $("#bangfuhoushouru #save").click(function () {
    	var validator = $("#bangfuhoushouru_Form").validate();
		if(validator.form()){
			bangfuhoushouru_save();
		}else{
			toastr["warning"]("warning", "请确保输入的金额为正确的数字");
		}
	});
    
    //保存支出
    $("#bangfuhouzhichu #save").click(function () {
    	var validator = $("#bangfuhouzhichu_Form").validate();
		if(validator.form()){
			bangfuhouzhichu_save();
		}else{
			toastr["warning"]("warning", "请确保输入的金额为正确的数字");
		}
	});
	
});


//帮扶后收支分析
function bfhsz(id){
	bangfuhouRefresh_actual();
	$.ajax({
	    url: "/assa/getCurrent_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:id,
	    	type:2
        },
	    success: function (data) {
	    	$("#tab_jbqk").hide();//基本情况
	    	$("#tab_dqszh").hide();//当前收支
	    	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
	    	$("#tab_bfcsh").hide();//帮扶措施
	    	$("#tab_zfqk").hide();//走访情况
	    	$("#tab_bfcx").hide();//帮扶成效
	    	$("#tab_bfgshzhfx").show();//帮扶后收支分析
	    	
	    	document.getElementById('tab_bfgshzhfx').scrollIntoView();
	    	
	    	$("#shang_yi #hu_pkid").val(id);//记录户主ID
	    	//收入
	    	$("#bangfuhoushouru #v1").val(data.shouru.v1);
	    	$("#bangfuhoushouru #v2").val(data.shouru.v2);
	    	$("#bangfuhoushouru #v3").val(data.shouru.v3);
	    	$("#bangfuhoushouru #v4").val(data.shouru.v4);
	    	$("#bangfuhoushouru #v5").val(data.shouru.v5);
	    	$("#bangfuhoushouru #v6").val(data.shouru.v6);
	    	$("#bangfuhoushouru #v7").val(data.shouru.v7);
	    	$("#bangfuhoushouru #v8").val(data.shouru.v8);
	    	$("#bangfuhoushouru #v11").val(data.shouru.v11);
	    	$("#bangfuhoushouru #v12").val(data.shouru.v12);
	    	$("#bangfuhoushouru #v13").val(data.shouru.v13);
	    	$("#bangfuhoushouru #v14").val(data.shouru.v14);
	    	$("#bangfuhoushouru #v15").val(data.shouru.v15);
	    	$("#bangfuhoushouru #v16").val(data.shouru.v16);
	    	$("#bangfuhoushouru #v17").val(data.shouru.v17);
	    	$("#bangfuhoushouru #v18").val(data.shouru.v18);
	    	$("#bangfuhoushouru #v19").val(data.shouru.v19);
	    	$("#bangfuhoushouru #v20").val(data.shouru.v20);
	    	$("#bangfuhoushouru #v23").val(data.shouru.v23);
	    	$("#bangfuhoushouru #v24").val(data.shouru.v24);
	    	$("#bangfuhoushouru #v25").val(data.shouru.v25);
	    	$("#bangfuhoushouru #v26").val(data.shouru.v26);
	    	$("#bangfuhoushouru #v27").val(data.shouru.v27);
	    	$("#bangfuhoushouru #v28").val(data.shouru.v28);
	    	$("#bangfuhoushouru #v29").val(data.shouru.v29);
	    	$("#bangfuhoushouru #v30").val(data.shouru.v30);
	    	$("#bangfuhoushouru #v31").val(data.shouru.v31);
	    	$("#bangfuhoushouru #v32").val(data.shouru.v32);
	    	$("#bangfuhoushouru #v33").val(data.shouru.v33);
	    	$("#bangfuhoushouru #v34").val(data.shouru.v34);
	    	$("#bangfuhoushouru #v35").val(data.shouru.v35);
	    	$("#bangfuhoushouru #v36").val(data.shouru.v36);
	    	$("#bangfuhoushouru #v37").val(data.shouru.v37);
	    	$("#bangfuhoushouru #v38").val(data.shouru.v38);
	    	
	    	//支出
	    	$('#bangfuhouzhichu #v1').val(data.zhichu.v1);
	    	$('#bangfuhouzhichu #v2').val(data.zhichu.v2);
	    	$('#bangfuhouzhichu #v3').val(data.zhichu.v3);
	    	$('#bangfuhouzhichu #v4').val(data.zhichu.v4);
	    	$('#bangfuhouzhichu #v5').val(data.zhichu.v5);
	    	$('#bangfuhouzhichu #v6').val(data.zhichu.v6);
	    	$('#bangfuhouzhichu #v7').val(data.zhichu.v7);
	    	$('#bangfuhouzhichu #v8').val(data.zhichu.v8);
	    	$('#bangfuhouzhichu #v9').val(data.zhichu.v9);
	    	$('#bangfuhouzhichu #v10').val(data.zhichu.v10);
	    	$('#bangfuhouzhichu #v11').val(data.zhichu.v11);
	    	$('#bangfuhouzhichu #v12').val(data.zhichu.v12);
	    	$('#bangfuhouzhichu #v13').val(data.zhichu.v13);
	    	$('#bangfuhouzhichu #v14').val(data.zhichu.v14);
	    	$('#bangfuhouzhichu #v15').val(data.zhichu.v15);
	    	$('#bangfuhouzhichu #v16').val(data.zhichu.v16);
	    	$('#bangfuhouzhichu #v17').val(data.zhichu.v17);
	    	$('#bangfuhouzhichu #v18').val(data.zhichu.v18);
	    	$('#bangfuhouzhichu #v19').val(data.zhichu.v19);
	    	$('#bangfuhouzhichu #v20').val(data.zhichu.v20);
	    	$('#bangfuhouzhichu #v21').val(data.zhichu.v21);
	    	$('#bangfuhouzhichu #v22').val(data.zhichu.v22);
	    	$('#bangfuhouzhichu #v23').val(data.zhichu.v23);
	    	$('#bangfuhouzhichu #v24').val(data.zhichu.v24);
	    	$('#bangfuhouzhichu #v25').val(data.zhichu.v25);
	    	$('#bangfuhouzhichu #v26').val(data.zhichu.v26);
	    	$('#bangfuhouzhichu #v27').val(data.zhichu.v27);
	    	$('#bangfuhouzhichu #v28').val(data.zhichu.v28);
	    	$('#bangfuhouzhichu #v29').val(data.zhichu.v29);
	    	$('#bangfuhouzhichu #v30').val(data.zhichu.v30);
	    	
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
	
	
}

//收入
function bangfuhoushouru_save(){
	var form_val = JSON.stringify(getFormJson("#bangfuhoushouru_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaves_shouru.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val,
	    	type:2
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "帮扶后收入");
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//支出
function bangfuhouzhichu_save(){
	var form_val = JSON.stringify(getFormJson("#bangfuhouzhichu_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaves_zhichu.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val,
	    	type:2
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "帮扶后支出");
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

function bangfuhouRefresh_actual(){
	$("#bangfuhoushouru_Form").find("input").each(function(){
		  var id = $(this).attr("id");
		  $("#"+id).val("");
	});
	$("#bangfuhouzhichu_Form").find("input").each(function(){
		  var id = $(this).attr("id");
		  $("#"+id).val("");
	});
}


