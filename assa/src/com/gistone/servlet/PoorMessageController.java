package com.gistone.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;
import com.gistone.util.Tool;

public class PoorMessageController extends MultiActionController{
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
		String cha_v8_1 = "";//年龄范围开始时间
		String cha_v8_2 = "";//年龄范围截止时间
		float cha_v24_1 = 0f;//年龄范围开始时间
		float cha_v24_2 = 0f;//年龄范围截止时间
		String cha_year = request.getParameter("cha_year");//查找的年份
		
		String hz_sql="";
		String str = "";
		
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " a.v6 like '%"+cha_v6+"%' and";
		}
		//判断身份证是否是户主身份证是则继续不是则先查出来户主身份证
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			//根据传过来的身份证判断查询成员  若不是户主则可以查询出来相关户主Id 若家庭只有户主一人则直接走else
			hz_sql = "select da_member.da_household_id m_id from da_member  where  v8 like '%"+cha_v8+"%'";
			List<Map> map = this.getBySqlMapper.findRecords(new SQLAdapter(hz_sql));
			if(map.size()>0){
				String 	pkid = map.get(0).get("m_id").toString();
				//通过户主Id查询户主身份证号
				hz_sql = "select v8 from da_household where pkid = '"+pkid+"'";
				List<Map> v_list = this.getBySqlMapper.findRecords(new SQLAdapter(hz_sql));
				if(map.size()>0){
					//通过户主身份证查询相关信息和原来步骤一样
					cha_v8 = v_list.get(0).get("v8").toString();
					str += " a.v8 like '%"+cha_v8+"%' and";
				}
			}else{
				cha_v8 = request.getParameter("cha_v8").trim();
				str += " a.v8 like '%"+cha_v8+"%' and";
			}
		}
		/*if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " a.v8 like '%"+cha_v8+"%' and";
		}*/
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(a.v8)>=18 and (TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(a.v8)>=18 and TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(a.v8)>=18 and TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
			}
		}
		if(request.getParameter("cha_v24_1")!=null&&!request.getParameter("cha_v24_1").equals("")&&Tool.isNumeric(request.getParameter("cha_v24_1").trim())){
			cha_v24_1 = Float.parseFloat(request.getParameter("cha_v24_1").trim());
//			if(request.getParameter("cha_v24_2")!=null&&!request.getParameter("cha_v24_2").equals("")&&Tool.isNumeric(request.getParameter("cha_v24_2").trim())){
//				cha_v24_2 = request.getParameter("cha_v24_2").trim();
//				str += " ROUND(a.v24,2)>="+cha_v24_1+" and ROUND(a.v24,2)<"+cha_v24_2+" and";
//			}else{
//				str += "  ROUND(a.v24,2)>="+cha_v24_1+" and";
//			}
		}
		if(request.getParameter("cha_v24_2")!=null&&!request.getParameter("cha_v24_2").equals("")&&Tool.isNumeric(request.getParameter("cha_v24_2").trim())){
				cha_v24_2 = Float.parseFloat(request.getParameter("cha_v24_2").trim());
				//str += " ROUND(a.v24,2)<"+cha_v24_2+" and";
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
		String  str_table = "";
		if( "2016".equals(cha_year) ) {
			str_table = "_2016";
		} else {
			str_table = "";
		}
		
		String total_number_sql="select a.v9,a.pkid from da_household"+str_table+" a ";//统计总人口数
		String count_st_sql = "select count(*) from (select a.pkid from da_household"+str_table+" a ";
		String people_sql = "select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v21,a.v22,a.v23,a.v11,a.sys_standard from da_household"+str_table+" a ";
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
			count_st_sql += " LEFT JOIN sys_personal_household_many"+str_table+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+str_table+" c on x.sys_personal_id = c.pkid join da_company"+str_table+" t2 on c.da_company_id=t2.pkid ";
			people_sql += " LEFT JOIN sys_personal_household_many"+str_table+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+str_table+" c on x.sys_personal_id = c.pkid join da_company"+str_table+" t2 on c.da_company_id=t2.pkid ";
			total_number_sql += " LEFT JOIN sys_personal_household_many"+str_table+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+str_table+" c on x.sys_personal_id = c.pkid join da_company"+str_table+" t2 on c.da_company_id=t2.pkid ";
		}
		
		
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;
		
		//两个条件为空，按照全部查询select * from sys_village_responsibility a  LEFT JOIN sys_company b  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_personal_id

		if(str.equals("")){
			total_number_sql += " GROUP BY a.pkid order by a.pkid";
			count_st_sql += " GROUP BY a.pkid) ww";
			people_sql += " GROUP BY a.pkid order by a.pkid limit "+number+","+size;
		}else{
			//带条件，按照条件查询
			total_number_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid order by a.pkid";
			count_st_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid) ww";
			people_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid order by a.pkid limit "+number+","+size;
		}
		
		int total=0;
		List<Map> Patient_st_List=new ArrayList<Map>();
		SQLAdapter Patient_total_Adapter = new SQLAdapter(total_number_sql);
		List<Map> Patient_total_List = this.getBySqlMapper.findRecords(Patient_total_Adapter);
		
		int v9=0;//家庭人口
		if(Patient_total_List.size()>0){
			List<String> da_household_ids2=new ArrayList<String>();
			JSONArray jsa=new JSONArray();
			//判断是否有人均收入的查询条件
			if(cha_v24_1!=0||cha_v24_2!=0){
			
			StringBuilder pkids=new StringBuilder();//贫苦户id
			List<Integer> familysize_list=new ArrayList<Integer>();//一户的家庭人数
			for(int i = 0;i<Patient_total_List.size();i++){
				Map Patient_total_map = Patient_total_List.get(i);
				if(i==Patient_total_List.size()-1){
					pkids.append(Patient_total_map.get("pkid"));
				}else{
					pkids.append(Patient_total_map.get("pkid")+",");
				}
				familysize_list.add((Integer) Patient_total_map.get("v9"));
			}
			//帮扶后总收入
			String dqsrh_sql2="SELECT da_household_id,v39 FROM da_helpback_income"+str_table+" where da_household_id in("+pkids+")";
			SQLAdapter dqsrh_sqlAdapter2 =new SQLAdapter(dqsrh_sql2);
			List<Map> dqsrh_list2=getBySqlMapper.findRecords(dqsrh_sqlAdapter2);
			//帮扶后总支出
			String dqzch_sql2="SELECT da_household_id,v31 FROM da_helpback_expenditure"+str_table+" where da_household_id in("+pkids+")";
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
				if(cha_v24_1!=0&&cha_v24_2==0){
					if(per_capita_income2>=cha_v24_1){
						da_household_ids2.add(dqsrh_map2.get("da_household_id").toString());
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(cha_v24_1!=0&&cha_v24_2!=0){
					if(per_capita_income2>=cha_v24_1&&per_capita_income2<cha_v24_2){
						da_household_ids2.add(dqsrh_map2.get("da_household_id").toString());
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}else if(cha_v24_1==0&&cha_v24_2!=0){
					if(per_capita_income2<cha_v24_2){
						da_household_ids2.add(dqsrh_map2.get("da_household_id").toString());
						pkids2.append(dqsrh_map2.get("da_household_id").toString()+",");
					}
				}
			}
			String count_st_sql2="select count(*) from (select a.pkid from da_household"+str_table+" a where a.pkid in ("+pkids2.substring(0, pkids2.lastIndexOf(","))+ ") GROUP BY a.pkid) ww";
			SQLAdapter count_st_Adapter2 = new SQLAdapter(count_st_sql2);
			total= this.getBySqlMapper.findrows(count_st_Adapter2);
			String people_sql2="select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v21,a.v22,a.v23,a.v11,a.sys_standard from da_household"+str_table+" a where a.pkid in ("+pkids2.substring(0, pkids2.lastIndexOf(","))+ ") GROUP BY a.pkid limit "+number+","+size;
			SQLAdapter Patient_st_Adapter2 = new SQLAdapter(people_sql2);
			Patient_st_List= this.getBySqlMapper.findRecords(Patient_st_Adapter2);
			}else{
				SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
				Patient_st_List= this.getBySqlMapper.findRecords(Patient_st_Adapter);
				
				SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
				total= this.getBySqlMapper.findrows(count_st_Adapter);
				
				for(int i = 0;i<Patient_total_List.size();i++){
					Map Patient_total_map = Patient_total_List.get(i);
					String pkid=Patient_total_map.get("pkid").toString().trim();
					da_household_ids2.add(pkid);
				}
			}
			for(int i = 0;i<Patient_st_List.size();i++){
				Map Patient_st_map = Patient_st_List.get(i);
				String pkid=Patient_st_map.get("pkid").toString().trim();
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
			for(int i = 0;i<Patient_total_List.size();i++){
				Map Patient_total_map = Patient_total_List.get(i);
				String pkid2=Patient_total_map.get("pkid").toString().trim();
				if(da_household_ids2.contains(pkid2)){
				if(Patient_total_map.keySet().contains("v9")){
					v9+=(Integer) Patient_total_map.get("v9");
				}
				}
			}
			JSONObject json = new JSONObject();
			json.put("total_number",v9);
			json.put("page", page);
			json.put("total", total);
			json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
			response.getWriter().write(json.toString());
		}
		return null;
	}
	/**
	 * 查看贫困户的详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getPoorDetailController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		String  year = request.getParameter("year");//年份
		if ("2016".equals(year)) {
			year = "_2016";
		} else {
			 year = "";
		}
		String sql="select * from da_household"+year+" a left join da_household_basic"+year+" b on a.pkid=b.da_household_id "+
					"LEFT JOIN (SELECT pic_path,pic_pkid from da_pic"+year+" WHERE pic_type=4 ) c ON a.pkid=c.pic_pkid  where a.pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		//户主信息
		JSONArray jsonArray1 =new JSONArray();
		
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("v6", val.get("v6")==null?"":val.get("v6"));//姓名
			obj.put("v7",val.get("v7")==null?"":val.get("v7"));//性别
			obj.put("v8",val.get("v8")==null?"":val.get("v8"));//证件号码
			obj.put("v9",val.get("v9")==null?"":val.get("v9"));//人数
			obj.put("v11",val.get("v11")==null?"":val.get("v11"));//民族
			obj.put("v12",val.get("v12")==null?"":val.get("v12"));//文化程度
			obj.put("v13",val.get("v13")==null?"":val.get("v13"));//在校生状况
			obj.put("v14",val.get("v14")==null?"":val.get("v14"));//健康状况
			obj.put("v15",val.get("v15")==null?"":val.get("v15"));//劳动能力
			obj.put("v16",val.get("v16")==null?"":val.get("v16"));//务工情况
			obj.put("v17",val.get("v17")==null?"":val.get("v17"));//务工时间
			obj.put("v18",val.get("v18")==null?"":val.get("v18"));//是否参加新农合
			obj.put("v19",val.get("v19")==null?"":val.get("v19"));//是否参加新型养老保险
			obj.put("v21",val.get("v21")==null?"":val.get("v21"));//脱贫属性
			obj.put("v25",val.get("v25")==null?"":val.get("v25"));//联系电话
			obj.put("v26",val.get("v26")==null?"":val.get("v26"));//开户银行名称
			obj.put("v27",val.get("v27")==null?"":val.get("v27"));//银行卡号
			obj.put("sys_standard",val.get("sys_standard")==null?"":val.get("sys_standard"));//识别标准
			obj.put("v22",val.get("v22")==null?"":val.get("v22"));//贫苦户属性
			obj.put("v23",val.get("v23")==null?"":val.get("v23"));//主要致贫原因
			obj.put("v29",val.get("v29")==null?"":val.get("v29"));//是否军烈属
			obj.put("v30",val.get("v30")==null?"":val.get("v30"));//是否独生子女
			obj.put("v31",val.get("v31")==null?"":val.get("v31"));//是否双女户
			obj.put("v32",val.get("v32")==null?"":val.get("v32"));//是否现役军人
			obj.put("v33",val.get("v33")==null?"":val.get("v33"));//其他致贫原因
			obj.put("v28",val.get("v28")==null?"":val.get("v28"));//其他致贫原因
			obj.put("basic_address",val.get("basic_address")==null?"":val.get("basic_address"));//家庭住址
			obj.put("basic_explain",val.get("basic_explain")==null?"":val.get("basic_explain"));//致贫原因说明
			obj.put("pic_path",val.get("pic_path")==null?"":val.get("pic_path"));//致贫原因说明
			jsonArray1.add(obj);
		}
		//家庭成员
		JSONArray jsonArray2 =new JSONArray();
		String xian_sql="select * from da_member"+year+" a LEFT JOIN (SELECT pic_path,pic_pkid from da_pic"+year+" WHERE pic_type=5 ) b ON a.pkid=b.pic_pkid  where a.da_household_id="+pkid;
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		for(Map val:xian_list){
			JSONObject obj=new JSONObject ();
			obj.put("cy_pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("cy_v6",val.get("v6")==null?"":val.get("v6"));//姓名
			obj.put("cy_v7",val.get("v7")==null?"":val.get("v7"));//性别
			obj.put("cy_v8",val.get("v8")==null?"":val.get("v8"));//证件号码
			obj.put("cy_v9",val.get("v9")==null?"":val.get("v9"));//人数
			obj.put("cy_v10",val.get("v10")==null?"":val.get("v10"));//与户主关系
			obj.put("cy_v11",val.get("v11")==null?"":val.get("v11"));//民族
			obj.put("cy_v12",val.get("v12")==null?"":val.get("v12"));//文化程度
			obj.put("cy_v13",val.get("v13")==null?"":val.get("v13"));//在校状况
			obj.put("cy_v14",val.get("v14")==null?"":val.get("v14"));//健康状况
			obj.put("cy_v15",val.get("v15")==null?"":val.get("v15"));//劳动力
			obj.put("cy_v16",val.get("v16")==null?"":val.get("v16"));//务工状态
			obj.put("cy_v17",val.get("v17")==null?"":val.get("v17"));//务工时间
			obj.put("cy_v18",val.get("v18")==null?"":val.get("v18"));//是否参加新农合
			obj.put("cy_v19",val.get("v19")==null?"":val.get("v19"));//是否参加新型养老保险
			obj.put("cy_v20",val.get("v20")==null?"":val.get("v20"));//是否参加城镇职工基本养老保险	
			obj.put("cy_v21",val.get("v21")==null?"":val.get("v21"));//脱贫属性
			
			obj.put("cy_v16",val.get("v16")==null?"":val.get("v16"));//务工情况
			obj.put("cy_v17",val.get("v17")==null?"":val.get("v17"));//务工时间
			obj.put("cy_v32",val.get("v32")==null?"":val.get("v32"));//是否现役军人
			obj.put("cy_v28",val.get("v28")==null?"":val.get("v28"));//政治面貌
			obj.put("cy_pic_path",val.get("pic_path")==null?"":val.get("pic_path"));//头像
			jsonArray2.add(obj);
		}
		
		//生产条件
		JSONArray jsonArray3 =new JSONArray();
		String sc_sql="select * FROM da_production"+year+" where da_household_id="+pkid;
		SQLAdapter sc_sqlAdapter =new SQLAdapter(sc_sql);
		List<Map> sc_list=getBySqlMapper.findRecords(sc_sqlAdapter);
		for(Map val:sc_list){
			JSONObject obj=new JSONObject ();
			obj.put("sc_pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("sc_v1",val.get("v1")==null?"":val.get("v1"));//耕地面积
			obj.put("sc_v2",val.get("v2")==null?"":val.get("v2"));//水浇地面积
			obj.put("sc_v3",val.get("v3")==null?"":val.get("v3"));//林地面积
			obj.put("sc_v4",val.get("v4")==null?"":val.get("v4"));//退耕还林面积
			obj.put("sc_v5",val.get("v5")==null?"":val.get("v5"));//草牧场面积
			obj.put("sc_v6",val.get("v6")==null?"":val.get("v6"));//生产用房面积
			obj.put("sc_v7",val.get("v7")==null?"":val.get("v7"));//其他
			obj.put("sc_v8",val.get("v8")==null?"":val.get("v8"));//家禽
			obj.put("sc_v9",val.get("v9")==null?"":val.get("v9"));//牛
			obj.put("sc_v10",val.get("v10")==null?"":val.get("v10"));//羊
			obj.put("sc_v11",val.get("v11")==null?"":val.get("v11"));//猪
			obj.put("sc_v12",val.get("v12")==null?"":val.get("v12"));//其他
			obj.put("sc_v13",val.get("v13")==null?"":val.get("v13"));//林果面积
			obj.put("sc_v14",val.get("v14")==null?"":val.get("v14"));//水面面积
			jsonArray3.add(obj);
			
		}
		
		//生活条件
		JSONArray jsonArray4 =new JSONArray();
		String sh_sql="SELECT * FROM da_life"+year+" where da_household_id="+pkid;
		SQLAdapter sh_sqlAdapter =new SQLAdapter(sh_sql);
		List<Map> sh_list=getBySqlMapper.findRecords(sh_sqlAdapter);
		for(Map val:sh_list){
			JSONObject sh_obj=new JSONObject ();
			sh_obj.put("sh_pkid",val.get("pkid")==null?"":val.get("pkid"));
			sh_obj.put("sh_v1",val.get("v1")==null?"":val.get("v1"));//住房面积
			sh_obj.put("sh_v2",val.get("v2")==null?"":val.get("v2"));//是否危房
			sh_obj.put("sh_v3",val.get("v3")==null?"":val.get("v3"));//是否纳入易地扶贫搬迁
			sh_obj.put("sh_v4",val.get("v4")==null?"":val.get("v4"));//饮水情况
			sh_obj.put("sh_v5",val.get("v5")==null?"":val.get("v5"));//通电情况
			sh_obj.put("sh_v6",val.get("v6")==null?"":val.get("v6"));//入户路类型
			sh_obj.put("sh_v9",val.get("v9")==null?"":val.get("v9"));//饮水是否安全
			sh_obj.put("sh_v10",val.get("v10")==null?"":val.get("v10"));//主要燃料类型
			sh_obj.put("sh_v11",val.get("v11")==null?"":val.get("v11"));//是否加入农民专业合作社
			sh_obj.put("sh_v12",val.get("v12")==null?"":val.get("v12"));//有无卫生厕所
			sh_obj.put("sh_v8",val.get("v8")==null?"":val.get("v8"));//饮水是否困难
			sh_obj.put("sh_v7",val.get("v7")==null?"":val.get("v7"));//与村主干路距离（公里）
			jsonArray4.add(sh_obj);
		}
		
		//当前收支分析
		JSONArray jsonArray5 =new JSONArray();
		String dqsr_sql="SELECT * FROM da_current_income"+year+" where da_household_id="+pkid;
		SQLAdapter dqsr_sqlAdapter =new SQLAdapter(dqsr_sql);
		List<Map> dqsr_list=getBySqlMapper.findRecords(dqsr_sqlAdapter);
		for(Map val:dqsr_list){
			JSONObject dqsr_obj=new JSONObject ();
			dqsr_obj.put("dqsr_pkid",val.get("pkid")==null?"":val.get("pkid"));
			dqsr_obj.put("dqsr_v1",val.get("v1")==null?"":val.get("v1"));//农业明细
			dqsr_obj.put("dqsr_v2",val.get("v2")==null?"":val.get("v2"));//农业金额
			dqsr_obj.put("dqsr_v3",val.get("v3")==null?"":val.get("v3"));//畜牧业明细
			dqsr_obj.put("dqsr_v4",val.get("v4")==null?"":val.get("v4"));//畜牧业金额
			dqsr_obj.put("dqsr_v5",val.get("v5")==null?"":val.get("v5"));//林业明细
			dqsr_obj.put("dqsr_v6",val.get("v6")==null?"":val.get("v6"));//林业金额
			dqsr_obj.put("dqsr_v7",val.get("v7")==null?"":val.get("v7"));//其他明细
			dqsr_obj.put("dqsr_v8",val.get("v8")==null?"":val.get("v8"));//其他金额
			dqsr_obj.put("dqsr_v9",val.get("v9")==null?"":val.get("v9"));//小计明细
			dqsr_obj.put("dqsr_v10",val.get("v10")==null?"":val.get("v10"));//小计金额
			dqsr_obj.put("dqsr_v11",val.get("v11")==null?"":val.get("v11"));//生态补贴明细
			dqsr_obj.put("dqsr_v12",val.get("v12")==null?"":val.get("v12"));//生态补贴金额
			dqsr_obj.put("dqsr_v13",val.get("v13")==null?"":val.get("v13"));//养老金明细
			dqsr_obj.put("dqsr_v14",val.get("v14")==null?"":val.get("v14"));//养老金金额	
			dqsr_obj.put("dqsr_v15",val.get("v15")==null?"":val.get("v15"));//低保补贴明细
			dqsr_obj.put("dqsr_v16",val.get("v16")==null?"":val.get("v16"));//低保补贴金额
			dqsr_obj.put("dqsr_v17",val.get("v17")==null?"":val.get("v17"));//燃煤补贴明细
			dqsr_obj.put("dqsr_v18",val.get("v18")==null?"":val.get("v18"));//燃煤补贴金额
			dqsr_obj.put("dqsr_v19",val.get("v19")==null?"":val.get("v19"));//其他明细
			dqsr_obj.put("dqsr_v20",val.get("v20")==null?"":val.get("v20"));//其他金额
			dqsr_obj.put("dqsr_v21",val.get("v21")==null?"":val.get("v21"));//小计明细
			dqsr_obj.put("dqsr_v22",val.get("v22")==null?"":val.get("v22"));//小计金额
			dqsr_obj.put("dqsr_v23",val.get("v23")==null?"":val.get("v23"));//土地流转明细
			dqsr_obj.put("dqsr_v24",val.get("v24")==null?"":val.get("v24"));//土地流转金额
			dqsr_obj.put("dqsr_v25",val.get("v25")==null?"":val.get("v25"));//其他明细
			dqsr_obj.put("dqsr_v26",val.get("v26")==null?"":val.get("v26"));//其他金额
			
			dqsr_obj.put("dqsr_v27",val.get("v27")==null?"":val.get("v27"));//工资明细1
			dqsr_obj.put("dqsr_v28",val.get("v28")==null?"":val.get("v28"));//工资金额1
			dqsr_obj.put("dqsr_v29",val.get("v29")==null?"":val.get("v29"));//工资明细2
			dqsr_obj.put("dqsr_v30",val.get("v30")==null?"":val.get("v30"));//工资金额2
			dqsr_obj.put("dqsr_v31",val.get("v31")==null?"":val.get("v31"));//其他明细1
			dqsr_obj.put("dqsr_v32",val.get("v32")==null?"":val.get("v32"));//其他金额1
			dqsr_obj.put("dqsr_v33",val.get("v33")==null?"":val.get("v33"));//其他明细2
			dqsr_obj.put("dqsr_v34",val.get("v34")==null?"":val.get("v34"));//其他金额2
			dqsr_obj.put("dqsr_v35",val.get("v35")==null?"":val.get("v35"));//工资项目1
			dqsr_obj.put("dqsr_v36",val.get("v36")==null?"":val.get("v36"));//工资项目2
			dqsr_obj.put("dqsr_v37",val.get("v37")==null?"":val.get("v37"));//其他项目1
			dqsr_obj.put("dqsr_v38",val.get("v38")==null?"":val.get("v38"));//其他项目2
			dqsr_obj.put("dqsr_v39",val.get("v39")==null?"":val.get("v39"));//年总收入
			
			dqsr_obj.put("dqsr_v40",val.get("v40")==null?"":val.get("v40"));//计划生育金明细
			dqsr_obj.put("dqsr_v41",val.get("v41")==null?"":val.get("v41"));//计划生育金额
			dqsr_obj.put("dqsr_v42",val.get("v42")==null?"":val.get("v42"));//五保金明细
			dqsr_obj.put("dqsr_v43",val.get("v43")==null?"":val.get("v43"));//五保金金额

			jsonArray5.add(dqsr_obj);
		}
		
		//当前支出
		JSONArray jsonArray6 =new JSONArray();
		String dqzc_sql="SELECT * FROM da_current_expenditure"+year+" where da_household_id="+pkid;
		SQLAdapter dqzc_sqlAdapter =new SQLAdapter(dqzc_sql);
		List<Map> dqzc_list=getBySqlMapper.findRecords(dqzc_sqlAdapter);
		for(Map val:dqzc_list){
			JSONObject dqzc_obj=new JSONObject ();
			dqzc_obj.put("dqzc_pkid",val.get("pkid")==null?"":val.get("pkid"));
			dqzc_obj.put("dqzc_v1",val.get("v1")==null?"":val.get("v1"));//农资明细
			dqzc_obj.put("dqzc_v2",val.get("v2")==null?"":val.get("v2"));//农资金额
			dqzc_obj.put("dqzc_v3",val.get("v3")==null?"":val.get("v3"));//固定资产折旧明细
			dqzc_obj.put("dqzc_v4",val.get("v4")==null?"":val.get("v4"));//固定资产折旧金额
			dqzc_obj.put("dqzc_v5",val.get("v5")==null?"":val.get("v5"));//水电明细
			dqzc_obj.put("dqzc_v6",val.get("v6")==null?"":val.get("v6"));//水电金额
			dqzc_obj.put("dqzc_v7",val.get("v7")==null?"":val.get("v7"));//承包土地明细
			dqzc_obj.put("dqzc_v8",val.get("v8")==null?"":val.get("v8"));//承包土地金额
			dqzc_obj.put("dqzc_v9",val.get("v9")==null?"":val.get("v9"));//饲草料明细
			dqzc_obj.put("dqzc_v10",val.get("v10")==null?"":val.get("v10"));//	饲草料金额
			dqzc_obj.put("dqzc_v11",val.get("v11")==null?"":val.get("v11"));//	防疫明细
			dqzc_obj.put("dqzc_v12",val.get("v12")==null?"":val.get("v12"));//防疫金额
			dqzc_obj.put("dqzc_v13",val.get("v13")==null?"":val.get("v13"));//种畜明细
			dqzc_obj.put("dqzc_v14",val.get("v14")==null?"":val.get("v14"));//种畜金额
			dqzc_obj.put("dqzc_v15",val.get("v15")==null?"":val.get("v15"));//销售通讯明细
			dqzc_obj.put("dqzc_v16",val.get("v16")==null?"":val.get("v16"));//销售通讯金额
			dqzc_obj.put("dqzc_v17",val.get("v17")==null?"":val.get("v17"));//	借贷明细
			dqzc_obj.put("dqzc_v18",val.get("v18")==null?"":val.get("v18"));//	借贷金额
			dqzc_obj.put("dqzc_v19",val.get("v19")==null?"":val.get("v19"));//政策明细1
			dqzc_obj.put("dqzc_v20",val.get("v20")==null?"":val.get("v20"));//政策金额1
			dqzc_obj.put("dqzc_v21",val.get("v21")==null?"":val.get("v21"));//政策明细2
			dqzc_obj.put("dqzc_v22",val.get("v22")==null?"":val.get("v22"));//	政策金额2
			dqzc_obj.put("dqzc_v23",val.get("v23")==null?"":val.get("v23"));//	政策项目1
			dqzc_obj.put("dqzc_v24",val.get("v24")==null?"":val.get("v24"));//政策项目2
			dqzc_obj.put("dqzc_v25",val.get("v25")==null?"":val.get("v25"));//其他项目1
			dqzc_obj.put("dqzc_v26",val.get("v26")==null?"":val.get("v26"));//其他明细1
			dqzc_obj.put("dqzc_v27",val.get("v27")==null?"":val.get("v27"));//其他金额1
			dqzc_obj.put("dqzc_v28",val.get("v28")==null?"":val.get("v28"));//	其他项目2
			dqzc_obj.put("dqzc_v29",val.get("v29")==null?"":val.get("v29"));//其他明细2
			dqzc_obj.put("dqzc_v30",val.get("v30")==null?"":val.get("v30"));//其他金额2
			dqzc_obj.put("dqzc_v31",val.get("v31")==null?"":val.get("v31"));//年总支出
			jsonArray6.add(dqzc_obj);
		}
		
		//帮扶人情况
		JSONArray jsonArray7 =new JSONArray();
		String bfr_sql="SELECT da_household_id,telephone ,col_post, col_name,t2.v1 FROM sys_personal_household_many"+year+" a "
				+ "LEFT JOIN sys_personal"+year+" b ON a.sys_personal_id = b.pkid LEFT join da_company"+year+" t2 on b.da_company_id=t2.pkid where da_household_id="+pkid;
//		System.out.println(bfr_sql);
		SQLAdapter bfr_sqlAdapter =new SQLAdapter(bfr_sql);
		List<Map> bfr_list=getBySqlMapper.findRecords(bfr_sqlAdapter);
		for(Map val:bfr_list){
			JSONObject bfr_obj=new JSONObject ();
			bfr_obj.put("bfr_pkid",val.get("pkid")==null?"":val.get("pkid"));
			bfr_obj.put("bfr_col_name",val.get("col_name")==null?"":val.get("col_name"));//帮扶人姓名
			bfr_obj.put("bfr_com_name",val.get("v1")==null?"":val.get("v1"));//帮扶人单位
			bfr_obj.put("bfr_col_post",val.get("col_post")==null?"":val.get("col_post"));//帮扶人职务
			bfr_obj.put("bfr_telephone",val.get("telephone")==null?"":val.get("telephone"));//帮扶人电话
			jsonArray7.add(bfr_obj);
		}
		
		JSONArray jsonArray15 =new JSONArray();
		String bfjihua_sql="SELECT v1 mubiao,v2 shixiao,v3 jihua, da_household_id jhid from da_help_info"+year+" where da_household_id="+pkid;
		SQLAdapter bfjihua_sqlAdapter =new SQLAdapter(bfjihua_sql);
		List<Map> bfjihua_list=getBySqlMapper.findRecords(bfjihua_sqlAdapter);
		for(Map val:bfjihua_list){
			JSONObject bfr_obj=new JSONObject ();
			bfr_obj.put("bfr_mubiao",val.get("mubiao")==null?"":val.get("mubiao"));//帮扶目标
			bfr_obj.put("bfr_shixiao",val.get("shixiao")==null?"":val.get("shixiao"));//帮扶时效
			bfr_obj.put("bfr_jihua",val.get("jihua")==null?"":val.get("jihua"));//帮扶计划
			jsonArray15.add(bfr_obj);
		}
		
		
		//帮扶人走访情况
		JSONArray jsonArray8 =new JSONArray();
		String zf_sql="SELECT v1,v2,v3, group_concat(pic_path order by pic_path separator ',') path FROM (" +
				"SELECT *  FROM da_help_visit"+year+" a LEFT JOIN (SELECT pic_path,pic_pkid from da_pic"+year+" WHERE pic_type=2 ) b ON a.pkid=b.pic_pkid "+
				" WHERE a.da_household_id="+pkid+" )aa GROUP BY pkid ORDER BY v1 DESC";
		SQLAdapter zf_sqlAdapter =new SQLAdapter(zf_sql);
		List<Map> zf_list=getBySqlMapper.findRecords(zf_sqlAdapter);
		for(Map val:zf_list){
			JSONObject zf_obj=new JSONObject ();
			zf_obj.put("zf_v1",val.get("v1")==null?"":val.get("v1"));//走访时间
			zf_obj.put("zf_v2",val.get("v2")==null?"":val.get("v2"));//帮扶干部
			zf_obj.put("zf_v3",val.get("v3")==null?"":val.get("v3"));//帮扶目标
			zf_obj.put("zf_pic",val.get("path")==null?"":val.get("path"));//帮扶目标
			
			jsonArray8.add(zf_obj);
		}
		//帮扶措施
		JSONArray jsonArray9 =new JSONArray();
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
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		if(Patient_st_List.size()>0){
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
				jsonArray9.add(val);
			}
			
		}
		
		//帮扶成效
		JSONArray jsonArray10 =new JSONArray();
		String bfcx_sql="SELECT v1,v2,v3, group_concat(pic_path order by pic_path separator ',') path FROM ("+
						"SELECT *  FROM da_help_results"+year+" a LEFT JOIN (SELECT pic_path,pic_pkid from da_pic"+year+" WHERE pic_type=3 ) b ON a.pkid=b.pic_pkid "+
						"WHERE a.da_household_id="+pkid+" )aa GROUP BY pkid ORDER BY v1 DESC";
		SQLAdapter bfcx_sqlAdapter =new SQLAdapter(bfcx_sql);
		List<Map> bfcx_list=getBySqlMapper.findRecords(bfcx_sqlAdapter);
		for(Map val:bfcx_list){
			JSONObject bfcx_obj=new JSONObject ();
			bfcx_obj.put("bfcx_v1",val.get("v1")==null?"":val.get("v1"));//时间
			bfcx_obj.put("bfcx_v2",val.get("v2")==null?"":val.get("v2"));//成效内容
			bfcx_obj.put("bfcx_v3",val.get("v3")==null?"":val.get("v3"));//贫困户签字
			bfcx_obj.put("bfcx_pic",val.get("path")==null?"":val.get("path"));//贫困户签字
			jsonArray10.add(bfcx_obj);
		}
		//帮扶后收入
		JSONArray jsonArray11 =new JSONArray();
		String dqsrh_sql="SELECT * FROM da_helpback_income"+year+" where da_household_id="+pkid;
		SQLAdapter dqsrh_sqlAdapter =new SQLAdapter(dqsrh_sql);
		List<Map> dqsrh_list=getBySqlMapper.findRecords(dqsrh_sqlAdapter);
		for(Map val:dqsrh_list){
			JSONObject dqsrh_obj=new JSONObject ();
			dqsrh_obj.put("dqsrh_pkid",val.get("pkid")==null?"":val.get("pkid"));
			dqsrh_obj.put("dqsrh_v1",val.get("v1")==null?"":val.get("v1"));//农业明细
			dqsrh_obj.put("dqsrh_v2",val.get("v2")==null?"":val.get("v2"));//农业金额
			dqsrh_obj.put("dqsrh_v3",val.get("v3")==null?"":val.get("v3"));//畜牧业明细
			dqsrh_obj.put("dqsrh_v4",val.get("v4")==null?"":val.get("v4"));//畜牧业金额
			dqsrh_obj.put("dqsrh_v5",val.get("v5")==null?"":val.get("v5"));//林业明细
			dqsrh_obj.put("dqsrh_v6",val.get("v6")==null?"":val.get("v6"));//林业金额
			dqsrh_obj.put("dqsrh_v7",val.get("v7")==null?"":val.get("v7"));//其他明细
			dqsrh_obj.put("dqsrh_v8",val.get("v8")==null?"":val.get("v8"));//其他金额
			dqsrh_obj.put("dqsrh_v9",val.get("v9")==null?"":val.get("v9"));//小计明细
			dqsrh_obj.put("dqsrh_v10",val.get("v10")==null?"":val.get("v10"));//小计金额
			dqsrh_obj.put("dqsrh_v11",val.get("v11")==null?"":val.get("v11"));//生态补贴明细
			dqsrh_obj.put("dqsrh_v12",val.get("v12")==null?"":val.get("v12"));//生态补贴金额
			dqsrh_obj.put("dqsrh_v13",val.get("v13")==null?"":val.get("v13"));//养老金明细
			dqsrh_obj.put("dqsrh_v14",val.get("v14")==null?"":val.get("v14"));//养老金金额	
			dqsrh_obj.put("dqsrh_v15",val.get("v15")==null?"":val.get("v15"));//低保补贴明细
			dqsrh_obj.put("dqsrh_v16",val.get("v16")==null?"":val.get("v16"));//低保补贴金额
			dqsrh_obj.put("dqsrh_v17",val.get("v17")==null?"":val.get("v17"));//燃煤补贴明细
			dqsrh_obj.put("dqsrh_v18",val.get("v18")==null?"":val.get("v18"));//燃煤补贴金额
			dqsrh_obj.put("dqsrh_v19",val.get("v19")==null?"":val.get("v19"));//其他明细
			dqsrh_obj.put("dqsrh_v20",val.get("v20")==null?"":val.get("v20"));//其他金额
			dqsrh_obj.put("dqsrh_v21",val.get("v21")==null?"":val.get("v21"));//小计明细
			dqsrh_obj.put("dqsrh_v22",val.get("v22")==null?"":val.get("v22"));//小计金额
			dqsrh_obj.put("dqsrh_v23",val.get("v23")==null?"":val.get("v23"));//土地流转明细
			dqsrh_obj.put("dqsrh_v24",val.get("v24")==null?"":val.get("v24"));//土地流转金额
			dqsrh_obj.put("dqsrh_v25",val.get("v25")==null?"":val.get("v25"));//其他明细
			dqsrh_obj.put("dqsrh_v26",val.get("v26")==null?"":val.get("v26"));//其他金额
			dqsrh_obj.put("dqsrh_v27",val.get("v27")==null?"":val.get("v27"));//工资明细1
			dqsrh_obj.put("dqsrh_v28",val.get("v28")==null?"":val.get("v28"));//工资金额1
			dqsrh_obj.put("dqsrh_v29",val.get("v29")==null?"":val.get("v29"));//工资明细2
			dqsrh_obj.put("dqsrh_v30",val.get("v30")==null?"":val.get("v30"));//工资金额2
			dqsrh_obj.put("dqsrh_v31",val.get("v31")==null?"":val.get("v31"));//其他明细1
			dqsrh_obj.put("dqsrh_v32",val.get("v32")==null?"":val.get("v32"));//其他金额1
			dqsrh_obj.put("dqsrh_v33",val.get("v33")==null?"":val.get("v33"));//其他明细2
			dqsrh_obj.put("dqsrh_v34",val.get("v34")==null?"":val.get("v34"));//其他金额2
			dqsrh_obj.put("dqsrh_v35",val.get("v35")==null?"":val.get("v35"));//工资项目1
			dqsrh_obj.put("dqsrh_v36",val.get("v36")==null?"":val.get("v36"));//工资项目2
			dqsrh_obj.put("dqsrh_v37",val.get("v37")==null?"":val.get("v37"));//其他项目1
			dqsrh_obj.put("dqsrh_v38",val.get("v38")==null?"":val.get("v38"));//其他项目2
			dqsrh_obj.put("dqsrh_v39",val.get("v39")==null?"":val.get("v39"));//年总收入
			jsonArray11.add(dqsrh_obj);
		}
		
		//帮扶后支出
		JSONArray jsonArray12 =new JSONArray();
		String dqzch_sql="SELECT * FROM da_helpback_expenditure"+year+" where da_household_id="+pkid;
		SQLAdapter dqzch_sqlAdapter =new SQLAdapter(dqzch_sql);
		List<Map> dqzch_list=getBySqlMapper.findRecords(dqzch_sqlAdapter);
		for(Map val:dqzch_list){
			JSONObject dqzch_obj=new JSONObject ();
			dqzch_obj.put("dqzch_pkid",val.get("pkid")==null?"":val.get("pkid"));
			dqzch_obj.put("dqzch_v1",val.get("v1")==null?"":val.get("v1"));//农资明细
			dqzch_obj.put("dqzch_v2",val.get("v2")==null?"":val.get("v2"));//农资金额
			dqzch_obj.put("dqzch_v3",val.get("v3")==null?"":val.get("v3"));//固定资产折旧明细
			dqzch_obj.put("dqzch_v4",val.get("v4")==null?"":val.get("v4"));//固定资产折旧金额
			dqzch_obj.put("dqzch_v5",val.get("v5")==null?"":val.get("v5"));//水电明细
			dqzch_obj.put("dqzch_v6",val.get("v6")==null?"":val.get("v6"));//水电金额
			dqzch_obj.put("dqzch_v7",val.get("v7")==null?"":val.get("v7"));//承包土地明细
			dqzch_obj.put("dqzch_v8",val.get("v8")==null?"":val.get("v8"));//承包土地金额
			dqzch_obj.put("dqzch_v9",val.get("v9")==null?"":val.get("v9"));//饲草料明细
			dqzch_obj.put("dqzch_v10",val.get("v10")==null?"":val.get("v10"));//	饲草料金额
			dqzch_obj.put("dqzch_v11",val.get("v11")==null?"":val.get("v11"));//	防疫明细
			dqzch_obj.put("dqzch_v12",val.get("v12")==null?"":val.get("v12"));//防疫金额
			dqzch_obj.put("dqzch_v13",val.get("v13")==null?"":val.get("v13"));//种畜明细
			dqzch_obj.put("dqzch_v14",val.get("v14")==null?"":val.get("v14"));//种畜金额
			dqzch_obj.put("dqzch_v15",val.get("v15")==null?"":val.get("v15"));//销售通讯明细
			dqzch_obj.put("dqzch_v16",val.get("v16")==null?"":val.get("v16"));//销售通讯金额
			dqzch_obj.put("dqzch_v17",val.get("v17")==null?"":val.get("v17"));//	借贷明细
			dqzch_obj.put("dqzch_v18",val.get("v18")==null?"":val.get("v18"));//	借贷金额
			dqzch_obj.put("dqzch_v19",val.get("v19")==null?"":val.get("v19"));//政策明细1
			dqzch_obj.put("dqzch_v20",val.get("v20")==null?"":val.get("v20"));//政策金额1
			dqzch_obj.put("dqzch_v21",val.get("v21")==null?"":val.get("v21"));//政策明细2
			dqzch_obj.put("dqzch_v22",val.get("v22")==null?"":val.get("v22"));//	政策金额2
			dqzch_obj.put("dqzch_v23",val.get("v23")==null?"":val.get("v23"));//	政策项目1
			dqzch_obj.put("dqzch_v24",val.get("v24")==null?"":val.get("v24"));//政策项目2
			dqzch_obj.put("dqzch_v25",val.get("v25")==null?"":val.get("v25"));//其他项目1
			dqzch_obj.put("dqzch_v26",val.get("v26")==null?"":val.get("v26"));//其他明细1
			dqzch_obj.put("dqzch_v27",val.get("v27")==null?"":val.get("v27"));//其他金额1
			dqzch_obj.put("dqzch_v28",val.get("v28")==null?"":val.get("v28"));//	其他项目2
			dqzch_obj.put("dqzch_v29",val.get("v29")==null?"":val.get("v29"));//其他明细2
			dqzch_obj.put("dqzch_v30",val.get("v30")==null?"":val.get("v30"));//其他金额2
			dqzch_obj.put("dqzch_v31",val.get("v31")==null?"":val.get("v31"));//年总支出
			jsonArray12.add(dqzch_obj);
		}
		
		//System.out.println(jsonArray13);
		//异地搬迁
		JSONArray jsonArray14 =new JSONArray();
		String yd_sql="SELECT move_type,focus_info,dispersed_info,dispersed_address,dispersed_price,dispersed_agreement,v1,v2,v3, sf,path FROM  da_household_move"+year+" a LEFT JOIN "+
						"(SELECT v3 sf, da_household_id FROM da_life"+year+") b ON a.da_household_id=b.da_household_id LEFT JOIN "+
						"(SELECT pic_pkid, group_concat(pic_path order by pic_path separator ',') path FROM da_pic"+year+" where pic_type='6' AND pic_pkid="+pkid+") c ON"+
						"  a.da_household_id=c.pic_pkid where a.da_household_id="+pkid+" GROUP BY a.da_household_id";
//		System.out.println(yd_sql);
		SQLAdapter yd_sqlAdapter =new SQLAdapter(yd_sql);
		List<Map> yd_list=getBySqlMapper.findRecords(yd_sqlAdapter);
		JSONObject yd_obj=new JSONObject ();
		if(yd_list.size()>0){
			for(Map val:yd_list){
				yd_obj.put("move_type",val.get("move_type")==null?"":val.get("move_type"));//集中安置或者分散安置
				yd_obj.put("focus_info",val.get("focus_info")==null?"":val.get("focus_info"));//集中安置分类	
				yd_obj.put("dispersed_info",val.get("dispersed_info")==null?"":val.get("dispersed_info"));//分散安置分类
				yd_obj.put("dispersed_address",val.get("dispersed_address")==null?"":val.get("dispersed_address"));//房源地
				
				yd_obj.put("dispersed_price",val.get("dispersed_price")==null?"":val.get("dispersed_price"));//房价（万元）
				yd_obj.put("dispersed_agreement",val.get("dispersed_agreement")==null?"":val.get("dispersed_agreement"));//与用工企业签订就业安置协议
				yd_obj.put("yd_v1",val.get("v1")==null?"":val.get("v1"));//	搬迁方式（单选）
				yd_obj.put("yd_v2",val.get("v2")==null?"":val.get("v2"));//安置地（单选）
				
				yd_obj.put("yd_v3",val.get("v3")==null?"":val.get("v3"));//搬迁可能存在的困难（可多选）
				yd_obj.put("sf",val.get("sf")==null?"":val.get("sf"));//是否
				if("".equals(val.get("path"))||val.get("path")==null){
					yd_obj.put("path","");//安置地（单选）
				}else{
					yd_obj.put("path",val.get("path")==null?"":val.get("path"));//安置地（单选）
				}
				jsonArray14.add(yd_obj);
			}
		}
		
		
		response.getWriter().write("{\"data1\":"+jsonArray1.toString() +
				",\"data2\":"+jsonArray2.toString()+",\"data3\":"+jsonArray3.toString()+
				",\"data4\":"+jsonArray4.toString()+",\"data5\":"+jsonArray5.toString()+"," +
				"\"data6\":"+jsonArray6.toString()+",\"data7\":"+jsonArray7.toString()+",\"data8\":"+jsonArray8.toString()+"," +
				"\"data9\":"+jsonArray9.toString()+",\"data10\":"+jsonArray10.toString()+",\"data11\":"+jsonArray11.toString()+"," +
				"\"data12\":"+jsonArray12.toString()+",\"data14\":"+jsonArray14.toString()+",\"data15\":"+jsonArray15.toString()+"}");
		response.getWriter().close();
		return null;
	
	}
	
	/**
	 * 走访、措施、成效，时间轴更新读取
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getZaiXiangGengXinController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid = request.getParameter("pkid");
		String str = request.getParameter("str");
		String year = request.getParameter("year");//年份
		if ( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "" ;
		}
		//System.out.println(str);
		
		JSONArray jsonArray13 =new JSONArray();
//		String sjz_sql="select * from(select pkid,v1,v2,v3,'da_help_results' from da_help_results where da_household_id="+pkid+""+
//				" union all select pkid,v1,v2,v3,'da_help_measures' from da_help_measures where da_household_id="+pkid+" "+
//				"union all select pkid,v1,v2,v3,'da_help_visit' from da_help_visit where da_household_id="+pkid+" ) x order by v1 desc";
//		
		String sql = "select * from(";
		
		if(str.indexOf("走访情况")>1){
			sql += "select pkid,v1,v2,v3,'da_help_visit' as da_type from da_help_visit"+year+" where da_household_id="+pkid+" union all ";
		}
		
		if(str.indexOf("帮扶措施")>1){
			sql += "select pkid,v1,v2,v3,'da_help_measures' as da_type from da_help_measures"+year+" where da_household_id="+pkid+" union all ";
		}
		
		if(str.indexOf("帮扶成效")>1){
			sql += "select pkid,v1,v2,v3,'da_help_results' as da_type from da_help_results"+year+" where da_household_id="+pkid+" union all ";
		}
		
		sql = sql.substring(0, sql.length()-10);
		
		sql += ") x order by v1 desc";
		
		
		//System.out.println(sql);
		SQLAdapter sjz_sqlAdapter =new SQLAdapter(sql);
		List<Map> sjz_list=getBySqlMapper.findRecords(sjz_sqlAdapter);
		for(Map val:sjz_list){
			JSONObject sjz_obj=new JSONObject ();
			sjz_obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			sjz_obj.put("v1",val.get("v1")==null?"":val.get("v1"));
			sjz_obj.put("v2",val.get("v2")==null?"":val.get("v2"));
			sjz_obj.put("v3",val.get("v3")==null?"":val.get("v3"));
			sjz_obj.put("da_type",val.get("da_type")==null?"":val.get("da_type"));
			String results=val.get("da_type").toString();
			
			if(results.equals("da_help_visit")){
				String hql="SELECT group_concat(pic_path order by pic_path separator ',') pie FROM da_pic"+year+" WHERE pic_pkid="+val.get("pkid")+" AND pic_type=2";
				SQLAdapter sjzf_sqlAdapter =new SQLAdapter(hql);
				List<Map> sjzf_list=getBySqlMapper.findRecords(sjzf_sqlAdapter);
				if(null==sjzf_list.get(0)){
					sjz_obj.put("pie","");
				}else{
					for(Map val1:sjzf_list){
						sjz_obj.put("pie",val1.get("pie")==null?"":val1.get("pie"));
					}
				}
			
				
			}
			if(results.equals("da_help_measures")){
				String hql="SELECT group_concat(pic_path order by pic_path separator ',') pie FROM da_pic"+year+" WHERE pic_pkid="+val.get("pkid")+" AND pic_type=1";
				SQLAdapter sjzf_sqlAdapter =new SQLAdapter(hql);
				List<Map> sjzf_list=getBySqlMapper.findRecords(sjzf_sqlAdapter);
				if(null==sjzf_list.get(0)){
					sjz_obj.put("pie","");
				}else{
					for(Map val1:sjzf_list){
						sjz_obj.put("pie",val1.get("pie")==null?"":val1.get("pie"));
					}
				}
				
			}
			if(results.equals("da_help_results")){
				String hql="SELECT group_concat(pic_path order by pic_path separator ',') pie FROM da_pic"+year+" WHERE pic_pkid="+val.get("pkid")+" AND pic_type=3";
				SQLAdapter sjzf_sqlAdapter =new SQLAdapter(hql);
				List<Map> sjzf_list=getBySqlMapper.findRecords(sjzf_sqlAdapter);
				if(null==sjzf_list.get(0)){
					sjz_obj.put("pie","");
				}else{
					for(Map val1:sjzf_list){
						sjz_obj.put("pie",val1.get("pie")==null?"":val1.get("pie"));
					}
				}
			}
		
			jsonArray13.add(sjz_obj);
		}
		//System.out.println("{\"data13\":"+jsonArray13.toString()+"}");
		response.getWriter().write("{\"data13\":"+jsonArray13.toString()+"}");
		response.getWriter().close();
		return null;
	}
	
	/**
	 * 查找所有的旗县
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveQixianController(HttpServletRequest request,HttpServletResponse response) throws IOException{


		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//旗县
		String sql="select * from sys_company where com_level=2";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		JSONArray jsonArray =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("com_name", val.get("com_name")==null?"":val.get("com_name"));
			jsonArray.add(obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	}
	/**
	 * 获取所有乡
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getXiangController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String str=request.getParameter("str");
		String sql="SELECT * from sys_company WHERE com_f_pkid=(SELECT pkid FROM sys_company WHERE com_name='"+str+"')";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		JSONArray jsonArray =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("com_name", val.get("com_name")==null?"":val.get("com_name"));
			jsonArray.add(obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	
	}
	/**
	 * 获取所有乡
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getXiangController_id(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String str=request.getParameter("str");
		String sql="SELECT * from sys_company WHERE com_f_pkid='"+str+"'";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		JSONArray jsonArray =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("com_name", val.get("com_name")==null?"":val.get("com_name"));
			jsonArray.add(obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	
	}
	/**
	 * 获取所有村
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getCunController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String sql="select * from sys_company where com_level=4";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		JSONArray jsonArray =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("com_name", val.get("com_name")==null?"":val.get("com_name"));
			jsonArray.add(obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	
	}
	
	public ModelAndView getExporExcel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		
		HSSFWorkbook wb=new HSSFWorkbook();
		
		String[] strs=request.getParameterValues("tabs");
		 // 第一步，创建一个webbook，对应一个Excel文件  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式   
        HSSFRow row = sheet.createRow(0);
        String[] heads=strs[0].split(",");
        int startindex=1;

        for (int k = 0; k < heads.length; k++) {
        	HSSFCell cell = row.createCell(k);
        	cell.setCellValue(heads[k]);
		}

        for (int i = startindex; i < strs.length; i++) {
        	 row = sheet.createRow(i);
        	if(strs!=null){
        		strs[i]=strs[i].replaceAll("\\<.*?\\>", "");
        		String[] cell_arr=strs[i].split(",");

        		for (int j = 0; j < cell_arr.length; j++) {
        			 HSSFCell cell = row.createCell(j);
        			 cell.setCellStyle(style);
        			 cell.setCellValue(cell_arr[j]);
        			 
				}
        	}
        	
		}
       String path=request.getSession().getServletContext().getRealPath("/")+"xlsfile";
      File file = new File(path);
       if(!file.exists()){
    	   
        	file.mkdir();
        }
//       
        // 第六步，将文件存到指定位置  
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateStr=format.format(new Date());
        try  
        {  
            FileOutputStream fout = new FileOutputStream(path+"//"+dateStr+".xls");  
            wb.write(fout);  
            fout.close();  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
        response.getWriter().write("{\"path\": \"xlsfile//"+dateStr+".xls\"}");
		return null;
	
	}
	
	
	/**
	 * 获得贫困户信息 管理测试使用 不在正式服务器使用
	 * @description 
	 * @method  getPoorMessageManage
	 * @param　request
	 * @author luoshuai 
	 * @date 2017年4月12日 下午3:26:52
	 *
	 */
	public ModelAndView getPoorMessageManage(HttpServletRequest request,HttpServletResponse response) throws Exception{
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
		String cha_v8_1 = "";//年龄范围开始时间
		String cha_v8_2 = "";//年龄范围截止时间
		String cha_year = request.getParameter("cha_year");//查找的年份
		
		String hz_sql="";
		String str = "";
		
		if(request.getParameter("cha_v6")!=null&&!request.getParameter("cha_v6").equals("")){
			cha_v6 = request.getParameter("cha_v6").trim();
			str += " a.v6 like '%"+cha_v6+"%' and";
		}
		//判断身份证是否是户主身份证是则继续不是则先查出来户主身份证
		if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			//根据传过来的身份证判断查询成员  若不是户主则可以查询出来相关户主Id 若家庭只有户主一人则直接走else
			hz_sql = "select da_member.da_household_id m_id from da_member  where  v8 like '%"+cha_v8+"%'";
			List<Map> map = this.getBySqlMapper.findRecords(new SQLAdapter(hz_sql));
			if(map.size()>0){
				String 	pkid = map.get(0).get("m_id").toString();
				//通过户主Id查询户主身份证号
				hz_sql = "select v8 from da_household where pkid = '"+pkid+"'";
				List<Map> v_list = this.getBySqlMapper.findRecords(new SQLAdapter(hz_sql));
				if(map.size()>0){
					//通过户主身份证查询相关信息和原来步骤一样
					cha_v8 = v_list.get(0).get("v8").toString();
					str += " a.v8 like '%"+cha_v8+"%' and";
				}
			}else{
				cha_v8 = request.getParameter("cha_v8").trim();
				str += " a.v8 like '%"+cha_v8+"%' and";
			}
		}
		/*if(request.getParameter("cha_v8")!=null&&!request.getParameter("cha_v8").equals("")){
			cha_v8 = request.getParameter("cha_v8").trim();
			str += " a.v8 like '%"+cha_v8+"%' and";
		}*/
		if(request.getParameter("cha_v8_1")!=null&&!request.getParameter("cha_v8_1").equals("")){
			cha_v8_1 = request.getParameter("cha_v8_1").trim();
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(a.v8)>=18 and (TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))<="+cha_v8_2+") and";
			}else{
				str += " LENGTH(a.v8)>=18 and TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))>="+cha_v8_1+" and";
			}
		}else{
			if(request.getParameter("cha_v8_2")!=null&&!request.getParameter("cha_v8_2").equals("")){
				cha_v8_2 = request.getParameter("cha_v8_2").trim();
				str += " LENGTH(a.v8)>=18 and TIMESTAMPDIFF(year,substring(a.v8, 7, 8),DATE(now()))<="+cha_v8_2+" and";
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
		String  str_table = "";
		if( "2016".equals(cha_year) ) {
			str_table = "_2016";
		} else {
			str_table = "";
		}
		
		String total_number_sql="select a.v9 from da_household"+str_table+" a ";//统计总人口数
		String count_st_sql = "select count(*) from (select a.pkid from da_household"+str_table+" a ";
		String people_sql = "select a.pkid,a.v3,a.v4,a.v5,a.v6,a.v9,a.v21,a.v22,a.v23,a.v11,a.sys_standard,a.init_flag from da_household"+str_table+" a ";
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
			count_st_sql += " LEFT JOIN sys_personal_household_many"+str_table+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+str_table+" c on x.sys_personal_id = c.pkid join da_company"+str_table+" t2 on c.da_company_id=t2.pkid ";
			people_sql += " LEFT JOIN sys_personal_household_many"+str_table+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+str_table+" c on x.sys_personal_id = c.pkid join da_company"+str_table+" t2 on c.da_company_id=t2.pkid ";
			total_number_sql += " LEFT JOIN sys_personal_household_many"+str_table+" x on x.da_household_id=a.pkid LEFT JOIN sys_personal"+str_table+" c on x.sys_personal_id = c.pkid join da_company"+str_table+" t2 on c.da_company_id=t2.pkid ";
		}
		
		
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;
		
		//两个条件为空，按照全部查询select * from sys_village_responsibility a  LEFT JOIN sys_company b  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_personal_id

		if(str.equals("")){
			total_number_sql += " GROUP BY a.pkid order by a.pkid";
			count_st_sql += " GROUP BY a.pkid) ww";
			people_sql += " GROUP BY a.pkid order by a.pkid limit "+number+","+size;
		}else{
			//带条件，按照条件查询
			total_number_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid order by a.pkid";
			count_st_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid) ww";
			people_sql += " where "+str.substring(0, str.length()-3)+" GROUP BY a.pkid order by a.pkid limit "+number+","+size;
		}
		
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findrows(count_st_Adapter);
		
		SQLAdapter Patient_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Patient_st_List = this.getBySqlMapper.findRecords(Patient_st_Adapter);
		
		SQLAdapter Patient_total_Adapter = new SQLAdapter(total_number_sql);
		List<Map> Patient_total_List = this.getBySqlMapper.findRecords(Patient_total_Adapter);
		int v9=0;//家庭人口
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
					if(Patient_st_map.get("init_flag").toString().equals("国家级贫困人口")&&Patient_st_map.get("v21").toString().equals("未脱贫")){
						val.put("chexiao","撤回");
					}else if(Patient_st_map.get("sys_standard").toString().equals("市级低收入人口")&&Patient_st_map.get("v21").toString().equals("未脱贫")){
						val.put("chexiao","撤回");
					}else if(Patient_st_map.get("sys_standard")==null || Patient_st_map.get("v21").toString()==null){
						val.put("chexiao","撤回");
					}else{
						val.put("chexiao","<a onclick='chexiao_update(\""+Patient_st_map.get("pkid")+"\")'>撤回</a>");
					}
					
				}
				jsa.add(val);
			}
			for(int i = 0;i<Patient_total_List.size();i++){
				Map Patient_total_map = Patient_total_List.get(i);
				if(Patient_total_map.keySet().contains("v9")){
					v9+=(Integer) Patient_total_map.get("v9");
				}
			}
			JSONObject json = new JSONObject();
			json.put("total_number",v9);
			json.put("page", page);
			json.put("total", total);
			json.put("rows", jsa); //这里的 rows 和total 的key 是固定的 
			response.getWriter().write(json.toString());
		}
		return null;
	}
	
	/**
	 * 撤销修改的贫困户级别  如 市贫户撤销 如果以前是国贫户则撤销之后为国贫户 以前是市贫户则不能更改
	 * @description 
	 * @method  getCunController
	 * @param　request
	 * @author luoshuai 
	 * @date 2017年4月12日 下午3:27:02
	 *
	 */
	public ModelAndView chexiao_update(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid = request.getParameter("pkid");
		String v21="",init_flag="";
		String sql="select d.sys_standard,d.init_flag,d.v21 from da_household d where pkid = "+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		//已脱贫   未脱贫 但是是市贫户 
		if(list.get(0).get("v21")!=null && !list.get(0).get("v21").equals("")){
			if(list.get(0).get("v21").toString().equals("已脱贫")){
				init_flag = "市级低收入人口";
				v21 = "未脱贫";
			}else if(list.get(0).get("v21").toString().equals("未脱贫")){
				init_flag = "国家级贫困人口";
				v21 = "未脱贫";
			}
			String update_sql = "update da_household set init_flag ='"+init_flag+"',v21='"+v21+"'where pkid="+pkid;
			this.getBySqlMapper.updateSelective(new SQLAdapter(update_sql));
			response.getWriter().write("1");
		}else{
			response.getWriter().write("0");
		}
		response.getWriter().close();
		return null;
	
	}
	
}
