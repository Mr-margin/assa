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

public class GachacunController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * 嘎查村默认页
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getGachacunController(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		
		String cha_gcc_fzr = ""; //嘎查村负责人
		String cha_gcc_phone = "";//负责人电话
		String cha_gcc_cun = "";//嘎查村

		String str = "";
		
		if(request.getParameter("cha_gcc_fzr")!=null&&!request.getParameter("cha_gcc_fzr").equals("")){
			cha_gcc_fzr = request.getParameter("cha_gcc_fzr").trim();
			str += " v1 like '%"+cha_gcc_fzr+"%' and";
		}
		if(request.getParameter("cha_gcc_phone")!=null&&!request.getParameter("cha_gcc_phone").equals("")){
			cha_gcc_phone = request.getParameter("cha_gcc_phone").trim();
			str += " v2 like '%"+cha_gcc_phone+"%' and";
		}
		if(request.getParameter("cha_gcc_cun")!=null&&!request.getParameter("cha_gcc_cun").equals("")){
			cha_gcc_cun = request.getParameter("cha_gcc_cun").trim();
			str += " com_name like '%"+cha_gcc_cun+"%' and";
		}
		
		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;
		
		String count_st_sql = "";
		String people_sql = "";
		//两个条件为空，按照全部查询select * from sys_village_responsibility a  LEFT JOIN sys_company b  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_personal_id

		if(str.equals("")){
			count_st_sql = "select count(*) from sys_village_responsibility a  LEFT JOIN sys_company b"+
						"  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_company_id";
			people_sql = "select * from sys_village_responsibility a  LEFT JOIN sys_company b"+
						"  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_company_id "+
						" limit "+number+","+(number+size);
		}else{
			//带条件，按照条件查询
			count_st_sql = "select * from sys_village_responsibility a  LEFT JOIN sys_company b"+
							"  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_company_id "+
							" where "+str.substring(0, str.length()-3);
			people_sql = "select * from sys_village_responsibility a  LEFT JOIN sys_company b"+
						"  ON a.sys_company_id=b.pkid LEFT JOIN sys_user c ON a.pkid=c.sys_company_id"+
						" where "+str.substring(0, str.length()-3)+" limit "+number+","+(number+size);
		}
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
					"('"+zhanghao+"','"+mima+"','1',"+main+",'1')";
		
		SQLAdapter sqlAdapter2 =new SQLAdapter(user);
		JSONArray jsonArray1 =new JSONArray();
		this.getBySqlMapper.findRecords(sqlAdapter2);
		
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
		
	}
	/**
	 * 修改嘎查村前
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getUpdateSaveFzr(HttpServletRequest request,HttpServletResponse response) throws IOException{

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		
		String sql="select * from sys_village_responsibility where pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		String xian_sql="select * from sys_user where sys_company_id="+pkid;
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		JSONArray jsonArray =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			obj.put("v1", val.get("v1")==null?"-":val.get("v1"));
			obj.put("v2",val.get("v2")==null?"-":val.get("v2"));
			obj.put("v5",val.get("v5")==null?"-":val.get("v5"));
			jsonArray.add(obj);
		}
		for(Map val:xian_list){
			JSONObject xian_obj=new JSONObject ();
			xian_obj.put("col_account",val.get("col_account")==null?"-":val.get("col_account"));
			xian_obj.put("col_password",val.get("col_password")==null?"-":val.get("col_password"));
			jsonArray.add(xian_obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	
	}
	/**
	 * 修改嘎查村后
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getUpDateFzrController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid=request.getParameter("pkid");
		String xm=request.getParameter("xm");
		String phone=request.getParameter("phone");
		String cun=request.getParameter("cun");
		String zhanghao=request.getParameter("zhanghao");
		String mima=request.getParameter("mima");
		JSONArray jsonArray =new JSONArray();
		
		String cha_sql="SELECT * FROM sys_company WHERE com_name='"+cun+"'";
		SQLAdapter cha_sqlAdapter =new SQLAdapter(cha_sql);
		this.getBySqlMapper.findRecords(cha_sqlAdapter);
		Integer sys_company_id=(Integer) getBySqlMapper.findRecords(cha_sqlAdapter).get(0).get("pkid");
		
		String sql="UPDATE sys_village_responsibility SET v1 = '"+xm+"',v2='"+phone+"',v5='"+cun+"',sys_company_id="+sys_company_id+""+
					" WHERE pkid = "+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		this.getBySqlMapper.findRecords(sqlAdapter);
		
		String hql1="select * from sys_user where sys_company_id="+pkid;
		SQLAdapter sqlAdapter12 =new SQLAdapter(hql1);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter12);
		String sql1="";
		if(null==list||list.size()==0){
			sql1="insert into sys_user(col_account,col_password,account_type,sys_company_id,account_state) values"+
					"('"+zhanghao+"','"+mima+"','1',"+pkid+",'1')";
		}else{
			sql1="UPDATE sys_user SET col_account = '"+zhanghao+"',col_password='"+mima+"'"+
					" WHERE sys_company_id = "+pkid;
		}
		
		
		SQLAdapter sqlAdapter1 =new SQLAdapter(sql1);
		this.getBySqlMapper.findRecords(sqlAdapter1);
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	}
	/**
	 * 删除负责人
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getDeleteFzrController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String pkid=request.getParameter("pkid");
		String sql="DELETE FROM sys_personal where pkid="+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		JSONArray jsonArray =new JSONArray();
		this.getBySqlMapper.findRecords(sqlAdapter);
		
		String sql1="DELETE FROM sys_village_responsibility where pkid="+pkid;
		SQLAdapter sqlAdapter1 =new SQLAdapter(sql1);
		this.getBySqlMapper.findRecords(sqlAdapter1);
		
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	
	}
}
