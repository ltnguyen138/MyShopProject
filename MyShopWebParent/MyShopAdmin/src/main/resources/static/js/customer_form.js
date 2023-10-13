var dropDownProvinces  ;
var dropDownDistrict  ;
var provinceSelected;
var password;
var confirmPassword;
var btnSubmit;

$(document).ready(function(){
	
	password =$("#password");

	btnSubmit=$("#btnSubmit")
	
	
	checkPasswork();
	
});


function checkPasswork(){
	var ischeckLengthPassword=checkLengthPassword();
	var ischeckSpecialCharPassword=checkSpecialCharPassword();
	var ischeckNumberInPassword=checkNumberInPassword();
	var ischeckUppercaseInPassword=checkUppercaseInPassword();
	
	if((ischeckLengthPassword&&ischeckSpecialCharPassword&&ischeckNumberInPassword&&ischeckUppercaseInPassword)||password.val().length==0){
		$("#btnSubmit").prop("disabled",false);
	}else{
		$("#btnSubmit").prop("disabled",true);
	}
	
}

function checkNumberInPassword(){
	if( /\d/.test(password.val() )){
		$("#numberPasswords").text("Có ít nhất 1 kí tự là số");
		$("#numberPassworde").text("");
		return true;
	}else{
		$("#numberPassworde").text("Có ít nhất 1 kí tự là số");
		$("#numberPasswords").text("");
		return false;
	}
}

function checkUppercaseInPassword(){
	if( /[A-Z]/.test(password.val() )){
		$("#uppercasePasswords").text("Có ít nhất 1 kí tự in hoa");
		$("#uppercasePassworde").text("");
		return true;
	}else{
		$("#uppercasePassworde").text("Có ít nhất 1 kí tự in hoa");
		$("#uppercasePasswords").text("");
		return false;
	}
}
function checkSpecialCharPassword(){
	if(/[@$#%^&*!]/.test(password.val() )){
		$("#specialPasswords").text("Có ít nhất 1 kí tự đặt biệt: @,$,#,%,^,&,*,! ");
		$("#specialPassworde").text("");
		return true;
	}else{
		$("#specialPassworde").text("Có ít nhất 1 kí tự đặt biệt: @,$,#,%,^,&,*,! ");
		$("#specialPasswords").text("");
		return false;
	}
}
function checkLengthPassword(){
	if(password.val().length<8){
		$("#lenghtPassworde").text("Có ít nhất 8 kí tự");
		$("#lenghtPasswords").text("");
		return false;
	}else{
		$("#lenghtPasswords").text("Có ít nhất 8 kí tự");
		$("#lenghtPassworde").text("");
		return true;
	}
}



