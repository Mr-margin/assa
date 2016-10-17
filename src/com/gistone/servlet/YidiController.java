package com.gistone.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

public class YidiController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * 获取行政区划树节点
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getTreeData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		HttpSession session = request.getSession();//取session
		Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
		JSONObject company_json = new JSONObject();
		try {
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			String com_name=company_json.get("com_name").toString();//获取用户名称
			String com_level=company_json.get("com_level").toString();//获取用户层级
			String com_code=company_json.get("com_code").toString();//获取用户code
			
			String sql="";//定义SQL语句的局部变量
			String sql_code="";//定义SQL查询语句中的条件
			
			if(com_level.equals("1")){//市级用户
				sql = "select * from sys_company";
			}else if(com_level.equals("2")){//旗县用户
				sql_code=com_code.substring(0,6);
				sql = "SELECT *FROM sys_company WHERE pkid=4 UNION ALL SELECT * FROM sys_company WHERE com_code LIKE '%"+sql_code+"%'";
			}
			
			SQLAdapter tree_Adapter = new SQLAdapter(sql);
			List<Map> tree_List = this.getBySqlMapper.findRecords(tree_Adapter);
			
			JSONObject val = new JSONObject();
			JSONArray jsa = new JSONArray();
			
			if(tree_List.size()>0){
				
				for(int i = 0;i<tree_List.size();i++){//第一遍循环，取市级单位
					Map shi_map = tree_List.get(i);
					if(shi_map.get("com_level").toString().equals("1")){
						
						JSONObject shi = new JSONObject();
						shi.put("text", shi_map.get("com_name"));
						shi.put("pkid", shi_map.get("pkid"));
						JSONArray shi_jsa = new JSONArray();
						
						for(int j = 0;j<tree_List.size();j++){//第二遍循环，取县级单位
							Map xian_map = tree_List.get(j);
							if(xian_map.get("com_level").toString().equals("2")&&xian_map.get("com_f_pkid").toString().equals(shi_map.get("pkid").toString())){//第二级，同时父id等于上一级id
								
								JSONObject xian = new JSONObject();
								xian.put("text", xian_map.get("com_name"));
								xian.put("pkid", xian_map.get("pkid"));
								JSONArray xian_jsa = new JSONArray();
								
								for(int p = 0;p<tree_List.size();p++){//第三遍循环，取乡单位
									Map xiang_map = tree_List.get(p);
									if(xiang_map.get("com_level").toString().equals("3")&&xiang_map.get("com_f_pkid").toString().equals(xian_map.get("pkid").toString())){//第三级，同时父id等于上一级id
										
										JSONObject xiang = new JSONObject();
										xiang.put("text", xiang_map.get("com_name"));
										xiang.put("pkid", xiang_map.get("pkid"));
										JSONArray xiang_jsa = new JSONArray();
										
										for(int k = 0;k<tree_List.size();k++){
											Map cun_map = tree_List.get(k);
											if(cun_map.get("com_level").toString().equals("4")&&cun_map.get("com_f_pkid").toString().equals(xiang_map.get("pkid").toString())){//第四级，同时父id等于上一级id
												JSONObject cun = new JSONObject();
												cun.put("text", cun_map.get("com_name"));
												cun.put("pkid", cun_map.get("pkid"));
												xiang_jsa.add(cun);
											}
										}
										
										xiang.put("nodes", xiang_jsa);
										xian_jsa.add(xiang);
									}
								}
								
								xian.put("nodes", xian_jsa);
								shi_jsa.add(xian);
							}
						}
						
						shi.put("nodes", shi_jsa);
						jsa.add(shi);//市级加到返回值中
					}
				}
				response.getWriter().write(jsa.toString());
			}
		} catch (Exception e) {
			response.getWriter().write("0");
		}
		return null;
	}
	
	//获取易地搬迁数据
	public ModelAndView getydbqData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String com_id = request.getParameter("pkid");
		
		String sql="SELECT * FROM da_move_statistics WHERE sys_company_id="+com_id+"";
		SQLAdapter get_Adapter = new SQLAdapter(sql);
		List<Map> Data_List = this.getBySqlMapper.findRecords(get_Adapter);
		
		JSONObject val = new JSONObject();
		if(Data_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Data_List.size();i++){
				Map Admin_st_map = Data_List.get(i);
				for (Object key : Admin_st_map.keySet()) {
					val.put(key, Admin_st_map.get(key));
				}
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else{
			response.getWriter().print("0");
		}
		
		return null;
	}
	
	//修改异地搬迁数据
	public ModelAndView upydbqdata(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String pkid=request.getParameter("pkid");
		String a11=request.getParameter("a11");
		String a12=request.getParameter("a12");
		String a13=request.getParameter("a13");
		String a14=request.getParameter("a14");
		String a21=request.getParameter("a21");
		String a22=request.getParameter("a22");
		String a23=request.getParameter("a23");
		String a24=request.getParameter("a24");
		String a31=request.getParameter("a31");
		String a32=request.getParameter("a32");
		String a33=request.getParameter("a33");
		String a34=request.getParameter("a34");
		
		String sql_1 = "UPDATE da_move_statistics SET start_place='"+a11+"',speed_proportion='"+a12+"',project_proportion='"+a13+"',capital='"+a14+"' WHERE sys_company_id='"+pkid+"' AND place_type='1';";
		String sql_2 = "UPDATE da_move_statistics SET start_place='"+a21+"',speed_proportion='"+a22+"',project_proportion='"+a23+"',capital='"+a24+"' WHERE sys_company_id='"+pkid+"' AND place_type='2';";
		String sql_3 = "UPDATE da_move_statistics SET start_place='"+a31+"',speed_proportion='"+a32+"',project_proportion='"+a33+"',capital='"+a34+"' WHERE sys_company_id='"+pkid+"' AND place_type='3';";
		
		SQLAdapter tree_1 = new SQLAdapter(sql_1);
	    SQLAdapter tree_2 = new SQLAdapter(sql_2);
	    SQLAdapter tree_3 = new SQLAdapter(sql_3);
	     
	     try{
			 this.getBySqlMapper.findRecords(tree_1);
		     this.getBySqlMapper.findRecords(tree_2);
		     this.getBySqlMapper.findRecords(tree_3);
				response.getWriter().write("1");
			}catch (Exception e){
				response.getWriter().write("0");
			}
		return null;
	}
	
	//获取工程进度数据-获取行政单位列表
	public ModelAndView getgcjdxbData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String com_id = request.getParameter("pkid");
		String sql="";
		if(com_id=="4"){
			sql="SELECT * FROM sys_company WHERE com_level=2";
		}else{
			sql="SELECT * from sys_company WHERE com_level=3 AND com_f_pkid="+com_id+"";
		}
		SQLAdapter get_Adapter = new SQLAdapter(sql);
		List<Map> Data_List = this.getBySqlMapper.findRecords(get_Adapter);
		
		JSONObject val = new JSONObject();
		if(Data_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Data_List.size();i++){
				Map Admin_st_map = Data_List.get(i);
				for (Object key : Admin_st_map.keySet()) {
					val.put(key, Admin_st_map.get(key));
				}
				jsa.add(val);
			}
			System.out.println(jsa.toString());
			response.getWriter().write(jsa.toString());
		}else{
			response.getWriter().print("0");
		}
		
		return null;
	}

	
	/**
	 * 数据统计模块，树节点
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView get_tj_TreeData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String sql = "select * from sys_company";//定义SQL语句的局部变量
		
		SQLAdapter tree_Adapter = new SQLAdapter(sql);
		List<Map> tree_List = this.getBySqlMapper.findRecords(tree_Adapter);
		
		JSONObject val = new JSONObject();
		JSONArray jsa = new JSONArray();
		
		if(tree_List.size()>0){
			
			for(int i = 0;i<tree_List.size();i++){//第一遍循环，取市级单位
				Map shi_map = tree_List.get(i);
				if(shi_map.get("com_level").toString().equals("1")){
					
					JSONObject shi = new JSONObject();
					shi.put("text", shi_map.get("com_name"));
					shi.put("pkid", shi_map.get("pkid"));
					JSONArray shi_jsa = new JSONArray();
					
					for(int j = 0;j<tree_List.size();j++){//第二遍循环，取县级单位
						Map xian_map = tree_List.get(j);
						if(xian_map.get("com_level").toString().equals("2")&&xian_map.get("com_f_pkid").toString().equals(shi_map.get("pkid").toString())){//第二级，同时父id等于上一级id
							
							JSONObject xian = new JSONObject();
							xian.put("text", xian_map.get("com_name"));
							xian.put("pkid", xian_map.get("pkid"));
							JSONArray xian_jsa = new JSONArray();
							
							for(int p = 0;p<tree_List.size();p++){//第三遍循环，取乡单位
								Map xiang_map = tree_List.get(p);
								if(xiang_map.get("com_level").toString().equals("3")&&xiang_map.get("com_f_pkid").toString().equals(xian_map.get("pkid").toString())){//第三级，同时父id等于上一级id
									
									JSONObject xiang = new JSONObject();
									xiang.put("text", xiang_map.get("com_name"));
									xiang.put("pkid", xiang_map.get("pkid"));
//									JSONArray xiang_jsa = new JSONArray();
//									
//									for(int k = 0;k<tree_List.size();k++){
//										Map cun_map = tree_List.get(k);
//										if(cun_map.get("com_level").toString().equals("4")&&cun_map.get("com_f_pkid").toString().equals(xiang_map.get("pkid").toString())){//第四级，同时父id等于上一级id
//											JSONObject cun = new JSONObject();
//											cun.put("text", cun_map.get("com_name"));
//											cun.put("pkid", cun_map.get("pkid"));
//											xiang_jsa.add(cun);
//										}
//									}
//									
//									xiang.put("nodes", xiang_jsa);
									xian_jsa.add(xiang);
								}
							}
							
							xian.put("nodes", xian_jsa);
							shi_jsa.add(xian);
						}
					}
					
					shi.put("nodes", shi_jsa);
					jsa.add(shi);//市级加到返回值中
				}
			}
			response.getWriter().write(jsa.toString());
		}
		
		return null;
	}
}
