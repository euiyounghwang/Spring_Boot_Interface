/**
 * SQL 저장/수정
 */

var checkedIndices =[];
var excel = false;

$( document ).ready(function() {	
	getConfigDataAsJson();
	
	$('#btn_delete').click(function(){
		$('#param_table>tbody>tr.row_selected').remove();
		$('#check-all-menu').prop('checked', false);
		deleteBtnStatus();
	});
		
	$('#check-all-menu').on('click', function(){
		var tr = $('input[name="ROWDEL"]','#param_table>tbody>tr');
		tr.prop('checked', this.checked);
	    if(this.checked){
	    	tr.closest('tr').addClass('row_selected');
	    }else {
	    	tr.closest('tr').removeClass('row_selected');
	    }
	    deleteBtnStatus();
	});
	
	$('.panel').on('click', 'input[name="ROWDEL"]', function(){
        if ( $(this).closest('tr').hasClass('row_selected') ) {
            $(this).closest('tr').removeClass('row_selected');
        }
        else {
            $(this).closest('tr').addClass('row_selected');
        }
        deleteBtnStatus();
	});
	
	if($('#OBJECT_ID').val()!==''){
		$.ajax({
			type : 'POST',
	        url:'selectSql',
	        data : JSON.stringify({'OBJECT_ID' : $('#OBJECT_ID').val()}),
	        contentType: 'application/json',
	        success:function(result){
	        	var sql = result.sql;
	        	var params = result.params; 
	        	$('#INFO_TITLE').val(sql.INFO_TITLE);
	        	$('#DESCRIPTION').val(sql.DESCRIPTION);
	        	$('#DATA_SUIT_CHK').prop('checked', sql.DATA_SUIT_CHK*1);
	        	$('#SQL_CONT').val(sql.SQL_CONT);
	        	$("#sel_host option").filter(function() {
    			    return this.text.split(' (')[0] == sql.DESCRIPTION1; 
    			}).attr('selected', true);
	        		
	        	if(params.length>0){
		        	for(var i=0; i<params.length; i ++){
	        			var type='';
	        			if(params[i].DATA_TYPE=='FUNCTION') type='checked';
		        		addRow(params[i].ELEMENT_NAME, params[i].ELEMENT_VALUE, type);
		        	}
	        	} else {
	        		$('#param_table>tbody>tr').remove();
	        	}
	        },
	        error : function(e){ 
	        	alert('SQL자원을 가져오던 중 에러가 발생했습니다.');
	        }
	     });
	}
});

function addRow(name, value, type){
    var my_tbody = document.getElementById('my-tbody');
	var row = my_tbody.insertRow( my_tbody.rows.length );
	var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    cell1.innerHTML = '<td><input type="checkbox" name="ROWDEL" ></td>';
    cell2.innerHTML = '<input type="text" name="ELEMENT_NAME" value="'+name+'" style="width:100%"/>';
    cell3.innerHTML = '<input type="text" name="ELEMENT_VALUE" value="'+value+'" style="width:100%"/>';
    cell4.innerHTML = '<td><input type="checkbox" name="DATA_TYPE" '+type+'></td>';
}

function saveSqlValidation(){
	var ELEMENT_NAME = [];
	var ELEMENT_VALUE=[];
	var parameter = [];
	var i=0;
	 $('input[name="ELEMENT_NAME"]').each(function() {
         if(this.value!='') {
        	 ELEMENT_NAME[i] = this.value;
        	 i++;
        }
    });
	 
	if($('#INFO_TITLE').val()==''){
		alert('SQL 자원을 입력해주세요.');
		return false;
	} else if($('#DESCRIPTION').val()==''){
		alert('SQL 설명을 입력해주세요.');
		return false;
	} else if($('#SQL_CONT').val()==''){
		alert('SQL 소스를 입력해주세요.');
		return false;
	} else if($('input[name="ELEMENT_NAME"]').length!=ELEMENT_NAME.length){
		alert('변수명을 입력해주세요.');
		return false;
	} else if($('#sel_host option:selected').text()=='선택'){
		alert('DB자원을 선택 해 주세요.');
		return false;
	} else {
		return true;
	}
	 
}

function saveSql(){
	var url = 'saveSql';
	var string = '저장 하시겠습니까?';
	
	if($('#OBJECT_ID').val()!=''){
		url = 'updateSql';
	}
	
	var validate = saveSqlValidation();
	
	if(validate){
		if(confirm(string)){
		var params = {};
		
		params["OBJECT_ID"]=$('#OBJECT_ID').val();
		params["INFO_TITLE"]=$('#INFO_TITLE').val();
		params["DESCRIPTION"]=$('#DESCRIPTION').val();
		params["DATA_SUIT_CHK"]=$('#DATA_SUIT_CHK').prop('checked');
		params["SQL_CONT"]=$('#SQL_CONT').val();
		params['DESCRIPTION1'] = $('#sel_host option:selected').text().split(' (')[0];
		
		var ELEMENT_NAME = [];
		var ELEMENT_VALUE=[];
		var DATA_TYPE=[];
		var parameter = [];
		var i=0;
		var j=0;
		 $('input[name="ELEMENT_NAME"]').each(function() {
             if(this.value!='') {
            	 ELEMENT_NAME[i] = this.value;
            	 i++;
            }
        });
		 if(ELEMENT_NAME.length!=0){
			 i=0;
			 $('input[name="ELEMENT_VALUE"]').each(function() {
				 if(this.value!=''){
	            	 ELEMENT_VALUE[i] = this.value;
	            } else {
	            	ELEMENT_VALUE[i] = '';
	            }
            	 i++;
	        });
			 j=0;
			 $('input[name="DATA_TYPE"]').each(function() {
				 if(this.checked){
					 DATA_TYPE[j] = 'FUNCTION';
	            } else {
	            	DATA_TYPE[j] = 'STRING';
	            }
				 j++
	        });
			 for(i=0; i<ELEMENT_NAME.length; i++){
				 parameter.push({'ELEMENT_NAME' : ELEMENT_NAME[i], 'ELEMENT_VALUE' : ELEMENT_VALUE[i], 'DATA_TYPE' : DATA_TYPE[i]});
			 }
			params["PARAMS"]=parameter;
		 }
		 
		$.ajax({
			type : 'POST',
	        url: url,
	        data : JSON.stringify(params).replace(/"\s+|\s+"/g,'"'),
	        contentType: 'application/json',
	        success:function(result){
	        	alert('SQL 저장을 완료했습니다.');
	        	loadCenterPage('interface_management/sql_list', 'Y');
	        },
	        error : function(e){ 
	        	alert('SQL 저장을 실패했습니다.');
	        }
	     });
		}
	}
}

function fnGetSelected(){
	return $('#param_table>tbody>tr.row_selected');
}

function deleteBtnStatus(){
	var row_selected = $('#param_table>tbody>tr.row_selected');
	if(row_selected.length>0){
		$('#btn_delete').prop('disabled', '');
	} else {
		$('#btn_delete').prop('disabled', 'disabled');
	}
}