/**
 * 서버 대상 항목 관리
 */

//var checkedValue = {}; //[page][rownum]
var table;
var checkedIndices =[];

$( document ).ready(function() {
	
	selectCodeType('sel_server_type','SERVER_TYPE','','','','selCode');
	selectCodeType('sel_infra_type','INFRA_TYPE','','','','selCode');
	selectCodeType('sel_host','HOST_NAME','','','','selCode');
	
	table = $('#center .grid_table').DataTable( {
        "serverSide": true,
        "paging": true,
        pagingType: "full_numbers_no_ellipses",
        columns : [
        	{
	        	data : '',
	            render: function (data, type, full, meta) { 
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+')">';
	            }
			},
			{data : 'SERVER_TYPE'},
			{data : 'INFRA_TYPE'},
			{
				data : 'HOST_NAME',
				render: function (data, type, full, meta) {
					return '<a href="#" title="수정" onclick="setInputbox(&quot;'+meta.row+'&quot;); return false;">'+data+'</a>';
				}
			},
			{data : 'SERVER_NAME'},
			{data : 'POSCO_NAT_IP'},
			{data : 'REAL_IP'},
			{data : 'SERVICE_IP'},
			{data : 'PORT'},
			{data : 'USER_ID'},
			{data : 'USER_PW'},
			{data : 'DESCRIPTION'}
		],
		searching:true,
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		info:true,
		columnDefs:[
			{targets:[0,1,2,4,5,7,8,9,10,11], orderable:false},
			{targets:[0,1,2,3,4,5,6,7,8,9,10,11], className:'dt-body-center'},
		],
		order:[3,'asc'],
		"ajax": { //data를 가져올 ajax 통신
			type : 'POST',
            url: "findServerList",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) { //통신 전 보낼 data 재정의. d: dataTables 기본 데이터 format.            	
            	$('#check-all-menu').prop('checked', false);
            	checkedIndices = [];
            	deleteBtnStatus();
            	            	
            	d['EVENT_ID'] = 'findServerList';
            	d['P_SERVER_TYPE'] = $('#sel_server_type option:selected').val();
            	d['P_INFRA_TYPE'] = $('#sel_infra_type option:selected').val();
            	d['P_HOST_NAME'] = $('#sel_host option:selected').val();
            	
            	if($('#sel_server_type').val()!='' && $('#sel_server_type option:selected').text()!='선택') d['P_SERVER_TYPE'] = $('#sel_server_type option:selected').val();
            	if($('#sel_infra_type').val()!='' && $('#sel_infra_type option:selected').text()!='선택') d['P_INFRA_TYPE'] = $('#sel_infra_type option:selected').val();
            	if($('#sel_host').val()!='' && $('#sel_host option:selected').text()!='선택') d['P_HOST_NAME'] = $('#sel_host option:selected').val();
          	
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) { // 테이블 그리기가 끝난 후 호출되는 함수.
            hideProgress();
        	if(table.rows()[0].length > 0){
	        	var tr = $('#center .grid_table tr');
	        	tr= tr.splice(1, tr.length-1);
        	}
        },
        lengthMenu: [[15, 30, 100], [15, 30, 100]],
        dom: '<"top"li>t<"bottom"p><"clear">'
    });
	
	$('#sel_server_type').on('change', function() {
		if($('#sel_server_type').val()!=''  && $('#sel_server_type option:selected').text()!='전체')
			selectCodeType('sel_host','HOST_NAME',$('#sel_server_type').val(),$('#sel_infra_type').val(),'', 'selCode');
	});
	
	$('#sel_infra_type').on('change', function() {
		if($('#sel_infra_type').val()!='' && $('#sel_infra_type option:selected').text()!='전체')
			selectCodeType('sel_host','HOST_NAME',$('#sel_server_type').val(),$('#sel_infra_type').val(),'', 'selCode');
	});
	
	table.on( 'page.dt', function () {
		$('#center').animate({scrollTop: 0}, 100);
	} );	
	
	$('#btn_refresh').click(function(){
		search();
	});
	
	$('#btn_delete').click(function(){
		if(checkedIndices.length>0){
			if(confirm('선택한 서버 정보를 삭제하시겠습니까?')){
				deleteServerInfo();
			}
		} else {
			alert('삭제할 서버 정보를 선택하세요.');
		}
	});
	
	$('#check-all-menu').on('click', function(){
	    var rows = table.rows({ 'search': 'applied' }).nodes();
	    $('input[type="checkbox"]', rows).prop('checked', this.checked);
	    
	    checkedIndices = [];
	    if(this.checked){
	    	for(var i=0; i<rows.length; i++){
	    		checkedIndices[i] = i;
	    	} 
	    }
	    deleteBtnStatus();
	});
});

