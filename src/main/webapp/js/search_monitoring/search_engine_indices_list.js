/**
 * 인덱스정보 조회
 */

var table;
var excel = false;

$( document ).ready(function() {
	search_engine_status('isstarted');
	make_indices_table();

	$(document.getElementById("butt_start")).trigger('click');
});

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function make_indices_table()
{
	table = $('#make_table').DataTable( {
		 ordering: false,
		"serverSide": true,
		"paging": true,
		"bDestroy": true,
		"autoWidth": false, //자동 열 너비 계산을 사용 또는 사용하지 않도록 설정한다.(true일 경우 td에 width값이 px단위로 고정되어 window resize 이벤트에 대응 안됨)
        pagingType: "full_numbers_no_ellipses",
        columns : [
        	{
				data : 'INDICES',
				render: function (data, type, full, meta) {
					return '<a href="#"><b>' + '<p align="left">' + data + '</p></b></a>';
				}
			},
        	{
				data : 'STATUS',
				render: function (data, type, full, meta) {
					if(data == 'red') {
						return '<img src="img/health-red.svg" />';
					}
					else if(data == 'yellow') {
						return '<img src="img/health-yellow.svg" />';
					}
					else if(data == 'green') {
						return '<img src="img/health-green.svg" />';
					}
				}
			},
        	{data : 'PR_RE_SET'},
        	{
				data : 'TOTAL_COUNT',
				render: function (data, type, full, meta) {
					return '<p align="center">' + numberWithCommas(data) + '</p>';
				}
			},
        	{data : 'UID'},
        	{
				data : 'DELETED',
				render: function (data, type, full, meta) {
					return '<p align="center">' + numberWithCommas(data) + '</p>';
				}
			},
        	{data : 'STORED'}
		],
		searching:true, 
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		  info:true, // 테이블의 정보 표기여부
			columnDefs:[ // 컬럼들의 속성 지정. 컬럼들의 index로 정의
				{targets:[0,1,2,4,6], orderable:false}, //orderable : 해당 컬럼으로 sort 할지. 쿼리에서 해당 값으로 order by 재정의 필요.
				{targets:[0,1,2,3,4,5,6], className:'dt-body-center'}, //className : 해당 컬럼들의 class 지정
			],
//			order:[3,'asc'], //default sort 값. 쿼리에서 해당 값으로 order by 정의 필요.
			"ajax": {
			type : 'POST',
            url: "getSearchEngineIndicesList",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
//            	showProgress();
            },
            data: function ( d ) {
            	if(excel){
            		d['start']  = 0;
            		d['length'] = table.settings()[0]._iRecordsDisplay;
            	} else {
            		if(d['draw']!=1)
            			d['start'] = table.settings()[0]._iDisplayStart;
            	}
            	d['version_params'] = $("#version_params").val();
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
        	hideProgress();
        },
        lengthMenu: [[10, 50, 100], [10, 50, 100]],
//        dom: '<"top"li>t<"bottom"p><"clear">',
        dom: 'Blft', // dataTables 옵션(결과 몇개보기, 검색, 엑셀, 프로세싱 등.. ) 사용시 해당 객체(dom)를 이 옵션에 표기해줘야 화면에 보여짐
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
	
	table.on( 'page.dt', function () {
		$('#center').animate({scrollTop: 0}, 100);
	} );
	
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function search_engine_status(start){
	var params = {};
	params['version_params'] = $("#version_params").val();
//	alert(params['version_params']);
//	alert($("#version_params").val());
	$.ajax({
		type : 'POST',
        url:'getSearchEngineStatus',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        beforeSend: function(){
//       	 $('.selected_info').text('');
//       	 $('.rd_xmlviewer XMP').text('');
//       	 	showProgress();
        	if (start == 'isstarted') {
        		$("#LoadingImage").show();
        	}
        	else {
        		$("#loading").html("<img src='img/loading_small.gif' width='30' height='30' />");
        	}
        },
        success:function(data){
//        	alert(data.data);
           $("#short_info").show();
           $("#content").show();
           var now = new Date();

           var year= now.getFullYear();
           var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
           var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
                    
           var chan_val = year + '-' + mon + '-' + day;
           
           $("#current_time").html(now);

        	$.each(data,function(key,value) {
        		    var active_primary_shards = '';
        			if(key == 'number_of_nodes') {
        				$("#number_of_nodes").html(value);
        			}
        			else if(key == 'active_shards') {
        				$("#number_of_total_shards").html(numberWithCommas(parseInt(value)));
					}
        			else if(key == 'count') {
        				$("#number_of_indices").html(numberWithCommas(value));
        			}
        			else if(key == 'count') {
        				$("#number_of_indices").html(numberWithCommas(value));
        			}
        			else if(key == 'status') {
    					if(value == 'red') {
    						$("#cluster_health_alert").html("<img src='img/health-red.svg' />&nbsp;&nbsp;");
    					}
    					else if(value == 'yellow') {
    						$("#cluster_health_alert").html("<img src='img/health-yellow.svg' />&nbsp;&nbsp;");
    					}
    					else if(value == 'green') {
    						$("#cluster_health_alert").html("<img src='img/health-green.svg' />&nbsp;&nbsp;");
    					}
    					
    					$("#status").html(value);
					}
        			else if(key == 'jvm') {
        				var obj = jQuery.parseJSON(value);
        				var heap_used = parseInt(obj.mem.heap_used_in_bytes/(1024*1024*1024));
        				var heap_max = parseInt(obj.mem.heap_max_in_bytes/(1024*1024*1024));
        				$("#elastic_uptime").html(obj.max_uptime);
        				$("#heap_percent").html(((heap_used/heap_max)*100).toFixed(2) + "%");
        				$("#heap_used_in_bytes").html(heap_used + "GB");
        				$("#heap_max_in_bytes").html(heap_max + "GB");
        			}
        			else if(key == 'unassigned_shards') {
        				$("#unassigned_alert_message").html(value);
					}
        			else if(key == 'docs') {
        				var obj = jQuery.parseJSON(value);
        				$("#number_of_doucuments").html(numberWithCommas(obj.count));
        			}
        			else if(key == 'fs') {
        				var obj = jQuery.parseJSON(value);
        				var available_bytes = parseInt(obj.available_in_bytes/(1024*1024*1024));
        				var total_fs_bytes = parseInt(obj.total_in_bytes/(1024*1024*1024));
        				$("#total_fs").html(obj.total.toUpperCase());
        				$("#available_fs").html(obj.available.toUpperCase());
        				$("#fs_percent").html(((available_bytes/total_fs_bytes)*100).toFixed(2) + "%");
        			}
        			else if(key == 'store') {
        				var obj = jQuery.parseJSON(value);
        				$("#number_of_disk").html(numberWithCommas(obj.size.toUpperCase()));
     				}
        			else if(key == 'versions') {
        				var obj = jQuery.parseJSON(value);
        				$("#elastic_version").html(obj);
        			}
        	});
        },
        error : function(e){ 
        },
        complete : function() {
//        	hideProgress();   
        	$("#loading").empty();
        	$("#LoadingImage").hide();

        }
     });
}



