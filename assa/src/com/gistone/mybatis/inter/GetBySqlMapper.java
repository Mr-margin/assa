package com.gistone.mybatis.inter;

import java.util.List;
import java.util.Map;
import com.gistone.mybatis.model.SQLAdapter;

/**
 * ClassName: GetBySqlMapper 
 * @Description: 通过原始SQL获取数据
 * @author liujianwang
 * @date 2015-9-17
 */
public interface GetBySqlMapper {
	
    List<Map> findRecords(SQLAdapter sQLAdapter);
    
    int findrows(SQLAdapter sQLAdapter);
    
    void insertSelective(SQLAdapter sQLAdapter);
    
    void updateSelective(SQLAdapter sQLAdapter);
    
    void deleteSelective(SQLAdapter sQLAdapter);
}