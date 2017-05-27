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
			
//			String count_g_sql = "select count(*) from da_household where sys_standard='国家级贫困人口' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str;
			//识别退出国贫总人数
			
			
			
			
//			String Metadata_g_sql = "select pkid,v3,v4,v5,v6,v9 from da_household where sys_standard='国家级贫困人口' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str+" limit "+number+","+size;
			//识别退出国贫所有人
			String sql ="select pkid,v9,v3,v4,v5,v6,renjun jisuan from ("+
						"  select a.*,b.v2,round((v39-v31)/v9,2) renjun from  ( "+
						" select pkid,v3,v4,v5,v6,v9 from da_household where  init_flag='国家级贫困人口' "+
						" and v21!='已脱贫' and (v3 like '%"+search+"%' or v4 like '%"+search+"%' or v5 like '%"+search+"%' or v6 like '%"+search+"%' or v9 like '%"+search+"%') "+str+""+
						") a LEFT JOIN (select da_household_id,v2 from da_life where  v2='否')b ON a.pkid=b.da_household_id LEFT JOIN ("+
						"select da_household_id,v39 from da_helpback_income where v39 is  not null ) q1 on a.pkid=q1.da_household_id LEFT JOIN ( "+
						"select da_household_id,v31 from da_helpback_expenditure where v31 is not null)q2 on a.pkid = q2.da_household_id "+
						" where b.v2='否' and (v39-v31)/v9>4000)aa group by pkid ";
			String select_sql = sql +"limit "+number+","+size;
			String count_sql = "select count(*) from ("+sql +")aa"; 
			SQLAdapter Metadata_g_Adapter = new SQLAdapter(select_sql);
			SQLAdapter count_g_Adapter = new SQLAdapter(count_sql);
			int total = this.getBySqlMapper.findrows(count_g_Adapter);
			List<Map> Metadata_g_List = this.getBySqlMapper.findRecords(Metadata_g_Adapter);
			
			if(Metadata_g_List.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<Metadata_g_List.size();i++){
					Map Metadata_g_map = Metadata_g_List.get(i);
					JSONObject val = new JSONObject();
					for (Object key : Metadata_g_map.keySet()) {
						val.put(key, Metadata_g_map.get(key));
						val.put("v6","<a onclick='chakan_info(\""+Metadata_g_map.get("pkid")+"\")'>"+Metadata_g_map.get("v6")+"</a>");
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
			//查询init_flag是市级低收入人口
			String count_s_sql = "select count(*) from da_household where init_flag='市级低收入人口' and (v3 like '"+search+"%' or v4 like '"+search+"%' or v5 like '"+search+"%' or v6 like '"+search+"%' or v9 like '"+search+"%') "+str;
			
			//符合市级低收入户要求帮扶后人均纯收入比帮扶前增长20%、帮扶后人均纯收入大于1万元、不是危房、所有家庭成员均参加新农合、养老保险 条件的sql 语句
			String sql = "";
					sql += "select pkid,v3,v4,v5,v6,v9, round(((v39-v31)/v9),2) hrj,round((ABS((d39-d31))/v9),2) drj,";
					sql += "round(((((v39-v31)/v9)-(ABS((d39-d31)/v9)))/ (ABS((d39-d31)/v9)))*100,2) jisuan from  ( ";
					sql +="select pkid,v3,v4,v5,v6,v9,init_flag from da_household where  init_flag='市级低收入人口' and v21!='已脱贫'";
					sql += " and (v3 like '"+search+"%' or v4 like '"+search+"%' or v5 like '"+search+"%' or v6 like '"+search+"%' or v9 like '"+search+"%') "+str+" ";
					sql += ") a LEFT JOIN (select da_household_id,v2 from da_life  where v2 ='否')b ON a.pkid=b.da_household_id LEFT JOIN (";
					sql += "select da_household_id,v39 from da_helpback_income where v39 is not null ) q1 on a.pkid=q1.da_household_id LEFT JOIN(";
				
					sql += "select da_household_id,v31 from da_helpback_expenditure where v31 is not null)q2 on a.pkid = q2.da_household_id LEFT JOIN ("+
							"select da_household_id,v39 d39 from da_current_income where v39 is not null ) q3 on a.pkid=q3.da_household_id LEFT JOIN (";
					sql += "select da_household_id ,v31 d31 from da_current_expenditure where v31 is not null) q4 on a.pkid=q4.da_household_id "+
							"   where b.v2='否' and ((((v39-v31)/v9)-(ABS((d39-d31)/v9)))/ (ABS((d39-d31)/v9)))*100>20  and ((v39-v31)/v9)>10000 group by pkid";
					
					
			String  select_sql = sql+"  limit "+number+","+size;	
			String  count_sql = "select count(*) from ("+sql+")aa";
			SQLAdapter count_s_Adapter = new SQLAdapter(count_sql);
			int total = this.getBySqlMapper.findrows(count_s_Adapter);
			SQLAdapter Metadats_g_Adapter = new SQLAdapter(select_sql);
			List<Map> Metadata_s_List = this.getBySqlMapper.findRecords(Metadats_g_Adapter);
			
			if(Metadata_s_List.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<Metadata_s_List.size();i++){
					Map Metadata_s_map = Metadata_s_List.get(i);
					JSONObject val = new JSONObject();
				
					for (Object key : Metadata_s_map.keySet()) {
						val.put(key, Metadata_s_map.get(key));
						val.put("v6","<a onclick='chakan_info(\""+Metadata_s_map.get("pkid")+"\")'>"+Metadata_s_map.get("v6")+"</a>");
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
	 * 识别退出
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public  ModelAndView getGp_tuichu(HttpServletRequest request,HttpServletResponse response ) throws IOException {
		String  str = request.getParameter("str");//贫困户pkid
		String [] pkid = str.split(",");
		String type = request.getParameter("type");//国贫还是市贫 0、国贫 1、市贫
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			try {
				for ( int i = 0 ; i < pkid.length ; i ++ ) {
					if( "0".equals(type) ) {
						String sql = "update da_household set init_flag='市级低收入人口' where pkid = "+pkid[i];
						String tui_sql = "select v21 from da_household_2016 where pkid="+pkid[i];
						List<Map> tui_list =  this.getBySqlMapper.findRecords(new SQLAdapter(tui_sql));
						if(tui_list.get(0).get("v21").equals("已脱贫")){
							response.getWriter().write("2");
						}else{
							SQLAdapter sqlAdapter = new SQLAdapter (sql);
							this.getBySqlMapper.updateSelective(sqlAdapter);
							
							String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
									" VALUES ('da_household',"+pkid[i]+",'退出',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','识别与退出','转市贫')";
							SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
							this.getBySqlMapper.findRecords(hqlAdapter1);
							response.getWriter().write("0");
						}
						
					}else if ( "1".equals(type) ) {
						String sql = "update da_household set v21='已脱贫' where pkid = "+pkid[i];
						String tui_sql = "select v21 from da_household_2016 where pkid="+pkid[i];
						List<Map> tui_list =  this.getBySqlMapper.findRecords(new SQLAdapter(tui_sql));
						if(tui_list.get(0).get("v21").equals("已脱贫")){
							response.getWriter().write("2");
						}else{
							SQLAdapter sqlAdapter = new SQLAdapter (sql);
							this.getBySqlMapper.updateSelective(sqlAdapter);
							String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
									" VALUES ('da_household',"+pkid[i]+",'退出',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','识别与退出','退贫')";
							SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
							this.getBySqlMapper.findRecords(hqlAdapter1);
							response.getWriter().write("0");
						}
						
					}
				}
				
			} catch (Exception e) {
				response.getWriter().write("1");
			}
		}
		return null ;
	}
}
