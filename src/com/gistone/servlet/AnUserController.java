package com.gistone.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.gistone.mybatis.inter.GetBySqlMapper;
import com.gistone.mybatis.model.SQLAdapter;



public class AnUserController  extends MultiActionController{
	@Autowired
	private GetBySqlMapper getBySqlMapper;

	
	public ModelAndView anGetPk(HttpServletRequest request,HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String pkid=request.getParameter("pkid");//aac001
//		String acid=pkid;//aac001
//		String code="";//地区编码
		String da_household_id = "";//户主id
		String sql="select pkid,v2,v3,v4,v5,v6,v7,v8,v9,v11,v12,v13,v14,v15,v16,v18,v19,sys_standard,v22,v23,v29,v33 from da_household where pkid="+pkid+" group by pkid";
		
		SQLAdapter Adapter_1 = new SQLAdapter(sql);
		List<Map> list=getBySqlMapper.findRecords(Adapter_1);
		//户主信息
		JSONArray jsonArray1 =new JSONArray();

		for(Map val:list){
			JSONObject obj=new JSONObject ();
			//code=val.get("CODE").toString();
			da_household_id=val.get("pkid").toString();

			obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
			obj.put("v2", val.get("v2")==null?"":val.get("v2"));//盟市
			obj.put("v3", val.get("v3")==null?"":val.get("v3"));//旗县
			obj.put("v4", val.get("v4")==null?"":val.get("v4"));//旗县
			obj.put("v5", val.get("v5")==null?"":val.get("v5"));//嘎查村
			obj.put("v6", val.get("v6")==null?"":val.get("v6"));//姓名
			obj.put("v7",val.get("v7")==null?"":val.get("v7"));//性别
			obj.put("v9",val.get("v9")==null?"":val.get("v9"));//人数
			obj.put("v11",val.get("v11")==null?"":val.get("v11"));//民族
			obj.put("v12",val.get("v12")==null?"":val.get("v12"));//文化程度
			obj.put("v13",val.get("v13")==null?"":val.get("v13"));//在校生状况
			obj.put("v14",val.get("v14")==null?"":val.get("v14"));//健康状况
			obj.put("v15",val.get("v15")==null?"":val.get("v15"));//劳动能力
			obj.put("v16",val.get("v16")==null?"":val.get("v16"));//务工情况
			obj.put("v17",val.get("V17")==null?"":val.get("V17"));//务工时间
			obj.put("v18",val.get("v18")==null?"":val.get("v18"));//是否参加新农合
			obj.put("v19",val.get("v19")==null?"":val.get("v19"));//是否参加新型养老保险
			obj.put("sys_standard",val.get("sys_standard")==null?"":val.get("sys_standard"));//识别标准
			obj.put("v22",val.get("v22")==null?"":val.get("v22"));//贫苦户属性
			obj.put("v23",val.get("v23")==null?"":val.get("v23"));//主要致贫原因
			obj.put("v29",val.get("v29")==null?"":val.get("v29"));//是否军烈属
			obj.put("v33",val.get("v33")==null?"":val.get("v33"));//其他致贫原因
			jsonArray1.add(obj);

		}
		//家庭成员
		JSONArray jsonArray2 =new JSONArray();
		String xian_sql="select pkid,v6,v7,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19 from da_member where da_household_id="+da_household_id+" group by pkid";
		SQLAdapter Adapter_2 = new SQLAdapter(xian_sql);
		List<Map> xian_list=this.getBySqlMapper.findRecords(Adapter_2);
		if(!xian_list.isEmpty()){
			if(xian_list.get(0) != null){
				for(Map val:xian_list){
					JSONObject obj=new JSONObject ();
					obj.put("cy_pkid",val.get("pkid")==null?"":val.get("pkid"));
					obj.put("cy_v6",val.get("v6")==null?"":val.get("v6"));//姓名
					obj.put("cy_v7",val.get("v7")==null?"":val.get("v7"));//性别
					obj.put("cy_v10",val.get("v10")==null?"":val.get("v10"));//与户主关系
					obj.put("cy_v11",val.get("v11")==null?"":val.get("v11"));//民族
					obj.put("cy_v12",val.get("v12")==null?"":val.get("v12"));//文化程度
					obj.put("cy_v13",val.get("v13")==null?"":val.get("v13"));//在校生状况
					obj.put("cy_v14",val.get("v14")==null?"":val.get("v14"));//健康状况
					obj.put("cy_v15",val.get("v15")==null?"":val.get("v15"));//劳动能力
					obj.put("cy_v16",val.get("v16")==null?"":val.get("v16"));//务工情况
					obj.put("cy_v17",val.get("v17")==null?"":val.get("v17"));//务工时间
					obj.put("cy_v18",val.get("v18")==null?"":val.get("v18"));//是否参加新农合
					obj.put("cy_v19",val.get("v19")==null?"":val.get("v19"));//是否参加新型养老保险
					//			obj.put("cy_pic_path",val.get("PIC_PATH")==null?"":val.get("PIC_PATH"));//头像 // 家庭成员头像暂时没有
					jsonArray2.add(obj);
				}
			}
		}

		//走访情况
		JSONArray jsonArray3 =new JSONArray();
		String zoufang_sql = "SELECT v1,v2,v3,group_concat(pic_path order by pic_path separator ',') path FROM (" +
							"SELECT *  FROM da_help_visit a LEFT JOIN (SELECT pic_path,pic_pkid from da_pic WHERE pic_type=2 ) b ON a.pkid=b.pic_pkid "+
							" WHERE a.da_household_id="+da_household_id+" )aa GROUP BY pkid ORDER BY v1 DESC";
		SQLAdapter Adapter_4 = new SQLAdapter(zoufang_sql);
		List<Map> sjz_list=getBySqlMapper.findRecords(Adapter_4);
		if(!sjz_list.isEmpty()){
			if(sjz_list.get(0)!=null){
				for(Map val:sjz_list){
					JSONObject sjz_obj=new JSONObject ();
					//sjz_obj.put("pkid",val.get("pkid")==null?"":val.get("pkid"));
					sjz_obj.put("v1",val.get("v1")==null?"":val.get("v1"));
					sjz_obj.put("v2",val.get("v2")==null?"":val.get("v2"));
					sjz_obj.put("v3",val.get("v3")==null?"":val.get("v3"));
					sjz_obj.put("pie",val.get("path")==null?"":val.get("path"));//帮扶目标
					jsonArray3.add(sjz_obj);
				}
			}
		}

		response.getWriter().write("{\"data1\":"+jsonArray1.toString() +
				",\"data2\":"+jsonArray2.toString()+",\"data3\":"+jsonArray3.toString()+"}");
		response.getWriter().close();
		return null;
	}
}
