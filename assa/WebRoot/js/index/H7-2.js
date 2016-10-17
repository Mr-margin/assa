$(function() {
    $('#treeview').treeview({
    	color: "#428bca",
    	multiSelect: false,
    	highlightSelected: true,
    	data: show_tree(),
    	onNodeSelected: function(e, o) {
            click_tree(o);
        }
    });
    
    com_level=jsondata.company.com_level;//用户层级
    code=jsondata.company.com_code;//行政编码
    com_name=jsondata.company.com_name;//名称
    data_info(jsondata.company.pkid)//数据初始化，传进用户id
    
    //标题赋值
    $('#title_name').html(com_name);
    //对下方表格表头赋值
    $('#xb_xzqh').html(xsmc);
});

var code;//行政编码
var com_level;//层级
var com_name;//名称
var xsmc;//名称
var datas;//从后台获取的表格数据
var qt_pkid;//company_id

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

//节点点击事件
function click_tree(val){
	$('#title_name').html(val.text);
	shuaxin();
	data_info(val.pkid);
}

//刷新
function shuaxin(){
	$("#a1_b1").html("暂无数据");
	$("#a1_b2").html("暂无数据");
	$("#a1_b3").html("暂无数据");
	$("#a1_b4").html("暂无数据");
	$("#a2_b1").html("暂无数据");
	$("#a2_b2").html("暂无数据");
	$("#a2_b3").html("暂无数据");
	$("#a2_b4").html("暂无数据");
	$("#a3_b1").html("暂无数据");
	$("#a3_b2").html("暂无数据");
	$("#a3_b3").html("暂无数据");
	$("#a3_b4").html("暂无数据");
}

//上方表格数据初始化
function data_info(pkid){
	qt_pkid=pkid;
	$.ajax({  		       
	    url: "/assa/getydbqData.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{
	    	pkid: pkid
	    },
	    success: function (data) {
	    	var start_place;
	    	$.each(data, function(i,item) {
	    		if(item.place_type.toString()=="1"){
	    			if(item.start_place==""||item.start_place==undefined){
	    				$("#a1_b1").text("暂无数据");
	    			}else{
	    				$("#a1_b1").text(item.start_place+"个");
	    			}
	    			if(item.speed_proportion==""||item.speed_proportion==undefined){
	    				$("#a1_b2").text("暂无数据");
	    			}else{
	    				$("#a1_b2").text(item.speed_proportion+"%");
	    			}
	    			if(item.project_proportion==""||item.project_proportion==undefined){
	    				$("#a1_b3").text("暂无数据");
	    			}else{
	    				$("#a1_b3").text(item.project_proportion+"%");
	    			}
	    			if(item.capital==""||item.capital==undefined){
	    				$("#a1_b4").text("暂无数据");
	    			}else{
	    				$("#a1_b4").text(item.capital+"万元");
	    			}
	    		}else if(item.place_type=="2"){
	    			if(item.start_place==""||item.start_place==undefined){
	    				$("#a2_b1").text("暂无数据");
	    			}else{
	    				$("#a2_b1").text(item.start_place+"个");
	    			}
	    			if(item.speed_proportion==""||item.speed_proportion==undefined){
	    				$("#a2_b2").text("暂无数据");
	    			}else{
	    				$("#a2_b2").text(item.speed_proportion+"%");
	    			}
	    			if(item.project_proportion==""||item.project_proportion==undefined){
	    				$("#a2_b3").text("暂无数据");
	    			}else{
	    				$("#a2_b3").text(item.project_proportion+"%");
	    			}
	    			if(item.capital==""||item.capital==undefined){
	    				$("#a2_b4").text("暂无数据");
	    			}else{
	    				$("#a2_b4").text(item.capital+"万元");
	    			}
	    		}else{
	    			if(item.start_place==""||item.start_place==undefined){
	    				$("#a3_b1").text("暂无数据");
	    			}else{
	    				$("#a3_b1").text(item.start_place+"个");
	    			}
	    			if(item.speed_proportion==""||item.speed_proportion==undefined){
	    				$("#a3_b2").text("暂无数据");
	    			}else{
	    				$("#a3_b2").text(item.speed_proportion+"%");
	    			}
	    			if(item.project_proportion==""||item.project_proportion==undefined){
	    				$("#a3_b3").text("暂无数据");
	    			}else{
	    				$("#a3_b3").text(item.project_proportion+"%");
	    			}
	    			if(item.capital==""||item.capital==undefined){
	    				$("#a3_b4").text("暂无数据");
	    			}else{
	    				$("#a3_b4").text(item.capital+"万元");
	    			}
	    		}
	    	});
	    	
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}

//加载行政区划树
function show_tree(){
	var treeData;
	$.ajax({  		       
	    url: "/assa/getTreeData.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    success: function (data) {
	    	
	    	if (typeof data != "undefined") {
	    		treeData = data;
	    	}else{
	    		toastr["error"]("error", "服务器异常");
	    	}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
	return treeData;
}