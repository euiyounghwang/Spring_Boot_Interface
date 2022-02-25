package com.poscoict.postech.configuration;




import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class SampleRunner implements ApplicationRunner {
	
	@Autowired
	private String Profile;
	
	private static Logger logger = (Logger)LoggerFactory.getLogger(SampleRunner.class);
		
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("\n##########################");
		System.out.println("##########################");
		System.out.println(Profile);
		System.out.println("##########################");
		System.out.println("##########################\n");
		logger.info("@@@@@@@@@");
	}

}
