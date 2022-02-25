package com.poscoict.postech.controller;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poscoict.postech.service.MonitoringService;

@Controller
class MainController {
	
//	CodeService codeService;
	
	@Value("${spring.task.name}")
	private String taskName;
	
	@Autowired
	MonitoringService monitoringService;

//	private static Logger logger = Logger.getLogger(MainController.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/")
	public String main(HttpServletRequest request)	{
//		HttpSession session = request.getSession();
//		if(session.getAttribute("certification")==null || "N".equals(session.getAttribute("certification"))){
//			return "common/devLogin_certification";
//		}
		logger.info("main function called >> " + taskName);
		return "main";
	}
	
	@RequestMapping(value = "/applicationTask")
	@ResponseBody
	public String applicationTask(HttpServletRequest request)	{
		String msg = taskName;
		return msg;
	}

	@RequestMapping(value = "/certification", method = RequestMethod.POST)
	@ResponseBody
	public String certification(@RequestBody Map<String, Object> params, HttpServletRequest request)	{
		String msg = certificateLogin(request, params);
		return msg;
	}
	
	@RequestMapping(value = "/loadPage/{page}")
	public String loadCenterPage(@PathVariable String page, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("certification")==null || "N".equals(session.getAttribute("certification"))){
			return "common/devLogin_certification";
		}
		return page;
	}
	
	public String certificateLogin(HttpServletRequest request, Map<String, Object> pass) {
		String msg = "";
		HttpSession session = request.getSession();
		String securedPassword = (String) pass.get("securedPassword");
		PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
		
		if (privateKey == null || securedPassword==null) {
			return "common/devLogin_certification";
		}
		
		String password = "";
		try {
			password = decryptRsa(privateKey, securedPassword);
		} catch (Exception e) {
			
		}
		
//		Map<String, String> params = new HashMap<String, String>(); 
//		params.put("fk_cd_tp", "064");
//		params.put("cd_tp", "064_0000");
//		params.put("company_code", "00");
//		String real_pass = null;
//		List<Map<String, String>> rowset = codeService.selectCodeInfo(params);
//		if(rowset.size()>0) {
//			for(Map<String, String> row : rowset) {
//				real_pass = row.get("CD_TP_MEANING")== null ? ""
//						: (String) row.get("CD_TP_MEANING");
//			}
//		}
		
//		boolean matchPassword = BCrypt.checkpw(password, real_pass);
		
		//System.out.println("password >> " + password);
		
//		boolean matchPassword = BCrypt.checkpw(password, real_pass);
		
		boolean matchPassword = false;
		if(password.equals("gsaadmin")) {
			matchPassword = true;
		}
				
		if (matchPassword) {
			msg = "success";
			session.setAttribute("certification", "Y");
		}else {
			msg = "fail";
			session.setAttribute("certification", "N");
		}
		
		return msg;
	}
	
	public static String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
	
	/**
     * 16진 문자열을 byte 배열로 변환한다.
     * 
     * @param hex
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
 
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
	
	@RequestMapping(value = "/loadPage/{page1}/{page2}")
	public String loadCenterPage2(@PathVariable String page1,@PathVariable String page2, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("certification")==null || "N".equals(session.getAttribute("certification"))){
			return "common/devLogin_certification";
		}
		return page1+"/"+page2;
	}
	
	@RequestMapping(value = "/loadPage/{page1}/{page2}" , method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView loadCenterPage(@PathVariable String page1,@PathVariable String page2, @RequestBody Map<String, Object> params, Model model, HttpServletRequest request){
		
		if(params.containsKey("CD_TP")) { //code
			model.addAttribute("company_code", params.get("COMPANY_CODE"));
			model.addAttribute("cd_tp", params.get("CD_TP"));
			model.addAttribute("cd_tp_meaning", params.get("CD_TP_MEANING"));
		} else if(params.containsKey("OBJECT_ID")) { //sql
			model.addAttribute("OBJECT_ID", params.get("OBJECT_ID"));
		} else if(params.containsKey("HOST_NAME")) { //code
			model.addAttribute("host_name", params.get("HOST_NAME"));
			model.addAttribute("real_ip", params.get("REAL_IP"));
			model.addAttribute("user_id", params.get("USER_ID"));
			model.addAttribute("user_pw", params.get("USER_PW"));
		} else if(params.containsKey("SEQ")) { //code
			model.addAttribute("seq", params.get("SEQ"));
			model.addAttribute("start_date", params.get("START_DATE"));
			model.addAttribute("end_date", params.get("END_DATE"));
			model.addAttribute("doc_title", params.get("DOC_TITLE"));
			model.addAttribute("owner_name", params.get("OWNER_NAME"));
		}
		return new ModelAndView(page1+"/"+page2);
	}
}
