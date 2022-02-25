package com.poscoict.postech.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

import org.apache.commons.io.FileUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.poscoict.postech.constants.WebConstants;
import com.poscoict.postech.repository.CodeMapper;

@Service
public class CodeService {
	
	@Autowired
	CodeMapper codeMapper;
	
//	private static Logger logger = Logger.getLogger(CodeService.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(CodeService.class);
		
	public Map<String, Object> pythonProcess(Map<String, Object> params){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try{
			String argv = "monitoring";
			String fk_cd_tp = (String) params.get("FK_CD_TP");
			logger.info("start pythonProcess : fk_cd_tp="+fk_cd_tp);
			
			String pythonPath = WebConstants.PYTHON_PATH;//python 실행파일 경로
			String pyFilePath = WebConstants.PY_FILE_PATH;//python 프로젝트경로	
			 
			ProcessBuilder pb = new ProcessBuilder(pythonPath+"python",pyFilePath+"Data_Main_Collect.py",""+argv,""+fk_cd_tp);
			Process p = pb.start();
			String storedFileName = null;
			 
			try (InputStream psout = p.getInputStream()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(psout));
				String thisLine = null;
				while ((thisLine = in.readLine()) != null) {
					logger.info(thisLine);
					if(thisLine.startsWith("Finish_return=")) {
						storedFileName = thisLine.substring(thisLine.indexOf("=")+1);//마지막파일 전체경로
					}
				}
	        }
			String filenum = storedFileName.substring(storedFileName.lastIndexOf("_")+1);//파일개수
			String headFileName = storedFileName.substring(0,storedFileName.lastIndexOf("_"));//전체경로 파일명 숫자앞부분
			
			logger.info("pythonProcess storedFileName:"+storedFileName);
			logger.info("pythonProcess filenum:"+filenum);
			logger.info("pythonProcess headFileName:"+headFileName);
			resultMap.put("filename", headFileName);
			resultMap.put("filenum", filenum);
			resultMap.put("msg", "success");
			
			}catch(Exception e){
				resultMap.put("msg", "error");
	        	logger.error(e.getMessage(), e.getCause());
			}

