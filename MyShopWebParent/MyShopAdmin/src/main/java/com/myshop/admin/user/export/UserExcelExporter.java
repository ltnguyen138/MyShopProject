package com.myshop.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.myshop.common.entity.User;



public class UserExcelExporter extends AbstractExportHeader {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	public UserExcelExporter() {
		workbook=new XSSFWorkbook();
	}
	private void writeHeaderLine () {
		sheet=workbook.createSheet("Users");
		XSSFRow row=sheet.createRow(0);
		XSSFCellStyle cellstyle=workbook.createCellStyle();
		XSSFFont font=workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellstyle.setFont(font);
		createCell(row, 0, "ID", cellstyle);
		createCell(row, 1, "Email", cellstyle);
		createCell(row, 2, "Họ", cellstyle);
		createCell(row, 3, "Tên", cellstyle);
		createCell(row, 4, "Vai trò", cellstyle);
		createCell(row, 5, "Trạng thái TK", cellstyle);
	}
	private void createCell(XSSFRow row,int colIndex,Object value,CellStyle style) {
		XSSFCell cell=row.createCell(colIndex);
		sheet.autoSizeColumn(colIndex);
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		}else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		}else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
	
	private void writeDataLines(List<User>listUsers) {
		int rowIndex=1;
		
		XSSFCellStyle cellstyle=workbook.createCellStyle();
		XSSFFont font=workbook.createFont();		
		font.setFontHeight(14);
		cellstyle.setFont(font);
		
		for(User u:listUsers) {
			XSSFRow row= sheet.createRow(rowIndex++);
			createCell(row, 0, u.getId(), cellstyle);
			createCell(row, 1, u.getEmail(), cellstyle);
			createCell(row, 2, u.getLastName(), cellstyle);
			createCell(row, 3, u.getFirstName(), cellstyle);
			createCell(row, 4, u.getRoles().toString(), cellstyle);
			createCell(row, 5, u.isEnabled(), cellstyle);
		}
	}
	public void export(List<User> listUser,HttpServletResponse response) throws IOException {
		super.setResponesHeader(response, "application/octet-stream", ".xlsx");
		
		writeHeaderLine();
		writeDataLines(listUser);
		
		ServletOutputStream outputstream=response.getOutputStream();
		workbook.write(outputstream);
		workbook.close();
		outputstream.close();
	}
}
