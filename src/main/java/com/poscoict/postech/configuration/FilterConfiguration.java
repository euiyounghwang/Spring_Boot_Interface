package com.poscoict.postech.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poscoict.postech.door.InterfaceFilter;


@Configuration
public class FilterConfiguration implements WebMvcConfigurer {
	
	@Bean
	public FilterRegistrationBean<InterfaceFilter> addFilter() {
		FilterRegistrationBean<InterfaceFilter> regBean = new FilterRegistrationBean<InterfaceFilter>(new InterfaceFilter());
		regBean.addUrlPatterns("/*");
		return regBean;
	}
	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		// TODO Auto-generated method stub
//		registry.addInterceptor(new StudyInterceptor()).addPathPatterns("/**");
////		WebMvcConfigurer.super.addInterceptors(registry);
//	}
	

}