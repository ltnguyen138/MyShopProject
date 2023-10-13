var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000;

function setupButtonEventHandlers(reportType,callbackFunction){
	$(".button-sales-by"+reportType).on("click",function(){
			
			divCustomDateRange = $("#divCustomDateRange"+reportType);
			
			$(".button-sales-by"+reportType).each(function(e){
				$(this).removeClass('btn-primary').addClass('btn-secondary');
			});
			
			$(this).removeClass('btn-secondary').addClass('btn-primary');
			period= $(this).attr("period");
			if(period){
				callbackFunction(period);
				divCustomDateRange.addClass("d-none");
			}else{
				
				divCustomDateRange.removeClass("d-none");
			}
			
		});
		
		$("#buttonViewReportByDateRange"+reportType).on("click", function(e) {
			
			validateDateRange(reportType,callbackFunction);
		});
		initCustomDateRange(reportType);
};

function validateDateRange(reportType,callbackFunction){
	startDateField = document.getElementById('startDate'+reportType);

	days = calculateDays(reportType);
	if(days<=30){
		console.log("nice");
		callbackFunction("custom");
	}else{
		startDateField.setCustomValidity("Khoảng thời gian không được lớn hơn 30 ngày");
		startDateField.reportValidity();
	}
};

function calculateDays(reportType) {

	startDateField = document.getElementById('startDate'+reportType);
	endDateField = document.getElementById('endDate'+reportType);

	startDate = startDateField.valueAsDate;
	endDate = endDateField.valueAsDate;

	differenceInMilliseconds = endDate - startDate;
	return differenceInMilliseconds / MILLISECONDS_A_DAY;
};
	
function initCustomDateRange(reportType) {
	startDateField = document.getElementById('startDate'+reportType);
	endDateField = document.getElementById('endDate'+reportType);

	toDate = new Date();
	endDateField.valueAsDate = toDate;

	fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	startDateField.valueAsDate = fromDate;
};

function getChartTitle(period){
	if(period=="last_7_days") return "Biểu đồ kết quả bán hàng trong 7 ngày gần đây";
	if(period=="last_28_days") return "Biểu đồ kết quả bán hàng trong 28 ngày gần đây";
	if(period=="last_6_months") return "Biểu đồ kết quả bán hàng trong 6 tháng gần đây";
	if(period=="last_years") return "Biểu đồ kết quả bán hàng trong1 năm gần đây";
	if(period=="custom") return "Tùy chọn khoảng thời gian";
	return "Biểu đồ kết quả bán hàng trong 7 ngày gần đây"
};

function getDenominator(period, reportType){
	if(period=="last_7_days") return 7;
	if(period=="last_28_days") return 28;
	if(period=="last_6_months") return 6;
	if(period=="last_years") return 12;
	if(period=="custom") return calculateDays(reportType);
	return 7;
};

function formatCurrency(amount) {
	formattedAmount = $.number(amount, decimalDigits, decimalPointType, thousandsPointType);
	return prefixCurrencySymbol + formattedAmount + suffixCurrencySymbol;
};


function setSalesAmount(period, reportType, labelTotalItems) {
	var denominators= getDenominator(period,reportType);
	
	$("#textTotalGrossSales"+reportType).text(formatCurrency(totalGrossSales));
	$("#textTotalNetSales"+reportType).text(formatCurrency(totalNetSales));
	$("#textAvgGrossSales"+reportType).text(formatCurrency(totalGrossSales/denominators));
	$("#textAvgNetSales"+reportType).text(formatCurrency(totalNetSales/denominators));
	$("#textTotalOders"+reportType).text($.number(totalItems));
	$("#labelTotalItems"+reportType).text(labelTotalItems);
}

function formatChartData(data, columnIndex1, columnIndex2) {
	var formatter = new google.visualization.NumberFormat({
		prefix: prefixCurrencySymbol,
		suffix: suffixCurrencySymbol,
		decimalSymbol: decimalPointType,
		groupingSymbol: thousandsPointType,
		fractionDigits: decimalDigits
	});

	formatter.format(data, columnIndex1);
	formatter.format(data, columnIndex2);
}
