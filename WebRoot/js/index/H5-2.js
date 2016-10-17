var app={};
$(document).ready(function(){
	$(".i-checks").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green",});//复选框样式
	getSaveMaintain();
});
$(function(){
	$(".iCheck-helper").click(function(){
		var type=this;
		$(".iCheck-helper").each(function(i){
			if(this==type){
				app.inx=i;
			}
		});
		if(app.inx==0){
			$("#hei").show();
			$("#bai").hide();
			
		}else if(app.inx==1){
			$("#hei").hide();
			$("#bai").show();
			
		}
	})
	//基本情况
	$("#jbqk").click(function(){
		if($('#jbqk').is(':checked')) {//开
			openRole("基本情况",1);
		}else{//关
			openRole("基本情况",0);
		}
		
	});
	//基本情况
	$("#jbqk1").click(function(){
		if($('#jbqk1').is(':checked')) {//开
			openRole("基本情况",1);
		}else{//关
			openRole("基本情况",0);
		}
		
	});
	//当前收支
	$("#dqsz").click(function(){
		if($('#dqsz').is(':checked')) {//开
			openRole("当前收支",1);
		}else{//关
			openRole("当前收支",0);
		}
		
	});
	//当前收支
	$("#dqsz1").click(function(){
		if($('#dqsz1').is(':checked')) {//开
			openRole("当前收支",1);
		}else{//关
			openRole("当前收支",0);
		}
		
	});
	//帮扶单位和责任人
	$("#bfdwr").click(function(){
		if($('#bfdwr').is(':checked')) {//开
			openRole("帮扶单位和责任人",1);
		}else{//关
			openRole("帮扶单位和责任人",0);
		}
		
	});
	$("#bfdwr1").click(function(){
		if($('#bfdwr1').is(':checked')) {//开
			openRole("帮扶单位和责任人",1);
		}else{//关
			openRole("帮扶单位和责任人",0);
		}
		
	});
	//帮扶措施
	$("#bfcs").click(function(){
		
		if($('#bfcs').is(':checked')) {//开
			openRole("帮扶措施",1);
		}else{//关
			openRole("帮扶措施",0);
		}
		
	});
	$("#bfcs1").click(function(){
		
		if($('#bfcs1').is(':checked')) {//开
			openRole("帮扶措施",1);
		}else{//关
			openRole("帮扶措施",0);
		}
		
	});
	//走访情况
	$("#zfqk").click(function(){
		if($('#zfqk').is(':checked')) {//开
			openRole("走访情况",1);
		}else{//关
			openRole("走访情况",0);
		}
		
	});
	$("#zfqk1").click(function(){
		if($('#zfqk1').is(':checked')) {//开
			openRole("走访情况",1);
		}else{//关
			openRole("走访情况",0);
		}
		
	});
	//帮扶成效
	$("#bfcx").click(function(){
		if($('#bfcx').is(':checked')) {//开
			openRole("帮扶成效",1);
		}else{//关
			openRole("帮扶成效",0);
		}
		
	});
	$("#bfcx1").click(function(){
		if($('#bfcx').is(':checked')) {//开
			openRole("帮扶成效",1);
		}else{//关
			openRole("帮扶成效",0);
		}
		
	});
	//帮扶后收支分析
	$("#bfhsz").click(function(){
		if($('#bfhsz').is(':checked')) {//开
			openRole("帮扶后收支分析",1);
		}else{//关
			openRole("帮扶后收支分析",0);
		}
		
	});
	$("#bfhsz1").click(function(){
		if($('#bfhsz1').is(':checked')) {//开
			openRole("帮扶后收支分析",1);
		}else{//关
			openRole("帮扶后收支分析",0);
		}
		
	});
});
//维护开关
function openRole(name,id){
	$.ajax({
		url:"/assa/getOpenRoleController.do",
		type:"POST",
		async:false,
		dataType:"json",
		data:{name:name,id:id},
		
		success:function(){
			
		},
		erro:function(){
			
		},
	});
}
//维护开关的初始化状态
function getSaveMaintain(){
	$.ajax({
		url:"/assa/getSaveMaintainController.do",
		type:"POST",
		async:false,
		dataType:"json",
		data:{},
		success:function(data){
			$.each(data,function(i,item){
				if(item.modular_name=="基本情况"){ 
					if(item.maintain=="0"){
						$('#jbqk').attr("checked",false);
					}
				}else{
					$('#jbqk').attr("checked",true);
				}
				
				if(item.modular_name=="当前收支"){
					if(item.maintain=="0"){
						$('#dqsz').attr("checked",false);
					}
				}else{
					$('#dqsz').attr("checked",true);
				}
				
				if(item.modular_name=="帮扶单位和责任人"){
					if(item.maintain=="0"){
						$('#bfdwr').attr("checked",false);
					}
				}else{
					$('#bfdwr').attr("checked",true);
				}
				if(item.modular_name=="帮扶措施"){
					if(item.maintain=="0"){
						$('#bfcs').attr("checked",false);
					}
				}else{
					$('#bfcs').attr("checked",true);
				}
				
				if(item.modular_name=="走访情况"){
					if(item.maintain=="0"){
						$('#zfqk').attr("checked",false);
					}
				}else{
					$('#zfqk').attr("checked",true);
				}
				
				if(item.modular_name=="帮扶成效"){
					if(item.maintain=="0"){
						$('#bfcx').attr("checked",false);
					}
				}else{
					$('#bfcx').attr("checked",true);
				}
				
				if(item.modular_name=="帮扶后收支分析"){
					if(item.maintain=="0"){
						$('#bfhsz').attr("checked",false);
					}
				}else{
					$('#bfhsz').attr("checked",true);
				}
				if(item.modular_name=="基本情况"){ 
					if(item.maintain=="0"){
						$('#jbqk1').attr("checked",false);
					}
				}else{
					$('#jbqk1').attr("checked",true);
				}
				
				if(item.modular_name=="当前收支"){
					if(item.maintain=="0"){
						$('#dqsz1').attr("checked",false);
					}
				}else{
					$('#dqsz1').attr("checked",true);
				}
				
				if(item.modular_name=="帮扶单位和责任人"){
					if(item.maintain=="0"){
						$('#bfdwr1').attr("checked",false);
					}
				}else{
					$('#bfdwr1').attr("checked",true);
				}
				if(item.modular_name=="帮扶措施"){
					if(item.maintain=="0"){
						$('#bfcs1').attr("checked",false);
					}
				}else{
					$('#bfcs1').attr("checked",true);
				}
				
				if(item.modular_name=="走访情况"){
					if(item.maintain=="0"){
						$('#zfqk1').attr("checked",false);
					}
				}else{
					$('#zfqk1').attr("checked",true);
				}
				
				if(item.modular_name=="帮扶成效"){
					if(item.maintain=="0"){
						$('#bfcx1').attr("checked",false);
					}
				}else{
					$('#bfcx1').attr("checked",true);
				}
				
				if(item.modular_name=="帮扶后收支分析"){
					if(item.maintain=="0"){
						$('#bfhsz1').attr("checked",false);
					}
				}else{
					$('#bfhsz1').attr("checked",true);
				}
			});
			
		},
		erro:function(){
			
		},
	});
}
