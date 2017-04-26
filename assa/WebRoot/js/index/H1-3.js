$(function () {
//	//开始编辑
//	$("#bangfuren #v1_button").click(function () {
//		$("#bangfuren #v1").hide();
//		$("#bangfuren #v1_button").hide();
//		$("#bangfuren #v1_bj").show();
//		$("#bangfuren #v1_bc").show();
//	});
//	//保存目标
//	$("#bangfuren #v1_bc").click(function () {
//		$.ajax({  		       
//		    url: "/assa/getSave_Mubiao.do",
//		    type: "POST",
//		    async:false,
//		    dataType: "text",
//		    data:{
//		    	pkid: $("#shang_yi #hu_pkid").val(),
//		    	baocunneirong: $("#bangfuren #v1_bj").val()
//	        },
//		    success: function (data) {
//		    	if (data == "1") {
//		    		toastr["success"]("success", "帮扶目标保存");
//		    		$("#bangfuren #v1").show();
//		    		$("#bangfuren #v1_button").show();
//		    		$("#bangfuren #v1_bj").hide();
//		    		$("#bangfuren #v1_bc").hide();
//		    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
//		    	}else{
//		    		toastr["warning"]("warning", "保存失败，检查数据后重试");
//		    	}
//		    },
//		    error: function () { 
//		    	toastr["error"]("error", "服务器异常");
//		    }  
//		});
//	});
//	
//	//开始编辑
//	$("#bangfuren #v2_button").click(function () {
//		$("#bangfuren #v2").hide();
//		$("#bangfuren #v2_button").hide();
//		$("#bangfuren #v2_bj").show();
//		$("#bangfuren #v2_bc").show();
//	});
//	
//	//保存时限
//	$("#bangfuren #v2_bc").click(function () {
//		$.ajax({  		       
//		    url: "/assa/getSave_Shixian.do",
//		    type: "POST",
//		    async:false,
//		    dataType: "text",
//		    data:{
//		    	pkid: $("#shang_yi #hu_pkid").val(),
//		    	baocunneirong: $("#bangfuren #v2_bj").val()
//	        },
//		    success: function (data) {
//		    	if (data == "1") {
//		    		toastr["success"]("success", "帮扶目标保存");
//		    		$("#bangfuren #v2").show();
//		    		$("#bangfuren #v2_button").show();
//		    		$("#bangfuren #v2_bj").hide();
//		    		$("#bangfuren #v2_bc").hide();
//		    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
//		    	}else{
//		    		toastr["warning"]("warning", "保存失败，检查数据后重试");
//		    	}
//		    },
//		    error: function () { 
//		    	toastr["error"]("error", "服务器异常");
//		    }  
//		});
//	});
//	
//	//开始编辑
//	$("#bangfuren #v3_button").click(function () {
//		$("#bangfuren #v3").hide();
//		$("#bangfuren #v3_button").hide();
//		$("#bangfuren #v3_bj").show();
//		$("#bangfuren #v3_bc").show();
//	});
//	
//	//保存计划
//	$("#bangfuren #v3_bc").click(function () {
//		$.ajax({  		       
//		    url: "/assa/getSave_Jihua.do",
//		    type: "POST",
//		    async:false,
//		    dataType: "text",
//		    data:{
//		    	pkid: $("#shang_yi #hu_pkid").val(),
//		    	baocunneirong: $("#bangfuren #v3_bj").val()
//	        },
//		    success: function (data) {
//		    	if (data == "1") {
//		    		toastr["success"]("success", "帮扶目标保存");
//		    		$("#bangfuren #v3").show();
//		    		$("#bangfuren #v3_button").show();
//		    		$("#bangfuren #v3_bj").hide();
//		    		$("#bangfuren #v3_bc").hide();
//		    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
//		    	}else{
//		    		toastr["warning"]("warning", "保存失败，检查数据后重试");
//		    	}
//		    },
//		    error: function () { 
//		    	toastr["error"]("error", "服务器异常");
//		    }  
//		});
//	});
	
	//帮扶计划
	$("#tab-b-3 #saveBFJH").click(function () {
		var form_val = JSON.stringify(getFormJson("#bangfujihua_Form"));//表单数据字符串
		$.ajax({  		       
		    url: "/assa/getSave_Mubiao.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#shang_yi #hu_pkid").val(),
		    	form_val: form_val
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "帮扶计划保存");
//		    		$("#bangfuren #v3").show();
//		    		$("#bangfuren #v3_button").show();
//		    		$("#bangfuren #v3_bj").hide();
//		    		$("#bangfuren #v3_bc").hide();
//		    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
		    	}else{
		    		toastr["warning"]("warning", "保存失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		});
	});
	
	//脱贫计划
	$("#tab-b-2 #saveTPJH").click(function () {
		var form_val = JSON.stringify(getFormJson("#tuopinjihua_Form"));//表单数据字符串
		$.ajax({  		       
		    url: "/assa/getSave_Jihua.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#shang_yi #hu_pkid").val(),
		    	form_val: form_val
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "脱贫计划保存");
//		    		$("#bangfuren #v3").show();
//		    		$("#bangfuren #v3_button").show();
//		    		$("#bangfuren #v3_bj").hide();
//		    		$("#bangfuren #v3_bc").hide();
//		    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
		    	}else{
		    		toastr["warning"]("warning", "保存失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		});
	});
	
});

function getbangfuren_xiqian(){
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
	    	qixian='<option value="4">市直属单位</option>';
	    	$.each(data,function(i,item){
	    		if(type=="单位"){
	    			if(val.com_level == "1"){
	    				qixian+='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
	    			}else if(val.com_level == "2"){
	    				if(val.xian==item.com_name){
	    					qixian+='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
	    				}
	    			}else if(val.com_level == "3"){
	    				if(val.xian==item.com_name){
	    					qixian+='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
	    				}
	    			}else if(val.com_level == "4"){
	    				if(val.xian==item.com_name){
	    					qixian+='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
	    				}
	    			}
	    		}else if(type=="管理员"){
	    			qixian+='<option  value="'+item.pkid+'">'+item.com_name+'</option>';
	    		}
	    		
	    	});
	    	$("#bangfuren #sys_company_id").html(qixian);
	    },
	    error: function () { 
	    }  
	
	});
}

