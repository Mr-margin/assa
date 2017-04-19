package com.gistone.servlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;
import com.gistone.util.QRCodeUtil;
import com.gistone.util.Tool;

public class CreateQRCode extends MultiActionController {
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	


	/**
	 * 生成二维码
	 * @param request
	 * @param response
	 * @throws Exception
	 *	@author Liulifeng
	 *  @date 2017年4月18日 下午4:09:42
	 */
	public void getQRCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String type = request.getParameter("type");
		
		String text = "http://110.18.60.194:10303/assa/anuser.html?pkid="; //原路径
		//String text = "http://192.168.1.35:8080/assa/anuser.html?pkid="; //localhost 改成本机的测试
		// 文件保存目录路径  
        String savePath =""; //   需要改成/7/下面的路径 request.getServletContext().getRealPath("/")+ "attached/7/"
        // 文件保存目录URL  
        String saveUrl = "";
        String sql = "";
        
        if(type.equals("all")){//全部二维码
        	//sql="select a.pkid,a.v2,a.v3,a.v4,a.v5,a.v6,a.v8 from da_household a limit 1,10";
        	sql="select a.pkid,a.v2,a.v3,a.v4,a.v5,a.v6,a.v8 from da_household a";
        	
        }else if(type.equals("xin")){//未生成的二维码，继续操作
        	sql="select a.pkid,a.v2,a.v3,a.v4,a.v5,a.v6,a.v8 from da_household a where a.pkid not in("
        			+ "select da_household_id  from DA_PIC_CODE)";
        	
        	
        }else{//未生成的二维码，继续操作
        	sql="select a.pkid,a.v2,a.v3,a.v4,a.v5,a.v6,a.v8 from da_household a where a.pkid not in("
        			+ "select da_household_id  from DA_PIC_CODE) and a.v3='"+type+"'";
        	
        }
        SQLAdapter Adapter_1 = new SQLAdapter(sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Adapter_1);
		if(Patient_st_List.size()>0){
			for(int i = 0;i<Patient_st_List.size();i++){ //循环生成二维码
												//这里需要获取他是什么市下什么村的生成文件夹 如果有 不用生成
				Map Patient_st_map = Patient_st_List.get(i);
						
				String V2 = Patient_st_map.get("v2")==null?"":Patient_st_map.get("v2").toString().trim();
				String V3 = Patient_st_map.get("v3")==null?"":Patient_st_map.get("v3").toString().trim();
				String V4 = Patient_st_map.get("v4")==null?"":Patient_st_map.get("v4").toString().trim();
				String V5 = Patient_st_map.get("v5")==null?"":Patient_st_map.get("v5").toString().trim();
				
				String PKID = Patient_st_map.get("pkid")==null?"":Patient_st_map.get("pkid").toString().trim();
				String V6 = Patient_st_map.get("v6")==null?"":Patient_st_map.get("v6").toString().trim();
				String V8 = Patient_st_map.get("v8")==null?"":Patient_st_map.get("v8").toString().trim();
				
				savePath = "E:/attached/households/"+V2+"/"+V3+"/"+V4+"/"+V5+"/";
				saveUrl	 =request.getContextPath() + "/attached/household/"+V2+"/"+V3+"/"+V4+"/"+V5+"/";
				create_folder(savePath);//创建文件夹
				
				//判断文件是否存在，存在删除
				File file=new File(savePath+PKID+"_"+V6+".jpg");
				if(file.isFile() && file.exists()){
					file.delete();
				}
				QRCodeUtil.encode(text+Patient_st_map.get("pkid"), "c:/11.jpg", savePath, PKID+"_"+V6+".jpg", true);//生成二维码方法
				
				String sql_i ="INSERT INTO da_pic_code(da_household_id,HOUSEHOLD_NAME,HOUSEHOLD_CARD,PIC_PATH) VALUES"+
						"('"+PKID+"','"+V6+"','"+V8+"','"+saveUrl+PKID+"_"+V6+".jpg"+"')";  //saveURL中要加个参数 路径或者名称
				
				SQLAdapter Adapter_2 = new SQLAdapter(sql_i);
				this.getBySqlMapper.insertSelective(Adapter_2);   //插入数据用的
			}
		}
		
		
	}
	
	//新建文件夹
	public void create_folder(String str_path) throws Exception {
		File uploadDir = new File(str_path);  
        if (!uploadDir.isDirectory()) {  
        	if(!uploadDir.exists()){
        		uploadDir.mkdirs();
        	}
        }
	}
	
	

}
