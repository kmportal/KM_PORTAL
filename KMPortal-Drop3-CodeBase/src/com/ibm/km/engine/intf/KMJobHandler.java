package com.ibm.km.engine.intf;

import java.util.concurrent.Callable;



/**
 * Interface for the handlers, each handler is specific to particluar type of 
 * Task it needs to be perform
 * @verion 0.1
 */
public interface KMJobHandler extends Callable<Boolean>{
   
    

}
