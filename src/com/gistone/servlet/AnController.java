package com.gistone.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;
import com.gistone.util.Tool;
import com.google.common.collect.ObjectArrays;

public class AnController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	//定义地图中的全局参数
	String char_name;
	String char_type;
	String char_standard;
	
	/**
	 * 安卓登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAnLoginx(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("u");
		String password = request.getParameter("p");
		try {
			String people_sql = "select * from sys_user WHERE col_account = '" + username + "'";
			SQLAdapter Login_Adapter = new SQLAdapter(people_sql);
			List<Map> Login = this.getBySqlMapper.findRecords(Login_Adapter);
			if(Login.size()>0){
				Map Login_map = Login.get(0);
				HttpSession session = request.getSession();
				session.setAttribute("Login_map", Login_map);
				if(Login_map.get("account_state").toString().equals("1")){//状态正常
					if(Tool.md5(password).equals(Login_map.get("col_password"))==true){//密码正确
						String a="";
						String b="";
						String c="";
						String d="";
						if(Login_map.get("sys_company_id").toString()==""||Login_map.get("sys_company_id").toString()==null){
							a="";
						}else{
							a=Login_map.get("sys_company_id").toString();
						}
						if(Login_map.get("pkid").toString()==""||Login_map.get("pkid").toString()==null){
							b="";
						}else{
							b=Login_map.get("pkid").toString();
						}
						if(Login_map.get("account_type").toString()==""||Login_map.get("account_type").toString()==null){
							c="";
						}else{
							c=Login_map.get("account_type").toString();
						}
						response.getWriter().write("{\"isError\":\"0\",\"result\":{\"cid\":"+a+",\"uid\":"+b+",\"type\":"+c+"}}");
						
						JSONArray jsonArray =new JSONArray();
						if(Login_map.get("account_type").toString().equals("1")){
							
						}else if(Login_map.get("account_type").toString().equals("2")){}
						response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");
					}
					else{
						response.getWriter().write("{\"isError\":\"2\",\"result\":\"\"}");
					}
				}
			}
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"2\",\"result\":\"\"}");
		}
	
		return null;
	}
	/**
	 * 查看用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getSavePoorController(HttpServletRequest request,HttpServletResponse response) throws IOException{
	
		String uid=request.getParameter("uid");//695
		String cid=request.getParameter("cid");//鄂尔多斯各级行政区划的主键 15724
		String type=request.getParameter("type");//行政单位还是帮扶人 
		String sid=request.getParameter("sid");//帮扶人
		Map map=new HashMap();
		map.put("因病", "1");
		map.put("因残", "2");
		map.put("因学", "3");
		map.put("因灾", "4");
		map.put("缺土地", "5");
		map.put("缺住房", "6");
		map.put("缺水", "7");
		map.put("缺电", "8");
		map.put("缺技术", "9");
		map.put("缺劳动力","10");
		map.put("缺资金", "11");
		map.put("交通条件落后", "12");
		map.put("自身发展动力不足", "13");
		map.put("其他", "14");
		Map criterion_map= new HashMap();
		criterion_map.put("国家级贫困人口", "1");
		criterion_map.put("市级低收入人口", "2");
		Object s[] = map.keySet().toArray();
		Object criterion[] = criterion_map.keySet().toArray();

		JSONArray jsonArray =new JSONArray();
		if(type.toString().equals("1")){//行政单位用户 
			String sql_zong = "select * from sys_company where pkid="+cid;
			SQLAdapter sql_zong_Adapter = new SQLAdapter(sql_zong);
			List<Map> sql_zong_list = this.getBySqlMapper.findRecords(sql_zong_Adapter);
			Map zpong = sql_zong_list.get(0);
			if(zpong.get("com_level").toString().equals("1")){//市级单位
				String shi_sql="SELECT count(*) b,v3 a from da_household GROUP BY v3 ";
				SQLAdapter shi_Adapter=new SQLAdapter(shi_sql);
				List<Map> shi_list=this.getBySqlMapper.findRecords(shi_Adapter);
				for(Map val:shi_list){
					JSONObject object=new JSONObject();
					object.put("a", val.get("a"));
					object.put("b", val.get("b"));
					object.put("c", "1");
					jsonArray.add(object);
				}
				response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
				return null;
			}else if(zpong.get("com_level").toString().equals("2")){//旗县级
				String xc_sql="SELECT COUNT(*) b,v4 a FROM da_household where v3=(select com_name from sys_company where pkid="+cid+") GROUP BY v4";
				SQLAdapter xc_sqlAdapter=new SQLAdapter(xc_sql);
				List<Map> xc_list=this.getBySqlMapper.findRecords(xc_sqlAdapter);
				for(Map val:xc_list){
					JSONObject object=new JSONObject();
					object.put("a",val.get("a"));
					object.put("b",val.get("b"));
					object.put("c","2");
					jsonArray.add(object);
				}
				response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
				return null;
			}else if(zpong.get("com_level").toString().equals("3")){//乡级或者村级
				String xz_sql="SELECT COUNT(*) b,v5 a FROM da_household where v4=(select com_name from sys_company where pkid="+cid+") GROUP BY v5";
				SQLAdapter xz_sqlAdapter=new SQLAdapter(xz_sql);
				List<Map> xz_list=this.getBySqlMapper.findRecords(xz_sqlAdapter);
				for(Map val:xz_list){
					JSONObject object=new JSONObject();
					object.put("a", val.get("a"));
					object.put("b", val.get("b"));
					object.put("c","3");
					jsonArray.add(object);
				}
				
				response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
				return null;
			}else if(zpong.get("com_level").toString().equals("4")){//村级
				String xc_sql="SELECT COUNT(*) b,v5 a FROM da_household where v5=(select com_name from sys_company where pkid="+cid+") and v4=(select com_name from sys_company where pkid=(select com_f_pkid from sys_company where pkid="+cid+"))";
				SQLAdapter xc_sqlAdapter=new SQLAdapter(xc_sql);
				List<Map> xc_list=this.getBySqlMapper.findRecords(xc_sqlAdapter);
				JSONObject object=new JSONObject();
				for(Map val : xc_list){
					if("".equals(val.get("a"))||val.get("a")==null){
						object.put("a",this.getBySqlMapper.findRecords(sql_zong_Adapter).get(0).get("com_name"));
					}else{
						object.put("a",val.get("a"));
					}
					
					object.put("b",val.get("b"));
					object.put("c","4");
					jsonArray.add(object);
				}
			
				
//				//乡级或者村级
//				String sql_qx = "SELECT * FROM da_household where v5=(select com_name from sys_company where pkid="+cid+")";
//				SQLAdapter qx_Adapter = new SQLAdapter(sql_qx);
//				List<Map> qx_list = this.getBySqlMapper.findRecords(qx_Adapter);
//				for (int i = 0; i < qx_list.size(); i++) {
//					JSONObject obj = new JSONObject();
//					obj.put("name", qx_list.get(i).get("v6"));// 贫困户主
//					obj.put("pid", qx_list.get(i).get("pkid"));// 贫困户主id
//					for (int j = 0; j < criterion_map.size(); j++) {
//						if ("".equals(qx_list.get(i).get("sys_standard"))|| qx_list.get(i).get("sys_standard") == null||qx_list.get(i).get("sys_standard").toString().equals("")) {
//							obj.put("criterion", "");// 标准
//						} else {
//							if (qx_list.get(i)
//									.get("sys_standard").toString()
//									.equals(criterion[j])) {
//								obj.put("criterion", criterion_map.get(criterion[j]));// 标准
//							}
//						}
//					}
//						if ("".equals(qx_list.get(i).get("v9"))|| qx_list.get(i).get("v9") == null) {
//							obj.put("populace", "");// 人数
//						} else {
//							obj.put("populace",
//									qx_list.get(i).get("v9"));// 人数
//						}
//						if ("".equals(qx_list.get(i).get("v23"))|| qx_list.get(i).get("v23") == null||qx_list.get(i).get("v23").toString().equals("")) {
//							obj.put("reason", "15");
//						}else{
//							
//							for (int k = 0; k < map.size(); k++) {
//								String zpyy="";
//								String a=qx_list.get(i).get("v23").toString();
//								if(a.indexOf(",")!=-1){
//									String[] str= qx_list.get(i).get("v23").toString().split(",");
//									zpyy=str[1];
//									if (zpyy.equals(s[k])) {
//										obj.put("reason", map.get(s[k]).toString());// 致贫原因
//									}
//								}else{
//									zpyy=qx_list.get(i).get("v23").toString();
//									if (zpyy.equals(s[k])) {
//										obj.put("reason", map.get(s[k]).toString());// 致贫原因
//									}
//								}
//							}
//						}
//						
//					jsonArray.add(obj);
//				}
//				response.getWriter().write("{\"isError\":\"0\",\"result\":{\"count\":"+object+",\"hu\":"+jsonArray.toString()+"}}");
				
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
			
			return null;
		}else if(type.toString().equals("2")){//帮扶人 
			String bfr_sql="SELECT da_household_id from sys_personal_household_many where sys_personal_id="+sid;
			SQLAdapter bfr_Adapter = new SQLAdapter(bfr_sql);
			List<Map> bfr_list = this.getBySqlMapper.findRecords(bfr_Adapter);
			for(int m=0;m<bfr_list.size();m++){
				String bfr_pk_sql="SELECT * FROM da_household where pkid="+bfr_list.get(m).get("da_household_id");
				SQLAdapter bfr_pk_Adapter = new SQLAdapter(bfr_pk_sql);
				List<Map> bfr_pk_list = this.getBySqlMapper.findRecords(bfr_pk_Adapter);
				for(int i=0;i<bfr_pk_list.size();i++){

					JSONObject obj = new JSONObject();
					obj.put("name", bfr_pk_list.get(i).get("v6"));// 贫困户主
					obj.put("pid", bfr_pk_list.get(i).get("pkid"));// 贫困户主id
					for (int j = 0; j < criterion_map.size(); j++) {
						if ("".equals(bfr_pk_list.get(i).get("sys_standard"))|| bfr_pk_list.get(i).get("sys_standard") == null) {
							obj.put("criterion", "");// 标准
						} else {
							if (bfr_pk_list.get(i)
									.get("sys_standard").toString()
									.equals(criterion[j])) {
								obj.put("criterion", criterion_map
										.get(criterion[j]));// 标准
							}
						}
					}
						if ("".equals(bfr_pk_list.get(i).get("v9"))|| bfr_pk_list.get(i).get("v9") == null) {
							obj.put("populace", "");// 人数
						} else {
							obj.put("populace",
									bfr_pk_list.get(i).get("v9"));// 人数
						}

						for (int k = 0; k < map.size(); k++) {
							if ("".equals(bfr_pk_list.get(i).get("v23"))|| bfr_pk_list.get(i).get("v23") == null) {
								obj.put("reason", "15");// 致贫原因
							} else {
								if (bfr_pk_list.get(i).get("v23").toString().equals(s[k])) {
									obj.put("reason", map.get(s[k]).toString());// 致贫原因
								}
							}
					}
					jsonArray.add(obj);
				
				}
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+",\"k\":"+uid.toString()+"}");
			return null;
		}
		else{
		}
		return null;
	}
	/**
	 * 二三级菜单
	 * @param request 
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getNameSaveHousehould(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String name=request.getParameter("name");//旗县、乡镇、村
		String type=request.getParameter("type");//判断是旗县还是乡镇还是村   
		String name1=request.getParameter("name1");
		String cid=request.getParameter("cid");
		JSONArray jsonArray=new JSONArray();
		if(type.toString().equals("3")){//旗县
			String sql="SELECT COUNT(*) b,v4 a FROM da_household where v3='"+name+"' GROUP BY v4";
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			for(Map val:list){
				JSONObject object=new JSONObject();
				object.put("a",val.get("a"));
				object.put("b",val.get("b"));
				object.put("c","2");
				jsonArray.add(object);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
		}else if(type.toString().equals("4")){//乡镇
			String sql="SELECT COUNT(*) b,v5 a FROM da_household where v4='"+name+"' GROUP BY v5";
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			for(Map val:list){
				JSONObject object=new JSONObject();
				object.put("a",val.get("a"));
				object.put("b",val.get("b"));
				object.put("c","3");
				jsonArray.add(object);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
		}else if(type.toString().equals("5")){//村
			JSONObject object=new JSONObject();
			String sql="SELECT COUNT(*) b,v5 a FROM da_household where v5='"+name+"'";
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			for(Map val:list){
				object.put("a",val.get("a"));
				object.put("b",val.get("b"));
				object.put("c","4");
			}
			
			response.getWriter().write("{\"isError\":\"0\",\"result\":{\"count\":"+object+",\"hu\":"+jsonArray.toString()+"}}");
		}
		return null;
	}
	/**
	 * 格根据村显示贫困户
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public  ModelAndView getShowPoor(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
		Map map=new HashMap();
		map.put("因病", "1");
		map.put("因残", "2");
		map.put("因学", "3");
		map.put("因灾", "4");
		map.put("缺土地", "5");
		map.put("缺住房", "6");
		map.put("缺水", "7");
		map.put("缺电", "8");
		map.put("缺技术", "9");
		map.put("缺劳动力", "10");
		map.put("缺资金", "11");
		map.put("交通条件落后", "12");
		map.put("自身发展动力不足", "13");
		map.put("其他", "14");
		Map criterion_map= new HashMap();
		criterion_map.put("国家级贫困人口", "1");
		criterion_map.put("市级低收入人口", "2");
		Object s[] = map.keySet().toArray();
		Object criterion[] = criterion_map.keySet().toArray();
		String name1=request.getParameter("tname");//镇的名称
		String name=request.getParameter("name");//存的名称
		
		if("".equals(name1)||name1==null){
			String cid=request.getParameter("cid");
			String cha_sql="select com_name,com_level from sys_company where pkid=(select com_f_pkid from sys_company where pkid="+cid+")";
			SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
			List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
			if(cha_list.size()>0){
				if("2".equals(cha_list.get(0).get("com_name").toString())){
					
				}
				name1=cha_list.get(0).get("com_name").toString();
			}
		}else if ("town".equals(name1)){
			String cid=request.getParameter("cid");
			String cha_sql="select com_name from sys_company where pkid="+cid;
			SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
			List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
			if(cha_list.size()>0){
				if("2".equals(cha_list.get(0).get("com_name").toString())){
					
				}
				name1=cha_list.get(0).get("com_name").toString();
			}
		}
		 String poor_sql = "SELECT v6,pkid,init_flag,sys_standard,v9,v23 from da_household where v5='"+name+"' and v4='"+name1+"'";
		 String cha_sql = " select sys_standard,init_flag, sum(v9) ren,count(*) num from( "+poor_sql+") aa where init_flag='国家级贫困人口' ";
		 String cha_sql1 = " select sys_standard,init_flag, sum(v9) ren,count(*) num from( "+poor_sql+") aa where init_flag='市级低收入人口' ";
		 SQLAdapter cha_sqlAdapter = new SQLAdapter (cha_sql);
		 SQLAdapter cha_sqlAdapter1 = new SQLAdapter (cha_sql1);
		 JSONArray cha_json = new JSONArray();
		 List<Map> cha_list = this.getBySqlMapper.findRecords(cha_sqlAdapter);
		 List<Map> cha_list1 = this.getBySqlMapper.findRecords(cha_sqlAdapter1);
		 
		 JSONArray jsonArray=new JSONArray();
		SQLAdapter poor_Adapter = new SQLAdapter(poor_sql);
		String bfdwSql = "";
		List<Map> poor_list = this.getBySqlMapper.findRecords(poor_Adapter);
		for (int i = 0; i < poor_list.size(); i++) {
			JSONObject obj = new JSONObject();
			obj.put("name", poor_list.get(i).get("v6"));// 贫困户主
			obj.put("pid", poor_list.get(i).get("pkid"));// 贫困户主id
			//查询帮扶干部、单位、电话开始
			bfdwSql="select sp.col_name,sp.col_post,sp.telephone from (select * from sys_personal_household_many where da_household_id='"+poor_list.get(i).get("pkid")+"' )sph left join sys_personal sp on sph.sys_personal_id=sp.pkid ";
			SQLAdapter bfdw_sqlAdapter = new SQLAdapter (bfdwSql);
			List<Map> bfdw_list = this.getBySqlMapper.findRecords(bfdw_sqlAdapter);
			JSONArray jsonBfdwArray=new JSONArray();//帮扶干部信息
			if(bfdw_list.size()>0){
				for(int b=0;b<bfdw_list.size();b++){
					JSONObject ob = new JSONObject();
					if ("".equals(bfdw_list.get(b).get("col_name"))|| bfdw_list.get(b).get("col_name") == null) {
						ob.put("name", "");// 帮扶干部名称
					} else {
						ob.put("name",bfdw_list.get(b).get("col_name"));// 帮扶干部名称
					}
					if ("".equals(bfdw_list.get(b).get("col_post"))|| bfdw_list.get(b).get("col_post") == null) {
						ob.put("dep", "");// 干部所在单位
					} else {
						ob.put("dep",bfdw_list.get(b).get("col_post"));// 干部所在单位
					}
					if ("".equals(bfdw_list.get(b).get("telephone"))|| bfdw_list.get(b).get("telephone") == null) {
						ob.put("telephone", "");// 帮扶干部名称
					} else {
						ob.put("telephone",bfdw_list.get(b).get("telephone"));// 帮扶干部名称
					}
					jsonBfdwArray.add(ob);
				}
			}
			obj.put("bfgb", jsonBfdwArray);
			//查询帮扶干部、单位、电话结束
			
			for (int j = 0; j < criterion_map.size(); j++) {
				// 原始标准
				if ("".equals(poor_list.get(i).get("sys_standard"))|| poor_list.get(i).get("sys_standard") == null) {
					obj.put("criterion", "2");
				} else {
					if (poor_list.get(i).get("sys_standard").toString().equals(criterion[j])) {
						obj.put("criterion", criterion_map.get(criterion[j]));// 标准
					}
				}
				//现在标准
				if ("".equals(poor_list.get(i).get("init_flag"))|| poor_list.get(i).get("init_flag") == null) {
					obj.put("nowcriterion", "2");
				} else {
					if (poor_list.get(i).get("init_flag").toString().equals(criterion[j])) {
						obj.put("nowcriterion", criterion_map.get(criterion[j]));// 标准
					}
				}
			}
			if ("".equals(poor_list.get(i).get("v9"))|| poor_list.get(i).get("v9") == null) {
				obj.put("populace", "");// 人数
			} else {
				obj.put("populace",poor_list.get(i).get("v9"));// 人数
			}

			if ("".equals(poor_list.get(i).get("v23"))|| poor_list.get(i).get("v23") == null) {
				obj.put("reason", "15");
			}else{
				for (int k = 0; k < map.size(); k++) {
					String zpyy="";
					String a=poor_list.get(i).get("v23").toString();
					if(a.indexOf(",")!=-1){
						String[] str= poor_list.get(i).get("v23").toString().split(",");
//						zpyy=str[1];
						if (zpyy.equals(s[k])) {
							obj.put("reason", map.get(s[k]).toString());// 致贫原因
						}
					}else{
						zpyy=poor_list.get(i).get("v23").toString();
						if (zpyy.equals(s[k])) {
							obj.put("reason", map.get(s[k]).toString());// 致贫原因  
						}
					}
				}
			}
			jsonArray.add(obj);
		}
		response.getWriter().write("{\"isError\":\"0\",\"result\":{\"count\":{\"gh\":"+(cha_list.get(0).get("num")==null?0:cha_list.get(0).get("num"))+",\"gs\":"+(cha_list.get(0).get("ren")==null?0:cha_list.get(0).get("ren"))+",\"sh\":"+(cha_list1.get(0).get("num")==null?0:cha_list1.get(0).get("num"))+",\"sc\":"+(cha_list1.get(0).get("ren")==null?0:cha_list1.get(0).get("ren"))+"},\"basic\":"+jsonArray.toString()+"}}");
		 return null;
	}
	/**
	 * 贫困户的详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getPoorDetailed(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pid");
		try {
			String sql="SELECT basic_address,v3,v4,v5,v21,v26,v27,v23,v33,sys_standard,v29,v30,v31,V6,pkid,v25,v8,v22,v11,v7,entry_year,init_flag,pic_path, ROUND((dqsz-dqzc)/v9,2) bfq,ROUND((dqszh-dqzch)/v9,2) bfh FROM  da_household a LEFT JOIN  "+
					"(select pic_path,pic_pkid from da_pic WHERE pic_type='4' )c ON a.pkid=c.pic_pkid LEFT JOIN  "+
					"(select v39 dqsz,da_household_id FROM da_current_income)d ON a.pkid=d.da_household_id LEFT JOIN  "+
					"(select v31 dqzc,da_household_id FROM da_current_expenditure ) e ON a.pkid=e.da_household_id LEFT JOIN"+
					"(select v39 dqszh,da_household_id from da_helpback_income)f ON a.pkid=f.da_household_id LEFT JOIN"+
					"(SELECT v31 dqzch,da_household_id FROM da_helpback_expenditure)g ON a.pkid =g.da_household_id LEFT JOIN "+
					"(SELECT  basic_address ,da_household_id from da_household_basic) h ON a.pkid=h.da_household_id  WHERE a.pkid="+pkid+"";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray =new JSONArray();
		JSONObject obj=new JSONObject ();
		obj.put("pid", list.get(0).get("pkid"));
//		if("".equals(list.get(0).get("basic_address"))||list.get(0).get("basic_address")==null){
//			obj.put("v1","");
//		}else{
//			obj.put("v1", list.get(0).get("basic_address"));
//		}
		
		obj.put("v1", "内蒙古自治区 "+"鄂尔多斯市 "+ list.get(0).get("v3")+" "+ list.get(0).get("v4")+" "+ list.get(0).get("v5")+"");
		if("".equals(list.get(0).get("entry_year"))||list.get(0).get("entry_year")==null){
			obj.put("pyear", "2017");//贫困户年份
		}else{
			obj.put("pyear",list.get(0).get("entry_year"));//贫困户年份
		}
		
		if("".equals(list.get(0).get("v21"))||list.get(0).get("v21")==null){
			obj.put("tpsx", "");//脱贫属性
		}else{
			obj.put("tpsx",list.get(0).get("v21"));//脱贫属性
		}
		
		if("".equals(list.get(0).get("v25"))||list.get(0).get("v25")==null){
			obj.put("v25", "");//联系电话
		}else{
			obj.put("v25",list.get(0).get("v25"));//联系电话
		}
		if("".equals(list.get(0).get("v26"))||list.get(0).get("v26")==null){
			obj.put("v26","");//开户银行
		}else{
			obj.put("v26",list.get(0).get("v26"));//开户银行
		}
		if("".equals(list.get(0).get("v27"))||list.get(0).get("v27")==null){
			obj.put("v27","");//银行账号
		}else{
			obj.put("v27",list.get(0).get("v27"));//银行账号
		}
		if("".equals(list.get(0).get("sys_standard"))||list.get(0).get("sys_standard")==null){
			obj.put("a", "");//识别标准  原始
		}else{
			obj.put("a", list.get(0).get("sys_standard"));//识别标准
		}
		if("".equals(list.get(0).get("init_flag"))||list.get(0).get("init_flag")==null){
			obj.put("aa", "");//贫困户标准  退出  变化的
		}else{
			obj.put("aa",list.get(0).get("init_flag"));//贫困户标准
		}
		if("".equals(list.get(0).get("pic_path"))||list.get(0).get("pic_path")==null){
			obj.put("b", "");//图片
		}else{
			String str=(String) list.get(0).get("pic_path");
			obj.put("b", str.substring(6));//图片
		}
		if("".equals(list.get(0).get("v22"))||list.get(0).get("v22")==null){
			obj.put("v22", "");//贫困户属性
		}else{
			obj.put("v22", list.get(0).get("v22"));//贫困户属性
		}
		if("".equals(list.get(0).get("v29"))||list.get(0).get("v29")==null){
			obj.put("v29", "");//是否军烈属
		}else{
			obj.put("v29", list.get(0).get("v29"));//是否军烈属
		}
		if("".equals(list.get(0).get("bfq"))||list.get(0).get("bfq")==null){
			obj.put("bfq", "");//帮扶前人均收入
		}else{
			obj.put("bfq", list.get(0).get("bfq"));//帮扶前人均收入
		}
		if("".equals(list.get(0).get("bfh"))||list.get(0).get("bfh")==null){
			obj.put("bfh", "");//帮扶后人均收入
		}else{
			obj.put("bfh", list.get(0).get("bfh"));//帮扶后人均收入
		}
		if("".equals(list.get(0).get("v30"))||list.get(0).get("v30")==null){
			obj.put("v30", "");//是否独生子女
		}else{
			obj.put("v30", list.get(0).get("v30"));//是否独生子女
		}
		if("".equals(list.get(0).get("v31"))||list.get(0).get("v31")==null){
			obj.put("v31", "");//是否双女户
		}else{
			obj.put("v31", list.get(0).get("v31"));//是否双女户
		}
		if("".equals(list.get(0).get("v33"))||list.get(0).get("v33")==null){
			obj.put("v33", "");//其他致贫原因
		}else{
			obj.put("v33", list.get(0).get("v33"));//其他致贫原因
		}
		if("".equals(list.get(0).get("v23"))||list.get(0).get("v23")==null){
			obj.put("v23", "");//主要致贫原因
		}else{
			obj.put("v23", list.get(0).get("v23"));//主要致贫原因
		}
		jsonArray.add(obj);
		response.getWriter().write("{\"isError\":\"0\",\"result\":"+obj.toString()+"}");
		response.getWriter().close();
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
			response.getWriter().close();
		}
		return null;
	}
	/**
	 * 查看家庭成员
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getSaveFamily(HttpServletRequest request,HttpServletResponse response) throws IOException{ 
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pid");
		try {
			JSONArray jsonArray =new JSONArray();
			JSONArray jsonArray1 =new JSONArray();
			JSONObject obj=new JSONObject ();
			JSONObject obj1=new JSONObject ();
			String hz_sql="SELECT pkid,v6,v7,v8,v10,v11,v12,v13,v14,v15,v16,v17,v32,v19,v28,pic_path from  da_household a  LEFT JOIN "+
						" (SELECT pic_path,pic_pkid FROM da_pic where pic_type=4)b ON a.pkid=b.pic_pkid where a.pkid="+pkid;
			SQLAdapter sqlAdaPter_hz=new SQLAdapter(hz_sql);
			List<Map> hz_list=getBySqlMapper.findRecords(sqlAdaPter_hz);
			if("".equals(hz_list.get(0).get("v6"))||hz_list.get(0).get("v6")==null){
				obj1.put("v6","");//姓名
			}else{
				obj1.put("v6", hz_list.get(0).get("v6"));//姓名
			}
			if("".equals(hz_list.get(0).get("v7"))||hz_list.get(0).get("v7")==null){
				obj1.put("v7","");//性别
			}else{
				obj1.put("v7", hz_list.get(0).get("v7"));//性别
			}
			if("".equals(hz_list.get(0).get("v8"))||hz_list.get(0).get("v8")==null){
				obj1.put("v8","");//证件号码
			}else{
				obj1.put("v8", hz_list.get(0).get("v8"));//证件号码
			}
			if("".equals(hz_list.get(0).get("v10"))||hz_list.get(0).get("v10")==null){
				obj1.put("v10","");//与户主关系
			}else{
				obj1.put("v10", hz_list.get(0).get("v10"));//与户主的关系
			}
			if("".equals(hz_list.get(0).get("v11"))||hz_list.get(0).get("v11")==null){
				obj1.put("v11","");//民族
			}else{
				obj1.put("v11", hz_list.get(0).get("v11"));//民族
			}
			if("".equals(hz_list.get(0).get("v28"))||hz_list.get(0).get("v28")==null){
				obj1.put("v28","");//政治面貌
			}else{
				obj1.put("v28", hz_list.get(0).get("v28"));//政治面貌
			}
			if("".equals(hz_list.get(0).get("v12"))||hz_list.get(0).get("v12")==null){
				obj1.put("v12","");//文化程度
			}else{
				obj1.put("v12", hz_list.get(0).get("v12"));//文化程度
			}
			if("".equals(hz_list.get(0).get("v13"))||hz_list.get(0).get("v13")==null){
				obj1.put("v13","");//在校生情况
			}else{
				obj1.put("v13", hz_list.get(0).get("v13"));//在校生情况
			}
			if("".equals(hz_list.get(0).get("v14"))||hz_list.get(0).get("v14")==null){
				obj1.put("v14","");//健康状态
			}else{
				obj1.put("v14", hz_list.get(0).get("v14"));//健康状态
			}
			if("".equals(hz_list.get(0).get("v15"))||hz_list.get(0).get("v15")==null){
				obj1.put("v15","");//劳动技能
			}else{
				obj1.put("v15", hz_list.get(0).get("v15"));//劳动技能
			}
			if("".equals(hz_list.get(0).get("v16"))||hz_list.get(0).get("v16")==null){
				obj1.put("v16","");//务工情况
			}else{
				obj1.put("v16", hz_list.get(0).get("v16"));//务工情况
			}
			if("".equals(hz_list.get(0).get("v17"))||hz_list.get(0).get("v17")==null){
				obj1.put("v17","");//务工时间
			}else{
				obj1.put("v17", hz_list.get(0).get("v17"));//务工时间
			}
			if("".equals(hz_list.get(0).get("v32"))||hz_list.get(0).get("v32")==null){
				obj1.put("v32","");//是否现役军人
			}else{
				obj1.put("v32", hz_list.get(0).get("v32"));//是否现役军人
			}
			if("".equals(hz_list.get(0).get("v19"))||hz_list.get(0).get("v19")==null){
				obj1.put("v19","");//是否参加大病医疗保险
			}else{
				obj1.put("v19", hz_list.get(0).get("v19"));//是否参加大病医疗保险
			}
			if("".equals(hz_list.get(0).get("pic_path"))||hz_list.get(0).get("pic_path")==null){
				obj1.put("a","");//头像
			}else{
				String str=(String) hz_list.get(0).get("pic_path");
				obj1.put("a", str.substring(6));//头像
			}
			obj1.put("pid", hz_list.get(0).get("pkid"));
			jsonArray.add(obj1);
			
			String sql="SELECT pkid,v6,v7,v8,v10,v11,v12,v13,v14,v15,v16,v17,v19,v28,v32,pic_path from  da_member a  LEFT JOIN " +
					" (SELECT pic_path,pic_pkid FROM da_pic where pic_type=5)b ON a.pkid=b.pic_pkid where a.da_household_id='"+pkid+"'";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
			for(int i=0;i<list.size();i++){
				if("".equals(list.get(i).get("v6"))||list.get(i).get("v6")==null){
					obj.put("v6","");//姓名
				}else{
					obj.put("v6", list.get(i).get("v6"));//姓名
				}
				if("".equals(list.get(i).get("v7"))||list.get(i).get("v7")==null){
					obj.put("v7","");//性别
				}else{
					obj.put("v7", list.get(i).get("v7"));//性别
				}
				if("".equals(list.get(i).get("v8"))||list.get(i).get("v8")==null){
					obj.put("v8","");//证件号码
				}else{
					obj.put("v8", list.get(i).get("v8"));//证件号码
				}
				if("".equals(list.get(i).get("v10"))||list.get(i).get("v10")==null){
					obj.put("v10","");//与户主关系
				}else{
					obj.put("v10", list.get(i).get("v10"));//与户主的关系
				}
				if("".equals(list.get(i).get("v11"))||list.get(i).get("v11")==null){
					obj.put("v11","");//民族
				}else{
					obj.put("v11", list.get(i).get("v11"));//民族
				}
				if("".equals(list.get(i).get("v28"))||list.get(i).get("v28")==null){
					obj.put("v28","");//政治面貌
				}else{
					obj.put("v28", list.get(i).get("v28"));//政治面貌
				}
				if("".equals(list.get(i).get("v12"))||list.get(i).get("v12")==null){
					obj.put("v12","");//文化程度
				}else{
					obj.put("v12", list.get(i).get("v12"));//文化程度
				}
				if("".equals(list.get(i).get("v13"))||list.get(i).get("v13")==null){
					obj.put("v13","");//在校生情况
				}else{
					obj.put("v13", list.get(i).get("v13"));//在校生情况
				}
				if("".equals(list.get(i).get("v14"))||list.get(i).get("v14")==null){
					obj.put("v14","");//健康状态
				}else{
					obj.put("v14", list.get(i).get("v14"));//健康状态
				}
				if("".equals(list.get(i).get("v15"))||list.get(i).get("v15")==null){
					obj.put("v15","");//劳动技能
				}else{
					obj.put("v15", list.get(i).get("v15"));//劳动技能
				}
				if("".equals(list.get(i).get("v16"))||list.get(i).get("v16")==null){
					obj.put("v16","");//务工情况
				}else{
					obj.put("v16", list.get(i).get("v16"));//务工情况
				}
				if("".equals(list.get(i).get("v17"))||list.get(i).get("v17")==null){
					obj.put("v17","");//务工时间
				}else{
					obj.put("v17", list.get(i).get("v17"));//务工时间
				}
				if("".equals(list.get(i).get("v32"))||list.get(i).get("v32")==null){
					obj.put("v32","");//是否现役军人
				}else{
					obj.put("v32", list.get(i).get("v32"));//是否现役军人
				}
				if("".equals(list.get(i).get("v19"))||list.get(i).get("v19")==null){
					obj.put("v19","");//是否参加大病医疗保险
				}else{
					obj.put("v19", list.get(i).get("v19"));//是否参加大病医疗保险
				}
				if("".equals(list.get(i).get("pic_path"))||list.get(i).get("pic_path")==null){
					obj.put("a","");//姓名
				}else{
					String strt=list.get(i).get("pic_path").toString();
					
					obj.put("a", strt.substring(6));//姓名
				}
				obj.put("pid", list.get(i).get("pkid"));//姓名
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
			} catch (Exception e) {
				response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
			}
		
		return null;
	}
	/**
	 * 查看走访记录情况
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveVisit(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
//		String pkid=request.getParameter("pid");
		String poor_id=request.getParameter("pid");
		try {
			String sql="SELECT v1,v2,v3,group_concat(SUBSTR(pic_path,7) order by pic_path separator ',') pic_path,pkid FROM da_help_visit a LEFT JOIN ("+
					"SELECT pic_path,pic_pkid FROM da_pic WHERE pic_type='2' ) b "+
					"ON a.pkid=b.pic_pkid  where da_household_id="+poor_id+" GROUP BY pkid  ORDER BY v1 DESC" ;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray =new JSONArray();
		
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("a",val.get("pkid")==null?"-":val.get("pkid"));
			if("".equals(val.get("v1"))||val.get("v1")==null){
				obj.put("b", "");//走访时间
			}else{
				obj.put("b", val.get("v1")==null?"-":val.get("v1"));//走访时间
			}
			if(val.get("v3")==null||"".equals(val.get("v3"))){
				obj.put("c","");//走访情况记录
			}else{
				obj.put("c",val.get("v3")==null?"-":val.get("v3"));//走访情况记录
			}
			if("".equals(val.get("pic_path"))||val.get("pic_path")==null){
				obj.put("d","");//走访图片
			}else{
				String path=val.get("pic_path").toString();
				obj.put("d",val.get("pic_path"));//走访图片
			}
			if("".equals(val.get("v2"))||val.get("v2")==null){
				obj.put("e","");
			}else{
				obj.put("e", val.get("v2")==null?"-":val.get("v2"));
			}
			jsonArray.add(obj);
		}
		response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
		response.getWriter().close();
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");
			response.getWriter().close();
		}
		
		
		return null;
	}
	/**
	 * 添加走访情况
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getAddVisitController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String da_household_id=request.getParameter("pid");//贫困户id
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd"); 
		String v=simpleDate.format(new Date());
		String v2=request.getParameter("name");//帮扶干部
		String v3=request.getParameter("record");//走访情况记录-
		String img=request.getParameter("imgname");//图片的名称
		String []iname=img.split(",");//多张图片名称用逗号隔开放入数组中
		String size=request.getParameter("length");//图片大小
		String []imgsize=size.split(",");
		String phone=request.getParameter("phone");
		if(da_household_id==null){
			da_household_id="";
		}
		if(v2==null){
			v2="";
		}
		if(v3==null){
			v3="";
		}
		try {
			//添加走访记录
			if(img==""||img==null){
				
			}else{
				String hql="insert into da_help_visit(da_household_id,v1,v2,v3)"+
						" VALUES('"+da_household_id+"','"+simpleDate.format(new Date())+"','"+v2+"','"+v3+"')";
				JSONArray jsonArray =new JSONArray();
				SQLAdapter hqlAdapter =new SQLAdapter(hql);
				this.getBySqlMapper.findRecords(hqlAdapter);
				String cha_sql="select pkid from da_help_visit where v1='"+v+"' and v3= '"+v3+"' and v2='"+v2+"' and da_household_id="+da_household_id;
				SQLAdapter chaAdapter =new SQLAdapter(cha_sql);
				List<Map> list=this.getBySqlMapper.findRecords(chaAdapter);
				if(list.size()>0){
//					Integer main=(Integer) getBySqlMapper.findRecords(chaAdapter).get(0).get("pkid");    
					Map limap = list.get(0);
					 if(img==""||img==null){
						 
					 }else{
						 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); 
						 String saveUrl = "/assa/attached/2/"+df.format(new Date())+"/";
						 for(int i=0;i<iname.length;i++){
							 String sql="INSERT INTO da_pic(pic_type,pic_pkid,pic_path,pic_size,pic_format) VALUES"+
										"('2','"+limap.get("pkid")+"','"+saveUrl+iname[i]+".jpg"+"','"+imgsize[i]+"','jpg')";
								SQLAdapter sqlAdapter =new SQLAdapter(sql);
								this.getBySqlMapper.findRecords(sqlAdapter);
						 }
					 }
						//添加记录表
						String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_time,record_mou_1,record_mou_2)"+
								" VALUES ('da_household','"+da_household_id+"','添加','1','"+phone+"','"+v+"','基本信息','走访记录')";
						SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
						this.getBySqlMapper.findRecords(hqlAdapter1);
				}
				//添加走访记录图片
				response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
				response.getWriter().close();
			}
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"resulr\":\"\"}");
			response.getWriter().close();
		}
		return null;
	}
	/**
	 * 上传走访记录图片
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getAddZfPhoto(HttpServletRequest request,HttpServletResponse response){//获取磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获取文件需要上传到的路径
		String savePath = request.getServletContext().getRealPath("/")+ "attached\\2\\"; 
		 // 文件保存目录URL  
        String saveUrl = request.getContextPath() + "/attached/2/"; 
        
		// 检查目录  
        File uploadDir = new File(savePath);  
        if (!uploadDir.isDirectory()) {  
        	//response.getWriter().write(getError("上传目录不存在。"));
        	if(!uploadDir.exists()){
        		uploadDir.mkdirs();
        	}
           // return null;
        }  
        // 检查目录写权限  
        if (!uploadDir.canWrite()) {  
        	//response.getWriter().write(getError("上传目录没有写权限。"));  
            return null;
        }
        //创建文件夹
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
        String ymd = sdf.format(new Date());  
        savePath += ymd + "\\";  
        saveUrl += ymd + "/";
        File dirFile = new File(savePath);  
        if (!dirFile.exists()) {  
            dirFile.mkdirs();  
        }
        
        //如果没有以下两行设置的话，上传大的文件，会占用很多内存
        //设置暂时存放的存储室，这个存储室，可以和最终存储文件的目录不同
        /**
         * 原理 它是先存储 暂时存储室，然后再真正写到对应的目录的硬盘上，按理来说 当上传一个文件时，其实是上传了两份，第一个是以。tem格式的
         * 然后再将其真正写到 对应目录的硬盘上  
         */
        
        factory.setRepository(new File(savePath));
        //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024*1024);
        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        try{
        	List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
        	for(FileItem item : list){
        		//获取表单的属性名字
        		String name = item.getFieldName();
        		String img=item.getName();
//        		long length=item.getSize();
//        		String size=Long.toString(length);
//        		String sql="update da_pic set pic_size='"+size+"' where pic_type='2' and ";
        		//如果获取的 表单信息是普通的文本信息
        		if(item.isFormField()){
        			//获取用户具体输入的字符串，因为表单提交过来的是字符串类型  
        			String value = item.getString();
        			
        			request.setAttribute(name, value);
        		}else{
        			//对于传入的非简单的字符串进行处理，比如说二进制的图片，文件
        			
        			//获取路径名
        			String value = item.getName();
        			//索引到最后一个反斜杠
        			int start = value.lastIndexOf("\\");
        			//截取 上传文件的 字符串名字，加1是 去掉反斜杠
        			String fileName = value.substring(start + 1);
        			
        			// 检查扩展名  
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); 
                    
        			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
                    String newFileName = img;
        			
        			request.setAttribute(name, newFileName);
        			//真正写到磁盘上
        			item.write(new File(savePath, newFileName));
        			
		        		}
		        	}
		        }catch(FileUploadException e){
		        	e.printStackTrace();
		        }catch(Exception e){
		        	
		        }
		return null;
		}
	/**
	 * 查看帮扶计划	
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getSaveBfjhController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String da_household_id=request.getParameter("pid");
		try {
			String sql="select * from da_help_info where da_household_id="+da_household_id;
			JSONArray jsonArray=new JSONArray();
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list =this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject obj=new JSONObject();
			if(null==list||list.size()==0){
				response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");
			}else{
				for(Map val:list){
					if("".equals( val.get("v1"))|| val.get("v1")==null){
						obj.put("a","");//帮扶目标
					}else{
						obj.put("a", val.get("v1")==null?"-":val.get("v1"));//帮扶目标
					}
					if("".equals(val.get("v2"))||val.get("v2")==null){
						obj.put("b","");//帮扶时限
					}else{
						obj.put("b", val.get("v2")==null?"-":val.get("v2"));//帮扶时限
					}
					if("".equals(val.get("v3"))||val.get("v3")==null){
						obj.put("c","");//帮扶计划
					}else{
						obj.put("c", val.get("v3")==null?"-":val.get("v3"));//帮扶计划
					}
					obj.put("d", val.get("pkid")==null?"-":val.get("pkid"));//
				jsonArray.add(obj);
			}
				response.getWriter().write("{\"isError\":\"0\",\"result\":"+obj.toString()+"}");
				
			}
		
			response.getWriter().close();
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
		}
		
		return null;
	}
	/**
	 * 查看台账脱贫计划
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getTzBfjh(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String num=request.getParameter("num");//序号、脱贫指标
		int a = Integer.parseInt(num); 
		String da_household_id=request.getParameter("pid");//贫困户id
		JSONArray jsonArray=new JSONArray();
		JSONObject obj=new JSONObject();
		try {
			String sql="select v"+(3*a-1)+",v"+(3*a)+",v"+(3*a+1)+" v1 from da_help_plan where da_household_id="+da_household_id+" ORDER BY v1 ASC";
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
//			if(null==list||list.size==0||list==null)
			for(Map val:list){
				if("".equals( val.get("v"+(3*a-1)))|| val.get("v"+(3*a-1))==null){
					obj.put("a","");//贫困现状
				}else{
					obj.put("a", val.get("v"+(3*a-1))==null?"-":val.get("v"+(3*a-1)));//贫困现状
				}
				if("".equals( val.get("v"+(3*a)))|| val.get("v"+(3*a))==null){
					obj.put("b","");//计划
				}else{
					obj.put("b", val.get("v"+(3*a))==null?"-":val.get("v"+(3*a)));//计划
				}
				if("".equals( val.get("v"+(3*a+1)))|| val.get("v"+(3*a+1))==null){
					obj.put("c","");//完成
				}else{
					obj.put("c", val.get("v"+(3*a+1))==null?"-":val.get("v"+(3*a+1)));//完成
				}
				if("".equals( val.get("v1"))|| val.get("v1")==null){
					obj.put("d","");//年份
				}else{
					obj.put("d", val.get("v1")==null?"-":val.get("v1"));//年份
				}
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
			response.getWriter().close();
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
			response.getWriter().close();
		}
		return null;
	}
	/**
	 * 添加帮扶计划
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getAddBfjhController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String v1=request.getParameter("aim");//帮扶目标
		String v2=request.getParameter("dline");//帮扶时限
		String v3=request.getParameter("plan");//帮扶计划
		String pkid=request.getParameter("id");//修改记录的id
		try{
			String sql="update da_help_info set v1='"+v1+"',v2='"+v2+"',v3='"+v3+"' where pkid="+pkid;
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			this.getBySqlMapper.findRecords(sqlAdapter);
			
			
			response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");//成功
			
			//添加
		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");//失败
		}
	
		return null;
	}
	/**
	 * 帮扶措施的一级菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getBfcsShu(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String da_household_id=request.getParameter("pid");
		String sql="SELECT count(*) b,a FROM (select v1 a, count(*) from da_help_tz_measures WHERE da_household_id="+da_household_id+" GROUP BY v2 )aa GROUP BY a ";
		try {
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			JSONArray jsonArray=new JSONArray();
			for(Map val:list){
				JSONObject obj=new JSONObject();
				obj.put("a", val.get("a"));
				obj.put("b", val.get("b"));
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
			response.getWriter().close();
		} catch (Exception e) {
			// TODO: handle exception
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
			response.getWriter().close();
		}
		
		return null;
	}
	/**
	 * 查看帮扶措施（项目类别、扶持措施、是否符合扶持条件）
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getSaveBfcsController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String da_household_id=request.getParameter("pid");
		String name=request.getParameter("name");
		
		try {
			String sql="select v2,v3 from da_help_tz_measures where da_household_id="+da_household_id+" AND v1='"+name+"' GROUP BY v2" ;
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			JSONArray jsonArray=new JSONArray();
			for(Map val:list){
				JSONObject obj=new JSONObject();
				if("".equals(val.get("v2"))||val.get("v2")==null){
					obj.put("v2", "");
				}else{
					obj.put("v2",val.get("v2"));//扶持措施
				}
				if("".equals(val.get("v3"))||val.get("v3")==null){
					obj.put("v3","");
				}else{
					obj.put("v3",val.get("v3"));//是否符合扶持条件
				}
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
			response.getWriter().close();
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
			response.getWriter().close();
		}
		
		return null;
	}
	/**
	 * 点击查看帮扶措施详细信息（查看年份具体信息）
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public  ModelAndView getMessagerYearBfcs(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String da_household_id=request.getParameter("pid");
		String v2=request.getParameter("v2");//扶持措施
		try {
			String sql="SELECT * from da_help_tz_measures WHERE da_household_id="+da_household_id+" AND v2='"+v2+"' ORDER BY v7 ";
			JSONArray jsonArray=new JSONArray();
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject obj=new JSONObject();
			for(Map val : list){
				if("".equals(val.get("v4"))||val.get("v4")==null){
					obj.put("v4","");
				}else{
					obj.put("v4",val.get("v4"));
				}
				if("".equals(val.get("v5"))||val.get("v5")==null){
					obj.put("v5","");
				}else{
					obj.put("v5",val.get("v5"));
				}
				if("".equals(val.get("v6"))||val.get("v6")==null){
					obj.put("v6","");
				}else{
					obj.put("v6",val.get("v6"));
				}
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":"+jsonArray.toString()+"}");
			response.getWriter().close();
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
			response.getWriter().close();
		}
		
		return null;
	}
	/**
	 * 修改帮扶措施
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getAddBfcsController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String da_household_id=request.getParameter("pid");//贫困户id
		String style=request.getParameter("type").trim();//1、修改2、添加
		String v1=request.getParameter("v1");
		String v2=request.getParameter("v2");
		String v3=request.getParameter("v3");
		String v4_16=request.getParameter("v4_16");
		String v5_16=request.getParameter("v5_16");
		String v6_16=request.getParameter("v6_16");
		
		String v4_17=request.getParameter("v4_17");
		String v5_17=request.getParameter("v5_17");
		String v6_17=request.getParameter("v6_17");
		
		String v4_18=request.getParameter("v4_18");
		String v5_18=request.getParameter("v5_18");
		String v6_18=request.getParameter("v6_18");
		
		String v4_19=request.getParameter("v4_19");
		String v5_19=request.getParameter("v5_19");
		String v6_19=request.getParameter("v6_19");
		JSONArray jsonArray=new JSONArray();
		try {
			if(style.toString().equals("1")){//修改
				//修改2016
				String sql_1="update da_help_tz_measures set v3='"+v3+"', v4='"+v4_16+"',v5='"+v5_16+"',v6='"+v6_16+"' where da_household_id="+da_household_id+" and v7='2016' and v2='"+v2+"'";
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				this.getBySqlMapper.findRecords(sqlAdapter_1);
				//修改2017
				String sql_2="update da_help_tz_measures set v3='"+v3+"', v4='"+v4_17+"',v5='"+v5_17+"',v6='"+v6_17+"' where da_household_id="+da_household_id+" and v7='2017' and v2='"+v2+"'";
				SQLAdapter sqlAdapter_2=new SQLAdapter(sql_2);
				this.getBySqlMapper.findRecords(sqlAdapter_2);
				//修改2018
				String sql_3="update da_help_tz_measures set v3='"+v3+"', v4='"+v4_18+"',v5='"+v5_18+"',v6='"+v6_18+"' where da_household_id="+da_household_id+" and v7='2018' and v2='"+v2+"'";
				SQLAdapter sqlAdapter_3=new SQLAdapter(sql_3);
				this.getBySqlMapper.findRecords(sqlAdapter_3);
				//修改2019
				String sql_4="update da_help_tz_measures set v3='"+v3+"', v4='"+v4_19+"',v5='"+v5_19+"',v6='"+v6_19+"' where da_household_id="+da_household_id+" and v7='2019' and v2='"+v2+"'";
				SQLAdapter sqlAdapter_4=new SQLAdapter(sql_4);
				this.getBySqlMapper.findRecords(sqlAdapter_4);
				response.getWriter().write("{\"isError\":\"0\"}");
				response.getWriter().close();
			}else if(style.toString().equals("2")){//添加
				String cha_sql="select * from da_help_tz_measures where v2='"+v2+"'  and da_household_id="+da_household_id;
				SQLAdapter sqlAdapter=new SQLAdapter(cha_sql);
				List<Map> cha_list=this.getBySqlMapper.findRecords(sqlAdapter);
				if(null==cha_list||cha_list.size()==0){
					String sql_16="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_16+"','"+v5_16+"','"+v6_16+"','2016')";
					SQLAdapter sqlAdapter_16=new SQLAdapter(sql_16);
					this.getBySqlMapper.findRecords(sqlAdapter_16);
					
					String sql_17="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_17+"','"+v5_17+"','"+v6_17+"','2017')";
					SQLAdapter sqlAdapter_17=new SQLAdapter(sql_17);
					this.getBySqlMapper.findRecords(sqlAdapter_17);
					
					String sql_18="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_18+"','"+v5_18+"','"+v6_18+"','2018')";
					SQLAdapter sqlAdapter_18=new SQLAdapter(sql_18);
					this.getBySqlMapper.findRecords(sqlAdapter_18);
					
					String sql_19="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_19+"','"+v5_19+"','"+v6_19+"','2019')";
					SQLAdapter sqlAdapter_19=new SQLAdapter(sql_19);
					this.getBySqlMapper.findRecords(sqlAdapter_19);
					
					response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");
					response.getWriter().close();
				}else{
					response.getWriter().write("{\"isError\":\"2\",\"result\":\"\"}");
					response.getWriter().close();
				}
			}
			
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\"}");
			response.getWriter().close();
		}
		
		return null;
	}
	/**
	 * 添加帮扶措施
	 * @param request
	 * @param response  
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getUpdateBfcs(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String da_household_id=request.getParameter("pid");
		String v1=request.getParameter("v1");
		String v2=request.getParameter("v2");
		String v3=request.getParameter("v3");
		String v4_16=request.getParameter("v4_16");
		String v4_17=request.getParameter("v4_17");
		String v4_18=request.getParameter("v4_18");
		String v4_19=request.getParameter("v4_19");
		String v5_16=request.getParameter("v5_16");
		String v5_17=request.getParameter("v5_17");
		String v5_18=request.getParameter("v5_18");
		String v5_19=request.getParameter("v5-19");
		String v6_16=request.getParameter("v6-16");
		String v6_17=request.getParameter("v6_17");
		String v6_18=request.getParameter("v6_18");
		String v6_19=request.getParameter("v6_19");
		
		try {
			String cha_sql="select * from da_help_tz_measures where v2='"+v2+"', and da_household_id="+da_household_id;
			SQLAdapter sqlAdapter=new SQLAdapter(cha_sql);
			List<Map> cha_list=this.getBySqlMapper.findRecords(sqlAdapter);
			if(null==cha_list||cha_list.size()==0){
				String sql_16="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_16+"','"+v5_16+"','"+v6_16+"','2016')";
				SQLAdapter sqlAdapter_16=new SQLAdapter(sql_16);
				this.getBySqlMapper.findRecords(sqlAdapter_16);
				
				String sql_17="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_17+"','"+v5_17+"','"+v6_17+"','2017')";
				SQLAdapter sqlAdapter_17=new SQLAdapter(sql_17);
				this.getBySqlMapper.findRecords(sqlAdapter_17);
				
				String sql_18="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_18+"','"+v5_18+"','"+v6_18+"','2018')";
				SQLAdapter sqlAdapter_18=new SQLAdapter(sql_18);
				this.getBySqlMapper.findRecords(sqlAdapter_18);
				
				String sql_19="insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values ("+da_household_id+",'"+v1+"','"+v2+"','"+v3+"','"+v4_19+"','"+v5_19+"','"+v6_19+"','2019')";
				SQLAdapter sqlAdapter_19=new SQLAdapter(sql_19);
				this.getBySqlMapper.findRecords(sqlAdapter_19);
				
				response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");
				response.getWriter().close();
			}else{
				response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
				response.getWriter().close();
			}
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"2\",\"result\":\"\"}");
			response.getWriter().close();
		}
		return null;
	}
	
	/**
	 * 检测app版本是否进行更新
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getSaveVersionx(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String v=request.getParameter("v");//版本
		String ipad_v=request.getParameter("version");
		String sql="";
		if("".equals(v)||v==null){
			sql="select * from app_version where pkid=2";
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			if(list.size()>0){
				String version=(String) list.get(0).get("version");
				if(ipad_v.toString().equals(version)){
					response.getWriter().write("");
				}else{
					response.getWriter().write( list.get(0).get("app_path").toString());
				}
			}
		}else{
			sql="select * from app_version where pkid=1";
			SQLAdapter sqlAdapter=new SQLAdapter(sql);
			List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
			if(list.size()>0){
				String version=(String) list.get(0).get("version");
				if(v.toString().equals(version)){
					response.getWriter().write("{\"isError\":\"0\",\"result\":\"n\"}");
				}else{
					String url=(String) list.get(0).get("app_path");
					response.getWriter().write("{\"isError\":\"0\",\"result\":\""+url.substring(6)+"\"}");
				}
			}
		}
		
	
		return null;
	}
	/**
	 * 上传用户头像
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getAddhuzhuPhoto(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String da_household_id=request.getParameter("pid");//贫困户id
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd"); 
		String v1=simpleDate.format(new Date());
		String img=request.getParameter("img");//图片名称
		String size=request.getParameter("size");//图片的大小
		String type=request.getParameter("type");
		SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMdd"); 
		try {
			if(type.toString().equals("4")){//户主  	
				String cha_sql="select * from da_pic where pic_pkid="+da_household_id+" and pic_type='4'";
				SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
				List<Map> cha_list = this.getBySqlMapper.findRecords(cha_sqlAdapter);
				String Url = "/assa/attached/4/"+dfs.format(new Date())+"/";
				if(null==cha_list||cha_list.size()==0){
					String sql="insert into da_pic (pic_type,pic_pkid,pic_path,pic_size,pic_format) values ('4','"+da_household_id+"','"+Url+img+".jpg','"+size+"','jpg')";
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					this.getBySqlMapper.findRecords(sqlAdapter);
				}else{
					String sql="update da_pic set pic_type='4', pic_path='"+Url+img+".jpg',pic_size='"+size+"', pic_format='jpg'  where pic_pkid="+da_household_id;
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					this.getBySqlMapper.findRecords(sqlAdapter);
				}
			}if(type.toString().equals("5")){//家庭成员
				String cha_sql="select * from da_pic where pic_pkid="+da_household_id+" and pic_type='5'";
				SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
				List<Map> cha_list = this.getBySqlMapper.findRecords(cha_sqlAdapter);
				String Url = "/assa/attached/5/"+dfs.format(new Date())+"/";
				if(null==cha_list||cha_list.size()==0){
					String sql="insert into da_pic (pic_type,pic_pkid,pic_path,pic_size,pic_format) values ('5','"+da_household_id+"','"+Url+img+".jpg','"+size+"','jpg')";
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					this.getBySqlMapper.findRecords(sqlAdapter);
				}else{
					String sql="update da_pic set pic_type='5', pic_path='"+Url+img+".jpg',pic_size='"+size+"', pic_format='jpg'  where pic_pkid="+da_household_id;
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					this.getBySqlMapper.findRecords(sqlAdapter);
				}
				
				
			}else{
				
			}
			response.getWriter().write("{\"isError\":\"0\",\"result\":\"\"}");
		} catch (Exception e) {
			response.getWriter().write("{\"isError\":\"1\",\"result\":\"\"}");
		}
		
		
		//获取磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获取文件需要上传到的路径
		String savePath = request.getServletContext().getRealPath("/")+ "attached\\"+type+"\\"; 
		 // 文件保存目录URL  
        String saveUrl = request.getContextPath() + "/attached/"+type+"/"; 
        
		// 检查目录  
        File uploadDir = new File(savePath);  
        if (!uploadDir.isDirectory()) {  
        	//response.getWriter().write(getError("上传目录不存在。"));
        	if(!uploadDir.exists()){
        		uploadDir.mkdirs();
        	}
           // return null;
        }  
        // 检查目录写权限  
        if (!uploadDir.canWrite()) {  
        	//response.getWriter().write(getError("上传目录没有写权限。"));  
            return null;
        }
        //创建文件夹
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   
        String ymd = sdf.format(new Date());  
        savePath += ymd + "\\";  
        saveUrl += ymd + "/";
        File dirFile = new File(savePath);  
        if (!dirFile.exists()) {  
            dirFile.mkdirs();  
        }
        
        //如果没有以下两行设置的话，上传大的文件，会占用很多内存
        //设置暂时存放的存储室，这个存储室，可以和最终存储文件的目录不同 
        /**
         * 原理 它是先存储 暂时存储室，然后再真正写到对应的目录的硬盘上，按理来说 当上传一个文件时，其实是上传了两份，第一个是以。tem格式的
         * 然后再将其真正写到 对应目录的硬盘上  
         */
        
        factory.setRepository(new File(savePath));
        //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024*1024);
        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        try{
        	List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
        	for(FileItem item : list){
        		//获取表单的属性名字
        		String name = item.getFieldName();
        		
        		String image=item.getName();
        		//如果获取的 表单信息是普通的文本信息
        		if(item.isFormField()){
        			//获取用户具体输入的字符串，因为表单提交过来的是字符串类型  
        			String value = item.getString();
        			
        			request.setAttribute(name, value);
        		}else{
        			//对于传入的非简单的字符串进行处理，比如说二进制的图片，文件  
        			//获取路径名
        			String value = item.getName();
        			//索引到最后一个反斜杠
        			int start = value.lastIndexOf("\\");
        			//截取 上传文件的 字符串名字，加1是 去掉反斜杠
        			String fileName = value.substring(start + 1);
        			
        			// 检查扩展名  
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); 
                    
        			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
                    String newFileName =image ;
        			
        			request.setAttribute(name, newFileName);
        			//真正写到磁盘上
        			item.write(new File(savePath, newFileName));
		        		}
		        	}
		        }catch(FileUploadException e){
		        	e.printStackTrace();
		        }catch(Exception e){
		        	
		        }
		return null;
	}
	
	/**
	 * 数据统计_贫困分布指标
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getPoorController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String type=request.getParameter("type");//市乡村标识
		String name=request.getParameter("name");//是乡村名称
		String count=request.getParameter("count");//大于数
		String distinction=request.getParameter("distinction");//区分统计还是...1、致贫原因；2、贫困户构成 3、家庭收入指标
		String sql;
		JSONArray jsonArray=new JSONArray();
		JSONArray jsonArray1=new JSONArray();
		JSONObject obj=new JSONObject();
		JSONObject obj1=new JSONObject();
		int none=0;
		
		
		if ("0".equals(type)) {//市级统计
			if ("1".equals(distinction)) {//致贫原因
				sql = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='国家级贫困人口' group by v23";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				if(list.size()>0&&list.get(0)!=null){
					for (Map val : list) {
						obj.put("zpyy",val.get("zpyy")==null||"".equals(val.get("zpyy"))?"":val.get("zpyy"));
						obj.put("num", val.get("num")==null||"".equals(val.get("num"))?"":val.get("num"));
						jsonArray.add(obj);
					}
				}else{
					obj.put("zpyy","");
					obj.put("num", "");
					jsonArray.add(obj);
				}
			
				
				String sql1 = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='市级低收入人口' group by v23";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				
				for(Map vall:list1){
					obj1.put("zpyy", vall.get("zpyy"));
					obj1.put("num",vall.get("num"));
					jsonArray1.add(obj1);
				}
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
				
			} else if ("2".equals(distinction)) {// 贫困户构成
				sql = "select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='国家级贫困人口' group by v22  ";
				SQLAdapter sqlAdapter_3 = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter_3);
				for (Map val : list) {
					obj.put("pkhlx", val.get("pkhlx"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				String sql1 = "select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='市级低收入人口' group by v22  ";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				for( Map vall:list1){
					obj1.put("pkhlx", vall.get("pkhlx"));
					obj1.put("num", vall.get("num"));
					jsonArray1.add(obj1);
				}
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
			} else if ("3".equals(distinction)) {// 家庭收、支
				//国贫收入
				sql = "select t2.v2 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "+
						"from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='国家级贫困人口'";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				JSONObject object = new JSONObject();
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("area", val.get("area"));
					if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
						obj.put("jyxsr", none);
					} else {
						obj.put("jyxsr", val.get("jyxsr"));
					}
					if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
						obj.put("zcxsr", none);
					} else {
						obj.put("zcxsr", val.get("zcxsr"));
					}
					if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
						obj.put("ccxsr", none);
					} else {
						obj.put("ccxsr", val.get("ccxsr"));
					}
					if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
						obj.put("gzxsr", none);
					} else {
						obj.put("gzxsr", val.get("gzxsr"));
					}
					if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
						obj.put("qtsr", none);
					} else {
						obj.put("qtsr", val.get("qtsr"));
					}
					jsonArray.add(obj);
				}
				//市贫收入
				String  sql_1= "select t2.v2 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "+
						"from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='市级低收入人口'";
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for (Map val : list_1) {
					obj_1.put("area", val.get("area"));
					if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
						obj_1.put("jyxsr", none);
					} else {
						obj_1.put("jyxsr", val.get("jyxsr"));
					}
					if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
						obj_1.put("zcxsr", none);
					} else {
						obj_1.put("zcxsr", val.get("zcxsr"));
					}
					if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
						obj_1.put("ccxsr", none);
					} else {
						obj_1.put("ccxsr", val.get("ccxsr"));
					}
					if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
						obj_1.put("gzxsr", none);
					} else {
						obj_1.put("gzxsr", val.get("gzxsr"));
					}
					if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
						obj_1.put("qtsr", none);
					} else {
						obj_1.put("qtsr", val.get("qtsr"));
					}
					jsonArray_1.add(obj_1);
				}
				//国贫支出
				String sql1="SELECT t2.v2 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
							" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
							" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='国家级贫困人口'";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				JSONArray jsonArray2=new JSONArray();
				JSONObject object1=new JSONObject();
				for (Map val : list1) {
					object1.put("area", val.get("area"));
					if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
						object1.put("csjy", none);
					} else {
						object1.put("csjy", val.get("csjy"));
					}
					if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
						object1.put("zczc", none);
					} else {
						object1.put("zczc", val.get("zczc"));
					}
					if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
						object1.put("qtzc", none);
					} else {
						object1.put("qtzc", val.get("qtzc"));
						
					}
					jsonArray2.add(object1);
				}
				//市贫支出
				String sql1_1="SELECT t2.v2 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
						" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
						" t2 on t1.da_household_id=t2.pkid  where  t2.sys_standard='市级低收入人口' ";
				
				SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
				List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
				JSONArray jsonArray2_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				for (Map val : list1_1) {
					obj1_1.put("area", val.get("area"));
					if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
						obj1_1.put("csjy", none);
					} else {
						obj1_1.put("csjy", val.get("csjy"));
					}
					if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
						obj1_1.put("zczc", none);
					} else {
						obj1_1.put("zczc", val.get("zczc"));
					}
					if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
						obj1_1.put("qtzc", none);
					} else {
						obj1_1.put("qtzc", val.get("qtzc"));
						
					}
					jsonArray2_1.add(obj1_1);
				}
				
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray2_1.toString()+"}");
			} else if("0".equals(distinction)) {//贫困人口
				sql = "select v3 as area,count(v3) as num from da_household where sys_standard='国家级贫困人口' group by v3";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("area", val.get("area"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				
				//国标家庭成员大于数
				String hu_sql="select * from ((select count(*) a1 from da_household where v9 ='1' and sys_standard='国家级贫困人口') a join"+
						" (select count(*) a2 from da_household where v9 ='2' and sys_standard='国家级贫困人口')b  join "+
						" (select count(*) a3 from da_household where v9 ='3' and sys_standard='国家级贫困人口')c  join "+
						" (select count(*) a4 from da_household where v9 ='4' and sys_standard='国家级贫困人口')d  join "+
						" (select count(*) a5 from da_household where v9 >='5' and sys_standard='国家级贫困人口')e )";
			
				SQLAdapter hu_sqlAdapter=new SQLAdapter(hu_sql);
				List<Map> hu_list = this.getBySqlMapper.findRecords(hu_sqlAdapter);
				for (Map val : hu_list) {
					obj1.put("a1", val.get("a1"));
					obj1.put("a2", val.get("a2"));
					obj1.put("a3", val.get("a3"));
					obj1.put("a4", val.get("a4"));
					obj1.put("a5", val.get("a5"));
					jsonArray1.add(obj1);
				}
				
				//国贫贫困户大于20人的村
				String cha_sql="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where sys_standard='国家级贫困人口')a GROUP by a.v5)ff ";
				SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
				JSONArray jsonArray2=new JSONArray();
				JSONObject obj2=new JSONObject();
				List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
				if(cha_list.size()>0&&cha_list.get(0)!=null){
					String main=cha_list.get(0).get("num").toString();
					int main1=Integer.parseInt(main);
					int count1=Integer.parseInt(count);
					if(count1>main1){
						obj2.put("num",0);
						jsonArray2.add(obj2);
					}else{
						String sql1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household  where sys_standard='国家级贫困人口')a GROUP by a.v5)ff where ff.num>"+count+"";
						SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
						
						List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter1);
						
						obj2.put("num",list2.get(0).get("num"));
						jsonArray2.add(obj2);
					}
				}else{
					obj2.put("num",0);
					jsonArray2.add(obj2);
				}
				//市标贫困人口
				String sql_1 = "select v3 as area,count(v3) as num from da_household where sys_standard='市级低收入人口' group by v3";
				SQLAdapter sqlAdapter_1 = new SQLAdapter(sql_1);
				List<Map> list_1 = this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for (Map val : list_1) {
					obj_1.put("area", val.get("area"));
					obj_1.put("num", val.get("num"));
					jsonArray_1.add(obj_1);
				}
				
				//市标家庭成员大于数
				String hu_sql_1="select * from ((select count(*) a1 from da_household where v9 ='1' and sys_standard='市级低收入人口') a join"+
						" (select count(*) a2 from da_household where v9 ='2' and sys_standard='市级低收入人口')b  join "+
						" (select count(*) a3 from da_household where v9 ='3' and sys_standard='市级低收入人口')c  join "+
						" (select count(*) a4 from da_household where v9 ='4' and sys_standard='市级低收入人口')d  join "+
						" (select count(*) a5 from da_household where v9 >5 and sys_standard='市级低收入人口')e )";
			
				SQLAdapter hu_sqlAdapter_1=new SQLAdapter(hu_sql_1);
				List<Map> hu_list_1 = this.getBySqlMapper.findRecords(hu_sqlAdapter_1);
				JSONArray jsonArray1_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				for (Map val : hu_list_1) {
					obj1_1.put("a1", val.get("a1"));
					obj1_1.put("a2", val.get("a2"));
					obj1_1.put("a3", val.get("a3"));
					obj1_1.put("a4", val.get("a4"));
					obj1_1.put("a5", val.get("a5"));
					jsonArray1_1.add(obj1_1);
				}
				
				//市贫贫困户大于20人的村
				String cha_sql_1="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where sys_standard='市级低收入人口')a GROUP by a.v5)ff ";
				SQLAdapter cha_sqlAdapter_1=new SQLAdapter(cha_sql_1);
				JSONArray jsonArray2_1=new JSONArray();
				JSONObject obj2_1=new JSONObject();
				List<Map> cha_list_1=this.getBySqlMapper.findRecords(cha_sqlAdapter_1);
				if(cha_list_1.size()>0&&cha_list_1.get(0)!=null){
					String main_1=cha_list_1.get(0).get("num").toString();
					int main1_1=Integer.parseInt(main_1);
					int count1_1=Integer.parseInt(count);
					if(count1_1>main1_1){
						obj2_1.put("num",0);
						jsonArray2_1.add(obj2_1);
					}else{
						String sql1_1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household  where sys_standard='市级低收入人口' )a GROUP by a.v5)ff where ff.num>"+count+"";
						SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
						
						List<Map> list2_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
						
						obj2_1.put("num",list2_1.get(0).get("num"));
						jsonArray2_1.add(obj2_1);
					}
				}else{
					obj2_1.put("num",0);
					jsonArray2_1.add(obj2_1);
				}
			
					response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+",\"result2\":"+
					jsonArray2.toString()+",\"result3\":"+jsonArray_1.toString()+",\"result4\":"+jsonArray1_1.toString()+",\"result5\":"+jsonArray2_1.toString()+"}");
				}

		} else if ("1".equals(type)) {// 县级统计
			if ("1".equals(distinction)) {//致贫原因
				sql = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='国家级贫困人口' and v3='"
							+name+"' group by v23";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("zpyy", val.get("zpyy"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				
				String sql1 = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='市级低收入人口' and v3='"+name+"' group by v23";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				
				for(Map vall:list1){
					obj1.put("zpyy", vall.get("zpyy"));
					obj1.put("num",vall.get("num"));
					jsonArray1.add(obj1);
				}
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
				
			} else if ("2".equals(distinction)) {// 贫困户构成
				 sql = "select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='国家级贫困人口' and v3='"
						+ name + "' group by v22 ";
				SQLAdapter sqlAdapter_3 = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter_3);
				for (Map val : list) {
					obj.put("pkhlx", val.get("pkhlx"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				String sql1="select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='市级低收入人口' and v3='"
						+ name + "' group by v22 ";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				for(Map vall :list1){
					obj1.put("pkhlx", vall.get("pkhlx"));
					obj1.put("num", vall.get("num"));
					jsonArray1.add(obj1);
				}
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
			} else if ("3".equals(distinction)) {// 家庭收入指标
				//国贫收入
				sql= "select t2.v3 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "
						+ "from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='国家级贫困人口'  and t2.v3='"
						+ name + "'";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				if(list.size()>0&&list.get(0)!=null){
					for (Map val : list) {
						obj.put("area", val.get("area"));
						if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
							obj.put("jyxsr",none);
						} else {
							obj.put("jyxsr", val.get("jyxsr"));
						}
						if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
							obj.put("zcxsr",none);
						} else {
							obj.put("zcxsr", val.get("zcxsr"));
						}
						if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
							obj.put("ccxsr", none);
						} else {
							obj.put("ccxsr", val.get("ccxsr"));
						}
						if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
							obj.put("gzxsr", none);
						} else {
							obj.put("gzxsr", val.get("gzxsr"));
						}
						if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
							obj.put("qtsr", none);
						} else {
							obj.put("qtsr", val.get("qtsr"));
						}
						jsonArray.add(obj);
					}
				}else{
					obj.put("area","");
					obj.put("jyxsr",none);
					obj.put("zcxsr",none);
					obj.put("ccxsr", none);
					obj.put("gzxsr", none);
					obj.put("qtsr", none);
					jsonArray.add(obj);
					
				}
				
				//市贫收入
				String  sql_1= "select t2.v3 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "
						+ "from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='市级低收入人口'  and t2.v3='"
						+ name + "' ";
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				if(list_1.size()>0&&list_1.get(0)!=null){
					for (Map val : list_1) {
						obj_1.put("area", val.get("area"));
						if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
							obj_1.put("jyxsr", none);
						} else {
							obj_1.put("jyxsr", val.get("jyxsr"));
						}
						if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
							obj_1.put("zcxsr", none);
						} else {
							obj_1.put("zcxsr", val.get("zcxsr"));
						}
						if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
							obj_1.put("ccxsr", none);
						} else {
							obj_1.put("ccxsr", val.get("ccxsr"));
						}
						if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
							obj_1.put("gzxsr", none);
						} else {
							obj_1.put("gzxsr", val.get("gzxsr"));
						}
						if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
							obj_1.put("qtsr", none);
						} else {
							obj_1.put("qtsr", val.get("qtsr"));
						}
						jsonArray_1.add(obj_1);
					}
				}else{
					obj_1.put("area", "");
					obj_1.put("jyxsr", none);
					obj_1.put("zcxsr", none);
					obj_1.put("ccxsr", none);
					obj_1.put("gzxsr", none);
					obj_1.put("qtsr", none);
					jsonArray_1.add(obj_1);
				}
				
				//国贫支出
				String sql1="SELECT t2.v3 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
						" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
						" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='国家级贫困人口' and t2.v3='"+name+"'";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				JSONArray jsonArray2=new JSONArray();
				JSONObject object1=new JSONObject();
				if(list1.size()>0&&list1.get(0)!=null){
					for (Map val : list1) {
						object1.put("area", val.get("area"));
						if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
							object1.put("csjy", none);
						} else {
							object1.put("csjy", val.get("csjy"));
						}
						if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
							object1.put("zczc", none);
						} else {
							object1.put("zczc", val.get("zczc"));
						}
						if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
							object1.put("qtzc", none);
						} else {
							object1.put("qtzc", val.get("qtzc"));
							
						}
						jsonArray2.add(object1);
					}
				}else{
					object1.put("area","");
					object1.put("csjy", none);
					object1.put("zczc", none);
					object1.put("qtzc", none);
					jsonArray2.add(object1);
				}
			
				//市贫支出
				String sql1_1="SELECT t2.v3 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
						" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
						" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='市级低收入人口' and t2.v3='"+name+"'";
				
				SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
				List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
				JSONArray jsonArray2_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				if(list1_1.size()>0&&list1_1.get(0)!=null){
					for (Map val : list1_1) {
						obj1_1.put("area", val.get("area"));
						if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
							obj1_1.put("csjy", none);
						} else {
							obj1_1.put("csjy", val.get("csjy"));
						}
						if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
							obj1_1.put("zczc", none);
						} else {
							obj1_1.put("zczc", val.get("zczc"));
						}
						if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
							obj1_1.put("qtzc", none);
						} else {
							obj1_1.put("qtzc", val.get("qtzc"));
							
						}
						jsonArray2_1.add(obj1_1);
					}
				}else{
						obj1_1.put("area", "");
						obj1_1.put("csjy", none);
						obj1_1.put("zczc", none);
						obj1_1.put("qtzc", none);
						jsonArray2_1.add(obj1_1);
				}
			
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray2_1.toString()+"}");
			} else if("0".equals(distinction)) {
				sql = "select v4 as area,count(v4) as num from da_household where v3='"+name+"' and sys_standard='国家级贫困人口' group by v4";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("area", val.get("area"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				
				String hu_sql="select * from ((select count(*) a1 from da_household where v3='"+name+"' and v9 ='1' and sys_standard='国家级贫困人口') a join"+
							" (select count(*) a2 from da_household where v3='"+name+"' and v9 ='2' and sys_standard='国家级贫困人口')b  join "+
							" (select count(*) a3 from da_household where v3='"+name+"' and v9 ='3' and sys_standard='国家级贫困人口')c  join "+
							" (select count(*) a4 from da_household where v3='"+name+"' and v9 ='4' and sys_standard='国家级贫困人口')d  join "+
							" (select count(*) a5 from da_household where v3='"+name+"' and v9 >5 and sys_standard='国家级贫困人口')e )";
				
				SQLAdapter hu_sqlAdapter=new SQLAdapter(hu_sql);
				List<Map> hu_list = this.getBySqlMapper.findRecords(hu_sqlAdapter);
				
				for (Map val : hu_list) {
					obj1.put("a1", val.get("a1"));
					obj1.put("a2", val.get("a2"));
					obj1.put("a3", val.get("a3"));
					obj1.put("a4", val.get("a4"));
					obj1.put("a5", val.get("a5"));
					jsonArray1.add(obj1);
				}
				
				//贫困户大于20人的村
				String cha_sql="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where v3='"+name+"' and sys_standard='国家级贫困人口')a GROUP by a.v5)ff  ";
				SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
				JSONArray jsonArray2=new JSONArray();
				JSONObject obj2=new JSONObject();
				List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
				if(cha_list.size()>0&&cha_list.get(0)!=null){
					String main=cha_list.get(0).get("num").toString();
					int main1=Integer.parseInt(main);
					int count1=Integer.parseInt(count);
					if(count1>main1){
						obj2.put("num",0);
						jsonArray2.add(obj2);
					}else{
						String sql1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household where v3='"+name+"' and sys_standard='国家级贫困人口')a GROUP by a.v5)ff where ff.num>"+count+"";
						SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
						List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter1);
						
						obj2.put("num",list2.get(0).get("num"));
						jsonArray2.add(obj2);
					}
				}else{
					obj2.put("num",0);
					jsonArray2.add(obj2);
				}
				
				//市标贫困人口
				String sql_1 = "select v4 as area,count(v4) as num from da_household where v3='"+name+"' and sys_standard='市级低收入人口' group by v4";
				SQLAdapter sqlAdapter_1 = new SQLAdapter(sql_1);
				List<Map> list_1 = this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for (Map val : list_1) {
					obj_1.put("area", val.get("area"));
					obj_1.put("num", val.get("num"));
					jsonArray_1.add(obj_1);
				}
				
				//市标家庭成员大于数
				String hu_sql_1="select * from ((select count(*) a1 from da_household where v3='"+name+"' and v9 ='1' and sys_standard='市级低收入人口') a join"+
						" (select count(*) a2 from da_household where v3='"+name+"' and v9 ='2' and sys_standard='市级低收入人口')b  join "+
						" (select count(*) a3 from da_household where v3='"+name+"' and v9 ='3' and sys_standard='市级低收入人口')c  join "+
						" (select count(*) a4 from da_household where v3='"+name+"' and v9 ='4' and sys_standard='市级低收入人口')d  join "+
						" (select count(*) a5 from da_household where v3='"+name+"' and v9 >5 and sys_standard='市级低收入人口')e )";
			
				SQLAdapter hu_sqlAdapter_1=new SQLAdapter(hu_sql_1);
				List<Map> hu_list_1 = this.getBySqlMapper.findRecords(hu_sqlAdapter_1);
				JSONArray jsonArray1_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				for (Map val : hu_list_1) {
					obj1_1.put("a1", val.get("a1"));
					obj1_1.put("a2", val.get("a2"));
					obj1_1.put("a3", val.get("a3"));
					obj1_1.put("a4", val.get("a4"));
					obj1_1.put("a5", val.get("a5"));
					jsonArray1_1.add(obj1_1);
				}
				
				//市贫贫困户大于20人的村
				String cha_sql_1="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where v3='"+name+"' and sys_standard='市级低收入人口')a GROUP by a.v5)ff  ";
				SQLAdapter cha_sqlAdapter_1=new SQLAdapter(cha_sql_1);
				JSONArray jsonArray2_1=new JSONArray();
				JSONObject obj2_1=new JSONObject();
				List<Map> cha_list_1=this.getBySqlMapper.findRecords(cha_sqlAdapter_1);
				if(cha_list_1.size()>0&&cha_list_1.get(0)!=null){
					String main_1=cha_list_1.get(0).get("num").toString();
					int main1_1=Integer.parseInt(main_1);
					int count1_1=Integer.parseInt(count);
					if(count1_1>main1_1){
						obj2_1.put("num",0);
						jsonArray2_1.add(obj2_1);
					}else{
						String sql1_1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household where v3='"+name+"' and sys_standard='市级低收入人口' )a GROUP by a.v5)ff where ff.num>"+count+"";
						SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
						
						List<Map> list2_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
						
						obj2_1.put("num",list2_1.get(0).get("num"));
						jsonArray2_1.add(obj2_1);
					}
				}else{
					obj2_1.put("num",0);
					jsonArray2_1.add(obj2_1);
				}
					response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+",\"result2\":"+
					jsonArray2.toString()+",\"result3\":"+jsonArray_1.toString()+",\"result4\":"+jsonArray1_1.toString()+",\"result5\":"+jsonArray2_1.toString()+"}");
			}

		} else if ("2".equals(type)) {//乡
			if ("1".equals(distinction)) {//致贫原因
				sql = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='国家级贫困人口' and v4='"+name+"' group by v23";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("zpyy", val.get("zpyy"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				
				String sql1 = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='市级低收入人口' and v4='"+name+"' group by v23";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				
				for(Map vall:list1){
					obj1.put("zpyy", vall.get("zpyy"));
					obj1.put("num",vall.get("num"));
					jsonArray1.add(obj1);
				}
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
				
			} else if ("2".equals(distinction)) {// 贫困户构成
				 sql = "select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='国家级贫困人口' and v4='"
							+ name + "' group by v22 ";
					SQLAdapter sqlAdapter_3 = new SQLAdapter(sql);
					List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter_3);
					if(list.size()>0&&list.get(0)!=null){
						for (Map val : list) {
							obj.put("pkhlx", val.get("pkhlx"));
							obj.put("num", val.get("num"));
							jsonArray.add(obj);
						}
					}else{
						obj.put("pkhlx", 0);
						obj.put("num", 0);
						jsonArray.add(obj);
					}
				
					String sql1="select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='市级低收入人口' and v4='"
							+ name + "' group by v22 ";
					SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
					List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
					if(list1.size()>0&&list1.get(0)!=null){
						for(Map vall :list1){
							obj1.put("pkhlx", vall.get("pkhlx"));
							obj1.put("num", vall.get("num"));
							jsonArray1.add(obj1);
						}
					}else{
						obj1.put("pkhlx",0);
						obj1.put("num", 0);
						jsonArray1.add(obj1);
					}
					
					response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
				} else if ("3".equals(distinction)) {
				// 家庭收入指标
				sql= "select t2.v4 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "
						+ "from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='国家级贫困人口'  and t2.v4='"
						+ name + "'";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				if(list.size()>0&&list.get(0)!=null){
					for (Map val : list) {
						obj.put("area", val.get("area"));
						if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
							obj.put("jyxsr", none);
						} else {
							obj.put("jyxsr", val.get("jyxsr"));
						}
						if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
							obj.put("zcxsr", none);
						} else {
							obj.put("zcxsr", val.get("zcxsr"));
						}
						if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
							obj.put("ccxsr", none);
						} else {
							obj.put("ccxsr", val.get("ccxsr"));
						}
						if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
							obj.put("gzxsr", none);
						} else {
							obj.put("gzxsr", val.get("gzxsr"));
						}
						if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
							obj.put("qtsr", none);
						} else {
							obj.put("qtsr", val.get("qtsr"));
						}
						jsonArray.add(obj);
					}
					
				}else{
					obj.put("area","");
					obj.put("jyxsr", none);
					obj.put("zcxsr", none);
					obj.put("ccxsr", none);
					obj.put("gzxsr", none);
					obj.put("qtsr", none);
					jsonArray.add(obj);
				}
				
				//市贫收入
				String  sql_1= "select t2.v4 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "
						+ "from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='市级低收入人口'  and t2.v4='"
						+ name + "'";
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				if(list_1.size()>0&&list_1.get(0)!=null){
					for (Map val : list_1) {
						obj_1.put("area", val.get("area"));
						if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
							obj_1.put("jyxsr", none);
						} else {
							obj_1.put("jyxsr", val.get("jyxsr"));
						}
						if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
							obj_1.put("zcxsr", none);
						} else {
							obj_1.put("zcxsr", val.get("zcxsr"));
						}
						if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
							obj_1.put("ccxsr", none);
						} else {
							obj_1.put("ccxsr", val.get("ccxsr"));
						}
						if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
							obj_1.put("gzxsr", none);
						} else {
							obj_1.put("gzxsr", val.get("gzxsr"));
						}
						if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
							obj_1.put("qtsr", none);
						} else {
							obj_1.put("qtsr", val.get("qtsr"));
						}
						jsonArray_1.add(obj_1);
					}
				}else{
					obj_1.put("area", "");
					obj_1.put("jyxsr", none);
					obj_1.put("zcxsr", none);
					obj_1.put("ccxsr", none);
					obj_1.put("gzxsr", none);
					obj_1.put("qtsr", none);
					jsonArray_1.add(obj_1);
				}
				
				
				
				String sql1="SELECT t2.v4 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
						" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
						" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='国家级贫困人口' and t2.v4='"+name+"'";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				JSONArray jsonArray2=new JSONArray();
				JSONObject object1=new JSONObject();
				if(list1.size()>0&&list1.get(0)!=null){
					for (Map val : list1) {
						object1.put("area", val.get("area"));
						if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
							object1.put("csjy", none);
						} else {
							object1.put("csjy", val.get("csjy"));
						}
						if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
							object1.put("zczc", none);
						} else {
							object1.put("zczc", val.get("zczc"));
						}
						if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
							object1.put("qtzc", none);
						} else {
							object1.put("qtzc", val.get("qtzc"));
							
						}
						jsonArray2.add(object1);
					}
				}else{
					object1.put("area", "");
					object1.put("csjy", none);
					object1.put("zczc", none);
					object1.put("qtzc", none);
					jsonArray2.add(object1);
				}
		
				
				//市贫支出
				String sql1_1="SELECT t2.v4 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
						" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
						" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='市级低收入人口' and t2.v4='"+name+"'";
				SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
				List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
				JSONArray jsonArray2_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				if(list1_1.size()>0&&list1_1.get(0)!=null){
					for (Map val : list1_1) {
						obj1_1.put("area", val.get("area"));
						if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
							obj1_1.put("csjy", none);
						} else {
							obj1_1.put("csjy", val.get("csjy"));
						}
						if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
							obj1_1.put("zczc", none);
						} else {
							obj1_1.put("zczc", val.get("zczc"));
						}
						if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
							obj1_1.put("qtzc", none);
						} else {
							obj1_1.put("qtzc", val.get("qtzc"));
							
						}
						jsonArray2_1.add(obj1_1);
					}
				}else{
					obj1_1.put("area", "");
					obj1_1.put("csjy", none);
					obj1_1.put("zczc", none);
					obj1_1.put("qtzc", none);
					jsonArray2_1.add(obj1_1);
				}
		
				
			response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray2_1.toString()+"}");
			
			} else if("0".equals(distinction)) {
				sql = "select v5 as area,count(v5) as num from da_household where v4='"+name+"' and sys_standard='国家级贫困人口' group by v5";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("area", val.get("area"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				
				String hu_sql="select * from ((select count(*) a1 from da_household where v4='"+name+"' and v9 ='1' and sys_standard='国家级贫困人口') a join"+
							" (select count(*) a2 from da_household where v4='"+name+"' and v9 ='2' and sys_standard='国家级贫困人口')b  join "+
							" (select count(*) a3 from da_household where v4='"+name+"' and v9 ='3' and sys_standard='国家级贫困人口')c  join "+
							" (select count(*) a4 from da_household where v4='"+name+"' and v9 ='4' and sys_standard='国家级贫困人口')d  join "+
							" (select count(*) a5 from da_household where v4='"+name+"' and v9 >5 and sys_standard='国家级贫困人口')e )";
				
				SQLAdapter hu_sqlAdapter=new SQLAdapter(hu_sql);
				List<Map> hu_list = this.getBySqlMapper.findRecords(hu_sqlAdapter);
				
				for (Map val : hu_list) {
					obj1.put("a1", val.get("a1"));
					obj1.put("a2", val.get("a2"));
					obj1.put("a3", val.get("a3"));
					obj1.put("a4", val.get("a4"));
					obj1.put("a5", val.get("a5"));
					jsonArray1.add(obj1);
				}
				
				//贫困户大于20人的村
				String cha_sql="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where v4='"+name+"' and sys_standard='国家级贫困人口')a GROUP by a.v5)ff  ";
				SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
				JSONArray jsonArray2=new JSONArray();
				JSONObject obj2=new JSONObject();
				List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
				if(cha_list.size()>0&&cha_list.get(0)!=null){
					String main=cha_list.get(0).get("num").toString();
					int main1=Integer.parseInt(main);
					int count1=Integer.parseInt(count);
					if(count1>main1){
						obj2.put("num",0);
						jsonArray2.add(obj2);
					}else{
						String sql1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household where v4='"+name+"' and sys_standard='国家级贫困人口')a GROUP by a.v5)ff where ff.num>"+count+"";
						SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
						List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter1);
						
						obj2.put("num",list2.get(0).get("num"));
						jsonArray2.add(obj2);
					}
				}else{
					obj2.put("num",0);
					jsonArray2.add(obj2);
				}
				
			
				
				//市标贫困人口
				String sql_1 = "select v5 as area,count(v5) as num from da_household where v4='"+name+"' and sys_standard='市级低收入人口' group by v5";
				SQLAdapter sqlAdapter_1 = new SQLAdapter(sql_1);
				List<Map> list_1 = this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for (Map val : list_1) {
					obj_1.put("area", val.get("area"));
					obj_1.put("num", val.get("num"));
					jsonArray_1.add(obj_1);
				}
				
				//市标家庭成员大于数
				String hu_sql_1="select * from ((select count(*) a1 from da_household where v4='"+name+"' and v9 ='1' and sys_standard='市级低收入人口') a join"+
						" (select count(*) a2 from da_household where v4='"+name+"' and v9 ='2' and sys_standard='市级低收入人口')b  join "+
						" (select count(*) a3 from da_household where v4='"+name+"' and v9 ='3' and sys_standard='市级低收入人口')c  join "+
						" (select count(*) a4 from da_household where v4='"+name+"' and v9 ='4' and sys_standard='市级低收入人口')d  join "+
						" (select count(*) a5 from da_household where v4='"+name+"' and v9 >5 and sys_standard='市级低收入人口')e )";
			
				SQLAdapter hu_sqlAdapter_1=new SQLAdapter(hu_sql_1);
				List<Map> hu_list_1 = this.getBySqlMapper.findRecords(hu_sqlAdapter_1);
				JSONArray jsonArray1_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				for (Map val : hu_list_1) {
					obj1_1.put("a1", val.get("a1"));
					obj1_1.put("a2", val.get("a2"));
					obj1_1.put("a3", val.get("a3"));
					obj1_1.put("a4", val.get("a4"));
					obj1_1.put("a5", val.get("a5"));
					jsonArray1_1.add(obj1_1);
				}
				
				//市贫贫困户大于20人的村
				String cha_sql_1="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where v4='"+name+"' and sys_standard='市级低收入人口')a GROUP by a.v5)ff  ";
				SQLAdapter cha_sqlAdapter_1=new SQLAdapter(cha_sql_1);
				JSONArray jsonArray2_1=new JSONArray();
				JSONObject obj2_1=new JSONObject();
				List<Map> cha_list1=this.getBySqlMapper.findRecords(cha_sqlAdapter_1);
				if(cha_list1.size()>0&&cha_list1.get(0)!=null){
					String main_1=cha_list1.get(0).get("num").toString();
					int main1_1=Integer.parseInt(main_1);
					int count1_1=Integer.parseInt(count);
					if(count1_1>main1_1){
						obj2_1.put("num",0);
						jsonArray2_1.add(obj2_1);
					}else{
						String sql1_1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household where v4='"+name+"' and sys_standard='市级低收入人口' )a GROUP by a.v5)ff where ff.num>"+count+"";
						SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
						
						List<Map> list2_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
						
						obj2_1.put("num",list2_1.get(0).get("num"));
						jsonArray2_1.add(obj2_1);
					}
				}else{
					obj2_1.put("num",0);
					jsonArray2_1.add(obj2_1);
				}
					response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+",\"result2\":"+
					jsonArray2.toString()+",\"result3\":"+jsonArray_1.toString()+",\"result4\":"+jsonArray1_1.toString()+",\"result5\":"+jsonArray2_1.toString()+"}");
			}

		} else if ("3".equals(type)) {
			if ("1".equals(distinction)) {//致贫原因
				sql = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='国家级贫困人口' and v5='"+name+"' group by v23";
				SQLAdapter sqlAdapter = new SQLAdapter(sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				for (Map val : list) {
					obj.put("zpyy", val.get("zpyy"));
					obj.put("num", val.get("num"));
					jsonArray.add(obj);
				}
				
				String sql1 = "select v23 as zpyy,count(v23) as num from da_household where v23 is not null and v23 != '' and sys_standard='市级低收入人口' and v5='"+name+"' group by v23";
				SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
				
				for(Map vall:list1){
					obj1.put("zpyy", vall.get("zpyy"));
					obj1.put("num",vall.get("num"));
					jsonArray1.add(obj1);
				}
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
				
			} else if ("2".equals(distinction)) {// 贫困户构成
				 sql = "select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='国家级贫困人口' and v5='"
							+ name + "' group by v22 ";
					SQLAdapter sqlAdapter_3 = new SQLAdapter(sql);
					List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter_3);
					for (Map val : list) {
						obj.put("pkhlx", val.get("pkhlx"));
						obj.put("num", val.get("num"));
						jsonArray.add(obj);
					}
					String sql1="select v22 as pkhlx,count(v22) as num from da_household where v22 is not null and v22 != '' and sys_standard='市级低收入人口' and v5='"
							+ name + "' group by v22 ";
					SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
					List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
					for(Map vall :list1){
						obj1.put("pkhlx", vall.get("pkhlx"));
						obj1.put("num", vall.get("num"));
						jsonArray1.add(obj1);
					}
					response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+"}");
				} else if ("3".equals(distinction)) {
					// 家庭收入指标
					sql= "select t2.v5 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "
							+ "from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.sys_standard='国家级贫困人口'  and t2.v5='"
							+ name + "'  ";
					SQLAdapter sqlAdapter = new SQLAdapter(sql);
					List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
					for (Map val : list) {
						obj.put("area", val.get("area"));
						if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
							obj.put("jyxsr", none);
						} else {
							obj.put("jyxsr", val.get("jyxsr"));
						}
						if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
							obj.put("zcxsr", none);
						} else {
							obj.put("zcxsr", val.get("zcxsr"));
						}
						if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
							obj.put("ccxsr", none);
						} else {
							obj.put("ccxsr", val.get("ccxsr"));
						}
						if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
							obj.put("gzxsr", none);
						} else {
							obj.put("gzxsr", val.get("gzxsr"));
						}
						if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
							obj.put("qtsr", none);
						} else {
							obj.put("qtsr", val.get("qtsr"));
						}
						jsonArray.add(obj);
					}
					
					//市贫收入
					String  sql_1= "select t2.v5 as area,sum(t1.v10) as jyxsr,sum(t1.v22) as zcxsr,sum(t1.v24+t1.v26) as ccxsr,sum(t1.v28+t1.v30) as gzxsr,sum(t1.v32+t1.v34) as qtsr "
							+ "from da_current_income as t1 left join da_household as t2 on t1.da_household_id=t2.pkid where t2.v5 is not null and t2.sys_standard='市级低收入人口'  and t2.v5='"
							+ name + "'  group by t2.v5";
					SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
					List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
					JSONArray jsonArray_1=new JSONArray();
					JSONObject obj_1=new JSONObject();
					for (Map val : list_1) {
						obj_1.put("area", val.get("area"));
						if ("".equals(val.get("jyxsr"))|| val.get("jyxsr") == null) {
							obj_1.put("jyxsr", none);
						} else {
							obj_1.put("jyxsr", val.get("jyxsr"));
						}
						if ("".equals(val.get("zcxsr"))|| val.get("zcxsr") == null) {
							obj_1.put("zcxsr", none);
						} else {
							obj_1.put("zcxsr", val.get("zcxsr"));
						}
						if ("".equals(val.get("ccxsr"))|| val.get("ccxsr") == null) {
							obj_1.put("ccxsr", none);
						} else {
							obj_1.put("ccxsr", val.get("ccxsr"));
						}
						if ("".equals(val.get("gzxsr"))|| val.get("gzxsr") == null) {
							obj_1.put("gzxsr", none);
						} else {
							obj_1.put("gzxsr", val.get("gzxsr"));
						}
						if ("".equals(val.get("qtsr"))|| val.get("qtsr") == null) {
							obj_1.put("qtsr", none);
						} else {
							obj_1.put("qtsr", val.get("qtsr"));
						}
						jsonArray_1.add(obj_1);
					}
					
					
					String sql1="SELECT t2.v5 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
							" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
							" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='国家级贫困人口' and t2.v5='"+name+"' ";
					SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
					List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
					JSONArray jsonArray2=new JSONArray();
					JSONObject object1=new JSONObject();
					for (Map val : list1) {
						object1.put("area", val.get("area"));
						if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
							object1.put("csjy", none);
						} else {
							object1.put("csjy", val.get("csjy"));
						}
						if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
							object1.put("zczc", none);
						} else {
							object1.put("zczc", val.get("zczc"));
						}
						if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
							object1.put("qtzc", none);
						} else {
							object1.put("qtzc", val.get("qtzc"));
							
						}
						jsonArray2.add(object1);
					}
					
					//市贫支出
					String sql1_1="SELECT t2.v5 as area,sum(t1.v2+t1.v4+t1.v6+t1.v8+t1.v10+t1.v12+t1.v14+t1.v16+t1.v18) as csjy,"+
							" sum(t1.v20+t1.v22) zczc,sum(t1.v27+t1.v30) qtzc from da_current_expenditure t1 left join da_household as "+
							" t2 on t1.da_household_id=t2.pkid  where t2.sys_standard='市级低收入人口' and t2.v5='"+name+"'SSSS";
					SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
					List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
					JSONArray jsonArray2_1=new JSONArray();
					JSONObject obj1_1=new JSONObject();
					for (Map val : list1_1) {
						obj1_1.put("area", val.get("area"));
						if ("".equals(val.get("csjy"))|| val.get("csjy") == null) {
							obj1_1.put("csjy", none);
						} else {
							obj1_1.put("csjy", val.get("csjy"));
						}
						if ("".equals(val.get("zczc"))|| val.get("zczc") == null) {
							obj1_1.put("zczc", none);
						} else {
							obj1_1.put("zczc", val.get("zczc"));
						}
						if ("".equals(val.get("qtzc"))|| val.get("qtzc") == null) {
							obj1_1.put("qtzc", none);
						} else {
							obj1_1.put("qtzc", val.get("qtzc"));
							
						}
						jsonArray2_1.add(obj1_1);
					}
					
				response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray2_1.toString()+"}");
				
				} else if("0".equals(distinction)) {
					sql = "select v5 as area,count(v5) as num from da_household where v5='"+name+"' and sys_standard='国家级贫困人口' group by v5";
					SQLAdapter sqlAdapter = new SQLAdapter(sql);
					List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
					for (Map val : list) {
						obj.put("area", val.get("area"));
						obj.put("num", val.get("num"));
						jsonArray.add(obj);
					}
					
					String hu_sql="select * from ((select count(*) a1 from da_household where v5='"+name+"' and v9 ='1' and sys_standard='国家级贫困人口') a join"+
								" (select count(*) a2 from da_household where v5='"+name+"' and v9 ='2' and sys_standard='国家级贫困人口')b  join "+
								" (select count(*) a3 from da_household where v5='"+name+"' and v9 ='3' and sys_standard='国家级贫困人口')c  join "+
								" (select count(*) a4 from da_household where v5='"+name+"' and v9 ='4' and sys_standard='国家级贫困人口')d  join "+
								" (select count(*) a5 from da_household where v5='"+name+"' and v9 >5 and sys_standard='国家级贫困人口')e )";
					
					SQLAdapter hu_sqlAdapter=new SQLAdapter(hu_sql);
					List<Map> hu_list = this.getBySqlMapper.findRecords(hu_sqlAdapter);
					
					for (Map val : hu_list) {
						obj1.put("a1", val.get("a1"));
						obj1.put("a2", val.get("a2"));
						obj1.put("a3", val.get("a3"));
						obj1.put("a4", val.get("a4"));
						obj1.put("a5", val.get("a5"));
						jsonArray1.add(obj1);
					}
					
					//贫困户大于20人的村
					String cha_sql="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where v5='"+name+"' and sys_standard='国家级贫困人口')a GROUP by a.v5)ff  ";
					SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
					JSONArray jsonArray2=new JSONArray();
					JSONObject obj2=new JSONObject();
					List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
					if(cha_list.size()>0&&cha_list.get(0)!=null){
						String main=cha_list.get(0).get("num").toString();
						int main1=Integer.parseInt(main);
						int count1=Integer.parseInt(count);
						if(count1>main1){
							obj2.put("num",0);
							jsonArray2.add(obj2);
						}else{
							String sql1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household where v5='"+name+"' and sys_standard='国家级贫困人口')a GROUP by a.v5)ff where ff.num>"+count+"";
							SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
							List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter1);
							
							obj2.put("num",list2.get(0).get("num"));
							jsonArray2.add(obj2);
						}
					}else{
						obj2.put("num",0);
						jsonArray2.add(obj2);
					}
					
				
					
					//市标贫困人口
					String sql_1 = "select v4 as area,count(v5) as num from da_household where v5='"+name+"' and sys_standard='市级低收入人口' group by v5";
					SQLAdapter sqlAdapter_1 = new SQLAdapter(sql_1);
					List<Map> list_1 = this.getBySqlMapper.findRecords(sqlAdapter_1);
					JSONArray jsonArray_1=new JSONArray();
					JSONObject obj_1=new JSONObject();
					for (Map val : list_1) {
						obj_1.put("area", val.get("area"));
						obj_1.put("num", val.get("num"));
						jsonArray_1.add(obj_1);
					}
					
					//市标家庭成员大于数
					String hu_sql_1="select * from ((select count(*) a1 from da_household where v5='"+name+"' and v9 ='1' and sys_standard='市级低收入人口') a join"+
							" (select count(*) a2 from da_household where v5='"+name+"' and v9 ='2' and sys_standard='市级低收入人口')b  join "+
							" (select count(*) a3 from da_household where v5='"+name+"' and v9 ='3' and sys_standard='市级低收入人口')c  join "+
							" (select count(*) a4 from da_household where v5='"+name+"' and v9 ='4' and sys_standard='市级低收入人口')d  join "+
							" (select count(*) a5 from da_household where v5='"+name+"' and v9 >5 and sys_standard='市级低收入人口')e )";
				
					SQLAdapter hu_sqlAdapter_1=new SQLAdapter(hu_sql_1);
					List<Map> hu_list_1 = this.getBySqlMapper.findRecords(hu_sqlAdapter_1);
					JSONArray jsonArray1_1=new JSONArray();
					JSONObject obj1_1=new JSONObject();
					for (Map val : hu_list_1) {
						obj1_1.put("a1", val.get("a1"));
						obj1_1.put("a2", val.get("a2"));
						obj1_1.put("a3", val.get("a3"));
						obj1_1.put("a4", val.get("a4"));
						obj1_1.put("a5", val.get("a5"));
						jsonArray1_1.add(obj1_1);
					}
					
					//市贫贫困户大于20人的村
					String cha_sql_1="select max(num) num from ( select count(*) num ,v5 from (select v5 from da_household where v5='"+name+"' and sys_standard='市级低收入人口')a GROUP by a.v5)ff  ";
					SQLAdapter cha_sqlAdapter_1=new SQLAdapter(cha_sql_1);
					JSONArray jsonArray2_1=new JSONArray();
					JSONObject obj2_1=new JSONObject();
					List<Map> cha_list1=this.getBySqlMapper.findRecords(cha_sqlAdapter_1);
					if(cha_list1.size()>0&&cha_list1.get(0)!=null){
						String main_1=cha_list1.get(0).get("num").toString();
						int main1_1=Integer.parseInt(main_1);
						int count1_1=Integer.parseInt(count);
						if(count1_1>main1_1){
							obj2_1.put("num",0);
							jsonArray2_1.add(obj2_1);
						}else{
							String sql1_1="select count(*) num from ( select count(*) num ,v5 from (select v5 from da_household where v5='"+name+"' and sys_standard='市级低收入人口' )a GROUP by a.v5)ff where ff.num>"+count+"";
							SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
							
							List<Map> list2_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
							
							obj2_1.put("num",list2_1.get(0).get("num"));
							jsonArray2_1.add(obj2_1);
						}
					}else{
						obj2_1.put("num",0);
						jsonArray2_1.add(obj2_1);
					}
					
						response.getWriter().write("{\"result\":" + jsonArray.toString() + ",\"result1\":"+jsonArray1.toString()+",\"result2\":"+
						jsonArray2.toString()+",\"result3\":"+jsonArray_1.toString()+",\"result4\":"+jsonArray1_1.toString()+",\"result5\":"+jsonArray2_1.toString()+"}");
				}
		}
		return null;
	}
	/**
	 * 数据统计_帮扶情况指标
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getBfqkCountController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String type=request.getParameter("type");//市乡村标识
		String name=request.getParameter("name");//是乡村名称
		String count=request.getParameter("count");//大于数
		String distinction=request.getParameter("distinction");//0.易地搬迁1.帮扶责任人落实2.帮扶计划制定情况3.帮扶措施落实4.金融富民工程5.帮扶力量组织
		JSONArray jsonArray=new JSONArray();
		JSONObject object=new JSONObject();
		JSONArray jsonArray1=new JSONArray();
		JSONArray jsonArray1_1=new JSONArray();
		JSONObject object1=new JSONObject();
		String cha_sql="select com_name from sys_company where com_f_pkid=(select pkid from sys_company where com_name='"+name+"')";
		SQLAdapter cha_sqlAdapter=new SQLAdapter(cha_sql);
		List<Map> cha_list=this.getBySqlMapper.findRecords(cha_sqlAdapter);
		if(!"".equals(type)){
			if("0".equals(type)){//市级
				if("0".equals(distinction)){//纳入异地扶贫搬迁
					//国贫市级下纳入易地搬迁人数
					String  sql="select count(*) num from(select * from (select pkid  from da_household where sys_standard='国家级贫困人口') a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是')aa";
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
					if(list.size()>0){
						for (Map val : list) {
							object.put("num", val.get("num"));
							jsonArray.add(object);
						}
						if(cha_list.size()>0){
							for(int i=0;i<cha_list.size();i++){
								//国贫市级下旗县 的纳入易地搬迁人数
								String xia_sql="select count(*) num from(select * from (select pkid from da_household where v3='"+cha_list.get(i).get("com_name")+"' and "+
										" sys_standard='国家级贫困人口') a LEFT JOIN (select da_household_id, v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
								SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
								List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
								if(xia_list.size()>0){
									object1.put("name", cha_list.get(i).get("com_name"));
									if("".equals(xia_list.get(0))||xia_list.get(0)==null){
										object1.put("num", 0 );
									}else{
										object1.put("num", xia_list.get(0).get("num") );
									}
									jsonArray1.add(object1);
								}
							}
						}
						
					}
					//市贫市级下纳入易地搬迁人数
					String  sql1="select count(*) num from(select * from (select pkid  from da_household where sys_standard='市级低收入人口') a "+
								"LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是')aa";

					SQLAdapter sqlAdapter_1=new SQLAdapter(sql1);
					List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
					JSONArray jsonArray_1=new JSONArray();
					JSONObject object_1=new JSONObject();
					if(list_1.size()>0){
						for (Map val : list_1) {
							object_1.put("num", val.get("num"));
							jsonArray_1.add(object_1);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){//市贫市级下旗县 的纳入易地搬迁人数
							String xia_sql_1="select count(*) num from(select * from (select pkid from da_household where v3='"+cha_list.get(i).get("com_name")+"' and "+
									" sys_standard='市级低收入人口') a LEFT JOIN (select da_household_id, v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_1.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray1_1.toString()+"}");
				}else if("1".equals(distinction)){//帮扶责任人落实
					JSONArray jsonArray1_s=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶责任人落实-国贫
							String xia_sql="select ((select count(*) from (select * from (select pkid from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
							//帮扶责任人落实-市贫
							String xia_sql_1="select ((select count(*) from (select * from (select pkid from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_s.add(object1_1);
							}
							
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray1_s.toString()+"}");
				}else if("2".equals(distinction)){//帮扶计划制定情况
					JSONArray jsonArray2=new JSONArray();
					JSONArray jsonArray2_1=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶计划制定情况-国贫
							String xia_sql2="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia2=new SQLAdapter(xia_sql2);
							List<Map> xia_list2=this.getBySqlMapper.findRecords(sqlAdapter_xia2);
							JSONObject object2=new JSONObject();
							JSONObject object3=new JSONObject();
							if(xia_list2.size()>0){
								object2.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2.get(0))||xia_list2.get(0)==null){
									object2.put("num", 0 );
								}else{
									object2.put("num", xia_list2.get(0).get("num") );
								}
								
								jsonArray2.add(object2);
							}
							//帮扶计划制定情况-市贫
							String xia_sql2_1="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v3='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia2_1=new SQLAdapter(xia_sql2_1);
							List<Map> xia_list2_1=this.getBySqlMapper.findRecords(sqlAdapter_xia2_1);
							JSONObject object2_1=new JSONObject();
							JSONObject object3_1=new JSONObject();
							
							if(xia_list2_1.size()>0){
								object2_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2_1.get(0))||xia_list2_1.get(0)==null){
									object2_1.put("num", 0 );
								}else{
									object2_1.put("num", xia_list2_1.get(0).get("num") );
								}
								
								jsonArray2_1.add(object2_1);
							}
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray2_1.toString()+"}");
				}else if("3".equals(distinction)){//帮扶措施落实情况
					JSONArray jsonArray3=new JSONArray();
					JSONArray jsonArray3_1=new JSONArray();
					JSONObject object3=new JSONObject();
					JSONObject object3_1=new JSONObject();
					//帮扶措施落实情况-国贫
					String xia_sql3="select t1.com_name as b1,b2,b12 from sys_company t1 "+
							" left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v3) t2 on t1.com_name=t2.v3 "+
							" left join (select v3,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口' group by v3) t12 on t1.com_name=t12.v3"+
							" where com_f_pkid=(select pkid from sys_company where com_f_pkid is null) and b2>0 order by b2 desc";
					SQLAdapter sqlAdapter_xia3=new SQLAdapter(xia_sql3);
					List<Map> xia_list3=this.getBySqlMapper.findRecords(sqlAdapter_xia3);
					if(xia_list3.size()>0){
						for(Map val:xia_list3){
							
							object3.put("name", val.get("b1"));
							object3.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							jsonArray3.add(object3);
						}
						
					}
					//帮扶措施落实情况-市贫
					String xia_sql3_1="select t1.com_name as b1,b2,b12 from sys_company t1 "+
							" left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v3) t2 on t1.com_name=t2.v3 "+
							" left join (select v3,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v3) t12 on t1.com_name=t12.v3"+
							" where com_f_pkid=(select pkid from sys_company where com_f_pkid is null) and b2>0 order by b2 desc";
					SQLAdapter sqlAdapter_xia3_1=new SQLAdapter(xia_sql3_1);
					List<Map> xia_list3_1=this.getBySqlMapper.findRecords(sqlAdapter_xia3_1);
					if(xia_list3_1.size()>0){
						for(Map val:xia_list3_1){
							object3_1.put("name", val.get("b1"));
							object3_1.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							jsonArray3_1.add(object3_1);
							
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray3.toString()+",\"result2\":"+jsonArray3_1.toString()+"}");
				}
				
			}else if("1".equals(type)){//旗县
				if("0".equals(distinction)){//纳入异地扶贫搬迁
					//国贫
					String sql ="select count(*) num from(select * from (select pkid from da_household where v3='"+name+"' and sys_standard='国家级贫困人口') "+
							"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
					if(list.size()>0){
						for (Map val : list) {
							object.put("num", val.get("num"));
							jsonArray.add(object);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							String xia_sql="select count(*) num from(select * from (select pkid from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) "+
									"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
						}
					}
					//市贫
					//市贫市级下纳入易地搬迁人数
					String  sql1="select count(*) num from(select * from (select pkid from da_household where v3='"+name+"' and sys_standard='市级低收入人口') "+
							"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";

					SQLAdapter sqlAdapter_1=new SQLAdapter(sql1);
					List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
					JSONArray jsonArray_1=new JSONArray();
					JSONObject object_1=new JSONObject();
					if(list_1.size()>0){
						for (Map val : list_1) {
							object_1.put("num", val.get("num"));
							jsonArray_1.add(object_1);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){//市贫市级下旗县 的纳入易地搬迁人数
							String xia_sql_1="select count(*) num from(select * from (select pkid from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) "+
									"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_1.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray1_1.toString()+"}");
				}else if("1".equals(distinction)){
					JSONArray jsonArray1_s=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							
							//帮扶责任人落实-国贫
							String xia_sql="select ((select count(*) from (select * from (select pkid from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
							//帮扶责任人落实-市贫
							String xia_sql_1="select ((select count(*) from (select * from (select pkid from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_s.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray1_s.toString()+"}");
				}else if("2".equals(distinction)){
					JSONArray jsonArray2=new JSONArray();
					JSONArray jsonArray2_1=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶计划制定情况-国贫
							String xia_sql2="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia2=new SQLAdapter(xia_sql2);
							List<Map> xia_list2=this.getBySqlMapper.findRecords(sqlAdapter_xia2);
							JSONObject object2=new JSONObject();
							JSONObject object3=new JSONObject();
							if(xia_list2.size()>0){
								object2.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2.get(0))||xia_list2.get(0)==null){
									object2.put("num", 0 );
								}else{
									object2.put("num", xia_list2.get(0).get("num") );
								}
								
								jsonArray2.add(object2);
							}
							//帮扶计划制定情况-市贫
							String xia_sql2_1="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v4='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia2_1=new SQLAdapter(xia_sql2_1);
							List<Map> xia_list2_1=this.getBySqlMapper.findRecords(sqlAdapter_xia2_1);
							JSONObject object2_1=new JSONObject();
							JSONObject object3_1=new JSONObject();
							
							if(xia_list2_1.size()>0){
								object2_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2_1.get(0))||xia_list2_1.get(0)==null){
									object2_1.put("num", 0 );
								}else{
									object2_1.put("num", xia_list2_1.get(0).get("num") );
								}
								
								jsonArray2_1.add(object2_1);
							}
						
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray2_1.toString()+"}");
				}else if("3".equals(distinction)){//帮扶措施落实情况
					JSONArray jsonArray3=new JSONArray();
					JSONArray jsonArray3_1=new JSONArray();
					JSONObject object3=new JSONObject();
					JSONObject object3_1=new JSONObject();
					//帮扶措施落实情况-国贫
					//帮扶措施落实情况-国贫
					String xia_sql3="select t1.com_name as b1,b2,b12 from sys_company t1 "+
							" left join (select v4,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v4) t2 on t1.com_name=t2.v4  "+
							" left join (select v4,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口' group by v4) t12 on t1.com_name=t12.v4"+
							" where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc";
					SQLAdapter sqlAdapter_xia3=new SQLAdapter(xia_sql3);
					List<Map> xia_list3=this.getBySqlMapper.findRecords(sqlAdapter_xia3);
					if(xia_list3.size()>0){
						for(Map val:xia_list3){
							object3.put("name", val.get("b1"));
							if("".equals(val.get("b12"))||val.get("b12")==null){
								object3.put("num",0);
							}else{
								object3.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							}
//							object3.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							jsonArray3.add(object3);
						}
						
					}
					//帮扶措施落实情况-市贫
					String xia_sql3_1="select t1.com_name as b1,b2,b12 from sys_company t1 "+
							" left join (select v4,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v4) t2 on t1.com_name=t2.v4  "+
							" left join (select v4,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v4) t12 on t1.com_name=t12.v4"+
							" where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc";
					SQLAdapter sqlAdapter_xia3_1=new SQLAdapter(xia_sql3_1);
					List<Map> xia_list3_1=this.getBySqlMapper.findRecords(sqlAdapter_xia3_1);
					if(xia_list3_1.size()>0){
						for(Map val:xia_list3_1){
							object3_1.put("name", val.get("b1"));
							if("".equals(val.get("b12"))||val.get("b12")==null){
								object3_1.put("num",0);
							}else{
								object3_1.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							}
							jsonArray3_1.add(object3_1);
							
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray3.toString()+",\"result2\":"+jsonArray3_1.toString()+"}");
				}
			}else if("2".equals(type)){//苏木乡
				if("0".equals(distinction)){//纳入异地扶贫搬迁
					//国贫
					String sql ="select count(*) num from(select * from (select pkid from da_household where v4='"+name+"' and sys_standard='国家级贫困人口') "+
							"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
					if(list.size()>0){
						for (Map val : list) {
							object.put("num", val.get("num"));
							jsonArray.add(object);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							String xia_sql="select count(*) num from(select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) "+
									"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
						}
					}
					//市贫
					//市贫市级下纳入易地搬迁人数
					String  sql1="select count(*) num from(select * from (select pkid from da_household where v4='"+name+"' and sys_standard='市级低收入人口') "+
							"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";

					SQLAdapter sqlAdapter_1=new SQLAdapter(sql1);
					List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
					JSONArray jsonArray_1=new JSONArray();
					JSONObject object_1=new JSONObject();
					if(list_1.size()>0){
						for (Map val : list_1) {
							object_1.put("num", val.get("num"));
							jsonArray_1.add(object_1);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){//市贫市级下旗县 的纳入易地搬迁人数
							String xia_sql_1="select count(*) num from(select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) "+
									"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_1.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray1_1.toString()+"}");
				}else if("1".equals(distinction)){//帮扶责任人落实
					JSONArray jsonArray1_s=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶责任人落实-国贫
							String xia_sql="select ((select count(*) from (select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
							//帮扶责任人落实-市贫
							String xia_sql_1="select ((select count(*) from (select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_s.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray1_s.toString()+"}");
				}else if("2".equals(distinction)){//帮扶计划制定情况
					JSONArray jsonArray2=new JSONArray();
					JSONArray jsonArray2_1=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶计划制定情况-国贫
							String xia_sql2="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia2=new SQLAdapter(xia_sql2);
							List<Map> xia_list2=this.getBySqlMapper.findRecords(sqlAdapter_xia2);
							JSONObject object2=new JSONObject();
							JSONObject object3=new JSONObject();
							if(xia_list2.size()>0){
								object2.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2.get(0))||xia_list2.get(0)==null){
									object2.put("num", 0 );
								}else{
									object2.put("num", xia_list2.get(0).get("num") );
								}
								
								jsonArray2.add(object2);
							}
							//帮扶计划制定情况-市贫
							String xia_sql2_1="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia2_1=new SQLAdapter(xia_sql2_1);
							List<Map> xia_list2_1=this.getBySqlMapper.findRecords(sqlAdapter_xia2_1);
							JSONObject object2_1=new JSONObject();
							JSONObject object3_1=new JSONObject();
							
							if(xia_list2_1.size()>0){
								object2_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2_1.get(0))||xia_list2_1.get(0)==null){
									object2_1.put("num", 0 );
								}else{
									object2_1.put("num", xia_list2_1.get(0).get("num") );
								}
								
								jsonArray2_1.add(object2_1);
							}
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray2_1.toString()+"}");
				}else if("3".equals(distinction)){
					JSONArray jsonArray3=new JSONArray();
					JSONArray jsonArray3_1=new JSONArray();
					JSONObject object3=new JSONObject();
					JSONObject object3_1=new JSONObject();
					//帮扶措施落实情况-国贫
					String xia_sql3="select t1.com_name as b1,b2,b12 from sys_company t1 "+
							" left join (select v5,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v5) t2 on t1.com_name=t2.v5 "+
							" left join (select v5,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口' group by v5) t12 on t1.com_name=t12.v5"+
							" where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc";
					SQLAdapter sqlAdapter_xia3=new SQLAdapter(xia_sql3);
					List<Map> xia_list3=this.getBySqlMapper.findRecords(sqlAdapter_xia3);
					if(xia_list3.size()>0){
						for(Map val:xia_list3){
							object3.put("name", val.get("b1"));
							if("".equals(val.get("b12"))||val.get("b12")==null){
								object3.put("num",0);
							}else{
								object3.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							}
							
							jsonArray3.add(object3);
						}
					}
					//帮扶措施落实情况-市贫
					String xia_sql3_1="select t1.com_name as b1,b2,b12 from sys_company t1 "+
							" left join (select v5,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v5) t2 on t1.com_name=t2.v5 "+
							" left join (select v5,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v5) t12 on t1.com_name=t12.v5"+
							" where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc";
					SQLAdapter sqlAdapter_xia3_1=new SQLAdapter(xia_sql3_1);
					List<Map> xia_list3_1=this.getBySqlMapper.findRecords(sqlAdapter_xia3_1);
					if(xia_list3_1.size()>0){
						for(Map val:xia_list3_1){
							object3_1.put("name", val.get("b1"));
							if("".equals(val.get("b12"))||val.get("b12")==null){
								object3_1.put("num",0);
							}else{
								object3_1.put("num",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
							}
							jsonArray3_1.add(object3_1);
							
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray3.toString()+",\"result2\":"+jsonArray3_1.toString()+"}");
				}
			
				
			}else if("3".equals(type)){//嘎查村
				if("0".equals(distinction)){//纳入异地扶贫搬迁
					//国贫
					String sql ="select count(*) num from(select * from (select pkid from da_household where v5='"+name+"' and sys_standard='国家级贫困人口') "+
							"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
					SQLAdapter sqlAdapter=new SQLAdapter(sql);
					List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
					if(list.size()>0){
						for (Map val : list) {
							object.put("num", val.get("num"));
							jsonArray.add(object);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							String xia_sql="select count(*) num from(select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) "+
									"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
						}
					}
					//市贫
					//市贫市级下纳入易地搬迁人数
					String  sql1="select count(*) num from(select * from (select pkid from da_household where v5='"+name+"' and sys_standard='市级低收入人口') "+
							"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";

					SQLAdapter sqlAdapter_1=new SQLAdapter(sql1);
					List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
					JSONArray jsonArray_1=new JSONArray();
					JSONObject object_1=new JSONObject();
					if(list_1.size()>0){
						for (Map val : list_1) {
							object_1.put("num", val.get("num"));
							jsonArray_1.add(object_1);
						}
					}
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){//市贫市级下旗县 的纳入易地搬迁人数
							String xia_sql_1="select count(*) num from(select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) "+
									"a LEFT JOIN (select da_household_id,v3 sf from da_life) b on a.pkid =b.da_household_id where b.sf='是' )aa";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_1.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray_1.toString()+",\"result3\":"+jsonArray1_1.toString()+"}");
				}else if("1".equals(distinction)){
					JSONArray jsonArray1_s=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							
							//帮扶责任人落实-国贫
							String xia_sql="select ((select count(*) from (select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia=new SQLAdapter(xia_sql);
							List<Map> xia_list=this.getBySqlMapper.findRecords(sqlAdapter_xia);
							if(xia_list.size()>0){
								object1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list.get(0))||xia_list.get(0)==null){
									object1.put("num", 0 );
								}else{
									object1.put("num", xia_list.get(0).get("num") );
								}
								
								jsonArray1.add(object1);
							}
							//帮扶责任人落实-市贫
							String xia_sql_1="select ((select count(*) from (select * from (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口' ) a LEFT JOIN"+
									" (select da_household_id from sys_personal_household_many) b on a.pkid=b.da_household_id where b.da_household_id is not null GROUP BY b.da_household_id )aa)"+
									"/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia_1=new SQLAdapter(xia_sql_1);
							List<Map> xia_list_1=this.getBySqlMapper.findRecords(sqlAdapter_xia_1);
							
							JSONObject object1_1=new JSONObject();
							if(xia_list_1.size()>0){
								object1_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list_1.get(0))||xia_list_1.get(0)==null){
									object1_1.put("num", 0 );
								}else{
									object1_1.put("num", xia_list_1.get(0).get("num") );
								}
								
								jsonArray1_s.add(object1_1);
							}
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray1_s.toString()+"}");
				}else if("2".equals(distinction)){
					JSONArray jsonArray2=new JSONArray();
					JSONArray jsonArray2_1=new JSONArray();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶计划制定情况-国贫
							String xia_sql2="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='国家级贫困人口')) num";
							SQLAdapter sqlAdapter_xia2=new SQLAdapter(xia_sql2);
							List<Map> xia_list2=this.getBySqlMapper.findRecords(sqlAdapter_xia2);
							JSONObject object2=new JSONObject();
							JSONObject object3=new JSONObject();
							if(xia_list2.size()>0){
								object2.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2.get(0))||xia_list2.get(0)==null){
									object2.put("num", 0 );
								}else{
									object2.put("num", xia_list2.get(0).get("num") );
								}
								
								jsonArray2.add(object2);
							}
							//帮扶计划制定情况-市贫
							String xia_sql2_1="select ((select count(*) from (select * from (select da_household_id from da_help_info where v3 is not null and v3!='' ) a LEFT JOIN "+
									" (select pkid from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口') b on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id "+
									")aa)/(select count(*) from da_household where v5='"+cha_list.get(i).get("com_name")+"' and sys_standard='市级低收入人口')) num";
							SQLAdapter sqlAdapter_xia2_1=new SQLAdapter(xia_sql2_1);
							List<Map> xia_list2_1=this.getBySqlMapper.findRecords(sqlAdapter_xia2_1);
							JSONObject object2_1=new JSONObject();
							JSONObject object3_1=new JSONObject();
							
							if(xia_list2_1.size()>0){
								object2_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list2_1.get(0))||xia_list2_1.get(0)==null){
									object2_1.put("num", 0 );
								}else{
									object2_1.put("num", xia_list2_1.get(0).get("num") );
								}
								
								jsonArray2_1.add(object2_1);
							}
						
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray2_1.toString()+"}");
				}else if("3".equals(distinction)){
					JSONArray jsonArray3=new JSONArray();
					JSONArray jsonArray3_1=new JSONArray();
					JSONObject object3=new JSONObject();
					JSONObject object3_1=new JSONObject();
					if(cha_list.size()>0){
						for(int i=0;i<cha_list.size();i++){
							//帮扶措施落实情况-国贫
							String xia_sql3="select ( (select count(*) from (select * from (select da_household_id from da_help_measures  ) a LEFT JOIN (select pkid from da_household where v5='"+ cha_list.get(i).get("com_name")+"') b"+
									" on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id )aa)/(select count(*) from da_household where v5='"+ cha_list.get(i).get("com_name")+"')) num";
							SQLAdapter sqlAdapter_xia3=new SQLAdapter(xia_sql3);
							List<Map> xia_list3=this.getBySqlMapper.findRecords(sqlAdapter_xia3);
							if(xia_list3.size()>0){
								object3.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list3.get(0))||xia_list3.get(0)==null){
									object3.put("num", 0 );
								}else{
									object3.put("num", xia_list3.get(0).get("num") );
								}
								
								jsonArray3.add(object3);
							}
							//帮扶措施落实情况-市贫
							String xia_sql3_1="select ( (select count(*) from (select * from (select da_household_id from da_help_measures  ) a LEFT JOIN (select pkid from da_household where v5='"+ cha_list.get(i).get("com_name")+"') b"+
									" on a.da_household_id=b.pkid where b.pkid is not null GROUP BY a.da_household_id )aa)/(select count(*) from da_household where v5='"+ cha_list.get(i).get("com_name")+"')) num";
							SQLAdapter sqlAdapter_xia3_1=new SQLAdapter(xia_sql3_1);
							List<Map> xia_list3_1=this.getBySqlMapper.findRecords(sqlAdapter_xia3_1);
							if(xia_list3_1.size()>0){
								object3_1.put("name", cha_list.get(i).get("com_name"));
								if("".equals(xia_list3_1.get(0))||xia_list3_1.get(0)==null){
									object3_1.put("num", 0 );
								}else{
									object3_1.put("num", xia_list3_1.get(0).get("num") );
								}
								
								jsonArray3_1.add(object3_1);
							}
						
						}
					}
					response.getWriter().write("{\"result1\":"+jsonArray3.toString()+",\"result2\":"+jsonArray3_1.toString()+"}");
				}
			
				
			}
			
		}else{
			if("2".equals(distinction)){//金融富民工程
				
			}else if("4".equals(distinction)){//帮扶力量组织
				String sql ="select * from (select count(*) bfr from sys_personal )a ,(select count(*) dw from da_company)b";
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				if(list.size()>0){
					object.put("bfr", list.get(0).get("bfr"));
					object.put("dw", list.get(0).get("dw"));
					jsonArray.add(object);
					response.getWriter().write("{\"result\":"+jsonArray.toString()+"}");
				}else{
					object.put("bfr", 0);
					object.put("dw", 0);
					jsonArray.add(object);
					response.getWriter().write("{\"result\":"+jsonArray.toString()+"}");
				}
			}
		}
		
		
		return null;
	}
	/**
	 * 数据统计_帮扶成效指标
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getBfcxCountController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		return null;
	}
	/**
	 * 数据统计_工作考核指标
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getGzkhCountController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String type=request.getParameter("type");//市乡村标识
		String name=request.getParameter("name");//是乡村名称
		String distinction=request.getParameter("distinction");
		JSONArray jsonArray=new JSONArray();
		JSONObject obj=new JSONObject();
		DecimalFormat df = new DecimalFormat("0.00");
		
		String xzqh = "select com_name from sys_company where com_f_pkid is null";
		SQLAdapter xzqh_sqlAdapter = new SQLAdapter (xzqh);
		List<Map> xzqh_list = this.getBySqlMapper.findRecords(xzqh_sqlAdapter);
		String shi_name = "";
		if(xzqh_list.size()>0){
			shi_name = xzqh_list.get(0).get("com_name").toString();
		}
		if("0".equals(type)){//市
			if("0".equals(distinction)){
				//帮扶台账填报完成率-国贫
				String sql = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql += " left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v3) t2 on t1.com_name=t2.v3 ";
				sql += " left join (select v3,COUNT(*) as b4 from b4_t  where sys_standard='国家级贫困人口' group by v3) t4 on t1.com_name=t4.v3";
				sql += " left join (select v3,COUNT(*) as b6 from b6_t  where sys_standard='国家级贫困人口' group by v3) t6 on t1.com_name=t6.v3";
				sql += " left join (select v3,COUNT(*) as b7 from b7_t where sys_standard='国家级贫困人口' group by v3) t7 on t1.com_name=t7.v3";
				sql += " left join (select v3,COUNT(*) as b8 from b8_t  where sys_standard='国家级贫困人口' group by v3) t8 on t1.com_name=t8.v3 ";
				sql += " left join (select v3,COUNT(*) as b9 from b9_t  where sys_standard='国家级贫困人口' group by v3) t9 on t1.com_name=t9.v3";
				sql += " left join (select v3,COUNT(*) as b10 from b10_t where sys_standard='国家级贫困人口' group by v3) t10 on t1.com_name=t10.v3 ";
				sql += " left join (select v3,COUNT(*) as b11 from b11_t where sys_standard='国家级贫困人口' group by v3) t11 on t1.com_name=t11.v3 ";
				sql += " left join (select v3,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口' group by v3) t12 on t1.com_name=t12.v3";
				sql += " left join (select v3,COUNT(*) as b13 from b13_t where sys_standard='国家级贫困人口' group by v3) t13 on t1.com_name=t13.v3 ";
				sql += " where com_f_pkid=(select pkid from sys_company where com_f_pkid is null) and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				for(Map val:list){
					obj.put("name",val.get("b1"));
					obj.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj.put("jbqk",0);
					}else{
						obj.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj.put("sctj",0);
					}else{
						obj.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj.put("shtj",0);
					}else{
						obj.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj.put("dqsr",0);
					}else{
						obj.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj.put("dqzc",0);
					}else{
						obj.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj.put("bfr",0);
					}else{
						obj.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj.put("bfjh",0);
					}else{
						obj.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj.put("bfcs",0);
					}else{
						obj.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj.put("zfqk",0);
					}else{
						obj.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray.add(obj);
					
				}
				//帮扶台账填报完成率-市贫
				String sql_1 = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql_1 += " left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v3) t2 on t1.com_name=t2.v3 ";
				sql_1 += " left join (select v3,COUNT(*) as b4 from b4_t  where sys_standard='市级低收入人口' group by v3) t4 on t1.com_name=t4.v3";
				sql_1 += " left join (select v3,COUNT(*) as b6 from b6_t  where sys_standard='市级低收入人口' group by v3) t6 on t1.com_name=t6.v3";
				sql_1 += " left join (select v3,COUNT(*) as b7 from b7_t where sys_standard='市级低收入人口' group by v3) t7 on t1.com_name=t7.v3";
				sql_1 += " left join (select v3,COUNT(*) as b8 from b8_t  where sys_standard='市级低收入人口' group by v3) t8 on t1.com_name=t8.v3 ";
				sql_1 += " left join (select v3,COUNT(*) as b9 from b9_t  where sys_standard='市级低收入人口' group by v3) t9 on t1.com_name=t9.v3";
				sql_1 += " left join (select v3,COUNT(*) as b10 from b10_t where sys_standard='市级低收入人口' group by v3) t10 on t1.com_name=t10.v3 ";
				sql_1 += " left join (select v3,COUNT(*) as b11 from b11_t where sys_standard='市级低收入人口' group by v3) t11 on t1.com_name=t11.v3 ";
				sql_1 += " left join (select v3,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v3) t12 on t1.com_name=t12.v3";
				sql_1 += " left join (select v3,COUNT(*) as b13 from b13_t where sys_standard='市级低收入人口' group by v3) t13 on t1.com_name=t13.v3 ";
				sql_1 += " where com_f_pkid=(select pkid from sys_company where com_f_pkid is null) and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for(Map val:list_1){
					obj_1.put("name",val.get("b1"));
					obj_1.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj_1.put("jbqk",0);
					}else{
						obj_1.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj_1.put("sctj",0);
					}else{
						obj_1.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj_1.put("shtj",0);
					}else{
						obj_1.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj_1.put("dqsr",0);
					}else{
						obj_1.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj_1.put("dqzc",0);
					}else{
						obj_1.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj_1.put("bfr",0);
					}else{
						obj_1.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj_1.put("bfjh",0);
					}else{
						obj_1.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj_1.put("bfcs",0);
					}else{
						obj_1.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj_1.put("zfqk",0);
					}else{
						obj_1.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray_1.add(obj_1);
					
				}
				response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray_1.toString()+"}");
			}else if("1".equals(distinction)){
				//帮扶人走访次数-国贫
				String sql_d="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口')";
				SQLAdapter sqlAdapter_d=new SQLAdapter(sql_d);
				List<Map> list_d=this.getBySqlMapper.findRecords(sqlAdapter_d);
				JSONArray jsonArray1=new JSONArray();
				JSONObject obj1=new JSONObject();
				obj1.put("name",shi_name);
				obj1.put("num", list_d.get(0).get("num")); 
				jsonArray1.add(obj1);
				JSONObject obj1_1=new JSONObject();
				JSONObject obj_1=new JSONObject();
				JSONArray jsonArray_1=new JSONArray();
				String sql="select com_name from sys_company where com_f_pkid=4";
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				for(int i=0;i<list.size();i++){
					String sql1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口' and v3='"+list.get(i).get("com_name")+"' )";
					SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
					List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
					if(list1.size()>0){
						obj.put("name",list.get(i).get("com_name"));
						obj.put("num",list1.get(0).get("num"));
					}else{
						obj.put("name",list.get(i).get("com_name"));
						obj.put("num",0);
					}
					jsonArray.add(obj);
					

					String sql1_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口' and v3='"+list.get(i).get("com_name")+"' )";
					SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
					List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
					if(list1_1.size()>0){
						obj_1.put("name",list.get(i).get("com_name"));
						obj_1.put("num",list1_1.get(0).get("num"));
					}else{
						obj_1.put("name",list.get(i).get("com_name"));
						obj_1.put("num",0);
					}
					jsonArray_1.add(obj_1);
				
				}
				//帮扶人走访次数-市贫
				String sql_d_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口')";
				SQLAdapter sqlAdapter_d_1=new SQLAdapter(sql_d_1);
				List<Map> list_d_1=this.getBySqlMapper.findRecords(sqlAdapter_d_1);
				JSONArray jsonArray1_1=new JSONArray();
				obj1_1.put("name", shi_name);
				obj1_1.put("num", list_d_1.get(0).get("num"));
				jsonArray1_1.add(obj1_1);
				
				response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray.toString()+",\"result3\":"+jsonArray1_1.toString()+",\"result4\":"+jsonArray_1.toString()+"}");
			}
		}else if("1".equals(type)){//县

			if("0".equals(distinction)){
				//帮扶台账填报完成率-国贫
				String sql = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql += " left join (select v4,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v4 ) t2 on t1.com_name=t2.v4 ";
				sql += " left join (select v4,COUNT(*) as b4 from b4_t where sys_standard='国家级贫困人口' group by v4) t4 on t1.com_name=t4.v4";
				sql += " left join (select v4,COUNT(*) as b6 from b6_t  where sys_standard='国家级贫困人口' group by v4) t6 on t1.com_name=t6.v4";
				sql += " left join (select v4,COUNT(*) as b7 from b7_t where sys_standard='国家级贫困人口' group by v4) t7 on t1.com_name=t7.v4";
				sql += " left join (select v4,COUNT(*) as b8 from b8_t  where sys_standard='国家级贫困人口' group by v4) t8 on t1.com_name=t8.v4 ";
				sql += " left join (select v4,COUNT(*) as b9 from b9_t where sys_standard='国家级贫困人口' group by v4) t9 on t1.com_name=t9.v4";
				sql += " left join (select v4,COUNT(*) as b10 from b10_t where sys_standard='国家级贫困人口' group by v4) t10 on t1.com_name=t10.v4 ";
				sql += " left join (select v4,COUNT(*) as b11 from b11_t  where sys_standard='国家级贫困人口' group by v4) t11 on t1.com_name=t11.v4 ";
				sql += " left join (select v4,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口' group by v4) t12 on t1.com_name=t12.v4";
				sql += " left join (select v4,COUNT(*) as b13 from b13_t  where sys_standard='国家级贫困人口' group by v4) t13 on t1.com_name=t13.v4 ";
				sql += " where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				for(Map val:list){
					obj.put("name",val.get("b1")==null?"-":val.get("b1"));
					obj.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj.put("jbqk",0);
					}else{
						obj.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj.put("sctj",0);
					}else{
						obj.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj.put("shtj",0);
					}else{
						obj.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj.put("dqsr",0);
					}else{
						obj.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj.put("dqzc",0);
					}else{
						obj.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj.put("bfr",0);
					}else{
						obj.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj.put("bfjh",0);
					}else{
						obj.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj.put("bfcs",0);
					}else{
						obj.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj.put("zfqk",0);
					}else{
						obj.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray.add(obj);
					
				}
				//帮扶台账填报完成率-市贫
				String sql_1 = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql_1 += " left join (select v4,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v4) t2 on t1.com_name=t2.v4 ";
				sql_1 += " left join (select v4,COUNT(*) as b4 from b4_t where sys_standard='市级低收入人口' group by v4) t4 on t1.com_name=t4.v4";
				sql_1 += " left join (select v4,COUNT(*) as b6 from b6_t where sys_standard='市级低收入人口' group by v4) t6 on t1.com_name=t6.v4";
				sql_1 += " left join (select v4,COUNT(*) as b7 from b7_t where sys_standard='市级低收入人口' group by v4) t7 on t1.com_name=t7.v4";
				sql_1 += " left join (select v4,COUNT(*) as b8 from b8_t where sys_standard='市级低收入人口'  group by v4) t8 on t1.com_name=t8.v4 ";
				sql_1 += " left join (select v4,COUNT(*) as b9 from b9_t where sys_standard='市级低收入人口' group by v4) t9 on t1.com_name=t9.v4";
				sql_1 += " left join (select v4,COUNT(*) as b10 from b10_t where sys_standard='市级低收入人口' group by v4) t10 on t1.com_name=t10.v4 ";
				sql_1 += " left join (select v4,COUNT(*) as b11 from b11_t  where sys_standard='市级低收入人口' group by v4) t11 on t1.com_name=t11.v4 ";
				sql_1 += " left join (select v4,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v4) t12 on t1.com_name=t12.v4";
				sql_1 += " left join (select v4,COUNT(*) as b13 from b13_t where sys_standard='市级低收入人口'  group by v4) t13 on t1.com_name=t13.v4 ";
				sql_1 += " where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for(Map val:list_1){
					obj_1.put("name",val.get("b1")==null?"-":val.get("b1"));
					obj_1.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj_1.put("jbqk",0);
					}else{
						obj_1.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj_1.put("sctj",0);
					}else{
						obj_1.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj_1.put("shtj",0);
					}else{
						obj_1.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj_1.put("dqsr",0);
					}else{
						obj_1.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj_1.put("dqzc",0);
					}else{
						obj_1.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj_1.put("bfr",0);
					}else{
						obj_1.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj_1.put("bfjh",0);
					}else{
						obj_1.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj_1.put("bfcs",0);
					}else{
						obj_1.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj_1.put("zfqk",0);
					}else{
						obj_1.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray_1.add(obj_1);
					
				}
				response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray_1.toString()+"}");
			}else if("1".equals(distinction)){
				//帮扶人走访次数-国贫
				String sql_d="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口' and v3='"+name+"')";
				SQLAdapter sqlAdapter_d=new SQLAdapter(sql_d);
				List<Map> list_d=this.getBySqlMapper.findRecords(sqlAdapter_d);
				JSONArray jsonArray1=new JSONArray();
				JSONObject obj1=new JSONObject();
				obj1.put("name", name);
				obj1.put("num", list_d.get(0).get("num"));
				jsonArray1.add(obj1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				String sql="select com_name from sys_company where com_f_pkid=(select pkid from sys_company where com_name='"+name+"')";
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				for(int i=0;i<list.size();i++){
					String sql1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口' and v4='"+list.get(i).get("com_name")+"' ) ";
					SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
					List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
					if(list1.size()>0){
						obj.put("name",list.get(i).get("com_name"));
						obj.put("num",list1.get(0).get("num"));
					}else{
						obj.put("name",list.get(i).get("com_name"));
						obj.put("num",0);
					}
					jsonArray.add(obj);

					String sql1_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口' and v4='"+list.get(i).get("com_name")+"' ) ";
					SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
					List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
					if(list1_1.size()>0){
						obj_1.put("name",list.get(i).get("com_name"));
						obj_1.put("num",list1_1.get(0).get("num"));
					}else{
						obj_1.put("name",list.get(i).get("com_name"));
						obj_1.put("num",0);
					}
					jsonArray_1.add(obj);
				
				}
				//帮扶人走访次数-市贫
				String sql_d_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口' and v3='"+name+"')";
				SQLAdapter sqlAdapter_d_1=new SQLAdapter(sql_d_1);
				List<Map> list_d_1=this.getBySqlMapper.findRecords(sqlAdapter_d_1);
				JSONArray jsonArray1_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				
				obj1_1.put("name", name);
				obj1_1.put("num", list_d_1.get(0).get("num"));
				jsonArray1_1.add(obj1_1);
				response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray.toString()+",\"result3\":"+jsonArray1_1.toString()+",\"result4\":"+jsonArray_1.toString()+"}");
				
			}
		
		}else if("2".equals(type)){//苏木乡
			if("0".equals(distinction)){
				//帮扶台账填报完成率-国贫
				String sql = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql += " left join (select v5,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v5) t2 on t1.com_name=t2.v5 ";
				sql += " left join (select v5,COUNT(*) as b4 from b4_t where sys_standard='国家级贫困人口' group by v5) t4 on t1.com_name=t4.v5";
				sql += " left join (select v5,COUNT(*) as b6 from b6_t where sys_standard='国家级贫困人口' group by v5) t6 on t1.com_name=t6.v5";
				sql += " left join (select v5,COUNT(*) as b7 from b7_t where sys_standard='国家级贫困人口' group by v5) t7 on t1.com_name=t7.v5";
				sql += " left join (select v5,COUNT(*) as b8 from b8_t where sys_standard='国家级贫困人口' group by v5) t8 on t1.com_name=t8.v5 ";
				sql += " left join (select v5,COUNT(*) as b9 from b9_t where sys_standard='国家级贫困人口' group by v5) t9 on t1.com_name=t9.v5";
				sql += " left join (select v5,COUNT(*) as b10 from b10_t where sys_standard='国家级贫困人口' group by v5) t10 on t1.com_name=t10.v5 ";
				sql += " left join (select v5,COUNT(*) as b11 from b11_t where sys_standard='国家级贫困人口'  group by v5) t11 on t1.com_name=t11.v5 ";
				sql += " left join (select v5,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口' group by v5) t12 on t1.com_name=t12.v5";
				sql += " left join (select v5,COUNT(*) as b13 from b13_t where sys_standard='国家级贫困人口' group by v5) t13 on t1.com_name=t13.v5 ";
				sql += " where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				for(Map val:list){
					obj.put("name",val.get("b1")==null?"-":val.get("b1"));
					obj.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj.put("jbqk",0);
					}else{
						obj.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj.put("sctj",0);
					}else{
						obj.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj.put("shtj",0);
					}else{
						obj.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj.put("dqsr",0);
					}else{
						obj.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj.put("dqzc",0);
					}else{
						obj.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj.put("bfr",0);
					}else{
						obj.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj.put("bfjh",0);
					}else{
						obj.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj.put("bfcs",0);
					}else{
						obj.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj.put("zfqk",0);
					}else{
						obj.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray.add(obj);
					
				}
				//帮扶台账填报完成率-市贫
				String sql_1 = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql_1 += " left join (select v5,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v5) t2 on t1.com_name=t2.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b4 from b4_t where sys_standard='市级低收入人口' group by v5) t4 on t1.com_name=t4.v5";
				sql_1 += " left join (select v5,COUNT(*) as b6 from b6_t where sys_standard='市级低收入人口' group by v5) t6 on t1.com_name=t6.v5";
				sql_1 += " left join (select v5,COUNT(*) as b7 from b7_t where sys_standard='市级低收入人口' group by v5) t7 on t1.com_name=t7.v5";
				sql_1 += " left join (select v5,COUNT(*) as b8 from b8_t where sys_standard='市级低收入人口' group by v5) t8 on t1.com_name=t8.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b9 from b9_t where sys_standard='市级低收入人口' group by v5) t9 on t1.com_name=t9.v5";
				sql_1 += " left join (select v5,COUNT(*) as b10 from b10_t where sys_standard='市级低收入人口' group by v5) t10 on t1.com_name=t10.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b11 from b11_t where sys_standard='市级低收入人口' group by v5) t11 on t1.com_name=t11.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v5) t12 on t1.com_name=t12.v5";
				sql_1 += " left join (select v5,COUNT(*) as b13 from b13_t where sys_standard='市级低收入人口'  group by v5) t13 on t1.com_name=t13.v5 ";
				sql_1 += " where com_f_pkid=(select pkid from sys_company where com_name='"+name+"') and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for(Map val:list_1){
					obj_1.put("name",val.get("b1")==null?"-":val.get("b1"));
					obj_1.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj_1.put("jbqk",0);
					}else{
						obj_1.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj_1.put("sctj",0);
					}else{
						obj_1.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj_1.put("shtj",0);
					}else{
						obj_1.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj_1.put("dqsr",0);
					}else{
						obj_1.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj_1.put("dqzc",0);
					}else{
						obj_1.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj_1.put("bfr",0);
					}else{
						obj_1.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj_1.put("bfjh",0);
					}else{
						obj_1.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj_1.put("bfcs",0);
					}else{
						obj_1.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj_1.put("zfqk",0);
					}else{
						obj_1.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray_1.add(obj_1);
					
				}
				response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray_1.toString()+"}");
			}else if("1".equals(distinction)){
				//帮扶人走访次数-国贫
				String sql_d="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口' and v4='"+name+"')";
				SQLAdapter sqlAdapter_d=new SQLAdapter(sql_d);
				List<Map> list_d=this.getBySqlMapper.findRecords(sqlAdapter_d);
				JSONArray jsonArray1=new JSONArray();
				JSONObject obj1=new JSONObject();
				obj1.put("name", name);
				obj1.put("num", list_d.get(0).get("num"));
				jsonArray1.add(obj1);
			
				String sql="select com_name from sys_company where com_f_pkid=(select pkid from sys_company where com_name='"+name+"')";
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for(int i=0;i<list.size();i++){
					String sql1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口' and v5='"+list.get(i).get("com_name")+"')";
					SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
					List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter1);
					if(list1.size()>0){
						obj.put("name",list.get(i).get("com_name"));
						obj.put("num",list1.get(0).get("num"));
					}else{
						obj.put("name",list.get(i).get("com_name"));
						obj.put("num",0);
					}
					jsonArray.add(obj);
					
					String sql1_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口' and v5='"+list.get(i).get("com_name")+"')";
					SQLAdapter sqlAdapter1_1=new SQLAdapter(sql1_1);
					List<Map> list1_1=this.getBySqlMapper.findRecords(sqlAdapter1_1);
					if(list1_1.size()>0){
						obj_1.put("name",list.get(i).get("com_name"));
						obj_1.put("num",list.get(0).get("num"));
					}else{
						obj_1.put("name",list.get(i).get("com_name"));
						obj_1.put("num",0);
					}
					jsonArray_1.add(obj_1);
				}
				//帮扶人走访次数-市贫
				String sql_d_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口' and v4='"+name+"')";
				SQLAdapter sqlAdapter_d_1=new SQLAdapter(sql_d_1);
				List<Map> list_d_1=this.getBySqlMapper.findRecords(sqlAdapter_d_1);
				JSONArray jsonArray1_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				
				obj1_1.put("name", name);
				obj1_1.put("num", list_d_1.get(0).get("num"));
				jsonArray1_1.add(obj1_1);
				response.getWriter().write("{\"result1\":"+jsonArray1.toString()+",\"result2\":"+jsonArray.toString()+",\"result3\":"+jsonArray1_1.toString()+",\"result4\":"+jsonArray_1.toString()+"}");
			}
		}else if("3".equals(type)){//嘎查村
			if("0".equals(distinction)){
				//帮扶台账填报完成率-国贫
				String sql = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql += " left join (select v5,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='国家级贫困人口' group by v5) t2 on t1.com_name=t2.v5 ";
				sql += " left join (select v5,COUNT(*) as b4 from b4_t where sys_standard='国家级贫困人口' group by v5) t4 on t1.com_name=t4.v5";
				sql += " left join (select v5,COUNT(*) as b6 from b6_t  where sys_standard='国家级贫困人口' group by v5) t6 on t1.com_name=t6.v5";
				sql += " left join (select v5,COUNT(*) as b7 from b7_t where sys_standard='国家级贫困人口' group by v5) t7 on t1.com_name=t7.v5";
				sql += " left join (select v5,COUNT(*) as b8 from b8_t where sys_standard='国家级贫困人口'  group by v5) t8 on t1.com_name=t8.v5 ";
				sql += " left join (select v5,COUNT(*) as b9 from b9_t  where sys_standard='国家级贫困人口' group by v5) t9 on t1.com_name=t9.v5";
				sql += " left join (select v5,COUNT(*) as b10 from b10_t where sys_standard='国家级贫困人口' group by v5) t10 on t1.com_name=t10.v5 ";
				sql += " left join (select v5,COUNT(*) as b11 from b11_t where sys_standard='国家级贫困人口' group by v5) t11 on t1.com_name=t11.v5 ";
				sql += " left join (select v5,COUNT(*) as b12 from b12_t where sys_standard='国家级贫困人口'  group by v5) t12 on t1.com_name=t12.v5";
				sql += " left join (select v5,COUNT(*) as b13 from b13_t where sys_standard='国家级贫困人口' group by v5) t13 on t1.com_name=t13.v5 ";
				sql += " where com_name='"+name+"' and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter=new SQLAdapter(sql);
				List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
				for(Map val:list){
					obj.put("name",val.get("b1")==null?"-":val.get("b1"));
					obj.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj.put("jbqk",0);
					}else{
						obj.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj.put("sctj",0);
					}else{
						obj.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj.put("shtj",0);
					}else{
						obj.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj.put("dqsr",0);
					}else{
						obj.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj.put("dqzc",0);
					}else{
						obj.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj.put("bfr",0);
					}else{
						obj.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj.put("bfjh",0);
					}else{
						obj.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj.put("bfcs",0);
					}else{
						obj.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj.put("zfqk",0);
					}else{
						obj.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray.add(obj);
					
				}
				//帮扶台账填报完成率-市贫
				String sql_1 = " select t1.com_name as b1,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13 from sys_company t1 ";
				sql_1 += " left join (select v5,COUNT(*) as b2,sum(v9) as b3 from da_household where sys_standard='市级低收入人口' group by v5) t2 on t1.com_name=t2.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b4 from b4_t where sys_standard='市级低收入人口' group by v5) t4 on t1.com_name=t4.v5";
				sql_1 += " left join (select v5,COUNT(*) as b6 from b6_t where sys_standard='市级低收入人口'  group by v5) t6 on t1.com_name=t6.v5";
				sql_1 += " left join (select v5,COUNT(*) as b7 from b7_t where sys_standard='市级低收入人口' group by v5) t7 on t1.com_name=t7.v5";
				sql_1 += " left join (select v5,COUNT(*) as b8 from b8_t where sys_standard='市级低收入人口'  group by v5) t8 on t1.com_name=t8.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b9 from b9_t where sys_standard='市级低收入人口'  group by v5) t9 on t1.com_name=t9.v5";
				sql_1 += " left join (select v5,COUNT(*) as b10 from b10_t where sys_standard='市级低收入人口'  group by v5) t10 on t1.com_name=t10.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b11 from b11_t where sys_standard='市级低收入人口'  group by v5) t11 on t1.com_name=t11.v5 ";
				sql_1 += " left join (select v5,COUNT(*) as b12 from b12_t where sys_standard='市级低收入人口' group by v5) t12 on t1.com_name=t12.v5";
				sql_1 += " left join (select v5,COUNT(*) as b13 from b13_t where sys_standard='市级低收入人口'  group by v5) t13 on t1.com_name=t13.v5 ";
				sql_1 += " where com_name='"+name+"' and b2>0 order by b2 desc ";
				
				SQLAdapter sqlAdapter_1=new SQLAdapter(sql_1);
				List<Map> list_1=this.getBySqlMapper.findRecords(sqlAdapter_1);
				JSONArray jsonArray_1=new JSONArray();
				JSONObject obj_1=new JSONObject();
				for(Map val:list_1){
					obj_1.put("name",val.get("b1")==null?"-":val.get("b1"));
					obj_1.put("pkh",val.get("b2")==null?"-":val.get("b2"));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj_1.put("jbqk",0);
					}else{
						obj_1.put("jbqk",(Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj_1.put("sctj",0);
					}else{
						obj_1.put("sctj",(Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj_1.put("shtj",0);
					}else{
						obj_1.put("shtj",(Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj_1.put("dqsr",0);
					}else{
						obj_1.put("dqsr",(Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj_1.put("dqzc",0);
					}else{
						obj_1.put("dqzc",(Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj_1.put("bfr",0);
					}else{
						obj_1.put("bfr",(Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj_1.put("bfjh",0);
					}else{
						obj_1.put("bfjh",(Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj_1.put("bfcs",0);
					}else{
						obj_1.put("bfcs",(Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj_1.put("zfqk",0);
					}else{
						obj_1.put("zfqk",(Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString())));
					}
					
					jsonArray_1.add(obj_1);
					
				}
				response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray_1.toString()+"}");
			}else if("1".equals(distinction)){//帮扶人走访次数
				//帮扶人走访次数-国贫
				String sql_d="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='国家级贫困人口' and v5='"+name+"')";
				SQLAdapter sqlAdapter_d=new SQLAdapter(sql_d);
				List<Map> list_d=this.getBySqlMapper.findRecords(sqlAdapter_d);
				JSONArray jsonArray1=new JSONArray();
				JSONObject obj1=new JSONObject();
				obj1.put("name", name);
				obj1.put("num", list_d.get(0).get("num"));
				jsonArray1.add(obj1);
				//帮扶人走访次数-市贫
				String sql_d_1="select count(*) num from da_help_visit where da_household_id in(select pkid from da_household where sys_standard='市级低收入人口' and v4='"+name+"')";
				SQLAdapter sqlAdapter_d_1=new SQLAdapter(sql_d_1);
				List<Map> list_d_1=this.getBySqlMapper.findRecords(sqlAdapter_d_1);
				JSONArray jsonArray1_1=new JSONArray();
				JSONObject obj1_1=new JSONObject();
				obj1_1.put("name", name);
				obj1_1.put("num", list_d_1.get(0).get("num"));
				jsonArray1_1.add(obj1_1);
				response.getWriter().write("{\"result\":"+jsonArray1.toString()+",\"result1\":"+jsonArray1_1.toString()+"}");
			}
		
		}
//		
		return null;
	}
	/**
	 * 数据统计_户详情指标
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getHuMessageController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String str=request.getParameter("str");
//		String str="高二晓";
		
		String sql="select * from da_household a left join (select pic_pkid,pic_path from da_pic where pic_type='4') b on a.pkid=b.pic_pkid where v6='"+str+"' or v8='"+str+"' ";
		SQLAdapter sqlAdapter=new SQLAdapter(sql);
		List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray=new JSONArray();
		JSONArray jsonArray2=new JSONArray();
		JSONArray jsonArray3=new JSONArray();
		JSONArray jsonArray4=new JSONArray();
		JSONArray jsonArray5=new JSONArray();
		JSONArray jsonArray6=new JSONArray();
		JSONArray jsonArray7=new JSONArray();
		
		JSONObject obj=new JSONObject();
		JSONObject obj2=new JSONObject();
		JSONObject obj3=new JSONObject();
		JSONObject obj4=new JSONObject();
		JSONObject obj5=new JSONObject();
		JSONObject obj6=new JSONObject();
		JSONObject obj7=new JSONObject();
		if(list.size()>0){
			for(Map val:list){
				obj.put("v3",val.get("v3")==null?"":val.get("v3"));
				obj.put("v4",val.get("v4")==null?"":val.get("v4"));
				obj.put("v5",val.get("v5")==null?"":val.get("v5"));
				obj.put("v6","".equals(val.get("v6"))||val.get("v6")==null?"":val.get("v6"));
				obj.put("v7","".equals(val.get("v7"))||val.get("v7")==null?"":val.get("v7"));
				obj.put("v8","".equals(val.get("v8"))||val.get("v8")==null?"":val.get("v8"));
				obj.put("v9","".equals(val.get("v9"))||val.get("v9")==null?"":val.get("v9"));
				obj.put("v10","".equals(val.get("v10"))||val.get("v10")==null?"户主":val.get("v10"));
				obj.put("v11","".equals(val.get("v11"))||val.get("v11")==null?"":val.get("v11"));
				obj.put("v12","".equals(val.get("v12"))||val.get("v12")==null?"":val.get("v12"));
				obj.put("v13","".equals(val.get("v13"))||val.get("v13")==null?"":val.get("v13"));
				obj.put("v14","".equals(val.get("v14"))||val.get("v14")==null?"":val.get("v14"));
				obj.put("v15","".equals(val.get("v15"))||val.get("v15")==null?"":val.get("v15"));
				obj.put("v16","".equals(val.get("v16"))||val.get("v16")==null?"":val.get("v16"));
				obj.put("v17","".equals(val.get("v17"))||val.get("v17")==null?"":val.get("v17"));
				obj.put("v18","".equals(val.get("v18"))||val.get("v18")==null?"":val.get("v18"));
				obj.put("v19","".equals(val.get("v19"))||val.get("v19")==null?"":val.get("v19"));
				obj.put("v20","".equals(val.get("v20"))||val.get("v20")==null?"":val.get("v20"));
				obj.put("v21","".equals(val.get("v21"))||val.get("v21")==null?"":val.get("v21"));
				obj.put("v22","".equals(val.get("v22"))||val.get("v22")==null?"":val.get("v22"));
				obj.put("v22","".equals(val.get("v22"))||val.get("v22")==null?"":val.get("v22"));
				obj.put("v23","".equals(val.get("v23"))||val.get("v23")==null?"":val.get("v23"));
				obj.put("v25","".equals(val.get("v25"))||val.get("v25")==null?"":val.get("v25"));
				obj.put("v28","".equals(val.get("v28"))||val.get("v28")==null?"":val.get("v28"));
				obj.put("sys_standard","".equals(val.get("sys_standard"))||val.get("sys_standard")==null?"":val.get("sys_standard"));
				obj.put("v29","".equals(val.get("v29"))||val.get("v29")==null?"":val.get("v29"));
				obj.put("v30","".equals(val.get("v30"))||val.get("v30")==null?"":val.get("v30"));
				obj.put("v31","".equals(val.get("v31"))||val.get("v31")==null?"":val.get("v31"));
				obj.put("v32","".equals(val.get("v32"))||val.get("v32")==null?"":val.get("v32"));
				obj.put("v33","".equals(val.get("v33"))||val.get("v33")==null?"":val.get("v33"));
				obj.put("pic_path","".equals(val.get("pic_path"))||val.get("pic_path")==null?"":val.get("pic_path"));
			}
			jsonArray.add(obj);
			//家庭成员
			String sql2="select * from da_member a LEFT JOIN (select pic_path,pic_pkid from  da_pic where pic_type='5')b "+
						" on a.pkid =b.pic_pkid where da_household_id="+list.get(0).get("pkid");
			SQLAdapter sqlAdapter2=new SQLAdapter(sql2);
			List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter2);
			if(list2.size()>0){
				for(Map val:list2){
					obj2.put("v6","".equals(val.get("v6"))||val.get("v6")==null?"":val.get("v6"));
					obj2.put("v7","".equals(val.get("v7"))||val.get("v7")==null?"":val.get("v7"));
					obj2.put("v8","".equals(val.get("v8"))||val.get("v8")==null?"":val.get("v8"));
					obj2.put("v10","".equals(val.get("v10"))||val.get("v10")==null?"":val.get("v10"));
					obj2.put("v11","".equals(val.get("v11"))||val.get("v11")==null?"":val.get("v11"));
					obj2.put("v12","".equals(val.get("v12"))||val.get("v12")==null?"":val.get("v12"));
					obj2.put("v13","".equals(val.get("v13"))||val.get("v13")==null?"":val.get("v13"));
					obj2.put("v14","".equals(val.get("v14"))||val.get("v14")==null?"":val.get("v14"));
					obj2.put("v15","".equals(val.get("v15"))||val.get("v15")==null?"":val.get("v15"));
					obj2.put("v16","".equals(val.get("v16"))||val.get("v16")==null?"":val.get("v16"));
					obj2.put("v17","".equals(val.get("v17"))||val.get("v17")==null?"":val.get("v17"));
					obj2.put("v18","".equals(val.get("v18"))||val.get("v18")==null?"":val.get("v18"));
					obj2.put("v19","".equals(val.get("v19"))||val.get("v19")==null?"":val.get("v19"));
					obj2.put("v20","".equals(val.get("v20"))||val.get("v20")==null?"":val.get("v20"));
					obj2.put("v28","".equals(val.get("v28"))||val.get("v28")==null?"":val.get("v28"));
					obj2.put("v32","".equals(val.get("v32"))||val.get("v32")==null?"":val.get("v32"));
					obj2.put("pic_path","".equals(val.get("pic_path"))||val.get("pic_path")==null?"":val.get("pic_path"));
					jsonArray2.add(obj2);
				}
			}
			//帮扶计划
			String sql3="select * from da_help_info where da_household_id="+list.get(0).get("pkid");
			SQLAdapter sqlAdapter3=new SQLAdapter(sql3);
			List<Map> list3=this.getBySqlMapper.findRecords(sqlAdapter3);
			if(list3.size()>0){
				for(Map val:list3){
					obj3.put("v1","".equals(val.get("v1"))||val.get("v1")==null?"":val.get("v1"));
					obj3.put("v2","".equals(val.get("v2"))||val.get("v2")==null?"":val.get("v2"));
					obj3.put("v3","".equals(val.get("v3"))||val.get("v3")==null?"":val.get("v3"));
					jsonArray3.add(obj3);
				}
			}
			//帮扶措施
			String sql4="select v1,v2,v3,da_household_id , MAX(CASE v7 WHEN '2016' THEN v4 ELSE '' END ) v4_2016, "+
	        		"MAX(CASE v7 WHEN '2016' THEN v5 ELSE '' END ) v5_2016,MAX(CASE v7 WHEN '2016' THEN v6 ELSE '' END ) v6_2016,  "+
	        		"MAX(CASE v7 WHEN '2017' THEN v4 ELSE '' END ) v4_2017,MAX(CASE v7 WHEN '2017' THEN v5 ELSE '' END ) v5_2017,"+
	        		"MAX(CASE v7 WHEN '2017' THEN v6 ELSE '' END ) v6_2017,MAX(CASE v7 WHEN '2018' THEN v4 ELSE '' END ) v4_2018, "+
	        		"MAX(CASE v7 WHEN '2018' THEN v5 ELSE '' END ) v5_2018,MAX(CASE v7 WHEN '2018' THEN v6 ELSE '' END ) v6_2018,"+
	        		"MAX(CASE v7 WHEN '2019' THEN v4 ELSE '' END ) v4_2019,MAX(CASE v7 WHEN '2019' THEN v5 ELSE '' END ) v5_2019, "+
	        		"MAX(CASE v7 WHEN '2019' THEN v6 ELSE '' END ) v6_2019 from da_help_tz_measures where da_household_id="+list.get(0).get("pkid")+" group  by v1,v2,v3 ";
			SQLAdapter sqlAdapter4=new SQLAdapter(sql4);
			List<Map> list4=this.getBySqlMapper.findRecords(sqlAdapter4);
			if(list4.size()>0){
				for(Map val:list4){
					obj4.put("v1","".equals(val.get("v1"))||val.get("v1")==null?"":val.get("v1"));
					obj4.put("v2","".equals(val.get("v2"))||val.get("v2")==null?"":val.get("v2"));
					obj4.put("v3","".equals(val.get("v3"))||val.get("v3")==null?"":val.get("v3"));
					obj4.put("v4_2016","".equals(val.get("v4_2016"))||val.get("v4_2016")==null?"":val.get("v4_2016"));
					obj4.put("v5_2016","".equals(val.get("v5_2016"))||val.get("v5_2016")==null?"":val.get("v5_2016"));
					obj4.put("v6_2016","".equals(val.get("v6_2016"))||val.get("v6_2016")==null?"":val.get("v6_2016"));
					obj4.put("v4_2017","".equals(val.get("v4_2017"))||val.get("v4_2017")==null?"":val.get("v4_2017"));
					obj4.put("v5_2017","".equals(val.get("v5_2017"))||val.get("v5_2017")==null?"":val.get("v5_2017"));
					obj4.put("v6_2017","".equals(val.get("v6_2017"))||val.get("v6_2017")==null?"":val.get("v6_2017"));
					obj4.put("v4_2018","".equals(val.get("v4_2018"))||val.get("v4_2018")==null?"":val.get("v4_2018"));
					obj4.put("v5_2018","".equals(val.get("v5_2018"))||val.get("v5_2018")==null?"":val.get("v5_2018"));
					obj4.put("v6_2018","".equals(val.get("v6_2018"))||val.get("v6_2018")==null?"":val.get("v6_2018"));
					obj4.put("v4_2019","".equals(val.get("v4_2019"))||val.get("v4_2019")==null?"":val.get("v4_2019"));
					obj4.put("v5_2019","".equals(val.get("v5_2019"))||val.get("v5_2019")==null?"":val.get("v5_2019"));
					obj4.put("v6_2019","".equals(val.get("v6_2019"))||val.get("v6_2019")==null?"":val.get("v6_2019"));
					jsonArray4.add(obj4);
				}
			}
			//走访记录
			String sql5="SELECT v1,v2,v3, group_concat(pic_path order by pic_path separator ',') path FROM ("+
						"	SELECT *  FROM da_help_visit a LEFT JOIN (SELECT pic_path,pic_pkid from da_pic WHERE pic_type='2' ) b ON a.pkid=b.pic_pkid"+
						" WHERE a.da_household_id="+list.get(0).get("pkid")+" )aa GROUP BY pkid ORDER BY v1 DESC";
			SQLAdapter sqlAdapter5=new SQLAdapter(sql5);
			List<Map> list5=this.getBySqlMapper.findRecords(sqlAdapter5);
			if(list5.size()>0){
				for(Map val:list5){
					obj5.put("v1","".equals(val.get("v1"))||val.get("v1")==null?"":val.get("v1"));
					obj5.put("v2","".equals(val.get("v2"))||val.get("v2")==null?"":val.get("v2"));
					obj5.put("v3","".equals(val.get("v3"))||val.get("v3")==null?"":val.get("v3"));
					obj5.put("path","".equals(val.get("path"))||val.get("path")==null?"":val.get("path"));
					jsonArray5.add(obj5);
				}
			}
			//当前人均纯收入
			String sql6="SELECT  ROUND((dqsz-dqzc)/v9,2) bfq,ROUND((dqszh-dqzch)/v9,2) bfh FROM  da_household a LEFT JOIN "+
						"(select v39 dqsz,da_household_id FROM da_current_income)d ON a.pkid=d.da_household_id LEFT JOIN  "+
						"(select v31 dqzc,da_household_id FROM da_current_expenditure ) e ON a.pkid=e.da_household_id LEFT JOIN"+
						" (select v39 dqszh,da_household_id from da_helpback_income)f ON a.pkid=f.da_household_id LEFT JOIN "+
						"(SELECT v31 dqzch,da_household_id FROM da_helpback_expenditure)g ON a.pkid =g.da_household_id WHERE a.pkid="+list.get(0).get("pkid")+"";
			SQLAdapter sqlAdapter6=new SQLAdapter(sql6);
			List<Map> list6=this.getBySqlMapper.findRecords(sqlAdapter6);
			if(list6.size()>0){
				for(Map val:list6){
					obj6.put("bfq","".equals(val.get("bfq"))||val.get("bfq")==null?0:val.get("bfq"));
					obj6.put("bfh","".equals(val.get("bfh"))||val.get("bfh")==null?0:val.get("bfh"));
					jsonArray6.add(obj6);
				}
			}
			
			//生产生活条件
			String sql7="select * from (select da_household_id,v1,v2,v3,v4,v5,v13,v14 from da_production)a LEFT JOIN "+
						"(select v1 fv1,v5 fv5,v6 fv6,v7 fv7,v8 fv8,v9 fv9,v10 fv10,v11 fv11,v12 fv12,"+
						"da_household_id fid from da_life)b ON a.da_household_id=b.fid where a.da_household_id="+list.get(0).get("pkid")+"";
			SQLAdapter sqlAdapter7=new SQLAdapter(sql7);
			List<Map> list7=this.getBySqlMapper.findRecords(sqlAdapter7);
			if(list7.size()>0){
				for(Map val:list7){
					obj7.put("v1","".equals(val.get("v1"))||val.get("v1")==null?0:val.get("v1"));
					obj7.put("v2","".equals(val.get("v2"))||val.get("v2")==null?0:val.get("v2"));
					obj7.put("v3","".equals(val.get("v3"))||val.get("v3")==null?0:val.get("v3"));
					obj7.put("v4","".equals(val.get("v4"))||val.get("v4")==null?0:val.get("v4"));
					obj7.put("v5","".equals(val.get("v5"))||val.get("v5")==null?0:val.get("v5"));
					obj7.put("v13","".equals(val.get("v13"))||val.get("v13")==null?0:val.get("v13"));
					obj7.put("v14","".equals(val.get("v14"))||val.get("v14")==null?0:val.get("v14"));
					obj7.put("fv1","".equals(val.get("fv1"))||val.get("fv1")==null?0:val.get("fv1"));
					obj7.put("fv5","".equals(val.get("fv5"))||val.get("fv5")==null?"":val.get("fv5"));
					obj7.put("fv6","".equals(val.get("fv6"))||val.get("fv6")==null?"":val.get("fv6"));
					obj7.put("fv7","".equals(val.get("fv7"))||val.get("fv7")==null?0:val.get("fv7"));
					obj7.put("fv8","".equals(val.get("fv8"))||val.get("fv8")==null?"":val.get("fv8"));
					obj7.put("fv9","".equals(val.get("fv9"))||val.get("fv9")==null?"":val.get("fv9"));
					obj7.put("fv10","".equals(val.get("fv10"))||val.get("fv10")==null?"":val.get("fv10"));
					obj7.put("fv11","".equals(val.get("fv11"))||val.get("fv11")==null?"":val.get("fv11"));	
					obj7.put("fv12","".equals(val.get("fv12"))||val.get("fv12")==null?"":val.get("fv12"));
					jsonArray7.add(obj7);
					
				}
			}
			response.getWriter().write("{\"result\":"+jsonArray.toString()+",\"result1\":"+jsonArray2.toString()+",\"result2\":"+jsonArray3.toString()+","+
					"\"result3\":"+jsonArray4.toString()+",\"result4\":"+jsonArray5.toString()+",\"result5\":"+jsonArray6.toString()+",\"result6\":"+jsonArray7.toString()+"}");
			
		}else{
			response.getWriter().write("0");
		}
		
		return null;
	}
	/**
	 * 统计国贫市贫人口和人均收入人均支出
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getPopulationController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String name=request.getParameter("name");
		String type=request.getParameter("type");
			if("0".equals(type)){//市级
				//总贫困竖线
				String z_sql="select count(*) num from da_household";
				SQLAdapter z_sqlAdapter = new SQLAdapter (z_sql);
				List <Map> z_list = this.getBySqlMapper.findRecords(z_sqlAdapter);
				JSONObject obj1=new JSONObject();
				JSONArray jsonArray1=new JSONArray();
				if(z_list.size()>0){
					obj1.put("znum",z_list.get(0).get("num"));
 				}
				
				//国贫户数
				String sql1="select count(*) num from da_household where sys_standard='国家级贫困人口'";
				SQLAdapter sqlAdapter=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter);
				if(list1.size()>0){
					obj1.put("gpnum",list1.get(0).get("num"));
				}
				
				//市贫户数
				String sql2="select count(*) num from da_household where sys_standard='市级低收入人口'";
				SQLAdapter sqlAdapter2=new SQLAdapter(sql2);
				List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter2);
				if(list2.size()>0){
					obj1.put("spnum", list2.get(0).get("num"));
				}
				
				
				//帮扶责任人
				String sql3="select count(*) num from sys_personal";
				SQLAdapter sqlAdapter3=new SQLAdapter(sql3);
				List<Map> list3=this.getBySqlMapper.findRecords(sqlAdapter3);
				if(list3.size()>0){
					obj1.put("bfrnum", list3.get(0).get("num"));
				}
				//帮扶单位
				String sql4="select count(*) num from da_company ";
				SQLAdapter sqlAdapter4=new SQLAdapter(sql4);
				List<Map> list4=this.getBySqlMapper.findRecords(sqlAdapter4);
				if(list4.size()>0){
					obj1.put("dwnum", list4.get(0).get("num"));
				}
				jsonArray1.add(obj1);
				response.getWriter().write("{\"result\":"+jsonArray1.toString()+"}");
			}else if("1".equals(type)){//县级
				//总贫困竖线
				String z_sql="select count(*) num from da_household where v3='"+name+"'";
				SQLAdapter z_sqlAdapter = new SQLAdapter (z_sql);
				List <Map> z_list = this.getBySqlMapper.findRecords(z_sqlAdapter);
				JSONObject obj1=new JSONObject();
				JSONArray jsonArray1=new JSONArray();
				if(z_list.size()>0){
					obj1.put("znum",z_list.get(0).get("num"));
 				}
				
				//国贫户数
				String sql1="select count(*) num from da_household where v3='"+name+"' and sys_standard='国家级贫困人口'";
				SQLAdapter sqlAdapter=new SQLAdapter(sql1);
				List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter);
				if(list1.size()>0){
					obj1.put("gpnum",list1.get(0).get("num"));
				}
				
				//市贫户数
				String sql2="select count(*) num from da_household where where v3='"+name+"' and sys_standard='市级低收入人口'";
				SQLAdapter sqlAdapter2=new SQLAdapter(sql2);
				List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter2);
				if(list2.size()>0){
					obj1.put("spnum", list2.get(0).get("num"));
				}
				//帮扶责任人
				String sql3="select count(*) num from sys_personal";
				SQLAdapter sqlAdapter3=new SQLAdapter(sql3);
				List<Map> list3=this.getBySqlMapper.findRecords(sqlAdapter3);
				if(list3.size()>0){
					obj1.put("bfrnum", list3.get(0).get("num"));
				}
				//帮扶单位
				String sql4="select count(*) num from da_company ";
				SQLAdapter sqlAdapter4=new SQLAdapter(sql4);
				List<Map> list4=this.getBySqlMapper.findRecords(sqlAdapter4);
				if(list4.size()>0){
					obj1.put("dwnum", list4.get(0).get("num"));
				}
				jsonArray1.add(obj1);
				response.getWriter().write("{\"result\":"+jsonArray1.toString()+"}");
		}else if("2".equals(type)){//苏木乡
			//总贫困竖线
			String z_sql="select count(*) num from da_household where v4='"+name+"'";
			SQLAdapter z_sqlAdapter = new SQLAdapter (z_sql);
			List <Map> z_list = this.getBySqlMapper.findRecords(z_sqlAdapter);
			JSONObject obj1=new JSONObject();
			JSONArray jsonArray1=new JSONArray();
			if(z_list.size()>0){
				obj1.put("znum",z_list.get(0).get("num"));
				}
			
			//国贫户数
			String sql1="select count(*) num from da_household where v4='"+name+"' and sys_standard='国家级贫困人口'";
			SQLAdapter sqlAdapter=new SQLAdapter(sql1);
			List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter);
			if(list1.size()>0){
				obj1.put("gpnum",list1.get(0).get("num"));
			}
			
			//市贫户数
			String sql2="select count(*) num from da_household where where v4='"+name+"' and sys_standard='市级低收入人口'";
			SQLAdapter sqlAdapter2=new SQLAdapter(sql2);
			List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter2);
			if(list2.size()>0){
				obj1.put("spnum", list2.get(0).get("num"));
			}
			//帮扶责任人
			String sql3="select count(*) num from sys_personal";
			SQLAdapter sqlAdapter3=new SQLAdapter(sql3);
			List<Map> list3=this.getBySqlMapper.findRecords(sqlAdapter3);
			if(list3.size()>0){
				obj1.put("bfrnum", list3.get(0).get("num"));
			}
			//帮扶单位
			String sql4="select count(*) num from da_company ";
			SQLAdapter sqlAdapter4=new SQLAdapter(sql4);
			List<Map> list4=this.getBySqlMapper.findRecords(sqlAdapter4);
			if(list4.size()>0){
				obj1.put("dwnum", list4.get(0).get("num"));
			}
			response.getWriter().write("{\"result\":"+jsonArray1.toString()+"}");
	}else if("3".equals(type)){//嘎查村
		//总贫困竖线
		String z_sql="select count(*) num from da_household where v5='"+name+"'";
		SQLAdapter z_sqlAdapter = new SQLAdapter (z_sql);
		List <Map> z_list = this.getBySqlMapper.findRecords(z_sqlAdapter);
		JSONObject obj1=new JSONObject();
		JSONArray jsonArray1=new JSONArray();
		if(z_list.size()>0){
			obj1.put("znum",z_list.get(0).get("num"));
			}
		
		//国贫户数
		String sql1="select count(*) num from da_household where v5='"+name+"' and sys_standard='国家级贫困人口'";
		SQLAdapter sqlAdapter=new SQLAdapter(sql1);
		List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter);
		if(list1.size()>0){
			obj1.put("gpnum",list1.get(0).get("num"));
		}
		
		//市贫户数
		String sql2="select count(*) num from da_household where where v5='"+name+"' and sys_standard='市级低收入人口'";
		SQLAdapter sqlAdapter2=new SQLAdapter(sql2);
		List<Map> list2=this.getBySqlMapper.findRecords(sqlAdapter2);
		if(list2.size()>0){
			obj1.put("spnum", list2.get(0).get("num"));
		}
		//帮扶责任人
		String sql3="select count(*) num from sys_personal";
		SQLAdapter sqlAdapter3=new SQLAdapter(sql3);
		List<Map> list3=this.getBySqlMapper.findRecords(sqlAdapter3);
		if(list3.size()>0){
			obj1.put("bfrnum", list3.get(0).get("num"));
		}
		//帮扶单位
		String sql4="select count(*) num from da_company ";
		SQLAdapter sqlAdapter4=new SQLAdapter(sql4);
		List<Map> list4=this.getBySqlMapper.findRecords(sqlAdapter4);
		if(list4.size()>0){
			obj1.put("dwnum", list4.get(0).get("num"));
		}
		jsonArray1.add(obj1);
		response.getWriter().write("{\"result\":"+jsonArray1.toString()+"}");
}
		
		return null;
	}
	
	/**
	 * 鄂尔多斯帮扶责任人-市贫
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getShiBfr(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject obj1=new JSONObject();
		JSONArray jsonArray1=new JSONArray();
		//市贫
		String sql1="select count(*) num from (select pkid from da_household where sys_standard='市级低收入人口') a left join  "+
					"(select da_household_id from sys_personal_household_many)b on a.pkid=b.da_household_id  where da_household_id!=''";
		SQLAdapter sqlAdapter=new SQLAdapter(sql1);
		List<Map> list1=this.getBySqlMapper.findRecords(sqlAdapter);
		if(list1.size()>0){
			obj1.put("gpnum",list1.get(0).get("num"));
		}
		
		//帮扶责任人-国贫
		String sql3="select count(*) num from (select pkid from da_household where sys_standard='国家级贫困人口') a left join  "+
					"(select da_household_id from sys_personal_household_many)b on a.pkid=b.da_household_id  where da_household_id!=''";
		SQLAdapter sqlAdapter3=new SQLAdapter(sql3);
		List<Map> list3=this.getBySqlMapper.findRecords(sqlAdapter3);
		if(list3.size()>0){
			obj1.put("num", list3.get(0).get("num"));
		}
		jsonArray1.add(obj1);
		response.getWriter().write("{\"result\":"+jsonArray1.toString()+"}");

		return null;
	}
	/**
	 * 接收移动端关于地图的参数
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getCharParameter(HttpServletRequest request,HttpServletResponse response) throws IOException{
		char_type=request.getParameter("type");
		String standard=request.getParameter("standard");
		if("0".equals(standard)){
			char_standard="国家级贫困人口";
		}else if("1".equals(standard)){
			char_standard="市级低收入人口";
		}
		char_name=request.getParameter("name");
		if(char_type==""||char_standard==""||char_name==""){
			response.getWriter().write("0");
		}else{
			response.getWriter().write("1");
		}
		return null;
	}
	/**
	 * 往前台js传参数
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getReceiveParameter(HttpServletRequest request,HttpServletResponse response ) throws IOException{
		JSONObject obj=new JSONObject();
		String sql = "select com_code from sys_company where com_name='"+char_name+"'";
		SQLAdapter sqlAdapter = new SQLAdapter(sql);
		List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
		if(list.size()>0){
			obj.put("code", list.get(0).get("com_code"));
		}else{
			obj.put("code", "");
		}
		obj.put("stardand",char_standard);
		response.getWriter().write(obj.toString());
		return null;
		
	}
	/**
	 * 地图
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getCharController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql = "";
		if("0".equals(char_type)){//市级
			sql="SELECT v3,count(*) AS count FROM da_household  where sys_standard='"+char_standard+"' GROUP BY v3";
		}else if ("1".equals(char_type)){//县级
			sql="SELECT a1.v4 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v3 = a2.com_name WHERE a2.com_name='"+char_name+"' AND a1.sys_standard='"+char_standard+"' GROUP BY a1.v4";
		}else if ("2".equals(char_type)){//乡级
			sql="SELECT a1.v5 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v4 = a2.com_name WHERE a2.com_name='"+char_name+"' AND a1.sys_standard='"+char_standard+"' GROUP BY a1.v5";
		}else if ("3".equals(char_type)){//村级
			 sql="SELECT a1.v5 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v4 = a2.com_name WHERE a2.com_name='"+char_name+"' AND a1.sys_standard='"+char_standard+"' GROUP BY a1.v5";
		}
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
		return null;
	
	}
	/**
	 * 随机取帮扶人十个列表
	 * @author 太年轻
	 * @date 2016年10月13日
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getSuiji(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String sql = "select pkid,v3,v4,v5,v6,v7,v9,v23,v33,pic_path from  "+
					" (SELECT pkid, v3,v4,v5,v6,v7,v9,v23,v33 FROM da_household ORDER BY RAND()  LIMIT 10) a "+
					" left join (select pic_pkid,pic_path from da_pic where pic_type='4' ) b on a.pkid = b.pic_pkid  ";
		
		SQLAdapter sqlAdapter = new SQLAdapter (sql);
		
		List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray = new JSONArray ();
		if ( list.size() > 0 ) {
			for ( int i = 0 ; i < list.size() ; i ++ ) {
				JSONObject obj = new JSONObject();
				obj.put("pkid", "" .equals(list.get(i).get("pkid")) ||  list.get(i).get("pkid") == null ? "" : list.get(i).get("pkid").toString());
				obj.put("v3", "" .equals(list.get(i).get("v3")) ||  list.get(i).get("v3") == null ? "" : list.get(i).get("v3").toString());
				obj.put("v4", "" .equals(list.get(i).get("v4")) ||  list.get(i).get("v4") == null ? "" : list.get(i).get("v4").toString());
				obj.put("v5", "" .equals(list.get(i).get("v5")) ||  list.get(i).get("v5") == null ? "" : list.get(i).get("v5").toString());
				obj.put("v6", "" .equals(list.get(i).get("v6")) ||  list.get(i).get("v6") == null ? "" : list.get(i).get("v6").toString());
				obj.put("v7", "" .equals(list.get(i).get("v7")) ||  list.get(i).get("v7") == null ? "" : list.get(i).get("v7").toString());
				obj.put("v9", "" .equals(list.get(i).get("v9")) ||  list.get(i).get("v9") == null ? "" : list.get(i).get("v9").toString());
				obj.put("v23", "" .equals(list.get(i).get("v23")) ||  list.get(i).get("v23") == null ? "" : list.get(i).get("v23").toString());
				obj.put("v33", "" .equals(list.get(i).get("v33")) ||  list.get(i).get("v33") == null ? "" : list.get(i).get("v33").toString());
				obj.put("pic_path", "" .equals(list.get(i).get("pic_path")) ||  list.get(i).get("pic_path") == null ? "" : list.get(i).get("pic_path").toString());
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"result\":"+jsonArray.toString()+"}");
		}
		
		return null;
	}
	/**
	 * 捐款搜索贫困户
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView getDoSpoor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String name = request.getParameter("name");//姓名
		String sql = "select pkid, v3,v4,v5,v6,v7,v9,v23,v33,pic_path from  "+
				" (SELECT pkid, v3,v4,v5,v6,v7,v9,v23,v33 FROM da_household where v6 ='"+name+"' or v8='"+name+"' "+
				" ) a left join (select pic_pkid,pic_path from da_pic where pic_type='4' ) b on a.pkid = b.pic_pkid  ";
		SQLAdapter sqlAdapter = new SQLAdapter (sql);
		
		List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray = new JSONArray ();
		if ( list.size() > 0 ) {
			for ( int i = 0 ; i < list.size() ; i ++ ) {
				JSONObject obj = new JSONObject();
				obj.put("pkid", "" .equals(list.get(i).get("pkid")) ||  list.get(i).get("pkid") == null ? "" : list.get(i).get("pkid").toString());
				obj.put("v3", "" .equals(list.get(i).get("v3")) ||  list.get(i).get("v3") == null ? "" : list.get(i).get("v3").toString());
				obj.put("v4", "" .equals(list.get(i).get("v4")) ||  list.get(i).get("v4") == null ? "" : list.get(i).get("v4").toString());
				obj.put("v5", "" .equals(list.get(i).get("v5")) ||  list.get(i).get("v5") == null ? "" : list.get(i).get("v5").toString());
				obj.put("v6", "" .equals(list.get(i).get("v6")) ||  list.get(i).get("v6") == null ? "" : list.get(i).get("v6").toString());
				obj.put("v7", "" .equals(list.get(i).get("v7")) ||  list.get(i).get("v7") == null ? "" : list.get(i).get("v7").toString());
				obj.put("v9", "" .equals(list.get(i).get("v9")) ||  list.get(i).get("v9") == null ? "" : list.get(i).get("v9").toString());
				obj.put("v23", "" .equals(list.get(i).get("v23")) ||  list.get(i).get("v23") == null ? "" : list.get(i).get("v23").toString());
				obj.put("v33", "" .equals(list.get(i).get("v33")) ||  list.get(i).get("v33") == null ? "" : list.get(i).get("v33").toString());
				obj.put("pic_path", "" .equals(list.get(i).get("pic_path")) ||  list.get(i).get("pic_path") == null ? "" : list.get(i).get("pic_path").toString());
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"result\":"+jsonArray.toString()+"}");
		}else {
			response.getWriter().write("0");
		}
		
		return null;
	}
	/**
	 * 我关注的贫困户——捐款
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView myPoorMessage ( HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String poor = request.getParameter("poor_id");
		String[] poor_id = poor.split(",");
		JSONArray jsonArray = new JSONArray () ;
		if ( poor_id .length == 0 ) {
			response.getWriter().write("0");
		} else {
			for ( int i = 0 ; i < poor_id.length ; i ++){
				
				String sql = "select pkid, v3,v4,v5,v6,v7,v9,v23,v33,pic_path from  "+
						" (SELECT pkid, v3,v4,v5,v6,v7,v9,v23,v33 FROM da_household where pkid = "+poor_id[i]+" "+
						" ) a left join (select pic_pkid,pic_path from da_pic where pic_type='4' ) b on a.pkid = b.pic_pkid  ";
				SQLAdapter sqlAdapter = new SQLAdapter (sql);
				List<Map> list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject obj = new JSONObject();
				obj.put("pkid", "" .equals(list.get(0).get("pkid")) ||  list.get(0).get("pkid") == null ? "" : list.get(0).get("pkid").toString());
				obj.put("v3", "" .equals(list.get(0).get("v3")) ||  list.get(0).get("v3") == null ? "" : list.get(0).get("v3").toString());
				obj.put("v4", "" .equals(list.get(0).get("v4")) ||  list.get(0).get("v4") == null ? "" : list.get(0).get("v4").toString());
				obj.put("v5", "" .equals(list.get(0).get("v5")) ||  list.get(0).get("v5") == null ? "" : list.get(0).get("v5").toString());
				obj.put("v6", "" .equals(list.get(0).get("v6")) ||  list.get(0).get("v6") == null ? "" : list.get(0).get("v6").toString());
				obj.put("v7", "" .equals(list.get(0).get("v7")) ||  list.get(0).get("v7") == null ? "" : list.get(0).get("v7").toString());
				obj.put("v9", "" .equals(list.get(0).get("v9")) ||  list.get(0).get("v9") == null ? "" : list.get(0).get("v9").toString());
				obj.put("v23", "" .equals(list.get(0).get("v23")) ||  list.get(0).get("v23") == null ? "" : list.get(0).get("v23").toString());
				obj.put("v33", "" .equals(list.get(0).get("v33")) ||  list.get(0).get("v33") == null ? "" : list.get(0).get("v33").toString());
				obj.put("pic_path", "" .equals(list.get(0).get("pic_path")) ||  list.get(0).get("pic_path") == null ? "" : list.get(0).get("pic_path").toString());
				jsonArray.add(obj);
			}
			response.getWriter().write("{\"result\":"+jsonArray.toString()+"}");
		}
		
		return null;
	}
}
