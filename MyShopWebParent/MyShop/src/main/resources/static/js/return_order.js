var returnModal;
var modalTitle;
var fieldNote;
var orderId;
var divReason;
var divMessage;
var firstButton;
var secondButton;

$(document).ready(function(){
	returnModal = $("#returnOrderModal");
	modalTitle = $("#returnOrderModalTitle");
	fieldNote = $("#returnNote");
	divReason = $("#divReason");
	divMessage = $("#divMessage");
	firstButton = $("#firstButton");
	secondButton = $("#secondButton");
	handleReturnOrderLink();
	
});

function handleReturnOrderLink() {
	$(".linkReturnOrder").on("click", function(e) {
		e.preventDefault();
		showReturnModalDialog($(this));
	});
}

function showReturnModalDialog(link) {
	divMessage.hide();
	divReason.show();
	firstButton.show();
	secondButton.text("Quay lại");
	fieldNote.val("");

	orderId = link.attr("orderId");
	modalTitle.text("Yêu cầu trả hàng, đơn hàng có ID #" + orderId);
	returnModal.modal("show");
}
function submitReturnOrderForm(formm) {
	reason = $("input[name='returnReason']:checked").val();
	note = fieldNote.val();

	

	sendReturnOrderRequest(reason, note);
	returnModal.modal("hide");
	return false;
}

function sendReturnOrderRequest(reason, note) {
	requestURL = contextPath + "orders/return";
	requestBody = { orderId: orderId, reason: reason, note: note };
	console.log(requestURL);
	$.ajax({
		type: "POST",
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'

	}).done(function(returnResponse) {
		console.log(returnResponse);
		showModalDialog("Thông báo","Yêu cầu trả hàng đã được gửi");
		updateStatusTextAndHideReturnButton(returnResponse.orderId);
	}).fail(function(err) {
		console.log(err);
		showModalDialog("Lỗi","Yêu cầu trả hàng đã gửi không thành công");
	});
}	
function updateStatusTextAndHideReturnButton(orderId) {
	$(".textOrderStatus" + orderId).each(function(index) {
		$(this).text("RETURN_REQUESTED");
	})

	$(".linkReturn" + orderId).each(function(index) {
		$(this).hide();
	})
}