/**
 * 메뉴xml - Tree menu 선택 시 
 */

var companyNames = {
		'00' : '공통',
		'30' : '포스코',
		'01' : 'POSCO ICT',
		'10' : 'PNR',
		'14' : '포스코강판',
		'37' : 'RIST', 
		'13' : '포스코엠텍',
		'02' : '포스코켐텍',
		'19' : '포스코기술투자',
		'31' : '엔투비',
		'07' : 'SNNC',
		'04' : '포스하이메탈',
		'18' : '포스코터미날',
		'22' : '포스코A&C',
		'26' : '포스코경영연구원',
		'27' : '포스코에너지',
		'12' : '포스코플랜텍',
		'41_daewoo' : '포스코대우',
		'41_tmc' : '포스코대우',
		'41_ast' : '포스코대우',
		'41_spfc' : '포스코대우',
		'17' : '포스코건설',
		'38' : '포스코교육재단',
		'60' : '포스코인재창조원',
		'67' : '한국퓨얼셀',
		'68' : '포스코SPS'
}

function makeCompanyNames(comCodes){
	if(comCodes=='-')
		return comCodes;
	
	var comArray = comCodes.split(',');
	var result = '';
	for(var i=0; i<comArray.length; i++){
		var com = comArray[i];
		com+='(' +companyNames[com]+ '), ';
		result += com;
	}
	return result.substr(0, result.length-2);
}


$( document ).ready(function() {
});

function selectCommonMetas(){
	$.ajax({
		type : 'POST',
        url:'selectMenuMetas',
        dataType:'json',
        success:function(data){
        	var obj = $('#sel_metas');
        	drawSelect(obj, data, 'meta');
       },
       error : function(e){ 
       }
    });
}

function selectCommonCompanys(type){
	$.ajax({
		type : 'POST',
        url:'selectCompanys',
        dataType:'json',
        success:function(data){
        	if(type == 'select'){
	        	var obj = $('#sel_company');
	        	drawSelect(obj, data, 'company');
        	} else if(type == 'tree'){
        		drawTreeLevel1(data);
        	}
       },
       error : function(e){ 
       }
    });
}

function drawTreeLevel1(items){
	$.each(items, function (i, item) {
		$('.tree').append('<li id='+item.CD_TP+'>');
		if(i==0){
			$('.tree li:last').append('<div class="item opened ">');
		}else{
			$('.tree li:last').append('<div class="item">');
		}
		$('.tree li:last div').append('<span class="label">'+item.CD_TP+'('+item.CD_TP_MEANING+')</span>');
		$('.tree li:last div').append('<button class="arr"></button>');
		$('.tree li:last').append('<ul>');
	});
	drawTreeLevel2('30');
}

function drawTreeLevel2(companyCode){
	var params = {};
	params['companyCode'] = companyCode;
	$.ajax({
		type : 'POST',
        url:'selectMenuByCompany',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(items){
        	var menuCnt = 0;
        	var submenu = '#'+companyCode + ' > ul';
        	$.each(items, function (i, item) {
        		if(item.DEPTH == '1') {
        			$(submenu).append('<li>');        			
        			$(submenu + ' > li:eq('+menuCnt+')').append('<div class="item" cd_tp1='+item.CD_TP1+'>');
        			$(submenu + ' > li:eq('+menuCnt+') > div').append('<span class="label" >'+item.MENU_ID+'</span>');
        			
        			
        			if(items[i+1].DEPTH=='2'){
        				$(submenu + ' > li:eq('+menuCnt+') > div').append('<button class="ct active"></button>');
        				$(submenu + ' > li:eq('+menuCnt+') > div').append('<button class="arr"></button>');
        			} else {
        				$(submenu + ' > li:eq('+menuCnt+') > div').append('<button class="ct"></button>');
        			}
        		} else if(item.DEPTH == '2'){
        			if(items[i-1].DEPTH=='1')
        				$(submenu + ' > li:eq('+menuCnt+')').append('<ul>');
        			$(submenu + ' > li:eq('+menuCnt+') > ul').append('<li>');
        			$(submenu + ' > li:eq('+menuCnt+') > ul > li:last').append('<div class="item" cd_tp1='+item.CD_TP1+'>');
        			$(submenu + ' > li:eq('+menuCnt+') > ul > li:last > div').append('<span class="label">'+item.MENU_ID+'</span>');
        			$(submenu + ' > li:eq('+menuCnt+') > ul > li:last > div').append('<button class="ct"></button>');
        		}
        		if(i!=items.length-1 && items[i+1].DEPTH=='1')
    				menuCnt++;
        	});
       },
       error : function(e){ 
       }
    });
}


