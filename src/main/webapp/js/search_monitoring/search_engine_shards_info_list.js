/**
 * was 서버 로그 조회
 */

var table;
var excel = false;
var timer=null;
$( document ).ready(function() {
	search_engine_status('isstarted');
	show_table_make();
	clearInterval(timer);
//	show_table();
//	setTimeout("location.reload(false)", 10000);
	timer = setInterval(function() 
	{ 
		search_engine_status(''); 
		show_table_make();
	}, 20000);
	
	$(window).unload(function() {
		clearInterval(timer);
    });
   
	$(window).on('beforeunload',function() {
		clearInterval(timer);
    });
});

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function show_table_make() {
	var params = {};
	params['version_params'] = $('#version_params').val();
	$.ajax({
		type : 'POST',
    url:'getSearchEngineNodeList',
    data : JSON.stringify(params),
    dataType:'json',
    contentType: 'application/json',
    beforeSend: function(){
    	$("#loading").show();
        },
    success:function(data){
    	$("#content").show();
    	$('#engine_node_make_table').empty();
//    	$('#engine_node_table').html('<tr><td>' + data + '</td></tr>');
    	$.each(data,function(key,value) {
    			 if (key == 'data') {
    				  $.each(value,function(keys,values) {
    					  var node_info = JSON.stringify(values.NODE).split(',')
    						$('#engine_node_make_table').append([
    							'<tbody>',
    						  '<tr class="odd" height="80px">',
    						      '<td style="width: 20px;" align="right"><font size="4"><b>' + JSON.stringify(values.MASTER).replace('"','').replace('"','') + '</b></font></td>',
    						      '<td style="width: 190px;" align="center">' + "<a href='#'><font size='4' color='#009DDF'><b>#&nbsp;" + node_info[0].replace('"','') + "</b></font></a><br /><font size='2'><b>[" + JSON.stringify(values.ROLES).replace('"','').replace('"','') + "]</b></font><br />" + node_info[1].replace('"','') + '</td>',
    						      '<td style="width: 170px;" align="center"><img src="img/health-green.svg" />&nbsp;Online</td>',
    						      '<td style="width: 190px;" align="center"><b>' + JSON.stringify(values.CPU).replace('"','').replace('"','') + '&nbsp;' + JSON.stringify(values.CPU_STATUS).replace('"','').replace('"','') + '</b></td>',
    						      '<td style="width: 190px;" align="center">' + JSON.stringify(values.LOAD).replace('"','').replace('"','') + '</td>',
    						      '<td style="width: 190px;" align="center">' + JSON.stringify(values.JVM).replace('"','').replace('"','') + '</td>',
    						      '<td style="width: 190px;" align="center">' + JSON.stringify(values.FS).replace('"','').replace('"','') + '</td>',
    						      '<td style="width: 170px;" align="center">' + numberWithCommas(JSON.stringify(values.INDICES).replace('"','').replace('"','')) + '</td>',
    						  '</tr>',
    						  '</tbody>',
    						  ].join(''));
    				    });
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


function show_table() {
//	$('#center .grid_table').dataTable().fnDestroy();
		table = $('#engine_node_table').DataTable( {
		ordering: false,
        "serverSide": true,
        "paging": true,
        "autoWidth": false, //자동 열 너비 계산을 사용 또는 사용하지 않도록 설정한다.(true일 경우 td에 width값이 px단위로 고정되어 window resize 이벤트에 대응 안됨)
        "bDestroy": true,
        pagingType: "full_numbers_no_ellipses",
        columns : [
			{
				data : 'NODE',
				render: function (data, type, full, meta) {
					var node_info = data.split(',')
					return "<a href='#'><font size='4'><b>#&nbsp;" + node_info[0] + "</b></font></a><br />" + node_info[1];

				}
			},
//			{
//				data : 'STATUS',
//				render: function (data, type, full, meta) {
//					if (data == 'green') {
//						return "<img src='img/health-green.svg' />";						
//					}
//					else if (data == 'yellow') {
//						return "<img src='img/health-yellow.svg' />";	
//					}
//					else if (data == 'red') {
//						return "<img src='img/health-red.svg' />";	
//					}
//
//				}
//			},
			{data : 'CPU'},
			{data : 'CPU'}
//			{data : 'LOAD'},
//			{data : 'JVM'},
//			{data : 'DISK'},
//			{data : 'SHARDS'}
		],
		searching:true, 
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		  info:true, // 테이블의 정보 표기여부
//			columnDefs:[ // 컬럼들의 속성 지정. 컬럼들의 index로 정의
//				{targets:[0,1,2,4,6,7,8,9], orderable:false}, //orderable : 해당 컬럼으로 sort 할지. 쿼리에서 해당 값으로 order by 재정의 필요.
//				{targets:[0,1,2,3,4,5,6,7,8,9], className:'dt-body-center'}, //className : 해당 컬럼들의 class 지정
//			],
//			order:[3,'asc'], //default sort 값. 쿼리에서 해당 값으로 order by 정의 필요.
			"ajax": {
			type : 'POST',
            url: "getSearchEngineNodeList",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
//            	showProgress();
//            	$("#LoadingImage").show();
            },
            data: function ( d ) {
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
        	hideProgress();
        }
    });
	
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



