$(function () {
	$("#shouru_Form").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
	$("#zhichu_Form").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
	
	//保存收入
    $("#shouru #save").click(function () {
    	var validator = $("#shouru_Form").validate();
		if(validator.form()){
			shouru_save();
		}else{
			toastr["warning"]("warning", "请确保输入的金额为正确的数字");
		}
	});
    
    //保存支出
    $("#zhichu #save").click(function () {
    	var validator = $("#zhichu_Form").validate();
		if(validator.form()){
			zhichu_save();
		}else{
			toastr["warning"]("warning", "请确保输入的金额为正确的数字");
		}
	});
	
});

//当前收支
function dqsz(id){
	Refresh_actual();
	$.ajax({
	    url: "/assa/getCurrent_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:id,
	    	type:1
        },
	    success: function (data) {
	    	$("#tab_jbqk").hide();//基本情况
	    	$("#tab_dqszh").show();//当前收支
	    	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
	    	$("#tab_bfcsh").hide();//帮扶措施
	    	$("#tab_zfqk").hide();//走访情况
	    	$("#tab_bfcx").hide();//帮扶成效
	    	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
	    	
	    	document.getElementById('tab_dqszh').scrollIntoView();
	    	
	    	$("#shang_yi #hu_pkid").val(id);//记录户主ID
	    	//收入
	    	$("#shouru #v1").val(data.shouru.v1);
	    	$("#shouru #v2").val(data.shouru.v2);
	    	$("#shouru #v3").val(data.shouru.v3);
	    	$("#shouru #v4").val(data.shouru.v4);
	    	$("#shouru #v5").val(data.shouru.v5);
	    	$("#shouru #v6").val(data.shouru.v6);
	    	$("#shouru #v7").val(data.shouru.v7);
	    	$("#shouru #v8").val(data.shouru.v8);
	    	$("#shouru #v11").val(data.shouru.v11);
	    	$("#shouru #v12").val(data.shouru.v12);
	    	$("#shouru #v13").val(data.shouru.v13);
	    	$("#shouru #v14").val(data.shouru.v14);
	    	$("#shouru #v15").val(data.shouru.v15);
	    	$("#shouru #v16").val(data.shouru.v16);
	    	$("#shouru #v17").val(data.shouru.v17);
	    	$("#shouru #v18").val(data.shouru.v18);
	    	$("#shouru #v19").val(data.shouru.v19);
	    	$("#shouru #v20").val(data.shouru.v20);
	    	$("#shouru #v23").val(data.shouru.v23);
	    	$("#shouru #v24").val(data.shouru.v24);
	    	$("#shouru #v25").val(data.shouru.v25);
	    	$("#shouru #v26").val(data.shouru.v26);
	    	$("#shouru #v27").val(data.shouru.v27);
	    	$("#shouru #v28").val(data.shouru.v28);
	    	$("#shouru #v29").val(data.shouru.v29);
	    	$("#shouru #v30").val(data.shouru.v30);
	    	$("#shouru #v31").val(data.shouru.v31);
	    	$("#shouru #v32").val(data.shouru.v32);
	    	$("#shouru #v33").val(data.shouru.v33);
	    	$("#shouru #v34").val(data.shouru.v34);
	    	$("#shouru #v35").val(data.shouru.v35);
	    	$("#shouru #v36").val(data.shouru.v36);
	    	$("#shouru #v37").val(data.shouru.v37);
	    	$("#shouru #v38").val(data.shouru.v38);
	    	$("#shouru #v40").val(data.shouru.v40);
	    	$("#shouru #v41").val(data.shouru.v41);
	    	$("#shouru #v42").val(data.shouru.v42);
	    	$("#shouru #v43").val(data.shouru.v43);
	    	$("#shouru #current_total_income").val(data.total_income);
	    	//支出
	    	$('#zhichu #v1').val(data.zhichu.v1);
	    	$('#zhichu #v2').val(data.zhichu.v2);
	    	$('#zhichu #v3').val(data.zhichu.v3);
	    	$('#zhichu #v4').val(data.zhichu.v4);
	    	$('#zhichu #v5').val(data.zhichu.v5);
	    	$('#zhichu #v6').val(data.zhichu.v6);
	    	$('#zhichu #v7').val(data.zhichu.v7);
	    	$('#zhichu #v8').val(data.zhichu.v8);
	    	$('#zhichu #v9').val(data.zhichu.v9);
	    	$('#zhichu #v10').val(data.zhichu.v10);
	    	$('#zhichu #v11').val(data.zhichu.v11);
	    	$('#zhichu #v12').val(data.zhichu.v12);
	    	$('#zhichu #v13').val(data.zhichu.v13);
	    	$('#zhichu #v14').val(data.zhichu.v14);
	    	$('#zhichu #v15').val(data.zhichu.v15);
	    	$('#zhichu #v16').val(data.zhichu.v16);
	    	$('#zhichu #v17').val(data.zhichu.v17);
	    	$('#zhichu #v18').val(data.zhichu.v18);
	    	$('#zhichu #v19').val(data.zhichu.v19);
	    	$('#zhichu #v20').val(data.zhichu.v20);
	    	$('#zhichu #v21').val(data.zhichu.v21);
	    	$('#zhichu #v22').val(data.zhichu.v22);
	    	$('#zhichu #v23').val(data.zhichu.v23);
	    	$('#zhichu #v24').val(data.zhichu.v24);
	    	$('#zhichu #v25').val(data.zhichu.v25);
	    	$('#zhichu #v26').val(data.zhichu.v26);
	    	$('#zhichu #v27').val(data.zhichu.v27);
	    	$('#zhichu #v28').val(data.zhichu.v28);
	    	$('#zhichu #v29').val(data.zhichu.v29);
	    	$('#zhichu #v30').val(data.zhichu.v30);
	    	$("#zhichu #current_total_expenditure").val(data.total_expenditure);
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
	
	
}

//收入
function shouru_save(){
	var form_val = JSON.stringify(getFormJson("#shouru_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaves_shouru.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val,
	    	type:1
        },
	    success: function (data) {
	    	var JsonData=JSON.parse(data);
	    	if (JsonData.isSuccess=="1") {
	    		toastr["success"]("success", "当前收入");
	    		$("#shouru #current_total_income").val(JsonData.total_income);
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
function zhichu_save(){
	var form_val = JSON.stringify(getFormJson("#zhichu_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaves_zhichu.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val,
	    	type:1
        },
	    success: function (data) {
	    	var JsonData=JSON.parse(data);
	    	if (JsonData.isSuccess=="1") {
	    		toastr["success"]("success", "当前支出");
	    		$("#zhichu #current_total_expenditure").val(JsonData.total_expenditure);
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

function Refresh_actual(){
	$("#shouru_Form").find("input").each(function(){
		  var id = $(this).attr("id");
		  $("#"+id).val("");
	});
	$("#zhichu_Form").find("input").each(function(){
		  var id = $(this).attr("id");
		  $("#"+id).val("");
	});
}


