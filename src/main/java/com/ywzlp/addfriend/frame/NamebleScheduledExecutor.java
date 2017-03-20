package com.ywzlp.addfriend.frame;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 带名字的线程池
 * @author yuwei
 */
public class NamebleScheduledExecutor extends ScheduledThreadPoolExecutor
		implements NameableExecutorService {
	private final String name;

	public NamebleScheduledExecutor(String name, int corePoolSize,
			ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
