var iconNames = {
	'PICKED': 'fa-people-carry',
	'SHIPPING': 'fa-shipping-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-undo'
};

var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;

$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");

	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModal(link);
	});
	
	addEventHandlerForYesButton();

});
function addEventHandlerForYesButton() {
	yesButton.click(function(e) {
		e.preventDefault();
		sendRequestToUpdateOrderStatus($(this));
	});
}


function sendRequestToUpdateOrderStatus(button) {
	requestURL = button.attr("href");
	console.log(requestURL);
	$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showModalDialog("Thông báo","Đơn hàng có ID: "+response.orderId+" đã được cập nhật sang trạng thái "+response.status);
		updateStatusIconColor(response.orderId, response.status);

		console.log(response);
	}).fail(function(err) {
		showMessageModal("Cập nhật không thanh công");
	})
}
function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fas " + iconNames[status] + " fa-2x icon-green'></i>");
}

function showUpdateConfirmModal(link) {
	noButton.show();
	yesButton.show();

	var orderId = link.attr("orderId");
	var status = link.attr("status");
	yesButton.attr("href", link.attr("href"));
	yesButton.text( link.attr("href"));
	confirmText.text("Bạn muốn cập nhật trạng thái đơn hàng ID #" + orderId
		+ " sang " + status + "?");

	confirmModalDialog.modal();
}

