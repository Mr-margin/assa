package com.gistone.mybatis.model;

public class SQLAdapter {
	String sql;  
	  
    public SQLAdapter(String sql) {  
        this.sql = sql;  
    }  
  
    public String getSql() {  
        return sql;  
    }  
  
    public void setSql(String sql) {  
        this.sql = sql;  
    }  
}
