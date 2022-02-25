package com.poscoict.postech.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.slf4j.*;

import com.poscoict.postech.constants.GlobalValues;
import com.poscoict.postech.constants.WebConstants;

import net.sf.json.JSONObject;

/**
 * @fileName : EngineQueryVO.java
 * @author : Mr.frodo
 * @date : 2016.11.10
 * @version : 1.0
 * @description : 통합검색 검색연계 표준가이드 Elastic Query에 필요한 bean object
 */
public class EngineProviderQueryVO {
//	private static Logger logger = Logger.getLogger(EngineProviderQueryVO.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(EngineProviderQueryVO.class);
	
	private String keyword; // 검색 키워드
	private String searchAuth; // 사용자 검색 권한
	private String detailParam; // 상세 검색 조건
	private String dataType; // 넘겨줄 데이타 타입
	private String pageFrom; // 목록 시작 번호
	private String pageSize; // 페이지 목록 개수
	private String fieldsAuth; // 필드쿼리 권한
	private String sysId; // I/F를 호출하는 시스템ID posco_posapia, posco_e-catalog
	private String comCode; //I/F를 호출하는 시스템의 회사코드 (포스코 30)
	private String systemName; //검색로그 insert용 메뉴xml에 정의된 kor_name값
	private String prefixType; //특허 메뉴 코드
	private String operator; //must(AND검색)/should(OR검색)
	private String reverse_operator; //should(AND검색)/must(OR검색)
	private String engineUrl; // 검색 엔진 주소
	private String queryPath;  // 시스템별 쿼리 XML 경로
		
	private ESUser user; // 검색한 사용자 정보
	private JSONObject json; // 호출URL q값 전체
	private JSONObject json_common; //호출URL q값 내 common
	private JSONObject json_condition; //호출URL q값 내 condition
	private JSONObject json_reverse_condition; //호출URL q값 내 condition 안에 있는 reverse_condition
	private JSONObject json_sort; //호출URL q값 내 sort
	
	private String resultData; // 검색 결과값

	private Map<String, String> mapQuery = new HashMap<String, String>(); // xml 파일의 쿼리 목록
	private Map<String, String> mapQueryArea = new HashMap<String, String>(); // q값 내 변수 별로 field에 정의된 query_format을 받아서 query-area로 지정된 Query들을 셋팅, 
	private Map<String, String> mapReverseQueryArea = new HashMap<String, String>(); // q값 내 변수 별로 field에 정의된 query_format을 받아서 reverse-query-area로 지정된 Query들을 셋팅, 
	private Map<String, String> mapFilterArea = new HashMap<String, String>(); // q값 내 변수 별로 field에 정의된 query_format을 받아서 filter-area로 지정된 Query들을 셋팅,
	private String sortArea = "";
	
	
	
	public EngineProviderQueryVO(ESUser user, JSONObject obj) throws Exception {
		logger.info("##### EngineProviderQueryVO START #####");
		this.user = user;
		this.json = obj;

		if(this.json.has("common")){
			this.json_common = this.json.getJSONObject("common");
		}
		if(this.json.has("condition")){
			this.json_condition = this.json.getJSONObject("condition");
		}
		if(this.json_condition.has("reverse_condition")){
			this.json_reverse_condition = this.json_condition.getJSONObject("reverse_condition");
		}
		if(this.json.has("sort")){
			this.setJson_sort(this.json.getJSONObject("sort"));
		}

		this.keyword = (this.json_common.has("es_keyword") ? this.json_common.getString("es_keyword") : "");
		this.pageSize = (this.json_common.has("es_num") ? this.json_common.getString("es_num") : "15");		
		this.sysId = this.json_common.has("es_sys_id")&&!this.json_common.getString("es_sys_id").equals("null")&&!this.json_common.getString("es_sys_id").equals("") ? this.json_common.getString("es_sys_id") : "posco_posapia";
		this.comCode = this.json_common.has("es_comp_cd")&&!this.json_common.getString("es_comp_cd").equals("null")&&!this.json_common.getString("es_comp_cd").equals("") ? this.json_common.getString("es_comp_cd") : "30";
		this.prefixType = this.json_common.has("es_prefix_type")&&!this.json_common.getString("es_prefix_type").isEmpty()? this.json_common.getString("es_prefix_type")+"." : "";
		
		String es_operator = this.json_common.has("es_operator")? this.json_common.getString("es_operator") : "";
		setOperator(es_operator);
		setReverse_operator(es_operator);

		this.setPageFrom();
		this.setDetailParam();
		
		setEngineUrl(WebConstants.ELA_URI);
		setQueryPath(GlobalValues.get_query_path());
		//logger.info("engineUrl="+this.engineUrl);
		//logger.info("queryPath="+this.queryPath);
		logger.info("##### EngineProviderQueryVO END #####");
	}

