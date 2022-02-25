package com.poscoict.postech.constants;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class WebConstants {
	
	public static String RESULT_CODE = "result_code"; // 검색 성공.실패 코드
	
	public static String GSA_DATA_KEY = "gsa_data_key";

	public static String GSA_ERROR_KEY = "gsa_error_key";

	public static String GSA_ERROR_KEY_MORE_INFO = "gsa_error_key_more_info";
//
//	public static String GSA_KEYWORDS_KEY = "gsa_keywords_key";
//
//	public static String IGNORE_SERVICES_KEY = "ignore-services";
//
//	public static String IGNORE_URI_KEY = "ignore-uri";
//
//	public static String RESOURCE_KEY = "ESResource";
//
//	public static String REQUEST_LANG_KEY = "user.request.lang.key";
//
//	public static String LOCALE_KOREA_KEY = "ko_KR";
//
//	public static String LOCALE_US_KEY = "en_US";
//
//	public static String SESSION_LOCALE = "SessionLocale";
//
//	public static String ECM_AUTH_RESULT_KEY = "ecm.auth.result.key";
//
//	public static String ECM_DOC_READ_ALLOW = "S";
//
//	public static String ECM_DOC_READ_REJECT = "F";
//
//	public static String PRINT_OUT_KEY = "print.out.key";
//
//	public static String LANGUAGE_KOR_CODE = "ko";
//
//	public static String LANGUAGE_ENG_CODE = "en";

	//개발계 검색엔진 IP
//	public static String GEABAL_IP_PORT_FOR_AUTOKEYWORD = "10.128.83.67:9200"; //그룹사별 키워드자동추천어 관리(개발계)
//	public static String GEABAL_IP_PORT_FOR_AUTOKEYWORD = "10.128.83.67:9200"; //그룹사별 키워드자동추천어 관리(개발계)

	//가동계 검색엔진 IP
	public static String GADONG_IP_PORT_FOR_SEARCH = "10.132.57.63:9200"; //가동계(조회용)
//	public static String GADONG_IP_PORT_FOR_SEARCH = "10.132.17.121:9200"; //가동계(조회용)
	
	//실행 Job/Feed 프로세스 관리화면 -> 황과장님 이메일로 전달함
	//실행 Job/Feed 프로세스 관리화면에서 Connector IP
//	public static String  FIND_FEED_PRO_IP = "10.132.12.75";

	//실행 Job/Feed 프로세스 관리화면에서 Connector Port
//	public static int  FIND_FEED_PRO_PORT = 7010;

	//실행 Job/Feed 프로세스 관리화면에서 Connector Port
//	public static int FIND_FEED_CONNECTION_TIMEOUT = 5000000;

	public static int CONNECT_TIMEOUT_6000 = 6000; //connectTimeout

	public static int SO_TIMEOUT = 10000; //FTPClient 소켓 타임 아웃

	//Crontab에서 주석
//	public static String SHARP = "#";

	//그룹사별 키워드자동추천어 관리에서 사용되는 코드값 TB_M02_CODE 의 CD_TP 058 로 사용중이지만 변경되면 수정
//	public static String AUTOKEYWORD_CD_TP_058 = "058";
	
	//대용량파일 다운로드 Python 경로
	//python 실행파일 경로
	//public static String PYTHON_PATH = ""; //서버경로
	public static String PYTHON_PATH = "C:\\Users\\Windows7_64bit\\AppData\\Local\\Programs\\Python\\Python35\\"; //로컬PC경로
	//python 프로젝트경로
	//public static String PY_FILE_PATH = ""; //서버경로
	//public static String PY_FILE_PATH = "D:\\WORK_SPACE\\ES\\ES_Data_Collect\\"; //로컬PC경로
	public static String PY_FILE_PATH = "/home/"; //로컬PC경로
	


	//서버별 색인 CronJob 관리-백업 폴더 경로 /WAS/DATA/ES/Monitoring_NAS/ -> 현재 호스트(10.132.12.75)만  이 폴더 있음.
	public static String BACKUP_CRONTAB_PATH = "/WAS/DATA/ES/Monitoring_NAS/";

	//서버별 색인 CronJob 관리-실제 Crontab 폴더 경로 /var/spool/cron/
	//현재 호스트(10.132.12.75)를 제외한 나머지 다른 호스트에 설정 필요 김종철 과장님에게 인수인계시 전달했음.
	public static String SAVE_CRONTAB_PATH = "/var/spool/cron/";
	
	//메뉴 배치JOB 실행서버(테스트계)
	public static String MENU_BATCH_DEV_HOST_NAME = "TLCFCON1";
	//public static String MENU_BATCH_DEV_REAL_IP = "10.132.12.88";
	public static String MENU_BATCH_DEV_REAL_IP = "10.132.12.89";
	public static String MENU_BATCH_DEV_USER_ID = "tomadmt";
	public static String MENU_BATCH_DEV_USER_PW = "posco123";
	//메뉴 배치JOB 실행서버(가동계)
	public static String MENU_BATCH_PRD_HOST_NAME = "PLCFCNA2";
	public static String MENU_BATCH_PRD_REAL_IP = "10.132.18.52";
	public static String MENU_BATCH_PRD_USER_ID = "tomadmp";
	public static String MENU_BATCH_PRD_USER_PW = "posco123";

	//검색엔진 경로설정
	public static String ELA_URI = "/_search";
	
	public static void setGadongIpPortForSearch(String ip) {
		GADONG_IP_PORT_FOR_SEARCH = ip;
	}
		
}



