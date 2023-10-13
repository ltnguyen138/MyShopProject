decimalSepartor = decimalPointType=='COMMA' ? ',' :'.';
thousandsSepartor = thousandsPointType=='COMMA' ? ',' :'.';
$(document).ready(function(){
	
	
	$(".linkMinus").on("click", function(evt){
		evt.preventDefault();
		productId= $(this).attr("pid");
		quantityInput=$("#quantityProductInCart"+productId);
		newQuatityValue = parseInt(quantityInput.val())-1;
		
		if(newQuatityValue>0){
			if(checkQuantity(newQuatityValue)){
				quantityInput.val(newQuatityValue);
				updateQuantity(productId,newQuatityValue)
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
			updateQuantity(productId,newQuatityValue)
		}
		
				
	});
	
	$(".linkRemove").on("click", function(evt){
		evt.preventDefault();
		
		
		removeProduct($(this));
		
				
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

function updateQuantity(productId,quantity){
	quantity= $("#quantityProductInCart"+productId).val();
	url = contextPath+"cart/update/"+productId+"/"+quantity;
	$.ajax({
		type:"POST",
		url:url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(priceAndDiscountValue){
		console.log(priceAndDiscountValue);
		updateSubTotalPrice(priceAndDiscountValue[0],productId);
		updateSubTotalDiscount(priceAndDiscountValue[1],productId);
		updateTotalPrice();
		updateTotalDiscount();
	}).fail(function(){
		showModalDialog("Rất tiếc","Thêm sản phẩm vào giỏ hàng KHÔNG thành công");
	});
};

function updateSubTotalPrice(subTotalPrice,productId){
	
	$("#subTotalPrice"+productId).text(formatCurrency(subTotalPrice));
	$("#subTotalPriceN"+productId).val(subTotalPrice);
}

function updateSubTotalDiscount(subTotalDiscount,productId){
	
	
	$("#subTotalDiscountN"+productId).val(subTotalDiscount);
}

function updateTotalPrice(){
	total = 0.0;
	$(".subTotalPriceN").each(function(index,element){
		total+= parseFloat($(element).val());		
	});
	
	$("#totalPrice").text(formatCurrency(total));
}
function updateTotalDiscount(){
	total = 0.0;
	$(".subTotalDiscountN").each(function(index,element){
		total+= parseFloat($(element).val());		
	});
	if(total>0){
		
		
		$("#totalDiscount").text(formatCurrency(total));
	}else{
		$("#divTotalDiscount").remove();
	}
	
}

function removeProduct(link){
	url = link.attr("href");
	$.ajax({
		type:"DELETE",
		url:url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(respone){
		if(respone==="Bạn cần đăng nhập thực hiện"){
			showModalNoLogin("Thông báo",respone);
		}else{
			showModalDialog("Thông báo",respone);
			rowNumber= link.attr("rowNumber");
			removeProductInHtml(rowNumber);
			updateTotalPrice();
			updateTotalDiscount();
			updateDivStatusCount();
		}
	}).fail(function(){
		showModalDialog("Rất tiếc","xóa sản phẩm khỏi giỏ hàng KHÔNG thành công");
	});
}

function removeProductInHtml(rowNumber){
	$("#row"+rowNumber).remove();
}
function updateDivStatusCount(){
	productCount=0;
	$(".divStatusCount").each(function(index,element){
		productCount++;
		$(element).text(index+1);
	});
	if(productCount<1){
		showEmptyCart();
	}
}

function showEmptyCart(){
	$("#divCart").remove();
	$("#divEmptyCart2").text("Giỏ hàng trống, bạn chưa thêm sản phẩm nào vào giỏ hàng!!!");
}

function formatCurrency(num){
	return $.number(num,decimalDigits,decimalSepartor,thousandsSepartor);
}