$(document).ready(function() {
	show_jbqk();
});
var pkid;
function show_jbqk(){
	var Request = new Object();
	Request = GetRequest();//截取URL的方法

	pkid=Request['pkid']; 
	$.ajax({  		       
	    url: "/assa/anGetPk.do",
	    type: "POST",
	    async:false,
	    dataType: "text",
	    data:{
	    	pkid: pkid
        },
	    success: function (data) {
	    	var dataJson=JSON.parse(data);
	    	$('#show_hz_v2').text(dataJson.data1[0].v2);
	    	$('#show_hz_v3').text(dataJson.data1[0].v3);
	    	$('#show_hz_v4').text(dataJson.data1[0].v4);
	    	$('#show_hz_v5').text(dataJson.data1[0].v5);
	    	$('#show_hz_v6').text(dataJson.data1[0].v6);//户主姓名

	    	$('#show_hz_sbbz').text(dataJson.data1[0].sys_standard);//识别标准
	    	$('#show_hz_pkgsx').text(dataJson.data1[0].v22);//贫困户属性
	    	$('#show_hz_rk').text(dataJson.data1[0].v9);//家庭人口
	    	$('#show_hz_sfjls').text(dataJson.data1[0].v29);//是否军烈属
	    	$('#show_hz_zpyy').text(dataJson.data1[0].v23);//致贫原因
	    	$('#show_hz_qtzpyy').html(dataJson.data1[0].v33);//其他致贫原因

	    	//户主的个人信息
	    	$('#show_hz_xm').text("户主：     "+dataJson.data1[0].v6);//户主姓名
	    	$('#shiw_hz_sex').text(dataJson.data1[0].v7);//性别
	    	$('#show_hz_mz').text(dataJson.data1[0].v11);//民族
	    	$('#show_hz_whcd').text(dataJson.data1[0].v12);//文化程度
	    	$('#show_hz_sfzx').text(dataJson.data1[0].v13);//在校生
	    	$('#show_hz_jkzk').text(dataJson.data1[0].v14);//健康状况
	    	$('#show_hz_ldl').text(dataJson.data1[0].v15);//劳动力
	    	$('#show_hz_wgqk').text(dataJson.data1[0].v16);//务工状况
	    	$('#show_hz_wgsj').text(dataJson.data1[0].v17);//务工时间
	    	$('#show_hz_cjxnh').text(dataJson.data1[0].v18);//是否参加新农合
	    	$('#show_hz_ylbx').text(dataJson.data1[0].v19);//是否参加养老保险
	    	if(dataJson.data1[0].pic_path==""||dataJson.data1[0].pic_path==null||dataJson.data1[0].pic_path==undefined||dataJson.data1[0].pic_path=="-"){
	    	}else{
	    		$('#hz_pic').html('<div class="val-md-1"><div class="text-center"><img src="'+dataJson.data1[0].pic_path+'" style="margin:0;vertical-align:baseline;width:200px;height:250px;"></div></div>');
	    	}

	    	//家庭成员
	    	html = '';
	    	if(dataJson.data2!=""||dataJson.data2!=undefined){
	    		$.each(dataJson.data2,function(i,item){
	    			html += '<div class="row"><div class="col-sm-12"><div class="ibox float-e-margins"><div class="ibox-title">';
	    			html += '<h5>'+item.cy_v10+':   '+item.cy_v6+'</h5><div class="ibox-tools"></div></div><div class="ibox-content"><div class="row" id="hz_pic">';
	    			if(item.cy_pic_path==""||item.cy_pic_path==null||item.cy_pic_path==undefined||item.cy_pic_path=="-"){

	    			}else{
	    				html += '<div class="val-md-1"><div class="text-center"><img src="'+item.cy_pic_path+'" style="margin:0;vertical-align:baseline;width:200px;height:250px;"></div></div>';
	    			}
	    			html += '</div>';
	    			html += '<div class="row show-grid-hui show-grid-white"><div class="col-xs-6">性别</div><div class="val-xs-6"><code>'+item.cy_v7+'</code></div>';
	    			html += '<div class="col-xs-6">民族</div><div class="val-xs-6"><code>'+item.cy_v11+'</code></div></div>';
	    			html += '<div class="row show-grid-hui show-grid-white"><div class="col-xs-6">文化程度</div><div class="val-xs-6"><code>'+item.cy_v12+'</code></div>';
	    			html += '<div class="col-xs-6">是否在校</div><div class="val-xs-6"><code>'+item.cy_v13+'</code></div></div>';
	    			html += '<div class="row show-grid-hui show-grid-white"><div class="col-xs-6">健康状况</div><div class="val-xs-6"><code>'+item.cy_v14+'</code></div>';
	    			html += '<div class="col-xs-6">劳动力</div><div class="val-xs-6"><code>'+item.cy_v15+'</code></div>';
	    			html += '<div class="col-xs-6">务工情况</div><div class="val-xs-6"><code>'+item.cy_v16+'</code></div>';
	    			html += '<div class="col-xs-6">务工时间</div><div class="val-xs-6"><code>'+item.cy_v17+'</code></div></div>';
	    			html += '<div class="row show-grid-hui show-grid-white"><div class="col-xs-6">新农合</div><div class="val-xs-6"><code>'+item.cy_v18+'</code></div>';
	    			html += '<div class="col-xs-6">养老保险</div><div class="val-xs-6"><code>'+item.cy_v19+'</code></div>';
	    			html += '</div></div></div></div>';
	    		});
	    	}else{
	    		html += '<div class="row"><div class="col-sm-12"><div class="ibox float-e-margins"><div class="ibox-title">';
	    		html += '<h5>家庭成员：无</h5><div class="ibox-tools"></div></div><div class="ibox-content"><div class="row" id="hz_pic">';
	    		html += '</div>';
	    		html += '</div></div></div></div>';
	    	}
	    	$("#jiatingchengyuan").html(html);

	    	//走访情况
	    	var html2_1 = '';
	    	var sjz_pie={};
	    	if (dataJson.data3.length>0) {
	    		$.each(dataJson.data3,function(i,item){
	    			html2_1 += '<div class="col-sm-12"><div> 走访干部：<strong>'+item.v2+'</strong></div><div><p>走访时间：<code>'+item.v1+'</code></p></div>';
	    			if (item.pie!="") {
	    				sjz_pie=item.pie.split(",");
	    				for(var j=0;j<sjz_pie.length;j++){
	    					html2_1+='<img src=\''+sjz_pie[j]+'\' style=\"margin:0;vertical-align:baseline;width:280px;height:280px;\">&nbsp;&nbsp;&nbsp;';
	    				}
	    			}
	    			html2_1 += '<div>走访情况：<p><code>'+item.v3+'</code></p></div></div><div class="col-sm-12">&nbsp;</div>';
	    		});
	    	}else{
	    		html2_1 += '<div class="col-sm-12"><div> 走访干部：<strong>暂无</strong></div><div><p>走访时间：<code></code></p></div>';
	    		html2_1 += '<div>走访情况：<p><code></code></p></div></div><div class="col-sm-12">&nbsp;</div>';
	    	}
	    	$("#zoufangqingkuang").html(html2_1);
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});

}
function GetRequest() { //截取URL的方法
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i ++) {
			theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]); 
		}
	}
	return theRequest;
}