package com.gistone.Initialization;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.gistone.util.OverallSituation;

public class CacheBeanPostProcessor implements BeanPostProcessor {

	//在spring中定义的bean初始化后调用这个方法 CacheBeanPostProcessor.do
	@Override
	public Object postProcessAfterInitialization(Object obj, String arg1) throws BeansException {
		try {
        	
        	//System.out.println("在spring中定义的bean初始化后调用这个方法 CacheBeanPostProcessor.do");
        	
        } catch (Exception e) {
             e.printStackTrace();
        }
        return obj;
    }
	
    //在spring中定义的bean初始化前调用这个方法
    @Override
    public Object postProcessBeforeInitialization(Object arg0, String arg1)
            throws BeansException {
        // TODO Auto-generated method stub
    	
    	//System.out.println("在spring中定义的bean初始化前调用这个方法。");
    	
        return arg0;
    }
}
