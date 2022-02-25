/**
 * 메뉴코드 메뉴
 */
var table;
var checkedIndices =[];
var excel = false;

$( document ).ready(function() {
	selectSearchCode('ES_SYSTEM','sel_system_id');
	selectSearchCode('ES_AUTH','sel_auth_id');
	
	table = $('#center .grid_table').DataTable( {
        "serverSide": true,
        "paging": true,
        columns : [
        	{
	        	data : '',
	            render: function (data, type, full, meta) {
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+')">';
	            }
			},
			{data : 'SYSTEM_ID'},
			{data : 'SYSTEM_NAME'},
			{data : 'AUTH_ID'},
			{data : 'AUTH_NAME'},
			{data : 'COMPANY_CODE'},
			{data : 'COMPANY_NAME'},
			{data : 'GROUP_ID'},
			{data : 'GROUP_NAME'},
			{data : 'FULL_NAME'},
			{
				data : 'USER_EMP_NO',
				render: function (data, type, full, meta) {
					return '<a href="#" title="수정" onclick="setInputbox(&quot;'+meta.row+'&quot;); return false;">'+data+'</a>';
				}
			},
			{data : 'USER_LOGIN_ID'},
			{data : 'EMAIL_ADDRESS'},
			{data : 'META_YN'},
			{data : 'USER_AUTH_EXPLAIN'},
			{data : 'ATTRIBUTE1'},
			{data : 'CREATION_TIMESTAMP'},
			{data : 'LAST_UPDATE_TIMESTAMP'}
		],
		searching:true, //FULL_NAME, USER_EMP_NO, USER_LOGIN_ID LIKE 검색
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		  },
		info:false,
		columnDefs:[
			{targets:[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17], orderable:false},
			{targets:[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17], className:'dt-body-center'},
		],
		"rowCallback": function( row, data, index ) {
		    if ( data["ATTRIBUTE1"] == "N" ){
		        $('td', row).css('background-color', '#eaeaea');
		    }
		},
		order:[],
		"ajax": {
			type : 'POST',
            url: "selectESAuthDB",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) {
            	$('#check-all-menu').prop('checked', false);
            	checkedIndices = [];
            	deleteBtnStatus();

            	if($('#sel_system_id').val()!='' && $('#sel_system_id option:selected').text()!='선택') d['P_SYSTEM_ID'] = $('#sel_system_id option:selected').val();
            	if($('#sel_auth_id').val()!='' && $('#sel_auth_id option:selected').text()!='선택') d['P_AUTH_ID'] = $('#sel_auth_id option:selected').val();
            	if($('#txt_full_name').val()!='') d['P_FULL_NAME'] = $('#txt_full_name').val();
            	
            	if(excel){
            		d['start']  = 0;
            		d['length'] = table.settings()[0]._iRecordsDisplay;
            	} else {
            		if(d['draw']!=1)
            			d['start'] = table.settings()[0]._iDisplayStart;
            	}
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
            hideProgress();
        },
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "전체"]],
//        lengthChange: false,
        dom: 'Blftip',
        buttons: [
            {
                extend: 'excel',
                text: 'Export Excel',
                title: 'ESAuthDBList',
                className:'btn2',
                action:  function (e, dt, button, config) {
                    var self = this;
                    var oldStart = table.settings()[0]._iDisplayStart;
                    excel = true;
                    table.one('preXhr', function (e, s, data) {
                        // Just this once, load all data from the server...
                        data.start = 0;
                        data.length = 2147483647;
                        excel = false;
                        table.one('preDraw', function (e, settings) {
                            // Call the original action function 
//                            oldExportAction(self, e, dt, button, config);
//                            function (self, e, dt, button, config) {
                            	if (button[0].className.indexOf('buttons-excel') >= 0 || button[0].className.indexOf('buttons-csv') >= 0) {
                                    if ($.fn.dataTable.ext.buttons.excelHtml5.available(table, config)) {
                                        $.fn.dataTable.ext.buttons.excelHtml5.action.call(self, e, table, button, config);
                                    }
                                    else {
                                        $.fn.dataTable.ext.buttons.excelFlash.action.call(self, e, table, button, config);
                                    }
                                } else if (button[0].className.indexOf('buttons-print') >= 0) {
                                    $.fn.dataTable.ext.buttons.print.action(e, table, button, config);
                                }
//                            };

                            	table.one('preXhr', function (e, s, data) {
                                // DataTables thinks the first item displayed is index 0, but we're not drawing that.
                                // Set the property to what it was before exporting.
                                settings._iDisplayStart = oldStart;
                                data.start = oldStart;
                            });
                            // Reload the grid with the original page. Otherwise, API functions like table.cell(this) don't work properly.
                            setTimeout(table.ajax.reload, 0);

                            // Prevent rendering of the full data to the DOM
                            return false;
                        });
                    });
                    // Requery the server with the new one-time export settings
                    table.ajax.reload();
                }
            }
        ]
    });
	
	
	
	$('#btn_refresh').click(function(){
		refreshMenu();
	});
	
	$('#btn_delete').click(function(){
		if(checkedIndices.length>0){
			if(confirm('선택한 ES권한을 사용정지 처리 하시겠습니까?')){
				deleteMenu();
			}
		} else {
			alert('ES권한 사용정지 처리 할 코드를 선택하세요.');
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



function selectSearchCode(cdTpTp, selectID){
	var params = {};
	params['cdTpTp'] = cdTpTp;
	$.ajax({
		type : 'POST',
        url:'selectCommonCode',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(data){
        	var obj = $('#'+selectID);
    		drawSelect(obj, data, 'commonCodeAll');
        },
        error : function(e){ 
        }
     });
}

function deleteMenu(){
	var params ={};
	params['system_id'] = '';
	params['auth_id'] = '';
	params['company_code'] = '';
	params['group_id'] = '';
	params['user_emp_no'] = '';
	
	for(var i=0; i <checkedIndices.length; i++){
		params['system_id'] += "'"+table.rows().data()[checkedIndices[i]]['SYSTEM_ID'] + "',";
		params['auth_id'] += "'"+table.rows().data()[checkedIndices[i]]['AUTH_ID'] + "',";
		params['company_code'] += "'"+table.rows().data()[checkedIndices[i]]['COMPANY_CODE'] + "',";
		params['group_id'] += "'"+table.rows().data()[checkedIndices[i]]['GROUP_ID'] + "',";
		params['user_emp_no'] += "'"+table.rows().data()[checkedIndices[i]]['USER_EMP_NO'] + "',";
	}
	params['system_id'] = params['system_id'].substr(0, params['system_id'].length-1);
	params['auth_id'] = params['auth_id'].substr(0, params['auth_id'].length-1);
	params['company_code'] = params['company_code'].substr(0, params['company_code'].length-1);
	params['group_id'] = params['group_id'].substr(0, params['group_id'].length-1);
	params['user_emp_no'] = params['user_emp_no'].substr(0, params['user_emp_no'].length-1);
	$.ajax({
		type : 'POST',
        url:'disabledESAuthDB',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('ES권한 사용정지 처리를 완료했습니다.');
        	refreshMenu();
        },
        error : function(e){ 
        	alert('ES권한 사용정지 처리 중 에러가 발생했습니다.');
        }
     });
}

function refreshMenu(){
	$('input[type="search"]').val('').keyup();
	$('#check-all-menu').prop('checked', false);
	table.ajax.reload();
}

function insertMenuValidation(){
	var params = $('.write_box :input').serializeObject();
	Object.keys(params).forEach(function(key) {
		if(params[key]!='' && params[key]!=undefined && params[key]!=null){
			var id = key.toLowerCase();
			$('#'+id).val(params[key].trim());
		}
	});
	params = $('.write_box :input').serializeObject();
	
	if(params['system_id']=='' 
		|| params['auth_id']==''
			|| params['company_code']==''
				|| params['group_id']==''
					|| params['user_emp_no']==''){
		alert('필수항목을 모두 입력해주세요.');
		return false;
	} else {
		return true;
	}
}


function insertMenu(){
	var url = 'mergeESAuthDB';
	
	if($('#user_emp_no').prop('readonly')){
		string = 'ES통합권한을 수정 하시겠습니까?';
	}else{
		var string = 'ES통합권한을 등록 하시겠습니까?';
	}
	
	
	var validate = insertMenuValidation();
	
	if(validate){
		if(confirm(string)){
		var params = $('.write_box :input').serializeObject();
		$.ajax({
			type : 'POST',
	        url: url,
	        data : JSON.stringify(params).replace(/"\s+|\s+"/g,'"'),
	        contentType: 'application/json',
	        success:function(result){
		        	closeInsertDiv();
		        	refreshMenu();
	        },
	        error : function(e){ 
	        	alert('[ERROR]통합권한 수정/등록에 실패하였습니다.');
	        }
	     });
		}
	}
	
}


function setInputbox(index){
	var row = table.rows().data()[index];
	$('.write_box :input').val(''); 
	
	Object.keys(row).forEach(function(key) {
		if(row[key]!=='' && row[key]!==undefined && row[key]!==null){
			var id = key.toLowerCase();
			$('#'+id).val(row[key]);
		}
	});
	$('#system_id').prop('readonly', true);
	$('#auth_id').prop('readonly', true);
	$('#company_code').prop('readonly', true);
	$('#group_id').prop('readonly', true);
	$('#user_emp_no').prop('readonly', true);
	openInsertDiv();
}

function closeInsertDiv(){
	$('#system_id').prop('readonly', false);
	$('#auth_id').prop('readonly', false);
	$('#company_code').prop('readonly', false);
	$('#group_id').prop('readonly', false);
	$('#user_emp_no').prop('readonly', false);
	$('.write_box :input').val(''); 
	$('#data_insert').hide();
}

function openInsertDiv(){
	$('#data_insert').show();
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