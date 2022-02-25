package com.poscoict.postech.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.poscoict.postech.service.ServermanageService;


@Controller
class ServermanageController {
	@Autowired
	ServermanageService servermanageService;	
	
	@RequestMapping(value = "/getCodeList", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> getCodeList(@RequestBody Map<String, Object> params)	{
		return servermanageService.getCodeList(params);
	}	
	
	@RequestMapping(value = "/findServerList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findServerList(@RequestBody Map<String, Object> params)	{
		return servermanageService.findServerList(params);
	}
	
	@RequestMapping(value = "/insertServerInfo", method = RequestMethod.POST)
	@ResponseBody
	public String insertServerInfo(@RequestBody Map<String, Object> params)	{
		return servermanageService.insertServerInfo(params);
	}
	
	@RequestMapping(value = "/deleteServerInfo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteServerInfo(@RequestBody Map<String, Object> params)	{
		return servermanageService.deleteServerInfo(params);
	}
	
	@RequestMapping(value = "/getConfigData", method = RequestMethod.POST)
	@ResponseBody
	public String getConfigData(@RequestBody Map<String, Object> params)	{
		return servermanageService.getConfigData(params);
	}
	
	@RequestMapping(value = "/getConfigDataAsJson", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getConfigDataAsJson(@RequestBody Map<String, Object> params)	{
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String confData = servermanageService.getConfigData(params);
			
			//System.out.println(confData);
			
			JSONObject jObject = XML.toJSONObject(confData);
		    ObjectMapper mapper = new ObjectMapper();
		    mapper.enable(SerializationFeature.INDENT_OUTPUT);
		    Object json = mapper.readValue(jObject.toString(), Object.class);
			
			result.put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
