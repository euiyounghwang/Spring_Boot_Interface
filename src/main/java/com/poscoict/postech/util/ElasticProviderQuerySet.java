package com.poscoict.postech.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.json.JSONObject;

//import org.apache.log4j.Logger;
import org.slf4j.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.poscoict.postech.model.EngineProviderQueryVO;
import com.poscoict.postech.service.InterFaceService;


/**
 * @fileName : ElasticPAPQuerySet.java
 * @author : ysrhee
 * @date : 2020.05.14
 * @version : 1.0
 * @description : 검색엔진에 요청하는 쿼리 정보를 EngineProviderQueryVO 객체에 저장 한다,
 */
public class ElasticProviderQuerySet {
//	private static Logger logger = Logger.getLogger(ElasticProviderQuerySet.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(ElasticProviderQuerySet.class);
	
	protected EngineProviderQueryVO vo;
	
	protected ElasticProviderQuerySet(EngineProviderQueryVO vo) {
		this.vo = vo;
	}
	
	/**
	 * elastic_query.xml에 작성 되어있는 쿼리 정보를 EngineQueryVO 객체에 담아 리턴한다,
	 * @return Node
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	protected synchronized void getSystemQueryInfo() throws Exception {
		logger.info("##### GETSYSTEMQUERYNODE START #####");
		StringBuffer sb =  new StringBuffer();
		BufferedReader br = null;
		
		try{
			//elastic_query.xml을 열어 내용 받아오기
			File file = new File(vo.getQueryPath());
			logger.info("getSystemQueryInfo () >> " + vo.getQueryPath());
			if (file.exists()) {
				logger.info("getSystemQueryInfo file() >> " + file);
				String read = "";
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				
				while ((read = br.readLine()) != null) {
					sb.append(read);			       
				}
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));//elastic_query.xml내용을 Document에 담아둔다.

				vo.setEngineUrl(doc.getElementsByTagName("system_host").item(0).getTextContent() + vo.getEngineUrl());
				
				//Document에서 system태그 하위 정보를 Element에 담아둔다.
				NodeList sysList = doc.getElementsByTagName("system");			
				Element element = (Element) sysList.item(0); 
				/*
				 * elastic_query_POSAPIA.xml에 작성 되어진 쿼리 목록을 가져온다.
				 * <system id="PATT">
				 *     <query></query>
				 *     ....
				 * </system>
				 */			
				NodeList queryList = element.getElementsByTagName("query");
				int querySize = queryList.getLength();
				for (int i=0; i<querySize; i++) {
					Element tmp = (Element) queryList.item(i);
					/*
					 * query 목록을 bean mapQuery 변수에 set 한다.
					 * <query id="query-where">
					 *     "query": {"bool": {<where-area/>}}
					 * </query>
					 * id를 key, content를 value로 저장
					 */
					vo.getMapQuery().put(tmp.getAttribute("id"), tmp.getTextContent().trim());
				}
				
				NodeList fieldList = element.getElementsByTagName("field");

				this.getDetailQuery(vo.getJson_condition(), fieldList, "condition");

				this.getDetailQuery(vo.getJson_common(), fieldList, "common");
				
				if(vo.getJson_reverse_condition()!=null && !vo.getJson_reverse_condition().isEmpty()) this.getDetailQuery(vo.getJson_reverse_condition(), fieldList, "reverse");
				
