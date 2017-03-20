package com.ywzlp.addfriend.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywzlp.addfriend.authc.QQSession;
import com.ywzlp.addfriend.frame.ExecutorUtil;

/**
 * @author yuwei
 *
 */
public class TaskExecutor implements AddFriends {
	
	private static Logger log = LoggerFactory.getLogger(TaskExecutor.class);
	
	private AtomicInteger sum;
	
	private ScheduledExecutorService executor;
	
	public TaskExecutor() {
		executor = ExecutorUtil.createSheduledExecute("add-user", 200);
		sum = new AtomicInteger(0);
	}

	@Override
	public void submit(final QQSession session) {
		
		log.info("添加 qq: " + session.getUid() + " 执行任务线程");
		
		//一分钟加一个好友
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				Long qq = TaskPool.getTask();
				if (qq != null) {
					log.info(session.getUid() + " 账号正在添加好友  " + qq);
					boolean isSuccess = session.addFriend(qq);
					if (!isSuccess) {
						//如果失败将qq重新加入任务池
						log.info("{} 好友重返队列", qq);
						TaskPool.submit(qq);
					} else {
						log.info("{} 好友请求发送成功, 当前共添加 {} 个好友", qq, sum.incrementAndGet());
					}
				} else {
					log.info("所有任务已经认领完毕...");
				}
			}
		}, 0, 2, TimeUnit.MINUTES);
	}
	
}
