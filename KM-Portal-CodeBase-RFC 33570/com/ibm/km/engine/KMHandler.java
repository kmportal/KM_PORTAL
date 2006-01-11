package com.ibm.km.engine;

import org.apache.log4j.Logger;

import com.ibm.km.engine.exception.LmsException;
import com.ibm.km.engine.intf.KMJobHandler;

public abstract class KMHandler implements KMJobHandler {

	private static Logger kmLogger=Logger.getLogger(KMHandler.class);
	
	public Boolean call() throws Exception {
		kmLogger.info("KMHandler::call");
		process();
		return new Boolean(true);
	}
	
	protected abstract void process() throws LmsException; 
	

}
