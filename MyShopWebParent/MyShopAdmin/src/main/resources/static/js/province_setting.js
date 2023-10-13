var  buttonLoad;
var dropdownProvinces;
var btnAddProvince;
var btnDeleteProvince;
var btnUpdateProvince;
var fieldProvinceName;
var fieldProvinceCode;
var selectedProvinceName;
$(document).ready(function(){
	buttonLoad= $("#buttonLoadProvinces");
	dropdownProvinces=$("#dropDownProvinces");
	btnAddProvince= $("#btnAddProvince");
	btnDeleteProvince = $("#btnDeleteProvince");
	btnUpdateProvince = $("#btnUpdateProvince");
	fieldProvinceName = $("#fieldProvinceName");
	fieldProvinceCode = $("#fieldProvinceCode");
	
	buttonLoad.click(function(){
		
		loadProvinces();
	});	
	
	dropdownProvinces.on("change", function(){
		changeFormStateToSelectesProvinces();
	});	
	
	btnAddProvince.click(function() {
		if (btnAddProvince.val() == "Thêm") {
			addCountry();
		} else {
			changeFormStateToNew();
		}
	});
	
	btnUpdateProvince.click(function() {
		updateProvince()
	});
	
	btnDeleteProvince.click(function() {
		deleteProvince()
	});
	
});

function validateFormProvince(){
	formProvince = document.getElementById("formProvince");
	if(!formProvince.checkValidity()){
		formProvince.reportValidity();
		return false;
	}
	return true;
}

function deleteProvince() {
	
	optionValue = dropdownProvinces.val();
	provinceId = optionValue.split("-")[0];

	url = contextPath + "provinces/delete/" + provinceId;

	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownProvinces option[value='" + optionValue + "']").remove();
		changeFormStateToNew();
		showToastMessage("Xóa thành công");
	
	}).fail(function(){
		showToastMessage("Có tồn tại quận/huyện trong tỉnh/thành phố, vui lòng xóa tất cả quận/huyện của tỉnh/thành phố này trước để thực hiện thao tác");
	});
}

function updateProvince() {
	
		if(!validateFormProvince()) return;

	url = contextPath + "provinces/save";
	provinceName = fieldProvinceName.val();
	provinceCode = fieldProvinceCode.val()

	provinceId = dropdownProvinces.val().split("-")[0];

	jsonData = { id: provinceId, name: provinceName, code: provinceCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(provinceId) {
		$("#dropDownProvinces option:selected").val(provinceId + "-" + provinceCode);
		$("#dropDownProvinces option:selected").text(provinceName);
		showToastMessage("Cập nhật thành công");

		changeFormStateToNew();
	
	});
}

function addCountry() {
	
	if(!validateFormProvince()) return;

	url = contextPath + "provinces/save";
	provinceName = fieldProvinceName.val();
	provinceCode = fieldProvinceCode.val();
	jsonData = { name: provinceName, code: provinceCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(provinceId) {
		selectNewlyAddedCountry(provinceId, provinceCode, provinceName);
		showToastMessage("Thêm tỉnh/thành phố thành công");
	});

}

function selectNewlyAddedCountry(provinceId, provinceCode,provinceName) {
	optionValue = provinceId + "-" + provinceCode;
	$("<option>").val(optionValue).text(provinceName).appendTo(dropdownProvinces);

	$("#dropDownProvinces option[value='" + optionValue + "']").prop("selected", true);

	fieldProvinceCode.val("");
	fieldProvinceName.val("").focus();
}

function changeFormStateToSelectesProvinces(){
	btnAddProvince.prop("value","Làm Mới");
	btnDeleteProvince.prop("disabled",false);
	btnUpdateProvince.prop("disabled",false);
	
	selectedProvinceName=$("#dropDownProvinces option:selected").text();
	fieldProvinceName.val(selectedProvinceName);
	provinceCode = dropdownProvinces.val().split("-")[1];
	fieldProvinceCode.val(provinceCode);
	
}

function changeFormStateToNew(){
	btnAddProvince.prop("value","Thêm");
	btnDeleteProvince.prop("disabled",true);
	btnUpdateProvince.prop("disabled",true);
	
	
	fieldProvinceName.val("").focus();
	
	fieldProvinceCode.val("");
}

function loadProvinces(){
	url=contextPath+"provinces/list";
	$.get(url, function(responseJson){
		dropdownProvinces.empty();
		$.each(responseJson,function(index,province){
			optionValue = province.id + "-" + province.code;
			$("<option>").val(optionValue).text(province.name).appendTo(dropdownProvinces);
		});
	}).done(function(){
		buttonLoad.val("Tải lại danh sách tỉnh/thành phố");
		showToastMessage("Danh sách tỉnh/thành phố đã được tải");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}