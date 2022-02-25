package com.poscoict.postech.service;

import java.net.URLEncoder;

//import org.apache.log4j.Logger;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import com.poscoict.postech.constants.WebConstants;
import com.poscoict.postech.model.ESUser;
import com.poscoict.postech.model.EngineProviderQueryVO;
import com.poscoict.postech.util.Base64Utils;
import com.poscoict.postech.util.ESUtil;
import com.poscoict.postech.util.EngineProviderSearchHandler;

import net.sf.json.JSONObject;

@Service
public class InterFaceService {
	
	
//	private static Logger logger = Logger.getLogger(InterFaceService.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(InterFaceService.class);
	
	/**
	 * 검색연계 I/F
	 * */
	public String dataProvider(String callback, String q) {
		String resultData = "";

		ESUser user = null;
		JSONObject jsonObj = new JSONObject();
		JSONObject paramObj = new JSONObject();
		
		try {
			
			if(q==null || q.trim().equals("")) {
				logger.error("##### PARAMETER Q IS NULL ERROR #####");
				throw new Exception("q 파라미터가 존재하지 않습니다.");
			}
			//q값을 복호화하여 JSONObject에 담기
			paramObj = this.getParamDecode(q);

			//JSONobject에서 사용자정보만 ESUser에 따로 담기
			user = this.getUserInfoSetting(paramObj);
			logger.info("##### USER ID : " + user.getUsrId());
			logger.info("##### USER EMP NO : " + user.getEmpNo());
			logger.info("##### USER COMPANYCODE : " + user.getCompanyCode());

	        StringBuilder     temp_sb = new StringBuilder();
//	        temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
          temp_sb.append("[param]**>>["+user.getUsrId()+"] >> params from DataProvider >>>>>>>>>>>>  " + paramObj.toString());
//	        temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
          logger.info(temp_sb.toString());
			
			// JSONObject값을 VO에 모두 담기
	        EngineProviderQueryVO vo = new EngineProviderQueryVO(user, paramObj);
			
			//ElasticPAPQuerySet에 VO 보내기
	        EngineProviderSearchHandler handler = new EngineProviderSearchHandler(vo);
			//1. VO와 queryXML내용을 조합하여, 2.Ela query 만들고 3.ElasticSearch로 보내서 검색결과를 받아와서 4.String으로 변환
			handler.setDataQuery();
			
			//검색결과String을 암호화한다.
			resultData = this.getResultEncode(vo.getResultData(), user.getCompanyCode());


			jsonObj.put(WebConstants.RESULT_CODE, "success");
			jsonObj.put(WebConstants.GSA_DATA_KEY, resultData);
			
			
		} catch (Exception e) {
			logger.error("##### DataProvider IF ERROR : " + e.getMessage(), e.getCause());
			
			jsonObj.put(WebConstants.RESULT_CODE, "failed");
			if (e.getMessage() != null) {
				if(e.getMessage().startsWith("CONN_TIME_OUT|")){
					jsonObj.put(WebConstants.GSA_ERROR_KEY_MORE_INFO, e.getMessage().substring(14));
					jsonObj.put(WebConstants.GSA_ERROR_KEY, "검색 서버 연결 시간을 초과 하였습니다.");
				}else{
					jsonObj.put(WebConstants.GSA_ERROR_KEY, e.getMessage());
				}
			}
		}
		return ESUtil.getJSONPString(callback, jsonObj);
	}
	
	/**
	 * 검색 사용자 정보 세팅
	 * @param req
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private ESUser getUserInfoSetting(JSONObject obj) throws Exception {
		ESUser user = new ESUser();
		
		if (obj.has("common") && obj.getJSONObject("common").has("es_user_id") && obj.getJSONObject("common").has("es_emp_no") && obj.getJSONObject("common").has("es_comp_cd")) {
			user.setUsrId(obj.getJSONObject("common").getString("es_user_id"));
			user.setEmpNo(obj.getJSONObject("common").getString("es_emp_no"));
			user.setCompanyCode(obj.getJSONObject("common").getString("es_comp_cd"));			
			
		} else {
			logger.error("##### USER INFO NOT ERROR #####");
			throw new Exception("사용자 정보가 없습니다.");
		}
		
		return user;
	}	
	/**
	 * 타 시스템에서 넘겨준 모든 데이터를 복호화 한다.
	 * @param str
	 * @return
	 */
	private JSONObject getParamDecode(String str) throws Exception {
		logger.info("##### PARAMETER Q : " + str);
		String tmp = str;
		JSONObject result = new JSONObject();
		Base64Utils base64 = new Base64Utils();
		tmp = base64.base64decoding(tmp);
		logger.info("##### PARAMETER Q DECODEING : " + tmp);
		try {
			if(tmp.startsWith("posmeta:")) tmp = tmp.substring(8);
			result = JSONObject.fromObject(tmp);
		} catch (Exception e) {
			logger.error("##### GETPARAMDECODE ERROR : " + e.getMessage(), e.getCause());
			throw new Exception("json 형식의 데이터가 아닙니다.");
		}
		return result;
	}
	
	/**
	 * 타 시스템으로 넘겨주는 데이터를 암호화 한다.
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	private String getResultEncode(String str, String compCode) {
		String tmp = str;
		try {
			Base64Utils base64 = new Base64Utils();
			tmp = base64.base64Encoding(tmp);
			tmp = URLEncoder.encode(tmp, "UTF-8");
		} catch (Exception e) {
			logger.error("##### GETRESULTMESSAGE ERROR : " + e.getMessage(), e.getCause());
		}
		return tmp;
	}
	
	
}

