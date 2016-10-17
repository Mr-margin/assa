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
import com.gistone.util.Tool;

public class SignOutController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	
	/**
	 * 国家标准退出名单初始化
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getCountry_init(HttpServletRequest request,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String search = "";
		
		if(request.getParameter("search")!=null&&!request.getParameter("search").equals("")){
			search = request.getParameter("search").trim();
		}
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		
		int page = number == 0 ? 1 : (number/size)+1;
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
			
			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			
			String str = "";
			if(company_json.get("com_level").toString().equals("1")){
				
			}else if(company_json.get("com_level").toString().equals("2")){
				str = " and v3='"+company_json.get("xian")+"'";
			}else if(company_json.get("com_level").toString().equals("3")){
				str = " and v3='"+company_json.get("xian")+"' and v4="+company_json.get("xiang")+"'";
			}else if(company_json.get("com_level").toString().equals("4")){
				str = " and v3='"+company_json.get("xian")+"' and v4="+company_json.get("xiang")+"' and v5="+company_json.get("cun")+"'";
			}
			
			String count_g_sql = "select count(*) from da_household where sys_standard='国家级贫困人口' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str;
			SQLAdapter count_g_Adapter = new SQLAdapter(count_g_sql);
			int total = this.getBySqlMapper.findrows(count_g_Adapter);
			
			
			String Metadata_g_sql = "select pkid,v3,v4,v5,v6,v9 from da_household where sys_standard='国家级贫困人口' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str+" limit "+number+","+size;
			SQLAdapter Metadata_g_Adapter = new SQLAdapter(Metadata_g_sql);
			List<Map> Metadata_g_List = this.getBySqlMapper.findRecords(Metadata_g_Adapter);
			
			if(Metadata_g_List.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<Metadata_g_List.size();i++){
					Map Metadata_g_map = Metadata_g_List.get(i);
					JSONObject val = new JSONObject();
					for (Object key : Metadata_g_map.keySet()) {
						val.put(key, Metadata_g_map.get(key));
					}
					jsa.add(val);
				}
				
				JSONObject json = new JSONObject();
				json.put("page", page);
				json.put("total", total);
				json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
				  
				//System.out.println(json.toString());
				response.getWriter().write(json.toString());
			}
			
		}
		
		return null;
	}
	
	/**
	 * 市级标准退出名单初始化
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getCity_init(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String search = "";
		
		if(request.getParameter("search")!=null&&!request.getParameter("search").equals("")){
			search = request.getParameter("search").trim();
		}
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		
		int page = number == 0 ? 1 : (number/size)+1;
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
			
			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			
			String str = "";
			if(company_json.get("com_level").toString().equals("1")){
				
			}else if(company_json.get("com_level").toString().equals("2")){
				str = " and v3='"+company_json.get("xian")+"'";
			}else if(company_json.get("com_level").toString().equals("3")){
				str = " and v3='"+company_json.get("xian")+"' and v4="+company_json.get("xiang")+"'";
			}else if(company_json.get("com_level").toString().equals("4")){
				str = " and v3='"+company_json.get("xian")+"' and v4="+company_json.get("xiang")+"' and v5="+company_json.get("cun")+"'";
			}
			
			String count_s_sql = "select count(*) from da_household where sys_standard='市级低收入人口' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str;
			SQLAdapter count_s_Adapter = new SQLAdapter(count_s_sql);
			int total = this.getBySqlMapper.findrows(count_s_Adapter);
			
			
			String Metadata_s_sql = "select pkid,v3,v4,v5,v6,v9 from da_household where sys_standard='市级低收入人口' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str+" limit "+number+","+size;
			SQLAdapter Metadats_g_Adapter = new SQLAdapter(Metadata_s_sql);
			List<Map> Metadata_s_List = this.getBySqlMapper.findRecords(Metadats_g_Adapter);
			
			if(Metadata_s_List.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<Metadata_s_List.size();i++){
					Map Metadata_s_map = Metadata_s_List.get(i);
					JSONObject val = new JSONObject();
					for (Object key : Metadata_s_map.keySet()) {
						val.put(key, Metadata_s_map.get(key));
					}
					jsa.add(val);
				}
				
				JSONObject json = new JSONObject();
				json.put("page", page);
				json.put("total", total);
				json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
				  
				//System.out.println(json.toString());
				response.getWriter().write(json.toString());
			}
			
		}
		
		return null;
	}
}
