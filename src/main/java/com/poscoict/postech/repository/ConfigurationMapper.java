package com.poscoict.postech.repository;

import java.util.List;
import java.util.Map;

public interface ConfigurationMapper {
	public void saveConfiguration(Map<String, Object> params);
	
	public List<Map<String, String>> selectESAuthDB(Map<String, Object> params);

	public List<Map<String, String>> selectCommonCode(Map<String, String> params);

	public void disabledESAuthDB(Map<String, Object> params);
	
	public void mergeESAuthDB(Map<String, Object> params);
}