//帮扶单位和责任人
function bfdwr(id,cunId){
	$("#tab_jbqk").hide();//基本情况
	$("#tab_dqszh").hide();//当前收支
	$("#tab_bfdwyzrr").show();//帮扶单位与责任人
	$("#tab_bfcsh").hide();//帮扶措施
	$("#tab_zfqk").hide();//走访情况
	$("#tab_bfcx").hide();//帮扶成效
	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
	document.getElementById('tab_bfdwyzrr').scrollIntoView();
	getbangfuren_xiqian();
/*	alert(cunId);*/
	var bsSuggest = $("#bangfuren #add_bangfuren_test").bsSuggest({
		url: "/assa/getPersonal.do?sys_company_id="+$("#bangfuren #sys_company_id").val()+"&huid="+id+"&cunId="+cunId,//请求数据的 URL 地址
		idField: 'pkid',//每组数据的哪个字段作为 data-id，优先级高于 indexId 设置（推荐）
		keyField: 'col_name',//每组数据的哪个字段作为输入框内容，优先级高于 indexKey 设置（推荐）
		effectiveFields: ['col_name','com_name','col_post','telephone'],//有效显示于列表中的字段，非有效字段都会过滤，默认全部，对自定义getData方法无效
		effectiveFieldsAlias: {'col_name':'姓名','com_name':'单位','col_post':'职务','telephone':'电话'},//有效字段的别名对象，用于 header 的显示
		showHeader: false,//是否显示选择列表的 header。为 true 时，有效字段大于一列则显示表头
		//showBtn: false,	//是否显示下拉按钮
		autoDropup: true	//选择菜单是否自动判断向上展开。设为 true，则当下拉菜单高度超过窗体，且向上方向不会被窗体覆盖，则选择菜单向上弹出
//		allowNoKeyword: false,	//是否允许无关键字时请求数据
//		multiWord: false,               //以分隔符号分割的多关键字支持
//	    separator: ',',                 //多关键字支持时的分隔符，默认为半角逗号
	}).on('onDataRequestSuccess', function (e, result) {//当 AJAX 请求数据成功时触发，并传回结果到第二个参数
        console.log('onDataRequestSuccess: ', result);
    }).on('onSetSelectValue', function (e, keyword, data) {//当从下拉菜单选取值时触发，并传回设置的数据到第二个参数
        console.log('onSetSelectValue: ', keyword, data);
    }).on('onUnsetSelectValue', function () {//当设置了 idField，且自由输入内容时触发（与背景警告色显示同步）
        console.log("onUnsetSelectValue");
    });
	
	$("#shang_yi #hu_pkid").val(id);//记录户主ID
	data_jiazai_bangfu(id);
	
}

