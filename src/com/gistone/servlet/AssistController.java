package com.gistone.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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

public class AssistController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * 帮扶人
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAssistUsder_List(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String cha_bfdw = ""; //帮扶单位
		String cha_bfr = "";//帮扶责任人
		String cha_juese = "";//相关角色
		String cha_dh = "";
		String cha_qixian = "";
		String cha_v3 = ""; 
		String str = "";
		String year = request.getParameter("cha_year");//年份
		if ("2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
			cha_bfdw = request.getParameter("cha_bfdw").trim();
			str += " t2.v1 like '%"+cha_bfdw+"%' and";
		}
		if(request.getParameter("cha_bfr")!=null&&!request.getParameter("cha_bfr").equals("")){
			cha_bfr = request.getParameter("cha_bfr").trim();
			str += " t1.col_name like '%"+cha_bfr+"%' and";
		}
		if(request.getParameter("cha_dh")!=null&&!request.getParameter("cha_dh").equals("")){
			cha_dh = request.getParameter("cha_dh").trim();
			str += " t1.telephone like '%"+cha_dh+"%' and";
		}
		if(request.getParameter("cha_qixian")!=null&&!request.getParameter("cha_qixian").equals("请选择")){
			cha_qixian = request.getParameter("cha_qixian").trim();
			str += " t2.sys_company_id ='"+cha_qixian+"' and";
		}
		if(request.getParameter("cha_v3")!=null&&!request.getParameter("cha_v3").equals("请选择")){
			cha_v3 = request.getParameter("cha_v3").trim();
			str += " t1.v3 ='"+cha_v3+"' and";
		}
		
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;
		
		String count_st_sql = "select count(*) from sys_personal"+year+" t1 left join da_company"+year+" t2 on t1.da_company_id=t2.pkid ";
		String people_sql = "select t1.pkid,t1.col_name,t1.v1,t1.v3,t2.v1 as v11,t1.col_post,t1.telephone,x.bf_num from sys_personal"+year+" t1 left join da_company"+year+" t2 on t1.da_company_id=t2.pkid "
				+ "LEFT JOIN (select sys_personal_id,count(*) as bf_num from sys_personal_household_many"+year+" group by sys_personal_id) x on t1.pkid=x.sys_personal_id ";
		
		if(request.getParameter("cha_juese")!=null&&!request.getParameter("cha_juese").equals("请选择")){
			cha_juese = request.getParameter("cha_juese").trim();
			str += " x.bf_num "+cha_juese+" and";
			count_st_sql += " LEFT JOIN (select sys_personal_id,count(*) as bf_num from sys_personal_household_many"+year+" group by sys_personal_id) x on t1.pkid=x.sys_personal_id ";
		}
		//两个条件为空，按照全部查询
		if(str.equals("")){
			people_sql += " limit "+number+","+size;
		}else{
			//带条件，按照条件查询
			count_st_sql += " where "+str.substring(0, str.length()-3);
			people_sql += " where "+str.substring(0, str.length()-3)+" limit "+number+","+size;
		}
		
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		
//		System.out.println(count_st_sql);
//		System.out.println(people_sql);
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		if(Patient_st_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Patient_st_map.keySet()) {
					val.put(key, Patient_st_map.get(key));
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
	 * 条件查询调出报表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportTerm(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String cha_bfdw = ""; //帮扶单位
		String cha_bfr = "";//帮扶责任人
		String cha_juese = "";//相关角色
		String cha_dh = "";
		String cha_qixian = "";
		String cha_v3 = "";
		String str = "";
		
		if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")){
			cha_bfdw = request.getParameter("cha_bfdw");
			str += " t2.v1 like '%"+cha_bfdw+"%' and";
		}
		if(request.getParameter("cha_bfr")!=null&&!request.getParameter("cha_bfr").equals("")){
			cha_bfr = request.getParameter("cha_bfr");
			str += " t1.col_name like '%"+cha_bfr+"%' and";
		}
		if(request.getParameter("cha_dh")!=null&&!request.getParameter("cha_dh").equals("")){
			cha_dh = request.getParameter("cha_dh");
			str += " t1.telephone like '%"+cha_dh+"%' and";
		}
		if(request.getParameter("cha_qixian")!=null&&!request.getParameter("cha_qixian").equals("请选择")){
			cha_qixian = request.getParameter("cha_qixian");
			str += " t2.sys_company_id ='"+cha_qixian+"' and";
		}
		if(request.getParameter("cha_v3")!=null&&!request.getParameter("cha_v3").equals("请选择")){
			cha_v3 = request.getParameter("cha_v3");
			str += " t1.v3 ='"+cha_v3+"' and";
		}

		String people_sql = "select t1.col_name as y1,t1.col_post as y2,t1.telephone as y3,t1.v1 as y4,t1.v2 as y5,t1.v3 as y6,t1.v4 as y7,t1.v5 as y8,t1.v6 as y9,"+
							" t2.v1 as y10,t2.v2 as y11,t2.v3 as y12,t2.v4 as y13,t2.v5 as y14,t5.com_name y15,t1.pkid from sys_personal t1 join da_company t2 on t1.da_company_id=t2.pkid JOIN sys_company t5 ON t5.pkid=t2.sys_company_id ";
		if(request.getParameter("cha_juese")!=null&&!request.getParameter("cha_juese").equals("请选择")){
			cha_juese = request.getParameter("cha_juese");
			str += " x.bf_num "+cha_juese+" and";
		}
		if(!str.equals("")){
			people_sql += " where "+str.substring(0, str.length()-3) ;
		}
		String bfr_sql="select * from (select * from("+ people_sql +") t3 join ("+
						" select sys_personal_id,GROUP_CONCAT(v6 ORDER BY v6 SEPARATOR ',') v6 from("+
						"select w2.sys_personal_id,w1.v6 from da_household w1 join sys_personal_household_many w2 on w1.pkid=w2.da_household_id"+
						") q1 group by sys_personal_id"+
						") t4 on t3.pkid=t4.sys_personal_id) t6 order by y10";
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(bfr_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".xls";
        //获取文件需要上传到的路径
		String savePath = request.getServletContext().getRealPath("/")+ "attached\\exportExcel\\"; 
		 // 文件保存目录URL  
        String saveUrl = request.getContextPath() + "/attached/exportExcel/"; 
        
		WritableWorkbook book = Workbook.createWorkbook( new File(savePath+newFileName));//打开文件
		//标题样式
		WritableFont title_style =new WritableFont(WritableFont.createFont("微软雅黑"), 10 ,WritableFont.BOLD);
		WritableCellFormat tsty = new WritableCellFormat(title_style);
		tsty.setAlignment(Alignment.CENTRE);  //平行居中
		tsty.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
		
		//正文样式
		WritableFont content_style =new WritableFont(WritableFont.createFont("微软雅黑"), 12 ,WritableFont.NO_BOLD);
		WritableCellFormat coty = new WritableCellFormat(content_style);
		coty.setAlignment(Alignment.CENTRE);  //平行居中 
		coty.setVerticalAlignment(VerticalAlignment.CENTRE); 
		WritableSheet sheet_1 = book.createSheet( "帮扶人干部信息 " , 0);//生成第一页工作表，参数0表示这是第一页
		
		int[] headerArrHight_1 = {13,20,25,20,20,30,20,30,20,30,40,30,20,30,20,30,30};
        String headerArr_1[] = {"序号","帮扶人","职务","电话","性别","证件号码","政治面貌","开始帮扶时间","帮扶结束时间","隶属关系","单位名称","单位地址","分管领导姓名","分管领导电话","帮扶对象（嘎查村）","单位所属行政区划","贫困户"};

        for (int i = 0; i < headerArr_1.length; i++) {
        	sheet_1.addCell(new Label( i , 0 , headerArr_1[i], tsty));
        	sheet_1.setColumnView(i, headerArrHight_1[i]);
        }
        int conut=1;
        sheet_1.setRowView(0, 500);
        for (int i = 0; i < Patient_st_List.size(); i++) {   //循环一个list里面的数据到excel中
        	Map s1_map = Patient_st_List.get(i);
			sheet_1.addCell(new Label( 0 , conut ,s1_map.get("pkid")==null?"":s1_map.get("pkid").toString() ,coty));
			sheet_1.addCell(new Label( 1 , conut ,s1_map.get("y1")==null?"":s1_map.get("y1").toString() ,coty));
        	sheet_1.addCell(new Label( 2 , conut ,s1_map.get("y2")==null?"":s1_map.get("y2").toString() ,coty));
        	sheet_1.addCell(new Label( 3 , conut ,s1_map.get("y3")==null?"":s1_map.get("y3").toString() ,coty));
        	sheet_1.addCell(new Label( 4 , conut ,s1_map.get("y4")==null?"":s1_map.get("y4").toString() ,coty));
        	sheet_1.addCell(new Label( 5 , conut ,s1_map.get("y5")==null?"":s1_map.get("y5").toString() ,coty));
        	sheet_1.addCell(new Label( 6 , conut ,s1_map.get("y6")==null?"":s1_map.get("y6").toString() ,coty));
        	sheet_1.addCell(new Label( 7 , conut ,s1_map.get("y7")==null?"":s1_map.get("y7").toString() ,coty));
        	sheet_1.addCell(new Label( 8 , conut ,s1_map.get("y8")==null?"":s1_map.get("y8").toString() ,coty));
        	sheet_1.addCell(new Label( 9 , conut ,s1_map.get("y9")==null?"":s1_map.get("y9").toString() ,coty));
        	sheet_1.addCell(new Label( 10 , conut ,s1_map.get("y10")==null?"":s1_map.get("y10").toString() ,coty));
        	sheet_1.addCell(new Label( 11 , conut ,s1_map.get("y11")==null?"":s1_map.get("y11").toString() ,coty));
        	sheet_1.addCell(new Label( 12 , conut ,s1_map.get("y12")==null?"":s1_map.get("y12").toString() ,coty));
        	sheet_1.addCell(new Label( 13 , conut ,s1_map.get("y13")==null?"":s1_map.get("y13").toString() ,coty));
        	sheet_1.addCell(new Label( 14 , conut ,s1_map.get("y14")==null?"":s1_map.get("y14").toString() ,coty));
        	sheet_1.addCell(new Label( 15 , conut ,s1_map.get("y15")==null?"":s1_map.get("y15").toString() ,coty));
        	sheet_1.addCell(new Label( 16 , conut ,s1_map.get("v6")==null?"":s1_map.get("v6").toString() ,coty));
        	sheet_1.setRowView(conut, 370); // 设置第一行的高度
    		conut++;
		 }
		 book.write();
		 book.close();
		 response.getWriter().write("{\"path\": \""+saveUrl+newFileName+"\"}");
		 response.getWriter().close();
		 return null;
	}
	
	/**
	 * 获取帮扶单位
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView get_bangfudanwei_h5(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String sys_company_id = request.getParameter("sys_company_id").trim();
		
		String where = "";
		if(sys_company_id!=null&&!sys_company_id.equals("")){
			where = " a1.sys_company_id like'%"+sys_company_id+"%'";
		}
		String st_sql = "SELECT   a1.pkid,   a3.com_name AS quname,   a1.v1,   a1.v2,   a1.v3,   a1.v4,   GROUP_CONCAT(a2.com_name)com_name,   GROUP_CONCAT(a1.v5)v5,   a1.sys_company_id,   a2.com_f_pkid,   (      SELECT         d.com_f_pkid      FROM         sys_company d      WHERE         pkid = a2.com_f_pkid   )AS f_pkid,   (      SELECT         com_name      FROM         sys_company      WHERE         pkid = a2.com_f_pkid   )AS xiang_com_name,   (      SELECT         com_name      FROM         sys_company      WHERE         pkid IN(            SELECT               d.com_f_pkid            FROM               sys_company d            WHERE               pkid = a2.com_f_pkid         )   )AS qixian_com_name FROM   da_company a1 LEFT JOIN sys_company a2 ON a1.v5 = a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id = a3.pkid "
				+ " GROUP BY   v1,  v2,   v3,   v4,   quname,   xiang_com_name,   qixian_com_name having"+where;
		//System.out.println(st_sql);
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(st_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		
		JSONArray jsa=new JSONArray();
		if(Patient_st_List.size()>0){
			
			for(int i = 0;i<Patient_st_List.size();i++){
				
				Map Patient_st_map = Patient_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Patient_st_map.keySet()) {
					String com_sql = "";
					String com_name="";
					val.put("pkid", Patient_st_map.get("pkid"));
					
//					if(Patient_st_map.get("v1").toString().length()>15){
//						val.put("v1", Patient_st_map.get("v1").toString().substring(0, 13)+"<br>"+Patient_st_map.get("v1").toString().substring(13));
//					}else{
//						val.put("v1", Patient_st_map.get("v1"));
//					}
					val.put("v1", Patient_st_map.get("v1"));
					val.put("v3", Patient_st_map.get("v3")==null?"":Patient_st_map.get("v3"));
					val.put("v4", Patient_st_map.get("v4")==null?"":Patient_st_map.get("v4"));
					//选择单位时  多个嘎查村的显示
					if(Patient_st_map.get("v5")!=null&&Patient_st_map.get("v5").toString().split(",").length>1){
						
						val.put("com_name", Patient_st_map.get("com_name")==null?"":Patient_st_map.get("com_name"));
					}
					
				}
				jsa.add(val);
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("message", "");
		json.put("value", jsa);
		json.put("code", Patient_st_List.size());
		json.put("redirect", "");
		
		response.getWriter().write(json.toString());
		return null;
	}
	
	
	/**
	 * 添加帮扶人信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getAddAssistController(HttpServletRequest request,HttpServletResponse response) throws IOException{

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String dwid = request.getParameter("dwid");
		String form_val = request.getParameter("form_val");
		JSONObject form_json = JSONObject.fromObject(form_val);//表单数据
		
		String col_name = "", v1 = "", v2 = "", v3 = "", v6 = "", telephone = "", v4 = "", v5 = "", col_post = "";
		String where = "";
		
		if(form_json.get("col_name")!=null&&!form_json.get("col_name").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			col_name = form_json.get("col_name").toString();
			where += "col_name='"+form_json.get("col_name")+"',";
		}else{
			where += "col_name='',";
		}
		
		if(form_json.get("v1")!=null&&!form_json.get("v1").equals("请选择")){//下拉框，一定有值，但是要筛除“请选择”
			v1 = form_json.get("v1").toString();
			where += "v1='"+form_json.get("v1")+"',";
		}else{
			if(form_json.get("v1").equals("请选择")){
				where += "v1='',";
			}
		}
		
		if(form_json.get("v2")!=null&&!form_json.get("v2").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			v2 = form_json.get("v2").toString();
			where += "v2='"+form_json.get("v2")+"',";
		}else{
			where += "v2='',";
		}
		
		if(form_json.get("v3")!=null&&!form_json.get("v3").equals("请选择")){//下拉框，一定有值，但是要筛除“请选择”
			v3 = form_json.get("v3").toString();
			where += "v3='"+form_json.get("v3")+"',";
		}else{
			if(form_json.get("v3").equals("请选择")){
				where += "v3='',";
			}
		}
		
		if(form_json.get("v6")!=null&&!form_json.get("v6").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			v6 = form_json.get("v6").toString();
			where += "v6='"+form_json.get("v6")+"',";
		}else{
			where += "v6='',";
		}
		
		if(form_json.get("telephone")!=null&&!form_json.get("telephone").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			telephone = form_json.get("telephone").toString();
			where += "telephone='"+form_json.get("telephone")+"',";
		}else{
			where += "telephone='',";
		}
		
		if(form_json.get("v4")!=null&&!form_json.get("v4").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			v4 = form_json.get("v4").toString();
			where += "v4='"+form_json.get("v4")+"',";
		}else{
			where += "v4='',";
		}
		
		if(form_json.get("v5")!=null&&!form_json.get("v5").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			v5 = form_json.get("v5").toString();
			where += "v5='"+form_json.get("v5")+"',";
		}else{
			where += "v5='',";
		}
		
		if(form_json.get("col_post")!=null&&!form_json.get("col_post").equals("")){//可以为空，如果没有取到值，证明前台为空，数据库需要清空此列
			col_post = form_json.get("col_post").toString();
			where += "col_post='"+form_json.get("col_post")+"',";
		}else{
			where += "col_post='',";
		}
		
		
		String sql = "";
		if(form_json.get("pkid")!=null&&!form_json.get("pkid").equals("")){
			where += "da_company_id="+dwid;
//			if(where.length()>0){
//				where = where.substring(0, where.length()-1);
//			}
			sql = "update sys_personal set "+where+" where pkid="+form_json.get("pkid");
		}else{
			sql = "INSERT INTO sys_personal(col_name,v1,v2,v3,v6,telephone,v4,v5,da_company_id,col_post) VALUES"+
					"('"+col_name+"','"+v1+"','"+v2+"','"+v3+"','"+v6+"','"+telephone+"','"+v4+"','"+v5+"',"+dwid+",'"+col_post+"')";
		}
//		System.out.println(sql);
		SQLAdapter Metadata_table_Adapter = new SQLAdapter(sql);
		try{
			this.getBySqlMapper.insertSelective(Metadata_table_Adapter);
			
			if(form_json.get("pkid")!=null&&!form_json.get("pkid").equals("")){
				HttpSession session = request.getSession();
				JSONObject json = new JSONObject();
				if(session.getAttribute("Login_map")!=null){//验证session不为空
					Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
					SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('sys_personal',"+form_json.get("pkid")+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶干部','')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}
			}else{
				String hou_sql = "select max(pkid) from sys_personal where col_name='"+col_name+"' and da_company_id="+dwid+" order by pkid desc";
				SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
				int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
				
				HttpSession session = request.getSession();
				JSONObject json = new JSONObject();
				if(session.getAttribute("Login_map")!=null){//验证session不为空
					Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
					SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
					String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
							" VALUES ('sys_personal',"+da_household_id+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶干部','')";
					SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
					this.getBySqlMapper.findRecords(hqlAdapter1);
				}
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}
	/**
	 * 修改帮扶人先获取他的值
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getUpdateSave(HttpServletRequest request,HttpServletResponse response) throws IOException{

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid");
		
		String sql_1 = "select t1.pkid,t1.col_name,t1.v1,t1.v2,t1.v3,t1.v4,t1.v5,t1.v6,t2.v1 as v11,t2.pkid as dwid,t2.sys_company_id as qixian,"
				+ "t1.col_post,t1.telephone from sys_personal t1 join da_company t2 on t1.da_company_id=t2.pkid where t1.pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql_1);
		List<Map> list = getBySqlMapper.findRecords(sqlAdapter);
		JSONObject zong = new JSONObject ();
		
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			
			obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			obj.put("col_name", val.get("col_name")==null?"-":val.get("col_name"));
			obj.put("v1",val.get("v1")==null?"":val.get("v1"));
			obj.put("v2",val.get("v2")==null?"":val.get("v2"));
			obj.put("v3",val.get("v3")==null?"":val.get("v3"));
			obj.put("v4",val.get("v4")==null?"":val.get("v4"));
			obj.put("v5",val.get("v5")==null?"":val.get("v5"));
			obj.put("v6",val.get("v6")==null?"":val.get("v6"));
			obj.put("v11",val.get("v11")==null?"":val.get("v11"));
			obj.put("col_post",val.get("col_post")==null?"":val.get("col_post"));
			obj.put("telephone", val.get("telephone")==null?"":val.get("telephone"));
			obj.put("dwid", val.get("dwid")==null?"":val.get("dwid"));
			obj.put("qixian", val.get("qixian")==null?"":val.get("qixian"));
			zong.put("bangfu", obj);
		}
		
		response.getWriter().write(zong.toString());
		response.getWriter().close();
		return null;
	
	}
	/**
	 * 删除帮扶人
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getDeleteBfr(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid=request.getParameter("pkid");
		String sql="DELETE FROM sys_personal where pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		try{
			this.getBySqlMapper.insertSelective(sqlAdapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('sys_personal',"+pkid+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶干部','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	
	}
}
