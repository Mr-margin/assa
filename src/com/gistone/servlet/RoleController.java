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

public class RoleController extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;
		/**
		 * 权限
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException 
		 */
		public ModelAndView getSaveRoleController(HttpServletRequest request,HttpServletResponse response) throws IOException{

			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			String functionMap="";
			JSONArray jsonArry=new JSONArray();
			if (session.getAttribute("function_map")!=null){
			Map function_map=(Map) session.getAttribute("function_map");
			Object s[] = function_map.keySet().toArray();
			
			for(int i = 0; i < function_map.size(); i++) {
				json.put("div_id", function_map.get(s[i]));
				jsonArry.add(json);
				}
			
			response.getWriter().write(jsonArry.toString());
			
			}else{
				response.getWriter().print("weidenglu");
			}
			return null;
			}
		/**
		 * 表格的查看情况
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException 
		 */
		public ModelAndView getSaveChakanController(HttpServletRequest request,HttpServletResponse response) throws IOException{
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			String functionMap="";
			JSONArray jsonArry=new JSONArray();
			if (session.getAttribute("company_map")!=null){
			Map company_map=(Map) session.getAttribute("company_map");
			
			String com_name = company_map.get("com_name").toString();
			String com_level = company_map.get("com_level").toString();

			json.put("com_name", com_name);
			json.put("com_level", com_level);
			jsonArry.add(json);
			
			response.getWriter().write(jsonArry.toString());
			
			}else{
				response.getWriter().print("weidenglu");
			}
			return null;
		}
		
}

