$(function () {
	//$("#show-content").height(document.body.scrollHeight-170);//当中间部分内容过少时，人为增加高度，设置为一屏
	$("#show-content").css("min-height",document.body.scrollHeight-90);
	
	$('#in').click(function () {//返回首页
		window.location.href="index.html?"+ver;
    });
	
	$('#out').click(function () {//退出
		window.location.href="/assa/H/1.html?"+ver;
    });
	
	$('#H1').click(function () {//录入贫困户信息
		window.location.href="H1-2.html?"+ver;
    });
	
	$('#H2').click(function () {//浏览贫困户信息
		window.location.href="H2.html?"+ver;
    });
	$('#H2_1').click(function () {//浏览贫困户信息
		window.location.href="H2_1.html?"+ver;
    });
	$('#H3-1').click(function () {//综合查询
		window.location.href="H3-1.html?"+ver;
    });
	
	$('#H3-2').click(function () {//统计汇总
		window.location.href="H3-2.html?"+ver;
    });
	
	$('#H4').click(function () {//识别与退出
		window.location.href="H4.html?"+ver;
    });
	
	$('#H5-1').click(function () {//帮扶干部维护
		window.location.href="H5-1.html?"+ver;
    });
	$('#H5-3').click(function () {//帮扶单位维护
		window.location.href="H5-3.html?"+ver;
	});
	$('#H6-1').click(function () {//嘎查村管理
		//window.location.href="H6-1.html?"+ver;
		window.location.href="H5-1-1.html?"+ver;
    });
//	$('#H7').click(function () {//用户管理
//		window.location.href="H7.html?"+ver;
//    });
	$('#H5-2').click(function () {//维护开关
		window.location.href="H5-2.html?"+ver;
    });
	
	$('#H7-1').click(function () {//安置点维护
		window.location.href="H7-1.html?"+ver;
    });
	
	$('#H7-2').click(function () {//工程进度统计
		window.location.href="H7-2.html?"+ver;
    });
	$('#H8').click(function () {//工程进度统计
		window.location.href="H8.html?"+ver;
	});
	$('#H9').click(function () {//数据统计
		window.location.href="H9.html?"+ver;
	});
	$('#H10').click(function () {//数据统计
		window.location.href="H10.html?"+ver;
	});
});

//将form中的值转换为键值对
//如：{Name:'摘取天上星',position:'IT技术'}
//ps:注意将同名的放在一个数组里
function getFormJson(form) {
	var o = {};
	var a = $(form).serializeArray();
	$.each(a, function() {
	    if (o[this.name] !== undefined) {
	        if (!o[this.name].push) {
	            o[this.name] = [o[this.name]];
	        }
	        o[this.name].push(this.value || '');
	    } else {
	        o[this.name] = this.value || '';
	    }
	});
	return o;
}