function getMenuData(){
	var params = {};
	var company = $('#dialog_tree .tree div.item.active').parents('li:eq(1)').attr('id')!=undefined ?
			$('#dialog_tree .tree div.item.active').parents('li:eq(1)')
			: $('#dialog_tree .tree div.item.active').parents('li:eq(2)');
	params['companyCode'] = 
		$('#dialog_tree .tree div.item.active').parents('li:eq(1)').attr('id')!=undefined ?
				$('#dialog_tree .tree div.item.active').parents('li:eq(1)').attr('id') 
				: $('#dialog_tree .tree div.item.active').parents('li:eq(2)').attr('id');
	params['cd_tp1'] = $('#dialog_tree .tree div.item.active').attr('cd_tp1');
	$.ajax({
		type : 'POST',
        url:'selectMenuDetailsByCompanyAndMenu',
        data : JSON.stringify(params),
        dataType:'json', 
        contentType: 'application/json',
        success:function(items){
        	var depth1 = '';
        	if(items[0].DEPTH=='2')
        		depth1 = company.children().children().children().first().text();
        	var menuString = company.children().first().text();  
        	menuString += depth1!='' ? ' - '+depth1+ ' - ' + items[0].MENUID : ' - ' + items[0].MENUID;
        	$('#content').prepend('<h2>'+menuString+'</h2>'); 
        	
        	if(items[0].CD_TP1.substr(0,4)=='EM46'){ //ElaConfig
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('메뉴코드(CD_TP1)'))
        				.append($('<td>').text(items[0].CD_TP1))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('한글명'))
        				.append($('<td>').text(items[0].CD_V_EXPLAIN))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('영문명'))
        				.append($('<td>').text(items[0].MENUID))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('사용회사코드'))
        				.append($('<td>').text(makeCompanyNames(items[0].USECOMCODE)))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('미사용회사코드'))
        				.append($('<td>').text(makeCompanyNames(items[0].NOTUSECOMCODE)))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text(items[0].MENU_METAS.split("=")[0]))
        				.append($('<td>').text(items[0].MENU_METAS.split("=")[1].replace(/"/gi,'')))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>'))
        				.append($('<td>'))
        			);
        		//XMP AREA
        		$('.rd_json pre').html('&lt;system<br>'
        				+'    id="'+items[0].MENUID+'"<br>'
//		        		+'    kor_name="'+items[0].CD_V_EXPLAIN+'"<br>'
//		        		+'    eng_name="'+items[0].MENUID+'"<br>'
		        		+'    '+items[0].MENU_METAS.split("=")[0]+'="'+items[0].MENU_METAS.split("=")[1].replace(/"/gi,'')+'"'
		        		+'&gt;&lt;system/&gt;');
        	}else{ // Not ElaConfig
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('메뉴코드(CD_TP1)'))
        				.append($('<td>').text(items[0].CD_TP1))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('서버환경'))
        				.append($('<td>').text(items[0].INFRATYPE))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('한글명'))
        				.append($('<td>').text(items[0].KOR_NAME))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('영문명'))
        				.append($('<td>').text(items[0].ENG_NAME))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('사용회사코드'))
        				.append($('<td>').text(makeCompanyNames(items[0].USECOMCODE)))
        			);
        		$('#content tbody').append($('<tr>')
        				.append($('<th>').text('미사용회사코드'))
        				.append($('<td>').text(makeCompanyNames(items[0].NOTUSECOMCODE)))
        			);
        		
        		$.each(items, function (i, item) {
        			if(i!=0){
        				var em = '';
        				if(item.EXPLAIN1!=null){ 
        					if(item.EXPLAIN2!=null) {
        						em = ' ('+item.EXPLAIN1 +' - '+item.EXPLAIN2+')';
        					}else{
        						em = ' ('+item.EXPLAIN1+')';
        					}
        				}else if(item.EXPLAIN2!=null) {
        					em = ' ('+item.EXPLAIN2+')';
        				} 
        				$('#content tbody').append($('<tr>')
                				.append($('<th>').text(item.META))
                				.append($('<td>').text(item.CD_TP_MEANING)
                						.append($('<em>').text(em)))
                			);
        			}
        		});
        		
        		$('#content tbody').append($('<tr>')
        				.append($('<th>'))
        				.append($('<td>'))
        			);
        		//XMP AREA
        		var systemString = '';
        		$.each(items, function (i, item) {
        			if(i!=0){
        				systemString += '    '+item.META+'="'+item.CD_TP_MEANING+'"<br>';
        			}
        		});
        		systemString = systemString.substr(0, systemString.length-4);
        		$('.rd_json pre').html('&lt;system<br>'
        				+'    id="'+items[0].MENUID+'"<br>'
		        		+'    kor_name="'+items[0].KOR_NAME+'"<br>'
		        		+'    eng_name="'+items[0].ENG_NAME+'"<br>'
//		        		+'    '+items[0].MENUID+'="'+items[0].MENU_METAS+'"'
		        		+systemString
		        		+'&gt;&lt;/system&gt;');
        	}

       },
       error : function(e){ 
       }
    });	
}

