/* ===================================================================== 
 * @FileName	: 
 * @Author      : fe3388 
 * @Date        : 2012. 9. 11. 
 * @Version		: 
 * @Description	: 
 ===================================================================== */

package com.poscoict.postech.model;

import java.io.Serializable;
/** 
 * @FileName	: ESUser.java
 * @Description	: SSO 인증 후 SWP 검색 AP 에서 session 에 적재 후 활용되는 사용자 정보를 정의 
 */
public class ESUser implements Serializable {
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 3463766719799149107L;
	
	
	/**
	 * -----------------------------------------------------
	 * SSO 항목 정의 (기본 정보)
	 * -----------------------------------------------------
	 */
	
	/** 사용자의 고유한 ID (email의 앞 부분 ) */
	private String usrId; // iv-user

	/** 사번 */
	private String empNo;

	/** 회사코드 */
	private String companyCode;


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the usrId
	 */
	public String getUsrId() {
		return usrId;
	}

	/**
	 * @param usrId
	 *            the usrId to set
	 */
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	/**
	 * @return the spEmpNo
	 */
	public String getEmpNo() {
		return empNo;
	}

	/**
	 * @param spEmpNo
	 *            the spEmpNo to set
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode
	 *            the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

}
