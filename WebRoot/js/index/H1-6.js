$(function () {
	//保存新措施
	$("#bangfuchengxiao #v1_tj_zoufang").click(function () {
		$.ajax({  		       
		    url: "/assa/getSave_cuoshi.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#shang_yi #hu_pkid").val(),
		    	v1 : $("#bangfuchengxiao #v1").val(),
		    	v2 : $("#bangfuchengxiao #v2").val(),
		    	v3 : $("#bangfuchengxiao #v3").val(),
		    	type : 3
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "保存帮扶成效");
		    		data_jiazai_chengxiao($("#shang_yi #hu_pkid").val());
		    	}else{
		    		toastr["warning"]("warning", "保存失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		});
	});
	
	//修改表格信息
	$("#bangfuchengxiao #v1_bc_zoufang").click(function () {
		$.ajax({  		       
		    url: "/assa/getUpdate_cuoshi.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#bangfuchengxiao #cuoshi_pkid").val(),
		    	v1 : $("#bangfuchengxiao #v1").val(),
		    	v2 : $("#bangfuchengxiao #v2").val(),
		    	v3 : $("#bangfuchengxiao #v3").val(),
		    	type : 3
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "修改帮扶成效");
		    		data_jiazai_chengxiao($("#shang_yi #hu_pkid").val());
		    	}else{
		    		toastr["warning"]("warning", "修改失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		});
	});
});


//帮扶成效
function bfcx(id){
	$("#tab_jbqk").hide();//基本情况
	$("#tab_dqszh").hide();//当前收支
	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
	$("#tab_bfcsh").hide();//帮扶措施
	$("#tab_zfqk").hide();//帮扶成效
	$("#tab_bfcx").show();//帮扶成效
	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
	
	document.getElementById('tab_bfcx').scrollIntoView();
	
//	$('#bangfucuoshi .blueimp-gallery').attr('id','blueimp-gallery-bak');
	$('#zoufangqingkaung .blueimp-gallery').attr('id','blueimp-gallery-bak');
	$('#bangfuchengxiao .blueimp-gallery').attr('id','blueimp-gallery');
	$('#yidifupinbanqian .blueimp-gallery').attr('id','blueimp-gallery-bak');
	$('#yidifupinbanqian #poht_list').html("");
//	$('#bangfucuoshi #poht_list').html("");
	$('#zoufangqingkaung #poht_list').html("");
	$('#bangfuchengxiao #poht_list').html("");
	
	$("#bangfuchengxiao #data_1 .input-group.date").datepicker({
        todayBtn: "linked",
        keyboardNavigation: !1,
        forceParse: !1,
        calendarWeeks: !0,
        autoclose: !0
    }),
	
	$("#shang_yi #hu_pkid").val(id);//记录户主ID
	data_jiazai_chengxiao(id);//数据初始化
}


//初始化数据
function data_jiazai_chengxiao(pkid){
	$("#bangfuchengxiao #v1_bc_zoufang").hide();//保存按钮
	$("#bangfuchengxiao #v1_tj_zoufang").show();//编辑按钮
	$("#bangfuchengxiao #new_cuoshi").show();
	$("#bangfuchengxiao #poht_list_div").hide();
	$("#bangfuchengxiao #webup_div").hide();
	
	$("#bangfuchengxiao #v1").val("");
	$("#bangfuchengxiao #v2").val("");
	$("#bangfuchengxiao #v3").val("");
	
	$.ajax({
	    url: "/assa/getcuoshi_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
	    	type:3
        },
	    success: function (data) {
	    	//表格数据
	    	var cuoshi_html = "<tr><th>时间</th><th style=\"width:250px\">成效内容</th><th style=\"width:150px\">贫困户签字</th><th style=\"text-align:center;\">照片</th><th style=\"text-align:center;\">操作</th></tr>";
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
	    		cuoshi_html += "<tr id=\"bangfu_table_"+item.pkid+"\"><td>"+item.v1+"</td><td>"+vls+"</td><td>"+item.v3+"</td>";
//	    		if(item.pic_num==0){
//	    			cuoshi_html += "<td class=\"client-status\"><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='chengxiao_pic_load(1,"+item.pkid+");'>上传</button></td>";
//	    		}else{
//	    			cuoshi_html += "<td class=\"client-status\"><a onClick=\"chengxiao_pic_show("+item.pkid+");\">查看</a>  " +
//							"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='chengxiao_pic_load(1,"+item.pkid+");'>上传</button></td>";
//	    			
	    			cuoshi_html += "<td  style=\"text-align:center;\" class=\"client-status\"><button type=\"button\" class=\"btn btn-primary btn-xs\" onClick=\"chengxiao_pic_show("+item.pkid+");\"><i class=\"fa fa-search\"></i> 查看 </button>  " +
							"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='chengxiao_pic_load(3,"+item.pkid+");'><i class=\"fa fa-upload\"></i> 上传 </button></td>";
	    		//}
	    		cuoshi_html += "<td  style=\"text-align:center;\" class=\"client-status\"><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"update_chengxiao('"+item.pkid+"','"+item.v1+"','"+item.v2+"','"+item.v3+"');\"><i class=\"fa fa-pencil\"></i> 修改 </button>   " +
	    				"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick='del_chengxiao("+item.pkid+");'><i class=\"fa fa-remove\"></i> 删除 </button></td></tr>";
	    	});
	    	$("#bangfuchengxiao #cuoshi_table").html(cuoshi_html);
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
}

function del_chengxiao(pkid){
	$.ajax({  		       
	    url: "/assa/getdel_cuoshi.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: pkid,
	    	type:3
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "删除帮扶成效");
	    		data_jiazai_chengxiao($("#shang_yi #hu_pkid").val());
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
function update_chengxiao(pkid,v1,v2,v3){
	$("#bangfuchengxiao #bangfu_table_"+pkid).addClass("success").siblings().removeClass("success"); 
	
	$("#bangfuchengxiao #v1_bc_zoufang").show();//保存按钮
	$("#bangfuchengxiao #v1_tj_zoufang").hide();//编辑按钮
	$("#bangfuchengxiao #new_cuoshi").show();
	$("#bangfuchengxiao #poht_list_div").hide();
	$("#bangfuchengxiao #webup_div").hide();
	
	$("#bangfuchengxiao #cuoshi_pkid").val(pkid);
	$("#bangfuchengxiao #v1").val(v1);
	$("#bangfuchengxiao #v2").val(v2);
	$("#bangfuchengxiao #v3").val(v3);
}

//查看图片
function chengxiao_pic_show(pkid){
	$("#bangfuchengxiao #bangfu_table_"+pkid).addClass("success").siblings().removeClass("success"); 
	
	$("#bangfuchengxiao #new_cuoshi").hide();
	$("#bangfuchengxiao #poht_list_div").show();
	$("#bangfuchengxiao #webup_div").hide();
	
	$.ajax({
	    url: "/assa/getPic_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
	    	type:3
        },
	    success: function (data) {
	    	//现有图片
	    	var tupian_html = "";
	    	$.each(data.pic,function(i,item){
	    		tupian_html += "<li id=\"pin_li_"+item.pkid+"\"><p class=\"imgWrap\"><a href=\""+item.pic_path+"\" title=\"帮扶成效图片\" data-gallery=\"\">" +
	    				"<img src=\""+item.pic_path+"\" style=\"margin:0;vertical-align:baseline;width:130px;height:85px;\"></a></p>" +
	    				"<div id=\"pin_del_"+item.pkid+"\" class=\"file-panel\" style=\"height: 0px;\"><span class=\"cancel\" onclick='pic_del_chengxiao("+item.pkid+","+pkid+");'>删除</span></div></li>";
	    	});
	    	
	    	$('#bangfuchengxiao #poht_list').html(tupian_html);//要先循环一遍加上html后 再循环一遍，添加事件
	    	$.each(data.pic,function(i,item){
	    		$("#bangfuchengxiao #pin_li_"+item.pkid).mouseenter(function(){  
	    			$("#bangfuchengxiao #pin_del_"+item.pkid).stop().animate({
		            	height: 30
		            });
	    		}); 
	    		
	    		$("#bangfuchengxiao #pin_li_"+item.pkid).mouseleave(function(){
	    			$("#bangfuchengxiao #pin_del_"+item.pkid).stop().animate({
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

function pic_del_chengxiao(pkid,fid){
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
	    		chengxiao_pic_show(fid);
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
function chengxiao_pic_load(type,id){
	$("#bangfuchengxiao #bangfu_table_"+id).addClass("success").siblings().removeClass("success");
	
	$("#bangfuchengxiao #new_cuoshi").hide();
	$("#bangfuchengxiao #poht_list_div").hide();
	$("#bangfuchengxiao #webup_div").show();
	
	//照片上传初始化
	$("#bangfuchengxiao #cuoshi_img_load").html("<div class=\"page-container\"><p>您可以尝试文件拖拽，使用QQ截屏工具，然后激活窗口后粘贴，或者点击添加图片按钮。支持：gif,jpg,jpeg,png,bmp。</p><div id=\"uploader\" class=\"wu-example\">" +
			"<div class=\"queueList\"><div id=\"dndArea\" class=\"placeholder\"><div id=\"filePicker\"></div><p>或将照片拖到这里，单次最多可选10张，单张照片限10M，总共限20M</p></div>" +
			"</div><div class=\"statusBar\" style=\"display:none;\"><div class=\"progress\"><span class=\"text\">0%</span><span class=\"percentage\"></span></div>" +
			"<div class=\"info\"></div><div class=\"btns\"><div id=\"filePicker2\"></div><div class=\"uploadBtn\">开始上传</div></div></div></div></div>");
	Img_load('3',id,'bangfuchengxiao');
}


