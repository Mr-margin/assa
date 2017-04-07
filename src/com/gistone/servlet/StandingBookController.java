package com.gistone.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;
import com.gistone.util.ImageEncoderService;

public class StandingBookController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 *贫困户显示的信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getPoorMessageController(HttpServletRequest request,HttpServletResponse response) throws Exception{
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
		String  year = request.getParameter("cha_year");//年份
		
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else{
			year = "";
		}
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
			str += " a.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " a.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " a.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " a.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " a.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " a.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " a.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " a.v9>=5 and";
			}else{
				str += " a.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " a.v21='"+cha_banqian+"' and";
		}
		
		String count_st_sql = "select count(*) from (select a.pkid from da_household"+year+" a ";
		String people_sql = "select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v21,a.v22,a.v23,a.v11,a.sys_standard from da_household"+year+" a ";
		//System.out.println(request.getParameter("cha_bfzrr"));
		//如果帮扶人和帮扶单位条件被选择
		if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
			if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
				cha_bfdw = request.getParameter("cha_bfdw").trim();
				str += " c.com_name like '%"+cha_bfdw+"%' and";
			}
			if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
				cha_bfzrr = request.getParameter("cha_bfzrr").trim();
				str += " c.col_name like '%"+cha_bfzrr+"%' and";
			}
			count_st_sql += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid ";
			people_sql += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid ";
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
		
		//System.out.println(count_st_sql);
		//System.out.println(people_sql);
		
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
	 * 查看贫困户的台账信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getStandingBookController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year ="_2016";
		} else {
			year = "";
		}
		String sql="select v1,v2,v3,v4,v5,v6,v7,v8,v10,v11,v28,v12,v13,v14,v15,v16,v17,v19,v32,v25,v26,v27,sys_standard,v22,v29,v30,v31,v23,v33 from da_household"+year+" where pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=this.getBySqlMapper.findRecords(sqlAdapter);
		//户主信息
		JSONArray jsonArray1 =new JSONArray();
		if(list.size()>0){
			
			for(int i = 0;i<list.size();i++){
				Map Patient_st_map = list.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Patient_st_map.keySet()) {
					val.put(key, Patient_st_map.get(key));
				}
				jsonArray1.add(val);
			}
		}
		//家庭成员
		JSONArray jsonArray2 =new JSONArray();
		String xian_sql="select v6,v7,v8,v10,v11,v28,v12,v13,v14,v15,v16,v17,v32,v19 from da_member"+year+" where da_household_id="+pkid;
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		if(xian_list.size()>0){
			for(int i=0;i<xian_list.size();i++){
				if(xian_list.get(i)==null){
					jsonArray2.add("");
				}else{
					Map xian_map=xian_list.get(i);
					JSONObject obj=new JSONObject ();
					for(Object key:xian_map.keySet()){
						obj.put(key, xian_map.get(key));
					}
					jsonArray2.add(obj);
				}
				
			}
		}
		//收入情况
		JSONArray jsonArray5 =new JSONArray();
		String dqsr_sql="SELECT v28,v30,v41,v12,v10,v16,v20,v24,v26,v43,v22,v14 FROM da_current_income"+year+" where da_household_id="+pkid;
		SQLAdapter dqsr_sqlAdapter =new SQLAdapter(dqsr_sql);
		List<Map> dqsr_list=getBySqlMapper.findRecords(dqsr_sqlAdapter);
		if(dqsr_list.size()>0){
			for(int i=0;i<dqsr_list.size();i++){
				if(dqsr_list.get(i)==null){
					jsonArray5.add("");
				}else{
					Map dqsr_map=dqsr_list.get(i);
					JSONObject obj=new JSONObject ();
					for(Object key:dqsr_map.keySet()){
						obj.put(key, dqsr_map.get(key));
					}
					jsonArray5.add(obj);
				}
				
			}
		}
	
		
		//生产条件
		JSONArray jsonArray3 =new JSONArray();
		String sc_sql="select v1,v2,v3,v4,v13,v5,v14 FROM da_production"+year+" where da_household_id="+pkid;
		SQLAdapter sc_sqlAdapter =new SQLAdapter(sc_sql);
		List<Map> sc_list=getBySqlMapper.findRecords(sc_sqlAdapter);
		if(sc_list.size()>0){
			for(int i=0;i<sc_list.size();i++){
				if(sc_list.get(i)==null){
					jsonArray3.add("");
				}else{
					Map sc_map=sc_list.get(i);
					JSONObject obj=new JSONObject();
					for(Object key:sc_map.keySet()){
						obj.put(key, sc_map.get(key));
					}
					jsonArray3.add(obj);
				}
			}
		}
		//生活条件
		JSONArray jsonArray4 =new JSONArray();
		String sh_sql="SELECT v5,v7,v6,v1,v8,v9,v10,v11,v12 FROM da_life"+year+" where da_household_id="+pkid;
		SQLAdapter sh_sqlAdapter =new SQLAdapter(sh_sql);
		List<Map> sh_list=getBySqlMapper.findRecords(sh_sqlAdapter);
		if(sh_list.size()>0){
			for(int i=0;i<sh_list.size();i++){
				if(sh_list.get(i)==null){
					jsonArray4.add("");
				}else{
					JSONObject obj=new JSONObject();
					Map sh_map=sh_list.get(i);
					for(Object key:sh_map.keySet()){
						obj.put(key, sh_map.get(key));
					}
					jsonArray4.add(obj);
				}
			}
				
		}
		
		
		//易地搬迁户需求
		JSONArray jsonArray6 =new JSONArray();
		String ydbq_sql="SELECT * FROM (select v3 vv3,da_household_id from da_life"+year+")a  LEFT JOIN da_household_move"+year+" b  ON a.da_household_id=b.da_household_id WHERE a.da_household_id="+pkid;
		SQLAdapter ydbq_sqlAdapter =new SQLAdapter(ydbq_sql);
		List<Map> ydbq_list=getBySqlMapper.findRecords(ydbq_sqlAdapter);
		if(ydbq_list.size()>0){
			for(int i=0;i<ydbq_list.size();i++){
				if(ydbq_list.get(i)==null){
					
				}else{
					JSONObject obj=new JSONObject();
					Map ydbq_map=ydbq_list.get(i);
					for(Object key:ydbq_map.keySet()){
						obj.put(key, ydbq_map.get(key));
					}
					jsonArray6.add(obj);
				}
				
			}
		}
		
		//帮扶人情况
		JSONArray jsonArray7 =new JSONArray();
		String bfr_sql="SELECT b.col_name,b.v1,b.v2,b.v3,t2.v1 as com_name,b.v4,b.v5,b.v6,t2.v2 as v7,b.telephone FROM sys_personal_household_many"+year+" a "
				+ "LEFT JOIN sys_personal"+year+" b ON a.sys_personal_id = b.pkid join da_company"+year+" t2 on b.da_company_id=t2.pkid where a.da_household_id="+pkid;
		SQLAdapter bfr_sqlAdapter =new SQLAdapter(bfr_sql);
		List<Map> bfr_list=getBySqlMapper.findRecords(bfr_sqlAdapter);
		if(bfr_list.size()>0){
			for(int i=0;i<bfr_list.size();i++){
				if(bfr_list.get(i)==null){
					jsonArray7.add("");
				}else{
					JSONObject obj=new JSONObject();
					Map bfr_map=bfr_list.get(i);
					for(Object key:bfr_map.keySet()){
						obj.put(key, bfr_map.get(key));
					}
					jsonArray7.add(obj);
				}
				
			}
		}
		//生产经营性支出
		JSONArray jsonArray8 =new JSONArray();
		String dqzc_sql="SELECT v2,v4,v6,v8,v10,v12,v14,v16,v18 FROM da_current_expenditure"+year+" where da_household_id="+pkid;   //这里是修改后的，原来的字段名有错 没有加v
		SQLAdapter dqzc_sqlAdapter =new SQLAdapter(dqzc_sql);
		List<Map> dqzc_list=getBySqlMapper.findRecords(dqzc_sqlAdapter);
		if(dqzc_list.size()>0){
			for(int i=0;i<dqzc_list.size();i++){
				if(dqzc_list.get(i)==null){
					jsonArray8.add("");
				}else{
					Map dqzc_map=dqzc_list.get(i);
					JSONObject obj=new JSONObject ();
					for(Object key:dqzc_map.keySet()){
						obj.put(key, dqzc_map.get(key));
					}
					jsonArray8.add(obj);
				}
				
			}
		}
		response.getWriter().write("{\"data1\":"+jsonArray1.toString() +
				",\"data2\":"+jsonArray2.toString()+",\"data3\":"+jsonArray3.toString()+
				",\"data4\":"+jsonArray4.toString()+",\"data5\":"+jsonArray5.toString()+"," +
				"\"data6\":"+jsonArray6.toString()+",\"data7\":"+jsonArray7.toString()+",\"data8\":"+jsonArray8.toString()+"}");
		response.getWriter().close();
		return null;
	
	}
	/**
	 * 台账信息、脱贫计划、帮扶措施、工作台账、贫困收入监测表
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getStandingController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		String year=request.getParameter("year");
		
		//脱贫计划
		JSONArray jsonArray1=new JSONArray();
		String  tp_sql="select * from da_help_plan where da_household_id="+pkid+" and  v1 ='"+year+"'";
		SQLAdapter tp_sqlAdapter=new SQLAdapter(tp_sql);
		List<Map> tp_list=this.getBySqlMapper.findRecords(tp_sqlAdapter);
		if(tp_list.size()>0){
			for(int i=0;i<tp_list.size();i++){
				JSONObject obj=new JSONObject();
				Map tp_map=tp_list.get(i);
				for(Object key:tp_map.keySet()){
					obj.put(key,tp_map.get(key));
				}
				jsonArray1.add(obj);
			}
		}
		
		//帮扶措施
		JSONArray jsonArray2=new JSONArray();
		String bfcs_sql="select * from da_help_tz_measures where da_household_id='"+pkid+"' and  v7='"+year+"'";
		SQLAdapter bfcs_sqlAdapter=new SQLAdapter(bfcs_sql);
		List<Map> bfcs_list=this.getBySqlMapper.findRecords(bfcs_sqlAdapter);
		if(bfcs_list.size()>0){
			for(int i=0;i<bfcs_list.size();i++){
				JSONObject obj=new JSONObject();
				Map bfcs_map=bfcs_list.get(i);
				for(Object key : bfcs_map.keySet()){
					obj.put(key,bfcs_map.get(key));
				}
				jsonArray2.add(obj);
			}
		}
		
		//工作台账
		JSONArray jsonArray3=new JSONArray();
		String gztz_sql="select * from da_work_tz where da_household_id="+pkid+" and v5="+year;
		SQLAdapter gztz_slqAdapter=new SQLAdapter(gztz_sql);
		List<Map> gztz_list=this.getBySqlMapper.findRecords(gztz_slqAdapter);
		if(gztz_list.size()>0){
			for(int i=0;i<gztz_list.size();i++){
				JSONObject obj=new JSONObject();
				Map gztz_map=gztz_list.get(i);
				for(Object key: gztz_map.keySet()){
					obj.put(key, gztz_map.get(key));
				}
				jsonArray3.add(obj);
			}
		}
		response.getWriter().write("{\"data1\":"+jsonArray1.toString()+",\"data2\":"+jsonArray2.toString()+",\"data3\":"+jsonArray3.toString()+"}");
		response.getWriter().close();
		
		return null;
	}
	
	
	
	
	/**
	 * 导出全部数据—基本情况
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//年龄范围开始时间
		String cha_v8_2 = "";//年龄范围截止时间
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				
				WritableSheet sheet_1 = book.createSheet( "贫困户基本信息 " , 0);//生成第一页工作表，参数0表示这是第一页
				
				int[] headerArrHight_1 = {13,20,25,20,20,30,20,15,20,30,35,10,30,10,20,13,25,30,30};
		        String headerArr_1[] = {"家庭编号","旗区","苏木乡","噶查村","户主姓名","证件号码","识别标准","贫困户属性","主要致贫原因","其他致贫原因","致贫原因说明","家庭人口","家庭住址"
		        		,"是否军烈属","是否独生子女户","是否双女户","联系电话","开户银行名称","银行卡号"};
		        for (int i = 0; i < headerArr_1.length; i++) {
		        	sheet_1.addCell(new Label( i , 0 , headerArr_1[i], tsty));
		        	sheet_1.setColumnView(i, headerArrHight_1[i]);
		        }
		        sheet_1.setRowView(0, 500); // 设置第一行的高度
				sheet_1.getSettings().setHorizontalFreeze(5);
				sheet_1.getSettings().setVerticalFreeze(1);
				//生产生活
				WritableSheet sheet_3 = book.createSheet( "生产生活" , 1);
		        int[] headerArrHight_3 = {13,20,25,20,15,15,20,15,20,15,
		        						18,15,15,12,15,8,8,8,15,15,10,
		        						20,10,15,15,30,30,20,15,20,15};
		        String headerArr_3[] = {"家庭编号","旗区","苏木乡","噶查村","户主姓名","耕地面积（亩）","水浇地面积（亩）","林地面积（亩）","退耕还林面积（亩）","草牧场面积（亩）",
					        		"生产用房面积（㎡）","林果面积（亩）","水面面积（亩）","其他","家禽（只）","牛（头）","羊（只）","猪（头）","其他","住房面积（㎡）","是否危房",
					        		"是否纳入易地扶贫搬迁","饮水情况","饮水是否困难","饮水是否安全","通电情况","入户路类型","与主干路距离（公里）","主要燃料类型","是否加入农民专业合作社","有无卫生厕所"};
		        for (int i = 0; i < headerArr_3.length; i++) {
		        	sheet_3.addCell(new Label( i , 0 , headerArr_3[i], tsty));
		        	sheet_3.setColumnView(i, headerArrHight_3[i]);
		        }
		        sheet_3.setRowView(0, 500); // 设置第一行的高度
		        sheet_3.getSettings().setHorizontalFreeze(5);
		        sheet_3.getSettings().setVerticalFreeze(1);
		        
		        int conut = 1;
		        int conut_3 = 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
		        	sheet_1.addCell(new Label( 0 , conut ,s1_map.get("pkid")==null?"":s1_map.get("pkid").toString() ,coty));
		        	sheet_1.addCell(new Label( 1 , conut ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        	sheet_1.addCell(new Label( 2 , conut ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        	sheet_1.addCell(new Label( 3 , conut ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        	sheet_1.addCell(new Label( 4 , conut ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        	sheet_1.addCell(new Label( 5 , conut ,s1_map.get("v8")==null?"":s1_map.get("v8").toString() ,coty));
		        	sheet_1.addCell(new Label( 6 , conut ,s1_map.get("sys_standard")==null?"":s1_map.get("sys_standard").toString() ,coty));
		        	sheet_1.addCell(new Label( 7 , conut ,s1_map.get("v22")==null?"":s1_map.get("v22").toString() ,coty));
		        	sheet_1.addCell(new Label( 8 , conut ,s1_map.get("v23")==null?"":s1_map.get("v23").toString() ,coty));
		        	sheet_1.addCell(new Label( 9 , conut ,s1_map.get("v33")==null?"":s1_map.get("v33").toString() ,coty));
		        	sheet_1.addCell(new Label( 10 , conut ,s1_map.get("basic_explain")==null?"":s1_map.get("basic_explain").toString() ,coty));
		        	sheet_1.addCell(new Label( 11 , conut ,s1_map.get("v9")==null?"":s1_map.get("v9").toString() ,coty));
		        	sheet_1.addCell(new Label( 12 , conut ,s1_map.get("basic_address")==null?"":s1_map.get("basic_address").toString() ,coty));
		        	sheet_1.addCell(new Label( 13 , conut ,s1_map.get("v29")==null?"":s1_map.get("v29").toString() ,coty));
		        	sheet_1.addCell(new Label( 14 , conut ,s1_map.get("v30")==null?"":s1_map.get("v30").toString() ,coty));
		        	sheet_1.addCell(new Label( 15 , conut ,s1_map.get("v31")==null?"":s1_map.get("v31").toString() ,coty));
		        	sheet_1.addCell(new Label( 16 , conut ,s1_map.get("v25")==null?"":s1_map.get("v25").toString() ,coty));
		        	sheet_1.addCell(new Label( 17 , conut ,s1_map.get("v26")==null?"":s1_map.get("v26").toString() ,coty));
		        	sheet_1.addCell(new Label( 18 , conut ,s1_map.get("v27")==null?"":s1_map.get("v27").toString() ,coty));
		        	sheet_1.setRowView(conut, 500); // 设置第一行的高度
		        	conut++;
					//生产生活
		        	String sql_3="select da_household_id ,v1 pv1,v2 pv2,v3 pv3,v4 pv4,v5 pv5,v6 pv6,v7 pv7,v8 pv8,v9 pv9,v10 pv10,v11 pv11,v12 pv12,v13 pv13,"+
		        					"v14 pv14 from da_production"+year+" where da_household_id='"+s1_map.get("pkid")+"'";
							
		        	SQLAdapter s3_Adapter = new SQLAdapter(sql_3);
					List<Map> s3_List = this.getBySqlMapper.findRecords(s3_Adapter);
				        for (int a = 0; a < s3_List.size(); a++) {   //循环一个list里面的数据到excel中
				        	Map sc_map = s3_List.get(a);
				        	sheet_3.addCell(new Label( 0 , conut_3 ,s1_map.get("pkid")==null?"":s1_map.get("pkid").toString() ,coty));
				        	sheet_3.addCell(new Label( 1 , conut_3 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
				        	sheet_3.addCell(new Label( 2 , conut_3 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
				        	sheet_3.addCell(new Label( 3 , conut_3 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
				        	sheet_3.addCell(new Label( 4 , conut_3 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        	
				        	sheet_3.addCell(new Label( 5 , conut_3 ,sc_map.get("pv1")==null||"".equals(sc_map.get("pv1"))?"":sc_map.get("pv1").toString() ,coty));
				        	sheet_3.addCell(new Label( 6 , conut_3 ,sc_map.get("pv2")==null||"".equals(sc_map.get("pv2"))?"":sc_map.get("pv2").toString() ,coty));
				        	sheet_3.addCell(new Label( 7 , conut_3 ,sc_map.get("pv3")==null||"".equals(sc_map.get("pv3"))?"":sc_map.get("pv3").toString() ,coty));
				        	sheet_3.addCell(new Label( 8 , conut_3 ,sc_map.get("pv4")==null||"".equals(sc_map.get("pv4"))?"":sc_map.get("pv4").toString() ,coty));
				        	sheet_3.addCell(new Label( 9 , conut_3 ,sc_map.get("pv5")==null||"".equals(sc_map.get("pv5"))?"":sc_map.get("pv5").toString() ,coty));
				        	sheet_3.addCell(new Label( 10 , conut_3 ,sc_map.get("pv6")==null||"".equals(sc_map.get("pv6"))?"":sc_map.get("pv6").toString() ,coty));
				        	sheet_3.addCell(new Label( 11 , conut_3 ,sc_map.get("pv13")==null||"".equals(sc_map.get("pv13"))?"":sc_map.get("pv13").toString() ,coty));
				        	sheet_3.addCell(new Label( 12 , conut_3 ,sc_map.get("pv14")==null||"".equals(sc_map.get("pv14"))?"":sc_map.get("pv14").toString() ,coty));
				        	sheet_3.addCell(new Label( 13 , conut_3 ,sc_map.get("pv7")==null||"".equals(sc_map.get("pv7"))?"":sc_map.get("pv7").toString() ,coty));
				        	sheet_3.addCell(new Label( 14 , conut_3 ,sc_map.get("pv8")==null||"".equals(sc_map.get("pv8"))?"":sc_map.get("pv8").toString() ,coty));
				        	sheet_3.addCell(new Label( 15 , conut_3 ,sc_map.get("pv9")==null||"".equals(sc_map.get("pv9"))?"":sc_map.get("pv9").toString() ,coty));
				        	sheet_3.addCell(new Label( 16 , conut_3 ,sc_map.get("pv10")==null||"".equals(sc_map.get("pv10"))?"":sc_map.get("pv10").toString() ,coty));
				        	sheet_3.addCell(new Label( 17 , conut_3 ,sc_map.get("pv11")==null||"".equals(sc_map.get("pv11"))?"":sc_map.get("pv11").toString() ,coty));
				        	sheet_3.addCell(new Label( 18 , conut_3 ,sc_map.get("pv12")==null||"".equals(sc_map.get("pv12"))?"":sc_map.get("pv12").toString() ,coty));
				        	
				        
				        }
				        
				        //生活
				        String sh_sql="select da_household_id,v1 lv1,v2 lv2,v3 lv3,v4 lv4, v5 lv5,v6 lv6,v7 lv7,v8 lv8,v9 lv9,v10 lv10,v11 lv11,v12 lv12 from da_life"+year+" where  da_household_id='"+s1_map.get("pkid")+"'";
				        SQLAdapter sh_sqlAdapter=new SQLAdapter (sh_sql);
				        List<Map> sh_list=this.getBySqlMapper.findRecords(sh_sqlAdapter);
				        for (int a = 0; a < sh_list.size(); a++) {
				        	//循环一个list里面的数据到excel中
				        	Map sc_map = sh_list.get(a);
				        	sheet_3.addCell(new Label( 19 , conut_3 ,sc_map.get("lv1")==null||"".equals(sc_map.get("lv1"))?"":sc_map.get("lv1").toString() ,coty));
				        	sheet_3.addCell(new Label( 20 , conut_3 ,sc_map.get("lv2")==null||"".equals(sc_map.get("lv2"))?"":sc_map.get("lv2").toString() ,coty));
				        	sheet_3.addCell(new Label( 21 , conut_3 ,sc_map.get("lv3")==null||"".equals(sc_map.get("lv3"))?"":sc_map.get("lv3").toString() ,coty));
				        	sheet_3.addCell(new Label( 22 , conut_3 ,sc_map.get("lv4")==null||"".equals(sc_map.get("lv4"))?"":sc_map.get("lv4").toString() ,coty));
				        	sheet_3.addCell(new Label( 23 , conut_3 ,sc_map.get("lv8")==null||"".equals(sc_map.get("lv8"))?"":sc_map.get("lv8").toString() ,coty));
				        	sheet_3.addCell(new Label( 24 , conut_3 ,sc_map.get("lv9")==null||"".equals(sc_map.get("lv9"))?"":sc_map.get("lv9").toString() ,coty));
				        	sheet_3.addCell(new Label( 25 , conut_3 ,sc_map.get("lv5")==null||"".equals(sc_map.get("lv5"))?"":sc_map.get("lv5").toString() ,coty));
				        	sheet_3.addCell(new Label( 26 , conut_3 ,sc_map.get("lv6")==null||"".equals(sc_map.get("lv6"))?"":sc_map.get("lv6").toString() ,coty));
				        	sheet_3.addCell(new Label( 27 , conut_3 ,sc_map.get("lv7")==null||"".equals(sc_map.get("lv7"))?"":sc_map.get("lv7").toString() ,coty));
				        	sheet_3.addCell(new Label( 28 , conut_3 ,sc_map.get("lv10")==null||"".equals(sc_map.get("lv10"))?"":sc_map.get("lv10").toString() ,coty));
				        	sheet_3.addCell(new Label( 29 , conut_3 ,sc_map.get("lv11")==null||"".equals(sc_map.get("lv11"))?"":sc_map.get("lv11").toString() ,coty));
				        	sheet_3.addCell(new Label( 30 , conut_3 ,sc_map.get("lv12")==null||"".equals(sc_map.get("lv12"))?"":sc_map.get("lv12").toString() ,coty));
				        	sheet_3.setRowView(conut_3, 500); // 设置第一行的高度
				        	
				        	conut_3++;
				        }
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	/**
	 * 导出家庭成员
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all1(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company"+year+" t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
		        //家庭成员
		        WritableSheet sheet_2 = book.createSheet( "家庭成员 " , 0);
		        
		        int[] headerArrHight_2 = {13,20,25,20,20,10,30,15,15,15,20,20,25,20,20,20,25,30,30,30};
		        String headerArr_2[] = {"家庭编号","旗区","苏木乡","噶查村","户主姓名","性别","证件号码","与户主关系","民族","文化程度","在校生状况","健康状况","劳动能力","务工状况","务工时间（月）","是否参加新农合",
		        		"是否参加新型养老保险","是否参加城镇职工基本养老保险","政治面貌","是否现役军人"};
		        
		        for (int i = 0; i < headerArr_2.length; i++) {
		        	sheet_2.addCell(new Label( i , 0 , headerArr_2[i], tsty));
		        	sheet_2.setColumnView(i, headerArrHight_2[i]);
		        }
//		        HSSFRow row =book.getSheet(0).getRow(1);
//		        setDataValidation(1,2);
		        sheet_2.setRowView(0, 500); // 设置第一行的高度
		        sheet_2.getSettings().setHorizontalFreeze(5);
		        sheet_2.getSettings().setVerticalFreeze(1);
		        int conut_2 = 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
		        	//家庭成员
					String sql_2 ="select pkid,v3,v4,v5,v6,v7,v8,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v28,v32, sys_standard from da_household"+year+" where pkid="+s1_map.get("pkid");
		        	SQLAdapter s2_sqlAdapter=new SQLAdapter(sql_2);
		        	List<Map> s2_list=this.getBySqlMapper.findRecords(s2_sqlAdapter);
		        	sheet_2.addCell(new Label( 0 , conut_2 ,s2_list.get(0).get("pkid")==null?"":s2_list.get(0).get("pkid").toString() ,coty));
		        	sheet_2.addCell(new Label( 1 , conut_2 ,s2_list.get(0).get("v3")==null?"":s2_list.get(0).get("v3").toString() ,coty));
		        	sheet_2.addCell(new Label( 2 , conut_2 ,s2_list.get(0).get("v4")==null?"":s2_list.get(0).get("v4").toString() ,coty));
		        	sheet_2.addCell(new Label( 3 , conut_2 ,s2_list.get(0).get("v5")==null?"":s2_list.get(0).get("v5").toString() ,coty));
		        	sheet_2.addCell(new Label( 4 , conut_2 ,s2_list.get(0).get("v6")==null?"":s2_list.get(0).get("v6").toString() ,coty));
		        	sheet_2.addCell(new Label( 5 , conut_2 ,s2_list.get(0).get("v7")==null?"":s2_list.get(0).get("v7").toString() ,coty));
		        	sheet_2.addCell(new Label( 6 , conut_2 ,s2_list.get(0).get("v8")==null?"":s2_list.get(0).get("v8").toString() ,coty));
		        	sheet_2.addCell(new Label( 7 , conut_2 ,s2_list.get(0).get("v10")==null?"":s2_list.get(0).get("v10").toString() ,coty));
		        	sheet_2.addCell(new Label( 8 , conut_2 ,s2_list.get(0).get("v11")==null?"":s2_list.get(0).get("v11").toString() ,coty));
		        	sheet_2.addCell(new Label( 9 , conut_2 ,s2_list.get(0).get("v12")==null?"":s2_list.get(0).get("v12").toString() ,coty));
		        	sheet_2.addCell(new Label( 10 , conut_2 ,s2_list.get(0).get("v13")==null?"":s2_list.get(0).get("v13").toString() ,coty));
		        	sheet_2.addCell(new Label( 11 , conut_2 ,s2_list.get(0).get("v14")==null?"":s2_list.get(0).get("v14").toString() ,coty));
		        	sheet_2.addCell(new Label( 12 , conut_2 ,s2_list.get(0).get("v15")==null?"":s2_list.get(0).get("v15").toString() ,coty));
		        	sheet_2.addCell(new Label( 13 , conut_2 ,s2_list.get(0).get("v16")==null?"":s2_list.get(0).get("v16").toString() ,coty));
		        	sheet_2.addCell(new Label( 14 , conut_2 ,s2_list.get(0).get("v17")==null?"":s2_list.get(0).get("v17").toString() ,coty));
		        	sheet_2.addCell(new Label( 15 , conut_2 ,s2_list.get(0).get("v18")==null?"":s2_list.get(0).get("v18").toString() ,coty));
		        	sheet_2.addCell(new Label( 16 , conut_2 ,s2_list.get(0).get("v19")==null?"":s2_list.get(0).get("v19").toString() ,coty));
		        	sheet_2.addCell(new Label( 17 , conut_2 ,s2_list.get(0).get("v20")==null?"":s2_list.get(0).get("v20").toString() ,coty));
		        	sheet_2.addCell(new Label( 18 , conut_2 ,s2_list.get(0).get("v28")==null?"":s2_list.get(0).get("v28").toString() ,coty));
		        	sheet_2.addCell(new Label( 19 , conut_2 ,s2_list.get(0).get("v32")==null?"":s2_list.get(0).get("v32").toString() ,coty));
		        	sheet_2.setRowView(conut_2, 500); // 设置第一行的高度
		        	conut_2++;
		        	String cha_sql="select da_household_id, v3,v4,v5,v6,v7,v8,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v28,v32 from da_member"+year+" where da_household_id="+s1_map.get("pkid");
		        	SQLAdapter sqlAdapter=new SQLAdapter(cha_sql);
		        	List<Map> cha_list=this.getBySqlMapper.findRecords(sqlAdapter);
		        	if(cha_list.size()>0){
		        		for(int j=0;j<cha_list.size();j++){
			        		
			        		sheet_2.addCell(new Label( 0 , conut_2 ,cha_list.get(j).get("da_household_id")==null?"":cha_list.get(j).get("da_household_id").toString() ,coty));
				        	sheet_2.addCell(new Label( 1 , conut_2 ,cha_list.get(j).get("v3")==null?"":cha_list.get(j).get("v3").toString() ,coty));
				        	sheet_2.addCell(new Label( 2 , conut_2 ,cha_list.get(j).get("v4")==null?"":cha_list.get(j).get("v4").toString() ,coty));
				        	sheet_2.addCell(new Label( 3 , conut_2 ,cha_list.get(j).get("v5")==null?"":cha_list.get(j).get("v5").toString() ,coty));
				        	sheet_2.addCell(new Label( 4 , conut_2 ,cha_list.get(j).get("v6")==null?"":cha_list.get(j).get("v6").toString() ,coty));
				        	sheet_2.addCell(new Label( 5 , conut_2 ,cha_list.get(j).get("v7")==null?"":cha_list.get(j).get("v7").toString() ,coty));
				        	sheet_2.addCell(new Label( 6 , conut_2 ,cha_list.get(j).get("v8")==null?"":cha_list.get(j).get("v8").toString() ,coty));
				        	sheet_2.addCell(new Label( 7 , conut_2 ,cha_list.get(j).get("v10")==null?"":cha_list.get(j).get("v10").toString() ,coty));
				        	sheet_2.addCell(new Label( 8 , conut_2 ,cha_list.get(j).get("v11")==null?"":cha_list.get(j).get("v11").toString() ,coty));
				        	sheet_2.addCell(new Label( 9 , conut_2 ,cha_list.get(j).get("v12")==null?"":cha_list.get(j).get("v12").toString() ,coty));
				        	sheet_2.addCell(new Label( 10 , conut_2 ,cha_list.get(j).get("v13")==null?"":cha_list.get(j).get("v13").toString() ,coty));
				        	sheet_2.addCell(new Label( 11 , conut_2 ,cha_list.get(j).get("v14")==null?"":cha_list.get(j).get("v14").toString() ,coty));
				        	sheet_2.addCell(new Label( 12 , conut_2 ,cha_list.get(j).get("v15")==null?"":cha_list.get(j).get("v15").toString() ,coty));
				        	sheet_2.addCell(new Label( 13 , conut_2 ,cha_list.get(j).get("v16")==null?"":cha_list.get(j).get("v16").toString() ,coty));
				        	sheet_2.addCell(new Label( 14 , conut_2 ,cha_list.get(j).get("v17")==null?"":cha_list.get(j).get("v17").toString() ,coty));
				        	sheet_2.addCell(new Label( 15 , conut_2 ,cha_list.get(j).get("v18")==null?"":cha_list.get(j).get("v18").toString() ,coty));
				        	sheet_2.addCell(new Label( 16 , conut_2 ,cha_list.get(j).get("v19")==null?"":cha_list.get(j).get("v19").toString() ,coty));
				        	sheet_2.addCell(new Label( 17 , conut_2 ,cha_list.get(j).get("v20")==null?"":cha_list.get(j).get("v20").toString() ,coty));
				        	sheet_2.addCell(new Label( 18 , conut_2 ,cha_list.get(j).get("v28")==null?"":cha_list.get(j).get("v28").toString() ,coty));
				        	sheet_2.addCell(new Label( 19 , conut_2 ,cha_list.get(j).get("v32")==null?"":cha_list.get(j).get("v32").toString() ,coty));
				        	sheet_2.setRowView(conut_2, 500); // 设置第一行的高度
				        	conut_2++;
			        	}
		        	}
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	/**
	 * 导出当前收支分析
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all2(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
				
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company"+year+" t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
		        
		        //当前收支分析
		        WritableSheet sheet_4 = book.createSheet( "当前收支分析" , 0);
		        
//		        Workbook wb = new HSSFWorkbook();  
//		        Sheet sheet = wb.createSheet("sheet1");  1
//		        sheet.createFreezePane(7, 3);
//		        
		        sheet_4.mergeCells( 0 ,0 , 0 , 2 );
		        sheet_4.addCell(new Label( 0 , 0 , "家庭编号", tsty));
//		        Workbook workbook = excelWorkbook.getWorkbook();
//		        HSSFRow   row =book.getSheet(0).getRows(0);
//		        HSSFCell cell = row.getCell(6);
		        
		        sheet_4.mergeCells( 1 ,0 , 1 , 2 );
		        sheet_4.addCell(new Label( 1 , 0, "旗区", tsty));
		        sheet_4.setColumnView(1, 20);
		        sheet_4.mergeCells( 2 ,0 , 2 , 2 );
		        sheet_4.addCell(new Label( 2 , 0 , "苏木乡", tsty));
		        sheet_4.setColumnView(2, 20);
		        sheet_4.mergeCells( 3 ,0 , 3 , 2 );
		        sheet_4.addCell(new Label( 3 , 0 , "嘎查村", tsty));
		        sheet_4.setColumnView(3, 20);
		        sheet_4.mergeCells(4 ,0 , 4 , 2 );
		        sheet_4.addCell(new Label( 4 , 0 , "户主姓名", tsty));
		        
		        
		        sheet_4.mergeCells( 5 ,0 ,47 ,0 );
		        sheet_4.addCell(new Label( 5 , 0 , "当前收入情况", tsty));
		        sheet_4.mergeCells( 48 ,0 , 78 ,0 );
		        sheet_4.addCell(new Label( 48 , 0 , "当前支出情况", tsty));
		        sheet_4.mergeCells( 5 , 1 , 6 , 1 );
		        sheet_4.addCell(new Label( 5 , 1 , "农业（水产）", tsty));
		        sheet_4.addCell(new Label( 5 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 6 , 2 , "金额（元）", tsty));
		        
		        sheet_4.mergeCells( 7 , 1 , 8 , 1 );
		        sheet_4.addCell(new Label( 7 , 1 , "畜牧业", tsty));
		        sheet_4.addCell(new Label( 7 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 8 , 2 , "金额（元）", tsty));
//
		        sheet_4.mergeCells( 9 , 1 , 10 , 1 );
		        sheet_4.addCell(new Label( 9 , 1 , "林业", tsty));
		        sheet_4.addCell(new Label( 9, 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 10 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 11 , 1 , 12 , 1 );
		        sheet_4.addCell(new Label( 11, 1 , "其他", tsty));
		        sheet_4.addCell(new Label(11 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 12 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 13, 1 , 14 , 1 );
		        sheet_4.addCell(new Label( 13, 1 , "小计", tsty));
		        sheet_4.addCell(new Label(13 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 14 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 15 , 1 , 16, 1 );
		        sheet_4.addCell(new Label( 15, 1 , "农林牧草、生态等补贴", tsty));
		        sheet_4.addCell(new Label(15 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 16 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 17 , 1 , 18, 1 );
		        sheet_4.addCell(new Label( 17, 1 , "养老金", tsty));
		        sheet_4.addCell(new Label(17 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 18 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 19 , 1 , 20, 1 );
		        sheet_4.addCell(new Label( 19, 1 , "低保（五保）补贴", tsty));
		        sheet_4.addCell(new Label(19 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 20 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 21 , 1 , 22, 1 );
		        sheet_4.addCell(new Label( 21, 1 , "燃煤补贴", tsty));
		        sheet_4.addCell(new Label(21 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 22 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 23 , 1 , 24, 1 );
		        sheet_4.addCell(new Label( 23, 1 , "五保金", tsty));
		        sheet_4.addCell(new Label(23 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 24 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 25 , 1 , 26, 1 );
		        sheet_4.addCell(new Label( 25, 1 , "计划生育", tsty));
		        sheet_4.addCell(new Label(25, 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 26 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 27 , 1 , 28, 1 );
		        sheet_4.addCell(new Label( 27, 1 , "其他", tsty));
		        sheet_4.addCell(new Label(27 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 28 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 29 , 1 , 30, 1 );
		        sheet_4.addCell(new Label( 29, 1 , "小计", tsty));
		        sheet_4.addCell(new Label(29 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 30 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 31 , 1 , 32, 1 );
		        sheet_4.addCell(new Label( 31, 1 , "土地、草牧场流转", tsty));
		        sheet_4.addCell(new Label(31 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 32 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 33 , 1 , 34, 1 );
		        sheet_4.addCell(new Label(33, 1 , "其他", tsty));
		        sheet_4.addCell(new Label(33 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 34 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 35 , 1 , 40, 1 );
		        sheet_4.addCell(new Label( 35 , 1 , "工资性收入", tsty));
		        sheet_4.addCell(new Label(35 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 36 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 37 , 2 , "金额", tsty));
		        sheet_4.addCell(new Label(38 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 39 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 40 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 41 , 1 ,46, 1 );
		        sheet_4.addCell(new Label(41 , 1 , "其他收入", tsty));
		        sheet_4.addCell(new Label(41 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 42 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label(43, 2 , "金额", tsty));
		        sheet_4.addCell(new Label(44 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 45 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 46 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 47 , 1 , 47, 2 );
		        sheet_4.addCell(new Label(47 , 1 , "总收入合计", tsty));
		        //当前支出
		        sheet_4.mergeCells( 48 , 1 , 49 , 1 );
		        sheet_4.addCell(new Label( 48 , 1 , "农资费用", tsty));
		        sheet_4.addCell(new Label( 48 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 49 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 50 , 1 , 51 , 1 );
		        sheet_4.addCell(new Label( 50 , 1 , "固定财产折旧和租赁费", tsty));
		        sheet_4.addCell(new Label( 50 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 51 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 52, 1 , 53 , 1 );
		        sheet_4.addCell(new Label( 52 , 1 , "水电燃料支出", tsty));
		        sheet_4.addCell(new Label(52 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 53 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 54 , 1 , 55 , 1 );
		        sheet_4.addCell(new Label( 54 , 1 , "承包土地、草场费用", tsty));
		        sheet_4.addCell(new Label(54 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 55 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 56 , 1 , 57 , 1 );
		        sheet_4.addCell(new Label( 56 , 1 , "饲草料", tsty));
		        sheet_4.addCell(new Label(56 , 2 , "支出明细细", tsty));
		        sheet_4.addCell(new Label( 57 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 58 , 1 , 59 , 1 );
		        sheet_4.addCell(new Label( 58 , 1 , "防疫防治支出", tsty));
		        sheet_4.addCell(new Label(58 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 59 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 60 , 1 , 61 , 1 );
		        sheet_4.addCell(new Label( 60 , 1 , "种（仔）畜", tsty));
		        sheet_4.addCell(new Label(60 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 61 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 62 , 1 , 63 , 1 );
		        sheet_4.addCell(new Label( 62 , 1 , "销售费用和通讯费用", tsty));
		        sheet_4.addCell(new Label(62 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 63 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 64 , 1 , 65 , 1 );
		        sheet_4.addCell(new Label( 64, 1 , "借贷利息", tsty));
		        sheet_4.addCell(new Label(64, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 65, 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 66 , 1 ,71 , 1 );
		        sheet_4.addCell(new Label( 66, 1 , "政策性支出", tsty));
		        sheet_4.addCell(new Label(66, 2 , "项目", tsty));
		        sheet_4.addCell(new Label(67, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label(68, 2 , "金额（元）", tsty));
		        sheet_4.addCell(new Label(69, 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 70, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 71, 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 72 , 1 ,77 , 1 );
		        sheet_4.addCell(new Label( 72, 1 , "其他支出", tsty));
		        sheet_4.addCell(new Label(72, 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 73, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 74, 2 , "金额（元）", tsty));
		        sheet_4.addCell(new Label(75, 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 76, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 77, 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 78 , 1 ,78 , 2 );
		        sheet_4.addCell(new Label( 78,1 , "总支出合计", tsty));
		        sheet_4.mergeCells( 79 , 0 ,79 , 2 );
		        sheet_4.addCell(new Label( 79,0 , "年纯收入", tsty));
		        sheet_4.mergeCells( 80 , 0 ,80 , 2 );
		        sheet_4.addCell(new Label( 80,0 , "年人均纯收入", tsty));
		        sheet_4.setRowView(0, 500);
		        sheet_4.setRowView(1, 500);
		        sheet_4.setRowView(2, 500);
				SheetSettings ws=sheet_4.getSettings();
				ws.setHorizontalFreeze(5);//列
				ws.setVerticalFreeze(3);//行
//		        wwork
//		        sheet_4.setHorizontalFreeze(2);
				//帮扶后收支
//				WritableSheet sheet_8 = book.createSheet("帮扶后收支分析", 1);
//				sheet_8.mergeCells(0, 0, 0, 2);
//				sheet_8.addCell(new Label(0, 0, "家庭编号", tsty));
//				
//				sheet_8.mergeCells(1, 0, 1, 2);
//				sheet_8.addCell(new Label(1, 0, "旗区", tsty));
//				sheet_8.setColumnView(1, 20);
//				sheet_8.mergeCells(2, 0, 2, 2);
//				sheet_8.addCell(new Label(2, 0, "苏木乡", tsty));
//				sheet_8.setColumnView(2, 20);
//				sheet_8.mergeCells(3, 0, 3, 2);
//				sheet_8.addCell(new Label(3, 0, "嘎查村", tsty));
//				sheet_8.setColumnView(3, 20);
//				sheet_8.mergeCells(4, 0, 4, 2);
//				sheet_8.addCell(new Label(4, 0, "户主姓名", tsty));
//				sheet_8.mergeCells(5, 0, 43, 0);
//				sheet_8.addCell(new Label(5, 0, "帮扶后收入情况", tsty));
//				sheet_8.mergeCells(44, 0, 74, 0);
//				sheet_8.addCell(new Label(44, 0, "帮扶后支出情况", tsty));
//				sheet_8.mergeCells(5, 1, 6, 1);
//				sheet_8.addCell(new Label(5, 1, "农业（水产）", tsty));
//				sheet_8.addCell(new Label(5, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(6, 2, "金额（元）", tsty));
//
//				sheet_8.mergeCells(7, 1, 8, 1);
//				sheet_8.addCell(new Label(7, 1, "畜牧业", tsty));
//				sheet_8.addCell(new Label(7, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(8, 2, "金额（元）", tsty));
//				//
//				sheet_8.mergeCells(9, 1, 10, 1);
//				sheet_8.addCell(new Label(9, 1, "林业", tsty));
//				sheet_8.addCell(new Label(9, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(10, 2, "金额", tsty));
//				sheet_8.mergeCells(11, 1, 12, 1);
//				sheet_8.addCell(new Label(11, 1, "其他", tsty));
//				sheet_8.addCell(new Label(11, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(12, 2, "金额", tsty));
//				sheet_8.mergeCells(13, 1, 14, 1);
//				sheet_8.addCell(new Label(13, 1, "小计", tsty));
//				sheet_8.addCell(new Label(13, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(14, 2, "金额", tsty));
//				sheet_8.mergeCells(15, 1, 16, 1);
//				sheet_8.addCell(new Label(15, 1, "农林牧草、生态等补贴", tsty));
//				sheet_8.addCell(new Label(15, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(16, 2, "金额", tsty));
//
//				sheet_8.mergeCells(17, 1, 18, 1);
//				sheet_8.addCell(new Label(17, 1, "养老金", tsty));
//				sheet_8.addCell(new Label(17, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(18, 2, "金额", tsty));
//
//				sheet_8.mergeCells(19, 1, 20, 1);
//				sheet_8.addCell(new Label(19, 1, "低保（五保）补贴", tsty));
//				sheet_8.addCell(new Label(19, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(20, 2, "金额", tsty));
//				sheet_8.mergeCells(21, 1, 22, 1);
//				sheet_8.addCell(new Label(21, 1, "燃煤补贴", tsty));
//				sheet_8.addCell(new Label(21, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(22, 2, "金额", tsty));
//				
////				sheet_8.mergeCells(19, 1, 20, 1);
////				sheet_8.addCell(new Label(19, 1, "五保金", tsty));
////				sheet_8.addCell(new Label(19, 2, "收入明细", tsty));
////				sheet_8.addCell(new Label(20, 2, "金额", tsty));
////				sheet_8.mergeCells(21, 1, 22, 1);
////				sheet_8.addCell(new Label(21, 1, "计划生育", tsty));
////				sheet_8.addCell(new Label(21, 2, "收入明细", tsty));
////				sheet_8.addCell(new Label(22, 2, "金额", tsty));
//				
//				sheet_8.mergeCells(23, 1, 24, 1);
//				sheet_8.addCell(new Label(23, 1, "其他", tsty));
//				sheet_8.addCell(new Label(23, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(24, 2, "金额", tsty));
//
//				sheet_8.mergeCells(25, 1, 26, 1);
//				sheet_8.addCell(new Label(25, 1, "小计", tsty));
//				sheet_8.addCell(new Label(25, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(26, 2, "金额", tsty));
//				sheet_8.mergeCells(27, 1, 28, 1);
//				sheet_8.addCell(new Label(27, 1, "土地、草牧场流转", tsty));
//				sheet_8.addCell(new Label(27, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(28, 2, "金额", tsty));
//				sheet_8.mergeCells(29, 1, 30, 1);
//				sheet_8.addCell(new Label(29, 1, "其他", tsty));
//				sheet_8.addCell(new Label(29, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(30, 2, "金额", tsty));
//
//				sheet_8.mergeCells(31, 1, 36, 1);
//				sheet_8.addCell(new Label(31, 1, "工资性收入", tsty));
//				sheet_8.addCell(new Label(31, 2, "项目", tsty));
//				sheet_8.addCell(new Label(32, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(33, 2, "金额", tsty));
//				sheet_8.addCell(new Label(34, 2, "项目", tsty));
//				sheet_8.addCell(new Label(35, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(36, 2, "金额", tsty));
//
//				sheet_8.mergeCells(37, 1, 42, 1);
//				sheet_8.addCell(new Label(37, 1, "其他收入", tsty));
//				sheet_8.addCell(new Label(37, 2, "项目", tsty));
//				sheet_8.addCell(new Label(38, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(39, 2, "金额", tsty));
//				sheet_8.addCell(new Label(40, 2, "项目", tsty));
//				sheet_8.addCell(new Label(41, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(42, 2, "金额", tsty));
//
//				sheet_8.mergeCells(43, 1, 43, 2);
//				sheet_8.addCell(new Label(43, 1, "总收入合计", tsty));
//				// 当前支出
//				sheet_8.mergeCells(44, 1, 45, 1);
//				sheet_8.addCell(new Label(44, 1, "农资费用", tsty));
//				sheet_8.addCell(new Label(44, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(45, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(46, 1, 47, 1);
//				sheet_8.addCell(new Label(46, 1, "固定财产折旧和租赁费", tsty));
//				sheet_8.addCell(new Label(46, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(47, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(48, 1, 49, 1);
//				sheet_8.addCell(new Label(48, 1, "水电燃料支出", tsty));
//				sheet_8.addCell(new Label(48, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(49, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(50, 1, 51, 1);
//				sheet_8.addCell(new Label(50, 1, "承包土地、草场费用", tsty));
//				sheet_8.addCell(new Label(50, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(51, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(52, 1, 53, 1);
//				sheet_8.addCell(new Label(52, 1, "饲草料", tsty));
//				sheet_8.addCell(new Label(52, 2, "支出明细细", tsty));
//				sheet_8.addCell(new Label(53, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(54, 1, 55, 1);
//				sheet_8.addCell(new Label(54, 1, "防疫防治支出", tsty));
//				sheet_8.addCell(new Label(54, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(55, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(56, 1, 57, 1);
//				sheet_8.addCell(new Label(56, 1, "种（仔）畜", tsty));
//				sheet_8.addCell(new Label(56, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(57, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(58, 1, 59, 1);
//				sheet_8.addCell(new Label(58, 1, "销售费用和通讯费用", tsty));
//				sheet_8.addCell(new Label(58, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(59, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(60, 1, 61, 1);
//				sheet_8.addCell(new Label(60, 1, "借贷利息", tsty));
//				sheet_8.addCell(new Label(60, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(61, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(62, 1, 67, 1);
//				sheet_8.addCell(new Label(62, 1, "政策性支出", tsty));
//				sheet_8.addCell(new Label(62, 2, "项目", tsty));
//				sheet_8.addCell(new Label(63, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(64, 2, "金额（元）", tsty));
//				sheet_8.addCell(new Label(65, 2, "项目", tsty));
//				sheet_8.addCell(new Label(66, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(67, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(68, 1, 73, 1);
//				sheet_8.addCell(new Label(68, 1, "其他支出", tsty));
//				sheet_8.addCell(new Label(68, 2, "项目", tsty));
//				sheet_8.addCell(new Label(69, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(70, 2, "金额（元）", tsty));
//				sheet_8.addCell(new Label(71, 2, "项目", tsty));
//				sheet_8.addCell(new Label(72, 2, "支出明细", tsty));
//				sheet_8.addCell(new Label(73, 2, "金额（元）", tsty));
//				sheet_8.mergeCells(74, 1, 74, 2);
//				sheet_8.addCell(new Label(74, 1, "总支出合计", tsty));
//				sheet_8.mergeCells(75, 0, 75, 2);政策性支出 
//				sheet_8.addCell(new Label(75, 0, "年纯收入", tsty));
//				sheet_8.mergeCells(76, 0, 76, 2);
//				sheet_8.addCell(new Label(76, 0, "年人均纯收入", tsty));
//				sheet_8.setRowView(0, 500);
//				sheet_8.setRowView(1, 500);
//				sheet_8.setRowView(2, 500);
		        int conut_4= 3;
		        int conut_8= 3;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        	
				        
				        //当前收支分析
				        String sql_4="select * from da_current_income"+year+" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter sqlAdapter_4=new SQLAdapter(sql_4);
				        List<Map> list_4=this.getBySqlMapper.findRecords(sqlAdapter_4);
				        sheet_4.addCell(new Label( 0 , conut_4 ,list_4.get(0).get("da_household_id")==null?"":list_4.get(0).get("da_household_id").toString() ,coty));
				        
				        sheet_4.addCell(new Label( 1 , conut_4 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
				        sheet_4.addCell(new Label( 2 , conut_4 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString(),coty));
				        sheet_4.addCell(new Label(3 , conut_4 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString(),coty));
				        sheet_4.addCell(new Label( 4 , conut_4 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        
				        sheet_4.addCell(new Label( 5 , conut_4 ,list_4.get(0).get("v1")==null?"":list_4.get(0).get("v1").toString() ,coty));
				        sheet_4.addCell(new Label( 6 , conut_4 ,list_4.get(0).get("v2")==null?"":list_4.get(0).get("v2").toString() ,coty));
				        sheet_4.addCell(new Label( 7 , conut_4 ,list_4.get(0).get("v3")==null?"":list_4.get(0).get("v3").toString() ,coty));
				        sheet_4.addCell(new Label( 8 , conut_4 ,list_4.get(0).get("v4")==null?"":list_4.get(0).get("v4").toString() ,coty));
				        sheet_4.addCell(new Label( 9 , conut_4 ,list_4.get(0).get("v5")==null?"":list_4.get(0).get("v5").toString() ,coty));
				        sheet_4.addCell(new Label( 10 , conut_4 ,list_4.get(0).get("v6")==null?"":list_4.get(0).get("v6").toString() ,coty));
				        sheet_4.addCell(new Label( 11 , conut_4 ,list_4.get(0).get("v7")==null?"":list_4.get(0).get("v7").toString() ,coty));
				        sheet_4.addCell(new Label( 12 , conut_4 ,list_4.get(0).get("v8")==null?"":list_4.get(0).get("v8").toString() ,coty));
				        sheet_4.addCell(new Label( 13 , conut_4 ,list_4.get(0).get("v9")==null?"":list_4.get(0).get("v9").toString() ,coty));
				        sheet_4.addCell(new Label( 14 , conut_4 ,list_4.get(0).get("v10")==null?"":list_4.get(0).get("v10").toString() ,coty));
				        sheet_4.addCell(new Label( 15 , conut_4 ,list_4.get(0).get("v11")==null?"":list_4.get(0).get("v11").toString() ,coty));
				        sheet_4.addCell(new Label( 16 , conut_4 ,list_4.get(0).get("v12")==null?"":list_4.get(0).get("v12").toString() ,coty));
				        sheet_4.addCell(new Label( 17 , conut_4 ,list_4.get(0).get("v13")==null?"":list_4.get(0).get("v13").toString() ,coty));
				        sheet_4.addCell(new Label( 18 , conut_4 ,list_4.get(0).get("v14")==null?"":list_4.get(0).get("v14").toString() ,coty));
				        sheet_4.addCell(new Label( 19 , conut_4 ,list_4.get(0).get("v15")==null?"":list_4.get(0).get("v15").toString() ,coty));
				        sheet_4.addCell(new Label( 20 , conut_4 ,list_4.get(0).get("v16")==null?"":list_4.get(0).get("v16").toString() ,coty));
				        sheet_4.addCell(new Label( 21 , conut_4 ,list_4.get(0).get("v17")==null?"":list_4.get(0).get("v17").toString() ,coty));
				        sheet_4.addCell(new Label( 22 , conut_4 ,list_4.get(0).get("v18")==null?"":list_4.get(0).get("v18").toString() ,coty));
				        sheet_4.addCell(new Label( 23 , conut_4 ,list_4.get(0).get("v40")==null?"":list_4.get(0).get("v40").toString() ,coty));
				        sheet_4.addCell(new Label( 24 , conut_4 ,list_4.get(0).get("v41")==null?"":list_4.get(0).get("v41").toString() ,coty));
				        sheet_4.addCell(new Label( 25 , conut_4 ,list_4.get(0).get("v42")==null?"":list_4.get(0).get("v42").toString() ,coty));
				        sheet_4.addCell(new Label( 26 , conut_4 ,list_4.get(0).get("v43")==null?"":list_4.get(0).get("v43").toString() ,coty));
				        sheet_4.addCell(new Label( 27 , conut_4 ,list_4.get(0).get("v19")==null?"":list_4.get(0).get("v19").toString() ,coty));
				        sheet_4.addCell(new Label( 28 , conut_4 ,list_4.get(0).get("v20")==null?"":list_4.get(0).get("v20").toString() ,coty));
				        sheet_4.addCell(new Label( 29 , conut_4 ,list_4.get(0).get("v21")==null?"":list_4.get(0).get("v21").toString() ,coty));
				        sheet_4.addCell(new Label( 30 , conut_4 ,list_4.get(0).get("v22")==null?"":list_4.get(0).get("v22").toString() ,coty));
				        sheet_4.addCell(new Label( 31 , conut_4 ,list_4.get(0).get("v23")==null?"":list_4.get(0).get("v23").toString() ,coty));
				        sheet_4.addCell(new Label( 32 , conut_4 ,list_4.get(0).get("v24")==null?"":list_4.get(0).get("v24").toString() ,coty));
				        sheet_4.addCell(new Label( 33 , conut_4 ,list_4.get(0).get("v25")==null?"":list_4.get(0).get("v25").toString() ,coty));
				        sheet_4.addCell(new Label( 34 , conut_4 ,list_4.get(0).get("v26")==null?"":list_4.get(0).get("v26").toString() ,coty));
				        sheet_4.addCell(new Label( 35 , conut_4 ,list_4.get(0).get("v35")==null?"":list_4.get(0).get("v35").toString() ,coty));
				        sheet_4.addCell(new Label( 36 , conut_4 ,list_4.get(0).get("v27")==null?"":list_4.get(0).get("v27").toString() ,coty));
				        sheet_4.addCell(new Label( 37 , conut_4 ,list_4.get(0).get("v28")==null?"":list_4.get(0).get("v28").toString() ,coty));
				        sheet_4.addCell(new Label( 38 , conut_4 ,list_4.get(0).get("v36")==null?"":list_4.get(0).get("v36").toString() ,coty));
				        sheet_4.addCell(new Label( 39 , conut_4 ,list_4.get(0).get("v29")==null?"":list_4.get(0).get("v29").toString() ,coty));
				        sheet_4.addCell(new Label( 40 , conut_4 ,list_4.get(0).get("v30")==null?"":list_4.get(0).get("v30").toString() ,coty));
				        sheet_4.addCell(new Label( 41 , conut_4 ,list_4.get(0).get("v37")==null?"":list_4.get(0).get("v37").toString() ,coty));
				        sheet_4.addCell(new Label( 42 , conut_4 ,list_4.get(0).get("v31")==null?"":list_4.get(0).get("v31").toString() ,coty));
				        sheet_4.addCell(new Label( 43 , conut_4 ,list_4.get(0).get("v32")==null?"":list_4.get(0).get("v32").toString() ,coty));
				        sheet_4.addCell(new Label( 44 , conut_4 ,list_4.get(0).get("v38")==null?"":list_4.get(0).get("v38").toString() ,coty));
				        sheet_4.addCell(new Label( 45 , conut_4 ,list_4.get(0).get("v33")==null?"":list_4.get(0).get("v33").toString() ,coty));
				        sheet_4.addCell(new Label( 46 , conut_4 ,list_4.get(0).get("v34")==null?"":list_4.get(0).get("v34").toString() ,coty));
				        sheet_4.addCell(new Label( 47 , conut_4 ,list_4.get(0).get("v39")==null?"":list_4.get(0).get("v39").toString() ,coty));
				        //支出
				        String zc_sql="select v1 cv1,v2 cv2,v3 cv3,v4 cv4,v5 cv5,v6 cv6,v7 cv7,v8 cv8,v9 cv9,v10 cv10,v11 cv11,"+
				        				"v12 cv12,v13 cv13,v14 cv14,v15 cv15,v16 cv16,v17 cv17,v18 cv18,v19 cv19,v20 cv20,v21 cv21,v22 cv22,"+
				        				"v23 cv23,v24 cv24,v25 cv25,v26 cv26,v27 cv27,v28 cv28,v29 cv29,v30 cv30,v31 cv31,da_household_id from da_current_expenditure"+year+" "+
				        				" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter zc_sqlAdapter=new SQLAdapter(zc_sql);
				        List<Map> zc_list=this.getBySqlMapper.findRecords(zc_sqlAdapter);
				        
				        sheet_4.addCell(new Label( 48 , conut_4 ,zc_list.get(0).get("cv1")==null?"":zc_list.get(0).get("cv1").toString() ,coty));
				        sheet_4.addCell(new Label( 49 , conut_4 ,zc_list.get(0).get("cv2")==null?"":zc_list.get(0).get("cv2").toString() ,coty));
				        sheet_4.addCell(new Label( 50 , conut_4 ,zc_list.get(0).get("cv3")==null?"":zc_list.get(0).get("cv3").toString() ,coty));
				        sheet_4.addCell(new Label( 51 , conut_4 ,zc_list.get(0).get("cv4")==null?"":zc_list.get(0).get("cv4").toString() ,coty));
				        sheet_4.addCell(new Label( 52 , conut_4 ,zc_list.get(0).get("cv5")==null?"":zc_list.get(0).get("cv5").toString() ,coty));
				        sheet_4.addCell(new Label( 53 , conut_4 ,zc_list.get(0).get("cv6")==null?"":zc_list.get(0).get("cv6").toString() ,coty));
				        sheet_4.addCell(new Label( 54 , conut_4 ,zc_list.get(0).get("cv7")==null?"":zc_list.get(0).get("cv7").toString() ,coty));
				        sheet_4.addCell(new Label( 55 , conut_4 ,zc_list.get(0).get("cv8")==null?"":zc_list.get(0).get("cv8").toString() ,coty));
				        sheet_4.addCell(new Label( 56 , conut_4 ,zc_list.get(0).get("cv9")==null?"":zc_list.get(0).get("cv9").toString() ,coty));
				        sheet_4.addCell(new Label( 57 , conut_4 ,zc_list.get(0).get("cv10")==null?"":zc_list.get(0).get("cv10").toString() ,coty));
				        sheet_4.addCell(new Label( 58 , conut_4 ,zc_list.get(0).get("cv11")==null?"":zc_list.get(0).get("cv11").toString() ,coty));
				        sheet_4.addCell(new Label( 59 , conut_4 ,zc_list.get(0).get("cv12")==null?"":zc_list.get(0).get("cv12").toString() ,coty));
				        sheet_4.addCell(new Label( 60 , conut_4 ,zc_list.get(0).get("cv13")==null?"":zc_list.get(0).get("cv13").toString() ,coty));
				        sheet_4.addCell(new Label( 61 , conut_4 ,zc_list.get(0).get("cv14")==null?"":zc_list.get(0).get("cv14").toString() ,coty));
				        sheet_4.addCell(new Label(62 , conut_4 ,zc_list.get(0).get("cv15")==null?"":zc_list.get(0).get("cv15").toString() ,coty));
				        sheet_4.addCell(new Label( 63 , conut_4 ,zc_list.get(0).get("cv16")==null?"":zc_list.get(0).get("cv16").toString() ,coty));
				        sheet_4.addCell(new Label( 64 , conut_4 ,zc_list.get(0).get("cv17")==null?"":zc_list.get(0).get("cv17").toString() ,coty));
				        sheet_4.addCell(new Label( 65 , conut_4 ,zc_list.get(0).get("cv18")==null?"":zc_list.get(0).get("cv18").toString() ,coty));
				        sheet_4.addCell(new Label( 66 , conut_4 ,zc_list.get(0).get("cv19")==null?"":zc_list.get(0).get("cv19").toString() ,coty));
				        sheet_4.addCell(new Label( 67 , conut_4 ,zc_list.get(0).get("cv20")==null?"":zc_list.get(0).get("cv20").toString() ,coty));
				        sheet_4.addCell(new Label( 68 , conut_4 ,zc_list.get(0).get("cv21")==null?"":zc_list.get(0).get("cv21").toString() ,coty));
				        sheet_4.addCell(new Label( 69 , conut_4 ,zc_list.get(0).get("cv22")==null?"":zc_list.get(0).get("cv22").toString() ,coty));
				        sheet_4.addCell(new Label( 70 , conut_4 ,zc_list.get(0).get("cv23")==null?"":zc_list.get(0).get("cv23").toString() ,coty));
				        sheet_4.addCell(new Label( 71 , conut_4 ,zc_list.get(0).get("cv24")==null?"":zc_list.get(0).get("cv24").toString() ,coty));
				        sheet_4.addCell(new Label( 72 , conut_4 ,zc_list.get(0).get("cv25")==null?"":zc_list.get(0).get("cv25").toString() ,coty));
				        sheet_4.addCell(new Label( 73 , conut_4 ,zc_list.get(0).get("cv26")==null?"":zc_list.get(0).get("cv26").toString() ,coty));
				        sheet_4.addCell(new Label( 74 , conut_4 ,zc_list.get(0).get("cv27")==null?"":zc_list.get(0).get("cv27").toString() ,coty));
				        sheet_4.addCell(new Label( 75 , conut_4 ,zc_list.get(0).get("cv28")==null?"":zc_list.get(0).get("cv28").toString() ,coty));
				        sheet_4.addCell(new Label( 76 , conut_4 ,zc_list.get(0).get("cv29")==null?"":zc_list.get(0).get("cv29").toString() ,coty));
				        sheet_4.addCell(new Label( 77 , conut_4 ,zc_list.get(0).get("cv30")==null?"":zc_list.get(0).get("cv30").toString() ,coty));
				        sheet_4.addCell(new Label( 78 , conut_4 ,zc_list.get(0).get("cv31")==null?"":zc_list.get(0).get("cv31").toString() ,coty));
				        
				        String a="";
						String b="";
						if("".equals(list_4.get(0).get("v39"))||list_4.get(0).get("v39")==null){
							a="0";
						}else{
							a=list_4.get(0).get("v39").toString();
						}
						if("".equals(zc_list.get(0).get("cv31"))||zc_list.get(0).get("cv31")==null){
							b="0";
						}else{
							b=zc_list.get(0).get("cv31").toString();
						}
						double c=Double.parseDouble(a);
						double c1=Double.parseDouble(b);
						double c2=c-c1;
						String ncsr=String.format("%.2f", c2);
						double cc=Double.parseDouble(s1_map.get("v9").toString());
						double cnum=c2/cc;
						String str1=String.format("%.2f", cnum);
				        sheet_4.addCell(new Label( 79 , conut_4 ,ncsr ,coty));
				        sheet_4.addCell(new Label( 80 , conut_4 ,str1,coty));
				        sheet_4.setRowView(conut_4, 500); // 设置第一行的高度
				        conut_4++;
				        
				        
				        //帮扶后收支
//				        String sql_8="select * from da_helpback_income where da_household_id="+s1_map.get("pkid");
//				        SQLAdapter sqlAdapter_8=new SQLAdapter(sql_8);
//				        List<Map> list_8=this.getBySqlMapper.findRecords(sqlAdapter_8);
//				        sheet_8.addCell(new Label( 0 , conut_8 ,list_8.get(0).get("da_household_id")==null?"":list_8.get(0).get("da_household_id").toString() ,coty));
//				        sheet_8.addCell(new Label(1, conut_8 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
//			          	sheet_8.addCell(new Label(2, conut_8 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
//			          	sheet_8.addCell(new Label(3, conut_8 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
//			          	sheet_8.addCell(new Label(4, conut_8 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
//				        sheet_8.addCell(new Label( 5 , conut_8 ,list_8.get(0).get("v1")==null?"":list_8.get(0).get("v1").toString() ,coty));
//				        sheet_8.addCell(new Label( 6 , conut_8 ,list_8.get(0).get("v2")==null?"":list_8.get(0).get("v2").toString() ,coty));
//				        sheet_8.addCell(new Label( 7 , conut_8 ,list_8.get(0).get("v3")==null?"":list_8.get(0).get("v3").toString() ,coty));
//				        sheet_8.addCell(new Label( 8 , conut_8 ,list_8.get(0).get("v4")==null?"":list_8.get(0).get("v4").toString() ,coty));
//				        sheet_8.addCell(new Label( 9 , conut_8 ,list_8.get(0).get("v5")==null?"":list_8.get(0).get("v5").toString() ,coty));
//				        sheet_8.addCell(new Label( 10 , conut_8 ,list_8.get(0).get("v6")==null?"":list_8.get(0).get("v6").toString() ,coty));
//				        sheet_8.addCell(new Label( 11 , conut_8 ,list_8.get(0).get("v7")==null?"":list_8.get(0).get("v7").toString() ,coty));
//				        sheet_8.addCell(new Label( 12 , conut_8 ,list_8.get(0).get("v8")==null?"":list_8.get(0).get("v8").toString() ,coty));
//				        sheet_8.addCell(new Label( 13 , conut_8 ,list_8.get(0).get("v9")==null?"":list_8.get(0).get("v9").toString() ,coty));
//				        sheet_8.addCell(new Label( 14 , conut_8 ,list_8.get(0).get("v10")==null?"":list_8.get(0).get("v10").toString() ,coty));
//				        sheet_8.addCell(new Label( 15 , conut_8 ,list_8.get(0).get("v11")==null?"":list_8.get(0).get("v11").toString() ,coty));
//				        sheet_8.addCell(new Label( 16 , conut_8 ,list_8.get(0).get("v12")==null?"":list_8.get(0).get("v12").toString() ,coty));
//				        sheet_8.addCell(new Label( 17 , conut_8 ,list_8.get(0).get("v13")==null?"":list_8.get(0).get("v13").toString() ,coty));
//				        sheet_8.addCell(new Label( 18 , conut_8 ,list_8.get(0).get("v14")==null?"":list_8.get(0).get("v14").toString() ,coty));
//				        sheet_8.addCell(new Label( 19 , conut_8 ,list_8.get(0).get("v15")==null?"":list_8.get(0).get("v15").toString() ,coty));
//				        sheet_8.addCell(new Label( 20 , conut_8 ,list_8.get(0).get("v16")==null?"":list_8.get(0).get("v16").toString() ,coty));
//				        sheet_8.addCell(new Label( 21 , conut_8 ,list_8.get(0).get("v17")==null?"":list_8.get(0).get("v17").toString() ,coty));
//				        sheet_8.addCell(new Label( 22 , conut_8 ,list_8.get(0).get("v18")==null?"":list_8.get(0).get("v18").toString() ,coty));
//				        
//				        sheet_4.addCell(new Label( 19 , conut_8 ,list_8.get(0).get("v40")==null?"":list_8.get(0).get("v40").toString() ,coty));
//				        sheet_4.addCell(new Label( 20 , conut_8 ,list_8.get(0).get("v41")==null?"":list_8.get(0).get("v41").toString() ,coty));
//				        sheet_4.addCell(new Label( 21 , conut_8 ,list_8.get(0).get("v42")==null?"":list_8.get(0).get("v42").toString() ,coty));
//				        sheet_4.addCell(new Label( 22 , conut_8 ,list_8.get(0).get("v43")==null?"":list_8.get(0).get("v43").toString() ,coty));
//				        
//				        sheet_8.addCell(new Label( 23 , conut_8 ,list_8.get(0).get("v19")==null?"":list_8.get(0).get("v19").toString() ,coty));
//				        sheet_8.addCell(new Label( 24 , conut_8 ,list_8.get(0).get("v20")==null?"":list_8.get(0).get("v20").toString() ,coty));
//				        sheet_8.addCell(new Label( 25 , conut_8 ,list_8.get(0).get("v21")==null?"":list_8.get(0).get("v21").toString() ,coty));
//				        sheet_8.addCell(new Label( 26 , conut_8 ,list_8.get(0).get("v22")==null?"":list_8.get(0).get("v22").toString() ,coty));
//				        sheet_8.addCell(new Label( 27 , conut_8 ,list_8.get(0).get("v23")==null?"":list_8.get(0).get("v23").toString() ,coty));
//				        sheet_8.addCell(new Label( 28 , conut_8 ,list_8.get(0).get("v24")==null?"":list_8.get(0).get("v24").toString() ,coty));
//				        sheet_8.addCell(new Label( 29 , conut_8 ,list_8.get(0).get("v25")==null?"":list_8.get(0).get("v25").toString() ,coty));
//				        sheet_8.addCell(new Label( 30 , conut_8 ,list_8.get(0).get("v26")==null?"":list_8.get(0).get("v26").toString() ,coty));
//				        sheet_8.addCell(new Label( 31 , conut_8 ,list_8.get(0).get("v35")==null?"":list_8.get(0).get("v35").toString() ,coty));
//				        sheet_8.addCell(new Label( 32 , conut_8 ,list_8.get(0).get("v27")==null?"":list_8.get(0).get("v27").toString() ,coty));
//				        sheet_8.addCell(new Label( 33 , conut_8 ,list_8.get(0).get("v28")==null?"":list_8.get(0).get("v28").toString() ,coty));
//				        sheet_8.addCell(new Label( 34 , conut_8 ,list_8.get(0).get("v36")==null?"":list_8.get(0).get("v36").toString() ,coty));
//				        sheet_8.addCell(new Label( 35 , conut_8 ,list_8.get(0).get("v29")==null?"":list_8.get(0).get("v29").toString() ,coty));
//				        sheet_8.addCell(new Label( 36 , conut_8 ,list_8.get(0).get("v30")==null?"":list_8.get(0).get("v30").toString() ,coty));
//				        sheet_8.addCell(new Label( 37 , conut_8 ,list_8.get(0).get("v37")==null?"":list_8.get(0).get("v37").toString() ,coty));
//				        sheet_8.addCell(new Label( 38 , conut_8 ,list_8.get(0).get("v31")==null?"":list_8.get(0).get("v31").toString() ,coty));
//				        sheet_8.addCell(new Label( 39 , conut_8 ,list_8.get(0).get("v32")==null?"":list_8.get(0).get("v32").toString() ,coty));
//				        sheet_8.addCell(new Label( 40 , conut_8 ,list_8.get(0).get("v38")==null?"":list_8.get(0).get("v38").toString() ,coty));
//				        sheet_8.addCell(new Label( 41 , conut_8 ,list_8.get(0).get("v33")==null?"":list_8.get(0).get("v33").toString() ,coty));
//				        sheet_8.addCell(new Label( 42 , conut_8 ,list_8.get(0).get("v34")==null?"":list_8.get(0).get("v34").toString() ,coty));
//				        sheet_8.addCell(new Label( 43 , conut_8 ,list_8.get(0).get("v39")==null?"":list_8.get(0).get("v39").toString() ,coty));
//				        
//				        //帮扶后支出
//				        String bfh_sql="select v1 cv1,v2 cv2,v3 cv3,v4 cv4,v5 cv5,v6 cv6,v7 cv7,v8 cv8,v9 cv9,v10 cv10,v11 cv11,"+
//				        				"v12 cv12,v13 cv13,v14 cv14,v15 cv15,v16 cv16,v17 cv17,v18 cv18,v19 cv19,v20 cv20,v21 cv21,v22 cv22,"+
//				        				"v23 cv23,v24 cv24,v25 cv25,v26 cv26,v27 cv27,v28 cv28,v29 cv29,v30 cv30,v31 cv31,da_household_id "+
//				        				"from da_helpback_expenditure where da_household_id="+s1_map.get("pkid");
//				        SQLAdapter bfh_sqlAdapter=new SQLAdapter(bfh_sql);
//				        List<Map> bfh_list=this.getBySqlMapper.findRecords(bfh_sqlAdapter);
//				        sheet_8.addCell(new Label( 44 , conut_8 ,bfh_list.get(0).get("cv1")==null?"":bfh_list.get(0).get("cv1").toString() ,coty));
//				        sheet_8.addCell(new Label( 45 , conut_8 ,bfh_list.get(0).get("cv2")==null?"":bfh_list.get(0).get("cv2").toString() ,coty));
//				        sheet_8.addCell(new Label( 46 , conut_8 ,bfh_list.get(0).get("cv3")==null?"":bfh_list.get(0).get("cv3").toString() ,coty));
//				        sheet_8.addCell(new Label( 47 , conut_8 ,bfh_list.get(0).get("cv4")==null?"":bfh_list.get(0).get("cv4").toString() ,coty));
//				        sheet_8.addCell(new Label( 48 , conut_8 ,bfh_list.get(0).get("cv5")==null?"":bfh_list.get(0).get("cv5").toString() ,coty));
//				        sheet_8.addCell(new Label( 49 , conut_8 ,bfh_list.get(0).get("cv6")==null?"":bfh_list.get(0).get("cv6").toString() ,coty));
//				        sheet_8.addCell(new Label( 50 , conut_8 ,bfh_list.get(0).get("cv7")==null?"":bfh_list.get(0).get("cv7").toString() ,coty));
//				        sheet_8.addCell(new Label( 51 , conut_8 ,bfh_list.get(0).get("cv8")==null?"":bfh_list.get(0).get("cv8").toString() ,coty));
//				        sheet_8.addCell(new Label( 52 , conut_8 ,bfh_list.get(0).get("cv9")==null?"":bfh_list.get(0).get("cv9").toString() ,coty));
//				        sheet_8.addCell(new Label( 53 , conut_8 ,bfh_list.get(0).get("cv10")==null?"":bfh_list.get(0).get("cv10").toString() ,coty));
//				        sheet_8.addCell(new Label( 54 , conut_8 ,bfh_list.get(0).get("cv11")==null?"":bfh_list.get(0).get("cv11").toString() ,coty));
//				        sheet_8.addCell(new Label( 55 , conut_8 ,bfh_list.get(0).get("cv12")==null?"":bfh_list.get(0).get("cv12").toString() ,coty));
//				        sheet_8.addCell(new Label( 56 , conut_8 ,bfh_list.get(0).get("cv13")==null?"":bfh_list.get(0).get("cv13").toString() ,coty));
//				        sheet_8.addCell(new Label( 57 , conut_8 ,bfh_list.get(0).get("cv14")==null?"":bfh_list.get(0).get("cv14").toString() ,coty));
//				        sheet_8.addCell(new Label( 58 , conut_8 ,bfh_list.get(0).get("cv15")==null?"":bfh_list.get(0).get("cv15").toString() ,coty));
//				        sheet_8.addCell(new Label( 59 , conut_8 ,bfh_list.get(0).get("cv16")==null?"":bfh_list.get(0).get("cv16").toString() ,coty));
//				        sheet_8.addCell(new Label( 60 , conut_8 ,bfh_list.get(0).get("cv17")==null?"":bfh_list.get(0).get("cv17").toString() ,coty));
//				        sheet_8.addCell(new Label( 61 , conut_8 ,bfh_list.get(0).get("cv18")==null?"":bfh_list.get(0).get("cv18").toString() ,coty));
//				        sheet_8.addCell(new Label( 62 , conut_8 ,bfh_list.get(0).get("cv19")==null?"":bfh_list.get(0).get("cv19").toString() ,coty));
//				        sheet_8.addCell(new Label( 63 , conut_8 ,bfh_list.get(0).get("cv20")==null?"":bfh_list.get(0).get("cv20").toString() ,coty));
//				        sheet_8.addCell(new Label( 64 , conut_8 ,bfh_list.get(0).get("cv21")==null?"":bfh_list.get(0).get("cv21").toString() ,coty));
//				        sheet_8.addCell(new Label( 65 , conut_8 ,bfh_list.get(0).get("cv22")==null?"":bfh_list.get(0).get("cv22").toString() ,coty));
//				        sheet_8.addCell(new Label( 66 , conut_8 ,bfh_list.get(0).get("cv23")==null?"":bfh_list.get(0).get("cv23").toString() ,coty));
//				        sheet_8.addCell(new Label( 67 , conut_8 ,bfh_list.get(0).get("cv24")==null?"":bfh_list.get(0).get("cv24").toString() ,coty));
//				        sheet_8.addCell(new Label( 68 , conut_8 ,bfh_list.get(0).get("cv25")==null?"":bfh_list.get(0).get("cv25").toString() ,coty));
//				        sheet_8.addCell(new Label( 69 , conut_8 ,bfh_list.get(0).get("cv26")==null?"":bfh_list.get(0).get("cv26").toString() ,coty));
//				        sheet_8.addCell(new Label( 70 , conut_8 ,bfh_list.get(0).get("cv27")==null?"":bfh_list.get(0).get("cv27").toString() ,coty));
//				        sheet_8.addCell(new Label( 71 , conut_8 ,bfh_list.get(0).get("cv28")==null?"":bfh_list.get(0).get("cv28").toString() ,coty));
//				        sheet_8.addCell(new Label( 72 , conut_8 ,bfh_list.get(0).get("cv29")==null?"":bfh_list.get(0).get("cv29").toString() ,coty));
//				        sheet_8.addCell(new Label( 73 , conut_8 ,bfh_list.get(0).get("cv30")==null?"":bfh_list.get(0).get("cv30").toString() ,coty));
//				        sheet_8.addCell(new Label( 74 , conut_8 ,bfh_list.get(0).get("cv31")==null?"":bfh_list.get(0).get("cv31").toString() ,coty));
//				        String aa="";
//						String bb="";
//						if("".equals(list_8.get(0).get("v39"))||list_8.get(0).get("v39")==null){
//							aa="0";
//						}else{
//							aa=list_8.get(0).get("v39").toString();
//						}
//						if("".equals(bfh_list.get(0).get("cv31"))||bfh_list.get(0).get("cv31")==null){
//							bb="0";
//						}else{
//							bb=bfh_list.get(0).get("cv31").toString();
//						}
//						double c_c=Double.parseDouble(aa);
//						double c1_c=Double.parseDouble(bb);
//						double c2_c=c_c-c1_c;
//						String ncsr_c=String.format("%.2f", c2_c);
//						double cc_c=Double.parseDouble(s1_map.get("v9").toString());
//						double cnum_c=c2_c/cc_c;
//						String str1_c=String.format("%.2f", cnum_c);
//				        sheet_8.addCell(new Label( 75 , conut_8 ,ncsr_c ,coty));
//				        sheet_8.addCell(new Label( 76 , conut_8 ,str1_c,coty));
//				        sheet_8.setRowView(conut_8, 500); // 设置第一行的高度
//				        conut_8++;
		        
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	/**
	 * 导出帮扶单位和帮扶人
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all3(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company"+year+" t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				
		        //帮扶单位和责任人
		        WritableSheet sheet_5 = book.createSheet( "帮扶单位和责任人" , 0);
		        sheet_5.addCell(new Label( 0,0 , "家庭编号", tsty));
		        sheet_5.addCell(new Label( 1,0 , "旗区", tsty));
		        sheet_5.addCell(new Label( 2,0 , "苏木乡", tsty));
		        sheet_5.addCell(new Label( 3,0 , "嘎查村", tsty));
		        sheet_5.addCell(new Label( 4,0 , "户主姓名", tsty));
		        sheet_5.addCell(new Label( 5,0 , "帮扶人姓名", tsty));
		        sheet_5.addCell(new Label( 6,0 , "单位", tsty));
		        sheet_5.addCell(new Label( 7,0 , "职务", tsty));
		        sheet_5.addCell(new Label( 8,0 , "电话", tsty));
		        sheet_5.addCell(new Label( 9,0 , "帮扶目标", tsty));
		        sheet_5.addCell(new Label( 10,0 , "帮扶时限", tsty));
		        sheet_5.addCell(new Label( 11,0 , "帮扶计划", tsty));
		        sheet_5.setRowView(0, 500);
		        sheet_5.setColumnView(1, 20);
		        sheet_5.setColumnView(2, 20);
		        sheet_5.setColumnView(3, 20);
		        sheet_5.setColumnView(4, 20);
		        sheet_5.setColumnView(5, 20);
		        sheet_5.setColumnView(6, 30);
		        sheet_5.setColumnView(7, 20);
		        sheet_5.setColumnView(8, 20);
		        sheet_5.setColumnView(9, 650);
		        sheet_5.setColumnView(10, 20);
		        sheet_5.setColumnView(11, 650);
		        sheet_5.getSettings().setHorizontalFreeze(5);
		        sheet_5.getSettings().setVerticalFreeze(1);
		        int conut_5= 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        //帮扶人
			        String bfr_sql="SELECT da_household_id,telephone ,col_post, col_name,t2.v1 FROM sys_personal_household_many"+year+" a"+
			        			" LEFT JOIN sys_personal"+year+" b ON a.sys_personal_id = b.pkid join da_company"+year+" t2 on b.da_company_id=t2.pkid where da_household_id="+s1_map.get("pkid");
			        SQLAdapter bfr_sqlAdapter=new SQLAdapter(bfr_sql);
			        List<Map> bfr_list=this.getBySqlMapper.findRecords(bfr_sqlAdapter);
			        int jh_count=conut_5;
			        if(bfr_list.size()>0){
			        	for(int d=0;d<bfr_list.size();d++){
			        		sheet_5.addCell(new Label(0, conut_5 ,"".equals(bfr_list.get(d).get("da_household_id"))||bfr_list.get(d).get("da_household_id")==null?"":bfr_list.get(d).get("da_household_id").toString() ,coty));
			        		sheet_5.addCell(new Label(1, conut_5 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        		sheet_5.addCell(new Label(2, conut_5 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        		sheet_5.addCell(new Label(3, conut_5 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        		sheet_5.addCell(new Label(4, conut_5 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        		sheet_5.addCell(new Label(5, conut_5 ,bfr_list.get(d).get("col_name")==null?"":bfr_list.get(d).get("col_name").toString() ,coty));
			        		sheet_5.addCell(new Label(6, conut_5 ,bfr_list.get(d).get("v1")==null?"":bfr_list.get(d).get("v1").toString() ,coty));
			        		sheet_5.addCell(new Label(7, conut_5 ,bfr_list.get(d).get("col_post")==null?"":bfr_list.get(d).get("col_post").toString() ,coty));
			        		sheet_5.addCell(new Label(8, conut_5 ,bfr_list.get(d).get("telephone")==null?"":bfr_list.get(d).get("telephone").toString() ,coty));
			        		sheet_5.setRowView(conut_5, 500); // 设置第一行的高度
			        		conut_5++;
			        	}
			        }else{
			        	sheet_5.addCell(new Label(0, conut_5 ,s1_map.get("pkid").toString() ,coty));
			        	sheet_5.addCell(new Label(1, conut_5 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        		sheet_5.addCell(new Label(2, conut_5 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        		sheet_5.addCell(new Label(3, conut_5 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        		sheet_5.addCell(new Label(4, conut_5 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        		sheet_5.addCell(new Label(5, conut_5 ,"",coty));
		        		sheet_5.addCell(new Label(6, conut_5 ,"",coty));
		        		sheet_5.addCell(new Label(7, conut_5 ,"",coty));
		        		sheet_5.addCell(new Label(8, conut_5 ,"",coty));
		        		sheet_5.setRowView(conut_5, 500); // 设置第一行的高度
		        		conut_5++;
			        }
	        
			        //计划
			        String bfjh_sql="SELECT v1 ,v2 ,v3  from da_help_info where da_household_id="+s1_map.get("pkid");
			        SQLAdapter bfjh_sqlAdapter=new SQLAdapter(bfjh_sql);
			        List<Map> bfjh_list=this.getBySqlMapper.findRecords(bfjh_sqlAdapter);
			        if(bfjh_list.size()>0){
			        	if(bfjh_list.get(0)==null){
			        		sheet_5.mergeCells( 9,jh_count,9,conut_5-1 );
				        	sheet_5.addCell(new Label(9, jh_count ,"" ,coty));
				        	sheet_5.mergeCells( 10,jh_count,10,conut_5-1 );
				        	sheet_5.addCell(new Label(10, jh_count ,"",coty));
				        	sheet_5.mergeCells( 11,jh_count,11,conut_5-1 );
				        	sheet_5.addCell(new Label(11, jh_count ,"",coty));
			        	}else{
			        		sheet_5.mergeCells( 9,jh_count,9,conut_5-1 );
				        	sheet_5.addCell(new Label(9, jh_count ,"".equals(bfjh_list.get(0).get("v1"))||bfjh_list.get(0).get("v1")==null?"":bfjh_list.get(0).get("v1").toString() ,coty));
				        	sheet_5.mergeCells( 10,jh_count,10,conut_5-1 );
				        	sheet_5.addCell(new Label(10, jh_count ,"".equals(bfjh_list.get(0).get("v2"))||bfjh_list.get(0).get("v2")==null?"":bfjh_list.get(0).get("v2").toString() ,coty));
				        	sheet_5.mergeCells( 11,jh_count,11,conut_5-1 );
				        	sheet_5.addCell(new Label(11, jh_count ,"".equals(bfjh_list.get(0).get("v3"))||bfjh_list.get(0).get("v3")==null?"":bfjh_list.get(0).get("v3").toString() ,coty));
			        	}
			        	
			        }else{
			        	sheet_5.mergeCells( 9,jh_count,9,conut_5-1);
			        	sheet_5.addCell(new Label(9, jh_count ,"" ,coty));
			        	sheet_5.mergeCells( 10,jh_count,10,conut_5-1 );
			        	sheet_5.addCell(new Label(10, jh_count ,"",coty));
			        	sheet_5.mergeCells( 11,jh_count,11,conut_5-1 );
			        	sheet_5.addCell(new Label(11, jh_count ,"" ,coty));
			        }
		        
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	/**
	 * 导出走访记录—帮扶措施
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all4(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company"+year+" t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				
		        //走访记录
		        WritableSheet sheet_6 = book.createSheet( "帮扶人走访记录" , 0);
		        sheet_6.addCell(new Label( 0,0 , "家庭编号", tsty));
		        sheet_6.addCell(new Label( 1,0 , "旗区", tsty));
		        sheet_6.setColumnView(1, 20);
		        sheet_6.addCell(new Label( 2,0 , "苏木乡", tsty));
		        sheet_6.setColumnView(2, 20);
		        sheet_6.addCell(new Label( 3,0 , "嘎查村", tsty));
		        sheet_6.setColumnView(3, 20);
		        sheet_6.addCell(new Label( 4,0 , "户主姓名", tsty));
		        sheet_6.addCell(new Label( 5,0 , "走访时间", tsty));
		        sheet_6.addCell(new Label( 6,0 , "帮扶干部", tsty));
		        sheet_6.addCell(new Label( 7,0 , "走访情况记录", tsty));
		        sheet_6.setRowView(0, 500);
		        sheet_6.setColumnView(7, 650);
		        sheet_6.getSettings().setHorizontalFreeze(5);
		        sheet_6.getSettings().setVerticalFreeze(1);
		        
		        //帮扶措施
				WritableSheet sheet_cs = book.createSheet("帮扶措施", 1);
				sheet_cs.mergeCells(0, 0, 0,1);
				sheet_cs.addCell(new Label(0, 0, "家庭编号",tsty));
				
				sheet_cs.mergeCells(1, 0, 1,1);
				sheet_cs.addCell(new Label(1, 0, "旗区",tsty));
				sheet_cs.setColumnView(1, 20);
				sheet_cs.mergeCells(2, 0, 2,1);
				sheet_cs.addCell(new Label(2, 0, "苏木乡",tsty));
				sheet_cs.setColumnView(2, 20);
				sheet_cs.mergeCells(3, 0, 3,1);
				sheet_cs.addCell(new Label(3, 0, "嘎查村",tsty));
				sheet_cs.setColumnView(3, 20);
				sheet_cs.mergeCells(4, 0, 4,1);
				sheet_cs.addCell(new Label(4, 0, "户主姓名",tsty));
				
				sheet_cs.mergeCells(5, 0, 5, 1);
				sheet_cs.addCell(new Label(5, 0, "项目类别",tsty));
				sheet_cs.mergeCells(6, 0, 6, 1);
				sheet_cs.addCell(new Label(6, 0, "扶持措施",tsty));
				sheet_cs.mergeCells(7, 0, 7, 1);
				sheet_cs.addCell(new Label(7, 0, "是否符合扶持条件",tsty));
				
				sheet_cs.mergeCells(8, 0, 10, 0);
				sheet_cs.addCell(new Label(8, 0, "2016年",tsty));
				sheet_cs.addCell(new Label(8, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(9, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(10, 1, "项目类别",tsty));
				
				sheet_cs.mergeCells(11, 0, 13, 0 );
				sheet_cs.addCell(new Label(11, 0, "2017年",tsty));
				sheet_cs.addCell(new Label(11, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(12, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(13, 1, "落实时间",tsty));
				
				sheet_cs.mergeCells(14, 0, 16, 0);
				sheet_cs.addCell(new Label(14, 0, "2018年",tsty));
				sheet_cs.addCell(new Label(14, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(15, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(16, 1, "落实时间",tsty));
				sheet_cs.mergeCells(17, 0, 19, 0);
				sheet_cs.addCell(new Label(17, 0, "2019年",tsty));
				sheet_cs.addCell(new Label(17, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(18, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(19, 1, "落实时间",tsty));
				sheet_cs.setRowView(0, 500);
				sheet_cs.setRowView(1, 500);
				sheet_cs.getSettings().setHorizontalFreeze(5);
				sheet_cs.getSettings().setVerticalFreeze(2);
		        int conut_6= 1;
		        int conut_7= 2;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        //走访记录
			        String zf_sql="select v1,v2,v3,da_household_id from da_help_visit"+year+" where da_household_id="+s1_map.get("pkid");
			        SQLAdapter zf_sqlAdapter=new SQLAdapter (zf_sql);
			        List<Map> zf_list=this.getBySqlMapper.findRecords(zf_sqlAdapter);
			        if(zf_list.size()>0){
			        	for(int e=0;e<zf_list.size();e++){
			        		sheet_6.addCell(new Label(0, conut_6 ,zf_list.get(e).get("da_household_id")==null?"":zf_list.get(e).get("da_household_id").toString() ,coty));
			        		sheet_6.addCell(new Label(1, conut_6 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        		sheet_6.addCell(new Label(2, conut_6 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        		sheet_6.addCell(new Label(3, conut_6 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        		sheet_6.addCell(new Label(4, conut_6 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        		sheet_6.addCell(new Label(5, conut_6 ,zf_list.get(e).get("v1")==null?"":zf_list.get(e).get("v1").toString() ,coty));
			        		sheet_6.addCell(new Label(6, conut_6 ,zf_list.get(e).get("v2")==null?"":zf_list.get(e).get("v2").toString() ,coty));
			        		sheet_6.addCell(new Label(7, conut_6 ,zf_list.get(e).get("v3")==null?"":zf_list.get(e).get("v3").toString() ,coty));
			        		sheet_6.setRowView(conut_6, 500);
			        		conut_6++;
			        	}
			        }else{
			        	sheet_6.addCell(new Label(0, conut_6 ,s1_map.get("pkid").toString(),coty));
		        		sheet_6.addCell(new Label(1, conut_6 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        		sheet_6.addCell(new Label(2, conut_6 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString(),coty));
		        		sheet_6.addCell(new Label(3, conut_6 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        		sheet_6.addCell(new Label(4, conut_6 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        		sheet_6.addCell(new Label(5, conut_6 ,"" ,coty));
		        		sheet_6.addCell(new Label(6, conut_6 ,"" ,coty));
		        		sheet_6.addCell(new Label(7, conut_6 ,"" ,coty));
		        		sheet_6.setRowView(conut_6, 500);
		        		conut_6++;
			        }
	        
			        //帮扶措施
			        String cs_sql="select v1,v2,v3,da_household_id , MAX(CASE v7 WHEN '2016' THEN v4 ELSE '' END ) v4_2016, "+
			        		"MAX(CASE v7 WHEN '2016' THEN v5 ELSE '' END ) v5_2016,MAX(CASE v7 WHEN '2016' THEN v6 ELSE '' END ) v6_2016,  "+
			        		"MAX(CASE v7 WHEN '2017' THEN v4 ELSE '' END ) v4_2017,MAX(CASE v7 WHEN '2017' THEN v5 ELSE '' END ) v5_2017,"+
			        		"MAX(CASE v7 WHEN '2017' THEN v6 ELSE '' END ) v6_2017,MAX(CASE v7 WHEN '2018' THEN v4 ELSE '' END ) v4_2018, "+
			        		"MAX(CASE v7 WHEN '2018' THEN v5 ELSE '' END ) v5_2018,MAX(CASE v7 WHEN '2018' THEN v6 ELSE '' END ) v6_2018,"+
			        		"MAX(CASE v7 WHEN '2019' THEN v4 ELSE '' END ) v4_2019,MAX(CASE v7 WHEN '2019' THEN v5 ELSE '' END ) v5_2019, "+
			        		"MAX(CASE v7 WHEN '2019' THEN v6 ELSE '' END ) v6_2019 from da_help_tz_measures"+year+" where da_household_id="+s1_map.get("pkid")+" group  by v1,v2,v3 ";
	        
			        SQLAdapter cs_sqlAdapter=new SQLAdapter(cs_sql);
			        List<Map> cs_list=this.getBySqlMapper.findRecords(cs_sqlAdapter);
			        if(cs_list.size()>0){
			        	for(int f=0;f<cs_list.size();f++){
				        	sheet_cs.addCell(new Label(0, conut_7 ,cs_list.get(f).get("da_household_id")==null?"":cs_list.get(f).get("da_household_id").toString() ,coty));
				        	sheet_cs.addCell(new Label(1, conut_7 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
				        	sheet_cs.addCell(new Label(2, conut_7 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
				        	sheet_cs.addCell(new Label(3, conut_7 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
				        	sheet_cs.addCell(new Label(4, conut_7 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        	sheet_cs.addCell(new Label(5, conut_7 ,cs_list.get(f).get("v1")==null?"":cs_list.get(f).get("v1").toString() ,coty));
				        	sheet_cs.addCell(new Label(6, conut_7 ,cs_list.get(f).get("v2")==null?"":cs_list.get(f).get("v2").toString() ,coty));
				        	sheet_cs.addCell(new Label(7, conut_7 ,cs_list.get(f).get("v3")==null?"":cs_list.get(f).get("v3").toString() ,coty));
				        	sheet_cs.addCell(new Label(8, conut_7 ,cs_list.get(f).get("v4_2016")==null?"":cs_list.get(f).get("v4_2016").toString() ,coty));
				        	sheet_cs.addCell(new Label(9, conut_7 ,cs_list.get(f).get("v5_2016")==null?"":cs_list.get(f).get("v5_2016").toString() ,coty));
				        	sheet_cs.addCell(new Label(10, conut_7 ,cs_list.get(f).get("v6_2016")==null?"":cs_list.get(f).get("v6_2016").toString() ,coty));
				        	sheet_cs.addCell(new Label(11, conut_7 ,cs_list.get(f).get("v4_2017")==null?"":cs_list.get(f).get("v4_2017").toString() ,coty));
				        	sheet_cs.addCell(new Label(12, conut_7 ,cs_list.get(f).get("v5_2016")==null?"":cs_list.get(f).get("v5_2017").toString() ,coty));
				        	sheet_cs.addCell(new Label(13, conut_7 ,cs_list.get(f).get("v6_2017")==null?"":cs_list.get(f).get("v6_2017").toString() ,coty));
				        	sheet_cs.addCell(new Label(14, conut_7 ,cs_list.get(f).get("v4_2018")==null?"":cs_list.get(f).get("v4_2018").toString() ,coty));
				        	sheet_cs.addCell(new Label(15, conut_7 ,cs_list.get(f).get("v5_2018")==null?"":cs_list.get(f).get("v5_2018").toString() ,coty));
				        	sheet_cs.addCell(new Label(16, conut_7 ,cs_list.get(f).get("v6_2018")==null?"":cs_list.get(f).get("v6_2018").toString() ,coty));
				        	sheet_cs.addCell(new Label(17, conut_7 ,cs_list.get(f).get("v4_2019")==null?"":cs_list.get(f).get("v4_2019").toString() ,coty));
				        	sheet_cs.addCell(new Label(18, conut_7 ,cs_list.get(f).get("v5_2019")==null?"":cs_list.get(f).get("v5_2019").toString() ,coty));
				        	sheet_cs.addCell(new Label(19, conut_7 ,cs_list.get(f).get("v6_2019")==null?"":cs_list.get(f).get("v6_2019").toString() ,coty));
				        	sheet_cs.setRowView(conut_7, 500);
				        	conut_7++;
			        	}
			        }else{
			        	sheet_cs.addCell(new Label(0, conut_7 ,s1_map.get("pkid").toString(),coty));
			          	sheet_cs.addCell(new Label(1, conut_7 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        	sheet_cs.addCell(new Label(2, conut_7 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        	sheet_cs.addCell(new Label(3, conut_7 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        	sheet_cs.addCell(new Label(4, conut_7 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        	sheet_cs.addCell(new Label(5, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(6, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(7, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(8, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(9, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(10, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(11, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(12, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(13, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(14, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(15, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(16, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(17, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(18, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(19, conut_7 ,"",coty));
			        	sheet_cs.setRowView(conut_7, 500);
			        	conut_7++;
			        }
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	/**
	 * 导出帮扶成效
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all5(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company"+year+" t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				//帮扶成效
				WritableSheet sheet_9 = book.createSheet("帮扶成效", 0);
				sheet_9.addCell(new Label(0, 0, "家庭编号", tsty));
				sheet_9.addCell(new Label(1, 0, "旗区", tsty));
				sheet_9.setColumnView(1, 20);
				sheet_9.addCell(new Label(2, 0, "苏木乡", tsty));
				sheet_9.setColumnView(2, 20);
				sheet_9.addCell(new Label(3, 0, "嘎查村", tsty));
				sheet_9.setColumnView(3, 20);
				sheet_9.addCell(new Label(4, 0, "户主姓名", tsty));
				sheet_9.addCell(new Label(5, 0, "时间", tsty));
				sheet_9.addCell(new Label(6, 0, "成效内容", tsty));
				sheet_9.addCell(new Label(7, 0, "贫困户签字", tsty));
				sheet_9.setRowView(0, 500);
				sheet_9.setColumnView(6, 650);
				sheet_9.getSettings().setHorizontalFreeze(5);
				sheet_9.getSettings().setVerticalFreeze(1);
		        int conut_9= 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        //帮扶成效
			        String cx_sql="select da_household_id,v1,v2,v3 from da_help_results"+year+" where da_household_id="+s1_map.get("pkid");
			        SQLAdapter cx_sqlAdapter=new SQLAdapter(cx_sql);
			        List<Map> cx_list=this.getBySqlMapper.findRecords(cx_sqlAdapter);
			        if(cx_list.size()>0){
			        	for ( int j = 0 ; j < cx_list .size() ; j ++ ){
			        		 sheet_9.addCell(new Label( 0, conut_9 ,cx_list.get(j).get("da_household_id")==null?"":cx_list.get(j).get("da_household_id").toString() ,coty));
			        		  sheet_9.addCell(new Label(1, conut_9 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        		  sheet_9.addCell(new Label(2, conut_9 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        		  sheet_9.addCell(new Label(3, conut_9,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        		  sheet_9.addCell(new Label(4, conut_9 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        		  sheet_9.addCell(new Label( 5, conut_9 ,cx_list.get(j).get("v1")==null?"":cx_list.get(j).get("v1").toString() ,coty));
			        		  sheet_9.addCell(new Label( 6, conut_9 ,cx_list.get(j).get("v2")==null?"":cx_list.get(j).get("v2").toString() ,coty));
			        		  sheet_9.addCell(new Label( 7, conut_9 ,cx_list.get(j).get("v3")==null?"":cx_list.get(j).get("v3").toString() ,coty));
			        		  sheet_9.setRowView(conut_9, 500); // 设置第一行的高度
			        		  conut_9++;
			        	}
		        		 
			        }else{
			        	  sheet_9.addCell(new Label( 0,conut_9, s1_map.get("pkid").toString() ,coty));
			        	  sheet_9.addCell(new Label(1, conut_9 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        		  sheet_9.addCell(new Label(2, conut_9 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        		  sheet_9.addCell(new Label(3, conut_9 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        		  sheet_9.addCell(new Label(4, conut_9 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        		  sheet_9.addCell(new Label( 5, conut_9 ,"" ,coty));
		        		  sheet_9.addCell(new Label( 6, conut_9 ,"" ,coty));
		        		  sheet_9.addCell(new Label( 7, conut_9 ,"" ,coty));
		        		  sheet_9.setRowView(conut_9, 500); // 设置第一行的高度
		        		  conut_9++;
			        }
		        
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	/**
	 * 导出帮扶后收入情况
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView exportExcel_all6(HttpServletRequest request,HttpServletResponse response) throws IOException{
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
				
//				coty.setLocked(true);
//				coty.setIndentation(4);
	          
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company"+year+" t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				//帮扶后收支
				WritableSheet sheet_8 = book.createSheet("帮扶后收支分析", 0);
				sheet_8.mergeCells(0, 0, 0, 2);
				sheet_8.addCell(new Label(0, 0, "家庭编号", tsty));
				
				sheet_8.mergeCells(1, 0, 1, 2);
				sheet_8.addCell(new Label(1, 0, "旗区", tsty));
				sheet_8.setColumnView(1, 20);
				sheet_8.mergeCells(2, 0, 2, 2);
				sheet_8.addCell(new Label(2, 0, "苏木乡", tsty));
				sheet_8.setColumnView(2, 20);
				sheet_8.mergeCells(3, 0, 3, 2);
				sheet_8.addCell(new Label(3, 0, "嘎查村", tsty));
				sheet_8.setColumnView(3, 20);
				sheet_8.mergeCells(4, 0, 4, 2);
				sheet_8.addCell(new Label(4, 0, "户主姓名", tsty));
				sheet_8.mergeCells(5, 0, 43, 0);
				sheet_8.addCell(new Label(5, 0, "帮扶后收入情况", tsty));
				sheet_8.mergeCells(44, 0, 74, 0);
				sheet_8.addCell(new Label(44, 0, "帮扶后支出情况", tsty));
				sheet_8.mergeCells(5, 1, 6, 1);
				sheet_8.addCell(new Label(5, 1, "农业（水产）", tsty));
				sheet_8.addCell(new Label(5, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(6, 2, "金额（元）", tsty));

				sheet_8.mergeCells(7, 1, 8, 1);
				sheet_8.addCell(new Label(7, 1, "畜牧业", tsty));
				sheet_8.addCell(new Label(7, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(8, 2, "金额（元）", tsty));
				//
				sheet_8.mergeCells(9, 1, 10, 1);
				sheet_8.addCell(new Label(9, 1, "林业", tsty));
				sheet_8.addCell(new Label(9, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(10, 2, "金额", tsty));
				sheet_8.mergeCells(11, 1, 12, 1);
				sheet_8.addCell(new Label(11, 1, "其他", tsty));
				sheet_8.addCell(new Label(11, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(12, 2, "金额", tsty));
				sheet_8.mergeCells(13, 1, 14, 1);
				sheet_8.addCell(new Label(13, 1, "小计", tsty));
				sheet_8.addCell(new Label(13, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(14, 2, "金额", tsty));
				sheet_8.mergeCells(15, 1, 16, 1);
				sheet_8.addCell(new Label(15, 1, "农林牧草、生态等补贴", tsty));
				sheet_8.addCell(new Label(15, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(16, 2, "金额", tsty));

				sheet_8.mergeCells(17, 1, 18, 1);
				sheet_8.addCell(new Label(17, 1, "养老金", tsty));
				sheet_8.addCell(new Label(17, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(18, 2, "金额", tsty));

				sheet_8.mergeCells(19, 1, 20, 1);
				sheet_8.addCell(new Label(19, 1, "低保（五保）补贴", tsty));
				sheet_8.addCell(new Label(19, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(20, 2, "金额", tsty));
				sheet_8.mergeCells(21, 1, 22, 1);
				sheet_8.addCell(new Label(21, 1, "燃煤补贴", tsty));
				sheet_8.addCell(new Label(21, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(22, 2, "金额", tsty));
				
//				sheet_8.mergeCells(19, 1, 20, 1);
//				sheet_8.addCell(new Label(19, 1, "五保金", tsty));
//				sheet_8.addCell(new Label(19, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(20, 2, "金额", tsty));
//				sheet_8.mergeCells(21, 1, 22, 1);
//				sheet_8.addCell(new Label(21, 1, "计划生育", tsty));
//				sheet_8.addCell(new Label(21, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(22, 2, "金额", tsty));
				
				sheet_8.mergeCells(23, 1, 24, 1);
				sheet_8.addCell(new Label(23, 1, "其他", tsty));
				sheet_8.addCell(new Label(23, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(24, 2, "金额", tsty));

				sheet_8.mergeCells(25, 1, 26, 1);
				sheet_8.addCell(new Label(25, 1, "小计", tsty));
				sheet_8.addCell(new Label(25, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(26, 2, "金额", tsty));
				sheet_8.mergeCells(27, 1, 28, 1);
				sheet_8.addCell(new Label(27, 1, "土地、草牧场流转", tsty));
				sheet_8.addCell(new Label(27, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(28, 2, "金额", tsty));
				sheet_8.mergeCells(29, 1, 30, 1);
				sheet_8.addCell(new Label(29, 1, "其他", tsty));
				sheet_8.addCell(new Label(29, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(30, 2, "金额", tsty));

				sheet_8.mergeCells(31, 1, 36, 1);
				sheet_8.addCell(new Label(31, 1, "工资性收入", tsty));
				sheet_8.addCell(new Label(31, 2, "项目", tsty));
				sheet_8.addCell(new Label(32, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(33, 2, "金额", tsty));
				sheet_8.addCell(new Label(34, 2, "项目", tsty));
				sheet_8.addCell(new Label(35, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(36, 2, "金额", tsty));

				sheet_8.mergeCells(37, 1, 42, 1);
				sheet_8.addCell(new Label(37, 1, "其他收入", tsty));
				sheet_8.addCell(new Label(37, 2, "项目", tsty));
				sheet_8.addCell(new Label(38, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(39, 2, "金额", tsty));
				sheet_8.addCell(new Label(40, 2, "项目", tsty));
				sheet_8.addCell(new Label(41, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(42, 2, "金额", tsty));

				sheet_8.mergeCells(43, 1, 43, 2);
				sheet_8.addCell(new Label(43, 1, "总收入合计", tsty));
				// 当前支出
				sheet_8.mergeCells(44, 1, 45, 1);
				sheet_8.addCell(new Label(44, 1, "农资费用", tsty));
				sheet_8.addCell(new Label(44, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(45, 2, "金额（元）", tsty));
				sheet_8.mergeCells(46, 1, 47, 1);
				sheet_8.addCell(new Label(46, 1, "固定财产折旧和租赁费", tsty));
				sheet_8.addCell(new Label(46, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(47, 2, "金额（元）", tsty));
				sheet_8.mergeCells(48, 1, 49, 1);
				sheet_8.addCell(new Label(48, 1, "水电燃料支出", tsty));
				sheet_8.addCell(new Label(48, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(49, 2, "金额（元）", tsty));
				sheet_8.mergeCells(50, 1, 51, 1);
				sheet_8.addCell(new Label(50, 1, "承包土地、草场费用", tsty));
				sheet_8.addCell(new Label(50, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(51, 2, "金额（元）", tsty));
				sheet_8.mergeCells(52, 1, 53, 1);
				sheet_8.addCell(new Label(52, 1, "饲草料", tsty));
				sheet_8.addCell(new Label(52, 2, "支出明细细", tsty));
				sheet_8.addCell(new Label(53, 2, "金额（元）", tsty));
				sheet_8.mergeCells(54, 1, 55, 1);
				sheet_8.addCell(new Label(54, 1, "防疫防治支出", tsty));
				sheet_8.addCell(new Label(54, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(55, 2, "金额（元）", tsty));
				sheet_8.mergeCells(56, 1, 57, 1);
				sheet_8.addCell(new Label(56, 1, "种（仔）畜", tsty));
				sheet_8.addCell(new Label(56, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(57, 2, "金额（元）", tsty));
				sheet_8.mergeCells(58, 1, 59, 1);
				sheet_8.addCell(new Label(58, 1, "销售费用和通讯费用", tsty));
				sheet_8.addCell(new Label(58, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(59, 2, "金额（元）", tsty));
				sheet_8.mergeCells(60, 1, 61, 1);
				sheet_8.addCell(new Label(60, 1, "借贷利息", tsty));
				sheet_8.addCell(new Label(60, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(61, 2, "金额（元）", tsty));
				sheet_8.mergeCells(62, 1, 67, 1);
				sheet_8.addCell(new Label(62, 1, "政策性支出", tsty));
				sheet_8.addCell(new Label(62, 2, "项目", tsty));
				sheet_8.addCell(new Label(63, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(64, 2, "金额（元）", tsty));
				sheet_8.addCell(new Label(65, 2, "项目", tsty));
				sheet_8.addCell(new Label(66, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(67, 2, "金额（元）", tsty));
				sheet_8.mergeCells(68, 1, 73, 1);
				sheet_8.addCell(new Label(68, 1, "其他支出", tsty));
				sheet_8.addCell(new Label(68, 2, "项目", tsty));
				sheet_8.addCell(new Label(69, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(70, 2, "金额（元）", tsty));
				sheet_8.addCell(new Label(71, 2, "项目", tsty));
				sheet_8.addCell(new Label(72, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(73, 2, "金额（元）", tsty));
				sheet_8.mergeCells(74, 1, 74, 2);
				sheet_8.addCell(new Label(74, 1, "总支出合计", tsty));
				sheet_8.mergeCells(75, 0, 75, 2);
				sheet_8.addCell(new Label(75, 0, "年纯收入", tsty));
				sheet_8.mergeCells(76, 0, 76, 2);
				sheet_8.addCell(new Label(76, 0, "年人均纯收入", tsty));
				sheet_8.setRowView(0, 500);
				sheet_8.setRowView(1, 500);
				sheet_8.setRowView(2, 500);
				SheetSettings ws=sheet_8.getSettings();
				ws.setHorizontalFreeze(5);//列
				ws.setVerticalFreeze(3);//行
		        int conut_4= 3;
		        int conut_8= 3;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        	
				        
				     
				        
				        
				        //帮扶后收支
				        String sql_8="select * from da_helpback_income"+year+" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter sqlAdapter_8=new SQLAdapter(sql_8);
				        List<Map> list_8=this.getBySqlMapper.findRecords(sqlAdapter_8);
				        sheet_8.addCell(new Label( 0 , conut_8 ,list_8.get(0).get("da_household_id")==null?"":list_8.get(0).get("da_household_id").toString() ,coty));
				        sheet_8.addCell(new Label(1, conut_8 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			          	sheet_8.addCell(new Label(2, conut_8 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			          	sheet_8.addCell(new Label(3, conut_8 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			          	sheet_8.addCell(new Label(4, conut_8 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        sheet_8.addCell(new Label( 5 , conut_8 ,list_8.get(0).get("v1")==null?"":list_8.get(0).get("v1").toString() ,coty));
				        sheet_8.addCell(new Label( 6 , conut_8 ,list_8.get(0).get("v2")==null?"":list_8.get(0).get("v2").toString() ,coty));
				        sheet_8.addCell(new Label( 7 , conut_8 ,list_8.get(0).get("v3")==null?"":list_8.get(0).get("v3").toString() ,coty));
				        sheet_8.addCell(new Label( 8 , conut_8 ,list_8.get(0).get("v4")==null?"":list_8.get(0).get("v4").toString() ,coty));
				        sheet_8.addCell(new Label( 9 , conut_8 ,list_8.get(0).get("v5")==null?"":list_8.get(0).get("v5").toString() ,coty));
				        sheet_8.addCell(new Label( 10 , conut_8 ,list_8.get(0).get("v6")==null?"":list_8.get(0).get("v6").toString() ,coty));
				        sheet_8.addCell(new Label( 11 , conut_8 ,list_8.get(0).get("v7")==null?"":list_8.get(0).get("v7").toString() ,coty));
				        sheet_8.addCell(new Label( 12 , conut_8 ,list_8.get(0).get("v8")==null?"":list_8.get(0).get("v8").toString() ,coty));
				        sheet_8.addCell(new Label( 13 , conut_8 ,list_8.get(0).get("v9")==null?"":list_8.get(0).get("v9").toString() ,coty));
				        sheet_8.addCell(new Label( 14 , conut_8 ,list_8.get(0).get("v10")==null?"":list_8.get(0).get("v10").toString() ,coty));
				        sheet_8.addCell(new Label( 15 , conut_8 ,list_8.get(0).get("v11")==null?"":list_8.get(0).get("v11").toString() ,coty));
				        sheet_8.addCell(new Label( 16 , conut_8 ,list_8.get(0).get("v12")==null?"":list_8.get(0).get("v12").toString() ,coty));
				        sheet_8.addCell(new Label( 17 , conut_8 ,list_8.get(0).get("v13")==null?"":list_8.get(0).get("v13").toString() ,coty));
				        sheet_8.addCell(new Label( 18 , conut_8 ,list_8.get(0).get("v14")==null?"":list_8.get(0).get("v14").toString() ,coty));
				        sheet_8.addCell(new Label( 19 , conut_8 ,list_8.get(0).get("v15")==null?"":list_8.get(0).get("v15").toString() ,coty));
				        sheet_8.addCell(new Label( 20 , conut_8 ,list_8.get(0).get("v16")==null?"":list_8.get(0).get("v16").toString() ,coty));
				        sheet_8.addCell(new Label( 21 , conut_8 ,list_8.get(0).get("v17")==null?"":list_8.get(0).get("v17").toString() ,coty));
				        sheet_8.addCell(new Label( 22 , conut_8 ,list_8.get(0).get("v18")==null?"":list_8.get(0).get("v18").toString() ,coty));
//				        
//				        sheet_4.addCell(new Label( 19 , conut_8 ,list_8.get(0).get("v40")==null?"":list_8.get(0).get("v40").toString() ,coty));
//				        sheet_4.addCell(new Label( 20 , conut_8 ,list_8.get(0).get("v41")==null?"":list_8.get(0).get("v41").toString() ,coty));
//				        sheet_4.addCell(new Label( 21 , conut_8 ,list_8.get(0).get("v42")==null?"":list_8.get(0).get("v42").toString() ,coty));
//				        sheet_4.addCell(new Label( 22 , conut_8 ,list_8.get(0).get("v43")==null?"":list_8.get(0).get("v43").toString() ,coty));
//				        
				        sheet_8.addCell(new Label( 23 , conut_8 ,list_8.get(0).get("v19")==null?"":list_8.get(0).get("v19").toString() ,coty));
				        sheet_8.addCell(new Label( 24 , conut_8 ,list_8.get(0).get("v20")==null?"":list_8.get(0).get("v20").toString() ,coty));
				        sheet_8.addCell(new Label( 25 , conut_8 ,list_8.get(0).get("v21")==null?"":list_8.get(0).get("v21").toString() ,coty));
				        sheet_8.addCell(new Label( 26 , conut_8 ,list_8.get(0).get("v22")==null?"":list_8.get(0).get("v22").toString() ,coty));
				        sheet_8.addCell(new Label( 27 , conut_8 ,list_8.get(0).get("v23")==null?"":list_8.get(0).get("v23").toString() ,coty));
				        sheet_8.addCell(new Label( 28 , conut_8 ,list_8.get(0).get("v24")==null?"":list_8.get(0).get("v24").toString() ,coty));
				        sheet_8.addCell(new Label( 29 , conut_8 ,list_8.get(0).get("v25")==null?"":list_8.get(0).get("v25").toString() ,coty));
				        sheet_8.addCell(new Label( 30 , conut_8 ,list_8.get(0).get("v26")==null?"":list_8.get(0).get("v26").toString() ,coty));
				        sheet_8.addCell(new Label( 31 , conut_8 ,list_8.get(0).get("v35")==null?"":list_8.get(0).get("v35").toString() ,coty));
				        sheet_8.addCell(new Label( 32 , conut_8 ,list_8.get(0).get("v27")==null?"":list_8.get(0).get("v27").toString() ,coty));
				        sheet_8.addCell(new Label( 33 , conut_8 ,list_8.get(0).get("v28")==null?"":list_8.get(0).get("v28").toString() ,coty));
				        sheet_8.addCell(new Label( 34 , conut_8 ,list_8.get(0).get("v36")==null?"":list_8.get(0).get("v36").toString() ,coty));
				        sheet_8.addCell(new Label( 35 , conut_8 ,list_8.get(0).get("v29")==null?"":list_8.get(0).get("v29").toString() ,coty));
				        sheet_8.addCell(new Label( 36 , conut_8 ,list_8.get(0).get("v30")==null?"":list_8.get(0).get("v30").toString() ,coty));
				        sheet_8.addCell(new Label( 37 , conut_8 ,list_8.get(0).get("v37")==null?"":list_8.get(0).get("v37").toString() ,coty));
				        sheet_8.addCell(new Label( 38 , conut_8 ,list_8.get(0).get("v31")==null?"":list_8.get(0).get("v31").toString() ,coty));
				        sheet_8.addCell(new Label( 39 , conut_8 ,list_8.get(0).get("v32")==null?"":list_8.get(0).get("v32").toString() ,coty));
				        sheet_8.addCell(new Label( 40 , conut_8 ,list_8.get(0).get("v38")==null?"":list_8.get(0).get("v38").toString() ,coty));
				        sheet_8.addCell(new Label( 41 , conut_8 ,list_8.get(0).get("v33")==null?"":list_8.get(0).get("v33").toString() ,coty));
				        sheet_8.addCell(new Label( 42 , conut_8 ,list_8.get(0).get("v34")==null?"":list_8.get(0).get("v34").toString() ,coty));
				        sheet_8.addCell(new Label( 43 , conut_8 ,list_8.get(0).get("v39")==null?"":list_8.get(0).get("v39").toString() ,coty));
//				        
//				        //帮扶后支出
				        String bfh_sql="select v1 cv1,v2 cv2,v3 cv3,v4 cv4,v5 cv5,v6 cv6,v7 cv7,v8 cv8,v9 cv9,v10 cv10,v11 cv11,"+
				        				"v12 cv12,v13 cv13,v14 cv14,v15 cv15,v16 cv16,v17 cv17,v18 cv18,v19 cv19,v20 cv20,v21 cv21,v22 cv22,"+
				        				"v23 cv23,v24 cv24,v25 cv25,v26 cv26,v27 cv27,v28 cv28,v29 cv29,v30 cv30,v31 cv31,da_household_id "+
				        				"from da_helpback_expenditure"+year+" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter bfh_sqlAdapter=new SQLAdapter(bfh_sql);
				        List<Map> bfh_list=this.getBySqlMapper.findRecords(bfh_sqlAdapter);
				        sheet_8.addCell(new Label( 44 , conut_8 ,bfh_list.get(0).get("cv1")==null?"":bfh_list.get(0).get("cv1").toString() ,coty));
				        sheet_8.addCell(new Label( 45 , conut_8 ,bfh_list.get(0).get("cv2")==null?"":bfh_list.get(0).get("cv2").toString() ,coty));
				        sheet_8.addCell(new Label( 46 , conut_8 ,bfh_list.get(0).get("cv3")==null?"":bfh_list.get(0).get("cv3").toString() ,coty));
				        sheet_8.addCell(new Label( 47 , conut_8 ,bfh_list.get(0).get("cv4")==null?"":bfh_list.get(0).get("cv4").toString() ,coty));
				        sheet_8.addCell(new Label( 48 , conut_8 ,bfh_list.get(0).get("cv5")==null?"":bfh_list.get(0).get("cv5").toString() ,coty));
				        sheet_8.addCell(new Label( 49 , conut_8 ,bfh_list.get(0).get("cv6")==null?"":bfh_list.get(0).get("cv6").toString() ,coty));
				        sheet_8.addCell(new Label( 50 , conut_8 ,bfh_list.get(0).get("cv7")==null?"":bfh_list.get(0).get("cv7").toString() ,coty));
				        sheet_8.addCell(new Label( 51 , conut_8 ,bfh_list.get(0).get("cv8")==null?"":bfh_list.get(0).get("cv8").toString() ,coty));
				        sheet_8.addCell(new Label( 52 , conut_8 ,bfh_list.get(0).get("cv9")==null?"":bfh_list.get(0).get("cv9").toString() ,coty));
				        sheet_8.addCell(new Label( 53 , conut_8 ,bfh_list.get(0).get("cv10")==null?"":bfh_list.get(0).get("cv10").toString() ,coty));
				        sheet_8.addCell(new Label( 54 , conut_8 ,bfh_list.get(0).get("cv11")==null?"":bfh_list.get(0).get("cv11").toString() ,coty));
				        sheet_8.addCell(new Label( 55 , conut_8 ,bfh_list.get(0).get("cv12")==null?"":bfh_list.get(0).get("cv12").toString() ,coty));
				        sheet_8.addCell(new Label( 56 , conut_8 ,bfh_list.get(0).get("cv13")==null?"":bfh_list.get(0).get("cv13").toString() ,coty));
				        sheet_8.addCell(new Label( 57 , conut_8 ,bfh_list.get(0).get("cv14")==null?"":bfh_list.get(0).get("cv14").toString() ,coty));
				        sheet_8.addCell(new Label( 58 , conut_8 ,bfh_list.get(0).get("cv15")==null?"":bfh_list.get(0).get("cv15").toString() ,coty));
				        sheet_8.addCell(new Label( 59 , conut_8 ,bfh_list.get(0).get("cv16")==null?"":bfh_list.get(0).get("cv16").toString() ,coty));
				        sheet_8.addCell(new Label( 60 , conut_8 ,bfh_list.get(0).get("cv17")==null?"":bfh_list.get(0).get("cv17").toString() ,coty));
				        sheet_8.addCell(new Label( 61 , conut_8 ,bfh_list.get(0).get("cv18")==null?"":bfh_list.get(0).get("cv18").toString() ,coty));
				        
				        sheet_8.addCell(new Label( 62 , conut_8 ,bfh_list.get(0).get("cv23")==null?"":bfh_list.get(0).get("cv23").toString() ,coty));
				        sheet_8.addCell(new Label( 63 , conut_8 ,bfh_list.get(0).get("cv19")==null?"":bfh_list.get(0).get("cv19").toString() ,coty));
				        sheet_8.addCell(new Label( 64 , conut_8 ,bfh_list.get(0).get("cv20")==null?"":bfh_list.get(0).get("cv20").toString() ,coty));
				        
				        sheet_8.addCell(new Label( 65 , conut_8 ,bfh_list.get(0).get("cv24")==null?"":bfh_list.get(0).get("cv24").toString() ,coty));
				        sheet_8.addCell(new Label( 66 , conut_8 ,bfh_list.get(0).get("cv21")==null?"":bfh_list.get(0).get("cv21").toString() ,coty));
				        sheet_8.addCell(new Label( 67 , conut_8 ,bfh_list.get(0).get("cv22")==null?"":bfh_list.get(0).get("cv22").toString() ,coty));
				        sheet_8.addCell(new Label( 68 , conut_8 ,bfh_list.get(0).get("cv25")==null?"":bfh_list.get(0).get("cv25").toString() ,coty));
				        sheet_8.addCell(new Label( 69 , conut_8 ,bfh_list.get(0).get("cv26")==null?"":bfh_list.get(0).get("cv26").toString() ,coty));
				        sheet_8.addCell(new Label( 70 , conut_8 ,bfh_list.get(0).get("cv27")==null?"":bfh_list.get(0).get("cv27").toString() ,coty));
				        sheet_8.addCell(new Label( 71 , conut_8 ,bfh_list.get(0).get("cv28")==null?"":bfh_list.get(0).get("cv28").toString() ,coty));
				        sheet_8.addCell(new Label( 72 , conut_8 ,bfh_list.get(0).get("cv29")==null?"":bfh_list.get(0).get("cv29").toString() ,coty));
				        sheet_8.addCell(new Label( 73 , conut_8 ,bfh_list.get(0).get("cv30")==null?"":bfh_list.get(0).get("cv30").toString() ,coty));
				        sheet_8.addCell(new Label( 74 , conut_8 ,bfh_list.get(0).get("cv31")==null?"":bfh_list.get(0).get("cv31").toString() ,coty));
				        String aa="";
						String bb="";
						if("".equals(list_8.get(0).get("v39"))||list_8.get(0).get("v39")==null){
							aa="0";
						}else{
							aa=list_8.get(0).get("v39").toString();
						}
						if("".equals(bfh_list.get(0).get("cv31"))||bfh_list.get(0).get("cv31")==null){
							bb="0";
						}else{
							bb=bfh_list.get(0).get("cv31").toString();
						}
						double c_c=Double.parseDouble(aa);
						double c1_c=Double.parseDouble(bb);
						double c2_c=c_c-c1_c;
						String ncsr_c=String.format("%.2f", c2_c);
						double cc_c=Double.parseDouble(s1_map.get("v9").toString());
						double cnum_c=c2_c/cc_c;
						String str1_c=String.format("%.2f", cnum_c);
				        sheet_8.addCell(new Label( 75 , conut_8 ,ncsr_c ,coty));
				        sheet_8.addCell(new Label( 76 , conut_8 ,str1_c,coty));
				        sheet_8.setRowView(conut_8, 500); // 设置第一行的高度
				        conut_8++;
		        
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	
	/**
	 * 导出所有信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView exportExcel_all7(HttpServletRequest request,HttpServletResponse response) throws IOException{
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
		String str="";
		String cha_v6 = "";//户主姓名
		String cha_v8 = "";//身份证号
		String cha_v8_1 = "";//最小年龄范围
		String cha_v8_2 = "";//最大年龄范围
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		String ss=request.getParameter("cha_bfdw");
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " t1.v6 like '%"+cha_v6+"%' and";
		}
		
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " t1.v8 like '%"+cha_v8+"%' and";
		}
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and (TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(t1.v8)>=18 and TIMESTAMPDIFF(year,substring(t1.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		
		if(request.getParameter("cha_qx")!=null&&!request.getParameter("cha_qx").equals("请选择")){
			cha_qx = request.getParameter("cha_qx").trim();
			str += " t1.v3 like '%"+cha_qx+"%' and";
		}
		if(request.getParameter("cha_smx")!=null&&!request.getParameter("cha_smx").equals("请选择")){
			cha_smx = request.getParameter("cha_smx").trim();
			str += " t1.v4 like '%"+cha_smx+"%' and";
		}
		if(request.getParameter("cha_gcc")!=null&&!request.getParameter("cha_gcc").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc").trim();
			str += " t1.v5 like '%"+cha_gcc+"%' and";
		}
		if(request.getParameter("cha_sbbz")!=null&&!request.getParameter("cha_sbbz").equals("请选择")){
			cha_sbbz = request.getParameter("cha_sbbz").trim();
			str += " t1.sys_standard like '%"+cha_sbbz+"%' and";
		}
		if(request.getParameter("cha_pksx")!=null&&!request.getParameter("cha_pksx").equals("请选择")){
			cha_pksx = request.getParameter("cha_pksx").trim();
			str += " t1.v22 like '%"+cha_pksx+"%' and";
		}
		if(request.getParameter("cha_zpyy")!=null&&!request.getParameter("cha_zpyy").equals("请选择")){
			cha_zpyy = request.getParameter("cha_zpyy").trim();
			str += " t1.v23 like '%"+cha_zpyy+"%' and";
		}
		if(request.getParameter("cha_mz")!=null&&!request.getParameter("cha_mz").equals("请选择")){
			cha_mz = request.getParameter("cha_mz").trim();
			str += " t1.v11 like '%"+cha_mz+"%' and";
		}
		if(request.getParameter("cha_renkou")!=null&&!request.getParameter("cha_renkou").equals("请选择")){
			cha_renkou = request.getParameter("cha_renkou").trim().substring(0,1);
			if("5".equals(cha_renkou)){
				str += " t1.v9>=5 and";
			}else{
				str += " t1.v9 like '%"+cha_renkou+"%' and";
			}
		}
		
		//如果易地扶贫搬迁条件被选择
		if(request.getParameter("cha_banqian")!=null&&!request.getParameter("cha_banqian").equals("请选择")){
			cha_banqian = request.getParameter("cha_banqian").trim();
			str += " t1.v21='"+cha_banqian+"' and";
		}
		
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
				
//				coty.setLocked(true);
//				coty.setIndentation(4);
				
				//贫困户基本信息
				String sql_1 = "select t1.pkid,t1.v3,t1.v4,t1.v5,t1.v6,t1.v8,t1.v9,t1.v22,t1.v29,t1.v30,t1.v31,t1.v23,t1.v33,t1.v25,t1.v26,t1.v27,t1.sys_standard,t2.basic_address,t2.basic_explain "
						+ "from da_household"+year+" t1 join da_household_basic"+year+" t2 on t1.pkid=t2.da_household_id ";
				
				if((request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals(""))||(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals(""))){
					if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
						cha_bfdw = request.getParameter("cha_bfdw").trim();
						str += " t3.v1 like '%"+cha_bfdw+"%' and";
					}
					if(request.getParameter("cha_bfzrr")!=null&&!request.getParameter("cha_bfzrr").equals("")){
						cha_bfzrr = request.getParameter("cha_bfzrr").trim();
						str += " c.col_name like '%"+cha_bfzrr+"%' and";
					}
					sql_1 += " LEFT JOIN sys_personal_household_many"+year+" x on x.da_household_id=t1.pkid LEFT JOIN sys_personal"+year+" c on x.sys_personal_id = c.pkid join da_company t3 on c.da_company_id=t3.pkid ";
				}
				
				
				if(str.equals("")){
					sql_1 += "order by t1.pkid";
				}else{
					sql_1 += " where "+str.substring(0, str.length()-3)+" order by t1.pkid";
				}
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				
				WritableSheet sheet_1 = book.createSheet( "贫困户基本信息 " , 0);//生成第一页工作表，参数0表示这是第一页
				
				int[] headerArrHight_1 = {13,20,25,20,20,30,20,15,20,30,35,10,30,10,20,13,25,30,30};
		        String headerArr_1[] = {"家庭编号","旗区","苏木乡","噶查村","户主姓名","证件号码","识别标准","贫困户属性","主要致贫原因","其他致贫原因","致贫原因说明","家庭人口","家庭住址"
		        		,"是否军烈属","是否独生子女户","是否双女户","联系电话","开户银行名称","银行卡号"};
		        for (int i = 0; i < headerArr_1.length; i++) {
		        	sheet_1.addCell(new Label( i , 0 , headerArr_1[i], tsty));
		        	sheet_1.setColumnView(i, headerArrHight_1[i]);
		        }
		        sheet_1.setRowView(0, 500); // 设置第一行的高度
				sheet_1.getSettings().setHorizontalFreeze(5);
				sheet_1.getSettings().setVerticalFreeze(1);
				//生产生活
				WritableSheet sheet_2 = book.createSheet( "生产生活" , 1);
		        int[] headerArrHight_3 = {13,20,25,20,15,15,20,15,20,15,
		        						18,15,15,12,15,8,8,8,15,15,10,
		        						20,10,15,15,30,30,20,15,20,15};
		        String headerArr_3[] = {"家庭编号","旗区","苏木乡","噶查村","户主姓名","耕地面积（亩）","水浇地面积（亩）","林地面积（亩）","退耕还林面积（亩）","草牧场面积（亩）",
					        		"生产用房面积（㎡）","林果面积（亩）","水面面积（亩）","其他","家禽（只）","牛（头）","羊（只）","猪（头）","其他","住房面积（㎡）","是否危房",
					        		"是否纳入易地扶贫搬迁","饮水情况","饮水是否困难","饮水是否安全","通电情况","入户路类型","与主干路距离（公里）","主要燃料类型","是否加入农民专业合作社","有无卫生厕所"};
		        for (int i = 0; i < headerArr_3.length; i++) {
		        	sheet_2.addCell(new Label( i , 0 , headerArr_3[i], tsty));
		        	sheet_2.setColumnView(i, headerArrHight_3[i]);
		        }
		        sheet_2.setRowView(0, 500); // 设置第一行的高度
		        sheet_2.getSettings().setHorizontalFreeze(5);
		        sheet_2.getSettings().setVerticalFreeze(1);
		        
		        int conut = 1;
		        int conut_3 = 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
		        	sheet_1.addCell(new Label( 0 , conut ,s1_map.get("pkid")==null?"":s1_map.get("pkid").toString() ,coty));
		        	sheet_1.addCell(new Label( 1 , conut ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        	sheet_1.addCell(new Label( 2 , conut ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        	sheet_1.addCell(new Label( 3 , conut ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        	sheet_1.addCell(new Label( 4 , conut ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        	sheet_1.addCell(new Label( 5 , conut ,s1_map.get("v8")==null?"":s1_map.get("v8").toString() ,coty));
		        	sheet_1.addCell(new Label( 6 , conut ,s1_map.get("sys_standard")==null?"":s1_map.get("sys_standard").toString() ,coty));
		        	sheet_1.addCell(new Label( 7 , conut ,s1_map.get("v22")==null?"":s1_map.get("v22").toString() ,coty));
		        	sheet_1.addCell(new Label( 8 , conut ,s1_map.get("v23")==null?"":s1_map.get("v23").toString() ,coty));
		        	sheet_1.addCell(new Label( 9 , conut ,s1_map.get("v33")==null?"":s1_map.get("v33").toString() ,coty));
		        	sheet_1.addCell(new Label( 10 , conut ,s1_map.get("basic_explain")==null?"":s1_map.get("basic_explain").toString() ,coty));
		        	sheet_1.addCell(new Label( 11 , conut ,s1_map.get("v9")==null?"":s1_map.get("v9").toString() ,coty));
		        	sheet_1.addCell(new Label( 12 , conut ,s1_map.get("basic_address")==null?"":s1_map.get("basic_address").toString() ,coty));
		        	sheet_1.addCell(new Label( 13 , conut ,s1_map.get("v29")==null?"":s1_map.get("v29").toString() ,coty));
		        	sheet_1.addCell(new Label( 14 , conut ,s1_map.get("v30")==null?"":s1_map.get("v30").toString() ,coty));
		        	sheet_1.addCell(new Label( 15 , conut ,s1_map.get("v31")==null?"":s1_map.get("v31").toString() ,coty));
		        	sheet_1.addCell(new Label( 16 , conut ,s1_map.get("v25")==null?"":s1_map.get("v25").toString() ,coty));
		        	sheet_1.addCell(new Label( 17 , conut ,s1_map.get("v26")==null?"":s1_map.get("v26").toString() ,coty));
		        	sheet_1.addCell(new Label( 18 , conut ,s1_map.get("v27")==null?"":s1_map.get("v27").toString() ,coty));
		        	sheet_1.setRowView(conut, 500); // 设置第一行的高度
		        	conut++;
					//生产生活
		        	String sql_3="select da_household_id ,v1 pv1,v2 pv2,v3 pv3,v4 pv4,v5 pv5,v6 pv6,v7 pv7,v8 pv8,v9 pv9,v10 pv10,v11 pv11,v12 pv12,v13 pv13,"+
		        					"v14 pv14 from da_production"+year+" where da_household_id='"+s1_map.get("pkid")+"'";
							
		        	SQLAdapter s3_Adapter = new SQLAdapter(sql_3);
					List<Map> s3_List = this.getBySqlMapper.findRecords(s3_Adapter);
				        for (int a = 0; a < s3_List.size(); a++) {   //循环一个list里面的数据到excel中
				        	Map sc_map = s3_List.get(a);
				        	sheet_2.addCell(new Label( 0 , conut_3 ,s1_map.get("pkid")==null?"":s1_map.get("pkid").toString() ,coty));
				        	sheet_2.addCell(new Label( 1 , conut_3 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
				        	sheet_2.addCell(new Label( 2 , conut_3 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
				        	sheet_2.addCell(new Label( 3 , conut_3 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
				        	sheet_2.addCell(new Label( 4 , conut_3 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        	
				        	sheet_2.addCell(new Label( 5 , conut_3 ,sc_map.get("pv1")==null||"".equals(sc_map.get("pv1"))?"":sc_map.get("pv1").toString() ,coty));
				        	sheet_2.addCell(new Label( 6 , conut_3 ,sc_map.get("pv2")==null||"".equals(sc_map.get("pv2"))?"":sc_map.get("pv2").toString() ,coty));
				        	sheet_2.addCell(new Label( 7 , conut_3 ,sc_map.get("pv3")==null||"".equals(sc_map.get("pv3"))?"":sc_map.get("pv3").toString() ,coty));
				        	sheet_2.addCell(new Label( 8 , conut_3 ,sc_map.get("pv4")==null||"".equals(sc_map.get("pv4"))?"":sc_map.get("pv4").toString() ,coty));
				        	sheet_2.addCell(new Label( 9 , conut_3 ,sc_map.get("pv5")==null||"".equals(sc_map.get("pv5"))?"":sc_map.get("pv5").toString() ,coty));
				        	sheet_2.addCell(new Label( 10 , conut_3 ,sc_map.get("pv6")==null||"".equals(sc_map.get("pv6"))?"":sc_map.get("pv6").toString() ,coty));
				        	sheet_2.addCell(new Label( 11 , conut_3 ,sc_map.get("pv13")==null||"".equals(sc_map.get("pv13"))?"":sc_map.get("pv13").toString() ,coty));
				        	sheet_2.addCell(new Label( 12 , conut_3 ,sc_map.get("pv14")==null||"".equals(sc_map.get("pv14"))?"":sc_map.get("pv14").toString() ,coty));
				        	sheet_2.addCell(new Label( 13 , conut_3 ,sc_map.get("pv7")==null||"".equals(sc_map.get("pv7"))?"":sc_map.get("pv7").toString() ,coty));
				        	sheet_2.addCell(new Label( 14 , conut_3 ,sc_map.get("pv8")==null||"".equals(sc_map.get("pv8"))?"":sc_map.get("pv8").toString() ,coty));
				        	sheet_2.addCell(new Label( 15 , conut_3 ,sc_map.get("pv9")==null||"".equals(sc_map.get("pv9"))?"":sc_map.get("pv9").toString() ,coty));
				        	sheet_2.addCell(new Label( 16 , conut_3 ,sc_map.get("pv10")==null||"".equals(sc_map.get("pv10"))?"":sc_map.get("pv10").toString() ,coty));
				        	sheet_2.addCell(new Label( 17 , conut_3 ,sc_map.get("pv11")==null||"".equals(sc_map.get("pv11"))?"":sc_map.get("pv11").toString() ,coty));
				        	sheet_2.addCell(new Label( 18 , conut_3 ,sc_map.get("pv12")==null||"".equals(sc_map.get("pv12"))?"":sc_map.get("pv12").toString() ,coty));
				        	
				        
				        }
				        
				        //生活
				        String sh_sql="select da_household_id,v1 lv1,v2 lv2,v3 lv3,v4 lv4, v5 lv5,v6 lv6,v7 lv7,v8 lv8,v9 lv9,v10 lv10,v11 lv11,v12 lv12 from da_life"+year+" where  da_household_id='"+s1_map.get("pkid")+"'";
				        SQLAdapter sh_sqlAdapter=new SQLAdapter (sh_sql);
				        List<Map> sh_list=this.getBySqlMapper.findRecords(sh_sqlAdapter);
				        for (int a = 0; a < sh_list.size(); a++) {
				        	//循环一个list里面的数据到excel中
				        	Map sc_map = sh_list.get(a);
				        	sheet_2.addCell(new Label( 19 , conut_3 ,sc_map.get("lv1")==null||"".equals(sc_map.get("lv1"))?"":sc_map.get("lv1").toString() ,coty));
				        	sheet_2.addCell(new Label( 20 , conut_3 ,sc_map.get("lv2")==null||"".equals(sc_map.get("lv2"))?"":sc_map.get("lv2").toString() ,coty));
				        	sheet_2.addCell(new Label( 21 , conut_3 ,sc_map.get("lv3")==null||"".equals(sc_map.get("lv3"))?"":sc_map.get("lv3").toString() ,coty));
				        	sheet_2.addCell(new Label( 22 , conut_3 ,sc_map.get("lv4")==null||"".equals(sc_map.get("lv4"))?"":sc_map.get("lv4").toString() ,coty));
				        	sheet_2.addCell(new Label( 23 , conut_3 ,sc_map.get("lv8")==null||"".equals(sc_map.get("lv8"))?"":sc_map.get("lv8").toString() ,coty));
				        	sheet_2.addCell(new Label( 24 , conut_3 ,sc_map.get("lv9")==null||"".equals(sc_map.get("lv9"))?"":sc_map.get("lv9").toString() ,coty));
				        	sheet_2.addCell(new Label( 25 , conut_3 ,sc_map.get("lv5")==null||"".equals(sc_map.get("lv5"))?"":sc_map.get("lv5").toString() ,coty));
				        	sheet_2.addCell(new Label( 26 , conut_3 ,sc_map.get("lv6")==null||"".equals(sc_map.get("lv6"))?"":sc_map.get("lv6").toString() ,coty));
				        	sheet_2.addCell(new Label( 27 , conut_3 ,sc_map.get("lv7")==null||"".equals(sc_map.get("lv7"))?"":sc_map.get("lv7").toString() ,coty));
				        	sheet_2.addCell(new Label( 28 , conut_3 ,sc_map.get("lv10")==null||"".equals(sc_map.get("lv10"))?"":sc_map.get("lv10").toString() ,coty));
				        	sheet_2.addCell(new Label( 29 , conut_3 ,sc_map.get("lv11")==null||"".equals(sc_map.get("lv11"))?"":sc_map.get("lv11").toString() ,coty));
				        	sheet_2.addCell(new Label( 30 , conut_3 ,sc_map.get("lv12")==null||"".equals(sc_map.get("lv12"))?"":sc_map.get("lv12").toString() ,coty));
				        	sheet_2.setRowView(conut_3, 500); // 设置第一行的高度
				        	
				        	conut_3++;
				        }
		        }
		        //家庭成员
		        WritableSheet sheet_3 = book.createSheet( "家庭成员 " , 0);
		        
		        int[] headerArrHight_2 = {13,20,25,20,20,10,30,15,15,15,20,20,25,20,20,20,25,30,30,30};
		        String headerArr_2[] = {"家庭编号","旗区","苏木乡","噶查村","户主姓名","性别","证件号码","与户主关系","民族","文化程度","在校生状况","健康状况","劳动能力","务工状况","务工时间（月）","是否参加新农合",
		        		"是否参加新型养老保险","是否参加城镇职工基本养老保险","政治面貌","是否现役军人"};
		        
		        for (int i = 0; i < headerArr_2.length; i++) {
		        	sheet_3.addCell(new Label( i , 0 , headerArr_2[i], tsty));
		        	sheet_3.setColumnView(i, headerArrHight_2[i]);
		        }
//		        HSSFRow row =book.getSheet(0).getRow(1);
//		        setDataValidation(1,2);
		        sheet_3.setRowView(0, 500); // 设置第一行的高度
		        sheet_3.getSettings().setHorizontalFreeze(5);
		        sheet_3.getSettings().setVerticalFreeze(1);
		        int conut_2 = 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
		        	//家庭成员
					String sql_2 ="select pkid,v3,v4,v5,v6,v7,v8,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v28,v32, sys_standard from da_household"+year+" where pkid="+s1_map.get("pkid");
		        	SQLAdapter s2_sqlAdapter=new SQLAdapter(sql_2);
		        	List<Map> s2_list=this.getBySqlMapper.findRecords(s2_sqlAdapter);
		        	sheet_3.addCell(new Label( 0 , conut_2 ,s2_list.get(0).get("pkid")==null?"":s2_list.get(0).get("pkid").toString() ,coty));
		        	sheet_3.addCell(new Label( 1 , conut_2 ,s2_list.get(0).get("v3")==null?"":s2_list.get(0).get("v3").toString() ,coty));
		        	sheet_3.addCell(new Label( 2 , conut_2 ,s2_list.get(0).get("v4")==null?"":s2_list.get(0).get("v4").toString() ,coty));
		        	sheet_3.addCell(new Label( 3 , conut_2 ,s2_list.get(0).get("v5")==null?"":s2_list.get(0).get("v5").toString() ,coty));
		        	sheet_3.addCell(new Label( 4 , conut_2 ,s2_list.get(0).get("v6")==null?"":s2_list.get(0).get("v6").toString() ,coty));
		        	sheet_3.addCell(new Label( 5 , conut_2 ,s2_list.get(0).get("v7")==null?"":s2_list.get(0).get("v7").toString() ,coty));
		        	sheet_3.addCell(new Label( 6 , conut_2 ,s2_list.get(0).get("v8")==null?"":s2_list.get(0).get("v8").toString() ,coty));
		        	sheet_3.addCell(new Label( 7 , conut_2 ,s2_list.get(0).get("v10")==null?"":s2_list.get(0).get("v10").toString() ,coty));
		        	sheet_3.addCell(new Label( 8 , conut_2 ,s2_list.get(0).get("v11")==null?"":s2_list.get(0).get("v11").toString() ,coty));
		        	sheet_3.addCell(new Label( 9 , conut_2 ,s2_list.get(0).get("v12")==null?"":s2_list.get(0).get("v12").toString() ,coty));
		        	sheet_3.addCell(new Label( 10 , conut_2 ,s2_list.get(0).get("v13")==null?"":s2_list.get(0).get("v13").toString() ,coty));
		        	sheet_3.addCell(new Label( 11 , conut_2 ,s2_list.get(0).get("v14")==null?"":s2_list.get(0).get("v14").toString() ,coty));
		        	sheet_3.addCell(new Label( 12 , conut_2 ,s2_list.get(0).get("v15")==null?"":s2_list.get(0).get("v15").toString() ,coty));
		        	sheet_3.addCell(new Label( 13 , conut_2 ,s2_list.get(0).get("v16")==null?"":s2_list.get(0).get("v16").toString() ,coty));
		        	sheet_3.addCell(new Label( 14 , conut_2 ,s2_list.get(0).get("v17")==null?"":s2_list.get(0).get("v17").toString() ,coty));
		        	sheet_3.addCell(new Label( 15 , conut_2 ,s2_list.get(0).get("v18")==null?"":s2_list.get(0).get("v18").toString() ,coty));
		        	sheet_3.addCell(new Label( 16 , conut_2 ,s2_list.get(0).get("v19")==null?"":s2_list.get(0).get("v19").toString() ,coty));
		        	sheet_3.addCell(new Label( 17 , conut_2 ,s2_list.get(0).get("v20")==null?"":s2_list.get(0).get("v20").toString() ,coty));
		        	sheet_3.addCell(new Label( 18 , conut_2 ,s2_list.get(0).get("v28")==null?"":s2_list.get(0).get("v28").toString() ,coty));
		        	sheet_3.addCell(new Label( 19 , conut_2 ,s2_list.get(0).get("v32")==null?"":s2_list.get(0).get("v32").toString() ,coty));
		        	sheet_3.setRowView(conut_2, 500); // 设置第一行的高度
		        	conut_2++;
		        	String cha_sql="select da_household_id, v3,v4,v5,v6,v7,v8,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v28,v32 from da_member"+year+" where da_household_id="+s1_map.get("pkid");
		        	SQLAdapter sqlAdapter=new SQLAdapter(cha_sql);
		        	List<Map> cha_list=this.getBySqlMapper.findRecords(sqlAdapter);
		        	if(cha_list.size()>0){
		        		for(int j=0;j<cha_list.size();j++){
			        		
		        			sheet_3.addCell(new Label( 0 , conut_2 ,cha_list.get(j).get("da_household_id")==null?"":cha_list.get(j).get("da_household_id").toString() ,coty));
		        			sheet_3.addCell(new Label( 1 , conut_2 ,cha_list.get(j).get("v3")==null?"":cha_list.get(j).get("v3").toString() ,coty));
		        			sheet_3.addCell(new Label( 2 , conut_2 ,cha_list.get(j).get("v4")==null?"":cha_list.get(j).get("v4").toString() ,coty));
		        			sheet_3.addCell(new Label( 3 , conut_2 ,cha_list.get(j).get("v5")==null?"":cha_list.get(j).get("v5").toString() ,coty));
		        			sheet_3.addCell(new Label( 4 , conut_2 ,cha_list.get(j).get("v6")==null?"":cha_list.get(j).get("v6").toString() ,coty));
		        			sheet_3.addCell(new Label( 5 , conut_2 ,cha_list.get(j).get("v7")==null?"":cha_list.get(j).get("v7").toString() ,coty));
		        			sheet_3.addCell(new Label( 6 , conut_2 ,cha_list.get(j).get("v8")==null?"":cha_list.get(j).get("v8").toString() ,coty));
		        			sheet_3.addCell(new Label( 7 , conut_2 ,cha_list.get(j).get("v10")==null?"":cha_list.get(j).get("v10").toString() ,coty));
		        			sheet_3.addCell(new Label( 8 , conut_2 ,cha_list.get(j).get("v11")==null?"":cha_list.get(j).get("v11").toString() ,coty));
		        			sheet_3.addCell(new Label( 9 , conut_2 ,cha_list.get(j).get("v12")==null?"":cha_list.get(j).get("v12").toString() ,coty));
		        			sheet_3.addCell(new Label( 10 , conut_2 ,cha_list.get(j).get("v13")==null?"":cha_list.get(j).get("v13").toString() ,coty));
		        			sheet_3.addCell(new Label( 11 , conut_2 ,cha_list.get(j).get("v14")==null?"":cha_list.get(j).get("v14").toString() ,coty));
		        			sheet_3.addCell(new Label( 12 , conut_2 ,cha_list.get(j).get("v15")==null?"":cha_list.get(j).get("v15").toString() ,coty));
		        			sheet_3.addCell(new Label( 13 , conut_2 ,cha_list.get(j).get("v16")==null?"":cha_list.get(j).get("v16").toString() ,coty));
		        			sheet_3.addCell(new Label( 14 , conut_2 ,cha_list.get(j).get("v17")==null?"":cha_list.get(j).get("v17").toString() ,coty));
		        			sheet_3.addCell(new Label( 15 , conut_2 ,cha_list.get(j).get("v18")==null?"":cha_list.get(j).get("v18").toString() ,coty));
		        			sheet_3.addCell(new Label( 16 , conut_2 ,cha_list.get(j).get("v19")==null?"":cha_list.get(j).get("v19").toString() ,coty));
		        			sheet_3.addCell(new Label( 17 , conut_2 ,cha_list.get(j).get("v20")==null?"":cha_list.get(j).get("v20").toString() ,coty));
		        			sheet_3.addCell(new Label( 18 , conut_2 ,cha_list.get(j).get("v28")==null?"":cha_list.get(j).get("v28").toString() ,coty));
		        			sheet_3.addCell(new Label( 19 , conut_2 ,cha_list.get(j).get("v32")==null?"":cha_list.get(j).get("v32").toString() ,coty));
		        			sheet_3.setRowView(conut_2, 500); // 设置第一行的高度
				        	conut_2++;
			        	}
		        	}
		        }
		        //当前收支分析
		        WritableSheet sheet_4 = book.createSheet( "当前收支分析" , 0);
//		        
		        sheet_4.mergeCells( 0 ,0 , 0 , 2 );
		        sheet_4.addCell(new Label( 0 , 0 , "家庭编号", tsty));
		        
		        sheet_4.mergeCells( 1 ,0 , 1 , 2 );
		        sheet_4.addCell(new Label( 1 , 0, "旗区", tsty));
		        sheet_4.setColumnView(1, 20);
		        sheet_4.mergeCells( 2 ,0 , 2 , 2 );
		        sheet_4.addCell(new Label( 2 , 0 , "苏木乡", tsty));
		        sheet_4.setColumnView(2, 20);
		        sheet_4.mergeCells( 3 ,0 , 3 , 2 );
		        sheet_4.addCell(new Label( 3 , 0 , "嘎查村", tsty));
		        sheet_4.setColumnView(3, 20);
		        sheet_4.mergeCells(4 ,0 , 4 , 2 );
		        sheet_4.addCell(new Label( 4 , 0 , "户主姓名", tsty));
		        
		        
		        sheet_4.mergeCells( 5 ,0 ,47 ,0 );
		        sheet_4.addCell(new Label( 5 , 0 , "当前收入情况", tsty));
		        sheet_4.mergeCells( 48 ,0 , 78 ,0 );
		        sheet_4.addCell(new Label( 48 , 0 , "当前支出情况", tsty));
		        sheet_4.mergeCells( 5 , 1 , 6 , 1 );
		        sheet_4.addCell(new Label( 5 , 1 , "农业（水产）", tsty));
		        sheet_4.addCell(new Label( 5 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 6 , 2 , "金额（元）", tsty));
		        
		        sheet_4.mergeCells( 7 , 1 , 8 , 1 );
		        sheet_4.addCell(new Label( 7 , 1 , "畜牧业", tsty));
		        sheet_4.addCell(new Label( 7 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 8 , 2 , "金额（元）", tsty));
//
		        sheet_4.mergeCells( 9 , 1 , 10 , 1 );
		        sheet_4.addCell(new Label( 9 , 1 , "林业", tsty));
		        sheet_4.addCell(new Label( 9, 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 10 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 11 , 1 , 12 , 1 );
		        sheet_4.addCell(new Label( 11, 1 , "其他", tsty));
		        sheet_4.addCell(new Label(11 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 12 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 13, 1 , 14 , 1 );
		        sheet_4.addCell(new Label( 13, 1 , "小计", tsty));
		        sheet_4.addCell(new Label(13 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 14 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 15 , 1 , 16, 1 );
		        sheet_4.addCell(new Label( 15, 1 , "农林牧草、生态等补贴", tsty));
		        sheet_4.addCell(new Label(15 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 16 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 17 , 1 , 18, 1 );
		        sheet_4.addCell(new Label( 17, 1 , "养老金", tsty));
		        sheet_4.addCell(new Label(17 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 18 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 19 , 1 , 20, 1 );
		        sheet_4.addCell(new Label( 19, 1 , "低保（五保）补贴", tsty));
		        sheet_4.addCell(new Label(19 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 20 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 21 , 1 , 22, 1 );
		        sheet_4.addCell(new Label( 21, 1 , "燃煤补贴", tsty));
		        sheet_4.addCell(new Label(21 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 22 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 23 , 1 , 24, 1 );
		        sheet_4.addCell(new Label( 23, 1 , "五保金", tsty));
		        sheet_4.addCell(new Label(23 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 24 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 25 , 1 , 26, 1 );
		        sheet_4.addCell(new Label( 25, 1 , "计划生育", tsty));
		        sheet_4.addCell(new Label(25, 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 26 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 27 , 1 , 28, 1 );
		        sheet_4.addCell(new Label( 27, 1 , "其他", tsty));
		        sheet_4.addCell(new Label(27 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 28 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 29 , 1 , 30, 1 );
		        sheet_4.addCell(new Label( 29, 1 , "小计", tsty));
		        sheet_4.addCell(new Label(29 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 30 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 31 , 1 , 32, 1 );
		        sheet_4.addCell(new Label( 31, 1 , "土地、草牧场流转", tsty));
		        sheet_4.addCell(new Label(31 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 32 , 2 , "金额", tsty));
		        sheet_4.mergeCells( 33 , 1 , 34, 1 );
		        sheet_4.addCell(new Label(33, 1 , "其他", tsty));
		        sheet_4.addCell(new Label(33 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 34 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 35 , 1 , 40, 1 );
		        sheet_4.addCell(new Label( 35 , 1 , "工资性收入", tsty));
		        sheet_4.addCell(new Label(35 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 36 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 37 , 2 , "金额", tsty));
		        sheet_4.addCell(new Label(38 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 39 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 40 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 41 , 1 ,46, 1 );
		        sheet_4.addCell(new Label(41 , 1 , "其他收入", tsty));
		        sheet_4.addCell(new Label(41 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 42 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label(43, 2 , "金额", tsty));
		        sheet_4.addCell(new Label(44 , 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 45 , 2 , "收入明细", tsty));
		        sheet_4.addCell(new Label( 46 , 2 , "金额", tsty));
		        
		        sheet_4.mergeCells( 47 , 1 , 47, 2 );
		        sheet_4.addCell(new Label(47 , 1 , "总收入合计", tsty));
		        //当前支出
		        sheet_4.mergeCells( 48 , 1 , 49 , 1 );
		        sheet_4.addCell(new Label( 48 , 1 , "农资费用", tsty));
		        sheet_4.addCell(new Label( 48 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 49 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 50 , 1 , 51 , 1 );
		        sheet_4.addCell(new Label( 50 , 1 , "固定财产折旧和租赁费", tsty));
		        sheet_4.addCell(new Label( 50 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 51 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 52, 1 , 53 , 1 );
		        sheet_4.addCell(new Label( 52 , 1 , "水电燃料支出", tsty));
		        sheet_4.addCell(new Label(52 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 53 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 54 , 1 , 55 , 1 );
		        sheet_4.addCell(new Label( 54 , 1 , "承包土地、草场费用", tsty));
		        sheet_4.addCell(new Label(54 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 55 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 56 , 1 , 57 , 1 );
		        sheet_4.addCell(new Label( 56 , 1 , "饲草料", tsty));
		        sheet_4.addCell(new Label(56 , 2 , "支出明细细", tsty));
		        sheet_4.addCell(new Label( 57 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 58 , 1 , 59 , 1 );
		        sheet_4.addCell(new Label( 58 , 1 , "防疫防治支出", tsty));
		        sheet_4.addCell(new Label(58 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 59 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 60 , 1 , 61 , 1 );
		        sheet_4.addCell(new Label( 60 , 1 , "种（仔）畜", tsty));
		        sheet_4.addCell(new Label(60 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 61 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 62 , 1 , 63 , 1 );
		        sheet_4.addCell(new Label( 62 , 1 , "销售费用和通讯费用", tsty));
		        sheet_4.addCell(new Label(62 , 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 63 , 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 64 , 1 , 65 , 1 );
		        sheet_4.addCell(new Label( 64, 1 , "借贷利息", tsty));
		        sheet_4.addCell(new Label(64, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 65, 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 66 , 1 ,71 , 1 );
		        sheet_4.addCell(new Label( 66, 1 , "政策性支出", tsty));
		        sheet_4.addCell(new Label(66, 2 , "项目", tsty));
		        sheet_4.addCell(new Label(67, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label(68, 2 , "金额（元）", tsty));
		        sheet_4.addCell(new Label(69, 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 70, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 71, 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 72 , 1 ,77 , 1 );
		        sheet_4.addCell(new Label( 72, 1 , "其他支出", tsty));
		        sheet_4.addCell(new Label(72, 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 73, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 74, 2 , "金额（元）", tsty));
		        sheet_4.addCell(new Label(75, 2 , "项目", tsty));
		        sheet_4.addCell(new Label( 76, 2 , "支出明细", tsty));
		        sheet_4.addCell(new Label( 77, 2 , "金额（元）", tsty));
		        sheet_4.mergeCells( 78 , 1 ,78 , 2 );
		        sheet_4.addCell(new Label( 78,1 , "总支出合计", tsty));
		        sheet_4.mergeCells( 79 , 0 ,79 , 2 );
		        sheet_4.addCell(new Label( 79,0 , "年纯收入", tsty));
		        sheet_4.mergeCells( 80 , 0 ,80 , 2 );
		        sheet_4.addCell(new Label( 80,0 , "年人均纯收入", tsty));
		        sheet_4.setRowView(0, 500);
		        sheet_4.setRowView(1, 500);
		        sheet_4.setRowView(2, 500);
				SheetSettings ws=sheet_4.getSettings();
				ws.setHorizontalFreeze(5);//列
				ws.setVerticalFreeze(3);//行
		        int conut_4= 3;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        	
				        
				        //当前收支分析
				        String sql_4="select * from da_current_income"+year+" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter sqlAdapter_4=new SQLAdapter(sql_4);
				        List<Map> list_4=this.getBySqlMapper.findRecords(sqlAdapter_4);
				        sheet_4.addCell(new Label( 0 , conut_4 ,list_4.get(0).get("da_household_id")==null?"":list_4.get(0).get("da_household_id").toString() ,coty));
				        
				        sheet_4.addCell(new Label( 1 , conut_4 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
				        sheet_4.addCell(new Label( 2 , conut_4 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString(),coty));
				        sheet_4.addCell(new Label(3 , conut_4 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString(),coty));
				        sheet_4.addCell(new Label( 4 , conut_4 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        
				        sheet_4.addCell(new Label( 5 , conut_4 ,list_4.get(0).get("v1")==null?"":list_4.get(0).get("v1").toString() ,coty));
				        sheet_4.addCell(new Label( 6 , conut_4 ,list_4.get(0).get("v2")==null?"":list_4.get(0).get("v2").toString() ,coty));
				        sheet_4.addCell(new Label( 7 , conut_4 ,list_4.get(0).get("v3")==null?"":list_4.get(0).get("v3").toString() ,coty));
				        sheet_4.addCell(new Label( 8 , conut_4 ,list_4.get(0).get("v4")==null?"":list_4.get(0).get("v4").toString() ,coty));
				        sheet_4.addCell(new Label( 9 , conut_4 ,list_4.get(0).get("v5")==null?"":list_4.get(0).get("v5").toString() ,coty));
				        sheet_4.addCell(new Label( 10 , conut_4 ,list_4.get(0).get("v6")==null?"":list_4.get(0).get("v6").toString() ,coty));
				        sheet_4.addCell(new Label( 11 , conut_4 ,list_4.get(0).get("v7")==null?"":list_4.get(0).get("v7").toString() ,coty));
				        sheet_4.addCell(new Label( 12 , conut_4 ,list_4.get(0).get("v8")==null?"":list_4.get(0).get("v8").toString() ,coty));
				        sheet_4.addCell(new Label( 13 , conut_4 ,list_4.get(0).get("v9")==null?"":list_4.get(0).get("v9").toString() ,coty));
				        sheet_4.addCell(new Label( 14 , conut_4 ,list_4.get(0).get("v10")==null?"":list_4.get(0).get("v10").toString() ,coty));
				        sheet_4.addCell(new Label( 15 , conut_4 ,list_4.get(0).get("v11")==null?"":list_4.get(0).get("v11").toString() ,coty));
				        sheet_4.addCell(new Label( 16 , conut_4 ,list_4.get(0).get("v12")==null?"":list_4.get(0).get("v12").toString() ,coty));
				        sheet_4.addCell(new Label( 17 , conut_4 ,list_4.get(0).get("v13")==null?"":list_4.get(0).get("v13").toString() ,coty));
				        sheet_4.addCell(new Label( 18 , conut_4 ,list_4.get(0).get("v14")==null?"":list_4.get(0).get("v14").toString() ,coty));
				        sheet_4.addCell(new Label( 19 , conut_4 ,list_4.get(0).get("v15")==null?"":list_4.get(0).get("v15").toString() ,coty));
				        sheet_4.addCell(new Label( 20 , conut_4 ,list_4.get(0).get("v16")==null?"":list_4.get(0).get("v16").toString() ,coty));
				        sheet_4.addCell(new Label( 21 , conut_4 ,list_4.get(0).get("v17")==null?"":list_4.get(0).get("v17").toString() ,coty));
				        sheet_4.addCell(new Label( 22 , conut_4 ,list_4.get(0).get("v18")==null?"":list_4.get(0).get("v18").toString() ,coty));
				        sheet_4.addCell(new Label( 23 , conut_4 ,list_4.get(0).get("v40")==null?"":list_4.get(0).get("v40").toString() ,coty));
				        sheet_4.addCell(new Label( 24 , conut_4 ,list_4.get(0).get("v41")==null?"":list_4.get(0).get("v41").toString() ,coty));
				        sheet_4.addCell(new Label( 25 , conut_4 ,list_4.get(0).get("v42")==null?"":list_4.get(0).get("v42").toString() ,coty));
				        sheet_4.addCell(new Label( 26 , conut_4 ,list_4.get(0).get("v43")==null?"":list_4.get(0).get("v43").toString() ,coty));
				        sheet_4.addCell(new Label( 27 , conut_4 ,list_4.get(0).get("v19")==null?"":list_4.get(0).get("v19").toString() ,coty));
				        sheet_4.addCell(new Label( 28 , conut_4 ,list_4.get(0).get("v20")==null?"":list_4.get(0).get("v20").toString() ,coty));
				        sheet_4.addCell(new Label( 29 , conut_4 ,list_4.get(0).get("v21")==null?"":list_4.get(0).get("v21").toString() ,coty));
				        sheet_4.addCell(new Label( 30 , conut_4 ,list_4.get(0).get("v22")==null?"":list_4.get(0).get("v22").toString() ,coty));
				        sheet_4.addCell(new Label( 31 , conut_4 ,list_4.get(0).get("v23")==null?"":list_4.get(0).get("v23").toString() ,coty));
				        sheet_4.addCell(new Label( 32 , conut_4 ,list_4.get(0).get("v24")==null?"":list_4.get(0).get("v24").toString() ,coty));
				        sheet_4.addCell(new Label( 33 , conut_4 ,list_4.get(0).get("v25")==null?"":list_4.get(0).get("v25").toString() ,coty));
				        sheet_4.addCell(new Label( 34 , conut_4 ,list_4.get(0).get("v26")==null?"":list_4.get(0).get("v26").toString() ,coty));
				        sheet_4.addCell(new Label( 35 , conut_4 ,list_4.get(0).get("v35")==null?"":list_4.get(0).get("v35").toString() ,coty));
				        sheet_4.addCell(new Label( 36 , conut_4 ,list_4.get(0).get("v27")==null?"":list_4.get(0).get("v27").toString() ,coty));
				        sheet_4.addCell(new Label( 37 , conut_4 ,list_4.get(0).get("v28")==null?"":list_4.get(0).get("v28").toString() ,coty));
				        sheet_4.addCell(new Label( 38 , conut_4 ,list_4.get(0).get("v36")==null?"":list_4.get(0).get("v36").toString() ,coty));
				        sheet_4.addCell(new Label( 39 , conut_4 ,list_4.get(0).get("v29")==null?"":list_4.get(0).get("v29").toString() ,coty));
				        sheet_4.addCell(new Label( 40 , conut_4 ,list_4.get(0).get("v30")==null?"":list_4.get(0).get("v30").toString() ,coty));
				        sheet_4.addCell(new Label( 41 , conut_4 ,list_4.get(0).get("v37")==null?"":list_4.get(0).get("v37").toString() ,coty));
				        sheet_4.addCell(new Label( 42 , conut_4 ,list_4.get(0).get("v31")==null?"":list_4.get(0).get("v31").toString() ,coty));
				        sheet_4.addCell(new Label( 43 , conut_4 ,list_4.get(0).get("v32")==null?"":list_4.get(0).get("v32").toString() ,coty));
				        sheet_4.addCell(new Label( 44 , conut_4 ,list_4.get(0).get("v38")==null?"":list_4.get(0).get("v38").toString() ,coty));
				        sheet_4.addCell(new Label( 45 , conut_4 ,list_4.get(0).get("v33")==null?"":list_4.get(0).get("v33").toString() ,coty));
				        sheet_4.addCell(new Label( 46 , conut_4 ,list_4.get(0).get("v34")==null?"":list_4.get(0).get("v34").toString() ,coty));
				        sheet_4.addCell(new Label( 47 , conut_4 ,list_4.get(0).get("v39")==null?"":list_4.get(0).get("v39").toString() ,coty));
				        //支出
				        String zc_sql="select v1 cv1,v2 cv2,v3 cv3,v4 cv4,v5 cv5,v6 cv6,v7 cv7,v8 cv8,v9 cv9,v10 cv10,v11 cv11,"+
				        				"v12 cv12,v13 cv13,v14 cv14,v15 cv15,v16 cv16,v17 cv17,v18 cv18,v19 cv19,v20 cv20,v21 cv21,v22 cv22,"+
				        				"v23 cv23,v24 cv24,v25 cv25,v26 cv26,v27 cv27,v28 cv28,v29 cv29,v30 cv30,v31 cv31,da_household_id from da_current_expenditure"+year+" "+
				        				" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter zc_sqlAdapter=new SQLAdapter(zc_sql);
				        List<Map> zc_list=this.getBySqlMapper.findRecords(zc_sqlAdapter);
				        
				        sheet_4.addCell(new Label( 48 , conut_4 ,zc_list.get(0).get("cv1")==null?"":zc_list.get(0).get("cv1").toString() ,coty));
				        sheet_4.addCell(new Label( 49 , conut_4 ,zc_list.get(0).get("cv2")==null?"":zc_list.get(0).get("cv2").toString() ,coty));
				        sheet_4.addCell(new Label( 50 , conut_4 ,zc_list.get(0).get("cv3")==null?"":zc_list.get(0).get("cv3").toString() ,coty));
				        sheet_4.addCell(new Label( 51 , conut_4 ,zc_list.get(0).get("cv4")==null?"":zc_list.get(0).get("cv4").toString() ,coty));
				        sheet_4.addCell(new Label( 52 , conut_4 ,zc_list.get(0).get("cv5")==null?"":zc_list.get(0).get("cv5").toString() ,coty));
				        sheet_4.addCell(new Label( 53 , conut_4 ,zc_list.get(0).get("cv6")==null?"":zc_list.get(0).get("cv6").toString() ,coty));
				        sheet_4.addCell(new Label( 54 , conut_4 ,zc_list.get(0).get("cv7")==null?"":zc_list.get(0).get("cv7").toString() ,coty));
				        sheet_4.addCell(new Label( 55 , conut_4 ,zc_list.get(0).get("cv8")==null?"":zc_list.get(0).get("cv8").toString() ,coty));
				        sheet_4.addCell(new Label( 56 , conut_4 ,zc_list.get(0).get("cv9")==null?"":zc_list.get(0).get("cv9").toString() ,coty));
				        sheet_4.addCell(new Label( 57 , conut_4 ,zc_list.get(0).get("cv10")==null?"":zc_list.get(0).get("cv10").toString() ,coty));
				        sheet_4.addCell(new Label( 58 , conut_4 ,zc_list.get(0).get("cv11")==null?"":zc_list.get(0).get("cv11").toString() ,coty));
				        sheet_4.addCell(new Label( 59 , conut_4 ,zc_list.get(0).get("cv12")==null?"":zc_list.get(0).get("cv12").toString() ,coty));
				        sheet_4.addCell(new Label( 60 , conut_4 ,zc_list.get(0).get("cv13")==null?"":zc_list.get(0).get("cv13").toString() ,coty));
				        sheet_4.addCell(new Label( 61 , conut_4 ,zc_list.get(0).get("cv14")==null?"":zc_list.get(0).get("cv14").toString() ,coty));
				        sheet_4.addCell(new Label(62 , conut_4 ,zc_list.get(0).get("cv15")==null?"":zc_list.get(0).get("cv15").toString() ,coty));
				        sheet_4.addCell(new Label( 63 , conut_4 ,zc_list.get(0).get("cv16")==null?"":zc_list.get(0).get("cv16").toString() ,coty));
				        sheet_4.addCell(new Label( 64 , conut_4 ,zc_list.get(0).get("cv17")==null?"":zc_list.get(0).get("cv17").toString() ,coty));
				        sheet_4.addCell(new Label( 65 , conut_4 ,zc_list.get(0).get("cv18")==null?"":zc_list.get(0).get("cv18").toString() ,coty));
				        sheet_4.addCell(new Label( 66 , conut_4 ,zc_list.get(0).get("cv19")==null?"":zc_list.get(0).get("cv19").toString() ,coty));
				        sheet_4.addCell(new Label( 67 , conut_4 ,zc_list.get(0).get("cv20")==null?"":zc_list.get(0).get("cv20").toString() ,coty));
				        sheet_4.addCell(new Label( 68 , conut_4 ,zc_list.get(0).get("cv21")==null?"":zc_list.get(0).get("cv21").toString() ,coty));
				        sheet_4.addCell(new Label( 69 , conut_4 ,zc_list.get(0).get("cv22")==null?"":zc_list.get(0).get("cv22").toString() ,coty));
				        sheet_4.addCell(new Label( 70 , conut_4 ,zc_list.get(0).get("cv23")==null?"":zc_list.get(0).get("cv23").toString() ,coty));
				        sheet_4.addCell(new Label( 71 , conut_4 ,zc_list.get(0).get("cv24")==null?"":zc_list.get(0).get("cv24").toString() ,coty));
				        sheet_4.addCell(new Label( 72 , conut_4 ,zc_list.get(0).get("cv25")==null?"":zc_list.get(0).get("cv25").toString() ,coty));
				        sheet_4.addCell(new Label( 73 , conut_4 ,zc_list.get(0).get("cv26")==null?"":zc_list.get(0).get("cv26").toString() ,coty));
				        sheet_4.addCell(new Label( 74 , conut_4 ,zc_list.get(0).get("cv27")==null?"":zc_list.get(0).get("cv27").toString() ,coty));
				        sheet_4.addCell(new Label( 75 , conut_4 ,zc_list.get(0).get("cv28")==null?"":zc_list.get(0).get("cv28").toString() ,coty));
				        sheet_4.addCell(new Label( 76 , conut_4 ,zc_list.get(0).get("cv29")==null?"":zc_list.get(0).get("cv29").toString() ,coty));
				        sheet_4.addCell(new Label( 77 , conut_4 ,zc_list.get(0).get("cv30")==null?"":zc_list.get(0).get("cv30").toString() ,coty));
				        sheet_4.addCell(new Label( 78 , conut_4 ,zc_list.get(0).get("cv31")==null?"":zc_list.get(0).get("cv31").toString() ,coty));
				        
				        String a="";
						String b="";
						if("".equals(list_4.get(0).get("v39"))||list_4.get(0).get("v39")==null){
							a="0";
						}else{
							a=list_4.get(0).get("v39").toString();
						}
						if("".equals(zc_list.get(0).get("cv31"))||zc_list.get(0).get("cv31")==null){
							b="0";
						}else{
							b=zc_list.get(0).get("cv31").toString();
						}
						double c=Double.parseDouble(a);
						double c1=Double.parseDouble(b);
						double c2=c-c1;
						String ncsr=String.format("%.2f", c2);
						double cc=Double.parseDouble(s1_map.get("v9").toString());
						double cnum=c2/cc;
						String str1=String.format("%.2f", cnum);
				        sheet_4.addCell(new Label( 79 , conut_4 ,ncsr ,coty));
				        sheet_4.addCell(new Label( 80 , conut_4 ,str1,coty));
				        sheet_4.setRowView(conut_4, 500); // 设置第一行的高度
				        conut_4++;
		        
		        }
		        //帮扶单位和责任人
		        WritableSheet sheet_5 = book.createSheet( "帮扶单位和责任人" , 0);
		        sheet_5.addCell(new Label( 0,0 , "家庭编号", tsty));
		        sheet_5.addCell(new Label( 1,0 , "旗区", tsty));
		        sheet_5.addCell(new Label( 2,0 , "苏木乡", tsty));
		        sheet_5.addCell(new Label( 3,0 , "嘎查村", tsty));
		        sheet_5.addCell(new Label( 4,0 , "户主姓名", tsty));
		        sheet_5.addCell(new Label( 5,0 , "帮扶人姓名", tsty));
		        sheet_5.addCell(new Label( 6,0 , "单位", tsty));
		        sheet_5.addCell(new Label( 7,0 , "职务", tsty));
		        sheet_5.addCell(new Label( 8,0 , "电话", tsty));
		        sheet_5.addCell(new Label( 9,0 , "帮扶目标", tsty));
		        sheet_5.addCell(new Label( 10,0 , "帮扶时限", tsty));
		        sheet_5.addCell(new Label( 11,0 , "帮扶计划", tsty));
		        sheet_5.setRowView(0, 500);
		        sheet_5.setColumnView(1, 20);
		        sheet_5.setColumnView(2, 20);
		        sheet_5.setColumnView(3, 20);
		        sheet_5.setColumnView(4, 20);
		        sheet_5.setColumnView(5, 20);
		        sheet_5.setColumnView(6, 30);
		        sheet_5.setColumnView(7, 20);
		        sheet_5.setColumnView(8, 20);
		        sheet_5.setColumnView(9, 650);
		        sheet_5.setColumnView(10, 20);
		        sheet_5.setColumnView(11, 650);
		        sheet_5.getSettings().setHorizontalFreeze(5);
		        sheet_5.getSettings().setVerticalFreeze(1);
		        int conut_5= 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        //帮扶人
			        String bfr_sql="SELECT da_household_id,telephone ,col_post, col_name,t2.v1 FROM sys_personal_household_many"+year+" a"+
			        			" LEFT JOIN sys_personal"+year+" b ON a.sys_personal_id = b.pkid join da_company"+year+" t2 on b.da_company_id=t2.pkid where da_household_id="+s1_map.get("pkid");
			        SQLAdapter bfr_sqlAdapter=new SQLAdapter(bfr_sql);
			        List<Map> bfr_list=this.getBySqlMapper.findRecords(bfr_sqlAdapter);
			        int jh_count=conut_5;
			        if(bfr_list.size()>0){
			        	for(int d=0;d<bfr_list.size();d++){
			        		sheet_5.addCell(new Label(0, conut_5 ,"".equals(bfr_list.get(d).get("da_household_id"))||bfr_list.get(d).get("da_household_id")==null?"":bfr_list.get(d).get("da_household_id").toString() ,coty));
			        		sheet_5.addCell(new Label(1, conut_5 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        		sheet_5.addCell(new Label(2, conut_5 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        		sheet_5.addCell(new Label(3, conut_5 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        		sheet_5.addCell(new Label(4, conut_5 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        		sheet_5.addCell(new Label(5, conut_5 ,bfr_list.get(d).get("col_name")==null?"":bfr_list.get(d).get("col_name").toString() ,coty));
			        		sheet_5.addCell(new Label(6, conut_5 ,bfr_list.get(d).get("v1")==null?"":bfr_list.get(d).get("v1").toString() ,coty));
			        		sheet_5.addCell(new Label(7, conut_5 ,bfr_list.get(d).get("col_post")==null?"":bfr_list.get(d).get("col_post").toString() ,coty));
			        		sheet_5.addCell(new Label(8, conut_5 ,bfr_list.get(d).get("telephone")==null?"":bfr_list.get(d).get("telephone").toString() ,coty));
			        		sheet_5.setRowView(conut_5, 500); // 设置第一行的高度
			        		conut_5++;
			        	}
			        }else{
			        	sheet_5.addCell(new Label(0, conut_5 ,s1_map.get("pkid").toString() ,coty));
			        	sheet_5.addCell(new Label(1, conut_5 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        		sheet_5.addCell(new Label(2, conut_5 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        		sheet_5.addCell(new Label(3, conut_5 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        		sheet_5.addCell(new Label(4, conut_5 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        		sheet_5.addCell(new Label(5, conut_5 ,"",coty));
		        		sheet_5.addCell(new Label(6, conut_5 ,"",coty));
		        		sheet_5.addCell(new Label(7, conut_5 ,"",coty));
		        		sheet_5.addCell(new Label(8, conut_5 ,"",coty));
		        		sheet_5.setRowView(conut_5, 500); // 设置第一行的高度
		        		conut_5++;
			        }
	        
			        //计划
			        String bfjh_sql="SELECT v1 ,v2 ,v3  from da_help_info where da_household_id="+s1_map.get("pkid");
			        SQLAdapter bfjh_sqlAdapter=new SQLAdapter(bfjh_sql);
			        List<Map> bfjh_list=this.getBySqlMapper.findRecords(bfjh_sqlAdapter);
			        if(bfjh_list.size()>0){
			        	if(bfjh_list.get(0)==null){
			        		sheet_5.mergeCells( 9,jh_count,9,conut_5-1 );
				        	sheet_5.addCell(new Label(9, jh_count ,"" ,coty));
				        	sheet_5.mergeCells( 10,jh_count,10,conut_5-1 );
				        	sheet_5.addCell(new Label(10, jh_count ,"",coty));
				        	sheet_5.mergeCells( 11,jh_count,11,conut_5-1 );
				        	sheet_5.addCell(new Label(11, jh_count ,"",coty));
			        	}else{
			        		sheet_5.mergeCells( 9,jh_count,9,conut_5-1 );
				        	sheet_5.addCell(new Label(9, jh_count ,"".equals(bfjh_list.get(0).get("v1"))||bfjh_list.get(0).get("v1")==null?"":bfjh_list.get(0).get("v1").toString() ,coty));
				        	sheet_5.mergeCells( 10,jh_count,10,conut_5-1 );
				        	sheet_5.addCell(new Label(10, jh_count ,"".equals(bfjh_list.get(0).get("v2"))||bfjh_list.get(0).get("v2")==null?"":bfjh_list.get(0).get("v2").toString() ,coty));
				        	sheet_5.mergeCells( 11,jh_count,11,conut_5-1 );
				        	sheet_5.addCell(new Label(11, jh_count ,"".equals(bfjh_list.get(0).get("v3"))||bfjh_list.get(0).get("v3")==null?"":bfjh_list.get(0).get("v3").toString() ,coty));
			        	}
			        	
			        }else{
			        	sheet_5.mergeCells( 9,jh_count,9,conut_5-1);
			        	sheet_5.addCell(new Label(9, jh_count ,"" ,coty));
			        	sheet_5.mergeCells( 10,jh_count,10,conut_5-1 );
			        	sheet_5.addCell(new Label(10, jh_count ,"",coty));
			        	sheet_5.mergeCells( 11,jh_count,11,conut_5-1 );
			        	sheet_5.addCell(new Label(11, jh_count ,"" ,coty));
			        }
		        
		        }
		        //走访记录
		        WritableSheet sheet_6 = book.createSheet( "帮扶人走访记录" , 0);
		        sheet_6.addCell(new Label( 0,0 , "家庭编号", tsty));
		        sheet_6.addCell(new Label( 1,0 , "旗区", tsty));
		        sheet_6.setColumnView(1, 20);
		        sheet_6.addCell(new Label( 2,0 , "苏木乡", tsty));
		        sheet_6.setColumnView(2, 20);
		        sheet_6.addCell(new Label( 3,0 , "嘎查村", tsty));
		        sheet_6.setColumnView(3, 20);
		        sheet_6.addCell(new Label( 4,0 , "户主姓名", tsty));
		        sheet_6.addCell(new Label( 5,0 , "走访时间", tsty));
		        sheet_6.addCell(new Label( 6,0 , "帮扶干部", tsty));
		        sheet_6.addCell(new Label( 7,0 , "走访情况记录", tsty));
		        sheet_6.setRowView(0, 500);
		        sheet_6.setColumnView(7, 650);
		        sheet_6.getSettings().setHorizontalFreeze(5);
		        sheet_6.getSettings().setVerticalFreeze(1);
		        
		        //帮扶措施
				WritableSheet sheet_cs = book.createSheet("帮扶措施", 1);
				sheet_cs.mergeCells(0, 0, 0,1);
				sheet_cs.addCell(new Label(0, 0, "家庭编号",tsty));
				
				sheet_cs.mergeCells(1, 0, 1,1);
				sheet_cs.addCell(new Label(1, 0, "旗区",tsty));
				sheet_cs.setColumnView(1, 20);
				sheet_cs.mergeCells(2, 0, 2,1);
				sheet_cs.addCell(new Label(2, 0, "苏木乡",tsty));
				sheet_cs.setColumnView(2, 20);
				sheet_cs.mergeCells(3, 0, 3,1);
				sheet_cs.addCell(new Label(3, 0, "嘎查村",tsty));
				sheet_cs.setColumnView(3, 20);
				sheet_cs.mergeCells(4, 0, 4,1);
				sheet_cs.addCell(new Label(4, 0, "户主姓名",tsty));
				
				sheet_cs.mergeCells(5, 0, 5, 1);
				sheet_cs.addCell(new Label(5, 0, "项目类别",tsty));
				sheet_cs.mergeCells(6, 0, 6, 1);
				sheet_cs.addCell(new Label(6, 0, "扶持措施",tsty));
				sheet_cs.mergeCells(7, 0, 7, 1);
				sheet_cs.addCell(new Label(7, 0, "是否符合扶持条件",tsty));
				
				sheet_cs.mergeCells(8, 0, 10, 0);
				sheet_cs.addCell(new Label(8, 0, "2016年",tsty));
				sheet_cs.addCell(new Label(8, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(9, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(10, 1, "项目类别",tsty));
				
				sheet_cs.mergeCells(11, 0, 13, 0 );
				sheet_cs.addCell(new Label(11, 0, "2017年",tsty));
				sheet_cs.addCell(new Label(11, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(12, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(13, 1, "落实时间",tsty));
				
				sheet_cs.mergeCells(14, 0, 16, 0);
				sheet_cs.addCell(new Label(14, 0, "2018年",tsty));
				sheet_cs.addCell(new Label(14, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(15, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(16, 1, "落实时间",tsty));
				sheet_cs.mergeCells(17, 0, 19, 0);
				sheet_cs.addCell(new Label(17, 0, "2019年",tsty));
				sheet_cs.addCell(new Label(17, 1, "项目需求量",tsty));
				sheet_cs.addCell(new Label(18, 1, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(19, 1, "落实时间",tsty));
				sheet_cs.setRowView(0, 500);
				sheet_cs.setRowView(1, 500);
				sheet_cs.getSettings().setHorizontalFreeze(5);
				sheet_cs.getSettings().setVerticalFreeze(2);
		        int conut_6= 1;
		        int conut_7= 2;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        //走访记录
			        String zf_sql="select v1,v2,v3,da_household_id from da_help_visit"+year+" where da_household_id="+s1_map.get("pkid");
			        SQLAdapter zf_sqlAdapter=new SQLAdapter (zf_sql);
			        List<Map> zf_list=this.getBySqlMapper.findRecords(zf_sqlAdapter);
			        if(zf_list.size()>0){
			        	for(int e=0;e<zf_list.size();e++){
			        		sheet_6.addCell(new Label(0, conut_6 ,zf_list.get(e).get("da_household_id")==null?"":zf_list.get(e).get("da_household_id").toString() ,coty));
			        		sheet_6.addCell(new Label(1, conut_6 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        		sheet_6.addCell(new Label(2, conut_6 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        		sheet_6.addCell(new Label(3, conut_6 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        		sheet_6.addCell(new Label(4, conut_6 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        		sheet_6.addCell(new Label(5, conut_6 ,zf_list.get(e).get("v1")==null?"":zf_list.get(e).get("v1").toString() ,coty));
			        		sheet_6.addCell(new Label(6, conut_6 ,zf_list.get(e).get("v2")==null?"":zf_list.get(e).get("v2").toString() ,coty));
			        		sheet_6.addCell(new Label(7, conut_6 ,zf_list.get(e).get("v3")==null?"":zf_list.get(e).get("v3").toString() ,coty));
			        		sheet_6.setRowView(conut_6, 500);
			        		conut_6++;
			        	}
			        }else{
			        	sheet_6.addCell(new Label(0, conut_6 ,s1_map.get("pkid").toString(),coty));
		        		sheet_6.addCell(new Label(1, conut_6 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        		sheet_6.addCell(new Label(2, conut_6 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString(),coty));
		        		sheet_6.addCell(new Label(3, conut_6 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        		sheet_6.addCell(new Label(4, conut_6 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        		sheet_6.addCell(new Label(5, conut_6 ,"" ,coty));
		        		sheet_6.addCell(new Label(6, conut_6 ,"" ,coty));
		        		sheet_6.addCell(new Label(7, conut_6 ,"" ,coty));
		        		sheet_6.setRowView(conut_6, 500);
		        		conut_6++;
			        }
	        
			        //帮扶措施
			        String cs_sql="select v1,v2,v3,da_household_id , MAX(CASE v7 WHEN '2016' THEN v4 ELSE '' END ) v4_2016, "+
			        		"MAX(CASE v7 WHEN '2016' THEN v5 ELSE '' END ) v5_2016,MAX(CASE v7 WHEN '2016' THEN v6 ELSE '' END ) v6_2016,  "+
			        		"MAX(CASE v7 WHEN '2017' THEN v4 ELSE '' END ) v4_2017,MAX(CASE v7 WHEN '2017' THEN v5 ELSE '' END ) v5_2017,"+
			        		"MAX(CASE v7 WHEN '2017' THEN v6 ELSE '' END ) v6_2017,MAX(CASE v7 WHEN '2018' THEN v4 ELSE '' END ) v4_2018, "+
			        		"MAX(CASE v7 WHEN '2018' THEN v5 ELSE '' END ) v5_2018,MAX(CASE v7 WHEN '2018' THEN v6 ELSE '' END ) v6_2018,"+
			        		"MAX(CASE v7 WHEN '2019' THEN v4 ELSE '' END ) v4_2019,MAX(CASE v7 WHEN '2019' THEN v5 ELSE '' END ) v5_2019, "+
			        		"MAX(CASE v7 WHEN '2019' THEN v6 ELSE '' END ) v6_2019 from da_help_tz_measures"+year+" where da_household_id="+s1_map.get("pkid")+" group  by v1,v2,v3 ";
	        
			        SQLAdapter cs_sqlAdapter=new SQLAdapter(cs_sql);
			        List<Map> cs_list=this.getBySqlMapper.findRecords(cs_sqlAdapter);
			        if(cs_list.size()>0){
			        	for(int f=0;f<cs_list.size();f++){
				        	sheet_cs.addCell(new Label(0, conut_7 ,cs_list.get(f).get("da_household_id")==null?"":cs_list.get(f).get("da_household_id").toString() ,coty));
				        	sheet_cs.addCell(new Label(1, conut_7 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
				        	sheet_cs.addCell(new Label(2, conut_7 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
				        	sheet_cs.addCell(new Label(3, conut_7 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
				        	sheet_cs.addCell(new Label(4, conut_7 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        	sheet_cs.addCell(new Label(5, conut_7 ,cs_list.get(f).get("v1")==null?"":cs_list.get(f).get("v1").toString() ,coty));
				        	sheet_cs.addCell(new Label(6, conut_7 ,cs_list.get(f).get("v2")==null?"":cs_list.get(f).get("v2").toString() ,coty));
				        	sheet_cs.addCell(new Label(7, conut_7 ,cs_list.get(f).get("v3")==null?"":cs_list.get(f).get("v3").toString() ,coty));
				        	sheet_cs.addCell(new Label(8, conut_7 ,cs_list.get(f).get("v4_2016")==null?"":cs_list.get(f).get("v4_2016").toString() ,coty));
				        	sheet_cs.addCell(new Label(9, conut_7 ,cs_list.get(f).get("v5_2016")==null?"":cs_list.get(f).get("v5_2016").toString() ,coty));
				        	sheet_cs.addCell(new Label(10, conut_7 ,cs_list.get(f).get("v6_2016")==null?"":cs_list.get(f).get("v6_2016").toString() ,coty));
				        	sheet_cs.addCell(new Label(11, conut_7 ,cs_list.get(f).get("v4_2017")==null?"":cs_list.get(f).get("v4_2017").toString() ,coty));
				        	sheet_cs.addCell(new Label(12, conut_7 ,cs_list.get(f).get("v5_2016")==null?"":cs_list.get(f).get("v5_2017").toString() ,coty));
				        	sheet_cs.addCell(new Label(13, conut_7 ,cs_list.get(f).get("v6_2017")==null?"":cs_list.get(f).get("v6_2017").toString() ,coty));
				        	sheet_cs.addCell(new Label(14, conut_7 ,cs_list.get(f).get("v4_2018")==null?"":cs_list.get(f).get("v4_2018").toString() ,coty));
				        	sheet_cs.addCell(new Label(15, conut_7 ,cs_list.get(f).get("v5_2018")==null?"":cs_list.get(f).get("v5_2018").toString() ,coty));
				        	sheet_cs.addCell(new Label(16, conut_7 ,cs_list.get(f).get("v6_2018")==null?"":cs_list.get(f).get("v6_2018").toString() ,coty));
				        	sheet_cs.addCell(new Label(17, conut_7 ,cs_list.get(f).get("v4_2019")==null?"":cs_list.get(f).get("v4_2019").toString() ,coty));
				        	sheet_cs.addCell(new Label(18, conut_7 ,cs_list.get(f).get("v5_2019")==null?"":cs_list.get(f).get("v5_2019").toString() ,coty));
				        	sheet_cs.addCell(new Label(19, conut_7 ,cs_list.get(f).get("v6_2019")==null?"":cs_list.get(f).get("v6_2019").toString() ,coty));
				        	sheet_cs.setRowView(conut_7, 500);
				        	conut_7++;
			        	}
			        }else{
			        	sheet_cs.addCell(new Label(0, conut_7 ,s1_map.get("pkid").toString(),coty));
			          	sheet_cs.addCell(new Label(1, conut_7 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        	sheet_cs.addCell(new Label(2, conut_7 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        	sheet_cs.addCell(new Label(3, conut_7 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        	sheet_cs.addCell(new Label(4, conut_7 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        	sheet_cs.addCell(new Label(5, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(6, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(7, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(8, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(9, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(10, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(11, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(12, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(13, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(14, conut_7 ,"" ,coty));
			        	sheet_cs.addCell(new Label(15, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(16, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(17, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(18, conut_7 ,"",coty));
			        	sheet_cs.addCell(new Label(19, conut_7 ,"",coty));
			        	sheet_cs.setRowView(conut_7, 500);
			        	conut_7++;
			        }
		        }
		      //帮扶成效
				WritableSheet sheet_9 = book.createSheet("帮扶成效", 0);
				sheet_9.addCell(new Label(0, 0, "家庭编号", tsty));
				sheet_9.addCell(new Label(1, 0, "旗区", tsty));
				sheet_9.setColumnView(1, 20);
				sheet_9.addCell(new Label(2, 0, "苏木乡", tsty));
				sheet_9.setColumnView(2, 20);
				sheet_9.addCell(new Label(3, 0, "嘎查村", tsty));
				sheet_9.setColumnView(3, 20);
				sheet_9.addCell(new Label(4, 0, "户主姓名", tsty));
				sheet_9.addCell(new Label(5, 0, "时间", tsty));
				sheet_9.addCell(new Label(6, 0, "成效内容", tsty));
				sheet_9.addCell(new Label(7, 0, "贫困户签字", tsty));
				sheet_9.setRowView(0, 500);
				sheet_9.setColumnView(6, 650);
				sheet_9.getSettings().setHorizontalFreeze(5);
				sheet_9.getSettings().setVerticalFreeze(1);
		        int conut_9= 1;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        //帮扶成效
			        String cx_sql="select da_household_id,v1,v2,v3 from da_help_results"+year+" where da_household_id="+s1_map.get("pkid");
			        SQLAdapter cx_sqlAdapter=new SQLAdapter(cx_sql);
			        List<Map> cx_list=this.getBySqlMapper.findRecords(cx_sqlAdapter);
			        if(cx_list.size()>0){
			        	for ( int j = 0 ; j < cx_list .size() ; j ++ ){
			        		 sheet_9.addCell(new Label( 0, conut_9 ,cx_list.get(j).get("da_household_id")==null?"":cx_list.get(j).get("da_household_id").toString() ,coty));
			        		  sheet_9.addCell(new Label(1, conut_9 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			        		  sheet_9.addCell(new Label(2, conut_9 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			        		  sheet_9.addCell(new Label(3, conut_9,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			        		  sheet_9.addCell(new Label(4, conut_9 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
			        		  sheet_9.addCell(new Label( 5, conut_9 ,cx_list.get(j).get("v1")==null?"":cx_list.get(j).get("v1").toString() ,coty));
			        		  sheet_9.addCell(new Label( 6, conut_9 ,cx_list.get(j).get("v2")==null?"":cx_list.get(j).get("v2").toString() ,coty));
			        		  sheet_9.addCell(new Label( 7, conut_9 ,cx_list.get(j).get("v3")==null?"":cx_list.get(j).get("v3").toString() ,coty));
			        		  sheet_9.setRowView(conut_9, 500); // 设置第一行的高度
			        		  conut_9++;
			        	}
		        		 
			        }else{
			        	  sheet_9.addCell(new Label( 0,conut_9, s1_map.get("pkid").toString() ,coty));
			        	  sheet_9.addCell(new Label(1, conut_9 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        		  sheet_9.addCell(new Label(2, conut_9 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        		  sheet_9.addCell(new Label(3, conut_9 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        		  sheet_9.addCell(new Label(4, conut_9 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        		  sheet_9.addCell(new Label( 5, conut_9 ,"" ,coty));
		        		  sheet_9.addCell(new Label( 6, conut_9 ,"" ,coty));
		        		  sheet_9.addCell(new Label( 7, conut_9 ,"" ,coty));
		        		  sheet_9.setRowView(conut_9, 500); // 设置第一行的高度
		        		  conut_9++;
			        }
		        
		        }
				//帮扶后收支
				WritableSheet sheet_8 = book.createSheet("帮扶后收支分析", 0);
				sheet_8.mergeCells(0, 0, 0, 2);
				sheet_8.addCell(new Label(0, 0, "家庭编号", tsty));
				
				sheet_8.mergeCells(1, 0, 1, 2);
				sheet_8.addCell(new Label(1, 0, "旗区", tsty));
				sheet_8.setColumnView(1, 20);
				sheet_8.mergeCells(2, 0, 2, 2);
				sheet_8.addCell(new Label(2, 0, "苏木乡", tsty));
				sheet_8.setColumnView(2, 20);
				sheet_8.mergeCells(3, 0, 3, 2);
				sheet_8.addCell(new Label(3, 0, "嘎查村", tsty));
				sheet_8.setColumnView(3, 20);
				sheet_8.mergeCells(4, 0, 4, 2);
				sheet_8.addCell(new Label(4, 0, "户主姓名", tsty));
				sheet_8.mergeCells(5, 0, 43, 0);
				sheet_8.addCell(new Label(5, 0, "帮扶后收入情况", tsty));
				sheet_8.mergeCells(44, 0, 74, 0);
				sheet_8.addCell(new Label(44, 0, "帮扶后支出情况", tsty));
				sheet_8.mergeCells(5, 1, 6, 1);
				sheet_8.addCell(new Label(5, 1, "农业（水产）", tsty));
				sheet_8.addCell(new Label(5, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(6, 2, "金额（元）", tsty));

				sheet_8.mergeCells(7, 1, 8, 1);
				sheet_8.addCell(new Label(7, 1, "畜牧业", tsty));
				sheet_8.addCell(new Label(7, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(8, 2, "金额（元）", tsty));
				//
				sheet_8.mergeCells(9, 1, 10, 1);
				sheet_8.addCell(new Label(9, 1, "林业", tsty));
				sheet_8.addCell(new Label(9, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(10, 2, "金额", tsty));
				sheet_8.mergeCells(11, 1, 12, 1);
				sheet_8.addCell(new Label(11, 1, "其他", tsty));
				sheet_8.addCell(new Label(11, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(12, 2, "金额", tsty));
				sheet_8.mergeCells(13, 1, 14, 1);
				sheet_8.addCell(new Label(13, 1, "小计", tsty));
				sheet_8.addCell(new Label(13, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(14, 2, "金额", tsty));
				sheet_8.mergeCells(15, 1, 16, 1);
				sheet_8.addCell(new Label(15, 1, "农林牧草、生态等补贴", tsty));
				sheet_8.addCell(new Label(15, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(16, 2, "金额", tsty));

				sheet_8.mergeCells(17, 1, 18, 1);
				sheet_8.addCell(new Label(17, 1, "养老金", tsty));
				sheet_8.addCell(new Label(17, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(18, 2, "金额", tsty));

				sheet_8.mergeCells(19, 1, 20, 1);
				sheet_8.addCell(new Label(19, 1, "低保（五保）补贴", tsty));
				sheet_8.addCell(new Label(19, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(20, 2, "金额", tsty));
				sheet_8.mergeCells(21, 1, 22, 1);
				sheet_8.addCell(new Label(21, 1, "燃煤补贴", tsty));
				sheet_8.addCell(new Label(21, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(22, 2, "金额", tsty));
				
//				sheet_8.mergeCells(19, 1, 20, 1);
//				sheet_8.addCell(new Label(19, 1, "五保金", tsty));
//				sheet_8.addCell(new Label(19, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(20, 2, "金额", tsty));
//				sheet_8.mergeCells(21, 1, 22, 1);
//				sheet_8.addCell(new Label(21, 1, "计划生育", tsty));
//				sheet_8.addCell(new Label(21, 2, "收入明细", tsty));
//				sheet_8.addCell(new Label(22, 2, "金额", tsty));
				
				sheet_8.mergeCells(23, 1, 24, 1);
				sheet_8.addCell(new Label(23, 1, "其他", tsty));
				sheet_8.addCell(new Label(23, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(24, 2, "金额", tsty));

				sheet_8.mergeCells(25, 1, 26, 1);
				sheet_8.addCell(new Label(25, 1, "小计", tsty));
				sheet_8.addCell(new Label(25, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(26, 2, "金额", tsty));
				sheet_8.mergeCells(27, 1, 28, 1);
				sheet_8.addCell(new Label(27, 1, "土地、草牧场流转", tsty));
				sheet_8.addCell(new Label(27, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(28, 2, "金额", tsty));
				sheet_8.mergeCells(29, 1, 30, 1);
				sheet_8.addCell(new Label(29, 1, "其他", tsty));
				sheet_8.addCell(new Label(29, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(30, 2, "金额", tsty));

				sheet_8.mergeCells(31, 1, 36, 1);
				sheet_8.addCell(new Label(31, 1, "工资性收入", tsty));
				sheet_8.addCell(new Label(31, 2, "项目", tsty));
				sheet_8.addCell(new Label(32, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(33, 2, "金额", tsty));
				sheet_8.addCell(new Label(34, 2, "项目", tsty));
				sheet_8.addCell(new Label(35, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(36, 2, "金额", tsty));

				sheet_8.mergeCells(37, 1, 42, 1);
				sheet_8.addCell(new Label(37, 1, "其他收入", tsty));
				sheet_8.addCell(new Label(37, 2, "项目", tsty));
				sheet_8.addCell(new Label(38, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(39, 2, "金额", tsty));
				sheet_8.addCell(new Label(40, 2, "项目", tsty));
				sheet_8.addCell(new Label(41, 2, "收入明细", tsty));
				sheet_8.addCell(new Label(42, 2, "金额", tsty));

				sheet_8.mergeCells(43, 1, 43, 2);
				sheet_8.addCell(new Label(43, 1, "总收入合计", tsty));
				// 当前支出
				sheet_8.mergeCells(44, 1, 45, 1);
				sheet_8.addCell(new Label(44, 1, "农资费用", tsty));
				sheet_8.addCell(new Label(44, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(45, 2, "金额（元）", tsty));
				sheet_8.mergeCells(46, 1, 47, 1);
				sheet_8.addCell(new Label(46, 1, "固定财产折旧和租赁费", tsty));
				sheet_8.addCell(new Label(46, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(47, 2, "金额（元）", tsty));
				sheet_8.mergeCells(48, 1, 49, 1);
				sheet_8.addCell(new Label(48, 1, "水电燃料支出", tsty));
				sheet_8.addCell(new Label(48, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(49, 2, "金额（元）", tsty));
				sheet_8.mergeCells(50, 1, 51, 1);
				sheet_8.addCell(new Label(50, 1, "承包土地、草场费用", tsty));
				sheet_8.addCell(new Label(50, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(51, 2, "金额（元）", tsty));
				sheet_8.mergeCells(52, 1, 53, 1);
				sheet_8.addCell(new Label(52, 1, "饲草料", tsty));
				sheet_8.addCell(new Label(52, 2, "支出明细细", tsty));
				sheet_8.addCell(new Label(53, 2, "金额（元）", tsty));
				sheet_8.mergeCells(54, 1, 55, 1);
				sheet_8.addCell(new Label(54, 1, "防疫防治支出", tsty));
				sheet_8.addCell(new Label(54, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(55, 2, "金额（元）", tsty));
				sheet_8.mergeCells(56, 1, 57, 1);
				sheet_8.addCell(new Label(56, 1, "种（仔）畜", tsty));
				sheet_8.addCell(new Label(56, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(57, 2, "金额（元）", tsty));
				sheet_8.mergeCells(58, 1, 59, 1);
				sheet_8.addCell(new Label(58, 1, "销售费用和通讯费用", tsty));
				sheet_8.addCell(new Label(58, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(59, 2, "金额（元）", tsty));
				sheet_8.mergeCells(60, 1, 61, 1);
				sheet_8.addCell(new Label(60, 1, "借贷利息", tsty));
				sheet_8.addCell(new Label(60, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(61, 2, "金额（元）", tsty));
				sheet_8.mergeCells(62, 1, 67, 1);
				sheet_8.addCell(new Label(62, 1, "政策性支出", tsty));
				sheet_8.addCell(new Label(62, 2, "项目", tsty));
				sheet_8.addCell(new Label(63, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(64, 2, "金额（元）", tsty));
				sheet_8.addCell(new Label(65, 2, "项目", tsty));
				sheet_8.addCell(new Label(66, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(67, 2, "金额（元）", tsty));
				sheet_8.mergeCells(68, 1, 73, 1);
				sheet_8.addCell(new Label(68, 1, "其他支出", tsty));
				sheet_8.addCell(new Label(68, 2, "项目", tsty));
				sheet_8.addCell(new Label(69, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(70, 2, "金额（元）", tsty));
				sheet_8.addCell(new Label(71, 2, "项目", tsty));
				sheet_8.addCell(new Label(72, 2, "支出明细", tsty));
				sheet_8.addCell(new Label(73, 2, "金额（元）", tsty));
				sheet_8.mergeCells(74, 1, 74, 2);
				sheet_8.addCell(new Label(74, 1, "总支出合计", tsty));
				sheet_8.mergeCells(75, 0, 75, 2);
				sheet_8.addCell(new Label(75, 0, "年纯收入", tsty));
				sheet_8.mergeCells(76, 0, 76, 2);
				sheet_8.addCell(new Label(76, 0, "年人均纯收入", tsty));
				sheet_8.setRowView(0, 500);
				sheet_8.setRowView(1, 500);
				sheet_8.setRowView(2, 500);
				SheetSettings ws2=sheet_8.getSettings();
				ws2.setHorizontalFreeze(5);//列
				ws2.setVerticalFreeze(3);//行
		        int conut_8= 3;
		        for (int i = 0; i < s1_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = s1_List.get(i);
			        	
				        
				     
				        
				        
				        //帮扶后收支
				        String sql_8="select * from da_helpback_income"+year+" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter sqlAdapter_8=new SQLAdapter(sql_8);
				        List<Map> list_8=this.getBySqlMapper.findRecords(sqlAdapter_8);
				        sheet_8.addCell(new Label( 0 , conut_8 ,list_8.get(0).get("da_household_id")==null?"":list_8.get(0).get("da_household_id").toString() ,coty));
				        sheet_8.addCell(new Label(1, conut_8 ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
			          	sheet_8.addCell(new Label(2, conut_8 ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
			          	sheet_8.addCell(new Label(3, conut_8 ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
			          	sheet_8.addCell(new Label(4, conut_8 ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
				        sheet_8.addCell(new Label( 5 , conut_8 ,list_8.get(0).get("v1")==null?"":list_8.get(0).get("v1").toString() ,coty));
				        sheet_8.addCell(new Label( 6 , conut_8 ,list_8.get(0).get("v2")==null?"":list_8.get(0).get("v2").toString() ,coty));
				        sheet_8.addCell(new Label( 7 , conut_8 ,list_8.get(0).get("v3")==null?"":list_8.get(0).get("v3").toString() ,coty));
				        sheet_8.addCell(new Label( 8 , conut_8 ,list_8.get(0).get("v4")==null?"":list_8.get(0).get("v4").toString() ,coty));
				        sheet_8.addCell(new Label( 9 , conut_8 ,list_8.get(0).get("v5")==null?"":list_8.get(0).get("v5").toString() ,coty));
				        sheet_8.addCell(new Label( 10 , conut_8 ,list_8.get(0).get("v6")==null?"":list_8.get(0).get("v6").toString() ,coty));
				        sheet_8.addCell(new Label( 11 , conut_8 ,list_8.get(0).get("v7")==null?"":list_8.get(0).get("v7").toString() ,coty));
				        sheet_8.addCell(new Label( 12 , conut_8 ,list_8.get(0).get("v8")==null?"":list_8.get(0).get("v8").toString() ,coty));
				        sheet_8.addCell(new Label( 13 , conut_8 ,list_8.get(0).get("v9")==null?"":list_8.get(0).get("v9").toString() ,coty));
				        sheet_8.addCell(new Label( 14 , conut_8 ,list_8.get(0).get("v10")==null?"":list_8.get(0).get("v10").toString() ,coty));
				        sheet_8.addCell(new Label( 15 , conut_8 ,list_8.get(0).get("v11")==null?"":list_8.get(0).get("v11").toString() ,coty));
				        sheet_8.addCell(new Label( 16 , conut_8 ,list_8.get(0).get("v12")==null?"":list_8.get(0).get("v12").toString() ,coty));
				        sheet_8.addCell(new Label( 17 , conut_8 ,list_8.get(0).get("v13")==null?"":list_8.get(0).get("v13").toString() ,coty));
				        sheet_8.addCell(new Label( 18 , conut_8 ,list_8.get(0).get("v14")==null?"":list_8.get(0).get("v14").toString() ,coty));
				        sheet_8.addCell(new Label( 19 , conut_8 ,list_8.get(0).get("v15")==null?"":list_8.get(0).get("v15").toString() ,coty));
				        sheet_8.addCell(new Label( 20 , conut_8 ,list_8.get(0).get("v16")==null?"":list_8.get(0).get("v16").toString() ,coty));
				        sheet_8.addCell(new Label( 21 , conut_8 ,list_8.get(0).get("v17")==null?"":list_8.get(0).get("v17").toString() ,coty));
				        sheet_8.addCell(new Label( 22 , conut_8 ,list_8.get(0).get("v18")==null?"":list_8.get(0).get("v18").toString() ,coty));
				        sheet_8.addCell(new Label( 23 , conut_8 ,list_8.get(0).get("v19")==null?"":list_8.get(0).get("v19").toString() ,coty));
				        sheet_8.addCell(new Label( 24 , conut_8 ,list_8.get(0).get("v20")==null?"":list_8.get(0).get("v20").toString() ,coty));
				        sheet_8.addCell(new Label( 25 , conut_8 ,list_8.get(0).get("v21")==null?"":list_8.get(0).get("v21").toString() ,coty));
				        sheet_8.addCell(new Label( 26 , conut_8 ,list_8.get(0).get("v22")==null?"":list_8.get(0).get("v22").toString() ,coty));
				        sheet_8.addCell(new Label( 27 , conut_8 ,list_8.get(0).get("v23")==null?"":list_8.get(0).get("v23").toString() ,coty));
				        sheet_8.addCell(new Label( 28 , conut_8 ,list_8.get(0).get("v24")==null?"":list_8.get(0).get("v24").toString() ,coty));
				        sheet_8.addCell(new Label( 29 , conut_8 ,list_8.get(0).get("v25")==null?"":list_8.get(0).get("v25").toString() ,coty));
				        sheet_8.addCell(new Label( 30 , conut_8 ,list_8.get(0).get("v26")==null?"":list_8.get(0).get("v26").toString() ,coty));
				        sheet_8.addCell(new Label( 31 , conut_8 ,list_8.get(0).get("v35")==null?"":list_8.get(0).get("v35").toString() ,coty));
				        sheet_8.addCell(new Label( 32 , conut_8 ,list_8.get(0).get("v27")==null?"":list_8.get(0).get("v27").toString() ,coty));
				        sheet_8.addCell(new Label( 33 , conut_8 ,list_8.get(0).get("v28")==null?"":list_8.get(0).get("v28").toString() ,coty));
				        sheet_8.addCell(new Label( 34 , conut_8 ,list_8.get(0).get("v36")==null?"":list_8.get(0).get("v36").toString() ,coty));
				        sheet_8.addCell(new Label( 35 , conut_8 ,list_8.get(0).get("v29")==null?"":list_8.get(0).get("v29").toString() ,coty));
				        sheet_8.addCell(new Label( 36 , conut_8 ,list_8.get(0).get("v30")==null?"":list_8.get(0).get("v30").toString() ,coty));
				        sheet_8.addCell(new Label( 37 , conut_8 ,list_8.get(0).get("v37")==null?"":list_8.get(0).get("v37").toString() ,coty));
				        sheet_8.addCell(new Label( 38 , conut_8 ,list_8.get(0).get("v31")==null?"":list_8.get(0).get("v31").toString() ,coty));
				        sheet_8.addCell(new Label( 39 , conut_8 ,list_8.get(0).get("v32")==null?"":list_8.get(0).get("v32").toString() ,coty));
				        sheet_8.addCell(new Label( 40 , conut_8 ,list_8.get(0).get("v38")==null?"":list_8.get(0).get("v38").toString() ,coty));
				        sheet_8.addCell(new Label( 41 , conut_8 ,list_8.get(0).get("v33")==null?"":list_8.get(0).get("v33").toString() ,coty));
				        sheet_8.addCell(new Label( 42 , conut_8 ,list_8.get(0).get("v34")==null?"":list_8.get(0).get("v34").toString() ,coty));
				        sheet_8.addCell(new Label( 43 , conut_8 ,list_8.get(0).get("v39")==null?"":list_8.get(0).get("v39").toString() ,coty));
//				        
//				        //帮扶后支出
				        String bfh_sql="select v1 cv1,v2 cv2,v3 cv3,v4 cv4,v5 cv5,v6 cv6,v7 cv7,v8 cv8,v9 cv9,v10 cv10,v11 cv11,"+
				        				"v12 cv12,v13 cv13,v14 cv14,v15 cv15,v16 cv16,v17 cv17,v18 cv18,v19 cv19,v20 cv20,v21 cv21,v22 cv22,"+
				        				"v23 cv23,v24 cv24,v25 cv25,v26 cv26,v27 cv27,v28 cv28,v29 cv29,v30 cv30,v31 cv31,da_household_id "+
				        				"from da_helpback_expenditure"+year+" where da_household_id="+s1_map.get("pkid");
				        SQLAdapter bfh_sqlAdapter=new SQLAdapter(bfh_sql);
				        List<Map> bfh_list=this.getBySqlMapper.findRecords(bfh_sqlAdapter);
				        sheet_8.addCell(new Label( 44 , conut_8 ,bfh_list.get(0).get("cv1")==null?"":bfh_list.get(0).get("cv1").toString() ,coty));
				        sheet_8.addCell(new Label( 45 , conut_8 ,bfh_list.get(0).get("cv2")==null?"":bfh_list.get(0).get("cv2").toString() ,coty));
				        sheet_8.addCell(new Label( 46 , conut_8 ,bfh_list.get(0).get("cv3")==null?"":bfh_list.get(0).get("cv3").toString() ,coty));
				        sheet_8.addCell(new Label( 47 , conut_8 ,bfh_list.get(0).get("cv4")==null?"":bfh_list.get(0).get("cv4").toString() ,coty));
				        sheet_8.addCell(new Label( 48 , conut_8 ,bfh_list.get(0).get("cv5")==null?"":bfh_list.get(0).get("cv5").toString() ,coty));
				        sheet_8.addCell(new Label( 49 , conut_8 ,bfh_list.get(0).get("cv6")==null?"":bfh_list.get(0).get("cv6").toString() ,coty));
				        sheet_8.addCell(new Label( 50 , conut_8 ,bfh_list.get(0).get("cv7")==null?"":bfh_list.get(0).get("cv7").toString() ,coty));
				        sheet_8.addCell(new Label( 51 , conut_8 ,bfh_list.get(0).get("cv8")==null?"":bfh_list.get(0).get("cv8").toString() ,coty));
				        sheet_8.addCell(new Label( 52 , conut_8 ,bfh_list.get(0).get("cv9")==null?"":bfh_list.get(0).get("cv9").toString() ,coty));
				        sheet_8.addCell(new Label( 53 , conut_8 ,bfh_list.get(0).get("cv10")==null?"":bfh_list.get(0).get("cv10").toString() ,coty));
				        sheet_8.addCell(new Label( 54 , conut_8 ,bfh_list.get(0).get("cv11")==null?"":bfh_list.get(0).get("cv11").toString() ,coty));
				        sheet_8.addCell(new Label( 55 , conut_8 ,bfh_list.get(0).get("cv12")==null?"":bfh_list.get(0).get("cv12").toString() ,coty));
				        sheet_8.addCell(new Label( 56 , conut_8 ,bfh_list.get(0).get("cv13")==null?"":bfh_list.get(0).get("cv13").toString() ,coty));
				        sheet_8.addCell(new Label( 57 , conut_8 ,bfh_list.get(0).get("cv14")==null?"":bfh_list.get(0).get("cv14").toString() ,coty));
				        sheet_8.addCell(new Label( 58 , conut_8 ,bfh_list.get(0).get("cv15")==null?"":bfh_list.get(0).get("cv15").toString() ,coty));
				        sheet_8.addCell(new Label( 59 , conut_8 ,bfh_list.get(0).get("cv16")==null?"":bfh_list.get(0).get("cv16").toString() ,coty));
				        sheet_8.addCell(new Label( 60 , conut_8 ,bfh_list.get(0).get("cv17")==null?"":bfh_list.get(0).get("cv17").toString() ,coty));
				        sheet_8.addCell(new Label( 61 , conut_8 ,bfh_list.get(0).get("cv18")==null?"":bfh_list.get(0).get("cv18").toString() ,coty));
				        
				        sheet_8.addCell(new Label( 62 , conut_8 ,bfh_list.get(0).get("cv23")==null?"":bfh_list.get(0).get("cv23").toString() ,coty));
				        sheet_8.addCell(new Label( 63 , conut_8 ,bfh_list.get(0).get("cv19")==null?"":bfh_list.get(0).get("cv19").toString() ,coty));
				        sheet_8.addCell(new Label( 64 , conut_8 ,bfh_list.get(0).get("cv20")==null?"":bfh_list.get(0).get("cv20").toString() ,coty));
				        
				        sheet_8.addCell(new Label( 65 , conut_8 ,bfh_list.get(0).get("cv24")==null?"":bfh_list.get(0).get("cv24").toString() ,coty));
				        sheet_8.addCell(new Label( 66 , conut_8 ,bfh_list.get(0).get("cv21")==null?"":bfh_list.get(0).get("cv21").toString() ,coty));
				        sheet_8.addCell(new Label( 67 , conut_8 ,bfh_list.get(0).get("cv22")==null?"":bfh_list.get(0).get("cv22").toString() ,coty));
				        sheet_8.addCell(new Label( 68 , conut_8 ,bfh_list.get(0).get("cv25")==null?"":bfh_list.get(0).get("cv25").toString() ,coty));
				        sheet_8.addCell(new Label( 69 , conut_8 ,bfh_list.get(0).get("cv26")==null?"":bfh_list.get(0).get("cv26").toString() ,coty));
				        sheet_8.addCell(new Label( 70 , conut_8 ,bfh_list.get(0).get("cv27")==null?"":bfh_list.get(0).get("cv27").toString() ,coty));
				        sheet_8.addCell(new Label( 71 , conut_8 ,bfh_list.get(0).get("cv28")==null?"":bfh_list.get(0).get("cv28").toString() ,coty));
				        sheet_8.addCell(new Label( 72 , conut_8 ,bfh_list.get(0).get("cv29")==null?"":bfh_list.get(0).get("cv29").toString() ,coty));
				        sheet_8.addCell(new Label( 73 , conut_8 ,bfh_list.get(0).get("cv30")==null?"":bfh_list.get(0).get("cv30").toString() ,coty));
				        sheet_8.addCell(new Label( 74 , conut_8 ,bfh_list.get(0).get("cv31")==null?"":bfh_list.get(0).get("cv31").toString() ,coty));
				        String aa="";
						String bb="";
						if("".equals(list_8.get(0).get("v39"))||list_8.get(0).get("v39")==null){
							aa="0";
						}else{
							aa=list_8.get(0).get("v39").toString();
						}
						if("".equals(bfh_list.get(0).get("cv31"))||bfh_list.get(0).get("cv31")==null){
							bb="0";
						}else{
							bb=bfh_list.get(0).get("cv31").toString();
						}
						double c_c=Double.parseDouble(aa);
						double c1_c=Double.parseDouble(bb);
						double c2_c=c_c-c1_c;
						String ncsr_c=String.format("%.2f", c2_c);
						double cc_c=Double.parseDouble(s1_map.get("v9").toString());
						double cnum_c=c2_c/cc_c;
						String str1_c=String.format("%.2f", cnum_c);
				        sheet_8.addCell(new Label( 75 , conut_8 ,ncsr_c ,coty));
				        sheet_8.addCell(new Label( 76 , conut_8 ,str1_c,coty));
				        sheet_8.setRowView(conut_8, 500); // 设置第一行的高度
				        conut_8++;
		        
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	/**
	 * 导出全部数据—基本情况
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel_all8(HttpServletRequest request,HttpServletResponse response) throws Exception {

		String form_name=request.getParameter("form_name");
		String form_val = request.getParameter("form_val");
		String danxuan_val=request.getParameter("danxuan_val");
		String json_level= request.getParameter("jsonlevel");
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String level=request.getParameter("level");
		String jsonname=request.getParameter("jsonname");
		String poverty_type=request.getParameter("poverty_type");//判断是啥类型贫困户（0：国贫1：市贫）
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		//System.out.println(poverty_type);
		JSONObject danxuan_json = JSONObject.fromObject(danxuan_val);//表单数据
		int shilevel;
		String shi_name="";
		String term="";
		String life_term="";
		String product_term="";
		
		String left_sql = "";
		String shilevel_sql="";
		String poverty_type_sql="";
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
		if(poverty_type.equals("1")){
			poverty_type_sql+=" and left(a.sys_standard,2)='市级' ";
		}else{
			poverty_type_sql+=" and left(a.sys_standard,3)='国家级' ";
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
		String where =" where "+shilevel_sql+poverty_type_sql+" and ("+ term.substring(0,term.length()-3) +")";
		String people_sql="select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v21,a.v22,a.v23,a.v11,a.sys_standard from da_household a ";
		String fpeople_sql=people_sql+left_sql+where+" GROUP BY a.pkid order by a.v2 ";
		if ( form_name ==null && form_name.equals("") && form_val  ==null && form_val .equals("")&& danxuan_val  ==null && danxuan_val .equals("")&& json_level  ==null && json_level .equals("")) {
			fpeople_sql= people_sql+ "GROUP BY a.pkid order by a.v2";
		}
		HttpSession session = request.getSession();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			try{
				Map Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				Map company = (Map)session.getAttribute("company");//用户的单位信息
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        
				WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setWrap(true);
//				tsty.setLocked(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setWrap(true);
				SQLAdapter Patient_st_Adapter = new SQLAdapter(fpeople_sql);
				List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
				WritableSheet sheet_1 = book.createSheet( "贫困户基本信息 " , 0);//生成第一页工作表，参数0表示这是第一页
				
				int[] headerArrHight_1 = {13,20,25,20,20,30,20,15,20,30,35,10,30,10,20,13,25,30,30};
		        String headerArr_1[] = {"家庭编号","户主姓名","旗区","苏木乡","噶查村","家庭人口","脱贫属性"
		        		,"贫困户属性","识别标识"};
		        for (int i = 0; i < headerArr_1.length; i++) {
		        	sheet_1.addCell(new Label( i , 0 , headerArr_1[i], tsty));
		        	sheet_1.setColumnView(i, headerArrHight_1[i]);
		        }
		        sheet_1.setRowView(0, 500); // 设置第一行的高度
				sheet_1.getSettings().setHorizontalFreeze(5);
				sheet_1.getSettings().setVerticalFreeze(1);
		        
		        int conut = 1;
		        for (int i = 0; i < Patient_st_List.size(); i++) {   //循环一个list里面的数据到excel中
		        	Map s1_map = Patient_st_List.get(i);
		        	sheet_1.addCell(new Label( 0 , conut ,s1_map.get("pkid")==null?"":s1_map.get("pkid").toString() ,coty));
		        	sheet_1.addCell(new Label( 1 , conut ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
		        	sheet_1.addCell(new Label( 2 , conut ,s1_map.get("v3")==null?"":s1_map.get("v3").toString() ,coty));
		        	sheet_1.addCell(new Label( 3 , conut ,s1_map.get("v4")==null?"":s1_map.get("v4").toString() ,coty));
		        	sheet_1.addCell(new Label( 4 , conut ,s1_map.get("v5")==null?"":s1_map.get("v5").toString() ,coty));
		        	sheet_1.addCell(new Label( 5 , conut ,s1_map.get("v9")==null?"":s1_map.get("v9").toString() ,coty));
		        	sheet_1.addCell(new Label( 6 , conut ,s1_map.get("v21")==null?"":s1_map.get("v21").toString() ,coty));
		        	sheet_1.addCell(new Label( 7 , conut ,s1_map.get("v22")==null?"":s1_map.get("v22").toString() ,coty));
		        	sheet_1.addCell(new Label( 8 , conut ,s1_map.get("sys_standard")==null?"":s1_map.get("sys_standard").toString() ,coty));
		        	sheet_1.setRowView(conut, 500); // 设置第一行的高度
		        	conut++;
		        }
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}
	
	
	
	
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid");
		String year = request.getParameter("year");
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		if(pkid!=null&&!pkid.equals("")){
			try{
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
		        //获取文件需要上传到的路径
				String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
				 // 文件保存目录URL  
		        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
		        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
				
		        WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
				
				//标题样式
				WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.BOLD);
				WritableCellFormat tsty = new WritableCellFormat(title_style);
				WritableCellFormat tstys = new WritableCellFormat(title_style);
				tsty.setAlignment(Alignment.CENTRE);  //平行居中
				tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				tsty.setBorder(Border.ALL, BorderLineStyle.THIN);
				tsty.setWrap(true);
				
				//正文样式
				WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 8 ,WritableFont.NO_BOLD);
				WritableCellFormat coty = new WritableCellFormat(content_style);
				coty.setAlignment(Alignment.CENTRE);  //平行居中
				coty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
				coty.setBorder(Border.ALL, BorderLineStyle.THIN);
				coty.setWrap(true);
				
				
				//贫困户基本信息
				String sql_1 = "select * from da_household"+year+" a left join da_household_basic"+year+" b on a.pkid=b.da_household_id LEFT JOIN (SELECT pic_path,pic_pkid from da_pic"+year+" WHERE pic_type=4 ) c ON a.pkid=c.pic_pkid  where a.pkid="+pkid;
				
				String sql_2=" select * from da_member"+year+" where da_household_id="+pkid;
				
				SQLAdapter s1_Adapter = new SQLAdapter(sql_1);
				List<Map> s1_List = this.getBySqlMapper.findRecords(s1_Adapter);
				
				SQLAdapter s2_Adapter=new SQLAdapter(sql_2);
				List<Map> s2_list=this.getBySqlMapper.findRecords(s2_Adapter);
				
				WritableSheet sheet_1 = book.createSheet( "贫困户基本信息 " , 0);//生成第一页工作表，参数0表示这是第一页
				
				Map huzhu_map = s1_List.get(0);
				
				sheet_1.setColumnView(0, 15);//设置列宽
				sheet_1.setColumnView(1, 17);
				sheet_1.setColumnView(2, 13);
				sheet_1.setColumnView(3, 15);
				sheet_1.setColumnView(4, 14);
				sheet_1.setColumnView(5, 11);
				
				sheet_1.mergeCells( 0 , 0 , 5 , 0 ); // 合并单元格(int m,int n,int p,int q);作用是从(m,n)到(p,q)的单元格全部合并[我的理解：m列、n行和p列、q行合并]//从零开始
				sheet_1.addCell(new Label( 0 , 0 ,huzhu_map.get("v3").toString()+huzhu_map.get("v4").toString()+huzhu_map.get("v5").toString() ,tsty));
				sheet_1.setRowView(0, 500);
				
				sheet_1.mergeCells( 0 , 1 , 5 , 1 );
				sheet_1.addCell(new Label( 0 , 1 , "基本情况" ,tsty));
				sheet_1.setRowView(1, 500);
				
				sheet_1.mergeCells( 0 , 2 , 1 , 6 );
				
				//加载户主照片
				String sql_pic = "select * from da_pic"+year+" where pic_type=4 and pic_pkid="+pkid;
				SQLAdapter pic_Adapter = new SQLAdapter(sql_pic);
				List<Map> pic_List = this.getBySqlMapper.findRecords(pic_Adapter);
				if(pic_List.size()>0){
					Map pic_map = pic_List.get(0);
					String path = request.getSession().getServletContext().getRealPath("");
					path = path.replaceAll("assa", "");
					path += pic_map.get("pic_path").toString().substring(1);
//					System.out.println(path);
					if(pic_map.get("pic_format").toString().equals("jpg")){
						String pngpath = path.substring(0, path.length()-4)+".png";
						if(ImageEncoderService.narrowAndFormateTransfer(path, pngpath, 500, 400, "png")){
							File imgFile = new File(pngpath);
							//WritableImage(col, row, width, height, imgFile);
							//col row是图片的起始行起始列  width height是定义图片跨越的行数与列数
							WritableImage image = new WritableImage(0,2,2,5,imgFile);
							sheet_1.addImage(image);
						}else{
							sheet_1.addCell(new Label( 0, 2 , "照片加载失败" ,coty));
						}
					}else{
						File imgFile = new File(path);
						WritableImage image = new WritableImage(0,2,2,5,imgFile);
						sheet_1.addImage(image);
					}
				}else{
//					sheet_1.mergeCells( 0 , 2 , 1 , 6 );
					sheet_1.addCell(new Label( 0 , 2 , "户主照片" ,coty));
				}
				
				
				
				sheet_1.addCell(new Label( 2 , 2 , "户主姓名" ,tsty));
				sheet_1.addCell(new Label( 3 , 2 , huzhu_map.get("v6")==null?"":huzhu_map.get("v6").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 2 , "民族" ,tsty));
				sheet_1.addCell(new Label( 5 , 2 , huzhu_map.get("v11")==null?"":huzhu_map.get("v11").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 3 , "人数" ,tsty));
				sheet_1.addCell(new Label( 3 , 3 , huzhu_map.get("v9")==null?"":huzhu_map.get("v9").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 3 , "联系电话" ,tsty));
				sheet_1.addCell(new Label( 5 , 3 , huzhu_map.get("v25")==null?"":huzhu_map.get("v25").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 4 , "身份证号" ,tsty));
				sheet_1.addCell(new Label( 3 , 4 , huzhu_map.get("v8")==null?"":huzhu_map.get("v8").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 4 , "政治面貌" ,tsty));
				sheet_1.addCell(new Label( 5 , 4 , huzhu_map.get("v28")==null?"":huzhu_map.get("v28").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 5 , "开户银行" ,tsty));
				sheet_1.addCell(new Label( 3 , 5 , huzhu_map.get("v26")==null?"":huzhu_map.get("v26").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 5 , "银行卡号" ,tsty));
				sheet_1.addCell(new Label( 5 , 5 , huzhu_map.get("v27")==null?"":huzhu_map.get("v27").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 6 , "家庭住址" ,tsty));
				sheet_1.mergeCells( 3 , 6 , 5 , 6 );
				sheet_1.addCell(new Label( 3 , 6 ,huzhu_map.get("basic_address")==null?"":huzhu_map.get("basic_address").toString() ,coty));
				sheet_1.addCell(new Label( 0 , 7 , "识别标准" ,tsty));
				sheet_1.addCell(new Label( 1 , 7 , huzhu_map.get("sys_standard")==null?"":huzhu_map.get("sys_standard").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 7 , "贫困户属性" ,tsty));
				sheet_1.addCell(new Label( 3 , 7 , huzhu_map.get("v22")==null?"":huzhu_map.get("v22").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 7 , "文化程度" ,tsty));
				sheet_1.addCell(new Label( 5 , 7 , huzhu_map.get("v12")==null?"":huzhu_map.get("v12").toString() ,coty));
				sheet_1.addCell(new Label( 0 , 8 , "是否在校" ,tsty));
				sheet_1.addCell(new Label( 1 , 8 , huzhu_map.get("v13")==null?"":huzhu_map.get("v13").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 8 , "健康情况" ,tsty));
				sheet_1.addCell(new Label( 3 , 8 , huzhu_map.get("v14")==null?"":huzhu_map.get("v14").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 8 , "劳动力" ,tsty));
				sheet_1.addCell(new Label( 5 , 8 , huzhu_map.get("v15")==null?"":huzhu_map.get("v15").toString() ,coty));
				sheet_1.addCell(new Label( 0 , 9 , "务工情况" ,tsty));
				sheet_1.addCell(new Label( 1 , 9 , huzhu_map.get("v16")==null?"":huzhu_map.get("v16").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 9 , "务工时间" ,tsty));
				sheet_1.addCell(new Label( 3 , 9 , huzhu_map.get("v17")==null?"":huzhu_map.get("v17").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 9 , "是否参加新农合" ,tsty));
				sheet_1.addCell(new Label( 5 , 9 , huzhu_map.get("v18")==null?"":huzhu_map.get("v18").toString() ,coty));
				sheet_1.addCell(new Label( 0 , 10 , "是否参加养老保险" ,tsty));
				sheet_1.addCell(new Label( 1 , 10 , huzhu_map.get("v19")==null?"":huzhu_map.get("v19").toString() ,coty));//有两个保险一个新型的一个城镇的 我填的是新型的
				sheet_1.addCell(new Label( 2 , 10 , "是否军烈属" ,tsty));
				sheet_1.addCell(new Label( 3 , 10 , huzhu_map.get("v29")==null?"":huzhu_map.get("v29").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 10 , "是否现役军人" ,tsty));
				sheet_1.addCell(new Label( 5 , 10 , huzhu_map.get("v32")==null?"":huzhu_map.get("v32").toString() ,coty));
				sheet_1.addCell(new Label( 0 , 11 , "是否独生子女户" ,tsty));
				sheet_1.addCell(new Label( 1 , 11 , huzhu_map.get("v30")==null?"":huzhu_map.get("v30").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 11 , "是否双女户" ,tsty));
				sheet_1.addCell(new Label( 3 , 11 , huzhu_map.get("v31")==null?"":huzhu_map.get("v31").toString() ,coty));
				sheet_1.addCell(new Label( 4 , 11 , "主要致贫原因" ,tsty));
				sheet_1.addCell(new Label( 5 , 11 , huzhu_map.get("v23")==null?"":huzhu_map.get("v23").toString() ,coty));
				sheet_1.addCell(new Label( 0 , 12 , "其他致贫原因" ,tsty));
				sheet_1.addCell(new Label( 1 , 12 , huzhu_map.get("v33")==null?"":huzhu_map.get("v33").toString() ,coty));
				sheet_1.addCell(new Label( 2 , 12 , "致贫原因说明" ,tsty));
				sheet_1.mergeCells( 3 , 12 , 5 , 12 );
				sheet_1.addCell(new Label( 3 , 12 , huzhu_map.get("basic_explain")==null?"":huzhu_map.get("basic_explain").toString() ,coty));
				for(int i=0;i<13;i++){
					sheet_1.setRowView(i, 600);
				
				}
				sheet_1.mergeCells( 0 , 14 , 5 , 14 );
				sheet_1.addCell(new Label( 0 , 14 , "家庭成员" ,tsty));
				sheet_1.setRowView(14, 600);
				int q_count = 14;
				for (int i = 0; i < s2_list.size(); i++) {   //循环一个list里面的数据到excel中
					Map s1_map = s2_list.get(i);
					
					sheet_1.mergeCells( 0 , q_count+1 , 1 , q_count+6 );
					
					String cy_sql="select * from da_pic"+year+" where pic_type=5 and pic_pkid="+s1_map.get("pkid");
					SQLAdapter cy_sqlAdapter = new SQLAdapter(cy_sql);
					List<Map> cy_list=this.getBySqlMapper.findRecords(cy_sqlAdapter);
					if(cy_list.size()>0){
						Map cy_map = cy_list.get(0);
						String path = request.getSession().getServletContext().getRealPath("");
						path = path.replaceAll("assa", "");
						path += cy_map.get("pic_path").toString().substring(1);
						if(cy_map.get("pic_format").toString().equals("jpg")){
						String pngpath = path.substring(0, path.length()-4)+".png";
						if(ImageEncoderService.narrowAndFormateTransfer(path, pngpath, 500, 400, "png")){
							File imgFile = new File(pngpath);
							//WritableImage(col, row, width, height, imgFile);
							//col row是图片的起始行起始列  width height是定义图片跨越的行数与列数
							WritableImage image = new WritableImage(0,q_count+1, 2 ,6,imgFile);
							sheet_1.addImage(image);
						}else{
							sheet_1.addCell(new Label( 0, 2 , "照片加载失败" ,coty));
						}
					}else{
						File imgFile = new File(path);
						WritableImage image = new WritableImage(0,q_count+1, 2, 6,imgFile);
						sheet_1.addImage(image);
					}
					}else{
//						sheet_1.mergeCells( 0 , q_count+1 , 1 , q_count+6 );
						sheet_1.addCell(new Label( 0 , q_count+1 , "家庭成员照片" ,coty));
					}
					
					sheet_1.addCell(new Label( 2 , q_count+1 , "姓名" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+1 , s1_map.get("v6")==null?"":s1_map.get("v6").toString()+"（"+s1_map.get("v10").toString()+"）" ,coty));
					sheet_1.addCell(new Label( 4 , q_count+1 , "性别" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+1 , s1_map.get("v7")==null?"":s1_map.get("v7").toString() ,coty));
					
					sheet_1.addCell(new Label( 2 , q_count+2 , "民族" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+2 , s1_map.get("v11")==null?"":s1_map.get("v11").toString() ,coty));
					sheet_1.addCell(new Label( 4 , q_count+2 , "证件号码" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+2 , s1_map.get("v8")==null?"":s1_map.get("v8").toString() ,coty));
					
					sheet_1.addCell(new Label( 2 , q_count+3 , "文化程度" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+3 , s1_map.get("v12")==null?"":s1_map.get("v12").toString() ,coty));
					sheet_1.addCell(new Label( 4 , q_count+3 , "是否在校" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+3 , s1_map.get("v13")==null?"":s1_map.get("v13").toString() ,coty));
					
					sheet_1.addCell(new Label( 2 , q_count+4 , "健康状况" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+4 , s1_map.get("v14")==null?"":s1_map.get("v14").toString() ,coty));
					sheet_1.addCell(new Label( 4 , q_count+4 , "劳动力" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+4 , s1_map.get("v15")==null?"":s1_map.get("v15").toString() ,coty));
					
					sheet_1.addCell(new Label( 2 , q_count+4 , "新农合" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+4 , s1_map.get("v18")==null?"":s1_map.get("v18").toString() ,coty));
					sheet_1.addCell(new Label( 4 , q_count+4 , "养老保险" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+4 , s1_map.get("v19")==null?"":s1_map.get("v19").toString() ,coty));//我用的是信仰要保险 有城镇的和新的
					
					sheet_1.addCell(new Label( 2 , q_count+5 , "是否现役军人" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+5 , s1_map.get("v32")==null?"":s1_map.get("v32").toString() ,coty));
					sheet_1.addCell(new Label( 4 , q_count+5 , "政治面貌" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+5 , s1_map.get("v28")==null?"":s1_map.get("v28").toString() ,coty));
					
					sheet_1.addCell(new Label( 2 , q_count+6 , "务工情况" ,tsty));
					sheet_1.addCell(new Label( 3 , q_count+6 , s1_map.get("v16")==null?"":s1_map.get("v16").toString() ,coty));
					sheet_1.addCell(new Label( 4 , q_count+6 , "务工时间" ,tsty));
					sheet_1.addCell(new Label( 5 , q_count+6 , s1_map.get("v17")==null?"":s1_map.get("v17").toString() ,coty));
					sheet_1.setRowView(q_count+1, 600);
					sheet_1.setRowView(q_count+2, 600);
					sheet_1.setRowView(q_count+3, 600);
					sheet_1.setRowView(q_count+4, 600);
					sheet_1.setRowView(q_count+5, 600);
					sheet_1.setRowView(q_count+6, 600);
					q_count += 7;
				}
				
				
				String sql_sc = "select * from da_production"+year+" where da_household_id="+pkid;
				
				SQLAdapter sc_Adapter = new SQLAdapter(sql_sc);
				List<Map> sc_List = this.getBySqlMapper.findRecords(sc_Adapter);
				Map sc_map = sc_List.get(0);
				
				WritableSheet sheet_sc = book.createSheet( "生产生活 " , 1);
				
				sheet_sc.setColumnView(0, 18);//设置列宽
				sheet_sc.setColumnView(1, 10);
				sheet_sc.setColumnView(2, 18);
				sheet_sc.setColumnView(3, 10);
				sheet_sc.setColumnView(4, 18);
				sheet_sc.setColumnView(5, 10);
				
				sheet_sc.mergeCells( 0 , 0 , 5 , 0 );
				sheet_sc.addCell(new Label( 0 , 0 , "生产条件" ,tsty));
				sheet_sc.setRowView(0, 600);
				
				
				sheet_sc.addCell(new Label( 0 , 1 ,"耕地面积",tsty));
				sheet_sc.addCell(new Label( 1 , 1 , sc_map.get("pv")==null?"":sc_map.get("v1").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 1 ,"有效灌溉地面（亩）",tsty));
				sheet_sc.addCell(new Label( 3 , 1 , sc_map.get("v2")==null?"":sc_map.get("v2").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 1 ,"林地面（亩）",tsty));
				sheet_sc.addCell(new Label( 5 , 1 , sc_map.get("v3")==null?"":sc_map.get("v3").toString() ,coty));
				sheet_sc.setRowView(1, 600);
				
				sheet_sc.addCell(new Label( 0 , 2 ,"退耕还林面积(亩)",tsty));
				sheet_sc.addCell(new Label( 1 , 2 , sc_map.get("v4")==null?"":sc_map.get("v4").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 2 ,"草牧场面积(亩)",tsty));
				sheet_sc.addCell(new Label( 3 , 2 , sc_map.get("v5")==null?"":sc_map.get("v5").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 2 ,"生产用房面积(亩)",tsty));
				sheet_sc.addCell(new Label( 5 , 2 , sc_map.get("v6")==null?"":sc_map.get("v6").toString() ,coty));
				sheet_sc.setRowView(2, 600);
				
				sheet_sc.addCell(new Label( 0 , 3 ,"林果面积（亩）",tsty));
				sheet_sc.addCell(new Label( 1 , 3 , sc_map.get("v13")==null?"":sc_map.get("v13").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 3 ,"水面面积(亩)",tsty));
				sheet_sc.addCell(new Label( 3 , 3 , sc_map.get("v14")==null?"":sc_map.get("v14").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 3 ,"其他",tsty));
				sheet_sc.addCell(new Label( 5 , 3 , sc_map.get("v12")==null?"":sc_map.get("v12").toString() ,coty));
				sheet_sc.addCell(new Label( 0 , 4 ,"牲畜数量",tsty));
				sheet_sc.mergeCells( 1 , 4 , 5 , 4 );
				String j8="";String j9="";String j10="";String j11="";String j12="";
				if("".equals(sc_map.get("v8"))||sc_map.get("v8")==null){
					j8="0";
				}else{
					j8=sc_map.get("v8").toString();
				}
				if("".equals(sc_map.get("v9"))||sc_map.get("v9")==null){
					j9="0";
				}else{
					j9=sc_map.get("v9").toString();
				}
				if("".equals(sc_map.get("v10"))||sc_map.get("v10")==null){
					j10="0";
				}else{
					j10=sc_map.get("v10").toString();
				}
				if("".equals(sc_map.get("v11"))||sc_map.get("v11")==null){
					j11="0";
				}else{
					j11=sc_map.get("v11").toString();
				}
				if("".equals(sc_map.get("v12"))||sc_map.get("v12")==null){
					j12="";
				}else{
					j12=sc_map.get("v12").toString();
				}
				String jcum="家禽（"+j8+"）只、牛（"+j9+"）头、羊（"+j10+"）只、猪（"+j11+"）头、其他（"+j12+"）";
				sheet_sc.addCell(new Label( 1 , 4 , jcum ,coty));
				sheet_sc.setRowView(3, 600);
				sheet_sc.setRowView(4, 650);
				//生活条件
				String sql_sh = "select * from da_life"+year+" where da_household_id="+pkid;
				
				SQLAdapter sh_Adapter = new SQLAdapter(sql_sh);
				List<Map> sh_List = this.getBySqlMapper.findRecords(sh_Adapter);
				Map sh_map = sh_List.get(0);
				
				sheet_sc.setColumnView(0, 18);//设置列宽
				sheet_sc.setColumnView(1, 10);
				sheet_sc.setColumnView(2, 18);
				sheet_sc.setColumnView(3, 10);
				sheet_sc.setColumnView(4, 18);
				sheet_sc.setColumnView(5, 10);
				
				sheet_sc.mergeCells( 0 , 6 , 5 , 6 );
				sheet_sc.addCell(new Label( 0 , 6 , "生活条件" ,tsty));
				sheet_sc.setRowView(6, 600);
				

				sheet_sc.addCell(new Label( 0 , 7 ,"住房面积(平方米)",tsty));
				sheet_sc.addCell(new Label( 1 , 7 , sh_map.get("v1")==null?"":sh_map.get("v1").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 7 ,"是否危房",tsty));
				sheet_sc.addCell(new Label( 3 , 7 , sh_map.get("v2")==null?"":sh_map.get("v2").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 7 , "是否纳入易地扶贫搬迁" ,tsty));
				sheet_sc.addCell(new Label( 5 , 7 , sh_map.get("v3")==null?"":sh_map.get("v3").toString() ,coty));
				sheet_sc.setRowView(7, 600);
				sheet_sc.addCell(new Label( 0 ,	8 ,"饮水情况",tsty));
				sheet_sc.addCell(new Label( 1 , 8 , sh_map.get("v4")==null?"":sh_map.get("v4").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 8 ,"饮水是否困难",tsty));
				sheet_sc.addCell(new Label( 3 , 8 , sh_map.get("v8")==null?"":sh_map.get("v8").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 8 ,"饮水是否安全",tsty));
				sheet_sc.addCell(new Label( 5 , 8 , sh_map.get("v9")==null?"":sh_map.get("v9").toString() ,coty));
				sheet_sc.setRowView(8, 600);
				
				sheet_sc.addCell(new Label( 0 ,	9 ,"通电情况",tsty));
				sheet_sc.addCell(new Label( 1 , 9 , sh_map.get("v5")==null?"":sh_map.get("v5").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 9 ,"入户路类型",tsty));
				sheet_sc.addCell(new Label( 3 , 9 , sh_map.get("v6")==null?"":sh_map.get("v6").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 9 ,"与主干路距离（公里）",tsty));
				sheet_sc.addCell(new Label( 5 , 9 , sh_map.get("v7")==null?"":sh_map.get("v7").toString() ,coty));
				sheet_sc.setRowView(9, 750);
				
				sheet_sc.addCell(new Label( 0 ,	10 ,"主要燃料类型",tsty));
				sheet_sc.addCell(new Label( 1 , 10 , sh_map.get("v10")==null?"":sh_map.get("v10").toString() ,coty));
				sheet_sc.addCell(new Label( 2 , 10 ,"是否加入农民专业合作社",tsty));
				sheet_sc.addCell(new Label( 3 , 10 , sh_map.get("v11")==null?"":sh_map.get("v11").toString() ,coty));
				sheet_sc.addCell(new Label( 4 , 10 ,"有无卫生厕所",tsty));
				sheet_sc.addCell(new Label( 5 , 10 , sh_map.get("v12")==null?"":sh_map.get("v12").toString() ,coty));
				sheet_sc.setRowView(10, 600);
				
				if("是".equals(sh_map.get("v3"))){
					//易地搬迁
					String sql_yd = "select * from da_household_move"+year+" where da_household_id="+pkid;
					
					SQLAdapter yd_Adapter = new SQLAdapter(sql_yd);
					List<Map> yd_List = this.getBySqlMapper.findRecords(yd_Adapter);
					Map yd_map = yd_List.get(0);
					
					sheet_sc.setColumnView(0, 18);//设置列宽
					sheet_sc.setColumnView(1, 10);
					sheet_sc.setColumnView(2, 18);
					sheet_sc.setColumnView(3, 10);
					sheet_sc.setColumnView(4, 18);
					sheet_sc.setColumnView(5, 10);
					
					sheet_sc.mergeCells( 0 , 12 , 5 , 12 );
					sheet_sc.addCell(new Label( 0 , 12 , "易地搬迁户需求" ,tsty));
					
					
					sheet_sc.mergeCells( 0 , 13 , 1 , 13 );
					sheet_sc.addCell(new Label( 0 ,	13 ,"是否搬迁户",tsty));
					sheet_sc.mergeCells( 2 , 13 , 5 , 13 );
					sheet_sc.addCell(new Label( 2 , 13 , "是" ,coty));
					sheet_sc.mergeCells( 0 , 14 , 1 , 14 );
					sheet_sc.addCell(new Label( 0 ,	14 ,"搬迁方式",tsty));
					sheet_sc.mergeCells( 2 , 14 , 5 , 14 );
					sheet_sc.addCell(new Label( 2 , 14 , yd_map.get("v1")==null?"":yd_map.get("v1").toString() ,coty));
					sheet_sc.mergeCells( 0 , 15 , 1 , 15 );
					sheet_sc.addCell(new Label( 0 ,	15 ,"安置方式",tsty));
					sheet_sc.mergeCells( 2 , 15 , 5 , 15 );
					sheet_sc.addCell(new Label( 2 , 15 , yd_map.get("move_type")==null?"":yd_map.get("move_type").toString() ,coty));
					sheet_sc.mergeCells( 0 , 16 , 1 , 16 );
					sheet_sc.addCell(new Label( 0 ,	16 ,"安置地",tsty));
					sheet_sc.mergeCells( 2 , 16 , 5 , 16 );
					sheet_sc.addCell(new Label( 2 , 16 ,  yd_map.get("v2")==null?"":yd_map.get("v2").toString() ,coty));
					sheet_sc.mergeCells( 0 , 17 , 1 , 17 );
					sheet_sc.addCell(new Label( 0 ,	17 ,"搬迁可能存在的困难",tsty));
					sheet_sc.mergeCells( 2 , 17 , 5 , 17 );
					sheet_sc.addCell(new Label( 2 , 17 , yd_map.get("v3")==null?"":yd_map.get("v3").toString() ,coty));
					
					
					
//					
					for(int i=12;i<18;i++){
						sheet_sc.setRowView(i, 500);
					}
				}


				
				//当前收支
				String sz_sql="SELECT * FROM da_current_income"+year+" where da_household_id="+pkid;
				SQLAdapter sz_sqlSQLAdapter=new SQLAdapter(sz_sql);
				List<Map> sz_list=this.getBySqlMapper.findRecords(sz_sqlSQLAdapter);
				Map sz_map=sz_list.get(0);
				String zc_sql="SELECT * FROM da_current_expenditure"+year+" where da_household_id="+pkid;
				SQLAdapter zc_sqlAdapter=new SQLAdapter(zc_sql);
				List<Map> zc_list=this.getBySqlMapper.findRecords(zc_sqlAdapter);
				Map zc_map=zc_list.get(0);
				
				
				double a1=Double.parseDouble(sz_map.get("v10")==null?"0":sz_map.get("v10").toString());
				double a2=Double.parseDouble(sz_map.get("v22")==null?"0":sz_map.get("v22").toString());
				double a3=Double.parseDouble(sz_map.get("v26")==null?"0":sz_map.get("v26").toString());
				double a4=Double.parseDouble(sz_map.get("v28")==null?"0":sz_map.get("v28").toString());
				double a5=Double.parseDouble(sz_map.get("v30")==null?"0":sz_map.get("v30").toString());
				double a6=Double.parseDouble(sz_map.get("v32")==null?"0":sz_map.get("v32").toString());
				double a7=Double.parseDouble(sz_map.get("v34")==null?"0":sz_map.get("v34").toString());
				double a8=Double.parseDouble(sz_map.get("v24")==null?"0":sz_map.get("v24").toString());
				double zong_sr=a1+a2+a3+a4+a5+a6+a7+a8;
				
				WritableSheet sheet_sz = book.createSheet( "当前收入分析" , 2);
				sheet_sz.setColumnView(0, 18);//设置列宽
				sheet_sz.setColumnView(1, 10);
				sheet_sz.setColumnView(2, 18);
				sheet_sz.setColumnView(3, 10);
				sheet_sz.setColumnView(4, 18);
				sheet_sz.setColumnView(5, 10);
				
				sheet_sz.mergeCells( 0 , 0 , 5 , 0 );
				sheet_sz.addCell(new Label( 0 , 0 , "当前收支分析" ,tsty));
				sheet_sz.setRowView(0, 500);
				
				sheet_sz.mergeCells( 0 , 1 , 0 , 2 );
				sheet_sz.addCell(new Label( 0 , 1 , "年人均纯收入(元)" ,tsty));
				
				sheet_sz.mergeCells( 1 , 1 , 5 , 1 );
				String a="";
				String b="";
				if("".equals(sz_map.get("v39"))||sz_map.get("v39")==null){
					a="0";
				}else{
					a=sz_map.get("v39").toString();
				}
				if("".equals(zc_map.get("v31"))||zc_map.get("v31")==null){
					b="0";
				}else{
					b=zc_map.get("v31").toString();
				}
				double c=Double.parseDouble(a);
				double c1=Double.parseDouble(b);
				double c2=c-c1;
				
				String str=String.format("%.2f", c2);
				String table1="年总收入（"+a+"）-年总支出（"+b+"）=年纯收入（"+str+"）";
				sheet_sz.addCell(new Label( 1 , 1 ,table1,coty));
				
				sheet_sz.mergeCells( 1 , 2, 5 , 2 );
				
				double cc=Double.parseDouble(huzhu_map.get("v9").toString());
				double cnum=c2/cc;
				DecimalFormat cf = new DecimalFormat("#.00");
				cf.format(cnum);
				String str1=String.format("%.2f", cnum);
				String table2="年纯收入（"+str+"）/家庭人数（"+huzhu_map.get("v9").toString()+"）=年人均纯收入（"+str1+"）";
				sheet_sz.addCell(new Label( 1 , 2 , table2 ,coty));
				sheet_sz.setRowView(1, 500);
				
				sheet_sz.setRowView(2, 500);
				
				sheet_sz.setColumnView(0, 18);//设置列宽
				sheet_sz.setColumnView(1, 10);
				sheet_sz.setColumnView(2, 18);
				sheet_sz.setColumnView(3, 10);
				sheet_sz.setColumnView(4, 18);
				sheet_sz.setColumnView(5, 10);
				sheet_sz.mergeCells( 0 ,4 , 5 , 4 );
				sheet_sz.addCell(new Label( 0 , 4, "当前收入情况" ,tsty));
				sheet_sz.setRowView(4, 500);
				sheet_sz.mergeCells( 0 ,5 , 3 , 5 );
				sheet_sz.addCell(new Label( 0 , 5 , "项目" ,tsty));
				
				sheet_sz.addCell(new Label( 4 , 5, "收入明细" ,tsty));
				sheet_sz.addCell(new Label( 5 , 5 , "金额（元）" ,tsty));
				sheet_sz.setRowView(5, 500);
				
				sheet_sz.mergeCells( 0 ,6, 1 , 10 );
				sheet_sz.addCell(new Label( 0 , 6 , "生产经营性收入" ,tsty));
				sheet_sz.mergeCells( 2 ,6, 3 , 6 );
				sheet_sz.addCell(new Label( 2 , 6, "农业（水产）" ,tsty));
				if( "".equals(sz_map.get("v1"))||sz_map.get("v1")==null){
					sheet_sz.addCell(new Label( 4 , 6,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 6,  "".equals(sz_map.get("v1"))?"":sz_map.get("v1").toString() ,coty));
				}
				if( "".equals(sz_map.get("v2"))||sz_map.get("v2")==null){
					sheet_sz.addCell(new Label( 5 , 6,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 6,  "".equals(sz_map.get("v2"))?"":sz_map.get("v2").toString() ,coty));
				}
				sheet_sz.setRowView(6, 500);
				sheet_sz.mergeCells( 2 ,7, 3 , 7 );
				sheet_sz.addCell(new Label( 2 , 7, "畜牧业" ,tsty));
				if("".equals(sz_map.get("v3"))||sz_map.get("v3")==null){
					sheet_sz.addCell(new Label( 4 , 7,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 7,  "".equals(sz_map.get("v3"))?"":sz_map.get("v3").toString() ,coty));
				}
				if("".equals(sz_map.get("v4"))||sz_map.get("v4")==null){
					sheet_sz.addCell(new Label( 5 , 7,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 7,  "".equals(sz_map.get("v4"))?"":sz_map.get("v4").toString() ,coty));
				}
				
				sheet_sz.setRowView(7, 500);
				sheet_sz.mergeCells( 2 ,8, 3 , 8 );
				sheet_sz.addCell(new Label( 2 , 8, "林业" ,tsty));
				if("".equals(sz_map.get("v5"))||sz_map.get("v5")==null){
					sheet_sz.addCell(new Label( 4 , 8,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 8,  "".equals(sz_map.get("v5"))?"":sz_map.get("v5").toString() ,coty));
				}
				if("".equals(sz_map.get("v6"))||sz_map.get("v6")==null){
					sheet_sz.addCell(new Label( 5 , 8,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 8,  "".equals(sz_map.get("v6"))?"":sz_map.get("v6").toString() ,coty));
				}
				
				sheet_sz.setRowView(8, 500);
				sheet_sz.mergeCells( 2 ,9, 3 , 9 );
				sheet_sz.addCell(new Label( 2 , 9, "其它" ,tsty));
				if("".equals(sz_map.get("v7"))||sz_map.get("v7")==null){
					sheet_sz.addCell(new Label( 4 , 9,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 9,  "".equals(sz_map.get("v7"))?"":sz_map.get("v7").toString() ,coty));
				}
				if("".equals(sz_map.get("v8"))||sz_map.get("v8")==null){
					sheet_sz.addCell(new Label( 5 , 9,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 9,  "".equals(sz_map.get("v8"))?"":sz_map.get("v8").toString() ,coty));
				}
				sheet_sz.setRowView(9, 500);
				
				sheet_sz.mergeCells( 2 ,10, 3 , 10 );
				sheet_sz.addCell(new Label( 2 , 10, "小计" ,tsty));
				if("".equals(sz_map.get("v9"))||sz_map.get("v9")==null){
					sheet_sz.addCell(new Label( 4 , 10,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 10,  "".equals(sz_map.get("v9"))?"":sz_map.get("v9").toString() ,coty));
				}
				if("".equals(sz_map.get("v10"))||sz_map.get("v10")==null){
					sheet_sz.addCell(new Label( 5 , 10,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 10,  "".equals(sz_map.get("v10"))?"":sz_map.get("v10").toString() ,coty));
				}
				
				sheet_sz.setRowView(10, 500);
				
				sheet_sz.mergeCells( 0 ,11, 1 , 16 );
				sheet_sz.addCell(new Label( 0 , 11 , "政策性收入" ,tsty));
				sheet_sz.mergeCells( 2 ,11, 3 , 11 );
				sheet_sz.addCell(new Label( 2 , 11, "农林牧草、生态等补贴" ,tsty));
				if("".equals(sz_map.get("v11"))||sz_map.get("v11")==null){
					sheet_sz.addCell(new Label( 4 , 11,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 11,  "".equals(sz_map.get("v11"))?"":sz_map.get("v11").toString() ,coty));
				}
				if("".equals(sz_map.get("v12"))||sz_map.get("v12")==null){
					sheet_sz.addCell(new Label( 5 , 11,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 11,  "".equals(sz_map.get("v12"))?"":sz_map.get("v12").toString() ,coty));
				}
				
				sheet_sz.setRowView(11, 500);
				sheet_sz.mergeCells( 2 ,12, 3 , 12 );
				sheet_sz.addCell(new Label( 2 , 12, "养老金" ,tsty));
				if("".equals(sz_map.get("v13"))||sz_map.get("v13")==null){
					sheet_sz.addCell(new Label( 4 , 12, "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 12, "".equals(sz_map.get("v13"))?"":sz_map.get("v13").toString() ,coty));
				}
				if("".equals(sz_map.get("v14"))||sz_map.get("v14")==null){
					sheet_sz.addCell(new Label( 5 , 12,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 12,  sz_map.get("v14").toString() ,coty));
				}
				
				
				sheet_sz.setRowView(12, 500);
				sheet_sz.mergeCells( 2 ,13, 3 , 13 );
				sheet_sz.addCell(new Label( 2 , 13, "低保（五保）补贴" ,tsty));
				if("".equals(sz_map.get("v15"))||sz_map.get("v15")==null){
					sheet_sz.addCell(new Label( 4 , 13,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 13,  sz_map.get("v15").toString() ,coty));
				}
				if("".equals(sz_map.get("v16"))||sz_map.get("v16")==null){
					sheet_sz.addCell(new Label( 5 , 13,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 13,  sz_map.get("v16").toString() ,coty));
				}
				
				
				sheet_sz.setRowView(13, 500);
				sheet_sz.mergeCells( 2 ,14, 3 , 14 );
				sheet_sz.addCell(new Label( 2 ,14, "燃煤补贴" ,tsty));
				if("".equals(sz_map.get("v17"))||sz_map.get("v17")==null){
					sheet_sz.addCell(new Label( 4 ,14, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 ,14, sz_map.get("v17")==null?"":sz_map.get("v17").toString() ,coty));
				}
				if("".equals(sz_map.get("v18"))||sz_map.get("v18")==null){
					sheet_sz.addCell(new Label( 5 ,14, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 ,14, sz_map.get("v18")==null?"":sz_map.get("v18").toString() ,coty));
				}
				
				
				sheet_sz.setRowView(14, 500);
				
				sheet_sz.mergeCells( 2 ,15, 3 , 15 );
				sheet_sz.addCell(new Label( 2 , 15, "其他" ,tsty));
				if("".equals(sz_map.get("v19"))||sz_map.get("v19")==null){
					sheet_sz.addCell(new Label( 4 , 15,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 15,  "".equals(sz_map.get("v19"))?"":sz_map.get("v19").toString() ,coty));
				}
				if("".equals(sz_map.get("v20"))||sz_map.get("v20")==null){
					sheet_sz.addCell(new Label( 5 , 15, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 15, sz_map.get("v20")==null?"":sz_map.get("v20").toString() ,coty));
				}
				
				
				sheet_sz.setRowView(15, 500);
				
				sheet_sz.mergeCells( 2 ,16, 3 , 16 );
				sheet_sz.addCell(new Label( 2 , 16, "小计" ,tsty));
				if("".equals(sz_map.get("v21"))||sz_map.get("v21")==null){
					sheet_sz.addCell(new Label( 4 , 16,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 16,  "".equals(sz_map.get("v21"))?"":sz_map.get("v21").toString() ,coty));
				}
				if("".equals(sz_map.get("v22"))||sz_map.get("v22")==null){
					sheet_sz.addCell(new Label( 5 , 16,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 16,  "".equals(sz_map.get("v22"))?"":sz_map.get("v22").toString() ,coty));
				}
				sheet_sz.setRowView(16, 500);
				
				sheet_sz.mergeCells( 0 ,17, 1 , 18 );
				sheet_sz.addCell(new Label( 0 , 17 , "财产性收入" ,tsty));
				sheet_sz.mergeCells( 2 ,17, 3 , 17 );
				sheet_sz.addCell(new Label( 2 , 17, "土地、草牧场流转" ,tsty));
				if("".equals(sz_map.get("v23"))||sz_map.get("v23")==null){
					sheet_sz.addCell(new Label( 4 , 17,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 17,  "".equals(sz_map.get("v23"))?"":sz_map.get("v23").toString() ,coty));
				}
				if("".equals(sz_map.get("v24"))||sz_map.get("v24")==null){
					sheet_sz.addCell(new Label( 5 , 17,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 17,  "".equals(sz_map.get("v24"))?"":sz_map.get("v24").toString() ,coty));
				}
				sheet_sz.setRowView(17, 500);
				
				sheet_sz.mergeCells( 2 ,18, 3 , 18 );
				sheet_sz.addCell(new Label( 2 , 18, "其它" ,tsty));
				if("".equals(sz_map.get("v25"))||sz_map.get("v25")==null){
					sheet_sz.addCell(new Label( 4 , 18,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 18,  "".equals(sz_map.get("v25"))?"":sz_map.get("v25").toString() ,coty));
				}
				if("".equals(sz_map.get("v26"))||sz_map.get("v26")==null){
					sheet_sz.addCell(new Label( 5 , 18,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 18,  "".equals(sz_map.get("v26"))?"":sz_map.get("v26").toString() ,coty));
				}
				
				
				sheet_sz.setRowView(18, 500);
				
				sheet_sz.mergeCells( 0 ,19, 1 , 20 );
				sheet_sz.addCell(new Label( 0 , 19 , "工资性收入" ,tsty));
				
				sheet_sz.mergeCells( 2 ,19, 3 , 19 );
				if("".equals(sz_map.get("v35"))||sz_map.get("v35")==null){
					sheet_sz.addCell(new Label( 2 , 19,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 19,  "".equals(sz_map.get("v35"))?"":sz_map.get("v35").toString() ,coty));
				}
				if("".equals(sz_map.get("v27"))||sz_map.get("v27")==null){
					sheet_sz.addCell(new Label( 4 , 19,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 19,  "".equals(sz_map.get("v27"))?"":sz_map.get("v27").toString() ,coty));
				}
				if("".equals(sz_map.get("v28"))||sz_map.get("v28")==null){
					sheet_sz.addCell(new Label( 5 , 19,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 19,  "".equals(sz_map.get("v28"))?"":sz_map.get("v28").toString() ,coty));
				}
				sheet_sz.setRowView(19, 500);
				sheet_sz.mergeCells( 2 ,20, 3 , 20 );
				if("".equals(sz_map.get("v36"))||sz_map.get("v36")==null){
					sheet_sz.addCell(new Label( 2 , 20,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 20,  "".equals(sz_map.get("v36"))?"":sz_map.get("v36").toString() ,coty));
				}
				if("".equals(sz_map.get("v29"))||sz_map.get("v29")==null){
					sheet_sz.addCell(new Label( 4 , 20,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 20,  "".equals(sz_map.get("v29"))?"":sz_map.get("v29").toString() ,coty));
				}
				if("".equals(sz_map.get("v30"))||sz_map.get("v30")==null){
					sheet_sz.addCell(new Label( 5 , 20,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 20,  "".equals(sz_map.get("v30"))?"":sz_map.get("v30").toString() ,coty));
				}
				
				
				
				sheet_sz.setRowView(20, 500);
				
				sheet_sz.mergeCells( 0 ,21, 1 , 22 );
				sheet_sz.addCell(new Label( 0 , 21 , "其他收入" ,tsty));
				sheet_sz.mergeCells( 2 ,21, 3 , 21 );
				if("".equals(sz_map.get("v37"))||sz_map.get("v37")==null){
					sheet_sz.addCell(new Label( 2 , 21,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 21,  "".equals(sz_map.get("v37"))?"":sz_map.get("v37").toString() ,coty));
				}
				if("".equals(sz_map.get("v31"))||sz_map.get("v31")==null){
					sheet_sz.addCell(new Label( 4 , 21,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 21,  "".equals(sz_map.get("v31"))?"":sz_map.get("v31").toString() ,coty));
				}
				if("".equals(sz_map.get("v32"))||sz_map.get("v32")==null){
					sheet_sz.addCell(new Label( 5 , 21,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 21,  "".equals(sz_map.get("v32"))?"":sz_map.get("v32").toString() ,coty));
				}
				sheet_sz.setRowView(21, 500);
				
				sheet_sz.mergeCells( 2 ,22, 3 , 22 );
				if("".equals(sz_map.get("v38"))||sz_map.get("v38")==null){
					sheet_sz.addCell(new Label( 2 , 22,  "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 22,  sz_map.get("v38").toString() ,coty));
				}
				if("".equals(sz_map.get("v33"))||sz_map.get("v33")==null){
					sheet_sz.addCell(new Label( 4 , 22,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 22,  sz_map.get("v33").toString() ,coty));
				}
				if("".equals(sz_map.get("v34"))||sz_map.get("v34")==null){
					sheet_sz.addCell(new Label( 5 , 22,  "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 22,  sz_map.get("v34").toString() ,coty));
				}
				sheet_sz.setRowView(22, 500);
				
				sheet_sz.mergeCells( 0 ,23, 3 , 23 );
				sheet_sz.addCell(new Label( 0 , 23 , "总收入合计" ,tsty));
				sheet_sz.addCell(new Label( 4 , 23, "" ,coty));
				if("".equals(sz_map.get("v39"))||sz_map.get("v39")==null){
					sheet_sz.addCell(new Label( 5 , 23,  "0",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 23,  sz_map.get("v39").toString(),coty));
				}
				sheet_sz.setRowView(23, 500);
				
				
				sheet_sz.mergeCells( 0 ,25, 5 , 25 );
				sheet_sz.addCell(new Label( 0 , 25 , "帮扶后支出情况" ,tsty));
				sheet_sz.setRowView(25, 500);
				sheet_sz.mergeCells( 0 ,26, 3 , 26 );
				sheet_sz.addCell(new Label( 0 , 26 , "项目" ,tsty));
				sheet_sz.addCell(new Label( 4 , 26, "支出明细" ,tsty));
				sheet_sz.addCell(new Label( 5 , 26, "金额（元）" ,tsty));
				sheet_sz.setRowView(26, 500);
				sheet_sz.mergeCells( 0 ,27, 1 , 35 );
				sheet_sz.addCell(new Label( 0 , 27 , "生产经营性支出" ,tsty));
				sheet_sz.mergeCells( 2 ,27, 3 , 27 );
				sheet_sz.addCell(new Label( 2 , 27, "农资费用" ,tsty));
				if("".equals(zc_map.get("v1"))||zc_map.get("v1")==null){
					sheet_sz.addCell(new Label( 4 , 27, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 27, zc_map.get("v1")==null?"":zc_map.get("v1").toString() ,coty));
				}
				if("".equals(zc_map.get("v2"))||zc_map.get("v2")==null){
					sheet_sz.addCell(new Label( 5 , 27, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 27, zc_map.get("v2")==null?"":zc_map.get("v2").toString() ,coty));
				}
				sheet_sz.setRowView(27, 500);
				sheet_sz.mergeCells( 2 ,28, 3 , 28 );
				sheet_sz.addCell(new Label( 2 , 28, "固定资产折扣和租赁费" ,tsty));
				if("".equals(zc_map.get("v3"))||zc_map.get("v3")==null){
					sheet_sz.addCell(new Label( 4 , 28, "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 28, zc_map.get("v3")==null?"":zc_map.get("v3").toString() ,coty));
				}
				if("".equals(zc_map.get("v4"))||zc_map.get("v4")==null){
					sheet_sz.addCell(new Label( 5 , 28, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 28, zc_map.get("v4")==null?"":zc_map.get("v4").toString() ,coty));
				}
				sheet_sz.setRowView(28, 500);
				sheet_sz.mergeCells( 2 ,29, 3 , 29 );
				sheet_sz.addCell(new Label( 2 , 29, "水电燃料支出" ,tsty));
				if("".equals(zc_map.get("v5"))||zc_map.get("v5")==null){
					sheet_sz.addCell(new Label( 4 , 29,"" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 29,zc_map.get("v5")==null?"":zc_map.get("v5").toString() ,coty));
				}
				if("".equals(zc_map.get("v6"))||zc_map.get("v6")==null){
					sheet_sz.addCell(new Label( 5 , 29, "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 29, zc_map.get("v6")==null?"":zc_map.get("v6").toString() ,coty));
				}
				
				sheet_sz.setRowView(29, 500);
				sheet_sz.mergeCells( 2 ,30, 3 , 30 );
				sheet_sz.addCell(new Label( 2 , 30, "承包土地、草场费用" ,tsty));
				if("".equals(zc_map.get("v7"))||zc_map.get("v7")==null){
					sheet_sz.addCell(new Label( 4 , 30, "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 30, zc_map.get("v7")==null?"":zc_map.get("v7").toString() ,coty));
				}
				if("".equals(zc_map.get("v8"))||zc_map.get("v8")==null){
					sheet_sz.addCell(new Label( 5 , 30, "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 30, zc_map.get("v8")==null?"":zc_map.get("v8").toString() ,coty));
				}
				
				sheet_sz.setRowView(30, 500);
				sheet_sz.mergeCells( 2 ,31, 3 , 31 );
				sheet_sz.addCell(new Label( 2 , 31, "饲草料" ,tsty));
				if("".equals(zc_map.get("v9"))||zc_map.get("v9")==null){
					sheet_sz.addCell(new Label( 4 , 31, "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 31, zc_map.get("v9")==null?"":zc_map.get("v9").toString() ,coty));
				}
				if("".equals(zc_map.get("v10"))||zc_map.get("v10")==null){
					sheet_sz.addCell(new Label( 5 , 31,"" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 31, zc_map.get("v10")==null?"":zc_map.get("v10").toString() ,coty));
				}
				
				
				sheet_sz.setRowView(31, 500);
				sheet_sz.mergeCells( 2 ,32, 3 , 32 );
				sheet_sz.addCell(new Label( 2 , 32, "防疫防治支出" ,tsty));
				if("".equals(zc_map.get("v11"))||zc_map.get("v11")==null){
					sheet_sz.addCell(new Label( 4 , 32, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 32, zc_map.get("v11")==null?"":zc_map.get("v11").toString() ,coty));
				}
				if("".equals(zc_map.get("v12"))||zc_map.get("v12")==null){
					sheet_sz.addCell(new Label( 5 , 32, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 32, zc_map.get("v12")==null?"":zc_map.get("v12").toString() ,coty));
				}
				sheet_sz.setRowView(32, 500);
				sheet_sz.mergeCells( 2 ,33, 3 , 33 );
				sheet_sz.addCell(new Label( 2 , 33, "种（仔）畜" ,tsty));
				if("".equals(zc_map.get("v13"))||zc_map.get("v13")==null){
					sheet_sz.addCell(new Label( 4 , 33, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 33, zc_map.get("v13")==null?"":zc_map.get("v13").toString() ,coty));
				}
				if("".equals(zc_map.get("v14"))||zc_map.get("v14")==null){
					sheet_sz.addCell(new Label( 5 , 33, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 33, zc_map.get("v14")==null?"":zc_map.get("v14").toString() ,coty));
				}
				
				sheet_sz.setRowView(33, 500);
				sheet_sz.mergeCells( 2 ,34, 3 , 34 );
				sheet_sz.addCell(new Label( 2 , 34, "销售费用和通信费用" ,tsty));
				if("".equals(zc_map.get("v15"))||zc_map.get("v15")==null){
					sheet_sz.addCell(new Label( 4 , 34,"" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 34, zc_map.get("v15")==null?"":zc_map.get("v15").toString() ,coty));
				}
				if("".equals(zc_map.get("v16"))||zc_map.get("v16")==null){
					sheet_sz.addCell(new Label( 5 , 34,"" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 34,zc_map.get("v16")==null?"":zc_map.get("v16").toString() ,coty));
				}
				
				sheet_sz.setRowView(34, 500);
				sheet_sz.mergeCells( 2 ,35, 3 , 35 );
				sheet_sz.addCell(new Label( 2 , 35, "借贷利息" ,tsty));
				if("".equals(zc_map.get("v17"))||zc_map.get("v17")==null){
					sheet_sz.addCell(new Label( 4 , 35, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 35, zc_map.get("v17")==null?"":zc_map.get("v17").toString() ,coty));
				}
				if("".equals(zc_map.get("v18"))||zc_map.get("v18")==null){
					sheet_sz.addCell(new Label( 5 , 35, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 35, zc_map.get("v18")==null?"":zc_map.get("v18").toString() ,coty));
				}
				sheet_sz.setRowView(35, 500);
				
				sheet_sz.mergeCells( 0,36, 1 , 37 );
				sheet_sz.addCell(new Label( 0 , 36, "政策性支出" ,tsty));
				sheet_sz.mergeCells( 2 ,36, 3 , 36 );
				if("".equals(zc_map.get("v23"))||zc_map.get("v23")==null){
					sheet_sz.addCell(new Label( 2 , 36, "",coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 36, zc_map.get("v23")==null?"":zc_map.get("v23").toString() ,coty));
				}
				if("".equals(zc_map.get("v19"))||zc_map.get("v19")==null){
					sheet_sz.addCell(new Label( 4 , 36, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 36, zc_map.get("v19")==null?"":zc_map.get("v19").toString() ,coty));
				}
				if("".equals(zc_map.get("v20"))||zc_map.get("v20")==null){
					sheet_sz.addCell(new Label( 5 , 36, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 36, zc_map.get("v20")==null?"":zc_map.get("v20").toString() ,coty));
				}
				
				
				
				sheet_sz.setRowView(36, 500);
				sheet_sz.mergeCells( 2 ,37, 3 , 37 );
				if("".equals(zc_map.get("v24"))||zc_map.get("v24")==null){
					sheet_sz.addCell(new Label( 2 , 37, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 37, zc_map.get("v24")==null?"":zc_map.get("v24").toString() ,coty));
				}
				if("".equals(zc_map.get("v21"))||zc_map.get("v21")==null){
					sheet_sz.addCell(new Label( 4 , 37, "",coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 37, zc_map.get("v21")==null?"":zc_map.get("v21").toString() ,coty));
				}
				if("".equals(zc_map.get("v22"))||zc_map.get("v22")==null){
					sheet_sz.addCell(new Label( 5 , 37, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 37, zc_map.get("v22")==null?"":zc_map.get("v22").toString() ,coty));
				}
				sheet_sz.setRowView(37, 500);
				
				sheet_sz.mergeCells( 0,38, 1 , 39 );
				sheet_sz.addCell(new Label( 0 , 38, "其他支出" ,tsty));
				sheet_sz.mergeCells( 2 ,38, 3 , 38 );
				if("".equals(zc_map.get("v25"))||zc_map.get("v25")==null){
					sheet_sz.addCell(new Label( 2 , 38, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 38, zc_map.get("v25")==null?"":zc_map.get("v25").toString() ,coty));
				}
				if("".equals(zc_map.get("v26"))||zc_map.get("v26")==null){
					sheet_sz.addCell(new Label( 4 , 38, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 38, zc_map.get("v26")==null?"":zc_map.get("v26").toString() ,coty));
				}
				if("".equals(zc_map.get("v27"))||zc_map.get("v27")==null){
					sheet_sz.addCell(new Label( 5 , 38, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 38, zc_map.get("v27")==null?"":zc_map.get("v27").toString() ,coty));
				}
				sheet_sz.setRowView(38, 500);
				sheet_sz.mergeCells( 2 ,39, 3 , 39 );
				if("".equals(zc_map.get("v28"))||zc_map.get("v28")==null){
					sheet_sz.addCell(new Label( 2 , 39, "",coty));
				}else{
					sheet_sz.addCell(new Label( 2 , 39, zc_map.get("v28")==null?"":zc_map.get("v28").toString() ,coty));
				}
				if("".equals(zc_map.get("v29"))||zc_map.get("v29")==null){
					sheet_sz.addCell(new Label( 4 , 39, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 4 , 39, zc_map.get("29")==null?"":zc_map.get("v29").toString() ,coty));
				}
				if("".equals(zc_map.get("v30"))||zc_map.get("v30")==null){
					sheet_sz.addCell(new Label( 5 , 39, "",coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 39, zc_map.get("v30")==null?"":zc_map.get("v30").toString() ,coty));
				}
				sheet_sz.setRowView(39, 500);
				sheet_sz.mergeCells( 0 ,40, 3 , 40 );
				sheet_sz.addCell(new Label( 0 , 40 , "总支出合计" ,tsty));
				sheet_sz.addCell(new Label( 4 , 40, "" ,tsty));
				if("".equals(zc_map.get("v31"))||zc_map.get("v31")==null){
					sheet_sz.addCell(new Label( 5 , 40, "" ,coty));
				}else{
					sheet_sz.addCell(new Label( 5 , 40, zc_map.get("v31")==null?"":zc_map.get("v31").toString() ,coty));
				}
				
				sheet_sz.setRowView(40, 500);
				
				//帮扶人与走访记录
				WritableSheet sheet_bf = book.createSheet("帮扶与走访", 3);
				sheet_bf.setColumnView(0, 12);// 设置列宽
				sheet_bf.setColumnView(1, 18);
				sheet_bf.setColumnView(2, 14);
				sheet_bf.setColumnView(3, 12);
				sheet_bf.setColumnView(4, 10);
				sheet_bf.setColumnView(5, 18);

				sheet_bf.mergeCells(0, 0, 5, 0);
				sheet_bf.addCell(new Label(0, 0, "帮扶单位和责任人",tsty));
				sheet_bf.setRowView(0, 500);
				
				sheet_bf.addCell(new Label( 0 ,	1 ,"姓名",tsty));
				sheet_bf.mergeCells(1, 1, 2, 1);
				sheet_bf.addCell(new Label( 1 , 1 , "单位",tsty));
				sheet_bf.mergeCells(3, 1, 4, 1);
				sheet_bf.addCell(new Label( 3 , 1 , "职务",tsty));
				sheet_bf.addCell(new Label( 5 , 1 , "电话",tsty));
				
				sheet_bf.setRowView(1, 500);
				
				String sql_bf="SELECT da_household_id,telephone ,col_post, col_name,t2.v1 FROM sys_personal_household_many"+year+" a "
						+ "LEFT JOIN sys_personal"+year+" b ON a.sys_personal_id = b.pkid join da_company"+year+" t2 on b.da_company_id=t2.pkid where da_household_id="+pkid;
				SQLAdapter bf_sqlAdapter=new SQLAdapter(sql_bf);
				List<Map> bf_list=this.getBySqlMapper.findRecords(bf_sqlAdapter);
				
				int bf_count = 1;
				for (int i = 0; i < bf_list.size(); i++) {   //循环一个list里面的数据到excel中
					Map bf_map = bf_list.get(i);
					
					sheet_bf.addCell(new Label( 0 ,	bf_count+1 , bf_map.get("col_name")==null?"":bf_map.get("col_name").toString(),coty));
					sheet_bf.mergeCells(1, bf_count+1, 2, bf_count+1);
					sheet_bf.addCell(new Label( 1 , bf_count+1 , bf_map.get("v1")==null?"":bf_map.get("v1").toString(),coty));
					sheet_bf.mergeCells(3, bf_count+1, 4, bf_count+1);
					sheet_bf.addCell(new Label( 3 , bf_count+1 , bf_map.get("col_post")==null?"":bf_map.get("col_post").toString(),coty));
					sheet_bf.addCell(new Label( 5 , bf_count+1 ,  bf_map.get("telephone")==null?"":bf_map.get("telephone").toString(),coty));
					sheet_bf.setRowView(bf_count+1, 500);
					bf_count++;
					
				}
				
				
				String sql_bf_1="SELECT * from da_help_info"+year+" where da_household_id="+pkid;
				SQLAdapter bf1_sqlAdapter=new SQLAdapter(sql_bf_1);
				List<Map> bf1_list=this.getBySqlMapper.findRecords(bf1_sqlAdapter);
				
				Map bf_map = bf1_list.get(0);
				sheet_bf.addCell(new Label( 0 , bf_count+1 ,  "帮扶目标",coty));
				sheet_bf.mergeCells(1, bf_count+1, 5, bf_count+1);
				sheet_bf.addCell(new Label( 1 , bf_count+1 ,  bf_map.get("v1")==null?"":bf_map.get("v1").toString(),coty));
				sheet_bf.setRowView(bf_count+1, 2000);
				
				sheet_bf.addCell(new Label( 0 , bf_count+2 ,  "帮扶时限",coty));
				sheet_bf.mergeCells(1, bf_count+2, 5, bf_count+2);
				sheet_bf.addCell(new Label( 1 , bf_count+2 ,  bf_map.get("v2")==null?"":bf_map.get("v2").toString(),coty));
				sheet_bf.setRowView(bf_count+2, 1000);
				
				sheet_bf.addCell(new Label( 0 , bf_count+3 ,  "帮扶计划",coty));
				sheet_bf.mergeCells(1, bf_count+3, 5, bf_count+3);
				sheet_bf.addCell(new Label( 1 , bf_count+3 ,  bf_map.get("v3")==null?"":bf_map.get("v3").toString(),coty));
				sheet_bf.setRowView(bf_count+3, 2000);
				
				
				sheet_bf.mergeCells(0, bf_count+5, 5, bf_count+5);
				sheet_bf.addCell(new Label(0, bf_count+5, "帮扶人走访情况记录",tsty));
				sheet_bf.setRowView(bf_count+5, 500);
				
				sheet_bf.addCell(new Label( 0 ,	bf_count+6 , "走访时间",tsty));
				sheet_bf.addCell(new Label(1 ,	bf_count+6 , "帮扶干部",tsty));
				sheet_bf.mergeCells(2, bf_count+6, 5, bf_count+6);
				sheet_bf.addCell(new Label(2 ,	bf_count+6 , "走访情况记录",tsty));
				sheet_bf.setRowView(bf_count+6, 500);
				
				String zf_sql="SELECT v1,v2,v3 FROM da_help_visit"+year+" where da_household_id="+pkid+" ORDER BY v1 DESC";
				SQLAdapter zf_sqlAdapter=new SQLAdapter(zf_sql);
				List<Map> zf_list=this.getBySqlMapper.findRecords(zf_sqlAdapter);
				int zf_count=bf_count+6;
				int zf_num=5-zf_list.size();
				if(zf_list.size()>0){
					for(int i=0;i<zf_list.size();i++){
						Map zf_map = zf_list.get(i);
						zf_num=5-zf_list.size();
						sheet_bf.addCell(new Label( 0 ,	zf_count+1 , zf_map.get("v1")==null?"":zf_map.get("v1").toString(),coty));
						sheet_bf.addCell(new Label( 1 ,	zf_count+1 , zf_map.get("v2")==null?"":zf_map.get("v2").toString(),coty));
						sheet_bf.mergeCells(2, zf_count+1, 5, zf_count+1);
						sheet_bf.addCell(new Label( 2 ,	zf_count+1 , zf_map.get("v3")==null?"":zf_map.get("v3").toString(),coty));
						sheet_bf.setRowView(zf_count+1, 500);
						zf_count++;
					}
				}
				if(zf_num!=0){
					for(int i=0;i<zf_num;i++){
						sheet_bf.addCell(new Label( 0 ,	zf_count+1 , "",coty));
						sheet_bf.addCell(new Label( 1 ,	zf_count+1 , "",coty));
						sheet_bf.mergeCells(2, zf_count+1, 5, zf_count+1);
						sheet_bf.addCell(new Label( 2 ,	zf_count+1 , "",coty));
						sheet_bf.setRowView(zf_count+1, 500);
						zf_count++;
					}
				}
				
				
				//帮扶措施
				WritableSheet sheet_cs = book.createSheet("帮扶措施", 4);
				sheet_cs.setColumnView(0, 8);
				sheet_cs.setColumnView(1, 9);
				sheet_cs.setColumnView(2, 9);
				sheet_cs.setColumnView(3, 9);
				sheet_cs.setColumnView(4, 9);
				sheet_cs.setColumnView(5, 8);
				sheet_cs.setColumnView(6, 8);
				sheet_cs.setColumnView(7, 9);
				sheet_cs.setColumnView(9, 8);
				sheet_cs.setColumnView(9, 8);
				sheet_cs.setColumnView(10, 9);
				sheet_cs.setColumnView(11, 8);
				sheet_cs.setColumnView(12, 8);
				sheet_cs.setColumnView(13, 9);
				sheet_cs.setColumnView(14, 8);

				sheet_cs.mergeCells(0, 0, 14, 0);
				sheet_cs.addCell(new Label(0, 0, "帮扶措施",tsty));
				sheet_cs.setRowView(0, 500);
				
				sheet_cs.mergeCells(0, 1, 0, 2);
				sheet_cs.addCell(new Label(0, 1, "项目类别",tsty));
				sheet_cs.mergeCells(1, 1, 1, 2);
				sheet_cs.addCell(new Label(1, 1, "扶持措施",tsty));
				sheet_cs.mergeCells(2, 1, 2, 2);
				sheet_cs.addCell(new Label(2, 1, "是否符合扶持条件",tsty));
				
				sheet_cs.mergeCells(3, 1, 5, 1);
				sheet_cs.addCell(new Label(3, 1, "2016年",tsty));
				sheet_cs.addCell(new Label(3, 2, "项目需求量",tsty));
				sheet_cs.addCell(new Label(4, 2, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(5, 2, "项目类别",tsty));
				
				sheet_cs.mergeCells(6, 1, 8, 1);
				sheet_cs.addCell(new Label(6, 1, "2017年",tsty));
				sheet_cs.addCell(new Label(6, 2, "项目需求量",tsty));
				sheet_cs.addCell(new Label(7, 2, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(8, 2, "落实时间",tsty));
				
				sheet_cs.mergeCells(9, 1, 11, 1);
				sheet_cs.addCell(new Label(9, 1, "2018年",tsty));
				sheet_cs.addCell(new Label(9, 2, "项目需求量",tsty));
				sheet_cs.addCell(new Label(10, 2, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(11, 2, "落实时间",tsty));
				sheet_cs.mergeCells(12, 1, 14, 1);
				sheet_cs.addCell(new Label(12, 1, "2019年",tsty));
				sheet_cs.addCell(new Label(12, 2, "项目需求量",tsty));
				sheet_cs.addCell(new Label(13, 2, "受益资金/政策",tsty));
				sheet_cs.addCell(new Label(14, 2, "落实时间",tsty));
				
				
				sheet_cs.setRowView(1, 500);
				sheet_cs.setRowView(2, 500);
				
				String cs_sql=" select v1,v2,v3, ";
				cs_sql += " MAX(CASE v7 WHEN '2016' THEN v4 ELSE '' END ) v4_2016, ";
				cs_sql += " MAX(CASE v7 WHEN '2016' THEN v5 ELSE '' END ) v5_2016, ";
				cs_sql += " MAX(CASE v7 WHEN '2016' THEN v6 ELSE '' END ) v6_2016, ";
				cs_sql += " MAX(CASE v7 WHEN '2017' THEN v4 ELSE '' END ) v4_2017, ";
				cs_sql += " MAX(CASE v7 WHEN '2017' THEN v5 ELSE '' END ) v5_2017, ";
				cs_sql += " MAX(CASE v7 WHEN '2017' THEN v6 ELSE '' END ) v6_2017, ";
				cs_sql += " MAX(CASE v7 WHEN '2018' THEN v4 ELSE '' END ) v4_2018, ";
				cs_sql += " MAX(CASE v7 WHEN '2018' THEN v5 ELSE '' END ) v5_2018, ";
				cs_sql += " MAX(CASE v7 WHEN '2018' THEN v6 ELSE '' END ) v6_2018, ";
				cs_sql += " MAX(CASE v7 WHEN '2019' THEN v4 ELSE '' END ) v4_2019, ";
				cs_sql += " MAX(CASE v7 WHEN '2019' THEN v5 ELSE '' END ) v5_2019, ";
				cs_sql += " MAX(CASE v7 WHEN '2019' THEN v6 ELSE '' END ) v6_2019 ";
				cs_sql += " from da_help_tz_measures"+year+" where da_household_id="+pkid+" group  by v1,v2,v3 ";
				SQLAdapter cs_sqlAdapter=new SQLAdapter(cs_sql);
				List<Map> cs_list=this.getBySqlMapper.findRecords(cs_sqlAdapter);
				int cs_count=2;
				int cs_num=10-cs_list.size();
				if(cs_list.size()>0){
					for(int i=0;i<cs_list.size();i++){
						Map cs_map = cs_list.get(i);
						sheet_cs.addCell(new Label( 0 ,	cs_count+1 , cs_map.get("v1")==null?"":cs_map.get("v1").toString(),coty));
						sheet_cs.addCell(new Label( 1 ,	cs_count+1 , cs_map.get("v2")==null?"":cs_map.get("v2").toString(),coty));
						sheet_cs.addCell(new Label( 2 ,	cs_count+1 , cs_map.get("v3")==null?"":cs_map.get("v3").toString(),coty));
						sheet_cs.addCell(new Label( 3 ,	cs_count+1 , cs_map.get("v4_2016")==null?"":cs_map.get("v4_2016").toString(),coty));
						sheet_cs.addCell(new Label( 4 ,	cs_count+1 , cs_map.get("v5_2016")==null?"":cs_map.get("v5_2016").toString(),coty));
						sheet_cs.addCell(new Label( 5 ,	cs_count+1 , cs_map.get("v6_2016")==null?"":cs_map.get("v6_2016").toString(),coty));
						sheet_cs.addCell(new Label( 6 ,	cs_count+1 , cs_map.get("v4_2017")==null?"":cs_map.get("v4_2017").toString(),coty));
						sheet_cs.addCell(new Label( 7 ,	cs_count+1 , cs_map.get("v5_2017")==null?"":cs_map.get("v5_2017").toString(),coty));
						sheet_cs.addCell(new Label( 8 ,	cs_count+1 , cs_map.get("v6_2017")==null?"":cs_map.get("v6_2017").toString(),coty));
						sheet_cs.addCell(new Label( 9 ,	cs_count+1 , cs_map.get("v4_2018")==null?"":cs_map.get("v4_2018").toString(),coty));
						sheet_cs.addCell(new Label( 10 ,	cs_count+1 , cs_map.get("v5_2018")==null?"":cs_map.get("v5_2018").toString(),coty));
						sheet_cs.addCell(new Label( 11,	cs_count+1 , cs_map.get("v6_2018")==null?"":cs_map.get("v6_2018").toString(),coty));
						sheet_cs.addCell(new Label( 12 ,	cs_count+1 , cs_map.get("v4_2019")==null?"":cs_map.get("v4_2019").toString(),coty));
						sheet_cs.addCell(new Label( 13,	cs_count+1 , cs_map.get("v5_2017")==null?"":cs_map.get("v5_2019").toString(),coty));
						sheet_cs.addCell(new Label( 14,	cs_count+1 , cs_map.get("v6_2019")==null?"":cs_map.get("v6_2019").toString(),coty));
						sheet_cs.setRowView(cs_count+1, 500);
						cs_count++;
					}
				}
				if(cs_num>0){
					for(int i=0;i<cs_num;i++){
						sheet_cs.addCell(new Label( 0 ,	cs_count+1 ,"",coty));
						sheet_cs.addCell(new Label( 1 ,	cs_count+1 ,"",coty));
						sheet_cs.addCell(new Label( 2 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 3 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 4 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 5 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 6 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 7 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 8 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 9 ,	cs_count+1 , "",coty));
						sheet_cs.addCell(new Label( 10 ,	cs_count+1 , "".toString(),coty));
						sheet_cs.addCell(new Label( 11,	cs_count+1 ,"".toString(),coty));
						sheet_cs.addCell(new Label( 12 ,	cs_count+1 , "".toString(),coty));
						sheet_cs.addCell(new Label( 13,	cs_count+1 ,"",coty));
						sheet_cs.addCell(new Label( 14,	cs_count+1 , "",coty));
						sheet_cs.setRowView(cs_count+1, 500);
						cs_count++;
					}
				}
				
				//帮扶后收支分析
				WritableSheet sheet_bfh = book.createSheet("帮扶后收支", 6);
				//当前收支da_helpback_income
				String szh_sql="SELECT * FROM da_helpback_income"+year+" where da_household_id="+pkid;
				SQLAdapter szh_sqlSQLAdapter=new SQLAdapter(szh_sql);
				List<Map> szh_list=this.getBySqlMapper.findRecords(szh_sqlSQLAdapter);
				Map szh_map=szh_list.get(0);
				
				String zch_sql="SELECT * FROM da_helpback_expenditure"+year+" where da_household_id="+pkid;
				SQLAdapter zch_sqlAdapter=new SQLAdapter(zch_sql);
				List<Map> zch_list=this.getBySqlMapper.findRecords(zch_sqlAdapter);
				Map zch_map=zch_list.get(0);
				sheet_bfh.setColumnView(0, 18);//设置列宽
				sheet_bfh.setColumnView(1, 10);
				sheet_bfh.setColumnView(2, 18);
				sheet_bfh.setColumnView(3, 10);
				sheet_bfh.setColumnView(4, 18);
				sheet_bfh.setColumnView(5, 10);
				
				sheet_bfh.mergeCells( 0 , 0 , 5 , 0 );
				sheet_bfh.addCell(new Label( 0 , 0 , "帮扶后收支分析" ,tsty));
				sheet_bfh.setRowView(0, 500);
				
				sheet_bfh.mergeCells( 0 , 1 , 0 , 2 );
				sheet_bfh.addCell(new Label( 0 , 1 , "年人均纯收入(元)" ,tsty));
				
				sheet_bfh.mergeCells( 1 , 1 , 5 , 1 );
//				String tab1="年总收入("+sz_map.get("v39")==null?"0":sz_map.get("v39").toString()+")-年总支出()=年纯收入()";
//				System.out.println(tab1);
				
				String aa="";
				String bb="";
				if("".equals(szh_map.get("v39"))||szh_map.get("v39")==null){
					aa="0";
				}else{
					aa=szh_map.get("v39").toString();
				}
				if("".equals(zch_map.get("v31"))||zch_map.get("v31")==null){
					bb="0";
				}else{
					bb=zch_map.get("v31").toString();
				}
				double ccc=Double.parseDouble(aa);
				double cc1=Double.parseDouble(bb);
				double cc2=ccc-cc1;
				
				String st1=String.format("%.2f", cc2);
				String tab1="年总收入（"+aa+"）-年总支出（"+bb+"）=年纯收入（"+st1+"）";
				sheet_bfh.addCell(new Label( 1 , 1 , tab1,coty));
				
				
				double bc=Double.parseDouble(huzhu_map.get("v9").toString());
				double hnum=cc2/bc;
				String str2=String.format("%.2f", hnum);
				String tab2="年纯收入（"+st1+"）/家庭人数（"+huzhu_map.get("v9").toString()+"）=年人均纯收入（"+str2+"）";
				
				sheet_bfh.mergeCells( 1 , 2, 5 , 2 );
				sheet_bfh.addCell(new Label( 1 , 2 ,tab2 ,coty));
				sheet_bfh.setRowView(1, 500);
				
				sheet_bfh.setRowView(2, 500);
				
				sheet_bfh.setColumnView(0, 18);//设置列宽
				sheet_bfh.setColumnView(1, 10);
				sheet_bfh.setColumnView(2, 18);
				sheet_bfh.setColumnView(3, 10);
				sheet_bfh.setColumnView(4, 18);
				sheet_bfh.setColumnView(5, 10);
				sheet_bfh.mergeCells( 0 ,4 , 5 , 4 );
				sheet_bfh.addCell(new Label( 0 , 4, "当前收入情况" ,tsty));
				sheet_bfh.setRowView(4, 500);
				sheet_bfh.mergeCells( 0 ,5 , 3 , 5 );
				sheet_bfh.addCell(new Label( 0 , 5 , "项目" ,tsty));
				
				sheet_bfh.addCell(new Label( 4 , 5, "收入明细" ,tsty));
				sheet_bfh.addCell(new Label( 5 , 5 , "项目" ,tsty));
				sheet_bfh.setRowView(5, 500);
				
				sheet_bfh.mergeCells( 0 ,6, 1 , 10 );
				sheet_bfh.addCell(new Label( 0 , 6 , "生产经营性收入" ,tsty));
				sheet_bfh.mergeCells( 2 ,6, 3 , 6 );
				sheet_bfh.addCell(new Label( 2 , 6, "农业（水产）" ,tsty));
				if( "".equals(szh_map.get("v1"))||szh_map.get("v1")==null){
					sheet_bfh.addCell(new Label( 4 , 6,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 6,  "".equals(szh_map.get("v1"))?"":szh_map.get("v1").toString() ,coty));
				}
				if( "".equals(szh_map.get("v2"))||szh_map.get("v2")==null){
					sheet_bfh.addCell(new Label( 5 , 6,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 6,  "".equals(szh_map.get("v2"))?"":szh_map.get("v2").toString() ,coty));
				}
				if( "".equals(szh_map.get("v3"))||szh_map.get("v3")==null){
					sheet_bfh.addCell(new Label( 5 , 6,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 6,  "".equals(szh_map.get("v2"))?"":szh_map.get("v2").toString() ,coty));
				}
				sheet_bfh.setRowView(6, 500);
				sheet_bfh.mergeCells( 2 ,7, 3 , 7 );
				sheet_bfh.addCell(new Label( 2 , 7, "畜牧业" ,tsty));
				if("".equals(szh_map.get("v3"))||szh_map.get("v3")==null){
					sheet_bfh.addCell(new Label( 4 , 7,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 7,  "".equals(szh_map.get("v3"))?"":szh_map.get("v3").toString() ,coty));
				}
				if("".equals(szh_map.get("v4"))||szh_map.get("v4")==null){
					sheet_bfh.addCell(new Label( 5 , 7,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 7,  "".equals(szh_map.get("v4"))?"":szh_map.get("v4").toString() ,coty));
				}
				
				sheet_bfh.setRowView(7, 500);
				sheet_bfh.mergeCells( 2 ,8, 3 , 8 );
				sheet_bfh.addCell(new Label( 2 , 8, "林业" ,tsty));
				if("".equals(szh_map.get("v5"))||szh_map.get("v5")==null){
					sheet_bfh.addCell(new Label( 4 , 8,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 8,  "".equals(szh_map.get("v5"))?"":szh_map.get("v5").toString() ,coty));
				}
				if("".equals(szh_map.get("v6"))||szh_map.get("v6")==null){
					sheet_bfh.addCell(new Label( 5 , 8,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 8,  "".equals(szh_map.get("v6"))?"":szh_map.get("v6").toString() ,coty));
				}
				
				sheet_bfh.setRowView(8, 500);
				sheet_bfh.mergeCells( 2 ,9, 3 , 9 );
				sheet_bfh.addCell(new Label( 2 , 9, "其它" ,tsty));
				if("".equals(szh_map.get("v7"))||szh_map.get("v7")==null){
					sheet_bfh.addCell(new Label( 4 , 9,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 9,  "".equals(szh_map.get("v7"))?"":szh_map.get("v7").toString() ,coty));
				}
				if("".equals(szh_map.get("v8"))||szh_map.get("v8")==null){
					sheet_bfh.addCell(new Label( 5 , 9,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 9,  "".equals(szh_map.get("v8"))?"":szh_map.get("v8").toString() ,coty));
				}
				sheet_bfh.setRowView(9, 500);
				
				sheet_bfh.mergeCells( 2 ,10, 3 , 10 );
				sheet_bfh.addCell(new Label( 2 , 10, "小计" ,tsty));
				if("".equals(szh_map.get("v9"))||szh_map.get("v9")==null){
					sheet_bfh.addCell(new Label( 4 , 10,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 10,  "".equals(szh_map.get("v9"))?"":szh_map.get("v9").toString() ,coty));
				}
				if("".equals(szh_map.get("v10"))||szh_map.get("v10")==null){
					sheet_bfh.addCell(new Label( 5 , 10,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 10,  "".equals(szh_map.get("v10"))?"":szh_map.get("v10").toString() ,coty));
				}
				
				sheet_bfh.setRowView(10, 500);
				
				sheet_bfh.mergeCells( 0 ,11, 1 , 16 );
				sheet_bfh.addCell(new Label( 0 , 11 , "政策性收入" ,tsty));
				sheet_bfh.mergeCells( 2 ,11, 3 , 11 );
				sheet_bfh.addCell(new Label( 2 , 11, "农林牧草、生态等补贴" ,tsty));
				if("".equals(szh_map.get("v11"))||szh_map.get("v11")==null){
					sheet_bfh.addCell(new Label( 4 , 11,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 11,  "".equals(szh_map.get("v11"))?"":szh_map.get("v11").toString() ,coty));
				}
				if("".equals(szh_map.get("v12"))||szh_map.get("v12")==null){
					sheet_bfh.addCell(new Label( 5 , 11,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 11,  "".equals(szh_map.get("v12"))?"":szh_map.get("v12").toString() ,coty));
				}
				
				sheet_bfh.setRowView(11, 500);
				sheet_bfh.mergeCells( 2 ,12, 3 , 12 );
				sheet_bfh.addCell(new Label( 2 , 12, "养老金" ,tsty));
				if("".equals(szh_map.get("v13"))||szh_map.get("v13")==null){
					sheet_bfh.addCell(new Label( 4 , 12, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 12, "".equals(szh_map.get("v13"))?"":szh_map.get("v13").toString() ,coty));
				}
				if("".equals(szh_map.get("v14"))||szh_map.get("v14")==null){
					sheet_bfh.addCell(new Label( 5 , 12,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 12,  szh_map.get("v14").toString() ,coty));
				}
				
				
				sheet_bfh.setRowView(12, 500);
				sheet_bfh.mergeCells( 2 ,13, 3 , 13 );
				sheet_bfh.addCell(new Label( 2 , 13, "低保（五保）补贴" ,tsty));
				if("".equals(szh_map.get("v15"))||szh_map.get("v15")==null){
					sheet_bfh.addCell(new Label( 4 , 13,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 13,  szh_map.get("v15").toString() ,coty));
				}
				if("".equals(szh_map.get("v16"))||szh_map.get("v16")==null){
					sheet_bfh.addCell(new Label( 5 , 13,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 13,  szh_map.get("v16").toString() ,coty));
				}
				
				
				sheet_bfh.setRowView(13, 500);
				sheet_bfh.mergeCells( 2 ,14, 3 , 14 );
				sheet_bfh.addCell(new Label( 2 ,14, "燃煤补贴" ,tsty));
				if("".equals(szh_map.get("v17"))||szh_map.get("v17")==null){
					sheet_bfh.addCell(new Label( 4 ,14, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 ,14, szh_map.get("v17")==null?"":szh_map.get("v17").toString() ,coty));
				}
				if("".equals(szh_map.get("v18"))||szh_map.get("v18")==null){
					sheet_bfh.addCell(new Label( 5 ,14, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 ,14, szh_map.get("v18")==null?"":szh_map.get("v18").toString() ,coty));
				}
				
				
				sheet_bfh.setRowView(14, 500);
				
				sheet_bfh.mergeCells( 2 ,15, 3 , 15 );
				sheet_bfh.addCell(new Label( 2 , 15, "其他" ,tsty));
				if("".equals(szh_map.get("v19"))||szh_map.get("v19")==null){
					sheet_bfh.addCell(new Label( 4 , 15,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 15,  "".equals(szh_map.get("v19"))?"":szh_map.get("v19").toString() ,coty));
				}
				if("".equals(szh_map.get("v20"))||szh_map.get("v20")==null){
					sheet_bfh.addCell(new Label( 5 , 15, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 15, szh_map.get("v20")==null?"":szh_map.get("v20").toString() ,coty));
				}
				
				
				sheet_bfh.setRowView(15, 500);
				
				sheet_bfh.mergeCells( 2 ,16, 3 , 16 );
				sheet_bfh.addCell(new Label( 2 , 16, "小计" ,tsty));
				if("".equals(szh_map.get("v21"))||szh_map.get("v21")==null){
					sheet_bfh.addCell(new Label( 4 , 16,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 16,  "".equals(szh_map.get("v21"))?"":szh_map.get("v21").toString() ,coty));
				}
				if("".equals(szh_map.get("v22"))||szh_map.get("v22")==null){
					sheet_bfh.addCell(new Label( 5 , 16,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 16,  "".equals(szh_map.get("v22"))?"":szh_map.get("v22").toString() ,coty));
				}
				sheet_bfh.setRowView(16, 500);
				
				sheet_bfh.mergeCells( 0 ,17, 1 , 18 );
				sheet_bfh.addCell(new Label( 0 , 17 , "财产性收入" ,tsty));
				sheet_bfh.mergeCells( 2 ,17, 3 , 17 );
				sheet_bfh.addCell(new Label( 2 , 17, "土地、草牧场流转" ,tsty));
				if("".equals(szh_map.get("v23"))||szh_map.get("v23")==null){
					sheet_bfh.addCell(new Label( 4 , 17,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 17,  "".equals(szh_map.get("v23"))?"":szh_map.get("v23").toString() ,coty));
				}
				if("".equals(szh_map.get("v24"))||szh_map.get("v24")==null){
					sheet_bfh.addCell(new Label( 5 , 17,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 17,  "".equals(szh_map.get("v24"))?"":szh_map.get("v24").toString() ,coty));
				}
				sheet_bfh.setRowView(17, 500);
				
				sheet_bfh.mergeCells( 2 ,18, 3 , 18 );
				sheet_bfh.addCell(new Label( 2 , 18, "其它" ,tsty));
				if("".equals(szh_map.get("v25"))||szh_map.get("v25")==null){
					sheet_bfh.addCell(new Label( 4 , 18,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 18,  "".equals(szh_map.get("v25"))?"":szh_map.get("v25").toString() ,coty));
				}
				if("".equals(szh_map.get("v26"))||szh_map.get("v26")==null){
					sheet_bfh.addCell(new Label( 5 , 18,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 18,  "".equals(szh_map.get("v26"))?"":szh_map.get("v26").toString() ,coty));
				}
				
				
				sheet_bfh.setRowView(18, 500);
				
				sheet_bfh.mergeCells( 0 ,19, 1 , 20 );
				sheet_bfh.addCell(new Label( 0 , 19 , "工资性收入" ,tsty));
				
				sheet_bfh.mergeCells( 2 ,19, 3 , 19 );
				if("".equals(szh_map.get("v35"))||szh_map.get("v35")==null){
					sheet_bfh.addCell(new Label( 2 , 19,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 19,  "".equals(szh_map.get("v35"))?"":szh_map.get("v35").toString() ,coty));
				}
				if("".equals(szh_map.get("v27"))||szh_map.get("v27")==null){
					sheet_bfh.addCell(new Label( 4 , 19,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 19,  "".equals(szh_map.get("v27"))?"":szh_map.get("v27").toString() ,coty));
				}
				if("".equals(szh_map.get("v28"))||szh_map.get("v28")==null){
					sheet_bfh.addCell(new Label( 5 , 19,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 19,  "".equals(szh_map.get("v28"))?"":szh_map.get("v28").toString() ,coty));
				}
				sheet_bfh.setRowView(19, 500);
				sheet_bfh.mergeCells( 2 ,20, 3 , 20 );
				if("".equals(szh_map.get("v36"))||szh_map.get("v36")==null){
					sheet_bfh.addCell(new Label( 2 , 20,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 20,  "".equals(szh_map.get("v36"))?"":szh_map.get("v36").toString() ,coty));
				}
				if("".equals(szh_map.get("v29"))||szh_map.get("v29")==null){
					sheet_bfh.addCell(new Label( 4 , 20,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 20,  "".equals(szh_map.get("v29"))?"":szh_map.get("v29").toString() ,coty));
				}
				if("".equals(szh_map.get("v30"))||szh_map.get("v30")==null){
					sheet_bfh.addCell(new Label( 5 , 20,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 20,  "".equals(szh_map.get("v30"))?"":szh_map.get("v30").toString() ,coty));
				}
				
				
				
				sheet_bfh.setRowView(20, 500);
				
				sheet_bfh.mergeCells( 0 ,21, 1 , 22 );
				sheet_bfh.addCell(new Label( 0 , 21 , "其他收入" ,tsty));
				sheet_bfh.mergeCells( 2 ,21, 3 , 21 );
				if("".equals(szh_map.get("v37"))||szh_map.get("v37")==null){
					sheet_bfh.addCell(new Label( 2 , 21,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 21,  "".equals(szh_map.get("v37"))?"":szh_map.get("v37").toString() ,coty));
				}
				if("".equals(szh_map.get("v31"))||szh_map.get("v31")==null){
					sheet_bfh.addCell(new Label( 4 , 21,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 21,  "".equals(szh_map.get("v31"))?"":szh_map.get("v31").toString() ,coty));
				}
				if("".equals(szh_map.get("v32"))||szh_map.get("v32")==null){
					sheet_bfh.addCell(new Label( 5 , 21,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 21,  "".equals(szh_map.get("v32"))?"":szh_map.get("v32").toString() ,coty));
				}
				sheet_bfh.setRowView(21, 500);
				
				sheet_bfh.mergeCells( 2 ,22, 3 , 22 );
				if("".equals(szh_map.get("v38"))||szh_map.get("v38")==null){
					sheet_bfh.addCell(new Label( 2 , 22,  "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 22,  szh_map.get("v38").toString() ,coty));
				}
				if("".equals(szh_map.get("v33"))||szh_map.get("v33")==null){
					sheet_bfh.addCell(new Label( 4 , 22,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 22,  szh_map.get("v33").toString() ,coty));
				}
				if("".equals(szh_map.get("v34"))||szh_map.get("v34")==null){
					sheet_bfh.addCell(new Label( 5 , 22,  "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 22,  szh_map.get("v34").toString() ,coty));
				}
				sheet_bfh.setRowView(22, 500);
				
				sheet_bfh.mergeCells( 0 ,23, 3 , 23 );
				sheet_bfh.addCell(new Label( 0 , 23 , "总收入合计" ,tsty));
				sheet_bfh.addCell(new Label( 4 , 23, "" ,coty));
				if("".equals(szh_map.get("v39"))||szh_map.get("v39")==null){
					sheet_bfh.addCell(new Label( 5 , 23,  "0",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 23,  szh_map.get("v39").toString(),coty));
				}
				sheet_bfh.setRowView(23, 500);
				
				
				sheet_bfh.mergeCells( 0 ,25, 5 , 25 );
				sheet_bfh.addCell(new Label( 0 , 25 , "帮扶后支出情况" ,tsty));
				sheet_bfh.setRowView(25, 500);
				sheet_bfh.mergeCells( 0 ,26, 3 , 26 );
				sheet_bfh.addCell(new Label( 0 , 26 , "项目" ,tsty));
				sheet_bfh.addCell(new Label( 4 , 26, "支出明细" ,tsty));
				sheet_bfh.addCell(new Label( 5 , 26, "金额（元）" ,tsty));
				sheet_bfh.setRowView(26, 500);
				sheet_bfh.mergeCells( 0 ,27, 1 , 35 );
				sheet_bfh.addCell(new Label( 0 , 27 , "生产经营性支出" ,tsty));
				sheet_bfh.mergeCells( 2 ,27, 3 , 27 );
				sheet_bfh.addCell(new Label( 2 , 27, "农资费用" ,tsty));
				if("".equals(zch_map.get("v1"))||zch_map.get("v1")==null){
					sheet_bfh.addCell(new Label( 4 , 27, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 27, zch_map.get("v1")==null?"":zch_map.get("v1").toString() ,coty));
				}
				if("".equals(zch_map.get("v2"))||zch_map.get("v2")==null){
					sheet_bfh.addCell(new Label( 5 , 27, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 27, zch_map.get("v2")==null?"":zch_map.get("v2").toString() ,coty));
				}
				sheet_bfh.setRowView(27, 500);
				sheet_bfh.mergeCells( 2 ,28, 3 , 28 );
				sheet_bfh.addCell(new Label( 2 , 28, "固定资产折扣和租赁费" ,tsty));
				if("".equals(zch_map.get("v3"))||zch_map.get("v3")==null){
					sheet_bfh.addCell(new Label( 4 , 28, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 28, zch_map.get("v3")==null?"":zch_map.get("v3").toString() ,coty));
				}
				if("".equals(zch_map.get("v4"))||zch_map.get("v4")==null){
					sheet_bfh.addCell(new Label( 5 , 28, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 28, zch_map.get("v4")==null?"":zch_map.get("v4").toString() ,coty));
				}
				sheet_bfh.setRowView(28, 500);
				sheet_bfh.mergeCells( 2 ,29, 3 , 29 );
				sheet_bfh.addCell(new Label( 2 , 29, "水电燃料支出" ,tsty));
				if("".equals(zch_map.get("v5"))||zch_map.get("v5")==null){
					sheet_bfh.addCell(new Label( 4 , 29,"" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 29,zch_map.get("v5")==null?"":zch_map.get("v5").toString() ,coty));
				}
				if("".equals(zch_map.get("v6"))||zch_map.get("v6")==null){
					sheet_bfh.addCell(new Label( 5 , 29, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 29, zch_map.get("v6")==null?"":zch_map.get("v6").toString() ,coty));
				}
				
				sheet_bfh.setRowView(29, 500);
				sheet_bfh.mergeCells( 2 ,30, 3 , 30 );
				sheet_bfh.addCell(new Label( 2 , 30, "承包土地、草场费用" ,tsty));
				if("".equals(zch_map.get("v7"))||zch_map.get("v7")==null){
					sheet_bfh.addCell(new Label( 4 , 30, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 30, zch_map.get("v7")==null?"":zch_map.get("v7").toString() ,coty));
				}
				if("".equals(zch_map.get("v8"))||zch_map.get("v8")==null){
					sheet_bfh.addCell(new Label( 5 , 30, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 30, zch_map.get("v8")==null?"":zch_map.get("v8").toString() ,coty));
				}
				
				sheet_bfh.setRowView(30, 500);
				sheet_bfh.mergeCells( 2 ,31, 3 , 31 );
				sheet_bfh.addCell(new Label( 2 , 31, "饲草料" ,tsty));
				if("".equals(zch_map.get("v9"))||zch_map.get("v9")==null){
					sheet_bfh.addCell(new Label( 4 , 31, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 31, zch_map.get("v9")==null?"":zch_map.get("v9").toString() ,coty));
				}
				if("".equals(zch_map.get("v10"))||zch_map.get("v10")==null){
					sheet_bfh.addCell(new Label( 5 , 31,"" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 31, zch_map.get("v10")==null?"":zch_map.get("v10").toString() ,coty));
				}
				
				
				sheet_bfh.setRowView(31, 500);
				sheet_bfh.mergeCells( 2 ,32, 3 , 32 );
				sheet_bfh.addCell(new Label( 2 , 32, "防疫防治支出" ,tsty));
				if("".equals(zch_map.get("v11"))||zch_map.get("v11")==null){
					sheet_bfh.addCell(new Label( 4 , 32, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 32, zch_map.get("v11")==null?"":zch_map.get("v11").toString() ,coty));
				}
				if("".equals(zch_map.get("v12"))||zch_map.get("v12")==null){
					sheet_bfh.addCell(new Label( 5 , 32, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 32, zch_map.get("v12")==null?"":zch_map.get("v12").toString() ,coty));
				}
				sheet_bfh.setRowView(32, 500);
				sheet_bfh.mergeCells( 2 ,33, 3 , 33 );
				sheet_bfh.addCell(new Label( 2 , 33, "种（仔）畜" ,tsty));
				if("".equals(zch_map.get("v13"))||zch_map.get("v13")==null){
					sheet_bfh.addCell(new Label( 4 , 33, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 33, zch_map.get("v13")==null?"":zch_map.get("v13").toString() ,coty));
				}
				if("".equals(zch_map.get("v14"))||zch_map.get("v14")==null){
					sheet_bfh.addCell(new Label( 5 , 33, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 33, zch_map.get("v14")==null?"":zch_map.get("v14").toString() ,coty));
				}
				
				sheet_bfh.setRowView(33, 500);
				sheet_bfh.mergeCells( 2 ,34, 3 , 34 );
				sheet_bfh.addCell(new Label( 2 , 34, "销售费用和通信费用" ,tsty));
				if("".equals(zch_map.get("v15"))||zch_map.get("v15")==null){
					sheet_bfh.addCell(new Label( 4 , 34,"" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 34, zch_map.get("v15")==null?"":zch_map.get("v15").toString() ,coty));
				}
				if("".equals(zch_map.get("v16"))||zch_map.get("v16")==null){
					sheet_bfh.addCell(new Label( 5 , 34,"" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 34,zch_map.get("v16")==null?"":zch_map.get("v16").toString() ,coty));
				}
				
				sheet_bfh.setRowView(34, 500);
				sheet_bfh.mergeCells( 2 ,35, 3 , 35 );
				sheet_bfh.addCell(new Label( 2 , 35, "借贷利息" ,tsty));
				if("".equals(zch_map.get("v17"))||zch_map.get("v17")==null){
					sheet_bfh.addCell(new Label( 4 , 35, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 35, zch_map.get("v17")==null?"":zch_map.get("v17").toString() ,coty));
				}
				if("".equals(zch_map.get("v18"))||zch_map.get("v18")==null){
					sheet_bfh.addCell(new Label( 5 , 35, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 35, zch_map.get("v18")==null?"":zch_map.get("v18").toString() ,coty));
				}
				sheet_bfh.setRowView(35, 500);
				
				sheet_bfh.mergeCells( 0,36, 1 , 37 );
				sheet_bfh.addCell(new Label( 0 , 36, "政策性支出" ,tsty));
				sheet_bfh.mergeCells( 2 ,36, 3 , 36 );
				if("".equals(zch_map.get("v23"))||zch_map.get("v23")==null){
					sheet_bfh.addCell(new Label( 2 , 36, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 36, zch_map.get("v23")==null?"":zch_map.get("v23").toString() ,coty));
				}
				if("".equals(zch_map.get("v19"))||zch_map.get("v19")==null){
					sheet_bfh.addCell(new Label( 4 , 36, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 36, zch_map.get("v19")==null?"":zch_map.get("v19").toString() ,coty));
				}
				if("".equals(zch_map.get("v20"))||zch_map.get("v20")==null){
					sheet_bfh.addCell(new Label( 5 , 36, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 36, zch_map.get("v20")==null?"":zch_map.get("v20").toString() ,coty));
				}
				
				
				
				sheet_bfh.setRowView(36, 500);
				sheet_bfh.mergeCells( 2 ,37, 3 , 37 );
				if("".equals(zch_map.get("v24"))||zch_map.get("v24")==null){
					sheet_bfh.addCell(new Label( 2 , 37, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 37, zch_map.get("v24")==null?"":zch_map.get("v24").toString() ,coty));
				}
				if("".equals(zch_map.get("v21"))||zch_map.get("v21")==null){
					sheet_bfh.addCell(new Label( 4 , 37, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 37, zch_map.get("v21")==null?"":zch_map.get("v21").toString() ,coty));
				}
				if("".equals(zch_map.get("v22"))||zch_map.get("v22")==null){
					sheet_bfh.addCell(new Label( 5 , 37, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 37, zch_map.get("v22")==null?"":zch_map.get("v22").toString() ,coty));
				}
				sheet_bfh.setRowView(37, 500);
				
				sheet_bfh.mergeCells( 0,38, 1 , 39 );
				sheet_bfh.addCell(new Label( 0 , 38, "其他支出" ,tsty));
				sheet_bfh.mergeCells( 2 ,38, 3 , 38 );
				if("".equals(zch_map.get("v25"))||zch_map.get("v25")==null){
					sheet_bfh.addCell(new Label( 2 , 38, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 38, zch_map.get("v25")==null?"":zch_map.get("v25").toString() ,coty));
				}
				if("".equals(zch_map.get("v26"))||zch_map.get("v26")==null){
					sheet_bfh.addCell(new Label( 4 , 38, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 38, zch_map.get("v26")==null?"":zch_map.get("v26").toString() ,coty));
				}
				if("".equals(zch_map.get("v27"))||zch_map.get("v27")==null){
					sheet_bfh.addCell(new Label( 5 , 38, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 38, zch_map.get("v27")==null?"":zch_map.get("v27").toString() ,coty));
				}
				sheet_bfh.setRowView(38, 500);
				sheet_bfh.mergeCells( 2 ,39, 3 , 39 );
				if("".equals(zch_map.get("v28"))||zch_map.get("v28")==null){
					sheet_bfh.addCell(new Label( 2 , 39, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 2 , 39, zch_map.get("v28")==null?"":zch_map.get("v28").toString() ,coty));
				}
				if("".equals(zch_map.get("v29"))||zch_map.get("v29")==null){
					sheet_bfh.addCell(new Label( 4 , 39, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 4 , 39, zch_map.get("29")==null?"":zch_map.get("v29").toString() ,coty));
				}
				if("".equals(zch_map.get("v30"))||zch_map.get("v30")==null){
					sheet_bfh.addCell(new Label( 5 , 39, "",coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 39, zch_map.get("v30")==null?"":zch_map.get("v30").toString() ,coty));
				}
				sheet_bfh.setRowView(39, 500);
				sheet_bfh.mergeCells( 0 ,40, 3 , 40 );
				sheet_bfh.addCell(new Label( 0 , 40 , "总支出合计" ,tsty));
				sheet_bfh.addCell(new Label( 4 , 40, "" ,tsty));
				if("".equals(zch_map.get("v31"))||zch_map.get("v31")==null){
					sheet_bfh.addCell(new Label( 5 , 40, "" ,coty));
				}else{
					sheet_bfh.addCell(new Label( 5 , 40, zch_map.get("v31")==null?"":zch_map.get("v31").toString() ,coty));
				}
				
				sheet_bfh.setRowView(40, 500);
				
				//帮扶成效
				WritableSheet sheet_cx = book.createSheet("帮扶成效", 7);
				sheet_cx.setColumnView(0, 15);// 设置列宽
				sheet_cx.setColumnView(1, 60);
				sheet_cx.setColumnView(2, 10);
				sheet_cx.mergeCells(0,0, 2, 0);
				sheet_cx.addCell(new Label(0, 0, "帮扶成效",tsty));
				sheet_cx.setRowView(0, 500);
				sheet_cx.addCell(new Label(0, 1, "时间",tsty));
				sheet_cx.addCell(new Label(1, 1, "成效内容",tsty));
				sheet_cx.addCell(new Label(2, 1, "贫困户签字",tsty));
				sheet_cx.setRowView(1, 500);
				
				String cx_sql="select * from da_help_results"+year+" where da_household_id="+pkid ;
				SQLAdapter cx_sqlAdapter=new SQLAdapter(cx_sql);
				List<Map> cx_list=this.getBySqlMapper.findRecords(cx_sqlAdapter);
				int cx_count=1;
				int cx_num=10-cx_list.size();
				if(cx_list.size()>0){
					for(int i=0;i<cx_list.size();i++){
						Map cx_map=cx_list.get(i);
						sheet_cx.addCell(new Label( 0 ,	cx_count+1 , cx_map.get("v1")==null?"":cx_map.get("v1").toString(),coty));
						sheet_cx.addCell(new Label( 1 ,	cx_count+1 , cx_map.get("v2")==null?"":cx_map.get("v2").toString(),coty));
						sheet_cx.addCell(new Label( 2 ,	cx_count+1 , cx_map.get("v3")==null?"":cx_map.get("v3").toString(),coty));
						sheet_cx.setRowView(cx_count+1, 500);
						cx_count++;
						
					}
				}
				if(cx_num>0){
					for(int i=0;i<cx_num;i++){
						sheet_cx.addCell(new Label( 0 ,	cx_count+1 , "",coty));
						sheet_cx.addCell(new Label( 1 ,	cx_count+1 , "",coty));
						sheet_cx.addCell(new Label( 2 ,	cx_count+1 , "",coty));
						sheet_cx.setRowView(cx_count+1, 500);
						cx_count++;
						
					}
				}
				
				
				
		        //写入数据并关闭文件
	            book.write();
	            book.close();
	            response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
	            response.getWriter().close();
				
			}catch(Exception e){
				response.getWriter().write("1");
				response.getWriter().close();
			}
		}else{
			response.getWriter().write("0");
			response.getWriter().close();
		}
		return null;
	}

}
