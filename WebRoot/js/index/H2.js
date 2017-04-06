var app={};
var cuoshi_tz_table = $('#cuoshi_tz_table');
var dy_bfcs = $('#dy_bfcs');
var da_household_id;
$(document).ready(function() {
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式
	$("#gundongzhou").slimScroll({height:"500px"});//时间轴滚动条的高度
	$("#jiatingchengyuan .contact-box").each(function(){animationHover(this,"pulse")});
	getinfo_ready($('#cha_mz'));
	getInfo_ready_zhipinyuanyin($('#cha_zpyy'));
	$('#exportExcel_all_dengdai').hide();
	
	if(jsondata.company_map.com_type=="单位"){
		var type = jsondata.company_map.com_type;
		var val = jsondata.company;
		var qixian;
		if(val.com_level == "1"){
			getinfo_xiqian($('#cha_qx'));
		}else if(val.com_level == "2"){
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			getinfo_xiang(jsondata.company.xian,$('#cha_smx'));
			chaxun.cha_qx = jsondata.company.xian;
		}else if(val.com_level == "3"){
			$('#cha_qx').html('<option value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			getinfo_cun(jsondata.company.xiang,$('#cha_gcc'));
			chaxun.cha_qx = jsondata.company.xian;
			chaxun.cha_smx = jsondata.company.xiang;
		}else if(val.com_level == "4"){
			$('#cha_qx').html('<option  value="'+jsondata.company.xian+'">'+jsondata.company.xian+'</option>');
			$("#cha_smx").html('<option value="'+jsondata.company.xiang+'">'+jsondata.company.xiang+'</option>');
			$("#cha_gcc").html('<option value="'+jsondata.company.cun+'">'+jsondata.company.cun+'</option>');
			chaxun.cha_qx = jsondata.company.xian;
			chaxun.cha_smx = jsondata.company.xiang;
			chaxun.cha_gcc = jsondata.company.cun;
		}
	}else if(jsondata.company_map.com_type=="管理员"){
		getinfo_xiqian($('#cha_qx'));
	}else{
		$("#chauxn_quanxian").hide();
	}
	
});
var metTable_bxbxxb = $('#metTable_bxbxxb');
var chaxun = {};//存储表格查询参数
var number;
var tjian;
$(function () {
	//导出
	$("#daochu").click(function(){
		var table=$(".content-head table")[0];
		var  head_arr=[];
		var type_arr=[];//每一列的类型(1:整型，2：字符型，3：浮点型)
		if(app.index==1){
			head_arr=[];
			type_arr=[2,2,2,2,2,2,
						2,2,2,2];
		}else{
			var tab=$(".content-head table")[0];
			
			for ( var i = 0; i < tab.rows[0].cells.length; i++) {
				var cell=tab.rows[0].cells[i];
				if(i!=0&&i!=tab.rows[0].cells.length-1){
					if(!$(cell).find("a").html()){
						head_arr.push(cell.innerHTML);
					}else{
					head_arr.push($(cell).find("a").html().substring(0,
							$(cell).find("a").html().indexOf('<span')));
					}
					if(i==1){
						type_arr.push(2);
					}else{
						type_arr.push(2);
					}
					
				}else if(i==0){
//					head_arr.push("编号");
					type_arr.push(2);
				}
			}
		}
		
		exportExcel(table,head_arr,type_arr,type_arr);
	});
	//基本情况页签
	$("#yeqian .active").click(function(){
		$('#tab-1 .blueimp-gallery').attr('id','blueimp-gallery');
		$('#tab-3 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-4 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-6 .blueimp-gallery').attr('id','blueimp-gallery-bak');
	});
	//帮扶人页签
	$("#yeqian .active2").click(function(){
		$('#tab-1 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-3 .blueimp-gallery').attr('id','blueimp-gallery');
		$('#tab-4 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-6 .blueimp-gallery').attr('id','blueimp-gallery-bak');
	});
	//帮扶措施页签
	$("#yeqian .active3").click(function(){
		$('#tab-1 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-3 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-4 .blueimp-gallery').attr('id','blueimp-gallery');
		$('#tab-6 .blueimp-gallery').attr('id','blueimp-gallery-bak');
	});
	//异地搬迁页签
	$("#yeqian .active5").click(function(){
		$('#tab-1 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-3 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-4 .blueimp-gallery').attr('id','blueimp-gallery-bak');
		$('#tab-6 .blueimp-gallery').attr('id','blueimp-gallery');
	});
	//时间轴的显示
	$("#show_shijz").click(function(){
		$("#timeLine_show").show();
		$("#show_table").hide();
		$("#show_shijz").removeAttr("class");
		this.className='btn btn-primary btn-xs';
		$("#show_biaoge").attr("class","btn btn-outline btn-primary btn-xs");
	});
	//表格的显示
	$("#show_biaoge").click(function(){
		$("#timeLine_show").hide();
		$("#show_table").show();
		$("#show_biaoge").removeAttr("class");
		this.className='btn btn-primary btn-xs';
		$("#show_shijz").attr("class","btn btn-outline btn-primary btn-xs");
	});
	
	//时间轴中的复选框
	$(".iCheck-helper").click(function(){
		var type=this;
		$(".iCheck-helper").each(function(i){
			if(this==type){
				app.inx=i;
			}
		});
		if(app.inx==0 || app.inx==undefined){
			//当前收入的饼图、折线图、柱状图
			$("#show_dqsrPie").show();
			$("#show_dqsrline").hide();
			$("#show_dqsrbar").hide();
			//当前支出饼图、折线图、柱状图
			$("#show_dqzcPie").show();
			$("#show_dqzclin").hide();
			$("#show_dqzcbar").hide();
			
			
		}else if(app.inx==1){
			//折线图的显示
			$("#show_dqsrPie").hide();
			$("#show_dqsrline").show();
			$("#show_dqsrbar").hide();
			
			$("#show_dqzcPie").hide();
			$("#show_dqzclin").show();
			$("#show_dqzcbar").hide();
		
		}else if(app.inx==2){
			//柱状图的显示
			$("#show_dqsrPie").hide();
			$("#show_dqsrline").hide();
			$("#show_dqsrbar").show();
			
			$("#show_dqzcPie").hide();
			$("#show_dqzclin").hide();
			$("#show_dqzcbar").show();
			
		}else if(app.inx==3){
			/*时间轴中的复选框选择*/
			
			if ($("#show_checked input[value='走访情况']").parent(".icheckbox_square-green").hasClass('checked')) {
	            $("#show_checked input[value='走访情况']").attr("checked","");
			}else{
				$("#show_checked input[value='走访情况']").removeAttr("checked");
			}
			//处理时间轴
			handle_shijianzhou();
			
		}else if(app.inx==4){
			if ($("#show_checked input[value='帮扶措施']").parent(".icheckbox_square-green").hasClass('checked')) {
	            $("#show_checked input[value='帮扶措施']").attr("checked","");
			}else{
				$("#show_checked input[value='帮扶措施']").removeAttr("checked");
			}
			//处理时间轴
			handle_shijianzhou();
				
		}else if(app.inx==5){
			if ($("#show_checked input[value='帮扶成效']").parent(".icheckbox_square-green").hasClass('checked')) {
	            $("#show_checked input[value='帮扶成效']").attr("checked","");
			}else{
				$("#show_checked input[value='帮扶成效']").removeAttr("checked");
			}
			//处理时间轴
			handle_shijianzhou();
			
		}else if(app.inx==6){
			//帮扶后收支的饼图折线图、折线图、柱状图
			$("#show_bfhsz").show();
			$("#show_bfhszline").hide();
			$("#show_bfhszbar").hide();
			//帮扶后支出的饼图、柱状图、折线图
			$("#show_bfhzc").show();
			$("#show_bfhzcline").hide();
			$("#show_bfhzcbar").hide();
			
		}else if(app.inx==7){
			//折线图的显示
			$("#show_bfhsz").hide();
			$("#show_bfhszline").show();
			$("#show_bfhszbar").hide();
			
			$("#show_bfhzc").hide();
			$("#show_bfhzcline").show();
			$("#show_bfhzcbar").hide();
			
		}else if(app.inx==8){
//			柱状图的显示
			$("#show_bfhsz").hide();
			$("#show_bfhszline").hide();
			$("#show_bfhszbar").show();
			
			$("#show_bfhzc").hide();
			$("#show_bfhzcline").hide();
			$("#show_bfhzcbar").show();
			
		}
	});
	
	//当前收支显示表格
	$("#show_pie_bg").click(function(){
		$("#show_dq_biaog").show();
		$("#show_pies").hide();
		$("#show_pie_bg").removeAttr("class");
		this.className='btn btn-primary btn-xs';
		$("#show_pie_tongji").attr("class","btn btn-outline btn-primary btn-xs");

	});
	//统计图
	$("#show_pie_tongji").click(function(){
		$("#show_dq_biaog").hide();
		$("#show_pies").show();
		$("#show_pie_tongji").removeAttr("class");
		this.className='btn btn-primary btn-xs';
		$("#show_pie_bg").attr("class","btn btn-outline btn-primary btn-xs");

	});
	//帮扶后收支显示表格
	$("#show_bfh_bg").click(function(){
		$("#show_bfhpies").hide();
		$("#show_bfhbg").show();
		$("#show_bfh_bg").removeAttr("class");
		this.className='btn btn-primary btn-xs';
		$("#show_bfh_tjt").attr("class","btn btn-outline btn-primary btn-xs");

		
	});
	//帮扶后统计图
	$("#show_bfh_tjt").click(function(){
		$("#show_bfhpies").show();
		$("#show_bfhbg").hide();
		$("#show_bfh_tjt").removeAttr("class");
		this.className='btn btn-primary btn-xs';
		$("#show_bfh_bg").attr("class","btn btn-outline btn-primary btn-xs");

	});
	//查询按钮
    $('#cha_button').click(function () {
    	chaxun.cha_qx = $("#cha_qx").val();
    	chaxun.cha_smx = $("#cha_smx").val();
    	chaxun.cha_gcc = $("#cha_gcc").val();
    	chaxun.cha_sbbz = $("#cha_sbbz").val();
    	chaxun.cha_pksx = $("#cha_pksx").val();
    	chaxun.cha_zpyy = $("#cha_zpyy").val();
    	chaxun.cha_mz = $("#cha_mz").val();
    	chaxun.cha_renkou = $("#cha_renkou").val();
    	chaxun.cha_bfdw = $("#cha_bfdw").val();
    	chaxun.cha_bfzrr = $("#cha_bfzrr").val();
    	chaxun.cha_banqian = $("#cha_banqian").val();
    	chaxun.cha_v6 = $("#cha_v6").val();
    	chaxun.cha_v8 = $("#cha_v8").val();
    	chaxun.cha_v8_1 = $("#cha_v8_1").val();//年龄范围开始时间
    	chaxun.cha_v8_2 = $("#cha_v8_2").val();//年龄范围截止时间
    	chaxun.cha_year = $("#cha_year").val();
    	$("#yeqian").hide();
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
    });
    //导出基本信息
    $('#exportExcel_all').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    //导出家庭成员
    $('#exportExcel_all1').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all1.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    
    //导出当前收支分析
    $('#exportExcel_all2').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all2.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    
    
    //导出帮扶单位和帮扶人
    $('#exportExcel_all3').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all3.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    
    
    //帮扶人走访记录和帮扶措施
    $('#exportExcel_all4').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all4.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    //导出帮扶成效
    $('#exportExcel_all5').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all5.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    //帮扶后收支
    $('#exportExcel_all6').click(function () {
    	$.ajax({  		       
    	    url: "/assa/exportExcel_all6.do",
    	    type: "POST",
    	    async:true,
    	    dataType: "json",
    	    data:{cha_qx:$("#cha_qx").val(),cha_smx:$("#cha_smx").val(),cha_gcc:$("#cha_gcc").val(),cha_sbbz:$("#cha_sbbz").val(),cha_pksx:$("#cha_pksx").val(),
    	    	cha_zpyy:$("#cha_zpyy").val(),cha_mz:$("#cha_mz").val(),cha_renkou:$("#cha_renkou").val(),cha_banqian:$("#cha_banqian").val(),cha_v6:$("#cha_v6").val(),
    	    	cha_v8:$("#cha_v8").val(),cha_v8_1:$("#cha_v8_1").val(),cha_bfdw:$("#cha_bfdw").val(),cha_bfzrr:$("#cha_bfzrr").val(),year:$("#cha_year").val()
    	    	},
    	   
    	    beforeSend: function(){
    	    	$('#exportExcel_all').hide();
    	    	$('#exportExcel_all1').hide();
    	    	$('#exportExcel_all2').hide();
    	    	$('#exportExcel_all3').hide();
    	    	$('#exportExcel_all4').hide();
    	    	$('#exportExcel_all5').hide();
    	    	$('#exportExcel_all6').hide();
    	    	$('#exportExcel_all_dengdai').show();
    	    },
    	    complete: function(){
    	    	$('#exportExcel_all').show();
    	    	$('#exportExcel_all1').show();
    	    	$('#exportExcel_all2').show();
    	    	$('#exportExcel_all3').show();
    	    	$('#exportExcel_all4').show();
    	    	$('#exportExcel_all5').show();
    	    	$('#exportExcel_all6').show();
    	    	$('#exportExcel_all_dengdai').hide();
    	    },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		if (data == "1") {
    	    			toastr["error"]("error", "服务器异常");
    	    		}else if (data == "0") {
    	    			toastr["error"]("error", "登录超时，请刷新页面重新登录操作");
    	    		}else{
    	    			window.open("http://"+window.location.host+data.path);
    	    		}
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    });
    //清空查询
    $('#close_cha_button').click(function () {
    	$("#cha_qx").val("请选择");
    	$("#cha_smx").val("请选择");
    	$("#cha_gcc").val("请选择");
    	$("#cha_sbbz").val("请选择");
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
    	$('#chauxnshiousuo').click();
    	chaxun = {};
    	$("#yeqian").hide();
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
    });
	gachacun_initialization();
});

//处理时间轴
function handle_shijianzhou(){
	var str="";
    $("#show_checked input[type='checkbox']:checkbox").each(function(){ 
        if($(this).attr("checked")){
            str += $(this).val()+","
        }
    })
    if(str!=""){
    	$.ajax({  		       
    	    url: "/assa/getZaiXiangGengXinController.do",
    	    type: "POST",
    	    async:false,
    	    dataType: "json",
    	    data:{
    	    	pkid: shijianzhou_pkid,
    	    	year: $("#cha_year").val(),
    	    	str: str,
    	    	
            },
    	    success: function (data) {
    	    	if (typeof data != "undefined") {
    	    		//$("#show_zfju").html("");
    	    		var html2_1='<div>';
    				var sjz_pie={};
    				var sjz_spie={};
    				var sjz_xpie={};
    				$.each(data.data13,function(i,item){
    					if(item.da_type=="da_help_visit"){
    						if(item.pie=="-"||item.pie==""||item.pie==null){
    							html2_1+='<div class="vertical-timeline-block"><div class="vertical-timeline-icon lazur-bg">'+
    							'<i class="fa fa-male"></i></div><div class="vertical-timeline-content" id="show_sjz_zoufang">';
    							html2_1+='<h2>帮扶人走访情况记录</h2><div class="col-sm-12"><div class="col-sm-8"><p>'+item.v3+'</p>'+
    							' <span class="vertical-date"><small>'+item.v1+'</small></span></div><div class="col-sm-4">';
    							html2_1+='</div></div></div></div>';
    						}else{
    							html2_1+='<div class="vertical-timeline-block"><div class="vertical-timeline-icon lazur-bg">'+
    							'<i class="fa fa-male"></i></div><div class="vertical-timeline-content" id="show_sjz_zoufang">';
    							html2_1+='<h2>帮扶人走访情况记录</h2><div class="col-sm-12"><div class="col-sm-8"><p>'+item.v3+'</p>'+
    							' <span class="vertical-date"><small>'+item.v1+'</small></span></div><div class="col-sm-4">';
    							sjz_pie=item.pie.split(",");
    							for(var j=0;j<sjz_pie.length;j++){
    								html2_1+='<a href=\''+sjz_pie[j]+'\' title=\"走访情况图片\" data-gallery=\"\">'+
    		    					'<img src=\''+sjz_pie[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    		    				}
    							
    							
    							html2_1+='</div></div></div></div>';
    						}
    						
    					}else if(item.da_type=="da_help_measures"){
    						if(item.pie=="-"||item.pie==""||item.pie==null){
    							html2_1+='<div class="vertical-timeline-block"><div class="vertical-timeline-icon blue-bg"> <i class="fa fa-file-text"></i></div>'+
    							'<div class="vertical-timeline-content" ><h2>帮扶措施</h2><div class="col-sm-12"><div class="col-sm-8">';
    							html2_1+='<p>'+item.v2+'</p><span class="vertical-date"><small>'+item.v1+'</small></span></div></div></div></div>';
    						}else{
    							html2_1+='<div class="vertical-timeline-block"><div class="vertical-timeline-icon blue-bg"> <i class="fa fa-file-text"></i></div>'+
    							'<div class="vertical-timeline-content" ><h2>帮扶措施</h2><div class="col-sm-12"><div class="col-sm-8">';
    							html2_1+='<p>'+item.v2+'</p><span class="vertical-date"><small>'+item.v1+'</small></span></div><div class="col-sm-4">';
    							sjz_spie=item.pie.split(",");
    							for(var j=0;j<sjz_spie.length;j++){
    								html2_1+='<a href=\''+sjz_spie[j]+'\' title=\"帮扶措施图片\" data-gallery=\"\">'+
    										'<img src=\''+sjz_spie[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    							}
    							html2_1+='</div></div></div></div>';
    						}
    						
    					}else if(item.da_type=="da_help_results"){
    						if(item.pie=="-"||item.pie==""||item.pie==null){
    							html2_1+='<div class="vertical-timeline-block" >  <div class="vertical-timeline-icon yellow-bg">'+
    							'<i class="fa fa-cny"></i> </div> <div class="vertical-timeline-content" id="show_sjz_bfcx">';
    							html2_1+='<h2>帮扶成效</h2><div class="col-sm-12"><div class="col-sm-8"> <p>'+item.v2+'</p><span class="vertical-date">'+
    									'<small>'+item.v1+'</small></span></div><div class="col-sm-4"></div></div></div></div>';
    						}else{
    							html2_1+='<div class="vertical-timeline-block" >  <div class="vertical-timeline-icon yellow-bg">'+
    							'<i class="fa fa-cny"></i> </div> <div class="vertical-timeline-content" id="show_sjz_bfcx">';
    							html2_1+='<h2>帮扶成效</h2><div class="col-sm-12"><div class="col-sm-8"><p>'+item.v2+'</p>'+
    							'<span class="vertical-date"><small>'+item.v1+'</small></span></div><div class="col-sm-4">';
    							sjz_xpie=item.pie.split(",");
    							for(var j=0;j<sjz_xpie.length;j++){
    								html2_1+='<a href=\''+sjz_xpie[j]+'\' title=\"帮扶成效图片\" data-gallery=\"\">'+
    										'<img src=\''+sjz_xpie[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    							}
    							html2_1+='</div></div></div></div>';
    						}
    						
    					}
    				});
    				html2_1+='</div>';
    				$("#show_zfju").html(html2_1);
    	    	}
    	    },
    	    error: function () { 
    	    	toastr["error"]("error", "服务器异常");
    	    }  
    	});
    }else{
    	$("#show_zfju").html("");
    }
}

//查看贫苦户的详细信息
function chakan_info(pkid){
	$("#hu_pkid").val(pkid);//记录户主ID
	cuoshi_tz_table.bootstrapTable('destroy');
	$("#yeqian").show();
	$("#spinners").show();
	$("#neirong_jiben").hide();
	document.getElementById('yeqian').scrollIntoView();
	savePoorMessage(pkid);
	cuoshi_initialization();
	cuoshi_initialization1();
}

//贫困户初始化
function gachacun_initialization(){
	metTable_bxbxxb.bootstrapTable({
		method: 'POST',
		height: "508",
		url: "/assa/getPoorMessageController.do",
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
		onLoadSuccess: function (data) {
			//console.log($("#total_number").html());
			$("th[data-field=v9] .th-inner" ).html("家庭人口(总人口数:"+data.total_number+")")
			//$("#total_number").html("("+data.total_number+")");
			//console.log(">>.."+$(".th-inner #total_number").html());
			//$("#total_number").html("("+data.total_number+")");
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
    temp.cha_v8_2 = chaxun.cha_v8_2;
    temp.cha_year = chaxun.cha_year;
	return temp;
}

var shijianzhou_pkid;
//显示贫困户的详细信息
function savePoorMessage(pkid){
	da_household_id=pkid;
	$("#yd_bqfs").text("");//搬迁方式
	$("#yd_azfs").text("");//安置方式
	$("#yd_azd").text("");//安置地
	$("#yd_kn").text("");//搬迁可能存在的困难
	
	$("#yd_lx").text("");//分散安置类型
	$("#yd_fyd").text("");//房源地
	$("#yd_fj").text("");//房价
	$("#yd_xy").text("");//与用工企业签订就业安置协议
	$("#yd_path").html("");
	shijianzhou_pkid = pkid;
	var html="";
	$.ajax({
	    url: "/assa/getPoorDetailController.do",
	    type: "POST",
	    async:true,
	    dataType:"json",
	    data:{
	    	pkid:pkid,year:$("#cha_year").val()
        },
	    success: function (data) {
	    	if(data.data4[0].sh_v3=="是"){
	    		$(".active5").show();
	    		$("#dy_ydbq").show();
	    		$("#dy_ydbp_photo").show();
	    	}else{
	    		$(".active5").hide();
	    		$("#dy_ydbq").hide();
	    		$("#dy_ydbp_photo").hide();
	    	}
	    	//户主的个人信息
	    	$('#show_hz_xm').text(data.data1[0].v6+"（户主）");//户主姓名
	    	$('#show_hz_xm1').text(data.data1[0].v6+"（户主）");//户主姓名
	    	$('#shiw_hz_sex').text(data.data1[0].v7);//性别
	    	$('#shiw_hz_sex1').text(data.data1[0].v7);//性别
	    	$('#show_hz_mz').text(data.data1[0].v11);//民族
	    	$('#show_hz_mz1').text(data.data1[0].v11);//民族
	    	$('#show_hz_rk').text(data.data1[0].v9);//人口
	    	$('#show_hz_phone').text(data.data1[0].v25);//电话
	    	$('#show_hz_sfz').text(data.data1[0].v8);//证件号码
	    	$('#show_hz_address').text(data.data1[0].basic_address);//家庭住址
	    	$('#show_hz_pkgsx').text(data.data1[0].v22);//贫困户属性
	    	$('#show_hz_zpyy').text(data.data1[0].v23);//致贫原因
	    	$('#show_hz_bank').text(data.data1[0].v26);//开户银行名称
	    	$('#show_hz_banknum').text(data.data1[0].v27);//银行卡号
	    	$('#show_hz_whcd').text(data.data1[0].v12);//文化程度
	    	$('#show_hz_zzmm').text(data.data1[0].v28);
	    	$('#show_hz_sfzx').text(data.data1[0].v13);//在校生
	    	$('#show_hz_jkzk').text(data.data1[0].v14);//健康状况
	    	$('#show_hz_ldl').text(data.data1[0].v15);//劳动力
	    	
	    	$('#show_hz_wgqk').text(data.data1[0].v16);//务工状况
	    	$('#show_hz_wgsj').text(data.data1[0].v17);//务工时间
	    	$('#show_hz_cjxnh').text(data.data1[0].v18);//是否参加新农合
	    	
	    	$('#show_hz_ylbx').text(data.data1[0].v19);//是否参加养老保险
	    	$('#show_hz_sfjls').text(data.data1[0].v29);//是否军烈属
	    	$('#show_hz_xyjr').text(data.data1[0].v32);//是否现役军人
	    	
	    	$('#show_hz_dszn').text(data.data1[0].v30);//是否独生子女
	    	$('#show_hz_snh').text(data.data1[0].v31);//是否双女户
	    	$('#show_hz_qtzpyy').text(data.data1[0].v33);//其他致贫原因
	    	$('#show_hz_rk1').text(data.data1[0].v9);//人口
	    	$('#show_hz_phone1').text(data.data1[0].v25);//电话
	    	$('#show_hz_sfz1').text(data.data1[0].v8);//证件号码
	    	$('#show_hz_address1').text(data.data1[0].basic_address);//家庭住址
	    	$('#show_hz_pkgsx1').text(data.data1[0].v22);//贫困户属性
	    	$('#show_hz_zpyy1').text(data.data1[0].v23);//致贫原因
	    	$('#show_hz_bank1').text(data.data1[0].v26);//开户银行名称
	    	$('#show_hz_banknum1').text(data.data1[0].v27);//银行卡号
	    	$('#show_hz_whcd1').text(data.data1[0].v12);//文化程度
	    	$('#show_hz_zzmm1').text(data.data1[0].v28);
	    	$('#show_hz_sfzx1').text(data.data1[0].v13);//在校生
	    	$('#show_hz_jkzk1').text(data.data1[0].v14);//健康状况
	    	$('#show_hz_ldl1').text(data.data1[0].v15);//劳动力
	    	
	    	$('#show_hz_wgqk1').text(data.data1[0].v16);//务工状况
	    	$('#show_hz_wgsj1').text(data.data1[0].v17);//务工时间
	    	$('#show_hz_cjxnh1').text(data.data1[0].v18);//是否参加新农合
	    	
	    	$('#show_hz_ylbx1').text(data.data1[0].v19);//是否参加养老保险
	    	$('#show_hz_sfjls1').text(data.data1[0].v29);//是否军烈属
	    	$('#show_hz_xyjr1').text(data.data1[0].v32);//是否现役军人
	    	
	    	$('#show_hz_dszn1').text(data.data1[0].v30);//是否独生子女
	    	$('#show_hz_snh1').text(data.data1[0].v31);//是否双女户
	    	$('#show_hz_qtzpyy1').text(data.data1[0].v33);//其他致贫原因
	    	
	    	var k = 0;
	    	var vls = data.data1[0].basic_explain;
	    	$('#show_hz_zfyysm1').text(vls);//致贫原因说明
    		for(var i = 0;i<vls.length;){
    			vls = vls.substring(0,k+60)+"<br>"+vls.substring(k+60);
    			k += 64;
    			i += 60;
    		}
	    	
	    	$('#show_hz_zfyysm').html("<br>"+vls);//致贫原因说明
//	    	$('#show_hz_zfyysm1').text(vls);//致贫原因说明
	    	
	    	if(data.data1[0].pic_path==""||data.data1[0].pic_path==null||data.data1[0].pic_path==undefined||data.data1[0].pic_path=="-"){
	    		$('#hz_pic').html('<a href="img/zw.jpg" title=\"户主头像\"data-gallery=\"\">'+
		    			'<img src="img/zw.jpg" style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\"></a>');
	    		$('#hz_pic1').html('<img src="img/zw.jpg" style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\"  />');
	    	}else{
	    		$('#hz_pic').html('<a href=\''+data.data1[0].pic_path+'\' title=\"户主头像\"data-gallery=\"\">'+
		    			'<img src=\''+data.data1[0].pic_path+'\' style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\"></a>');
	    		$('#hz_pic1').html('<img src="'+data.data1[0].pic_path+'" style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\" />');
		    	
	    	}
	    	if(data.data1[0].sys_standard=="国标"){
	    		$('#show_hz_sbbz').text("国家级标准");
	    	}else if(data.data1[0].sys_standard=="市标") {
	    		$('#show_hz_sbbz').text("市级标准");
	    	}else{
	    		$('#show_hz_sbbz').text(data.data1[0].sys_standard);
	    	}
	    	//生产条件
	    	if(data.data3==""||data.data3==null||data.data3==undefined){
	    	}else{
	    		$('#show_gdmj').text(data.data3[0].sc_v1);//耕地面积
		    	$('#show_sjdmj').text(data.data3[0].sc_v2);//水浇地面积
		    	$('#show_ldmj').text(data.data3[0].sc_v3);//林地面积
		    	$('#shoow_tghl').text(data.data3[0].sc_v4);//退耕还林面积
		    	$('#show_cmc').text(data.data3[0].sc_v5);//草牧场面积
		    	$('#show_scyf').text(data.data3[0].sc_v6);//生产用房面积
		    	$('#show_qt').text(data.data3[0].sc_v7);//其他
		    	$('#show_jiaqin').text(data.data3[0].sc_v8);//家禽
		    	$('#show_niu').text(data.data3[0].sc_v9);//牛
		    	$('#show_yang').text(data.data3[0].sc_v10);//羊
		    	$('#show_zhu').text(data.data3[0].sc_v11);//猪
		    	$('#show_jq_qita').text(data.data3[0].sc_v12);//其他
		    	$('#show_lgmj').text(data.data3[0].sc_v13);//林果面积
		    	$('#show_smmj').text(data.data3[0].sc_v14);//水面面积
		    	$('#show_gdmj1').text(data.data3[0].sc_v1);//耕地面积
		    	$('#show_sjdmj1').text(data.data3[0].sc_v2);//水浇地面积
		    	$('#show_ldmj1').text(data.data3[0].sc_v3);//林地面积
		    	$('#shoow_tghl1').text(data.data3[0].sc_v4);//退耕还林面积
		    	$('#show_cmc1').text(data.data3[0].sc_v5);//草牧场面积
		    	$('#show_scyf1').text(data.data3[0].sc_v6);//生产用房面积
		    	$('#show_qt1').text(data.data3[0].sc_v7);//其他
		    	$('#show_hz_cssl').text("家禽（"+data.data3[0].sc_v8+"）只、牛（"+data.data3[0].sc_v9+"）头、羊（"+data.data3[0].sc_v10+"）只、猪（"+data.data3[0].sc_v11+"）头、其他（"+data.data3[0].sc_v12+"）");//家禽
		    	$('#show_lgmj1').text(data.data3[0].sc_v13);//林果面积
		    	$('#show_smmj1').text(data.data3[0].sc_v14);//水面面积
	    	}
	    	//生活条件
	    	if(data.data4==""||data.data4==null||data.data4==undefined){
	    	}else{
	    		$('#show_zfmj').text(data.data4[0].sh_v1);//住房面积
		    	$('#show_sfwf').text(data.data4[0].sh_v2);//是否危房
		    	$('#show_ydbq').text(data.data4[0].sh_v3);//是否纳入易地扶贫搬迁
		    	$('#show_ysqk').text(data.data4[0].sh_v4);//饮水情况
		    	$('#show_rhllx').text(data.data4[0].sh_v6);//入户路类型
		    	
		    	$('#show_yssfkn').text(data.data4[0].sh_v8);//饮水是否困难
		    	$('#show_zgljl').text(data.data4[0].sh_v7);//与主干路距离
		    	$('#show_rllx').text(data.data4[0].sh_v10);//燃料类型
		    	$('#show_sfcjzyh').text(data.data4[0].sh_v11);//专业合作社
		    	$('#show_ywwscs').text(data.data4[0].sh_v12);//有无卫生厕所
		    	$('#show_tdqk').text(data.data4[0].sh_v5);//有无卫生厕所
		    	$('#show_sfysaq').text(data.data4[0].sh_v9);//有无卫生厕所
		    	
		    	$('#show_zfmj1').text(data.data4[0].sh_v1);//住房面积
		    	$('#show_sfwf1').text(data.data4[0].sh_v2);//是否危房
		    	$('#show_ydbq1').text(data.data4[0].sh_v3);//是否纳入易地扶贫搬迁
		    	$('#show_ysqk1').text(data.data4[0].sh_v4);//饮水情况
		    	$('#show_rhllx1').text(data.data4[0].sh_v6);//入户路类型
		    	
		    	$('#show_yssfkn1').text(data.data4[0].sh_v8);//饮水是否困难
		    	$('#show_zgljl1').text(data.data4[0].sh_v7);//与主干路距离
		    	$('#show_rllx1').text(data.data4[0].sh_v10);//燃料类型
		    	$('#show_sfcjzyh1').text(data.data4[0].sh_v11);//专业合作社
		    	$('#show_ywwscs1').text(data.data4[0].sh_v12);//有无卫生厕所
		    	$('#show_tdqk1').text(data.data4[0].sh_v5);//有无卫生厕所
		    	$('#show_sfysaq1').text(data.data4[0].sh_v9);//有无卫生厕所
		    	
		    	
		    	
	    	}
	    	//家庭成员
	    	html="<div>";
	    	dy_html="";
	    	$.each(data.data2,function(i,item){
	    		//家庭成员
	    			if(item.cy_v6 ==undefined||item.cy_v6 ==""||item.cy_v6 ==null){
	    				html+="";
	    				dy_html+="";
	    			}else{
	    				html+='<div class="col-sm-4"><div class="contact-box" style=\"padding: 10px;\"><div class=\"row\" style=\"padding-right: 5px;padding-left: 5px;\"><div class="col-sm-4"> <div class="text-center">';
	    				dy_html+='<tr class="gradeX">';
	    				
	    				if(item.cy_pic_path==""||item.cy_pic_path==null||item.cy_pic_path==undefined||item.cy_pic_path=="-"){
	    					html+='<a href="img/zw.jpg" title=\"家庭成员照片\"data-gallery=\"\">'+
		    				'<img alt="image" class="img-responsive" src="img/zw.jpg"'+
		    				'style=\"margin:0;vertical-align:baseline;width:76px;height:76px;\"></a>';
	    				
	    					dy_html+='<td rowspan="5" colspan="2"><img src="img/zw.jpg" style="margin:0;vertical-align:baseline;width:150px;height:150px;"></td>';
	    				}else{
	    					html+='<a href="'+item.cy_pic_path+'" title=\"家庭成员照片\"data-gallery=\"\">'+
		    				'<img alt="image" class="img-responsive" src=\''+item.cy_pic_path+'\''+
		    				'style=\"margin:0;vertical-align:baseline;width:76px;height:76px;\"></a>';
	    					dy_html+='<td rowspan="5" colspan="2"><img src="src=\''+item.cy_pic_path+'\'" style="margin:0;vertical-align:baseline;width:150px;height:150px;"></td>';
	    				}
		    			
	    				
		    			html+='</div></div><div class="col-sm-8"> <h3><strong>'+item.cy_v6+'（'+item.cy_v10+'）</strong></h3>'+
		    			'<p><code>'+item.cy_v7+'</code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<code>'+item.cy_v11+'</code></p>'+
		    			'<p>'+item.cy_v8+'</p></div></div>'+
		    			' <div class=\"row\" style=\"padding-right: 5px;padding-left: 5px;\"><div class="col-sm-6" style=\"padding-right: 5px;padding-left: 5px;\"><address>文化程度：<code>'+item.cy_v12+'</code><br>'+
		    			'健康状况：<code >'+item.cy_v14+'</code><br>新农合：<code>'+item.cy_v18+'</code><br>是否现役军人：<code>'+item.cy_v32+'</code><br>务工情况：<code>'+item.cy_v16+'</code></address></div>'+
		    			'<div class="col-sm-6" style=\"padding-right: 5px;padding-left: 5px;\"><address>是否在校：<code>'+item.cy_v13+'</code><br>'+
		    			'劳动力：&nbsp;&nbsp;&nbsp;<code>'+item.cy_v15+'</code><br>养老保险：<code>'+item.cy_v19+'</code><br>政治面貌：<code>'+item.cy_v28+'</code><br>务工时间：<code>'+item.cy_v17+'</code></address></div></div>'+
		    			'<div class="clearfix"></div></div></div>';
		    			
		    			dy_html+='<td>姓名</td> <td>'+item.cy_v6+'（'+item.cy_v10+'）</td><td>性别</td><td>'+item.cy_v7+'</td></tr>'+
		    					'<tr class="gradeX"><td >民族</td><td>'+item.cy_v11+'</td><td >身份证号</td><td>'+item.cy_v8+'</td> </tr>'+
		    					'<tr class="gradeX"><td >文化程度</td><td id=""></td>'+item.cy_v12+'<td >是否在校</td><td>'+item.cy_v13+'</td></tr>'+
		    					' <tr class="gradeX"><td >健康状况</td><td>'+item.cy_v14+'</td><td >劳动力</td><td>'+item.cy_v15+'</td></tr>'+
		    					' <tr class="gradeX"><td >新农合</td><td >'+item.cy_v18+'</td><td >养老保险</td> <td>'+item.cy_v19+'</td></tr>'+
		    					'<tr class="gradeX"><td >是否现役军人</td><td>'+item.cy_v32+'</td><td >政治面貌</td><td>'+item.cy_v28+'</td><td >务工情况</td><td>'+item.cy_v16+'</td></tr>'+
		    					'<tr class="gradeX"><td >务工时间</td><td>'+item.cy_v17+'</td><td ></td><td ></td><td ></td><td ></td>'
	    			}
	    	});
	    	html+='</div>';
	    	$("#jiatingchengyuan").html(html);
	    	$("#dy_jtcy").html(dy_html);
	    	
	    	//帮扶人
	    	var html1='<div><table class="table table-bordered"><thead><tr><th style="text-align:center;width:12%;">姓名</th>'+
		    		'<th style="text-align:center;width:36%;">单位</th><th style="text-align:center;width:36%;">职务</th><th style="text-align:center;width:15%;">电话</th></tr></thead>'+
		    		'<tbody>';
	    	$.each(data.data7,function(i,item){
    				if(item.bfr_col_name==undefined||item.bfr_col_name==""||item.bfr_col_name==null){
    					html1+="";
    				}else{
    					html1+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.bfr_col_name+'</td>'+
    						'<td style="border:1px solid #ddd;text-align:center;">'+item.bfr_com_name+'</td>'+
	    					'<td style="border:1px solid #ddd;text-align:center;">'+item.bfr_col_post+'</td>'+
	    					'<td style="border:1px solid #ddd;text-align:center;">'+item.bfr_telephone+'</td></tr>';
    				}
	    	});
	    	html1+=	'<tr><th style="text-align:center;border-bottom:1px solid #DDD;background-color:#F5F5F6">帮扶目标</th>'+
					'<td style="border:1px solid #ddd;" colspan="3">'+data.data15[0].bfr_mubiao+'</td></tr>'+
					'<tr><th style="text-align:center;border-bottom:1px solid #DDD;background-color:#F5F5F6">帮扶时限</th>'+
					'<td style="border:1px solid #ddd;" colspan="3">'+data.data15[0].bfr_shixiao+'</td></tr>'+
					'<tr><th style="text-align:center;border-bottom:1px solid #DDD;background-color:#F5F5F6">帮扶计划</th>'+
					'<td style="border:1px solid #ddd;" colspan="3">'+data.data15[0].bfr_jihua+'</td></tr></tbody></table>';
			html1+='</div>';
			$("#show_bangfuren").html(html1);
			$("#dy_dwzrr").html(html1);
			
			//走访情况
	    	var html2='<div><table class="table table-striped table-bordered"><thead><tr><th style="text-align:center;width:15%;">走访时间</th>'+
	    				'<th style="text-align:center;width:30%;">帮扶干部</th><th style="width:55%;">走访情况记录</th>'+
	    				'</tr></thead><tbody>';
	    	var html2_1='<div><table class="table table-striped table-bordered"><thead><tr><th style="text-align:center;width:15%;">走访时间</th>'+
	    	'<th style="text-align:center;width:30%;">帮扶干部</th><th style="width:55%;">走访情况记录</th>'+
	    	'</tr></thead><tbody>';
			var pic={};
	    	$.each(data.data8,function(i,item){
	    			
    				if(item.zf_v1==undefined||item.zf_v1==""||item.zf_v1==null){
    					html2+='';
    				}else{
    					if(item.zf_pic=="-"||item.zf_pic==""||item.zf_pic==null){
    						html2+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.zf_v1+'</td>'+
        					'<td style="border:1px solid #ddd;text-align:center;">'+item.zf_v2+'</td>'+
        					'<td style="border:1px solid #ddd;">'+item.zf_v3+
        					'</td></tr>';
    						html2_1+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.zf_v1+'</td>'+
    						'<td style="border:1px solid #ddd;text-align:center;">'+item.zf_v2+'</td>'+
    						'<td style="border:1px solid #ddd;">'+item.zf_v3+
    						'</td></tr>';
    	    			}else {
    	    				html2+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.zf_v1+'</td>'+
        					'<td style="border:1px solid #ddd;text-align:center;">'+item.zf_v2+'</td>'+
        					'<td style="border:1px solid #ddd;"><div class="col-sm-12"><div  style="margin-left:-30px;" class="col-sm-8">'+item.zf_v3+'</div>'+
        					'<div class="col-sm-4">';
    	    				pic=item.zf_pic.split(",");
    	    				for(var j=0;j<pic.length;j++){
    	    					html2+='<a href=\''+pic[j]+'\' title=\"走访情况图片\" data-gallery=\"\">'+
            					'<img src=\''+pic[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    	    				}
    	    				
        					html2+='</div></div></td></tr>';
        					html2_1+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.zf_v1+'</td>'+
        					'<td style="border:1px solid #ddd;text-align:center;">'+item.zf_v2+'</td>'+
        					'<td style="border:1px solid #ddd;"><div class="col-sm-12"><div class="col-sm-8">'+item.zf_v3+'</div>'+
        					'<div class="col-sm-4">';
        					pic=item.zf_pic.split(",");
        					for(var j=0;j<pic.length;j++){
        						html2_1+='<a href=\''+pic[j]+'\' title=\"走访情况图片\" data-gallery=\"\">'+
        						'<img src=\''+pic[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
        					}
        					
        					html2+='</div></div></td></tr>';
        					html2_1+='</div></div></td></tr>';
    	    				
    	    			}
    				}
    				
			});
	    	html2+='</tbody></table></div>';
	    	html2_1+='</tbody></table></div>';
			$("#show_zoufang").html(html2);
			$("#dy_zfjl").html(html2_1);
			
			//帮扶措施
			
		
	    	var html3='<div><table class="table table-striped table-bordered"><thead><tr><th style="text-align:center;" colspan="1" rowspan="2">项目类别</th><th style="text-align:center;" colspan="1" rowspan="2">扶持措施</th>'+
			'<th style="text-align:center;" colspan="1" rowspan="2">是否符合扶持条件</th><th style="text-align:center;" colspan="3">2016年</th><th style="text-align:center;" colspan="3">2017年</th><th style="text-align:center;" colspan="3">2018年</th>'+
			'<th style="text-align:center;" colspan="3">2019年</th></tr><tr><th style="text-align:center;" >项目需求量</th><th style="text-align:center;">受益资金/政策</th><th style="text-align:center;">落实时间</th><th style="text-align:center;" >项目需求量</th>'+
			'<th style="text-align:center;">受益资金/政策</th><th style="text-align:center;">落实时间</th><th style="text-align:center;" >项目需求量</th><th style="text-align:center;">受益资金/政策</th><th style="text-align:center;">落实时间</th>'+
			'<th style="text-align:center;" >项目需求量</th><th style="text-align:center;">受益资金/政策</th><th style="text-align:center;">落实时间</th></tr></thead><tbody>';
			
			var bfcs_pic={};
	    	$.each(data.data9,function(i,item){
	    		html3+='<tr><td>'+item.v1+'</td><td>'+item.v2+'</td><td>'+item.v3+'</td><td>'+item.v4_2016+'</td><td>'+item.v5_2016+'</td>'+
	    				'<td>'+item.v6_2016+'</td><td>'+item.v4_2017+'</td><td>'+item.v5_2017+'</td><td>'+item.v6_2017+'</td><td>'+item.v4_2018+'</td>'+
	    				'<td>'+item.v5_2018+'</td><td>'+item.v6_2018+'</td><td>'+item.v4_2019+'</td><td>'+item.v5_2019+'</td><td>'+item.v6_2019+'</td></tr>';
//    				
			});
	    	
			html3+='</tbody></table></div>';
//			console.log(html3)
			$("#dy_bfcs11").html(html3);
			
			//帮扶成效
	    	var html4='<div><table class="table table-striped table-bordered"><thead><tr><th style="text-align:center;width:20%;">时间</th>'+
	    			'<th style="text-align:center;">成效内容</th><th style="text-align:center;width:20%;">贫困户签字</th></tr></thead><tbody>';
	    	var html4_1='<div><table class="table table-striped table-bordered"><thead><tr><th style="text-align:center;width:20%;">时间</th>'+
	    	'<th style="text-align:center;">成效内容</th><th style="text-align:center;width:20%;">贫困户签字</th></tr></thead><tbody>';
			
			var bfcx_pic={};
	    	$.each(data.data10,function(i,item){
    				if(item.bfcx_v1==undefined||item.bfcx_v1==""||item.bfcx_v1==null){
    					html4+='';
    				}else{
    					if(item.bfcx_pic=="-"||item.bfcx_pic==""||item.bfcx_pic==null){
    						html4+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v1+'</td>'+
							'<td style="border:1px solid #ddd;">'+item.bfcx_v2+'</td>'+
							'<td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v3+'</td> </tr>';
    						
    						html4_1+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v1+'</td>'+
    						'<td style="border:1px solid #ddd;">'+item.bfcx_v2+'</td>'+
    						'<td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v3+'</td> </tr>';
    					}else{
    						html4+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v1+'</td>'+
        					'<td style="border:1px solid #ddd;"><div class="col-sm-12">'+
        					'<div style="margin-left:-30px;" class="col-sm-7"><div style="float:left;">'+item.bfcx_v2+'</div></div>'+
        					'<div class="col-sm-5"><div style="float:left">';
    						
    						html4_1+='<tr><td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v1+'</td>'+
    						'<td style="border:1px solid #ddd;"><div class="col-sm-12">'+
    						'<div class="col-sm-7"><div style="float:left;">'+item.bfcx_v2+'</div></div>'+
    						'<div class="col-sm-5"><div style="float:left">';
    						
    						bfcx_pic=item.bfcx_pic.split(",");
    	    				for(var j=0;j<bfcx_pic.length;j++){
    	    					html4+='<a href=\''+bfcx_pic[j]+'\' title=\"帮扶措施图片\" data-gallery=\"\">'+
            					'<img src=\''+bfcx_pic[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    	    					html4_1+='<a href=\''+bfcx_pic[j]+'\' title=\"帮扶措施图片\" data-gallery=\"\">'+
    	    					'<img src=\''+bfcx_pic[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    	    				}
        					html4+='</div></div></div><td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v3+'</td>'+
        						'</tr>';
        					html4_1+='</div></div></div><td style="border:1px solid #ddd;text-align:center;">'+item.bfcx_v3+'</td>'+
        					'</tr>';
    					}
    				}
			});
	    	html4+='</tbody></table>';
	    	html4_1+='</tbody></table>';
			$("#show_bfcx").html(html4);
			$("#dy_bfcx").html(html4_1);
	    	
	    	//当前收入情况
			if(data.data5==""||data.data5==null||data.data5==undefined){
				var a=0;
				$("#show_dqzongsr").text('0');//年总收入
			}else{
				$("#show_ny").text(data.data5[0].dqsr_v1);//农业明细
				$("#show_ny_je").text(data.data5[0].dqsr_v2);//农业明细金额
		    	$("#show_cmy").text(data.data5[0].dqsr_v3);//畜牧业明细
		    	$("#show_cmy_je").text(data.data5[0].dqsr_v4);//畜牧业金额
		    	$("#show_ly").text(data.data5[0].dqsr_v5);//林业明细
		    	$("#show_ly_je").text(data.data5[0].dqsr_v6);//林业金额
		    	$("#show_qtsc").text(data.data5[0].dqsr_v7);//生产其他明细
		    	$("#show_qtsc_je").text(data.data5[0].dqsr_v8);//其他金额
		    	$("#show_scxj").text(data.data5[0].dqsr_v9);//生产小计明细
		    	$("#show_scxj_je").text(data.data5[0].dqsr_v10);//生产小计明细金额
		    	$("#show_zcst").text(data.data5[0].dqsr_v11);//生态补贴明细
		    	$("#show_zcst_je").text(data.data5[0].dqsr_v12);//生态补贴明细金额
		    	$("#show_yl").text(data.data5[0].dqsr_v13);//养老金明细
		    	$("#show_yl_je").text(data.data5[0].dqsr_v14);//养老金金额
		    	$("#show_db").text(data.data5[0].dqsr_v15);//低保补贴明细
		    	$("#show_db_je").text(data.data5[0].dqsr_v16);//低保补贴明细金额
		    	$("#show_rm").text(data.data5[0].dqsr_v17);//燃煤补贴明细
		    	$("#show_rm_je").text(data.data5[0].dqsr_v18);//燃煤补贴金额
		    	$("#show_zcqt").text(data.data5[0].dqsr_v19);//政策其他明细
		    	$("#show_zcqt_je").text(data.data5[0].dqsr_v20);//政策其他明细金额
		    	$("#show_zcxj").text(data.data5[0].dqsr_v21);//政策小计明细
		    	$("#show_zcxj_je").text(data.data5[0].dqsr_v22);//政策小计明细金额
		    	$("#show_td").text(data.data5[0].dqsr_v23);//土地流转明细
		    	$("#show_td_je").text(data.data5[0].dqsr_v24);//土地流转明细金额
		    	$("#show_tdqt").text(data.data5[0].dqsr_v25);//土地其他明细
		    	$("#show_tdqt_je").text(data.data5[0].dqsr_v26);//土地其他明细金额
		    	
		    	$("#show_gzxm1").text(data.data5[0].dqsr_v35);//工资项目1
		    	$("#show_gzmx1").text(data.data5[0].dqsr_v27);//工资明细1
		    	$("#show_gzmx1_je").text(data.data5[0].dqsr_v28);//工资1金额
		    	
		    	$("#show_gzxm2").text(data.data5[0].dqsr_v36);//工资项目2
		    	$("#show_gzmx2").text(data.data5[0].dqsr_v29);//工资明细2
		    	$("#show_gzmx2_je").text(data.data5[0].dqsr_v30);//工资2金额
		    	
		    	$("#show_qtxm1").text(data.data5[0].dqsr_v37);//其他项目1
		    	$("#show_gzqt1").text(data.data5[0].dqsr_v31);//其他明细1
		    	$("#show_gzqt1_je").text(data.data5[0].dqsr_v32);//明细1金额
		    	
		    	$("#show_qtxm2").text(data.data5[0].dqsr_v38);//其他项目2
		    	$("#show_gzqt2").text(data.data5[0].dqsr_v33);//明细2
		    	$("#show_gzqt2_je").text(data.data5[0].dqsr_v34);//明细2工资
		    	
		    	$("#show_srzj").text(data.data5[0].dqsr_v39);//总计
		    	if(data.data5[0].dqsr_v39==""||data.data5[0].dqsr_v39==null||data.data5[0].dqsr_v39==undefined){
		    		$("#show_dqzongsr").text("0");//年总收入
		    		var a=0;
		    	}else{
		    		$("#show_dqzongsr").text(data.data5[0].dqsr_v39);//年总收入
		    		var a=data.data5[0].dqsr_v39;
		    	}
		    	
		    	
		    	$("#show_wbj").text(data.data5[0].dqsr_v40);//五保金明细
		    	$("#show_wbj_je").text(data.data5[0].dqsr_v41);//五保金金额
		    	$("#show_jhsy").text(data.data5[0].dqsr_v42);//计划生育金明细
		    	$("#show_jhsy_je").text(data.data5[0].dqsr_v43);//计划生育金额
		    	
			}
			//当前收入饼图
	    	var show_ny= $("#show_ny").text();//农业明细
	    	var show_ny_je= $("#show_ny_je").text();//农业明细金额
	    	var show_cmy= $("#show_cmy").text();//畜牧业明细
	    	var show_cmy_je=$("#show_cmy_je").text();//畜牧业金额
	    	var show_ly=$("#show_ly").text();//林业明细
	    	var show_ly_je=$("#show_ly_je").text();//林业金额
	    	var show_qtsc=$("#show_qtsc").text();//生产其他明细
	    	var show_qtsc_je=$("#show_qtsc_je").text();//其他金额
	    	var show_zcst=$("#show_zcst").text();//生态补贴明细
	    	var show_zcst_je=$("#show_zcst_je").text();//生态补贴明细金额
	    	var show_yl=$("#show_yl").text();//养老金明细
	    	var show_yl_je=$("#show_yl_je").text();//养老金金额
	    	var show_db=$("#show_db").text();//低保补贴明细
	    	var show_db_je=$("#show_db_je").text();//低保补贴明细金额
	    	var show_rm=$("#show_rm").text();//燃煤补贴明细
	    	var show_rm_je=$("#show_rm_je").text();//燃煤补贴金额
	    	var show_zcqt=$("#show_zcqt").text();//政策其他明细
	    	var show_zcqt_je=$("#show_zcqt_je").text();//政策其他明细金额
	    	var show_td=$("#show_td").text();//土地流转明细
	    	var show_td_je=$("#show_td_je").text();//土地流转明细金额
	    	var show_tdqt=$("#show_tdqt").text();//土地其他明细
	    	var show_tdqt_je=$("#show_tdqt_je").text();//土地其他明细金额
	    	var show_gzmx1=$("#show_gzmx1").text();//工资明细1
	    	var show_gzmx1_je=$("#show_gzmx1_je").text();//工资1金额
	    	var show_gzmx2=$("#show_gzmx2").text();//工资明细2
	    	var show_gzmx2_je=$("#show_gzmx2_je").text();//工资2金额
	    	var show_gzqt1=$("#show_gzqt1").text();//其他明细1
	    	var show_gzqt1_je=$("#show_gzqt1_je").text();//明细1金额
	    	var show_gzqt2=$("#show_gzqt2").text();//明细2
	    	var show_gzqt2_je=$("#show_gzqt2_je").text();//明细2工资
	    	var xingming=(data.data1[0].v6);//户主姓名
	    	var show_gzxm1=$("#show_gzxm1").text();//工资项目1
	    	var show_gzxm2=$("#show_gzxm2").text();//工资项目2
	    	var show_qtxm1=$("#show_qtxm1").text();//其他项目1
	    	var show_qtxm2=$("#show_qtxm2").text();//其他项目2
	    	
	    	var show_wbj=$("#show_wbj").text();//五保金明细
	    	var show_wbj_je=$("#show_wbj_je").text();//五保金金额
	    	var show_jhsy=$("#show_jhsy").text();//计划生育金明细
	    	var show_jhsy_je=$("#show_jhsy_je").text();//计划生育金额
	    	
	    	$("#show_dqsrPie").html("<p><strong>当前收入情况统计图</strong></p><div id='pie-chart1' class='chart' style='height:550px;width:630px;'></div>");
	    	$("#show_dqsrline").html("<p></p><div id='pie-chart1_1' class='chart' style='height:550px;width:630px;'></div>");
	    	$("#show_dqsrbar").html("<p></p><div id='pie-chart1_2' class='chart' style='height:550px;width:630px;'></div>");
	    	
	    	 
	    	 if(show_wbj_je==""||show_wbj_je=="-"){
	    		 show_wbj_je="0";
	    	 }
	    	 if(show_jhsy_je==""||show_jhsy_je=="-"){
	    		 show_jhsy_je="0";
	    	 }
	    	 if(show_ny_je==""||show_ny_je=="-"){
	    		 show_ny_je="0";
	    	 }
	    	 if(show_cmy_je==""||show_cmy_je=="-"){
	    		 show_cmy_je="0";
	    	 }
	    	 if(show_ly_je==""||show_ly_je=="-"){
	    		 show_ly_je="0";
	    	 }
             if(show_qtsc_je==""||show_qtsc_je=="-"){
            	 show_qtsc_je="0";
	    	 }
             if(show_zcst_je==""||show_zcst_je=="-"){
            	 show_zcst_je="0";
	    	 }
             if(show_yl_je==""||show_yl_je=="-"){
            	 show_yl_je="0";
	    	 }
             if(show_db_je==""||show_db_je=="-"){
            	 show_db_je="0";
	    	 }
             if(show_rm_je==""||show_rm_je=="-"){
            	 show_rm_je="0";
	    	 }
             if(show_zcqt_je==""||show_zcqt_je=="-"){
            	 show_zcqt_je="0";
	    	 }
             if(show_td_je==""||show_td_je=="-"){
            	 show_td_je="0";
	    	 }   
             if(show_tdqt_je==""||show_tdqt_je=="-"){
            	 show_tdqt_je="0";
	    	 } 
             if(show_gzmx1_je==""||show_gzmx1_je=="-"){
            	 show_gzmx1_je="0";
	    	 } 
             if(show_gzmx2_je==""||show_gzmx2_je=="-"){
            	 show_gzmx2_je="0";
	    	 } 
             if(show_gzqt1_je==""||show_gzqt1_je=="-"){
            	 show_gzqt1_je="0";
	    	 } 
             if(show_gzqt2_je==""||show_gzqt2_je=="-"){
            	 show_gzqt2_je="0";
	    	 }
    		option = {
	    			tooltip: {
	    		        trigger: 'item',
	    		        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    		    },
	    		    legend: {
	    		        orient: 'vertical',
	    		        x: 'left',
	    		        data:["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
	    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)","五保金","计划生育金",show_gzxm1,
	    		              show_gzxm2,show_qtxm1,show_qtxm2]
	    		    },
	    		    series: [
	    		        {
	    		            name:xingming+"当前收入情况",
	    		            type:'pie',
	    		            radius: ['40%', '60%'],
	    		            avoidLabelOverlap: false,
	    		            label: {
	    		                normal: {
	    		                    show: false,
	    		                    position: 'center'
	    		                },
	    		                emphasis: {
	    		                    show: true,
	    		                    formatter: '{b} \r\n {c}(元)',
	    		                    textStyle: {
	    		                        fontSize: '30',
	    		                        fontWeight: 'bold'
	    		                    }
	    		                }
	    		            },
	    		            labelLine: {
	    		                normal: {
	    		                    show: false
	    		                }
	    		            },
	    		            data:[
	    		                  {value:show_ny_je, name:"农业（水产）"},
	    		                  {value:show_cmy_je, name:"畜牧业"},
	    		                  {value:show_ly_je, name:"林业"},
	    		                  {value:show_qtsc_je, name:"其他(生产经营)"},
	    		                  {value:show_zcst_je, name:"农林牧草、生态等补贴"},
	    		                  {value:show_yl_je, name:"养老金"},
	    		                  {value:show_db_je, name:"低保（五保）补贴"},
	    		                  {value:show_rm_je, name:"燃煤补贴"},
	    		                  {value:show_zcqt_je, name:"其他(政策性)"},
	    		                  {value:show_td_je, name:"土地、草牧场流转"},
	    		                  {value:show_tdqt_je, name:"其他(财产性)"},
	    		                  
	    		                  {value:show_wbj_je, name:"五保金"},
	    		                  {value:show_jhsy_je, name:"计划生育金"},
	    		                  
	    		                  {value:show_gzmx1_je, name:show_gzxm1},
	    		                  {value:show_gzmx2_je, name:show_gzxm2},
	    		                  {value:show_gzqt1_je, name:show_qtxm1},
	    		                  {value:show_gzqt2_je, name:show_qtxm2},
	    		            ]
	    		        }
	    		    ]};
    		var myChart = echarts.init(document.getElementById('pie-chart1'));
			myChart.setOption(option);
			
			option1_1 = {
					   tooltip: {//显示提示框
					       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//						       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
					   },
					   title : {//图名的显示
						   fontFamily :"SimSun",//字体格式
					    	x: 'center',//居中
					        text: "当前收入情况"//图名
					    },
					   legend: {//图中数据颜色的标识（选中的图例）
					       y: 'bottom',//Y轴的底部
					       data: ["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
		    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)","五保金","计划生育金",show_gzxm1,
		    		              show_gzxm2,show_qtxm1,show_qtxm2]//折线图折线的标识
					   },
					   xAxis: [//X轴
					       {
					           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
					           name: "明细",//坐标轴名称
					           axisLabel:{//坐标轴文本标签选项
		                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
		                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
		                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
					           },
					           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
					           data:["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
			    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)","五保金","计划生育金",show_gzxm1,
			    		              show_gzxm2,show_qtxm1,show_qtxm2]//X轴的坐标
					       }
					   ],
					   grid: { // 控制图的大小，调整下面这些值就可以，
				             x: 50,//离左边的间距
				             x2:100,//离右边的间距
				             y2: 230,//离下面的间距
				             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
				         },
				    
					   yAxis: [//Y轴
					       {
					    	   name:"金额（元）",//Y轴名称
					    	   type : 'value',
					    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
					    	   splitNumber: 5 //分隔段数，默认为5	
					       }
					   ],
					    toolbox: {//辅助工具箱
					       show: false//显示策略,可选true（显示）false（隐藏）
					   },
					   calculable: true,//是否启用拖拽重计算特性，默认关闭
					   series: [//数据系列
					       {
					           name: "收入明细",//名称
					           type: "line",//折线图
					           data: 
	    		                  [show_ny_je,show_cmy_je,show_ly_je,show_qtsc_je,show_zcst_je,show_yl_je,show_db_je,
	    		                   show_rm_je,show_zcqt_je,show_td_je,show_tdqt_je,show_wbj_je,show_jhsy_je,show_gzmx1_je,show_gzmx2_je,show_gzqt1_je,show_gzqt2_je]
					       }
					       
					   ]
					};
			var myChart1_1 = echarts.init(document.getElementById('pie-chart1_1'));
			myChart1_1.setOption(option1_1);
			
			option1_2 = {
					   tooltip: {//显示提示框
					       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//						       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
					   },
					   title : {//图名的显示
						   fontFamily :"SimSun",//字体格式
					    	x: 'center',//居中
					        text: "当前收入情况"//图名
					    },
					   legend: {//图中数据颜色的标识（选中的图例）
					       y: 'bottom',//Y轴的底部
					       data: ["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
		    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)","五保金","计划生育金",show_gzxm1,
		    		              show_gzxm2,show_qtxm1,show_qtxm2]//折线图折线的标识
					   },
					   xAxis: [//X轴
					       {
					           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
					           name: "明细",//坐标轴名称
					           axisLabel:{//坐标轴文本标签选项
		                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
		                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
		                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
					           },
					           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
					           data:["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
			    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)","五保金","计划生育金",show_gzxm1,
			    		              show_gzxm2,show_qtxm1,show_qtxm2]//X轴的坐标
					       }
					   ],
					   grid: { // 控制图的大小，调整下面这些值就可以，
				             x: 50,//离左边的间距
				             x2:100,//离右边的间距
				             y2: 230,//离下面的间距
				             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
				         },
				    
					   yAxis: [//Y轴
					       {
					    	   name:"金额（元）",//Y轴名称
					    	   type : 'value',
					    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
					    	   splitNumber: 5 //分隔段数，默认为5	
					       }
					   ],
					    toolbox: {//辅助工具箱
					       show: false//显示策略,可选true（显示）false（隐藏）
					   },
					   calculable: true,//是否启用拖拽重计算特性，默认关闭
					   series: [//数据系列
					       {
					           name: "明细",//名称
					           type: "bar",//折线图
					           data: 
	    		                  [show_ny_je,show_cmy_je,show_ly_je,show_qtsc_je,show_zcst_je,show_yl_je,show_db_je,
	    		                   show_rm_je,show_zcqt_je,show_td_je,show_tdqt_je,show_wbj_je,show_jhsy_je,show_gzmx1_je,show_gzmx2_je,show_gzqt1_je,show_gzqt2_je]
					       }
					   ]
					};
			var myChart1_2 = echarts.init(document.getElementById('pie-chart1_2'));
			myChart1_2.setOption(option1_2);
	    	
	    	
	    	//当前支出情况
	    	if(data.data6==""||data.data6==null||data.data6==undefined){
	    		var b=0;
	    		$("#show_dqnzzc").text('0');
	    	}else{
	    		
	    		
		    	$("#show_nzmx").text(data.data6[0].dqzc_v1);//农资明细
		    	$("#show_nzmx_je").text(data.data6[0].dqzc_v2);//农资明细金额
		    	$("#show_gdzc").text(data.data6[0].dqzc_v3);//固定资产折旧明细
		    	$("#show_gdzc_je").text(data.data6[0].dqzc_v4);//固定资产折旧明细金额
		    	$("#show_sdrl").text(data.data6[0].dqzc_v5);//水电明细
		    	$("#show_sdrl_je").text(data.data6[0].dqzc_v6);//水电明细金额
		    	$("#show_cbtd").text(data.data6[0].dqzc_v7);//承包土地明细
		    	$("#show_cbtd_je").text(data.data6[0].dqzc_v8);//承包土地明细金额
		    	$("#show_scl").text(data.data6[0].dqzc_v9);//饲草料明细
		    	$("#show_scl_je").text(data.data6[0].dqzc_v10);//饲草料明细金额
		    	$("#show_fyfz").text(data.data6[0].dqzc_v11);//防疫明细
		    	$("#show_fyfz_je").text(data.data6[0].dqzc_v12);//防疫明细金额
		    	$("#show_zzc").text(data.data6[0].dqzc_v13);//种畜明细
		    	$("#show_zzc_je").text(data.data6[0].dqzc_v14);//种畜明细金额
		    	$("#show_xstx").text(data.data6[0].dqzc_v15);//销售通讯明细
		    	$("#show_xstx_je").text(data.data6[0].dqzc_v16);//销售通讯明细金额
		    	$("#show_dk").text(data.data6[0].dqzc_v17);//借贷明细
		    	$("#show_dk_je").text(data.data6[0].dqzc_v18);//借贷明细金额
		    	
		    	$("#show_zczcxm1").text(data.data6[0].dqzc_v23);//政策项目1
		    	$("#show_zcmx").text(data.data6[0].dqzc_v19);//政策明细1
		    	$("#show_zcmx_je").text(data.data6[0].dqzc_v20);//政策明细1金额
		    	
		    	$("#show_zczcxm2").text(data.data6[0].dqzc_v24);//政策项目2
		    	$("#show_zcmx2").text(data.data6[0].dqzc_v21);//政策明细2
		    	$("#show_zcmx2_je").text(data.data6[0].dqzc_v22);//政策明细2细金额
		    	
		    	
		    	$("#show_zcqtxm1").text(data.data6[0].dqzc_v25);//其他项目1
		    	$("#show_zcqtxmmx1").text(data.data6[0].dqzc_v26);//其他明细1
		    	$("#show_zcqtxm1_je").text(data.data6[0].dqzc_v27);//其他金额1
		    	
		    	$("#show_zcqtxm2").text(data.data6[0].dqzc_v28);//其他项目2
		    	$("#show_zcqtxmmx2").text(data.data6[0].dqzc_v29);//其他明细2
		    	$("#show_zcqtxm2_je").text(data.data6[0].dqzc_v30);//其他金额2
		    	
		    	$("#show_zongzc").text(data.data6[0].dqzc_v31);//年总支出
		    	
		    	$("#show_dqnzzc_1").text(data.data6[0].dqzc_v31);//年总支出
		    	$("#show_nzmx_1").text(data.data6[0].dqzc_v1);//农资明细
		    	$("#show_nzmx_je_1").text(data.data6[0].dqzc_v2);//农资明细金额
		    	$("#show_gdzc_1").text(data.data6[0].dqzc_v3);//固定资产折旧明细
		    	$("#show_gdzc_je_1").text(data.data6[0].dqzc_v4);//固定资产折旧明细金额
		    	$("#show_sdrl_1").text(data.data6[0].dqzc_v5);//水电明细
		    	$("#show_sdrl_je_1").text(data.data6[0].dqzc_v6);//水电明细金额
		    	$("#show_cbtd_1").text(data.data6[0].dqzc_v7);//承包土地明细
		    	$("#show_cbtd_je_1").text(data.data6[0].dqzc_v8);//承包土地明细金额
		    	$("#show_scl_1").text(data.data6[0].dqzc_v9);//饲草料明细
		    	$("#show_scl_je_1").text(data.data6[0].dqzc_v10);//饲草料明细金额
		    	$("#show_fyfz_1").text(data.data6[0].dqzc_v11);//防疫明细
		    	$("#show_fyfz_je_1").text(data.data6[0].dqzc_v12);//防疫明细金额
		    	$("#show_zzc_1").text(data.data6[0].dqzc_v13);//种畜明细
		    	$("#show_zzc_je_1").text(data.data6[0].dqzc_v14);//种畜明细金额
		    	$("#show_xstx_1").text(data.data6[0].dqzc_v15);//销售通讯明细
		    	$("#show_xstx_je_1").text(data.data6[0].dqzc_v16);//销售通讯明细金额
		    	$("#show_dk_1").text(data.data6[0].dqzc_v17);//借贷明细
		    	$("#show_dk_je_1").text(data.data6[0].dqzc_v18);//借贷明细金额
		    	
		    	$("#show_zczcxm1_1").text(data.data6[0].dqzc_v23);//政策项目1
		    	$("#show_zcmx_1").text(data.data6[0].dqzc_v19);//政策明细1
		    	$("#show_zcmx_je_1").text(data.data6[0].dqzc_v20);//政策明细1金额
		    	
		    	$("#show_zczcxm2_1").text(data.data6[0].dqzc_v24);//政策项目2
		    	$("#show_zcmx2_1").text(data.data6[0].dqzc_v21);//政策明细2
		    	$("#show_zcmx2_je_1").text(data.data6[0].dqzc_v22);//政策明细2细金额
		    	
		    	
		    	$("#show_zcqtxm1_1").text(data.data6[0].dqzc_v25);//其他项目1
		    	$("#show_zcqtxmmx1_1").text(data.data6[0].dqzc_v26);//其他明细1
		    	$("#show_zcqtxm1_je_1").text(data.data6[0].dqzc_v27);//其他金额1
		    	
		    	$("#show_zcqtxm2_1").text(data.data6[0].dqzc_v28);//其他项目2
		    	$("#show_zcqtxmmx2_1").text(data.data6[0].dqzc_v29);//其他明细2
		    	$("#show_zcqtxm2_je_1").text(data.data6[0].dqzc_v30);//其他金额2
		    	
		    	
		    	if(data.data6[0].dqzc_v31==""||data.data6[0].dqzc_v31==null||data.data6[0].dqzc_v31==undefined){
		    		$("#show_zongzc_1").text("0");//年总支出
		    		var b=0;
		    		$("#show_dqnzzc").text("0");//年总支出
		    	}else{
		    		$("#show_zongzc_1").text(data.data6[0].dqzc_v31);//年总支出
		    		$("#show_dqnzzc").text(data.data6[0].dqzc_v31);//年总支出
		    		var b=data.data6[0].dqzc_v31;
		    	}
	    	}
	    	
	    	//收支分析
	    	if(a=="-"||b=="-"){
	    		$("#show_dqniancsr").text("-");//年纯收入
		    	$("#show_dqncsr").text("-");//年总收入
		    	$("#show_dqjtrs").text(data.data1[0].v9);//人数
		    	$("#show_dqnrj").text("-");//年人均收入
	    	}else{
//	    		$("#show_dqniancsr").text(a-b);//年纯收入
	    		if((parseFloat(a)-parseFloat(b)).toFixed(2)=="NaN"){
	    			$("#show_dqniancsr").text("0");//年纯收入
			    	$("#show_dqncsr").text("0");//年总收入
			    	$("#show_dqnrj").text((("0")/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}else{
	    			$("#show_dqniancsr").text((parseFloat(a)-parseFloat(b)).toFixed(2));//年纯收入
			    	$("#show_dqncsr").text((parseFloat(a)-parseFloat(b)).toFixed(2));//年总收入
			    	$("#show_dqnrj").text(((parseFloat(a)-parseFloat(b))/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}
	    		
		    	$("#show_dqjtrs").text(data.data1[0].v9);//人数
		    	
	    	}
	    	
	    	//当前支出饼图
	    	$("#show_dqzcPie").html("<p><strong>当前支出情况统计图</strong></p><div id='pie-chart2' class='chart' style='height:550px;width:630px;'></div>");
	    	$("#show_dqzclin").html("<p></p><div id='pie-chart2_2' class='chart' style='height:550px;width:630px;'></div>");
	    	$("#show_dqzcbar").html("<p></p><div id='pie-chart2_3' class='chart' style='height:550px;width:630px;'></div>");
	    	if(data.data6[0].dqzc_v2==""||data.data6[0].dqzc_v2=="-"){
	    		data.data6[0].dqzc_v2="0"
	    	}
	    	if(data.data6[0].dqzc_v4==""||data.data6[0].dqzc_v4=="-"){
	    		data.data6[0].dqzc_v4="0"
	    	}
	    	if(data.data6[0].dqzc_v6==""||data.data6[0].dqzc_v6=="-"){
	    		data.data6[0].dqzc_v6="0"
	    	}
	    	if(data.data6[0].dqzc_v8==""||data.data6[0].dqzc_v8=="-"){
	    		data.data6[0].dqzc_v8="0"
	    	}
	    	if(data.data6[0].dqzc_v10==""||data.data6[0].dqzc_v10=="-"){
	    		data.data6[0].dqzc_v10="0"
	    	}
	    	if(data.data6[0].dqzc_v12==""||data.data6[0].dqzc_v12=="-"){
	    		data.data6[0].dqzc_v12="0"
	    	}
	    	if(data.data6[0].dqzc_v14==""||data.data6[0].dqzc_v14=="-"){
	    		data.data6[0].dqzc_v14="0"
	    	}
	    	if(data.data6[0].dqzc_v16==""||data.data6[0].dqzc_v16=="-"){
	    		data.data6[0].dqzc_v16="0"
	    	}
	    	if(data.data6[0].dqzc_v18==""||data.data6[0].dqzc_v18=="-"){
	    		data.data6[0].dqzc_v18="0"
	    	}
	    	if(data.data6[0].dqzc_v20==""||data.data6[0].dqzc_v20=="-"){
	    		data.data6[0].dqzc_v20="0"
	    	}
	    	if(data.data6[0].dqzc_v22==""||data.data6[0].dqzc_v22=="-"){
	    		data.data6[0].dqzc_v22="0"
	    	}
	    	if(data.data6[0].dqzc_v27==""||data.data6[0].dqzc_v27=="-"){
	    		data.data6[0].dqzc_v27="0"
	    	}
	    	if(data.data6[0].dqzc_v30==""||data.data6[0].dqzc_v30=="-"){
	    		data.data6[0].dqzc_v30="0"
	    	}
    		option2 = {
    			tooltip: {
    		        trigger: 'item',
    		        formatter: "{a} <br/>{b}: {c} ({d}%)"
    		    },
    		    legend: {
    		        orient: 'vertical',
    		        x: 'left',
    		        data:["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
    		              "饲草料","防疫防治支出","种（仔）畜",
    		             "销售费用和通讯费用","借贷",
    		             data.data6[0].dqzc_v23,data.data6[0].dqzc_v24,data.data6[0].dqzc_v25,data.data6[0].dqzc_v28]
    		    },
    		    series: [
    		        {
    		            name:xingming+"当前支出情况",
    		            type:'pie',
    		            radius: ['40%', '60%'],
    		            avoidLabelOverlap: false,
    		            label: {
    		                normal: {
    		                    show: false,
    		                    position: 'center'
    		                },
    		                emphasis: {
    		                	formatter: '{b} \r\n {c}(元)',
    		                    show: true,
    		                    textStyle: {
    		                        fontSize: '30',
    		                        fontWeight: 'bold'
    		                    }
    		                }
    		            },
    		            labelLine: {
    		                normal: {
    		                    show: false
    		                }
    		            },
    		            data:[
								{value:data.data6[0].dqzc_v2, name:"农资费用"},
								{value:data.data6[0].dqzc_v4, name:"固定资产折旧和租赁费"},
								{value:data.data6[0].dqzc_v6, name:"水电燃料支出"},
								{value:data.data6[0].dqzc_v8, name:"承包土地、草场费用"},
								
								{value:data.data6[0].dqzc_v10, name:"饲草料"},
								{value:data.data6[0].dqzc_v12, name:"防疫防治支出"},
								{value:data.data6[0].dqzc_v14, name:"种（仔）畜"},
								{value:data.data6[0].dqzc_v16, name:"销售费用和通讯费用"},
								{value:data.data6[0].dqzc_v18, name:"借贷"},
								{value:data.data6[0].dqzc_v20, name:data.data6[0].dqzc_v23},
								{value:data.data6[0].dqzc_v22, name:data.data6[0].dqzc_v24},
								{value:data.data6[0].dqzc_v27, name:data.data6[0].dqzc_v25},
								{value:data.data6[0].dqzc_v30, name:data.data6[0].dqzc_v28},
    		            ]
    		        }
    		    ]};
			var myChart2 = echarts.init(document.getElementById('pie-chart2'));
			myChart2.setOption(option2);
			
			option2_2 = {
					   tooltip: {//显示提示框
					       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//						       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
					   },
					   title : {//图名的显示
						   fontFamily :"SimSun",//字体格式
					    	x: 'center',//居中
					        text: "当前支出情况"//图名
					    },
					   legend: {//图中数据颜色的标识（选中的图例）
					       y: 'bottom',//Y轴的底部
					       data: ["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
		    		              "饲草料","防疫防治支出","种（仔）畜",
			    		             "销售费用和通讯费用","借贷",
			    		             data.data6[0].dqzc_v23,data.data6[0].dqzc_v24,data.data6[0].dqzc_v25,data.data6[0].dqzc_v28]//折线图折线的标识
					   },
					   xAxis: [//X轴
					       {
					           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
					           name: "明细",//坐标轴名称
					           axisLabel:{//坐标轴文本标签选项
		                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
		                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
		                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
					           },
					           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
					           data:["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
			    		              "饲草料","防疫防治支出","种（仔）畜",
				    		             "销售费用和通讯费用","借贷",
				    		             data.data6[0].dqzc_v23,data.data6[0].dqzc_v24,data.data6[0].dqzc_v25,data.data6[0].dqzc_v28]//X轴的坐标
					       }
					   ],
					   grid: { // 控制图的大小，调整下面这些值就可以，
				             x: 50,//离左边的间距
				             x2:100,//离右边的间距
				             y2: 230,//离下面的间距
				             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
				         },
				    
					   yAxis: [//Y轴
					       {
					    	   name:"金额（元）",//Y轴名称
					    	   type : 'value',
					    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
					    	   splitNumber: 5 //分隔段数，默认为5	
					       }
					   ],
					    toolbox: {//辅助工具箱
					       show: false//显示策略,可选true（显示）false（隐藏）
					   },
					   calculable: true,//是否启用拖拽重计算特性，默认关闭
					   series: [//数据系列
					       {
					           name: "支出明细",//名称
					           type: "line",//折线图
					           data: 
	    		                  [
	    		                   data.data6[0].dqzc_v2,data.data6[0].dqzc_v4,data.data6[0].dqzc_v6,data.data6[0].dqzc_v8
	    		                   ,data.data6[0].dqzc_v10,data.data6[0].dqzc_v12,data.data6[0].dqzc_v14,
	    		                   data.data6[0].dqzc_v16,data.data6[0].dqzc_v18,data.data6[0].dqzc_v20,data.data6[0].dqzc_v22,
	    		                   data.data6[0].dqzc_v27,data.data6[0].dqzc_v30]
					       }
					   ]
					};
			var myChart2_2 = echarts.init(document.getElementById('pie-chart2_2'));
			myChart2_2.setOption(option2_2);
			
			option2_3 = {
					   tooltip: {//显示提示框
					       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//						       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
					   },
					   title : {//图名的显示
						   fontFamily :"SimSun",//字体格式
					    	x: 'center',//居中
					        text: "当前支出情况"//图名
					    },
					   legend: {//图中数据颜色的标识（选中的图例）
					       y: 'bottom',//Y轴的底部
					       data: ["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
		    		              "饲草料","防疫防治支出","种（仔）畜",
			    		             "销售费用和通讯费用","借贷",
			    		             data.data6[0].dqzc_v23,data.data6[0].dqzc_v24,data.data6[0].dqzc_v25,data.data6[0].dqzc_v28]//折线图折线的标识
					   },
					   xAxis: [//X轴
					       {
					           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
					           name: "明细",//坐标轴名称
					           axisLabel:{//坐标轴文本标签选项
		                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
		                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
		                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
					           },
					           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
					           data:["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
			    		              "饲草料","防疫防治支出","种（仔）畜",
				    		             "销售费用和通讯费用","借贷",
				    		             data.data6[0].dqzc_v23,data.data6[0].dqzc_v24,data.data6[0].dqzc_v25,data.data6[0].dqzc_v28]//X轴的坐标
					       }
					   ],
					   grid: { // 控制图的大小，调整下面这些值就可以，
				             x: 50,//离左边的间距
				             x2:100,//离右边的间距
				             y2: 230,//离下面的间距
				             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
				         },
				    
					   yAxis: [//Y轴
					       {
					    	   name:"金额（元）",//Y轴名称
					    	   type : 'value',
					    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
					    	   splitNumber: 5 //分隔段数，默认为5	
					       }
					   ],
					    toolbox: {//辅助工具箱
					       show: false//显示策略,可选true（显示）false（隐藏）
					   },
					   calculable: true,//是否启用拖拽重计算特性，默认关闭
					   series: [//数据系列
					       {
					           name: "支出明细",//名称
					           type: "bar",//折线图
					           data: 
	    		                  [data.data6[0].dqzc_v2,data.data6[0].dqzc_v4,data.data6[0].dqzc_v6,data.data6[0].dqzc_v8
	    		                   ,data.data6[0].dqzc_v10,data.data6[0].dqzc_v12,data.data6[0].dqzc_v14,
	    		                   data.data6[0].dqzc_v16,data.data6[0].dqzc_v18,data.data6[0].dqzc_v20,data.data6[0].dqzc_v22,
	    		                   data.data6[0].dqzc_v27,data.data6[0].dqzc_v30]
					       }
					   ]
					};
			var myChart2_3= echarts.init(document.getElementById('pie-chart2_3'));
			myChart2_3.setOption(option2_3);
	    	//帮扶后收入
	    	if(data.data11==""||data.data11==null||data.data11==undefined){
	    		var aa=0;
	    		$("#show_hdqzongsr").text('0');
	    	}else{
	    		$("#show_hny").text(data.data11[0].dqsrh_v1);//农业明细
		    	$("#show_hny_je").text(data.data11[0].dqsrh_v2);//农业明细金额
		    	$("#show_hcmy").text(data.data11[0].dqsrh_v3);//畜牧业明细
		    	$("#show_hcmy_je").text(data.data11[0].dqsrh_v4);//畜牧业金额
		    	$("#show_hly").text(data.data11[0].dqsrh_v5);//林业明细
		    	$("#show_hly_je").text(data.data11[0].dqsrh_v6);//林业金额
		    	$("#show_hqtsc").text(data.data11[0].dqsrh_v7);//生产其他明细
		    	$("#show_hqtsc_je").text(data.data11[0].dqsrh_v8);//其他金额
		    	$("#show_hscxj").text(data.data11[0].dqsrh_v9);//生产小计明细
		    	$("#show_hscxj_je").text(data.data11[0].dqsrh_v10);//生产小计明细金额
		    	$("#show_hzcst").text(data.data11[0].dqsrh_v11);//生态补贴明细
		    	$("#show_hzcst_je").text(data.data11[0].dqsrh_v12);//生态补贴明细金额
		    	$("#show_hyl").text(data.data11[0].dqsrh_v13);//养老金明细
		    	$("#show_hyl_je").text(data.data11[0].dqsrh_v14);//养老金金额
		    	$("#show_hdb").text(data.data11[0].dqsrh_v15);//低保补贴明细
		    	$("#show_hdb_je").text(data.data11[0].dqsrh_v16);//低保补贴明细金额
		    	$("#show_hrm").text(data.data11[0].dqsrh_v17);//燃煤补贴明细
		    	$("#show_hrm_je").text(data.data11[0].dqsrh_v18);//燃煤补贴金额
		    	$("#show_hzcqt").text(data.data11[0].dqsrh_v19);//政策其他明细
		    	$("#show_hzcqt_je").text(data.data11[0].dqsrh_v20);//政策其他明细金额
		    	$("#show_hzcxj").text(data.data11[0].dqsrh_v21);//政策小计明细
		    	$("#show_hzcxj_je").text(data.data11[0].dqsrh_v22);//政策小计明细金额
		    	$("#show_htd").text(data.data11[0].dqsrh_v23);//土地流转明细
		    	$("#show_htd_je").text(data.data11[0].dqsrh_v24);//土地流转明细金额
		    	$("#show_htdqt").text(data.data11[0].dqsrh_v25);//土地其他明细
		    	$("#show_htdqt_je").text(data.data11[0].dqsrh_v26);//土地其他明细金额
		    	
		    	$("#show_hgzxm1").text(data.data11[0].dqsrh_v35);//工资项目1
		    	$("#show_hgzmx1").text(data.data11[0].dqsrh_v27);//工资明细1
		    	$("#show_hgzmx1_je").text(data.data11[0].dqsrh_v28);//工资1金额
		    	
		    	$("#show_hgzxm2").text(data.data11[0].dqsrh_v36);//工资项目2
		    	$("#show_hgzmx2").text(data.data11[0].dqsrh_v29);//工资明细2
		    	$("#show_hgzmx2_je").text(data.data11[0].dqsrh_v30);//工资2金额
		    	
		    	$("#show_hqtxm1").text(data.data11[0].dqsrh_v37);//其他项目1
		    	$("#show_hgzqt1").text(data.data11[0].dqsrh_v31);//其他明细1
		    	$("#show_hgzqt1_je").text(data.data11[0].dqsrh_v32);//明细1金额
		    	
		    	$("#show_hqtxm2").text(data.data11[0].dqsrh_v38);//其他项目2
		    	$("#show_hgzqt2").text(data.data11[0].dqsrh_v33);//明细2
		    	$("#show_hgzqt2_je").text(data.data11[0].dqsrh_v34);//明细2工资
		    	
		    	$("#show_hsrzj").text(data.data11[0].dqsrh_v39);//总计
		    	if(data.data11[0].dqsrh_v39==""||data.data11[0].dqsrh_v39==null||data.data11[0].dqsrh_v39==undefined){
		    		$("#show_hdqzongsr").text("0");//年总收入
		    		var aa=0;
		    	}else{
		    		$("#show_hdqzongsr").text(data.data11[0].dqsrh_v39);//年总收入
		    		var aa=data.data11[0].dqsrh_v39;
		    	}
		    	
		    	
	    	}
	    	
		    	//帮扶后收入饼图
		    	var show_hny= $("#show_hny").text();//农业明细
		    	var show_hny_je= $("#show_hny_je").text();//农业明细金额
		    	var show_hcmy= $("#show_hcmy").text();//畜牧业明细
		    	var show_hcmy_je=$("#show_hcmy_je").text();//畜牧业金额
		    	var show_hly=$("#show_hly").text();//林业明细
		    	var show_hly_je=$("#show_hly_je").text();//林业金额
		    	var show_hqtsc=$("#show_hqtsc").text();//生产其他明细
		    	var show_hqtsc_je=$("#show_hqtsc_je").text();//其他金额
		    	var show_hzcst=$("#show_hzcst").text();//生态补贴明细
		    	var show_hzcst_je=$("#show_hzcst_je").text();//生态补贴明细金额
		    	var show_hyl=$("#show_hyl").text();//养老金明细
		    	var show_hyl_je=$("#show_hyl_je").text();//养老金金额
		    	var show_hdb=$("#show_hdb").text();//低保补贴明细
		    	var show_hdb_je=$("#show_hdb_je").text();//低保补贴明细金额
		    	var show_hrm=$("#show_hrm").text();//燃煤补贴明细
		    	var show_hrm_je=$("#show_hrm_je").text();//燃煤补贴金额
		    	var show_hzcqt=$("#show_hzcqt").text();//政策其他明细
		    	var show_hzcqt_je=$("#show_hzcqt_je").text();//政策其他明细金额
		    	var show_htd=$("#show_htd").text();//土地流转明细
		    	var show_htd_je=$("#show_htd_je").text();//土地流转明细金额
		    	var show_htdqt=$("#show_htdqt").text();//土地其他明细
		    	var show_htdqt_je=$("#show_htdqt_je").text();//土地其他明细金额
		    	var show_hgzmx1=$("#show_hgzmx1").text();//工资明细1
		    	var show_hgzmx1_je=$("#show_hgzmx1_je").text();//工资1金额
		    	var show_hgzmx2=$("#show_hgzmx2").text();//工资明细2
		    	var show_hgzmx2_je=$("#show_hgzmx2_je").text();//工资2金额
		    	var show_hgzqt1=$("#show_hgzqt1").text();//其他明细1
		    	var show_hgzqt1_je=$("#show_hgzqt1_je").text();//明细1金额
		    	var show_hgzqt2=$("#show_hgzqt2").text();//明细2
		    	var show_hgzqt2_je=$("#show_hgzqt2_je").text();//明细2工资
		    	var show_hgzxm1= $("#show_hgzxm1").text();//工资项目1
		    	var show_hgzxm2=$("#show_hgzxm2").text();//工资项目2
		    	var show_hqtxm1=$("#show_hqtxm1").text();//其他项目1
		    	var show_hqtxm2=$("#show_hqtxm2").text();//其他项目2
		    	$("#show_bfhsz").html("<p><strong>帮扶后收入情况统计图</strong></p><div id='pie-chart3' class='chart' style='height:550px;width:630px;'></div>");
		    	$("#show_bfhszline").html("<p></p><div id='pie-chart3_2' class='chart' style='height:550px;width:630px;'></div>");
		    	$("#show_bfhszbar").html("<div id='pie-chart3_3' class='chart' style='height:550px;width:630px;'></div>");
	    		if(show_hny_je==""||show_hny_je=="-"){
	    			show_hny_je="0";
	    		}	
	    		if(show_hcmy_je==""||show_hcmy_je=="-"){
	    			show_hcmy_je="0";
	    		}
	    		if(show_hly_je==""||show_hly_je=="-"){
	    			show_hly_je="0";
	    		}
	    		if(show_hqtsc_je==""||show_hqtsc_je=="-"){
	    			show_hqtsc_je="0";
	    		}
	    		if(show_hzcst_je==""||show_hzcst_je=="-"){
	    			show_hzcst_je="0";
	    		}
	    		if(show_hyl_je==""||show_hyl_je=="-"){
	    			show_hyl_je="0";
	    		}
	    		if(show_hdb_je==""||show_hdb_je=="-"){
	    			show_hdb_je="0";
	    		}
	    		if(show_hrm_je==""||show_hrm_je=="-"){
	    			show_hrm_je="0";
	    		}
	    		if(show_hzcqt_je==""||show_hzcqt_je=="-"){
	    			show_hzcqt_je="0";
	    		}
	    		if(show_htd_je==""||show_htd_je=="-"){
	    			show_htd_je="0";
	    		}
	    		if(show_htdqt_je==""||show_htdqt_je=="-"){
	    			show_htdqt_je="0";
	    		}
	    		if(show_hgzmx1_je==""||show_hgzmx1_je=="-"){
	    			show_hgzmx1_je="0";
	    		}
	    		if(show_hgzmx2_je==""||show_hgzmx2_je=="-"){
	    			show_hgzmx2_je="0";
	    		}
	    		if(show_hgzqt1_je==""||show_hgzqt1_je=="-"){
	    			show_hgzqt1_je="0";
	    		}
	    		if(show_hgzqt2_je==""||show_hgzqt2_je=="-"){
	    			show_hgzqt2_je="0";
	    		}
	    		option3 = {
	    			tooltip: {
	    		        trigger: 'item',
	    		        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    		    },
	    		    legend: {
	    		        orient: 'vertical',
	    		        x: 'left',
	    		        data:["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
	    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)",show_hgzxm1,
	    		              show_hgzxm2,show_hqtxm1,show_hqtxm2]
	    		    },
	    		    series: [
	    		        {
	    		            name:xingming+"当前收入情况",
	    		            type:'pie',
	    		            radius: ['40%', '60%'],
	    		            avoidLabelOverlap: false,
	    		            label: {
	    		                normal: {
	    		                    show: false,
	    		                    position: 'center'
	    		                },
	    		                emphasis: {
	    		                    show: true,
	    		                    formatter: '{b} \r\n {c}(元)',
	    		                    textStyle: {
	    		                        fontSize: '30',
	    		                        fontWeight: 'bold'
	    		                    }
	    		                }
	    		            },
	    		            labelLine: {
	    		                normal: {
	    		                    show: false
	    		                }
	    		            },
	    		            data:[
	    		                  {value:show_hny_je, name: "农业（水产）"},
	    		                  {value:show_hcmy_je, name:"畜牧业"},
	    		                  {value:show_hly_je, name:"林业"},
	    		                  {value:show_hqtsc_je, name:"其他(生产经营)"},
	    		                  {value:show_hzcst_je, name:"农林牧草、生态等补贴"},
	    		                  {value:show_hyl_je, name:"养老金"},
	    		                  {value:show_hdb_je, name:"低保（五保）补贴"},
	    		                  {value:show_hrm_je, name:"燃煤补贴"},
	    		                  {value:show_hzcqt_je, name:"其他(政策性)"},
	    		                  {value:show_htd_je, name:"土地、草牧场流转"},
	    		                  {value:show_htdqt_je, name:"其他(财产性)"},
	    		                  {value:show_hgzmx1_je, name:show_hgzxm1},
	    		                  {value:show_hgzmx2_je, name:show_hgzxm2},
	    		                  {value:show_hgzqt1_je, name:show_hqtxm1},
	    		                  {value:show_hgzqt2_je, name:show_hqtxm2},
	    		            ]
	    		        }
	    		    ]};
				var myChart3 = echarts.init(document.getElementById('pie-chart3'));
				myChart3.setOption(option3);
    	
    	

				option3_2 = {
					   tooltip: {//显示提示框
					       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//							       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
					   },
					   title : {//图名的显示
						   fontFamily :"SimSun",//字体格式
					    	x: 'center',//居中
					        text: "帮扶后收入情况"//图名
					    },
					   legend: {//图中数据颜色的标识（选中的图例）
					       y: 'bottom',//Y轴的底部
					       data: ["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
		    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)",show_hgzxm1,
		    		              show_hgzxm2,show_hqtxm1,show_hqtxm2]//折线图折线的标识
					   },
					   xAxis: [//X轴
					       {
					           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
					           name: "明细",//坐标轴名称
					           axisLabel:{//坐标轴文本标签选项
		                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
		                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
		                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
					           },
					           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
					           data:["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
			    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)",show_hgzxm1,
			    		              show_hgzxm2,show_hqtxm1,show_hqtxm2]//X轴的坐标
					       }
					   ],
					   grid: { // 控制图的大小，调整下面这些值就可以，
				             x: 50,//离左边的间距
				             x2:100,//离右边的间距
				             y2: 230,//离下面的间距
				             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
				         },
				    
					   yAxis: [//Y轴
					       {
					    	   name:"金额（元）",//Y轴名称
					    	   type : 'value',
					    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
					    	   splitNumber: 5 //分隔段数，默认为5	
					       }
					   ],
					    toolbox: {//辅助工具箱
					       show: false//显示策略,可选true（显示）false（隐藏）
					   },
					   calculable: true,//是否启用拖拽重计算特性，默认关闭
					   series: [//数据系列
					       {
					           name: "收入明细",//名称
					           type: "line",//折线图
					           data: 
	    		                  [show_hny_je,show_hcmy_je,show_hly_je,show_hqtsc_je,show_hzcst_je,show_hyl_je,show_hdb_je,
					        	   show_hrm_je,show_hzcqt_je,show_htd_je,show_htdqt_je,show_hgzmx1_je,show_hgzmx2_je,show_hgzqt1_je,show_hgzqt2_je]
					       }
					   ]
					};
				var myChart3_2 = echarts.init(document.getElementById('pie-chart3_2'));
				myChart3_2.setOption(option3_2);
				
				option3_3 = {
					   tooltip: {//显示提示框
					       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//							       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
					   },
					   title : {//图名的显示
						   fontFamily :"SimSun",//字体格式
					    	x: 'center',//居中
					        text: "帮扶后收入情况"//图名
					    },
					   legend: {//图中数据颜色的标识（选中的图例）
					       y: 'bottom',//Y轴的底部
					       data: ["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
		    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)",show_hgzxm1,
		    		              show_hgzxm2,show_hqtxm1,show_hqtxm2]//折线图折线的标识
					   },
					   xAxis: [//X轴
					       {
					           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
					           name: "明细",//坐标轴名称
					           axisLabel:{//坐标轴文本标签选项
		                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
		                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
		                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
					           },
					           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
					           data:["农业（水产）","畜牧业","林业","其他(生产经营)","农林牧草、生态等补贴","养老金",
			    		              "低保（五保）补贴","燃煤补贴","其他(政策性)","土地、草牧场流转","其他(财产性)",show_hgzxm1,
			    		              show_hgzxm2,show_hqtxm1,show_hqtxm2]//X轴的坐标
					       }
					   ],
					   grid: { // 控制图的大小，调整下面这些值就可以，
				             x: 50,//离左边的间距
				             x2:100,//离右边的间距
				             y2: 230,//离下面的间距
				             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
				         },
				    
					   yAxis: [//Y轴
					       {
					    	   name:"金额（元）",//Y轴名称
					    	   type : 'value',
					    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
					    	   splitNumber: 5 //分隔段数，默认为5	
					       }
					   ],
					    toolbox: {//辅助工具箱
					       show: false//显示策略,可选true（显示）false（隐藏）
					   },
					   calculable: true,//是否启用拖拽重计算特性，默认关闭
					   series: [//数据系列
					       {
					           name: "明细",//名称
					           type: "bar",//折线图
					           data: 
	    		                  [show_hny_je,show_hcmy_je,show_hly_je,show_hqtsc_je,show_hzcst_je,show_hyl_je,show_hdb_je,
					        	   show_hrm_je,show_hzcqt_je,show_htd_je,show_htdqt_je,show_hgzmx1_je,show_hgzmx2_je,show_hgzqt1_je,show_hgzqt2_je]
					       }
					   ]
					};
				var myChart3_3 = echarts.init(document.getElementById('pie-chart3_3'));
				myChart3_3.setOption(option3_3);
			    	
	    	
	    	
	    	
	    	//帮扶后支出
	    	if(data.data12==""||data.data12==null||data.data12==undefined){
	    		var bb=0;
	    		$("#show_hdqnzzc").text('0');//年总支出
	    	}else{
	    		$("#show_hnzmx").text(data.data12[0].dqzch_v1);//农资明细
		    	$("#show_hnzmx_je").text(data.data12[0].dqzch_v2);//农资明细金额
		    	$("#show_hgdzc").text(data.data12[0].dqzch_v3);//固定资产折旧明细
		    	$("#show_hgdzc_je").text(data.data12[0].dqzch_v4);//固定资产折旧明细金额
		    	$("#show_hsdrl").text(data.data12[0].dqzch_v5);//水电明细
		    	$("#show_hsdrl_je").text(data.data12[0].dqzch_v6);//水电明细金额
		    	$("#show_hcbtd").text(data.data12[0].dqzch_v7);//承包土地明细
		    	$("#show_hcbtd_je").text(data.data12[0].dqzch_v8);//承包土地明细金额
		    	$("#show_hscl").text(data.data12[0].dqzch_v9);//饲草料明细
		    	$("#show_hscl_je").text(data.data12[0].dqzch_v10);//饲草料明细金额
		    	$("#show_hfyfz").text(data.data12[0].dqzch_v11);//防疫明细
		    	$("#show_hfyfz_je").text(data.data12[0].dqzch_v12);//防疫明细金额
		    	$("#show_hzzc").text(data.data12[0].dqzch_v13);//种畜明细
		    	$("#show_hzzc_je").text(data.data12[0].dqzch_v14);//种畜明细金额
		    	$("#show_hxstx").text(data.data12[0].dqzch_v15);//销售通讯明细
		    	$("#show_hxstx_je").text(data.data12[0].dqzch_v16);//销售通讯明细金额
		    	$("#show_hdk").text(data.data12[0].dqzch_v17);//借贷明细
		    	$("#show_hdk_je").text(data.data12[0].dqzch_v18);//借贷明细金额
		    	
		    	$("#show_hzczcxm1").text(data.data12[0].dqzch_v23);//政策项目1
		    	$("#show_hzcmx").text(data.data12[0].dqzch_v19);//政策明细1
		    	$("#show_hzcmx_je").text(data.data12[0].dqzch_v20);//政策明细1金额
		    	
		    	$("#show_hzczcxm2").text(data.data12[0].dqzch_v24);//政策项目2
		    	$("#show_hzcmx2").text(data.data12[0].dqzch_v21);//政策明细2
		    	$("#show_hzcmx2_je").text(data.data12[0].dqzch_v22);//政策明细2细金额
		    	
		    	
		    	$("#show_hzcqtxm1").text(data.data12[0].dqzch_v25);//其他项目1
		    	$("#show_hzcqtxmmx1").text(data.data12[0].dqzch_v26);//其他明细1
		    	$("#show_hzcqtxm1_je").text(data.data12[0].dqzch_v27);//其他金额1
		    	
		    	$("#show_hzcqtxm2").text(data.data12[0].dqzch_v28);//其他项目2
		    	$("#show_hzcqtxmmx2").text(data.data12[0].dqzch_v29);//其他明细2
		    	$("#show_hzcqtxm2_je").text(data.data12[0].dqzch_v30);//其他金额2
		    	
		    	$("#show_hzongzc").text(data.data12[0].dqzch_v31);//年总支出
		    	if(data.data12[0].dqzch_v31==""||data.data12[0].dqzch_v31==null||data.data12[0].dqzch_v31==undefined){
		    		$("#show_hdqnzzc").text("0");//年总支出
		    		var bb=0;
		    	}else{
		    		$("#show_hdqnzzc").text(data.data12[0].dqzch_v31);//年总支出
		    		var bb=data.data12[0].dqzch_v31;
		    	}
		    	
		    	
	    	}
	    	
	    	if(aa=='-'||bb=='-'){
	    		$("#show_hdqncsr").text('-');//年总收入
		    	$("#show_hdqniancsr").text('-');//年总收入
		    	$("#show_hdqjtrs").text(data.data1[0].v9);//人数
		    	$("#show_hdqnrj").text('-');//年人均收入
	    	}else{
	    		if((parseFloat(aa)-parseFloat(bb)).toFixed(2)=="NaN"){
	    			$("#show_hdqncsr").text("0");//年总收入
			    	$("#show_hdqniancsr").text("0");//年总收入
			    	$("#show_hdqjtrs").text(data.data1[0].v9);//人数
			    	$("#show_hdqnrj").text((("0")/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}else{
	    			$("#show_hdqncsr").text((parseFloat(aa)-parseFloat(bb)).toFixed(2));//年总收入
			    	$("#show_hdqniancsr").text((parseFloat(aa)-parseFloat(bb)).toFixed(2));//年总收入
			    	$("#show_hdqjtrs").text(data.data1[0].v9);//人数
			    	$("#show_hdqnrj").text(((parseFloat(aa)-parseFloat(bb))/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}
	    		
	    	
	    	}
	    	
	    	
	    	//增长比率
	    	var bfhrj_hl=((parseFloat(aa)-parseFloat(bb))/(data.data1[0].v9)).toFixed(2);//帮扶后收入
	    	var bfqrj_hl=((parseFloat(a)-parseFloat(b))/(data.data1[0].v9)).toFixed(2);//帮扶前收入
	    	if ( bfqrj_hl == 0 || bfqrj_hl <0 ) {
	    		$("#zz_bilv").text("数据错误");
	    	} else {
	    		$("#zz_bilv").text((((bfhrj_hl-bfqrj_hl)/bfqrj_hl).toFixed(2))*100+"%");
	    	}
	    	
	    	
	    	//帮扶后支出情况统计图
	    	$("#show_bfhzc").html("<p><strong>帮扶后支出情况统计图</strong></p><div id='pie-chart4' class='chart' style='height:550px;width:630px;'></div>");
	    	$("#show_bfhzcline").html("<div id='pie-chart4_2' class='chart' style='height:550px;width:630px;'></div>");
	    	$("#show_bfhzcbar").html("<div id='pie-chart4_3' class='chart' style='height:550px;width:630px;'></div>");
	    	if(data.data12[0].dqzch_v2==""||data.data12[0].dqzch_v2=="-"){
	    		data.data12[0].dqzch_v2="0";
	    	}
	    	if(data.data12[0].dqzch_v4==""||data.data12[0].dqzch_v4=="-"){
	    		data.data12[0].dqzch_v4="0";
	    	}
	    	if(data.data12[0].dqzch_v6==""||data.data12[0].dqzch_v6=="-"){
	    		data.data12[0].dqzch_v6="0";
	    	}
	    	if(data.data12[0].dqzch_v8==""||data.data12[0].dqzch_v8=="-"){
	    		data.data12[0].dqzch_v8="0";
	    	}
	    	if(data.data12[0].dqzch_v10==""||data.data12[0].dqzch_v10=="-"){
	    		data.data12[0].dqzch_v10="0";
	    	}
	    	if(data.data12[0].dqzch_v12==""||data.data12[0].dqzch_v12=="-"){
	    		data.data12[0].dqzch_v12="0";
	    	}
	    	if(data.data12[0].dqzch_v14==""||data.data12[0].dqzch_v14=="-"){
	    		data.data12[0].dqzch_v14="0";
	    	}
	    	if(data.data12[0].dqzch_v16==""||data.data12[0].dqzch_v16=="-"){
	    		data.data12[0].dqzch_v16="0";
	    	}
	    	if(data.data12[0].dqzch_v18==""||data.data12[0].dqzch_v18=="-"){
	    		data.data12[0].dqzch_v18="0";
	    	}
	    	if(data.data12[0].dqzch_v20==""||data.data12[0].dqzch_v20=="-"){
	    		data.data12[0].dqzch_v20="0";
	    	}
	    	if(data.data12[0].dqzch_v22==""||data.data12[0].dqzch_v22=="-"){
	    		data.data12[0].dqzch_v22="0";
	    	}
	    	if(data.data12[0].dqzch_v27==""||data.data12[0].dqzch_v27=="-"){
	    		data.data12[0].dqzch_v27="0";
	    	}
	    	if(data.data12[0].dqzch_v30==""||data.data12[0].dqzch_v30=="-"){
	    		data.data12[0].dqzch_v30="0";
	    	}
	    	var show_hzczcxm1=$("#show_hzczcxm1").text();//政策项目1
	    	var show_hzczcxm2=$("#show_hzczcxm2").text();//政策项目2
	    	var show_hzcqtxm1=$("#show_hzcqtxm1").text();//其他项目1
	    	var show_hzcqtxm2=$("#show_hzcqtxm2").text();//其他项目2
	    	option4 = {
	    			tooltip: {
	    		        trigger: 'item',
	    		        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    		    },
	    		    legend: {
	    		        orient: 'vertical',
	    		        x: 'left',
	    		        data:["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
	    		              "饲草料","防疫防治支出","种（仔）畜",
		    		             "销售费用和通讯费用","借贷",
		    		             show_hzczcxm1,show_hzczcxm2,show_hzcqtxm1,show_hzcqtxm2]
	    		    },
	    		    series: [
	    		        {
	    		            name:xingming+"当前支出情况",
	    		            type:'pie',
	    		            radius: ['40%', '60%'],
	    		            avoidLabelOverlap: false,
	    		            label: {
	    		                normal: {
	    		                    show: false,
	    		                    position: 'center'
	    		                },
	    		                emphasis: {
	    		                    show: true,
	    		                    formatter: '{b} \r\n {c}(元)',
	    		                    textStyle: {
	    		                        fontSize: '30',
	    		                        fontWeight: 'bold'
	    		                    }
	    		                }
	    		            },
	    		            labelLine: {
	    		                normal: {
	    		                    show: false
	    		                }
	    		            },
	    		            data:[
									{value:data.data12[0].dqzch_v2, name:"农资费用"},
									{value:data.data12[0].dqzch_v4, name:"固定资产折旧和租赁费"},
									{value:data.data12[0].dqzch_v6, name:"水电燃料支出"},
									{value:data.data12[0].dqzch_v8, name:"承包土地、草场费用"},
									
									{value:data.data12[0].dqzch_v10, name:"饲草料"},
									{value:data.data12[0].dqzch_v12, name:"防疫防治支出"},
									{value:data.data12[0].dqzch_v14, name:"种（仔）畜"},
									{value:data.data12[0].dqzch_v16, name:"销售费用和通讯费用"},
									{value:data.data12[0].dqzch_v18, name:"借贷"},
									{value:data.data12[0].dqzch_v20, name:show_hzczcxm1},
									{value:data.data12[0].dqzch_v22, name:show_hzczcxm2},
									{value:data.data12[0].dqzch_v27, name:show_hzcqtxm1},
									{value:data.data12[0].dqzch_v30, name:show_hzcqtxm2},
	    		            ]
	    		        }
	    		    ]};
				var myChart4 = echarts.init(document.getElementById('pie-chart4'));
				myChart4.setOption(option4);
				
				option4_2 = {
						   tooltip: {//显示提示框
						       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//						       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
						   },
						   title : {//图名的显示
							   fontFamily :"SimSun",//字体格式
						    	x: 'center',//居中
						        text: "帮扶后支出情况"//图名
						    },
						   legend: {//图中数据颜色的标识（选中的图例）
						       y: 'bottom',//Y轴的底部
						       data: ["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
			    		              "饲草料","防疫防治支出","种（仔）畜",
				    		             "销售费用和通讯费用","借贷",
				    		             show_hzczcxm1,show_hzczcxm2,show_hzcqtxm1,show_hzcqtxm2]//折线图折线的标识
						   },
						   xAxis: [//X轴
						       {
						           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
						           name: "明细",//坐标轴名称
						           axisLabel:{//坐标轴文本标签选项
			                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
			                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
			                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
						           },
						           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
						           data:["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
				    		              "饲草料","防疫防治支出","种（仔）畜",
					    		             "销售费用和通讯费用","借贷",
					    		             show_hzczcxm1,show_hzczcxm2,show_hzcqtxm1,show_hzcqtxm2]//X轴的坐标
						       }
						   ],
						   grid: { // 控制图的大小，调整下面这些值就可以，
					             x: 50,//离左边的间距
					             x2:100,//离右边的间距
					             y2: 230,//离下面的间距
					             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
					         },
					    
						   yAxis: [//Y轴
						       {
						    	   name:"金额（元）",//Y轴名称
						    	   type : 'value',
						    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
						    	   splitNumber: 5 //分隔段数，默认为5	
						       }
						   ],
						    toolbox: {//辅助工具箱
						       show: false//显示策略,可选true（显示）false（隐藏）
						   },
						   calculable: true,//是否启用拖拽重计算特性，默认关闭
						   series: [//数据系列
						       {
						           name: "支出明细",//名称
						           type: "line",//折线图
						           data: 
		    		                  [data.data12[0].dqzch_v2,data.data12[0].dqzch_v4,data.data12[0].dqzch_v6,data.data12[0].dqzch_v8,
								       data.data12[0].dqzch_v10,data.data12[0].dqzch_v12,data.data12[0].dqzch_v14,data.data12[0].dqzch_v16,
								       data.data12[0].dqzch_v18,data.data12[0].dqzch_v20,data.data12[0].dqzch_v22,
								       data.data12[0].dqzch_v27,data.data12[0].dqzch_v30,]
						       }
						   ]
						};
				var myChart4_2 = echarts.init(document.getElementById('pie-chart4_2'));
				myChart4_2.setOption(option4_2);
				
				option4_3 = {
						   tooltip: {//显示提示框
						       trigger: "axis",//触发类型，默认触发的类型，可选为：item|axis
//						       formatter: "{a} <br/>{b} : {c}"//拖拽重计算独有，当选择axis时候就不用你了，当选择item，则选择formatter
						   },
						   title : {//图名的显示
							   fontFamily :"SimSun",//字体格式
						    	x: 'center',//居中
						        text: "帮扶后支出情况"//图名
						    },
						   legend: {//图中数据颜色的标识（选中的图例）
						       y: 'bottom',//Y轴的底部
						       data: ["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
			    		              "饲草料","防疫防治支出","种（仔）畜",
				    		             "销售费用和通讯费用","借贷",
				    		             show_hzczcxm1,show_hzczcxm2,show_hzcqtxm1,show_hzcqtxm2]//折线图折线的标识
						   },
						   xAxis: [//X轴
						       {
						           type: "category",//坐标轴类型，横轴默认为category，纵轴默认为数值型value
						           name: "明细",//坐标轴名称
						           axisLabel:{//坐标轴文本标签选项
			                           interval:0,//小标记显示挑选间隔，默认为'auto'，可选为：'auto'（自动隐藏显示不下的） | 0（全部显示） | {number}（用户指定选择间隔）
			                         rotate:45,//标签旋转的角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90-90
			                         margin:2,//坐标轴文本标签与坐标轴的间距，默认为8，单位px
						           },
						           splitLine: {show: false},//分隔线，默认是显示，show来控制显示状态
						           data:["农资费用","固定资产折旧和租赁费","水电燃料支出","承包土地、草场费用",
				    		              "饲草料","防疫防治支出","种（仔）畜",
					    		             "销售费用和通讯费用","借贷",
					    		             show_hzczcxm1,show_hzczcxm2,show_hzcqtxm1,show_hzcqtxm2]//X轴的坐标
						       }
						   ],
						   grid: { // 控制图的大小，调整下面这些值就可以，
					             x: 50,//离左边的间距
					             x2:100,//离右边的间距
					             y2: 230,//离下面的间距
					             y:50// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
					         },
					    
						   yAxis: [//Y轴
						       {
						    	   name:"金额（元）",//Y轴名称
						    	   type : 'value',
						    	   splitArea : {show : true},//分隔区域，默认不显示，属性show控制显示是否，splitArea控制区域样式。
						    	   splitNumber: 5 //分隔段数，默认为5	
						       }
						   ],
						    toolbox: {//辅助工具箱
						       show: false//显示策略,可选true（显示）false（隐藏）
						   },
						   calculable: true,//是否启用拖拽重计算特性，默认关闭
						   series: [//数据系列
						       {
						           name: "支出明细",//名称
						           type: "bar",//折线图
						           data: 
		    		                  [data.data12[0].dqzch_v2,data.data12[0].dqzch_v4,data.data12[0].dqzch_v6,data.data12[0].dqzch_v8,
								       data.data12[0].dqzch_v10,data.data12[0].dqzch_v12,data.data12[0].dqzch_v14,data.data12[0].dqzch_v16,
								       data.data12[0].dqzch_v18,data.data12[0].dqzch_v20,data.data12[0].dqzch_v22,
								       data.data12[0].dqzch_v27,data.data12[0].dqzch_v30,]
						       }
						   ]
						};
				var myChart4_3= echarts.init(document.getElementById('pie-chart4_3'));
				myChart4_3.setOption(option4_3);
				
				
				
				handle_shijianzhou();
				
				//异地搬迁照片
				if(data.data14[0]==undefined||data.data14[0]==null||data.data14[0]==""||data.data14[0]=="-"){
					
				}else{
					$("#yd_bqfs").text(data.data14[0].yd_v1);//搬迁方式
					$("#yd_azfs").text(data.data14[0].move_type);//安置方式
					$("#yd_azd").text(data.data14[0].yd_v2);//安置地
					$("#yd_kn").text(data.data14[0].yd_v3);//搬迁可能存在的困难
					
					$("#yd_lx").text(data.data14[0].dispersed_info);//分散安置类型
					$("#yd_fyd").text(data.data14[0].dispersed_address);//房源地
					$("#yd_fj").text(data.data14[0].dispersed_price);//房价
					$("#yd_xy").text(data.data14[0].dispersed_agreement);//与用工企业签订就业安置协议
					if(data.data14[0].dispersed_info=="进城购房"){
						$("#yd-fyd").show();
						$("#yd-fj").show();
						$("#yd-xy").show();
						
						$("#dy_bqfyd").show();
					}
					var yd_path={};
					
					yd_path=data.data14[0].path.split(",");
					var yd_photo="";
					for(var j=0;j<yd_path.length;j++){
						yd_photo+='<a href=\''+yd_path[j]+'\' title=\"易地搬迁图片\" data-gallery=\"\">'+
						'<img src=\''+yd_path[j]+'\' style=\"margin:0;vertical-align:baseline;width:130px;height:130px;\"></a>&nbsp;&nbsp;&nbsp;';
					}
					$("#yd_path").html(yd_photo);
				}
				
				$("#spinners").hide();
				$("#neirong_jiben").show();
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}
//帮扶措施
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
//			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		}
	});
}
//帮扶措施
function cuoshi_initialization1(){
	dy_bfcs.bootstrapTable({
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
			dy_bfcs.bootstrapTable('removeAll');
//			toastr["info"]("info", "没有找到匹配的记录");
		},
		onClickRow: function (row, $element) {
			$('.success').removeClass('success');
			$($element).addClass('success');
		}
	});
}
//格式化详细页面模式的视图
function detailFormatter(index, row) {
    var html = "<table style=\"padding:20px\"><tr><td>2016年</td><td>项目需求量："+row.v4_2016+"</td><td>受益资金/政策："+row.v5_2016+"</td><td>落实时间："+row.v6_2016+"</td></tr>" +
    		"<tr><td>2017年</td><td>项目需求量："+row.v4_2017+"</td><td>受益资金/政策："+row.v5_2017+"</td><td>落实时间："+row.v6_2017+"</td></tr>" +
    		"<tr><td>2018年</td><td>项目需求量："+row.v4_2018+"</td><td>受益资金/政策："+row.v5_2018+"</td><td>落实时间："+row.v6_2018+"</td></tr>" +
    		"<tr><td>2019年</td><td>项目需求量："+row.v4_2019+"</td><td>受益资金/政策："+row.v5_2019+"</td><td>落实时间："+row.v6_2019+"</td></tr></table>";
    return html;
}
//格式化详细页面模式的视图
function detailFormatter1(index, row) {
	var html = "<table style=\"padding:20px\"><tr><td>2016年</td><td>项目需求量："+row.v4_2016+"</td><td>受益资金/政策："+row.v5_2016+"</td><td>落实时间："+row.v6_2016+"</td></tr>" +
	"<tr><td>2017年</td><td>项目需求量："+row.v4_2017+"</td><td>受益资金/政策："+row.v5_2017+"</td><td>落实时间："+row.v6_2017+"</td></tr>" +
	"<tr><td>2018年</td><td>项目需求量："+row.v4_2018+"</td><td>受益资金/政策："+row.v5_2018+"</td><td>落实时间："+row.v6_2018+"</td></tr>" +
	"<tr><td>2019年</td><td>项目需求量："+row.v4_2019+"</td><td>受益资金/政策："+row.v5_2019+"</td><td>落实时间："+row.v6_2019+"</td></tr></table>";
	return html;
}
function queryParams_cuoshi(params) {  //配置参数
	var temp = {};
    temp.pkid = $("#hu_pkid").val();
    temp.year = $("#cha_year").val();
   
    return temp;
}

/*
 * 打印
 */
function dayins(){
	return;
	window.open("H2_2.html?"+shijianzhou_pkid+"&0")
}


function method1(tableid) {
	$.ajax({
	    url: "/assa/exportExcel.do",
	    type: "POST",
	    async:true,
	    dataType: "json",
	    data:{
	    	pkid:shijianzhou_pkid,year:$("#cha_year").val()
        },
	    beforeSend: function(){
	    },
	    complete: function(){
	    },
	    
	    success: function (data) {
	    	if (typeof data != "undefined") {
	    		if (data == "1") {
	    			toastr["error"]("error", "服务器异常");
	    		}else if (data == "0") {
	    			toastr["error"]("error", "查询失败，没有获取必须的条件");
	    		}else{
	    			window.open("http://"+window.location.host+data.path);
	    		}
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

