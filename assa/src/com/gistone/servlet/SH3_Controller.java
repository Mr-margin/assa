package com.gistone.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;
/**
 * 挂图作战
 * @author chendong
 *
 */
public class SH3_Controller extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * H3 页面挂图计划优化 饼状图
	 * @param request
	 * @param response
	 * @return
	 * @author 呼树明
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public  ModelAndView H3_pie_All(HttpServletRequest request,HttpServletResponse response) throws IOException{
 		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String mokuai_name = request.getParameter("mokuai_name");//说明：模块名称。例如 jiatingshouzhi
		String shujv = request.getParameter("shujv");//传输数据，例如：东胜区，家庭规模的人数
		String leixing_name = request.getParameter("leixing");//传入的类型，例如 国家级 贫困人口
		String  year = request.getParameter("year");//年份
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		HttpSession session = request.getSession();
		Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
		JSONObject company_json = new JSONObject();
		String sql = "";
		for(String key : company.keySet()){
			company_json.put(key, company.get(key));
		}
		String com_name = company_json.get("com_name").toString();//获取用户名称
		String com_level = company_json.get("com_level").toString();//获取用户层级
		//加载行政区划
		String level=request.getParameter("level");
		String jsonname=request.getParameter("jsonname");
		String json_level= request.getParameter("jsonlevel");
		String shilevel_sql="";
		int shilevel=0;
		String shi_name="";
		//判断哪些需要加载行政区划
			if (json_level==null) {
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
				shilevel_sql+=" and v2 like '%"+ shi_name +"%' ";
			}else if (shilevel==2) {
				shilevel_sql+=" and v3 like '%"+ shi_name +"%' ";
			}else if (shilevel==3) {
				shilevel_sql+=" and v4 like '%"+ shi_name +"%' ";
			}
			else if (shilevel==4) {
				shilevel_sql+=" and v5 like '%"+ shi_name +"%' ";
			}
		
		
		if (mokuai_name.equals("jiatingshouzhi")) {	//根据模块名称来判断执行模块 家庭收支模块
			int yiqian = 0; //0-1000元
			int ydeqian = 0; //1000-2000元
			int edsqian = 0; //2000-3000元
			int sdsqian = 0; //3000-4000元
			int sdwqian = 0; //4000-5000元
			int wys = 0; //5000元以上
			String pkid_sql = "SELECT pkid,v9 FROM da_household"+year+" where  sys_standard='"+ leixing_name +"'"+shilevel_sql+"";
			SQLAdapter pkidAdapter = new SQLAdapter(pkid_sql);
			List<Map> pkidList = this.getBySqlMapper.findRecords(pkidAdapter);
			StringBuilder pkids=new StringBuilder();
			List<Integer> familysize_list=new ArrayList<Integer>();//一户的家庭人数
			if(pkidList.size()>0){
				for(int i=0;i<pkidList.size();i++){
					if(i==pkidList.size()-1){
						pkids.append(pkidList.get(i).get("pkid").toString());
					}else{
						pkids.append(pkidList.get(i).get("pkid").toString()+",");
					}
					familysize_list.add((Integer) pkidList.get(i).get("v9"));
				}
				//帮扶后总收入
				String dqsrh_sql2="SELECT da_household_id,v39 FROM da_helpback_income"+year+" where da_household_id in("+pkids+")";
				SQLAdapter dqsrh_sqlAdapter2 =new SQLAdapter(dqsrh_sql2);
				List<Map> dqsrh_list2=getBySqlMapper.findRecords(dqsrh_sqlAdapter2);
				//帮扶后总支出
				String dqzch_sql2="SELECT da_household_id,v31 FROM da_helpback_expenditure"+year+" where da_household_id in("+pkids+")";
				SQLAdapter dqzch_sqlAdapter2 =new SQLAdapter(dqzch_sql2);
				List<Map> dqzch_list2=getBySqlMapper.findRecords(dqzch_sqlAdapter2);
				JSONObject val = new JSONObject();
				JSONArray jsa = new JSONArray();
				for(int y=0;y<dqzch_list2.size();y++){
					int familysize=familysize_list.get(y);
					Map dqsrh_map2=dqsrh_list2.get(y);
					Map dqzch_map2=dqzch_list2.get(y);
					float dqsrh2=(float) (dqsrh_map2.get("v39")==null?0.00:(Float)dqsrh_map2.get("v39"));
					float dqzch2=(float) (dqzch_map2.get("v31")==null?0.00:(Float)dqzch_map2.get("v31"));
					float year_income2=dqsrh2-dqzch2;//年纯收入
					float per_capita_income2=year_income2/familysize;//人均纯收入
					if (per_capita_income2>0 && per_capita_income2<=1000) {
						yiqian += 1;
					} else if (per_capita_income2>1000 && per_capita_income2<=2000) {
						ydeqian += 1;
					}else if (per_capita_income2>2000 && per_capita_income2<=3000) {
						edsqian += 1;
					}else if (per_capita_income2>3000 && per_capita_income2<=4000) {
						sdsqian += 1;
					}else if (per_capita_income2>4000 && per_capita_income2<=5000) {
						sdwqian += 1;
					}else if (per_capita_income2>5000) {
						wys += 1;
					}
					
				}
				val.put("name", "0-1000");
				val.put("value",yiqian);
				jsa.add(val);
				val.put("name", "1000-2000");
				val.put("value",ydeqian);
				jsa.add(val);
				val.put("name", "2000-3000");
				val.put("value",edsqian);
				jsa.add(val);
				val.put("name", "3000-4000");
				val.put("value",sdsqian);
				jsa.add(val);
				val.put("name", "4000-5000");
				val.put("value",sdwqian);
				jsa.add(val);
				val.put("name", "5000以上");
				val.put("value",wys);
				jsa.add(val);
				response.getWriter().write(jsa.toString());
			}else {
				response.getWriter().write("0");
			}
			
//			if (com_level.equals("1")) { //用户层级为1时
//				if (shujv.equals("level-1")) {
//					sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where sys_standard='"+ leixing_name +"'";
//				}else {
//					sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where v3='"+shujv+"' and sys_standard='"+ leixing_name +"'";
//				}
//			}else if (com_level.equals("2")) {	//层级为2的用户
//				if (shujv.equals("level-1")) {
//					sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where v3='"+ com_name +"' and sys_standard='"+ leixing_name +"'";
//				}else {
//					sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where v3='"+shujv+"' and sys_standard='"+ leixing_name +"'";
//				}
//			}else if (com_level.equals("3")) {	//层级为3的时候用户的名字
//				if (shujv.equals("level-1")) {
//					 sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where v4='"+ com_name +"' and sys_standard='"+ leixing_name +"'";
//				}else {
//					sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where v4='"+shujv+"' and sys_standard='"+ leixing_name +"'";
//				}
//			}else {		//获取上级名称
//				com_name=company_json.get("xiang").toString();	
//				sql = "SELECT ROUND(v24,2) as num FROM da_household"+year+" where v4='"+ com_name +"' and sys_standard='"+ leixing_name +"'";
//			}
		}else if(mokuai_name.equals("lsbfcs")){ //根据模块名称来判断执行模块 落实帮扶比例模块 
//			if (com_level.equals("1")) {	//当用户层级为1时
//				if (shujv.equals("level-1")) {
//					sql="select SUM(b2) as b2,SUM(b12) as b12,(SUM(b2)-SUM(b12)) as b3 from sys_company t1"+  
//								  " left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t2 on t1.com_name=t2.v3"+  
//								  " left join (select v3,COUNT(*) as b12 from b12_t"+year+""+year+" where sys_standard='"+ leixing_name +"'  group by v3) t12 on t1.com_name=t12.v3"+
//								  " where  com_f_pkid=4 and b2>0 order by b2 desc ";
//				}else {
//					sql="select SUM(b2) as b2,SUM(b12) as b12,(SUM(b2)-SUM(b12)) as b3 from sys_company t1"+  
//							  " left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t2 on t1.com_name=t2.v3"+  
//							  " left join (select v3,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t12 on t1.com_name=t12.v3"+
//							  " where t1.com_name='"+ shujv +"' and com_f_pkid=4 and b2>0 order by b2 desc ";
//				}
//			}else if (com_level.equals("2")) {	//当层级为2时
//					sql="select SUM(b2) as b2,SUM(b12) as b12,(SUM(b2)-SUM(b12)) as b3,t2.v3 from sys_company t1"+ 
//							  " left join (select v3,v4,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t2 on t1.com_name=t2.v3"+ 
//							  " left join (select v3,v4,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t12 on t1.com_name=t12.v3 "+
//							  " where t2.v3='"+ com_name +"' and com_f_pkid=4 and b2>0 order by b2 desc ";
//				
//			}else if (com_level.equals("3")) {	//当层级为3时
//					sql="select SUM(b2) as b2,SUM(b12) as b12,(SUM(b2)-SUM(b12)) as b3,t2.v4 from sys_company t1"+ 
//						  " left join (select v3,v4,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v4) t2 on t1.com_name=t2.v3"+ 
//						  " left join (select v3,v4,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v4) t12 on t1.com_name=t12.v3 "+
//						  " where t2.v4='"+ com_name +"' and com_f_pkid=4 and b2>0 order by b2 desc ";
//			}else {	//当层级为4时
//				com_name = company_json.get("xiang").toString();
//				if (shujv.equals("level-1")) {
//					sql="select SUM(b2) as b2,SUM(b12) as b12,(SUM(b2)-SUM(b12)) as b3,t2.v4 from sys_company t1"+ 
//							  " left join (select v3,v4,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v4) t2 on t1.com_name=t2.v3"+ 
//							  " left join (select v3,v4,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v4) t12 on t1.com_name=t12.v3 "+
//							  " where t2.v4='"+ com_name +"' and com_f_pkid=4 and b2>0 order by b2 desc ";
//				
//				}else {//当层级为4时
//					com_name=company_json.get("xiang").toString(); //获取上级用户
//					sql = "	select SUM(b2) as b2,SUM(b12) as b12,(SUM(b2)-SUM(b12)) as b3 from sys_company t1"+  
//							  	  " left join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t2 on t1.com_name=t2.v3"+  
//							  	  " left join (select v3,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t12 on t1.com_name=t12.v3"+
//							  	  " where t1.com_name='"+ shujv +"' and com_f_pkid=4 and b2>0 order by b2 desc ";
//				}
//			}
			if (shilevel==1) {	//当层级为1时
				sql="SELECT SUM(t2.b2) as b2,SUM(t12.b12) as b12,(SUM(t2.b2)-SUM(t12.b12)) as b3 from "+ 
						  " (select v3,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3 ORDER BY v3) t2 "+ 
						  " left join (select v3,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v3 ORDER BY v3) t12 on t2.v3=t12.v3 ";
			}else if (shilevel==2) {	//当层级为2时
				sql="SELECT SUM(t2.b2) as b2,SUM(t12.b12) as b12,(SUM(t2.b2)-SUM(t12.b12)) as b3 from "+ 
						  " (select v4,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"' and v3 like '%"+shi_name+"%'  group by v4 ORDER BY v4) t2 "+ 
						  " left join (select v4,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"' and v3 like '%"+shi_name+"%' group by v4 ORDER BY v4) t12 on t2.v4=t12.v4 ";
			}else if (shilevel==3) {	//当层级为3时
				sql="SELECT SUM(t2.b2) as b2,SUM(t12.b12) as b12,(SUM(t2.b2)-SUM(t12.b12)) as b3 from "+ 
						  " (select v5,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"' and v4 like '%"+shi_name+"%'  group by v5 ORDER BY v5) t2 "+ 
						  " left join (select v5,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"' and v4 like '%"+shi_name+"%' group by v5 ORDER BY v5) t12 on t2.v5=t12.v5 ";
			}else if (shilevel==4) {	//当层级为3时
				sql="SELECT SUM(t2.b2) as b2,SUM(t12.b12) as b12,(SUM(t2.b2)-SUM(t12.b12)) as b3 from "+ 
						  " (select v5,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"' and v5 like '%"+shi_name+"%'  group by v5 ORDER BY v5) t2 "+ 
						  " left join (select v5,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"' and v5 like '%"+shi_name+"%' group by v5 ORDER BY v5) t12 on t2.v5=t12.v5 ";
			}
			
			SQLAdapter quan_sql_Adapter = new SQLAdapter(sql);
			List<Map> quanList = this.getBySqlMapper.findRecords(quan_sql_Adapter);
			JSONObject val = new JSONObject();
			if (quanList.size()>0 && quanList.get(0)!=null) {
				JSONArray jsa = new JSONArray();
				for (int i = 0; i < quanList.size(); i++) {
					Map quanMap = quanList.get(i);
					val.put("name","落实帮扶完成");
					val.put("value", quanMap.get("b12"));
					jsa.add(val);
					val.put("name", "落实帮扶未完成");
					val.put("value", quanMap.get("b3"));
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}else {
				response.getWriter().write("0");
			}
		}else if(mokuai_name.equals("zpyy")){	//根据模块名称来判断执行模块 致贫原因模块 
			String region="";
			String region2="";
			if(shilevel==1){
				region=" and v2='"+shi_name+"' ";
				region2=" and y.v2='"+shi_name+"' ";
			}else if(shilevel==2){
				region=" and v3='"+shi_name+"' ";
				region2=" and y.v3='"+shi_name+"' ";
			}else if(shilevel==3){
				region=" and v4='"+shi_name+"' ";
				region2=" and y.v4='"+shi_name+"' ";
			}else if(shilevel==4){
				region=" and v5='"+shi_name+"' ";
				region2=" and y.v5='"+shi_name+"' ";
			}
			sql = "select t1.huv23 AS v3,(t1.hu+t2.jia) as count from(select v23 as huv23,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' "+region+" group by v23) t1 join (select y.v23 as jiav23,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' "+region2+" group by y.v23)t2 on t1.huv23=t2.jiav23 ";
			//sql = "SELECT count(v9)as count,v23 as v3 FROM da_household"+year+" WHERE sys_standard='"+leixing_name+"' "+region+" GROUP BY v23";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			if(sql_list.size()>0){
				JSONArray jsa = new JSONArray();
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						if(Admin_st_map.get("v3")==""||Admin_st_map.get("v3").equals("")==true){
							val.put("name", "未知");
							val.put("value", Admin_st_map.get("count"));
						}else{
							val.put("name", Admin_st_map.get("v3"));
							val.put("value", Admin_st_map.get("count"));
						}
					}
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().print("0");
			}
		}else if (mokuai_name.equals("whcd")) { //根据模块名称来判断执行模块  文化程度模块 
			String region="";
			String region2="";
			if(shilevel==1){
				region=" and v2 like'%"+shi_name+"%' ";
				region2=" and x.v2 like '%"+shi_name+"%' ";
			}else if(shilevel==2){
				region=" and v3 like '%"+shi_name+"%' ";
				region2=" and x.v3 like '%"+shi_name+"%' ";
			}else if(shilevel==3){
				region=" and v4 like '%"+shi_name+"%' ";
				region2=" and x.v4 like '%"+shi_name+"%' ";
			}else if(shilevel==4){
				region=" and v5 like '%"+shi_name+"%' ";
				region2=" and x.v5 like '%"+shi_name+"%' ";
			}
			sql = "select t1.huv12 AS v12,(if(isnull(t1.hu),0,t1.hu)+if(isnull(t2.jia),0,t2.jia)) as count from(select v12 as huv12,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' "+region+" group by v12) t1 join (select y.v12 as jiav12,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' "+region2+" group by y.v12)t2 on t1.huv12=t2.jiav12 ";
			SQLAdapter sqlAdapter = new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			if(sql_list.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						if(Admin_st_map.get("v12")==""||Admin_st_map.get("v12").equals("")==true){
							val.put("name", "未知");
							val.put("value", Admin_st_map.get("count"));
						}else{
							val.put("name", Admin_st_map.get("v12"));
							val.put("value", Admin_st_map.get("count"));
						}
					}
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().print("0");
			}
		}else if (mokuai_name.equals("nljg")) { //根据模块名称来判断执行模块 年龄结构模块模块 
			Calendar cal = Calendar.getInstance();
			int int1=cal.get(Calendar.YEAR);
			int liushiyishang=0;
			int wushiyisahng=0;
			int sishizhiwushi=0;
			int sanshizhisishi=0;
			int ershizhisanshi=0;
			int shizhiershi=0;
			int shiyinei=0;
			int int2;
			int int3;
			int int4;
//			if(com_level.equals("1")){
//				if(shujv.equals("level-1")){
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) AND sys_standard='"+leixing_name+"' UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) ) x group by nnum";
//				}else{
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20)and v3='"+shujv+"'AND sys_standard='"+leixing_name+"'UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) AND  y.v3='"+shujv+"') x group by nnum";
//				}
//			}else if(com_level.equals("2")){//旗县级用户
//				if(shujv.equals("level-1")){
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) AND sys_standard='"+leixing_name+"' UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v3='"+com_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) ) x group by nnum";
//				}else{
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20)and v3='"+shujv+"'AND sys_standard='"+leixing_name+"'UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) AND  y.v3='"+shujv+"') x group by nnum";
//				}
//			}else if(com_level.equals("3")){//苏木乡镇
//				if(shujv.equals("level-1")){
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) AND sys_standard='"+leixing_name+"' UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v4='"+com_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) ) x group by nnum";
//				}else{
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20)and v4='"+shujv+"'AND sys_standard='"+leixing_name+"'UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) AND  y.v4='"+shujv+"') x group by nnum";
//				}
//			}else if(com_level.equals("4")){//嘎查村
//				com_name=company_json.get("xiang").toString();//获取上级用户名称
//				if(shujv.equals("level-1")){
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) AND sys_standard='"+leixing_name+"' UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v4='"+com_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) ) x group by nnum";
//				}else{
//					sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20)and v4='"+shujv+"'AND sys_standard='"+leixing_name+"'UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) AND  y.v4='"+shujv+"') x group by nnum";
//				}
//			}
			String XZQH="";
			if (shilevel==1) {										//判断区级
				XZQH=" and y.v2 like '%"+ shi_name +"%' ";
			}else if (shilevel==2) {
				XZQH=" and y.v3 like '%"+ shi_name +"%' ";
			}else if (shilevel==3) {
				XZQH=" and y.v4 like '%"+ shi_name +"%' ";
			}else if (shilevel==4) {
				XZQH=" and y.v5 like '%"+ shi_name +"%' ";
			}
			sql="select nnum,count(*)as count from (select substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20)"+shilevel_sql+"AND sys_standard='"+leixing_name+"'UNION ALL select substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20) "+XZQH+") x group by nnum";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			if(sql_list.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
//					for (Object key : Admin_st_map.keySet()) {
						int1=Integer.parseInt(String.valueOf(Admin_st_map.get("nnum"))); 
						int2=cal.get(Calendar.YEAR); 
						int3=int2-int1;
						int4=Integer.parseInt(String.valueOf(Admin_st_map.get("count"))); 
						if(int3>=130){
						}else if(int3>60&&int3<130){
							liushiyishang+=int4;
						}else if(int3>50&&int3<=60){
							wushiyisahng+=int4;
						}else if(int3>40&&int3<=50){
							sishizhiwushi+=int4;
						}else if(int3>30&&int3<=40){
							sanshizhisishi+=int4;
						}else if(int3>20&&int3<=30){
							ershizhisanshi+=int4;
						}else if(int3>10&&int3<=20){
							shizhiershi+=int4;
						}else if(int3>=0&&int3<=10){
							shiyinei+=int4;
						}
//					}
				}
				val.put("name", "0-10岁");
				val.put("value",shiyinei);
				jsa.add(val);
				val.put("name", "10-20岁");
				val.put("value",shizhiershi);
				jsa.add(val);
				val.put("name", "20-30岁");
				val.put("value",ershizhisanshi);
				jsa.add(val);
				val.put("name", "30-40岁");
				val.put("value",sanshizhisishi);
				jsa.add(val);
				val.put("name", "40-50岁");
				val.put("value",sishizhiwushi);
				jsa.add(val);
				val.put("name", "50-60岁");
				val.put("value",wushiyisahng);
				jsa.add(val);
				val.put("name", "60岁以上");
				val.put("value", liushiyishang);
				jsa.add(val);
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().print("0");
			}
		}else if (mokuai_name.equals("jtgm")) {
//			if (shilevel==1) {
//					if(shujv.equals("level-1")==true){
//						sql="SELECT v9,COUNT(v9) as count FROM da_household"+year+" where sys_standard='"+leixing_name+"'GROUP BY v9";
//					}else {
//						sql="SELECT v9,COUNT(v9) as count FROM da_household"+year+" WHERE v3='"+shujv+"' and sys_standard='"+leixing_name+"' GROUP BY v9";
//					}
//			}else if (shilevel==2) {
//				sql="SELECT v9,COUNT(v9) as count FROM da_household"+year+" where v3='"+com_name+"' and sys_standard='"+leixing_name+"'GROUP BY v9";
//			}else if (shilevel==3) {
//				sql="SELECT v9,COUNT(v9) as count FROM da_household"+year+" where sys_standard='"+leixing_name+"' and v4='"+com_name+"' GROUP BY v9";
//			}else {
//			com_name=company_json.get("xiang").toString();//获取上级用户名称
//				sql="SELECT v9,COUNT(v9) as count FROM da_household"+year+" where sys_standard='"+leixing_name+"' and v4='"+com_name+"' GROUP BY v9";
//			}
			sql="SELECT v9,COUNT(v9) as count FROM da_household"+year+" where sys_standard='"+leixing_name+"'"+shilevel_sql+" GROUP BY v9";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			if(sql_list.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						if(Admin_st_map.get("v9").equals("")==true){
						}
						val.put("name", Admin_st_map.get("v9"));
						val.put("value", Admin_st_map.get("count"));
						val.put("col_name", shi_name);
					}
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().print("0");
			}
		}
		return  null;
	}
	
	/**
	 * H3 页面挂图计划优化 地图
	 * @param request
	 * @param response
	 * @return
	 * @author 呼树明
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public ModelAndView H3_map_All(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		
		String shujv = request.getParameter("shujv"); //获取筛选条件
		String leixing_name = request.getParameter("leixing");// 获取国家级还是市级的贫困户
		String year = request.getParameter("year");//年份
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "" ;
		}
		String mokuai_name = request.getParameter("mokuai_name");//模块名称
		HttpSession session = request.getSession();//取session
		Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
		JSONObject company_json = new JSONObject();
		for(String key : company.keySet()){
			company_json.put(key, company.get(key));
		}
		String sql="";
		String com_name=company_json.get("com_name").toString();//获取用户名称
		String com_pkid = company_json.get("pkid").toString();//获取当前用户id
		String com_level=company_json.get("com_level").toString();//获取用户层级
		
		String level=request.getParameter("level");
		String jsonname=request.getParameter("jsonname");
		String json_level= request.getParameter("jsonlevel");
		String shilevel_sql="";
		int shilevel=0;
		String shi_name="";
		String com_code2="";
		String com_code3="";
		int com_f_pkid=0;//上级行政区划id
		if (json_level==null) {
			String shi_sql ="select * from sys_company c where c.com_level='"+ level +"' and c.com_name='"+jsonname+"'";
			SQLAdapter shi_Adapter = new SQLAdapter(shi_sql);
			List<Map> shi_listmap= this.getBySqlMapper.findRecords(shi_Adapter);
			shilevel= (Integer) shi_listmap.get(0).get("com_level");
			shi_name= jsonname;
			com_code2=shi_listmap.get(0).get("com_code").toString();
			if(shilevel!=1){
				com_f_pkid=Integer.parseInt(shi_listmap.get(0).get("com_f_pkid").toString());
			}
			if(shilevel==4){
			
			String shi_sql2 ="select c.com_code from sys_company c where c.pkid="+com_f_pkid+"";
			SQLAdapter shi_Adapter2 = new SQLAdapter(shi_sql2);
			List<Map> com_codeList= this.getBySqlMapper.findRecords(shi_Adapter2);
			if(com_codeList.size()>0){
				com_code3=com_codeList.get(0).get("com_code").toString();
			}
			}
		}else{
			String shi_sql ="select * from sys_company c where c.pkid='"+ json_level +"'";
			SQLAdapter shi_Adapter = new SQLAdapter(shi_sql);
			List<Map> shi_listmap= this.getBySqlMapper.findRecords(shi_Adapter);
			shilevel= (Integer) shi_listmap.get(0).get("com_level");
			shi_name= (String) shi_listmap.get(0).get("com_name");
			com_code2=shi_listmap.get(0).get("com_code").toString();
			if(shilevel!=1){
				com_f_pkid=Integer.parseInt(shi_listmap.get(0).get("com_f_pkid").toString());
			}
			if(shilevel==4){
				String shi_sql2 ="select c.com_code from sys_company c where c.pkid="+com_f_pkid+"";
				SQLAdapter shi_Adapter2 = new SQLAdapter(shi_sql2);
				List<Map> com_codeList= this.getBySqlMapper.findRecords(shi_Adapter2);
				if(com_codeList.size()>0){
					com_code3=com_codeList.get(0).get("com_code").toString();
				}
				}
		}
		
		
		
		if (mokuai_name.equals("jiatingshouzhi")) {	//根据模块名称来判断执行模块 家庭收支模块
//			String tiaojian="";
//			if(shujv.equals("0-1000")==true){	//判断筛选金额
//				tiaojian="h.num >0 and h.num <= 1000";
//			}else if(shujv.equals("1000-2000")==true){
//				tiaojian="h.num > 1000 and h.num <= 2000";
//			}else if(shujv.equals("2000-3000")==true){
//				tiaojian="h.num > 2000 and h.num <= 3000";
//			}else if(shujv.equals("3000-4000")==true){
//				tiaojian="h.num > 3000 and h.num <= 4000";
//			}else if(shujv.equals("4000-5000")==true){
//				tiaojian="h.num > 4000 and h.num <= 5000";
//			}else if(shujv.equals("5000及以上")==true){
//				tiaojian="h.num > 5000 ";
//			}
			
//			if (shilevel==1) {										//判断区级
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v3 FROM da_household"+year+" where v2 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}else if (shilevel==2) {
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v4 as v3 FROM da_household"+year+" where v3 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}else if (shilevel==3) {
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v5 as v3 FROM da_household"+year+" where v4 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}else if (shilevel==4) {
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v5 as v3 FROM da_household"+year+" where v5 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}
			
			if (shilevel==1) {										//判断区级
				sql="SELECT pkid,v9 FROM da_household"+year+" where v2 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"' group by pkid";
			}else if (shilevel==2) {
				sql="SELECT pkid,v9 FROM da_household"+year+" where v3 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"' group by pkid";
			}else if (shilevel==3) {
				sql="SELECT pkid,v9 FROM da_household"+year+" where v4 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"' group by pkid";
			}else if (shilevel==4) {
				sql="SELECT pkid,v9 FROM da_household"+year+" where v5 like '%"+ shi_name +"%' and  sys_standard='"+ leixing_name +"' group by pkid";
			}
			
//			if (com_level.equals("1")) {	//当用户层级为1时
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num ,v3 FROM da_household"+year+" where  sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}else if (com_level.equals("2")) {	//当用户层级为2时
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v4 as v3 FROM da_household"+year+" where v3='"+ com_name +"' and v3= sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}else if (com_level.equals("3")) {	//当用户层级为3时
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v5 as v3 FROM da_household"+year+" where v4='"+ com_name +"' and v3= sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}else {	//当用户层级为4时
//				com_name=company_json.get("xiang").toString();	//获取上级用户
//				sql="select v3,count(*) as count from (SELECT ROUND(v24,2) as num , v5 as v3 FROM da_household"+year+" where v4='"+ com_name +"' and v3= sys_standard='"+ leixing_name +"') h where "+tiaojian+" group by v3";
//			}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			StringBuilder pkids=new StringBuilder();
			List<Integer> familysize_list=new ArrayList<Integer>();//一户的家庭人数
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				for(int i=0;i<sql_list.size();i++){
					if(i==sql_list.size()-1){
						pkids.append(sql_list.get(i).get("pkid").toString());
					}else{
						pkids.append(sql_list.get(i).get("pkid").toString()+",");
					}
					familysize_list.add((Integer) sql_list.get(i).get("v9"));
				}
				//帮扶后总收入
				String dqsrh_sql2="SELECT da_household_id,v39 FROM da_helpback_income"+year+" where da_household_id in("+pkids+")";
				SQLAdapter dqsrh_sqlAdapter2 =new SQLAdapter(dqsrh_sql2);
				List<Map> dqsrh_list2=getBySqlMapper.findRecords(dqsrh_sqlAdapter2);
				//帮扶后总支出
				String dqzch_sql2="SELECT da_household_id,v31 FROM da_helpback_expenditure"+year+" where da_household_id in("+pkids+")";
				SQLAdapter dqzch_sqlAdapter2 =new SQLAdapter(dqzch_sql2);
				List<Map> dqzch_list2=getBySqlMapper.findRecords(dqzch_sqlAdapter2);
				StringBuilder pkids2=new StringBuilder();
				for(int y=0;y<dqzch_list2.size();y++){
					int familysize=familysize_list.get(y);
					Map dqsrh_map2=dqsrh_list2.get(y);
					Map dqzch_map2=dqzch_list2.get(y);
					float dqsrh2=(float) (dqsrh_map2.get("v39")==null?0.00:(Float)dqsrh_map2.get("v39"));
					float dqzch2=(float) (dqzch_map2.get("v31")==null?0.00:(Float)dqzch_map2.get("v31"));
					float year_income2=dqsrh2-dqzch2;//年纯收入
					float per_capita_income2=year_income2/familysize;//人均纯收入
				if(shujv.equals("0-1000")==true){	//判断筛选金额
					if (per_capita_income2>0 && per_capita_income2<=1000) {
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(shujv.equals("1000-2000")==true){
					if (per_capita_income2>1000 && per_capita_income2<=2000) {
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(shujv.equals("2000-3000")==true){
					if (per_capita_income2>2000 && per_capita_income2<=3000) {
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(shujv.equals("3000-4000")==true){
					if (per_capita_income2>3000 && per_capita_income2<=4000) {
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(shujv.equals("4000-5000")==true){
					if (per_capita_income2>4000 && per_capita_income2<=5000) {
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(shujv.equals("5000及以上")==true){
					if (per_capita_income2>5000 ) {
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}
					
				}
				if(pkids.toString().length()>0){
				String sql2="";
				if (shilevel==1) {										//判断区级
					sql2="select v3,count(*) as count from (SELECT  v3 FROM da_household"+year+" where pkid in ("+pkids2.substring(0, pkids2.lastIndexOf(","))+ ") ) h  group by v3";
				}else if (shilevel==2) {
					sql2="select v3,count(*) as count from (SELECT  v4 as v3 FROM da_household"+year+" where pkid in ("+pkids2.substring(0, pkids2.lastIndexOf(","))+ ") ) h  group by v3";
				}else if (shilevel==3) {
					sql2="select v3,count(*) as count from (SELECT v5 as v3 FROM da_household"+year+" where pkid in ("+pkids2.substring(0, pkids2.lastIndexOf(","))+ ") ) h  group by v3";
				}else if (shilevel==4) {
					sql2="select v3,count(*) as count from (SELECT v5 as v3 FROM da_household"+year+" where pkid in ("+pkids2.substring(0, pkids2.lastIndexOf(","))+ ") ) h  group by v3";
				}
				SQLAdapter sqlAdapter2 =new SQLAdapter(sql2);
				List<Map> sql_list2 = this.getBySqlMapper.findRecords(sqlAdapter2);
				for(int i = 0;i<sql_list2.size();i++){
					JSONObject val = new JSONObject();
					Map Admin_st_map = sql_list2.get(i);
					for (Object key : Admin_st_map.keySet()) {
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				}else{
					JSONObject val = new JSONObject();
					val.put("com_level", shilevel);
					val.put("com_code2", com_code2);
					val.put("com_code3", com_code3);
					val.put("isvalue", "0");
					jsa.add(val);
				}
			}else{
				JSONObject val = new JSONObject();
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().print(jsa.toString());
			return null;	
		}else if(mokuai_name.equals("lsbfcs")) { 
			//根据模块名称来判断执行模块 落实帮扶比例模块
			if (shilevel==1) {	//当层级为1时
//				sql="select b2,b12,t2.v3 as b1 ,(b2-b12) as v3 from sys_company t1  "+
//						   "right join (select v3,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3) t2 on t1.com_name=t2.v3 "+
//						   "right join (select v3,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  GROUP BY v3  ) t12 on t1.com_name=t12.v3  "+
//						   "where  t2.v3=t12.v3  ORDER BY t1.com_name";
				sql="SELECT t2.v3 as b1,t2.b2 as b2,t12.b12 as b12,(t2.b2-t12.b12) as b3 from "+ 
						  " (select v3,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v3 ORDER BY v3) t2 "+ 
						  " left join (select v3,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  group by v3 ORDER BY v3) t12 on t2.v3=t12.v3 ";
			}else if (shilevel==2) {	//当层级为2时
//				sql="select b2,b12,t2.v4 as b1 ,(b2-b12) as v3 from sys_company t1  "+
//						   "left join (select v4,v3,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v4) t2 on t1.com_name=t2.v3 "+
//						   "left join (select v4,v3,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  GROUP BY v4  ) t12 on t1.com_name=t12.v3  "+
//						   "where t2.v3 like '%"+ shi_name +"%' and t2.v3=t12.v3 and  com_f_pkid=4  ORDER BY t1.com_name";
				sql="SELECT t2.v4 as b1,t2.b2 as b2,t12.b12 as b12,(t2.b2-t12.b12) as b3 from "+ 
						  " (select v4,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"' and v3 like '%"+shi_name+"%'  group by v4 ORDER BY v4) t2 "+ 
						  " left join (select v4,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"' and v3 like '%"+shi_name+"%' group by v4 ORDER BY v4) t12 on t2.v4=t12.v4 ";
			}else if (shilevel==3) {	//当层级为3时
//				sql="select b2,b12,t2.v5 as b1,(b2-b12) as v3 from sys_company t1  "+
//						   "left join (select  v5,v4,v3,v2,COUNT(*) as b2,sum(v9) as b3 from da_household"+year+" where sys_standard='"+ leixing_name +"'  group by v5) t2 on t1.com_name=t2.v4  "+
//						   "left join (select v5,v3,v4,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"'  GROUP BY v5  ) t12 on t1.com_name=t12.v4  "+
//						   "where t2.v4 like '%"+ shi_name +"%' and t2.v4=t12.v4    ORDER BY t1.com_name";
				sql="SELECT t2.v5 as b1,t2.b2 as b2,t12.b12 as b12,(t2.b2-t12.b12) as b3 from "+ 
						  " (select v5,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"' and v4 like '%"+shi_name+"%'  group by v5 ORDER BY v5) t2 "+ 
						  " left join (select v5,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"' and v4 like '%"+shi_name+"%' group by v5 ORDER BY v5) t12 on t2.v5=t12.v5 ";
			}else if (shilevel==4) {	//当层级为3时
				sql="SELECT t2.v5 as b1,t2.b2 as b2,t12.b12 as b12,(t2.b2-t12.b12) as b3 from "+ 
				  " (select v5,COUNT(*) as b2 from da_household"+year+" where sys_standard='"+ leixing_name +"' and v5 like '%"+shi_name+"%'  group by v5 ORDER BY v5) t2 "+ 
				  " left join (select v5,COUNT(*) as b12 from b12_t"+year+" where sys_standard='"+ leixing_name +"' and v5 like '%"+shi_name+"%' group by v5 ORDER BY v5) t12 on t2.v5=t12.v5 ";
	}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				
				for(int i = 0;i<sql_list.size();i++){
					Map bangfu_st_map = sql_list.get(i);
					JSONObject val = new JSONObject();
					for (Object key : bangfu_st_map.keySet()) {
						val.put("name", bangfu_st_map.get("b1"));
						int  LSWC=0;
						if(bangfu_st_map.keySet().contains("b12")){
							LSWC=Integer.parseInt( bangfu_st_map.get("b12").toString().trim());
						}
						int  LSWWC=0;
						if(bangfu_st_map.keySet().contains("b3")){
							LSWWC=Integer.parseInt( bangfu_st_map.get("b3").toString().trim());
						}else{
							LSWWC=Integer.parseInt( bangfu_st_map.get("b2").toString().trim());
						}
						if (shujv.equals("落实完成")) {
							if(LSWC>0){
								val.put("value", LSWC);
							}
							
						}else {
							if(LSWWC>0){
								val.put("value", LSWWC);
							}
							
						}
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				
			}else{
				JSONObject val = new JSONObject();
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else if (mokuai_name.equals("zpyy")) {	//根据模块名称来判断执行模块 致贫原因模块
			if (shilevel==1) {
				if(shujv==""){//文化程度为空
					sql="select t1.huv3 AS v3,(t1.hu+t2.jia) as count from(select v3 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v23 = '"+shujv+"' AND v2='鄂尔多斯市' group by v3) t1 join (select y.v3 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND y.v23 = '"+shujv+"' AND y.v2='鄂尔多斯市' group by y.v3)t2 on t1.huv3=t2.jiav3 ";
				}else{
					sql="select t1.huv3 AS v3,(t1.hu+t2.jia) as count from(select y1.com_name as huv3 ,IFNULL((ling+hu),0) as hu from (select com_name,'0' as ling from sys_company where com_level=2 group by com_name) y1 left join (select v3 as huv3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v23 LIKE '%"+shujv+"%' AND v2='鄂尔多斯市' group by v3) y2 on y1.com_name=y2.huv3) t1 join (select y1.com_name as jiav3,IFNULL((ling+jia),0) as jia from (select com_name,'0' as ling from sys_company where com_level=2 group by com_name) y1 left join (select y.v3 as jiav3,count(*) as jia  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and y.v23 LIKE '%"+shujv+"%' AND y.v2='鄂尔多斯市' group by y.v3) y2 on y1.com_name=y2.jiav3)t2 on t1.huv3=t2.jiav3 ";
				}
				//sql="SELECT v3,count(v9)AS count FROM da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v23 LIKE '%"+shujv+"%' and v2='鄂尔多斯市' GROUP BY v3";
			}else if (shilevel==2) {
				sql="SELECT a1.v3 AS v3,ifnull((a1.hu+a2.hu),0)AS count FROM(SELECT b1.v3 as v3,b2.hu as hu FROM(SELECT v4 AS v3,0 FROM da_household"+year+" WHERE v3='"+shi_name+"' GROUP BY v4)b1 LEFT JOIN (select v4 as v3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v3='"+shi_name+"' and v23  like '%"+shujv+"%'  group by v4)b2 ON b1.v3=b2.v3 )a1 JOIN(select y.v4 as v3,count(*) as hu  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v3='"+shi_name+"' and y.v23 like '%"+shujv+"%' group by y.v4)a2 on a1.v3=a2.v3";
				//sql="SELECT v4 as v3,count(v9)AS count FROM da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v23 LIKE '%"+shujv+"%' and v3='"+shi_name+"' GROUP BY v4";
			}else if (shilevel==3) {
				sql="SELECT a1.v3 AS v3,ifnull((a1.hu+a2.hu),0)AS count FROM(SELECT b1.v3 as v3,b2.hu as hu FROM(SELECT v5 AS v3,0 FROM da_household"+year+" WHERE v4='"+shi_name+"' GROUP BY v5)b1 LEFT JOIN (select v5 as v3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v4='"+shi_name+"' and v23  like '%"+shujv+"%'  group by v5)b2 ON b1.v3=b2.v3 )a1 JOIN(select y.v5 as v3,count(*) as hu  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v4='"+shi_name+"' and y.v23 like '%"+shujv+"%' group by y.v5)a2 on a1.v3=a2.v3";
				//sql="SELECT v5 as v3,count(v9)AS count FROM da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v23 LIKE '%"+shujv+"%' and v4='"+shi_name+"' GROUP BY v5";
			}else {
				sql="SELECT a1.v3 AS v3,ifnull((a1.hu+a2.hu),0)AS count FROM(SELECT b1.v3 as v3,b2.hu as hu FROM(SELECT v5 AS v3,0 FROM da_household"+year+" WHERE v5='"+shi_name+"' GROUP BY v5)b1 LEFT JOIN (select v5 as v3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v5='"+shi_name+"' and v23  like '%"+shujv+"%'  group by v5)b2 ON b1.v3=b2.v3 )a1 JOIN(select y.v5 as v3,count(*) as hu  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v5='"+shi_name+"' and y.v23 like '%"+shujv+"%' group by y.v5)a2 on a1.v3=a2.v3";
				//com_name=company_json.get("xiang").toString();	//获取上级用户
				//sql="SELECT v5 as v3,count(v9)AS count FROM da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v23 LIKE '%"+shujv+"%' and v5='"+shi_name+"' GROUP BY v5";
			}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				for(int i = 0;i<sql_list.size();i++){
					JSONObject val = new JSONObject();
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				
			}else{
				JSONObject val = new JSONObject();
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else if (mokuai_name.equals("whcd")) {	//根据模块名称来判断执行模块 文化程度模块
			if (shilevel==1) {
				if(shujv==""){//文化程度为空
					sql="select t1.huv3 AS v3,(if(isnull(t1.hu),0,t1.hu)+if(isnull(t2.jia),0,t2.jia)) as count from(select v3 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v12 = '"+shujv+"' AND v2='鄂尔多斯市' group by v3) t1 join (select y.v3 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND y.v12 = '"+shujv+"' AND y.v2='鄂尔多斯市' group by y.v3)t2 on t1.huv3=t2.jiav3 ";
				}else{
					sql="select t1.huv3 AS v3,(if(isnull(t1.hu),0,t1.hu)+if(isnull(t2.jia),0,t2.jia)) as count from(select y1.com_name as huv3 ,IFNULL((ling+hu),0) as hu from (select com_name,'0' as ling from sys_company where com_level=2 group by com_name) y1 left join (select v3 as huv3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v12 LIKE '%"+shujv+"%' AND v2='鄂尔多斯市' group by v3) y2 on y1.com_name=y2.huv3) t1 join (select y1.com_name as jiav3,IFNULL((ling+jia),0) as jia from (select com_name,'0' as ling from sys_company where com_level=2 group by com_name) y1 left join (select y.v3 as jiav3,count(*) as jia  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and y.v12 LIKE '%"+shujv+"%' AND y.v2='鄂尔多斯市' group by y.v3) y2 on y1.com_name=y2.jiav3)t2 on t1.huv3=t2.jiav3 ";
				}
			}else if (shilevel==2) {
					sql="SELECT a1.v3,(if(isnull(a1.hu),0,a1.hu)+if(isnull(a2.hu),0,a2.hu)) AS count FROM(SELECT b1.v3 as v3,b2.hu as hu FROM(SELECT v4 AS v3,0 FROM da_household"+year+" WHERE v3 like '%"+shi_name+"%' GROUP BY v4)b1 LEFT JOIN (select v4 as v3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v3 like '%"+shi_name+"%' and v12 ='"+shujv+"'  group by v4)b2 ON b1.v3=b2.v3 )a1 JOIN(select y.v4 as v3,count(*) as hu  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v3 like '%"+shi_name+"%' and y.v12 = '"+shujv+"' group by y.v4)a2 on a1.v3=a2.v3";
			}else if (shilevel==3) {
					sql="SELECT a1.v3,(if(isnull(a1.hu),0,a1.hu)+if(isnull(a2.hu),0,a2.hu)) AS count FROM(SELECT b1.v3 as v3,b2.hu as hu FROM(SELECT v5 AS v3,0 FROM da_household"+year+" WHERE v4 like '%"+shi_name+"%' GROUP BY v5)b1 LEFT JOIN (select v5 as v3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v4 like '%"+shi_name+"%' and v12 ='"+shujv+"' group by v5)b2 ON b1.v3=b2.v3 )a1  LEFT JOIN(select y.v5 as v3,count(*) as hu  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v4 like '%"+shi_name+"%' and y.v12 ='"+shujv+"' group by y.v5)a2 on a1.v3=a2.v3";
			}else {
					//com_name=company_json.get("xiang").toString();
					sql="SELECT a1.v3,(if(isnull(a1.hu),0,a1.hu)+if(isnull(a2.hu),0,a2.hu)) AS count FROM(SELECT b1.v3 as v3,b2.hu as hu FROM(SELECT v5 AS v3,0 FROM da_household"+year+" WHERE v5 like '%"+shi_name+"%' GROUP BY v5)b1 LEFT JOIN (select v5 as v3,count(*) as hu  from da_household"+year+" WHERE sys_standard='"+leixing_name+"' and v5 like '%"+shi_name+"%' and v12 ='"+shujv+"' group by v5)b2 ON b1.v3=b2.v3 )a1  LEFT JOIN(select y.v5 as v3,count(*) as hu  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v5 like '%"+shi_name+"%' and y.v12 ='"+shujv+"' group by y.v5)a2 on a1.v3=a2.v3";
			}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					JSONObject val = new JSONObject();
					for (Object key : Admin_st_map.keySet()) {
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				
			}else{
				JSONObject val = new JSONObject();
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else if (mokuai_name.equals("nljg")) {	//根据模块名称来判断执行模块 年龄结构模块
			String ditu=null;
			Calendar cal = Calendar.getInstance();
			int time1=cal.get(Calendar.YEAR);
			if(shujv.equals("10岁以下")==true){
				ditu="((x.nnum>="+(time1-10)+")and(x.nnum<"+time1+"))";
			}else if(shujv.equals("10~20岁")==true){
				ditu="((x.nnum>="+(time1-20)+")and(x.nnum<"+(time1-10)+"))";
			}else if(shujv.equals("20~30岁")==true){
				ditu="((x.nnum>="+(time1-30)+")and(x.nnum<"+(time1-20)+"))";
			}else if(shujv.equals("30~40岁")==true){
				ditu="((x.nnum>="+(time1-40)+")and(x.nnum<"+(time1-30)+"))";
			}else if(shujv.equals("40~50岁")==true){
				ditu="((x.nnum>="+(time1-50)+")and(x.nnum<"+(time1-40)+"))";
			}else if(shujv.equals("50岁及以上")==true){
				ditu="((x.nnum>="+(time1-60)+")and(x.nnum<"+(time1-50)+"))";
			}else if(shujv.equals("60岁及以上")==true){
				ditu="(x.nnum<"+(time1-60)+")";
			}
			if (shilevel==1) {										//判断区级
				sql="select v3,count(*)as count from (select v3 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v2 like '%"+ shi_name +"%' AND sys_standard='"+leixing_name+"' UNION ALL select y.v3 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and y.v2 like '%"+ shi_name +"%' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
			}else if (shilevel==2) {
				sql="select v3,count(*)as count from (select v4 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v3 like '%"+ shi_name +"%' AND sys_standard='"+leixing_name+"' UNION ALL select y.v4 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and y.v3 like '%"+ shi_name +"%' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
			}else if (shilevel==3) {
				sql="select v3,count(*)as count from (select v5 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v4 like '%"+ shi_name +"%' AND sys_standard='"+leixing_name+"' UNION ALL select y.v5 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and y.v4 like '%"+ shi_name +"%' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
			}
			else if (shilevel==4) {
				sql="select v3,count(*)as count from (select v5 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v5 like '%"+ shi_name +"%' AND sys_standard='"+leixing_name+"' UNION ALL select y.v5 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and y.v5 like '%"+ shi_name +"%' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
			}
//			if(com_level.equals("1")){//层级为1的用户
//				sql="select v3,count(*)as count from (select v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) AND sys_standard='"+leixing_name+"'UNION ALL select y.v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
//			}else if(com_level.equals("2")){//层级为2的用户
//				sql="select v3,count(*)as count from (select v4 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v3='"+com_name+"' AND sys_standard='"+leixing_name+"' UNION ALL select y.v4 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v3='"+com_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
//			}else if(com_level.equals("3")){//层级为3的用户
//				sql="select v3,count(*)as count from (select v5 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v4='"+com_name+"' AND sys_standard='"+leixing_name+"' UNION ALL select y.v5 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v4='"+com_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
//			}else if(com_level.equals("4")){//层级为4的用户
//				com_name=company_json.get("xiang").toString();//获取上级用户名称
//				sql="select v3,count(*)as count from (select v5 as v3,substring(v8,7,4) as nnum from da_household"+year+" where (CHAR_LENGTH(v8)=18 OR CHAR_LENGTH(v8)=20) and v4='"+com_name+"' AND sys_standard='"+leixing_name+"' UNION ALL select y.v5 as v3,substring(y.v8,7,4) as nnum  from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' and x.v4='"+com_name+"' and (CHAR_LENGTH(y.v8)=18 OR CHAR_LENGTH(y.v8)=20)) x WHERE "+ditu+" group by v3";
//			}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				
			}else{
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else if (mokuai_name.equals("jtgm")) { //根据模块名称来判断执行模块 家庭规模模块
			if (shilevel==1) {
				sql="SELECT v3,COUNT(v9) AS count FROM da_household"+year+" WHERE v9='"+shujv+"'and sys_standard='"+leixing_name+"' and v2 like '%"+shi_name+"%' GROUP BY v3";
			}else if (shilevel==2) {
				sql="SELECT v4 as v3,COUNT(v9) AS count FROM da_household"+year+" WHERE v9='"+shujv+"'and sys_standard='"+leixing_name+"' and v3 like '%"+ shi_name +"%' GROUP BY v4";	
			}else if (shilevel==3) {
				sql="SELECT v5 as v3 ,COUNT(v9) AS count FROM da_household"+year+" WHERE v9='"+shujv+"'and sys_standard='"+leixing_name+"' and v4 like '%"+ shi_name +"%' GROUP BY v5";
			}else if (shilevel==4) {
				sql="SELECT v5 as v3 ,COUNT(v9) AS count FROM da_household"+year+" WHERE v9='"+shujv+"'and sys_standard='"+leixing_name+"' and v5 like '%"+ shi_name +"%' GROUP BY v5";
			}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				
				for(int i = 0;i<sql_list.size();i++){
					JSONObject val = new JSONObject();
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				
			}else{
				JSONObject val = new JSONObject();
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else if (mokuai_name.equals("bffzr")) { //根据模块名称来判断执行模块 帮扶负责人模块
			
			if(shilevel==1){//当层级为1的时候
				sql="SELECT b2.v3,COUNT(*)as count FROM("
						+ "SELECT a2.* FROM sys_personal"+year+" a1 INNER JOIN sys_personal_household_many"+year+" a2 ON  a1.pkid=a2.sys_personal_id "
						+ ")b1 INNER JOIN da_household"+year+" b2 ON b1.da_household_id = b2.pkid WHERE b2.sys_standard='"+leixing_name+"' and b2.v2='鄂尔多斯市' GROUP BY b2.v3";
			}else if(shilevel==2){//当层级为2的时候
				sql="SELECT b2.v4 as v3,COUNT(*)as count FROM("
						+ "SELECT a2.* FROM sys_personal"+year+" a1 INNER JOIN sys_personal_household_many"+year+" a2 ON  a1.pkid=a2.sys_personal_id "
						+ ")b1 INNER JOIN da_household"+year+" b2 ON b1.da_household_id = b2.pkid  WHERE b2.sys_standard='"+leixing_name+"' AND b2.v3='"+shi_name+"' GROUP BY b2.v4";
			}else if(shilevel==3){//当层级为3的时候
				sql="SELECT b2.v5 as v3,COUNT(*)as count FROM("
						+ "SELECT a2.* FROM sys_personal"+year+" a1 INNER JOIN sys_personal_household_many"+year+" a2 ON  a1.pkid=a2.sys_personal_id "
						+ ")b1 INNER JOIN da_household"+year+" b2 ON b1.da_household_id = b2.pkid  WHERE b2.sys_standard='"+leixing_name+"' AND b2.v4='"+shi_name+"' GROUP BY b2.v5";
			}else if(shilevel==4){//当层级为4的时候
				sql="SELECT b2.v5 as v3,COUNT(*)as count FROM("
						+ "SELECT a2.* FROM sys_personal"+year+" a1 INNER JOIN sys_personal_household_many"+year+" a2 ON  a1.pkid=a2.sys_personal_id "
						+ ")b1 INNER JOIN da_household"+year+" b2 ON b1.da_household_id = b2.pkid  WHERE b2.sys_standard='"+leixing_name+"' AND b2.v5='"+shi_name+"' GROUP BY b2.v5";
			}
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONArray jsa=new JSONArray();
			if(sql_list.size()>0){
				for(int i = 0;i<sql_list.size();i++){
					JSONObject val = new JSONObject();
					Map Admin_st_map = sql_list.get(i);
					for (Object key : Admin_st_map.keySet()) {
						val.put("name", Admin_st_map.get("v3"));
						val.put("value", Admin_st_map.get("count"));
						val.put("com_level", shilevel);
						val.put("com_code2", com_code2);
						if(shilevel==4){
							val.put("com_code3", com_code3);
						}
					}
					jsa.add(val);
				}
				
			}else{
				JSONObject val = new JSONObject();
				val.put("com_level", shilevel);
				val.put("com_code2", com_code2);
				val.put("com_code3", com_code3);
				val.put("isvalue", "0");
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else if (mokuai_name.equals("zcgzd")) {
			if(shilevel==1){//当层级为1的时候
				sql="SELECT d1.count,d2.com_name FROM ("
						+ "SELECT c1.sys_company_id,COUNT(*) count FROM ("
						+ "SELECT * FROM (SELECT v5,sys_company_id from  da_company"+year+" WHERE v5 IS NOT NULL"
						+ ")a1 INNER JOIN sys_company a2 ON (a1.v5=a2.pkid)OR(a1.v5=a2.com_name) GROUP BY a2.com_name"
						+ ")c1 GROUP BY c1.sys_company_id "
						+ ")d1 LEFT JOIN sys_company d2 ON d1.sys_company_id=d2.pkid";
			}else if(shilevel==2){//当层级为2的时候
				sql="SELECT d2.com_name,d1.count FROM ("
						+ "SELECT c1.com_f_pkid,COUNT(*) count FROM ("
						+ "SELECT * FROM ("
						+ "SELECT v5 from  da_company"+year+" WHERE v5 IS NOT NULL"
						+ ")a1 INNER JOIN sys_company a2 ON (a1.v5=a2.pkid)OR(a1.v5=a2.com_name) WHERE a2.com_f_pkid IN("
						+ "SELECT pkid FROM sys_company WHERE com_f_pkid='"+com_f_pkid+"') GROUP BY a2.com_name"
						+ ")c1 GROUP BY c1.com_f_pkid"
						+ ")d1 LEFT JOIN sys_company d2 ON d1.com_f_pkid=d2.pkid";
			}else if(shilevel==3){//当层级为3的时候
				sql="SELECT * FROM ("
						+ "SELECT v5 from  da_company"+year+" WHERE v5 IS NOT NULL)a1 INNER JOIN sys_company a2 ON (a1.v5=a2.pkid)OR(a1.v5=a2.com_name) "
						+ "WHERE a2.com_f_pkid ='"+com_f_pkid+"' GROUP BY a2.com_name ";
			}else if(shilevel==4){//当层级为4的时候
				com_name=company_json.get("xiang").toString();//获取上级用户名称
				com_pkid = company_json.get("xiang_id").toString();//获取上级用户id
				sql="SELECT * FROM ("
						+ "SELECT v5 from  da_company"+year+" WHERE v5 IS NOT NULL)a1 INNER JOIN sys_company a2 ON (a1.v5=a2.pkid)OR(a1.v5=a2.com_name) "
						+ "WHERE a2.com_f_pkid ='"+com_f_pkid+"' GROUP BY a2.com_name ";
			}
			if(shilevel==1||shilevel==2){
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONArray jsa=new JSONArray();
				if(sql_list.size()>0){
					for(int i = 0;i<sql_list.size();i++){//由于其中有存在鄂尔多斯的数据
						JSONObject val = new JSONObject();
						Map Admin_st_map = sql_list.get(i);
						if(Admin_st_map.get("com_name").toString().equals("level-1")){

						}else{
							for (Object key : Admin_st_map.keySet()) {
								val.put("name", Admin_st_map.get("com_name"));
								val.put("value", Admin_st_map.get("count"));
								val.put("com_level", shilevel);
								val.put("com_code2", com_code2);
								if(shilevel==4){
									val.put("com_code3", com_code3);
								}
							}
							jsa.add(val);
						}
					}
					
				}else{
					JSONObject val = new JSONObject();
					val.put("com_level", shilevel);
					val.put("com_code2", com_code2);
					val.put("com_code3", com_code3);
					val.put("isvalue", "0");
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}
			if(shilevel==3||shilevel==4){
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				
				JSONArray jsa=new JSONArray();
				if(sql_list.size()>0){
					
					for(int i = 0;i<sql_list.size();i++){
						JSONObject val = new JSONObject();
						Map Admin_st_map = sql_list.get(i);
						for (Object key : Admin_st_map.keySet()) {
							val.put("name", Admin_st_map.get("com_name"));
							val.put("value", 1);
							val.put("com_level", shilevel);
							val.put("com_code2", com_code2);
							if(shilevel==4){
								val.put("com_code3", com_code3);
							}
						}
						jsa.add(val);
					}
					
				}else{
					JSONObject val = new JSONObject();
					val.put("com_level", shilevel);
					val.put("com_code2", com_code2);
					val.put("com_code3", com_code3);
					val.put("isvalue", "0");
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}
		}
		return null;
	}
	
	/**
	 * H3 页面挂图计划优化 条形图
	 * @param request
	 * @param response
	 * @return
	 * @author 呼树明
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public ModelAndView getPicture_Line(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String level=request.getParameter("level");
		String jsonname=request.getParameter("jsonname");
		String json_level= request.getParameter("jsonlevel");
		int shilevel=0;
		String shi_name="";
		if (json_level==null) {
			String shi_sql ="select * from sys_company c where c.com_level='"+ level +"' and c.com_name='"+jsonname+"'";
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
		
		String leixing_name = request.getParameter("leixing");//获取国家级还是市级的贫困户
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "" ;
		}
		String sql="";
		JSONArray json= new JSONArray();
		if(shilevel==1){//市级用户
			sql="SELECT c1.v3 as v3,IFNULL(c2.jk,0)AS jk,IFNULL(c2.jb ,0)AS jb FROM(SELECT v3,0 FROM da_household"+year+"  GROUP BY v3)c1 LEFT JOIN(SELECT a1.v3 as v3,IFNULL(a1.count,0) as jb,IFNULL(a2.count2,0) as jk FROM (select x1.huv3 AS v3,(x1.hu+x2.jia) as count from(select v3 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v14 !='健康' and v2 like '%"+shi_name+"%' group by v3) x1 join (select y.v3 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND y.v14 !='健康' and y.v2 like '%"+shi_name+"%' group by y.v3)x2 on x1.huv3=x2.jiav3) a1 LEFT JOIN (select x1.huv3 AS v32,(x1.hu+x2.jia) as count2 from(select v3 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v14 ='健康' and v2 like '%"+shi_name+"%' group by v3) x1 join (select y.v3 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND y.v14 ='健康' and y.v2 like '%"+shi_name+"%' group by y.v3)x2 on x1.huv3=x2.jiav3) a2 ON a1.v3 = a2.v32)c2 on c1.v3=c2.v3";
		}else if(shilevel==2){//旗县用户
			
//			String sql1 = "select count(*) jk,b.v4 from (select * from da_household"+year+" where v3 like '%"+shi_name+"%' and sys_standard='"+leixing_name+"') a left JOIN "+
//						" (select * from da_member"+year+" where  v3 like '%"+shi_name+"%') b on a.pkid=b.da_household_id where b.v14='健康' group by b.v4 ";
//			String sql2 = " select count(*) jk,v4 from da_household"+year+" where v3 like '%"+shi_name+"%' and sys_standard='"+leixing_name+"' and v14='健康' GROUP BY v4 ";
//			String sql3 = "select count(*) jb,b.v4 from (select * from da_household"+year+" where v3 like '%"+shi_name+"%' and sys_standard='"+leixing_name+"') a left JOIN "+
//						" (select * from da_member"+year+" where  v3 like'%"+shi_name+"%') b on a.pkid=b.da_household_id where b.v14!='健康' group by b.v4 ";
//			String sql4 = " select count(*) jb,v4 from da_household"+year+" where  v3 like'%"+shi_name+"%' and sys_standard='"+leixing_name+"' and v14!='健康' GROUP BY v4";
//			SQLAdapter sqlAdapter1 = new SQLAdapter(sql1);
//			SQLAdapter sqlAdapter2 = new SQLAdapter(sql2);
//			SQLAdapter sqlAdapter3 = new SQLAdapter(sql3);
//			SQLAdapter sqlAdapter4 = new SQLAdapter(sql4);
//			List<Map> list1 = this.getBySqlMapper.findRecords(sqlAdapter1);
//			List<Map> list2 = this.getBySqlMapper.findRecords(sqlAdapter2);
//			List<Map> list3 = this.getBySqlMapper.findRecords(sqlAdapter3);
//			List<Map> list4= this.getBySqlMapper.findRecords(sqlAdapter4);
//			for ( int  i = 0 ; i<list1.size(); i++ ) {
//				JSONObject obj = new JSONObject();
//				obj.put("name",list1.get(i).get("v4"));
//				String str ="";
//				if(list2.size()>i){
//					str = list2.get(i).get("jk").toString();
//				}else {
//					str="0";
//				}
//				int jk= Integer.parseInt(str)+Integer.parseInt(list1.get(i).get("jk").toString());
//				obj.put("jk",jk);
//				String str4 ="";
//				String str3="";
//				if(list4.size()>i){
//					str4= list4.get(i).get("jb").toString();
//				}else{
//					str4="0";
//				}
//				if(list3.size()>i){
//					str3= list3.get(i).get("jb").toString();
//				}else{
//					str3="0";
//				}
//				int jb= Integer.parseInt(str3)+Integer.parseInt(str4);
//				obj.put("jb",jb);
//				json.add(obj);
//			}
//			
//			response.getWriter().write(json.toString());
//			return  null;
			sql="SELECT c1.v3 as v3,IFNULL(c2.jk,0)AS jk,IFNULL(c2.jb ,0)AS jb FROM(SELECT v4 as v3,0 FROM da_household"+year+" WHERE v3 like '%"+shi_name+"%' GROUP BY v4)c1 LEFT JOIN(SELECT a1.v3 as v3,IFNULL(a1.count,0) as jb,IFNULL(a2.count2,0) as jk FROM (select x1.huv3 AS v3,(x1.hu+x2.jia) as count from(select v4 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v3 like '%"+shi_name+"%' AND v14 !='健康' group by v4) x1 join (select y.v4 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND x.v3  like '%"+shi_name+"%' AND y.v14 !='健康' group by y.v4)x2 on x1.huv3=x2.jiav3) a1 LEFT JOIN (select x1.huv3 AS v32,(x1.hu+x2.jia) as count2 from(select v4 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v3 like '%"+shi_name+"%' AND v14 ='健康' group by v4) x1 join (select y.v4 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND x.v3 like '%"+shi_name+"%' AND y.v14 ='健康' group by y.v4)x2 on x1.huv3=x2.jiav3) a2 ON a1.v3 = a2.v32)c2 on c1.v3=c2.v3";
		}else if(shilevel==3){//乡级用户
			sql="SELECT c1.v3 as v3,IFNULL(c2.jk,0)AS jk,IFNULL(c2.jb ,0)AS jb FROM(SELECT v5 as v3,0 FROM da_household"+year+" WHERE v4 like '%"+shi_name+"%' GROUP BY v5)c1 LEFT JOIN(SELECT a1.v3 as v3,IFNULL(a1.count,0) as jb,IFNULL(a2.count2,0) as jk FROM (select x1.huv3 AS v3,(x1.hu+x2.jia) as count from(select v5 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v4  like '%"+shi_name+"%' AND v14 !='健康' group by v5) x1 join (select y.v5 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND x.v4 like '%"+shi_name+"%' AND y.v14 !='健康' group by y.v5)x2 on x1.huv3=x2.jiav3) a1 LEFT JOIN (select x1.huv3 AS v32,(x1.hu+x2.jia) as count2 from(select v5 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v4 like '%"+shi_name+"%' AND v14 ='健康' group by v5) x1 join (select y.v5 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND x.v4 like '%"+shi_name+"%' AND y.v14 ='健康' group by y.v5)x2 on x1.huv3=x2.jiav3) a2 ON a1.v3 = a2.v32)c2 on c1.v3=c2.v3";
		}else if(shilevel==4){//村级用户
			sql="SELECT c1.v3 as v3,IFNULL(c2.jk,0)AS jk,IFNULL(c2.jb ,0)AS jb FROM(SELECT v5 as v3,0 FROM da_household"+year+" WHERE v5 like '%"+shi_name+"%' GROUP BY v5)c1 LEFT JOIN(SELECT a1.v3 as v3,IFNULL(a1.count,0) as jb,IFNULL(a2.count2,0) as jk FROM (select x1.huv3 AS v3,(x1.hu+x2.jia) as count from(select v5 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v5 like  '%"+shi_name+"%' AND v14 !='健康' group by v5) x1 join (select y.v5 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND x.v5 like '%"+shi_name+"%' AND y.v14 !='健康' group by y.v5)x2 on x1.huv3=x2.jiav3) a1 LEFT JOIN (select x1.huv3 AS v32,(x1.hu+x2.jia) as count2 from(select v5 as huv3,count(*) as hu from da_household"+year+" WHERE sys_standard='"+leixing_name+"' AND v5 like '%"+shi_name+"%' AND v14 ='健康' group by v5) x1 join (select y.v5 as jiav3,count(*) as jia from da_household"+year+" x join da_member"+year+" y on x.pkid=y.da_household_id where x.sys_standard='"+leixing_name+"' AND x.v5 like '%"+shi_name+"%' AND y.v14 ='健康' group by y.v5)x2 on x1.huv3=x2.jiav3) a2 ON a1.v3 = a2.v32)c2 on c1.v3=c2.v3";
		}
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
		JSONObject val = new JSONObject();
		if(sql_list.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<sql_list.size();i++){
				Map Admin_st_map = sql_list.get(i);
				for (Object key : Admin_st_map.keySet()) {
					val.put("name", Admin_st_map.get("v3"));
					val.put("jk", Admin_st_map.get("jk"));
					val.put("jb", Admin_st_map.get("jb"));
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
	 * 获取行政区划树节点
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */public ModelAndView getincompleteTreeData2(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
//										JSONArray xiang_jsa = new JSONArray();
//										
//										for(int k = 0;k<tree_List.size();k++){
//											Map cun_map = tree_List.get(k);
//											if(cun_map.get("com_level").toString().equals("4")&&cun_map.get("com_f_pkid").toString().equals(xiang_map.get("pkid").toString())){//第四级，同时父id等于上一级id
//												JSONObject cun = new JSONObject();
//												cun.put("text", cun_map.get("com_name"));
//												cun.put("pkid", cun_map.get("pkid"));
//												xiang_jsa.add(cun);
//											}
//										}
										
										//xiang.put("nodes", xiang_jsa);
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
								//JSONArray xiang_jsa = new JSONArray();
								
//								for(int k = 0;k<tree_List.size();k++){
//									Map cun_map = tree_List.get(k);
//									if(cun_map.get("com_level").toString().equals("4")&&cun_map.get("com_f_pkid").toString().equals(xiang_map.get("pkid").toString())){//第四级，同时父id等于上一级id
//										JSONObject cun = new JSONObject();
//										cun.put("text", cun_map.get("com_name"));
//										cun.put("pkid", cun_map.get("pkid"));
//										xiang_jsa.add(cun);
//									}
//								}
								
								//xiang.put("nodes", xiang_jsa);
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
						//JSONArray xiang_jsa = new JSONArray();
						
//						for(int k = 0;k<tree_List.size();k++){
//							Map cun_map = tree_List.get(k);
//							if(cun_map.get("com_level").toString().equals("4")&&cun_map.get("com_f_pkid").toString().equals(xiang_map.get("pkid").toString())){//第四级，同时父id等于上一级id
//								JSONObject cun = new JSONObject();
//								cun.put("text", cun_map.get("com_name"));
//								cun.put("pkid", cun_map.get("pkid"));
//								xiang_jsa.add(cun);
//							}
//						}
						
						//xiang.put("nodes", xiang_jsa);
						xiang_js.add(xiang);
					}
				}
				response.getWriter().write(xiang_js.toString());
			}
			//response.getWriter().write(jsa.toString());
		}
		
		return null;
	}
	
}
