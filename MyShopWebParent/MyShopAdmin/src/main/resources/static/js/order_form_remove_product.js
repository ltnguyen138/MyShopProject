$(document).ready(function() {
	$("#productList").on("click", ".linkRemove", function(e) {
		e.preventDefault();

		if (doesOrderHaveOnlyOneProduct()) {
			
			showModalDialog("Lỗi","Phai có it nhất 1 sản phẩm trong đơn hàng");
		} else {
			removeProduct($(this));
			updateOrderAmounts();
		}
	});
});

function removeProduct(link) {
	rowNumber = link.attr("rowNumber");
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();

	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function doesOrderHaveOnlyOneProduct() {
	productCount = $(".hiddenProductId").length;
	return productCount == 1;
}