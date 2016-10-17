package com.gistone.util;
 import java.io.File;                                
  import java.util.Enumeration;                                
  import java.util.Properties;                                
                                  
  import javax.servlet.ServletContext;                                
  import javax.servlet.ServletContextEvent;                                
  import javax.servlet.ServletContextListener;                                
                                  
  import org.apache.commons.logging.Log;                                
  import org.apache.commons.logging.LogFactory;                                
  import org.logicalcobwebs.proxool.ProxoolException;                                
  import org.logicalcobwebs.proxool.ProxoolFacade;                                
  import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;                                
  import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;                                
                                  
  /**                                
   * <b>Function Name：</b>系统框架_数据库连接池监听器。<br>                                
   * <b>History：</b>2013/05/10 attagain create<br>                                
   * <br>                                
   *                                 
   * @author attagain                                
   * @version 1.0.0 2013/05/10                                
   */                                
  public class DbPoolProxoolListener implements ServletContextListener {                                
   /**                               
    * LOG输出对象                               
    */                               
   private static final Log LOG = LogFactory                               
     .getLog(DbPoolProxoolListener.class);                             
   /**                               
    * 上下文参数_xmlFile(XML文件)                               
    */                               
   private static final String CONTEXT_PARAM_XMLFILE = "xmlFile";                               
   /**                               
    * 上下文参数_propertyFile(属性配置文件)                               
    */                               
   private static final String CONTEXT_PARAM_PROPERTYFILE = "propertyFile";                               
   /**                               
    * 上下文参数_autoShutdown(自动关闭)                               
    */                               
   private static final String AUTO_SHUTDOWN_PROPERTY = "autoShutdown";                               
   /**                               
    * 数据库属性前缀                               
    */                               
   private static final String PREFIX_JDBC = "jdbc";                               
   /**                               
    * Servlet上下路径_根                               
    */                               
   private static final String SERVLET_PATH_ROOT = "/";                               
   /**                               
    * 自动退出                               
    */                               
   private boolean autoShutdown = true;                               
                                  
   /**                               
    * 数据库连接池监听初始化。<br>                               
    *                                
    * @param servletContextEvent                               
    *            servlet上下文事件参数对象                               
    */                               
   public void contextInitialized(ServletContextEvent servletContextEvent) {                               
    // 获取Servlet上下文对象                              
    ServletContext context = servletContextEvent.getServletContext();                              
    // 获取Servlet上下文根的绝对路径                              
    String appDir = servletContextEvent.getServletContext().getRealPath(                              
      SERVLET_PATH_ROOT); 
    if (!appDir.endsWith(java.io.File.separator)) {
    	appDir = appDir + java.io.File.separator;
    }
    // 定义属性配置信息对象                              
    Properties properties = new Properties();                              
                                  
    // 获取上下文初始化参数集合                              
    Enumeration<?> names = context.getInitParameterNames();                              
                                  
    // 逐个循环处理上下文参数信息                              
    while (names.hasMoreElements()) {                              
     // 获取参数名                             
     String name = names.nextElement().toString();                             
     // 根据参数名，获取参数值                             
     String value = context.getInitParameter(name);                             
                                  
     if (CONTEXT_PARAM_XMLFILE.equals(name)) {                             
      // XML配置的情况                            
                                  
      try {                            
       // 获取配置文件句柄                           
       File file = new File(value);                           
                                  
       if (file.isAbsolute()) {                           
        // 相对路径                          
        JAXPConfigurator.configure(value, false);                          
       } else {                           
        // 绝对路径                          
        JAXPConfigurator.configure(appDir + File.separator                          
          + value, false);                        
       }                           
      } catch (ProxoolException e) {                            
       LOG.error("Problem configuring " + value, e);                           
      }                            
     } else if (CONTEXT_PARAM_PROPERTYFILE.equals(name)) {                             
      // 属性文件配置的情况                            
                                  
      try {                            
       // 获取配置文件句柄                           
       File file = new File(value);                           
                                  
       if (file.isAbsolute()) {                           
        // 相对路径                          
        PropertyConfigurator.configure(value);                          
       } else {                           
        // 绝对路径                          
        PropertyConfigurator.configure(appDir + File.separator                          
          + value);                        
       }                           
      } catch (ProxoolException e) {                            
       LOG.error("Problem configuring " + value, e);                           
      }                            
     } else if (AUTO_SHUTDOWN_PROPERTY.equals(name)) {                             
      // 自动关闭的情况                            
      autoShutdown = Boolean.valueOf(value).booleanValue();                            
     } else if (PREFIX_JDBC.startsWith(name)) {                             
      // 其他情况（JDBC开头的字符）                            
      properties.setProperty(name, value);                            
     }                             
    }                              
                                  
    if (properties.size() > 0) {                              
     // 自定义属性存在的情况                             
     try {                             
      // 配置自定属性                            
      PropertyConfigurator.configure(properties);                            
     } catch (ProxoolException e) {                             
      LOG.error("Problem configuring using init properties", e);                            
     }                             
    }                              
   }                               
                                  
   /**                               
    * 数据库连接池监听销毁。<br>                               
    *                                
    * @param servletContextEvent                               
    *            servlet上下文事件参数对象                               
    */                               
   public void contextDestroyed(ServletContextEvent servletContextEvent) {                               
    if (this.autoShutdown) {                              
     ProxoolFacade.shutdown(0);                             
    }                              
   }                               
  }                    