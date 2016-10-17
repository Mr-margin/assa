var str;
var da_household_id;
$(function(){
	var url = document.location.href;
	if(url.indexOf("?")!=-1&&url.indexOf("&")!=-1){
		url = url.substr(url.indexOf("?")+1, url.indexOf("?")+1);//从=号后面的内容
		da_household_id=url.substring("",url.indexOf("&"))
		str=url.substr(url.indexOf("&")+1,url.indexOf("&")+1)
	}else{
		return;
	}
	var html="";
	$.ajax({
	    url: "/assa/getPoorDetailController.do",
	    type: "POST",
	    async:true,
	    dataType:"json",
	    data:{
	    	pkid:da_household_id,
        },
        
	    success: function (data) {
	    	if(data.data4[0].sh_v3=="是"){
	    		$(".active5").show();
	    		$("#dy_ydbq").show();
	    		$("#dy_ydbp_photo").show();
	    	}else{
	    		$(".active5").hide();
	    		$("#dy_ydbq").hide();
	    		$("#dy_ydbp_photo").hide();
	    	}
	    	//户主的个人信息
	    	$('#show_hz_xm1').text(data.data1[0].v6+"（户主）");//户主姓名
	    	$('#shiw_hz_sex1').text(data.data1[0].v7);//性别
	    	$('#show_hz_mz1').text(data.data1[0].v11);//民族
	    	$('#show_hz_rk1').text(data.data1[0].v9);//人口
	    	$('#show_hz_phone1').text(data.data1[0].v25);//电话
	    	$('#show_hz_sfz1').text(data.data1[0].v8);//证件号码
	    	$('#show_hz_address1').text(data.data1[0].basic_address);//家庭住址
	    	$('#show_hz_pkgsx1').text(data.data1[0].v22);//贫困户属性
	    	$('#show_hz_zpyy1').text(data.data1[0].v23);//致贫原因
	    	$('#show_hz_bank1').text(data.data1[0].v26);//开户银行名称
	    	$('#show_hz_banknum1').text(data.data1[0].v27);//银行卡号
	    	$('#show_hz_whcd1').text(data.data1[0].v12);//文化程度
	    	$('#show_hz_zzmm1').text(data.data1[0].v28);
	    	$('#show_hz_sfzx1').text(data.data1[0].v13);//在校生
	    	$('#show_hz_jkzk1').text(data.data1[0].v14);//健康状况
	    	$('#show_hz_ldl1').text(data.data1[0].v15);//劳动力
	    	
	    	$('#show_hz_wgqk1').text(data.data1[0].v16);//务工状况
	    	$('#show_hz_wgsj1').text(data.data1[0].v17);//务工时间
	    	$('#show_hz_cjxnh1').text(data.data1[0].v18);//是否参加新农合
	    	
	    	$('#show_hz_ylbx1').text(data.data1[0].v19);//是否参加养老保险
	    	$('#show_hz_sfjls1').text(data.data1[0].v29);//是否军烈属
	    	$('#show_hz_xyjr1').text(data.data1[0].v32);//是否现役军人
	    	
	    	$('#show_hz_dszn1').text(data.data1[0].v30);//是否独生子女
	    	$('#show_hz_snh1').text(data.data1[0].v31);//是否双女户
	    	$('#show_hz_qtzpyy1').text(data.data1[0].v33);//其他致贫原因
	    	
	    	var k = 0;
	    	var vls = data.data1[0].basic_explain;
	    	$('#show_hz_zfyysm1').text(vls);//致贫原因说明
    		for(var i = 0;i<vls.length;){
    			vls = vls.substring(0,k+60)+"<br>"+vls.substring(k+60);
    			k += 64;
    			i += 60;
    		}
	    	
	    	$('#show_hz_zfyysm').html("<br>"+vls);//致贫原因说明
//	    	$('#show_hz_zfyysm1').text(vls);//致贫原因说明
	    	
	    	if(data.data1[0].pic_path==""||data.data1[0].pic_path==null||data.data1[0].pic_path==undefined||data.data1[0].pic_path=="-"){
	    		$('#hz_pic').html('<a href="img/zw.jpg" title=\"户主头像\"data-gallery=\"\">'+
		    			'<img src="img/zw.jpg" style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\"></a>');
	    		$('#hz_pic1').html('<img src="img/zw.jpg" style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\"  />');
	    	}else{
	    		$('#hz_pic').html('<a href=\''+data.data1[0].pic_path+'\' title=\"户主头像\"data-gallery=\"\">'+
		    			'<img src=\''+data.data1[0].pic_path+'\' style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\"></a>');
	    		$('#hz_pic1').html('<img src="'+data.data1[0].pic_path+'" style=\"margin:0;vertical-align:baseline;width:200px;height:200px;\" />');
		    	
	    	}
	    	$('#show_hz_sbbz1').text(data.data1[0].sys_standard);
	    	//生产条件
	    	if(data.data3==""||data.data3==null||data.data3==undefined){
	    	}else{
		    	$('#show_gdmj1').text(data.data3[0].sc_v1);//耕地面积
		    	$('#show_sjdmj1').text(data.data3[0].sc_v2);//水浇地面积
		    	$('#show_ldmj1').text(data.data3[0].sc_v3);//林地面积
		    	$('#shoow_tghl1').text(data.data3[0].sc_v4);//退耕还林面积
		    	$('#show_cmc1').text(data.data3[0].sc_v5);//草牧场面积
		    	$('#show_scyf1').text(data.data3[0].sc_v6);//生产用房面积
		    	$('#show_qt1').text(data.data3[0].sc_v7);//其他
		    	$('#show_hz_cssl').text("家禽（"+data.data3[0].sc_v8+"）只、牛（"+data.data3[0].sc_v9+"）头、羊（"+data.data3[0].sc_v10+"）只、猪（"+data.data3[0].sc_v11+"）头、其他（"+data.data3[0].sc_v12+"）");//家禽
		    	$('#show_lgmj1').text(data.data3[0].sc_v13);//林果面积
		    	$('#show_smmj1').text(data.data3[0].sc_v14);//水面面积
	    	}
	    	//生活条件
	    	if(data.data4==""||data.data4==null||data.data4==undefined){
	    	}else{
		    	
		    	$('#show_zfmj1').text(data.data4[0].sh_v1);//住房面积
		    	$('#show_sfwf1').text(data.data4[0].sh_v2);//是否危房
		    	$('#show_ydbq1').text(data.data4[0].sh_v3);//是否纳入易地扶贫搬迁
		    	$('#show_ysqk1').text(data.data4[0].sh_v4);//饮水情况
		    	$('#show_rhllx1').text(data.data4[0].sh_v6);//入户路类型
		    	
		    	$('#show_yssfkn1').text(data.data4[0].sh_v8);//饮水是否困难
		    	$('#show_zgljl1').text(data.data4[0].sh_v7);//与主干路距离
		    	$('#show_rllx1').text(data.data4[0].sh_v10);//燃料类型
		    	$('#show_sfcjzyh1').text(data.data4[0].sh_v11);//专业合作社
		    	$('#show_ywwscs1').text(data.data4[0].sh_v12);//有无卫生厕所
		    	$('#show_tdqk1').text(data.data4[0].sh_v5);//有无卫生厕所
		    	$('#show_sfysaq1').text(data.data4[0].sh_v9);//有无卫生厕所
		    	
		    	
		    	
	    	}
	    	//家庭成员
	    	dy_html="";
	    	$.each(data.data2,function(i,item){
	    		//家庭成员
	    			if(item.cy_v6 ==undefined||item.cy_v6 ==""||item.cy_v6 ==null){
	    				dy_html+="";
	    			}else{
	    				dy_html+='<tr>';
	    				
	    				if(item.cy_pic_path==""||item.cy_pic_path==null||item.cy_pic_path==undefined||item.cy_pic_path=="-"){
	    					dy_html+='<td style="border: 1px solid #000;" rowspan="5" colspan="2" ><img src="img/zw.jpg" style="margin:0;vertical-align:baseline;width:150px;height:150px;"></td>';
	    				}else{
	    					dy_html+='<td style="border: 1px solid #000;" rowspan="5" colspan="2"><img src=\''+item.cy_pic_path+'\' style="margin:0;vertical-align:baseline;width:150px;height:150px;"></td>';
	    				}
		    			
		    			dy_html+='<td style="border: 1px solid #000;">姓名</td> <td style="border: 1px solid #000;">'+item.cy_v6+'（'+item.cy_v10+'）</td><td style="border: 1px solid #000;">性别</td><td style="border: 1px solid #000;">'+item.cy_v7+'</td><td >民族</td><td style="border: 1px solid #000;">'+item.cy_v11+'</td></tr>'+
		    					'<tr ><td style="border: 1px solid #000;" >身份证号</td><td>'+item.cy_v8+'</td><td style="border: 1px solid #000;" >文化程度</td><td style="border: 1px solid #000;" id="">'+item.cy_v12+'</td><td style="border: 1px solid #000;">是否在校</td><td style="border: 1px solid #000;">'+item.cy_v13+'</td> </tr>'+
		    					'<tr><td style="border: 1px solid #000;">健康状况</td><td style="border: 1px solid #000;">'+item.cy_v14+'</td><td style="border: 1px solid #000;">劳动力</td><td>'+item.cy_v15+'</td><td style="border: 1px solid #000;">新农合</td><td style="border: 1px solid #000;">'+item.cy_v18+'</td></tr>'+
		    					' <tr><td style="border: 1px solid #000;">养老保险</td> <td style="border: 1px solid #000;">'+item.cy_v19+'</td><td style="border: 1px solid #000;">是否现役军人</td><td>'+item.cy_v32+'</td><td style="border: 1px solid #000;">政治面貌</td><td>'+item.cy_v28+'</td></tr>'+
		    					'<tr><td style="border: 1px solid #000;">务工情况</td><td style="border: 1px solid #000;">'+item.cy_v16+'</td><td style="border: 1px solid #000;">务工时间</td><td style="border: 1px solid #000;">'+item.cy_v17+'</td><td style="border: 1px solid #000;"></td><td style="border: 1px solid #000;"></td></tr>';
	    			}
	    	});
	    	$("#dy_jtcy").html(dy_html);
	    	
	    	//帮扶人
	    	var html1='<table><thead><tr><th colspan="5" style="border: 1px solid #000;">帮扶单位和责任人</th></tr><tr><th style="text-align:center;width:12%;border: 1px solid #000;">姓名</th>'+
		    		'<th style="text-align:center;width:36%;border: 1px solid #000;">单位</th><th style="text-align:center;width:36%;border: 1px solid #000;">职务</th><th style="text-align:center;width:15%;border: 1px solid #000;">电话</th></tr></thead>'+
		    		'<tbody>';
	    	$.each(data.data7,function(i,item){
    				if(item.bfr_col_name==undefined||item.bfr_col_name==""||item.bfr_col_name==null){
    					html1+="";
    				}else{
    					html1+='<tr><td style="text-align:center;border: 1px solid #000;">'+item.bfr_col_name+'</td>'+
    						'<td style="text-align:center;border: 1px solid #000;" colspan="2">'+item.bfr_com_name+'</td>'+
	    					'<td style="text-align:center;border: 1px solid #000;">'+item.bfr_col_post+'</td>'+
	    					'<td style="text-align:center;border: 1px solid #000;">'+item.bfr_telephone+'</td></tr>';
    				}
	    	});
	    	html1+=	'<tr><th style="text-align:center;border: 1px solid #000;">帮扶目标</th>'+
					'<td style="border: 1px solid #000;" colspan="4">'+data.data15[0].bfr_mubiao+'</td></tr>'+
					'<tr><th style="text-align:center;border: 1px solid #000;">帮扶时限</th>'+
					'<td style="border: 1px solid #000;" colspan="4">'+data.data15[0].bfr_shixiao+'</td></tr>'+
					'<tr><th style="text-align:center;border: 1px solid #000;">帮扶计划</th>'+
					'<td style="border: 1px solid #000;" colspan="4">'+data.data15[0].bfr_jihua+'</td></tr></tbody></table>';
			$("#dy_dwzrr").html(html1);
			
			//走访情况
	    	var html2_1='<div><table>'+
	    	'<tbody><tr><th style="text-align:center;width:15%;border: 1px solid #000;">走访时间</th><th style="text-align:center;width:30%;border: 1px solid #000;">帮扶干部</th><th style="width:55%;border: 1px solid #000;" colspan="2">走访情况记录</th>';
			var pic={};
	    	$.each(data.data8,function(i,item){
	    			
    				if(item.zf_v1==undefined||item.zf_v1==""||item.zf_v1==null){
    				}else{
    					if(item.zf_pic=="-"||item.zf_pic==""||item.zf_pic==null){
    						html2_1+='<tr ><td style="text-align:center;border: 1px solid #000;">'+item.zf_v1+'</td>'+
    						'<td style="text-align:center;border: 1px solid #000;">'+item.zf_v2+'</td>'+
    						'<td style="border: 1px solid #000;" colspan="2">'+item.zf_v3+
    						'</td></tr>';
    	    			}else {
        					html2_1+='<tr><td style="text-align:center;border: 1px solid #000;">'+item.zf_v1+'</td>'+
        					'<td style="text-align:center;border: 1px solid #000;">'+item.zf_v2+'</td>'+
        					'<td style="border: 1px solid #000;" colspan="2">'+item.zf_v3+
        					'<br>';
        					pic=item.zf_pic.split(",");
        					for(var j=0;j<pic.length;j++){
        						html2_1+='<a href=\''+pic[j]+'\' title=\"走访情况图片\" data-gallery=\"\">'+
        						'<img src=\''+pic[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
        					}
        					
        					html2_1+='</td></tr>';
    	    				
    	    			}
    				}
    				
			});
	    	html2_1+='</tbody></table></div>';
			$("#dy_zfjl").html(html2_1);
			
			//帮扶措施
			
		
	    	var html3='<table><thead><tr></tr><th colspan="15" style="border: 1px solid #000;">帮扶措施</th><tr><th style="text-align:center;border: 1px solid #000;" colspan="1" rowspan="2">项目类别</th><th style="text-align:center;border: 1px solid #000;" colspan="1" rowspan="2">扶持措施</th>'+
			'<th style="text-align:center;border: 1px solid #000;" colspan="1" rowspan="2">是否符合扶持条件</th><th style="text-align:center;border: 1px solid #000;" colspan="3">2016年</th><th style="text-align:center;border: 1px solid #000;" colspan="3">2017年</th><th style="text-align:center;border: 1px solid #000;" colspan="3">2018年</th>'+
			'<th style="text-align:center;border: 1px solid #000;" colspan="3">2019年</th></tr><tr><th style="text-align:center;border: 1px solid #000;" >项目需求量</th><th style="text-align:center;border: 1px solid #000;">受益资金/政策</th><th style="text-align:center;border: 1px solid #000;">落实时间</th><th style="text-align:center;border: 1px solid #000;" >项目需求量</th>'+
			'<th style="text-align:center;border: 1px solid #000;">受益资金/政策</th><th style="text-align:center;border: 1px solid #000;">落实时间</th><th style="text-align:center;border: 1px solid #000;" >项目需求量</th><th style="text-align:center;border: 1px solid #000;">受益资金/政策</th><th style="text-align:center;border: 1px solid #000;">落实时间</th>'+
			'<th style="text-align:center;border: 1px solid #000;" >项目需求量</th><th style="text-align:center;border: 1px solid #000;">受益资金/政策</th><th style="text-align:center;border: 1px solid #000;">落实时间</th></tr></thead><tbody>';
			
			var bfcs_pic={};
	    	$.each(data.data9,function(i,item){
	    		html3+='<tr><td style="border: 1px solid #000;">'+item.v1+'</td><td style="border: 1px solid #000;">'+item.v2+'</td><td style="border: 1px solid #000;">'+item.v3+'</td><td style="border: 1px solid #000;">'+item.v4_2016+'</td><td style="border: 1px solid #000;">'+item.v5_2016+'</td>'+
	    				'<td style="border: 1px solid #000;">'+item.v6_2016+'</td><td style="border: 1px solid #000;">'+item.v4_2017+'</td><td style="border: 1px solid #000;">'+item.v5_2017+'</td><td style="border: 1px solid #000;">'+item.v6_2017+'</td><td style="border: 1px solid #000;">'+item.v4_2018+'</td>'+
	    				'<td style="border: 1px solid #000;">'+item.v5_2018+'</td><td style="border: 1px solid #000;">'+item.v6_2018+'</td><td style="border: 1px solid #000;">'+item.v4_2019+'</td><td style="border: 1px solid #000;">'+item.v5_2019+'</td><td style="border: 1px solid #000;">'+item.v6_2019+'</td></tr>';
//    				
			});
	    	
			html3+='</tbody></table>';
			$("#dy_bfcs11").html(html3);
			
			//帮扶成效
	    	var html4_1='<table  ><thead><tr><th style="text-align:center;width:20%;border: 1px solid #000;">时间</th>'+
	    	'<th style="text-align:center;border: 1px solid #000;" colspan="2">成效内容</th><th style="text-align:center;width:20%;border: 1px solid #000;">贫困户签字</th></tr></thead><tbody>';
			
			var bfcx_pic={};
	    	$.each(data.data10,function(i,item){
    				if(item.bfcx_v1==undefined||item.bfcx_v1==""||item.bfcx_v1==null){
    				}else{
    					if(item.bfcx_pic=="-"||item.bfcx_pic==""||item.bfcx_pic==null){
    						html4_1+='<tr><td style="text-align:center;border: 1px solid #000;">'+item.bfcx_v1+'</td>'+
    						'<td style="border: 1px solid #000;" colspan="2">'+item.bfcx_v2+'</td>'+
    						'<td style="text-align:center;border: 1px solid #000;">'+item.bfcx_v3+'</td> </tr>';
    					}else{
    						html4_1+='<tr><td style="text-align:center;border: 1px solid #000;">'+item.bfcx_v1+'</td>'+
    						'<td style="border: 1px solid #000;" colspan="2">'+
    						''+item.bfcx_v2+'<br>'+
    						'';
    						
    						bfcx_pic=item.bfcx_pic.split(",");
    	    				for(var j=0;j<bfcx_pic.length;j++){
    	    					html4_1+='<a href=\''+bfcx_pic[j]+'\' title=\"帮扶措施图片\" data-gallery=\"\">'+
    	    					'<img src=\''+bfcx_pic[j]+'\' style=\"margin:0;vertical-align:baseline;width:65px;height:50px;\"></a>&nbsp;&nbsp;&nbsp;';
    	    				}
        					html4_1+='<td style="text-align:center;border: 1px solid #000;">'+item.bfcx_v3+'</td>'+
        					'</tr>';
    					}
    				}
			});
	    	html4_1+='</tbody></table>';
			$("#dy_bfcx").html(html4_1);
	    	
	    	//当前收入情况
			if(data.data5==""||data.data5==null||data.data5==undefined){
				var a=0;
				$("#show_dqzongsr").text('0');//年总收入
			}else{
		    	$("#show_ny_1").text(data.data5[0].dqsr_v1);//农业明细
		    	$("#show_ny_je_1").text(data.data5[0].dqsr_v2);//农业明细金额
		    	$("#show_cmy_1").text(data.data5[0].dqsr_v3);//畜牧业明细
		    	$("#show_cmy_je_1").text(data.data5[0].dqsr_v4);//畜牧业金额
		    	$("#show_ly_1").text(data.data5[0].dqsr_v5);//林业明细
		    	$("#show_ly_je_1").text(data.data5[0].dqsr_v6);//林业金额
		    	$("#show_qtsc_1").text(data.data5[0].dqsr_v7);//生产其他明细
		    	$("#show_qtsc_je_1").text(data.data5[0].dqsr_v8);//其他金额
		    	$("#show_scxj_1").text(data.data5[0].dqsr_v9);//生产小计明细
		    	$("#show_scxj_je_1").text(data.data5[0].dqsr_v10);//生产小计明细金额
		    	$("#show_zcst_1").text(data.data5[0].dqsr_v11);//生态补贴明细
		    	$("#show_zcst_je_1").text(data.data5[0].dqsr_v12);//生态补贴明细金额
		    	$("#show_yl_1").text(data.data5[0].dqsr_v13);//养老金明细
		    	$("#show_yl_je_1").text(data.data5[0].dqsr_v14);//养老金金额
		    	$("#show_db_1").text(data.data5[0].dqsr_v15);//低保补贴明细
		    	$("#show_db_je_1").text(data.data5[0].dqsr_v16);//低保补贴明细金额
		    	$("#show_rm_1").text(data.data5[0].dqsr_v17);//燃煤补贴明细
		    	$("#show_rm_je_1").text(data.data5[0].dqsr_v18);//燃煤补贴金额
		    	$("#show_zcqt_1").text(data.data5[0].dqsr_v19);//政策其他明细
		    	$("#show_zcqt_je_1").text(data.data5[0].dqsr_v20);//政策其他明细金额
		    	$("#show_zcxj_1").text(data.data5[0].dqsr_v21);//政策小计明细
		    	$("#show_zcxj_je_1").text(data.data5[0].dqsr_v22);//政策小计明细金额
		    	$("#show_td_1").text(data.data5[0].dqsr_v23);//土地流转明细
		    	$("#show_td_je_1").text(data.data5[0].dqsr_v24);//土地流转明细金额
		    	$("#show_tdqt_1").text(data.data5[0].dqsr_v25);//土地其他明细
		    	$("#show_tdqt_je_1").text(data.data5[0].dqsr_v26);//土地其他明细金额
		    	
		    	$("#show_gzxm1_1").text(data.data5[0].dqsr_v35);//工资项目1
		    	$("#show_gzmx1_1").text(data.data5[0].dqsr_v27);//工资明细1
		    	$("#show_gzmx1_je_1").text(data.data5[0].dqsr_v28);//工资1金额
		    	
		    	$("#show_gzxm2_1").text(data.data5[0].dqsr_v36);//工资项目2
		    	$("#show_gzmx2_1").text(data.data5[0].dqsr_v29);//工资明细2
		    	$("#show_gzmx2_je_1").text(data.data5[0].dqsr_v30);//工资2金额
		    	
		    	$("#show_qtxm1_1").text(data.data5[0].dqsr_v37);//其他项目1
		    	$("#show_gzqt1_1").text(data.data5[0].dqsr_v31);//其他明细1
		    	$("#show_gzqt1_je_1").text(data.data5[0].dqsr_v32);//明细1金额
		    	
		    	$("#show_qtxm2_1").text(data.data5[0].dqsr_v38);//其他项目2
		    	$("#show_gzqt2_1").text(data.data5[0].dqsr_v33);//明细2
		    	$("#show_gzqt2_je_1").text(data.data5[0].dqsr_v34);//明细2工资
		    	
		    	$("#show_srzj_1").text(data.data5[0].dqsr_v39);//总计
		    	$("#show_dqzongsr_1").text(data.data5[0].dqsr_v39);//年总收入
		    	
		    	$("#show_wbj_1").text(data.data5[0].dqsr_v40);//五保金明细
		    	$("#show_wbj_je_1").text(data.data5[0].dqsr_v41);//五保金金额
		    	$("#show_jhsy_1").text(data.data5[0].dqsr_v42);//计划生育金明细
		    	$("#show_jhsy_je_1").text(data.data5[0].dqsr_v43);//计划生育金额
		    	var a=data.data5[0].dqsr_v39;
			}
	    	
	    	
	    	//当前支出情况
	    	if(data.data6==""||data.data6==null||data.data6==undefined){
	    		var b=0;
	    		$("#show_dqnzzc").text('0');
	    	}else{
		    	$("#show_dqnzzc_1").text(data.data6[0].dqzc_v31);//年总支出
		    	$("#show_nzmx_1").text(data.data6[0].dqzc_v1);//农资明细
		    	$("#show_nzmx_je_1").text(data.data6[0].dqzc_v2);//农资明细金额
		    	$("#show_gdzc_1").text(data.data6[0].dqzc_v3);//固定资产折旧明细
		    	$("#show_gdzc_je_1").text(data.data6[0].dqzc_v4);//固定资产折旧明细金额
		    	$("#show_sdrl_1").text(data.data6[0].dqzc_v5);//水电明细
		    	$("#show_sdrl_je_1").text(data.data6[0].dqzc_v6);//水电明细金额
		    	$("#show_cbtd_1").text(data.data6[0].dqzc_v7);//承包土地明细
		    	$("#show_cbtd_je_1").text(data.data6[0].dqzc_v8);//承包土地明细金额
		    	$("#show_scl_1").text(data.data6[0].dqzc_v9);//饲草料明细
		    	$("#show_scl_je_1").text(data.data6[0].dqzc_v10);//饲草料明细金额
		    	$("#show_fyfz_1").text(data.data6[0].dqzc_v11);//防疫明细
		    	$("#show_fyfz_je_1").text(data.data6[0].dqzc_v12);//防疫明细金额
		    	$("#show_zzc_1").text(data.data6[0].dqzc_v13);//种畜明细
		    	$("#show_zzc_je_1").text(data.data6[0].dqzc_v14);//种畜明细金额
		    	$("#show_xstx_1").text(data.data6[0].dqzc_v15);//销售通讯明细
		    	$("#show_xstx_je_1").text(data.data6[0].dqzc_v16);//销售通讯明细金额
		    	$("#show_dk_1").text(data.data6[0].dqzc_v17);//借贷明细
		    	$("#show_dk_je_1").text(data.data6[0].dqzc_v18);//借贷明细金额
		    	
		    	$("#show_zczcxm1_1").text(data.data6[0].dqzc_v23);//政策项目1
		    	$("#show_zcmx_1").text(data.data6[0].dqzc_v19);//政策明细1
		    	$("#show_zcmx_je_1").text(data.data6[0].dqzc_v20);//政策明细1金额
		    	
		    	$("#show_zczcxm2_1").text(data.data6[0].dqzc_v24);//政策项目2
		    	$("#show_zcmx2_1").text(data.data6[0].dqzc_v21);//政策明细2
		    	$("#show_zcmx2_je_1").text(data.data6[0].dqzc_v22);//政策明细2细金额
		    	
		    	
		    	$("#show_zcqtxm1_1").text(data.data6[0].dqzc_v25);//其他项目1
		    	$("#show_zcqtxmmx1_1").text(data.data6[0].dqzc_v26);//其他明细1
		    	$("#show_zcqtxm1_je_1").text(data.data6[0].dqzc_v27);//其他金额1
		    	
		    	$("#show_zcqtxm2_1").text(data.data6[0].dqzc_v28);//其他项目2
		    	$("#show_zcqtxmmx2_1").text(data.data6[0].dqzc_v29);//其他明细2
		    	$("#show_zcqtxm2_je_1").text(data.data6[0].dqzc_v30);//其他金额2
		    	
		    	$("#show_zongzc_1").text(data.data6[0].dqzc_v31);//年总支出
		    	var b=data.data6[0].dqzc_v31;
	    	}
	    	
	    	//收支分析
	    	if(a=="-"||b=="-"){
		    	$("#show_dqniancsr_1").text("-");//年纯收入
		    	$("#show_dqncsr_1").text("-");//年总收入
		    	$("#show_dqjtrs_1").text(data.data1[0].v9);//人数
		    	$("#show_dqnrj_1").text("-");//年人均收入
	    	}else{
//	    		$("#show_dqniancsr").text(a-b);//年纯收入
	    		if((parseFloat(a)-parseFloat(b)).toFixed(2)=="NaN"){
			    	$("#show_dqniancsr_1").text("0");//年纯收入
			    	$("#show_dqncsr_1").text("0");//年总收入
			    	$("#show_dqnrj_1").text((("0")/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}else{
	    			$("#show_dqniancsr_1").text((parseFloat(a)-parseFloat(b)).toFixed(2));//年纯收入
			    	$("#show_dqncsr_1").text((parseFloat(a)-parseFloat(b)).toFixed(2));//年总收入
			    	$("#show_dqnrj_1").text(((parseFloat(a)-parseFloat(b))/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}
		    	$("#show_dqjtrs_1").text(data.data1[0].v9);//人数
		    	
	    	}
	    	
	    	//帮扶后收入
	    	if(data.data11==""||data.data11==null||data.data11==undefined){
	    		var aa=0;
	    		$("#show_hdqzongsr").text('0');
	    	}else{
		    	$("#show_hny_1").text(data.data11[0].dqsrh_v1);//农业明细
		    	$("#show_hny_je_1").text(data.data11[0].dqsrh_v2);//农业明细金额
		    	$("#show_hcmy_1").text(data.data11[0].dqsrh_v3);//畜牧业明细
		    	$("#show_hcmy_je_1").text(data.data11[0].dqsrh_v4);//畜牧业金额
		    	$("#show_hly_1").text(data.data11[0].dqsrh_v5);//林业明细
		    	$("#show_hly_je_1").text(data.data11[0].dqsrh_v6);//林业金额
		    	$("#show_hqtsc_1").text(data.data11[0].dqsrh_v7);//生产其他明细
		    	$("#show_hqtsc_je_1").text(data.data11[0].dqsrh_v8);//其他金额
		    	$("#show_hscxj_1").text(data.data11[0].dqsrh_v9);//生产小计明细
		    	$("#show_hscxj_je_1").text(data.data11[0].dqsrh_v10);//生产小计明细金额
		    	$("#show_hzcst_1").text(data.data11[0].dqsrh_v11);//生态补贴明细
		    	$("#show_hzcst_je_1").text(data.data11[0].dqsrh_v12);//生态补贴明细金额
		    	$("#show_hyl_1").text(data.data11[0].dqsrh_v13);//养老金明细
		    	$("#show_hyl_je_1").text(data.data11[0].dqsrh_v14);//养老金金额
		    	$("#show_hdb_1").text(data.data11[0].dqsrh_v15);//低保补贴明细
		    	$("#show_hdb_je_1").text(data.data11[0].dqsrh_v16);//低保补贴明细金额
		    	$("#show_hrm_1").text(data.data11[0].dqsrh_v17);//燃煤补贴明细
		    	$("#show_hrm_je_1").text(data.data11[0].dqsrh_v18);//燃煤补贴金额
		    	$("#show_hzcqt_1").text(data.data11[0].dqsrh_v19);//政策其他明细
		    	$("#show_hzcqt_je_1").text(data.data11[0].dqsrh_v20);//政策其他明细金额
		    	$("#show_hzcxj_1").text(data.data11[0].dqsrh_v21);//政策小计明细
		    	$("#show_hzcxj_je_1").text(data.data11[0].dqsrh_v22);//政策小计明细金额
		    	$("#show_htd_1").text(data.data11[0].dqsrh_v23);//土地流转明细
		    	$("#show_htd_je_1").text(data.data11[0].dqsrh_v24);//土地流转明细金额
		    	$("#show_htdqt_1").text(data.data11[0].dqsrh_v25);//土地其他明细
		    	$("#show_htdqt_je_1").text(data.data11[0].dqsrh_v26);//土地其他明细金额
		    	
		    	$("#show_hgzxm1_1").text(data.data11[0].dqsrh_v35);//工资项目1
		    	$("#show_hgzmx1_1").text(data.data11[0].dqsrh_v27);//工资明细1
		    	$("#show_hgzmx1_je_1").text(data.data11[0].dqsrh_v28);//工资1金额
		    	
		    	$("#show_hgzxm2_1").text(data.data11[0].dqsrh_v36);//工资项目2
		    	$("#show_hgzmx2_1").text(data.data11[0].dqsrh_v29);//工资明细2
		    	$("#show_hgzmx2_je_1").text(data.data11[0].dqsrh_v30);//工资2金额
		    	
		    	$("#show_hqtxm1_1").text(data.data11[0].dqsrh_v37);//其他项目1
		    	$("#show_hgzqt1_1").text(data.data11[0].dqsrh_v31);//其他明细1
		    	$("#show_hgzqt1_je_1").text(data.data11[0].dqsrh_v32);//明细1金额
		    	
		    	$("#show_hqtxm2_1").text(data.data11[0].dqsrh_v38);//其他项目2
		    	$("#show_hgzqt2_1").text(data.data11[0].dqsrh_v33);//明细2
		    	$("#show_hgzqt2_je_1").text(data.data11[0].dqsrh_v34);//明细2工资
		    	
		    	$("#show_hsrzj_1").text(data.data11[0].dqsrh_v39);//总计
		    	$("#show_hdqzongsr_1").text(data.data11[0].dqsrh_v39);//年总收入
		    	var aa=data.data11[0].dqsrh_v39;
	    	}
	    	
	    	//帮扶后支出
	    	if(data.data12==""||data.data12==null||data.data12==undefined){
	    		var bb=0;
	    		$("#show_hdqnzzc").text('0');//年总支出
	    	}else{
		    	$("#show_hnzmx_1").text(data.data12[0].dqzch_v1);//农资明细
		    	$("#show_hnzmx_je_1").text(data.data12[0].dqzch_v2);//农资明细金额
		    	$("#show_hgdzc_1").text(data.data12[0].dqzch_v3);//固定资产折旧明细
		    	$("#show_hgdzc_je_1").text(data.data12[0].dqzch_v4);//固定资产折旧明细金额
		    	$("#show_hsdrl_1").text(data.data12[0].dqzch_v5);//水电明细
		    	$("#show_hsdrl_je_1").text(data.data12[0].dqzch_v6);//水电明细金额
		    	$("#show_hcbtd_1").text(data.data12[0].dqzch_v7);//承包土地明细
		    	$("#show_hcbtd_je_1").text(data.data12[0].dqzch_v8);//承包土地明细金额
		    	$("#show_hscl_1").text(data.data12[0].dqzch_v9);//饲草料明细
		    	$("#show_hscl_je_1").text(data.data12[0].dqzch_v10);//饲草料明细金额
		    	$("#show_hfyfz_1").text(data.data12[0].dqzch_v11);//防疫明细
		    	$("#show_hfyfz_je_1").text(data.data12[0].dqzch_v12);//防疫明细金额
		    	$("#show_hzzc_1").text(data.data12[0].dqzch_v13);//种畜明细
		    	$("#show_hzzc_je_1").text(data.data12[0].dqzch_v14);//种畜明细金额
		    	$("#show_hxstx_1").text(data.data12[0].dqzch_v15);//销售通讯明细
		    	$("#show_hxstx_je_1").text(data.data12[0].dqzch_v16);//销售通讯明细金额
		    	$("#show_hdk_1").text(data.data12[0].dqzch_v17);//借贷明细
		    	$("#show_hdk_je_1").text(data.data12[0].dqzch_v18);//借贷明细金额
		    	
		    	$("#show_hzczcxm1_1").text(data.data12[0].dqzch_v23);//政策项目1
		    	$("#show_hzcmx_1").text(data.data12[0].dqzch_v19);//政策明细1
		    	$("#show_hzcmx_je_1").text(data.data12[0].dqzch_v20);//政策明细1金额
		    	
		    	$("#show_hzczcxm2_1").text(data.data12[0].dqzch_v24);//政策项目2
		    	$("#show_hzcmx2_1").text(data.data12[0].dqzch_v21);//政策明细2
		    	$("#show_hzcmx2_je_1").text(data.data12[0].dqzch_v22);//政策明细2细金额
		    	
		    	
		    	$("#show_hzcqtxm1_1").text(data.data12[0].dqzch_v25);//其他项目1
		    	$("#show_hzcqtxmmx1_1").text(data.data12[0].dqzch_v26);//其他明细1
		    	$("#show_hzcqtxm1_je_1").text(data.data12[0].dqzch_v27);//其他金额1
		    	
		    	$("#show_hzcqtxm2_1").text(data.data12[0].dqzch_v28);//其他项目2
		    	$("#show_hzcqtxmmx2_1").text(data.data12[0].dqzch_v29);//其他明细2
		    	$("#show_hzcqtxm2_je_1").text(data.data12[0].dqzch_v30);//其他金额2
		    	
		    	$("#show_hzongzc_1").text(data.data12[0].dqzch_v31);//年总支出
		    	$("#show_hdqnzzc_1").text(data.data12[0].dqzch_v31);//年总支出
		    	var bb=data.data12[0].dqzch_v31;
	    	}
	    	
	    	if(aa=='-'||bb=='-'){
		    	
		    	$("#show_hdqncsr_1").text('-');//年总收入
		    	$("#show_hdqniancsr_1").text('-');//年总收入
		    	$("#show_hdqjtrs_1").text(data.data1[0].v9);//人数
		    	$("#show_hdqnrj_1").text('-');//年人均收入
	    	}else{
	    		if((parseFloat(aa)-parseFloat(bb)).toFixed(2)=="NaN"){
			    	$("#show_hdqncsr_1").text("0");//年总收入
			    	$("#show_hdqniancsr_1").text("0");//年总收入
			    	$("#show_hdqjtrs_1").text(data.data1[0].v9);//人数
			    	$("#show_hdqnrj_1").text((("0")/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}else{
			    	$("#show_hdqncsr_1").text((parseFloat(aa)-parseFloat(bb)).toFixed(2));//年总收入
			    	$("#show_hdqniancsr_1").text((parseFloat(aa)-parseFloat(bb)).toFixed(2));//年总收入
			    	$("#show_hdqjtrs_1").text(data.data1[0].v9);//人数
			    	$("#show_hdqnrj_1").text(((parseFloat(aa)-parseFloat(bb))/(data.data1[0].v9)).toFixed(2));//年人均收入
	    		}
	    		
	    	
	    	}
	    	
	    	
	    	var show_hzczcxm1=$("#show_hzczcxm1").text();//政策项目1
	    	var show_hzczcxm2=$("#show_hzczcxm2").text();//政策项目2
	    	var show_hzcqtxm1=$("#show_hzcqtxm1").text();//其他项目1
	    	var show_hzcqtxm2=$("#show_hzcqtxm2").text();//其他项目2
				//异地搬迁照片
				if(data.data14[0]==undefined||data.data14[0]==null||data.data14[0]==""||data.data14[0]=="-"){
					
				}else{
					$("#yd_bqfs").text(data.data14[0].yd_v1);//搬迁方式
					$("#yd_azfs").text(data.data14[0].move_type);//安置方式
					$("#yd_azd").text(data.data14[0].yd_v2);//安置地
					$("#yd_kn").text(data.data14[0].yd_v3);//搬迁可能存在的困难
					
					$("#yd_lx").text(data.data14[0].dispersed_info);//分散安置类型
					$("#yd_fyd").text(data.data14[0].dispersed_address);//房源地
					$("#yd_fj").text(data.data14[0].dispersed_price);//房价
					$("#yd_xy").text(data.data14[0].dispersed_agreement);//与用工企业签订就业安置协议
					
					$("#yd_bqfs_1").text(data.data14[0].yd_v1);//搬迁方式
					$("#yd_azfs_1").text(data.data14[0].move_type);//安置方式
					$("#yd_azd_1").text(data.data14[0].yd_v2);//安置地
					$("#yd_kn_1").text(data.data14[0].yd_v3);//搬迁可能存在的困难
					
					$("#yd_lx_1").text(data.data14[0].dispersed_info);//分散安置类型
					$("#yd_fyd_1").text(data.data14[0].dispersed_address);//房源地
					$("#yd_fj_1").text(data.data14[0].dispersed_price);//房价
					$("#yd_xy_1").text(data.data14[0].dispersed_agreement);//与用工企业签订就业安置协议
					
					if(data.data14[0].dispersed_info=="进城购房"){
						$("#yd-fyd").show();
						$("#yd-fj").show();
						$("#yd-xy").show();
						
						$("#dy_bqfyd").show();
					}
					var yd_path={};
					
					yd_path=data.data14[0].path.split(",");
					
					var yd_photo="";
					var dy_ydphoto='<tbody><tr><th colspan="6" style="text-align: center;border: 1px solid #000;">易地搬迁图片</th></tr><tr><td style="border: 1px solid #000;" rowspan="6" colspan="6">';
					for(var j=0;j<yd_path.length;j++){
						yd_photo+='<a href=\''+yd_path[j]+'\' title=\"易地搬迁图片\" data-gallery=\"\">'+
						'<img src=\''+yd_path[j]+'\' style=\"margin:0;vertical-align:baseline;width:130px;height:130px;\"></a>';
						dy_ydphoto+='<img src='+yd_path[j]+' style="width:130px;height:130px;">&nbsp;&nbsp;&nbsp;';
					}
					dy_ydphoto+='</td></tr>';
					$("#yd_path").html(yd_photo);
					$("#dy_ydbp_photo").html(dy_ydphoto);
				}
//				window.print();
				if(str=="0"){
					window.print();
				}else if(str=="1"){
//					setTimeout("alertV()",20000); 
					method1(df);
				}
	    },
	    error: function () { 
	    	toastr["error"]("error", "服务器异常");
	    }  
	
	});
	
});
function alertV(){
	method1(df);
}
//导出
var idTmr;
function  getExplorer() {
	var explorer = window.navigator.userAgent ;
	//ie 
	if (explorer.indexOf("MSIE") >= 0) {
		return 'ie';
	}
	//firefox 
	else if (explorer.indexOf("Firefox") >= 0) {
		return 'Firefox';
	}
	//Chrome
	else if(explorer.indexOf("Chrome") >= 0){
		return 'Chrome';
	}
	//Opera
	else if(explorer.indexOf("Opera") >= 0){
		return 'Opera';
	}
	//Safari
	else if(explorer.indexOf("Safari") >= 0){
		return 'Safari';
	}
}

/**
 * 导出
 * @param tableid 
 */
function method1(tableid){//整个表格拷贝到EXCEL中
	if(getExplorer()=='ie')
	{
		var curTbl = document.getElementById(tableid);
		var oXL = new ActiveXObject("Excel.Application");
		
		//创建AX对象excel 
		var oWB = oXL.Workbooks.Add();
		//获取workbook对象 
		var xlsheet = oWB.Worksheets(1);
		//激活当前sheet 
		var sel = document.body.createTextRange();
		sel.moveToElementText(curTbl);
		//把表格中的内容移到TextRange中 
		sel.select();
		//全选TextRange中内容 
		sel.execCommand("Copy");
		//复制TextRange中内容  
		xlsheet.Paste();
		//粘贴到活动的EXCEL中       
		oXL.Visible = true;
		//设置excel可见属性

		try {
			var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
		} catch (e) {
			print("Nested catch caught " + e);
		} finally {
			oWB.SaveAs(fname);

			oWB.Close(savechanges = false);
			//xls.visible = false;
			oXL.Quit();
			oXL = null;
			//结束excel进程，退出完成
			//window.setInterval("Cleanup();",1);
			idTmr = window.setInterval("Cleanup();", 1);

		}
		
	}
	else
	{
		tableToExcel(tableid)
	}
}
function Cleanup() {
    window.clearInterval(idTmr);
    CollectGarbage();
}
var tableToExcel = (function() {
	  var uri = 'data:application/vnd.ms-excel;base64,',
//		  alert(url)
	  template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
		base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
//			alert(template)
		format = function(s, c) {
			return s.replace(/{(\w+)}/g,
			function(m, p) { return c[p]; }) }
		return function(table, name) {
		if (!table.nodeType) table = document.getElementById(table)
		var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
		window.location.href = uri + base64(format(template, ctx))
	  }
	})()
