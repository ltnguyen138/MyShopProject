$(document).ready(function(){
	
		
		$("a[name='linkRemoveDetail']").each(function(index) {
			
			$(this).click(function() {
				removeDetailByIndex(index)
				
			});
		});
		
	})	;

function addDetailSection(){
	
	allDivDetails=$("[id^='divDetails']");
	divDetailCount=allDivDetails.length;
	htmlDetailSection=`
			<div class="form-group row my-2" id="divDetails${divDetailCount}">
				<input type="hidden" name="detailId" value="0" /> 
				<label class="my-1 col-sm-2">Tên chi tiết:</label>			
				<input type="text"  class="form-control col-sm-3" name="detailNames" maxlength="255"  /> 
				
				
				<label class="my-1 col-sm-1">&nbsp; Giá trị: </label>
				<input type="text"  class="form-control col-sm-5" name="detailValues" maxlength="255" /> 
			
			</div>	
	`;
	$("#divProductDetails").append(htmlDetailSection);
	allDivDetails=$("[id^='divDetails']");
	previousDetailSection=allDivDetails.last();
	previousDetailSectionID=previousDetailSection.attr("id");
	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark float-right"
			href="javascript:removeDetail('${previousDetailSectionID}')" 
			title="Xóa chi tiết nay"></a>
	`;
	
	previousDetailSection.append(htmlLinkRemove);
}
function removeDetail(id){
	$("#"+id).remove();
}
function removeDetailByIndex(index) {
	$("#divDetails" + index).remove();
	}