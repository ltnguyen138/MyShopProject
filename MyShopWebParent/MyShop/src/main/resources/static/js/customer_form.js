var dropDownProvinces  ;
var dropDownDistrict  ;
var provinceSelected;
var password;
var confirmPassword;
var btnSubmit;

$(document).ready(function(){
	dropDownProvinces =$("#province");	
	dropDownDistrict =$("#district");
	password =$("#password");
	confirmPassword=$("#confirmPassword");
	btnSubmit=$("#btnSubmit")
	dropDownProvinces.on("change", function(){
		loadDistrictByProvince();
	});	
	
	$("#buttonCancel").on("click",function(){
				window.location=contextPath;
			});
	loadDistrictByProvince();		
});

function checkEmailAndPassword(formc){

	checkEmailUnique(formc);
	
	
}

function checkPassworkAddUpdateAcc(){
	var ischeckLengthPassword=checkLengthPassword();
	var ischeckSpecialCharPassword=checkSpecialCharPassword();
	var ischeckNumberInPassword=checkNumberInPassword();
	var ischeckUppercaseInPassword=checkUppercaseInPassword();
	var ischeckConfirmPassword=checkConfirmPassword();
	if((ischeckLengthPassword&&ischeckSpecialCharPassword&&ischeckNumberInPassword&&ischeckUppercaseInPassword&&ischeckConfirmPassword)
	||(password.val().length==0&&confirmPassword.val().length==0)){
		$("#btnSubmitUpdate").prop("disabled",false);
	}else{
		$("#btnSubmitUpdate").prop("disabled",true);
	}
	
}

function checkPassworkAddNewAcc(){
	var ischeckLengthPassword=checkLengthPassword();
	var ischeckSpecialCharPassword=checkSpecialCharPassword();
	var ischeckNumberInPassword=checkNumberInPassword();
	var ischeckUppercaseInPassword=checkUppercaseInPassword();
	var ischeckConfirmPassword=checkConfirmPassword();
	if(ischeckLengthPassword&&ischeckSpecialCharPassword&&ischeckNumberInPassword&&ischeckUppercaseInPassword&&ischeckConfirmPassword){
		$("#btnSubmitAdd").prop("disabled",false);
	}else{
		$("#btnSubmitAdd").prop("disabled",true);
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

function checkConfirmPassword(){
	if(password.val()===confirmPassword.val()){
		$("#confirmPasswords").text("Mật khẩu nhập lại  chính xác");
		$("#confirmPassworde").text("");
		return true;
	}else{
		$("#confirmPassworde").text("Mật khẩu nhập lại không chính xác");
		$("#confirmPasswords").text("");
		return false;
	}
}
function loadDistrictByProvince(){
	provinceId=dropDownProvinces.val();
	url= contextPath+"districts/list_by_province/"+provinceId;
	$.get(url,function(responseJson){
		dropDownDistrict.empty();
		$.each(responseJson,function(index,district){
			$("<option>").val(district.name).text(district.name).appendTo(dropDownDistrict);
		});
	});
};

function checkEmailUnique(formc){
			url= contextPath+"customers/check_email";
				customerEmail=$("#email").val();
				customerId=$("#id").val();
				csrfValue=$("input[name='_csrf']").val();
				params={id:customerId, email: customerEmail,_csrf: csrfValue};
				$.post(url, params, function(response){
					if(response=="OK"){
						
						formc.submit();
					}else if (response=="Duplicate"){
						showModalDialog("Lỗi","Email "+customerEmail+" đã được sử dụng");
					}
					
				});
				
				return false;
		}