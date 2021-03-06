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
import com.gistone.util.Tool;

public class Index_Controller extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;

	//	首页上方的表格中-贫困户数-贫困人数
	public ModelAndView getindex_table1(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String gors = request.getParameter("gors");
		String code = request.getParameter("code");
		String pkid = request.getParameter("pkid");

		HttpSession session = request.getSession();//取session
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称

			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			if(code.toString().equals("shi")==true){//市级用户

				/*String sql="select * from  (select * from (select com_name  from sys_company s where s.com_f_pkid = "+pkid+" )a  LEFT JOIN  ( SELECT         b.v3,          count,          count1,          sum,          sum1       FROM          (             SELECT                v3,          "
						+ "      count(*)AS count,                sum(v9)AS sum             FROM                da_household             WHERE                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY                v3          )a  "
						+ "     RIGHT JOIN(          SELECT             v3,             count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016          WHERE             init_flag = '"+gors+"'          AND v21 != '已脱贫'     AND entry_year = '2016'     GROUP BY             v3       )b ON a.v3 = b.v3  "
						+ " UNION  SELECT          a.v3,          count,          count1,          sum,          sum1       FROM          (             SELECT                v3,                count(*)AS count,                sum(v9)AS sum             FROM                da_household             WHERE         "
						+ "       init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY                v3          )a       left JOIN(          SELECT             v3,             count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016          WHERE        "
						+ "     init_flag = '"+gors+"'          AND v21 != '已脱贫'     AND entry_year = '2016'    GROUP BY             v3       )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )e INNER JOIN ( select * from (select com_name from sys_company s where s.com_f_pkid = "+pkid+")a  LEFT JOIN  (  SELECT       a.v3,       sum2,       sum3    FROM       (          SELECT         "
						+ "    v3,             count(*)AS sum2          FROM             da_household          WHERE             sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2017'          GROUP BY             v3       )a    LEFT JOIN(       SELECT          v3,        "
						+ "  count(*)AS sum3       FROM          da_household_2016       WHERE          sys_standard = '"+gors+"'       AND v21 = '已脱贫'   AND entry_year = '2016'    GROUP BY          v3    )b ON a.v3 = b.v3      UNION        SELECT          b.v3,          sum2,          sum3       FROM          (         "
						+ "    SELECT                v3,                count(*)AS sum2             FROM                da_household             WHERE                sys_standard = '"+gors+"'             AND v21 = '已脱贫'             AND entry_year = '2017'             GROUP BY                v3          )a       RIGHT JOIN(   "
						+ "       SELECT             v3,             count(*)AS sum3          FROM             da_household_2016          WHERE             sys_standard = '"+gors+"'          AND v21 = '已脱贫'     AND entry_year = '2016'     GROUP BY             v3       )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )f on e.com_name = f.com_name ";*/
				/*String sql = " SELECT B.*, A.count1, count,  sum,       sum1, sum2,sum3 FROM (SELECT    e.com_name,count,             count1,             sum,             sum1,  						sum2,             sum3 FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM           "
						+ "     sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count,             count1,             sum,             sum1          FROM             (                SELECT                   v3,                   count(*)AS count,         "
						+ "          sum(v9)AS sum                FROM                   da_household                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                            GROUP BY                   v3             )a          RIGHT JOIN(             SELECT             "
						+ "   v3,                count(*)AS count1,                sum(v9)AS sum1             FROM                da_household_2016             WHERE                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2016'             GROUP BY                v3          )b ON a.v3 = b.v3          UNION    "
						+ "         SELECT                a.v3,                count,                count1,                sum,                sum1             FROM                (                   SELECT                      v3,                      count(*)AS count,                      sum(v9)AS sum                   FROM                      da_household        "
						+ "           WHERE                      init_flag = '"+gors+"'                   AND v21 != '已脱贫'                                   GROUP BY                      v3                )a             LEFT JOIN(                SELECT                   v3,                   count(*)AS count1,                   sum(v9)AS sum1      "
						+ "          FROM                   da_household_2016                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                AND entry_year = '2016'                GROUP BY                   v3             )b ON a.v3 = b.v3       )d ON a.com_name = d.v3    )e INNER JOIN(    SELECT       *    FROM       (     "
						+ "     SELECT     "
						+ "        com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a    LEFT JOIN(       SELECT          a.v3,          sum2,          sum3       FROM          (             SELECT                v3,                count(*)AS sum2             FROM                da_household             WHERE                sys_standard = '"+gors+"'   "
						+ "          AND v21 = '已脱贫'                          GROUP BY                v3          )a       LEFT JOIN(          SELECT             v3,             count(*)AS sum3          FROM             da_household_2016          WHERE             sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2016'    "
						+ "      GROUP BY             v3       )b ON a.v3 = b.v3    "
						+ "   UNION          SELECT             b.v3,             sum2,             sum3          FROM             (                SELECT                   v3,                   count(*)AS sum2                FROM                   da_household                WHERE                   sys_standard = '"+gors+"'                AND v21 = '已脱贫'                "
						+ "              GROUP BY                   v3             )a          RIGHT JOIN(             SELECT                v3,                count(*)AS sum3             FROM                da_household_2016             WHERE                sys_standard = '"+gors+"'             AND v21 = '已脱贫'             AND entry_year = '2016'             GROUP BY                v3          )b ON a.v3 = b.v3  "
						+ "  )d ON a.com_name = d.v3 )f ON e.com_name = f.com_name )A ,  ( SELECT    e.com_name ,count_17_hu,sum_17_person,count_16_hu,sum_16_person, count_17_hu_no,sum_17_person_no,count_16_hu_no,sum_16_person_no, count_17_hu_yes, sum_17_person_yes,count_16_hu_yes, sum_16_person_yes "+
						" FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM                sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count_17_hu,             sum_17_person,             count_16_hu,             sum_16_person          FROM             (       "
						+ "         SELECT                   v3,                   count(*)AS count_17_hu,                   sum(v9)AS sum_17_person                FROM                   da_household                            GROUP BY                   v3             )a          RIGHT JOIN(             SELECT                v3,                count(*)AS count_16_hu,        "
						+ "        sum(v9)AS sum_16_person             FROM                da_household_2016             WHERE             entry_year = '2016'             GROUP BY                v3          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu, 							sum_17_person, 							count_16_hu, 						"
						+ "	sum_16_person             FROM                (                   SELECT                      v3,                      count(*)AS count_17_hu,                      sum(v9)AS sum_17_person                   FROM                      da_household                                  GROUP BY                      v3                )a         "
						+ "    LEFT JOIN(                SELECT                   v3,                   count(*)AS count_16_hu,                   sum(v9)AS sum_16_person                FROM                   da_household_2016                WHERE               entry_year = '2016'                GROUP BY                   v3            )b ON a.v3 = b.v3 					       )d ON a.com_name = d.v3    )e INNER JOIN (  "
						+ "  SELECT       *    FROM       (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_no,             sum_17_person_no,             count_16_hu_no,             sum_16_person_no          FROM             (                SELECT                   v3,   "
						+ "                count(*)AS count_17_hu_no,                   sum(v9)AS sum_17_person_no                FROM                   da_household                WHERE 							v21 != '已脱贫'							                GROUP BY                   v3             )a          RIGHT JOIN(             SELECT                v3,                count(*)AS count_16_hu_no,  "
						+ "              sum(v9)AS sum_16_person_no             FROM                da_household_2016             WHERE 						v21 != '已脱贫' and             entry_year = '2016'             GROUP BY                v3          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu_no, 							sum_17_person_no, 			"
						+ "			count_16_hu_no, 							sum_16_person_no             FROM                (                   SELECT                      v3,                      count(*)AS count_17_hu_no,                      sum(v9)AS sum_17_person_no                   FROM                      da_household                   WHERE 										v21 != '已脱贫'      "
						+ "             GROUP BY                      v3                )a             LEFT JOIN(                SELECT                   v3,                   count(*)AS count_16_hu_no,                   sum(v9)AS sum_16_person_no                FROM                   da_household_2016                WHERE 								v21 != '已脱贫' and               entry_year = '2016'                GROUP BY                   v3            )b ON a.v3 = b.v3 	"
						+ "				       )d ON a.com_name = d.v3  )f ON e.com_name = f.com_name  INNER "+
						" JOIN (    SELECT          com_name , count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes from        (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,         "
						+ "    sum_16_person_yes          FROM             (                SELECT                   v3,                   count(*)AS count_17_hu_yes,                   sum(v9)AS sum_17_person_yes                FROM                   da_household                WHERE 							v21 = '已脱贫'  		           GROUP BY                   v3             )a   "
						+ "       RIGHT JOIN(             SELECT                v3,                count(*)AS count_16_hu_yes,                sum(v9)AS sum_16_person_yes             FROM                da_household_2016             WHERE 						v21 = '已脱贫' and             entry_year = '2016'             GROUP BY                v3          )b ON a.v3 = b.v3      union 				"
						+ "		SELECT                a.v3,                 count_17_hu_yes, 							sum_17_person_yes, 							count_16_hu_yes, 							sum_16_person_yes             FROM                (                   SELECT                      v3,                      count(*)AS count_17_hu_yes,                      sum(v9)AS sum_17_person_yes       "
						+ "            FROM                      da_household                   WHERE 										v21 = '已脱贫'                    GROUP BY                      v3                )a             LEFT JOIN(                SELECT                   v3,                   count(*)AS count_16_hu_yes,                   sum(v9)AS sum_16_person_yes    "
						+ "            FROM                   da_household_2016                WHERE 								v21 = '已脱贫' and               entry_year = '2016'                GROUP BY                   v3            )b ON a.v3 = b.v3 					 )d ON a.com_name = d.v3 )g  on f.com_name = g.com_name )B WHERE A.com_name = B.com_name ";*/
				String sql = "SELECT    B.*, A.count1,    count,    sum,    sum1,    sum2,    sum3 FROM    (       SELECT          e.com_name,          count,          count1,          sum,          sum1,          sum2,          sum3       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE    s.com_f_pkid =  "+pkid+"                )a  "
						+ "           LEFT JOIN(                SELECT b.v3, count, count1, sum, sum1                FROM (    SELECT       v3,       count(*)AS count,       sum(v9)AS sum    FROM       da_household    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    GROUP BY       v3 )a                RIGHT JOIN( SELECT    v3,    count(*)AS count1,    sum(v9)AS sum1 FROM    da_household_2016 WHERE    init_flag = '"+gors+"' AND v21 != '已脱贫' AND entry_year = '2016' GROUP BY    v3                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count,    count1,    sum,    sum1 FROM    (       SELECT  v3,  count(*)AS count,  sum(v9)AS sum       FROM  da_household       WHERE  init_flag = '"+gors+"'       AND v21 != '已脱贫'       GROUP BY  v3    )a LEFT JOIN(    SELECT       v3,       count(*)AS count1,       sum(v9)AS sum1    FROM       da_household_2016    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    AND entry_year = '2016'    GROUP BY       v3 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s               "
								+ " WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                a.v3,                sum2,                sum3             FROM                ( SELECT    v3,    count(*)AS sum2 FROM    da_household WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' GROUP BY    v3                )a             LEFT JOIN(                SELECT v3, count(*)AS sum3                FROM da_household_2016                WHERE sys_standard = '"+gors+"'                AND v21 = '已脱贫'                AND entry_year = '2016'                GROUP BY v3             )b ON a.v3 = b.v3             UNION                SELECT b.v3, sum2, sum3                FROM (    SELECT       v3,       count(*)AS sum2    FROM       da_household    WHERE       sys_standard = '"+gors+"'    AND v21 = '已脱贫'    GROUP BY       v3 )a                RIGHT JOIN( SELECT    v3,    count(*)AS sum3 FROM    da_household_2016 WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' AND entry_year = '2016' GROUP BY    v3                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name    )A,    (       SELECT          e.com_name,          count_17_hu,          sum_17_person,          count_16_hu,          sum_16_person,          count_17_hu_no,          sum_17_person_no,          count_16_hu_no,          sum_16_person_no,          count_17_hu_yes,          sum_17_person_yes,          count_16_hu_yes,          sum_16_person_yes       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE"
										+ "    s.com_f_pkid =  "+pkid+"                )a             LEFT JOIN(                SELECT b.v3, count_17_hu, sum_17_person, count_16_hu, sum_16_person                FROM (    SELECT       v3,       count(*)AS count_17_hu,       sum(v9)AS sum_17_person    FROM       da_household	where sys_standard = '"+gors+"'	    GROUP BY       v3 )a                RIGHT JOIN( SELECT    v3,    count(*)AS count_16_hu,    sum(v9)AS sum_16_person FROM    da_household_2016 WHERE    entry_year = '2016' 	and sys_standard = '"+gors+"' GROUP BY    v3                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count_17_hu,    sum_17_person,    count_16_hu,    sum_16_person FROM    (       SELECT  v3,  count(*)AS count_17_hu,  sum(v9)AS sum_17_person       FROM  da_household 														WHERE sys_standard = '"+gors+"' GROUP BY  v3    )a LEFT JOIN(    SELECT       v3,       count(*)AS count_16_hu,       sum(v9)AS sum_16_person    FROM       da_household_2016    WHERE       entry_year = '2016' 	and sys_standard = '"+gors+"'    GROUP BY       v3 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s       "
												+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_no,                sum_17_person_no,                count_16_hu_no,                sum_16_person_no             FROM                ( SELECT    v3,    count(*)AS count_17_hu_no,    sum(v9)AS sum_17_person_no FROM    da_household WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' GROUP BY    v3                )a             RIGHT JOIN(                SELECT v3, count(*)AS count_16_hu_no, sum(v9)AS sum_16_person_no                FROM da_household_2016                WHERE v21 != '已脱贫'  and init_flag = '"+gors+"'                AND entry_year = '2016'                GROUP BY v3             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_no, sum_17_person_no, count_16_hu_no, sum_16_person_no                FROM (    SELECT       v3,       count(*)AS count_17_hu_no,       sum(v9)AS sum_17_person_no    FROM       da_household    WHERE       v21 != '已脱贫'  and init_flag = '"+gors+"'    GROUP BY       v3 )a                LEFT JOIN( SELECT    v3,    count(*)AS count_16_hu_no,    sum(v9)AS sum_16_person_no FROM    da_household_2016 WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' AND entry_year = '2016' GROUP BY    v3                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name       INNER JOIN(          SELECT             com_name,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes          FROM             (                SELECT com_name                FROM sys_company s       "
														+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_yes,                sum_17_person_yes,                count_16_hu_yes,                sum_16_person_yes             FROM                ( SELECT    v3,    count(*)AS count_17_hu_yes,    sum(v9)AS sum_17_person_yes FROM    da_household WHERE    v21 = '已脱贫'  and sys_standard = '"+gors+"' GROUP BY    v3                )a             RIGHT JOIN(                SELECT v3, count(*)AS count_16_hu_yes, sum(v9)AS sum_16_person_yes                FROM da_household_2016                WHERE v21 = '已脱贫'                AND entry_year = '2016' and sys_standard = '"+gors+"'                GROUP BY v3             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_yes, sum_17_person_yes, count_16_hu_yes, sum_16_person_yes                FROM (    SELECT       v3,       count(*)AS count_17_hu_yes,       sum(v9)AS sum_17_person_yes    FROM       da_household    WHERE       v21 = '已脱贫' and sys_standard = '"+gors+"'    GROUP BY       v3 )a                LEFT JOIN( SELECT    v3,    count(*)AS count_16_hu_yes,    sum(v9)AS sum_16_person_yes FROM    da_household_2016 WHERE    v21 = '已脱贫' AND entry_year = '2016' and sys_standard = '"+gors+"' GROUP BY    v3                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )g ON f.com_name = g.com_name    )B WHERE    A.com_name = B.com_name ";
			
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				/*SQLAdapter sqlAdapter2 =new SQLAdapter(sql2);
				List<Map> sql_list2 = this.getBySqlMapper.findRecords(sqlAdapter2);*/
				
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					JSONObject val = new JSONObject();
					for(int i = 0;i<sql_list.size();i++){
						val.put("v3","".equals(sql_list.get(i).get("com_name")) || sql_list.get(i).get("com_name") == null ?"": sql_list.get(i).get("com_name").toString());
						val.put("count","".equals(sql_list.get(i).get("count")) || sql_list.get(i).get("count") == null ?"0": sql_list.get(i).get("count").toString());
						val.put("count1","".equals(sql_list.get(i).get("count1")) || sql_list.get(i).get("count1") == null ?"0": sql_list.get(i).get("count1").toString());
						val.put("sum","".equals(sql_list.get(i).get("sum")) || sql_list.get(i).get("sum") == null ?"0": sql_list.get(i).get("sum").toString());
						val.put("sum1","".equals(sql_list.get(i).get("sum1")) || sql_list.get(i).get("sum1") == null ?"0": sql_list.get(i).get("sum1").toString());
						val.put("sum2","".equals(sql_list.get(i).get("sum2")) || sql_list.get(i).get("sum2") == null ?"0": sql_list.get(i).get("sum2").toString());
						val.put("sum3","".equals(sql_list.get(i).get("sum3")) || sql_list.get(i).get("sum3") == null ?"0": sql_list.get(i).get("sum3").toString());
						val.put("count_17_hu","".equals(sql_list.get(i).get("count_17_hu")) || sql_list.get(i).get("count_17_hu") == null ?"0": sql_list.get(i).get("count_17_hu").toString());
						val.put("sum_17_person","".equals(sql_list.get(i).get("sum_17_person")) || sql_list.get(i).get("sum_17_person") == null ?"0": sql_list.get(i).get("sum_17_person").toString());
						val.put("count_16_hu","".equals(sql_list.get(i).get("count_16_hu")) || sql_list.get(i).get("count_16_hu") == null ?"0": sql_list.get(i).get("count_16_hu").toString());
						val.put("sum_16_person","".equals(sql_list.get(i).get("sum_16_person")) || sql_list.get(i).get("sum_16_person") == null ?"0": sql_list.get(i).get("sum_16_person").toString());
						val.put("count_17_hu_no","".equals(sql_list.get(i).get("count_17_hu_no")) || sql_list.get(i).get("count_17_hu_no") == null ?"0": sql_list.get(i).get("count_17_hu_no").toString());
						val.put("sum_17_person_no","".equals(sql_list.get(i).get("sum_17_person_no")) || sql_list.get(i).get("sum_17_person_no") == null ?"0": sql_list.get(i).get("sum_17_person_no").toString());
						val.put("count_16_hu_no","".equals(sql_list.get(i).get("count_16_hu_no")) || sql_list.get(i).get("count_16_hu_no") == null ?"0": sql_list.get(i).get("count_16_hu_no").toString());
						val.put("sum_16_person_no","".equals(sql_list.get(i).get("sum_16_person_no")) || sql_list.get(i).get("sum_16_person_no") == null ?"0": sql_list.get(i).get("sum_16_person_no").toString());
						val.put("count_17_hu_yes","".equals(sql_list.get(i).get("count_17_hu_yes")) || sql_list.get(i).get("count_17_hu_yes") == null ?"0": sql_list.get(i).get("count_17_hu_yes").toString());
						val.put("sum_17_person_yes","".equals(sql_list.get(i).get("sum_17_person_yes")) || sql_list.get(i).get("sum_17_person_yes") == null ?"0": sql_list.get(i).get("sum_17_person_yes").toString());
						val.put("count_16_hu_yes","".equals(sql_list.get(i).get("count_16_hu_yes")) || sql_list.get(i).get("count_16_hu_yes") == null ?"0": sql_list.get(i).get("count_16_hu_yes").toString());
						val.put("sum_16_person_yes","".equals(sql_list.get(i).get("sum_16_person_yes")) || sql_list.get(i).get("sum_16_person_yes") == null ?"0": sql_list.get(i).get("sum_16_person_yes").toString());

						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().write("0");
				}
			}else if(company_json.get("com_level").toString().equals("2")==true){//二级用户
/*
				String sql="select * from  (select * from (select com_name from sys_company s where s.com_f_pkid ="+pkid+" )a  LEFT JOIN  ( SELECT          b.v3,          count,          count1,          sum,          sum1       FROM          (             SELECT              "
						+ "  y1.v4 as v3,                count(*)AS count,                sum(v9)AS sum             FROM                da_household y1             JOIN sys_company y2 ON y1.v3 = y2.com_name             WHERE                y2.com_code = "+code+"              and init_flag = '"+gors+"'        "
						+ "     AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY                y1.v4          )a       RIGHT JOIN(          SELECT            y1.v4 as v3,             count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016 y1     "
						+ "     JOIN sys_company y2 ON y1.v3 = y2.com_name          WHERE             y2.com_code = "+code+"          and             init_flag = '"+gors+"'          AND v21 != '已脱贫'    AND entry_year = '2016'      GROUP BY             y1.v4       )b ON a.v3 = b.v3   UNION  SELECT          a.v3,          count,  "
						+ "        count1,          sum,          sum1       FROM          (             SELECT                y1.v4 as v3,                count(*)AS count,                sum(v9)AS sum             FROM                da_household y1 					JOIN sys_company y2 ON y1.v3 = y2.com_name 			"
						+ "			WHERE             y2.com_code = "+code+"             and                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY               y1.v4          )a       left JOIN(          SELECT        y1.v4 as     v3,           "
						+ "  count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016 y1          JOIN sys_company y2 ON y1.v3 = y2.com_name          WHERE             y2.com_code = "+code+" 					and             init_flag = '"+gors+"'          AND v21 != '已脱贫'  AND entry_year = '2016'  "
						+ "      GROUP BY             y1.v4        )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )e INNER JOIN ( select * from (select com_name from sys_company s where s.com_f_pkid = "+pkid+")a  LEFT JOIN  (  SELECT       a.v3,       sum2,       sum3    FROM       (          SELECT            y1.v4 as v3,     "
						+ "        count(*)AS sum2          FROM             da_household y1             JOIN sys_company y2 ON y1.v3 = y2.com_name             WHERE                y2.com_code = "+code+" 					and 						sys_standard = '"+gors+"'          AND v21 = '已脱贫'        "
						+ "  AND entry_year = '2017'          GROUP BY             y1.v4       )a    LEFT JOIN(       SELECT          y1.v4 as v3,          count(*)AS sum3       FROM          da_household_2016 y1       JOIN sys_company y2 ON y1.v3 = y2.com_name           WHERE             y2.com_code = "+code+"      "
						+ " and          sys_standard = '"+gors+"'       AND v21 = '已脱贫'  AND entry_year = '2016'     GROUP BY          y1.v4     )b ON a.v3 = b.v3      UNION        SELECT          b.v3,          sum2,          sum3       FROM          (             SELECT              y1.v4 as v3,             count(*)AS sum2          FROM    "
						+ "         da_household y1             JOIN sys_company y2 ON y1.v3 = y2.com_name             WHERE                y2.com_code = "+code+" 					and 						sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2017'          GROUP BY      "
						+ "       y1.v4          )a       RIGHT JOIN(          SELECT             y1.v4 as v3,          count(*)AS sum3       FROM          da_household_2016 y1       JOIN sys_company y2 ON y1.v3 = y2.com_name           WHERE             y2.com_code = "+code+"       and          sys_standard = '"+gors+"'  "
						+ "    AND v21 = '已脱贫'  AND entry_year = '2016'     GROUP BY          y1.v4        )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )f on e.com_name = f.com_name  ";*/
				/*String sql = " SELECT B.*, A.count1, count,  sum,       sum1, sum2,sum3 FROM (SELECT    e.com_name,count,             count1,             sum,             sum1,  						sum2,             sum3 FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM           "
						+ "     sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count,             count1,             sum,             sum1          FROM             (                SELECT                v4 as  v3,                   count(*)AS count,         "
						+ "          sum(v9)AS sum                FROM                   da_household                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                            GROUP BY                   v4             )a          RIGHT JOIN(             SELECT             "
						+ "   v4 as  v3,                count(*)AS count1,                sum(v9)AS sum1             FROM                da_household_2016             WHERE                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2016'             GROUP BY                v4          )b ON a.v3 = b.v3          UNION    "
						+ "         SELECT                a.v3,                count,                count1,                sum,                sum1             FROM                (                   SELECT                      v4 as  v3,                      count(*)AS count,                      sum(v9)AS sum                   FROM                      da_household        "
						+ "           WHERE                      init_flag = '"+gors+"'                   AND v21 != '已脱贫'                                     GROUP BY                      v4                )a             LEFT JOIN(                SELECT                   v4 as  v3,                   count(*)AS count1,                   sum(v9)AS sum1      "
						+ "          FROM                   da_household_2016                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                AND entry_year = '2016'                GROUP BY                   v4             )b ON a.v3 = b.v3       )d ON a.com_name = d.v3    )e INNER JOIN(    SELECT       *    FROM       (     "
						+ "     SELECT     "
						+ "        com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a    LEFT JOIN(       SELECT          a.v3,          sum2,          sum3       FROM          (             SELECT                v4 as  v3,                count(*)AS sum2             FROM                da_household             WHERE                sys_standard = '"+gors+"'   "
						+ "          AND v21 = '已脱贫'                         GROUP BY                v4          )a       LEFT JOIN(          SELECT             v4 as  v3,             count(*)AS sum3          FROM             da_household_2016          WHERE             sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2016'    "
						+ "      GROUP BY             v4      )b ON a.v3 = b.v3    "
						+ "   UNION          SELECT             b.v3,             sum2,             sum3          FROM             (                SELECT                   v4 as  v3,                   count(*)AS sum2                FROM                   da_household                WHERE                   sys_standard = '"+gors+"'                AND v21 = '已脱贫'                "
						+ "              GROUP BY                   v4           )a          RIGHT JOIN(             SELECT                v4 as  v3,                count(*)AS sum3             FROM                da_household_2016             WHERE                sys_standard = '"+gors+"'             AND v21 = '已脱贫'             AND entry_year = '2016'             GROUP BY                v4          )b ON a.v3 = b.v3  "
						+ "  )d ON a.com_name = d.v3 )f ON e.com_name = f.com_name )A ,  ( SELECT    e.com_name ,count_17_hu,sum_17_person,count_16_hu,sum_16_person, count_17_hu_no,sum_17_person_no,count_16_hu_no,sum_16_person_no, count_17_hu_yes, sum_17_person_yes,count_16_hu_yes, sum_16_person_yes "+
						" FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM                sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count_17_hu,             sum_17_person,             count_16_hu,             sum_16_person          FROM             (       "
						+ "         SELECT                   v4 as  v3,                   count(*)AS count_17_hu,                   sum(v9)AS sum_17_person                FROM                   da_household                               GROUP BY                   v4            )a          RIGHT JOIN(             SELECT                v4 as  v3,                count(*)AS count_16_hu,        "
						+ "        sum(v9)AS sum_16_person             FROM                da_household_2016             WHERE             entry_year = '2016'             GROUP BY                v4          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu, 							sum_17_person, 							count_16_hu, 						"
						+ "	sum_16_person             FROM                (                   SELECT                      v4 as  v3,                      count(*)AS count_17_hu,                      sum(v9)AS sum_17_person                   FROM                      da_household                                    GROUP BY                      v4                )a         "
						+ "    LEFT JOIN(                SELECT                   v4 as  v3,                   count(*)AS count_16_hu,                   sum(v9)AS sum_16_person                FROM                   da_household_2016                WHERE               entry_year = '2016'                GROUP BY                   v4            )b ON a.v3 = b.v3 					       )d ON a.com_name = d.v3    )e INNER JOIN (  "
						+ "  SELECT       *    FROM       (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_no,             sum_17_person_no,             count_16_hu_no,             sum_16_person_no          FROM             (                SELECT                   v4 as  v3,   "
						+ "                count(*)AS count_17_hu_no,                   sum(v9)AS sum_17_person_no                FROM                   da_household                WHERE 							v21 != '已脱贫' 						                GROUP BY                   v4            )a          RIGHT JOIN(             SELECT                v4 as  v3,                count(*)AS count_16_hu_no,  "
						+ "              sum(v9)AS sum_16_person_no             FROM                da_household_2016             WHERE 						v21 != '已脱贫' and             entry_year = '2016'             GROUP BY                v4          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu_no, 							sum_17_person_no, 			"
						+ "			count_16_hu_no, 							sum_16_person_no             FROM                (                   SELECT                      v4 as  v3,                      count(*)AS count_17_hu_no,                      sum(v9)AS sum_17_person_no                   FROM                      da_household                   WHERE 										v21 != '已脱贫'      "
						+ "             GROUP BY                      v4                )a             LEFT JOIN(                SELECT                   v4 as  v3,                   count(*)AS count_16_hu_no,                   sum(v9)AS sum_16_person_no                FROM                   da_household_2016                WHERE 								v21 != '已脱贫' and               entry_year = '2016'                GROUP BY                   v4           )b ON a.v3 = b.v3 	"
						+ "				       )d ON a.com_name = d.v3  )f ON e.com_name = f.com_name  INNER "+
						" JOIN (    SELECT          com_name , count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes from        (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,         "
						+ "    sum_16_person_yes          FROM             (                SELECT                   v4 as  v3,                   count(*)AS count_17_hu_yes,                   sum(v9)AS sum_17_person_yes                FROM                   da_household                WHERE 							v21 = '已脱贫'            GROUP BY                   v4             )a   "
						+ "       RIGHT JOIN(             SELECT                v4 as  v3,                count(*)AS count_16_hu_yes,                sum(v9)AS sum_16_person_yes             FROM                da_household_2016             WHERE 						v21 = '已脱贫' and             entry_year = '2016'             GROUP BY                v4          )b ON a.v3 = b.v3      union 				"
						+ "		SELECT                a.v3,                 count_17_hu_yes, 							sum_17_person_yes, 							count_16_hu_yes, 							sum_16_person_yes             FROM                (                   SELECT                      v4 as  v3,                      count(*)AS count_17_hu_yes,                      sum(v9)AS sum_17_person_yes       "
						+ "            FROM                      da_household                   WHERE 										v21 = '已脱贫'                    GROUP BY                      v4               )a             LEFT JOIN(                SELECT                   v4 as  v3,                   count(*)AS count_16_hu_yes,                   sum(v9)AS sum_16_person_yes    "
						+ "            FROM                   da_household_2016                WHERE 								v21 = '已脱贫' and               entry_year = '2016'                GROUP BY                   v4            )b ON a.v3 = b.v3 					 )d ON a.com_name = d.v3 )g  on f.com_name = g.com_name )B WHERE A.com_name = B.com_name ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);*/
				String sql = "SELECT    B.*, A.count1,    count,    sum,    sum1,    sum2,    sum3 FROM    (       SELECT          e.com_name,          count,          count1,          sum,          sum1,          sum2,          sum3       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE    s.com_f_pkid =  "+pkid+"                )a  "
						+ "           LEFT JOIN(                SELECT b.v3, count, count1, sum, sum1                FROM (    SELECT       v4 as  v3,       count(*)AS count,       sum(v9)AS sum    FROM       da_household    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    GROUP BY       v4 )a                RIGHT JOIN( SELECT    v4 as  v3,    count(*)AS count1,    sum(v9)AS sum1 FROM    da_household_2016 WHERE    init_flag = '"+gors+"' AND v21 != '已脱贫' AND entry_year = '2016' GROUP BY    v4                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count,    count1,    sum,    sum1 FROM    (       SELECT  v4 as  v3,  count(*)AS count,  sum(v9)AS sum       FROM  da_household       WHERE  init_flag = '"+gors+"'       AND v21 != '已脱贫'       GROUP BY  v4    )a LEFT JOIN(    SELECT       v4 as  v3,       count(*)AS count1,       sum(v9)AS sum1    FROM       da_household_2016    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    AND entry_year = '2016'    GROUP BY       v4 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s               "
								+ " WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                a.v3,                sum2,                sum3             FROM                ( SELECT    v4 as  v3,    count(*)AS sum2 FROM    da_household WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' GROUP BY    v4               )a             LEFT JOIN(                SELECT v4 as  v3, count(*)AS sum3                FROM da_household_2016                WHERE sys_standard = '"+gors+"'                AND v21 = '已脱贫'                AND entry_year = '2016'                GROUP BY v4             )b ON a.v3 = b.v3             UNION                SELECT b.v3, sum2, sum3                FROM (    SELECT       v4 as  v3,       count(*)AS sum2    FROM       da_household    WHERE       sys_standard = '"+gors+"'    AND v21 = '已脱贫'    GROUP BY       v4 )a                RIGHT JOIN( SELECT    v4 as  v3,    count(*)AS sum3 FROM    da_household_2016 WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' AND entry_year = '2016' GROUP BY    v4                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name    )A,    (       SELECT          e.com_name,          count_17_hu,          sum_17_person,          count_16_hu,          sum_16_person,          count_17_hu_no,          sum_17_person_no,          count_16_hu_no,          sum_16_person_no,          count_17_hu_yes,          sum_17_person_yes,          count_16_hu_yes,          sum_16_person_yes       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE"
										+ "    s.com_f_pkid =  "+pkid+"                )a             LEFT JOIN(                SELECT b.v3, count_17_hu, sum_17_person, count_16_hu, sum_16_person                FROM (    SELECT       v4 as  v3,       count(*)AS count_17_hu,       sum(v9)AS sum_17_person    FROM       da_household	where sys_standard = '"+gors+"'	    GROUP BY       v4 )a                RIGHT JOIN( SELECT    v4 as  v3,    count(*)AS count_16_hu,    sum(v9)AS sum_16_person FROM    da_household_2016 WHERE    entry_year = '2016' 	and sys_standard = '"+gors+"' GROUP BY    v4                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count_17_hu,    sum_17_person,    count_16_hu,    sum_16_person FROM    (       SELECT  v4 as  v3,  count(*)AS count_17_hu,  sum(v9)AS sum_17_person       FROM  da_household 														WHERE sys_standard = '"+gors+"' GROUP BY  v4    )a LEFT JOIN(    SELECT       v4 as  v3,       count(*)AS count_16_hu,       sum(v9)AS sum_16_person    FROM       da_household_2016    WHERE       entry_year = '2016' 	and sys_standard = '"+gors+"'    GROUP BY       v4 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s       "
												+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_no,                sum_17_person_no,                count_16_hu_no,                sum_16_person_no             FROM                ( SELECT    v4 as  v3,    count(*)AS count_17_hu_no,    sum(v9)AS sum_17_person_no FROM    da_household WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' GROUP BY    v4                )a             RIGHT JOIN(                SELECT v4 as  v3, count(*)AS count_16_hu_no, sum(v9)AS sum_16_person_no                FROM da_household_2016                WHERE v21 != '已脱贫'  and init_flag = '"+gors+"'                AND entry_year = '2016'                GROUP BY v4             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_no, sum_17_person_no, count_16_hu_no, sum_16_person_no                FROM (    SELECT       v4 as  v3,       count(*)AS count_17_hu_no,       sum(v9)AS sum_17_person_no    FROM       da_household    WHERE       v21 != '已脱贫'  and init_flag = '"+gors+"'    GROUP BY       v4 )a                LEFT JOIN( SELECT    v4 as  v3,    count(*)AS count_16_hu_no,    sum(v9)AS sum_16_person_no FROM    da_household_2016 WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' AND entry_year = '2016' GROUP BY    v4                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name       INNER JOIN(          SELECT             com_name,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes          FROM             (                SELECT com_name                FROM sys_company s       "
														+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_yes,                sum_17_person_yes,                count_16_hu_yes,                sum_16_person_yes             FROM                ( SELECT    v4 as  v3,    count(*)AS count_17_hu_yes,    sum(v9)AS sum_17_person_yes FROM    da_household WHERE    v21 = '已脱贫'  and sys_standard = '"+gors+"' GROUP BY    v4                )a             RIGHT JOIN(                SELECT v4 as  v3, count(*)AS count_16_hu_yes, sum(v9)AS sum_16_person_yes                FROM da_household_2016                WHERE v21 = '已脱贫'                AND entry_year = '2016' and sys_standard = '"+gors+"'                GROUP BY v4             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_yes, sum_17_person_yes, count_16_hu_yes, sum_16_person_yes                FROM (    SELECT       v4 as  v3,       count(*)AS count_17_hu_yes,       sum(v9)AS sum_17_person_yes    FROM       da_household    WHERE       v21 = '已脱贫' and sys_standard = '"+gors+"'    GROUP BY       v4)a                LEFT JOIN( SELECT    v4 as  v3,    count(*)AS count_16_hu_yes,    sum(v9)AS sum_16_person_yes FROM    da_household_2016 WHERE    v21 = '已脱贫' AND entry_year = '2016' and sys_standard = '"+gors+"' GROUP BY    v4                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )g ON f.com_name = g.com_name    )B WHERE    A.com_name = B.com_name ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				/*SQLAdapter sqlAdapter2 =new SQLAdapter(sql2);
				List<Map> sql_list2 = this.getBySqlMapper.findRecords(sqlAdapter2);*/
				
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					JSONObject val = new JSONObject();
					for(int i = 0;i<sql_list.size();i++){
						val.put("v3","".equals(sql_list.get(i).get("com_name")) || sql_list.get(i).get("com_name") == null ?"": sql_list.get(i).get("com_name").toString());
						val.put("count","".equals(sql_list.get(i).get("count")) || sql_list.get(i).get("count") == null ?"0": sql_list.get(i).get("count").toString());
						val.put("count1","".equals(sql_list.get(i).get("count1")) || sql_list.get(i).get("count1") == null ?"0": sql_list.get(i).get("count1").toString());
						val.put("sum","".equals(sql_list.get(i).get("sum")) || sql_list.get(i).get("sum") == null ?"0": sql_list.get(i).get("sum").toString());
						val.put("sum1","".equals(sql_list.get(i).get("sum1")) || sql_list.get(i).get("sum1") == null ?"0": sql_list.get(i).get("sum1").toString());
						val.put("sum2","".equals(sql_list.get(i).get("sum2")) || sql_list.get(i).get("sum2") == null ?"0": sql_list.get(i).get("sum2").toString());
						val.put("sum3","".equals(sql_list.get(i).get("sum3")) || sql_list.get(i).get("sum3") == null ?"0": sql_list.get(i).get("sum3").toString());
						val.put("count_17_hu","".equals(sql_list.get(i).get("count_17_hu")) || sql_list.get(i).get("count_17_hu") == null ?"0": sql_list.get(i).get("count_17_hu").toString());
						val.put("sum_17_person","".equals(sql_list.get(i).get("sum_17_person")) || sql_list.get(i).get("sum_17_person") == null ?"0": sql_list.get(i).get("sum_17_person").toString());
						val.put("count_16_hu","".equals(sql_list.get(i).get("count_16_hu")) || sql_list.get(i).get("count_16_hu") == null ?"0": sql_list.get(i).get("count_16_hu").toString());
						val.put("sum_16_person","".equals(sql_list.get(i).get("sum_16_person")) || sql_list.get(i).get("sum_16_person") == null ?"0": sql_list.get(i).get("sum_16_person").toString());
						val.put("count_17_hu_no","".equals(sql_list.get(i).get("count_17_hu_no")) || sql_list.get(i).get("count_17_hu_no") == null ?"0": sql_list.get(i).get("count_17_hu_no").toString());
						val.put("sum_17_person_no","".equals(sql_list.get(i).get("sum_17_person_no")) || sql_list.get(i).get("sum_17_person_no") == null ?"0": sql_list.get(i).get("sum_17_person_no").toString());
						val.put("count_16_hu_no","".equals(sql_list.get(i).get("count_16_hu_no")) || sql_list.get(i).get("count_16_hu_no") == null ?"0": sql_list.get(i).get("count_16_hu_no").toString());
						val.put("sum_16_person_no","".equals(sql_list.get(i).get("sum_16_person_no")) || sql_list.get(i).get("sum_16_person_no") == null ?"0": sql_list.get(i).get("sum_16_person_no").toString());
						val.put("count_17_hu_yes","".equals(sql_list.get(i).get("count_17_hu_yes")) || sql_list.get(i).get("count_17_hu_yes") == null ?"0": sql_list.get(i).get("count_17_hu_yes").toString());
						val.put("sum_17_person_yes","".equals(sql_list.get(i).get("sum_17_person_yes")) || sql_list.get(i).get("sum_17_person_yes") == null ?"0": sql_list.get(i).get("sum_17_person_yes").toString());
						val.put("count_16_hu_yes","".equals(sql_list.get(i).get("count_16_hu_yes")) || sql_list.get(i).get("count_16_hu_yes") == null ?"0": sql_list.get(i).get("count_16_hu_yes").toString());
						val.put("sum_16_person_yes","".equals(sql_list.get(i).get("sum_16_person_yes")) || sql_list.get(i).get("sum_16_person_yes") == null ?"0": sql_list.get(i).get("sum_16_person_yes").toString());

						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else if(company_json.get("com_level").toString().equals("3")==true){

			/*	String sql =" select * from  ( select * from (select com_name from sys_company s where s.com_f_pkid ="+pkid+" )a  LEFT JOIN  ( SELECT          b.v3,          count,          count1,          sum,          sum1       FROM          (             SELECT                y1.v5 as v3,      "
						+ "          count(*)AS count,                sum(v9)AS sum             FROM                da_household y1             JOIN sys_company y2 ON y1.v4 = y2.com_name             WHERE                y2.com_code = '"+code+"'              and init_flag = '"+gors+"'          "
						+ "   AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY                y1.v5          )a       RIGHT JOIN(          SELECT            y1.v5 as v3,             count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016 y1     "
						+ "     JOIN sys_company y2 ON y1.v4 = y2.com_name          WHERE             y2.com_code = '"+code+"'          and             init_flag = '"+gors+"'          AND v21 != '已脱贫'  AND entry_year = '2016'        GROUP BY             y1.v5       )b ON a.v3 = b.v3   UNION  SELECT          a.v3,      "
						+ "    count,          count1,          sum,          sum1       FROM          (             SELECT                y1.v5 as v3,                count(*)AS count,                sum(v9)AS sum             FROM                da_household y1 					JOIN sys_company y2 ON y1.v4 = y2.com_name 		"
						+ "				WHERE             y2.com_code = '"+code+"'             and                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY               y1.v5          )a       left JOIN(          SELECT              y1.v5 as v3,        "
						+ "     count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016 y1          JOIN sys_company y2 ON y1.v4 = y2.com_name          WHERE             y2.com_code = '"+code+"' 					and             init_flag = '"+gors+"'          AND v21 != '已脱贫'   AND entry_year = '2016'  "
						+ "     GROUP BY             y1.v5        )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )e INNER JOIN ( select * from (select com_name from sys_company s where s.com_f_pkid = "+pkid+" )a  LEFT JOIN  (  SELECT       a.v3,       sum2,       sum3    FROM       (          SELECT            y1.v5 as v3,        "
						+ "     count(*)AS sum2          FROM             da_household y1             JOIN sys_company y2 ON y1.v4 = y2.com_name             WHERE                y2.com_code = '"+code+"' 					and 						sys_standard = '"+gors+"'          AND v21 = '已脱贫'      "
						+ "    AND entry_year = '2017'          GROUP BY             y1.v5       )a    LEFT JOIN(       SELECT          y1.v5 as v3,          count(*)AS sum3       FROM          da_household_2016 y1       JOIN sys_company y2 ON y1.v4 = y2.com_name           WHERE             y2.com_code = '"+code+"'     "
						+ "  and          sys_standard = '"+gors+"'       AND v21 = '已脱贫'  AND entry_year = '2016'     GROUP BY          y1.v5     )b ON a.v3 = b.v3      UNION        SELECT          b.v3,          sum2,          sum3       FROM          (             SELECT              y1.v5 as v3,             count(*)AS sum2          FROM        "
						+ "     da_household y1             JOIN sys_company y2 ON y1.v4 = y2.com_name             WHERE                y2.com_code = '"+code+"' 					and 						sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2017'          GROUP BY             y1.v5          )a   "
						+ "    RIGHT JOIN(          SELECT             y1.v5 as v3,          count(*)AS sum3       FROM          da_household_2016 y1       JOIN sys_company y2 ON y1.v4 = y2.com_name           WHERE             y2.com_code = '"+code+"'       and          sys_standard = '"+gors+"'       AND v21 = '已脱贫'    AND entry_year = '2016'   GROUP BY  "
						+ "        y1.v5        )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )f on e.com_name = f.com_name ";*/
				/*String sql = " SELECT B.*, A.count1, count,  sum,       sum1, sum2,sum3 FROM (SELECT    e.com_name,count,             count1,             sum,             sum1,  						sum2,             sum3 FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM           "
						+ "     sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count,             count1,             sum,             sum1          FROM             (                SELECT                v5 as  v3,                   count(*)AS count,         "
						+ "          sum(v9)AS sum                FROM                   da_household                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                             GROUP BY                   v5             )a          RIGHT JOIN(             SELECT             "
						+ "   v5 as  v3,                count(*)AS count1,                sum(v9)AS sum1             FROM                da_household_2016             WHERE                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3          UNION    "
						+ "         SELECT                a.v3,                count,                count1,                sum,                sum1             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count,                      sum(v9)AS sum                   FROM                      da_household        "
						+ "           WHERE                      init_flag = '"+gors+"'                   AND v21 != '已脱贫'                                   GROUP BY                      v5                )a             LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count1,                   sum(v9)AS sum1      "
						+ "          FROM                   da_household_2016                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                AND entry_year = '2016'                GROUP BY                   v5             )b ON a.v3 = b.v3       )d ON a.com_name = d.v3    )e INNER JOIN(    SELECT       *    FROM       (     "
						+ "     SELECT     "
						+ "        com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a    LEFT JOIN(       SELECT          a.v3,          sum2,          sum3       FROM          (             SELECT                v5 as  v3,                count(*)AS sum2             FROM                da_household             WHERE                sys_standard = '"+gors+"'   "
						+ "          AND v21 = '已脱贫'                     GROUP BY                v5          )a       LEFT JOIN(          SELECT             v5 as  v3,             count(*)AS sum3          FROM             da_household_2016          WHERE             sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2016'    "
						+ "      GROUP BY             v5      )b ON a.v3 = b.v3    "
						+ "   UNION          SELECT             b.v3,             sum2,             sum3          FROM             (                SELECT                   v5 as  v3,                   count(*)AS sum2                FROM                   da_household                WHERE                   sys_standard = '"+gors+"'                AND v21 = '已脱贫'                  "
						+ "              GROUP BY                   v5           )a          RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS sum3             FROM                da_household_2016             WHERE                sys_standard = '"+gors+"'             AND v21 = '已脱贫'             AND entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3  "
						+ "  )d ON a.com_name = d.v3 )f ON e.com_name = f.com_name )A ,  ( SELECT    e.com_name ,count_17_hu,sum_17_person,count_16_hu,sum_16_person, count_17_hu_no,sum_17_person_no,count_16_hu_no,sum_16_person_no, count_17_hu_yes, sum_17_person_yes,count_16_hu_yes, sum_16_person_yes "+
						" FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM                sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count_17_hu,             sum_17_person,             count_16_hu,             sum_16_person          FROM             (       "
						+ "         SELECT                   v5 as  v3,                   count(*)AS count_17_hu,                   sum(v9)AS sum_17_person                FROM                   da_household                               GROUP BY                   v5            )a          RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS count_16_hu,        "
						+ "        sum(v9)AS sum_16_person             FROM                da_household_2016             WHERE             entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu, 							sum_17_person, 							count_16_hu, 						"
						+ "	sum_16_person             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count_17_hu,                      sum(v9)AS sum_17_person                   FROM                      da_household                             GROUP BY                      v5                )a         "
						+ "    LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count_16_hu,                   sum(v9)AS sum_16_person                FROM                   da_household_2016                WHERE               entry_year = '2016'                GROUP BY                   v5            )b ON a.v3 = b.v3 					       )d ON a.com_name = d.v3    )e INNER JOIN (  "
						+ "  SELECT       *    FROM       (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_no,             sum_17_person_no,             count_16_hu_no,             sum_16_person_no          FROM             (                SELECT                   v5 as  v3,   "
						+ "                count(*)AS count_17_hu_no,                   sum(v9)AS sum_17_person_no                FROM                   da_household                WHERE 							v21 != '已脱贫'							                GROUP BY                   v5            )a          RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS count_16_hu_no,  "
						+ "              sum(v9)AS sum_16_person_no             FROM                da_household_2016             WHERE 						v21 != '已脱贫' and             entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu_no, 							sum_17_person_no, 			"
						+ "			count_16_hu_no, 							sum_16_person_no             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count_17_hu_no,                      sum(v9)AS sum_17_person_no                   FROM                      da_household                   WHERE 										v21 != '已脱贫'       "
						+ "             GROUP BY                      v5                )a             LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count_16_hu_no,                   sum(v9)AS sum_16_person_no                FROM                   da_household_2016                WHERE 								v21 != '已脱贫' and               entry_year = '2016'                GROUP BY                   v5           )b ON a.v3 = b.v3 	"
						+ "				       )d ON a.com_name = d.v3  )f ON e.com_name = f.com_name  INNER "+
						" JOIN (    SELECT          com_name , count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes from        (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,         "
						+ "    sum_16_person_yes          FROM             (                SELECT                   v5 as  v3,                   count(*)AS count_17_hu_yes,                   sum(v9)AS sum_17_person_yes                FROM                   da_household                WHERE 							v21 = '已脱贫'  		           GROUP BY                   v5             )a   "
						+ "       RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS count_16_hu_yes,                sum(v9)AS sum_16_person_yes             FROM                da_household_2016             WHERE 						v21 = '已脱贫' and             entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3      union 				"
						+ "		SELECT                a.v3,                 count_17_hu_yes, 							sum_17_person_yes, 							count_16_hu_yes, 							sum_16_person_yes             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count_17_hu_yes,                      sum(v9)AS sum_17_person_yes       "
						+ "            FROM                      da_household                   WHERE 										v21 = '已脱贫'                  GROUP BY                      v5               )a             LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count_16_hu_yes,                   sum(v9)AS sum_16_person_yes    "
						+ "            FROM                   da_household_2016                WHERE 								v21 = '已脱贫' and               entry_year = '2016'                GROUP BY                   v5            )b ON a.v3 = b.v3 					 )d ON a.com_name = d.v3 )g  on f.com_name = g.com_name )B WHERE A.com_name = B.com_name ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);*/
				String sql = "SELECT    B.*, A.count1,    count,    sum,    sum1,    sum2,    sum3 FROM    (       SELECT          e.com_name,          count,          count1,          sum,          sum1,          sum2,          sum3       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE    s.com_f_pkid =  "+pkid+"                )a  "
						+ "           LEFT JOIN(                SELECT b.v3, count, count1, sum, sum1                FROM (    SELECT       v5 as  v3,       count(*)AS count,       sum(v9)AS sum    FROM       da_household    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    GROUP BY       v5 )a                RIGHT JOIN( SELECT    v5 as  v3,    count(*)AS count1,    sum(v9)AS sum1 FROM    da_household_2016 WHERE    init_flag = '"+gors+"' AND v21 != '已脱贫' AND entry_year = '2016' GROUP BY    v5                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count,    count1,    sum,    sum1 FROM    (       SELECT  v5 as  v3,  count(*)AS count,  sum(v9)AS sum       FROM  da_household       WHERE  init_flag = '"+gors+"'       AND v21 != '已脱贫'       GROUP BY  v5    )a LEFT JOIN(    SELECT       v5 as  v3,       count(*)AS count1,       sum(v9)AS sum1    FROM       da_household_2016    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    AND entry_year = '2016'    GROUP BY       v5 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s               "
								+ " WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                a.v3,                sum2,                sum3             FROM                ( SELECT    v5 as  v3,    count(*)AS sum2 FROM    da_household WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' GROUP BY    v5               )a             LEFT JOIN(                SELECT v5 as  v3, count(*)AS sum3                FROM da_household_2016                WHERE sys_standard = '"+gors+"'                AND v21 = '已脱贫'                AND entry_year = '2016'                GROUP BY v5             )b ON a.v3 = b.v3             UNION                SELECT b.v3, sum2, sum3                FROM (    SELECT       v5 as  v3,       count(*)AS sum2    FROM       da_household    WHERE       sys_standard = '"+gors+"'    AND v21 = '已脱贫'    GROUP BY       v5 )a                RIGHT JOIN( SELECT    v5 as  v3,    count(*)AS sum3 FROM    da_household_2016 WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' AND entry_year = '2016' GROUP BY    v5                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name    )A,    (       SELECT          e.com_name,          count_17_hu,          sum_17_person,          count_16_hu,          sum_16_person,          count_17_hu_no,          sum_17_person_no,          count_16_hu_no,          sum_16_person_no,          count_17_hu_yes,          sum_17_person_yes,          count_16_hu_yes,          sum_16_person_yes       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE"
										+ "    s.com_f_pkid =  "+pkid+"                )a             LEFT JOIN(                SELECT b.v3, count_17_hu, sum_17_person, count_16_hu, sum_16_person                FROM (    SELECT       v5 as  v3,       count(*)AS count_17_hu,       sum(v9)AS sum_17_person    FROM       da_household	where sys_standard = '"+gors+"'	    GROUP BY       v5 )a                RIGHT JOIN( SELECT    v5 as  v3,    count(*)AS count_16_hu,    sum(v9)AS sum_16_person FROM    da_household_2016 WHERE    entry_year = '2016' 	and sys_standard = '"+gors+"' GROUP BY    v5                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count_17_hu,    sum_17_person,    count_16_hu,    sum_16_person FROM    (       SELECT  v5 as  v3,  count(*)AS count_17_hu,  sum(v9)AS sum_17_person       FROM  da_household 														WHERE sys_standard = '"+gors+"' GROUP BY  v5    )a LEFT JOIN(    SELECT       v5 as  v3,       count(*)AS count_16_hu,       sum(v9)AS sum_16_person    FROM       da_household_2016    WHERE       entry_year = '2016' 	and sys_standard = '"+gors+"'    GROUP BY       v5 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s       "
												+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_no,                sum_17_person_no,                count_16_hu_no,                sum_16_person_no             FROM                ( SELECT    v5 as  v3,    count(*)AS count_17_hu_no,    sum(v9)AS sum_17_person_no FROM    da_household WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' GROUP BY    v5                )a             RIGHT JOIN(                SELECT v5 as  v3, count(*)AS count_16_hu_no, sum(v9)AS sum_16_person_no                FROM da_household_2016                WHERE v21 != '已脱贫'  and init_flag = '"+gors+"'                AND entry_year = '2016'                GROUP BY v5             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_no, sum_17_person_no, count_16_hu_no, sum_16_person_no                FROM (    SELECT       v5 as  v3,       count(*)AS count_17_hu_no,       sum(v9)AS sum_17_person_no    FROM       da_household    WHERE       v21 != '已脱贫'  and init_flag = '"+gors+"'    GROUP BY       v5 )a                LEFT JOIN( SELECT    v5 as  v3,    count(*)AS count_16_hu_no,    sum(v9)AS sum_16_person_no FROM    da_household_2016 WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' AND entry_year = '2016' GROUP BY    v5                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name       INNER JOIN(          SELECT             com_name,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes          FROM             (                SELECT com_name                FROM sys_company s       "
														+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_yes,                sum_17_person_yes,                count_16_hu_yes,                sum_16_person_yes             FROM                ( SELECT    v5 as  v3,    count(*)AS count_17_hu_yes,    sum(v9)AS sum_17_person_yes FROM    da_household WHERE    v21 = '已脱贫'  and sys_standard = '"+gors+"' GROUP BY    v5                )a             RIGHT JOIN(                SELECT v5 as  v3, count(*)AS count_16_hu_yes, sum(v9)AS sum_16_person_yes                FROM da_household_2016                WHERE v21 = '已脱贫'                AND entry_year = '2016' and sys_standard = '"+gors+"'                GROUP BY v5             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_yes, sum_17_person_yes, count_16_hu_yes, sum_16_person_yes                FROM (    SELECT       v5 as  v3,       count(*)AS count_17_hu_yes,       sum(v9)AS sum_17_person_yes    FROM       da_household    WHERE       v21 = '已脱贫' and sys_standard = '"+gors+"'    GROUP BY       v5)a                LEFT JOIN( SELECT    v5 as  v3,    count(*)AS count_16_hu_yes,    sum(v9)AS sum_16_person_yes FROM    da_household_2016 WHERE    v21 = '已脱贫' AND entry_year = '2016' and sys_standard = '"+gors+"' GROUP BY    v5                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )g ON f.com_name = g.com_name    )B WHERE    A.com_name = B.com_name ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				/*SQLAdapter sqlAdapter2 =new SQLAdapter(sql2);
				List<Map> sql_list2 = this.getBySqlMapper.findRecords(sqlAdapter2);*/
				
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					JSONObject val = new JSONObject();
					for(int i = 0;i<sql_list.size();i++){
						val.put("v3","".equals(sql_list.get(i).get("com_name")) || sql_list.get(i).get("com_name") == null ?"": sql_list.get(i).get("com_name").toString());
						val.put("count","".equals(sql_list.get(i).get("count")) || sql_list.get(i).get("count") == null ?"0": sql_list.get(i).get("count").toString());
						val.put("count1","".equals(sql_list.get(i).get("count1")) || sql_list.get(i).get("count1") == null ?"0": sql_list.get(i).get("count1").toString());
						val.put("sum","".equals(sql_list.get(i).get("sum")) || sql_list.get(i).get("sum") == null ?"0": sql_list.get(i).get("sum").toString());
						val.put("sum1","".equals(sql_list.get(i).get("sum1")) || sql_list.get(i).get("sum1") == null ?"0": sql_list.get(i).get("sum1").toString());
						val.put("sum2","".equals(sql_list.get(i).get("sum2")) || sql_list.get(i).get("sum2") == null ?"0": sql_list.get(i).get("sum2").toString());
						val.put("sum3","".equals(sql_list.get(i).get("sum3")) || sql_list.get(i).get("sum3") == null ?"0": sql_list.get(i).get("sum3").toString());
						val.put("count_17_hu","".equals(sql_list.get(i).get("count_17_hu")) || sql_list.get(i).get("count_17_hu") == null ?"0": sql_list.get(i).get("count_17_hu").toString());
						val.put("sum_17_person","".equals(sql_list.get(i).get("sum_17_person")) || sql_list.get(i).get("sum_17_person") == null ?"0": sql_list.get(i).get("sum_17_person").toString());
						val.put("count_16_hu","".equals(sql_list.get(i).get("count_16_hu")) || sql_list.get(i).get("count_16_hu") == null ?"0": sql_list.get(i).get("count_16_hu").toString());
						val.put("sum_16_person","".equals(sql_list.get(i).get("sum_16_person")) || sql_list.get(i).get("sum_16_person") == null ?"0": sql_list.get(i).get("sum_16_person").toString());
						val.put("count_17_hu_no","".equals(sql_list.get(i).get("count_17_hu_no")) || sql_list.get(i).get("count_17_hu_no") == null ?"0": sql_list.get(i).get("count_17_hu_no").toString());
						val.put("sum_17_person_no","".equals(sql_list.get(i).get("sum_17_person_no")) || sql_list.get(i).get("sum_17_person_no") == null ?"0": sql_list.get(i).get("sum_17_person_no").toString());
						val.put("count_16_hu_no","".equals(sql_list.get(i).get("count_16_hu_no")) || sql_list.get(i).get("count_16_hu_no") == null ?"0": sql_list.get(i).get("count_16_hu_no").toString());
						val.put("sum_16_person_no","".equals(sql_list.get(i).get("sum_16_person_no")) || sql_list.get(i).get("sum_16_person_no") == null ?"0": sql_list.get(i).get("sum_16_person_no").toString());
						val.put("count_17_hu_yes","".equals(sql_list.get(i).get("count_17_hu_yes")) || sql_list.get(i).get("count_17_hu_yes") == null ?"0": sql_list.get(i).get("count_17_hu_yes").toString());
						val.put("sum_17_person_yes","".equals(sql_list.get(i).get("sum_17_person_yes")) || sql_list.get(i).get("sum_17_person_yes") == null ?"0": sql_list.get(i).get("sum_17_person_yes").toString());
						val.put("count_16_hu_yes","".equals(sql_list.get(i).get("count_16_hu_yes")) || sql_list.get(i).get("count_16_hu_yes") == null ?"0": sql_list.get(i).get("count_16_hu_yes").toString());
						val.put("sum_16_person_yes","".equals(sql_list.get(i).get("sum_16_person_yes")) || sql_list.get(i).get("sum_16_person_yes") == null ?"0": sql_list.get(i).get("sum_16_person_yes").toString());

						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else if(company_json.get("com_level").toString().equals("4")==true){
				pkid = company_json.getString("com_f_pkid");
			/*	
				String sql =" select * from  ( select * from (select com_name from sys_company s where s.com_f_pkid ="+pkid+" )a  LEFT JOIN  ( SELECT          b.v3,          count,          count1,          sum,          sum1       FROM          (             SELECT                y1.v5 as v3,      "
						+ "          count(*)AS count,                sum(v9)AS sum             FROM                da_household y1             JOIN sys_company y2 ON y1.v4 = y2.com_name             WHERE                y2.com_code = '"+code+"'              and init_flag = '"+gors+"'          "
						+ "   AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY                y1.v5          )a       RIGHT JOIN(          SELECT            y1.v5 as v3,             count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016 y1     "
						+ "     JOIN sys_company y2 ON y1.v4 = y2.com_name          WHERE             y2.com_code = '"+code+"'          and             init_flag = '"+gors+"'          AND v21 != '已脱贫'    AND entry_year = '2016'      GROUP BY             y1.v5       )b ON a.v3 = b.v3   UNION  SELECT          a.v3,      "
						+ "    count,          count1,          sum,          sum1       FROM          (             SELECT                y1.v5 as v3,                count(*)AS count,                sum(v9)AS sum             FROM                da_household y1 					JOIN sys_company y2 ON y1.v4 = y2.com_name 		"
						+ "				WHERE             y2.com_code = '"+code+"'             and                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2017'             GROUP BY               y1.v5          )a       left JOIN(          SELECT              y1.v5 as v3,        "
						+ "     count(*)AS count1,             sum(v9)AS sum1          FROM             da_household_2016 y1          JOIN sys_company y2 ON y1.v4 = y2.com_name          WHERE             y2.com_code = '"+code+"' 					and             init_flag = '"+gors+"'          AND v21 != '已脱贫'  AND entry_year = '2016'   "
						+ "     GROUP BY             y1.v5        )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )e INNER JOIN ( select * from (select com_name from sys_company s where s.com_f_pkid = "+pkid+" )a  LEFT JOIN  (  SELECT       a.v3,       sum2,       sum3    FROM       (          SELECT            y1.v5 as v3,        "
						+ "     count(*)AS sum2          FROM             da_household y1             JOIN sys_company y2 ON y1.v4 = y2.com_name             WHERE                y2.com_code = '"+code+"' 					and 						sys_standard = '"+gors+"'          AND v21 = '已脱贫'      "
						+ "    AND entry_year = '2017'          GROUP BY             y1.v5       )a    LEFT JOIN(       SELECT          y1.v5 as v3,          count(*)AS sum3       FROM          da_household_2016 y1       JOIN sys_company y2 ON y1.v4 = y2.com_name           WHERE             y2.com_code = '"+code+"'     "
						+ "  and          sys_standard = '"+gors+"'       AND v21 = '已脱贫'   AND entry_year = '2016'    GROUP BY          y1.v5     )b ON a.v3 = b.v3      UNION        SELECT          b.v3,          sum2,          sum3       FROM          (             SELECT              y1.v5 as v3,             count(*)AS sum2          FROM        "
						+ "     da_household y1             JOIN sys_company y2 ON y1.v4 = y2.com_name             WHERE                y2.com_code = '"+code+"' 					and 						sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2017'          GROUP BY             y1.v5          )a   "
						+ "    RIGHT JOIN(          SELECT             y1.v5 as v3,          count(*)AS sum3       FROM          da_household_2016 y1       JOIN sys_company y2 ON y1.v4 = y2.com_name           WHERE             y2.com_code = '"+code+"'       and          sys_standard = '"+gors+"'       AND v21 = '已脱贫'   AND entry_year = '2016'    GROUP BY  "
						+ "        y1.v5        )b ON a.v3 = b.v3  )d on a.com_name = d.v3 )f on e.com_name = f.com_name ";*/
				/*String sql = " SELECT B.*, A.count1, count,  sum,       sum1, sum2,sum3 FROM (SELECT    e.com_name,count,             count1,             sum,             sum1,  						sum2,             sum3 FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM           "
						+ "     sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count,             count1,             sum,             sum1          FROM             (                SELECT                v5 as  v3,                   count(*)AS count,         "
						+ "          sum(v9)AS sum                FROM                   da_household                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                           GROUP BY                   v5             )a          RIGHT JOIN(             SELECT             "
						+ "   v5 as  v3,                count(*)AS count1,                sum(v9)AS sum1             FROM                da_household_2016             WHERE                init_flag = '"+gors+"'             AND v21 != '已脱贫'             AND entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3          UNION    "
						+ "         SELECT                a.v3,                count,                count1,                sum,                sum1             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count,                      sum(v9)AS sum                   FROM                      da_household        "
						+ "           WHERE                      init_flag = '"+gors+"'                   AND v21 != '已脱贫'                                    GROUP BY                      v5                )a             LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count1,                   sum(v9)AS sum1      "
						+ "          FROM                   da_household_2016                WHERE                   init_flag = '"+gors+"'                AND v21 != '已脱贫'                AND entry_year = '2016'                GROUP BY                   v5             )b ON a.v3 = b.v3       )d ON a.com_name = d.v3    )e INNER JOIN(    SELECT       *    FROM       (     "
						+ "     SELECT     "
						+ "        com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a    LEFT JOIN(       SELECT          a.v3,          sum2,          sum3       FROM          (             SELECT                v5 as  v3,                count(*)AS sum2             FROM                da_household             WHERE                sys_standard = '"+gors+"'   "
						+ "          AND v21 = '已脱贫'                        GROUP BY                v5          )a       LEFT JOIN(          SELECT             v5 as  v3,             count(*)AS sum3          FROM             da_household_2016          WHERE             sys_standard = '"+gors+"'          AND v21 = '已脱贫'          AND entry_year = '2016'    "
						+ "      GROUP BY             v5      )b ON a.v3 = b.v3    "
						+ "   UNION          SELECT             b.v3,             sum2,             sum3          FROM             (                SELECT                   v5 as  v3,                   count(*)AS sum2                FROM                   da_household                WHERE                   sys_standard = '"+gors+"'                AND v21 = '已脱贫'                 "
						+ "              GROUP BY                   v5           )a          RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS sum3             FROM                da_household_2016             WHERE                sys_standard = '"+gors+"'             AND v21 = '已脱贫'             AND entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3  "
						+ "  )d ON a.com_name = d.v3 )f ON e.com_name = f.com_name )A ,  ( SELECT    e.com_name ,count_17_hu,sum_17_person,count_16_hu,sum_16_person, count_17_hu_no,sum_17_person_no,count_16_hu_no,sum_16_person_no, count_17_hu_yes, sum_17_person_yes,count_16_hu_yes, sum_16_person_yes "+
						" FROM    (       SELECT          *       FROM          (             SELECT                com_name             FROM                sys_company s             WHERE                s.com_f_pkid = "+pkid+"          )a       LEFT JOIN(          SELECT             b.v3,             count_17_hu,             sum_17_person,             count_16_hu,             sum_16_person          FROM             (       "
						+ "         SELECT                   v5 as  v3,                   count(*)AS count_17_hu,                   sum(v9)AS sum_17_person                FROM                   da_household                             GROUP BY                   v5            )a          RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS count_16_hu,        "
						+ "        sum(v9)AS sum_16_person             FROM                da_household_2016             WHERE             entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu, 							sum_17_person, 							count_16_hu, 						"
						+ "	sum_16_person             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count_17_hu,                      sum(v9)AS sum_17_person                   FROM                      da_household                                      GROUP BY                      v5                )a         "
						+ "    LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count_16_hu,                   sum(v9)AS sum_16_person                FROM                   da_household_2016                WHERE               entry_year = '2016'                GROUP BY                   v5            )b ON a.v3 = b.v3 					       )d ON a.com_name = d.v3    )e INNER JOIN (  "
						+ "  SELECT       *    FROM       (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_no,             sum_17_person_no,             count_16_hu_no,             sum_16_person_no          FROM             (                SELECT                   v5 as  v3,   "
						+ "                count(*)AS count_17_hu_no,                   sum(v9)AS sum_17_person_no                FROM                   da_household                WHERE 							v21 != '已脱贫' 						                GROUP BY                   v5            )a          RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS count_16_hu_no,  "
						+ "              sum(v9)AS sum_16_person_no             FROM                da_household_2016             WHERE 						v21 != '已脱贫' and             entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3      union 						SELECT                a.v3,                 count_17_hu_no, 							sum_17_person_no, 			"
						+ "			count_16_hu_no, 							sum_16_person_no             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count_17_hu_no,                      sum(v9)AS sum_17_person_no                   FROM                      da_household                   WHERE 										v21 != '已脱贫'      "
						+ "             GROUP BY                      v5                )a             LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count_16_hu_no,                   sum(v9)AS sum_16_person_no                FROM                   da_household_2016                WHERE 								v21 != '已脱贫' and               entry_year = '2016'                GROUP BY                   v5           )b ON a.v3 = b.v3 	"
						+ "				       )d ON a.com_name = d.v3  )f ON e.com_name = f.com_name  INNER "+
						" JOIN (    SELECT          com_name , count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes from        (          SELECT             com_name          FROM             sys_company s          WHERE             s.com_f_pkid = "+pkid+"       )a     LEFT JOIN(          SELECT             b.v3,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,         "
						+ "    sum_16_person_yes          FROM             (                SELECT                   v5 as  v3,                   count(*)AS count_17_hu_yes,                   sum(v9)AS sum_17_person_yes                FROM                   da_household                WHERE 							v21 = '已脱贫'            GROUP BY                   v5             )a   "
						+ "       RIGHT JOIN(             SELECT                v5 as  v3,                count(*)AS count_16_hu_yes,                sum(v9)AS sum_16_person_yes             FROM                da_household_2016             WHERE 						v21 = '已脱贫' and             entry_year = '2016'             GROUP BY                v5          )b ON a.v3 = b.v3      union 				"
						+ "		SELECT                a.v3,                 count_17_hu_yes, 							sum_17_person_yes, 							count_16_hu_yes, 							sum_16_person_yes             FROM                (                   SELECT                      v5 as  v3,                      count(*)AS count_17_hu_yes,                      sum(v9)AS sum_17_person_yes       "
						+ "            FROM                      da_household                   WHERE 										v21 = '已脱贫'                  GROUP BY                      v5               )a             LEFT JOIN(                SELECT                   v5 as  v3,                   count(*)AS count_16_hu_yes,                   sum(v9)AS sum_16_person_yes    "
						+ "            FROM                   da_household_2016                WHERE 								v21 = '已脱贫' and               entry_year = '2016'                GROUP BY                   v5            )b ON a.v3 = b.v3 					 )d ON a.com_name = d.v3 )g  on f.com_name = g.com_name )B WHERE A.com_name = B.com_name ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);*/
				String sql = "SELECT    B.*, A.count1,    count,    sum,    sum1,    sum2,    sum3 FROM    (       SELECT          e.com_name,          count,          count1,          sum,          sum1,          sum2,          sum3       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE    s.com_f_pkid =  "+pkid+"                )a  "
						+ "           LEFT JOIN(                SELECT b.v3, count, count1, sum, sum1                FROM (    SELECT       v5 as  v3,       count(*)AS count,       sum(v9)AS sum    FROM       da_household    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    GROUP BY       v5 )a                RIGHT JOIN( SELECT    v5 as  v3,    count(*)AS count1,    sum(v9)AS sum1 FROM    da_household_2016 WHERE    init_flag = '"+gors+"' AND v21 != '已脱贫' AND entry_year = '2016' GROUP BY    v5                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count,    count1,    sum,    sum1 FROM    (       SELECT  v5 as  v3,  count(*)AS count,  sum(v9)AS sum       FROM  da_household       WHERE  init_flag = '"+gors+"'       AND v21 != '已脱贫'       GROUP BY  v5    )a LEFT JOIN(    SELECT       v5 as  v3,       count(*)AS count1,       sum(v9)AS sum1    FROM       da_household_2016    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    AND entry_year = '2016'    GROUP BY       v5 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s               "
								+ " WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                a.v3,                sum2,                sum3             FROM                ( SELECT    v5 as  v3,    count(*)AS sum2 FROM    da_household WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' GROUP BY    v5               )a             LEFT JOIN(                SELECT v5 as  v3, count(*)AS sum3                FROM da_household_2016                WHERE sys_standard = '"+gors+"'                AND v21 = '已脱贫'                AND entry_year = '2016'                GROUP BY v5             )b ON a.v3 = b.v3             UNION                SELECT b.v3, sum2, sum3                FROM (    SELECT       v5 as  v3,       count(*)AS sum2    FROM       da_household    WHERE       sys_standard = '"+gors+"'    AND v21 = '已脱贫'    GROUP BY       v5 )a                RIGHT JOIN( SELECT    v5 as  v3,    count(*)AS sum3 FROM    da_household_2016 WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' AND entry_year = '2016' GROUP BY    v5                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name    )A,    (       SELECT          e.com_name,          count_17_hu,          sum_17_person,          count_16_hu,          sum_16_person,          count_17_hu_no,          sum_17_person_no,          count_16_hu_no,          sum_16_person_no,          count_17_hu_yes,          sum_17_person_yes,          count_16_hu_yes,          sum_16_person_yes       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE"
										+ "    s.com_f_pkid =  "+pkid+"                )a             LEFT JOIN(                SELECT b.v3, count_17_hu, sum_17_person, count_16_hu, sum_16_person                FROM (    SELECT       v5 as  v3,       count(*)AS count_17_hu,       sum(v9)AS sum_17_person    FROM       da_household	where sys_standard = '"+gors+"'	    GROUP BY       v5 )a                RIGHT JOIN( SELECT    v5 as  v3,    count(*)AS count_16_hu,    sum(v9)AS sum_16_person FROM    da_household_2016 WHERE    entry_year = '2016' 	and sys_standard = '"+gors+"' GROUP BY    v5                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count_17_hu,    sum_17_person,    count_16_hu,    sum_16_person FROM    (       SELECT  v5 as  v3,  count(*)AS count_17_hu,  sum(v9)AS sum_17_person       FROM  da_household 														WHERE sys_standard = '"+gors+"' GROUP BY  v5    )a LEFT JOIN(    SELECT       v5 as  v3,       count(*)AS count_16_hu,       sum(v9)AS sum_16_person    FROM       da_household_2016    WHERE       entry_year = '2016' 	and sys_standard = '"+gors+"'    GROUP BY       v5 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s       "
												+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_no,                sum_17_person_no,                count_16_hu_no,                sum_16_person_no             FROM                ( SELECT    v5 as  v3,    count(*)AS count_17_hu_no,    sum(v9)AS sum_17_person_no FROM    da_household WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' GROUP BY    v5                )a             RIGHT JOIN(                SELECT v5 as  v3, count(*)AS count_16_hu_no, sum(v9)AS sum_16_person_no                FROM da_household_2016                WHERE v21 != '已脱贫'  and init_flag = '"+gors+"'                AND entry_year = '2016'                GROUP BY v5             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_no, sum_17_person_no, count_16_hu_no, sum_16_person_no                FROM (    SELECT       v5 as  v3,       count(*)AS count_17_hu_no,       sum(v9)AS sum_17_person_no    FROM       da_household    WHERE       v21 != '已脱贫'  and init_flag = '"+gors+"'    GROUP BY       v5 )a                LEFT JOIN( SELECT    v5 as  v3,    count(*)AS count_16_hu_no,    sum(v9)AS sum_16_person_no FROM    da_household_2016 WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' AND entry_year = '2016' GROUP BY    v5                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name       INNER JOIN(          SELECT             com_name,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes          FROM             (                SELECT com_name                FROM sys_company s       "
														+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_yes,                sum_17_person_yes,                count_16_hu_yes,                sum_16_person_yes             FROM                ( SELECT    v5 as  v3,    count(*)AS count_17_hu_yes,    sum(v9)AS sum_17_person_yes FROM    da_household WHERE    v21 = '已脱贫'  and sys_standard = '"+gors+"' GROUP BY    v5                )a             RIGHT JOIN(                SELECT v5 as  v3, count(*)AS count_16_hu_yes, sum(v9)AS sum_16_person_yes                FROM da_household_2016                WHERE v21 = '已脱贫'                AND entry_year = '2016' and sys_standard = '"+gors+"'                GROUP BY v5             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_yes, sum_17_person_yes, count_16_hu_yes, sum_16_person_yes                FROM (    SELECT       v5 as  v3,       count(*)AS count_17_hu_yes,       sum(v9)AS sum_17_person_yes    FROM       da_household    WHERE       v21 = '已脱贫' and sys_standard = '"+gors+"'    GROUP BY       v5)a                LEFT JOIN( SELECT    v5 as  v3,    count(*)AS count_16_hu_yes,    sum(v9)AS sum_16_person_yes FROM    da_household_2016 WHERE    v21 = '已脱贫' AND entry_year = '2016' and sys_standard = '"+gors+"' GROUP BY    v5                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )g ON f.com_name = g.com_name    )B WHERE    A.com_name = B.com_name ";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				/*SQLAdapter sqlAdapter2 =new SQLAdapter(sql2);
				List<Map> sql_list2 = this.getBySqlMapper.findRecords(sqlAdapter2);*/
				
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					JSONObject val = new JSONObject();
					for(int i = 0;i<sql_list.size();i++){
						val.put("v3","".equals(sql_list.get(i).get("com_name")) || sql_list.get(i).get("com_name") == null ?"": sql_list.get(i).get("com_name").toString());
						val.put("count","".equals(sql_list.get(i).get("count")) || sql_list.get(i).get("count") == null ?"0": sql_list.get(i).get("count").toString());
						val.put("count1","".equals(sql_list.get(i).get("count1")) || sql_list.get(i).get("count1") == null ?"0": sql_list.get(i).get("count1").toString());
						val.put("sum","".equals(sql_list.get(i).get("sum")) || sql_list.get(i).get("sum") == null ?"0": sql_list.get(i).get("sum").toString());
						val.put("sum1","".equals(sql_list.get(i).get("sum1")) || sql_list.get(i).get("sum1") == null ?"0": sql_list.get(i).get("sum1").toString());
						val.put("sum2","".equals(sql_list.get(i).get("sum2")) || sql_list.get(i).get("sum2") == null ?"0": sql_list.get(i).get("sum2").toString());
						val.put("sum3","".equals(sql_list.get(i).get("sum3")) || sql_list.get(i).get("sum3") == null ?"0": sql_list.get(i).get("sum3").toString());
						val.put("count_17_hu","".equals(sql_list.get(i).get("count_17_hu")) || sql_list.get(i).get("count_17_hu") == null ?"0": sql_list.get(i).get("count_17_hu").toString());
						val.put("sum_17_person","".equals(sql_list.get(i).get("sum_17_person")) || sql_list.get(i).get("sum_17_person") == null ?"0": sql_list.get(i).get("sum_17_person").toString());
						val.put("count_16_hu","".equals(sql_list.get(i).get("count_16_hu")) || sql_list.get(i).get("count_16_hu") == null ?"0": sql_list.get(i).get("count_16_hu").toString());
						val.put("sum_16_person","".equals(sql_list.get(i).get("sum_16_person")) || sql_list.get(i).get("sum_16_person") == null ?"0": sql_list.get(i).get("sum_16_person").toString());
						val.put("count_17_hu_no","".equals(sql_list.get(i).get("count_17_hu_no")) || sql_list.get(i).get("count_17_hu_no") == null ?"0": sql_list.get(i).get("count_17_hu_no").toString());
						val.put("sum_17_person_no","".equals(sql_list.get(i).get("sum_17_person_no")) || sql_list.get(i).get("sum_17_person_no") == null ?"0": sql_list.get(i).get("sum_17_person_no").toString());
						val.put("count_16_hu_no","".equals(sql_list.get(i).get("count_16_hu_no")) || sql_list.get(i).get("count_16_hu_no") == null ?"0": sql_list.get(i).get("count_16_hu_no").toString());
						val.put("sum_16_person_no","".equals(sql_list.get(i).get("sum_16_person_no")) || sql_list.get(i).get("sum_16_person_no") == null ?"0": sql_list.get(i).get("sum_16_person_no").toString());
						val.put("count_17_hu_yes","".equals(sql_list.get(i).get("count_17_hu_yes")) || sql_list.get(i).get("count_17_hu_yes") == null ?"0": sql_list.get(i).get("count_17_hu_yes").toString());
						val.put("sum_17_person_yes","".equals(sql_list.get(i).get("sum_17_person_yes")) || sql_list.get(i).get("sum_17_person_yes") == null ?"0": sql_list.get(i).get("sum_17_person_yes").toString());
						val.put("count_16_hu_yes","".equals(sql_list.get(i).get("count_16_hu_yes")) || sql_list.get(i).get("count_16_hu_yes") == null ?"0": sql_list.get(i).get("count_16_hu_yes").toString());
						val.put("sum_16_person_yes","".equals(sql_list.get(i).get("sum_16_person_yes")) || sql_list.get(i).get("sum_16_person_yes") == null ?"0": sql_list.get(i).get("sum_16_person_yes").toString());

						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}
		}else{//未登录
			/*2017-4-25之前更改  原查询不能查全部行政区划只能查询 有未脱贫贫困户的行政区划下的数据
			 * String sql = "SELECT  *FROM  (  SELECT     b.v3,     count,      count1,      sum,      sum1    FROM      (  SELECT          v3,          count(*)AS count,          sum(v9)AS sum        FROM "
					+ "         da_household        WHERE          sys_standard = '"+gors+"'        AND v21 != '已脱贫'   AND entry_year = '2017'     GROUP BY          v3      )a    RIGHT JOIN(      SELECT        v3,        count(*)AS count1,     "
					+ "   sum(v9)AS sum1      FROM        da_household_2016      WHERE        sys_standard = '"+gors+"'      AND v21 != '已脱贫'      GROUP BY        v3    )b ON a.v3 = b.v3  )c LEFT JOIN(  SELECT    a.v3,    sum2,   "
					+ " sum3  FROM    (      SELECT        v3,        count(*)AS sum2      FROM        da_household      WHERE        sys_standard = '"+gors+"'      AND v21 = '已脱贫'   AND entry_year = '2017'   GROUP BY        v3    )a  left JOIN(    SELECT      v3,     "
					+ " count(*)AS sum3    FROM      da_household_2016    WHERE      sys_standard = '"+gors+"'    AND v21 = '已脱贫'    GROUP BY      v3  )b ON a.v3 = b.v3  UNION    SELECT      b.v3,      sum2,      sum3    FROM      (    SELECT    v3,   count(*)AS sum2    "
					+ "    FROM          da_household        WHERE          sys_standard = '"+gors+"'        AND v21 = '已脱贫'   AND entry_year = '2017'     GROUP BY          v3      )a    right JOIN(      SELECT        v3,        count(*)AS sum3      FROM        da_household_2016      WHERE      "
					+ "  sys_standard = '"+gors+"'      AND v21 = '已脱贫'      GROUP BY        v3    )b ON a.v3 = b.v3)d on c.v3 = d.v3";*/
			String sql = "SELECT    B.*, A.count1,    count,    sum,    sum1,    sum2,    sum3 FROM    (       SELECT          e.com_name,          count,          count1,          sum,          sum1,          sum2,          sum3       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE    s.com_f_pkid =  "+pkid+"                )a  "
					+ "           LEFT JOIN(                SELECT b.v3, count, count1, sum, sum1                FROM (    SELECT       v3,       count(*)AS count,       sum(v9)AS sum    FROM       da_household    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    GROUP BY       v3 )a                RIGHT JOIN( SELECT    v3,    count(*)AS count1,    sum(v9)AS sum1 FROM    da_household_2016 WHERE    init_flag = '"+gors+"' AND v21 != '已脱贫' AND entry_year = '2016' GROUP BY    v3                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count,    count1,    sum,    sum1 FROM    (       SELECT  v3,  count(*)AS count,  sum(v9)AS sum       FROM  da_household       WHERE  init_flag = '"+gors+"'       AND v21 != '已脱贫'       GROUP BY  v3    )a LEFT JOIN(    SELECT       v3,       count(*)AS count1,       sum(v9)AS sum1    FROM       da_household_2016    WHERE       init_flag = '"+gors+"'    AND v21 != '已脱贫'    AND entry_year = '2016'    GROUP BY       v3 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s               "
							+ " WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                a.v3,                sum2,                sum3             FROM                ( SELECT    v3,    count(*)AS sum2 FROM    da_household WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' GROUP BY    v3                )a             LEFT JOIN(                SELECT v3, count(*)AS sum3                FROM da_household_2016                WHERE sys_standard = '"+gors+"'                AND v21 = '已脱贫'                AND entry_year = '2016'                GROUP BY v3             )b ON a.v3 = b.v3             UNION                SELECT b.v3, sum2, sum3                FROM (    SELECT       v3,       count(*)AS sum2    FROM       da_household    WHERE       sys_standard = '"+gors+"'    AND v21 = '已脱贫'    GROUP BY       v3 )a                RIGHT JOIN( SELECT    v3,    count(*)AS sum3 FROM    da_household_2016 WHERE    sys_standard = '"+gors+"' AND v21 = '已脱贫' AND entry_year = '2016' GROUP BY    v3                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name    )A,    (       SELECT          e.com_name,          count_17_hu,          sum_17_person,          count_16_hu,          sum_16_person,          count_17_hu_no,          sum_17_person_no,          count_16_hu_no,          sum_16_person_no,          count_17_hu_yes,          sum_17_person_yes,          count_16_hu_yes,          sum_16_person_yes       FROM          (             SELECT                *             FROM                ( SELECT    com_name FROM    sys_company s WHERE"
									+ "    s.com_f_pkid =  "+pkid+"                )a             LEFT JOIN(                SELECT b.v3, count_17_hu, sum_17_person, count_16_hu, sum_16_person                FROM (    SELECT       v3,       count(*)AS count_17_hu,       sum(v9)AS sum_17_person    FROM       da_household	where sys_standard = '"+gors+"'	    GROUP BY       v3 )a                RIGHT JOIN( SELECT    v3,    count(*)AS count_16_hu,    sum(v9)AS sum_16_person FROM    da_household_2016 WHERE    entry_year = '2016' 	and sys_standard = '"+gors+"' GROUP BY    v3                )b ON a.v3 = b.v3                UNION SELECT    a.v3,    count_17_hu,    sum_17_person,    count_16_hu,    sum_16_person FROM    (       SELECT  v3,  count(*)AS count_17_hu,  sum(v9)AS sum_17_person       FROM  da_household 														WHERE sys_standard = '"+gors+"' GROUP BY  v3    )a LEFT JOIN(    SELECT       v3,       count(*)AS count_16_hu,       sum(v9)AS sum_16_person    FROM       da_household_2016    WHERE       entry_year = '2016' 	and sys_standard = '"+gors+"'    GROUP BY       v3 )b ON a.v3 = b.v3             )d ON a.com_name = d.v3          )e       INNER JOIN(          SELECT             *          FROM             (                SELECT com_name                FROM sys_company s       "
											+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_no,                sum_17_person_no,                count_16_hu_no,                sum_16_person_no             FROM                ( SELECT    v3,    count(*)AS count_17_hu_no,    sum(v9)AS sum_17_person_no FROM    da_household WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' GROUP BY    v3                )a             RIGHT JOIN(                SELECT v3, count(*)AS count_16_hu_no, sum(v9)AS sum_16_person_no                FROM da_household_2016                WHERE v21 != '已脱贫'  and init_flag = '"+gors+"'                AND entry_year = '2016'                GROUP BY v3             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_no, sum_17_person_no, count_16_hu_no, sum_16_person_no                FROM (    SELECT       v3,       count(*)AS count_17_hu_no,       sum(v9)AS sum_17_person_no    FROM       da_household    WHERE       v21 != '已脱贫'  and init_flag = '"+gors+"'    GROUP BY       v3 )a                LEFT JOIN( SELECT    v3,    count(*)AS count_16_hu_no,    sum(v9)AS sum_16_person_no FROM    da_household_2016 WHERE    v21 != '已脱贫'  and init_flag = '"+gors+"' AND entry_year = '2016' GROUP BY    v3                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )f ON e.com_name = f.com_name       INNER JOIN(          SELECT             com_name,             count_17_hu_yes,             sum_17_person_yes,             count_16_hu_yes,             sum_16_person_yes          FROM             (                SELECT com_name                FROM sys_company s       "
													+ "         WHERE s.com_f_pkid =  "+pkid+"             )a          LEFT JOIN(             SELECT                b.v3,                count_17_hu_yes,                sum_17_person_yes,                count_16_hu_yes,                sum_16_person_yes             FROM                ( SELECT    v3,    count(*)AS count_17_hu_yes,    sum(v9)AS sum_17_person_yes FROM    da_household WHERE    v21 = '已脱贫'  and sys_standard = '"+gors+"' GROUP BY    v3                )a             RIGHT JOIN(                SELECT v3, count(*)AS count_16_hu_yes, sum(v9)AS sum_16_person_yes                FROM da_household_2016                WHERE v21 = '已脱贫'                AND entry_year = '2016' and sys_standard = '"+gors+"'                GROUP BY v3             )b ON a.v3 = b.v3             UNION                SELECT a.v3, count_17_hu_yes, sum_17_person_yes, count_16_hu_yes, sum_16_person_yes                FROM (    SELECT       v3,       count(*)AS count_17_hu_yes,       sum(v9)AS sum_17_person_yes    FROM       da_household    WHERE       v21 = '已脱贫' and sys_standard = '"+gors+"'    GROUP BY       v3 )a                LEFT JOIN( SELECT    v3,    count(*)AS count_16_hu_yes,    sum(v9)AS sum_16_person_yes FROM    da_household_2016 WHERE    v21 = '已脱贫' AND entry_year = '2016' and sys_standard = '"+gors+"' GROUP BY    v3                )b ON a.v3 = b.v3          )d ON a.com_name = d.v3       )g ON f.com_name = g.com_name    )B WHERE    A.com_name = B.com_name ";
		
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			
			
			if(sql_list.size()>0){
				JSONArray jsa=new JSONArray();
				JSONObject val = new JSONObject();
				for(int i = 0;i<sql_list.size();i++){
					val.put("v3","".equals(sql_list.get(i).get("com_name")) || sql_list.get(i).get("com_name") == null ?"": sql_list.get(i).get("com_name").toString());
					val.put("count","".equals(sql_list.get(i).get("count")) || sql_list.get(i).get("count") == null ?"0": sql_list.get(i).get("count").toString());
					val.put("count1","".equals(sql_list.get(i).get("count1")) || sql_list.get(i).get("count1") == null ?"0": sql_list.get(i).get("count1").toString());
					val.put("sum","".equals(sql_list.get(i).get("sum")) || sql_list.get(i).get("sum") == null ?"0": sql_list.get(i).get("sum").toString());
					val.put("sum1","".equals(sql_list.get(i).get("sum1")) || sql_list.get(i).get("sum1") == null ?"0": sql_list.get(i).get("sum1").toString());
					val.put("sum2","".equals(sql_list.get(i).get("sum2")) || sql_list.get(i).get("sum2") == null ?"0": sql_list.get(i).get("sum2").toString());
					val.put("sum3","".equals(sql_list.get(i).get("sum3")) || sql_list.get(i).get("sum3") == null ?"0": sql_list.get(i).get("sum3").toString());
					val.put("count_17_hu","".equals(sql_list.get(i).get("count_17_hu")) || sql_list.get(i).get("count_17_hu") == null ?"0": sql_list.get(i).get("count_17_hu").toString());
					val.put("sum_17_person","".equals(sql_list.get(i).get("sum_17_person")) || sql_list.get(i).get("sum_17_person") == null ?"0": sql_list.get(i).get("sum_17_person").toString());
					val.put("count_16_hu","".equals(sql_list.get(i).get("count_16_hu")) || sql_list.get(i).get("count_16_hu") == null ?"0": sql_list.get(i).get("count_16_hu").toString());
					val.put("sum_16_person","".equals(sql_list.get(i).get("sum_16_person")) || sql_list.get(i).get("sum_16_person") == null ?"0": sql_list.get(i).get("sum_16_person").toString());
					val.put("count_17_hu_no","".equals(sql_list.get(i).get("count_17_hu_no")) || sql_list.get(i).get("count_17_hu_no") == null ?"0": sql_list.get(i).get("count_17_hu_no").toString());
					val.put("sum_17_person_no","".equals(sql_list.get(i).get("sum_17_person_no")) || sql_list.get(i).get("sum_17_person_no") == null ?"0": sql_list.get(i).get("sum_17_person_no").toString());
					val.put("count_16_hu_no","".equals(sql_list.get(i).get("count_16_hu_no")) || sql_list.get(i).get("count_16_hu_no") == null ?"0": sql_list.get(i).get("count_16_hu_no").toString());
					val.put("sum_16_person_no","".equals(sql_list.get(i).get("sum_16_person_no")) || sql_list.get(i).get("sum_16_person_no") == null ?"0": sql_list.get(i).get("sum_16_person_no").toString());
					val.put("count_17_hu_yes","".equals(sql_list.get(i).get("count_17_hu_yes")) || sql_list.get(i).get("count_17_hu_yes") == null ?"0": sql_list.get(i).get("count_17_hu_yes").toString());
					val.put("sum_17_person_yes","".equals(sql_list.get(i).get("sum_17_person_yes")) || sql_list.get(i).get("sum_17_person_yes") == null ?"0": sql_list.get(i).get("sum_17_person_yes").toString());
					val.put("count_16_hu_yes","".equals(sql_list.get(i).get("count_16_hu_yes")) || sql_list.get(i).get("count_16_hu_yes") == null ?"0": sql_list.get(i).get("count_16_hu_yes").toString());
					val.put("sum_16_person_yes","".equals(sql_list.get(i).get("sum_16_person_yes")) || sql_list.get(i).get("sum_16_person_yes") == null ?"0": sql_list.get(i).get("sum_16_person_yes").toString());

					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}
			
			
		}

		return null;
	}
	//	首页地图中的数据data
	public ModelAndView getindex_table11(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String gors = request.getParameter("gors");
		String code = request.getParameter("code");


		HttpSession session = request.getSession();//取session
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称

			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}

			if(code.toString().equals("shi")){//市级
				String sql="select * from (select com_name from sys_company  s  where s.com_f_pkid = 4)b  LEFT JOIN (select * from (SELECT v3,count(*) AS count FROM da_household  where init_flag='"+gors+"' and v21!='已脱贫'  GROUP BY v3)a)c on b.com_name = c.v3";
				SQLAdapter sqlAdapter =new SQLAdapter(sql);
				List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
				JSONObject val = new JSONObject();
				if(sql_list.size()>0){
					JSONArray jsa=new JSONArray();
					for(int i = 0;i<sql_list.size();i++){
						Map Admin_st_map = sql_list.get(i);
						for(int j = 0; j<Admin_st_map.size(); j++){
							val.put("name", Admin_st_map.get("com_name"));
							val.put("value", Admin_st_map.get("count")==null?"0":Admin_st_map.get("count").toString());
						}
						jsa.add(val);
					}
					response.getWriter().write(jsa.toString());
				}else{
					response.getWriter().print("0");
				}
			}else{
				if(company_json.get("com_level").toString().equals("2")==true){//二级单位

					String sql="select * from (select com_name from sys_company  s  where s.com_f_pkid = "+company_json.get("pkid").toString()+")b LEFT JOIN (select * from ( SELECT a1.v4 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v3 = a2.com_name WHERE a2.com_code='"+code+"' AND a1.init_flag='"+gors+"' and a1.v21!='已脱贫'   GROUP BY a1.v4)a)c on b.com_name = c.v3";
					SQLAdapter sqlAdapter =new SQLAdapter(sql);
					List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
					JSONObject val = new JSONObject();
					if(sql_list.size()>0){
						JSONArray jsa=new JSONArray();
						for(int i = 0;i<sql_list.size();i++){
							Map Admin_st_map = sql_list.get(i);
							for(int j = 0; j<Admin_st_map.size(); j++){
								val.put("name", Admin_st_map.get("com_name"));
								val.put("value",  Admin_st_map.get("count")==null?"0":Admin_st_map.get("count").toString());
							}
							jsa.add(val);
						}
						response.getWriter().write(jsa.toString());
					}else{
						response.getWriter().print("0");
					}
				}else if(company_json.get("com_level").toString().equals("3")==true){//三级单位
					String sql="select * from (select com_name from sys_company  s  where s.com_f_pkid = "+company_json.get("pkid").toString()+")b LEFT JOIN (select * from (SELECT a1.v5 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v4 = a2.com_name WHERE a2.com_code='"+code+"' AND a1.init_flag='"+gors+"' and a1.v21!='已脱贫' GROUP BY a1.v5)a)c on b.com_name = c.v3";
					SQLAdapter sqlAdapter =new SQLAdapter(sql);
					List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
					JSONObject val = new JSONObject();
					if(sql_list.size()>0){
						JSONArray jsa=new JSONArray();
						for(int i = 0;i<sql_list.size();i++){
							Map Admin_st_map = sql_list.get(i);
							for(int j = 0; j<Admin_st_map.size(); j++){
								val.put("name", Admin_st_map.get("com_name"));
								val.put("value",  Admin_st_map.get("count")==null?"0":Admin_st_map.get("count").toString());
							}
							jsa.add(val);
						}
						response.getWriter().write(jsa.toString());
					}else{
						response.getWriter().print("0");
					}
				}else if(company_json.get("com_level").toString().equals("4")==true){//四级单位
					
					String sql="select * from (select com_name from sys_company  s  where s.com_f_pkid = "+company_json.get("com_f_pkid").toString()+")b LEFT JOIN (select * from (SELECT a1.v5 AS v3,COUNT(*) AS count from da_household a1 JOIN sys_company a2 on a1.v4 = a2.com_name WHERE a2.com_code='"+code+"' AND a1.init_flag='"+gors+"' and a1.v21!='已脱贫'  GROUP BY a1.v5)a)c on b.com_name = c.v3";
					SQLAdapter sqlAdapter =new SQLAdapter(sql);
					List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
					JSONObject val = new JSONObject();
					if(sql_list.size()>0){
						JSONArray jsa=new JSONArray();
						for(int i = 0;i<sql_list.size();i++){
							Map Admin_st_map = sql_list.get(i);
							for(int j = 0; j<Admin_st_map.size(); j++){
								val.put("name", Admin_st_map.get("com_name"));
								val.put("value",  Admin_st_map.get("count")==null?"0":Admin_st_map.get("count").toString());
							}
							jsa.add(val);
						}
						response.getWriter().write(jsa.toString());
					}else{
						response.getWriter().print("0");
					}
				}
			}

		}else{//如果session为空，即未登录
			String sql="select * from (select com_name from sys_company  s  where s.com_f_pkid = 4)b LEFT JOIN (select * from (SELECT v3,count(*) AS count FROM da_household  where init_flag='"+gors+"' and v21!='已脱贫' GROUP BY v3)a)c on b.com_name = c.v3";
			SQLAdapter sqlAdapter =new SQLAdapter(sql);
			List<Map> sql_list = this.getBySqlMapper.findRecords(sqlAdapter);
			JSONObject val = new JSONObject();
			if(sql_list.size()>0){
				JSONArray jsa=new JSONArray();
				for(int i = 0;i<sql_list.size();i++){
					Map Admin_st_map = sql_list.get(i);
					for(int j = 0; j<Admin_st_map.size(); j++){
						val.put("name", Admin_st_map.get("com_name"));
						val.put("value",  Admin_st_map.get("count")==null?"0":Admin_st_map.get("count").toString());
					}
					jsa.add(val);
				}
				response.getWriter().write(jsa.toString());
			}else{
				response.getWriter().print("0");
			}
		}

		return null;
	}

	//首页下方的表格
	public ModelAndView getindex_table2(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String gors = request.getParameter("gors");//获取判断国标或者市标的参数
		String code = request.getParameter("code");//获取行政编码
		String pkid = request.getParameter("pkid");
		HttpSession session = request.getSession();//取session
		JSONObject json = new JSONObject();
		String sql = "", ziduan = "", tiaojian = "", cun_duyou = "",xc_name = "";

		if(session.getAttribute("Login_map")!=null){//验证session不为空
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			if(code.toString().equals("shi")){//市级
				ziduan = "v1";
				tiaojian = " where com_level=2 and b2>0 order by b2 desc ";
			}else if(company_json.get("com_level").toString().equals("2")==true){//二级用户
				ziduan = "v2";
				tiaojian = " where com_f_pkid="+company_json.get("pkid")+" and b2>0 order by b2 desc ";
			}else if(company_json.get("com_level").toString().equals("3")==true){//三级用户
				ziduan = "v3";
				xc_name = "and v2= '"+company_json.get("com_name").toString()+"'";
				tiaojian = " where com_f_pkid="+company_json.get("pkid")+" and b2>0  order by b2 desc ";
				cun_duyou = " and v2 in(select com_name from sys_company w1 where w1.pkid="+company_json.get("pkid")+") ";
			}else if(company_json.get("com_level").toString().equals("4")==true){//四级用户
				ziduan = "v3";
				xc_name = "and v2= '"+company_json.get("xiang").toString()+"'";
				tiaojian = " where com_f_pkid="+company_json.get("com_f_pkid")+" and b2>0 order by b2 desc ";
				cun_duyou = " and v2 in(select com_name from sys_company w1 where w1.pkid="+company_json.get("com_f_pkid")+") ";
				pkid=company_json.getString("com_f_pkid");
			}
		}else{//用户未登录，默认显示全市
			ziduan = "v1";
			tiaojian = " where c.com_level=2 and b2>0 order by b2 desc ";
		}
		sql = " select * from (select com_name  from sys_company s where s.com_f_pkid = "+pkid+") d  LEFT JOIN ( SELECT a.v2,a.b1,b2,b22,b3,b33,b4,b6,b7,b8,b9,b10,b11,b12,b13 from (SELECT v2,b1,b2,b3,b4,b6,b7,b8,b9 from (select a.v2,a."+ziduan+" as b1,"+
				"SUM(a.b2) as b2 , SUM(a.b3) as b3, SUM(a.b4) as b4,SUM(a.b6)as b6,SUM(a.b7)as b7 ,SUM(a.b8) as b8,SUM(a.b9) as b9,"+
				"a.b14 from da_statistics a "+
				" where a.b14='"+ gors +"' "+xc_name+" GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name"+tiaojian+")a left join("+
				"SELECT v2,b1,b2 b22,b3 b33,b10,b11,b12, b13 from (select a.v2,a."+ziduan+" as b1,"+
				"SUM(a.b2) as b2 , SUM(a.b3) as b3,	SUM(a.b10)AS b10,SUM(a.b11)AS b11, SUM(a.b12)AS b12, SUM(a.b13)AS b13  from da_statistics_2016 a "+
				" where a.b14='"+ gors +"' "+xc_name+" and a.entry_year = '2016' GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name"+tiaojian+")b on a.b1=b.b1 union SELECT a.v2,b.b1,b2,b22,b3,b33,b4,b6,b7,b8,b9,b10,b11,b12,b13 from (SELECT v2,b1,b2,b3,b4,b6,b7,b8,b9 from (select a.v2,a."+ziduan+" as b1,"+
				"SUM(a.b2) as b2 , SUM(a.b3) as b3, SUM(a.b4) as b4,SUM(a.b6)as b6,SUM(a.b7)as b7 ,SUM(a.b8) as b8,SUM(a.b9) as b9,"+
				"a.b14 from da_statistics a "+
				" where a.b14='"+ gors +"' "+xc_name+" GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name"+tiaojian+")a right join("+
				"SELECT v2,b1,b2 b22,b3 b33,b10,b11,b12, b13 from (select a.v2,a."+ziduan+" as b1,"+
				"SUM(a.b2) as b2 , SUM(a.b3) as b3,	SUM(a.b10)AS b10,SUM(a.b11)AS b11, SUM(a.b12)AS b12, SUM(a.b13)AS b13  from da_statistics_2016 a "+
				" where a.b14='"+ gors +"' "+xc_name+" and a.entry_year = '2016' GROUP BY a."+ziduan+") b join sys_company c on b.b1=c.com_name"+tiaojian+")b on a.b1=b.b1 )c on d.com_name = c.b1";
		SQLAdapter sql_find=new SQLAdapter(sql);
		List<Map> sql_list = this.getBySqlMapper.findRecords(sql_find);
		JSONObject val = new JSONObject();
		if(sql_list.size()>0){
			JSONArray jsa=new JSONArray();
			for(Map asmap:sql_list){
				val.put("b1",asmap.get("com_name")==null?"":asmap.get("com_name"));
				val.put("b2",asmap.get("b2")==null?"0":asmap.get("b2"));
				val.put("b3",asmap.get("b3")==null?"0":asmap.get("b3"));
				val.put("b22",asmap.get("b22")==null?"0":asmap.get("b22"));
				val.put("b33",asmap.get("b33")==null?"0":asmap.get("b33"));
				val.put("b10",asmap.get("b10")==null?"0":asmap.get("b10"));
				val.put("b11",asmap.get("b11")==null?"0":asmap.get("b11"));
				val.put("b12",asmap.get("b12")==null?"0":asmap.get("b12"));
				val.put("b13",asmap.get("b13")==null?"0":asmap.get("b13"));
				jsa.add(val);
			}
			response.getWriter().write(jsa.toString());
		}else{
			response.getWriter().print("0");
		}
		return null;
	}

	//登录验证
	public ModelAndView loginin(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String username = request.getParameter("add_account");//获取用户名 
		String password = request.getParameter("add_password");//获取密码
		String people_sql = "select * from sys_user WHERE col_account = '" + username + "'";
		SQLAdapter Login_Adapter = new SQLAdapter(people_sql);
		List<Map> Login = this.getBySqlMapper.findRecords(Login_Adapter);
		if(Login.size()>0){
			Map Login_map = Login.get(0);

			if(Login_map.get("account_state").toString().equals("1")){//状态正常
				if(Tool.md5(password).equals(Login_map.get("col_password"))==true){//密码正确

					HttpSession session = request.getSession();
					//Login_map.put("col_password", "");
					Login_map.remove("col_password");
					session.setAttribute("Login_map", Login_map);
					//功能权限
					Map function_map = new HashMap();

					//表格显示的内容
					Map company_map=new HashMap();
					//维护开关
					Map weihu_map=new HashMap();

					if(username.equals("admin")){//超级用户 

						//超管就是市级的
						String sql_zong = "select * from sys_company where pkid="+Login_map.get("sys_company_id");
						SQLAdapter sql_zong_Adapter = new SQLAdapter(sql_zong);
						List<Map> sql_zong_list = this.getBySqlMapper.findRecords(sql_zong_Adapter);
						Map zpong = sql_zong_list.get(0);
						session.setAttribute("company", zpong);

						//System.out.println(Tool.md5("admin123456"));
						//超管有所有权限，维护开关不影响
						String sql_1 = "select pkid,div_id,maintain from sys_function";
						SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
						List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
						for(int i = 0;i<sql_1_list.size();i++){
							Map tep = sql_1_list.get(i);
							function_map.put(tep.get("div_id"), tep.get("div_id"));
							weihu_map.put(tep.get("div_id"), "1");
						}
						session.setAttribute("function_map", function_map);
						session.setAttribute("weihu_map", weihu_map);

						//超管的数据浏览权限为市级最大
						company_map.put("com_type", "管理员");
						session.setAttribute("company_map", company_map);

						response.getWriter().print("1");//成功

					}else{

						//功能模块权限
						if(Login_map.get("account_type").toString().equals("1")){//行政单位用户 ，数据显示需要关联，行政区划表（sys_company，sys_company_id）
							company_map.put("com_type", "单位");
							session.setAttribute("company_map", company_map);//浏览数据的权限

							//先获取单位信息，获取单位层级
							String sql_zong = "select * from sys_company where pkid="+Login_map.get("sys_company_id");
							SQLAdapter sql_zong_Adapter = new SQLAdapter(sql_zong);
							List<Map> sql_zong_list = this.getBySqlMapper.findRecords(sql_zong_Adapter);
							Map zpong = sql_zong_list.get(0);

							if(zpong.get("com_level").toString().equals("1")){//市级单位

								session.setAttribute("company", zpong);

							}else if(zpong.get("com_level").toString().equals("2")){//旗县级

								zpong.put("xian_id",zpong.get("pkid"));
								zpong.put("xian",zpong.get("com_name"));
								zpong.put("xian_code",zpong.get("com_code"));
								session.setAttribute("company", zpong);

							}else if(zpong.get("com_level").toString().equals("3")){//乡级或者村级

								String sql_3 = "select x.*,y.pkid as xian_id,y.com_name as xian,y.com_code as xian_code from sys_company x join sys_company y on x.com_f_pkid=y.pkid where x.pkid="+Login_map.get("sys_company_id");
								SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
								List<Map> sql_3_list = this.getBySqlMapper.findRecords(sql_3_Adapter);
								Map namemap = sql_3_list.get(0);
								namemap.put("xiang_id",namemap.get("pkid"));
								namemap.put("xiang",namemap.get("com_name"));
								namemap.put("xiang_code",namemap.get("com_code"));
								session.setAttribute("company", namemap);

							}else if(zpong.get("com_level").toString().equals("4")){
								
								String sql_3 = "select x.*,z.pkid as xian_id,z.com_name as xian,z.com_code as xian_code,y.pkid as xiang_id,y.com_name as xiang,y.com_code as xiang_code from sys_company x join sys_company y on x.com_f_pkid=y.pkid join sys_company z on y.com_f_pkid=z.pkid where x.pkid="+Login_map.get("sys_company_id");
								SQLAdapter sql_3_Adapter = new SQLAdapter(sql_3);
								List<Map> sql_3_list = this.getBySqlMapper.findRecords(sql_3_Adapter);
								Map namemap = sql_3_list.get(0);
								namemap.put("cun_id",namemap.get("pkid"));
								namemap.put("cun",namemap.get("com_name"));
								namemap.put("cun_code",namemap.get("com_code"));
								session.setAttribute("company", namemap);

							}

							//行政区划单位不受角色控制，拥有所有权限，但是受维护开关限制
							//String sql_1 = "select pkid,div_id,maintain from sys_function";
							String sql_1 = "select y.modular_name,y.div_id,y.maintain from sys_company_function_many x join sys_function y on x.sys_function_id=y.pkid where sys_company_level="+zpong.get("com_level").toString();
							SQLAdapter sql_1_Adapter = new SQLAdapter(sql_1);
							List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
							for(int i = 0;i<sql_1_list.size();i++){
								Map tep = sql_1_list.get(i);

								//								if(tep.get("div_id").toString().equals("H5-3_li")){//维护开关，只有市级有权限，这个是限制死的
								//									if(zpong.get("com_level").toString().equals("1")){//市级单位
								//										function_map.put(tep.get("div_id"), tep.get("div_id"));
								//										weihu_map.put(tep.get("div_id"), tep.get("maintain"));
								//									}
								//								}else{
								function_map.put(tep.get("div_id"), tep.get("div_id"));
								weihu_map.put(tep.get("div_id"), tep.get("maintain"));
								//								}

							}

							session.setAttribute("function_map", function_map);
							session.setAttribute("weihu_map", weihu_map);

						}else if(Login_map.get("account_type").toString().equals("2")){//帮扶个人用户,数据需要关联贫困户与帮扶人关系表 （sys_personal_household_many，sys_personal_id）

							String namesql="SELECT col_name FROM sys_personal WHERE pkid="+Login_map.get("sys_personal_id"); 
							SQLAdapter namesql_Adapter = new SQLAdapter(namesql);
							List<Map> namesql_list = this.getBySqlMapper.findRecords(namesql_Adapter);
							Map namemap = namesql_list.get(0);
							namemap.put("username",namemap.get("col_name"));
							session.setAttribute("namemap", namemap);

							if(!Login_map.get("sys_role_id").toString().equals("")&&Login_map.get("sys_role_id")!=null){//当用户权限不为null.
								String sql = "select y.pkid,y.div_id from sys_role_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
										" sys_role_id ="+Login_map.get("sys_role_id");
								SQLAdapter sql_1_Adapter = new SQLAdapter(sql);
								List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
								for(int i = 0;i<sql_1_list.size();i++){
									Map tep = sql_1_list.get(i);
									function_map.put(tep.get("div_id"), tep.get("div_id"));
								}
							}

							//再查询一下，用户直接对应权限表(sys_user_function_many)
							String sql_2 = "select y.pkid,y.div_id,y.maintain from sys_user_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
									" sys_user_id=(SELECT sys_personal_id from sys_user WHERE pkid="+Login_map.get("pkid")+")";
							SQLAdapter sql_2_Adapter = new SQLAdapter(sql_2);
							List<Map> sql_2_list = this.getBySqlMapper.findRecords(sql_2_Adapter);
							if(sql_2_list.size()>0){//管理员为这个用户单独设置了权限

								function_map = new HashMap();//清空角色设置的权限
								for(int i = 0;i<sql_2_list.size();i++){
									Map tep = sql_2_list.get(i);
									function_map.put(tep.get("div_id"), tep.get("div_id"));
								}
							}

							session.setAttribute("function_map", function_map);
							//加维护开关权限
							String sql_2_2= "select y.pkid,y.div_id,y.maintain from sys_user_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
									" sys_user_id=(SELECT sys_personal_id from sys_user WHERE pkid="+Login_map.get("pkid")+")";
							SQLAdapter sql_2_2_Adapter = new SQLAdapter(sql_2_2);
							List<Map> sql_2_2_list = this.getBySqlMapper.findRecords(sql_2_2_Adapter);
							if(sql_2_2_list.size()>0){

								function_map = new HashMap();
								for(int i = 0;i<sql_2_2_list.size();i++){
									Map tep = sql_2_2_list.get(i);
									weihu_map.put(tep.get("div_id"), tep.get("maintain"));
								}
							}
							session.setAttribute("weihu_map", weihu_map);
							//浏览数据的权限
							company_map.put("com_type", "帮扶人");
							session.setAttribute("company_map", company_map);

						}else if(Login_map.get("account_type").toString().equals("3")){//只有查询权限用户
							//超管就是市级的
							String sql_zong = "select * from sys_company where pkid="+Login_map.get("sys_company_id");
							SQLAdapter sql_zong_Adapter = new SQLAdapter(sql_zong);
							List<Map> sql_zong_list = this.getBySqlMapper.findRecords(sql_zong_Adapter);
							Map zpong = sql_zong_list.get(0);
							session.setAttribute("company", zpong);

							if(!Login_map.get("sys_role_id").toString().equals("")&&Login_map.get("sys_role_id")!=null){//当用户权限不为null.
								String sql = "select y.pkid,y.div_id from sys_role_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
										" x.sys_role_id ="+Login_map.get("sys_role_id");
								SQLAdapter sql_1_Adapter = new SQLAdapter(sql);
								List<Map> sql_1_list = this.getBySqlMapper.findRecords(sql_1_Adapter);
								for(int i = 0;i<sql_1_list.size();i++){
									Map tep = sql_1_list.get(i);
									function_map.put(tep.get("div_id"), tep.get("div_id"));
									weihu_map.put(tep.get("div_id"), "1");
								}
							}
							session.setAttribute("function_map", function_map);
							//加维护开关权限
//							String sql_2_2= "select y.pkid,y.div_id,y.maintain from sys_user_function_many x,sys_function y where x.sys_function_id=y.pkid and"+
//									" sys_user_id=(SELECT sys_personal_id from sys_user WHERE pkid="+Login_map.get("pkid")+")";
//							SQLAdapter sql_2_2_Adapter = new SQLAdapter(sql_2_2);
//							List<Map> sql_2_2_list = this.getBySqlMapper.findRecords(sql_2_2_Adapter);
//							if(sql_2_2_list.size()>0){
//
//								function_map = new HashMap();
//								for(int i = 0;i<sql_2_2_list.size();i++){
//									Map tep = sql_2_2_list.get(i);
//									weihu_map.put(tep.get("div_id"), tep.get("maintain"));
//								}
//							}
							session.setAttribute("weihu_map", weihu_map);
							//浏览数据的权限
							company_map.put("com_type", "最小权限用户");
							session.setAttribute("company_map", company_map);

						}
						response.getWriter().print("1");//成功
					}

				}else{
					response.getWriter().print("0");//密码不正确
				}
			}else{
				response.getWriter().print("3");//状态停用
			}

		}else{
			response.getWriter().print("2");//没有此用户
		}
		return null;
	}

	//session获取用户登陆信息
	public ModelAndView getLogin_massage(HttpServletRequest request,HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session.getAttribute("Login_map")!=null){//验证session不为空

			Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
			Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
			Map<String,String> function_map = (Map)session.getAttribute("function_map");//菜单权限表
			Map<String,String> weihu_map = (Map)session.getAttribute("weihu_map");//维护开关
			Map<String,String> company_map = (Map)session.getAttribute("company_map");

			JSONObject Login_map_json = new JSONObject();
			for(String key : Login_map.keySet()){
				Login_map_json.put(key, Login_map.get(key));
			}
			json.put("Login_map", Login_map_json);

			JSONObject company_json = new JSONObject();
			for(String key : company.keySet()){
				company_json.put(key, company.get(key));
			}
			json.put("company", company_json);

			JSONObject function_map_json = new JSONObject();
			for(String key : function_map.keySet()){
				function_map_json.put(key, function_map.get(key));
			}
			json.put("function_map", function_map_json);

			JSONObject weihu_map_json = new JSONObject();
			for(String key : weihu_map.keySet()){
				weihu_map_json.put(key, weihu_map.get(key));
			}
			json.put("weihu_map", weihu_map_json);

			JSONObject company_map_json = new JSONObject();
			for(String key : company_map.keySet()){
				company_map_json.put(key, company_map.get(key));
			}
			json.put("company_map", company_map_json);

			response.getWriter().write(json.toString());
		}else{
			response.getWriter().print("weidenglu");
		}
		return null;
	}

	//销毁session
	public ModelAndView login_out(HttpServletRequest request,HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();
		try{
			session.invalidate();
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}


	//更新密码
	public ModelAndView upPassword(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String id = request.getParameter("pkid");//获取用户ID
		String password = request.getParameter("password");//获取密码
		password = Tool.md5(password);

		String people_sql = "update sys_user set col_password='"+password+"' where pkid="+id;
		SQLAdapter Login_Adapter = new SQLAdapter(people_sql);
		try{
			this.getBySqlMapper.updateSelective(Login_Adapter);
			response.getWriter().write("1");
		}catch (Exception e){
			response.getWriter().write("0");
		}
		return null;
	}

	//验证修改密码时输入是否和原密码相同
	public ModelAndView o_password(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String password1 = request.getParameter("val");
		String id = request.getParameter("pkid");

		String sql = "SELECT count(*) FROM sys_user WHERE pkid='" + id + "' and col_password = '" + Tool.md5(password1) + "'";
		SQLAdapter sqlAdapter = new SQLAdapter(sql);
		int resultSize = getBySqlMapper.findrows(sqlAdapter);
		if (resultSize == 0){
			response.getWriter().print("0");
		}else{
			response.getWriter().print("1");
		}
		return null;
	}

	//获取帮扶单位的列表
	public ModelAndView get_bfdw_List(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		String search = "";
		String cha_qixian="";
		String cha_bfdw="";
		String cha_ldxm="";
		String cha_lddh="";
		String cha_sbfc="";
		String str="";
		
		String year = request.getParameter("cha_year");//年份
		if( "2016".equals(year) ) {
			year = "_2016";
		} else {
			year = "";
		}
		
		if(request.getParameter("cha_qixian")!=null&&!request.getParameter("cha_qixian").equals("请选择")){
			cha_qixian = request.getParameter("cha_qixian").trim();
			str += " a1.sys_company_id like '%"+cha_qixian+"%' and";
		}
		if(request.getParameter("cha_bfdw")!=null&&!request.getParameter("cha_bfdw").equals("")	){
			cha_bfdw = request.getParameter("cha_bfdw").trim();
			str += " a1.v1 like '%"+cha_bfdw+"%' and";
		}
		if(request.getParameter("cha_ldxm")!=null&&!request.getParameter("cha_ldxm").equals("")){
			cha_ldxm = request.getParameter("cha_ldxm").trim();
			str += " a1.v3 like '%"+cha_ldxm+"%' and";
		}
		if(request.getParameter("cha_lddh")!=null&&!request.getParameter("cha_lddh").equals("")){
			cha_lddh = request.getParameter("cha_lddh").trim();
			str += " a1.v4 like '%"+cha_lddh+"%' and";
		}
		if(request.getParameter("cha_sbfc")!=null&&!request.getParameter("cha_sbfc").equals("")){
			cha_sbfc = request.getParameter("cha_sbfc").trim();
			str += " a2.com_name like '%"+cha_sbfc+"%' and";
		}

		if(request.getParameter("search")!=null&&!request.getParameter("search").equals("")){
			search = request.getParameter("search").trim();
		}

		int size = Integer.parseInt(pageSize);
		int number = Integer.parseInt(pageNumber);
		int page = number == 0 ? 1 : (number/size)+1;

		String people_sql="";
		String count_st_sql="";

		HttpSession session = request.getSession();//取session
		Map<String,String> company = (Map)session.getAttribute("company");//用户所属单位表，加前台显示时用的上下级关联名称
		JSONObject company_json = new JSONObject();
		for(String key : company.keySet()){
			company_json.put(key, company.get(key));
		}
		
		String pkid=company_json.get("pkid").toString();//获取用户名称
		if(pkid.equals("4")){
			if(str==""){
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from  da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name  ORDER BY pkid  desc limit "+number+","+size;
				count_st_sql =  "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from  da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name";
			}else{
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where "+str.substring(0, str.length()-3)+"  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name ORDER BY pkid  desc limit "+number+","+size;
/*				count_st_sql = "select count(*) from da_company"+year+" a1 LEFT JOIN sys_company a2 ON a1.v5=a2.pkid where "+str.substring(0, str.length()-3);
 * 	*/			count_st_sql="select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from da_company"+year+" a1 "
			+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where "+str.substring(0, str.length()-3)+"  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name";
				}	
		}else{
			String xian_id=company_json.get("xian_id").toString();//获取用户名称
			if(str==""){
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where sys_company_id="+xian_id+"  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name ORDER BY pkid  desc limit "+number+","+size;
/*				count_st_sql = "select count(*) from da_company"+year+" a1 where sys_company_id="+xian_id;
*/				count_st_sql="select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from da_company"+year+" a1 "
		+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where sys_company_id="+xian_id+"  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name";
				}else{
				people_sql = "select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from da_company"+year+" a1 "
						+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where sys_company_id="+xian_id+" and "+str.substring(0, str.length()-3)+" group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name ORDER BY pkid  desc limit "+number+","+size;
/*				count_st_sql = "select count(*) from da_company"+year+" a1 LEFT JOIN sys_company a2 ON a1.v5=a2.pkid where sys_company_id="+xian_id+" and "+str.substring(0, str.length()-3);
*/				count_st_sql="select a1.pkid,a3.com_name as quname,a1.v1,a1.v2,a1.v3,a1.v4,GROUP_CONCAT(a2.com_name) com_name,GROUP_CONCAT(a1.v5) v5,a1.sys_company_id,a2.com_f_pkid,(select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid) as f_pkid ,(select com_name from sys_company where pkid = a2.com_f_pkid) as xiang_com_name,(select com_name  from sys_company where pkid in (select d.com_f_pkid from sys_company d where pkid = a2.com_f_pkid))as qixian_com_name  from da_company"+year+" a1 "
		+ "LEFT JOIN sys_company a2 ON a1.v5=a2.pkid LEFT JOIN sys_company a3 ON a1.sys_company_id=a3.pkid  where sys_company_id="+xian_id+"  group by v1,v2,v3,v4,quname,xiang_com_name,qixian_com_name";
				}	
		}
		SQLAdapter count_st_Adapter = new SQLAdapter(count_st_sql);
		int total = this.getBySqlMapper.findRecords(count_st_Adapter).size();
		
		SQLAdapter Admin_st_Adapter = new SQLAdapter(people_sql);
		List<Map> Admin_st_List = this.getBySqlMapper.findRecords(Admin_st_Adapter);
		if(Admin_st_List.size()>0){
			JSONArray jsa=new JSONArray();
			for(int i = 0;i<Admin_st_List.size();i++){
				String com_sql = "";
				String com_name="";
				Map Admin_st_map = Admin_st_List.get(i);
				JSONObject val = new JSONObject();
				for (Object key : Admin_st_map.keySet()) {
					//如果v5存在多个村的id时  将其拆分查询村名 并且拼接显示在前台
					if(key.equals("v5")&&Admin_st_map.get(key).toString().split(",").length>0){
						String[] ids = Admin_st_map.get(key).toString().split(",");
						for(int j=0;j<ids.length;j++){
							com_sql = "select com_name from sys_company where pkid = '"+ids[j]+"'";
							SQLAdapter com_sql_Adapter = new SQLAdapter(com_sql);
							List<Map> com_list =  this.getBySqlMapper.findRecords(com_sql_Adapter);
							if(com_list.size()>0){
								com_name += com_list.get(0).get("com_name").toString()+",";
							}	
						}
					}
					
					val.put(key, Admin_st_map.get(key));
				}
				//覆盖原来数据库中查询的一个村的错误数据
				if(val.get("com_name")!=null){
					val.put("com_name", com_name.substring(0, com_name.length()-1));
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
	//添加帮扶单位
	public ModelAndView addBfdw(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String add_bfdw_mc = request.getParameter("add_bfdw_mc");
		
		String add_bfdw_ldxm = request.getParameter("add_bfdw_ldxm");
		String add_bfdw_dz = request.getParameter("add_bfdw_dz");
		String add_bfdw_lddh = request.getParameter("add_bfdw_lddh");
		String add_qixian = "";
		if(request.getParameter("add_qixian")!=null&&!request.getParameter("add_qixian").equals("请选择")){
			 add_qixian = request.getParameter("add_qixian");
		}else{
			add_qixian = "";
		}
		String cha_gcc = "null";

		if(request.getParameter("cha_gcc_ids")!=null&&!request.getParameter("cha_gcc_ids").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc_ids").trim();
		}
		String arr[] = cha_gcc.split(",");
		
		String Sql = "insert into da_company (sys_company_id,v1,v2,v3,v4,v5) values('"+add_qixian+"','"+add_bfdw_mc+"','"+add_bfdw_dz+"','"+add_bfdw_ldxm+"','"+add_bfdw_lddh+"','"+cha_gcc+"')";
		
		
		try{
			if(arr.length>1){
				for(int i=0;i<arr.length;i++){
					Sql = "insert into da_company (sys_company_id,v1,v2,v3,v4,v5) values('"+add_qixian+"','"+add_bfdw_mc+"','"+add_bfdw_dz+"','"+add_bfdw_ldxm+"','"+add_bfdw_lddh+"','"+arr[i]+"')";
					SQLAdapter people_Adapter = new SQLAdapter(Sql);
					this.getBySqlMapper.insertSelective(people_Adapter);
				}
			}else{
				SQLAdapter people_Adapter = new SQLAdapter(Sql);
				this.getBySqlMapper.insertSelective(people_Adapter);
			}
			
			String hou_sql = "select max(pkid) from da_company order by pkid desc";
			SQLAdapter hou_Adapter = new SQLAdapter(hou_sql);
			int da_household_id = this.getBySqlMapper.findrows(hou_Adapter);
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_company',"+da_household_id+",'添加',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶单位','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			//				e.printStackTrace();
			response.getWriter().write("0");
		}
		return null;
	}

	//修改帮扶单位
	public ModelAndView upBfdw(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String id = request.getParameter("pkid").toString();
		String up_bfdw_mc = request.getParameter("up_bfdw_mc").toString();//单位 名称
		String up_qixian = request.getParameter("up_qixian").toString();//旗县
		String up_bfdw_ldxm = request.getParameter("up_bfdw_ldxm").toString();//领导姓名
		String up_bfdw_dz = request.getParameter("up_bfdw_dz").toString();//地址
		String up_bfdw_lddh = request.getParameter("up_bfdw_lddh").toString();//领导电话
		
		
		String up_bfdw_mc2 = request.getParameter("up_bfdw_mc2")+"";//单位 名称
		String up_qixian2 = request.getParameter("up_qixian2")+"";//旗县
		String up_bfdw_ldxm2 = request.getParameter("up_bfdw_ldxm2")+"";//领导姓名
		String up_bfdw_dz2 = request.getParameter("up_bfdw_dz2")+"";//地址
		String up_bfdw_lddh2 = request.getParameter("up_bfdw_lddh2");//领导电话
		
		String cha_gcc = "null";
		if(request.getParameter("cha_gcc_ids")!=null&&!request.getParameter("cha_gcc_ids").equals("请选择")){
			cha_gcc = request.getParameter("cha_gcc_ids").trim();
		}
		String ids[] = cha_gcc.split(",");
		String Sql = "update da_company set sys_company_id='"+up_qixian+"', v1='"+up_bfdw_mc+"', v2='"+up_bfdw_dz+"',v3='"+up_bfdw_ldxm+"',v4='"+up_bfdw_lddh+"',v5='"+cha_gcc+"' where pkid="+id;
	
		
		
		SQLAdapter people_Adapter = new SQLAdapter(Sql);
		try{
			if(ids.length>1){
			//删除原来记录 重新插入新数据
				Sql = "delete from da_company where v1='"+up_bfdw_mc2+"' and v3='"+up_bfdw_ldxm2+"' and v4='"+up_bfdw_lddh2+"' and v2='"+up_bfdw_dz2+"' and sys_company_id ='"+up_qixian2+"'";
				this.getBySqlMapper.deleteSelective(new SQLAdapter(Sql));
				for(int i=0;i<ids.length;i++){
					if(i==0){
						Sql = "insert da_company(pkid,sys_company_id,v1,v2,v3,v4,v5) VALUES('"+id+"','"+up_qixian+"','"+up_bfdw_mc+"','"+up_bfdw_dz+"','"+up_bfdw_ldxm+"','"+up_bfdw_lddh+"','"+ids[i]+"')";
						this.getBySqlMapper.insertSelective(new SQLAdapter(Sql));
					}else{
						Sql = "insert da_company(sys_company_id,v1,v2,v3,v4,v5) VALUES('"+up_qixian+"','"+up_bfdw_mc+"','"+up_bfdw_dz+"','"+up_bfdw_ldxm+"','"+up_bfdw_lddh+"','"+ids[i]+"')";
						this.getBySqlMapper.insertSelective(new SQLAdapter(Sql));
					}
				}
			}else{
				Sql = "delete from da_company where v1='"+up_bfdw_mc2+"' and v3='"+up_bfdw_ldxm2+"' and v4='"+up_bfdw_lddh2+"' and v2='"+up_bfdw_dz2+"' and sys_company_id ='"+up_qixian2+"'";
				this.getBySqlMapper.deleteSelective(new SQLAdapter(Sql));
				Sql = "insert da_company(pkid,sys_company_id,v1,v2,v3,v4,v5) VALUES('"+id+"','"+up_qixian+"','"+up_bfdw_mc+"','"+up_bfdw_dz+"','"+up_bfdw_ldxm+"','"+up_bfdw_lddh+"','"+cha_gcc+"')";
				this.getBySqlMapper.insertSelective(new SQLAdapter(Sql));
			}
			
			
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_company',"+id+",'修改',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶单位','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch (Exception e){
			//				e.printStackTrace();
			response.getWriter().write("0");
		}
		return null;
	}
	//删除帮扶单位
	public ModelAndView getDeleteBfdw(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String pkid = request.getParameter("pkid");
	    String sql = "select * from da_company where pkid ="+pkid;
	    List<Map> info = this.getBySqlMapper.findRecords(new SQLAdapter(sql));
		String up_bfdw_mc = info.get(0).get("v1").toString();//单位 名称
		String up_qixian = info.get(0).get("sys_company_id").toString();//旗县
		String up_bfdw_ldxm = info.get(0).get("v3").toString();//领导姓名
		String up_bfdw_dz = info.get(0).get("v2").toString();//地址
		String up_bfdw_lddh = info.get(0).get("v4").toString();//领导电话
			sql = "delete from da_company where v1='"+up_bfdw_mc+"' and v3='"+up_bfdw_ldxm+"' and v4='"+up_bfdw_lddh+"' and v2='"+up_bfdw_dz+"' and sys_company_id ='"+up_qixian+"'";
		SQLAdapter del_sql = new SQLAdapter(sql);
		try{
//			List<Map> Admin_st_List = this.getBySqlMapper.findRecords(del_sql);
			this.getBySqlMapper.deleteSelective(del_sql);
			HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			if(session.getAttribute("Login_map")!=null){//验证session不为空
				Map<String,String> Login_map = (Map)session.getAttribute("Login_map");//用户的user表内容
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String hql1="insert into da_record(record_table,record_pkid,record_type,record_p_t,record_phone,record_name,record_time,record_mou_1,record_mou_2)"+
						" VALUES ('da_company',"+pkid+",'删除',2,'','"+Login_map.get("col_account")+"','"+simpleDate.format(new Date())+"','帮扶单位','')";
				SQLAdapter hqlAdapter1 =new SQLAdapter(hql1);
				this.getBySqlMapper.findRecords(hqlAdapter1);
			}
			
			response.getWriter().write("1");
		}catch(Exception e){
			response.getWriter().write("0");
		}
		return null;
	}
	
	
		
/**
 * 
 * @description 
 * @method  setYear
 * @param　request
 * @author luoshuai 
 * @date 2017年6月6日 下午4:28:34
 *
 */
	public ModelAndView setYear(HttpServletRequest request,HttpServletResponse response) throws Exception{
			String entry_year = request.getParameter("entry_year");
			String start_time = request.getParameter("start_time");
			String end_time = request.getParameter("end_time");

			String	sql = "update sys_user set start_time = '"+start_time+"',end_time = '"+end_time+"',entry_year = '"+entry_year+"' where pkid = 1";
			SQLAdapter set_year_sql = new SQLAdapter(sql);
			try{
//				List<Map> Admin_st_List = this.getBySqlMapper.findRecords(del_sql);
				this.getBySqlMapper.updateSelective(set_year_sql);
				response.getWriter().write("1");
			}catch(Exception e){
				response.getWriter().write("0");
			}
			return null;
		}
}

