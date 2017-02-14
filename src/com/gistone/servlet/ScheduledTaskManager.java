package com.gistone.servlet;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class ScheduledTaskManager {
	/**
	 * 定时任务
	 */
//	@Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
	@Scheduled(fixedRate= 1000*60)
	public void statusCheck() {
		System.out.println("一分钟");
		
	}
	

    /**
     * 心跳更新。启动时执行一次，之后每隔1分钟执行一次
     */
    @Scheduled(fixedRate = 1000*60*1)
    public void heartbeat() {
        System.out.println("心跳更新... " + new Date());
    }

    /**
     * 卡点持久化。启动时执行一次，之后每隔2分钟执行一次
     */
    @Scheduled(fixedRate = 1000*60*2)
    public void persistRecord() {
        System.out.println("卡点持久化... " + new Date());
    }

}
