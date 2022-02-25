package com.poscoict.postech.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.poscoict.postech.service.CodeService;

@Controller
public class CodeControllor {
	@Autowired
	CodeService codeService;

	@RequestMapping(value = "/pythonProcess", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> pythonProcess(@RequestBody Map<String, Object> params)	{
		return codeService.pythonProcess(params);
	}
	@RequestMapping(value = "/selectMenuProperties", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectMenuProperties(@RequestBody Map<String, Object> params)	{
		return codeService.selectMenuProperties(params);
	}
	
	@RequestMapping(value = "/selectMenuMetaCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectMenuMetaCode(@RequestBody Map<String, Object> params)	{
		return codeService.selectMenuMetaCode(params);
	}
	
	@RequestMapping(value = "/selectLogCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectLogCode(@RequestBody Map<String, Object> params)	{
		return codeService.selectLogCode(params);
	}
	
	@RequestMapping(value = "/selectCodeTypeInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectCodeTypeInfo(@RequestBody Map<String, Object> params)	{
		return codeService.selectCodeTypeInfo(params);
	}
	
	@RequestMapping(value = "/selectCodeTypeInfoDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectCodeTypeInfoDetail(@RequestBody Map<String, Object> params)	{
		return codeService.selectCodeTypeInfoDetail(params);
	}
	
	@RequestMapping(value = "/selectMenuMetas", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Map<String, String>> selectMenuMetas()	{
		return codeService.selectMenuMetas();
	}
	
	@RequestMapping(value = "/selectCompanys", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Map<String, String>> selectCompanys()	{
		return codeService.selectCompanys();
	}
	
	@RequestMapping(value = "/selectMenuByCompany", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectMenuByCompany(@RequestBody Map<String, String> params)	{
		return codeService.selectMenuByCompany(params);
	}
	
	@RequestMapping(value = "/selectMenuDetailsByCompanyAndMenu", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectMenuDetailsByCompanyAndMenu(@RequestBody Map<String, String> params)	{
		return codeService.selectMenuDetailsByCompanyAndMenu(params);
	}

	@RequestMapping(value = "/selectMetaListByMetas", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectMetaListByMetas(@RequestBody Map<String, String> params)	{
		return codeService.selectMetaListByMetas(params);
	}
	
	@RequestMapping(value = "/selectMenucodeByMeta", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectMenucodeByMeta(@RequestBody Map<String, String> params)	{
		return codeService.selectMenucodeByMeta(params);
	}
	
	@RequestMapping(value = "/selectNotExistMenucodeByMeta", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectNotExistMenucodeByMeta(@RequestBody Map<String, String> params)	{
		return codeService.selectNotExistMenucodeByMeta(params);
	}
	
	@RequestMapping(value = "/selectCompanyByMetaMenu", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectCompanyByMetaMenu(@RequestBody Map<String, Object> params)	{
		return codeService.selectCompanyByMetaMenu(params);
	}
	
	@RequestMapping(value = "/selectNotExistCompanyByMetaMenu", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectNotExistCompanyByMetaMenu(@RequestBody Map<String, Object> params)	{
		return codeService.selectNotExistCompanyByMetaMenu(params);
	}
	
	@RequestMapping(value = "/selectMenuDuplication", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectMenuDuplication(@RequestBody Map<String, Object> params)	{
		return codeService.selectMenuDuplication(params);
	}
	
	@RequestMapping(value = "/updateMetaValues", method = RequestMethod.POST)
	@ResponseBody
	public void updateMetaValues(@RequestBody Map<String, Object> params)	{
		codeService.updateMetaValues(params);
	}
	
	@RequestMapping(value = "/insertMetaToMenu", method = RequestMethod.POST)
	@ResponseBody
	public void insertMetaToMenu(@RequestBody Map<String, Object> params)	{
		codeService.insertMetaToMenu(params);
	}
	
	@RequestMapping(value = "/insertMenu", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> insertMenu(@RequestBody Map<String, Object> params)	{
		return codeService.insertMenu(params);
	}
	
	@RequestMapping(value = "/insertMenuMeta", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> insertMenuMeta(@RequestBody Map<String, Object> params)	{
		return codeService.insertMenuMeta(params);
	}
	
	@RequestMapping(value = "/insertLogCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> insertLogCode(@RequestBody Map<String, Object> params)	{
		return codeService.insertLogCode(params);
	}
	
	@RequestMapping(value = "/insertCodeTypeInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> insertCodeTypeInfo(@RequestBody Map<String, Object> params)	{
		return codeService.insertCodeTypeInfo(params);
	}
	
	@RequestMapping(value = "/insertCodeTypeInfoDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> insertCodeTypeInfoDetail(@RequestBody Map<String, Object> params)	{
		return codeService.insertCodeTypeInfoDetail(params);
	}
	
	@RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateMenu(@RequestBody Map<String, Object> params)	{
		return codeService.updateMenu(params);
	}
	
	@RequestMapping(value = "/updateMenuMeta", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateMenuMeta(@RequestBody Map<String, Object> params)	{
		return codeService.updateMenuMeta(params);
	}
	
	@RequestMapping(value = "/updateLogCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateLogCode(@RequestBody Map<String, Object> params)	{
		return codeService.updateLogCode(params);
	}
	
	@RequestMapping(value = "/updateCodeTypeInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateCodeTypeInfo(@RequestBody Map<String, Object> params)	{
		return codeService.updateCodeTypeInfo(params);
	}
	
	@RequestMapping(value = "/updateCodeTypeInfoDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateCodeTypeInfoDetail(@RequestBody Map<String, Object> params)	{
		return codeService.updateCodeTypeInfoDetail(params);
	}
	
	@RequestMapping(value = "/deleteMenu", method = RequestMethod.POST)
	@ResponseBody
	public void deleteMenu(@RequestBody Map<String, Object> params)	{
		codeService.deleteMenu(params);
	}
	
	@RequestMapping(value = "/deleteMenuMeta", method = RequestMethod.POST)
	@ResponseBody
	public void deleteMenuMeta(@RequestBody Map<String, Object> params)	{
		codeService.deleteMenuMeta(params);
	}
	
	@RequestMapping(value = "/deleteLogCode", method = RequestMethod.POST)
	@ResponseBody
	public void deleteLogCode(@RequestBody Map<String, Object> params)	{
		codeService.deleteLogCode(params);
	}
	
	@RequestMapping(value = "/deleteCodeTypeInfo", method = RequestMethod.POST)
	@ResponseBody
	public void deleteCodeTypeInfo(@RequestBody Map<String, Object> params)	{
		codeService.deleteCodeTypeInfo(params);
	}
	
	@RequestMapping(value = "/deleteCodeTypeInfoDetail", method = RequestMethod.POST)
	@ResponseBody
	public void deleteCodeTypeInfoDetail(@RequestBody Map<String, Object> params)	{
		codeService.deleteCodeTypeInfoDetail(params);
	}
	
	@RequestMapping(value = "/deleteMetaInMenu", method = RequestMethod.POST)
	@ResponseBody
	public void deleteMetaInMenu(@RequestBody Map<String, Object> params)	{
		codeService.deleteMetaInMenu(params);
	}
	
	@RequestMapping("/fileUpload")
	@ResponseBody
	public Map<String,Object> fileUpload(HttpServletRequest req, HttpServletResponse rep
) throws FileNotFoundException, IOException {
		MultipartHttpServletRequest mReq = (MultipartHttpServletRequest) req;
		Map<String,Object> resultMap = codeService.fileUpload(mReq);
		return resultMap;
	}

	
	@RequestMapping("/fileDownload")
	@ResponseBody
	public Map<String,Object> fileDownload(HttpServletRequest req, HttpServletResponse rep
) throws FileNotFoundException, IOException {
		Map<String,Object> resultMap = codeService.fileDownload(req, rep);
		return resultMap;
	}
	
	@RequestMapping(value = "/selectTable102", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> selectTable102(@RequestBody Map<String, Object> params)	{
		return codeService.selectTable102(params);
	}
	
}
