package com.poscoict.postech.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.postech.constants.GlobalValues;

import net.sf.json.JSONObject;

@Service
public class ElasearchService {	
	@Autowired
//	CodeService codeService;
	
//	@Autowired
//	GlobalValues GlovalValueService;
	
//	private static Logger logger = Logger.getLogger(MonitoringService.class);
	
	private static Logger logger = (Logger) LoggerFactory.getLogger(MonitoringService.class);
	
	public JSONObject handleElaCommon(Map<String, Object> params){
		JSONObject json_result = null;
		String url = params.get("elaIP").toString();
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpEntity resEntity = null;
		try {
//			System.out.println(url);
			HttpGet request = new HttpGet(url);
//			request.setHeader("Authorization", "Basic " + Base64Coder.encodeString("elastic:gsaadmin"));
			request.addHeader("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
			request.addHeader("content-type", "application/json");
			request.addHeader("Accept", "application/json");

			// request.setEntity(params);
			HttpResponse response_elastic = httpClient.execute(request);
			resEntity = response_elastic.getEntity();
			result = EntityUtils.toString(resEntity);
			
			json_result = JSONObject.fromObject(result);
//			System.out.println(result);
			
			// handle response here...
		} catch (Exception ex) {
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return json_result;
	}
	
	
	public String handleElaCommonString(Map<String, Object> params){
		JSONObject json_result = null;
		String url = params.get("elaIP").toString();
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpEntity resEntity = null;
		try {
//			System.out.println(url);
			HttpGet request = new HttpGet(url);
//			request.setHeader("Authorization", "Basic " + Base64Coder.encodeString("elastic:gsaadmin"));
			request.addHeader("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
			request.addHeader("content-type", "application/json");
			request.addHeader("Accept", "application/json");

			// request.setEntity(params);
			HttpResponse response_elastic = httpClient.execute(request);
			resEntity = response_elastic.getEntity();
			result = EntityUtils.toString(resEntity);
			
//			json_result = JSONObject.fromObject(result);
//			System.out.println(result);
			
			// handle response here...
		} catch (Exception ex) {
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	public JSONObject handleEla(Map<String, Object> params){
		JSONObject json_result = null;
		String url = params.get("elaIP").toString();
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		try {
			StringBuilder temp_sb = new StringBuilder();
			 
			connect = new URL(url);
			httpConn = (HttpURLConnection)connect.openConnection();
			httpConn.setConnectTimeout(50000);
			httpConn.setRequestProperty("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
			httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			httpConn.setRequestProperty("size", params.get("length").toString());
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			
			//request write
			os = httpConn.getOutputStream();

			logger.debug("\n\n");
			logger.debug("######################## 검색조건 Param ######################");
			
			String elajsonParam = "";
			if(params.get("EVENT_ID").toString().equals("findWasServerLog")) {
				elajsonParam = getWasServerLog(params); 
			} else if(params.get("EVENT_ID").toString().equals("findServerList")) {
				elajsonParam = getServerList(params);
			} else if(params.get("EVENT_ID").toString().equals("getCodeList")) {
				elajsonParam = getServerList(params); 
			}
			
			logger.debug("elajsonParam"+elajsonParam);
			os.write(elajsonParam.getBytes("UTF-8"));

			os.flush();
			os.close();
			os = null;

			reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
//			System.out.println("aaa " + reader.readLine());
			json_result = JSONObject.fromObject(String.valueOf(reader.readLine()));

			String line;
			while (null != (line = reader.readLine())) {
			data.append(line);
		   }
			
			httpConn.disconnect();
			httpConn = null;

			temp_sb.append("\n**>> URLs to Ela >>>>>>>>>>>>  " + url);
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			temp_sb.append("\n**>> getElajsonParam >>>>>>>>>>>>>>>  " + elajsonParam);
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			temp_sb.append("\n**>> Json Result >>>>>>>>>>>>>>>  "+json_result);
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			logger.debug(temp_sb.toString());

			
//			System.out.println("전체개수:"+Integer.parseInt(json_result.getJSONObject("hits").getString("total")));
			logger.debug("전체개수:"+Integer.parseInt(json_result.getJSONObject("hits").getJSONObject("total").getString("value")));

			logger.debug("data >>" + data.toString());
		
		} catch( Exception ee) {
	    	ee.printStackTrace();
	    	logger.error("HttpNetworkException : " + ee.getMessage());
	    } finally {
            if (os != null) {
                try {
                	os.close();
                	os = null;
                } catch (IOException e) {
                    logger.error(e.getMessage(), e.getCause());
                }
            }

			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error(e.getMessage(), e.getCause());
				}
			}

			if (httpConn != null) {
				try {
					httpConn.disconnect();
					httpConn = null;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
	    }
		return json_result;
	}

	/**
	 * [쿼리생성]WAS 서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getWasServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";

			st.append("{");
			st.append("\"_source\": [\"PATH\", \"HOST\", \"LOG_LEVEL\", \"HANDLER\", \"SSO_USER_NAME\", \"LOG_TEXT\", \"TIMESTAMP\"],");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");

			//검색어
			if( !"".equals(message) )
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}

			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			//체인코드
			String pChainCode = params.get("P_CHAIN_CODE").toString();
			if( !"".equals(pChainCode) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"CHAIN_CODE\": [");
				st.append("                   \"" +pChainCode + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//로그레벨
			String pLogLevel = params.get("P_LOG_LEVEL").toString(); 
			if( !"".equals(pLogLevel) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"LOG_LEVEL.keyword\": [");
				st.append("                   \"" +pLogLevel + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}

			//HOST
//			String pHostName = params.get("P_HOST_NAME").toString().toUpperCase(); 
			String pHostName = params.get("P_HOST_NAME").toString().toUpperCase();
			if( !"".equals(pHostName) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"HOST.keyword\": [");

				String[] pHostNameArr =pHostName.split("\\|");
				int pHostNameArrLen = pHostNameArr.length;

				System.out.println("pHostNameArrLen:" + pHostNameArrLen);
				for(int i=0; i < pHostNameArrLen ; i++)
				{
					//마지막 index
					if((pHostNameArrLen-1) == i)
					{
						st.append("                   \"" +pHostNameArr[i] +"\"                         ");
					}
					else
					{
						st.append("                   \"" +pHostNameArr[i] +"\",                         ");
					}
				}
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"plain\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.debug( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getWasServerLog");
			logger.error("getWasServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	
	
	/**
	 * [쿼리생성]서버 항목 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getServerList(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String server_type = params.get("SERVER_TYPE")!=null? params.get("SERVER_TYPE").toString() : "";
			String infra_type = params.get("INFRA_TYPE")!=null? params.get("INFRA_TYPE").toString() : "";
			String host_name = params.get("HOST_NAME")!=null? params.get("HOST_NAME").toString() : "";
			String must_not = params.get("MUST_NOT")!=null? params.get("MUST_NOT").toString() : "";//L/B서버 제외용
			//정렬
			String orderBy = params.get("orderBy")!=null? params.get("orderBy").toString() : "";
			String dir = params.get("dir")!=null? params.get("dir").toString() : "";
			
			server_type = server_type.replace("선택", "");
			infra_type = infra_type.replace("선택", "");
			host_name = host_name.replace("선택", "");
			
			st.append("{");
			st.append("   \"query\": {");
			if("".equals(server_type) && "".equals(infra_type) && "".equals(host_name)) {
				st.append("   	\"match_all\": {}");
			}else {
				st.append("    \"bool\": {                        ");
				st.append("        \"must\": [                    ");
				Boolean usecomma = false;
				if(!"".equals(server_type)) {
					st.append("            {                          ");
					st.append("                \"terms\": {           ");
					st.append("                    \"SERVER_TYPE\": [ ");
					st.append("                        \""+server_type+"\"        ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                         ");
					usecomma = true;
				}
				if(!"".equals(infra_type)) {
					if(usecomma) st.append("            ,                         ");
					st.append("            {                          ");
					st.append("                \"terms\": {           ");
					st.append("                    \"INFRA_TYPE\": [  ");
					st.append("                        \""+infra_type+"\"     ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                         ");
					usecomma = true;
				}
				if(!"".equals(host_name)) {
					if(usecomma) st.append("            ,                         ");
					st.append("            {                          ");
					st.append("                \"terms\": {           ");
					st.append("                    \"HOST_NAME\": [   ");
					st.append("                        \""+host_name+"\"   ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                          ");					
				}
				st.append("         ]                              ");

				if(!"".equals(must_not)) {
					st.append("      ,                         ");
					st.append("      \"must_not\": [             ");
					st.append("        {                       ");
					st.append("          \"terms\": {            ");
					st.append("            \"HOST_NAME\": [      ");
					int idx=0;
					for(String mustnotval : must_not.split(",")) {
						if(idx>0) 
							st.append("      ,                         ");
						st.append("              \""+mustnotval+"\"        ");
						idx++;
					}
					st.append("            ]                   ");
					st.append("          }                     ");
					st.append("        }                       ");
					st.append("      ]                         ");				
				}
				st.append("    }                                  ");
				 
			}
			st.append("         },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"size\": " + params.get("length").toString() + ",");
			if(!"".equals(orderBy)) {
				st.append("   \"sort\": [");
				st.append("    	  		{");
				st.append("    	           \""+orderBy+"\": {");
				st.append("    	              \"order\": \""+dir+"\"");
				st.append("    	            }");
				st.append("    	  		}");
				st.append("    	  ],");
				
			}
			st.append("    	  \"aggs\": {");
			st.append("    	  	\"SERVER_TYPE\": {");
			st.append("    	  	  \"sampler\": {");
			st.append("    	  	  	\"shard_size\": 150000");
			st.append("    	  	  	  },");
			st.append("    	  			\"aggs\": {");
			st.append("    	  			  \"SERVER_TYPE\": {");
			st.append("    	  	  			\"terms\": {");
			st.append("    	  	  			  \"field\": \"SERVER_TYPE\",");
			st.append("    	  	  	  			\"order\": {");
			st.append("    	  	  	  			  \"_term\": \"asc\"");
			st.append("    	  	  	  			  }");
			st.append("    	  	  	  			}");
			st.append("    	  	  		  	 }");
			st.append("    	  	  		  }");
			st.append("    	  			},");
			st.append("    	  	\"INFRA_TYPE\": {");
			st.append("    	  	  \"sampler\": {");
			st.append("    	  	  	\"shard_size\": 150000");
			st.append("    	  	  	  },");
			st.append("    	  			\"aggs\": {");
			st.append("    	  			  \"INFRA_TYPE\": {");
			st.append("    	  	  			\"terms\": {");
			st.append("    	  	  			  \"field\": \"INFRA_TYPE\",");
			st.append("    	  	  	  			\"order\": {");
			st.append("    	  	  	  			  \"_term\": \"asc\"");
			st.append("    	  	  	  			  }");
			st.append("    	  	  	  			}");
			st.append("    	  	  		  	 }");
			st.append("    	  	  		  }");
			st.append("    	  			},");
			st.append("    	  	\"HOST_NAME\": {");
			st.append("    	  	  \"sampler\": {");
			st.append("    	  	  	\"shard_size\": 150000");
			st.append("    	  	  	  },");
			st.append("    	  			\"aggs\": {");
			st.append("    	  			  \"HOST_NAME\": {");
			st.append("    	  	  			\"terms\": {");
			st.append("    	  	  			  \"field\": \"HOST_NAME\",");
			st.append("    	  	  	  			\"order\": {");
			st.append("    	  	  	  			  \"_term\": \"asc\"");
			st.append("    	  	  	  			  }");
			st.append("    	  	  	  			}");
			st.append("    	  	  		  	 }");
			st.append("    	  	  		  }");
			st.append("    	  			}");
			st.append("    	  	  	  }");
			st.append("    	  	  	}");

			logger.debug(st.toString());

		}catch( Exception e)
		{
			System.out.println("Error  getServerList");
			logger.error("getServerList Exception : " + e.getMessage());
		}

		return st.toString();
	}
}