function deleteServerInfo(){
	var params ={};
	params['id'] = '';
	
	for(var i=0; i <checkedIndices.length; i++){
		params['id'] += table.rows().data()[checkedIndices[i]]['ID'] + ",";
	}
	params['id'] = params['id'].substr(0, params['id'].length-1);
	//alert(params['id']);
	$.ajax({
		type : 'POST',
        url:'deleteServerInfo',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	var errStr = JSON.parse(result);
        	
        	if(errStr.errors == false){
            	alert('서버 정보를 삭제 하였습니다.');
            	search();        		
        	} else {
        		alert("처리 중 에러가 발생하였습니다.");
        	}
        },
        error : function(e){ 
        	alert('서버 정보 삭제 중 에러가 발생했습니다.');
        }        
     });
}

function search(){
	$('#check-all-menu').prop('checked', false);
	table.ajax.reload();
}

function insertServerValidation(){
	var params = $('.write_box :input').serializeObject();
	Object.keys(params).forEach(function(key) {
		if(params[key]!='' && params[key]!=undefined && params[key]!=null){
			var id = key.toLowerCase();
			$('#'+id).val(params[key].trim());
		}
	});
	params = $('.write_box :input').serializeObject();
	
	if(params['server_type'] == "선택"){
		params['server_type']="";
	}
	
	if(params['infra_type'] == "선택"){
		params['infra_type']="";
	}

	if(params['server_type']=='' 
		|| params['infra_type']==''
			|| params['host_name']==''
				|| params['real_ip']==''){
		alert('필수항목을 모두 입력해주세요.');
		return false;
	} else {
		return true;
	}
}

function saveSeverInfo(){
	
	var url = 'insertServerInfo';
	var string = '';
	
	if($('#id').val() != ""){
		string = '서버 정보를 수정 하시겠습니까?';
	}else{
		string = '서버 정보를 등록 하시겠습니까?';
	}
	
	var validate = insertServerValidation();
	  
	if(validate){
		if(confirm(string)){
		var params = $('.write_box :input').serializeObject();
		$.ajax({
			type : 'POST',
	        url: url,
	        data : JSON.stringify(params).replace(/"\s+|\s+"/g,'"'),
	        contentType: 'application/json',
	        success:function(result){
	        	var errStr = JSON.parse(result);

	        	if(errStr.errors == false){
		        	alert("정상 처리 되었습니다.");
		        	closeInsertDiv();
		        	search();
	        	} else {
	        		alert("처리 중 에러가 발생하였습니다.");
	        	}
	        },
	        error : function(e){ 
	        	alert('서버 정보 등록 중 에러가 발생하였습니다.');
	        }
	     });
		}
	}
	
}

function setInputbox(index){
	var row = table.rows().data()[index];
	$('.write_box :input').val('');

	selectCodeType('server_type','SERVER_TYPE','','','','selCode');
	selectCodeType('infra_type','INFRA_TYPE','','','','selCode');
	
	Object.keys(row).forEach(function(key) {
		
		if(row[key]!=='' && row[key]!==undefined && row[key]!==null){
			var id = key.toLowerCase();
			$('#'+id).val(row[key]);			

			if(id=='server_type'){
				setTimeout(function(){		
					$("#server_type option:contains('" + row[key] +"')").prop('selected', true);
				},700);
			}else if(id=='infra_type'){
				setTimeout(function(){
					$("#infra_type option:contains('" + row[key] +"')").prop('selected', true);	
				},700);
			}
			
		}
		//selectCodeType('server_type','SERVER_TYPE',$("#server_type option:selected").text().trim(),'','','hostAll');
	});

	//openInsertDiv();
	$('#data_insert').show();
}

function closeInsertDiv(){
	$('.write_box :input').val(''); 
	$('#data_insert').hide();
}

function openInsertDiv(){
	$('#data_insert').show();
	
	selectCodeType('server_type','SERVER_TYPE','','','','selCode');
	selectCodeType('infra_type','INFRA_TYPE','','','','selCode');
}

function checkIndex(index){
	if(checkedIndices.indexOf(index)>-1){
		var removeIdx = checkedIndices.indexOf(index);
		checkedIndices.splice(removeIdx, 1);
	}else{
		checkedIndices[checkedIndices.length] = index;
	}
	//console.log('checkedIndices:'+checkedIndices);
	deleteBtnStatus();
}

function deleteBtnStatus(){
	if(checkedIndices.length>0){
		$('#btn_delete').prop('disabled', false);
	} else {
		$('#btn_delete').prop('disabled', true);
	}
}