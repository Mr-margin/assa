package com.gistone.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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

public class DataStatisticsController  extends MultiActionController{

	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * 国家标准数据统计
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getCountry_Stat(HttpServletRequest request,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String qu_g = ""; //旗区
		String sum_g = "";	//苏木乡
		String type = request.getParameter("type").trim(); //贫困人口类型
		String order=request.getParameter("order");	//正序
		String sort=request.getParameter("sort");	//倒序
		String sum_name = "";
		String year = request.getParameter("year");//年份
		if( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		if(request.getParameter("qu_g")!=null&&!request.getParameter("qu_g").equals("")){ //获取旗区的值
			qu_g = request.getParameter("qu_g").trim();
		}
		
		String pkid = ""; //旗区的ID
		String ziduan = ""; //字段名称
		String cun_duyou = "";  //村级别的查询语句
		if(qu_g.equals("4")){//鄂尔多斯
			pkid = qu_g;
			ziduan = "v1";
		}else{
			if(request.getParameter("sum_g")!=null&&!request.getParameter("sum_g").equals("请选择")){//村
				sum_g = request.getParameter("sum_g").trim();
				pkid = sum_g;
				
				sum_name=request.getParameter("sum_name");
				cun_duyou="and a.v2='"+sum_name+"'";
				
				ziduan = "v3";
			}else{//乡
				pkid = qu_g;
				ziduan = "v2";
			}
		}
		String sql = "SELECT * from (select a."+ziduan+" as b1,SUM(a.b2) as b2 , SUM(a.b3) as b3, SUM(a.b4) as b4,SUM(a.b6)as b6,SUM(a.b7)as b7 ,SUM(a.b8) as b8,SUM(a.b9) as b9,SUM(a.b10) as b10,SUM(a.b11) as b11,SUM(a.b12) as b12,SUM(a.b13) as b13,a.b14,SUM(a.b16) as b16,SUM(a.b17) as b17 from da_statistics"+year+" a"
				 +" where a.b14='"+ type +"' "+ cun_duyou +" GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name";
		if(sort==""||sort==null){
			sql += " where com_f_pkid="+pkid+"   and b2>0 order by b2 desc ";
		}else{
			sql += " where com_f_pkid="+pkid+"   and b2>0 order by "+sort+" "+order+" ";
		}
		SQLAdapter sql_find = new SQLAdapter(sql);
		List<Map> Metadata_g_List = this.getBySqlMapper.findRecords(sql_find);
		int total = Metadata_g_List.size();
		DecimalFormat df = new DecimalFormat("0.00");
		String str1;
		if(Metadata_g_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(Map val:Metadata_g_List){
				JSONObject obj = new JSONObject ();
				str1 = "";
				if(val.get("b2")!=null){
					obj.put("b1",val.get("b1")==null?"":val.get("b1"));
					obj.put("b2",val.get("b2")==null?"":val.get("b2"));
					obj.put("b3",val.get("b3")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b3").toString()));
					if("".equals(val.get("b4"))||val.get("b4")==null){
						obj.put("b4",val.get("b4")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b4").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b4").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b4",val.get("b4")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b4").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b6"))||val.get("b6")==null){
						obj.put("b6",val.get("b6")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b6").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b6").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b6",val.get("b6")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b6").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					
					if("".equals(val.get("b7"))||val.get("b7")==null){
						obj.put("b7",val.get("b7")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b7").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b7").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b7",val.get("b7")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b7").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b8"))||val.get("b8")==null){
						obj.put("b8",val.get("b8")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b8").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b8").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b8",val.get("b8")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b8").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b9"))||val.get("b9")==null){
						obj.put("b9",val.get("b9")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b9").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b9").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b9",val.get("b9")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b9").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b10"))||val.get("b10")==null){
						obj.put("b10",val.get("b10")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b10").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b10").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b10",val.get("b10")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b10").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b11"))||val.get("b11")==null){
						obj.put("b11",val.get("b11")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b11").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b11").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b11",val.get("b11")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b11").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b12"))||val.get("b12")==null){
						obj.put("b12",val.get("b12")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b12").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b12").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b12",val.get("b12")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b12").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b13"))||val.get("b13")==null){
						obj.put("b13",val.get("b13")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b13").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b13").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b13",val.get("b13")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b13").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b16"))||val.get("b16")==null){
						obj.put("b16",val.get("b16")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b16").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b16").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b16",val.get("b16")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b16").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					if("".equals(val.get("b17"))||val.get("b17")==null){
						obj.put("b17",val.get("b17")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b17").toString()));
					}else{
						str1=df.format((Double.parseDouble(val.get("b17").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
						obj.put("b17",val.get("b17")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b17").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
					}
					jsa.add(obj);
				}
			}
			JSONObject json = new JSONObject();
			json.put("page", "");
			json.put("total", total);
			json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
			response.getWriter().write(json.toString());
		}
		return null;
	}
	
	public String getData(String b2, String str){
		if(Integer.parseInt(str)<Integer.parseInt(b2)){
			return "<code>"+str+"</code>";
		}else{
			return str;
		}
	}
	
	/**
	 * 市级标准数据统计
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getCity_Stat(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		
		
		return null;
	}
	
	 /**
		 * @method 点击刷新按钮后，将数据库查询到的结果保存到另一张表中
		 * @param request
		 * @param response
		 * @throws Exception
		 * @author 张晓翔
		 * @date 2016年8月16日下午5:53:14
		 */
	public void updata_xinbiao(HttpServletRequest request,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String year=request.getParameter("year");
		
		if(year.equals("2016")){
			year="_2016";
		}
		String sfcg="0";//作为是否成功清除表da_statistics的判断依据
		Date now= new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = dateFormat.format( now );//获取时间
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time2 = dateFormat2.format( now );//获取时间
		String del_sql="truncate table da_statistics"+year;//清空表da_statistics
		try {
				SQLAdapter del_sqlAdapter = new SQLAdapter(del_sql);
				this.getBySqlMapper.findRecords(del_sqlAdapter);
				sfcg="1";//清除成功为1
		} catch (Exception e) {
			sfcg="0";//清除失败为0
		}
		if(sfcg.equals("1")){//如果清除成功，执行插入数据语句
			String insert_sql="INSERT INTO da_statistics"+year+" (v1,v2,v3,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17) "
					+ " select t1.v3 v1,t1.v4 v2,t1.v5 v3,b2,b3,b4,b6,b7,b8,b9,b10,b11,b12,b13,t1.sys_standard b14,'up_time' b15,b16,b17 from("
					+ "select v3,v4,v5,COUNT(*) as b2,sum(v9) as b3,sys_standard from da_household"+year+" where v21!='已脱贫' group by v3,v4,v5,sys_standard) t1 "
					+ " left join (select v3,v4,v5,COUNT(*) as b4 ,sys_standard from b4_t"+year+" group by v3,v4,v5,sys_standard) t4 on t1.v3=t4.v3 and t1.v4=t4.v4 and t1.v5=t4.v5 and t1.sys_standard=t4.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b6 ,sys_standard from b6_t"+year+" group by v3,v4,v5,sys_standard) t6 on t1.v3=t6.v3 and t1.v4=t6.v4 and t1.v5=t6.v5 and t1.sys_standard=t6.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b7 ,sys_standard from b7_t"+year+" group by v3,v4,v5,sys_standard) t7 on t1.v3=t7.v3 and t1.v4=t7.v4 and t1.v5=t7.v5 and t1.sys_standard=t7.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b8 ,sys_standard from b8_t"+year+" group by v3,v4,v5,sys_standard) t8 on t1.v3=t8.v3 and t1.v4=t8.v4 and t1.v5=t8.v5 and t1.sys_standard=t8.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b9 ,sys_standard from b9_t"+year+" group by v3,v4,v5,sys_standard) t9 on t1.v3=t9.v3 and t1.v4=t9.v4 and t1.v5=t9.v5  and t1.sys_standard=t9.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b10 ,sys_standard from b10_t"+year+" group by v3,v4,v5,sys_standard) t10 on t1.v3=t10.v3 and t1.v4=t10.v4 and t1.v5=t10.v5 and t1.sys_standard=t10.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b11 ,sys_standard from b11_t"+year+" group by v3,v4,v5,sys_standard) t11 on t1.v3=t11.v3 and t1.v4=t11.v4 and t1.v5=t11.v5 and t1.sys_standard=t11.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b12 ,sys_standard from b12_t"+year+" group by v3,v4,v5,sys_standard) t12 on t1.v3=t12.v3 and t1.v4=t12.v4 and t1.v5=t12.v5 and t1.sys_standard=t12.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b13 ,sys_standard from b13_t"+year+" group by v3,v4,v5,sys_standard) t13 on t1.v3=t13.v3 and t1.v4=t13.v4 and t1.v5=t13.v5 and t1.sys_standard=t13.sys_standard"
					+ " left join (select v3,v4,v5,COUNT(*) as b16 ,sys_standard from b14_t"+year+" group by v3,v4,v5,sys_standard) t14 on t1.v3=t14.v3 and t1.v4=t14.v4 and t1.v5=t14.v5 and t1.sys_standard=t14.sys_standard "
					+ " left join (select v3,v4,v5,COUNT(*) as b17 ,sys_standard from b15_t"+year+" group by v3,v4,v5,sys_standard) t15 on t1.v3=t15.v3 and t1.v4=t15.v4 and t1.v5=t15.v5 and t1.sys_standard=t15.sys_standard";
			try {
				SQLAdapter insert_sqlAdapter = new SQLAdapter(insert_sql);
				this.getBySqlMapper.findRecords(insert_sqlAdapter);
				sfcg="2";//插入数据成功
				String uptime_sql="UPDATE da_statistics"+year+" SET b15 ='"+time+"'";//插入da_statistics的b15为当前系统时间
				SQLAdapter uptime_sqlAdapter = new SQLAdapter(uptime_sql);
				this.getBySqlMapper.updateSelective(uptime_sqlAdapter);
				String uptime_sql2="UPDATE da_click_refresh SET updateTime ='"+time2+"',isOperation=0";//插入da_statistics的b15为当前系统时间
				SQLAdapter uptime_sqlAdapter2 = new SQLAdapter(uptime_sql2);
				this.getBySqlMapper.updateSelective(uptime_sqlAdapter2);
				response.getWriter().write("1");
				
				
			} catch (Exception e) {
				String uptime_sql2="UPDATE da_click_refresh SET updateTime ='"+time2+"',isOperation=0";//插入da_statistics的b15为当前系统时间
				SQLAdapter uptime_sqlAdapter2 = new SQLAdapter(uptime_sql2);
				this.getBySqlMapper.updateSelective(uptime_sqlAdapter2);
				response.getWriter().write("0");//插入失败，返回0
			}
		}else{
			String uptime_sql2="UPDATE da_click_refresh SET updateTime ='"+time2+"',isOperation=0";//插入da_statistics的b15为当前系统时间
			SQLAdapter uptime_sqlAdapter2 = new SQLAdapter(uptime_sql2);
			this.getBySqlMapper.updateSelective(uptime_sqlAdapter2);
			response.getWriter().write("0");//插入失败，返回0
		}
	}
	
	public void time_data(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql = "select b15 from da_statistics where pkid=1";
		SQLAdapter sql_time = new SQLAdapter(sql);
		List<Map> time_list = this.getBySqlMapper.findRecords(sql_time);
		String time="0";
		if(time_list.size()>0){
			time=time_list.get(0).get("b15").toString();
		}
//		JSONObject json_time= new JSONObject();
//		json_time.put("b15", time);
		response.getWriter().write(time);
	}
	/**
	 * 校验别人是否在进行点击刷新操作
	 * @param request
	 * @param response
	 * @throws IOException
	 *	@author Liulifeng
	 *  @date 2017年4月19日 下午2:54:34
	 */
	public void isOperation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String isOperation_sql="select * from da_click_refresh";
		SQLAdapter sql_adapter = new SQLAdapter(isOperation_sql);
		try {
			List<Map> isOperation_list = this.getBySqlMapper.findRecords(sql_adapter);
			if(isOperation_list.size()>0){
				for(Map val:isOperation_list){
					int isOperation=Integer.parseInt(val.get("isOperation").toString());
					String updateTime=val.get("updateTime").toString();
					SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date begin = dfs.parse(updateTime);
					Date end = new Date();
					long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
					long minute2=between/60;
					if(isOperation==0||minute2>=30){
						String uptime_sql2="UPDATE da_click_refresh SET isOperation=1";//插入da_statistics的b15为当前系统时间
						SQLAdapter uptime_sqlAdapter2 = new SQLAdapter(uptime_sql2);
						this.getBySqlMapper.updateSelective(uptime_sqlAdapter2);
						response.getWriter().write("1");
					}else{
						response.getWriter().write("0");
					}
				}
			}
		} catch (Exception e) {
			response.getWriter().write("2");
		}
		
	}
	
	/**
	 * 帮扶人与单位数据统计
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getBangFu_Stat(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String order=request.getParameter("order");
		String sort=request.getParameter("sort");
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String sql = " select t1.com_name as b1,b2,b3,b4,b5 from sys_company t1 ";
		sql += " left join (select sys_company_id,count(*) as b2 from sys_personal"+year+" t1 join da_company"+year+" t2 on t1.da_company_id=t2.pkid group by sys_company_id) t2 ";
		sql += " on t1.pkid=t2.sys_company_id LEFT JOIN (select sys_company_id,count(*) as b3 from da_company t1 ";
		sql += " join (select t1.pkid,t1.da_company_id from sys_personal"+year+" t1 join sys_personal_household_many"+year+" t2 on t1.pkid=t2.sys_personal_id group by t1.pkid,t1.da_company_id ";
		sql += " ) t2 on t1.pkid=t2.da_company_id group by sys_company_id) t3 on t1.pkid=t3.sys_company_id ";
		sql += " LEFT JOIN (select sys_company_id,count(*) as b4 from da_company group by sys_company_id) t4 on t1.pkid=t4.sys_company_id ";
		sql += " LEFT JOIN (select sys_company_id,count(*) as b5 from da_company where v5 is not null and v5 !='' group by sys_company_id) t5 on t1.pkid=t5.sys_company_id ";
		if(sort==null||sort==""){
			sql += " where com_f_pkid=4 or t1.pkid=4 order by b2 asc ";
		}else{
			sql += " where com_f_pkid=4 or t1.pkid=4 order by "+sort+" "+order+" ";
		}
		DecimalFormat df = new DecimalFormat("0.00");
//		System.out.println(sql);
		SQLAdapter Metadata_g_Adapter = new SQLAdapter(sql);
		List<Map> Metadata_g_List = this.getBySqlMapper.findRecords(Metadata_g_Adapter);
		int total = Metadata_g_List.size();
		String str1;
		if(Metadata_g_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(Map val:Metadata_g_List){
				JSONObject obj=new JSONObject ();
				obj.put("b1",val.get("b1")==null?"":val.get("b1"));
				obj.put("b2",val.get("b2")==null?"<code>0</code>":val.get("b2"));
				if("".equals(val.get("b3"))||val.get("b3")==null){
					obj.put("b3",val.get("b3")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b3").toString()));
				}else{
					str1=df.format((Double.parseDouble(val.get("b3").toString()))/(Double.parseDouble(val.get("b2").toString()))*100)+"%";
					obj.put("b3",val.get("b3")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b3").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
				}
					obj.put("b4",val.get("b4")==null?"<code>0</code>":val.get("b4"));
				if("".equals(val.get("b5"))||val.get("b5")==null){
					obj.put("b5",val.get("b5")==null?"<code>0</code>":getData(val.get("b2").toString(),val.get("b5").toString()));
				}else{
					str1=df.format((Double.parseDouble(val.get("b5").toString()))/(Double.parseDouble(val.get("b4").toString()))*100)+"%";
					obj.put("b5",val.get("b5")==null?"<code>0</code>":getData(val.get("b4").toString(),val.get("b5").toString())+"("+str1+")"+"<div class=\"progress progress-mini\"><div style=\"width:"+str1+";\" class=\"progress-bar\"></div></div>");
				}
				
				jsa.add(obj);
			}
			JSONObject json = new JSONObject();
			json.put("page", "");
			json.put("total", total);
			json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
			response.getWriter().write(json.toString());
		}
		
		return null;
	}
}
