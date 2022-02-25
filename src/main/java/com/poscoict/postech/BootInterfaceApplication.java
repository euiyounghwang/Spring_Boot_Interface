package com.poscoict.postech;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableScheduling
@ComponentScan
@EnableAutoConfiguration
@MapperScan("com.poscoict.postech.repository")
public class BootInterfaceApplication {

	public static void main(String[] args) {
//		BasicConfigurator.configure();
		SpringApplication.run(BootInterfaceApplication.class, args);
	}

}
