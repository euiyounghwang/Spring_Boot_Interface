
$( document ).ready(function() {
	select_elastic_list();
});

function save_validation(){
	// select box 선택 인 경우 저장할 내용이 없습니다.
	// select box 동일 내용 인 경우 저장할 내용이 없습니다.
	if($('#sel_ela_ip option:selected').text()=='선택'){ //차후 select box 추가시>> && 추가select box값=='선택' 
		alert('저장할 내용이 없습니다.');
		return false;
	} else if($('#sel_ela_ip option:selected').text()==$('#content tbody > tr:eq(0) > td:eq(0)>span').text().split('    ')[0]){
		alert('저장된 값과 선택한 값이 동일합니다.');
		return false;
	} else {
		return true;
	}
}

function save_configuration(){
	var validate = save_validation();
	if(validate){
		if(confirm('변경된 설정을 저장하시겠습니까?')){
			var params = {};
			if($('#sel_ela_ip option:selected').text()!='선택' && $('#sel_ela_ip option:selected').text()!=$('#content tbody > tr:eq(0) > td:eq(0) > span').text().split('    ')[0])
				params['ELASTIC_IP'] = $('#sel_ela_ip option:selected').val();
			params
			$.ajax({
				type : 'POST',
		        url: 'saveConfiguration',
		        data : JSON.stringify(params),
		        contentType: 'application/json',
		        success:function(result){
		        	alert('변경이 완료되었습니다.');
		        	select_elastic_list();
		        },
		        error : function(e){ 
		        	alert('설정 변경을 실패했습니다.');
		        }
		     });
		}
	}
	
	
	//저장 후 새로고침 
}

function select_elastic_list(){
	var params ={};
	params['FK_CD_TP'] = 'ELASTIC_IP'
		
	$.ajax({
		type : 'POST',
        url: 'selectTable102',
        data : JSON.stringify(params),
        contentType: 'application/json',
        dataType:'json',
        success:function(result){
        	//CD_V_EXPLAIN(CD_TP_MEANING), CD_TP, ACTIVE_FLAG
        	var active_ela = $.grep(result, function(v) {
        	    return v.ACTIVE_FLAG === "Y";
        	});
        	$('#content tbody > tr:eq(0) > td:eq(0) > span').text('');
        	$('#content tbody > tr:eq(0) > td:eq(0) > span').append(active_ela[0].CD_V_EXPLAIN + '(' + active_ela[0].CD_TP_MEANING + ')&nbsp;&nbsp;&nbsp;&nbsp;');
        	drawSelect($('#sel_ela_ip'), result, 'ela_ip');
        },
        error : function(e){
        	alert('failed loading data.');
        }
     });
}