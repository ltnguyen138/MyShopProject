var dropDownProvinces;
var district;
var dropDownDistrict;

$(document).ready(function(){
	dropDownProvinces =$("#province");	
	district =$("#district");
	dropDownDistrict=$("#listDistrict");
	
	
	dropDownProvinces.on("change", function(){
		loadDistrictByProvince();
		district.val("").focus();
	});	
	
	
	loadDistrictByProvince();
});

function loadDistrictByProvince(){
	provinceId=dropDownProvinces.val();
	
	if(provinceId==null){
		return;
	}
	url= contextPath+"districts/list_by_province/"+provinceId;
	$.get(url,function(responseJson){
		dropDownDistrict.empty();
		$.each(responseJson,function(index,district){
			$("<option>").val(district.name).text(district.name).appendTo(dropDownDistrict);
		});
	});
};
