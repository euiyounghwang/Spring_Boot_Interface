var jsonparameter="";
$(document).ready(function(){
	setDashboard();
	resetResult();
	$(window).unbind('resize.setDashboard').bind('resize.setDashboard',setDashboard);
	$('#right').unbind('show.setDashboard hide.setDashboard').bind('show.setDashboard hide.setDashboard',setDashboard);
	function setDashboard(){
		if($('#content .ifmoniter')[0]){
			var dashboard = $('#content .ifmoniter');
			if(dashboard.width() < 1300){
				dashboard.addClass('col2');
			}else{
				dashboard.removeClass('col2');
			}
		}
	}
	  
	$('#ask').click(function(){
		jsonChk(); 
	});
	
	$('#json_beautify').click(function(){
		json_beautify('jsonParam'); 
	});
	
	$('#json_compress').click(function(){
		json_compress();
	});
		
	$('#req_site').on('change', function() {
		var selectParam = '';		
		var selectSite = document.getElementById("req_site").value;
		var selectOtherParam = '';		
		
		if(selectSite == 'ict_hrsearch') selectParam = '{"es_keyword":"이현","es_page_idx":"1","es_auth":"1","es_req_id":"1","es_sys_id":"ict_hrsearch_idx","es_comp_cd":"01","es_num":"10","es_dept_cd":"38_UA000","es_emp_no":"946","es_user_id":"285264","HR.STATUS":"1"}';
		else if(selectSite == 'posco_hrsearch') selectParam = '{"es_keyword":"용강","es_sys_id":"posco_hrsearch","es_page_idx":"1","es_req_id":"1","es_num":"50","es_auth":"","es_dept_cd":"38_ZZZZZ","es_comp_cd":"30","es_emp_no":"pd292816","es_user_id":"pd292816","es_keyword_match":"","es_keyword_include":"","es_keyword_exclude":"","HR.STATUS ":"1","HR.INVENTORY_ITEM_STATUS_CODE":"10,20"}';
		else if(selectSite == 'posco_posapia') selectParam = '{"common":{"es_comp_cd":"30","es_emp_no":"271610","es_user_id":"jinha.jeong","es_sys_id":"posco_posapia","es_keyword":"","es_page_idx":"1","es_num":"10","es_req_id":"2","es_search_type":"A","es_exact_fields":["PUR_MAT_ITEM_SPEC","SBC_NM_KO"]},"condition":{"PUR_MAT_ITEM_ID":"","PUR_MAT_ITEM_ITDS_ID":"","ITEM_NUMBER":"","PUR_MAT_ITEM_CUR_STAT_NM":"","PUR_MAT_ITEM_ITDS":"^Screw^ OR ^test^","SBC_ID":"","SBC_NM_KO":"","SBC_NM_EN":"","SBC_NM_CN":"","SBC_CNT":"","PUR_MAT_ITEM_SPEC":"","PUR_MAT_ITEM_PRE_SPEC":"","PUR_MAT_ITEM_REV_NO":"","PUR_MAT_ITEM_UT":"","PUR_MAT_ITEM_SLY_TP":"","PUR_MAT_ITEM_ICZJ_F":"","PUR_MAT_ITEM_PLAN_NO":"","PUR_MAT_ITEM_PLAN_LNNZ":"","DRAW_CNT":"","IMAGE_ID":"","SPEC.PUR_MAT_ITEM_SPEC_ID":["133","560"],"SPEC.PUR_MAT_ITEM_SPEC_ITEM_V_133":"","SPEC.PUR_MAT_ITEM_SPEC_ITEM_V_560":"","SPEC.~~":""},"exists":{"PUR_MAT_ITEM_ITDS":"yes","IMAGE_ID":"","SPEC.PUR_MAT_ITEM_SPEC_ITEM_NM_101":"","SPEC.PUR_MAT_ITEM_SPEC_ITEM_NM_102":""},"range":{"CREATION_TIMESTAMP":"2000-01-01..","LAST_UPDATE_TIMESTAMP":"","SPEC.CREATION_TIMESTAMP_501":"","SPEC.LAST_UPDATE_TIMESTAMP_501":"","SPEC.CREATION_TIMESTAMP_502":"","SPEC.LAST_UPDATE_TIMESTAMP_502":""},"sort":{"CREATION_TIMESTAMP":"desc","LAST_UPDATE_TIMESTAMP":"asc","SPEC.PUR_MAT_ITEM_SPEC_ITEM_NM_133":"desc","SPEC.PUR_MAT_ITEM_SPEC_ITEM_V_133":"asc","SPEC.PUR_MAT_ITEM_SPEC_ITEM_NM_560":"desc","SPEC.PUR_MAT_ITEM_SPEC_ITEM_V_560":"asc"}}';
		else if(selectSite == 'posco_e-catalog') selectParam = '{"common":{"es_comp_cd":"30","es_emp_no":"294562","es_user_id":"pd294562","es_dept_cd":"","es_auth":"","es_sys_id":"posco_e-catalog","es_keyword":"","es_page_idx":"1","es_num":"10","es_req_id":"2","es_search_type":"A","es_exact_fields":["CORP_CD"]},"condition":{"CATALOG_ID":"","CATALOG_NO":"","CORP_CD":"^31^","CORP_NM":"","LC_ID":"","LC_NM":"","CATALOG_NM":"^Hydraulic Valve^","USE_NM":"","SPCL_NM":"","CUR_CD":"","CAT_COST":"","CAT_WT":"","CAT_UNIT":"","CAT_AREA":"","CAT_SPEC":"","CAT_MODEL":"","CAT_MAKER":"","QCODE":"","ACTIVE_YN":"","APPL_ID":"","MAIN_IMG_NM":"","MAIN_IMG_PATH":"","IMG_NM1":"","IMG_PATH1":"","IMG_NM2":"","IMG_PATH2":"","FILE_NM":"","FILE_PATH":"","URL1":"","URL2":"","CREATION_TIMESTAMP":"","LAST_UPDATE_TIMESTAMP":"","SPEC.LC_SPEC_ID":["51"],"SPEC.LC_SPEC_VALUE_51":"^FOR^"},"exists":{"ACTIVE_YN":"yes","CUR_CD":""},"range":{"CREATION_TIMESTAMP":"2018-01-01..","LAST_UPDATE_TIMESTAMP":"2018-01-01..2019-07-31","SPEC.SPEC_CREATION_TIMESTAMP_51":"2018-01-01..","SPEC.SPEC_LAST_UPDATE_TIMESTAMP_51":"2018-01-01..2019-07-31"},"sort":{"CREATION_TIMESTAMP":"desc","LAST_UPDATE_TIMESTAMP":"asc"}}';
		else if(selectSite == 'patt') selectParam = '{"common":{"es_comp_cd":"30","es_emp_no":"292816","es_user_id":"euiyoung.hwang","es_dept_cd":"3ACA0","es_auth":"","es_sys_id":"patt","es_keyword":"","es_keyword_test":"","es_page_idx":"1","es_num":"15","es_prefix_type":"PATT_BASE","es_operator":"AND"},"condition":{"PATT_APPL_DT":"1983-01-01..2020-01-01","REPORT_EMP_NM_REP":"김효준","PATT_APPL_TYPE_CD":"SUGS01,SUGS011,-SUGS011","PATT_APPL_SECT":"AOR,-AOR1","WORKS_CODE":"B,A,-C","PATT_SUGG_ACPT_DT":"1983-01-01..","PATT_LAST_DC_EV_DT":"1983-01-01..2020-01-01","PATT_LAST_GRD":"B,A,-C,-D,-A","PATT_TRAN_OS_YN":"N,A,-C,-D,-A","PATT_NATN_SCOPE":"KR"}}';
//		else if(selectSite == 'dataprovider_citizen') selectParam = '{"common":{"es_comp_cd":"30","es_emp_no":"294562","es_user_id":"pd294562","es_dept_cd":"3ACA0","es_auth":"","es_sys_id":"citizen","es_keyword":"","es_page_idx":"1","es_num":"15","es_prefix_type":"","es_operator":"AND"},"condition":{"CATEGORY":"B0013"}}';
//		else if(selectSite == 'dataprovider_citizen') selectParam = '{"common":{"es_comp_cd":"30","es_emp_no":"294562","es_user_id":"pd294562","es_dept_cd":"3ACA0","es_auth":"","es_sys_id":"citizen","es_keyword":"","es_page_idx":"1","es_num":"15","es_prefix_type":"","es_operator":"AND"},"condition":{"PREFIX":"JOURNAL","TITLE":"Electronic"}}';
		else if(selectSite == 'dataprovider_iar') selectParam = '{"common":{"es_comp_cd":"30","es_emp_no":"294562","es_user_id":"pd294562","es_dept_cd":"3ACA0","es_auth":"","es_sys_id":"iar","es_keyword":"","es_page_idx":"1","es_num":"15","es_prefix_type":"JOURNAL","es_operator":"AND"},"condition":{"TITLE":"Electronic"}}';
		else if(selectSite == 'provideAIF'){
			selectParam = '{"reqsysid":"pride","subject":"","intext":"","allintext":"연구","srch_con_cont":"키1 키2 키3","operator":"OR","owner":"","cdaterange":"2018-01-11..2018-04-11","dept_name":"전사","menu_id":13,"sortmeta":"meta:INPUTDATE:D::ED"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=posco_pride&sysId=pride';
		}
		else{
			selectParam = '{"reqsysid":"posco_today","keyword":"남기고싶은이야기","menu_code":"001001|MN004001","menu_sub_code":"","usrid":"PC912346","sortmeta":"meta:INPUTDATE:D::ED","search_type":"E","subject":"","intext":"","allintext":""}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=posco_today&sysId=posco_today';
		}

		
		//요청 param 예제 표시하기
		document.getElementById("jsonParam").value = '';
		document.getElementById("jsonParam").value = selectParam;
		document.getElementById("otherParam").value = '';
		document.getElementById("otherParam").value = selectOtherParam;
	});
	
	window.onload = function(e){ 
	    companycheck();
	}
	
	$('select').on('change', function() {
		resetResult();
	});
});

