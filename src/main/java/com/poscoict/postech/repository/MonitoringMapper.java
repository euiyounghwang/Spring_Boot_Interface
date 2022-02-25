package com.poscoict.postech.repository;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public interface MonitoringMapper {
	
	public List<Map<String, String>> commonCodeList(Map<String, String> params);
	
	public List<Map<String, String>> selectSqlList(Map<String, Object> params);
	
	public void saveSql(Map<String, Object> params);
	
	public void saveSqlParams(Map<String, Object> params);
	
	public Map<String, String> selectMaxSqlObjectId();	
	
	public void deleteSql(Map<String, Object> params);
	
	public void deleteSqlParams(Map<String, Object> params);
	
	public Map<String, String> selectSql(Map<String, Object> params);
	
	public List<Map<String, String>> selectSqlDetail(Map<String, Object> params);

	public List<Map<String, Object>> excuteSqlHeaderSelect(Map<String, Object> params);
	public List<Map<String, Object>> excuteSqlDataTableSelect(Map<String, Object> params);
	public int excuteSqlUpdate(Map<String, Object> params);
	public int excuteSqlDelete(Map<String, Object> params);
	public int excuteSqlInsert(Map<String, Object> params);
	
}
