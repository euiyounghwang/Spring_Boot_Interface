package com.poscoict.postech.controller;

import javax.servlet.http.HttpServletRequest;

//import org.apache.log4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poscoict.postech.service.InterFaceService;
import com.poscoict.postech.util.ESUtil;

@Controller
class InterFaceController {
	@Autowired
	InterFaceService interfaceService;
	
//	private static Logger logger = Logger.getLogger(InterFaceController.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(InterFaceController.class);
    
    /**
     * 검색연계 IF
     * dataProvider?q=cG9zbWV0YTp7CiAgICAiY29tbW9uIjogewogICAgICAgICJlc19jb21wX2NkIjogIjMwIiwKICAgICAgICAiZXNfZW1wX25vIjogIjI5NDU2MiIsCiAgICAgICAgImVzX3VzZXJfaWQiOiAicGQyOTQ1NjIiLAogICAgICAgICJlc19kZXB0X2NkIjogIjNBQ0EwIiwKICAgICAgICAiZXNfYXV0aCI6ICIiLAogICAgICAgICJlc19zeXNfaWQiOiAiY2l0aXplbiIsCiAgICAgICAgImVzX2tleXdvcmQiOiAi6riw7JeF7Iuc66%2B8IiwKICAgICAgICAiZXNfcGFnZV9pZHgiOiAiMSIsCiAgICAgICAgImVzX251bSI6ICIxNSIsCiAgICAgICAgImVzX3ByZWZpeF90eXBlIjogIiIsCiAgICAgICAgImVzX29wZXJhdG9yIjogIkFORCIKICAgIH0sCiAgICAiY29uZGl0aW9uIjogewogICAgICAgICJDQVRFR09SWSI6ICJCMDAwMSIKICAgIH0KfQ%3D%3D
     * */
	@RequestMapping(value = "/dataProvider")
  @ResponseBody
	public String dataProvider(HttpServletRequest request)	{
    	String callback =  ESUtil.getString(request, "callback");
    	String q =  ESUtil.getString(request, "q");
    	logger.info(q);
    	return interfaceService.dataProvider(callback,q);
	}
}