function json_beautify(id){
	var jsonDataVal = document.getElementById(id).value; // textarea의 데이터를 가져온다.
	var jsonData = JSON.parse(jsonDataVal); // json object로 만든다.
	var jsonBeauty = JSON.stringify(jsonData, null, 4);//정렬
	document.getElementById(id).value = '';
	document.getElementById(id).value = jsonBeauty;
}

function json_compress(){
    var value = document.getElementById("jsonParam").value;// textarea의 데이터를 가져온다.
//    value = value.replace(/\s+/gi, "");// 공백제거 
    value = value.replace(/\s+/gi, "");// 공백제거
    value = value.replace("AND", " AND ");// 공백제거
    value = value.replace("OR", " OR ");// 공백제거
	document.getElementById("jsonParam").value = '';
	document.getElementById("jsonParam").value = value;
}

function companycheck(){
	 var data = document.getElementById('jsonParam').value;
	 if(data.indexOf("ict_hrsearch")>0){
		
			document.getElementById('POSCOmore').style.display="none";
			document.getElementById('POSCO').style.display="none";

		 	document.getElementById('ICTmore').innerText="전송요청값 펼쳐보기";
			document.getElementById('ICTmore').style.display="";
			
		}else if(data.indexOf("posco_hrsearch")>0){
			
			document.getElementById('ICTmore').style.display="none"
			document.getElementById('ICT').innerHTML="";
			
			document.getElementById('POSCOmore').innerText="전송요청값 펼쳐보기";
			document.getElementById('POSCOmore').style.display="";
			
			
		}
}

