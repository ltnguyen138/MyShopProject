$(document).ready(function(){
		
		$("#fileImage").change(function(){
			fileSize=this.files[0].size;
			if(fileSize>1048576){
				this.setCustomValidity("Kích thước file phải nhỏ hơn 1MB");
				this.reportValidity();
			}else{
				this.setCustomValidity("");
				showImageThumbnail(this)
			}
				
			
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