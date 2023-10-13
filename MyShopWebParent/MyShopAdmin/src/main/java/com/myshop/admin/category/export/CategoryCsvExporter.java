package com.myshop.admin.category.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.myshop.common.entity.Category;
import com.myshop.common.entity.User;



public class CategoryCsvExporter extends AbstractExportHeader{
	public void export(List<Category> listCategory,HttpServletResponse response) throws IOException {
		
		super.setResponesHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter csvBeanWriter= new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"ID","Tên","Bí danh","Trạng thái"};
		csvBeanWriter.writeHeader(csvHeader);

		String[] fieldMapping= {"id","name","alias","enabled"};
		for(Category c :listCategory) {
			c.setName(c.getName().replace("--", "  "));
			csvBeanWriter.write(c, fieldMapping);
		}
		csvBeanWriter.close();
	}
}
