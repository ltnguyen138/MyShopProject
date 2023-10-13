$(document).ready(function(){
		$("#logoutLink").on("click", function(e){
			e.preventDefault();
			document.logoutForm.submit();
		});
		customDropdownMenu();
		customizeTabs()
	});
	
$(document).ready(function(){
		$(".link-delete").on("click",function(e){
			e.preventDefault();
			link=$(this);
			userEmail=link.attr("userEmail");
			$("#confirmBody").text("Xác nhận xóa người dùng "+userEmail);
			$("#confirmModal").modal();
			$("#yesButton").attr("href",link.attr("href"));
		})
		
	});
	$(document).ready(function(){
		$(".link-updateEnabled").on("click",function(e){
			e.preventDefault();
			link=$(this);
			userEmail=link.attr("mName");
			$("#confirmTitle").text("Xác nhận");
			$("#confirmBody").text("Xác nhận cập nhật trạng thái tài khoản người dùng: "+userEmail);
			$("#confirmModal").modal();
			$("#yesButton").attr("href",link.attr("href"));
		})
	});
$(document).ready(function(){
			$("#buttonCancel").on("click",function(){
				window.location="[[@{/users}]]"
			});
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
		
		function checkEmailUnique(formm){
			
			url="[[@{/users/check_email}]]";
			userEmail=$("#email").val();
			userId=$("#id").val();
			csrfValue=$("input[name='_csrf']").val();
			params={id:userId, email: userEmail,_csrf: csrfValue};
			$.post(url, params, function(response){
				if(response=="OK"){
					formm.submit();
					
				}else if (response=="Duplicate"){
					showModalDialog("Lỗi","Email "+userEmail+" đã được sử dụng");
				}
				
			});
			return false;
		}
		function showModalDialog(title,message){
			$("#modalTitle").text(title);
			$("#modalBody").text(message);
			$("#modalDialog").modal();
		}
		function customDropdownMenu(){
			$(".dropdown").hover(
				function(){
					$(this).find(".dropdown-menu").first().stop(true,true).delay(200).slideDown();
				},
				function(){
					$(this).find(".dropdown-menu").first().stop(true,true).delay(100).slideUp();
				}
			);
		}
		
function customizeTabs() {
	// Javascript to enable link to tab
	var url = document.location.toString();
	if (url.match('#')) {
		$('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
	}

	
}