function replaceAll(str,search, replace) {
    return str.split(search).join(replace);
} 

function jsonChk(){
	var flag = false;

	try{
		var data = document.getElementById('jsonParam').value;		
		jsonparameter=JSON.parse(data+'');
		if(data.indexOf("reqsysid")>0){
			flag=true;			
		}
		 
		$(document.getElementById("metaChk")).html(''); 
		$(document.getElementById("POSCO")).html('');
		$(document.getElementById("ICT")).html('');
		document.getElementById("resulttxtarea").value = '';//기존 검색결과 초기화
		
		if(flag){
			fnCallProvideIF('xml');
		}else{
			fnCallProvideIF('json');
		}
	
	}catch (e){
		 alert("error : "+e.message);
	}
		
	
}

function fnCallProvideIF(rltType) {
	showProgress();
	resetResult();
	 var params = {};
	 params['req_site'] = $('#req_site').val();
	 params['req_chain'] = $('#req_chain').val();
	 params['req_was'] = $('#req_was').val();
	 params['jsonParam'] = $('#jsonParam').val();
	 params['otherParam'] = $('#otherParam').val();
	$.ajax({
		type : 'POST',
        url:'callProvideIF',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(result){
        	if(result.result_code=='success'){ //draw meta table
        		if(result.metaList != undefined){
        			var metaList = result.metaList;
        			var metaListArray = metaList.split(',');
        			var metaListObject = {};
        			
        			var jsonParam = JSON.parse(result.param);
        			var paramMetas = [];
        			var cnt=0;
        			
        			var patt_prefix_type;
        			for(key in jsonParam){ // metas from parameter
        				if(params['req_site'].indexOf("patt")>-1 || params['req_site'].indexOf("dataprovider")>-1){
        					var key_1lv = key;
        					for(key in jsonParam[key_1lv]){ // metas from parameter
        						var key_2lv = key
        						var keyName = key_2lv;
        						if(key_1lv.indexOf("common")>-1 && key_2lv.indexOf("es_prefix_type")>-1){
        							patt_prefix_type = jsonParam[key_1lv][key_2lv];
        						}
        						if(key_1lv.indexOf("condition")>-1){
        							if(patt_prefix_type!=null && patt_prefix_type.length>0) keyName=patt_prefix_type+"."+key_2lv;
        							else keyName=key_2lv;
        						}
        						
            					paramMetas[cnt] = keyName+':'+jsonParam[key_1lv][key_2lv];
            					cnt++;
        					}
        				}else{
            				if(key.split('.').length==2){
            					paramMetas[cnt] = key+':'+jsonParam[key];
            					cnt++;
            				}
            				patt_prefix_type="HR";
        				}
        				
        			}
        			cnt = 0;
        			var undefinedMetas = [];
        			for(var i=0; i<paramMetas.length; i++){
        				var index = metaListArray.indexOf(paramMetas[i].split(':')[0]);
        				if(index>-1){
        					metaListArray[index] = paramMetas[i].split(':')[0]+'.' +paramMetas[i].split(':')[1]; 
        				}else {
        					undefinedMetas[cnt] = paramMetas[i].split(':')[0];
        					cnt++;
        				}
        			}
        			for(var i=0; i<undefinedMetas.length; i++){
        				$('#nonMeatList').append('<li>'+undefinedMetas[i]+'</li>');
        			}
        			for(var i=0; i<metaListArray.length; i++){
        				metaListObject[metaListArray[i].split('.')[0]]=metaListObject[metaListArray[i].split('.')[0]]!=undefined ?metaListObject[metaListArray[i].split('.')[0]] + metaListArray[i]+'|' :  metaListArray[i]+'|';
        			}
        			var metaCnt = 0;
        			for(key in metaListObject){
    				   metaListObject[key] = metaListObject[key].substr(0, metaListObject[key].length-1);
    				   
    				   var list = metaListObject[key].split('|');
    				   $('#metaList').append('<li>'+key);
    				   $('#metaList > li:eq('+metaCnt+')').append('<dl class="inline">');
    				   for(var i=0;i <list.length; i++){
    					   if(patt_prefix_type!=null && patt_prefix_type.length>0){
    						   if(list[i].split('.').length == 3 && list[i].split('.')[2].length>0){
    							   $('#metaList > li:eq('+metaCnt+') > dl').append('<dt class="bold">'+list[i].split('.')[0]+'.'+list[i].split('.')[1]+'</dt><dd>'+list[i].split('.')[2]+'</dd>');        						   
        					   } else { // length 2 contents exists 
        						   $('#metaList > li:eq('+metaCnt+') > dl').append('<dt>'+list[i]+'</dt><dd></dd>');
        					   }
    					   }else{
    						   if(list[i].split('.').length == 2 && list[i].split('.')[1].length>0){        						   
        						   $('#metaList > li:eq('+metaCnt+') > dl').append('<dt class="bold">'+list[i].split('.')[0]+'</dt><dd>'+list[i].split('.')[1]+'</dd>');
        					   } else { // length 1 contents exists 
        						   $('#metaList > li:eq('+metaCnt+') > dl').append('<dt>'+list[i]+'</dt><dd></dd>');
        					   }
    					   }
    					   
    				   }
    				   metaCnt++;
    				}
        		}
        		$("#resulttxtarea").val(result.gsa_data_key);
        		if(rltType=='json') json_beautify('resulttxtarea');
        	} else {
        		var errorMsg;
        		errorMsg = result.gsa_error_key;
        		if(result.gsa_error_key_more_info != undefined){
        			errorMsg += "\n"+result.gsa_error_key_more_info;
        				}
        		$("#resulttxtarea").val(errorMsg);
        	}
        },
        complete : function(){
        	hideProgress();
        },
        error : function(e){ 
        	console.log("callProvideIF Error");
        }
     });
} 

function resetResult(){
	$('#metaList').empty();
	$('#nonMeatList').empty();
	$('#resulttxtarea').val('');
}

function fnOther(){		
	alert("HRSEARCH 외 타 시스템 적용 준비중");
}