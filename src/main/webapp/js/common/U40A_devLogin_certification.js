$( document ).ready( function(){
	$('#password').keypress(function(event) {
	    if (event.keyCode == 13 || event.which == 13) {
	    	validateEncryptedForm();
	    }
	});
}); 
		


function validateEncryptedForm(){
	var password = $("#password").val();
    if (!password) {
        alert('인증번호를 입력해주세요.');
        return false;
    }
    try {
        var rsaPublicKeyModulus = $("#rsaPublicKeyModulus").val();
        var rsaPublicKeyExponent = $("#rsaPublicKeyExponent").val();
        submitEncryptedForm(password, rsaPublicKeyModulus, rsaPublicKeyExponent);
    } catch(err) {
        alert(err);
    }
    return false;
}

function getContextPath() {
//	   return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
			return window.location.pathname;
	}
	

function submitEncryptedForm(password, rsaPublicKeyModulus, rsaPublicKeyExponent) {
	//jsbn_rsa.js : jsbn library의 RSA 암호화 사용
	var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);

    // 사용자ID와 비밀번호를 RSA로 암호화한다.
    var securedPassword = rsa.encrypt(password);
    
//    alert(getContextPath());
    
    $.ajax({
		type : 'POST',
		url : "certification",     
        data : JSON.stringify({
	    	'securedPassword':securedPassword
	    }),
        contentType: 'application/json',
        async : true,
        success:function(result){
        	if(result=='success'){
//        		window.location='/monitoring/';
        		window.location=getContextPath();
        	}else {
        		alert('비밀번호가 일치하지 않습니다.');
        	}
        },
        error : function(e){ 
        	
        }
     });
}