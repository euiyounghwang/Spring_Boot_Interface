package com.poscoict.postech.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalValues {
	
	private static String search_engine_ip = "";
	
//	@Value("${server.running.flag}")
	private static String server_running_flag;
	
//	@Value("${local.search_engine}")
	private static String local_elasticsearch;
	
//	@Value("${server.search_engine}")
	private static String server_elasticsearch;
	
//	@Value("${server.rest_url}")
	private static String server_rest_url;

//	@Value("${deploy.company_code}")
	private static String deploy_company_code;

//	@Value("${server.upload_path}")
	private static String server_upload_path;
	
	
	//private static String url;
	private static String dbxml_path;
	
	private static String query_path;
	
	
/*	@Value("${feeder_db.xml.url}")
	private void setValue_URL(String flag){
		url = flag;
	}*/
	
	@Value("${feeder_db.xml.path}")
	private void setValue_dbxmlPath(String flag){
		dbxml_path = flag;
	}
	
	
	@Value("${ela.query.path}")
	private void setValue_queryPath(String flag){
		query_path = flag;
	}
	
	
	@Value("${server.running.flag}")
	private void setValue_flag(String flag){
		server_running_flag = flag;
	}
	
//	@Value("${local.search_engine}")
//	private void setValue_local(String flag){
//		local_elasticsearch = flag;
//	}
	
	@Value("${server.search_engine}")
	private void setValue_server(String flag){
		search_engine_ip = flag;
	}
	
	@Value("${deploy.company_code}")
	private void setValue_company_code(String flag){
		deploy_company_code = flag;
	}
	
	
/*	public static String xmlURL() {
		return url;
	}*/

	public static String xmlPath() {
		return dbxml_path;
	}
	
		
	public static String getSearchEngineInfo() {
//		System.out.println("getSearchEngineInfo " + server_running_flag);
//		System.out.println("getSearchEngineInfo " + local_elasticsearch);
//		System.out.println("getSearchEngineInfo " + server_elasticsearch);
//		if (server_running_flag.equals("Y")) {
//			search_engine_ip = server_elasticsearch;
//		}
//		else {
//			search_engine_ip = local_elasticsearch;
//		}
		return search_engine_ip;
	}
	
	
	public static String get_query_path() {
		return query_path;
	}
	
	public static String getCompanyCode() {
		return deploy_company_code;
	}
	
	
}