<%--
/* -----------------------------------------------------------------------------
 * PROJECT : M02(검색엔진 모니터링 시스템)
 * FileName : provideIf000.jsp
 *******************************************************************************
 * 시스템별 인터페이스 검증
 *******************************************************************************
 *
 * Program History
 * ------------------------------------------------------------------------------
 * Date             Changed In      Description
 * ------------------------------------------------------------------------------
 * 2017-12-08       박현진                          최초 작성
 * 2018-09-12		이용선			POSAPIA IF 추가
 * 2018-10-12		이용선			HR Search 파라메타 검증시스템 버그 수정
 * 2018-12-04     박성은			SPRING 프로젝트로 컨버팅 
 * ------------------------------------------------------------------------------
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <div class="search_box"  id="guide_view" style="display: none;">
	<table>
		<colgroup>
			<col span="4">
			<col style="width:13px;">
			<col span="5">
		</colgroup>
		<tbody>
			<tr>
				<td><b>PATT 필수파라미터</b><br/>
				es_comp_cd : 회사코드 / es_emp_no :	사용자직번</br>
				es_user_id :	사용자ID(메일ID) / es_dept_cd : 부서코드</br>
es_sys_id : 시스템아이디 ex)[patt] / es_page_idx : 페이지번호 </br>
es_num :	한 페이지당 요청 건수 / es_keyword : 통합검색 키워드 </br>
es_prefix_type : 메뉴코드 ex)[PATT_BASE, PATT_INFO_C, PATT_INFO_P]</br>
es_operator : [must, should] </br>

</td>
			</tr>
		</tbody>
	</table>
</div>
    <div class="btns_bar">
    	<div class="left">
    		<button class="btn1" onclick="$('#guide_view').show();"><span>필수파라메타 보기</span></button>
    		<button class="btn2" onclick="$('#guide_view').hide();"><span>필수파라메타 접기</span></button>
    	</div>
    </div>
<!-- #content -->
<div id="content">	
	<!-- .ifmoniter -->
	<div class="ifmoniter">
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>parameter 입력&nbsp;&nbsp;&nbsp;</h2>
				<button class="btn1" id="json_beautify">Data정렬</button>
				<button class="btn1"  id="json_compress">한줄로</button>
				<div  class="connter">
				<textarea name="jsonParam" id="jsonParam" rows="20" style="width: 99%;">{"common":{"es_comp_cd":"30","es_emp_no":"294562","es_user_id":"cp206438","es_dept_cd":"","es_auth":"","es_sys_id":"iar","es_keyword":"","es_page_idx":"1","es_num":"1","es_prefix_type":"JOURNAL","es_operator":"OR"},"condition":{"ARTICLE_ID":"33116"},"sort":{"PBLC_YM":"desc","ARTICLE_ID":"asc"}}</textarea>
				</div>
				<div><strong>ex)localhost:8101, 192.168.79.138:8101</strong><br/>
			<input type="text" id="req_was" name="req_was" value="127.0.0.1:8101" style="width:40%"/>
			
			<select name="req_chain" id="req_chain" style="width:14%">
			<option value='S23C'>S23C</option>
			</select>
				
			<select name="req_site" id="req_site" style="width:28%">
			<option value='dataprovider_iar'>dataProvider IAR</option>
			</select>
			
			<button type="button" id="ask" class="btn1">요청</button><br/>
			<input type="text" id="otherParam" name="otherParam" value="" style="width:95%"/>
			
				</div>
			</div>
		</div>
		<!-- //.panel -->
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>검색결과</h2>
				<div class="connter" ><textarea id="resulttxtarea" rows="24" style="width: 99%;"></textarea>
				</div>
			</div>
		</div>
		<!-- //.panel -->
		<!-- .panel -->
		<div class="panel">
			<div class="section half" >
				<h2>elastic_query.xml에<br> 정의되지 않은 <br>상세 META</h2>
				<div  class="connter">
  				<ol class="q" id="nonMeatList">
  				</ol>
				</div>
			</div>
			
			<div class="section half">
				<h2>전송 요청 값</h2>				
				<div  class="connter">
  				<ol class="q" id="metaList">
  				</ol>
				</div>
			</div>
		</div>
		<!-- //.panel -->
	</div>
</div>


<script src="<%=request.getContextPath()%>/js/interface_management/interface_verify.js"></script>

