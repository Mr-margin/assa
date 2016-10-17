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

public class MaintainController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
		/**
		 * 维护的开关
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException 
		 */
		public ModelAndView getOpenRoleController(HttpServletRequest request,HttpServletResponse response) throws IOException{
			
			String  name=request.getParameter("name");
			String 	id=request.getParameter("id");
			String sql="update sys_function set maintain='"+id+"'  where modular_name ='"+name+"'";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			JSONArray jsonArray =new JSONArray();
			this.getBySqlMapper.findRecords(sqlAdapter);
			
			response.getWriter().write(jsonArray.toString());
			response.getWriter().close();
			return null;
		}
		/**
		 * 维护开关的初始化
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 */
		public ModelAndView getSaveMaintainController(HttpServletRequest request,HttpServletResponse response) throws IOException{
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			
			String sql="select pkid,modular_name ,maintain from sys_function";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> list=getBySqlMapper.findRecords(sqlAdapter);
			JSONArray jsonArray =new JSONArray();
			for(Map val:list){
				JSONObject obj=new JSONObject ();
				obj.put("pkid",val.get("pkid")==null?"-":val.get("pkid"));
				obj.put("modular_name", val.get("modular_name")==null?"-":val.get("modular_name"));//角色名称
				obj.put("maintain",val.get("maintain")==null?"-":val.get("maintain"));
				jsonArray.add(obj);
			}
			response.getWriter().write(jsonArray.toString());
			response.getWriter().close();
			return null;
			
		}
}

