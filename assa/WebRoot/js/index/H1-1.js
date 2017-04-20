$(function () {
	$("#shenghuo_Form").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
	$("#shengchan_Form").validate({
	    onfocusout: function(element){
	        $(element).valid();
	    }
	});
	
	//户主保存
    $("#huzhu #save").click(function () {
    	huzhu_save();
	});
    
    //家庭成员保存
    $("#jiatingchengyuan #save").click(function () {
    	jiatingchengyuan_save();
	});
    
    //生产条件保存
    $("#shengchan #save").click(function () {
    	shengchan_save();
	});
    
    //生活条件保存
    $("#shenghuo #save").click(function () {
    	shenghuo_save();
	});
    
    //上传户主照片
    $("#tab-j-1 #shh_hu_pic").click(function () {
    	var form = $("form[name=huzhu_ImportForm]");
    	var options  = {
            url:"/assa/ImportData.do",
            type:'post',
            success:function(data, jqForm, options){
            	
            	$('#tab-j-1 #huzhuzhaopian_path').html("");//文件名清空
            	
                var jsondata = eval("("+data+")");
                if(jsondata.error == "0"){
                	 $("#tab-j-1 #huzhu_pic_show").attr("src", jsondata.path);
                    toastr["success"]("success", jsondata.message);
                }else{
                    toastr["error"]("error", jsondata.message);
                }
            }
        };
        form.ajaxSubmit(options);
	});
    
    //上传家庭成员照片
    $("#tab-j-2 #shh_jtcy_pic").click(function () {
    	var form = $("form[name=jtcy_ImportForm]");
    	var options  = {
            url:"/assa/ImportData.do",
            type:'post',
            success:function(data, jqForm, options){
            	
            	$('#tab-j-2 #huzhuzhaopian_path').html("");//文件名清空
            	
                var jsondata = eval("("+data+")");
                if(jsondata.error == "0"){
                	 $("#tab-j-2 #jtcy_pic_show").attr("src", jsondata.path);
                	 $('#jiatingchengyuan #jiating_table #vjtpic_tab_'+hang_jt).val(jsondata.path);
                    toastr["success"]("success", jsondata.message);
                }else{
                    toastr["error"]("error", jsondata.message);
                }
            }
        };
        form.ajaxSubmit(options);
	});
    
    $('#tab-j-1 #file-pretty input[type="file"]').prettyFile();
    $('#tab-j-2 #file-pretty input[type="file"]').prettyFile();
    
    //添加新成员
    $('#save_jiating').click(function () {
		//提交表单进行验证
		var validator = $("#upForm_jiating").validate();
		if(validator.form()){
			add_jaiitng_new();
		}
    });
    
    //删除家庭成员
    $('#del_jiatingchengyaun').click(function () {
    	swal({
            title: "您确定要删除家庭成员吗？",
            text: "这不是脱贫操作，删除后将无法恢复，请谨慎！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "删除",
            closeOnConfirm: false
        },
        function() {
            del_jiatingchengyaun();
        });
    });
    
    //选择集中安置或者分散安置
    $('#yidifupinbanqian #move_type_div .iCheck-helper').click(function () {
    	move_type_fun();
    });
    
    $('#yidifupinbanqian #move_type_div label').click(function () {
    	move_type_fun();
    });
    
    //易地扶贫搬迁保存
    $("#yidifupinbanqian #save").click(function () {
    	yidifupinbanqian_save();
	});
    
});

