var minzu = ['请选择','汉族','满族','回族','蒙古族','藏族','维吾尔族','苗族','彝族','壮族','布依族','朝鲜族','侗族','瑶族','白族','土家族','哈尼族','哈萨克族','傣族','黎族','僳僳族','佤族','畲族','高山族','拉祜族','水族','东乡族','纳西族','景颇族','柯尔克孜族','土族','达斡尔族','仫佬族','羌族','布朗族','撒拉族','毛南族','仡佬族','锡伯族','阿昌族','普米族','塔吉克族','怒族','乌孜别克族','俄罗斯族','鄂温克族','德昂族','保安族','裕固族','京族','塔塔尔族','独龙族','鄂伦春族','赫哲族','门巴族','珞巴族','基诺族','其他'];
var guanxi = ['请选择','配偶','之子','之女','之儿媳','之女婿','之孙子','之孙女','之外孙子','之外孙女','之父','之母','之岳父','之岳母','之公公','之婆婆','之祖父','之祖母','之外祖父','之外祖母','其他'];
var zaixiaosheng = ['请选择','非在校生','学前教育','小学','七年级','八年级','九年级','高中一年级','高中二年级','高中三年级','中职一年级','中职二年级','中职三年级','高职一年级','高职二年级','高职三年级','大专及以上'];
var pinkunhushuxing = ['请选择','一般贫困户','低保户','五保户','一般农户','低保贫困户','五保贫困户'];
var wenhuachengdu = ['请选择','文盲或半文盲','学龄前儿童','小学','初中','高中','大专及以上'];
var zhipinyuanyin=['因病','因残','因学','因灾','缺土地','缺住房','缺水','缺电','缺技术','缺劳动力','缺资金','交通条件落后','自身发展动力不足','其他'];
function getinfo_ready(str){
	var param;
	for(var i=0;i<minzu.length;i++){
		param+='<option>'+minzu[i]+'</option>';
	}
	str.html(param);
}

function getinfo_ready_guanxi(str){
	var param;
	for(var i=0;i<guanxi.length;i++){
		param+='<option>'+guanxi[i]+'</option>';
	}
	str.html(param);

}
/**
 * 获取所有的旗县
 */
function getinfo_xiqian(str){
	var qixian;
	
	var type = jsondata.company_map.com_type;
	var val = jsondata.company;
	$.ajax({
	    url: "/assa/getSaveQixianController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
        },
	    success: function (data) {
	    	qixian='<option>请选择</option>';
	    	$.each(data,function(i,item){
	    		if(type=="单位"){
	    			if(val.com_level == "1"){
	    				qixian+='<option  value="'+item.com_name+'">'+item.com_name+'</option>';
	    			}else if(val.com_level == "2"){
	    				if(val.xian==item.com_name){
	    					qixian='<option  value="'+item.com_name+'">'+item.com_name+'</option>';
	    				}
	    			}else if(val.com_level == "3"){
	    				if(val.xian==item.com_name){
	    					qixian='<option  value="'+item.com_name+'">'+item.com_name+'</option>';
	    				}
	    			}else if(val.com_level == "4"){
	    				if(val.xian==item.com_name){
	    					qixian='<option  value="'+item.com_name+'">'+item.com_name+'</option>';
	    				}
	    			}
	    			
	    		}else if(type=="管理员"){
	    			qixian+='<option  value="'+item.com_name+'">'+item.com_name+'</option>';
	    		}
	    		
	    	});
	    	str.html(qixian);
	    },
	    error: function () { 
	    }  
	
	});
}


function showtop(id){
	
}
/**
 * 获取乡
 */
function getinfo_xiang(chongmingle,str){
	var xiang;
	var type = jsondata.company_map.com_type;
	var val = jsondata.company;
	
	$.ajax({
	    url: "/assa/getXiangController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	str:chongmingle,
        },
	    success: function (data) {
	    	xiang='<option>请选择</option>';
	    	$.each(data,function(i,item){
	    		if(type=="单位"){
	    			if(val.com_level == "1"){
	    				// 2017 - 4 -18  value赋值 
	    				xiang+='<option value='+item.com_name+'>'+item.com_name+'</option>';
	    			}else if(val.com_level == "2"){
	    				xiang+='<option value='+item.com_name+'>'+item.com_name+'</option>';
	    			}else if(val.com_level == "3"){
	    				if(val.xiang==item.com_name){
	    					xiang='<option value='+item.com_name+'>'+item.com_name+'</option>';
	    				}
	    			}else if(val.com_level == "4"){
	    				if(val.xiang==item.com_name){
	    					xiang='<option value='+item.com_name+'>'+item.com_name+'</option>';
	    				}
	    			}
	    		}else if(type=="管理员"){
	    			xiang+='<option value='+item.com_name+'>'+item.com_name+'</option>';
	    		}
	    	});
	    	str.html(xiang);
	    },
	    error: function () { 
	    }  
	
	});
}
/**
 * 获取村
 */
function getinfo_cun(chongmingle,str){
	var cun;
	var type = jsondata.company_map.com_type;
	var val = jsondata.company;
	
	$.ajax({
	    url: "/assa/getXiangController.do",
	    type: "POST",
	    async:false,
	    dataType:"json",
	    data:{
	    	str:chongmingle,
        },
	    success: function (data) {
	    	cun='<option>请选择</option>';
	    	$.each(data,function(i,item){
	    		if(type=="单位"){
	    			if(val.com_level == "1"){
	    				cun+='<option>'+item.com_name+'</option>';
	    			}else if(val.com_level == "2"){
	    				cun+='<option>'+item.com_name+'</option>';
	    			}else if(val.com_level == "3"){
	    				cun+='<option>'+item.com_name+'</option>';
	    			}else if(val.com_level == "4"){
	    				if(val.cun==item.com_name){
	    					cun='<option>'+item.com_name+'</option>';
	    				}
	    			}
	    		}else if(type=="管理员"){
	    			cun+='<option>'+item.com_name+'</option>';
	    		}
	    	});
	    	str.html(cun);
	    },
	    error: function () { 
	    }  
	
	});
}
/**
 * 致贫原因
 * @param str
 */
function getInfo_ready_zhipinyuanyin(str){
	var param='<option>请选择</option>';
	for(var i=0;i<zhipinyuanyin.length;i++){
		param+='<option>'+zhipinyuanyin[i]+'</option>';
	}
	str.html(param);
}

function getinfo_ready_zaixiaosheng(str){
	var param;
	for(var i=0;i<zaixiaosheng.length;i++){
		param+='<option>'+zaixiaosheng[i]+'</option>';
	}
	str.html(param);
}

function getinfo_ready_pinkunhushuxing(str){
	var param;
	for(var i=0;i<pinkunhushuxing.length;i++){
		param+='<option>'+pinkunhushuxing[i]+'</option>';
	}
	str.html(param);
}

function getinfo_ready_wenhuachengdu(str){
	var param;
	for(var i=0;i<wenhuachengdu.length;i++){
		param+='<option>'+wenhuachengdu[i]+'</option>';
	}
	str.html(param);
}