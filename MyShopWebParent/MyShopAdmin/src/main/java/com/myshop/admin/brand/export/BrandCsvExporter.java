package com.myshop.admin.brand.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;
import com.myshop.common.entity.User;



public class BrandCsvExporter extends AbstractExportHeader{
	public void export(List<Brand> listBrands,HttpServletResponse response) throws IOException {
		
		super.setResponesHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter csvBeanWriter= new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"ID","Tên","Danh mục sản phẩm"};
		csvBeanWriter.writeHeader(csvHeader);

		String[] fieldMapping= {"id","name","categories"};
		for(Brand b :listBrands) {
			csvBeanWriter.write(b, fieldMapping);
			
		}
		csvBeanWriter.close();
	}
}
