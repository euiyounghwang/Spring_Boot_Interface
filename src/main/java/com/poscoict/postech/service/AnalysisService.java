package com.poscoict.postech.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import com.poscoict.postech.model.EngineProviderQueryVO;

import net.sf.json.JSONObject;

@Service
public class AnalysisService {	
	
//	private static Logger logger = Logger.getLogger(AnalysisService.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(AnalysisService.class);
	
	public JSONObject handleAnalysis(Map<String, Object> params){
		
		System.out.println("handleAnalysis");
		JSONObject json_result = null;
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		try {
			StringBuilder temp_sb = new StringBuilder();

			String url = params.get("elaIP").toString();
			connect = new URL(url);
			httpConn = (HttpURLConnection)connect.openConnection();
			httpConn.setConnectTimeout(50000);
			httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			
			//request write
			os = httpConn.getOutputStream();

			System.out.println("######################## 검색조건 Param ######################");
			
			String elajsonParam = "";
			
			elajsonParam = getParam(params); 
			
			System.out.println("elajsonParam"+elajsonParam);
			os.write(elajsonParam.getBytes("UTF-8"));

			os.flush();
			os.close();
			os = null;

			reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
			String readerResult = reader.readLine();
			System.out.println("readerResult="+readerResult);
			json_result = JSONObject.fromObject(readerResult);

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

			logger.info(temp_sb.toString());

		   System.out.println(data);
		
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
	private String getParam(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);
		String match = "[^\\uAC00-\\uD7A3xfe0-9a-zA-Z\\s\\.\\r\\n|\\r|\\n|\\n\\r|^\\u2460-\\u2473|^\\u3250-\\u325F|^\\u32B1-\\u32BF]";

		try
		{
			//검색어
			String fileName = params.get("fileName")!=null? params.get("fileName").toString() : "";
			String textParam = params.get("textParam").toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll(match, "").replaceAll("\\p{Space}", " ")
					.replaceAll("( )+", " ").replaceAll("\\[", "")
					.replaceAll("\\^", "").replaceAll("\\@", "")
					.replaceAll("\\'", "").replaceAll("\'", "").replaceAll("\\\"", "")
					.replaceAll("\"", "").replaceAll("null", "");

			st.append("{");
			st.append("   \"text\": \"" + textParam + "\" ,");
			st.append("   \"docTitle\": \"" + fileName + "\" ,");
			st.append("   \"company_code\": \"17\",");
			st.append("   \"loginId\": \"S13324\","); 
			st.append("   \"key\": \"doc0900bf4b9ef20165\","); 
			st.append("   \"ownerNo\": \"S13324\",");
			st.append("   \"ownerName\": \"euiyoung\","); 
			st.append("   \"ownerDeptCode\": \"30_CADDDD\","); 
			st.append("   \"ownerDeptName\": \"dept\","); 
			st.append("   \"page_num\": \"1\",");
			st.append("   \"page_size\": \"50\","); 
			st.append("   \"group_key\": \"euiyoungmZhcqHABaWjtH9mmCwHw\","); 
			st.append("   \"response_format\": \"json\"");
			st.append(" }");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getParam");
			logger.error("getParam Exception : " + e.getMessage());
		}

		return st.toString();
	}
}
