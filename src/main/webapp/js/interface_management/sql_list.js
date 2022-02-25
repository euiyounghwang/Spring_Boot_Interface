/**
 * sql 관리
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
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+');">';
	            }
			},
			{data : 'DESCRIPTION1'},
			{
				data : 'INFO_TITLE',
				render: function (data, type, full, meta) {
					return '<a href="#" onclick="loadCenterPageAjax(&quot;interface_management/sql_upsert&quot;, &quot;Y&quot;, &quot;'+meta.row+'&quot;);">'+data+'</a>';
				}
			},
			{data : 'DESCRIPTION'},
			{
				data : 'OBJECT_ID',
				visible : false
			},
			
		],
		searching:true,
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		  },
		info:false,
		columnDefs:[
			{targets:[1,2], orderable:true},
			{targets:[0,3], orderable:false},
			{targets:[0], className:'dt-body-center'},
		],
		order:[1,'asc'],
		"ajax": {
			type : 'POST',
            url: "selectSqlList",
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
//            	d['order'][0].column = 1;
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
            hideProgress();
        },
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "전체"]],
//        lengthChange: false,
        dom: 'Blftip',
        buttons: []
    });
	
	$('#btn_refresh').click(function(){
		refreshMenu();
	});
	
	$('#btn_delete').click(function(){
		if(checkedIndices.length>0){
			if(confirm('선택한 SQL 자원을 삭제하시겠습니까?')){
				deleteSQL();
			}
		} else {
			alert('삭제할 SQL 자원을 선택하세요.');
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

function deleteSQL(){
	var params ={};
	params['OBJECT_ID'] = '';
	
	for(var i=0; i <checkedIndices.length; i++){
		params['OBJECT_ID'] += "'"+table.rows().data()[checkedIndices[i]]['OBJECT_ID'] + "',";
	}
	params['OBJECT_ID'] = params['OBJECT_ID'].substr(0, params['OBJECT_ID'].length-1);
	$.ajax({
		type : 'POST',
        url:'deleteSql',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('SQL 자원 삭제를 완료했습니다.');
        	refreshMenu();
        },
        error : function(e){ 
        	alert('SQL 자원 삭제 중 에러가 발생했습니다.');
        }
     });
}

function refreshMenu(){
	$('input[type="search"]').val('').keyup();
	$('#check-all-menu').prop('checked', false);
	table.ajax.reload();
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