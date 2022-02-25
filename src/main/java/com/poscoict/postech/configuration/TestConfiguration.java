package com.poscoict.postech.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class TestConfiguration {
	
	@Bean
	public String profile_info() {
		return "Profile {dev}";
	}

}