var data;
var chartOptions;
var totalGrossSales;
var totalNetSales;
var totalItems;
var	startDateField;
var	endDateField;

$(document).ready(function(){
		setupButtonEventHandlers("_category", loadSalesReportByDateForCategory);
		
	})	;
	

	
function loadSalesReportByDateForCategory(period){
	if(period=="custom"){
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();
		requestURL = contextPath + "reports/category/"+startDate+"/"+endDate;
	}else{
		requestURL = contextPath + "reports/category/"+period;
	}
	
	$.get(requestURL,function(responseJSON){
		prepareChartDataForSalesReportByCategory(responseJSON);
		customizeChartForSalesReportByCategory(period);
		formatChartData(data, 1, 2);
		drawChartForSalesReportByCategory();
		setSalesAmount(period, '_category', "Tổng sản phẩm");
	});
}

function prepareChartDataForSalesReportByCategory(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Loại sản phẩm');
	data.addColumn('number', 'Doanh thu');
	data.addColumn('number', 'Lợi nhuận');
	
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseInt(reportItem.productsCount);
		
	});

};
function customizeChartForSalesReportByCategory(period){
	chartOptions = {
		
		height: 360,
		legend: {position: 'right'},
		
		
	};
	
};
function drawChartForSalesReportByCategory(){
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
	salesChart.draw(data, chartOptions);
	
	
};

