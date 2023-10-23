package com.myshop.admin.order;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.myshop.common.entity.User;
import com.myshop.common.entity.order.Order;

public class OrderCsvExporter {
	public void export(List<Order> listOrder,HttpServletResponse response) throws IOException {
		DateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String time =dateformat.format(new Date());
		String fileName="oders_"+time+".csv";
		response.setContentType("text/csv");
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
		
		ICsvBeanWriter csvBeanWriter= new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"ID","Ten KH","Ngày","Tong tien nhap", "Tong tien ban","Phuong thuc thanh toan","Trang thai don"};
		csvBeanWriter.writeHeader(csvHeader);
		String[] fieldMapping= {"id","fullName","orderTime","productCost","total","paymentMethod","orderStatus"};
		for(Order u :listOrder) {
			csvBeanWriter.write(u, fieldMapping);
		}
		csvBeanWriter.close();
	}
}
