$(document).ready(function(){
	$("#btnAddProductToCart").on("click", function(evt){
		addToCart();
	});
	
});

function addToCart(){
	quantity= $("#quantityProductInCart"+productId).val();
	url = contextPath+"cart/add/"+productId+"/"+quantity;
	$.ajax({
		type:"POST",
		url:url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(respone){
		if(respone==="Bạn cần đăng nhập để có thể thêm sản phẩm vào giỏ hàng"){
			showModalNoLogin("Thông báo",respone);
		}else{
			showModalDialog("Thông báo",respone);
		}
	}).fail(function(){
		showModalDialog("Rất tiếc","Thêm sản phẩm vào giỏ hàng KHÔNG thành công");
	});
};