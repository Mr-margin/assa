package com.gistone.servlet;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;
import com.gistone.util.Tool;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DataProcessing_Controller extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	
	//录入帮扶人与贫困户关系数据
	public ModelAndView sys_personal_household_many(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
//		String sql = "select * from sys_user where pkid>1";
//		SQLAdapter Metadata_st_Adapter = new SQLAdapter(sql);
//		List<Map> Metadata_st_List = this.getBySqlMapper.findRecords(Metadata_st_Adapter);
//		for(int i = 0;i<Metadata_st_List.size();i++){
//			Map Metadata_st_map = Metadata_st_List.get(i);
//			
//			String del_sql = "update sys_user set col_password='"+Tool.md5(Metadata_st_map.get("col_password").toString())+"'  where pkid="+Metadata_st_map.get("pkid").toString();
//			SQLAdapter del_st_Adapter = new SQLAdapter(del_sql);
//			this.getBySqlMapper.deleteSelective(del_st_Adapter);
//			
//		}
		
		
		String sql_3 = "select * from da_household";
		SQLAdapter Metadata_st_Adapter = new SQLAdapter(sql_3);
		List<Map> Metadata_st_List = this.getBySqlMapper.findRecords(Metadata_st_Adapter);
		for(int i = 0;i<Metadata_st_List.size();i++){
			Map st_map = Metadata_st_List.get(i);
			
			String sql = "select count(*) from (select v9 from da_household where pkid="+st_map.get("pkid")+" union ALL select v9 from da_member where da_household_id="+st_map.get("pkid")+") t1";
			SQLAdapter count_st_Adapter = new SQLAdapter(sql);
			int total = this.getBySqlMapper.findrows(count_st_Adapter);
			System.out.println(sql);
			
			String sql_1 = "update da_household set v9='"+total+"' where pkid="+st_map.get("pkid");
			String sql_2 = "update da_member set v9='"+total+"' where da_household_id="+st_map.get("pkid");
			
			SQLAdapter Adapter_1 = new SQLAdapter(sql_1);
			SQLAdapter Adapter_2 = new SQLAdapter(sql_2);
			try{
				this.getBySqlMapper.insertSelective(Adapter_1);
				this.getBySqlMapper.insertSelective(Adapter_2);
			}catch (Exception e){
			}
		}
		
		
		
		
		
		
//		List<Map> listmap = new ArrayList();
//		List<Map> listmap_d = new ArrayList();
//		
//		//获取筛选后的帮扶人信息
//		String sql = "select * from sys_personal where sys_company_id=6";
//		SQLAdapter Metadata_st_Adapter = new SQLAdapter(sql);
//		List<Map> Metadata_st_List = this.getBySqlMapper.findRecords(Metadata_st_Adapter);
//		
//		for(int i = 0;i<Metadata_st_List.size();i++){
//			Map Metadata_st_map = Metadata_st_List.get(i);
//			
//			String sql_1 = "select * from 伊金霍洛旗  where col_name='"+Metadata_st_map.get("col_name")+"' "
//					+ "and com_name='"+Metadata_st_map.get("com_name")+"' "
//					+ "and col_post='"+Metadata_st_map.get("col_post")+"' "
//					+ "and telephone='"+Metadata_st_map.get("telephone")+"'";
//			SQLAdapter st_1 = new SQLAdapter(sql_1);
//			List<Map> st_List_1 = this.getBySqlMapper.findRecords(st_1);
//			
//			for(int j = 0;j<st_List_1.size();j++){
//				Map st_map_1 = st_List_1.get(j);
//				
//				String sql_2 = "select * from da_household where v3='"+st_map_1.get("v1")+"' "
//						+ "and v4='"+st_map_1.get("v2")+"' "
//						+ "and v5='"+st_map_1.get("v3")+"' "
//						+ "and v6='"+st_map_1.get("v4")+"' ";
//						//+ "and v25='"+st_map_1.get("v5")+"'";
//				SQLAdapter st_2 = new SQLAdapter(sql_2);
//				List<Map> st_List_2 = this.getBySqlMapper.findRecords(st_2);
//				System.out.println(sql_2);
//				if(st_List_2.size()==1){//找到贫困户
//					Map st_map_2 = st_List_2.get(0);
//					
//					String add_sql = "insert into sys_personal_household_many(sys_personal_id,da_household_id) values("+Metadata_st_map.get("pkid")+","+st_map_2.get("pkid")+")";
//					SQLAdapter Metadata_table_Adapter = new SQLAdapter(add_sql);
//					this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
//					
//					
//					String del_sql = "delete from 伊金霍洛旗  where pkid="+st_map_1.get("pkid");
//					SQLAdapter del_st_Adapter = new SQLAdapter(del_sql);
//					this.getBySqlMapper.deleteSelective(del_st_Adapter);
//					
//					System.out.println(Metadata_st_map.get("pkid"));
//					
//				}else if(st_List_2.size()>1){//找到多个
//					st_map_1.put("zhenshiID", Metadata_st_map.get("pkid"));
//					listmap_d.add(st_map_1);
//				}else{//没找到
//					st_map_1.put("zhenshiID", Metadata_st_map.get("pkid"));
//					listmap.add(st_map_1);
//				}
//			}
//			
//		}
		System.out.println("完成");
		
		return null;
	}
	
	/**
	 * 
	 * @param path 文件路径
	 * @param listmap 要写入的数据
	 */
	public void getExportAll(String path, List<Map> listmap){
		try{
			
			// 打开文件
            WritableWorkbook book = Workbook.createWorkbook( new File(path));
            //生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet( " 第一页 " , 0 );
            WritableFont font2 =new WritableFont(WritableFont.createFont("微软雅黑"), 9 ,WritableFont.NO_BOLD);
            WritableCellFormat wcf3 = new WritableCellFormat(font2);//正文样式
            wcf3.setAlignment(Alignment.LEFT);  //左对齐
            wcf3.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            wcf3.setBackground(Colour.BLACK);//设置背景颜色
            wcf3.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            wcf3.setWrap(true);//自动换行
            
//            for(int i = 0;i<bit.length;i++){
//            	sheet.addCell(new Label(i, 0, bit[i], wcf3));
//            }
//            
//            for(int i = 0;i<listmap.size();i++){
//            	List l_str = listmap.get(i);
//            	for(int j = 0;j<l_str.size();j++){
//            		String val = (String)l_str.get(j);
//            		sheet.addCell(new Label(j+1, i+1, val, wcf3));
//            	}
//            	
//            }
            
            //写入数据并关闭文件
            book.write();
            book.close();
			System.out.println("ok");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	//录入贫困户数据
	public ModelAndView xls_db_house(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		// 文件保存目录路径  
        String savePath = request.getServletContext().getRealPath("/")+ "orig_data/20160421/市标/乌审旗/1/";
        getAllFile(savePath);
        
        	
        System.out.println("完成");
        
		return null;
	}
	
	/**
	 * 读取文件夹里面的所有文件
	 * @param path String 文件夹路径 如 c:/fqf
	 */
	public void getAllFile(String path) {
		File file = new File(path);//根文件夹
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {//循环根文件夹下所有内容
			
			if (path.endsWith(File.separator)) {//判断path是否以系统指定的分隔符作为结束，兼容多种操作系统
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			
			if (temp.isFile()) {//如果是文件
				//System.out.println(temp.getPath()+"-----"+tempList[i]);
				//文件内容处理
				//get_Company(temp.getPath(), tempList[i], temp.getParentFile());//处理行政区划
				get_household(temp.getPath(), tempList[i], temp.getParentFile());
			}
			if (temp.isDirectory()) {//如果是文件夹，进行递归
				getAllFile(temp.getPath());
			}
		}
	}
	
	
	/**
	 * 先处理行政区划，将所有行政区划入库
	 * @param path 文件路径
	 * @param filename 文件名
	 * @param Parent 父文件名
	 */
	public void get_Company(String path, String filename, File Parent){
		filename = filename.substring(0,filename.indexOf("."));
		String sql = "select * from sys_company where com_level=3 and com_name='"+filename+"'";
		SQLAdapter Metadata_st_Adapter = new SQLAdapter(sql);
		List<Map> Metadata_st_List = this.getBySqlMapper.findRecords(Metadata_st_Adapter);
		if(Metadata_st_List.size()>0){
			Map Metadata_st_map = Metadata_st_List.get(0);
			
			Map<String, String> huzhuname = new HashMap();
			try{
				InputStream is = new FileInputStream(path);
				Workbook wb = Workbook.getWorkbook(is);
				
				Sheet st = wb.getSheet(0);//取第一张工作表
				Cell[] myCells;
				
				//循环行Row
				for (int rowNum = 2; rowNum < st.getRows(); rowNum++) {//从第三行开始循环
					myCells = st.getRow(rowNum);
					if(myCells.length == 0){
				          continue;
			    	}
					
					huzhuname.put(myCells[4].getContents().trim(), myCells[4].getContents().trim());
				}
				
				for(String key : huzhuname.keySet()){
					String add_sql = "insert into sys_company(com_name,com_level,com_f_pkid) values('"+key+"',4,"+Metadata_st_map.get("pkid")+")";
					SQLAdapter Metadata_table_Adapter = new SQLAdapter(add_sql);
					this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
				}
				
				
			}catch(Exception e){
				System.out.println("出现错误---"+Parent.getName()+"----"+filename+"----");
			}
		}else{
			System.out.println("出现未录制的行政区划---"+Parent.getName()+"----"+filename+"----");
		}
	}
	
	
	
	/**
	 * 第一遍先录入户主信息
	 * @param path
	 * @param filename
	 * @param Parent
	 */
	public void get_household(String path, String filename, File Parent){
		
		Map<String, String> cun_name = new HashMap();
		
		//按照文件名读取所有村列表，其实没啥用
		filename = filename.substring(0,filename.indexOf("."));
		String sql = "select y.* from sys_company x, sys_company y where x.pkid=y.com_f_pkid and x.com_level=3 and x.com_name='"+filename+"'";
		SQLAdapter Metadata_st_Adapter = new SQLAdapter(sql);
		List<Map> Metadata_st_List = this.getBySqlMapper.findRecords(Metadata_st_Adapter);
		if(Metadata_st_List.size()>0){
			for(int i = 0;i<Metadata_st_List.size();i++){
				Map Metadata_st_map = Metadata_st_List.get(i);
				cun_name.put(Metadata_st_map.get("com_name").toString(), Metadata_st_map.get("pkid").toString());
			}
		}else{
			System.out.println("出现未录制的行政区划11---"+Parent.getName()+"----"+filename+"----");
		}
		
		//读取文件导入数据库
		Map<String, String> huzhuname = new HashMap();
		try{
			InputStream is = new FileInputStream(path);
			Workbook wb = Workbook.getWorkbook(is);

			Sheet st = wb.getSheet(0);//取第一张工作表
			Cell[] myCells;

			int number = 0;//每户的人数
			int da_household_id = 0;
			int jiatingrenshu = 0;
			//循环行Row
			for (int rowNum = 2; rowNum < st.getRows(); rowNum++) {//从第三行开始循环
				myCells = st.getRow(rowNum);
				if(myCells.length == 0){
					continue;
				}
				
				String values = "";
				for(int cellNum = 0; cellNum < 27; cellNum++){
					if(myCells[cellNum]==null){
						continue;
					}
					values += "'"+myCells[cellNum].getContents().trim()+"',";
				}
				
				if(cun_name.get(myCells[4].getContents().trim())==null){
					System.out.println("出现未录制的行政区划22---"+Parent.getName()+"----"+filename+"----"+myCells[4].getContents().trim());
				}
				
				if(myCells[9].getContents().trim().equals("户主")){
					//全新的一条，开始记录每户人数
					number = Integer.parseInt(myCells[8].getContents().trim());
					
					//System.out.println(myCells[9].getContents().trim()+"      "+myCells[5].getContents().trim()+"    "+myCells[8].getContents().trim());
					values += "'国家级贫困人口'";
					//国家级贫困人口
					//市级低收入人口
					
					//加数据库
					String add_sql = "insert into da_household(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v21,v22,v23,v24,v25,v26,v27,sys_standard) "
							+ "values("+values+")";
					if(add_sql.indexOf("高四")>-1){
//						System.out.println(add_sql);
					}
					
					SQLAdapter Metadata_table_Adapter = new SQLAdapter(add_sql);
//					this.getBySqlMapper.insertSelective(Metadata_table_Adapter);//户主信息
//					
//					//取得户主的ID
//					String hou_sql = "select max(pkid) from da_household";
//					SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);//元数据表应该有哪些列
//					da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
//					
//					
//					//户主关联下的所有表，均需要增加记录
//					String sql_1 = "insert into da_household_basic(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
//					this.getBySqlMapper.insertSelective(sql_1_Adapter);//户主信息扩展
//					
//					
//					String sql_2 = "insert into da_production(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_2_Adapter = new SQLAdapter(sql_2);
//					this.getBySqlMapper.insertSelective(sql_2_Adapter);//生产条件
//					
//					String sql_3 = "insert into da_life(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
//					this.getBySqlMapper.insertSelective(sql_3_Adapter);//生活条件
//					
//					String sql_4 = "insert into da_current_income(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_4_Adapter = new SQLAdapter(sql_4);
//					this.getBySqlMapper.insertSelective(sql_4_Adapter);//当前收入情况
//					
//					String sql_5 = "insert into da_current_expenditure(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_5_Adapter = new SQLAdapter(sql_5);
//					this.getBySqlMapper.insertSelective(sql_5_Adapter);//当前支出情况
//					
//					String sql_6 = "insert into da_help_info(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_6_Adapter = new SQLAdapter(sql_6);
//					this.getBySqlMapper.insertSelective(sql_6_Adapter);//帮扶目标计划等内容
//					
//					String sql_9 = "insert into da_helpback_income(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_9_Adapter = new SQLAdapter(sql_9);
//					this.getBySqlMapper.insertSelective(sql_9_Adapter);//帮扶后收入情况
//					
//					String sql_10 = "insert into da_helpback_expenditure(da_household_id) values('"+da_household_id+"')";
//					SQLAdapter sql_10_Adapter = new SQLAdapter(sql_10);
//					this.getBySqlMapper.insertSelective(sql_10_Adapter);//帮扶后支出情况
					
					
					
					jiatingrenshu = 1;//记录家庭人数
				}else{
					jiatingrenshu++;
					
					if(jiatingrenshu<=number){//算上当前的人数，家庭人数应该比记录的人口数少或者等于
						//System.out.println(myCells[9].getContents().trim()+"      "+myCells[5].getContents().trim()+"    "+myCells[8].getContents().trim());
						//加数据库
						values += "'"+da_household_id+"'";
						String add_sql = "insert into da_member(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v21,v22,v23,v24,v25,v26,v27,da_household_id) "
								+ "values("+values+")";
						SQLAdapter Metadata_table_Adapter = new SQLAdapter(add_sql);
//						this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
						
					}else{
						System.out.println("错误---"+Parent.getName()+"----"+filename+"----"+myCells[4].getContents().trim()+"----"+myCells[5].getContents().trim());
					}
				}

			}

		}catch(Exception e){
			System.out.println("出现错误---"+Parent.getName()+"----"+filename+"----");
			e.printStackTrace();
		}
	}
	
	
}
