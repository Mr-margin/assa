package com.gistone.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class PoorUserController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * 添加帮扶人信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getAddGRController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String xm=request.getParameter("xm");//负责人
		String cun=request.getParameter("cun");//嘎查村
		String phone=request.getParameter("phone");//电话
		String zhanghao=request.getParameter("zhanghao");//登录账号
		String mima=request.getParameter("mima");//登录密码
		
		String cha_sql="SELECT * FROM sys_company WHERE com_name='"+cun+"'";
		SQLAdapter cha_sqlAdapter =new SQLAdapter(cha_sql);
		JSONArray cha_jsonArray =new JSONArray();
		this.getBySqlMapper.findRecords(cha_sqlAdapter);
		Integer sys_company_id=(Integer) getBySqlMapper.findRecords(cha_sqlAdapter).get(0).get("pkid");
		
		
		String sql="INSERT INTO sys_village_responsibility(v1,v2,sys_company_id,v5) VALUES"+
					"('"+xm+"','"+phone+"',"+sys_company_id+",'"+cun+"')";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		JSONArray jsonArray =new JSONArray();
		this.getBySqlMapper.findRecords(sqlAdapter);
		

		String hql="select max(pkid) pkid from sys_village_responsibility where v1= '"+xm+"' ";
		SQLAdapter sqlAdapter1 =new SQLAdapter(hql);
		Integer main=(Integer) getBySqlMapper.findRecords(sqlAdapter1).get(0).get("pkid");
		String user="insert into sys_user(col_account,col_password,account_type,sys_company_id,account_state) values"+
					"('"+zhanghao+"','"+mima+"','1',"+main+",'正常')";
		
		SQLAdapter sqlAdapter2 =new SQLAdapter(user);
		JSONArray jsonArray1 =new JSONArray();
		this.getBySqlMapper.findRecords(sqlAdapter2);
		
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
		
	}
	
	/**
	 * 添加新的贫困户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAddHu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String qx=request.getParameter("qx");
		String xaing=request.getParameter("xaing");
		String cun=request.getParameter("cun");
		String huname=request.getParameter("huname");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
        int da_household_id = 0;
        try{
        	String add_sql = "insert into da_household(v2,v3,v4,v5,v6,v9,v21) values('"+newFileName+"','"+qx+"','"+xaing+"','"+cun+"','"+huname+"','1','未脱贫')";
    		SQLAdapter Metadata_table_Adapter = new SQLAdapter(add_sql);
    		this.getBySqlMapper.insertSelective(Metadata_table_Adapter);//户主信息
    		
    		//取得户主的ID
    		String hou_sql = "select pkid from da_household where v2='"+newFileName+"'";
    		SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
    		da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
    		
    		
    		//户主关联下的所有表，均需要增加记录
    		String sql_1 = "insert into da_household_basic(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
    		this.getBySqlMapper.insertSelective(sql_1_Adapter);//户主信息扩展
    		
    		
    		String sql_2 = "insert into da_production(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_2_Adapter = new SQLAdapter(sql_2);
    		this.getBySqlMapper.insertSelective(sql_2_Adapter);//生产条件
    		
    		String sql_3 = "insert into da_life(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
    		this.getBySqlMapper.insertSelective(sql_3_Adapter);//生活条件
    		
    		String sql_4 = "insert into da_current_income(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_4_Adapter = new SQLAdapter(sql_4);
    		this.getBySqlMapper.insertSelective(sql_4_Adapter);//当前收入情况
    		
    		String sql_5 = "insert into da_current_expenditure(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_5_Adapter = new SQLAdapter(sql_5);
    		this.getBySqlMapper.insertSelective(sql_5_Adapter);//当前支出情况
    		
    		String sql_6 = "insert into da_help_info(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_6_Adapter = new SQLAdapter(sql_6);
    		this.getBySqlMapper.insertSelective(sql_6_Adapter);//帮扶目标计划等内容
    		
    		String sql_9 = "insert into da_helpback_income(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_9_Adapter = new SQLAdapter(sql_9);
    		this.getBySqlMapper.insertSelective(sql_9_Adapter);//帮扶后收入情况
    		
    		String sql_10 = "insert into da_helpback_expenditure(da_household_id) values('"+da_household_id+"')";
    		SQLAdapter sql_10_Adapter = new SQLAdapter(sql_10);
    		this.getBySqlMapper.insertSelective(sql_10_Adapter);//帮扶后支出情况
    		
    		
    		HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+da_household_id+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','扶贫手册','新增户主')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
    		
    		response.getWriter().write("1");
        }catch(Exception e){
    		
    		String hou_sql = "delete from da_household where pkid='"+da_household_id+"'";
    		SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
    		this.getBySqlMapper.insertSelective(hou_Adapter);
    		
    		String sql_1 = "delete from da_household_basic where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
    		this.getBySqlMapper.insertSelective(sql_1_Adapter);//户主信息扩展
    		
    		String sql_2 = "delete from da_production where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_2_Adapter = new SQLAdapter(sql_2);
    		this.getBySqlMapper.insertSelective(sql_2_Adapter);//生产条件
    		
    		String sql_3 = "delete from da_life where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
    		this.getBySqlMapper.insertSelective(sql_3_Adapter);//生活条件
    		
    		String sql_4 = "delete from da_current_income where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_4_Adapter = new SQLAdapter(sql_4);
    		this.getBySqlMapper.insertSelective(sql_4_Adapter);//当前收入情况
    		
    		String sql_5 = "delete from da_current_expenditure where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_5_Adapter = new SQLAdapter(sql_5);
    		this.getBySqlMapper.insertSelective(sql_5_Adapter);//当前支出情况
    		
    		String sql_6 = "delete from da_help_info where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_6_Adapter = new SQLAdapter(sql_6);
    		this.getBySqlMapper.insertSelective(sql_6_Adapter);//帮扶目标计划等内容
    		
    		String sql_9 = "delete from da_helpback_income where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_9_Adapter = new SQLAdapter(sql_9);
    		this.getBySqlMapper.insertSelective(sql_9_Adapter);//帮扶后收入情况
    		
    		String sql_10 = "delete from da_helpback_expenditure where da_household_id='"+da_household_id+"'";
    		SQLAdapter sql_10_Adapter = new SQLAdapter(sql_10);
    		this.getBySqlMapper.insertSelective(sql_10_Adapter);//帮扶后支出情况
    		
        	response.getWriter().write("0");
        }
		
		return null;
	}
	
	/**
	 * 删除现有贫困户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getDelHu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String str = request.getParameter("str");
		
		String hu_add_sql = "insert into da_household_del select * from da_household where pkid in("+str+")";
		SQLAdapter hu_add_Adapter = new SQLAdapter(hu_add_sql);
		
		String me_add_sql = "insert into da_member_del select * from da_member where da_household_id in("+str+")";
		SQLAdapter me_add_Adapter = new SQLAdapter(me_add_sql);
		
		String hu_del_sql = "delete from da_household where pkid in("+str+")";
		SQLAdapter hu_del_Adapter = new SQLAdapter(hu_del_sql);
		
		String me_del_sql = "delete from da_member where da_household_id in("+str+")";
		SQLAdapter me_del_Adapter = new SQLAdapter(me_del_sql);
		
		try{
			
			this.getBySqlMapper.insertSelective(hu_add_Adapter);
			this.getBySqlMapper.insertSelective(me_add_Adapter);
			this.getBySqlMapper.insertSelective(hu_del_Adapter);
			this.getBySqlMapper.insertSelective(me_del_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				
				if(str.indexOf(",")>-1){
					String val[] = str.split(",");
					for(int i = 0;i<val.length;i++){
						String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
								" VALUES ('da_household_del',"+val[i]+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','扶贫手册','删除贫困户')";
						SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
						this.getBySqlMapper.findRecords(hqlAdapter1);
					}
				}else{
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('da_household_del',"+str+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','扶贫手册','删除贫困户')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}
				
			}
			
			response.getWriter().write("1");
		}catch(Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 *贫困户维护显示
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getPoorUserController(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String cha_qx = "";//旗县
		String cha_smx ="";//苏木乡
		String cha_gcc ="";//嘎查村
		String cha_sbbz ="";//识别标准
		String cha_pksx ="";//贫困户属性
		String cha_zpyy ="";//致贫原因
		String cha_mz ="";//户主民族
		String cha_renkou ="";//贫困户人口
		String cha_bfdw ="";//帮扶单位
		String cha_bfzrr ="";//帮扶责任人
		String cha_banqian ="";//是否纳入易地扶贫搬迁
		
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//年龄范围
		

		String str = "";
		
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " a.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " a.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("请选择")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(cha_v8_1.equals("大于60岁")){
				str += " LENGTH(a.v8)>=18 and year(now())- year(substring(a.v8,7,8))>=60 and";
			}else if(cha_v8_1.equals("小于16岁")){
				str += " LENGTH(a.v8)>=18 and year(now())- year(substring(a.v8,7,8))<=16 and";
			}else if(cha_v8_1.equals("17岁至59岁")){
				str += " LENGTH(a.v8)>=18 and (year(now())- year(substring(a.v8,7,8))>=17 or year(now())- year(substring(a.v8,7,8))>=59) and";
			}
		}
		
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " a.v3='"+cha_qx+"' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " a.v4='"+cha_smx+"' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " a.v5='"+cha_gcc+"' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " a.sys_standard='"+cha_sbbz+"' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " a.v22='"+cha_pksx+"' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " a.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " a.v11='"+cha_mz+"' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " a.v9>=5 and";
			}else{
				str += " a.v9='"+cha_renkou+"' and";
			}
		}
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " a.v21='"+cha_banqian+"' and";
//					str += " d.v3='"+cha_banqian+"' and";
//					count_st_sql += " LEFT JOIN da_life d on a.pkid=d.da_household_id ";
//					people_sql += " LEFT JOIN da_life d on a.pkid=d.da_household_id ";
		}
		
		
		String count_st_sql = "select count(*) from (select a.pkid from da_household a ";
		String people_sql = "select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v22,a.v23,a.v11,a.sys_standard,a.entry_year from da_household a ";
		
		//如果帮扶人和帮扶单位条件被选择
		if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
			if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
				cha_bfdw = request.getParameter("cha_bfdw").trim();
				str += " t2.v1 like '%"+cha_bfdw+"%' and";
			}
			if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
				cha_bfzrr = request.getParameter("cha_bfzrr").trim();
				str += " c.col_name like '%"+cha_bfzrr+"%' and";
			}
			count_st_sql += " LEFT JOIN sys_personal_household_many x on x.da_household_id=a.pkid LEFT JOIN sys_personal c on x.sys_personal_id = c.pkid join da_company t2 on c.da_company_id=t2.pkid ";
			people_sql += " LEFT JOIN sys_personal_household_many x on x.da_household_id=a.pkid LEFT JOIN sys_personal c on x.sys_personal_id = c.pkid join da_company t2 on c.da_company_id=t2.pkid ";
		}
		
		
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;
		
		//两个条件为空，按照全部查询select * from sys_village_responsibility a  LEFT JOIN sys_company b  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_personal_id

		if(str.equals("")){
			count_st_sql += " GROUP BY a.pkid) ww";
			people_sql += " GROUP BY a.pkid order by a.v2,a.pkid limit "+number+","+size;
		}else{
			//带条件，按照条件查询
			count_st_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid) ww";
			people_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid order by a.v2,a.pkid limit "+number+","+size;
		}
		
//		System.out.println(people_sql);
		//System.out.println(count_st_sql);
		
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		if(Patient_st_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Patient_st_map.keySet()) {
					if(!Patient_st_map.keySet().contains("entry_year")){
						val.put("entry_year", "2016");
					}
					val.put(key, Patient_st_map.get(key));
					
					if(key.toString().equals("sys_standard")){
						if(Patient_st_map.get(key)!=null&&!Patient_st_map.get(key).equals("")){
							val.put(key, Patient_st_map.get(key).toString().substring(0, 2));
						}
					}
					
					if(key.toString().equals("pkid")){
						HttpSession session = request.getSession();
//						JSONObject json = new JSONObject();
						String functionMap="";
						JSONArray jsonArry=new JSONArray();
						
						val.put("jbqk","");
						val.put("bfdwr","");
						val.put("bfcs","");
						val.put("zfqk","");
						val.put("bfcx","");
						val.put("bfhsz","");
						val.put("dqsz","");
						if (session.getAttribute("function_map")!=null){
							Map function_map=(Map) session.getAttribute("function_map");
							Map weihu_map=(Map) session.getAttribute("weihu_map");
							Object ss[] = weihu_map.keySet().toArray();
							Object s[] = function_map.keySet().toArray();
							for (int k=0;k<weihu_map.size();k++){
								if(weihu_map.get(ss[k]).toString().equals("1")){
									
									if(function_map.get(s[k]).toString().equals("jbqk")){
										 val.put("jbqk", "<button  type=\"button\" class=\"btn btn-primary btn-xs jbqk\" onclick=\"jbqk("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									if(function_map.get(s[k]).toString().equals("bfdwr")){
										val.put("bfdwr", "<button  type=\"button\" class=\"btn btn-primary btn-xs bfdwr\" onclick=\"bfdwr("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									if(function_map.get(s[k]).toString().equals("bfcs")){
										val.put("bfcs", "<button  type=\"button\" class=\"btn btn-primary btn-xs bfcs\" onclick=\"bfcs("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									if(function_map.get(s[k]).toString().equals("zfqk")){
										val.put("zfqk", "<button type=\"button\" class=\"btn btn-primary btn-xs zfqk\" onclick=\"zfqk("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									if(function_map.get(s[k]).toString().equals("bfcx")){
										val.put("bfcx", "<button type=\"button\" class=\"btn btn-primary btn-xs bfcx\" onclick=\"bfcx("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									if(function_map.get(s[k]).toString().equals("bfhsz")){
										val.put("bfhsz", "<button  type=\"button\" class=\"btn btn-primary btn-xs bfhsz\" onclick=\"bfhsz("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									if(function_map.get(s[k]).toString().equals("dqsz")){
										val.put("dqsz", "<button  type=\"button\" class=\"btn btn-primary btn-xs dqsz\" onclick=\"dqsz("+Patient_st_map.get(key)+");\"><i class=\"fa fa-pencil\"></i> 编辑 </button>");
									}
									
								}
							}
						
						}
					}
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
	
	/**
	 * 修改贫困户之前
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getUpdateSavePoor(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		
		String sql="select * from da_household a left join da_household_basic b on a.pkid=b.da_household_id where a.pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		//System.out.println(sql);
		JSONObject zong = new JSONObject ();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("v6", val.get("v6")==null?"":val.get("v6"));//姓名
			obj.put("v7",val.get("v7")==null?"":val.get("v7"));//性别
			obj.put("v8",val.get("v8")==null?"":val.get("v8"));//证件号码
			obj.put("v11",val.get("v11")==null?"":val.get("v11"));//民族
			
			obj.put("v12",val.get("v12")==null?"":val.get("v12"));//文化程度
			obj.put("v13",val.get("v13")==null?"":val.get("v13"));//在校状况
			obj.put("v14",val.get("v14")==null?"":val.get("v14"));//健康状况
			obj.put("v15",val.get("v15")==null?"":val.get("v15"));//劳动力
			obj.put("v18",val.get("v18")==null?"":val.get("v18"));//是否参加新农合
			obj.put("v19",val.get("v19")==null?"":val.get("v19"));//是否参加新型养老保险
			obj.put("v28",val.get("v28")==null?"":val.get("v28"));//政治面貌
			
			obj.put("v25",val.get("v25")==null?"":val.get("v25"));//联系电话
			obj.put("v26",val.get("v26")==null?"":val.get("v26"));//开户银行
			obj.put("v27",val.get("v27")==null?"":val.get("v27"));//银行账号
			obj.put("v16",val.get("v16")==null?"":val.get("v16"));//务工情况
			obj.put("v17",val.get("v17")==null?"":val.get("v17"));//务工时间
			obj.put("sys_standard",val.get("sys_standard")==null?"":val.get("sys_standard"));//识别标准
			obj.put("v22",val.get("v22")==null?"":val.get("v22"));//贫苦户属性
			obj.put("v23",val.get("v23")==null?"":val.get("v23"));//致贫原因
			obj.put("v29",val.get("v29")==null?"":val.get("v29"));//是否军烈属
			obj.put("v30",val.get("v30")==null?"":val.get("v30"));//是否独生子女户
			obj.put("v31",val.get("v31")==null?"":val.get("v31"));//是否双女户
			obj.put("v32",val.get("v32")==null?"":val.get("v32"));//是否现役军人
			obj.put("v33",val.get("v33")==null?"":val.get("v33"));//其他致贫原因
			obj.put("basic_address",val.get("basic_address")==null?"":val.get("basic_address"));//家庭住址
			obj.put("basic_explain",val.get("basic_explain")==null?"":val.get("basic_explain"));//致贫原因说明
//			jsonArray.add(obj);
			zong.put("huzhu", obj);
		}
		
		String count_st_sql = "select * from da_pic where pic_type=4 and pic_pkid="+pkid;
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		List<Map> list_pic = getBySqlMapper.findRecords(count_st_Adapter);
		if(list_pic.size()>0){
			Map st_map = list_pic.get(0);
			zong.put("huzhu_pic", st_map.get("pic_path")==null?"":st_map.get("pic_path"));
		}
		
		//家庭成员
		String xian_sql="select * from da_member where da_household_id="+pkid;
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		if(xian_list.size()>0){
			JSONArray jsonArray =new JSONArray();
			for(Map val:xian_list){
				JSONObject xian_obj=new JSONObject ();
				xian_obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
				xian_obj.put("v6",val.get("v6")==null?"":val.get("v6"));//姓名
				xian_obj.put("v7",val.get("v7")==null?"":val.get("v7"));//性别
				xian_obj.put("v8",val.get("v8")==null?"":val.get("v8"));//证件号码
				xian_obj.put("v9",val.get("v9")==null?"":val.get("v9"));//人数
				xian_obj.put("v10",val.get("v10")==null?"":val.get("v10"));//与户主关系
				xian_obj.put("v11",val.get("v11")==null?"":val.get("v11"));//民族
				xian_obj.put("v12",val.get("v12")==null?"":val.get("v12"));//文化程度
				xian_obj.put("v13",val.get("v13")==null?"":val.get("v13"));//在校状况
				xian_obj.put("v14",val.get("v14")==null?"":val.get("v14"));//健康状况
				xian_obj.put("v15",val.get("v15")==null?"":val.get("v15"));//劳动力
				xian_obj.put("v16",val.get("v16")==null?"":val.get("v16"));//务工状态
				xian_obj.put("v17",val.get("v17")==null?"":val.get("v17"));//务工时间
				xian_obj.put("v18",val.get("v18")==null?"":val.get("v18"));//是否参加新农合
				xian_obj.put("v19",val.get("v19")==null?"":val.get("v19"));//是否参加新型养老保险
				xian_obj.put("v20",val.get("v20")==null?"":val.get("v20"));//是否参加城镇职工基本养老保险	
				xian_obj.put("v21",val.get("v21")==null?"":val.get("v21"));//脱贫属性
				xian_obj.put("v28",val.get("v28")==null?"":val.get("v28"));//政治面貌
				xian_obj.put("v32",val.get("v32")==null?"":val.get("v32"));//是否现役军人
				
				String jtcy_st_sql = "select * from da_pic where pic_type=5 and pic_pkid="+val.get("pkid");
//				System.out.println(jtcy_st_sql);
				SQLAdapter jtcy_st_Adapter = new SQLAdapter(jtcy_st_sql);
				List<Map> jtcy_pic = getBySqlMapper.findRecords(jtcy_st_Adapter);
				if(jtcy_pic.size()>0){
					Map st_map = jtcy_pic.get(0);
					xian_obj.put("pic", st_map.get("pic_path")==null?"":st_map.get("pic_path"));
				}
				
				jsonArray.add(xian_obj);
			}
			zong.put("jaiting", jsonArray);
		}
		
		//生产条件
		String sc_sql="select * FROM da_production where da_household_id="+pkid;
		SQLAdapter sc_sqlAdapter =new SQLAdapter(sc_sql);
		List<Map> sc_list=getBySqlMapper.findRecords(sc_sqlAdapter);
		for(Map val:sc_list){
			JSONObject sc_obj=new JSONObject ();
			sc_obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			sc_obj.put("v1",val.get("v1")==null?"":val.get("v1"));//耕地面积
			sc_obj.put("v2",val.get("v2")==null?"":val.get("v2"));//水浇地面积
			sc_obj.put("v3",val.get("v3")==null?"":val.get("v3"));//林地面积
			sc_obj.put("v4",val.get("v4")==null?"":val.get("v4"));//退耕还林面积
			sc_obj.put("v5",val.get("v5")==null?"":val.get("v5"));//草牧场面积
			sc_obj.put("v6",val.get("v6")==null?"":val.get("v6"));//生产用房面积
			sc_obj.put("v7",val.get("v7")==null?"":val.get("v7"));//其他
			sc_obj.put("v8",val.get("v8")==null?"":val.get("v8"));//家禽
			sc_obj.put("v9",val.get("v9")==null?"":val.get("v9"));//牛
			sc_obj.put("v10",val.get("v10")==null?"":val.get("v10"));//羊
			sc_obj.put("v11",val.get("v11")==null?"":val.get("v11"));//猪
			sc_obj.put("v12",val.get("v12")==null?"":val.get("v12"));//其他
			sc_obj.put("v13",val.get("v13")==null?"":val.get("v13"));//林果面积（亩）
			sc_obj.put("v14",val.get("v14")==null?"":val.get("v14"));//水面面积（亩）
//			jsonArray.add(sc_obj);
			zong.put("shenchan", sc_obj);
		}
		
		//生活条件
		String sh_sql="SELECT * FROM da_life where da_household_id="+pkid;
		SQLAdapter sh_sqlAdapter =new SQLAdapter(sh_sql);
		List<Map> sh_list=getBySqlMapper.findRecords(sh_sqlAdapter);
		for(Map val:sh_list){
			JSONObject sh_obj=new JSONObject ();
			sh_obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			sh_obj.put("v1",val.get("v1")==null?"":val.get("v1"));//住房面积
			sh_obj.put("v2",val.get("v2")==null?"":val.get("v2"));//是否危房
			sh_obj.put("v3",val.get("v3")==null?"":val.get("v3"));//是否纳入易地扶贫搬迁
			sh_obj.put("v4",val.get("v4")==null?"":val.get("v4"));//饮水情况
			sh_obj.put("v5",val.get("v5")==null?"":val.get("v5"));//通电情况
			sh_obj.put("v6",val.get("v6")==null?"":val.get("v6"));//入户路类型
			sh_obj.put("v7",val.get("v7")==null?"":val.get("v7"));//与村主干路距离
			sh_obj.put("v8",val.get("v8")==null?"":val.get("v8"));//饮水是否困难
			sh_obj.put("v9",val.get("v9")==null?"":val.get("v9"));//饮水是否安全
			sh_obj.put("v10",val.get("v10")==null?"":val.get("v10"));//主要燃料类型
			sh_obj.put("v11",val.get("v11")==null?"":val.get("v11"));//是否加入农民专业合作社
			sh_obj.put("v12",val.get("v12")==null?"":val.get("v12"));//有无卫生厕所
//			jsonArray.add(sh_obj);
			zong.put("shenhuo", sh_obj);
		}
		
		
		//易地扶贫搬迁
		String yidi_sql="SELECT * FROM da_household_move where da_household_id="+pkid;
		SQLAdapter yidi_sqlAdapter =new SQLAdapter(yidi_sql);
		List<Map> yidi_list=getBySqlMapper.findRecords(yidi_sqlAdapter);
		for(Map val:yidi_list){
			JSONObject yidi_obj=new JSONObject ();
			//yidi_obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			yidi_obj.put("move_type",val.get("move_type")==null?"":val.get("move_type"));
			yidi_obj.put("focus_info",val.get("focus_info")==null?"":val.get("focus_info"));
			yidi_obj.put("dispersed_info",val.get("dispersed_info")==null?"":val.get("dispersed_info"));
			yidi_obj.put("dispersed_address",val.get("dispersed_address")==null?"":val.get("dispersed_address"));
			yidi_obj.put("dispersed_price",val.get("dispersed_price")==null?"":val.get("dispersed_price"));
			yidi_obj.put("dispersed_agreement",val.get("dispersed_agreement")==null?"":val.get("dispersed_agreement"));
			yidi_obj.put("v1",val.get("v1")==null?"":val.get("v1"));
			yidi_obj.put("v2",val.get("v2")==null?"":val.get("v2"));
			yidi_obj.put("v3",val.get("v3")==null?"":val.get("v3"));
			//System.out.println(yidi_obj.toString());
			zong.put("yidi", yidi_obj);
		}
				
		response.getWriter().write(zong.toString());
		response.getWriter().close();
		return null;
	
	}
	
	/**
	 * 保存户主信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSavehuzhu(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String where = "";
		String jiatingwhere = "";
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//必填项，判断为空的时候不修改数据库
			where += "v6='"+form_json.get("v6")+"',";
		}
		if(form_json.get("v7")!=null&&!form_json.get("v7").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v7").equals("请选择")){
				where += "v7='',";
			}else{
				where += "v7='"+form_json.get("v7")+"',";
			}
		}
		if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v11").equals("请选择")){
				where += "v11='',";
			}else{
				where += "v11='"+form_json.get("v11")+"',";
			}
		}
		
		if(form_json.get("v12")!=null&&!form_json.get("v12").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v12").equals("请选择")){
				where += "v12='',";
			}else{
				where += "v12='"+form_json.get("v12")+"',";
			}
		}
		if(form_json.get("v13")!=null&&!form_json.get("v13").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v13").equals("请选择")){
				where += "v13='',";
			}else{
				where += "v13='"+form_json.get("v13")+"',";
			}
		}
		if(form_json.get("v14")!=null&&!form_json.get("v14").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v14").equals("请选择")){
				where += "v14='',";
			}else{
				where += "v14='"+form_json.get("v14")+"',";
			}
		}
		if(form_json.get("v15")!=null&&!form_json.get("v15").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v15").equals("请选择")){
				where += "v15='',";
			}else{
				where += "v15='"+form_json.get("v15")+"',";
			}
		}
		if(form_json.get("v28")!=null&&!form_json.get("v28").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v28").equals("请选择")){
				where += "v28='',";
			}else{
				where += "v28='"+form_json.get("v28")+"',";
			}
		}
		if(form_json.get("v18")!=null&&!form_json.get("v18").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v18='"+form_json.get("v18")+"',";
		}else{
			where += "v18='',";
		}
		if(form_json.get("v19")!=null&&!form_json.get("v19").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v19='"+form_json.get("v19")+"',";
		}else{
			where += "v19='',";
		}
		
		
		if(form_json.get("v25")!=null&&!form_json.get("v25").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v25='"+form_json.get("v25")+"',";
			jiatingwhere += "v25='"+form_json.get("v25")+"',";
		}else{
			where += "v25='',";
			jiatingwhere += "v25='',";
		}
		
		if(form_json.get("v26")!=null&&!form_json.get("v26").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v26='"+form_json.get("v26")+"',";
			jiatingwhere += "v26='"+form_json.get("v26")+"',";
		}else{
			where += "v26='',";
			jiatingwhere += "v26='',";
		}
		if(form_json.get("v27")!=null&&!form_json.get("v27").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v27='"+form_json.get("v27")+"',";
			jiatingwhere += "v27='"+form_json.get("v27")+"',";
		}else{
			where += "v27='',";
			jiatingwhere += "v27='',";
		}
		if(form_json.get("v17")!=null&&!form_json.get("v17").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v17='"+form_json.get("v17")+"',";
		}else{
			where += "v17='',";
		}
		if(form_json.get("v16")!=null&&!form_json.get("v16").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v16").equals("请选择")){
				where += "v16='',";
			}else{
				where += "v16='"+form_json.get("v16")+"',";
			}
		}
		
		if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v8='"+form_json.get("v8")+"',";
		}else{
			where += "v8='',";
		}
		if(form_json.get("sys_standard")!=null&&!form_json.get("sys_standard").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("sys_standard").equals("请选择")){
				where += "sys_standard='',";
			}else{
				where += "sys_standard='"+form_json.get("sys_standard")+"',";
			}
		}
		if(form_json.get("v22")!=null&&!form_json.get("v22").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v22").equals("请选择")){
				where += "v22='',";
				jiatingwhere += "v22='',";
			}else{
				where += "v22='"+form_json.get("v22")+"',";
				jiatingwhere += "v22='"+form_json.get("v22")+"',";
			}
		}

		if(form_json.get("v33")!=null&&!form_json.get("v33").equals("")){//复选框，不一定有值
			if(form_json.get("v33").toString().indexOf(",")>-1){//多选
				JSONArray jsonArray = JSONArray.fromObject(form_json.get("v33"));
				String str = "";
				for(int i = 0;i<jsonArray.size();i++){
					str += jsonArray.getString(i)+",";
				}
				where += "v33='"+str.substring(0, str.length()-1)+"',";
				jiatingwhere += "v33='"+str.substring(0, str.length()-1)+"',";
			}else{//单选
				where += "v33='"+form_json.get("v33")+"',";
				jiatingwhere += "v33='"+form_json.get("v33")+"',";
			}
		}else{
			where += "v33='',";
			jiatingwhere += "v33='',";
		}
		
		if(form_json.get("v29")!=null&&!form_json.get("v29").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v29='"+form_json.get("v29")+"',";
			jiatingwhere += "v29='"+form_json.get("v29")+"',";
		}else{
			where += "v29='',";
			jiatingwhere += "v29='',";
		}
		if(form_json.get("v30")!=null&&!form_json.get("v30").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v30='"+form_json.get("v30")+"',";
			jiatingwhere += "v30='"+form_json.get("v30")+"',";
		}else{
			where += "v30='',";
			jiatingwhere += "v30='',";
		}
		if(form_json.get("v31")!=null&&!form_json.get("v31").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v31='"+form_json.get("v31")+"',";
			jiatingwhere += "v31='"+form_json.get("v31")+"',";
		}else{
			where += "v31='',";
			jiatingwhere += "v31='',";
		}
		if(form_json.get("v32")!=null&&!form_json.get("v32").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v32='"+form_json.get("v32")+"',";
		}else{
			where += "v32='',";
		}
		if(form_json.get("v23")!=null&&!form_json.get("v23").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v23='"+form_json.get("v23")+"',";
			jiatingwhere += "v23='"+form_json.get("v23")+"',";
		}else{
			where += "v23='',";
			jiatingwhere += "v23='',";
		}
		
		String bas_where = "";
		if(form_json.get("hz_zpyy")!=null&&!form_json.get("hz_zpyy").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			
			String dest = "";
	        if (form_json.get("hz_zpyy").toString()!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(form_json.get("hz_zpyy").toString());
	            dest = m.replaceAll("");
	        }
			
			bas_where += "basic_explain='"+dest+"',";
		}else{
			bas_where += "basic_explain='',";
		}
		if(form_json.get("hz_jtzz")!=null&&!form_json.get("hz_jtzz").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			bas_where += "basic_address='"+form_json.get("hz_jtzz")+"',";
		}else{
			bas_where += "basic_address='',";
		}
		
		if(where.length()>0){
			where = where.substring(0, where.length()-1);
		}
		
		if(jiatingwhere.length()>0){
			jiatingwhere = jiatingwhere.substring(0, jiatingwhere.length()-1);
		}
		
		if(bas_where.length()>0){
			bas_where = bas_where.substring(0, bas_where.length()-1);
		}
		
		String hu_sql = "update da_household set "+where+" where pkid="+pkid;
		String jiating_sql = "update da_member set "+jiatingwhere+" where da_household_id="+pkid;
		String hu_bas_sql = "update da_household_basic set "+bas_where+" where da_household_id="+pkid;
		
//		System.out.println(hu_sql);
//		System.out.println(hu_bas_sql);
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		SQLAdapter jiating_sql_Adapter = new SQLAdapter(jiating_sql);
		SQLAdapter hu_bas_sql_Adapter = new SQLAdapter(hu_bas_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			this.getBySqlMapper.insertSelective(jiating_sql_Adapter);
			this.getBySqlMapper.insertSelective(hu_bas_sql_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','基本情况','户主情况')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}
	
	/**
	 * 保存新的家庭成员
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveNewjiating(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String name = request.getParameter("name");
		
		String hu_bas_sql = "insert into da_member(v1,v2,v3,v4,v5,v6,v9,v21,v23,v25,v26,v27,da_household_id) select v1,v2,v3,v4,v5,'"+name+"',v9,v21,v23,v25,v26,v27,pkid from da_household where pkid="+pkid;
		//System.out.println(hu_bas_sql);
		SQLAdapter hu_bas_sql_Adapter = new SQLAdapter(hu_bas_sql);
		try{
			this.getBySqlMapper.insertSelective(hu_bas_sql_Adapter);
			getjiatingchengyuan(pkid);//更新家庭成员人数
			
			String hou_sql = "select pkid from da_member where da_household_id="+pkid+" and v6='"+name+"'";
    		SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
    		int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_member',"+da_household_id+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','扶贫手册','新增家庭成员')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 删除家庭成员
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getDelJiatingchengyuan(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String huzhuid = request.getParameter("huzhuid");
		
		String me_add_sql = "insert into da_member_del select * from da_member where pkid="+pkid;
		SQLAdapter me_add_Adapter = new SQLAdapter(me_add_sql);
		
		String me_del_sql = "delete from da_member where pkid="+pkid;
		SQLAdapter me_del_Adapter = new SQLAdapter(me_del_sql);
		
		try{
			
			this.getBySqlMapper.insertSelective(me_add_Adapter);
			this.getBySqlMapper.insertSelective(me_del_Adapter);
			getjiatingchengyuan(huzhuid);//更新家庭成员人数
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_member_del',"+pkid+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','扶贫手册','删除家庭成员')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
				
			}
			
			response.getWriter().write("1");
		}catch(Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 保存家庭成员信息 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSavejiating(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String where = "";
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//必填项，判断为空的时候不修改数据库
			where += "v6='"+form_json.get("v6")+"',";
		}
		if(form_json.get("v7")!=null&&!form_json.get("v7").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v7").equals("请选择")){
				where += "v7='',";
			}else{
				where += "v7='"+form_json.get("v7")+"',";
			}
		}
		if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v8='"+form_json.get("v8")+"',";
		}else{
			where += "v8='',";
		}
		if(form_json.get("v10")!=null&&!form_json.get("v10").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v10").equals("请选择")){
				where += "v10='',";
			}else{
				where += "v10='"+form_json.get("v10")+"',";
			}
		}
		if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v11").equals("请选择")){
				where += "v11='',";
			}else{
				where += "v11='"+form_json.get("v11")+"',";
			}
		}
		if(form_json.get("v12")!=null&&!form_json.get("v12").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v12").equals("请选择")){
				where += "v12='',";
			}else{
				where += "v12='"+form_json.get("v12")+"',";
			}
		}
		if(form_json.get("v13")!=null&&!form_json.get("v13").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v13").equals("请选择")){
				where += "v13='',";
			}else{
				where += "v13='"+form_json.get("v13")+"',";
			}
		}
		if(form_json.get("v14")!=null&&!form_json.get("v14").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v14").equals("请选择")){
				where += "v14='',";
			}else{
				where += "v14='"+form_json.get("v14")+"',";
			}
		}
		if(form_json.get("v15")!=null&&!form_json.get("v15").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v15").equals("请选择")){
				where += "v15='',";
			}else{
				where += "v15='"+form_json.get("v15")+"',";
			}
		}
		if(form_json.get("v17")!=null&&!form_json.get("v17").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v17='"+form_json.get("v17")+"',";
		}else{
			where += "v17='',";
		}
		if(form_json.get("v16")!=null&&!form_json.get("v16").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v16").equals("请选择")){
				where += "v16='',";
			}else{
				where += "v16='"+form_json.get("v16")+"',";
			}
		}
		if(form_json.get("v18")!=null&&!form_json.get("v18").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v18='"+form_json.get("v18")+"',";
		}else{
			where += "v18='',";
		}
		if(form_json.get("v19")!=null&&!form_json.get("v19").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v19='"+form_json.get("v19")+"',";
		}else{
			where += "v19='',";
		}
		if(form_json.get("v32")!=null&&!form_json.get("v32").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v32='"+form_json.get("v32")+"',";
		}else{
			where += "v32='',";
		}
		if(form_json.get("v28")!=null&&!form_json.get("v28").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v28").equals("请选择")){
				where += "v28='',";
			}else{
				where += "v28='"+form_json.get("v28")+"',";
			}
		}
		
		if(where.length()>0){
			where = where.substring(0, where.length()-1);
		}
		String hu_bas_sql = "update da_member set "+where+" where pkid="+form_json.get("jiating_pkid");
		//System.out.println(hu_bas_sql);
		SQLAdapter hu_bas_sql_Adapter = new SQLAdapter(hu_bas_sql);
		try{
			this.getBySqlMapper.insertSelective(hu_bas_sql_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_member',"+form_json.get("jiating_pkid")+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','基本情况','家庭成员')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 保存生产
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveshengchan(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String where = "";
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v1='"+form_json.get("v1")+"',";
		}else{
			where += "v1=null,";
		}
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v2='"+form_json.get("v2")+"',";
		}else{
			where += "v2=null,";
		}
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v3='"+form_json.get("v3")+"',";
		}else{
			where += "v3=null,";
		}
		if(form_json.get("v4")!=null&&!form_json.get("v4").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v4='"+form_json.get("v4")+"',";
		}else{
			where += "v4=null,";
		}
		if(form_json.get("v5")!=null&&!form_json.get("v5").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v5='"+form_json.get("v5")+"',";
		}else{
			where += "v5=null,";
		}
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v6='"+form_json.get("v6")+"',";
		}else{
			where += "v6=null,";
		}
		if(form_json.get("v7")!=null&&!form_json.get("v7").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			String dest = "";
	        if (form_json.get("v7").toString()!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(form_json.get("v7").toString());
	            dest = m.replaceAll("");
	        }
			where += "v7='"+dest+"',";
		}else{
			where += "v7='',";
		}
		if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v8='"+form_json.get("v8")+"',";
		}else{
			where += "v8='',";
		}
		if(form_json.get("v9")!=null&&!form_json.get("v9").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v9='"+form_json.get("v9")+"',";
		}else{
			where += "v9='',";
		}
		if(form_json.get("v10")!=null&&!form_json.get("v10").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v10='"+form_json.get("v10")+"',";
		}else{
			where += "v10='',";
		}
		if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v11='"+form_json.get("v11")+"',";
		}else{
			where += "v11='',";
		}
		if(form_json.get("v12")!=null&&!form_json.get("v12").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			String dest = "";
	        if (form_json.get("v12").toString()!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(form_json.get("v12").toString());
	            dest = m.replaceAll("");
	        }
			where += "v12='"+dest+"',";
		}else{
			where += "v12='',";
		}
		if(form_json.get("v13")!=null&&!form_json.get("v13").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v13='"+form_json.get("v13")+"',";
		}else{
			where += "v13=null,";
		}
		if(form_json.get("v14")!=null&&!form_json.get("v14").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v14='"+form_json.get("v14")+"',";
		}else{
			where += "v14=null,";
		}
		
		if(where.length()>0){
			where = where.substring(0, where.length()-1);
		}
		
		String hu_sql = "update da_production set "+where+" where da_household_id="+pkid;
		
		//System.out.println(hu_sql);
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','基本情况','生产条件')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 保存生活
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveshenghuo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String where = "";
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v1='"+form_json.get("v1")+"',";
		}else{
			where += "v1=null,";
		}
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v2='"+form_json.get("v2")+"',";
		}else{
			where += "v2='',";
		}
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v3='"+form_json.get("v3")+"',";
		}else{
			where += "v3='',";
		}
		
		if(form_json.get("v4")!=null&&!form_json.get("v4").equals("")){//复选框，不一定有值
			if(form_json.get("v4").toString().indexOf(",")>-1){//多选
				JSONArray jsonArray = JSONArray.fromObject(form_json.get("v4"));
				String str = "";
				for(int i = 0;i<jsonArray.size();i++){
					str += jsonArray.getString(i)+",";
				}
				where += "v4='"+str.substring(0, str.length()-1)+"',";
			}else{//单选
				where += "v4='"+form_json.get("v4")+"',";
			}
		}else{
			where += "v4='',";
		}
		
		if(form_json.get("v5")!=null&&!form_json.get("v5").equals("")){//复选框，不一定有值
			if(form_json.get("v5").toString().indexOf(",")>-1){//多选
				JSONArray jsonArray = JSONArray.fromObject(form_json.get("v5"));
				String str = "";
				for(int i = 0;i<jsonArray.size();i++){
					str += jsonArray.getString(i)+",";
				}
				where += "v5='"+str.substring(0, str.length()-1)+"',";
			}else{//单选
				where += "v5='"+form_json.get("v5")+"',";
			}
		}else{
			where += "v5='',";
		}
		
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//复选框，不一定有值
			if(form_json.get("v6").toString().indexOf(",")>-1){//多选
				JSONArray jsonArray = JSONArray.fromObject(form_json.get("v6"));
				String str = "";
				for(int i = 0;i<jsonArray.size();i++){
					str += jsonArray.getString(i)+",";
				}
				where += "v6='"+str.substring(0, str.length()-1)+"',";
			}else{//单选
				where += "v6='"+form_json.get("v6")+"',";
			}
		}else{
			where += "v6='',";
		}
		
		if(form_json.get("v7")!=null&&!form_json.get("v7").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v7='"+form_json.get("v7")+"',";
		}else{
			where += "v7=null,";
		}
		if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v8='"+form_json.get("v8")+"',";
		}else{
			where += "v8='',";
		}
		if(form_json.get("v9")!=null&&!form_json.get("v9").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v9='"+form_json.get("v9")+"',";
		}else{
			where += "v9='',";
		}
		if(form_json.get("v10")!=null&&!form_json.get("v10").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(form_json.get("v10").equals("请选择")){
				where += "v10='',";
			}else{
				where += "v10='"+form_json.get("v10")+"',";
			}
		}
		if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v11='"+form_json.get("v11")+"',";
		}else{
			where += "v11='',";
		}
		if(form_json.get("v12")!=null&&!form_json.get("v12").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v12='"+form_json.get("v12")+"',";
		}else{
			where += "v12='',";
		}
		
		
		if(where.length()>0){
			where = where.substring(0, where.length()-1);
		}
		
		String hu_sql = "update da_life set "+where+" where da_household_id="+pkid;
		
		//System.out.println(hu_sql);
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','基本情况','生活条件')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 更新家庭成员
	 * @param pkid
	 */
	public void getjiatingchengyuan(String pkid){
		
		String sql = "select count(*) from (select v9 from da_household where pkid="+pkid+" union ALL select v9 from da_member where da_household_id="+pkid+") t1";
		SQLAdapter count_st_Adapter = new SQLAdapter(sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		
		String sql_1 = "update da_household set v9='"+total+"' where pkid="+pkid;
		String sql_2 = "update da_member set v9='"+total+"' where da_household_id="+pkid;
		
		SQLAdapter Adapter_1 = new SQLAdapter(sql_1);
		SQLAdapter Adapter_2 = new SQLAdapter(sql_2);
		try{
			this.getBySqlMapper.insertSelective(Adapter_1);
			this.getBySqlMapper.insertSelective(Adapter_2);
		}catch (Exception e){
		}
	}
	
	
	/**
	 * 读取当前收入与支出
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getCurrent_info(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		String type = request.getParameter("type").trim();
		JSONObject zong = new JSONObject ();
		
		//收入
		String sql = "";
		//支出
		String xian_sql = "";
		if(type.equals("1")){//当前
			sql="select * from da_current_income where da_household_id="+pkid;
			xian_sql="select * from da_current_expenditure where da_household_id="+pkid;
		}else if(type.equals("2")){//帮扶后
			sql="select * from da_helpback_income where da_household_id="+pkid;
			xian_sql="select * from da_helpback_expenditure where da_household_id="+pkid;
		}
		
		
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		Float total_income=(float) 0;//收入总额
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("v1",val.get("v1")==null?"":val.get("v1"));
			obj.put("v2",val.get("v2")==null?"":val.get("v2"));
			obj.put("v3",val.get("v3")==null?"":val.get("v3"));
			obj.put("v4",val.get("v4")==null?"":val.get("v4"));
			obj.put("v5",val.get("v5")==null?"":val.get("v5"));
			obj.put("v6",val.get("v6")==null?"":val.get("v6"));
			obj.put("v7",val.get("v7")==null?"":val.get("v7"));
			obj.put("v8",val.get("v8")==null?"":val.get("v8"));
			obj.put("v11",val.get("v11")==null?"":val.get("v11"));
			obj.put("v12",val.get("v12")==null?"":val.get("v12"));
			obj.put("v13",val.get("v13")==null?"":val.get("v13"));
			obj.put("v14",val.get("v14")==null?"":val.get("v14"));
			obj.put("v15",val.get("v15")==null?"":val.get("v15"));
			obj.put("v16",val.get("v16")==null?"":val.get("v16"));
			obj.put("v17",val.get("v17")==null?"":val.get("v17"));
			obj.put("v18",val.get("v18")==null?"":val.get("v18"));
			obj.put("v19",val.get("v19")==null?"":val.get("v19"));
			obj.put("v20",val.get("v20")==null?"":val.get("v20"));
			obj.put("v23",val.get("v23")==null?"":val.get("v23"));
			obj.put("v24",val.get("v24")==null?"":val.get("v24"));
			obj.put("v25",val.get("v25")==null?"":val.get("v25"));
			obj.put("v26",val.get("v26")==null?"":val.get("v26"));
			obj.put("v27",val.get("v27")==null?"":val.get("v27"));
			obj.put("v28",val.get("v28")==null?"":val.get("v28"));
			obj.put("v29",val.get("v29")==null?"":val.get("v29"));
			obj.put("v30",val.get("v30")==null?"":val.get("v30"));
			obj.put("v31",val.get("v31")==null?"":val.get("v31"));
			obj.put("v32",val.get("v32")==null?"":val.get("v32"));
			obj.put("v33",val.get("v33")==null?"":val.get("v33"));
			obj.put("v34",val.get("v34")==null?"":val.get("v34"));
			obj.put("v35",val.get("v35")==null?"":val.get("v35"));
			obj.put("v36",val.get("v36")==null?"":val.get("v36"));
			obj.put("v37",val.get("v37")==null?"":val.get("v37"));
			obj.put("v38",val.get("v38")==null?"":val.get("v38"));
			
			if(type.equals("1")){//当前
				obj.put("v40",val.get("v40")==null?"":val.get("v40"));
				obj.put("v41",val.get("v41")==null?"":val.get("v41"));
				obj.put("v42",val.get("v42")==null?"":val.get("v42"));
				obj.put("v43",val.get("v43")==null?"":val.get("v43"));
			}
			if(val.get("v39")!=null){
				total_income+=Float.parseFloat(val.get("v39").toString());
			}
			zong.put("shouru", obj);
			zong.put("total_income", total_income);
		}
		
		
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		Float total_expenditure=(float) 0;
		for(Map val:xian_list){
			JSONObject obj=new JSONObject ();
			obj.put("v1",val.get("v1")==null?"":val.get("v1"));
			obj.put("v2",val.get("v2")==null?"":val.get("v2"));
			obj.put("v3",val.get("v3")==null?"":val.get("v3"));
			obj.put("v4",val.get("v4")==null?"":val.get("v4"));
			obj.put("v5",val.get("v5")==null?"":val.get("v5"));
			obj.put("v6",val.get("v6")==null?"":val.get("v6"));
			obj.put("v7",val.get("v7")==null?"":val.get("v7"));
			obj.put("v8",val.get("v8")==null?"":val.get("v8"));
			obj.put("v9",val.get("v9")==null?"":val.get("v9"));
			obj.put("v10",val.get("v10")==null?"":val.get("v10"));
			obj.put("v11",val.get("v11")==null?"":val.get("v11"));
			obj.put("v12",val.get("v12")==null?"":val.get("v12"));
			obj.put("v13",val.get("v13")==null?"":val.get("v13"));
			obj.put("v14",val.get("v14")==null?"":val.get("v14"));
			obj.put("v15",val.get("v15")==null?"":val.get("v15"));
			obj.put("v16",val.get("v16")==null?"":val.get("v16"));
			obj.put("v17",val.get("v17")==null?"":val.get("v17"));
			obj.put("v18",val.get("v18")==null?"":val.get("v18"));
			obj.put("v19",val.get("v19")==null?"":val.get("v19"));
			obj.put("v20",val.get("v20")==null?"":val.get("v20"));
			obj.put("v21",val.get("v21")==null?"":val.get("v21"));
			obj.put("v22",val.get("v22")==null?"":val.get("v22"));
			obj.put("v23",val.get("v23")==null?"":val.get("v23"));
			obj.put("v24",val.get("v24")==null?"":val.get("v24"));
			obj.put("v25",val.get("v25")==null?"":val.get("v25"));
			obj.put("v26",val.get("v26")==null?"":val.get("v26"));
			obj.put("v27",val.get("v27")==null?"":val.get("v27"));
			obj.put("v28",val.get("v28")==null?"":val.get("v28"));
			obj.put("v29",val.get("v29")==null?"":val.get("v29"));
			obj.put("v30",val.get("v30")==null?"":val.get("v30"));
			
			if(val.get("v31")!=null){
				total_expenditure+=Float.parseFloat(val.get("v31").toString());
			}
			
			zong.put("zhichu", obj);
			zong.put("total_expenditure", total_expenditure);
		}
			
		response.getWriter().write(zong.toString());
		response.getWriter().close();
		
		return null;
	}
	
	/**
	 * 保存收入
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaves_shouru(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		String type = request.getParameter("type").trim();
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String where = "";
		float xiaoji_1 = 0;
		float xiaoji_2 = 0;
		float zong = 0;
		float total_income=0;
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v1='"+form_json.get("v1")+"',";
		}else{
			where += "v1=null,";
		}
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v2='"+form_json.get("v2")+"',";
			xiaoji_1 += Float.parseFloat(form_json.get("v2").toString());
		}else{
			where += "v2=0,";
		}
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v3='"+form_json.get("v3")+"',";
		}else{
			where += "v3=null,";
		}
		if(form_json.get("v4")!=null&&!form_json.get("v4").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v4='"+form_json.get("v4")+"',";
			xiaoji_1 += Float.parseFloat(form_json.get("v4").toString());
		}else{
			where += "v4=0,";
		}
		if(form_json.get("v5")!=null&&!form_json.get("v5").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v5='"+form_json.get("v5")+"',";
		}else{
			where += "v5=null,";
		}
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v6='"+form_json.get("v6")+"',";
			xiaoji_1 += Float.parseFloat(form_json.get("v6").toString());
		}else{
			where += "v6=0,";
		}
		if(form_json.get("v7")!=null&&!form_json.get("v7").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v7='"+form_json.get("v7")+"',";
		}else{
			where += "v7=null,";
		}
		if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v8='"+form_json.get("v8")+"',";
			xiaoji_1 += Float.parseFloat(form_json.get("v8").toString());
		}else{
			where += "v8=0,";
		}
		
		if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v11='"+form_json.get("v11")+"',";
		}else{
			where += "v11=null,";
		}
		if(form_json.get("v12")!=null&&!form_json.get("v12").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v12='"+form_json.get("v12")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v12").toString());
		}else{
			where += "v12=0,";
		}
		if(form_json.get("v13")!=null&&!form_json.get("v13").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v13='"+form_json.get("v13")+"',";
		}else{
			where += "v13=null,";
		}
		if(form_json.get("v14")!=null&&!form_json.get("v14").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v14='"+form_json.get("v14")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v14").toString());
		}else{
			where += "v14=0,";
		}
		if(form_json.get("v15")!=null&&!form_json.get("v15").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v15='"+form_json.get("v15")+"',";
		}else{
			where += "v15=null,";
		}
		if(form_json.get("v16")!=null&&!form_json.get("v16").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v16='"+form_json.get("v16")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v16").toString());
		}else{
			where += "v16=0,";
		}
		if(form_json.get("v17")!=null&&!form_json.get("v17").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v17='"+form_json.get("v17")+"',";
		}else{
			where += "v17=null,";
		}
		if(form_json.get("v18")!=null&&!form_json.get("v18").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v18='"+form_json.get("v18")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v18").toString());
		}else{
			where += "v18=0,";
		}
		if(form_json.get("v19")!=null&&!form_json.get("v19").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v19='"+form_json.get("v19")+"',";
		}else{
			where += "v19=null,";
		}
		if(form_json.get("v20")!=null&&!form_json.get("v20").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v20='"+form_json.get("v20")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v20").toString());
		}else{
			where += "v20=0,";
		}
		if(form_json.get("v23")!=null&&!form_json.get("v23").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v23='"+form_json.get("v23")+"',";
		}else{
			where += "v23=null,";
		}
		if(form_json.get("v24")!=null&&!form_json.get("v24").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v24='"+form_json.get("v24")+"',";
			zong += Float.parseFloat(form_json.get("v24").toString());
		}else{
			where += "v24=0,";
		}
		if(form_json.get("v25")!=null&&!form_json.get("v25").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v25='"+form_json.get("v25")+"',";
		}else{
			where += "v25=null,";
		}
		if(form_json.get("v26")!=null&&!form_json.get("v26").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v26='"+form_json.get("v26")+"',";
			zong += Float.parseFloat(form_json.get("v26").toString());
		}else{
			where += "v26=0,";
		}
		if(form_json.get("v27")!=null&&!form_json.get("v27").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v27='"+form_json.get("v27")+"',";
		}else{
			where += "v27=null,";
		}
		if(form_json.get("v28")!=null&&!form_json.get("v28").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v28='"+form_json.get("v28")+"',";
			zong += Float.parseFloat(form_json.get("v28").toString());
		}else{
			where += "v28=0,";
		}
		if(form_json.get("v29")!=null&&!form_json.get("v29").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v29='"+form_json.get("v29")+"',";
		}else{
			where += "v29=null,";
		}
		if(form_json.get("v30")!=null&&!form_json.get("v30").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v30='"+form_json.get("v30")+"',";
			zong += Float.parseFloat(form_json.get("v30").toString());
		}else{
			where += "v30=0,";
		}
		if(form_json.get("v31")!=null&&!form_json.get("v31").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v31='"+form_json.get("v31")+"',";
		}else{
			where += "v31=null,";
		}
		if(form_json.get("v32")!=null&&!form_json.get("v32").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v32='"+form_json.get("v32")+"',";
			zong += Float.parseFloat(form_json.get("v32").toString());
		}else{
			where += "v32=0,";
		}
		if(form_json.get("v33")!=null&&!form_json.get("v33").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v33='"+form_json.get("v33")+"',";
		}else{
			where += "v33=null,";
		}
		if(form_json.get("v34")!=null&&!form_json.get("v34").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v34='"+form_json.get("v34")+"',";
			zong += Float.parseFloat(form_json.get("v34").toString());
		}else{
			where += "v34=0,";
		}
		if(form_json.get("v35")!=null&&!form_json.get("v35").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v35='"+form_json.get("v35")+"',";
		}else{
			where += "v35=null,";
		}
		if(form_json.get("v36")!=null&&!form_json.get("v36").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v36='"+form_json.get("v36")+"',";
		}else{
			where += "v36=null,";
		}
		if(form_json.get("v37")!=null&&!form_json.get("v37").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v37='"+form_json.get("v37")+"',";
		}else{
			where += "v37=null,";
		}
		if(form_json.get("v38")!=null&&!form_json.get("v38").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v38='"+form_json.get("v38")+"',";
		}else{
			where += "v38=null,";
		}
		
		if(form_json.get("v40")!=null&&!form_json.get("v40").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v40='"+form_json.get("v40")+"',";
		}else{
			where += "v40=null,";
		}
		if(form_json.get("v41")!=null&&!form_json.get("v41").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v41='"+form_json.get("v41")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v41").toString());
		}else{
			where += "v41=0,";
		}
		if(form_json.get("v42")!=null&&!form_json.get("v42").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v42='"+form_json.get("v42")+"',";
		}else{
			where += "v42=null,";
		}
		if(form_json.get("v43")!=null&&!form_json.get("v43").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v43='"+form_json.get("v43")+"',";
			xiaoji_2 += Float.parseFloat(form_json.get("v43").toString());
		}else{
			where += "v43=0,";
		}
		
		where += "v10="+xiaoji_1+",v22="+xiaoji_2+",v39="+(zong+xiaoji_1+xiaoji_2);
		total_income+=(zong+xiaoji_1+xiaoji_2);
//		if(where.length()>0){
//			where = where.substring(0, where.length()-1);
//		}
		
		String hu_sql = "";
		
		if(type.equals("1")){//当前
			hu_sql = "update da_current_income set "+where+" where da_household_id="+pkid;
		}else if(type.equals("2")){//帮扶后
			hu_sql = "update da_helpback_income set "+where+" where da_household_id="+pkid;
		}
		
		//System.out.println(hu_sql);
		JSONObject jsonObject=new JSONObject();
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				
				if(type.equals("1")){//当前
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','当前收支','当前收入情况')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}else if(type.equals("2")){//帮扶后
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶后收支分析','帮扶后收入情况')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}
			}
			
			jsonObject.put("isSuccess", "1");
			jsonObject.put("total_income", total_income);
			
		}catch (Exception e){
			jsonObject.put("isSuccess", "0");
		}
		response.getWriter().write(jsonObject.toString());
		return null;
	}
	
	/**
	 * 保存支出
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaves_zhichu(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		String type = request.getParameter("type").trim();
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		float zong = 0;
		float total_expenditure=0;
		String where = "";
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v1='"+form_json.get("v1")+"',";
		}else{
			where += "v1=null,";
		}
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v2='"+form_json.get("v2")+"',";
			zong += Float.parseFloat(form_json.get("v2").toString());
		}else{
			where += "v2=0,";
		}
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v3='"+form_json.get("v3")+"',";
		}else{
			where += "v3=null,";
		}
		if(form_json.get("v4")!=null&&!form_json.get("v4").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v4='"+form_json.get("v4")+"',";
			zong += Float.parseFloat(form_json.get("v4").toString());
		}else{
			where += "v4=0,";
		}
		if(form_json.get("v5")!=null&&!form_json.get("v5").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v5='"+form_json.get("v5")+"',";
		}else{
			where += "v5=null,";
		}
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v6='"+form_json.get("v6")+"',";
			zong += Float.parseFloat(form_json.get("v6").toString());
		}else{
			where += "v6=0,";
		}
		if(form_json.get("v7")!=null&&!form_json.get("v7").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v7='"+form_json.get("v7")+"',";
		}else{
			where += "v7=null,";
		}
		if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v8='"+form_json.get("v8")+"',";
			zong += Float.parseFloat(form_json.get("v8").toString());
		}else{
			where += "v8=0,";
		}
		if(form_json.get("v9")!=null&&!form_json.get("v9").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v9='"+form_json.get("v9")+"',";
		}else{
			where += "v9=null,";
		}
		if(form_json.get("v10")!=null&&!form_json.get("v10").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v10='"+form_json.get("v10")+"',";
			zong += Float.parseFloat(form_json.get("v10").toString());
		}else{
			where += "v10=0,";
		}
		if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v11='"+form_json.get("v11")+"',";
		}else{
			where += "v11=null,";
		}
		if(form_json.get("v12")!=null&&!form_json.get("v12").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v12='"+form_json.get("v12")+"',";
			zong += Float.parseFloat(form_json.get("v12").toString());
		}else{
			where += "v12=0,";
		}
		if(form_json.get("v13")!=null&&!form_json.get("v13").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v13='"+form_json.get("v13")+"',";
		}else{
			where += "v13=null,";
		}
		if(form_json.get("v14")!=null&&!form_json.get("v14").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v14='"+form_json.get("v14")+"',";
			zong += Float.parseFloat(form_json.get("v14").toString());
		}else{
			where += "v14=0,";
		}
		if(form_json.get("v15")!=null&&!form_json.get("v15").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v15='"+form_json.get("v15")+"',";
		}else{
			where += "v15=null,";
		}
		if(form_json.get("v16")!=null&&!form_json.get("v16").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v16='"+form_json.get("v16")+"',";
			zong += Float.parseFloat(form_json.get("v16").toString());
		}else{
			where += "v16=0,";
		}
		if(form_json.get("v17")!=null&&!form_json.get("v17").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v17='"+form_json.get("v17")+"',";
		}else{
			where += "v17=null,";
		}
		if(form_json.get("v18")!=null&&!form_json.get("v18").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v18='"+form_json.get("v18")+"',";
			zong += Float.parseFloat(form_json.get("v18").toString());
		}else{
			where += "v18=0,";
		}
		if(form_json.get("v19")!=null&&!form_json.get("v19").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v19='"+form_json.get("v19")+"',";
		}else{
			where += "v19=null,";
		}
		if(form_json.get("v20")!=null&&!form_json.get("v20").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v20='"+form_json.get("v20")+"',";
			zong += Float.parseFloat(form_json.get("v20").toString());
		}else{
			where += "v20=0,";
		}
		if(form_json.get("v21")!=null&&!form_json.get("v21").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v21='"+form_json.get("v21")+"',";
		}else{
			where += "v21=null,";
		}
		if(form_json.get("v22")!=null&&!form_json.get("v22").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v22='"+form_json.get("v22")+"',";
			zong += Float.parseFloat(form_json.get("v22").toString());
		}else{
			where += "v22=0,";
		}
		if(form_json.get("v23")!=null&&!form_json.get("v23").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v23='"+form_json.get("v23")+"',";
		}else{
			where += "v23=null,";
		}
		if(form_json.get("v24")!=null&&!form_json.get("v24").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v24='"+form_json.get("v24")+"',";
		}else{
			where += "v24=null,";
		}
		if(form_json.get("v25")!=null&&!form_json.get("v25").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v25='"+form_json.get("v25")+"',";
		}else{
			where += "v25=null,";
		}
		if(form_json.get("v26")!=null&&!form_json.get("v26").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v26='"+form_json.get("v26")+"',";
		}else{
			where += "v26=null,";
		}
		if(form_json.get("v27")!=null&&!form_json.get("v27").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v27='"+form_json.get("v27")+"',";
			zong += Float.parseFloat(form_json.get("v27").toString());
		}else{
			where += "v27=0,";
		}
		if(form_json.get("v28")!=null&&!form_json.get("v28").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v28='"+form_json.get("v28")+"',";
		}else{
			where += "v28=null,";
		}
		if(form_json.get("v29")!=null&&!form_json.get("v29").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v29='"+form_json.get("v29")+"',";
		}else{
			where += "v29=null,";
		}
		if(form_json.get("v30")!=null&&!form_json.get("v30").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			where += "v30='"+form_json.get("v30")+"',";
			zong += Float.parseFloat(form_json.get("v30").toString());
		}else{
			where += "v30=0,";
		}
		
		where += "v31="+zong;
		total_expenditure+=zong;
//		if(where.length()>0){
//			where = where.substring(0, where.length()-1);
//		}
		String hu_sql = "";
		
		if(type.equals("1")){//当前
			hu_sql = "update da_current_expenditure set "+where+" where da_household_id="+pkid;
		}else if(type.equals("2")){//帮扶后
			hu_sql = "update da_helpback_expenditure set "+where+" where da_household_id="+pkid;
		}
		
		//System.out.println(hu_sql);
		JSONObject jsonObject=new JSONObject();
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				
				if(type.equals("1")){//当前
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','当前收支','当前支出情况')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}else if(type.equals("2")){//帮扶后
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶后收支分析','帮扶后支出情况')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}
			}
			jsonObject.put("isSuccess", "1");
			jsonObject.put("total_expenditure", total_expenditure);
			
		}catch (Exception e){
			jsonObject.put("isSuccess", "0");
		}
		response.getWriter().write(jsonObject.toString());
		return null;
	}
	
	/**
	 * 查找帮扶责任人信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getPersonal(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String sys_company_id = request.getParameter("sys_company_id").trim();
		String huid = request.getParameter("huid").trim();
		String where = "";
		if(sys_company_id!=null&&!sys_company_id.equals("")){
			where = " where t2.sys_company_id="+sys_company_id+" and t3.sys_personal_id is null";
		}
		String st_sql = "SELECT t1.pkid,t1.col_name,t2.v1 as com_name,t1.col_post,t1.telephone from sys_personal t1 join da_company t2 on t1.da_company_id=t2.pkid "
				+ "left join (select * from sys_personal_household_many where da_household_id="+huid+") t3 on t1.pkid=t3.sys_personal_id"+where;
		//System.out.println(st_sql);
		String count_sql = "SELECT count(*) from sys_personal t1 join da_company t2 on t1.da_company_id=t2.pkid "
				+ "left join (select * from sys_personal_household_many where da_household_id="+huid+") t3 on t1.pkid=t3.sys_personal_id"+where;
		
		SQLAdapter count_st_Adapter = new SQLAdapter(count_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		
//		System.out.println(st_sql);
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(st_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		
		JSONArray jsa=new JSONArray();
		if(Patient_st_List.size()>0){
			
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Patient_st_map.keySet()) {
					
					
					val.put("pkid", Patient_st_map.get("pkid"));
					
					if(Patient_st_map.get("col_name").toString().indexOf("、")>-1){
						val.put("col_name", Patient_st_map.get("col_name").toString().replaceAll("、","<br>"));
					}else{
						val.put("col_name", Patient_st_map.get("col_name"));
					}
					
					
					if(Patient_st_map.get("com_name").toString().indexOf("、")>-1){
						val.put("com_name", Patient_st_map.get("com_name").toString().replaceAll("、","<br>"));
					}else if(Patient_st_map.get("com_name").toString().length()>15){
						String s = Patient_st_map.get("com_name").toString();
						s = s.substring(0, 10)+"<br>"+s.substring(10);
						val.put("com_name", s);
					}else{
						val.put("com_name", Patient_st_map.get("com_name"));
					}
					
					if(Patient_st_map.get("col_post").toString().indexOf("、")>-1){
						val.put("col_post", Patient_st_map.get("col_post").toString().replaceAll("、","<br>"));
					}else if(Patient_st_map.get("col_post").toString().length()>15){
						String s = Patient_st_map.get("col_post").toString();
						s = s.substring(0, 10)+"<br>"+s.substring(10);
						val.put("col_post", s);
					}else{
						val.put("col_post", Patient_st_map.get("col_post"));
					}
					
					if(Patient_st_map.get("telephone").toString().indexOf("、")>-1){
						val.put("telephone", Patient_st_map.get("telephone").toString().replaceAll("、","<br>"));
					}else if(Patient_st_map.get("telephone").toString().length()>15){
						String s = Patient_st_map.get("telephone").toString();
						s = s.substring(0, 10)+"<br>"+s.substring(11);
						val.put("telephone", s);
					}else{
						val.put("telephone", Patient_st_map.get("telephone"));
					}
					
				}
				jsa.add(val);
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("message", "");
		json.put("value", jsa);
		json.put("code", total);
		json.put("redirect", "");
		
		response.getWriter().write(json.toString());
		return null;
	}
	
	/**
	 * 根据户主ID获取帮扶责任人列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getPersonal_household(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();
		JSONObject zong = new JSONObject ();
		
		//相关的帮扶责任人人
		String sql="select x.pkid,y.col_name,y.com_name,y.col_post,y.telephone from sys_personal_household_many x,"
				+ "(SELECT t1.pkid,t1.col_name,t2.v1 as com_name,t1.col_post,t1.telephone from sys_personal t1 join da_company t2 on t1.da_company_id=t2.pkid) y "
				+ "where x.sys_personal_id=y.pkid and da_household_id="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		JSONArray jsa=new JSONArray();
		for(Map val:list){
			
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("col_name",val.get("col_name")==null?"":val.get("col_name"));
			obj.put("com_name",val.get("com_name")==null?"":val.get("com_name"));
			obj.put("col_post",val.get("col_post")==null?"":val.get("col_post"));
			obj.put("telephone",val.get("telephone")==null?"":val.get("telephone"));
			jsa.add(obj);
		}
		zong.put("bangfu", jsa);
		
		String da_help_info_sql = "select * from da_help_info where da_household_id="+pkid;
		SQLAdapter helpAdapter =new SQLAdapter(da_help_info_sql);
		List<Map> help_list=getBySqlMapper.findRecords(helpAdapter);
		JSONObject helpjsa=new JSONObject ();
		for(Map val:help_list){
			
			helpjsa.put("v1",val.get("v1")==null?"":val.get("v1"));
			helpjsa.put("v2",val.get("v2")==null?"":val.get("v2"));
			helpjsa.put("v3",val.get("v3")==null?"":val.get("v3"));
		}
		zong.put("da_help_info", helpjsa);
		
		//帮扶目标计划等内容
		String xian_sql="select * from da_help_plan where da_household_id="+pkid+" order by v1" ;
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		
		if(xian_list.size()>0){
			JSONObject obj=new JSONObject ();
			for(int i = 0;i<xian_list.size();i++){
				Map val = xian_list.get(i);
				String v1 = val.get("v1")==null?"":val.get("v1").toString();//年度
				obj.put("v2",val.get("v2")==null?"":val.get("v2"));//贫困户人均可支配收入现状
				obj.put("v3_"+v1,val.get("v3")==null?"":val.get("v3"));
				obj.put("v4_"+v1,val.get("v4")==null?"":val.get("v4"));
				obj.put("v5",val.get("v5")==null?"":val.get("v5"));//有安全住房现状
				obj.put("v6_"+v1,val.get("v6")==null?"":val.get("v6"));
				obj.put("v7_"+v1,val.get("v7")==null?"":val.get("v7"));
				obj.put("v8",val.get("v8")==null?"":val.get("v8"));//无因贫辍学学生现状
				obj.put("v9_"+v1,val.get("v9")==null?"":val.get("v9"));
				obj.put("v10_"+v1,val.get("v10")==null?"":val.get("v10"));
				obj.put("v11",val.get("v11")==null?"":val.get("v11"));//无因病致贫返贫现状
				obj.put("v12_"+v1,val.get("v12")==null?"":val.get("v12"));
				obj.put("v13_"+v1,val.get("v13")==null?"":val.get("v13"));
				obj.put("v14",val.get("v14")==null?"":val.get("v14"));//参加新型农村合作医疗现状
				obj.put("v15_"+v1,val.get("v15")==null?"":val.get("v15"));
				obj.put("v16_"+v1,val.get("v16")==null?"":val.get("v16"));
				obj.put("v17",val.get("v17")==null?"":val.get("v17"));//参加城乡居民基本养老保险现状
				obj.put("v18_"+v1,val.get("v18")==null?"":val.get("v18"));
				obj.put("v19_"+v1,val.get("v19")==null?"":val.get("v19"));
			}
			zong.put("help_info", obj);
		}else{
			//没有查询到贫困户的记录，需要添加记录
			String sql_1 = "insert into da_help_plan(da_household_id,v1) values("+pkid+",'2016')";
			String sql_2 = "insert into da_help_plan(da_household_id,v1) values("+pkid+",'2017')";
			String sql_3 = "insert into da_help_plan(da_household_id,v1) values("+pkid+",'2018')";
			String sql_4 = "insert into da_help_plan(da_household_id,v1) values("+pkid+",'2019')";
			SQLAdapter M1_Adapter = new SQLAdapter(sql_1);
			SQLAdapter M2_Adapter = new SQLAdapter(sql_2);
			SQLAdapter M3_Adapter = new SQLAdapter(sql_3);
			SQLAdapter M4_Adapter = new SQLAdapter(sql_4);
			try{
				this.getBySqlMapper.insertSelective(M1_Adapter);
				this.getBySqlMapper.insertSelective(M2_Adapter);
				this.getBySqlMapper.insertSelective(M3_Adapter);
				this.getBySqlMapper.insertSelective(M4_Adapter);
			}catch (Exception e){
				
			}
		}
			
		response.getWriter().write(zong.toString());
		response.getWriter().close();
		
		return null;
	}
	
	/**
	 * 保存帮扶责任人与贫困户之间的关系
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getSave_Personal(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid").trim();
		String huid = request.getParameter("huid").trim();
		
		if(pkid!=null&&!pkid.equals("")){
			if(huid!=null&&!huid.equals("")){
				
				String sql="INSERT INTO sys_personal_household_many(sys_personal_id,da_household_id) VALUES("+pkid+","+huid+")";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				try{
					this.getBySqlMapper.findRecords(sqlAdapter);
					
					HttpSession session = request.getSession();
					JSONObject json = new JSONObject();
					if(session.getAttribute("Login_map")!=null){//验证session不为空
						Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
						SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
						String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
								" VALUES ('da_household',"+huid+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶责任人和计划','指定帮扶责任人')";
						SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
						this.getBySqlMapper.findRecords(hqlAdapter1);
					}
					
					response.getWriter().write("1");
				}catch (Exception e){
					response.getWriter().write("0");
				}
			}
		}
		return null;
	}
	
	/**
	 * 解除责任人与贫困户的关系
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getDel_Personal(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid").trim();
		
		String hou_sql = "select da_household_id from sys_personal_household_many where pkid="+pkid;
		SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
		int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
		
		String up_Sql = "delete from sys_personal_household_many where pkid="+pkid;
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(up_Sql);
		
		try{
			this.getBySqlMapper.deleteSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+da_household_id+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶责任人和计划','解除帮扶责任人')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 保存帮扶计划
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getSave_Mubiao(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String where = "";
		
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			String dest = "";
	        if (form_json.get("v1")!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(form_json.get("v1").toString());
	            dest = m.replaceAll("");
	        }
			where += "v1='"+dest+"',";
		}else{
			where += "v1=null,";
		}
		
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			String dest = "";
	        if (form_json.get("v2")!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(form_json.get("v2").toString());
	            dest = m.replaceAll("");
	        }
			where += "v2='"+dest+"',";
		}else{
			where += "v2=null,";
		}
		
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			String dest = "";
	        if (form_json.get("v3")!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(form_json.get("v3").toString());
	            dest = m.replaceAll("");
	        }
			where += "v3='"+dest+"',";
		}else{
			where += "v3=null,";
		}
		
		if(where.length()>0){
			where = where.substring(0, where.length()-1);
		}
		String sql = "update da_help_info set "+where+" where da_household_id="+pkid;
//		System.out.println(sql);
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(sql);
		
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶责任人和计划','帮扶计划')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
//	
//	/**
//	 * 保存帮扶时限
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ModelAndView getSave_Shixian(HttpServletRequest request,HttpServletResponse response) throws Exception{
//		
//		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		
//		String pkid = request.getParameter("pkid").trim();
//		String baocunneirong = request.getParameter("baocunneirong").trim();
//		String dest = "";
//        if (baocunneirong!=null) {
//            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//            Matcher m = p.matcher(baocunneirong);
//            dest = m.replaceAll("");
//        }
//		
//		String hu_sql = "update da_help_info set v2='"+baocunneirong+"' where da_household_id="+pkid;
//		
//		//System.out.println(hu_sql);
//		
//		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
//		try{
//			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
//			response.getWriter().write("1");
//		}catch (Exception e){
//			response.getWriter().write("0");
//		}
//		return null;
//	}
	
	/**
	 * 保存脱贫计划
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getSave_Jihua(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String returnval = "";
		String year[] = {"2016","2017","2018","2019"};
		for(int i = 0;i<year.length;i++){
			String where = "";
			String sql = "";
			
			if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v2='"+form_json.get("v2")+"',";
			}else{
				where += "v2=null,";
			}
			
			if(form_json.get("v3_"+year[i])!=null&&!form_json.get("v3_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v3='"+form_json.get("v3_"+year[i])+"',";
			}else{
				where += "v3=null,";
			}
			
			if(form_json.get("v4_"+year[i])!=null&&!form_json.get("v4_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v4='"+form_json.get("v4_"+year[i])+"',";
			}else{
				where += "v4=null,";
			}
			
			if(form_json.get("v5")!=null&&!form_json.get("v5").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v5='"+form_json.get("v5")+"',";
			}else{
				where += "v5=null,";
			}
			
			if(form_json.get("v6_"+year[i])!=null&&!form_json.get("v6_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v6='"+form_json.get("v6_"+year[i])+"',";
			}else{
				where += "v6=null,";
			}
			
			if(form_json.get("v7_"+year[i])!=null&&!form_json.get("v7_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v7='"+form_json.get("v7_"+year[i])+"',";
			}else{
				where += "v7=null,";
			}
			
			if(form_json.get("v8")!=null&&!form_json.get("v8").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v8='"+form_json.get("v8")+"',";
			}else{
				where += "v8=null,";
			}
			
			if(form_json.get("v9_"+year[i])!=null&&!form_json.get("v9_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v9='"+form_json.get("v9_"+year[i])+"',";
			}else{
				where += "v9=null,";
			}
			
			if(form_json.get("v10_"+year[i])!=null&&!form_json.get("v10_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v10='"+form_json.get("v10_"+year[i])+"',";
			}else{
				where += "v10=null,";
			}
			
			if(form_json.get("v11")!=null&&!form_json.get("v11").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v11='"+form_json.get("v11")+"',";
			}else{
				where += "v11=null,";
			}
			
			if(form_json.get("v12_"+year[i])!=null&&!form_json.get("v12_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v12='"+form_json.get("v12_"+year[i])+"',";
			}else{
				where += "v12=null,";
			}
			
			if(form_json.get("v13_"+year[i])!=null&&!form_json.get("v13_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v13='"+form_json.get("v13_"+year[i])+"',";
			}else{
				where += "v13=null,";
			}
			
			if(form_json.get("v14")!=null&&!form_json.get("v14").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v14='"+form_json.get("v14")+"',";
			}else{
				where += "v14=null,";
			}
			
			if(form_json.get("v15_"+year[i])!=null&&!form_json.get("v15_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v15='"+form_json.get("v15_"+year[i])+"',";
			}else{
				where += "v15=null,";
			}
			
			if(form_json.get("v16_"+year[i])!=null&&!form_json.get("v16_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v16='"+form_json.get("v16_"+year[i])+"',";
			}else{
				where += "v16=null,";
			}
			
			if(form_json.get("v17")!=null&&!form_json.get("v17").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v17='"+form_json.get("v17")+"',";
			}else{
				where += "v17=null,";
			}
			
			if(form_json.get("v18_"+year[i])!=null&&!form_json.get("v18_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v18='"+form_json.get("v18_"+year[i])+"',";
			}else{
				where += "v18=null,";
			}
			
			if(form_json.get("v19_"+year[i])!=null&&!form_json.get("v19_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v19='"+form_json.get("v19_"+year[i])+"',";
			}else{
				where += "v19=null,";
			}
			
			if(where.length()>0){
				where = where.substring(0, where.length()-1);
			}
			sql = "update da_help_plan set "+where+" where da_household_id="+pkid+" and v1='"+year[i]+"'; ";
//			System.out.println(sql);
			SQLAdapter Metadata_table_Adapter = new SQLAdapter(sql);
			
			try{
				this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
				returnval = "1";
			}catch (Exception e){
				response.getWriter().write("0");
			}
		}
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
					" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶责任人和计划','脱贫计划')";
			SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
			this.getBySqlMapper.findRecords(hqlAdapter1);
		}
		response.getWriter().write(returnval);
		
		return null;
	}
	
	/**
	 * 台账-帮扶措施
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getcuoshi_tz_info(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//贫困户ID
		String  year = request.getParameter("year");//年份
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String people_sql = " select v1,v2,v3, ";
		people_sql += " MAX(CASE v7 WHEN '2016' THEN v4 ELSE '' END ) v4_2016, ";
		people_sql += " MAX(CASE v7 WHEN '2016' THEN v5 ELSE '' END ) v5_2016, ";
		people_sql += " MAX(CASE v7 WHEN '2016' THEN v6 ELSE '' END ) v6_2016, ";
		people_sql += " MAX(CASE v7 WHEN '2017' THEN v4 ELSE '' END ) v4_2017, ";
		people_sql += " MAX(CASE v7 WHEN '2017' THEN v5 ELSE '' END ) v5_2017, ";
		people_sql += " MAX(CASE v7 WHEN '2017' THEN v6 ELSE '' END ) v6_2017, ";
		people_sql += " MAX(CASE v7 WHEN '2018' THEN v4 ELSE '' END ) v4_2018, ";
		people_sql += " MAX(CASE v7 WHEN '2018' THEN v5 ELSE '' END ) v5_2018, ";
		people_sql += " MAX(CASE v7 WHEN '2018' THEN v6 ELSE '' END ) v6_2018, ";
		people_sql += " MAX(CASE v7 WHEN '2019' THEN v4 ELSE '' END ) v4_2019, ";
		people_sql += " MAX(CASE v7 WHEN '2019' THEN v5 ELSE '' END ) v5_2019, ";
		people_sql += " MAX(CASE v7 WHEN '2019' THEN v6 ELSE '' END ) v6_2019 ";
		people_sql += " from da_help_tz_measures"+year+" where da_household_id="+pkid+" group  by v1,v2,v3 ";
		
//		System.out.println(people_sql);
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		if(Patient_st_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				JSONObject val = new JSONObject();
				String v1 = "",v2 = "",v3 = "",v4_2016 = "",v5_2016 = "",v6_2016 = "";
				String v4_2017 = "",v5_2017 = "",v6_2017 = "";
				String v4_2018 = "",v5_2018 = "",v6_2018 = "";
				String v4_2019 = "",v5_2019 = "",v6_2019 = "";
				for (Object key : Patient_st_map.keySet()) {
					
					val.put(key, Patient_st_map.get(key));
					
					if(key.toString().equals("v1")){
						v1 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v2")){
						v2 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v3")){
						v3 = Patient_st_map.get(key).toString();
					}
					
					if(key.toString().equals("v4_2016")){
						v4_2016 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v5_2016")){
						v5_2016 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v6_2016")){
						v6_2016 = Patient_st_map.get(key).toString();
					}
					
					if(key.toString().equals("v4_2017")){
						v4_2017 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v5_2017")){
						v5_2017 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v6_2017")){
						v6_2017 = Patient_st_map.get(key).toString();
					}
					
					if(key.toString().equals("v4_2018")){
						v4_2018 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v5_2018")){
						v5_2018 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v6_2018")){
						v6_2018 = Patient_st_map.get(key).toString();
					}
					
					if(key.toString().equals("v4_2019")){
						v4_2019 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v5_2019")){
						v5_2019 = Patient_st_map.get(key).toString();
					}
					if(key.toString().equals("v6_2019")){
						v6_2019 = Patient_st_map.get(key).toString();
					}
				}
				val.put("caozuo", "<button  type=\"button\" class=\"btn btn-primary btn-xs jbqk\" onclick=\"update_tz_cuoshi('"+v1+"','"+v2+"','"+v3+"',"
						+ "'"+v4_2016+"','"+v5_2016+"','"+v6_2016+"','"+v4_2017+"','"+v5_2017+"','"+v6_2017+"','"+v4_2018+"','"+v5_2018+"','"+v6_2018+"','"+v4_2019+"','"+v5_2019+"','"+v6_2019+"');\"><i class=\"fa fa-pencil\"></i> 编辑 </button>&nbsp;&nbsp;&nbsp;&nbsp;<button  type=\"button\" class=\"btn btn-primary btn-xs jbqk\" onclick=\"del_tz('"+v1+"','"+v2+"','"+pkid+"');\"><i class=\"fa fa-remove\"></i> 删除 </button>");
				jsa.add(val);
			}
			JSONObject json = new JSONObject();
			json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
			response.getWriter().write(jsa.toString());
		}
		
		return null;
	}
	
	/**
	 * 获取台账未使用的帮扶措施条目 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView get_tz_cuoshi_tiaomu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid");
		
		String str1[] = {"主导产业-规模种植","主导产业-设施农业","主导产业-特色种植","主导产业-规模养殖","主导产业-棚圈建设","主导产业-特色养殖","主导产业-小额贷款","主导产业-电商扶贫","主导产业-旅游扶贫","主导产业-光伏扶贫",
				"主导产业-农业保险","主导产业-农机局购置补贴","主导产业-危房改造","主导产业-安全饮水","主导产业-易地扶贫搬迁","生态保护-实施生态项目","生态保护-安置护林员","教育扶贫-学前教育","教育扶贫-义务教育","教育扶贫-高中、中职教育",
				"教育扶贫-高等教育","医疗救助-新农合住院报销","医疗救助-新农合参合费用补助","医疗救助-大病保险报销","社会救助和保障-农村低保","社会救助和保障-农村五保供养","社会救助和保障-农村特困家庭医疗救助","社会救助和保障-临时救助",
				"社会救助和保障-灾害救助","社会救助和保障-城乡居民社会养老保险","社会救助和保障-高龄老人生活补贴","社会救助和保障-农村参加养老保险人员丧葬费","社会救助和保障-残疾人补贴","劳动力培训-有培训需要的劳动力参加培训","其它-其它"};
		JSONArray jsa=new JSONArray();
		
		String people_sql = " select v1,v2 from da_help_tz_measures where da_household_id="+pkid+" group by v1,v2 ";
//		System.out.println(people_sql);
		SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		if(Patient_st_List.size()>0){
			
			String str[] = new String[Patient_st_List.size()];
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				
				str[i] = Patient_st_map.get("v1")+"-"+Patient_st_map.get("v2");
				
			}
			String[] arrResult = arrContrast(str1, str);
			
			if(arrResult.length>1){
				for (String strResult : arrResult) {
					jsa.add(strResult);
				}
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().write("");
			}
		}else{
			for (String strResult : str1) {
				jsa.add(strResult);
			}
			response.getWriter().write(jsa.toString());
		}
		return null;
	}
	
	//处理数组字符  
    private String[] arrContrast(String[] arr1, String[] arr2){
        List<String> list = new LinkedList<String>();  
        for (String str : arr1) {//处理第一个数组,list里面的值为1,2,3,4  
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : arr2) {//如果第二个数组存在和第一个数组相同的值，就删除  
            if(list.contains(str)){
                list.remove(str);
            }
        }
        String[] result = {};//创建空数组  
        return list.toArray(result);//List to Array  
    }
	
	/**
	 * 保存台账措施
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getSave_tz_cuoshi(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String returnval = "";
		String year[] = {"2016","2017","2018","2019"};
		for(int i = 0;i<year.length;i++){
			String v1 = "",v2 = "",v3 = "",v4 = "",v5 = "",v6 = "";
			String sql = "";
			
			if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				String str[] = form_json.get("v1").toString().split("-");
				v1 = str[0];
				v2 = str[1];
			}
			
			if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				v3 = form_json.get("v3").toString();
			}
			
			if(form_json.get("v4_"+year[i])!=null&&!form_json.get("v4_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				v4 = form_json.get("v4_"+year[i]).toString();
			}
			
			if(form_json.get("v5_"+year[i])!=null&&!form_json.get("v5_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				v5 = form_json.get("v5_"+year[i]).toString();
			}
			
			if(form_json.get("v6_"+year[i])!=null&&!form_json.get("v6_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				v6 = form_json.get("v6_"+year[i]).toString();
			}
			
			sql = "insert into da_help_tz_measures(da_household_id,v1,v2,v3,v4,v5,v6,v7) values('"+pkid+"','"+v1+"','"+v2+"','"+v3+"','"+v4+"','"+v5+"','"+v6+"','"+year[i]+"'); ";
//			System.out.println(sql);
			SQLAdapter Metadata_table_Adapter = new SQLAdapter(sql);
			
			try{
				this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
				returnval = "1";
			}catch (Exception e){
				response.getWriter().write("0");
			}
		}
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
					" VALUES ('da_household',"+pkid+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶措施','"+form_json.get("v1")+"')";
			SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
			this.getBySqlMapper.findRecords(hqlAdapter1);
		}
		response.getWriter().write(returnval);
		
		return null;
	}
	
	/**
	 * 修改台账措施
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getUpdate_tz_cuoshi(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String v1 = form_json.get("v1").toString();
		String v2 = form_json.get("v2").toString();
		
		String returnval = "";
		String year[] = {"2016","2017","2018","2019"};
		for(int i = 0;i<year.length;i++){
			String where = "";
			String sql = "";
			
			if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v3='"+form_json.get("v3")+"',";
			}else{
				where += "v3=null,";
			}
			
			if(form_json.get("v4_"+year[i])!=null&&!form_json.get("v4_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v4='"+form_json.get("v4_"+year[i])+"',";
			}else{
				where += "v4=null,";
			}
			
			if(form_json.get("v5_"+year[i])!=null&&!form_json.get("v5_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v5='"+form_json.get("v5_"+year[i])+"',";
			}else{
				where += "v5=null,";
			}
			
			if(form_json.get("v6_"+year[i])!=null&&!form_json.get("v6_"+year[i]).equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
				where += "v6='"+form_json.get("v6_"+year[i])+"',";
			}else{
				where += "v6=null,";
			}
			
			if(where.length()>0){
				where = where.substring(0, where.length()-1);
			}
			sql = "update da_help_tz_measures set "+where+" where da_household_id="+pkid+" and v7='"+year[i]+"' and v1='"+v1+"' and v2='"+v2+"'; ";
//			System.out.println(sql);
			SQLAdapter Metadata_table_Adapter = new SQLAdapter(sql);
			
			try{
				this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
				returnval = "1";
			}catch (Exception e){
				response.getWriter().write("0");
			}
		}
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
					" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶措施','"+v1+"-"+v2+"')";
			SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
			this.getBySqlMapper.findRecords(hqlAdapter1);
		}
		response.getWriter().write(returnval);
		
		return null;
	}
	
	/**
	 * 删除台账措施
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getdel_tz_cuoshi(HttpServletRequest request,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//数据表ID
		String v1 = request.getParameter("v1").trim();
		String v2 = request.getParameter("v2").trim();
		
		String hu_sql = "delete from da_help_tz_measures where da_household_id="+pkid+" and v1='"+v1+"' and v2='"+v2+"'";
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.deleteSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶措施','"+v1+"-"+v2+"')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 获取帮扶措施表格和图片数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getcuoshi_info(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//贫困户ID
		String type = request.getParameter("type").trim();//类型，区分措施，走访，成效
		JSONObject zong = new JSONObject ();
		
		String sql = "";
		//String pic_sql = "select count(*) as pic_num from da_pic where pic_type="+type+" and pic_pkid=";
		
		if(type.equals("1")){//帮扶措施
			sql = "select * from da_help_measures where da_household_id="+pkid+" order by v1 desc";
		}else if(type.equals("2")){//走访情况
			sql = "select * from da_help_visit where da_household_id="+pkid+" order by v1 desc";
		}else if(type.equals("3")){//帮扶成效
			sql = "select * from da_help_results where da_household_id="+pkid+" order by v1 desc";
		}
		//System.out.println(sql);
		
		if(!sql.equals("")){
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
			JSONArray jsa=new JSONArray();
			for(Map val:list){
				
				//SQLAdapter count_st_Adapter = new SQLAdapter(pic_sql+val.get("pkid"));
				//int total = this.getBySqlMapper.findrows(count_st_Adapter);
				
				JSONObject obj=new JSONObject ();
				obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
				obj.put("v1",val.get("v1")==null?"":val.get("v1"));
				obj.put("v2",val.get("v2")==null?"":val.get("v2"));
				obj.put("v3",val.get("v3")==null?"":val.get("v3"));
				//obj.put("pic_num",total);//图片数量
				if(type.equals("2")){
					obj.put("sys_personal_id",val.get("sys_personal_id")==null?"":val.get("sys_personal_id"));
				}
				jsa.add(obj);
			}
			zong.put("table", jsa);
		}
			
		response.getWriter().write(zong.toString());
		response.getWriter().close();
		
		return null;
	}

	/**
	 * 保存文字信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView getSave_cuoshi(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//数据表ID
		String v1 = request.getParameter("v1").trim();
		String v2 = request.getParameter("v2").trim();
		String v3 = request.getParameter("v3").trim();
		String type = request.getParameter("type").trim();
		
		String hu_sql = "";
		String str = "";
		if(type.equals("1")){
			
			String dest = "";
	        if (v2!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(v2);
	            dest = m.replaceAll("");
	        }
			
			hu_sql="INSERT INTO da_help_measures(da_household_id,v1,v2,v3) VALUES"+
					"('"+pkid+"','"+v1+"','"+dest+"','"+v3+"')";
			str = "帮扶措施";
		}else if(type.equals("2")){
			String dest = "";
	        if (v3!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(v3);
	            dest = m.replaceAll("");
	        }
			hu_sql="INSERT INTO da_help_visit(da_household_id,v1,v2,v3) VALUES"+
					"('"+pkid+"','"+v1+"','"+v2+"','"+dest+"')";
			str = "走访情况";
		}else if(type.equals("3")){
			String dest = "";
	        if (v2!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(v2);
	            dest = m.replaceAll("");
	        }
			hu_sql="INSERT INTO da_help_results(da_household_id,v1,v2,v3) VALUES"+
					"('"+pkid+"','"+v1+"','"+dest+"','"+v3+"')";
			str = "帮扶成效";
		}
		
		//System.out.println(hu_sql);
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','"+str+"','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 修改文字信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView getUpdate_cuoshi(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//数据表ID
		String v1 = request.getParameter("v1").trim();
		String v2 = request.getParameter("v2").trim();
		String v3 = request.getParameter("v3").trim();
		String type = request.getParameter("type").trim();
		
		String hu_sql = "";
		String str = "";
		String hou_sql = "";
		if(type.equals("1")){
			hu_sql = "update da_help_measures set v1='"+v1+"',v2='"+v2+"',v3='"+v3+"' where pkid="+pkid;
			str = "帮扶措施";
			hou_sql = "select da_household_id from da_help_measures where pkid="+pkid;
		}else if(type.equals("2")){
			hu_sql = "update da_help_visit set v1='"+v1+"',v2='"+v2+"',v3='"+v3+"' where pkid="+pkid;
			str = "走访情况";
			hou_sql = "select da_household_id from da_help_visit where pkid="+pkid;
		}else if(type.equals("3")){
			hu_sql = "update da_help_results set v1='"+v1+"',v2='"+v2+"',v3='"+v3+"' where pkid="+pkid;
			str = "帮扶成效";
			hou_sql = "select da_household_id from da_help_results where pkid="+pkid;
		}
		
		//System.out.println(hu_sql);
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			
			SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
			int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+da_household_id+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','"+str+"','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 删除文字信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView getdel_cuoshi(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//数据表ID
		String type = request.getParameter("type").trim();
		
		String hu_sql = "";
		String str = "";
		String hou_sql = "";
		if(type.equals("1")){
			hu_sql = "delete from da_help_measures where pkid="+pkid;
			str = "帮扶措施";
			hou_sql = "select da_household_id from da_help_measures where pkid="+pkid;
		}else if(type.equals("2")){
			hu_sql = "delete from da_help_visit where pkid="+pkid;
			str = "走访情况";
			hou_sql = "select da_household_id from da_help_visit where pkid="+pkid;
		}else if(type.equals("3")){
			hu_sql = "delete from da_help_results where pkid="+pkid;
			str = "帮扶成效";
			hou_sql = "select da_household_id from da_help_results where pkid="+pkid;
		}
		
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(hu_sql);
		try{
			
			SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
			int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+da_household_id+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','"+str+"','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			this.getBySqlMapper.deleteSelective(Metadata_table_Adapter);
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	
	/**
	 * 图片上传	帮扶措施:1		走访情况：2		帮扶成效：3
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getImg_load(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid").trim();
		String type = request.getParameter("type").trim();
		//System.out.println(pkid);
		
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
                    String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
        			
        			request.setAttribute(name, newFileName);
        			//真正写到磁盘上
        			item.write(new File(savePath, newFileName));
        			
        			long size = item.getSize();//文件大小
        			//System.out.println(savePath+newFileName);
        			//System.out.println(saveUrl+newFileName);
        			
        			response.getWriter().write(getSave_poth(type, pkid, saveUrl+newFileName, size, fileExt)); 
        		}
        	}
        }catch(FileUploadException e){
        	e.printStackTrace();
        }catch(Exception e){
        	
        }
		return null;
	}
	
	/**
	 * 保存图片信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public String getSave_poth(String pic_type, String pic_pkid, String pic_path, long pic_size, String pic_format){
		
		String sql="INSERT INTO da_pic(pic_type,pic_pkid,pic_path,pic_size,pic_format) VALUES"+
				"('"+pic_type+"','"+pic_pkid+"','"+pic_path+"','"+pic_size+"','"+pic_format+"')";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		try{
			this.getBySqlMapper.findRecords(sqlAdapter);
			return "1";
		}catch (Exception e){
			return "0";
		}
	}
	
	/**
	 * 获取图片信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getPic_info(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//数据表ID
		String type = request.getParameter("type").trim();//类型，1：措施。2：走访。3：成效。4：户主。5：成员。6：易地搬迁。
		JSONObject zong = new JSONObject ();
		
		String sql = "select * from da_pic where pic_type="+type+" and pic_pkid="+pkid;
		
	
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsa=new JSONArray();
		for(Map val:list){
			
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("pic_path",val.get("pic_path")==null?"":val.get("pic_path"));
			jsa.add(obj);
		}
		zong.put("pic", jsa);
			
		response.getWriter().write(zong.toString());
		response.getWriter().close();
		
		return null;
	}
	
	/**
	 * 删除图片信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView getdel_poth(HttpServletRequest request,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid").trim();//数据表ID
		
		String up_Sql = "delete from da_pic where pkid="+pkid;
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(up_Sql);
		
		try{
			this.getBySqlMapper.deleteSelective(Metadata_table_Adapter);
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}
	
	
	/**
	 * 上传照片  户主照片：4	家庭成员：5
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView ImportData(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			
			FileItemFactory factory = new DiskFileItemFactory();  
            ServletFileUpload upload = new ServletFileUpload(factory);  
            upload.setHeaderEncoding("UTF-8");  
            List items = upload.parseRequest(request);
            
            String type = "";
            String pkid = "";
            
            Iterator itrFormField = items.iterator();
            while (itrFormField.hasNext()) {
                FileItem item = (FileItem) itrFormField.next();
                if(item.isFormField()){//判断FileItem类对象封装的数据是否属于一个普通表单字段，还是属于一个文件表单字段，如果是普通表单字段则返回true，否则返回false。
                	if(item.getFieldName().equals("type")){//getFieldName方法用于返回表单字段元素的name属性值,例如“name=p1”中的“p1”
                		type = item.getString("UTF-8");
                	}else if(item.getFieldName().equals("pkid")){
                		pkid = item.getString("UTF-8");
                	}
                }
            }
			
            PrintWriter out = response.getWriter();  
            // 文件保存目录路径  
            String savePath = request.getServletContext().getRealPath("/")+ "attached/"+type+"/";
            // 文件保存目录URL  
            String saveUrl = request.getContextPath() + "/attached/"+type+"/";
            
            // 定义允许上传的文件扩展名  
            HashMap<String, String> extMap = new HashMap<String, String>();  
            extMap.put("image", "gif,jpg,jpeg,png,bmp");
            extMap.put("excel", "xls");
            String dirName = "image";
            // 最大文件大小  
            long maxSize = 1000000;  
            response.setContentType("text/html; charset=UTF-8");  
            
            if (!ServletFileUpload.isMultipartContent(request)) {  
            	response.getWriter().write(getMessage("1","请选择文件。",""));  
                return null;
            }  
            
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
            	response.getWriter().write(getMessage("1","上传目录没有写权限。",""));  
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
            
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (!item.isFormField()) {//判断FileItem类对象封装的数据是否属于一个普通表单字段，还是属于一个文件表单字段，如果是普通表单字段则返回true，否则返回false。
                	String fileName = item.getName();//getName方法用于获得文件上传字段中的文件名
                    // 检查文件大小  
                    if (item.getSize() > maxSize) {  
                    	response.getWriter().write(getMessage("1","上传文件大小超过限制。",""));  
                        return null;
                    }  
                    // 检查扩展名  
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();  
                    if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {  
                    	response.getWriter().write(getMessage("1","上传文件扩展名是不允许的扩展名。\n只允许"    + extMap.get(dirName) + "格式。",""));  
                        return null;
                    }  
  
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
                    String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;  
                    try {  
                        File uploadedFile = new File(savePath, newFileName);  
                        item.write(uploadedFile);  
                    } catch (Exception e) {  
                    	response.getWriter().write(getMessage("1","上传文件失败。",""));  
                        return null;
                    }
                    long size = item.getSize();//文件大小
                    if (!pkid.equals("")) {//如果表名不为空，可以将上传的文件导入数据库中
                    	response.getWriter().write(getSave_ImportData(saveUrl+newFileName, type, pkid, size, fileExt));
                    	return null;
                    }
                }
            }
        } catch (FileUploadException e1) {  
            e1.printStackTrace();  
        } 
		
		return null;
	}
	
	/**
	 * 保存上传的图片信息
	 * @param saveUrl
	 * @param newFileName
	 * @param table_name
	 * @param savePath
	 * @return
	 */
	private String getSave_ImportData(String saveUrl, String type, String pkid, long pic_size, String pic_format){
		
		String count_st_sql = "select count(*) from da_pic where pic_type="+type+" and pic_pkid="+pkid;
		//System.out.println(count_st_sql);
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		
		if(total>0){
			String up_Sql = "delete from da_pic where pic_type="+type+" and pic_pkid="+pkid;
			SQLAdapter Metadata_table_Adapter = new SQLAdapter(up_Sql);
			try{
				this.getBySqlMapper.deleteSelective(Metadata_table_Adapter);
			}catch (Exception e){
				return getMessage("1","照片替换失败","");
			}
		}
		
		String sql="INSERT INTO da_pic(pic_type,pic_pkid,pic_path,pic_size,pic_format) VALUES"+
				"('"+type+"','"+pkid+"','"+saveUrl+"','"+pic_size+"','"+pic_format+"')";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		try{
			this.getBySqlMapper.findRecords(sqlAdapter);
	        return getMessage("0","照片保存成功",saveUrl);
		}catch (Exception e){
			return getMessage("1","服务器异常","");
		}
	}
	
	/**
	 * 错误信息处理
	 * @param message 错误信息内容
	 * @return 返回错误信息JSONObject字符串
	 */
	private String getMessage(String error, String message, String path){  
        JSONObject obj = new JSONObject();  
        obj.put("error", error);
        obj.put("message", message);
        if(error.equals("0")){
        	obj.put("path", path);
        }
        return obj.toString();  
    }
	
	
	/**
	 * 易地扶贫搬迁保存
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveYidifupinbanqian(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String pkid = request.getParameter("pkid");//户主ID
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String move_type = "";//集中安置或者分散安置
		String focus_info = "";//集中安置分类
		String dispersed_info = "";//分散安置分类
		String dispersed_address = "";//房源地
		String dispersed_price = "null";//房价（万元）
		String dispersed_agreement = "";//与用工企业签订就业安置协议
		String v1 = "";//搬迁方式
		String v2 = "";//安置地
		String v3 = "";//搬迁可能存在的困难
		
		if(form_json.get("move_type")!=null&&!form_json.get("move_type").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			move_type = form_json.get("move_type").toString();
		}
		
		if(form_json.get("focus_info")!=null&&!form_json.get("focus_info").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(!form_json.get("focus_info").equals("请选择")){
				focus_info = form_json.get("focus_info").toString();
			}
		}
		
		if(form_json.get("dispersed_info")!=null&&!form_json.get("dispersed_info").equals("")){//下拉框，一定有值，但是要筛除“请选择”
			if(!form_json.get("dispersed_info").equals("请选择")){
				dispersed_info = form_json.get("dispersed_info").toString();
			}
		}
		
		if(form_json.get("dispersed_address")!=null&&!form_json.get("dispersed_address").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			dispersed_address = form_json.get("dispersed_address").toString();
		}
		
		if(form_json.get("dispersed_price")!=null&&!form_json.get("dispersed_price").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			dispersed_price = form_json.get("dispersed_price").toString();
		}
		
		if(form_json.get("dispersed_agreement")!=null&&!form_json.get("dispersed_agreement").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			dispersed_agreement = form_json.get("dispersed_agreement").toString();
		}
		
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			v1 = form_json.get("v1").toString();
		}
		
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			v2 = form_json.get("v2").toString();
		}
		
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("")){//复选框，不一定有值
			if(form_json.get("v3").toString().indexOf(",")>-1){//多选
				JSONArray jsonArray = JSONArray.fromObject(form_json.get("v3"));
				String str = "";
				for(int i = 0;i<jsonArray.size();i++){
					str += jsonArray.getString(i)+",";
				}
				v3 = str.substring(0, str.length()-1);
			}else{//单选
				v3 = form_json.get("v3").toString();
			}
		}
		
		String count_st_sql = "select count(*) from da_household_move where da_household_id="+pkid;
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		if(move_type.equals("集中安置")){
			dispersed_info = "";
			dispersed_address = "";
			dispersed_price = "0";
			dispersed_agreement = "";
		}else if(move_type.equals("分散安置")){
			focus_info = "";
			if(!dispersed_info.equals("进城购房")){
				dispersed_address = "";
				dispersed_price = "0";
				dispersed_agreement = "";
			}
		}
		String sql = "";
		if(total==0){
			sql = "insert into da_household_move(da_household_id,move_type,focus_info,dispersed_info,dispersed_address,dispersed_price,dispersed_agreement,v1,v2,v3) "
					+ "values("+pkid+",'"+move_type+"','"+focus_info+"','"+dispersed_info+"','"+dispersed_address+"','"+dispersed_price+"','"+dispersed_agreement+"','"+v1+"','"+v2+"','"+v3+"')";
		}else if(total>0){
			sql = "update da_household_move set move_type='"+move_type+"',focus_info='"+focus_info+"',dispersed_info='"+dispersed_info+"',dispersed_address='"+dispersed_address+"',"
					+ "dispersed_price='"+dispersed_price+"',dispersed_agreement='"+dispersed_agreement+"',v1='"+v1+"',v2='"+v2+"',v3='"+v3+"' where da_household_id="+pkid;
		}
		//System.out.println(sql);
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_household',"+pkid+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','基本情况','易地扶贫搬迁')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		
		return null;
	}
	
	/**
	 * 查询未完整和完整的贫困户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getincompletedata(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String form_name=request.getParameter("form_name");
		String form_val = request.getParameter("form_val");
		String danxuan_val=request.getParameter("danxuan_val");
		String json_level= request.getParameter("jsonlevel");
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String level=request.getParameter("level");
		String jsonname=request.getParameter("jsonname");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
//		System.out.println(form_json);
		JSONObject danxuan_json = JSONObject.fromObject(danxuan_val);//表单数据
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;
		int shilevel;
		String shi_name="";
		String term="";
		String life_term="";
		String product_term="";
		
		String left_sql = "";
		String shilevel_sql="";
		if (json_level.equals("undefined")) {
			String shi_sql ="select * from sys_company c where c.com_level='"+ level +"'";
			SQLAdapter shi_Adapter = new SQLAdapter(shi_sql);
			List<Map> shi_listmap= this.getBySqlMapper.findRecords(shi_Adapter);
			shilevel= (Integer) shi_listmap.get(0).get("com_level");
			shi_name= jsonname;
		}else{
			String shi_sql ="select * from sys_company c where c.pkid='"+ json_level +"'";
			SQLAdapter shi_Adapter = new SQLAdapter(shi_sql);
			List<Map> shi_listmap= this.getBySqlMapper.findRecords(shi_Adapter);
			shilevel= (Integer) shi_listmap.get(0).get("com_level");
			shi_name= (String) shi_listmap.get(0).get("com_name");
		}
		
		if (shilevel==1) {										//判断区级
			shilevel_sql+=" a.v2 like '%"+ shi_name +"%' ";
		}else if (shilevel==2) {
			shilevel_sql+=" a.v3 like '%"+ shi_name +"%' ";
		}else if (shilevel==3) {
			shilevel_sql+=" a.v4 like '%"+ shi_name +"%' ";
		}else if (shilevel==4) {
			shilevel_sql+=" a.v5 like '%"+ shi_name +"%' ";
		}
		
		if (form_name.equals("jinben_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				if (form_json.get("v6")!=null&&!form_json.get("v6").equals("")) {
					term+="a.v6 is NULL  or a.v6 = ''  or";
					
				}
				if (form_json.get("v7")!=null&&!form_json.get("v7").equals("")) {
					term+=" a.v7 is NULL  or a.v7 = ''  or";
					
				}
				if (form_json.get("v11")!=null&&!form_json.get("v11").equals("")) {
					term+=" a.v11 is NULL  or a.v11 = ''  or";
				}
				if (form_json.get("v25")!=null&&!form_json.get("v25").equals("")) {
					term+=" a.v25 is NULL  or a.v25 = ''  or";
				}
				if (form_json.get("v8")!=null&&!form_json.get("v8").equals("")) {
					term+=" a.v8 is NULL  or a.v8 = ''  or";
				}
				if (form_json.get("v28")!=null&&!form_json.get("v28").equals("")) {
					term+=" a.v28 is NULL  or a.v28 = ''  or";
				} 
				if (form_json.get("v26")!=null&&!form_json.get("v26").equals("")) {
					term+=" a.v26 is NULL  or a.v26 = ''  or";
				}
				if (form_json.get("v27")!=null&&!form_json.get("v27").equals("")) {
					term+=" a.v27 is NULL  or a.v27 = ''  or";
				}
				if (form_json.get("hz_jtzz")!=null&&!form_json.get("hz_jtzz").equals("")) {
					left_sql=" join da_household_basic r on a.pkid = r.da_household_id";
					term+=" r.basic_address is NULL  or r.basic_address = ''  or";
				}
				if (form_json.get("sys_standard")!=null&&!form_json.get("sys_standard").equals("")) {
					term+=" a.sys_standard is NULL  or a.sys_standard = ''  or";
				}
				if (form_json.get("v22")!=null&&!form_json.get("v22").equals("")) {
					term+=" a.v22 is NULL  or a.v22 = ''  or";
				}
				if (form_json.get("v12")!=null&&!form_json.get("v12").equals("")) {
					term+=" a.v12 is NULL  or a.v12 = ''  or";
				}
				if (form_json.get("v13")!=null&&!form_json.get("v13").equals("")) {
					term+=" a.v13 is NULL  or a.v13 = ''  or";
				}
				if (form_json.get("v14")!=null&&!form_json.get("v14").equals("")) {
					term+=" a.v14 is NULL  or a.v14 = ''  or";
				}
				if (form_json.get("v15")!=null&&!form_json.get("v15").equals("")) {
					term+=" a.v15 is NULL  or a.v15 = ''  or";
				} 
				if (form_json.get("v16")!=null&&!form_json.get("v16").equals("")) {
					term+=" a.v16 is NULL  or a.v16 = ''  or";
				}
				if (form_json.get("v17")!=null&&!form_json.get("v17").equals("")) {
					term+=" a.v17 is NULL  or a.v17 = ''  or";
				}
				if (form_json.get("v18")!=null&&!form_json.get("v18").equals("")) {
					term+=" a.v18 is NULL  or a.v18 = ''  or";
				}
				if (form_json.get("v19")!=null&&!form_json.get("v19").equals("")) {
					term+=" a.v19 is NULL  or a.v19 = ''  or";
				}
				if (form_json.get("v29_div")!=null&&!form_json.get("v29_div").equals("")) {
					term+=" a.v29 is NULL  or a.v29 = ''  or";
				}
				if (form_json.get("v32")!=null&&!form_json.get("v32").equals("")) {
					term+=" a.v32 is NULL  or a.v32 = ''  or";
				}
				if (form_json.get("v30")!=null&&!form_json.get("v30").equals("")) {
					term+=" a.v30 is NULL  or a.v30 = ''  or";
				}
				if (form_json.get("hz_zpyy")!=null&&!form_json.get("hz_zpyy").equals("")) {
					left_sql=" join da_household_basic r on a.pkid = r.da_household_id";
					term+=" r.basic_explain is NULL  or r.basic_explain = ''  or";
				}
				
			}else {									  //已完成	
				if (form_json.get("v6")!=null&&!form_json.get("v6").equals("")) {
					term+=" a.v6 != '' and";
				}
				if (form_json.get("v7")!=null&&!form_json.get("v7").equals("")) {
					term+=" a.v7 != '' and";
				}
				if (form_json.get("v11")!=null&&!form_json.get("v11").equals("")) {
					term+=" a.v11 != '' and";
				}
				if (form_json.get("v25")!=null&&!form_json.get("v25").equals("")) {
					term+=" a.v25 != '' and";
				}
				if (form_json.get("v8")!=null&&!form_json.get("v8").equals("")) {
					term+=" a.v8 != '' and";
				}
				if (form_json.get("v28")!=null&&!form_json.get("v28").equals("")) {
					term+=" a.v28 != '' and";
				}
				if (form_json.get("v26")!=null&&!form_json.get("v26").equals("")) {
					term+=" a.v26 != '' and";
				}
				if (form_json.get("v27")!=null&&!form_json.get("v27").equals("")) {
					term+=" a.v27 != '' and";
				}
				if (form_json.get("hz_jtzz")!=null&&!form_json.get("hz_jtzz").equals("")) {
					left_sql=" join da_household_basic r on a.pkid = r.da_household_id";
					term+=" r.basic_address != '' and";
				}
				if (form_json.get("sys_standard")!=null&&!form_json.get("sys_standard").equals("")) {
					term+=" a.sys_standard != '' and";
				}
				if (form_json.get("v22")!=null&&!form_json.get("v22").equals("")) {
					term+=" a.v22 != '' and";
				}
				if (form_json.get("v12")!=null&&!form_json.get("v12").equals("")) {
					term+=" a.v12 != '' and";
				}
				if (form_json.get("v13")!=null&&!form_json.get("v13").equals("")) {
					term+=" a.v13 != '' and";
				}
				if (form_json.get("v14")!=null&&!form_json.get("v14").equals("")) {
					term+=" a.v14 != '' and";
				}
				if (form_json.get("v15")!=null&&!form_json.get("v15").equals("")) {
					term+=" a.v15 != '' and";
				}
				if (form_json.get("v16")!=null&&!form_json.get("v16").equals("")) {
					term+=" a.v16 != '' and";
				}
				if (form_json.get("v17")!=null&&!form_json.get("v17").equals("")) {
					term+=" a.v17 != '' and";
				}
				if (form_json.get("v18")!=null&&!form_json.get("v18").equals("")) {
					term+=" a.v18 != '' and";
				}
				if (form_json.get("v19")!=null&&!form_json.get("v19").equals("")) {
					term+=" a.v19 != '' and";
				}
				if (form_json.get("v29_div")!=null&&!form_json.get("v29_div").equals("")) {
					term+=" a.v29 != '' and";
				}
				if (form_json.get("v32")!=null&&!form_json.get("v32").equals("")) {
					term+=" a.v32 != '' and";
				}
				if (form_json.get("v30")!=null&&!form_json.get("v30").equals("")) {
					term+=" a.v30 != '' and";
				}
				if (form_json.get("v23_div")!=null&&!form_json.get("v23_div").equals("")) {
					term+=" a.v23 != '' and";
				}
				if (form_json.get("v33_div")!=null&&!form_json.get("v33_div").equals("")) {
					term+=" a.v33 != '' and";
				}
				if (form_json.get("hz_zpyy")!=null&&!form_json.get("hz_zpyy").equals("")) {
					left_sql=" join da_household_basic r on a.pkid = r.da_household_id";
					term+=" r.basic_explain != '' and";
				}
			}
		}else if (form_name.equals("shengchan_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_production c on a.pkid = c.da_household_id ";
				if (form_json.get("v1-1")!=null&&!form_json.get("v1-1").equals("")) {
					term+=" c.v1 is NULL  or ";
				}
				if (form_json.get("v2-1")!=null&&!form_json.get("v2-1").equals("")) {
					term+=" c.v2 is NULL  or ";
				}
				if (form_json.get("v3-1")!=null&&!form_json.get("v3-1").equals("")) {
					term+=" c.v3 is NULL  or ";
				}
				if (form_json.get("v5-1")!=null&&!form_json.get("v5-1").equals("")) {
					term+=" c.v5 is NULL  or ";
				}
				if (form_json.get("v6-1")!=null&&!form_json.get("v6-1").equals("")) {
					term+=" c.v6 is NULL  or ";
				}
				if (form_json.get("v13-1")!=null&&!form_json.get("v13-1").equals("")) {
					term+=" c.v13 is NULL  or ";
				}
				if (form_json.get("v14-1")!=null&&!form_json.get("v14-1").equals("")) {
					term+=" c.v14 is NULL  or ";
				}
				if (form_json.get("v7-1")!=null&&!form_json.get("v7-1").equals("")) {
					term+=" c.v7 is NULL  or c.v7 = ''  or";
				}
				if (form_json.get("v8-1")!=null&&!form_json.get("v8-1").equals("")) {
					term+=" c.v8 is NULL  or c.v8 = ''  or";
				}
			}else {									  //已完成	
				left_sql=" join da_production c on a.pkid = c.da_household_id ";
				if (form_json.get("v1-1")!=null&&!form_json.get("v1-1").equals("")) {
					term+=" c.v1 is not NULL and";
				}
				if (form_json.get("v2-1")!=null&&!form_json.get("v2-1").equals("")) {
					term+=" c.v2 is not NULL and";
				}
				if (form_json.get("v3-1")!=null&&!form_json.get("v3-1").equals("")) {
					term+=" c.v3 is not NULL and";
				}
				if (form_json.get("v5-1")!=null&&!form_json.get("v5-1").equals("")) {
					term+=" c.v5 is not NULL and";
				}
				if (form_json.get("v6-1")!=null&&!form_json.get("v6-1").equals("")) {
					term+=" c.v6 is not NULL and";
				}
				if (form_json.get("v13-1")!=null&&!form_json.get("v13-1").equals("")) {
					term+=" c.v13 is not NULL and";
				}
				if (form_json.get("v14-1")!=null&&!form_json.get("v14-1").equals("")) {
					term+=" c.v14 is not NULL and";
				}
				if (form_json.get("v7-1")!=null&&!form_json.get("v7-1").equals("")) {
					term+=" c.v7 != '' and";
				}
				if (form_json.get("v8-1")!=null&&!form_json.get("v8-1").equals("")) {
					term+=" c.v8 != '' and";
				}
			}
		}else if (form_name.equals("shenghuo_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_life b on a.pkid=b.da_household_id";
				if (form_json.get("v1-2")!=null&&!form_json.get("v1-2").equals("")) {
					term+=" b.v1 is NULL  or ";
				}
				if (form_json.get("v2-2")!=null&&!form_json.get("v2-2").equals("")) {
					term+=" b.v2 is NULL  or b.v2 = ''  or";
				}
				if (form_json.get("v3-2")!=null&&!form_json.get("v3-2").equals("")) {
					term+=" b.v3 is NULL  or b.v3 = ''  or";
				}
				if (form_json.get("v4-2")!=null&&!form_json.get("v4-2").equals("")) {
					term+=" b.v4 is NULL  or b.v4 = ''  or";
				}
				if (form_json.get("v8-2")!=null&&!form_json.get("v8-2").equals("")) {
					term+=" b.v8 is NULL  or b.v8 = ''  or";
				}
				if (form_json.get("v9-2")!=null&&!form_json.get("v9-2").equals("")) {
					term+=" b.v9 is NULL  or b.v9 = ''  or";
				}
				if (form_json.get("v5-2")!=null&&!form_json.get("v5-2").equals("")) {
					term+=" b.v5 is NULL  or b.v5 = ''  or";
				}
				if (form_json.get("v6-2")!=null&&!form_json.get("v6-2").equals("")) {
					term+=" b.v6 is NULL  or b.v6 = ''  or ";
				}
				if (form_json.get("v7-2")!=null&&!form_json.get("v7-2").equals("")) {
					term+=" b.v7 is NULL  or ";
				}
				if (form_json.get("v10-2")!=null&&!form_json.get("v10-2").equals("")) {
					term+=" b.v10 is NULL  or b.v10 = ''  or";
				}
				if (form_json.get("v11-2")!=null&&!form_json.get("v11-2").equals("")) {
					term+=" b.v11 is NULL  or b.v11 = ''  or";
				}
				if (form_json.get("v12-2")!=null&&!form_json.get("v12-2").equals("")) {
					term+=" b.v12 is NULL  or b.v12 = ''  or";
				}
			}else {									  //已完成	
				left_sql=" join da_life b on a.pkid=b.da_household_id";
				if (form_json.get("v1-2")!=null&&!form_json.get("v1-2").equals("")) {
					term+=" b.v1 is not NULL and";
				}
				if (form_json.get("v2-2")!=null&&!form_json.get("v2-2").equals("")) {
					term+=" b.v2 != '' and";
				}
				if (form_json.get("v3-2")!=null&&!form_json.get("v3-2").equals("")) {
					term+=" b.v3 != '' and";
				}
				if (form_json.get("v4-2")!=null&&!form_json.get("v4-2").equals("")) {
					term+=" b.v4 != '' and";
				}
				if (form_json.get("v8-2")!=null&&!form_json.get("v8-2").equals("")) {
					term+=" b.v8 != '' and";
				}
				if (form_json.get("v9-2")!=null&&!form_json.get("v9-2").equals("")) {
					term+=" b.v9 != '' and";
				}
				if (form_json.get("v5-2")!=null&&!form_json.get("v5-2").equals("")) {
					term+=" b.v5 != '' and";
				}
				if (form_json.get("v6-2")!=null&&!form_json.get("v6-2").equals("")) {
					term+=" b.v6 != '' and";
				}
				if (form_json.get("v7-2")!=null&&!form_json.get("v7-2").equals("")) {
					term+=" b.v7 is not NULL and";
				}
				if (form_json.get("v10-2")!=null&&!form_json.get("v10-2").equals("")) {
					term+=" b.v10 != '' and";
				}
				if (form_json.get("v11-2")!=null&&!form_json.get("v11-2").equals("")) {
					term+=" b.v11 != '' and";
				}
				if (form_json.get("v12-2")!=null&&!form_json.get("v12-2").equals("")) {
					term+=" b.v12 != '' and";
				}
			}
		}else if (form_name.equals("jiating_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_member d on a.pkid=d.da_household_id";
				if (form_json.get("v6-3")!=null&&!form_json.get("v6-3").equals("")) {//
					term+=" d.v6 is NULL  or d.v6 = ''  or";
				}
				if (form_json.get("v7-3")!=null&&!form_json.get("v7-3").equals("")) {
					term+=" d.v7 is NULL  or d.v7 = ''  or";
				}
				if (form_json.get("v8-3")!=null&&!form_json.get("v8-3").equals("")) {
					term+=" d.v8 is NULL  or d.v8 = ''  or";
				}
				if (form_json.get("v10-3")!=null&&!form_json.get("v10-3").equals("")) {
					term+=" d.v10 is NULL  or d.v10 = ''  or";
				}
				if (form_json.get("v11-3")!=null&&!form_json.get("v11-3").equals("")) {
					term+=" d.v11 is NULL  or d.v11 = ''  or";
				}
				if (form_json.get("v28-3")!=null&&!form_json.get("v28-3").equals("")) {
					term+=" d.v28 is NULL  or d.v28 = ''  or";
				}
				if (form_json.get("v32-3")!=null&&!form_json.get("v32-3").equals("")) {
					term+=" d.v32 is NULL  or d.v32 = ''  or";
				}
				if (form_json.get("v12-3")!=null&&!form_json.get("v12-3").equals("")) {
					term+=" d.v12 is NULL  or d.v12 = ''  or";
				}
				if (form_json.get("v13-3")!=null&&!form_json.get("v13-3").equals("")) {
					term+=" d.v13 is NULL  or d.v13 = ''  or";
				}
				if (form_json.get("v14-3")!=null&&!form_json.get("v14-3").equals("")) {
					term+=" d.v14 is NULL  or d.v14 = ''  or";
				}
				if (form_json.get("v15-3")!=null&&!form_json.get("v15-3").equals("")) {
					term+=" d.v15 is NULL  or d.v15 = ''  or";
				}
				if (form_json.get("v16-3")!=null&&!form_json.get("v16-3").equals("")) {
					term+=" d.v16 is NULL  or d.v16 = ''  or";
				}
				if (form_json.get("v17-3")!=null&&!form_json.get("v17-3").equals("")) {
					term+=" d.v17 is NULL  or d.v17 = ''  or";
				}
				if (form_json.get("v18-3")!=null&&!form_json.get("v18-3").equals("")) {
					term+=" d.v18 is NULL  or d.v18 = ''  or";
				}
				if (form_json.get("v19-3")!=null&&!form_json.get("v19-3").equals("")) {
					term+=" d.v19 is NULL  or d.v19 = ''  or";
				}
			}else {									  //已完成	
				left_sql=" join da_member d on a.pkid=d.da_household_id";
				if (form_json.get("v6-3")!=null&&!form_json.get("v6-3").equals("")) {//
					term+=" d.v6 != '' and";
				}
				if (form_json.get("v7-3")!=null&&!form_json.get("v7-3").equals("")) {
					term+=" d.v7 != '' and";
				}
				if (form_json.get("v11-3")!=null&&!form_json.get("v11-3").equals("")) {
					term+=" d.v11 != '' and";
				}
				if (form_json.get("v8-3")!=null&&!form_json.get("v8-3").equals("")) {
					term+=" d.v8 != '' and";
				}
				if (form_json.get("v28-3")!=null&&!form_json.get("v28-3").equals("")) {
					term+=" d.v28 != '' and";
				}
				if (form_json.get("v32-3")!=null&&!form_json.get("v32-3").equals("")) {
					term+=" d.v32 != '' and";
				}
				if (form_json.get("v10-3")!=null&&!form_json.get("v10-3").equals("")) {
					term+=" d.v10 != '' and";
				}
				if (form_json.get("v12-3")!=null&&!form_json.get("v12-3").equals("")) {
					term+=" d.v12 != '' and";
				}
				if (form_json.get("v13-3")!=null&&!form_json.get("v13-3").equals("")) {
					term+=" d.v13 != '' and";
				}
				if (form_json.get("v14-3")!=null&&!form_json.get("v14-3").equals("")) {
					term+=" d.v14 != '' and";
				}
				if (form_json.get("v15-3")!=null&&!form_json.get("v15-3").equals("")) {
					term+=" d.v15 != '' and";
				}
				if (form_json.get("v16-3")!=null&&!form_json.get("v16-3").equals("")) {
					term+=" d.v16 != '' and";
				}
				if (form_json.get("v17-3")!=null&&!form_json.get("v17-3").equals("")) {
					term+=" d.v12 != '' and";
				}
				if (form_json.get("v18-3")!=null&&!form_json.get("v18-3").equals("")) {
					term+=" d.v12 != '' and";
				}
			}
		}else if (form_name.equals("shouru_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_current_income e on a.pkid = e.da_household_id";
				if (form_json.get("v2")!=null&&!form_json.get("v2").equals("")) {
					term+=" e.v2 is NULL or ";
				}
				if (form_json.get("v4")!=null&&!form_json.get("v4").equals("")) {
					term+=" e.v4 is NULL or ";
				}
				if (form_json.get("v6")!=null&&!form_json.get("v6").equals("")) {
					term+=" e.v6 is NULL or ";
				}
				if (form_json.get("v8")!=null&&!form_json.get("v8").equals("")) {
					term+=" e.v8 is NULL or ";
				}
				if (form_json.get("v12")!=null&&!form_json.get("v12").equals("")) {
					term+=" e.v12 is NULL or ";
				}
				if (form_json.get("v14")!=null&&!form_json.get("v14").equals("")) {
					term+=" e.v14 is NULL or ";
				}
				if (form_json.get("v16")!=null&&!form_json.get("v16").equals("")) {
					term+=" e.v16 is NULL or ";
				}
				if (form_json.get("v18")!=null&&!form_json.get("v18").equals("")) {
					term+=" e.v18 is NULL or ";
				}
				if (form_json.get("v43")!=null&&!form_json.get("v43").equals("")) {
					term+=" e.v43 is NULL or ";
				}
				if (form_json.get("v41")!=null&&!form_json.get("v41").equals("")) {
					term+=" e.v41 is NULL or ";
				}
				if (form_json.get("v26")!=null&&!form_json.get("v26").equals("")) {
					term+=" e.v26 is NULL or ";
				}
				if (form_json.get("v24")!=null&&!form_json.get("v24").equals("")) {
					term+=" e.v24 is NULL or ";
				}
				if (form_json.get("v28")!=null&&!form_json.get("v28").equals("")) {
					term+=" e.v28 is NULL or ";
				}
				if (form_json.get("v32")!=null&&!form_json.get("v32").equals("")) {
					term+=" e.v32 is NULL or ";
				}
			}else {									  //已完成	
				left_sql=" join da_current_income e on a.pkid = e.da_household_id";
				if (form_json.get("v2")!=null&&!form_json.get("v2").equals("")) {
					term+=" e.v2 is not NULL and";
				}
				if (form_json.get("v4")!=null&&!form_json.get("v4").equals("")) {
					term+=" e.v4 is not NULL and";
				}
				if (form_json.get("v6")!=null&&!form_json.get("v6").equals("")) {
					term+=" e.v6 is not NULL and";
				}
				if (form_json.get("v8")!=null&&!form_json.get("v8").equals("")) {
					term+=" e.v8 is not NULL and";
				}
				if (form_json.get("v12")!=null&&!form_json.get("v12").equals("")) {
					term+=" e.v12 is not NULL and";
				}
				if (form_json.get("v14")!=null&&!form_json.get("v14").equals("")) {
					term+=" e.v14 is not NULL and";
				}
				if (form_json.get("v16")!=null&&!form_json.get("v16").equals("")) {
					term+=" e.v16 is not NULL and";
				}
				if (form_json.get("v18")!=null&&!form_json.get("v18").equals("")) {
					term+=" e.v18 is not NULL and";
				}
				if (form_json.get("v43")!=null&&!form_json.get("v43").equals("")) {
					term+=" e.v43 is not NULL and";
				}
				if (form_json.get("v41")!=null&&!form_json.get("v41").equals("")) {
					term+=" e.v41 is not NULL and";
				}
				if (form_json.get("v26")!=null&&!form_json.get("v26").equals("")) {
					term+=" e.v26 is not NULL and";
				}
				if (form_json.get("v24")!=null&&!form_json.get("v24").equals("")) {
					term+=" e.v24 is not NULL and";
				}
				if (form_json.get("v28")!=null&&!form_json.get("v28").equals("")) {
					term+=" e.v28 is not NULL and";
				}
				if (form_json.get("v32")!=null&&!form_json.get("v32").equals("")) {
					term+=" e.v32 is not NULL and";
				}
			}
		}else if (form_name.equals("zhichu_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_current_expenditure f on a.pkid = f.da_household_id";
				if (form_json.get("zc-v2")!=null&&!form_json.get("zc-v2").equals("")) {
					term+=" f.v2 is NULL or ";
				}
				if (form_json.get("zc-v4")!=null&&!form_json.get("zc-v4").equals("")) {
					term+=" f.v4 is NULL or ";
				}
				if (form_json.get("zc-v6")!=null&&!form_json.get("zc-v6").equals("")) {
					term+=" f.v6 is NULL or ";
				}
				if (form_json.get("zc-v8")!=null&&!form_json.get("zc-v8").equals("")) {
					term+=" f.v8 is NULL or ";
				}
				if (form_json.get("zc-v10")!=null&&!form_json.get("zc-v10").equals("")) {
					term+=" f.v10 is NULL or ";
				}
				if (form_json.get("zc-v12")!=null&&!form_json.get("zc-v12").equals("")) {
					term+=" f.v12 is NULL or ";
				}
				if (form_json.get("zc-v14")!=null&&!form_json.get("zc-v14").equals("")) {
					term+=" f.v14 is NULL or ";
				}
				if (form_json.get("zc-v16")!=null&&!form_json.get("zc-v16").equals("")) {
					term+=" f.v16 is NULL or ";
				}
				if (form_json.get("zc-v18")!=null&&!form_json.get("zc-v18").equals("")) {
					term+=" f.v18 is NULL or ";
				}
				if (form_json.get("zc-v20")!=null&&!form_json.get("zc-v20").equals("")) {
					term+=" f.v20 is NULL or ";
				}
				if (form_json.get("zc-v27")!=null&&!form_json.get("zc-v27").equals("")) {
					term+=" f.v27 is NULL or ";
				}
			}else {									  //已完成	
				left_sql=" join da_current_expenditure f on a.pkid = f.da_household_id";
				if (form_json.get("zc-v2")!=null&&!form_json.get("zc-v2").equals("")) {
					term+=" f.v2 is not NULL and";
				}
				if (form_json.get("zc-v4")!=null&&!form_json.get("zc-v4").equals("")) {
					term+=" f.v4 is not NULL and";
				}
				if (form_json.get("zc-v6")!=null&&!form_json.get("zc-v6").equals("")) {
					term+=" f.v6 is not NULL and";
				}
				if (form_json.get("zc-v8")!=null&&!form_json.get("zc-v8").equals("")) {
					term+=" f.v8 is not NULL and";
				}
				if (form_json.get("zc-v10")!=null&&!form_json.get("zc-v10").equals("")) {
					term+=" f.v10 is not NULL and";
				}
				if (form_json.get("zc-v12")!=null&&!form_json.get("zc-v12").equals("")) {
					term+=" f.v12 is not NULL and";
				}
				if (form_json.get("zc-v14")!=null&&!form_json.get("zc-v14").equals("")) {
					term+=" f.v14 is not NULL and";
				}
				if (form_json.get("zc-v16")!=null&&!form_json.get("zc-v16").equals("")) {
					term+=" f.v16 is not NULL and";
				}
				if (form_json.get("zc-v18")!=null&&!form_json.get("zc-v18").equals("")) {
					term+=" f.v18 is not NULL and";
				}
				if (form_json.get("zc-v20")!=null&&!form_json.get("zc-v20").equals("")) {
					term+=" f.v20 is not NULL and";
				}
				if (form_json.get("zc-v27")!=null&&!form_json.get("zc-v27").equals("")) {
					term+=" f.v27 is not NULL and";
				}
			}
		}else if (form_name.equals("bfr_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join sys_personal_household_many g on g.da_household_id=a.pkid join sys_personal h on h.pkid=g.sys_personal_id";
				if (form_json.get("col_name")!=null&&!form_json.get("col_name").equals("")) {
					term+=" h.col_name is NULL or h.col_name = ''  or";
				}
				if (form_json.get("col_post")!=null&&!form_json.get("col_post").equals("")) {
					term+=" h.col_post is NULL or h.col_post = ''  or";
				}
				if (form_json.get("telephone")!=null&&!form_json.get("telephone").equals("")) {
					term+=" h.telephone is NULL or h.telephone = ''  or";
				}
				if (form_json.get("com_name")!=null&&!form_json.get("com_name").equals("")) {
					left_sql+="  join da_company j on j.pkid = h.da_company_id";
					term+=" j.v1 is NULL or j.v1 = ''  or";
				}
			}else {									  //已完成	
				left_sql=" join sys_personal_household_many g on g.da_household_id=a.pkid join sys_personal h on h.pkid=g.sys_personal_id";
				if (form_json.get("col_name")!=null&&!form_json.get("col_name").equals("")) {
					term+=" h.col_name != '' and";
				}
				if (form_json.get("col_post")!=null&&!form_json.get("col_post").equals("")) {
					term+=" h.col_post != '' and";
				}
				if (form_json.get("telephone")!=null&&!form_json.get("telephone").equals("")) {
					term+=" h.telephone != '' and";
				}
				if (form_json.get("com_name")!=null&&!form_json.get("com_name").equals("")) {
					left_sql+="  join da_company j on j.pkid = h.da_company_id";
					term+=" j.v1 != '' and";
				}
			}
		}else if (form_name.equals("bfmb_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_help_info i on a.pkid = i.da_household_id";
				if (form_json.get("bf_v1")!=null&&!form_json.get("bf_v1").equals("")) {
					term+=" i.v1 is NULL or i.v1 = ''  or";
				}
				if (form_json.get("bf_v2")!=null&&!form_json.get("bf_v2").equals("")) {
					term+=" i.v2 is NULL or i.v2 = ''  or";
				}
				if (form_json.get("bf_v3")!=null&&!form_json.get("bf_v3").equals("")) {
					term+=" i.v3 is NULL or i.v3 = ''  or";
				}
			}else {									  //已完成	
				left_sql=" join da_help_info i on a.pkid = i.da_household_id";
				if (form_json.get("bf_v1")!=null&&!form_json.get("bf_v1").equals("")) {
					term+=" i.v1 != '' and";
				}
				if (form_json.get("bf_v2")!=null&&!form_json.get("bf_v2").equals("")) {
					term+=" i.v2 != '' and";
				}
				if (form_json.get("bf_v3")!=null&&!form_json.get("bf_v3").equals("")) {
					term+=" i.v3 != '' and";
				}
			}
		}else if (form_name.equals("zoufang_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_help_visit k on k.da_household_id=a.pkid";
				if (form_json.get("zf-v1")!=null&&!form_json.get("zf-v1").equals("")) {
					term+=" k.v1 is NULL or k.v1 = ''  or";
				}
				if (form_json.get("zf-v2")!=null&&!form_json.get("zf-v2").equals("")) {
					term+=" k.v2 is NULL or k.v2 = ''  or";
				}
				if (form_json.get("zf-v3")!=null&&!form_json.get("zf-v3").equals("")) {
					term+=" k.v3 is NULL or k.v3 = ''  or";
				}
			}else {									  //已完成	
				left_sql=" join da_help_visit k on k.da_household_id=a.pkid";
				if (form_json.get("zf-v1")!=null&&!form_json.get("zf-v1").equals("")) {
					term+=" k.v1 != '' and";
				}
				if (form_json.get("zf-v2")!=null&&!form_json.get("zf-v2").equals("")) {
					term+=" k.v2 != '' and";
				}
				if (form_json.get("zf-v3")!=null&&!form_json.get("zf-v3").equals("")) {
					term+=" k.v3 != '' and";
				}
			}
		}else if (form_name.equals("chengxiao_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_help_results l on l.da_household_id=a.pkid ";
				if (form_json.get("cx-v1")!=null&&!form_json.get("cx-v1").equals("")) {
					term+=" l.v1 is NULL or l.v1 = ''  or";
				}
				if (form_json.get("cx-v2")!=null&&!form_json.get("cx-v2").equals("")) {
					term+=" l.v2 is NULL or l.v2 = ''  or";
				}
				if (form_json.get("cx-v3")!=null&&!form_json.get("cx-v3").equals("")) {
					term+=" l.v3 is NULL or l.v3 = ''  or";
				}
			}else {
				left_sql=" left join da_help_results l on l.da_household_id=a.pkid ";
				if (form_json.get("cx-v1")!=null&&!form_json.get("cx-v1").equals("")) {
					term+=" l.v1 != '' and";
				}
				if (form_json.get("cx-v2")!=null&&!form_json.get("cx-v2").equals("")) {
					term+=" l.v2 != '' and";
				}
				if (form_json.get("cx-v3")!=null&&!form_json.get("cx-v3").equals("")) {
					term+=" l.v3 != '' and";
				}
			}
		}else if (form_name.equals("cuoshi_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
					left_sql+=" join da_help_tz_measures m on m.da_household_id=a.pkid";
					if (form_json.get("cs_v1")!=null&&!form_json.get("cs_v1").equals("")) {
						term+=" m.v1 is NULL or m.v1 = ''  or";
					}
					if (form_json.get("cs_v2")!=null&&!form_json.get("cs_v2").equals("")) {
						term+=" m.v2 is NULL or m.v2 = ''  or";
					}
					if (form_json.get("cs_v3")!=null&&!form_json.get("cs_v3").equals("")) {
						term+=" m.v3 is NULL or m.v3 = ''  or";
					}
				}else {
					left_sql+=" left join da_help_tz_measures m on m.da_household_id=a.pkid";
					if (form_json.get("cs_v1")!=null&&!form_json.get("cs_v1").equals("")) {
						term+=" m.v1 != '' and";
					}
					if (form_json.get("cs_v2")!=null&&!form_json.get("cs_v2").equals("")) {
						term+=" m.v2 != '' and";
					}
					if (form_json.get("cs_v3")!=null&&!form_json.get("cs_v3").equals("")) {
						term+=" m.v3 != '' and";
					}
				}
		}else if (form_name.equals("hshouru_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql="  join da_helpback_income n on n.da_household_id = a.pkid";
				if (form_json.get("v2")!=null&&!form_json.get("v2").equals("")) {
					term+=" n.v2 is NULL or ";
				}
				if (form_json.get("v4")!=null&&!form_json.get("v4").equals("")) {
					term+=" n.v4 is NULL or ";
				}
				if (form_json.get("v6")!=null&&!form_json.get("v6").equals("")) {
					term+=" n.v6 is NULL or ";
				}
				if (form_json.get("v8")!=null&&!form_json.get("v8").equals("")) {
					term+=" n.v8 is NULL or ";
				}
				if (form_json.get("v12")!=null&&!form_json.get("v12").equals("")) {
					term+=" n.v12 is NULL or ";
				}
				if (form_json.get("v14")!=null&&!form_json.get("v14").equals("")) {
					term+=" n.v14 is NULL or ";
				}
				if (form_json.get("v16")!=null&&!form_json.get("v16").equals("")) {
					term+=" n.v16 is NULL or ";
				}
				if (form_json.get("v18")!=null&&!form_json.get("v18").equals("")) {
					term+=" n.v18 is NULL or ";
				}
				if (form_json.get("v43")!=null&&!form_json.get("v43").equals("")) {
					term+=" n.v43 is NULL or ";
				}
				if (form_json.get("v41")!=null&&!form_json.get("v41").equals("")) {
					term+=" n.v41 is NULL or ";
				}
				if (form_json.get("v26")!=null&&!form_json.get("v26").equals("")) {
					term+=" n.v26 is NULL or ";
				}
				if (form_json.get("v24")!=null&&!form_json.get("v24").equals("")) {
					term+=" n.v24 is NULL or ";
				}
				if (form_json.get("v28")!=null&&!form_json.get("v28").equals("")) {
					term+=" n.v28 is NULL or ";
				}
				if (form_json.get("v32")!=null&&!form_json.get("v32").equals("")) {
					term+=" n.v32 is NULL or ";
				}
			}else {									  //已完成	
				left_sql="  join da_helpback_income n on n.da_household_id = a.pkid";
				if (form_json.get("v2")!=null&&!form_json.get("v2").equals("")) {
					term+=" n.v2 is not NULL and";
				}
				if (form_json.get("v4")!=null&&!form_json.get("v4").equals("")) {
					term+=" n.v4 is not NULL and";
				}
				if (form_json.get("v6")!=null&&!form_json.get("v6").equals("")) {
					term+=" n.v6 is not NULL and";
				}
				if (form_json.get("v8")!=null&&!form_json.get("v8").equals("")) {
					term+=" n.v8 is not NULL and";
				}
				if (form_json.get("v12")!=null&&!form_json.get("v12").equals("")) {
					term+=" n.v12 is not NULL and";
				}
				if (form_json.get("v14")!=null&&!form_json.get("v14").equals("")) {
					term+=" n.v14 is not NULL and";
				}
				if (form_json.get("v16")!=null&&!form_json.get("v16").equals("")) {
					term+=" n.v16 is not NULL and";
				}
				if (form_json.get("v18")!=null&&!form_json.get("v18").equals("")) {
					term+=" n.v18 is not NULL and";
				}
				if (form_json.get("v43")!=null&&!form_json.get("v43").equals("")) {
					term+=" n.v43 is not NULL and";
				}
				if (form_json.get("v41")!=null&&!form_json.get("v41").equals("")) {
					term+=" n.v41 is not NULL and";
				}
				if (form_json.get("v26")!=null&&!form_json.get("v26").equals("")) {
					term+=" n.v26 is not NULL and";
				}
				if (form_json.get("v24")!=null&&!form_json.get("v24").equals("")) {
					term+=" n.v24 is not NULL and";
				}
				if (form_json.get("v28")!=null&&!form_json.get("v28").equals("")) {
					term+=" n.v28 is not NULL and";
				}
				if (form_json.get("v32")!=null&&!form_json.get("v32").equals("")) {
					term+=" n.v32 is not NULL and";
				}
			}
		}else if (form_name.equals("hzhichu_form")) {
			if (danxuan_json.get("a").equals("0")) {  //未完成
				left_sql=" join da_helpback_expenditure o on o.da_household_id = a.pkid";
				if (form_json.get("zc-v2")!=null&&!form_json.get("zc-v2").equals("")) {
					term+=" o.v2 is NULL or ";
				}
				if (form_json.get("zc-v4")!=null&&!form_json.get("zc-v4").equals("")) {
					term+=" o.v4 is NULL or ";
				}
				if (form_json.get("zc-v6")!=null&&!form_json.get("zc-v6").equals("")) {
					term+=" o.v6 is NULL or ";
				}
				if (form_json.get("zc-v8")!=null&&!form_json.get("zc-v8").equals("")) {
					term+=" o.v8 is NULL or ";
				}
				if (form_json.get("zc-v10")!=null&&!form_json.get("zc-v10").equals("")) {
					term+=" o.v10 is NULL or ";
				}
				if (form_json.get("zc-v12")!=null&&!form_json.get("zc-v12").equals("")) {
					term+=" o.v12 is NULL or ";
				}
				if (form_json.get("zc-v14")!=null&&!form_json.get("zc-v14").equals("")) {
					term+=" o.v14 is NULL or ";
				}
				if (form_json.get("zc-v16")!=null&&!form_json.get("zc-v16").equals("")) {
					term+=" o.v16 is NULL or ";
				}
				if (form_json.get("zc-v18")!=null&&!form_json.get("zc-v18").equals("")) {
					term+=" o.v18 is NULL or ";
				}
				if (form_json.get("zc-v20")!=null&&!form_json.get("zc-v20").equals("")) {
					term+=" o.v20 is NULL or ";
				}
				if (form_json.get("zc-v27")!=null&&!form_json.get("zc-v27").equals("")) {
					term+=" o.v27 is NULL or ";
				}
			}else {									  //已完成	
				left_sql=" join da_helpback_expenditure o on o.da_household_id = a.pkid";
				if (form_json.get("zc-v2")!=null&&!form_json.get("zc-v2").equals("")) {
					term+=" o.v2 is not NULL and";
				}
				if (form_json.get("zc-v4")!=null&&!form_json.get("zc-v4").equals("")) {
					term+=" o.v4 is not NULL and";
				}
				if (form_json.get("zc-v6")!=null&&!form_json.get("zc-v6").equals("")) {
					term+=" o.v6 is not NULL and";
				}
				if (form_json.get("zc-v8")!=null&&!form_json.get("zc-v8").equals("")) {
					term+=" o.v8 is not NULL and";
				}
				if (form_json.get("zc-v10")!=null&&!form_json.get("zc-v10").equals("")) {
					term+=" o.v10 is not NULL and";
				}
				if (form_json.get("zc-v12")!=null&&!form_json.get("zc-v12").equals("")) {
					term+=" o.v12 is not NULL and";
				}
				if (form_json.get("zc-v14")!=null&&!form_json.get("zc-v14").equals("")) {
					term+=" o.v14 is not NULL and";
				}
				if (form_json.get("zc-v16")!=null&&!form_json.get("zc-v16").equals("")) {
					term+=" o.v16 is not NULL and";
				}
				if (form_json.get("zc-v18")!=null&&!form_json.get("zc-v18").equals("")) {
					term+=" o.v18 is not NULL and";
				}
				if (form_json.get("zc-v20")!=null&&!form_json.get("zc-v20").equals("")) {
					term+=" o.v20 is not NULL and";
				}
				if (form_json.get("zc-v27")!=null&&!form_json.get("zc-v27").equals("")) {
					term+=" o.v27 is not NULL and";
				}
			}
		}
		String aString="";
		String where =" where "+shilevel_sql+" and ("+ term.substring(0,term.length()-3) +")";
		String people_sql="select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v21,a.v22,a.v23,a.v11,a.sys_standard from da_household a ";
		String count_sql ="select count(*) from (select a.pkid from da_household a "+ left_sql +" where "+ shilevel_sql +" and ("+ term.substring(0,term.length()-3) +") GROUP BY a.pkid) z ";
		String fpeople_sql=people_sql+left_sql+where+"GROUP BY a.pkid order by a.v2,a.pkid limit "+number+","+size;
		SQLAdapter count_st_Adapter = new SQLAdapter(count_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		if ( form_name ==null && form_name.equals("") && form_val  ==null && form_val .equals("")&& danxuan_val  ==null && danxuan_val .equals("")&& json_level  ==null && json_level .equals("")) {
			count_sql="select count(*) from (select a.pkid from da_household a  GROUP BY a.pkid) z ";
			fpeople_sql= people_sql+ "GROUP BY a.pkid order by a.v2,a.pkid limit "+number+","+size;
		}
		SQLAdapter Patient_st_Adapter = new SQLAdapter(fpeople_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		if(Patient_st_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Patient_st_map.keySet()) {
					
					val.put(key, Patient_st_map.get(key));
					
					if(key.toString().equals("col_name")){
						if("".equals(Patient_st_map.get("col_name")+"")){
							val.put("col_name", "否");
						}else{
							val.put("col_name", "是");
						}
					}
					val.put("chakan","<a onclick='chakan_info(\""+Patient_st_map.get("pkid")+"\")'>查看</a>");
//					else if(!(key.toString().equals("col_name"))) {
//						val.put("col_name", "否");
//					}
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
	
	/**
	 * 获取行政区划树节点
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */public ModelAndView getincompleteTreeData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		HttpSession session = request.getSession();//取session
		Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
		JSONObject company_json = new JSONObject();
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
		}else if(com_level.equals("3")){//旗县用户
			sql_code=com_code.substring(0,9);
			sql = "SELECT *FROM sys_company WHERE pkid=4 UNION ALL SELECT * FROM sys_company WHERE com_code LIKE '%"+sql_code+"%'";
		}else if(com_level.equals("4")){//旗县用户
			sql_code=com_code;
			sql = "SELECT *FROM sys_company WHERE pkid=4 UNION ALL SELECT * FROM sys_company WHERE com_code LIKE '%"+sql_code+"%'";
		}
		
		SQLAdapter tree_Adapter = new SQLAdapter(sql);
		List<Map> tree_List = this.getBySqlMapper.findRecords(tree_Adapter);
		
		JSONObject val = new JSONObject();
		JSONArray jsa = new JSONArray();
		
		if(tree_List.size()>0){
			if (com_level.equals("1")) {
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
			}else if (com_level.equals("2")) {
				JSONArray xian_js = new JSONArray();
				for(int j = 0;j<tree_List.size();j++){//第二遍循环，取县级单位
					Map xian_map = tree_List.get(j);
					if(xian_map.get("com_level").toString().equals("2")){//第二级，同时父id等于上一级id
						
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
						xian_js.add(xian);
					}
				}
				response.getWriter().write(xian_js.toString());
			}else if (com_level.equals("3")) {
				JSONArray xiang_js = new JSONArray();
				for(int p = 0;p<tree_List.size();p++){//第三遍循环，取乡单位
					Map xiang_map = tree_List.get(p);
					if(xiang_map.get("com_level").toString().equals("3")){//第三级，同时父id等于上一级id
						
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
						xiang_js.add(xiang);
					}
				}
				response.getWriter().write(xiang_js.toString());
			}else {
				JSONArray cun_js = new JSONArray();
				for(int k = 0;k<tree_List.size();k++){
					Map cun_map = tree_List.get(k);
					if(cun_map.get("com_level").toString().equals("4")){//第四级，同时父id等于上一级id
						JSONObject cun = new JSONObject();
						cun.put("text", cun_map.get("com_name"));
						cun.put("pkid", cun_map.get("pkid"));
						cun_js.add(cun);
					}
				}
				response.getWriter().write(cun_js.toString());
			}
			//response.getWriter().write(jsa.toString());
		}
		
		return null;
	}
	
	
}


