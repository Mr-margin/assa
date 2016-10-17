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

public class ExecutiveController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
	
	/**
	 * 行政区划
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getExecutive(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
//		String cha_bfdw=request.getParameter("cha_bfdw");
		String cha_bfdw = ""; //帮扶单位
		String cha_bfr = "";//帮扶责任人

		String str = "";
		//查找鄂尔多斯
		String sql="SELECT * FROM sys_company where pkid=(select min(pkid) from sys_company)";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		
		String xian_sql="SELECT * FROM sys_company where com_f_pkid=(select min(pkid) from sys_company)";
		SQLAdapter xian_sqlAdapter =new SQLAdapter(xian_sql);
		List<Map> xian_list=getBySqlMapper.findRecords(xian_sqlAdapter);
		JSONArray jsonArray =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			obj.put("com_name",val.get("com_name")==null?"-":val.get("com_name"));
			obj.put("com_code",val.get("com_code")==null?"-":val.get("com_code"));
			jsonArray.add(obj);
		}
		for(Map val:xian_list){
			JSONObject xian_obj=new JSONObject ();
			xian_obj.put("xian_pkid",val.get("pkid")==null?"-":val.get("pkid"));
			xian_obj.put("xian_com_name",val.get("com_name")==null?"-":val.get("com_name"));
			xian_obj.put("xian_code",val.get("com_code")==null?"-":val.get("com_code"));
			jsonArray.add(xian_obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	}
	/**
	 * 显示乡
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveXiangController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String id=request.getParameter("id");
		String sql="SELECT * FROM sys_company where com_f_pkid="+id;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray =new JSONArray();
//		JSONArray jsonArray1 =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			obj.put("com_name",val.get("com_name")==null?"-":val.get("com_name"));
			obj.put("com_code",val.get("com_code")==null?"-":val.get("com_code"));
			obj.put("com_level",val.get("com_level")==null?"-":val.get("com_level"));
			obj.put("com_f_code",val.get("com_f_code")==null?"-":val.get("com_f_code"));
			obj.put("com_f_pkid",val.get("com_f_pkid")==null?"-":val.get("com_f_pkid"));
//			String sql1="SELECT * FROM sys_user WHERE account_type=1 AND sys_company_id="+val.get("pkid");
//			SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
//			List<Map> list1=getBySqlMapper.findRecords(sqlAdapter1);
//			JSONObject obj1=new JSONObject ();
//				for(Map va:list1){
//					obj1.put("col_account",val.get("col_account")==null?"-":va.get("col_account"));
//					obj1.put("col_password",val.get("col_password")==null?"-":va.get("col_password"));
//				}
//			jsonArray1.add(obj1);
//			obj.put("huida",jsonArray1);
			jsonArray.add(obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	}
	/**
	 * 显示村
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getSaveCunController(HttpServletRequest request,HttpServletResponse response) throws IOException{

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String id=request.getParameter("id");
		String sql="SELECT * FROM sys_company where com_f_pkid="+id;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
		JSONArray jsonArray =new JSONArray();
//		JSONArray jsonArray1 =new JSONArray();
		for(Map val:list){
			JSONObject obj=new JSONObject ();
			obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
			obj.put("com_name",val.get("com_name")==null?"-":val.get("com_name"));
			obj.put("com_code",val.get("com_code")==null?"-":val.get("com_code"));
			obj.put("com_level",val.get("com_level")==null?"-":val.get("com_level"));
			obj.put("com_f_code",val.get("com_f_code")==null?"-":val.get("com_f_code"));
			obj.put("com_f_pkid",val.get("com_f_pkid")==null?"-":val.get("com_f_pkid"));
//			String sql1="SELECT * FROM sys_user WHERE account_type=1 AND sys_company_id="+val.get("pkid");
//			SQLAdapter sqlAdapter1=new SQLAdapter(sql1);
//			List<Map> list1=getBySqlMapper.findRecords(sqlAdapter1);
//			JSONObject obj1=new JSONObject ();
//				for(Map va:list1){
//					obj1.put("col_account",val.get("col_account")==null?"-":va.get("col_account"));
//					obj1.put("col_password",val.get("col_password")==null?"-":va.get("col_password"));
//				}
//			jsonArray1.add(obj1);
//			obj.put("huida",jsonArray1);
			jsonArray.add(obj);
		}
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	}
	/**
	 * 添加村行政
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getAddXzqhController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String id=request.getParameter("cun_id");//上级行政区划id
		String com_name=request.getParameter("com_name");//行政名称
		String com_code=request.getParameter("com_code");//行政编码
		String cun_code=request.getParameter("cun_code");//上级行政编码
		
		String sql="INSERT INTO sys_company(com_name,com_code,com_level,com_f_code,com_f_pkid) VALUES"+
					"('"+com_name+"','4','"+cun_code+"','"+id+"')";
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		JSONArray jsonArray =new JSONArray();
		this.getBySqlMapper.findRecords(sqlAdapter);
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	}
	/**
	 * 修改行政编码
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView getUpdateBmController(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String pkid=request.getParameter("pkid");//上级行政区划id
		String bm=request.getParameter("bm");//行政名称
		String sql="UPDATE sys_company SET com_code = '"+bm+"'"+
				" WHERE pkid = "+pkid;
		SQLAdapter sqlAdapter =new SQLAdapter(sql);
		JSONArray jsonArray =new JSONArray();
		this.getBySqlMapper.findRecords(sqlAdapter);
		response.getWriter().write(jsonArray.toString());
		response.getWriter().close();
		return null;
	
	}
}