function selectMetaListByMetas(){
	if($('#sel_metas').val()=='선택'){
		alert('속성을 선택하세요.');
		return;
	}
	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	if($('#sel_company').val()!='00')
		params['companyCode'] = $('#sel_company').val();
	if($('#input_metaValue').val()!='')
		params['cd_tp_meaning'] =  $('#input_metaValue').val();
	$.ajax({
		type : 'POST',
        url:'selectMetaListByMetas',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(items){
        	if(items.length!=0){
        		var dataTable = $('#grid_meta_search').DataTable();
            	dataTable.clear();
            	dataTable.rows.add(items);
            	dataTable.draw();
        	} else {
        		alert('입력하신 조건에 맞는 데이터가 없습니다.');
        	}
        	
        },
        error : function(e){ 
        }
     });
}

function selectMenucodeByMeta(){
	if($('#sel_metas').val()=='선택'){
		return;
	}
	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	
	$.ajax({
		type : 'POST',
        url:'selectMenucodeByMeta',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(items){
        	$('#check-all-menu').prop('checked', false);
    		$('#check-all-company').prop('checked', false);
        	var tableCompany = $('#grid_table_company').DataTable();
        	tableCompany.clear();
			tableCompany.draw();
			
        	if(items.length!=0){
        		var dataTable = $('#grid_table_menu').DataTable();
            	dataTable.clear();
            	dataTable.rows.add(items);
            	dataTable.draw();	
        	} else {
        		alert('입력하신 조건에 맞는 데이터가 없습니다.');
        	}
        	
        },
        error : function(e){ 
        }
     });
}

function selectNotExistMenucodeByMeta(){
	if($('#sel_metas').val()=='선택'){
		return;
	}
	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	
	$.ajax({
		type : 'POST',
        url:'selectNotExistMenucodeByMeta',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(items){
        	var tableCompany = $('#grid_table_company').DataTable();
        	tableCompany.clear();
			tableCompany.draw();
			
        	if(items.length!=0){
        		var dataTable = $('#grid_table_menu').DataTable();
            	dataTable.clear();
            	dataTable.rows.add(items);
            	dataTable.draw();	
        	} else {
        		alert('입력하신 조건에 맞는 데이터가 없습니다.');
        	}
        	
        },
        error : function(e){ 
        }
     });
}

function selectCompanyByMetaMenu(menu_list){
	var menu_params = [];
	if(menu_list.length < 1){
		var dataTable = $('#grid_table_company').DataTable();
    	dataTable.clear();
    	dataTable.draw();
		return;
	} else {
		for(var i =0; i< menu_list.length; i++){
			menu_params[i] = menu_list[i];
		}
	}
	
	if($('#sel_metas').val()=='선택'){
		alert('속성 값을 선택하세요.');
		return;
	}
	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	params['menu_list'] = menu_params;
	if($('#content > h2').text()=='메뉴 속성 추가'){
		params['insertCheck'] = 'Y';
	}
	var dataTable = $('#grid_table_company').DataTable();
	dataTable.clear();
	$.ajax({
		type : 'POST',
        url:'selectCompanyByMetaMenu',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(items){
        	if(items.length!=0){
            	dataTable.rows.add(items);
            	dataTable.draw();
        	}
        },
        error : function(e){ 
        }
     });
}

