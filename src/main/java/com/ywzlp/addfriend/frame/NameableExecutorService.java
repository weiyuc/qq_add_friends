package com.ywzlp.addfriend.frame;

import java.util.concurrent.ExecutorService;

public interface NameableExecutorService extends ExecutorService {

	public String getName();
}