//重新加载帮扶人
function getbangfuren(test_ren){
	var $i;
	$i = $('#bangfuren #add_bangfuren_test').bsSuggest("destroy");
	var bsSuggest = $("#bangfuren #add_bangfuren_test").bsSuggest({
		url: "/assa/getPersonal.do?sys_company_id="+test_ren+"&huid="+$("#shang_yi #hu_pkid").val(),//请求数据的 URL 地址
		idField: 'pkid',//每组数据的哪个字段作为 data-id，优先级高于 indexId 设置（推荐）
		keyField: 'col_name',//每组数据的哪个字段作为输入框内容，优先级高于 indexKey 设置（推荐）
		effectiveFields: ['col_name','com_name','col_post','telephone'],//有效显示于列表中的字段，非有效字段都会过滤，默认全部，对自定义getData方法无效
		effectiveFieldsAlias: {'col_name':'姓名','com_name':'单位','col_post':'职务','telephone':'电话'},//有效字段的别名对象，用于 header 的显示
		showHeader: false,//是否显示选择列表的 header。为 true 时，有效字段大于一列则显示表头
		//showBtn: false,	//是否显示下拉按钮
		autoDropup: true	//选择菜单是否自动判断向上展开。设为 true，则当下拉菜单高度超过窗体，且向上方向不会被窗体覆盖，则选择菜单向上弹出
//			allowNoKeyword: false,	//是否允许无关键字时请求数据
//			multiWord: false,               //以分隔符号分割的多关键字支持
//		    separator: ',',                 //多关键字支持时的分隔符，默认为半角逗号
	}).on('onDataRequestSuccess', function (e, result) {
        console.log('onDataRequestSuccess: ', result);
    }).on('onSetSelectValue', function (e, keyword, data) {
        console.log('onSetSelectValue: ', keyword, data);
    }).on('onUnsetSelectValue', function () {
        console.log("onUnsetSelectValue");
    });
	
}


//保存帮扶责任人
function save_bangfuren(){
	if($("#bangfuren #add_bangfuren_test").attr("data-id")!=undefined&&$("#bangfuren #add_bangfuren_test").attr("data-id")!=""){
  	$.ajax({  		       
  	    url: "/assa/getSave_Personal.do",
  	    type: "POST",
  	    async:false,
  	    dataType: "text",
  	    data:{
  	    	pkid: $("#bangfuren #add_bangfuren_test").attr("data-id"),
  	    	huid: $("#shang_yi #hu_pkid").val()
          },
  	    success: function (data) {
  	    	if (data == "1") {
  	    		toastr["success"]("success", "指定新的帮扶责任人");
  	    		$("#bangfuren #add_bangfuren_test").val("");
  	    		$("#bangfuren #add_bangfuren_test").removeAttr("data-id");
  	    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
  	    		getbangfuren($("#bangfuren #sys_company_id").val());
  	    	}else{
  	    		toastr["warning"]("warning", "指定失败，检查数据后重试");
  	    	}
  	    },
  	    error: function () { 
  	    	toastr["error"]("error", "服务器异常");
  	    }  
  	});
	}
}