				if(vo.getJson_sort()!=null && !vo.getJson_sort().isEmpty()) this.getSortQuery(vo.getJson_sort());
				
			} else {
				throw new Exception("쿼리 파일이 존재하지 않습니다.");
			}
			
			logger.info("##### GETSYSTEMQUERYNODE END #####");
			
		} catch (FileNotFoundException fe) {  logger.error(fe.getMessage(), fe.getCause());
		} catch (IOException ie) {            logger.error(ie.getMessage(), ie.getCause());
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}	finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                	logger.error(e.getMessage(), e.getCause());
                }
            }
        }
	}
	
	/**
	 * field 태그 id가 JSONObject key name과 일치하면 해당 field 태그의 데이타 Type을 set 한다.
	 * @param obj
	 * @param nodeList
	 * @param mode = condition, filter, reverse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void getDetailQuery(JSONObject obj_json, NodeList fieldList, String mode) {

		logger.info("##### getDetailQuery START #####");
		//logger.logInfo("(테스트용)"+obj_json);
		int nodeSize = fieldList.getLength();

		Iterator<Object> keys_json = obj_json.keys();
		
		
		while (keys_json.hasNext()) {
			String objKey = (String) keys_json.next();
			String objKeyVal = obj_json.getString(objKey);

			if(objKeyVal.isEmpty()) continue; //json 변수에 값이 들어가 있지 않을 경우 통과
			if(objKey.equals("es_operator"))  continue; // es_operator는 EnginePATTSearchHandler에서 감싸는 상위쿼리로 사용한다.
			
			//logger.logInfo("(테스트용)objKey="+objKey);
			//field 메타 값 만큼 반복한다.
			for (int i=0; i<nodeSize; i++) {
				Element tmp_field = (Element) fieldList.item(i);

				if (tmp_field.getAttribute("id").equals(objKey) || tmp_field.getAttribute("id").equals(vo.getPrefixType()+objKey) ) {
										
					String thisFormat = tmp_field.getAttribute("query_format");
					String fieldID = tmp_field.hasAttribute("search-field-id")?tmp_field.getAttribute("search-field-id"):"";
					String fieldIDs = tmp_field.hasAttribute("search-field-ids")?tmp_field.getAttribute("search-field-ids"):"";
					String selectArea = tmp_field.hasAttribute("select-area")?tmp_field.getAttribute("select-area"):"query-area";

					//search-field-ids를 대체하는 값은 배열로 만든다.
					String[] arr_fields = fieldIDs.split(",");
					StringBuffer sb_fields = new StringBuffer();
					for(String str : arr_fields){
						if(sb_fields.length()>0) sb_fields.append(",");
						sb_fields.append("\"");
						sb_fields.append(str);
						sb_fields.append("\"");
					}
					
					//keyword-vals를 대체하는 값은 배열로 만든다.
					String[] arr_objKeyVal = objKeyVal.split(",");
					StringBuffer sb_keyval = new StringBuffer();
					for(String str : arr_objKeyVal){
						if(sb_keyval.length()>0) sb_keyval.append(",");
						sb_keyval.append("\"");
						sb_keyval.append(convertElaKeyword(str));
						sb_keyval.append("\"");
					}
					
					//range-start, range-end를 대체하는 값을 배열로 만든다.
					String[] arr_rangeVal = objKeyVal.split("\\.\\.");
					
					String thisQuery = vo.getMapQuery().get(thisFormat)
											.replace("<search-field-id/>", fieldID)
											.replace("<search-field-ids/>", sb_fields.toString())
											.replace("<keyword-val/>", convertElaKeyword(objKeyVal))
											.replace("<keyword-vals/>", sb_keyval.toString());
					
					thisQuery = (arr_rangeVal[0].length()>0)?thisQuery.replace("<range-start/>", arr_rangeVal[0]):thisQuery.replace(",\"gte\": \"<range-start/>\"", "");
					thisQuery = (arr_rangeVal.length>1 && arr_rangeVal[1].length()>0)?thisQuery.replace("<range-end/>", arr_rangeVal[1]):thisQuery.replace(",\"lte\": \"<range-end/>\"", "");
					
					//logger.logInfo("(테스트용)"+tmp_field.getAttribute("id")+"="+thisQuery);
					
					if(selectArea.equals("filter-area")){
						vo.getMapFilterArea().put(tmp_field.getAttribute("id"), thisQuery);
						
					}else{
						
						if(mode!=null && mode.contains("reverse")) vo.getMapReverseQueryArea().put(tmp_field.getAttribute("id"), thisQuery);
						else vo.getMapQueryArea().put(tmp_field.getAttribute("id"), thisQuery);
						
					}
					
					
					
					break;// 해당하는 field를 찾으면 이후 field수색은 중단하고 다음 json 변수로 넘어간다. 
										
				}
			} // for end
		} // while end		
		
		logger.info("##### getDetailQuery END #####");
		
	}
	

	
	/**
	 * JSONObject의 sort절에 선언된 변수로 sort쿼리 생성.
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void getSortQuery(JSONObject obj_json) {


		StringBuffer sb = new StringBuffer();
		logger.info("##### getSortQuery START #####");
		logger.info(String.valueOf(obj_json));

		Iterator<Object> keys_json = obj_json.keys();		
		
		while (keys_json.hasNext()) {
			String objKey = (String) keys_json.next();
			String objKeyVal = obj_json.getString(objKey);

			if(objKeyVal.isEmpty()) continue; //json 변수에 값이 들어가 있지 않을 경우 통과
			//logger.logInfo("(테스트용)objKey="+objKey);
			
			String thisQuery = vo.getMapQuery().get("query-sort-date")
					.replace("<search-field-id/>", objKey)
					.replace("<sort-val/>", convertElaKeyword(objKeyVal));
			
			sb.append(thisQuery);			
			
		} // while end
		
		vo.setSortArea(vo.getMapQuery().get("sort-where").replace("<sort-area/>", sb.toString().substring(0, sb.length()-1)));
		
		logger.info("##### getSortQuery END #####");
		
	}
	
	private String convertElaKeyword(String keyword){
		
		
		String rtnKeyword = keyword.replaceAll(" or ", " OR ").replaceAll(" and ", " AND ");
		
		
		return rtnKeyword;
		
	}
	


}
