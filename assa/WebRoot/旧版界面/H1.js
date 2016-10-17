$(function () {
	$("#show-content").height(document.body.scrollHeight-170);//当中间部分内容过少时，人为增加高度，设置为一屏
	$("#gundongzhou").slimScroll({height:"500px"});//时间轴滚动条的高度
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式
	
	var title_type = 1;//默认为展开状态
	//点击标题缩放查询框
    $('#chauxnshiousuo_title').click(function () {
    	$('#chauxnshiousuo').click();
    });
    //在现有收缩图标上追加处理事件
    $("#chauxnshiousuo").bind("click",function(){
    	if(title_type==1){//展开状态
    		$('#title_tle').html("收起");
    		title_type = 0;
    	}else if(title_type==0){//收起状态
    		$('#title_tle').html("展开");
    		title_type = 1;
    	}
	});
    
    
    
    
});
