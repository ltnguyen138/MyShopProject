var count =1;
$(document).ready(function(){
	
		$("#fileImage").change(function(){
			if(!checkFileSize(this)){
				return
			}
			showImageThumbnail(this);
		});
		
		$("input[name='extraFileImage']").each(function(index) {
			
			countElements();
			$(this).change(function() {
				if (!checkFileSize(this)) {		
					return;
				}
				showExtraImageThumbnail(this, index);
			});
		});
		
		$("a[name='linkRemoveExtraImage']").each(function(index) {
			
			$(this).click(function() {
				removeExtraImage(index)
				
			});
		});
		
	})	;
	

	function showImageThumbnail(fileI){
		var file=fileI.files[0];
		var reader= new FileReader();
		reader.readAsDataURL(file);
		reader.onload=function(e){
			$("#thumbnail").attr("src",e.target.result);
		};
		
	}
	
	function showExtraImageThumbnail(fileI,index){
		var file=fileI.files[0];
		var reader= new FileReader();
		
		fileName = file.name;
		
		imageNameHiddenField = $("#extraImagesName" + index);
		if (imageNameHiddenField.length) {
			imageNameHiddenField.val(fileName);
		}
		
		reader.readAsDataURL(file);
		reader.onload=function(e){
			$("#extraThumbnail"+index).attr("src",e.target.result);
		};
		if (index >= count-1 ) {
		addExtraImageSection(index + 1);
	}
	}
	
	function checkFileSize(inputFile){
		fileSize=inputFile.files[0].size;
		if(fileSize>1048576){
			inputFile.setCustomValidity("Kích thước file phải nhỏ hơn 1MB");
			inputFile.reportValidity();
			return false;
		}else{
			inputFile.setCustomValidity("");
			return true;
		}
	}
	
	function addExtraImageSection(index){
		htmlExtraImage = 
		`<div class=' border m-3 p-2 text-center' id='divExtraImage${index}'>
				<div id="extraImageHeader${index}"><label >Hình ảnh phụ ${index+1}:</label></div>
				<div>							
					
					<img alt="Hình ảnh phụ ${index+1}"  class="img-fluid" id="extraThumbnail${index}" src="${defaultImageThumbnailSrc}" 
					style="width: 250px; height: 170px"/>																
				</div>
				<div>
					<input type="file" name="extraFileImage" id="extraFileImage" class="m-2  "  
					onchange="showExtraImageThumbnail(this, ${index})"  accept="image/png, image/jpeg"/>
				</div>
			</div>`
	;
	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark float-right"
			href="javascript:removeExtraImage(${index - 1})" 
			title="Remove this image"></a>
	`;
	
		
		$("#divProductImages").append(htmlExtraImage);
		$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
		countElements();
	}
	function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
	}
 function countElements() {
    count = $("input[name='extraFileImage']").length;
    
}