		   return resultMap;
	}
	
	public Map<String,Object> fileDownload(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try{
			String storedFileName = request.getParameter("filename");			
			logger.info("fileDownload storedFileName:"+storedFileName);

			//마지막 파일은 받아와서 브라우저로 보낸다.
		    byte fileByte[] = FileUtils.readFileToByteArray(new File(storedFileName));
			String originalFileName = storedFileName.substring(storedFileName.lastIndexOf("/")+1);//마지막파일명

	        //파일 다운로드를 위해 컨테츠 타입을 application/download 설정
	        response.setContentType("application/octet-stream; charset=utf-8");
		    response.setContentLength(fileByte.length);
		    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";");
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    response.getOutputStream().write(fileByte);		     
		    response.getOutputStream().flush();
		    response.getOutputStream().close();
			
			resultMap.put("msg", "success");
			
			}catch(Exception e){
				resultMap.put("msg", "error");
	        	logger.error(e.getMessage(), e.getCause());
			}

		   return resultMap;
	}
	
	public List<Map<String, String>> selectMenuDetailsByCompanyAndMenu(Map<String, String> params){
		if(!params.containsKey("cd_tp1") || "EM46".equals(params.get("cd_tp1").substring(0,4))) {//ElaConfig
			return codeMapper.selectMenuDetailsByCompanyAndMenu(params);
		} else {//Etc
			return codeMapper.selectMenuDetailsByCompanyAndMenu2(params);
		}
	}
	
	public Map<String, Object> selectMenuProperties(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data") +" "+ dir;
		params.put("orderBy", orderBy);
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = codeMapper.selectMenuProperties(params);
		results.put("draw", params.get("draw"));
		if(list.size()>0) {
			results.put("recordsTotal", list.get(0).get("TOTCNT"));
			results.put("recordsFiltered",list.get(0).get("TOTCNT"));
		} else {
			results.put("recordsTotal", 0);
			results.put("recordsFiltered",0);
		}
		results.put("data", list);
		
		return results;
	}
	
	public Map<String, Object> selectMenuMetaCode(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data") +" "+ dir;
		params.put("orderBy", orderBy);
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = codeMapper.selectMenuMetaCode(params);
		results.put("draw", params.get("draw"));
		if(list.size()>0) {
			results.put("recordsTotal", list.get(0).get("TOTCNT"));
			results.put("recordsFiltered",list.get(0).get("TOTCNT"));
		} else {
			results.put("recordsTotal", 0);
			results.put("recordsFiltered",0);
		}
		results.put("data", list);
		
		return results;
	}
	
	public Map<String, Object> selectLogCode(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data") +" "+ dir;
		params.put("orderBy", orderBy);
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = codeMapper.selectLogCode(params);
		results.put("draw", params.get("draw"));
		if(list.size()>0) {
			results.put("recordsTotal", list.get(0).get("TOTCNT"));
			results.put("recordsFiltered",list.get(0).get("TOTCNT"));
		} else {
			results.put("recordsTotal", 0);
			results.put("recordsFiltered",0);
		}
		results.put("data", list);
		
		return results;
	}
	
	public Map<String, Object> selectCodeTypeInfo(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data") +" "+ dir;
		params.put("orderBy", orderBy);
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = codeMapper.selectCodeTypeInfo(params);
		results.put("draw", params.get("draw"));
		if(list.size()>0) {
			results.put("recordsTotal", list.get(0).get("TOTCNT"));
			results.put("recordsFiltered",list.get(0).get("TOTCNT"));
		} else {
			results.put("recordsTotal", 0);
			results.put("recordsFiltered",0);
		}
		results.put("data", list);
		
		return results;
	}
	
	public Map<String, Object> selectCodeTypeInfoDetail(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data") +" "+ dir;
		params.put("orderBy", orderBy);
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = codeMapper.selectCodeTypeInfoDetail(params);
		results.put("draw", params.get("draw"));
		if(list.size()>0) {
			results.put("recordsTotal", list.get(0).get("TOTCNT"));
			results.put("recordsFiltered",list.get(0).get("TOTCNT"));
		} else {
			results.put("recordsTotal", 0);
			results.put("recordsFiltered",0);
		}
		results.put("data", list);
		
		return results;
	}
	
	public List<Map<String, String>> selectMenuMetas(){
//		HashMap<String, String> params = new HashMap<>();
//		params.put("companyCode", companyCode);
//		params.put("menuCode", menuCode);
		return codeMapper.selectMenuMetas();
	}
	
	public List<Map<String, String>> selectCompanys(){
		return codeMapper.selectCompanys();
	}
	
	public List<Map<String, String>> selectMenuByCompany(Map<String, String> params){
		return codeMapper.selectMenuByCompany(params);
	}
	
	public List<Map<String, String>> selectMetaListByMetas(Map<String, String> params){
		return codeMapper.selectMetaListByMetas(params);
	}
	
	public List<Map<String, String>> selectNotExistMenucodeByMeta(Map<String, String> params){
		return codeMapper.selectNotExistMenucodeByMeta(params);
	}
	
	public List<Map<String, String>> selectMenucodeByMeta(Map<String, String> params){
		return codeMapper.selectMenucodeByMeta(params);
	}
	
	public List<Map<String, String>> selectCompanyByMetaMenu(Map<String, Object> params){
		return codeMapper.selectCompanyByMetaMenu(params);
	}
	
	public List<Map<String, String>> selectNotExistCompanyByMetaMenu(Map<String, Object> params){
		return codeMapper.selectNotExistCompanyByMetaMenu(params);
	}
	
	public List<Map<String, String>> selectMenuDuplication(Map<String, Object> params){
		return codeMapper.selectMenuDuplication(params);
	}
	
	public void updateMetaValues(Map<String, Object> params){
		codeMapper.updateMetaValues(params);
	}
	
	public void insertMetaToMenu(Map<String, Object> params){
		codeMapper.insertMetaToMenu(params);
	}
	
	public Map<String, String> insertMenu(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectMenuDuplication(params);
		
		if(duplicate.size()>0) {
			result.put("msg", "중복된 데이터(CD_TP1) 가 존재합니다.");
			result.put("value", "false");
		} else {
			codeMapper.insertMenu(params);
			result.put("msg", "메뉴 추가가 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> insertMenuMeta(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectMenuMetaDuplication(params);
		
		if(duplicate.size()>0) {
			result.put("msg", "중복된 데이터(CD_TP1) 가 존재합니다.");
			result.put("value", "false");
		} else {
			codeMapper.insertMenuMeta(params);
			result.put("msg", "메뉴 속성 추가가 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> insertLogCode(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectLogCodeDuplication(params);
		
		if(duplicate.size()>0) {
			result.put("msg", "중복된 데이터(CD_TP1) 가 존재합니다.");
			result.put("value", "false");
		} else {
			codeMapper.insertLogCode(params);
			result.put("msg", "로그영역 코드 추가가 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> insertCodeTypeInfo(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectCodeTypeInfoDuplication(params);
		
		if(duplicate.size()>0) {
			result.put("msg", "중복된 데이터(CD_TP) 가 존재합니다.");
			result.put("value", "false");
		} else {
			codeMapper.insertCodeTypeInfo(params);
			result.put("msg", "코드유형 정보 추가가 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> insertCodeTypeInfoDetail(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectCodeTypeInfoDetailDuplication(params);
		
		if(duplicate.size()>0) {
			result.put("msg", "중복된 데이터(CD_TP) 가 존재합니다.");
			result.put("value", "false");
		} else {
			codeMapper.insertCodeTypeInfoDetail(params);
			result.put("msg", "코드유형 정보 추가가 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> updateMenu(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectMenuDuplication(params);
		
		if(duplicate.size()==0) {
			result.put("msg", "선택하신 데이터가 존재하지 않습니다.");
			result.put("value", "false");
		} else {
			codeMapper.updateMenu(params);
			result.put("msg", "메뉴 수정이 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> updateMenuMeta(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectMenuMetaDuplication(params);
		
		if(duplicate.size()==0) {
			result.put("msg", "선택하신 데이터가 존재하지 않습니다.");
			result.put("value", "false");
		} else {
			codeMapper.updateMenuMeta(params);
			result.put("msg", "메뉴속성 코드 수정이 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> updateLogCode(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectLogCodeDuplication(params);
		
		if(duplicate.size()==0) {
			result.put("msg", "선택하신 데이터가 존재하지 않습니다.");
			result.put("value", "false");
		} else {
			codeMapper.updateLogCode(params);
			result.put("msg", "로그영역 코드 수정이 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> updateCodeTypeInfo(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectCodeTypeInfoDuplication(params);
		
		if(duplicate.size()==0) {
			result.put("msg", "선택하신 데이터가 존재하지 않습니다.");
			result.put("value", "false");
		} else {
			codeMapper.updateCodeTypeInfo(params);
			result.put("msg", "코드유형 정보 수정이 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public Map<String, String> updateCodeTypeInfoDetail(Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> duplicate = codeMapper.selectCodeTypeInfoDetailDuplication(params);
		
		if(duplicate.size()==0) {
			result.put("msg", "선택하신 데이터가 존재하지 않습니다.");
			result.put("value", "false");
		} else {
			codeMapper.updateCodeTypeInfoDetail(params);
			result.put("msg", "코드유형 정보 수정이 완료되었습니다.");
			result.put("value", "true");
		}
		return result; 
	}
	
	public void deleteMenu(Map<String, Object> params){
		codeMapper.deleteMenu(params);
	}
	
	public void deleteMenuMeta(Map<String, Object> params){
		codeMapper.deleteMenuMeta(params);
	}
	
	public void deleteLogCode(Map<String, Object> params){
		codeMapper.deleteLogCode(params);
	}
	
	public void deleteCodeTypeInfo(Map<String, Object> params){
		codeMapper.deleteCodeTypeInfo(params);
	}
	
	public void deleteCodeTypeInfoDetail(Map<String, Object> params){
		codeMapper.deleteCodeTypeInfoDetail(params);
	}
	
	public void deleteMetaInMenu(Map<String, Object> params){
		codeMapper.deleteMetaInMenu(params);
	}
	
	public List<Map<String, String>> selectCodeInfo(Map<String, String> params){
		return codeMapper.selectCodeInfo(params);
	}
	
	public Map<String,Object> fileUpload(MultipartHttpServletRequest mReq){
	   Map<String,Object> resultMap = new HashMap<String,Object>();
	   String line;
	   StringBuffer query= new StringBuffer();
	   
       String header="";
       String fk_cd_tp = mReq.getParameter("server_cd_tp");
       List<StringBuffer> query_list = new ArrayList<StringBuffer>();
       
       Map<String, Object> param = new HashMap<String, Object>();
	   //기존 추가로직 seq넣는 로직 추가
	 
	   Iterator<String> iter = mReq.getFileNames();
	   if(iter.hasNext() == false){
	      resultMap.put("message", "파일이 없습니다.");
	   } else {
	      while (iter.hasNext()) {
	         String uploadFileNm = iter.next();
	         MultipartFile mFile = mReq.getFile(uploadFileNm);
	         try{         
		         InputStream iStream = mFile.getInputStream();
//		         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream, "euc-kr"));
		         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));   
		            int i=0;
	                  while ((line = bufferedReader.readLine()) != null)
	                  {
	                	  if(i==0) {
	                		  query.append(" INSERT ALL \n");
                			  line = line.replaceAll("\"\"", "").replaceAll("\"", "").replace("\uFEFF", "");
	                		  header =line+", CREATED_OBJECT_ID, CREATION_TIMESTAMP";
//                			  header = line;
	                	  } else {
	                		  query.append("INTO POSMINI.TB_U40_ES_MA_102(" + header +") \n");
		                	  query.append("VALUES (");
//		                	  query.append( fk_cd_tp+(i-1<10 ? "_0000" + (i-1) : i-1<100 ? "_000"+ (i-1) : i-1<1000 ? "_00"+ (i-1) :  i-1<10000 ? "_0"+ (i-1) :"_"+(i-1)) + "',"); //cd_tp
		                	  if(line.indexOf("\"")>-1) {
		                		  String[] seperate = line.split("\"");
		                		  if(seperate.length==3) {
			                		  String[] data1 = seperate[0].substring(0, seperate[0].length()-1).split(",");
			                		  for(int j=0;j <data1.length; j++) {
			                			  query.append("'" + data1[j].replace("'", "''") + "',"); // replace("'", "''") : csv 값에 홑따옴표 존재 시, 삽입 방법 (ex:'tom's pen' ) 
			                		  }
			                		  query.append( "'" + seperate[1].replace("'", "''") + "',");
			                		  String[] data2 = seperate[2].substring(1, seperate[2].length()).split(",");
			                		  for(int j=0;j <data2.length; j++) {
			                			  query.append("'" + data2[j].replace("'", "''") + "',");
			                		  }
		                		  } else  if(seperate.length==5) { //size 5
		                			  String[] data1 = seperate[0].substring(0, seperate[0].length()-1).split(",");
			                		  for(int j=0;j <data1.length; j++) {
			                			  query.append("'" + data1[j].replace("'", "''") + "',");
			                		  }
			                		  query.append( "'" + seperate[1].replace("'", "''") + "',");
			                		  query.append( "'" + seperate[3].replace("'", "''") + "',");
			                		  String[] data2 = seperate[4].substring(1, seperate[4].length()).split(",");
			                		  for(int j=0;j <data2.length; j++) {
			                			  query.append("'" + data2[j].replace("'", "''") + "',");
			                		  }
		                		  } else { // every fields surround " " 
		                			  seperate = line.split("\",\""); // ","
		                			  for(int j=0;j <seperate.length; j++) {
		                				  seperate[j] = seperate[j].replaceAll("\"", "");
	                					  query.append( "'" + seperate[j].replace("'", "''") + "',");	  
		                			  }
		                		  }
		                	  } else {
		                		  String[] data = line.split(",");
			                	  for(int j=0;j <data.length; j++) {
			                		  query.append("'" + data[j].replace("'", "''") + "',");
			                	  }
		                	  }
		                	  query.append("'monitoring.CodeService', SYSDATE) \n");
	                	  }
	                	  if(i%1000==0 && i!=0) {
	                		  query.append("SELECT * FROM DUAL ");
	                		  query_list.add(query);
	                		  query= new StringBuffer();
	                		  query.append(" INSERT ALL \n");
	                	  }
	                	  i++;
	                  }
	                  if((i-1)%1000!=0) {
	                	  query.append("SELECT * FROM DUAL ");
                		  query_list.add(query);
                		  query= new StringBuffer();
	                  }
	         } catch(Exception e) {
	        	 logger.error(e.getMessage(), e.getCause());
	         }
	      }
      }
	   param.put("FK_CD_TP", fk_cd_tp);
	   codeMapper.deleteCodeTypeInfoDetailByFK_CD_TP(param);
	   
	   for(int i=0; i<query_list.size(); i++) {
		   param.put("QUERY", query_list.get(i));
		   codeMapper.fileInsert(param);
	   }
	   
	   resultMap.put("msg", "success");
	   return resultMap;
	}
	
	public List<Map<String, Object>> selectTable102(Map<String, Object> params){
		return codeMapper.selectTable102(params);
	}
}
