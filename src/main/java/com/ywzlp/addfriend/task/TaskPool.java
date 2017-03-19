package com.ywzlp.addfriend.task;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 任务池
 * @author yuwei
 */
public class TaskPool {
	
	private static final Logger log = LoggerFactory.getLogger(TaskPool.class);
	
	private static ConcurrentLinkedQueue<Long> queue;
	
	static {
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public static void submit(Long task) {
		log.info("成功导入账号 " + task);
		queue.offer(task);
	}

	public static void submit(Collection<Long> tasks) {
		queue.addAll(tasks);
	}

	public static long getTaskNum() {
		return queue.size();
	}

	public static Long getTask() {
		return queue.poll();
	}
	
}
