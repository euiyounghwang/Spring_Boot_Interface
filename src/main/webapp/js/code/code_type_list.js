/**
 * 코드유형 정보관리
 */

var table;
var checkedIndices =[];
var excel = false;

$( document ).ready(function() {	
	table = $('#center .grid_table').DataTable( {
//        "processing": true,
        "serverSide": true,
        "paging": true,
        columns : [
        	{
	        	data : '',
	            render: function (data, type, full, meta) {
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+')">';
	            }
			},
			{data : 'COMPANY_CODE'},
			{
				data : 'CD_TP',
				render: function (data, type, full, meta) {
					return '<a href="#" title="수정" onclick="setInputbox(&quot;'+meta.row+'&quot;); return false;">'+data+'</a>';
				}
			},
			{data : 'CD_TP_MEANING'},
			{data : 'CD_V_EXPLAIN'},
			{data : 'FK_CD_TP'},
			{data : 'SEARCH_SORT_SEQ'},
			{data : 'DEPTH'},
			{data : 'ACTIVE_FLAG'},
			{
	        	data : 'CD_TP',
	            render: function (data, type, full, meta) {
		                return '<button class="btn1" onclick="loadCenterPageAjax(&quot;code/code_type_list_detail&quot;, &quot;Y&quot;, &quot;'+meta.row+'&quot;);"><span>상세</span></button>';
		                
	            }
			},
			{data : 'ATTRIBUTE1'},
			{data : 'ATTRIBUTE2'},
			{data : 'ATTRIBUTE3'},
			{data : 'ATTRIBUTE4'},
			{data : 'ATTRIBUTE5'},
			{data : 'CREATION_TIMESTAMP'},
			{data : 'LAST_UPDATE_TIMESTAMP'}
		],
		searching:true, //CD_TP_MEANING, CD_V_EXPLAIN, ATTRIBUTE1 LIKE 검색
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		  },
		info:false,
		columnDefs:[
			{targets:[0,1,3,4,5,9,10,11,12,13], orderable:false},
			{targets:[0,1,2,5,6,7,8,9,10,11,13,14,15], className:'dt-body-center'},
		],
		order:[2,'asc'],
		"ajax": {
			type : 'POST',
            url: "selectCodeTypeInfo",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) {
            	$('#check-all-menu').prop('checked', false);
            	checkedIndices = [];
            	deleteBtnStatus();
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
                title: 'CodeTypeInfo',
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
			if(confirm('선택한 코드유형 정보를 삭제하시겠습니까?')){
				deleteMenu();
			}
		} else {
			alert('삭제할 코드유형 정보를 선택하세요.');
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

function deleteMenu(){
	var params ={};
	params['cd_tp'] = '';
	
	for(var i=0; i <checkedIndices.length; i++){
		params['cd_tp'] += "'"+table.rows().data()[checkedIndices[i]]['CD_TP'] + "',";
	}
	params['cd_tp'] = params['cd_tp'].substr(0, params['cd_tp'].length-1);
	$.ajax({
		type : 'POST',
        url:'deleteCodeTypeInfo',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('코드유형 정보 삭제를 완료했습니다.');
        	refreshMenu();
        },
        error : function(e){ 
        	alert('코드유형 정보 삭제 중 에러가 발생했습니다.');
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
	
	if(params['cd_tp']=='' 
		|| params['company_code']==''
				|| params['depth']==''
					|| params['active_flag']==''
						|| params['cd_tp_meaning']==''){
		alert('필수항목을 모두 입력해주세요.');
		return false;
	} else if(params['active_flag']!='Y' && params['active_flag']!='N'){
		alert('ACTIVE_FLAG 값은 Y 혹은 N 으로 입력해주세요.');
		return false;
	} else if(isNaN(params['depth'])){
		alert('DEPTH 값을 올바르게 입력하세요.');
		return false;
	} else if(isNaN(params['search_sort_seq'])){
		alert('SEARCH_SORT_SEQ는 숫자만 입력할 수 있습니다.');
		return false;
	} else {
		return true;
	}
}

function insertMenu(){
	var url = 'insertCodeTypeInfo';
	var string = '코드유형 정보를 등록 하시겠습니까?';
	
	if($('#cd_tp').prop('readonly')){
		url = 'updateCodeTypeInfo';
		string = '코드유형 정보를 수정 하시겠습니까?';
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
	        	if(result.value=="true"){
		        	alert(result.msg);
		        	closeInsertDiv();
		        	refreshMenu();
	        	} else {
	        		alert(result.msg);
	        	}
	        },
	        error : function(e){ 
	        	alert('코드유형 정보 추가를 실패했습니다.');
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
	$('#company_code').prop('readonly', true);
	$('#cd_tp').prop('readonly', true);
	openInsertDiv();
}

function closeInsertDiv(){
	$('#company_code').prop('readonly', false);
	$('#cd_tp').prop('readonly', false);
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