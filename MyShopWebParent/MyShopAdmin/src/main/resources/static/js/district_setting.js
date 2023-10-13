var buttonLoadProvincesForDistrict  ;
var dropDownProvincesForDistrict  ;
var dropDownDistrict  ;
var fieldDistrictName  ;
var btnAddDistrict  ;
var btnDeleteDistrict  ;
var btnUpdateDistrict  ;
var selectedProvinceNameForDistrict;
var selectedProvinceForDistrict;
var selectedDistrict;
var provinceIdForDistrict;
$(document).ready(function(){
	buttonLoadProvincesForDistrict= $("#buttonLoadProvincesForDistrict");
	dropDownProvincesForDistrict= $("#dropDownProvincesForDistrict");
	dropDownDistrict= $("#dropDownDistrict");
	fieldDistrictName= $("#fieldDistrictName");
	btnAddDistrict= $("#btnAddDistrict");
	btnDeleteDistrict= $("#btnDeleteDistrict");
	btnUpdateDistrict= $("#btnUpdateDistrict");
	
	buttonLoadProvincesForDistrict.click(function(){
		dropDownDistrict.empty();
		loadProvincesForDistrict();
		changeFormDistrictToNew();
	});
	
	dropDownProvincesForDistrict.on("change", function(){
		loadDistrictByProvince();
	});	
	
	dropDownDistrict.on("change", function(){
		changeFormDistrictToSelectesDistrict();
	});	
	
	btnAddDistrict.click(function() {
		if (btnAddDistrict.val() == "Thêm") {
			addDistrict();
		} else {
			changeFormDistrictToNew();
		}
	});
	
	btnUpdateDistrict.click(function() {
		updateDistrict()
	});
	
	btnDeleteDistrict.click(function() {
		deleteDistrict()
	});
	
});

function validateFormDistrict(){
	formDistrict = document.getElementById("formDistrict");
	if(!formDistrict.checkValidity()){
		formDistrict.reportValidity();
		return false;
	}
	return true;
}


function deleteDistrict() {
	selectedDistrict=$("#dropDownDistrict option:selected")
	districtId= selectedDistrict.val();

	url = contextPath + "districts/delete/" + districtId;

	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownDistrict option[value='" + districtId + "']").remove();
		changeFormDistrictToNew();
		showToastMessage("Xóa thành công");
	
	});
}


function updateDistrict() {
	
	if(!validateFormDistrict()) return;
	url = contextPath + "districts/save";
	selectedProvinceForDistrict=$("#dropDownProvincesForDistrict option:selected");
	selectedDistrict=$("#dropDownDistrict option:selected");
	
	
	provinceId=parseInt(selectedProvinceForDistrict.val().split("-")[0]);
	code = selectedProvinceForDistrict.val().split("-")[1];
	
	provinceName=selectedProvinceForDistrict.text();
	districtName = fieldDistrictName.val();
	districtId= selectedDistrict.val();
	alert(code);
	jsonData = {id:districtId, name: districtName, province:{id:provinceId, name: provinceName, code: code}  };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(districtId) {
		$("#dropDownDistrict option:selected").val(districtId);
		$("#dropDownDistrict option:selected").text(districtName);
		showToastMessage("Cập nhật thành công");

		changeFormDistrictToNew();
	
	});
}



function addDistrict(){
	if(!validateFormDistrict()) return;
	url = contextPath + "districts/save";
	
	selectedProvinceForDistrict=$("#dropDownProvincesForDistrict option:selected")
	
	provinceId=dropDownProvincesForDistrict.val().split("-")[0];
	provinceName=selectedProvinceForDistrict.text();
	districtName = fieldDistrictName.val();
	
	jsonData = { name: districtName, province:{id:provinceId, name: provinceName}  };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(districtId) {
		selectNewlyAddedDistrict(districtId, districtName);
		showToastMessage("Thêm quận/huyện thành công");
	});

};

function selectNewlyAddedDistrict(districtId, districtName){
	$("<option>").val(districtId).text(districtName).appendTo(dropDownDistrict);

	$("#dropDownDistrict option[value='" + districtId + "']").prop("selected", true);

	
	fieldDistrictName.val("").focus();
}

function loadProvincesForDistrict(){
	url=contextPath+"provinces/list";
	$.get(url, function(responseJson){
		dropDownProvincesForDistrict.empty();
		$.each(responseJson,function(index,province){
			optionValue = province.id + "-" + province.code;
			$("<option>").val(optionValue).text(province.name).appendTo(dropDownProvincesForDistrict);
		});
	}).done(function(){
		
		buttonLoadProvincesForDistrict.val("Tải lại danh sách tỉnh/thành phố");
		showToastMessage("Danh sách tỉnh/thành phố đã được tải");
	});
};

function loadDistrictByProvince(){
	provinceId=dropDownProvincesForDistrict.val().split("-")[0];
	url= contextPath+"districts/list_by_province/"+provinceId;
	$.get(url,function(responseJson){
		dropDownDistrict.empty();
		$.each(responseJson,function(index,district){
			$("<option>").val(district.id).text(district.name).appendTo(dropDownDistrict);
		});
	});
};

function changeFormDistrictToNew(){
	btnAddDistrict.prop("value","Thêm");
	btnDeleteDistrict.prop("disabled",true);
	btnUpdateDistrict.prop("disabled",true);
	
	
	fieldDistrictName.val("").focus();
};

function changeFormDistrictToSelectesDistrict(){
	btnAddDistrict.prop("value","Làm Mới");
	btnDeleteDistrict.prop("disabled",false);
	btnUpdateDistrict.prop("disabled",false);
	
	selectedDistrict=$("#dropDownDistrict option:selected").text();
	fieldDistrictName.val(selectedDistrict);
	
	
}