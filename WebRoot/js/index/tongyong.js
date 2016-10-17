$.validator.addMethod("cp", function(value, element) {//修改密码时，验证输入的旧密码是否正确
	var cf="";
	var pkid = jsondata.Login_map.pkid;
	$.ajax({  		       
		url:"/assa/o_password.do",
		type: "POST",
		async:false,
		dataType: "text",
		data:{
			pkid:pkid,
			val:value
		},
		success: function (data) {
			if (data == "0") {
				cf=data;
			}else{
				return false
			}
		},
		error: function () { 
		}  
	});
	return this.optional(element) || cf!="0";
}, "原密码错误");

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
/**
 * 传参
 * @author chendong
 * @date 2016年8月3日
 * @param url
 * @param data
 * @returns
 */
function ajax_async_t(url,data,dataType,async){
	var rel;
	if(async==""||async==undefined){
		async=true;
	}else{
		async=false;
	}
	$.ajax({  		       
	    url: url,
	    type: "POST",
	    async:false,
	    dataType: dataType,
	    data: data,
	    success: function (ret) {
	    	rel = ret;
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	return rel;
}
$(function () {
	
	$("#weibu_title").html("<div class=\"pull-right\">技术支持：北京山海础石信息技术有限公司</div><div><strong>Copyright</strong>&nbsp;&nbsp;&nbsp;<i class=\"fa fa-copyright\"></i>&nbsp;&nbsp;&nbsp;2016&nbsp;&nbsp;&nbsp;鄂尔多斯市扶贫开发办公室&nbsp;&nbsp;&nbsp;版权所有&nbsp;&nbsp;&nbsp;蒙ICP备13001436号</div>");
	shifoudenglu();//进入页面时判断是否登陆，如果登录，获取用户的菜单与数据权限
	
	$("#changepassword_form").validate({//validate实时验证
		onfocusout: function(element){
			$(element).valid();
		}
	});
	
	//点击弹出登录框
	$("#tc").click(function(){
		$("#gray").show();
		$("#popup").show();//查找ID为popup的DIV show()显示#gray
		tc_center();
	});
	
	//赋值于下拉按钮
	$("#chauxnshiousuo").html('<i class="fa fa-chevron-down"></i><strong onclick="title_tle_zhankai()">展开</strong>');
});

var jsondata;//用户session中的全部信息


//进入页面时，判断是否登陆
function shifoudenglu(){
	$.ajax({  		       
	    url:"/assa/getLogin_massage.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    success: function (data) {
	    	if (data == "weidenglu") {//如果未登录，上方按钮显示用户登录
	    		//alert(window.location.href);
	    		var url = window.location.href;
	    		if(url.substring(url.lastIndexOf("/")+1)!="index.html"){
	    			window.location.replace("/assa/index.html");
	    		}
		    		//$("#yonghuxiangguan").html('<a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown" id="tc"><i class="fa fa-user"></i><div style="display:inline;" id="yonghumingzi11">用户登录 </div><div id="yh_xinxi" style="display:none;"></div></a>');
	    		$("#yonghuxiangguan").html("<button type=\"button\" class=\"btn btn-primary\" style=\"display:inline;margin-top: 10px;margin-right: 15px;\" id=\"tc\" name=\"tc\" ><i class=\"fa fa-user\"></i>&nbsp;&nbsp;<span class=\"bold\">用户登录</span></button>");
//	    		//出现悬浮登陆按钮
//	    		var gohome = '<div id="xuanfu_1" class="gohome"><a style="padding-top:15px;" id="tc" class="animated bounceInUp"  href="javascript:void(0);"  title="点击登录">登录</a></div>';
//	    		$("body").append(gohome);
	    		
	    	}else{//如果登录成功，显示用户信息
	    		jsondata = eval("("+data+")");
	    		$("#yonghuxiangguan").html('<a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i><div style="display:inline;" id="yonghumingzi">'+jsondata.company.com_name+'</div><div id="yh_xinxi" style="display:none;"></div><span class="caret"></span></a><ul role="menu" class="dropdown-menu"><li><a href="orig_data/manual.pdf">使用手册</a></li><li id=""><a onclick="xiugaimima()">修改密码</a></li><li id=""><a id="out">返回平台</a></li><li id=""><a onclick="tuichudenglu()">退出登录</a></li></ul>');
	    		$("#yh_xinxi").html(jsondata);
//	    		//出现个人信息按钮
//	    		var gohome = '<div id="xuanfu_2" class="gohome_1"><a style="padding-top:14px;" id="yhxx"  class="gohome_2  animated bounceInUp" title="个人信息"></a><a style="padding-top: 35px;margin-left:-1px" class="gohome_3 animated bounceInUp" href="javascript:"display=none;" onclick="xiugaimima()">修改密码</a><a style="padding-top: 35px;margin-left:60px" class="gohome_4 animated bounceInUp" href="javascript:"display=none;" onclick="tuichudenglu()">退出登录</a></div>';
//	    		$("body").append(gohome);
//	    		$("#yhxx").html(jsondata.username);
//	    		jsondata = eval("("+data+")");
	    		
	    		//对菜单进行设置
	    		if(typeof jsondata.function_map != "undefined"){
	    			$.each(jsondata.function_map,function(i,item){
	    				//if(item.charAt(0)=="H"){
	    				$("#"+item).css("display","");
	    				//}
	    			});
	    		}
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//点击确认登录
function denglu(){
	$.ajax({  		       
	    url:"/assa/loginin.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	add_account:$("#name").val(),
	    	add_password:$("#pass").val()
        },
	    success: function (data) {
	    	if (data == "1") {
	    		location.reload();
	    	}
	    	if (data == "0"){
	    		toastr["warning"]("warning", "失败，账号或密码错误");
	    	}
	    	if (data == "2"){
	    		toastr["warning"]("warning", "失败，用户名不存在");
	    	}
	    	else{
	    		
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	}); 
}

//点击进入修改密码弹窗
function xiugaimima(){
	$("#gray_2").show();
	$("#popup_2").show();//查找ID为popup的DIV show()显示#gray
	tc_center();
}

//点击确认修改
function xiugaimima_anniu(){
	var validator = $("#changepassword_form").validate();
	if(validator.form()){
		up_password(jsondata.Login_map.pkid,$("#new_pass").val());
	}
}
//修改密码方法
function up_password(pkid,new_pass){
	$.ajax({  		       
	    url: "/assa/upPassword.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid:pkid,
	    	password:new_pass
        },
	    success: function (data) {
	    	if (data == "1") {
	    		toastr["success"]("success", "密码已修改");
	    		shuaxin_xiugaimima();
	    		$(".guanbi").click();//关闭弹窗
	    	}else{
	    		toastr["warning"]("warning", "修改失败，检查数据后重试");
	    		shuaxin_xiugaimima();
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//退出登录
function tuichudenglu(){
	$.ajax({  		       
	    url: "/assa/login_out.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    success: function (data) {
	    	if (data == "1") {
	    		window.location.href="index.html";
	    	}else{
	    		toastr["warning"]("warning", "退出失败，检查数据后重试");
	    		shuaxin_xiugaimima();
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//刷新修改密码弹窗内容
function shuaxin_xiugaimima(){//刷新
	$("#old_pass").val("");
	$("#new_pass").val("");
	$("#new_pass_2").val("");
}

//查询框-点击展开
function title_tle_zhankai(){
	$("#chauxnshiousuo").html('<i class="fa fa-chevron-down"></i><strong onclick="title_tle_shousuo()">收起</strong>');
}
//查询框-点击收起
function title_tle_shousuo(){
	$("#chauxnshiousuo").html('<i class="fa fa-chevron-up"></i><strong  onclick="title_tle_zhankai()">展开</strong>');
}