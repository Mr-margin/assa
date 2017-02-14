package com.gistone.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
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

public class Index_Controller extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;

	//	首页上方的表格中-贫困户数-贫困人数
	public ModelAndView getindex_table1(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String gors = request.getParameter("gors");
		String code = request.getParameter("code");

		HttpSession session = request.getSession();//取session
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称

			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}

			if(code.toString().equals("shi")==true){//市级用户
				String sql="select b.v3,count,count1,sum,sum1 from (SELECT v3,count(*) AS count,sum(v9) AS sum FROM da_household where sys_standard='"+gors+"' and v21 !='已脱贫' group by v3) a"+
							" right JOIN (SELECT v3,count(*) AS count1 ,sum(v9) AS sum1 FROM da_household_2016 where sys_standard='"+gors+"' and v21 !='已脱贫' group by v3 )b on  a.v3=b.v3";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject val = new JSONObject();
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					for(int i = 0;i<sql_list.size();i++){
						Map Admin_st_map = sql_list.get(i);
						for (Object key : Admin_st_map.keySet()) {
							val.put(key, Admin_st_map.get(key));
						}
						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else if(company_json.get("com_level").toString().equals("2")==true){//二级用户
//				String sql="SELECT y1.v4 AS v3,count(*) AS count,sum(y1.v9) AS sum FROM da_household y1 JOIN sys_company y2 ON y1.v3 = y2.com_name where y2.com_code="+code+" AND y1.sys_standard='"+gors+"' and v21!='已脱贫'  group by y1.v4";
				String sql=" select b.v3,count,count1,sum,sum1 from ( "+
							"SELECT y1.v4 AS v3,count(*) AS count,sum(y1.v9) AS sum FROM da_household y1 JOIN sys_company y2 ON y1.v3 = y2.com_name where y2.com_code="+code+" AND y1.sys_standard='"+gors+"' and v21!='已脱贫'  group by y1.v4"+
							")a right join ("+
							"SELECT y1.v4 AS v3,count(*) AS count1,sum(y1.v9) AS sum1 FROM da_household_2016 y1 JOIN sys_company y2 ON y1.v3 = y2.com_name where y2.com_code="+code+" AND y1.sys_standard='"+gors+"' and v21!='已脱贫'  group by y1.v4"+
							" )b on a.v3 = b.v3"	;
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject val = new JSONObject();
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					for(int i = 0;i<sql_list.size();i++){
						val.put("v3","".equals(sql_list.get(i).get("v3")) || sql_list.get(i).get("v3") == null ?"": sql_list.get(i).get("v3").toString());
						val.put("count","".equals(sql_list.get(i).get("count")) || sql_list.get(i).get("count") == null ?"0": sql_list.get(i).get("count").toString());
						val.put("count1","".equals(sql_list.get(i).get("count1")) || sql_list.get(i).get("count1") == null ?"0": sql_list.get(i).get("count1").toString());
						val.put("sum","".equals(sql_list.get(i).get("sum")) || sql_list.get(i).get("sum") == null ?"0": sql_list.get(i).get("sum").toString());
						val.put("sum1","".equals(sql_list.get(i).get("sum1")) || sql_list.get(i).get("sum1") == null ?"0": sql_list.get(i).get("sum1").toString());
						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else if(company_json.get("com_level").toString().equals("3")==true){
//				String sql="SELECT y1.v5 AS v3,count(*) AS count,sum(y1.v9) AS sum FROM da_household y1 JOIN sys_company y2 ON y1.v4 = y2.com_name where y2.com_code='"+code+"' AND y1.sys_standard='"+gors+"' and y1.v21!='已脱贫'  group by y1.v5";
				String sql=" select b.v3,count,count1,sum,sum1 from ("+
						"SELECT y1.v5 AS v3,count(*) AS count,sum(y1.v9) AS sum FROM da_household y1 JOIN sys_company y2 ON y1.v4 = y2.com_name where y2.com_code='"+code+"' AND y1.sys_standard='"+gors+"' and y1.v21!='已脱贫'  group by y1.v5"+
						")a right join ("+
						"SELECT y1.v5 AS v3,count(*) AS count1,sum(y1.v9) AS sum1 FROM da_household_2016 y1 JOIN sys_company y2 ON y1.v4 = y2.com_name where y2.com_code='"+code+"' AND y1.sys_standard='"+gors+"' and y1.v21!='已脱贫'  group by y1.v5"+
						")b on a.v3 = b.v3 ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject val = new JSONObject();
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					for(int i = 0;i<sql_list.size();i++){
						Map Admin_st_map = sql_list.get(i);
						for (Object key : Admin_st_map.keySet()) {
							val.put(key, Admin_st_map.get(key));
						}
						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else if(company_json.get("com_level").toString().equals("4")==true){
				String sql=" select b.v3,count,count1,sum,sum1 from ("+
						"SELECT y1.v5 AS v3,count(*) AS count,sum(y1.v9) AS sum FROM da_household y1 JOIN sys_company y2 ON y1.v4 = y2.com_name where y2.com_code='"+code+"' AND y1.sys_standard='"+gors+"' and y1.v21!='已脱贫' group by y1.v5"+
						" )a right join ("+
						" SELECT y1.v5 AS v3,count(*) AS count1,sum(y1.v9) AS sum1 FROM da_household_2016 y1 JOIN sys_company y2 ON y1.v4 = y2.com_name where y2.com_code='"+code+"' AND y1.sys_standard='"+gors+"' and y1.v21!='已脱贫' group by y1.v5"+
						")b on a.v3=b.v3";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject val = new JSONObject();
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					for(int i = 0;i<sql_list.size();i++){
						Map Admin_st_map = sql_list.get(i);
						for (Object key : Admin_st_map.keySet()) {
							val.put(key, Admin_st_map.get(key));
						}
						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}
		}else{//未登录
			String sql="select b.v3,count,count1,sum,sum1 from (SELECT v3,count(*) AS count,sum(v9) AS sum FROM da_household where sys_standard='"+gors+"' and v21!='已脱贫' group by v3)a "+
						"  right JOIN  (SELECT v3,count(*) AS count1,sum(v9) AS sum1 FROM da_household_2016 where sys_standard='"+gors+"' and v21!='已脱贫' group by v3)b "+
						" on a.v3 = b.v3 ";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						val.put(key, Admin_st_map.get(key));
					}
					jsa.add(val);
				}
			}
			
			response.getWriter().write(jsa.toString());
		}

		return null;
	}
	//	首页地图中的数据data
	public ModelAndView getindex_table11(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String gors = request.getParameter("gors");
		String code = request.getParameter("code");


		HttpSession session = request.getSession();//取session
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称

			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}

			if(code.toString().equals("shi")){//市级
				String sql="SELECT v3,count(*) AS count FROM da_household  where sys_standard='"+gors+"' and v21!='已脱贫' GROUP BY v3";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject val = new JSONObject();
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					for(int i = 0;i<sql_list.size();i++){
						Map Admin_st_map = sql_list.get(i);
						for(int j = 0; j<Admin_st_map.size(); j++){
							val.put("name", Admin_st_map.get("v3"));
							val.put("value", Admin_st_map.get("count"));
						}
						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else{
				if(company_json.get("com_level").toString().equals("2")==true){//二级单位

					String sql="SELECT a1.v4 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v3 = a2.com_name WHERE a2.com_code='"+code+"' AND a1.sys_standard='"+gors+"' and a1.v21!='已脱贫' GROUP BY a1.v4";
					SQLAdapter sqlAdapter =new SQLAdapter(sql);
					List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
					JSONObject val = new JSONObject();
					if(sql_list.size()>0){
						JSONArray jsa=new JSONArray();
						for(int i = 0;i<sql_list.size();i++){
							Map Admin_st_map = sql_list.get(i);
							for(int j = 0; j<Admin_st_map.size(); j++){
								val.put("name", Admin_st_map.get("v3"));
								val.put("value", Admin_st_map.get("count"));
							}
							jsa.add(val);
						}
						response.getWriter().write(jsa.toString());
					}else{
						response.getWriter().print("0");
					}
				}else if(company_json.get("com_level").toString().equals("3")==true){//三级单位
					String sql="SELECT a1.v5 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v4 = a2.com_name WHERE a2.com_code='"+code+"' AND a1.sys_standard='"+gors+"' and a1.v21!='已脱贫' GROUP BY a1.v5";
					SQLAdapter sqlAdapter =new SQLAdapter(sql);
					List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
					JSONObject val = new JSONObject();
					if(sql_list.size()>0){
						JSONArray jsa=new JSONArray();
						for(int i = 0;i<sql_list.size();i++){
							Map Admin_st_map = sql_list.get(i);
							for(int j = 0; j<Admin_st_map.size(); j++){
								val.put("name", Admin_st_map.get("v3"));
								val.put("value", Admin_st_map.get("count"));
							}
							jsa.add(val);
						}
						response.getWriter().write(jsa.toString());
					}else{
						response.getWriter().print("0");
					}
				}else if(company_json.get("com_level").toString().equals("4")==true){//四级单位
					String sql="SELECT a1.v5 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v4 = a2.com_name WHERE a2.com_code='"+code+"' AND a1.sys_standard='"+gors+"' and a1.v21!='已脱贫' GROUP BY a1.v5";
					SQLAdapter sqlAdapter =new SQLAdapter(sql);
					List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
					JSONObject val = new JSONObject();
					if(sql_list.size()>0){
						JSONArray jsa=new JSONArray();
						for(int i = 0;i<sql_list.size();i++){
							Map Admin_st_map = sql_list.get(i);
							for(int j = 0; j<Admin_st_map.size(); j++){
								val.put("name", Admin_st_map.get("v3"));
								val.put("value", Admin_st_map.get("count"));
							}
							jsa.add(val);
						}
						response.getWriter().write(jsa.toString());
					}else{
						response.getWriter().print("0");
					}
				}
			}

		}else{//如果session为空，即未登录
			String sql="SELECT v3,count(*) AS count FROM da_household  where sys_standard='"+gors+"' and v21!='已脱贫' GROUP BY v3";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			if(sql_list.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for(int j = 0; j<Admin_st_map.size(); j++){
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
					}
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().print("0");
			}
		}

		return null;
	}

	//首页下方的表格
	public ModelAndView getindex_table2(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String gors = request.getParameter("gors");//获取判断国标或者市标的参数
		String code = request.getParameter("code");//获取行政编码
		HttpSession session = request.getSession();//取session
		JSONObject json = new JSONObject();
		String sql = "", ziduan = "", tiaojian = "", cun_duyou = "",xc_name = "";

		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			if(code.toString().equals("shi")){//市级
				ziduan = "v1";
				tiaojian = " where com_level=2 and b2>0 order by b2 desc ";
			}else if(company_json.get("com_level").toString().equals("2")==true){//二级用户
				ziduan = "v2";
				tiaojian = " where com_f_pkid="+company_json.get("pkid")+" and b2>0 order by b2 desc ";
			}else if(company_json.get("com_level").toString().equals("3")==true){//三级用户
				ziduan = "v3";
				xc_name = "and v2= '"+company_json.get("com_name").toString()+"'";
				tiaojian = " where com_f_pkid="+company_json.get("pkid")+" and b2>0  order by b2 desc ";
				cun_duyou = " and v2 in(select com_name from sys_company w1 where w1.pkid="+company_json.get("pkid")+") ";
			}else if(company_json.get("com_level").toString().equals("4")==true){//四级用户
				ziduan = "v3";
				xc_name = "and v2= '"+company_json.get("xiang").toString()+"'";
				tiaojian = " where com_f_pkid="+company_json.get("com_f_pkid")+" and b2>0 order by b2 desc ";
				cun_duyou = " and v2 in(select com_name from sys_company w1 where w1.pkid="+company_json.get("com_f_pkid")+") ";
			}
		}else{//用户未登录，默认显示全市
			ziduan = "v1";
			tiaojian = " where c.com_level=2 and b2>0 order by b2 desc ";
		}
		sql = " SELECT a.v2,b.b1,b2,b22,b3,b33,b4,b6,b7,b8,b9,b10,b11,b12,b13 from (SELECT v2,b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from (select a.v2,a."+ziduan+" as b1,"+
				"SUM(a.b2) as b2 , SUM(a.b3) as b3, SUM(a.b4) as b4,SUM(a.b6)as b6,SUM(a.b7)as b7 ,SUM(a.b8) as b8,SUM(a.b9) as b9,SUM(a.b10) as b10,SUM(a.b11) as b11,"+
				"SUM(a.b12) as b12,SUM(a.b13) as b13,a.b14 from da_statistics a "+
				" where a.b14='"+ gors +"' "+xc_name+" GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name"+tiaojian+")a right join("+
				"SELECT v2,b1,b2 b22,b3 b33 from (select a.v2,a."+ziduan+" as b1,"+
				"SUM(a.b2) as b2 , SUM(a.b3) as b3  from da_statistics_2016 a "+
				" where a.b14='"+ gors +"' "+xc_name+" GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name"+tiaojian+")b on a.b1=b.b1";
		SQLAdapter sql_find=new SQLAdapter(sql);
		List<Map> sql_list = this.getBySqlMapper.findRecords(sql_find);
		JSONObject val = new JSONObject();
		if(sql_list.size()>0){
			JSONArray jsa=new JSONArray();
			for(Map asmap:sql_list){
				val.put("b1",asmap.get("b1")==null?"":asmap.get("b1"));
				val.put("b2",asmap.get("b2")==null?"0":asmap.get("b2"));
				val.put("b3",asmap.get("b3")==null?"0":asmap.get("b3"));
				val.put("b22",asmap.get("b22")==null?"0":asmap.get("b22"));
				val.put("b33",asmap.get("b33")==null?"0":asmap.get("b33"));
				val.put("b10",asmap.get("b10")==null?"0":asmap.get("b10"));
				val.put("b11",asmap.get("b11")==null?"0":asmap.get("b11"));
				val.put("b12",asmap.get("b12")==null?"0":asmap.get("b12"));
				val.put("b13",asmap.get("b13")==null?"0":asmap.get("b13"));
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else{
			response.getWriter().print("0");
		}
		return null;
	}

	//登录验证
	public ModelAndView loginin(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String username = request.getParameter("add_account");//获取用户名 
		String password = request.getParameter("add_password");//获取密码
		String people_sql = "select * from sys_user WHERE col_account = '" + username + "'";
		SQLAdapter Login_Adapter = new SQLAdapter(people_sql);
		List<Map> Login = this.getBySqlMapper.findRecords(Login_Adapter);
		if(Login.size()>0){
			Map Login_map = Login.get(0);

			if(Login_map.get("account_state").toString().equals("1")){//状态正常
				if(Tool.md5(password).equals(Login_map.get("col_password"))==true){//密码正确

					HttpSession session = request.getSession();
					//Login_map.put("col_password", "");
					Login_map.remove("col_password");
					session.setAttribute("Login_map", Login_map);
					//功能权限
					Map function_map = new HashMap();

					//表格显示的内容
					Map company_map=new HashMap();
					//维护开关
					Map weihu_map=new HashMap();

					if(username.equals("admin")){//超级用户 

						//超管就是市级的
						String sql_zong = "select * from sys_company where pkid="+Login_map.get("sys_company_id");
						SQLAdapter sql_zong_Adapter = new SQLAdapter(sql_zong);
						List<Map> sql_zong_list = this.getBySqlMapper.findRecords(sql_zong_Adapter);
						Map zpong = sql_zong_list.get(0);
						session.setAttribute("company", zpong);

						//System.out.println(Tool.md5("admin123456"));
						//超管有所有权限，维护开关不影响
						String sql_1 = "select pkid,div_id,maintain from sys_function";
						SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
						List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
						for(int i = 0;i<sql_1_list.size();i++){
							Map tep = sql_1_list.get(i);
							function_map.put(tep.get("div_id"), tep.get("div_id"));
							weihu_map.put(tep.get("div_id"), "1");
						}
						session.setAttribute("function_map", function_map);
						session.setAttribute("weihu_map", weihu_map);

						//超管的数据浏览权限为市级最大
						company_map.put("com_type", "管理员");
						session.setAttribute("company_map", company_map);

						response.getWriter().print("1");//成功

					}else{

						//功能模块权限
						if(Login_map.get("account_type").toString().equals("1")){//行政单位用户 ，数据显示需要关联，行政区划表（sys_company，sys_company_id）
							company_map.put("com_type", "单位");
							session.setAttribute("company_map", company_map);//浏览数据的权限

							//先获取单位信息，获取单位层级
							String sql_zong = "select * from sys_company where pkid="+Login_map.get("sys_company_id");
							SQLAdapter sql_zong_Adapter = new SQLAdapter(sql_zong);
							List<Map> sql_zong_list = this.getBySqlMapper.findRecords(sql_zong_Adapter);
							Map zpong = sql_zong_list.get(0);

							if(zpong.get("com_level").toString().equals("1")){//市级单位

								session.setAttribute("company", zpong);

							}else if(zpong.get("com_level").toString().equals("2")){//旗县级

								zpong.put("xian_id",zpong.get("pkid"));
								zpong.put("xian",zpong.get("com_name"));
								zpong.put("xian_code",zpong.get("com_code"));
								session.setAttribute("company", zpong);

							}else if(zpong.get("com_level").toString().equals("3")){//乡级或者村级

								String sql_3 = "select x.*,y.pkid as xian_id,y.com_name as xian,y.com_code as xian_code from sys_company x join sys_company y on x.com_f_pkid=y.pkid where x.pkid="+Login_map.get("sys_company_id");
								SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
								List<Map> sql_3_list = this.getBySqlMapper.findRecords(sql_3_Adapter);
								Map namemap = sql_3_list.get(0);
								namemap.put("xiang_id",namemap.get("pkid"));
								namemap.put("xiang",namemap.get("com_name"));
								namemap.put("xiang_code",namemap.get("com_code"));
								session.setAttribute("company", namemap);

							}else if(zpong.get("com_level").toString().equals("4")){

								String sql_3 = "select x.*,z.pkid as xian_id,z.com_name as xian,z.com_code as xian_code,y.pkid as xiang_id,y.com_name as xiang,y.com_code as xiang_code from sys_company x join sys_company y on x.com_f_pkid=y.pkid join sys_company z on y.com_f_pkid=z.pkid where x.pkid="+Login_map.get("sys_company_id");
								SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
								List<Map> sql_3_list = this.getBySqlMapper.findRecords(sql_3_Adapter);
								Map namemap = sql_3_list.get(0);
								namemap.put("cun_id",namemap.get("pkid"));
								namemap.put("cun",namemap.get("com_name"));
								namemap.put("cun_code",namemap.get("com_code"));
								session.setAttribute("company", namemap);

							}

							//行政区划单位不受角色控制，拥有所有权限，但是受维护开关限制
							//String sql_1 = "select pkid,div_id,maintain from sys_function";
							String sql_1 = "select y.modular_name,y.div_id,y.maintain from sys_company_function_many x join sys_function y on x.sys_function_id=y.pkid where sys_company_level="+zpong.get("com_level").toString();
							SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
							List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
							for(int i = 0;i<sql_1_list.size();i++){
								Map tep = sql_1_list.get(i);

								//								if(tep.get("div_id").toString().equals("H5-3_li")){//维护开关，只有市级有权限，这个是限制死的
								//									if(zpong.get("com_level").toString().equals("1")){//市级单位
								//										function_map.put(tep.get("div_id"), tep.get("div_id"));
								//										weihu_map.put(tep.get("div_id"), tep.get("maintain"));
								//									}
								//								}else{
								function_map.put(tep.get("div_id"), tep.get("div_id"));
								weihu_map.put(tep.get("div_id"), tep.get("maintain"));
								//								}

							}

							session.setAttribute("function_map", function_map);
							session.setAttribute("weihu_map", weihu_map);

						}else if(Login_map.get("account_type").toString().equals("2")){//帮扶个人用户,数据需要关联贫困户与帮扶人关系表 （sys_personal_household_many，sys_personal_id）

							String namesql="SELECT col_name FROM sys_personal WHERE pkid="+Login_map.get("sys_personal_id"); 
							SQLAdapter namesql_Adapter = new SQLAdapter(namesql);
							List<Map> namesql_list = this.getBySqlMapper.findRecords(namesql_Adapter);
							Map namemap = namesql_list.get(0);
							namemap.put("username",namemap.get("col_name"));
							session.setAttribute("namemap", namemap);

							if(!Login_map.get("sys_role_id").toString().equals("")&&Login_map.get("sys_role_id")!=null){//当用户权限不为null.
								String sql = "select y.pkid,y.div_id from sys_role_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
										" sys_role_id ="+Login_map.get("sys_role_id");
								SQLAdapter sql_1_Adapter = new SQLAdapter(sql);
								List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
								for(int i = 0;i<sql_1_list.size();i++){
									Map tep = sql_1_list.get(i);
									function_map.put(tep.get("div_id"), tep.get("div_id"));
								}
							}

							//再查询一下，用户直接对应权限表(sys_user_function_many)
							String sql_2 = "select y.pkid,y.div_id,y.maintain from sys_user_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
									" sys_user_id=(SELECT sys_personal_id from sys_user WHERE pkid="+Login_map.get("pkid")+")";
							SQLAdapter sql_2_Adapter = new SQLAdapter(sql_2);
							List<Map> sql_2_list = this.getBySqlMapper.findRecords(sql_2_Adapter);
							if(sql_2_list.size()>0){//管理员为这个用户单独设置了权限

								function_map = new HashMap();//清空角色设置的权限
								for(int i = 0;i<sql_2_list.size();i++){
									Map tep = sql_2_list.get(i);
									function_map.put(tep.get("div_id"), tep.get("div_id"));
								}
							}

							session.setAttribute("function_map", function_map);
							//加维护开关权限
							String sql_2_2= "select y.pkid,y.div_id,y.maintain from sys_user_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
									" sys_user_id=(SELECT sys_personal_id from sys_user WHERE pkid="+Login_map.get("pkid")+")";
							SQLAdapter sql_2_2_Adapter = new SQLAdapter(sql_2_2);
							List<Map> sql_2_2_list = this.getBySqlMapper.findRecords(sql_2_2_Adapter);
							if(sql_2_2_list.size()>0){

								function_map = new HashMap();
								for(int i = 0;i<sql_2_2_list.size();i++){
									Map tep = sql_2_2_list.get(i);
									weihu_map.put(tep.get("div_id"), tep.get("maintain"));
								}
							}
							session.setAttribute("weihu_map", weihu_map);
							//浏览数据的权限
							company_map.put("com_type", "帮扶人");
							session.setAttribute("company_map", company_map);

						}
						response.getWriter().print("1");//成功
					}

				}else{
					response.getWriter().print("0");//密码不正确
				}
			}else{
				response.getWriter().print("3");//状态停用
			}

		}else{
			response.getWriter().print("2");//没有此用户
		}
		return null;
	}

	//session获取用户登陆信息
	public ModelAndView getLogin_massage(HttpServletRequest request,HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空

			Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
			Map<String,String> function_map = (Map)session.getAttribute("function_map");//菜单权限表
			Map<String,String> weihu_map = (Map)session.getAttribute("weihu_map");//维护开关
			Map<String,String> company_map = (Map)session.getAttribute("company_map");

			JSONObject Login_map_json = new JSONObject();
			for(String key : Login_map.keySet()){
				Login_map_json.put(key, Login_map.get(key));
			}
			json.put("Login_map", Login_map_json);

			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			json.put("company", company_json);

			JSONObject function_map_json = new JSONObject();
			for(String key : function_map.keySet()){
				function_map_json.put(key, function_map.get(key));
			}
			json.put("function_map", function_map_json);

			JSONObject weihu_map_json = new JSONObject();
			for(String key : weihu_map.keySet()){
				weihu_map_json.put(key, weihu_map.get(key));
			}
			json.put("weihu_map", weihu_map_json);

			JSONObject company_map_json = new JSONObject();
			for(String key : company_map.keySet()){
				company_map_json.put(key, company_map.get(key));
			}
			json.put("company_map", company_map_json);

			response.getWriter().write(json.toString());
		}else{
			response.getWriter().print("weidenglu");
		}
		return null;
	}

	//销毁session
	public ModelAndView login_out(HttpServletRequest request,HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();
		try{
			session.invalidate();
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}


	//更新密码
	public ModelAndView upPassword(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String id = request.getParameter("pkid");//获取用户ID
		String password = request.getParameter("password");//获取密码
		password = Tool.md5(password);

		String people_sql = "update sys_user set col_password='"+password+"' where pkid="+id;
		SQLAdapter Login_Adapter = new SQLAdapter(people_sql);
		try{
			this.getBySqlMapper.updateSelective(Login_Adapter);
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}

	//验证修改密码时输入是否和原密码相同
	public ModelAndView o_password(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String password1 = request.getParameter("val");
		String id = request.getParameter("pkid");

		String sql = "SELECT count(*) FROM sys_user WHERE pkid='" + id + "' and col_password = '" + Tool.md5(password1) + "'";
		SQLAdapter sqlAdapter = new SQLAdapter(sql);
		int resultSize = getBySqlMapper.findrows(sqlAdapter);
		if (resultSize == 0){
			response.getWriter().print("0");
		}else{
			response.getWriter().print("1");
		}
		return null;
	}

	//获取帮扶单位的列表
	public ModelAndView get_bfdw_List(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String search = "";
		String cha_qixian="";
		String cha_bfdw="";
		String cha_ldxm="";
		String cha_lddh="";
		String cha_sbfc="";
		String str="";
		
		String year = request.getParameter("cha_year");//年份
		if( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		
		if(request.getParameter("cha_qixian")!=null&&!request.getParameter("cha_qixian").equals("请选择")){
			cha_qixian = request.getParameter("cha_qixian").trim();
			str += " a1.sys_company_id like '%"+cha_qixian+"%' and";
		}
		if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")	){
			cha_bfdw = request.getParameter("cha_bfdw").trim();
			str += " a1.v1 like '%"+cha_bfdw+"%' and";
		}
		if(request.getParameter("cha_ldxm")!=null&&!request.getParameter("cha_ldxm").equals("")){
			cha_ldxm = request.getParameter("cha_ldxm").trim();
			str += " a1.v3 like '%"+cha_ldxm+"%' and";
		}
		if(request.getParameter("cha_lddh")!=null&&!request.getParameter("cha_lddh").equals("")){
			cha_lddh = request.getParameter("cha_lddh").trim();
			str += " a1.v4 like '%"+cha_lddh+"%' and";
		}
		if(request.getParameter("cha_sbfc")!=null&&!request.getParameter("cha_sbfc").equals("")){
			cha_sbfc = request.getParameter("cha_sbfc").trim();
			str += " a2.com_name like '%"+cha_sbfc+"%' and";
		}

		if(request.getParameter("search")!=null&&!request.getParameter("search").equals("")){
			search = request.getParameter("search").trim();
		}

		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;

		String people_sql="";
		String count_st_sql="";

		HttpSession session = request.getSession();//取session
		Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
		JSONObject company_json = new JSONObject();
		for(String key : company.keySet()){
			company_json.put(key, company.get(key));
		}
		
		String pkid=company_json.get("pkid").toString();//获取用户名称
		if(pkid.equals("4")){
			if(str==""){
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,a2.com_name,a1.v5,a1.sys_company_id,a2.com_f_pkid from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  limit "+number+","+size;
				count_st_sql = "select count(*) from da_company a1";
			}else{
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,a2.com_name,a1.v5,a1.sys_company_id,a2.com_f_pkid from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where "+str.substring(0, str.length()-3)+" limit "+number+","+size;
				count_st_sql = "select count(*) from da_company"+year+" a1 LEFT JOIN sys_company a2 ON a1.v5=a2.pkid where "+str.substring(0, str.length()-3);
			}	
		}else{
			String xian_id=company_json.get("xian_id").toString();//获取用户名称
			if(str==""){
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,a2.com_name,a1.v5,a1.sys_company_id,a2.com_f_pkid from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where sys_company_id="+xian_id+" limit "+number+","+size;
				count_st_sql = "select count(*) from da_company"+year+" a1 where sys_company_id="+xian_id;
			}else{
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,a2.com_name,a1.v5,a1.sys_company_id,a2.com_f_pkid from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where sys_company_id="+xian_id+" and "+str.substring(0, str.length()-3)+" limit "+number+","+size;
				count_st_sql = "select count(*) from da_company"+year+" a1 LEFT JOIN sys_company a2 ON a1.v5=a2.pkid where sys_company_id="+xian_id+" and "+str.substring(0, str.length()-3);
			}	
		}
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);

		SQLAdapter Admin_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Admin_st_List = this.getBySqlMapper.findRecords(Admin_st_Adapter);
		if(Admin_st_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Admin_st_List.size();i++){
				Map Admin_st_map = Admin_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Admin_st_map.keySet()) {
					val.put(key, Admin_st_map.get(key));
				}
				jsa.add(val);
			}
			JSONObject json = new JSONObject();
			json.put("page", page);
			json.put("total", total);
			json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
			response.getWriter().write(json.toString());
		}
		return null;
	}
	//添加帮扶单位
	public ModelAndView addBfdw(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String add_bfdw_mc = request.getParameter("add_bfdw_mc");
		String add_qixian = request.getParameter("add_qixian");
		String add_bfdw_ldxm = request.getParameter("add_bfdw_ldxm");
		String add_bfdw_dz = request.getParameter("add_bfdw_dz");
		String add_bfdw_lddh = request.getParameter("add_bfdw_lddh");
		
		String cha_gcc = "null";

		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
		}
		
		String Sql = "insert into da_company (sys_company_id,v1,v2,v3,v4,v5) values('"+add_qixian+"','"+add_bfdw_mc+"','"+add_bfdw_dz+"','"+add_bfdw_ldxm+"','"+add_bfdw_lddh+"','"+cha_gcc+"')";
		SQLAdapter people_Adapter = new SQLAdapter(Sql);
		try{
			this.getBySqlMapper.insertSelective(people_Adapter);
			
			String hou_sql = "select max(pkid) from da_company order by pkid desc";
			SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
			int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_company',"+da_household_id+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶单位','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			//				e.printStackTrace();
			response.getWriter().write("0");
		}
		return null;
	}

	//修改帮扶单位
	public ModelAndView upBfdw(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String id = request.getParameter("pkid");
		String up_bfdw_mc = request.getParameter("up_bfdw_mc");
		String up_qixian = request.getParameter("up_qixian");
		String up_bfdw_ldxm = request.getParameter("up_bfdw_ldxm");
		String up_bfdw_dz = request.getParameter("up_bfdw_dz");
		String up_bfdw_lddh = request.getParameter("up_bfdw_lddh");
		
		String cha_gcc = "null";
		if(request.getParameter("up_cha_gcc")!=null&&!request.getParameter("up_cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("up_cha_gcc").trim();
		}
		
		String Sql = "update da_company set sys_company_id='"+up_qixian+"', v1='"+up_bfdw_mc+"', v2='"+up_bfdw_dz+"',v3='"+up_bfdw_ldxm+"',v4='"+up_bfdw_lddh+"',v5='"+cha_gcc+"' where pkid="+id;
		SQLAdapter people_Adapter = new SQLAdapter(Sql);
		try{
			this.getBySqlMapper.insertSelective(people_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_company',"+id+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶单位','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			//				e.printStackTrace();
			response.getWriter().write("0");
		}
		return null;
	}
	//删除帮扶单位
	public ModelAndView getDeleteBfdw(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String pkid = request.getParameter("pkid");
		String sql = "DELETE from da_company  WHERE pkid="+pkid;
		SQLAdapter del_sql = new SQLAdapter(sql);
		try{
//			List<Map> Admin_st_List = this.getBySqlMapper.findRecords(del_sql);
			this.getBySqlMapper.deleteSelective(del_sql);
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_company',"+pkid+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶单位','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch(Exception e){
			response.getWriter().write("0");
		}
		return null;
	}
}

