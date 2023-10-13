$(document).ready(function(){
		
		$("#shortDescription").richText();
		$("#fullDescription").richText();
		
		$("#buttonCancel").on("click",function(){
			window.location=moduleURL;
		});
		
		dropdownBrand.change(function(){
			dropdownCategory.empty();
			getCategories();
		});
		getCategoriesNewForm();
		
		
	})	;
	
	function getCategoriesNewForm(){
		catId=$("#categoryId");
		if(!catId.length){
			getCategories();
		}
	}
	
	function getCategories(){
		brandId=dropdownBrand.val();
		url=brandURL+"/"+brandId+"/categories";
		$.get(url, function(responseJson){
			$.each(responseJson, function(index,category){
				$("<option>").val(category.id).text(category.name).appendTo(dropdownCategory)
			});
		});
	}
	
	function checkUnique(formp){
		
		pId=$("#id").val();
		pName=$("#name").val();
		pAlias=$("#alias").val();
		csrfValue=$("input[name='_csrf']").val();
		params={id:pId, name: pName, alias: pAlias, _csrf: csrfValue};
		$.post(checkUniqueUrl, params, function(response){
			if(response=="Ok"){
				formp.submit();
				
			}else if (response=="DuplicateName"){
				showModalDialog("Lỗi","Tên sản phẩm: "+pName+" đã được sử dụng");
			}else if (response=="DuplicateAlias"){
				showModalDialog("Lỗi","Bí danh: "+pAlias+" đã được sử dụng");
			}
		});
		return false;
	}
	
	