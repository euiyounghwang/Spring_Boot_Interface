package com.poscoict.postech.door;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.*;


public class InterfaceFilter implements Filter{
	
	private static Logger logger = (Logger) LoggerFactory.getLogger(InterfaceFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("init CORSFilter");
		System.out.println("init CORSFilter");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
//		System.out.println("@@@@In Filter@@@");
//		chain.doFilter(request, response);
		 HttpServletResponse res = (HttpServletResponse) response;
		 res.setHeader("Access-Control-Allow-Origin", "*");
		 res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		 res.setHeader("Access-Control-Max-Age", "3600");
		 res.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	   chain.doFilter(request, res);

//		System.out.println("@@@@Out Filter@@@");
	}
	
	@Override
	public void destroy() {
		logger.info("destroy CORSFilter");
		System.out.println("destroy CORSFilter");
	}

}


