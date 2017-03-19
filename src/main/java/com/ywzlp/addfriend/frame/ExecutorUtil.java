package com.ywzlp.addfriend.frame;

public class ExecutorUtil {

	public static final NamebleScheduledExecutor createSheduledExecute(
			String name, int size) {
		NameableThreadFactory factory = new NameableThreadFactory(name, true);
		return new NamebleScheduledExecutor(name, size, factory);
	}

}