//易地扶贫搬迁保存
function yidifupinbanqian_save(){
	var form_val = JSON.stringify(getFormJson("#yidifupinbanqian_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaveYidifupinbanqian.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "易地扶贫搬迁修改");
	    		
//	    		if($("#shenghuo input[name='v3']:checked").val()=='是'){
//	    			$("#ydfpbq").show();
//	    			yidi_pic_load('6',$("#shang_yi #hu_pkid").val());
//	    			yidi_pic_show($("#shang_yi #hu_pkid").val());
//	    		}else{
//	    			$("#ydfpbq").hide();
//	    		}
	    		
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//选择集中安置或者分散安置
function move_type_fun(){
	
	if($('#yidifupinbanqian #move_type_div .checked #move_type').val()=='集中安置'){
		
		$("#yidifupinbanqian #leixing_1").show();
		$("#yidifupinbanqian #leixing_2").hide();
		
		$("#yidifupinbanqian #fangyuandi").hide();
		$("#yidifupinbanqian #fangjia").hide();
		$("#yidifupinbanqian #yonggongxieyi").hide();
		
	}else if($('#yidifupinbanqian #move_type_div .checked #move_type').val()=='分散安置'){
		
		$("#yidifupinbanqian #leixing_1").hide();
		$("#yidifupinbanqian #leixing_2").show();
		
		getDispersed($("#yidifupinbanqian #dispersed_info").val());
	}
}

//分散安置的扩展
function getDispersed(thisVal){
	if(thisVal=='进城购房'){
		$("#yidifupinbanqian #fangyuandi").show();
		$("#yidifupinbanqian #fangjia").show();
		$("#yidifupinbanqian #yonggongxieyi").show();
	}else{
		$("#yidifupinbanqian #fangyuandi").hide();
		$("#yidifupinbanqian #fangjia").hide();
		$("#yidifupinbanqian #yonggongxieyi").hide();
	}
}

//删除家庭成员
function del_jiatingchengyaun(){
	$.ajax({  		       
	    url: "/assa/getDelJiatingchengyuan.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid:$("#jiatingchengyuan #jiating_pkid").val(),
	    	huzhuid: $("#shang_yi #hu_pkid").val()
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "删除家庭成员");
	    		swal("删除成功！", "您已经永久删除了这个家庭成员。", "success");
	    	}else{
	    		toastr["warning"]("warning", "操作失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	jbqk($("#shang_yi #hu_pkid").val());
}
//添加家庭成员
function add_jaiitng_new(){
	$.ajax({  		       
	    url: "/assa/getSaveNewjiating.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	name: $("#jiatingname_add").val()
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "补充新家庭成员");
	    	}else{
	    		toastr["warning"]("warning", "操作失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	$("#close_jiating").click();
	jbqk($("#shang_yi #hu_pkid").val());
}


//基本情况
function jbqk(id){
	$('#tab-j-1 #type').val("4");//户主照片上传类型
	$('#tab-j-2 #type').val("5");//家庭成员照片上传类型
	$('#tab-j-1 #pkid').val(id);//照片上传户主ID
	$("#ydfpbq").hide();
	
//	$('#bangfucuoshi .blueimp-gallery').attr('id','blueimp-gallery-bak');
	$('#zoufangqingkaung .blueimp-gallery').attr('id','blueimp-gallery-bak');
	$('#bangfuchengxiao .blueimp-gallery').attr('id','blueimp-gallery-bak');
	$('#yidifupinbanqian .blueimp-gallery').attr('id','blueimp-gallery');
	$('#yidifupinbanqian #poht_list').html("");
//	$('#bangfucuoshi #poht_list').html("");
	$('#zoufangqingkaung #poht_list').html("");
	$('#bangfuchengxiao #poht_list').html("");
	
	//每次打开新的贫困户，切换到户主情况
	$("#tab_jbqk #jiben_ul li").removeClass("active");
	$("#tab_jbqk #jiben_ul #jiben_moren_li").addClass("active");
	$("#tab_jbqk #jiben_tab div").removeClass("active");
	$("#tab_jbqk #jiben_tab #tab-j-1").addClass("active");
	
	jbqk_initialization();
	$.ajax({
	    url: "/assa/getUpdateSavePoor.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	data_year:$("#data_year").val(),
	    	pkid:id,
        },
	    success: function (data) {
	    	$("#tab_jbqk").show();//基本情况
	    	$("#tab_dqszh").hide();//当前收支
	    	$("#tab_bfdwyzrr").hide();//帮扶单位与责任人
	    	$("#tab_bfcsh").hide();//帮扶措施
	    	$("#tab_zfqk").hide();//走访情况
	    	$("#tab_bfcx").hide();//帮扶成效
	    	$("#tab_bfgshzhfx").hide();//帮扶后收支分析
	    	
	    	$("#jieting_up_div").hide();
	    	
	    	document.getElementById('tab_jbqk').scrollIntoView();
	    	//户主的个人信息
	    	$("#shang_yi #hu_pkid").val(data.huzhu.pkid);//记录户主ID
	    	$("#huzhu #v6").val(data.huzhu.v6);//户主姓名
	    	if(data.huzhu.v7==''){
	    		$('#huzhu #v7').val("请选择");//性别
	    	}else{
	    		$('#huzhu #v7').val(data.huzhu.v7);//性别
	    	}
	    	if(data.huzhu.v11==''){
	    		$('#huzhu #v11').val("请选择");//民族
	    	}else{
	    		$('#huzhu #v11').val(data.huzhu.v11);//民族
	    	}
	    	$('#huzhu #v25').val(data.huzhu.v25);//联系电话
	    	$('#huzhu #v26').val(data.huzhu.v26);//开户银行
	    	$('#huzhu #v27').val(data.huzhu.v27);//银行账号
	    	$('#huzhu #v17').val(data.huzhu.v17);//务工时间
	    	if(data.huzhu.v16==''){
	    		$('#huzhu #v16').val("请选择");//务工情况
	    	}else{
	    		$('#huzhu #v16').val(data.huzhu.v16);//务工情况
	    	}
	    	
	    	if(data.huzhu.sys_standard==''){
	    		$('#huzhu #sys_standard').val("请选择");//识别标准
	    	}else{
	    		$('#huzhu #sys_standard').val(data.huzhu.sys_standard);//识别标准
	    	}
	    	$('#huzhu #v8').val(data.huzhu.v8);//证件号码
	    	//$('#huzhu #hz_jtzz').val(data.huzhu.basic_address);//家庭住址
	    	//家庭住址
	    	//旗县
	    	if(data.huzhu.v3==''){
	    		$('#huzhu #cha_qx2').val("请选择");
	    	}else{
	    		$('#huzhu #cha_qx2').val(data.huzhu.v3);
	    		
	    	}
	    	//苏木乡
	    	if(data.huzhu.v4==''){
	    		$('#huzhu #cha_smx2').val("请选择");
	    	}else{
	    		getinfo_xiang($('#huzhu #cha_qx2').val(),$('#cha_smx2'));
	    		$('#huzhu #cha_smx2').val(data.huzhu.v4);
	    	}
	    	//嘎查村
	    	if(data.huzhu.v5==''){
	    		$('#huzhu #cha_gcc2').val("请选择");
	    	}else{
	    		getinfo_cun($('#huzhu #cha_smx2').val(),$('#cha_gcc2'));
	    		$('#huzhu #cha_gcc2').val(data.huzhu.v5);
	    	}
	    	if(data.huzhu.v22==''){
	    		$('#huzhu #v22').val("请选择");//贫困户属性
	    	}else{
	    		$('#huzhu #v22').val(data.huzhu.v22);//贫困户属性
	    	}
	    	
	    	if(data.huzhu.v12==''){
	    		$('#huzhu #v12').val("请选择");//文化程度
	    	}else{
	    		$('#huzhu #v12').val(data.huzhu.v12);//文化程度
	    	}
	    	if(data.huzhu.v13==''){
	    		$('#huzhu #v13').val("请选择");//是否在校
	    	}else{
	    		$('#huzhu #v13').val(data.huzhu.v13);//是否在校
	    	}
	    	if(data.huzhu.v14==''){
	    		$('#huzhu #v14').val("请选择");//健康状况
	    	}else{
	    		$('#huzhu #v14').val(data.huzhu.v14);//健康状况
	    	}
	    	if(data.huzhu.v15==''){
	    		$('#huzhu #v15').val("请选择");//劳动力
	    	}else{
	    		$('#huzhu #v15').val(data.huzhu.v15);//劳动力
	    	}
	    	if(data.huzhu.v28==''){
	    		$('#huzhu #v28').val("请选择");//政治面貌
	    	}else{
	    		$('#huzhu #v28').val(data.huzhu.v28);//政治面貌
	    	}
	    	if(data.huzhu.v18==''){//是否参加新农合
	    		$("#huzhu #v18_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v18_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v18'][value='"+data.huzhu.v18+"']").prop("checked","true");//是否参加新农合
	    		$("#huzhu input[name='v18'][value='"+data.huzhu.v18+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	
	    	if(data.huzhu.v19==''){//是否参加新型养老保险
	    		$("#huzhu #v19_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v19_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v19'][value='"+data.huzhu.v19+"']").prop("checked","true");//是否参加新型养老保险
	    		$("#huzhu input[name='v19'][value='"+data.huzhu.v19+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	if(data.huzhu.v29==''){//是否军烈属
	    		$("#huzhu #v29_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v29_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v29'][value='"+data.huzhu.v29+"']").prop("checked","true");
	    		$("#huzhu input[name='v29'][value='"+data.huzhu.v29+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	
	    	if(data.huzhu.v30==''){//是否独生子女户
	    		$("#huzhu #v30_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v30_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v30'][value='"+data.huzhu.v30+"']").prop("checked","true");
	    		$("#huzhu input[name='v30'][value='"+data.huzhu.v30+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	if(data.huzhu.v31==''){//是否双女户
	    		$("#huzhu #v31_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v31_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v31'][value='"+data.huzhu.v31+"']").prop("checked","true");
	    		$("#huzhu input[name='v31'][value='"+data.huzhu.v31+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	
	    	if(data.huzhu.v32==''){//是否现役军人
	    		$("#huzhu #v32_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v32_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v32'][value='"+data.huzhu.v32+"']").prop("checked","true");
	    		$("#huzhu input[name='v32'][value='"+data.huzhu.v32+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	
	    	$('#huzhu #hz_zpyy').val(data.huzhu.basic_explain.replace(/[\r\n]/g,""));//致贫原因说明
	    	
	    	if(data.huzhu.v23==''){//主要致贫原因
	    		$("#huzhu #v23_div input[type=radio]").removeAttr("checked");
	    		$("#huzhu #v23_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	    	}else{
	    		$("#huzhu input[name='v23'][value='"+data.huzhu.v23+"']").prop("checked","true");
	    		$("#huzhu input[name='v23'][value='"+data.huzhu.v23+"']").parent(".iradio_square-green").addClass("checked");
	    	}
	    	
    		if(data.huzhu.v33.indexOf(",")>-1){//多选
	    		var strs= new Array(); //定义一数组 
	    		strs=data.huzhu.v33.split(","); //字符分割 
	    		for (i=0;i<strs.length ;i++ ){ 
	    			$("#huzhu input[name='v33'][value='"+strs[i]+"']").prop("checked","true");//[0]
			    	$("#huzhu input[name='v33'][value='"+strs[i]+"']").parent(".icheckbox_square-green").addClass("checked");
	    		} 
	    	}else{//单选
	    		$("#huzhu input[name='v33'][value='"+data.huzhu.v33+"']").prop("checked","true");//[0]
		    	$("#huzhu input[name='v33'][value='"+data.huzhu.v33+"']").parent(".icheckbox_square-green").addClass("checked");
	    	}
	    	
	    	//户主照片
	    	var huzhu_pic_path;
	    	if (typeof data.huzhu_pic != "undefined") {
	    		if(data.huzhu_pic != ""){
	    			huzhu_pic_path = data.huzhu_pic;
	    		}else{
	    			huzhu_pic_path = "img/zw.jpg";
	    		}
	    	}else{
	    		huzhu_pic_path = "img/zw.jpg";
	    	}
	    	$("#tab-j-1 #huzhu_pic_show").attr("src", huzhu_pic_path);
	    	
	    	//家庭成员的信息
	    	if (typeof data.jaiting != "undefined") {
				var jiating_html = "";
				$.each(data.jaiting,function(i,item){
					var jtcy_pic_path;
					if (typeof item.pic != "undefined") {
						if(item.pic != ""){
							jtcy_pic_path = item.pic;
						}else{
							jtcy_pic_path = "img/zw.jpg";
						}
					}else{
						jtcy_pic_path = "img/zw.jpg";
					}
					
					jiating_html += "<tr onclick=\"jiating('"+(i+1)+"','"+item.pkid+"');\"><td>"+(i+1)+"<input type=\"text\" style=\"display: none;\" id='vjtpic_tab_"+(i+1)+"' value='"+jtcy_pic_path+"'></td>" +
							"<td id='v6_"+(i+1)+"'>"+item.v6+"</td><td id='v7_"+(i+1)+"'>"+item.v7+"</td><td id='v10_"+(i+1)+"'>"+item.v10+"</td>" +
							"<td id='v8_"+(i+1)+"'>"+item.v8+"</td><td id='v11_"+(i+1)+"'>"+item.v11+"</td><td id='v28_"+(i+1)+"'>"+item.v28+"</td><td id='v32_"+(i+1)+"'>"+item.v32+"</td>" +
							"<td id='v12_"+(i+1)+"'>"+item.v12+"</td><td id='v13_"+(i+1)+"'>"+item.v13+"</td><td id='v14_"+(i+1)+"'>"+item.v14+"</td><td id='v15_"+(i+1)+"'>"+item.v15+"</td>" +
							"<td id='v16_"+(i+1)+"'>"+item.v16+"</td><td id='v17_"+(i+1)+"'>"+item.v17+"</td>" +
							"<td id='v18_"+(i+1)+"'>"+item.v18+"</td><td id='v19_"+(i+1)+"'>"+item.v19+"</td></tr>";
				});
				$('#jiatingchengyuan #jiating_table').html(jiating_html);
			}
	    	
	    	//生产条件
	    	if (typeof data.shenchan != "undefined") {
	    		$('#shengchan #v1').val(data.shenchan.v1);
		    	$('#shengchan #v2').val(data.shenchan.v2);
		    	$('#shengchan #v3').val(data.shenchan.v3);
		    	$('#shengchan #v4').val(data.shenchan.v4);
		    	$('#shengchan #v5').val(data.shenchan.v5);
		    	$('#shengchan #v6').val(data.shenchan.v6);
		    	$('#shengchan #v7').val(data.shenchan.v7.replace(/[\r\n]/g,""));
		    	$('#shengchan #v8').val(data.shenchan.v8);
		    	$('#shengchan #v9').val(data.shenchan.v9);
		    	$('#shengchan #v10').val(data.shenchan.v10);
		    	$('#shengchan #v11').val(data.shenchan.v11);
		    	$('#shengchan #v12').val(data.shenchan.v12.replace(/[\r\n]/g,""));
		    	$('#shengchan #v13').val(data.shenchan.v13);
		    	$('#shengchan #v14').val(data.shenchan.v14);
	    	}
	    	
	    	
	    	//生活条件
	    	if (typeof data.shenhuo != "undefined") {
	    		$('#shenghuo #v1').val(data.shenhuo.v1);//住房面积
	    		$('#shenghuo #v7').val(data.shenhuo.v7);//与村主干路距离
		    	if(data.shenhuo.v2==''){
		    		$("#shenghuo input[name='v2'][type=radio]").removeAttr("checked");
		    		$("#shenghuo input[name='v2'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#shenghuo input[name='v2'][value='"+data.shenhuo.v2+"']").prop("checked","true");//[0]
		    		$("#shenghuo input[name='v2'][value='"+data.shenhuo.v2+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	
		    	if(data.shenhuo.v3==''){
		    		$("#shenghuo input[name='v3'][type=radio]").removeAttr("checked");
		    		$("#shenghuo input[name='v3'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		if(data.shenhuo.v3=='是'){
		    			$("#ydfpbq").show();
		    			yidi_pic_load('6',id);
		    			yidi_pic_show(id);
		    		}else{
		    			$("#ydfpbq").hide();
		    		}
		    		$("#shenghuo input[name='v3'][value='"+data.shenhuo.v3+"']").prop("checked","true");//[0]
		    		$("#shenghuo input[name='v3'][value='"+data.shenhuo.v3+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	
		    	if(data.shenhuo.v4!=''){
		    		if(data.shenhuo.v4.indexOf(",")>-1){//多选
			    		var strs= new Array(); //定义一数组 
			    		strs=data.shenhuo.v4.split(","); //字符分割 
			    		for (i=0;i<strs.length ;i++ ){ 
			    			$("#shenghuo #v4_div input[value='"+strs[i]+"']").prop("checked","true");//[0]
					    	$("#shenghuo #v4_div input[value='"+strs[i]+"']").parent(".icheckbox_square-green").addClass("checked");
			    		} 
			    	}else{//单选
			    		$("#shenghuo #v4_div input[value='"+data.shenhuo.v4+"']").prop("checked","true");//[0]
				    	$("#shenghuo #v4_div input[value='"+data.shenhuo.v4+"']").parent(".icheckbox_square-green").addClass("checked");
			    	}
		    	}
		    	
		    	if(data.shenhuo.v5!=''){
			    	if(data.shenhuo.v5.indexOf(",")>-1){//多选
			    		var strs= new Array(); //定义一数组 
			    		strs=data.shenhuo.v5.split(","); //字符分割 
			    		for (i=0;i<strs.length ;i++ ){ 
			    			$("#shenghuo #v5_div input[value='"+strs[i]+"']").prop("checked","true");//[0]
					    	$("#shenghuo #v5_div input[value='"+strs[i]+"']").parent(".icheckbox_square-green").addClass("checked");
			    		} 
			    	}else{//单选
			    		$("#shenghuo #v5_div input[value='"+data.shenhuo.v5+"']").prop("checked","true");//[0]
				    	$("#shenghuo #v5_div input[value='"+data.shenhuo.v5+"']").parent(".icheckbox_square-green").addClass("checked");
			    	}
		    	}
		    	
		    	if(data.shenhuo.v6!=''){
			    	if(data.shenhuo.v6.indexOf(",")>-1){//多选
			    		var strs= new Array(); //定义一数组 
			    		strs=data.shenhuo.v6.split(","); //字符分割 
			    		for (i=0;i<strs.length ;i++ ){ 
			    			$("#shenghuo #v6_div input[value='"+strs[i]+"']").prop("checked","true");//[0]
					    	$("#shenghuo #v6_div input[value='"+strs[i]+"']").parent(".icheckbox_square-green").addClass("checked");
			    		} 
			    	}else{//单选
			    		$("#shenghuo #v6_div input[value='"+data.shenhuo.v6+"']").prop("checked","true");//[0]
				    	$("#shenghuo #v6_div input[value='"+data.shenhuo.v6+"']").parent(".icheckbox_square-green").addClass("checked");
			    	}
		    	}
		    	
		    	if(data.shenhuo.v8==''){
		    		$("#shenghuo input[name='v8'][type=radio]").removeAttr("checked");
		    		$("#shenghuo input[name='v8'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#shenghuo input[name='v8'][value='"+data.shenhuo.v8+"']").prop("checked","true");//[0]
		    		$("#shenghuo input[name='v8'][value='"+data.shenhuo.v8+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	if(data.shenhuo.v9==''){
		    		$("#shenghuo input[name='v9'][type=radio]").removeAttr("checked");
		    		$("#shenghuo input[name='v9'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#shenghuo input[name='v9'][value='"+data.shenhuo.v9+"']").prop("checked","true");//[0]
		    		$("#shenghuo input[name='v9'][value='"+data.shenhuo.v9+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	if(data.shenhuo.v11==''){
		    		$("#shenghuo input[name='v11'][type=radio]").removeAttr("checked");
		    		$("#shenghuo input[name='v11'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#shenghuo input[name='v11'][value='"+data.shenhuo.v11+"']").prop("checked","true");//[0]
		    		$("#shenghuo input[name='v11'][value='"+data.shenhuo.v11+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	if(data.shenhuo.v12==''){
		    		$("#shenghuo input[name='v12'][type=radio]").removeAttr("checked");
		    		$("#shenghuo input[name='v12'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#shenghuo input[name='v12'][value='"+data.shenhuo.v12+"']").prop("checked","true");//[0]
		    		$("#shenghuo input[name='v12'][value='"+data.shenhuo.v12+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	
		    	if(data.shenhuo.v10==''){
		    		$('#shenghuo #v10').val("请选择");//主要燃料类型
		    	}else{
		    		$('#shenghuo #v10').val(data.shenhuo.v10);//主要燃料类型
		    	}
	    	}
	    	
	    	//易地扶贫搬迁
	    	if (typeof data.yidi != "undefined") {
	    		//集中安置或者分散安置
	    		if(data.yidi.move_type==''){
		    		$("#yidifupinbanqian input[name='move_type'][type=radio]").removeAttr("checked");
		    		$("#yidifupinbanqian input[name='move_type'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		if(data.yidi.move_type=='集中安置'){
		    			$("#yidifupinbanqian #leixing_1").show();
		    			$("#yidifupinbanqian #leixing_2").hide();
		    			$("#yidifupinbanqian #fangyuandi").hide();
		    			$("#yidifupinbanqian #fangjia").hide();
		    			$("#yidifupinbanqian #yonggongxieyi").hide();
		    		}else if(data.yidi.move_type=='分散安置'){
		    			$("#yidifupinbanqian #leixing_1").hide();
		    			$("#yidifupinbanqian #leixing_2").show();
		    		}
		    		$("#yidifupinbanqian input[name='move_type'][value='"+data.yidi.move_type+"']").prop("checked","true");//[0]
		    		$("#yidifupinbanqian input[name='move_type'][value='"+data.yidi.move_type+"']").parent(".iradio_square-green").addClass("checked");
		    	}
	    		
	    		//集中安置分类
	    		if(data.yidi.focus_info==''){
		    		$('#yidifupinbanqian #focus_info').val("请选择");
		    	}else{
		    		$('#yidifupinbanqian #focus_info').val(data.yidi.focus_info);
		    	}
	    		
	    		//分散安置分类
	    		if(data.yidi.dispersed_info==''){
		    		$('#yidifupinbanqian #dispersed_info').val("请选择");
		    	}else{
		    		$('#yidifupinbanqian #dispersed_info').val(data.yidi.dispersed_info);
		    		if(data.yidi.dispersed_info=='进城购房'){
		    			$("#yidifupinbanqian #fangyuandi").show();
		    			$("#yidifupinbanqian #fangjia").show();
		    			$("#yidifupinbanqian #yonggongxieyi").show();
		    		}else{
		    			$("#yidifupinbanqian #fangyuandi").hide();
		    			$("#yidifupinbanqian #fangjia").hide();
		    			$("#yidifupinbanqian #yonggongxieyi").hide();
		    		}
		    	}
	    		//房源地
	    		$('#yidifupinbanqian #dispersed_address').val(data.yidi.dispersed_address);
	    		//房价（万元）
		    	$('#yidifupinbanqian #dispersed_price').val(data.yidi.dispersed_price);
		    	
		    	//与用工企业签订就业安置协议
		    	if(data.yidi.dispersed_agreement==''){
		    		$("#yidifupinbanqian input[name='dispersed_agreement'][type=radio]").removeAttr("checked");
		    		$("#yidifupinbanqian input[name='dispersed_agreement'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#yidifupinbanqian input[name='dispersed_agreement'][value='"+data.yidi.dispersed_agreement+"']").prop("checked","true");//[0]
		    		$("#yidifupinbanqian input[name='dispersed_agreement'][value='"+data.yidi.dispersed_agreement+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	
		    	if(data.yidi.v1==''){
		    		$("#yidifupinbanqian input[name='v1'][type=radio]").removeAttr("checked");
		    		$("#yidifupinbanqian input[name='v1'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#yidifupinbanqian input[name='v1'][value='"+data.yidi.v1+"']").prop("checked","true");//[0]
		    		$("#yidifupinbanqian input[name='v1'][value='"+data.yidi.v1+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	
		    	if(data.yidi.v2==''){
		    		$("#yidifupinbanqian input[name='v2'][type=radio]").removeAttr("checked");
		    		$("#yidifupinbanqian input[name='v2'][type=radio]").parent(".icheckbox_square-green").removeClass("checked");
		    	}else{
		    		$("#yidifupinbanqian input[name='v2'][value='"+data.yidi.v2+"']").prop("checked","true");//[0]
		    		$("#yidifupinbanqian input[name='v2'][value='"+data.yidi.v2+"']").parent(".iradio_square-green").addClass("checked");
		    	}
		    	
		    	if(data.yidi.v3!=''){
			    	if(data.yidi.v3.indexOf(",")>-1){//多选
			    		var strs= new Array(); //定义一数组 
			    		strs=data.yidi.v3.split(","); //字符分割 
			    		for (i=0;i<strs.length ;i++ ){ 
			    			$("#yidifupinbanqian #v3_div input[value='"+strs[i]+"']").prop("checked","true");//[0]
					    	$("#yidifupinbanqian #v3_div input[value='"+strs[i]+"']").parent(".icheckbox_square-green").addClass("checked");
			    		} 
			    	}else{//单选
			    		$("#yidifupinbanqian #v3_div input[value='"+data.yidi.v3+"']").prop("checked","true");//[0]
				    	$("#yidifupinbanqian #v3_div input[value='"+data.yidi.v3+"']").parent(".icheckbox_square-green").addClass("checked");
			    	}
		    	}
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
}

var jiating_biaoshi;
var hang_jt = 0;//家庭当前选中的行
//家庭成员写入前
function jiating(i,pkid,pic){
	$("#jiatingchengyuan #v6").val("");//家庭成员姓名
	$('#jiatingchengyuan #v8').val("");//证件号码
	$('#jiatingchengyuan #v7').val("请选择");//性别
	$('#jiatingchengyuan #v10').val("请选择");//与户主关系
	$('#jiatingchengyuan #v11').val("请选择");//民族
	$('#jiatingchengyuan #v12').val("请选择");//文化程度
	$('#jiatingchengyuan #v13').val("请选择");//是否在校
	$('#jiatingchengyuan #v14').val("请选择");//健康状况
	$('#jiatingchengyuan #v15').val("请选择");//劳动力
	$('#jiatingchengyuan #v16').val("请选择");//务工情况
	$('#jiatingchengyuan #v17').val("");//务工时间
	$('#jiatingchengyuan #v28').val("请选择");//政治面貌
	
	$("#jiatingchengyuan #jiating_pkid").val("");
	$("#jiatingchengyuan input[type=radio]").removeAttr("checked");
	$("#jiatingchengyuan input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	hang_jt = i;
	$("#jieting_up_div").show();
	
	$('#tab-j-2 #pkid').val(pkid);//家庭成员照片上传的ID
	$("#tab-j-2 #jtcy_pic_show").attr("src", $('#jiatingchengyuan #jiating_table #vjtpic_tab_'+i).val());
	
	jiating_biaoshi = i;
	$("#jiatingchengyuan #jiating_pkid").val(pkid);
	$("#jiatingchengyuan #v6").val($('#jiatingchengyuan #jiating_table #v6_'+i).html());//家庭成员姓名
	if($('#jiatingchengyuan #jiating_table #v7_'+i).html()==''){
		$('#jiatingchengyuan #v7').val("请选择");
	}else{
		$('#jiatingchengyuan #v7').val($('#jiatingchengyuan #jiating_table #v7_'+i).html());
	}
	$('#jiatingchengyuan #v8').val($('#jiatingchengyuan #jiating_table #v8_'+i).html());//证件号码
	if($('#jiatingchengyuan #jiating_table #v10_'+i).html()==''){
		$('#jiatingchengyuan #v10').val("请选择");//与户主关系
	}else{
		$('#jiatingchengyuan #v10').val($('#jiatingchengyuan #jiating_table #v10_'+i).html());//与户主关系
	}
	if($('#jiatingchengyuan #jiating_table #v11_'+i).html()==''){
		$('#jiatingchengyuan #v11').val("请选择");
	}else{
		$('#jiatingchengyuan #v11').val($('#jiatingchengyuan #jiating_table #v11_'+i).html());
	}
	
	if($('#jiatingchengyuan #jiating_table #v28_'+i).html()==''){
		$('#jiatingchengyuan #v28').val("请选择");
	}else{
		$('#jiatingchengyuan #v28').val($('#jiatingchengyuan #jiating_table #v28_'+i).html());
	}
	
	if($('#jiatingchengyuan #jiating_table #v12_'+i).html()==''){
		$('#jiatingchengyuan #v12').val("请选择");
	}else{
		$('#jiatingchengyuan #v12').val($('#jiatingchengyuan #jiating_table #v12_'+i).html());
	}
	if($('#jiatingchengyuan #jiating_table #v13_'+i).html()==''){
		$('#jiatingchengyuan #v13').val("请选择");
	}else{
		$('#jiatingchengyuan #v13').val($('#jiatingchengyuan #jiating_table #v13_'+i).html());
	}
	if($('#jiatingchengyuan #jiating_table #v14_'+i).html()==''){
		$('#jiatingchengyuan #v14').val("请选择");
	}else{
		$('#jiatingchengyuan #v14').val($('#jiatingchengyuan #jiating_table #v14_'+i).html());
	}
	if($('#jiatingchengyuan #jiating_table #v15_'+i).html()==''){
		$('#jiatingchengyuan #v15').val("请选择");
	}else{
		$('#jiatingchengyuan #v15').val($('#jiatingchengyuan #jiating_table #v15_'+i).html());
	}
	if($('#jiatingchengyuan #jiating_table #v16_'+i).html()==''){
		$('#jiatingchengyuan #v16').val("请选择");
	}else{
		$('#jiatingchengyuan #v16').val($('#jiatingchengyuan #jiating_table #v16_'+i).html());
	}
	$("#jiatingchengyuan #v17").val($('#jiatingchengyuan #jiating_table #v17_'+i).html());
	
	
	if($('#jiatingchengyuan #jiating_table #v18_'+i).html()==''){
		$("#jiatingchengyuan #v18_div input[type=radio]").removeAttr("checked");
		$("#jiatingchengyuan #v18_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	}else{
		$("#jiatingchengyuan input[name='v18'][value='"+$('#jiatingchengyuan #jiating_table #v18_'+i).html()+"']").prop("checked","true");//[0]
		$("#jiatingchengyuan input[name='v18'][value='"+$('#jiatingchengyuan #jiating_table #v18_'+i).html()+"']").parent(".iradio_square-green").addClass("checked");
	}
	
	if($('#jiatingchengyuan #jiating_table #v19_'+i).html()==''){
		$("#jiatingchengyuan #v19_div input[type=radio]").removeAttr("checked");
		$("#jiatingchengyuan #v19_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	}else{
		$("#jiatingchengyuan input[name='v19'][value='"+$('#jiatingchengyuan #jiating_table #v19_'+i).html()+"']").prop("checked","true");//[0]
		$("#jiatingchengyuan input[name='v19'][value='"+$('#jiatingchengyuan #jiating_table #v19_'+i).html()+"']").parent(".iradio_square-green").addClass("checked");
	}
	
	if($('#jiatingchengyuan #jiating_table #v32_'+i).html()==''){//是否现役军人
		$("#jiatingchengyuan #v32_div input[type=radio]").removeAttr("checked");
		$("#jiatingchengyuan #v32_div input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	}else{
		$("#jiatingchengyuan input[name='v32'][value='"+$('#jiatingchengyuan #jiating_table #v32_'+i).html()+"']").prop("checked","true");//[0]
		$("#jiatingchengyuan input[name='v32'][value='"+$('#jiatingchengyuan #jiating_table #v32_'+i).html()+"']").parent(".iradio_square-green").addClass("checked");
	}
}

//基本信息初始化
function jbqk_initialization(){
	//户主信息
	$("#shang_yi #hu_pkid").val("");//记录户主ID
	$("#huzhu #v6").val("");//户主姓名
	$('#huzhu #v7').val("请选择");//性别
	$('#huzhu #v11').val("请选择");//民族
	$('#huzhu #v25').val("");//联系电话
	$('#huzhu #v26').val("");//开户银行
	$('#huzhu #v27').val("");//银行账号
	$('#huzhu #v16').val("请选择");//务工情况
	$('#huzhu #v17').val("");//务工时间
	$('#huzhu #sys_standard').val("请选择");//识别标准
	$('#huzhu #v8').val("");//证件号码
	//$('#huzhu #hz_jtzz').val("");
	$('#huzhu #v12').val("请选择");//文化程度
	$('#huzhu #v13').val("请选择");//是否在校
	$('#huzhu #v14').val("请选择");//健康状况
	$('#huzhu #v15').val("请选择");//劳动力
	$('#huzhu #v28').val("请选择");//政治面貌
	$('#huzhu #v22').val("请选择");//贫困户属性
	$('#huzhu #hz_zpyy').val("请选择");//致贫原因
	$("#huzhu input[type=checkbox]").removeAttr("checked");
	$("#huzhu input[type=checkbox]").parent(".icheckbox_square-green").removeClass("checked");
	$("#huzhu input[type=radio]").removeAttr("checked");
	$("#huzhu input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	
	//$('#huzhu #v33_div').html("<label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"因病\"> <i></i>因病</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"因残\"><i></i>因残</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"因学\"><i></i>因学</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"因灾\"><i></i>因灾</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺土地\"> <i></i>缺土地</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺住房\"> <i></i>缺住房</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺水\"> <i></i>缺水</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺电\"> <i></i>缺电</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺技术\"> <i></i>缺技术</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺劳动力\"> <i></i>缺劳动力</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"缺资金\"> <i></i>缺资金</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"交通条件落后\"> <i></i>交通条件落后</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"自身发展动力不足\"> <i></i>自身发展动力不足</label><label><input type=\"checkbox\" id=\"v33\" name=\"v33\" value=\"其他\"> <i></i>其他</label>");
	
	//家庭成员
	$('#jiatingchengyuan #jiating_table').html("<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>");
	
	//生产条件
	$("#shengchan #v1").val("");
	$("#shengchan #v2").val("");
	$("#shengchan #v3").val("");
	$("#shengchan #v4").val("");
	$("#shengchan #v5").val("");
	$("#shengchan #v6").val("");
	$("#shengchan #v7").val("");
	$("#shengchan #v8").val("");
	$("#shengchan #v9").val("");
	$("#shengchan #v10").val("");
	$("#shengchan #v11").val("");
	$("#shengchan #v12").val("");
	$("#shengchan #v13").val("");
	$("#shengchan #v14").val("");
	
	//生活条件
	$("#shenghuo #v1").val("");
	$("#shenghuo #v7").val("");
	$('#shenghuo #v10').val("请选择");
	$("#shenghuo input[type=radio]").removeAttr("checked");
	$("#shenghuo input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	$("#shenghuo input[type=checkbox]").removeAttr("checked");
	$("#shenghuo input[type=checkbox]").parent(".icheckbox_square-green").removeClass("checked");
	
	//易地扶贫搬迁
	$('#yidifupinbanqian #focus_info').val("请选择");//集中安置分类
	$('#yidifupinbanqian #dispersed_info').val("请选择");//分散安置分类
	$("#yidifupinbanqian #dispersed_address").val("");//房源地
	$("#yidifupinbanqian #dispersed_price").val("");//房价（万元）
	$("#yidifupinbanqian input[type=radio]").removeAttr("checked");
	$("#yidifupinbanqian input[type=radio]").parent(".iradio_square-green").removeClass("checked");
	$("#yidifupinbanqian input[type=checkbox]").removeAttr("checked");
	$("#yidifupinbanqian input[type=checkbox]").parent(".icheckbox_square-green").removeClass("checked");
}

//户主保存
function huzhu_save(){	
	if($("#huzhu_Form #sys_standard").val()=='请选择'){
		toastr["warning"]("warning", "识别标准必须选择");
	}else if($("#huzhu_Form #v6").val()==''){
		toastr["warning"]("warning", "户主姓名必须填写");
	}else if($("#huzhu_pic_show")[0].src.indexOf("zw.jpg")>0){
		toastr["warning"]("warning", "户主照片必须上传");
	}else if($("#huzhu_Form #v7").val()=='请选择'){
		toastr["warning"]("warning", "户主性别必须选择");	
	}else if($("#huzhu_Form #v11").val()=='请选择'){
		toastr["warning"]("warning", "户主民族必须选择");	
	}else if($("#huzhu_Form #v25").val()==''){
		toastr["warning"]("warning", "联系电话必须填写");	
	}else if($("#huzhu_Form #v8").val()==''){
		toastr["warning"]("warning", "户主身份证必须填写");	
	}else if($("#huzhu_Form #v28").val()=='请选择'){
		toastr["warning"]("warning", "政治面貌必须选择");	
	}
//	else if($("#huzhu_Form #hz_jtzz").val()==''){
//		toastr["warning"]("warning", "家庭地址必须填写");	
//	}
	else if($("#huzhu_Form #cha_qx2").val()=='请选择'){
		toastr["warning"]("warning", "旗县必须选择");
	}else if($("#huzhu_Form #cha_smx2").val()=='请选择'){
		toastr["warning"]("warning", "苏木乡必须选择");
	}
//	else if($("#huzhu_Form #cha_gcc2").val()=='请选择'){
//		toastr["warning"]("warning", "嘎查村必须选择");
//	}
	else if($("#huzhu_Form #v22").val()=='请选择'){
		toastr["warning"]("warning", "贫苦户属性必须选择");
	}else if($("#huzhu_Form #v12").val()=='请选择'){
		toastr["warning"]("warning", "文化程度必须选择");	
	}else if($("#huzhu_Form #v13").val()=='请选择'){
		toastr["warning"]("warning", "是否在校必须选择");	
	}else if($("#huzhu_Form #v14").val()=='请选择'){
		toastr["warning"]("warning", "健康状况必须选择");	
	}else if($("#huzhu_Form #v15").val()=='请选择'){
		toastr["warning"]("warning", "劳动力必须选择");	
	}else if($("#huzhu_Form #v16").val()=='请选择'){
		toastr["warning"]("warning", "务工情况必须选择");	
	}else if($("#huzhu_Form #v17").val()==''){
		toastr["warning"]("warning", "务工时间必须填写");	
	}else if($('input:radio[name="v18"]:checked').val()==undefined){
		toastr["warning"]("warning", "是否参加新农合必须选择");	
	}else if($('input:radio[name="v19"]:checked').val()==undefined){
		toastr["warning"]("warning", "是否参加养老保险必须选择");
	}else if($('input:radio[name="v29"]:checked').val()==undefined){
		toastr["warning"]("warning", "是否军烈属必须选择");	
	}else if($('input:radio[name="v32"]:checked').val()==undefined){
		toastr["warning"]("warning", "是否现役军人必须选择");	
	}else if($('input:radio[name="v30"]:checked').val()==undefined){
		toastr["warning"]("warning", "是否独生子女必须选择");	
	}else if($('input:radio[name="v31"]:checked').val()==undefined){
		toastr["warning"]("warning", "是否双女必须选择");	
	}else if($('input:radio[name="v23"]:checked').val()==undefined){
		toastr["warning"]("warning", "主要致贫原因必须选择");		
	}else if($('input:checkbox[name="v33"]:checked').val()==undefined){
		toastr["warning"]("warning", "其他致贫原因必须选择");	
	}else{
		var form_val = JSON.stringify(getFormJson("#huzhu_Form"));//表单数据字符串
		$.ajax({  		       
		    url: "/assa/getSavehuzhu.do",
		    type: "POST",
		    async:false,
		    dataType: "text",
		    data:{
		    	pkid: $("#shang_yi #hu_pkid").val(),
		    	form_val: form_val
	        },
		    success: function (data) {
		    	if (data == "1") {
		    		toastr["success"]("success", "户主信息修改");
		    	}else{
		    		toastr["warning"]("warning", "修改失败，检查数据后重试");
		    	}
		    },
		    error: function () { 
		    	toastr["error"]("error", "服务器异常");
		    }  
		});
	}
}

//家庭成员保存
function jiatingchengyuan_save(){
	var form_val = JSON.stringify(getFormJson("#jiatingchengyuan_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSavejiating.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	form_val: form_val
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "家庭成员信息修改");
	    		var i = jiating_biaoshi;
	    		
	    		$('#jiatingchengyuan #jiating_table #v6_'+i).html($("#jiatingchengyuan #v6").val());
	    		$('#jiatingchengyuan #jiating_table #v8_'+i).html($('#jiatingchengyuan #v8').val());
	    		
	    		if($('#jiatingchengyuan #v7').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v7_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v7_'+i).html($('#jiatingchengyuan #v7').val());
	    		}
	    		
	    		if($('#jiatingchengyuan #v10').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v10_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v10_'+i).html($('#jiatingchengyuan #v10').val());
	    		}
	    		if($('#jiatingchengyuan #v11').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v11_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v11_'+i).html($('#jiatingchengyuan #v11').val());
	    		}
	    		if($('#jiatingchengyuan #v12').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v12_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v12_'+i).html($('#jiatingchengyuan #v12').val());
	    		}
	    		if($('#jiatingchengyuan #v13').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v13_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v13_'+i).html($('#jiatingchengyuan #v13').val());
	    		}
	    		if($('#jiatingchengyuan #v14').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v14_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v14_'+i).html($('#jiatingchengyuan #v14').val());
	    		}
	    		if($('#jiatingchengyuan #v15').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v15_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v15_'+i).html($('#jiatingchengyuan #v15').val());
	    		}
	    		if($('#jiatingchengyuan #v16').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v16_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v16_'+i).html($('#jiatingchengyuan #v16').val());
	    		}
	    		$('#jiatingchengyuan #jiating_table #v17_'+i).html($('#jiatingchengyuan #v17').val());
	    		if($('#jiatingchengyuan #v28').val()=='请选择'){
	    			$('#jiatingchengyuan #jiating_table #v28_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v28_'+i).html($('#jiatingchengyuan #v28').val());
	    		}
	    		
	    		if($("#jiatingchengyuan input[name='v18']:checked").val()==undefined){
	    			//$('#jiatingchengyuan #jiating_table #v18_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v18_'+i).html($("#jiatingchengyuan input[name='v18']:checked").val());
	    		}
	    		
	    		if($("#jiatingchengyuan input[name='v19']:checked").val()==undefined){
	    			//$('#jiatingchengyuan #jiating_table #v19_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v19_'+i).html($("#jiatingchengyuan input[name='v19']:checked").val());
	    		}
	    		
	    		if($("#jiatingchengyuan input[name='v32']:checked").val()==undefined){
	    			//$('#jiatingchengyuan #jiating_table #v32_'+i).html("");
	    		}else{
	    			$('#jiatingchengyuan #jiating_table #v32_'+i).html($("#jiatingchengyuan input[name='v32']:checked").val());
	    		}
	    		
	    		$("#jieting_up_div").hide();
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//生产条件
function shengchan_save(){
	var form_val = JSON.stringify(getFormJson("#shengchan_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaveshengchan.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	data_year:$("#data_year").val(),
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "生产条件修改");
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//生活条件
function shenghuo_save(){
	var form_val = JSON.stringify(getFormJson("#shenghuo_Form"));//表单数据字符串
	$.ajax({  		       
	    url: "/assa/getSaveshenghuo.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	data_year:$("#data_year").val(),
	    	pkid: $("#shang_yi #hu_pkid").val(),
	    	form_val: form_val
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "生活条件修改");
	    		if($("#shenghuo input[name='v3']:checked").val()=='是'){
	    			$("#ydfpbq").show();
	    			yidi_pic_load('6',$("#shang_yi #hu_pkid").val());
	    			yidi_pic_show($("#shang_yi #hu_pkid").val());
	    		}else{
	    			$("#ydfpbq").hide();
	    		}
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//查看图片
function yidi_pic_show(pkid){
	$.ajax({
	    url: "/assa/getPic_info.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	pkid:pkid,
	    	type:6
        },
	    success: function (data) {
	    	//现有图片
	    	var tupian_html = "";
	    	$.each(data.pic,function(i,item){
	    		tupian_html += "<li id=\"pin_li_"+item.pkid+"\"><p class=\"imgWrap\"><a href=\""+item.pic_path+"\" title=\"易地扶贫搬迁图片\" data-gallery=\"\">" +
	    				"<img src=\""+item.pic_path+"\" style=\"margin:0;vertical-align:baseline;width:130px;height:85px;\"></a></p>" +
	    				"<div id=\"pin_del_"+item.pkid+"\" class=\"file-panel\" style=\"height: 0px;\"><span class=\"cancel\" onclick='pic_del_yidi("+item.pkid+","+pkid+");'>删除</span></div></li>";
	    	});
	    	
	    	$('#yidifupinbanqian #poht_list').html(tupian_html);//要先循环一遍加上html后 再循环一遍，添加事件
	    	$.each(data.pic,function(i,item){
	    		$("#yidifupinbanqian #pin_li_"+item.pkid).mouseenter(function(){  
	    			$("#yidifupinbanqian #pin_del_"+item.pkid).stop().animate({
		            	height: 30
		            });
	    		}); 
	    		
	    		$("#yidifupinbanqian #pin_li_"+item.pkid).mouseleave(function(){
	    			$("#yidifupinbanqian #pin_del_"+item.pkid).stop().animate({
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

function pic_del_yidi(pkid,fid){
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
	    		toastr["success"]("success", "删除易地扶贫搬迁图片");
	    		yidi_pic_show(fid);
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
function yidi_pic_load(type,id){
	//照片上传初始化
	$("#yidifupinbanqian #yidi_img_load").html("<div class=\"page-container\"><p>您可以尝试文件拖拽，使用QQ截屏工具，然后激活窗口后粘贴，或者点击添加图片按钮。支持：gif,jpg,jpeg,png,bmp。</p><div id=\"uploader\" class=\"wu-example\">" +
			"<div class=\"queueList\"><div id=\"dndArea\" class=\"placeholder\"><div id=\"filePicker\"></div><p>或将照片拖到这里，单次最多可选10张，单张照片限10M，总共限20M</p></div>" +
			"</div><div class=\"statusBar\" style=\"display:none;\"><div class=\"progress\"><span class=\"text\">0%</span><span class=\"percentage\"></span></div>" +
			"<div class=\"info\"></div><div class=\"btns\"><div id=\"filePicker2\"></div><div class=\"uploadBtn\">开始上传</div></div></div></div></div>");
	Img_load('6',id,'yidi_img_load');
}


