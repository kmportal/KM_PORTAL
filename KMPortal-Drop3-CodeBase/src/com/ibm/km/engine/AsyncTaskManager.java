package com.ibm.km.engine;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class AsyncTaskManager {

	private static AsyncTaskManager resDealsCode = null;
	private final ExecutorService es = Executors.newCachedThreadPool();

	public AsyncTaskManager() {
		
	}
	
	public static AsyncTaskManager getInstance() {
		if(resDealsCode == null) {
			synchronized (AsyncTaskManager.class) {
				resDealsCode = new AsyncTaskManager();
			}
		}
		return resDealsCode;
	}
	
	public void processHandler(KMHandler processor) {
		es.submit(processor);
	}
	


	
	public void shutDownAll(){
		es.shutdown();
	}
}