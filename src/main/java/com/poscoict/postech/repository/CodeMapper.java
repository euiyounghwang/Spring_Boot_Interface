package com.poscoict.postech.repository;

import java.util.List;
import java.util.Map;

public interface CodeMapper {

	public List<Map<String, String>> selectMenuDetailsByCompanyAndMenu(Map<String, String> params);
	public List<Map<String, String>> selectMenuDetailsByCompanyAndMenu2(Map<String, String> params);
	
	public List<Map<String, String>> selectMenuProperties(Map<String, Object> params);
	
	public List<Map<String, String>> selectMenuMetaCode(Map<String, Object> params);
	
	public List<Map<String, String>> selectLogCode(Map<String, Object> params);
	
	public List<Map<String, String>> selectCodeTypeInfo(Map<String, Object> params);
	
	public List<Map<String, String>> selectCodeTypeInfoDetail(Map<String, Object> params);
	
	public List<Map<String, String>> selectMenuMetas();
	
	public List<Map<String, String>> selectCompanys();

	public List<Map<String, String>> selectMenuByCompany(Map<String, String> params);
	
	public List<Map<String, String>> selectMetaListByMetas(Map<String, String> params);
	
	public List<Map<String, String>> selectMenucodeByMeta(Map<String, String> params);

	public List<Map<String, String>> selectNotExistMenucodeByMeta(Map<String, String> params);

	public List<Map<String, String>> selectCompanyByMetaMenu(Map<String, Object> params);

	public List<Map<String, String>> selectNotExistCompanyByMetaMenu(Map<String, Object> params);
	
	public List<Map<String, String>> selectMenuDuplication(Map<String, Object> params);
	
	public List<Map<String, String>> selectMenuMetaDuplication(Map<String, Object> params);
	
	public List<Map<String, String>> selectLogCodeDuplication(Map<String, Object> params);
	
	public List<Map<String, String>> selectCodeTypeInfoDuplication(Map<String, Object> params);
	
	public List<Map<String, String>> selectCodeTypeInfoDetailDuplication(Map<String, Object> params);
	
	public void updateMetaValues(Map<String, Object> params);
	
	public void insertMetaToMenu(Map<String, Object> params);
	
	public void insertMenu(Map<String, Object> params);
	
	public void insertMenuMeta(Map<String, Object> params);
	
	public void insertLogCode(Map<String, Object> params);
	
	public void insertCodeTypeInfo(Map<String, Object> params);
	
	public void insertCodeTypeInfoDetail(Map<String, Object> params);
	
	public void updateMenu(Map<String, Object> params);
	
	public void updateMenuMeta(Map<String, Object> params);
	
	public void updateLogCode(Map<String, Object> params);
	
	public void updateCodeTypeInfo(Map<String, Object> params);
	
	public void updateCodeTypeInfoDetail(Map<String, Object> params);
	
	public void deleteMenu(Map<String, Object> params);

	public void deleteMenuMeta(Map<String, Object> params);
	
	public void deleteCodeTypeInfo(Map<String, Object> params);
	
	public void deleteCodeTypeInfoDetail(Map<String, Object> params);
	
	public void deleteCodeTypeInfoDetailByFK_CD_TP(Map<String, Object> params);
	
	public void deleteLogCode(Map<String, Object> params);
	
	public void deleteMetaInMenu(Map<String, Object> params);
	
	public List<Map<String, String>> selectCodeInfo(Map<String, String> params);
	
	public int fileInsert(Map<String, Object> params);
	
	public List<Map<String, Object>> selectTable102(Map<String, Object> params);
}
