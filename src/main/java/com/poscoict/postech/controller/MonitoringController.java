package com.poscoict.postech.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poscoict.postech.service.MenualDBConnectionService;
import com.poscoict.postech.service.MonitoringService;

@Controller
class MonitoringController {
	
	@Autowired
	MonitoringService monitoringService;
	
	@Autowired
	MenualDBConnectionService menualDBService;
	
//	@Value("${server.running.flag}")
//	private String server_running_flag = "";
//	
//	@Value("${local.search_engine}")
//	private String local_elasticsearch = "";
	
		
	@RequestMapping(value = "/findWasServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findWasServerLog(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		//System.out.println("findWasServerLog");
		return monitoringService.findWasServerLog(params);
	}
	

	@RequestMapping(value = "/selectSqlList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectSqlList(@RequestBody Map<String, Object> params)	{
		return monitoringService.selectSqlList(params);
	}
	
	@RequestMapping(value = "/saveSql", method = RequestMethod.POST)
	@ResponseBody
	public void saveSql(@RequestBody Map<String, Object> params)	{
		monitoringService.saveSql(params);
	}
	
	@RequestMapping(value = "/updateSql", method = RequestMethod.POST)
	@ResponseBody
	public void updateSql(@RequestBody Map<String, Object> params)	{
		monitoringService.updateSql(params);
	}
	
	@RequestMapping(value = "/deleteSql", method = RequestMethod.POST)
	@ResponseBody
	public void deleteSql(@RequestBody Map<String, Object> params)	{
		monitoringService.deleteSql(params);
	}
	
	@RequestMapping(value = "/selectSql", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectSql(@RequestBody Map<String, Object> params)	{
		return monitoringService.selectSql(params);
	}
	
	@RequestMapping(value = "/excuteSql", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> excuteSql(@RequestBody Map<String, Object> params)	{
		return menualDBService.excuteSql(params);
	}
	
	@RequestMapping(value = "/callProvideIF", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> callProvideIF(@RequestBody Map<String, Object> params)	{
		return monitoringService.callProvideIF(params);
	}
	
}
