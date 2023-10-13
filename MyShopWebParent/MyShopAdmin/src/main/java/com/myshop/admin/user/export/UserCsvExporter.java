package com.myshop.admin.user.export;

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



public class UserCsvExporter {
	public void export(List<User> listUser,HttpServletResponse response) throws IOException {
		DateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String time =dateformat.format(new Date());
		String fileName="users_"+time+".csv";
		response.setContentType("text/csv");
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
		
		ICsvBeanWriter csvBeanWriter= new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"ID","Email","Họ","Tên","Vai trò","Trạng thái TK"};
		csvBeanWriter.writeHeader(csvHeader);
		String[] fieldMapping= {"id","email","lastName","firstName","roles","enabled"};
		for(User u :listUser) {
			csvBeanWriter.write(u, fieldMapping);
		}
		csvBeanWriter.close();
	}
}
