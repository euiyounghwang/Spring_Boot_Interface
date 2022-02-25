package com.poscoict.postech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poscoict.postech.service.MonitoringService;

@Controller
class SearchEngineController {
	@Autowired
	MonitoringService monitoringService;
	
//	@Value("${server.running.flag}")
//	private String server_running_flag = "";
//	
//	@Value("${local.search_engine}")
//	private String local_elasticsearch = "";
	@RequestMapping(value = "/getSearchEngineStatus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getSearchEngineStatus(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
//		System.out.println("getSearchEngineStatus");
		return monitoringService.getSearchEngineStatus(params);
	}
	
	@RequestMapping(value = "/getSearchEngineNodeList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getSearchEngineNodeList(@RequestBody Map<String, Object> params)	{
		return monitoringService.getSearchEngineNodeList(params);
	}
	
	@RequestMapping(value = "/getSearchEngineIndicesList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getSearchEngineIndicesList(@RequestBody Map<String, Object> params)	{
		return monitoringService.getSearchEngineIndicesList(params);
	}
}
