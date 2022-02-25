package com.poscoict.postech.util;

import java.util.Iterator;

//import org.apache.log4j.Logger;
import org.slf4j.*;

import com.poscoict.postech.model.EngineProviderQueryVO;

import net.sf.json.JSONObject;

/**
 * @fileName : EngineProviderSearchHandler.java
 * @author : ysrhee
 * @date : 2020.05.14
 * @version : 1.0, 1.1
 * @description : 기존 쿼리 작성후 엔진 서버와 통신하던 역할을 분리 쿼리를 작성하는 클래스로 로직 변경
 */
public class EngineProviderSearchHandler extends ElasticProviderQuerySet {
//	private static Logger logger = Logger.getLogger(EngineProviderSearchHandler.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(EngineProviderSearchHandler.class);
	
	public EngineProviderSearchHandler(EngineProviderQueryVO bean) {
		super(bean);
	}
	
	/**
	 * Elastic Full Query
	 * @param bodyQuery
	 * @return
	 */
	private String getFullQuery(String bodyQuery) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if(super.vo.getMapQueryArea().containsKey("es_source"))
			sb.append(super.vo.getMapQueryArea().get("es_source"));
		else
			sb.append(super.vo.getMapQuery().get("query-header"));
		sb.append(bodyQuery)
		.append(super.vo.getMapQuery().get("query-footer").replace("<sort-where/>", super.vo.getSortArea()).replace("<page-from/>", super.vo.getPageFrom()).replace("<page-size/>", super.vo.getPageSize()))
		.append("}");
		return sb.toString();
	}
	
	/**
	 * 쿼리작성
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setDataQuery() throws Exception {
		logger.info("##### setDataQuery START #####");
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_query = new StringBuffer();
		StringBuffer sb_reverse = new StringBuffer();
		StringBuffer sb_filter = new StringBuffer();
		JSONObject json = new JSONObject();
		
		long startTime = System.currentTimeMillis();
		
		//elastic_query.xml에 작성 되어있는 쿼리 정보를 EngineQueryVO 객체 mapQuery와 mapSortQuery에 담아 리턴한다,
		super.getSystemQueryInfo();

		String queryWhere = super.vo.getMapQuery().get("query-where");
		String reverseWhere = "";
		String filterWhere = "";
		String queryArea = "";
		
		/*
		 * common:{   } 구문 분석 및 쿼리생성 
		 * */
		if(!super.vo.getJson_common().isEmpty()){
			
			Iterator<Object> keys = super.vo.getJson_common().keys();
			
			while(keys.hasNext()){
				String objKey = (String) keys.next();
				String keywordVal = super.vo.getJson_common().getString(objKey);
				
				if(objKey.equals("es_source"))
					continue;//es_source field의 쿼리는 query-area에 들어가지 않고, getFullQuery()에서 query-header로 사용된다.
				
				if(!keywordVal.isEmpty()){
					if(super.vo.getMapQueryArea().containsKey(objKey)){
						if(sb_query.length()>0) sb_query.append(",");
						
						sb_query.append(super.vo.getMapQueryArea().get(objKey));						
					}else if(super.vo.getMapFilterArea().containsKey(objKey)){
						if(sb_filter.length()>0) sb_filter.append(",");
						
						sb_filter.append(super.vo.getMapFilterArea().get(objKey));						
					}

					
				}
				
				
			}
			
			
		}
		/*
		 * condition:{   } 구문 분석 및 쿼리생성 
		 * */
		if(!super.vo.getJson_condition().isEmpty()){
			
			Iterator<Object> keys = super.vo.getJson_condition().keys();
			

			//logger.logInfo("getMapQueryArea:"+super.vo.getMapQueryArea().toString());
			//logger.logInfo("getMapFilterArea:"+super.vo.getMapFilterArea().toString());
			while(keys.hasNext()){
				String objKey = (String) keys.next();
				String keywordVal = super.vo.getJson_condition().getString(objKey);
				//logger.logInfo("super.vo.getPrefixType()+objKey:"+super.vo.getPrefixType()+objKey);
				if(!keywordVal.isEmpty()){
					if(super.vo.getMapQueryArea().containsKey(super.vo.getPrefixType()+objKey)){
						if(sb_query.length()>0) sb_query.append(",");						
						sb_query.append(super.vo.getMapQueryArea().get(super.vo.getPrefixType()+objKey));
						
					}else if(super.vo.getMapFilterArea().containsKey(super.vo.getPrefixType()+objKey)){
						if(sb_filter.length()>0) sb_filter.append(",");						
						sb_filter.append(super.vo.getMapFilterArea().get(super.vo.getPrefixType()+objKey));
						
					}
					
				}
				
				
			}
			//logger.logInfo("sb_query:"+sb_query.toString());
			//logger.logInfo("sb_filter:"+sb_filter.toString());

			/*
			 * condition:{ "reverse_condition : {}  } 구문 분석 및 쿼리생성 
			 * */
			if(super.vo.getJson_reverse_condition()!=null && !super.vo.getJson_reverse_condition().isEmpty()){
				Iterator<Object> reverse_keys = super.vo.getJson_reverse_condition().keys();
				reverseWhere = super.vo.getMapQuery().get("reverse-where");	
				
				
				while(reverse_keys.hasNext()){
					String objKey = (String) reverse_keys.next();
					String keywordVal = super.vo.getJson_reverse_condition().getString(objKey);
					if(!keywordVal.isEmpty()){
						if(super.vo.getMapReverseQueryArea().containsKey(super.vo.getPrefixType()+objKey)){
							if(sb_reverse.length()>0) sb_reverse.append(",");						
							sb_reverse.append(super.vo.getMapReverseQueryArea().get(super.vo.getPrefixType()+objKey));
							
						}
						
					}
					
					
				}
				
				reverseWhere = reverseWhere.replace("<reverse-val/>", super.vo.getReverse_operator()).replace("<reverse-query-area/>", sb_reverse.toString());
				
			}
			
			
		}
		

		if(sb_filter.length()>0){
			filterWhere = super.vo.getMapQuery().get("filter-where");
			filterWhere = filterWhere
					.replace("<filter-area/>", sb_filter.toString());
			
		}
		//logger.logInfo("(테스트용)filterWhere:"+filterWhere);
		
		
		if(sb_query.length()>0){
			queryArea = sb_query.toString();
		}
			
		queryWhere = queryWhere.replace("<keyword-val/>", super.vo.getOperator())
				.replace("<query-area/>", queryArea)
				.replace("<reverse-where/>", reverseWhere)
				.replace("<filter-where/>", filterWhere);
		
		sb.append(queryWhere);

		
		//logger.logInfo("#FullQuery  = "+sb.toString());
		long endTime = System.currentTimeMillis();
		logger.info("##### QUERY SETTING TIME : " + (endTime - startTime));
		
		json = EngineConnectionHandler.search(super.vo.getEngineUrl(), this.getFullQuery(sb.toString()), super.vo.getUser().getUsrId());
		logger.info("##### super.vo.getEngineUrl() : " + super.vo.getEngineUrl());
		
		super.vo.setResultData(ESUtil.replaceElaResult(json.toString()));
	}

}