function selectNotExistCompanyByMetaMenu(menu_list){
	var menu_params = [];
	if(menu_list.length < 1){
		var dataTable = $('#grid_table_company').DataTable();
    	dataTable.clear();
    	dataTable.draw();
		return;
	} else {
		for(var i =0; i< menu_list.length; i++){
			menu_params[i] = menu_list[i];
		}
	}
	
	if($('#sel_metas').val()=='선택'){
		alert('속성 값을 선택하세요.');
		return;
	}
	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	params['menu_list'] = menu_params;
	if($('#content > h2').text()=='메뉴 속성 추가'){
		params['insertCheck'] = 'Y';
	}
	var dataTable = $('#grid_table_company').DataTable();
	dataTable.clear();
	$.ajax({
		type : 'POST',
        url:'selectNotExistCompanyByMetaMenu',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(items){
        	if(items.length!=0){
            	dataTable.rows.add(items);
            	dataTable.draw();
        	}
        },
        error : function(e){ 
        }
     });
}
function updateMetaValues(menu, company){
	var menu_params = [];
	var company_params = [];
	
	for(var i =0; i< menu.length; i++){
		menu_params[i] = menu[i];
	}
	for(var i =0; i< company.length; i++){
		company_params[i] = company[i];
	}

	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	params['metaValue'] = $('#input_metaValue').val();
	params['menu_list'] = menu_params;
	params['companyCode'] = company_params;
	
	$.ajax({
		type : 'POST',
        url:'updateMetaValues',
        data : JSON.stringify(params),
//        dataType:'json',
        contentType: 'application/json',
        success:function(result){
        	alert('메뉴속성 재설정이 완료되었습니다.');
        	
        },
        error : function(e){ 
        	alert('메뉴속성 재설정에 실패했습니다.');
        }
     });
}

function insertMetaToMenu(menu, company){
	var menu_params = [];
	var company_params = [];
	
	for(var i =0; i< menu.length; i++){
		menu_params[i] = menu[i];
	}
	for(var i =0; i< company.length; i++){
		company_params[i] = company[i];
	}

	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	params['metaValue'] = $('#input_metaValue').val();
	params['menu_list'] = menu_params;
	params['companyCode'] = company_params;
	
	$.ajax({
		type : 'POST',
        url:'insertMetaToMenu',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('메뉴속성 추가가 완료되었습니다.');
        	selectNotExistMenucodeByMeta();
        },
        error : function(e){ 
        	alert('메뉴속성 추가에 실패했습니다.');
        }
     });
}

function deleteMetaInMenu(menu, company){
	var menu_params = [];
	var company_params = [];
	
	for(var i =0; i< menu.length; i++){
		menu_params[i] = menu[i];
	}
	for(var i =0; i< company.length; i++){
		company_params[i] = company[i];
	}

	var params = {};
	params['cd_tp1'] = $('#sel_metas').val();
	params['menu_list'] = menu_params;
	params['companyCode'] = company_params;
	
	
	$.ajax({
		type : 'POST',
        url:'deleteMetaInMenu',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('메뉴속성 삭제가 완료되었습니다.');
        	selectMenucodeByMeta();
        },
        error : function(e){ 
        	alert('메뉴속성 삭제에 실패했습니다.');
        }
     });
}

function getMenuFullData(comCode, infraType){
	 var params = {};
	 params['companyCode'] = comCode;
	 params['infraType'] = infraType;

	 $.ajax({
	 type : 'POST',
	         url:'selectMenuDetailsByCompanyAndMenu',
	         data : JSON.stringify(params),
	         dataType:'json',
	         contentType: 'application/json',
	         success:function(items){
	     var menuline = '<company ';    
	         if(comCode.indexOf('_')>-1) menuline += 'id="'+comCode.split("_")[0]+'" ';
	         else menuline += 'id="'+comCode+'" ';
	         menuline += 'name="'+companyNames[comCode]+'"';
	         if(comCode.indexOf('_')>-1) menuline += ' dept="'+comCode.split("_")[1]+'"';
	         menuline += '>\n';
	         
	     var bf_depth;
	         for(var m =1; m< items.length; m++){        
	         if(items[m].DEPTH=='1'){
	         if(m>1){
	             if(bf_depth=='1') menuline+= '/>\n';
	             else menuline+= '/>\n</system>\n';
	             }
	         menuline+='<system ';
	         }else{
	         if(m>1){
	             if(bf_depth=='1') menuline+= '>\n';
	             else menuline+= '/>\n';
	             }
	         menuline+='<sub_menu ';
	         }
	         bf_depth = items[m].DEPTH;
	         
	     menuline+='id="'+items[m].MENUID+'" ';
	     if(items[m].KOR_NAME!=null && items[m].CD_TP1.substr(0,4)!='EM46') menuline+= 'kor_name="'+items[m].KOR_NAME+'" ';
	     if(items[m].ENG_NAME!=null && items[m].CD_TP1.substr(0,4)!='EM46') menuline+= 'eng_name="'+items[m].ENG_NAME+'" ';
	     if(items[m].MENU_METAS!=null) menuline+= items[m].MENU_METAS;
	         }
	         menuline += '/>\n</system>\n</company>';
	     $('.rd_xmlviewer XMP').text(menuline);//XMP AREA

	        },
	        error : function(e){ 
	        }
     });
 }

	 

