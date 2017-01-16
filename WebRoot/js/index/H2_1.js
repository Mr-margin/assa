var app={};
var all_pkid;
$(document).ready(function() {
	$("#yeqian").css("display","none");
	document.getElementById('metTable_bxbxxb').scrollIntoView();
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式
	$("#gundongzhou").slimScroll({height:"500px"});//时间轴滚动条的高度
	$("#jiatingchengyuan .contact-box").each(function(){animationHover(this,"pulse")});
	getinfo_ready($('#cha_mz'));
	getInfo_ready_zhipinyuanyin($('#cha_zpyy'));
	
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
    	chaxun.cha_v8_1 = $("#cha_v8_1").val();
    	$("#yeqian").hide();
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据
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
    	$('#chauxnshiousuo').click();
    	chaxun = {};
    	$("#yeqian").hide();
    	metTable_bxbxxb.bootstrapTable('destroy');//销毁现有表格数据
    	gachacun_initialization();//重新初始化数据  
    });
    //导出
	$("#daochu").click(function(){
		exportExcel();
	});
	gachacun_initialization();
});


//查看贫苦户的详细信息
function chakan_info(pkid){

	//先清空
	$("#bfzrr_table").html('');
	$('#show_hz_phone').text("");//电话
	$('#show_hz_bank').text("");//开户银行
	$('#show_hz_banknum').text("");//银行卡号
	$('#show_hz_pkgsx').text("");//贫困户属性
	$('#show_hz_sfjls').text("");//是否军烈属
	$('#show_hz_dsznh').text("");//是否独生子女户
	$('#show_hz_snh').text("");//是否双女户
	$('#show_zyzp').text("");//主要致贫原因
	$('#show_qtzp').text("");//其他致贫原因
	$('#show_hz_sbbz').text("");//识别标准
	$("#show_gzxsr").text("");//工资性收入
	$("#show_jhsy").text("");//计划生育金
	$("#show_stbc").text("");//生态补偿金
	$("#show_scjyx").text("");//生产经营性收入
	$("#show_dbj").text("");//低保金
	$("#show_qtzy").text("");//其他转移性收入
	$("#show_ccxsr").text("");//财产性收入
	$("#show_wbj").text("");//五保金
	$("#shoow_zyxsr").text("");//转移性收入
	$("#show_ylbx").text("");//养老保险金
	$("#show_scjy").text("");//生产经营性支出
	$('#show_gdmj').text("");//耕地面积
	$('#show_ggmj').text("");//有效灌溉面积
	$('#show_ldmj').text("");//林地面积
	$('#show_tghlmj').text("");//退耕还林面积
	$('#show_lgmj').text("");//林果面积
	$('#show_mcdmj').text("");//牧草地面积
	$('#show_smmj').text("");//水面面积
	$('#show_sfscyd').text("");//是否通生产用电
	$('#show_shyd').text("");//是否通生活用电
	$('#show_zgljl').text("");//与村主干路距离
	$('#show_rullx').text("");//入户路类型
	$('#show_zfmj').text("");//住房面积
	$('#show_yskn').text("");//饮水是否困难
	$('#show_ysaq').text("");//饮水是否安全
	$('#show_zyrl').text("");//主要燃料类型
	$('#show_zyhzs').text("");//是否加入农民专业合作社
	$('#show_wscs').text("");//有无卫生厕所
	$('#show_sfbqh').text("");//是否搬迁户
	$('#show_bqfs').text("");//搬迁方式
	$('#show_azfs').text("");//安置方式
	$('#shoow_azd').text("");//安置地
	$('#show_czkn').text("");//搬迁可能存在的困难
	
	$("#yeqian").show();
	document.getElementById('yeqian').scrollIntoView();
	$("#yeqian ul>li:eq(0) a").trigger("click");//跳转第一个页签
//	$("#neirong_jiben").hide();
	
	savePoorMessage(pkid);
	
	
}
//贫困户初始化
function gachacun_initialization(){
	metTable_bxbxxb.bootstrapTable({
		method: 'POST',
		height: "597",
		url: "/assa/getPoorMessageController.do",
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
		},
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
	return temp;
}
//台账基本信息

//显示贫困户的台账详细信息
function savePoorMessage(pkid){
	all_pkid=pkid;
	var html="";
	$.ajax({
	    url: "/assa/getStandingBookController.do",
	    type: "POST",
	    async:true,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
        },
	    success: function (data) {
	    	//基本信息
//	    	$('#show_hz_address1').text((data.data1[0].v1));
//	    	$('#show_hz_address2').text((data.data1[0].v2));
	    	$('#show_hz_address3').text((data.data1[0].v3));
	    	$('#show_hz_address4').text((data.data1[0].v4));
	    	$('#show_hz_address5').text(data.data1[0].v5);
	    	
	    	$('#show_hz_phone').text(data.data1[0].v25);//电话
	    	$('#show_hz_bank').text(data.data1[0].v26);//开户银行
	    	$('#show_hz_banknum').text(data.data1[0].v27);//银行卡号
	    	$('#show_hz_pkgsx').text(data.data1[0].v22);//贫困户属性
	    	$('#show_hz_sfjls').text(data.data1[0].v29);//是否军烈属
	    	$('#show_hz_dsznh').text(data.data1[0].v30);//是否独生子女户
	    	$('#show_hz_snh').text(data.data1[0].v31);//是否双女户
	    	$('#show_zyzp').text(data.data1[0].v23);//主要致贫原因
	    	$('#show_qtzp').text(data.data1[0].v33);//其他致贫原因
	    	$('#show_hz_sbbz').text(data.data1[0].sys_standard);//识别标准
	    	
	    	//家庭成员
	    	var jtcy;
	    	
	    	jtcy+='<tr><td><code>1</code></td><td><code>'+data.data1[0].v6+'</code></td>';
	    	if(data.data1[0].v7==undefined||data.data1[0].v7==null){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v7+'</code></td>';
	    	}
	    	if(data.data1[0].v8==undefined||data.data1[0].v8==null){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v8+'</code></td>';
	    	}
	    	if(data.data1[0].v10==undefined||data.data1[0].v10==null){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v10+'</code></td>';
	    	}
	    	if(data.data1[0].v11==undefined||data.data1[0].v11==null){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v11+'</code></td>';
	    	}
//	    	jtcy+='<td><code>'+data.data1[0].v11+'</code></td>';
	    	if(data.data1[0].v28==null||data.data1[0].v28==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v28+'</code></td>';
	    	}
	    	
	    	if(data.data1[0].v12==null||data.data1[0].v12==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v12+'</code></td>';
	    	}
	    	if(data.data1[0].v13==null||data.data1[0].v13==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v13+'</code></td>';
	    	}
	    	if(data.data1[0].v14==null||data.data1[0].v14==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v14+'</code></td>';
	    	}
	    	if(data.data1[0].v15==null||data.data1[0].v15==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v15+'</code></td>';
	    	}
	    	if(data.data1[0].v16==null||data.data1[0].v16==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v16+'</code></td>';
	    	}
	    	if(data.data1[0].v17==null||data.data1[0].v17==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v17+'</code></td>';
	    	}
			if(data.data1[0].v32==null||data.data1[0].v32==undefined){
	    		jtcy+='<td><code></code></td>';
	    	}else{
	    		jtcy+='<td><code>'+data.data1[0].v32+'</code></td>';
	    	}	
			if(data.data1[0].v19==null||data.data1[0].v19==undefined){
				jtcy+='<td><code></code></td>';
			}else{
				jtcy+='<td><code>'+data.data1[0].v19+'</code></td>';
			}	
			jtcy+='</tr>';
	    	if(data.data2==""||data.data2==null||data.data2==undefined){
	    		
	    	}else{
	    		var v6;var v7;var v8;var v10;var v11;var v12;var v13;var v14;var v28;var v15;var v16;var v17;var v32;var v19;
	    		
	    		$.each(data.data2,function(i,item){
	    			if(item.v6==null||item.v6==undefined){
	    				v6="";
	    			}else{
	    				v6=item.v6;
	    			}
	    			if(item.v7==null||item.v7==undefined){
	    				v7="";
	    			}else{
	    				v7=item.v7;
	    			}
	    			if(item.v8==null||item.v8==undefined){
	    				v8="";
	    			}else{
	    				v8=item.v8;
	    			}
	    			if(item.v10==null||item.v10==undefined){
	    				v10="";
	    			}else{
	    				v10=item.v10;
	    			}
	    			if(item.v11==null||item.v11==undefined){
	    				v11="";
	    			}else{
	    				v11=item.v11;
	    			}
	    			if(item.v12==null||item.v12==undefined){
	    				v12="";
	    			}else{
	    				v12=item.v12;
	    			}
	    			if(item.v13==null||item.v13==undefined){
	    				v13="";
	    			}else{
	    				v13=item.v13;
	    			}
	    			if(item.v14==null||item.v14==undefined){
	    				v14="";
	    			}else{
	    				v14=item.v14;
	    			}
	    			if(item.v15==null||item.v15==undefined){
	    				v15="";
	    			}else{
	    				v15=item.v15;
	    			}
	    			if(item.v16==null||item.v16==undefined){
	    				v16="";
	    			}else{
	    				v16=item.v16;
	    			}
	    			if(item.v17==null||item.v17==undefined){
	    				v17="";
	    			}else{
	    				v17=item.v17;
	    			}
	    			if(item.v19==null||item.v19==undefined){
	    				v19="";
	    			}else{
	    				v19=item.v19;
	    			}
	    			if(item.v28==null||item.v28==undefined){
	    				v28="";
	    			}else{
	    				v28=item.v28;
	    			}
	    			if(item.v32==null||item.v32==undefined){
	    				v32="";
	    			}else{
	    				v32=item.v32;
	    			}
		    		jtcy+='<tr><td><code>'+(i+2)+'</code></td><td><code>'+v6+'</code></td><td><code>'+v7+'</code></td><td><code>'+v8+'</code></td><td><code>'
			    			+v10+'</code></td></code></td><td><code>'+v11+'</code></td>'+
			    			'<td><code>'+v28+'</code></td><td><code>'+v12+'</code></td><td><code>'+v13+'</code></td><td><code>'+v14+'</code></td><td><code>'
		    				+v15+'</code></td>'+
		    				'<td><code>'+v16+'</code></td><td><code>'+v17+'</code></td><td><code>'+v32+'</code></td><td><code>'+v19+'</code></td></tr>';
		    		
		    	});
		    	
	    	}
	    	$("#jtcy_table").html(jtcy);
	    	
	    	//收入情况
	    	var v28;var v30;
	    	if(data.data5[0].v28==""||data.data5[0].v28==null){
	    		v28=0;
	    	}else{
	    		v28=data.data5[0].v28;
	    	}
	    	if(data.data5[0].v30==""||data.data5[0].v30==null){
	    		v30=0;
	    	}else{
	    		v30=data.data5[0].v30;
	    	}
	    	$("#show_gzxsr").text(v28+v30);//工资性收入
	    	$("#show_jhsy").text(data.data5[0].v41);//计划生育金
	    	$("#show_stbc").text(data.data5[0].v12);//生态补偿金
	    	$("#show_scjyx").text(data.data5[0].v10);//生产经营性收入
	    	$("#show_dbj").text(data.data5[0].v16);//低保金
	    	$("#show_qtzy").text(data.data5[0].v20);//其他转移性收入
	    	var v24;var v26;
	    	if(data.data5[0].v24==""||data.data5[0].v24==null){
	    		v24=0;
	    	}else{
	    		v24=data.data5[0].v24;
	    	}
	    	if(data.data5[0].v26==""||data.data5[0].v26==null){
	    		v26=0;
	    	}else{
	    		v26=data.data5[0].v26;
	    	}
	    	$("#show_ccxsr").text(v24+v26);//财产性收入
	    	$("#show_wbj").text(data.data5[0].v43);//五保金
	    	$("#shoow_zyxsr").text(data.data5[0].v22);//转移性收入
	    	$("#show_ylbx").text(data.data5[0].v14);//养老保险金
	    	var v2;var v4;var v6;var v8;var v10;var v12;var v14;var v16;var v18;var v20;
	    	if(data.data8[0].v2==""||data.data8[0].v2==null){
	    		v2=0;
	    	}else{
	    		v2=data.data8[0].v2
	    	}
	    	if(data.data8[0].v4==""||data.data8[0].v4==null){
	    		v4=0;
	    	}else{
	    		v4=data.data8[0].v4;
	    	}
	    	if(data.data8[0].v6==""||data.data8[0].v6==null){
	    		v6=0;
	    	}else{
	    		v6=data.data8[0].v6;
	    	}
	    	if(data.data8[0].v8==""||data.data8[0].v8==null){
	    		v8=0;
	    	}else{
	    		v8=data.data8[0].v8;
	    	}
	    	if(data.data8[0].v10==""||data.data8[0].v10==null){
	    		v10=0;
	    	}else{
	    		v10=data.data8[0].v10;
	    	}
	    	if(data.data8[0].v12==""||data.data8[0].v12==null){
	    		v12=0;
	    	}else{
	    		v12=data.data8[0].v12;
	    	}
	    	if(data.data8[0].v14==""||data.data8[0].v14==null){
	    		v14=0;
	    	}else{
	    		v14=data.data8[0].v14;
	    	}
	    	if(data.data8[0].v16==""||data.data8[0].v16==null){
	    		v16=0;
	    	}else{
	    		v16=data.data8[0].v16;
	    	}
	    	if(data.data8[0].v18==""||data.data8[0].v18==null){
	    		v18=0;
	    	}else{
	    		v18=data.data8[0].v18;
	    	}
	    	$("#show_scjy").text((v2+v4+v6+v8+v10+v12+v14+v16+v18));//生产经营性支出
	    	
	    	
	    	//生产条件
	    	if(data.data3==""||data.data3==null||data.data3==undefined){
	    	}else{
	    		$('#show_gdmj').text(data.data3[0].v1);//耕地面积
		    	$('#show_ggmj').text(data.data3[0].v2);//有效灌溉面积
		    	$('#show_ldmj').text(data.data3[0].v3);//林地面积
		    	$('#show_tghlmj').text(data.data3[0].v4);//退耕还林面积
		    	$('#show_lgmj').text(data.data3[0].v13);//林果面积
		    	$('#show_mcdmj').text(data.data3[0].v5);//牧草地面积
		    	$('#show_smmj').text(data.data3[0].v14);//水面面积
	    	}
	    	//生活条件
	    	if(data.data4==""||data.data4==null||data.data4==undefined){
	    	}else{
	    		if(data.data4[0].v5==""||data.data4[0].v5==null||data.data4[0].v5=="-"||data.data4[0].v5==undefined){
	    			$('#show_sfscyd').text("否");//是否通生产用电
	    			$('#show_shyd').text("否");//是否通生活用电
	    		}else{
	    			$('#show_sfscyd').text("是");//是否通生产用电
	    			$('#show_shyd').text("是");//是否通生活用电
	    		}
	    		
	    		$('#show_zgljl').text(data.data4[0].v7);//与村主干路距离
		    	$('#show_rullx').text(data.data4[0].v6);//入户路类型
		    	$('#show_zfmj').text(data.data4[0].v1);//住房面积
		    	
		    	$('#show_yskn').text(data.data4[0].v8);//饮水是否困难
		    	$('#show_ysaq').text(data.data4[0].v9);//饮水是否安全
		    	$('#show_zyrl').text(data.data4[0].v10);//主要燃料类型
		    	$('#show_zyhzs').text(data.data4[0].v11);//是否加入农民专业合作社
		    	$('#show_wscs').text(data.data4[0].v12);//有无卫生厕所
	    	}
	    	
	    	//易地搬迁户需求
	    	if(data.data6==""||data.data6==null||data.data6==undefined){
	    	}else{
	    		$('#show_sfbqh').text(data.data6[0].vv3);//是否搬迁户
	    		$('#show_bqfs').text(data.data6[0].v1);//搬迁方式
		    	$('#show_azfs').text(data.data6[0].move_type);//安置方式
		    	$('#shoow_azd').text(data.data6[0].v2);//安置地
		    	$('#show_czkn').text(data.data6[0].v3);//搬迁可能存在的困难
	    	}
	    	//帮扶责任人
	    	var html1;
	    	var bcol_name="";var bv1 ="";var bv2="";var bv3="";var bcom_name="";var bv4="";var bv5=""; var bv6="";var bv7="";var btelephone="";
	    	$.each(data.data7,function(i,item){
    				if(item.col_name==undefined||item.col_name==""||item.col_name==null){
    				}else{
    					
    					if(item.col_name==null||item.col_name==undefined){
    						bcol_name="";
    					}else{
    						bcol_name=item.col_name;
    					}
    					if(item.v1==null||item.v1==undefined){
    						bv1="";
    					}else{
    						bv1=item.v1;
    					}
    					if(item.v2==null||item.v2==undefined){
    						bv2="";
    					}else{
    						bv2=item.v2;
    					}
    					if(item.v3==null||item.v3==undefined){
    						bv3="";
    					}else{
    						bv3=item.v3;
    					}
    					if(item.v4==null||item.v4==undefined){
    						bv4="";
    					}else{
    						bv4=item.col_name;
    					}
    					if(item.v5==null||item.v5==undefined){
    						bv5="";
    					}else{
    						bv5=item.v5;
    					}
    					if(item.v6==null||item.v6==undefined){
    						bv6="";
    					}else{
    						bv6=item.v6;
    					}
    					if(item.v7==null||item.v7==undefined){
    						bv7="";
    					}else{
    						bv7=item.v7;
    					}
    					if(item.com_name==null||item.com_name==undefined){
    						bcom_name="";
    					}else{
    						bcom_name=item.com_name;
    					}
    					if(item.telephone==null||item.telephone==undefined){
    						btelephone="";
    					}else{
    						btelephone=item.telephone;
    					}
    					html1+='<tr><td><code>'+(i+1)+'</code></td><td><code>'+bcol_name+'</code></td>'+
    						'<td><code>'+bv1+'</code></td>'+
	    					'<td><code>'+bv2+'</code></td>'+
	    					'<td><code>'+bv3+'</code></td>'+
	    					'<td><code>'+bcom_name+'</code></td>'+
	    					'<td><code>'+bv4+'</code></td>'+
	    					'<td><code>'+bv5+'</code></td>'+
	    					'<td><code>'+bv6+'</code></td>'+
	    					'<td><code>'+bv7+'</code></td>'+
	    					'<td><code>'+btelephone+'</code></td>'+
	    					'</tr>';
    				}
//	    		html1+='</div>';
				
	    	});
	    	$("#bfzrr_table").html(html1);
				$("#neirong_jiben").show();
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	
}

//脱贫计划、帮扶措施、工作台账、贫困户收入监测表   
function showStanding(year,num){
	$("#show_tpv2_"+num).text("");
	$("#show_tpv3_"+num).text("");
	$("#show_tpv4_"+num).text("");
	$("#show_tpv5_"+num).text("");
	$("#show_tpv6_"+num).text("");
	$("#show_tpv7_"+num).text("");
	$("#show_tpv8_"+num).text("");
	$("#show_tpv9_"+num).text("");
	$("#show_tpv10_"+num).text("");
	$("#show_tpv11_"+num).text("");
	$("#show_tpv12_"+num).text("");
	$("#show_tpv13_"+num).text("");
	$("#show_tpv14_"+num).text("");
	$("#show_tpv15_"+num).text("");
	$("#show_tpv16_"+num).text("");
	$("#show_tpv17_"+num).text("");
	$("#show_tpv18_"+num).text("");
	$("#show_tpv19_"+num).text("");
	$("#bfcs_table_"+num).html("");
	$("#gztz_table_"+num).html("");
	
	
	if(all_pkid==""||all_pkid==null){
		return;
	}
	$.ajax({
		 url: "/assa/getStandingController.do",
		    type: "POST",
		    async:true,
		    dataType:"json",
		    data:{
		    	pkid:all_pkid,
		    	year:year
	        },
	        success:function(data){
	        	//脱贫计划
	        	if(data.data1==""||data.data1==null){
	        		
	        	}else{
	        		$("#show_tpv2_"+num).text(data.data1[0].v2);
		        	$("#show_tpv3_"+num).text(data.data1[0].v3);
		        	$("#show_tpv4_"+num).text(data.data1[0].v4);
		        	$("#show_tpv5_"+num).text(data.data1[0].v5);
		        	$("#show_tpv6_"+num).text(data.data1[0].v6);
		        	$("#show_tpv7_"+num).text(data.data1[0].v7);
		        	$("#show_tpv8_"+num).text(data.data1[0].v8);
		        	$("#show_tpv9_"+num).text(data.data1[0].v9);
		        	$("#show_tpv10_"+num).text(data.data1[0].v10);
		        	$("#show_tpv11_"+num).text(data.data1[0].v11);
		        	$("#show_tpv12_"+num).text(data.data1[0].v12);
		        	$("#show_tpv13_"+num).text(data.data1[0].v13);
		        	$("#show_tpv14_"+num).text(data.data1[0].v14);
		        	$("#show_tpv15_"+num).text(data.data1[0].v15);
		        	$("#show_tpv16_"+num).text(data.data1[0].v16);
		        	$("#show_tpv17_"+num).text(data.data1[0].v17);
		        	$("#show_tpv18_"+num).text(data.data1[0].v18);
		        	$("#show_tpv19_"+num).text(data.data1[0].v19);
	        	}
	        	
	        	//帮扶措施
	        	try {
	        		if(data.data2==""||data.data2==null){
		        		
		        	}else{
		        		var hj=0;var hjy=0;var v4;var v5;var v6;var v5_1;
			        	var html;
		        		$.each(data.data2,function(i,item){
		        			hj=i+1;
		        			if(item.v4==null||item.v4==undefined){
		        				v4="";
		        			}else{
		        				v4=item.v4;
		        			}
		        			if(item.v5==null||item.v5==undefined||item.v5==""){
		        				v5="";
		        				v5_1="0"
		        			}else{
		        				v5=item.v5;
		        				v5_1=item.v5;
		        			}
		        			if(item.v6==null||item.v6==undefined){
		        				v6="";
		        			}else{
		        				v6=item.v6;
		        			}
		        			if(isNaN(parseInt(v5_1))){
		        			}else{
		        				hjy+=parseInt(v5_1);
		        			}
		        			
		        			html+='<tr><th><code>'+(i+1)+'</code></th><th><code>'+item.v1+'<code></th><th><code>'+item.v2+'</code></th><th><code>'+item.v3+'</code></th>'+
		        			'<th><code>'+v4+'</code></th><th><code>'+v5+'</code></th><th><code>'+v6+'</code></th></tr>'
		        		});
		        		 html+='<tr><th style="text-align:center;"  colspan="4">合计</th><th><code>'+hj+'项</code></th><th><code>'+hjy+'元</code></th><th></th></tr>';
		 	        	$("#bfcs_table_"+num).html(html);
		        	}
					
				} catch (e) {
					// TODO: handle exception
				}
	        
	        	
	        	//工作台账
	        	if(data.data3==""||data.data3==null){
	        	}else{
	        		var html1;
	        		$.each(data.data3,function(i,item){
	        			html1+='<tr><th><code>'+item.v6+'</code></th><th><code>'+item.v1+'</code></th><th><code>'+item.v2+'</code></th><th><code>'+item.v3+
	        			'</code></th><th><code>'+item.v4+'</code></th></tr>';
	        		});
	        		$("#gztz_table_"+num).html(html1);
	        	}
	        },
	        error:function(){
	        	toastr["error"]("error", "服务器异常");
	        }
	});
}
/**
 * 导出excel
 * @param table
 * @param head_arr
 * @param type_arr
 */
function exportExcel() {
	
	var row = metTable_bxbxxb.bootstrapTable('getSelections');
	if (row.length>0 ) {
		var str = "";
		$.each(row,function(i,item){
			str += item.pkid+",";
			});
		str = str.substring(0,str.length-1);
		exportExcell(str);
		
	}


} 
function exportExcell(str){
//	return;
	var daochu=$("#daochu").val();
    $.ajax({  
      type:'post',  
      traditional :true,  
      url: "/assa/exportExcel.do",
      dataType:'json',
      data:{str:str},  
      success:function(data){  
      	window.location.href=data.path;
      }  
  });  

}