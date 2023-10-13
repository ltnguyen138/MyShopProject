function showModalDialog(title,message){
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDialog").modal();
	}
function showModalNoLogin(title,message){
	$("#modalNoLoginTitle").text(title);
	$("#modalNoLoginBody").text(message);
	$("#modalNoLogin").modal();
}