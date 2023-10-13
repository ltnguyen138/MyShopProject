package com.myshop.admin.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myshop.admin.order.OrderDatailRepository;
import com.myshop.common.entity.order.OrderDetail;

@Service
public class OrderDetailReportService extends AbstractReportService{

	@Autowired
	OrderDatailRepository orderDatailRepository;
	
	@Override
	protected List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType) {
		
		List<OrderDetail> listOrderDetails = null;
		if (reportType.equals(ReportType.CATEGORY)) {
			listOrderDetails = orderDatailRepository.findWithCategoryAndTimeBetween(startDate, endDate);
		} else if (reportType.equals(ReportType.PRODUCT)) {
			listOrderDetails = orderDatailRepository.findWithProductNameAndTimeBetween(startDate, endDate);
		}
		
		
		List<ReportItem> listReportItems = new ArrayList<>();
		
		for (OrderDetail detail : listOrderDetails) {
			String identifier = "";
			
			if (reportType.equals(ReportType.CATEGORY)) {
				identifier = detail.getProduct().getCategory().getName();
			} else if (reportType.equals(ReportType.PRODUCT)) {
				identifier = detail.getProduct().getShortName();
			}
			
			ReportItem reportItem = new ReportItem(identifier);
			
			double grossSales = detail.getSubtotal() + detail.getShippingCost();
			double netSales = detail.getSubtotal() - detail.getProductCost();
			
			int itemIndex = listReportItems.indexOf(reportItem);
			
			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				reportItem.addGrossSales(grossSales);
				reportItem.addNetSales(netSales);
				reportItem.increaseProductsCount(detail.getQuantity());
			} else {
				listReportItems.add(new ReportItem(identifier, grossSales, netSales, detail.getQuantity()));
			}
		}
		printReportData( listReportItems);
		return listReportItems;
	}

	private void printReportData(List<ReportItem> listReportItems) {
		for (ReportItem item : listReportItems) {
			System.out.printf("%-20s, %10.2f, %10.2f, %d \n",
					item.getIdentifier(), item.getGrossSales(), item.getNetSales(), item.getProductsCount());
		}
	}
	
	private void printRawData(List<OrderDetail> listOrderDetails) {
		// TODO Auto-generated method stub
		for (OrderDetail detail : listOrderDetails) {
			System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n",
					detail.getQuantity(), detail.getProduct().getCategory().getAlias(),
					detail.getSubtotal(), detail.getProductCost(), detail.getShippingCost());
		}
	}

}
