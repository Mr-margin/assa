var isWinRT = (typeof Windows === "undefined") ? false : true;
var ver = "v=2.0.2";

function inputScript(inc){
    if (!isWinRT) {
        var script = '<' + 'script type="text/javascript" src="' + inc + '"' + '><' + '/script>';
        document.writeln(script);
    } else {
        var script = document.createElement("script");
        script.src = inc;
        document.getElementsByTagName("HEAD")[0].appendChild(script);
    }
}

function inputCSS(style){
    if (!isWinRT) {
        var css = '<' + 'link rel="stylesheet" href="' + style + '"' + '><' + '/>';
        document.writeln(css);
    } else { 
        var link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = style;
        document.getElementsByTagName("HEAD")[0].appendChild(link);
    }
}

function loadSMLibs(str) {//
	
	inputScript('js/jquery.min.js?v=2.1.4');
	inputScript('js/bootstrap.min.js?v=3.3.5');
	inputScript('js/content.min.js?v=1.0.0');
	inputScript('js/plugins/slimscroll/jquery.slimscroll.min.js');
	inputScript('js/plugins/flot/jquery.flot.js');
	inputScript('js/plugins/flot/jquery.flot.tooltip.min.js');
	inputScript('js/plugins/flot/jquery.flot.resize.js');
	inputScript('js/plugins/bootstrap-table/bootstrap-table.min.js');//表格
	inputScript('js/plugins/bootstrap-table/bootstrap-table-mobile.min.js');//表格
	inputScript('js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js');//表格
	inputScript('js/plugins/validate/jquery.validate.min.js');//表单验证
	inputScript('js/plugins/validate/messages_zh.min.js');//表单验证
	inputScript('js/plugins/toastr/toastr.min.js');//消息框
	inputScript('js/plugins/sweetalert/sweetalert.min.js');//对话框
	inputScript('js/plugins/iCheck/icheck.min.js');
	inputScript('js/plugins/blueimp/jquery.blueimp-gallery.min.js');
	inputScript('js/plugins/peity/jquery.peity.min.js');
	inputScript('js/plugins/echarts/echarts.min.3.1.2.js');
	
	inputScript('js/minzu.js?'+ver);
	inputScript('js/index/tongyong.js?'+ver);//登录与权限
	inputScript('js/index/Jump.js?'+ver);//各种下拉框赋值
	inputScript('js/index/tanchu.js?'+ver);//登录与修改密码弹出相关
	if(str=='1-2'){
		inputScript('js/jquery.form.js');
		inputScript('js/plugins/suggest/bootstrap-suggest.min.js');//按钮式下拉菜单组件的搜索建议插件
		inputScript('js/plugins/webuploader/webuploader.min.js');//百度Web Uploader文件上传工具
		inputScript('js/plugins/datapicker/bootstrap-datepicker.js');//简单好用的日期选择器
		inputScript('js/plugins/prettyfile/bootstrap-prettyfile.js');//文件上传
		
		inputScript('js/index/Img_load.js?'+ver);
		inputScript('js/index/H1.js?'+ver);
		inputScript('js/index/H1-1.js?'+ver);
		inputScript('js/index/H1-2.js?'+ver);
		inputScript('js/index/H1-3.js?'+ver);
		inputScript('js/index/H1-4.js?'+ver);
		inputScript('js/index/H1-5.js?'+ver);
		inputScript('js/index/H1-6.js?'+ver);
		inputScript('js/index/H1-7.js?'+ver);
	}else if(str=='index'){
		inputScript('js/index/index.js?'+ver);
	}else if(str=='9'){
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/index/H9.js?'+ver);
	}else if(str=='8'){
		inputScript('js/pooruser/currency.js?'+ver);
		inputScript('js/index/H8.js?'+ver);
	}else if(str=='7-2'){
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/index/H7-2.js?'+ver);
	}else if(str=='7-1'){
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/index/H7-1.js?'+ver);
	}else if(str=='6-1'){
		inputScript('js/currency.js?'+ver);
		inputScript('js/gachacun/H6.js?'+ver);
	}else if(str=='5-3'){
		inputScript('js/plugins/suggest/bootstrap-suggest.min.js');//按钮式下拉菜单组件的搜索建议插件
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/plugins/chosen/chosen.jquery.js');//多选选择器
		inputScript('js/plugins/chosen/form-advanced-demo.min.js');//多选选择器
		inputScript('js/index/H5-3.js?'+ver);
	}else if(str=='5-2'){
		inputScript('js/index/H5-2.js?'+ver);
	}else if(str=='5-1'){
		inputScript('js/plugins/suggest/bootstrap-suggest.min.js');//按钮式下拉菜单组件的搜索建议插件
		inputScript('js/plugins/datapicker/bootstrap-datepicker.js');//简单好用的日期选择器
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/pooruser/currency.js?'+ver);
		inputScript('js/pooruser/pooruser.js?'+ver);
	}else if(str=='5-1-1'){
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/tree/treeview.js?'+ver);
	}else if(str=='4'){
		inputScript('js/index/H4.js?'+ver);
	}else if(str=='3-2'){
		inputScript('js/plugins/treeview/bootstrap-treeview.js');//树形结构
		inputScript('js/index/H3-2.js?'+ver);
	}else if(str=='3-1'){
		inputScript('js/index/H3-1.js?'+ver);
	}else if(str=='2'){
		inputScript('js/pooruser/currency.js?'+ver);
		inputScript('js/index/H2.js?'+ver);
	}else if(str=='2-1'){
		inputScript('js/pooruser/currency.js?'+ver);
		inputScript('js/index/H2_1.js?'+ver);
	}else if(str=='2-2'){
		inputScript('js/index/H2_2.js?'+ver);
	}else if(str=='10'){
		inputScript('js/plugins/treeview/bootstrap-treeview.js');
		inputScript('js/index/H10.js?'+ver);
		inputScript('js/pooruser/currency.js?'+ver);
		
	}
}
