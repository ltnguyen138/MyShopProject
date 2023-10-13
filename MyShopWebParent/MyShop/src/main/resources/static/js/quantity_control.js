
$(document).ready(function(){
	
	
	$(".linkMinus").on("click", function(evt){
		evt.preventDefault();
		productId= $(this).attr("pid");
		quantityInput=$("#quantityProductInCart"+productId);
		newQuatityValue = parseInt(quantityInput.val())-1;
		
		if(newQuatityValue>0){
			if(checkQuantity(newQuatityValue)){
				quantityInput.val(newQuatityValue);
			}
			
			
		}
	});
	
	$(".linkPlus").on("click", function(evt){
		evt.preventDefault();
		productId= $(this).attr("pid");
		quantityInput=$("#quantityProductInCart"+productId);
		newQuatityValue = parseInt(quantityInput.val())+1;
		
		if(checkQuantity(newQuatityValue)){
			quantityInput.val(newQuatityValue);
		}
		
				
	});
	
	function checkQuantity(newQuatityValue){
		quantityOfProduct = $("#quantityOfProduct");
		if(newQuatityValue > parseInt(quantityOfProduct.val())){
			showModalDialog("Rất tiếc","Số lượng hàng hóa tại cửa hàng không đủ");
			
			return false;
		}else{
			$("#btnAddProductToCart").prop("disabled",false);
			return true;
		}
	}
	
})