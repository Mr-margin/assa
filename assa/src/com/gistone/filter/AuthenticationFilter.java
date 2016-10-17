package com.gistone.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {

	protected String excludePage = "/index.html";
	protected String redirectPage = "/index.html";
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest myrequest = (HttpServletRequest)request;//
		HttpServletResponse myresponse = (HttpServletResponse)response;
		myresponse.setCharacterEncoding("utf-8");
		String currentURL = myrequest.getRequestURI();//
		String targetURL = currentURL.substring(currentURL.lastIndexOf("/")+1, currentURL.length());  //
		HttpSession session = myrequest.getSession();
//		System.out.println("1");
		if(currentURL.indexOf("/H/1.html") != -1 ){
			if(excludePage.indexOf(targetURL) == -1){		
				if((session == null || session.getAttribute("Login_map")==null)){
					String isAjaxRequest = myrequest.getHeader("isAjaxRequest" );
					if (isAjaxRequest!=null&&isAjaxRequest.equals("true")) { 
						myresponse.addHeader("sessionstatus" ,"timeout");
						PrintWriter out = myresponse.getWriter();
						out.print("login-time-out");   
						out.close();
					}else {
						PrintWriter out = myresponse.getWriter();
						StringBuilder builder = new StringBuilder();
						builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
						builder.append("window.top.location.href=\"");
						String path = myrequest.getContextPath();
						builder.append(path);
						builder.append("/index.html\";</script>");
						myresponse.setContentType("text/html;charset=utf-8");
						out.print(builder.toString());
						out.close();// 普通 jsp页面session过期处理
					}
					return;
				}
			}
		}
		chain.doFilter(myrequest, myresponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.excludePage = filterConfig.getInitParameter("excludePage");
		this.redirectPage = filterConfig.getInitParameter("redirectPage");
	}

}