///解除责任人与贫困户的关系
function jiechu_bangfu(pkid){
	$.ajax({  		       
	    url: "/assa/getDel_Personal.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: pkid
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "帮扶责任人与贫困户关系解除");
	    		data_jiazai_bangfu($("#shang_yi #hu_pkid").val());
	    		getbangfuren($("#bangfuren #sys_company_id").val());
	    	}else{
	    		toastr["warning"]("warning", "解除失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//数据初始化
function data_jiazai_bangfu(id){
	$.ajax({
	    url: "/assa/getPersonal_household.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:id,
        },
	    success: function (data) {
	    	var bangfu_html = "<tr><th>姓名</th><th>单位</th><th>职务</th><th>电话</th><th>操作</th></tr>";
	    	
	    	$.each(data.bangfu,function(i,item){
	    		bangfu_html += "<tr><td>"+item.col_name+"</td><td>"+item.com_name+"</td><td>"+item.col_post+"</td><td>"+item.telephone+"</td><td class=\"client-status\">" +
	    				"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='jiechu_bangfu("+item.pkid+");' style=\"display:none;\" id=\"relieveBFZRR\"><i class=\"fa fa-remove\"></i> 解除 </button></td></tr>";
	    	});
	    	$('#bangfuren #bangfu_table').html(bangfu_html);
	    	
	    	if (typeof data.da_help_info != "undefined") {
	    		$("#tab-b-3 #v1").val(data.da_help_info.v1);
		    	$("#tab-b-3 #v2").val(data.da_help_info.v2);
		    	$("#tab-b-3 #v3").val(data.da_help_info.v3);
	    	}
	    	
	    	
	    	if (typeof data.help_info != "undefined") {
	    		$("#tab-b-2 #v2").val(data.help_info.v2);
	    		$("#tab-b-2 #v3_2016").val(data.help_info.v3_2016);
	    		$("#tab-b-2 #v3_2017").val(data.help_info.v3_2017);
	    		$("#tab-b-2 #v3_2018").val(data.help_info.v3_2018);
	    		$("#tab-b-2 #v3_2019").val(data.help_info.v3_2019);
	    		$("#tab-b-2 #v4_2016").val(data.help_info.v4_2016);
	    		$("#tab-b-2 #v4_2017").val(data.help_info.v4_2017);
	    		$("#tab-b-2 #v4_2018").val(data.help_info.v4_2018);
	    		$("#tab-b-2 #v4_2019").val(data.help_info.v4_2019);
	    		$("#tab-b-2 #v5").val(data.help_info.v5);
	    		$("#tab-b-2 #v6_2016").val(data.help_info.v6_2016);
	    		$("#tab-b-2 #v6_2017").val(data.help_info.v6_2017);
	    		$("#tab-b-2 #v6_2018").val(data.help_info.v6_2018);
	    		$("#tab-b-2 #v6_2019").val(data.help_info.v6_2019);
	    		$("#tab-b-2 #v7_2016").val(data.help_info.v7_2016);
	    		$("#tab-b-2 #v7_2017").val(data.help_info.v7_2017);
	    		$("#tab-b-2 #v7_2018").val(data.help_info.v7_2018);
	    		$("#tab-b-2 #v7_2019").val(data.help_info.v7_2019);
	    		$("#tab-b-2 #v8").val(data.help_info.v8);
	    		$("#tab-b-2 #v9_2016").val(data.help_info.v9_2016);
	    		$("#tab-b-2 #v9_2017").val(data.help_info.v9_2017);
	    		$("#tab-b-2 #v9_2018").val(data.help_info.v9_2018);
	    		$("#tab-b-2 #v9_2019").val(data.help_info.v9_2019);
	    		$("#tab-b-2 #v10_2016").val(data.help_info.v10_2016);
	    		$("#tab-b-2 #v10_2017").val(data.help_info.v10_2017);
	    		$("#tab-b-2 #v10_2018").val(data.help_info.v10_2018);
	    		$("#tab-b-2 #v10_2019").val(data.help_info.v10_2019);
	    		$("#tab-b-2 #v11").val(data.help_info.v11);
	    		$("#tab-b-2 #v12_2016").val(data.help_info.v12_2016);
	    		$("#tab-b-2 #v12_2017").val(data.help_info.v12_2017);
	    		$("#tab-b-2 #v12_2018").val(data.help_info.v12_2018);
	    		$("#tab-b-2 #v12_2019").val(data.help_info.v12_2019);
	    		$("#tab-b-2 #v13_2016").val(data.help_info.v13_2016);
	    		$("#tab-b-2 #v13_2017").val(data.help_info.v13_2017);
	    		$("#tab-b-2 #v13_2018").val(data.help_info.v13_2018);
	    		$("#tab-b-2 #v13_2019").val(data.help_info.v13_2019);
	    		$("#tab-b-2 #v14").val(data.help_info.v14);
	    		$("#tab-b-2 #v15_2016").val(data.help_info.v15_2016);
	    		$("#tab-b-2 #v15_2017").val(data.help_info.v15_2017);
	    		$("#tab-b-2 #v15_2018").val(data.help_info.v15_2018);
	    		$("#tab-b-2 #v15_2019").val(data.help_info.v15_2019);
	    		$("#tab-b-2 #v16_2016").val(data.help_info.v16_2016);
	    		$("#tab-b-2 #v16_2017").val(data.help_info.v16_2017);
	    		$("#tab-b-2 #v16_2018").val(data.help_info.v16_2018);
	    		$("#tab-b-2 #v16_2019").val(data.help_info.v16_2019);
	    		$("#tab-b-2 #v17").val(data.help_info.v17);
	    		$("#tab-b-2 #v18_2016").val(data.help_info.v18_2016);
	    		$("#tab-b-2 #v18_2017").val(data.help_info.v18_2017);
	    		$("#tab-b-2 #v18_2018").val(data.help_info.v18_2018);
	    		$("#tab-b-2 #v18_2019").val(data.help_info.v18_2019);
	    		$("#tab-b-2 #v19_2016").val(data.help_info.v19_2016);
	    		$("#tab-b-2 #v19_2017").val(data.help_info.v19_2017);
	    		$("#tab-b-2 #v19_2018").val(data.help_info.v19_2018);
	    		$("#tab-b-2 #v19_2019").val(data.help_info.v19_2019);
	    	}
	    	
//	    	$("#bangfuren #v1").html(data.help_info.v1);
//	    	$("#bangfuren #v1_bj").val(data.help_info.v1.replace(/[\r\n]/g,""));
//	    	$("#bangfuren #v2").html(data.help_info.v2);
//	    	$("#bangfuren #v2_bj").val(data.help_info.v2.replace(/[\r\n]/g,""));
//	    	$("#bangfuren #v3").html(data.help_info.v3);
//	    	$("#bangfuren #v3_bj").val(data.help_info.v3.replace(/[\r\n]/g,""));
	    	
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
}
//获取选中行数据
function getSelectedRow(form) {
    var index = form.find('tr.success').data('index');
    return form.bootstrapTable('getData')[index];
}

////数据初始化
//function data_jiazai_bangfu(id){
//	$("#bangfuren #v1").html("");
//	$("#bangfuren #v1_bj").val("");
//	$("#bangfuren #v2").html("");
//	$("#bangfuren #v2_bj").val("");
//	$("#bangfuren #v3").html("");
//	$("#bangfuren #v3_bj").val("");
//	$.ajax({
//	    url: "/assa/getPersonal_household.do",
//	    type: "POST",
//	    async:false,
//	    dataType:"json",
//	    data:{
//	    	pkid:id,
//        },
//	    success: function (data) {
//	    	var bangfu_html = "<tr><th>姓名</th><th>单位</th><th>职务</th><th>电话</th><th>操作</th></tr>";
//	    	
//	    	$.each(data.bangfu,function(i,item){
//	    		bangfu_html += "<tr><td>"+item.col_name+"</td><td>"+item.com_name+"</td><td>"+item.col_post+"</td><td>"+item.telephone+"</td><td class=\"client-status\">" +
//	    				"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='jiechu_bangfu("+item.pkid+");'><i class=\"fa fa-remove\"></i> 解除 </button></td></tr>";
//	    	});
//	    	$('#bangfuren #bangfu_table').html(bangfu_html);
//	    	
//	    	$("#bangfuren #v1").show();
//	    	$("#bangfuren #v2").show();
//	    	$("#bangfuren #v3").show();
//	    	
//	    	$("#bangfuren #v1_button").show();
//	    	$("#bangfuren #v2_button").show();
//	    	$("#bangfuren #v3_button").show();
//	    	
//	    	$("#bangfuren #v1_bj").hide();
//	    	$("#bangfuren #v2_bj").hide();
//	    	$("#bangfuren #v3_bj").hide();
//	    	
//	    	$("#bangfuren #v1_bc").hide();
//	    	$("#bangfuren #v2_bc").hide();
//	    	$("#bangfuren #v3_bc").hide();
//	    	
//	    	$("#bangfuren #v1").html(data.help_info.v1);
//	    	$("#bangfuren #v1_bj").val(data.help_info.v1.replace(/[\r\n]/g,""));
//	    	$("#bangfuren #v2").html(data.help_info.v2);
//	    	$("#bangfuren #v2_bj").val(data.help_info.v2.replace(/[\r\n]/g,""));
//	    	$("#bangfuren #v3").html(data.help_info.v3);
//	    	$("#bangfuren #v3_bj").val(data.help_info.v3.replace(/[\r\n]/g,""));
//	    	
//	    },
//	    error: function () { 
//	    	toastr["error"]("error", "服务器异常");
//	    }  
//	
//	});
//}




