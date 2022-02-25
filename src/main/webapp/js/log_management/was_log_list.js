/**
 * was 서버 로그 조회
 */

//document.write("<script src='/monitoring/js/search_monitoring/search_engine_node_list.js'></script>");
//<script src='/monitoring/js/search_monitoring/search_engine_node_list.js'></script>
//require("/monitoring/js/search_monitoring/search_engine_node_list.js");
var table;
var excel = false;

function includeJs(jsFilePath) {
    var js = document.createElement("script");

    js.type = "text/javascript";
    js.src = jsFilePath;

    document.body.appendChild(js);
}

//includeJs("/monitoring/js/search_monitoring/search_engine_node_list.js");
//clearInterval(timer);
//isPause=true;
//timer=null;

$( document ).ready(function() {
	selectCodeType('sel_server_conf','INFRA_TYPE','WAS','','','selCode');
	setTimeout(function(){ 
		$("#sel_server_conf option:contains('가동계')").prop('selected', true);
		selectCodeType('sel_host','HOST_NAME','WAS',$("#sel_server_conf option:selected").text().trim(),'','hostAll'); 
	},700);
		
//	selectChainCode();
//	selectLogLevel();

	$('#startDate').datepicker({
		inline: true,
		changeMonth: true,
		changeYear: true,
		yearRange: "1990:+0",
		dateFormat: "yy-mm-dd"
	});
	$('#startDate').datepicker('setDate', -1);
	
	$('#endDate').datepicker({
		inline: true,
		changeMonth: true,
		changeYear: true,
		yearRange: "1990:+0",
		dateFormat: "yy-mm-dd"
	});
	$('#endDate').datepicker('setDate', new Date());
	
	table = $('#center .grid_table').DataTable( {
		ordering: false,
        "serverSide": true,
        "paging": true,
        "autoWidth": false, //자동 열 너비 계산을 사용 또는 사용하지 않도록 설정한다.(true일 경우 td에 width값이 px단위로 고정되어 window resize 이벤트에 대응 안됨)
        pagingType: "full_numbers_no_ellipses",
        columns : [
			{data : 'INDEX'},
			{data : 'ID'},
			{data : 'LOG_PATH'},
			{data : 'TIMESTAMP'},
			{data : 'HOST'},
			{data : 'LOG_LEVEL'},
			{data : 'HANDLER'},
			{data : 'SSO_USER_NAME'}
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
			{targets:[0,1,2,3,4,5,6,7], className:'dt-body-center'},
			{ "orderable": false, "targets": [0,1,2,3, 4, 5, 6,7] },
		],
		"rowCallback": function( row, data, index ) {
		    if ( data["LOG_LEVEL"] == "ERROR" ){
		        $('td', row).css('background-color', '#F5BCA9');
		    }
		   else if ( data["SSO_USER_NAME"].indexOf("pd294562") < 0){
		        $('td', row).css('background-color', '#CEE3F6');
		        $('td', row).css('font-weight', '900');
		       }
		   else if ( data["LOG_TEXT"].indexOf("FILE_UPLOAD") > -1){
		        $('td', row).css('background-color', '#CEE3F6');
		        $('td', row).css('font-weight', '900');
		       }
		},
		"ajax": {
			type : 'POST',
            url: "findWasServerLog",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
            	showProgress();
            },
            data: function ( d ) {
            	if(excel){
            		d['start']  = 0;
            		d['length'] = table.settings()[0]._iRecordsDisplay;
            	} else {
            		if(d['draw']!=1)
            			d['start'] = table.settings()[0]._iDisplayStart;
            	}
            	d['EVENT_ID'] = 'findWasServerLog';
            	d['P_CHAIN_CODE'] = $('#sel_chain_code option:selected').text()=='전체'?'':$('#sel_chain_code option:selected').text().trim();
            	d['P_LOG_LEVEL'] = $('#sel_log_level option:selected').text()=='전체'?'':$('#sel_log_level option:selected').text().trim();
            	
            	if($('#sel_host option:selected').text()==''){ //before selectbox loaded setting parameters..
            		d['P_HOST_NAME'] = "PLCIUADA1|PLCIUADA2";
            	} else {
            		d['P_HOST_NAME'] = $('#sel_host option:selected').text()=='전체'?$('#sel_host option:selected').val():$('#sel_host option:selected').text().trim();
            	}
            	d['START_DATE'] = $('#startDate').val();
            	d['END_DATE'] = $('#endDate').val();
            	d['P_SERVER_TYPE_CD'] = '20';
            	d['P_SERVER_CONF_CD'] = $('#sel_server_conf option:selected').val();
            	d['search'].value = $('#input_search').val();
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
        	hideProgress();
        	//second row add : log message 
        	if(table.rows()[0].length > 0){
	        	var tr = $('#center .grid_table tr');
	        	tr= tr.splice(1, tr.length-1);
	        	for(var i =0; i<tr.length; i++){
	        		var row = table.row( tr[i] );		
	        		row.child( secondRowFormat(row.data()) ).show();
	        		$(row.child()[0]).children().children().removeAttr('style');
	        		$(row.child()[0]).children().children().children().children().children().css('border-bottom', '0px');
	        		if (row.data().LOG_LEVEL == "ERROR" ){
	        			$(row.child()[0]).css('background-color', '#F5BCA9');
	        			$(row.child()[0]).children().children().children().children().children().css('background-color', '#F5BCA9');
	        	    }
	    		}
        	}
        },
        lengthMenu: [[10, 50, 100], [10, 50, 100]],
        dom: '<"top"li>t<"bottom"p><"clear">'
    });
	
	table.on( 'page.dt', function () {
		$('#center').animate({scrollTop: 0}, 100);
	} );
	
	$('#btn_refresh').click(function(){
		search();
	});
	
	$('#sel_server_conf').on('change', function() {
		if($('#sel_server_conf').val()!='')
			selectCodeType('sel_host','HOST_NAME','WAS',$("#sel_server_conf option:selected").text().trim(),'','hostAll');
	});

	$('#input_search').on('keypress', function (e) {
	       if(e.which === 13){
    	   		search();
	       }
	});
});

function secondRowFormat (d) {		
    return '<table cellpadding="5" cellspacing="0" border="0">'+
            '<tr><td><textarea class="ui-widget" style="width:100%; height:100px;">'+d.LOG_TEXT+'</textarea></td>'+
        '</tr>'+
    '</table>';
}

function search(){
	$('input[type="search"]').val('').keyup();
	table.ajax.reload();
}

function selectChainCode(){
	var params = {};
	params['cdTpTp'] = 'CHAIN_CODE';
	$.ajax({
		type : 'POST',
        url:'commonCodeList',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(data){
        	var obj = $('#sel_chain_code');
    		drawSelect(obj, data, 'commonCodeAll');
        },
        error : function(e){ 
        }
     });
}

function selectLogLevel(){
	var params = {};
	params['cdTpTp'] = 'LOG_LEVEL';
	$.ajax({
		type : 'POST',
        url:'commonCodeList',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(data){
        	var obj = $('#sel_log_level');
    		drawSelect(obj, data, 'commonCodeAll');
        },
        error : function(e){ 
        }
     });
}
