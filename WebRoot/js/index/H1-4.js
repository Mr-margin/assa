$(function () {
	//修改表格信息
	$("#updateModal_tz_cuoshi #save").click(function () {
		var form_val = JSON.stringify(getFormJson("#cuoshi_tz_Form"));//表单数据字符串
		$.ajax({  		       
		    url: "/assa/getUpdate_tz_cuoshi.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#shang_yi #hu_pkid").val(),
		    	form_val: form_val
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "修改帮扶措施");
		    		$("#updateModal_tz_cuoshi #close").click();
		    		cuoshi_tz_table.bootstrapTable('destroy');
		    		cuoshi_initialization();
		    	}else{
		    		toastr["warning"]("warning", "修改失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		});
	});
	
	//保存新台账措施
	$("#addModal_tz_cuoshi #save").click(function () {
		var form_val = JSON.stringify(getFormJson("#add_cuoshi_tz_Form"));//表单数据字符串
		$.ajax({  		       
		    url: "/assa/getSave_tz_cuoshi.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#shang_yi #hu_pkid").val(),
		    	form_val: form_val
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "保存帮扶措施");
		    		$("#addModal_tz_cuoshi #close").click();
		    		cuoshi_tz_table.bootstrapTable('destroy');
		    		cuoshi_initialization();
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

//删除台账帮扶措施
function del_tz(v1,v2,pkid){
	$.ajax({  		       
	    url: "/assa/getdel_tz_cuoshi.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: pkid,
	    	v1: v1,
	    	v2: v2
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "删除帮扶措施");
	    		cuoshi_tz_table.bootstrapTable('destroy');
	    		cuoshi_initialization();
	    	}else{
	    		toastr["warning"]("warning", "删除失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//打开台账帮扶措施新建页面
function show_add_tz_cuoshi(){
	$.ajax({
	    url: "/assa/get_tz_cuoshi_tiaomu.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val()
        },
	    success: function (data) {
	    	var tiaomu = '';
	    	
	    	if(data!=""){
	    		$.each(data,function(i,item){
	    			tiaomu+='<option>'+item+'</option>';
		    	});
	    		Refresh_actual_add();
	    		$("#addModal_tz_cuoshi #v1").html(tiaomu);
	    		$("#addModal_tz_cuoshi").modal();
	    	}else{
	    		toastr["warning"]("warning", "删除失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    }  
	
	});
}

//帮扶措施
function bfcs(id){
	$("#tab_jbqk").hide();//基本情况
	$("#tab_dqszh").hide();//当前收支
	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
	$("#tab_bfcsh").show();//帮扶措施
	$("#tab_zfqk").hide();//走访情况
	$("#tab_bfcx").hide();//帮扶成效
	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
	
	document.getElementById('tab_bfcsh').scrollIntoView();
	
//	$('#bangfucuoshi .blueimp-gallery').attr('id','blueimp-gallery');
//	$('#zoufangqingkaung .blueimp-gallery').attr('id','blueimp-gallery-bak');
//	$('#bangfuchengxiao .blueimp-gallery').attr('id','blueimp-gallery-bak');
//	$('#yidifupinbanqian .blueimp-gallery').attr('id','blueimp-gallery-bak');
//	$('#yidifupinbanqian #poht_list').html("");
//	$('#bangfucuoshi #poht_list').html("");
//	$('#zoufangqingkaung #poht_list').html("");
//	$('#bangfuchengxiao #poht_list').html("");
	
//	$("#updateModal_tz_cuoshi .input-group.date").datepicker({
//        todayBtn: "linked",
//        keyboardNavigation: !1,
//        forceParse: !1,
//        calendarWeeks: !0,
//        autoclose: !0
//    }),
	$("#shang_yi #hu_pkid").val(id);//记录户主ID
	//data_jiazai_cuoshi(id);//数据初始化
	cuoshi_tz_table.bootstrapTable('destroy');
	cuoshi_initialization();
	
}
var cuoshi_tz_table = $('#cuoshi_tz_table');

//帮扶人初始化
function cuoshi_initialization(){

	cuoshi_tz_table.bootstrapTable({
		method: 'POST',
		//height: "597",
		url: "/assa/getcuoshi_tz_info.do",
		dataType: "json",//从服务器返回的数据类型
		striped: true,	 //使表格带有条纹
		detailView: true, //显示详细页面 
		detailFormatter:detailFormatter,//格式化详细页面模式的视图
		queryParams: queryParams_cuoshi, //参数
		queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
		silent: true,  //刷新事件必须设置
		contentType: "application/x-www-form-urlencoded",	//请求远程数据的内容类型。
		onLoadError: function (data) {
			cuoshi_tz_table.bootstrapTable('removeAll');
			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		}
	});
}
//格式化详细页面模式的视图
function detailFormatter(index, row) {
//    var html = [];
//    $.each(row, function (key, value) {
//        html.push('<p><b>' + key + ':</b> ' + value + '</p>');
//    });
    
    var html = "<table style=\"padding:20px\"><tr><td>2016年</td><td>项目需求量："+row.v4_2016+"</td><td>受益资金/政策："+row.v5_2016+"</td><td>落实时间："+row.v6_2016+"</td></tr>" +
    		"<tr><td>2017年</td><td>项目需求量："+row.v4_2017+"</td><td>受益资金/政策："+row.v5_2017+"</td><td>落实时间："+row.v6_2017+"</td></tr>" +
    		"<tr><td>2018年</td><td>项目需求量："+row.v4_2018+"</td><td>受益资金/政策："+row.v5_2018+"</td><td>落实时间："+row.v6_2018+"</td></tr>" +
    		"<tr><td>2019年</td><td>项目需求量："+row.v4_2019+"</td><td>受益资金/政策："+row.v5_2019+"</td><td>落实时间："+row.v6_2019+"</td></tr></table>";
    return html;
}



function queryParams_cuoshi(params) {  //配置参数
	var temp = {};
    temp.pkid = $("#shang_yi #hu_pkid").val();
    return temp;
}

//编辑台账帮扶措施
function update_tz_cuoshi(v1,v2,v3,v4_2016,v5_2016,v6_2016,v4_2017,v5_2017,v6_2017,v4_2018,v5_2018,v6_2018,v4_2019,v5_2019,v6_2019){
	Refresh_actual();
	
	$("#updateModal_tz_cuoshi #cuoshi_title").html(v1+'-'+v2);
	
	$("#updateModal_tz_cuoshi #v1").val(v1);
	$("#updateModal_tz_cuoshi #v2").val(v2);
	
	if(v3==''){
		$("#updateModal_tz_cuoshi #v3_div input[type=radio]").removeAttr("checked");
		$("#updateModal_tz_cuoshi #v3_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	}else{
		$("#updateModal_tz_cuoshi #v3_div input[name='v3'][value='"+v3+"']").prop("checked","true");
		$("#updateModal_tz_cuoshi #v3_div input[name='v3'][value='"+v3+"']").parent(".iradio_square-green").addClass("checked");
	}
	
	$("#updateModal_tz_cuoshi #v4_2016").val(v4_2016);
	$("#updateModal_tz_cuoshi #v5_2016").val(v5_2016);
	$("#updateModal_tz_cuoshi #v6_2016").val(v6_2016);
	
	$("#updateModal_tz_cuoshi #v4_2017").val(v4_2017);
	$("#updateModal_tz_cuoshi #v5_2017").val(v5_2017);
	$("#updateModal_tz_cuoshi #v6_2017").val(v6_2017);
	
	$("#updateModal_tz_cuoshi #v4_2018").val(v4_2018);
	$("#updateModal_tz_cuoshi #v5_2018").val(v5_2018);
	$("#updateModal_tz_cuoshi #v6_2018").val(v6_2018);
	
	$("#updateModal_tz_cuoshi #v4_2019").val(v4_2019);
	$("#updateModal_tz_cuoshi #v5_2019").val(v5_2019);
	$("#updateModal_tz_cuoshi #v6_2019").val(v6_2019);
	
	$("#updateModal_tz_cuoshi").modal();
	
}

function Refresh_actual(){
	$("#cuoshi_tz_Form").find("input").each(function(){
		  var id = $(this).attr("id");
		  if(id!="v3"){
			  $("#cuoshi_tz_Form #"+id).val("");
		  }
		  
	});
	$("#cuoshi_tz_Form input[type=radio]").removeAttr("checked");
	$("#cuoshi_tz_Form input[type=radio]").parent(".iradio_square-green").removeClass("checked");
}

function Refresh_actual_add(){
	$("#add_cuoshi_tz_Form").find("input").each(function(){
		  var id = $(this).attr("id");
		  if(id!="v3"){
			  $("#add_cuoshi_tz_Form #"+id).val("");
		  }
	});
	$("#add_cuoshi_tz_Form input[type=radio]").removeAttr("checked");
	$("#add_cuoshi_tz_Form input[type=radio]").parent(".iradio_square-green").removeClass("checked");
}







//初始化数据
function data_jiazai_cuoshi(pkid){
	$("#bangfucuoshi #v1_bc").hide();//保存按钮
	$("#bangfucuoshi #v1_tj").show();//编辑按钮
	$("#bangfucuoshi #new_cuoshi").show();
	$("#bangfucuoshi #poht_list_div").hide();
	$("#bangfucuoshi #webup_div").hide();
	
	$("#bangfucuoshi #v1").val("");
	$("#bangfucuoshi #v2").val("");
	$("#bangfucuoshi #v3").val("");
	
	$.ajax({
	    url: "/assa/getcuoshi_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
	    	type:1
        },
	    success: function (data) {
	    	//表格数据
	    	var cuoshi_html = "<tr><th>时间</th><th style=\"width:200px\">项目内容</th><th style=\"width:200px\">帮扶单位</th><th style=\"text-align:center;\">照片</th><th style=\"text-align:center;\">操作</th></tr>";
	    	$.each(data.table,function(i,item){
	    		item.v1 = item.v1.replace(/[\r\n]/g,"");
	    		item.v2 = item.v2.replace(/[\r\n]/g,"");
	    		item.v3 = item.v3.replace(/[\r\n]/g,"");
	    		var vls = item.v2;
	    		if(item.v2.length>20){
	    			var k = 0;
		    		for(var i = 0;i<item.v2.length;){
		    			vls = vls.substring(0,k+20)+"<br>"+vls.substring(k+20);
		    			k += 24;
		    			i += 20;
		    		}
	    		}
	    		
	    		cuoshi_html += "<tr id=\"cuoshi_table_"+item.pkid+"\"><td>"+item.v1+"</td><td>"+vls+"</td><td>"+item.v3+"</td>";
//	    		if(item.pic_num==0){
//	    			cuoshi_html += "<td class=\"client-status\"><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='cuoshi_pic_load(1,"+item.pkid+");'>上传</button></td>";
//	    		}else{
//	    			cuoshi_html += "<td class=\"client-status\"><a onClick=\"cuoshi_pic_show("+item.pkid+");\">查看</a>  " +
//							"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='cuoshi_pic_load(1,"+item.pkid+");'>上传</button></td>";
//	    			
	    			cuoshi_html += "<td  style=\"text-align:center;\" class=\"client-status\"><button type=\"button\" class=\"btn btn-primary btn-xs\" onClick=\"cuoshi_pic_show("+item.pkid+");\"><i class=\"fa fa-search\"></i> 查看 </button>  " +
							"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='cuoshi_pic_load(1,"+item.pkid+");'><i class=\"fa fa-upload\"></i> 上传 </button></td>";
	    		//}
	    		cuoshi_html += "<td  style=\"text-align:center;\" class=\"client-status\"><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"update_cuoshi('"+item.pkid+"','"+item.v1+"','"+item.v2+"','"+item.v3+"');\"><i class=\"fa fa-pencil\"></i> 修改 </button>   " +
	    				"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='del_cuoshi("+item.pkid+");'><i class=\"fa fa-remove\"></i> 删除 </button></td></tr>";
	    	});
	    	$("#bangfucuoshi #cuoshi_table").html(cuoshi_html);
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
}

function del_cuoshi(pkid){
	$.ajax({  		       
	    url: "/assa/getdel_cuoshi.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: pkid,
	    	type:1
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "删除帮扶措施");
	    		data_jiazai_cuoshi($("#shang_yi #hu_pkid").val());
	    	}else{
	    		toastr["warning"]("warning", "删除失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}


//修改表格信息
function update_cuoshi(pkid,v1,v2,v3){
	$("#bangfucuoshi #cuoshi_table_"+pkid).addClass("success").siblings().removeClass("success");
	
	$("#bangfucuoshi #v1_bc").show();//保存按钮
	$("#bangfucuoshi #v1_tj").hide();//编辑按钮
	$("#bangfucuoshi #new_cuoshi").show();
	$("#bangfucuoshi #poht_list_div").hide();
	$("#bangfucuoshi #webup_div").hide();
	
	$("#bangfucuoshi #cuoshi_pkid").val(pkid);
	$("#bangfucuoshi #v1").val(v1);
	$("#bangfucuoshi #v2").val(v2);
	$("#bangfucuoshi #v3").val(v3);
}

//查看图片
function cuoshi_pic_show(pkid){
	$("#bangfucuoshi #cuoshi_table_"+pkid).addClass("success").siblings().removeClass("success");
	
	$("#bangfucuoshi #new_cuoshi").hide();
	$("#bangfucuoshi #poht_list_div").show();
	$("#bangfucuoshi #webup_div").hide();
	
	$.ajax({
	    url: "/assa/getPic_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
	    	type:1
        },
	    success: function (data) {
	    	//现有图片
	    	var tupian_html = "";
	    	$.each(data.pic,function(i,item){
	    		tupian_html += "<li id=\"pin_li_"+item.pkid+"\"><p class=\"imgWrap\"><a href=\""+item.pic_path+"\" title=\"帮扶措施图片\" data-gallery=\"\">" +
	    				"<img src=\""+item.pic_path+"\" style=\"margin:0;vertical-align:baseline;width:130px;height:85px;\"></a></p>" +
	    				"<div id=\"pin_del_"+item.pkid+"\" class=\"file-panel\" style=\"height: 0px;\"><span class=\"cancel\" onclick='pic_del("+item.pkid+","+pkid+");'>删除</span></div></li>";
	    	});
	    	
	    	$('#bangfucuoshi #poht_list').html(tupian_html);//要先循环一遍加上html后 再循环一遍，添加事件
	    	$.each(data.pic,function(i,item){
	    		$("#bangfucuoshi #pin_li_"+item.pkid).mouseenter(function(){  
	    			$("#bangfucuoshi #pin_del_"+item.pkid).stop().animate({
		            	height: 30
		            });
	    		}); 
	    		
	    		$("#bangfucuoshi #pin_li_"+item.pkid).mouseleave(function(){
	    			$("#bangfucuoshi #pin_del_"+item.pkid).stop().animate({
			                height: 0
			        });
			    });
	    	});
	    	
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

function pic_del(pkid,fid){
	$.ajax({  		       
	    url: "/assa/getdel_poth.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: pkid
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "删除帮扶措施图片");
	    		cuoshi_pic_show(fid);
	    	}else{
	    		toastr["warning"]("warning", "删除失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}


//初始化上传组件
function cuoshi_pic_load(type,id){
	
	$("#bangfucuoshi #cuoshi_table_"+id).addClass("success").siblings().removeClass("success");
	
	$("#bangfucuoshi #new_cuoshi").hide();
	$("#bangfucuoshi #poht_list_div").hide();
	$("#bangfucuoshi #webup_div").show();
	
	//照片上传初始化
	$("#bangfucuoshi #cuoshi_img_load").html("<div class=\"page-container\"><p>您可以尝试文件拖拽，使用QQ截屏工具，然后激活窗口后粘贴，或者点击添加图片按钮。支持：gif,jpg,jpeg,png,bmp。</p><div id=\"uploader\" class=\"wu-example\">" +
			"<div class=\"queueList\"><div id=\"dndArea\" class=\"placeholder\"><div id=\"filePicker\"></div><p>或将照片拖到这里，单次最多可选10张，单张照片限10M，总共限20M</p></div>" +
			"</div><div class=\"statusBar\" style=\"display:none;\"><div class=\"progress\"><span class=\"text\">0%</span><span class=\"percentage\"></span></div>" +
			"<div class=\"info\"></div><div class=\"btns\"><div id=\"filePicker2\"></div><div class=\"uploadBtn\">开始上传</div></div></div></div></div>");
	Img_load('1',id,'bangfucuoshi');
}


