var cun_id;
var cun_code;
$(function() {
	$("#xiang").hide();
	$("#cun").hide();
	saveXzqh();
	//bianjiTable();
	
    $("#xg_xiang").click(function(){
    	var row = getSelectedRow(show_xiang);//必须确认先选中一条白细胞数据
    	if (typeof row != "undefined") {
    		pkid=row.pkid;
    		 
    		updateBfxinxi(row.pkid);
    		
    	}else{
    		toastr["info"]("info", "必须选择一条记录");
    	}
    });
    $("#add_xz_button").click(function(){
    	$("#add_xz_cun").modal();
    })
});
/**
 * 修改行政编码
 */
function bianjiTable(){

	var input = document.createElement("input");
    input.type="text" ;
    //得到当前的单元格
    var currentCell ; 
    function editCell(event){
        if(event==null){
            currentCell=window.event.srcElement;
        }else{
            currentCell=event.target;
        }
        if(currentCell.tagName=="H"){
            //用单元格的值来填充文本框的值
            //input.value=currentCell.innerHTML;
                 //当文本框丢失焦点时调用last
            //input.onblur=last;
            //input.ondblclick=last;
            //currentCell.innerHTML="";
            //把文本框加到当前单元格上.
            //currentCell.appendChild(input);
               //根据liu_binq63 的建议修定下面的bug 非常感谢
            //input.focus();

         }
    }
    function last(){
        //充文本框的值给当前单元格
        currentCell.innerHTML = input.value;
        var pkid=currentCell.attributes.name.nodeValue;
        var bm=input.value;
        $.ajax({
	    url: "/assa/getUpdateBmController.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{pkid:pkid,bm:bm},
	    success: function (data) {
	    	toastr["success"]("success", "修改成功");
	    },
	   
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
    }
    //最后为表格绑定处理方法.
    document.getElementById("show_cun").ondblclick=editCell;
    document.getElementById("show_xiang").ondblclick=editCell;
    document.getElementById("treeview11").ondblclick=editCell;
}

/**
 * 行政区划默认显示
 */
function saveXzqh(){
	$.ajax({
	    url: "/assa/getExecutive.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    success: function (data) {
	    	var xian=[];
	   	 $.each(data,function(idx,item){
	   		 if(idx!=0){
	   			var json = eval('(' + "{text:'<a onclick=show_xiang("+item.xian_pkid+")>"
	   					+item.xian_com_name+"</a><span   style=float:right;><h>"+item.xian_code+"</h></span>',href: '#child1',tags: ['2']}" + ')'); 
	   			xian[--idx]=json;
	   		 }else{
	   			return;
	   		 }
	   	 });
	   	var e = [{
	        text: (data[0].com_name),
	        href: "#parent1",
	        tags: ["4"],
	        nodes:xian
	    } ],
	   
	    t = '[{"text": "父节点 1","nodes": [{"text": "子节点 1","nodes": [{"text": "孙子节点 1"},{"text": "孙子节点 2"}]},{"text": "子节点 2"}]},{"text": "父节点 2"},{"text": "父节点 3"},{"text": "父节点 4"},{"text": "父节点 5"}]';
	    $("#treeview11").treeview({
	        color: "#428bca",
	        data: e,
	        
	    })
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	});
}


/**
 * 显示乡
 * @param id
 */
function show_xiang(id){
	var html;
	$.ajax({
	    url: "/assa/getSaveXiangController.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{id:id},
	    success: function (data) {
	    	html='<ul class="list-group">';
	   	 $.each(data,function(idx,item){
	   		html+='<li class="list-group-item node-treeview11" data-nodeid="">'+
	   			'<a href="javascript:void" onclick="show_cun('+item.pkid+',\''+item.com_code+'\')">'+item.com_name+'</a><span style="float:right;"><h>'+item.com_code+'</h></span></li>';
	   		 
	   	 });
	    	html+='</ul>';
	   	 $("#show_xiang").html(html);
	   	$("#xiang").show();
		$("#cun").hide();
	    },
//	    show_xiang
	   
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	})
}
/**
 * 显示村
 * @param id
 */

function show_cun(id,code){
	cun_id=id;
	cun_code=code;
	var html;var denglu;
	$.ajax({
	    url: "/assa/getSaveCunController.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{id:id},
	    success: function (data) {
	    	html='<ul class="list-group">';
	   	 $.each(data,function(idx,item){
	   		html+='<li class="list-group-item node-treeview11" data-nodeid="">'+
	   			'<a onclick="show_cuns('+item.pkid+')">'+item.com_name+'</a><span style="float:right;"><h name="'+item.pkid+'">'+item.com_code+'</h></span></li>';
	   		 
	   	 });
	    	html+='</ul>';
	   	 $("#show_cun").html(html);
	  	$("#xiang").show();
		$("#cun").show();
	    },
	   
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	})
}
/**
 * 添加村
 */
function addXzqh(){
	var com_name=$("#add_xz_name").val();
	var com_code=$("#add_xz_bm").val();
	$.ajax({
	    url: "/assa/getAddXzqhController.do",
	    type: "POST",
	    async:false,
	    dataType: "json",
	    data:{id:cun_id,com_name:com_name,com_code:com_code,cun_code:cun_code},
	    success: function (data) {
	    	toastr["success"]("success", "新数据添加");
	    	$("#add_xz_name").val("");
	    	$("#add_xz_bm").val("");
	    	show_cun(cun_id);
	    },
	   
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	})

}
