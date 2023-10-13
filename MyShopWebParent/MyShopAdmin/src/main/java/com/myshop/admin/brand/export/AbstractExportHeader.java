package com.myshop.admin.brand.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;





public class AbstractExportHeader {

		public void setResponesHeader(HttpServletResponse response,String contentType,String extension) {
			DateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String time =dateformat.format(new Date());
			String fileName="brands_"+time+extension;
			response.setContentType(contentType);
			String headerKey="Content-Disposition";
			String headerValue="attachment; filename="+fileName;
			response.setHeader(headerKey, headerValue);
		}
}