	public String getKeyword() {
		return this.keyword;
	}
	public String getSearchAuth() {
		return this.searchAuth;
	}
	public String getEngineUrl() {
		return this.engineUrl;
	}
	public void setEngineUrl(String engineUrl) {
		this.engineUrl = engineUrl;
	}
	public String getQueryPath() {
		logger.info("getQueryPath >> "  + this.queryPath);
		return this.queryPath;
	}
	public void setQueryPath(String queryPath) {
		this.queryPath = queryPath;
	}
	public String getDetailParam() {
		return this.detailParam;
	}
	/** DB에 저장하기 위한 상세검색 조건을 추출 후 저장한다. */
	@SuppressWarnings("unchecked")
	public void setDetailParam() {
		StringBuffer sb = new StringBuffer();
		Iterator<Object> keys = this.json_condition.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if(!this.json_condition.getString(key).isEmpty()){
				sb.append(key).append(":").append(this.json_condition.getString(key)).append(";");	
			}
		}
		if (sb.length() > 0) {
			this.detailParam = sb.toString().substring(0, sb.toString().length() -1);
		} else {
			this.detailParam = "";
		}
	}
	public String getDataType() {
		return this.dataType;
	}
	public String getPageFrom() {
		return this.pageFrom;
	}
	public void setPageFrom() {
		String pageNum = (this.json_common.has("es_page_idx") && !("").equals(this.json_common.getString("es_page_idx")) ? this.json_common.getString("es_page_idx") : "1");
		
		int index = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(this.pageSize);
		this.pageFrom = String.valueOf(index);
	}
	public String getPageSize() {
		return this.pageSize;
	}
	public String getFieldsAuth() {
		return this.fieldsAuth;
	}
	public ESUser getUser() {
		return this.user;
	}
	public JSONObject getJson() {
		return this.json;
	}
	public JSONObject getJson_common() {
		return this.json_common;
	}
	public JSONObject getJson_condition() {
		return this.json_condition;
	}

	public JSONObject getJson_reverse_condition() {
		return json_reverse_condition;
	}

	public String getResultData() {
		return (null != this.resultData ? this.resultData : "");
	}
	public void setResultData(String resultData) {
		this.resultData = resultData;
	}
	public Map<String, String> getMapQuery() {
		return this.mapQuery;
	}
	public Map<String, String> getMapQueryArea() {
		return mapQueryArea;
	}
	public Map<String, String> getMapFilterArea() {
		return mapFilterArea;
	}
	public Map<String, String> getMapReverseQueryArea() {
		return mapReverseQueryArea;
	}
	@Override
	public String toString() {
		return "EngineQueryVO [searchAuth="
				+ searchAuth + ", engineUrl=" + engineUrl + ", queryPath=" + queryPath + ", detailParam="
				+ detailParam + ", dataType=" + dataType + ", pageFrom=" + pageFrom + ", pageSize="
				+ pageSize + ", fieldsAuth=" + fieldsAuth + ", user="
				+ user + ", json=" + json + ", resultData=" + resultData + "]";
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getPrefixType() {
		return prefixType;
	}

	public void setPrefixType(String prefixType) {
		this.prefixType = prefixType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) { //must(AND검색)/should(OR검색)
		String operator_val = "";
		if(operator.equals("AND"))
			operator_val = "must";
		else if(operator.equals("OR"))
			operator_val = "should";	
	
		this.operator = operator_val;
	}

	public String getReverse_operator() {
		return reverse_operator;
	}

	public void setReverse_operator(String operator) { //should(AND검색)/must(OR검색)
		
		String reverse_operator_val = "";
		if(operator.equals("AND"))
			reverse_operator_val = "should";
		else if(operator.equals("OR"))
			reverse_operator_val = "must";
		
		this.reverse_operator = reverse_operator_val;
	}

	public JSONObject getJson_sort() {
		return json_sort;
	}

	public void setJson_sort(JSONObject json_sort) {
		this.json_sort = json_sort;
	}

	public String getSortArea() {
		return sortArea;
	}

	public void setSortArea(String sortArea) {
		this.sortArea = sortArea;
	}

}
