/**
 * 메뉴코드 메뉴
 */
//var checkedValue = {}; //[page][rownum]
var table;
var checkedIndices =[];
var excel = false;

$( document ).ready(function() {
	/*
	 * DataTables 옵션 정의
	 * 관련 url : https://datatables.net/reference/
	 * 
	 * */
	
	table = $('#center .grid_table').DataTable( {
//        "processing": true, //Ajax 서버 통신중일때 "processing... 이라는 layer 생성 여부 "
        "serverSide": true, // Ajax 서버통신을 통해서 데이터를 가져올지 여부
        "paging": true, // 페이징 기능 사용여부
        columns : [ //jsp에 정의한 <table> 에 들어갈 필드들. <th>로 jsp에 정의필수. 매칭 필수.
        	{
	        	data : '', //여기에 보여주려는 데이터 Key값을 넣으면 해당필드에 해당값이 매핑된다.
	            render: function (data, type, full, meta) { //기본적인 renderring 이외에 customizing이 필요한경우 재정의 가능하다. 
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+')">';
	            }
			},
			{data : 'COMPANY_CODE'},
			{data : 'CD_TP'},
			{
				data : 'CD_TP1',
				render: function (data, type, full, meta) {
					return '<a href="#" title="수정" onclick="setInputbox(&quot;'+meta.row+'&quot;); return false;">'+data+'</a>';
				}
			},
			{data : 'CD_TP_MEANING'},
			{data : 'CD_V_EXPLAIN'},
			{data : 'FK_CD_TP'},
			{data : 'SEARCH_SORT_SEQ'},
			{data : 'DEPTH'},
			{data : 'START_ACTIVE_DATE'},
			{data : 'END_DATE_ACTIVE'},
			{data : 'ACTIVE_FLAG'},
			{data : 'ATTRIBUTE1'},
			{data : 'ATTRIBUTE2'},
			{data : 'ATTRIBUTE3'},
			{data : 'ATTRIBUTE4'},
			{data : 'ATTRIBUTE5'},
			{data : 'CREATION_TIMESTAMP'},
			{data : 'LAST_UPDATE_TIMESTAMP'}
		],
		searching:true, //검색 기능 사용여부. 쿼리에서 해당 값으로 where 정의 해줘야함.
		language: { //default display 언어를 변경하는 기능
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		  },
		info:false, // 테이블의 정보 표기여부
		columnDefs:[ // 컬럼들의 속성 지정. 컬럼들의 index로 정의
			{targets:[0,1,2,4,5,6,12,13,14,15,16], orderable:false}, //orderable : 해당 컬럼으로 sort 할지. 쿼리에서 해당 값으로 order by 재정의 필요.
			{targets:[0,1,2,3,6,7,8,9,10,11,13,14,15,16, 17,18], className:'dt-body-center'}, //className : 해당 컬럼들의 class 지정
		],
		order:[7,'asc'], //default sort 값. 쿼리에서 해당 값으로 order by 정의 필요.
		"ajax": { //data를 가져올 ajax 통신
			type : 'POST',
            url: "selectMenuProperties",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) { //통신 전 보낼 data 재정의. d: dataTables 기본 데이터 format.
            	$('#check-all-menu').prop('checked', false);
            	checkedIndices = [];
            	deleteBtnStatus();
            	if(excel){ //excel 값을 가져오는 통신인지 확인
            		d['start']  = 0;
            		d['length'] = table.settings()[0]._iRecordsDisplay; //_iRecordsDisplay : 화면에 보여지는 데이터 length
            	} else {
            		if(d['draw']!=1)
            			d['start'] = table.settings()[0]._iDisplayStart; //_iDisplayStart : 화면에 보여지는 데이터 순번 (paging)
            	}
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) { // 테이블 그리기가 끝난 후 호출되는 함수.
            hideProgress();
        },
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "전체"]], //결과 몇개씩 보기 옵션 설정 [[서버전송 length 값 ],[화면에 보여지는 값]]
        dom: 'Blftip', // dataTables 옵션(결과 몇개보기, 검색, 엑셀, 프로세싱 등.. ) 사용시 해당 객체(dom)를 이 옵션에 표기해줘야 화면에 보여짐
        // 값 : 지정 옵션값 => 의미
        //////////////////////////////////////////////
        // B : Button => 엑셀 (다른 파일도 export가능)
        // l : lengthMenu => 결과 몇개씩 보기 
        // f : searching => 검색
        // t : table => 테이블 (옵션 없이 테이블만 사용시에는 dom 생략가능)
        // i : information => 전체 데이터 갯수 or 현재 몇번째 데이터 
        // p : processing =>  processing... 레이어  
        // 참고 : 지정을 했는대도 화면에 안보여질땐 각 객체의 위치를 강제로 지정할수도 있다. ex) dom: '<"top"li>t<"bottom"p><"clear">'
        // 관련 url : https://datatables.net/reference/option/dom
        buttons: [ //해당 테이블을 데이터를 파일로 다운하기 위한 버튼옵션
            {
                extend: 'excel', //파일 형식
                text: 'Export Excel', //버튼 text
                title: 'MenuCode', //파일명
                className:'btn2', //버튼 class
                action:  function (e, dt, button, config) { //버튼 눌렀을떄 action (전체데이터 들고와서 excel 생성 후 기존 조회 조건으로 다시 셋팅)
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
			if(confirm('선택한 메뉴를 삭제하시겠습니까?')){
				deleteMenu();
			}
		} else {
			alert('삭제할 메뉴를 선택하세요.');
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
	params['cd_tp1'] = '';
	
	for(var i=0; i <checkedIndices.length; i++){
		params['cd_tp1'] += "'"+table.rows().data()[checkedIndices[i]]['CD_TP1'] + "',";
	}
	params['cd_tp1'] = params['cd_tp1'].substr(0, params['cd_tp1'].length-1);
	$.ajax({
		type : 'POST',
        url:'deleteMenu',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('메뉴 삭제를 완료했습니다.');
        	refreshMenu();
        },
        error : function(e){ 
        	alert('메뉴 삭제 중 에러가 발생했습니다.');
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
	
	if(params['cd_tp1']=='' 
		|| params['company_code']==''
			|| params['search_sort_seq']==''
				|| params['depth']==''
					|| params['active_flag']==''
						|| params['attribute1']==''
							|| params['attribute2']==''){
		alert('필수항목을 모두 입력해주세요.');
		return false;
	} else if((params['active_flag']!='Y' && params['active_flag']!='N') || (params['attribute2']!='Y' && params['attribute2']!='N')){
		alert('ACTIVE_FLAG와 ATTRIBUTE2 값은 Y 혹은 N 으로 입력해주세요.');
		return false;
	} else if(params['cd_tp1'].substr(0,2)!='EM' || isNaN(params['cd_tp1'].substr(2,params['cd_tp1'].length-2))
		//	|| params['fk_cd_tp'].substr(0,2)!='EM' 
			//	|| isNaN(params['fk_cd_tp'].substr(2,params['fk_cd_tp'].length-2))
				){
		alert('CD_TP1 의 형식이 잘못되었습니다. (ex:EM숫자)');
		return false;
	} else if(!(params['depth']==0 || params['depth']==1 || params['depth']==2)){
		alert('DEPTH 값을 올바르게 입력하세요.');
		return false;
	} else if(isNaN(params['search_sort_seq'])){
		alert('SEARCH_SORT_SEQ는 숫자만 입력할 수 있습니다.');
		return false;
	} else {
		return true;
	}
}

function selectMenuDuplication(){
	var params = $('.write_box :input').serializeObject()
	
	$.ajax({
		type : 'POST',
        url:'selectMenuDuplication',
//        async: false,
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	if(result.length>0){
        		return true;
        	}else {
        		return false;
        	}
        },
        error : function(e){ 
        	alert('메뉴 중복 확인 중 에러가 발생했습니다.');
        }
     });
}


function insertMenu(){
	var url = 'insertMenu';
	var string = '메뉴를 등록 하시겠습니까?';
	
	if($('#cd_tp1').prop('readonly')){
		url = 'updateMenu';
		string = '메뉴를 수정 하시겠습니까?';
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
	        	alert('메뉴 추가를 실패했습니다.');
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
			if(id=='company_code'){
				$('#old_company_code').val(row[key]); //변경전 회사코드 set to hidden
			}
		}
	});
	$('#cd_tp1').prop('readonly', true);
	openInsertDiv();
}

function closeInsertDiv(){
	$('#cd_tp1').prop('readonly', false);
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