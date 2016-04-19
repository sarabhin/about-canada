package com.cognizant.example;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HelloExample{	
	final static Logger logger = Logger.getLogger(HelloExample.class);
	
	public static void main(String[] args) {
	
		HelloExample obj = new HelloExample();
		obj.runMe("mkyong");
		Class<?> cl=new Object(){}.getClass().getEnclosingClass();
		
		Properties props = new Properties();
		try {
			props.load(cl.getResourceAsStream("log4j.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertyConfigurator.configure(props);
	}
	
	private void runMe(String parameter){
		
		if(logger.isDebugEnabled()){
			logger.debug("This is debug : " + parameter);
		}
		
		if(logger.isInfoEnabled()){
			logger.info("This is info : " + parameter);
		}
		
		logger.warn("This is warn : " + parameter);
		logger.error("This is error : " + parameter);
		logger.fatal("This is fatal : " + parameter);
		
	}
	
}