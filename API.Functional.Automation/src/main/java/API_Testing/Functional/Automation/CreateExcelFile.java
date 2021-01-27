package API_Testing.Functional.Automation;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class CreateExcelFile {
	
	int rownum = 0;
	List<HSSFSheet> hssfSheetsList = new ArrayList<HSSFSheet>();
	static List<String> totalSheetsList = new ArrayList<String>(); 
	Collection<File> files;
	HSSFWorkbook workbook;
	
	File exactFile;
	{
		workbook = new HSSFWorkbook();
		for (String sheets : totalSheetsList)
			hssfSheetsList.add(workbook.createSheet(sheets));
		
		Row headerRow = hssfSheetsList.get(0).createRow(rownum);
		headerRow.setHeightInPoints(40);
	}
	
	
	CreateExcelFile(List<List> l1, List<String> totalSheetsList) throws Exception
	{
		for(HSSFSheet sheet : hssfSheetsList)
		{
			try {
				for (int j = 0; j < l1.size(); j++) 
				{
					Row row = sheet.createRow(rownum);
					List<String> l2= l1.get(j);
					for(int k=0; k<l2.size(); k++)
					{
						Cell cell = row.createCell(k);
						cell.setCellValue(l2.get(k));
					}
					rownum++;
				}
				 
				} catch (Exception e) {
					e.printStackTrace();
				} finally {}
				rownum = 0;
		}
	}
	
	
	
	
	public static void createExcelFileWithHeaders(String file, List<String> headerRow, List<String> totalSheetsList1) throws Exception
	{
		List<List> listRecord1 = new ArrayList<List>();
		listRecord1.add(headerRow);
		totalSheetsList = totalSheetsList1;
		CreateExcelFile cls = new CreateExcelFile(listRecord1, totalSheetsList1);
		cls.createExcelFile(file);
	}
	void createExcelFile(String file)
	{
		FileOutputStream fos = null;
		try {
		fos=new FileOutputStream(new File(file));
		HSSFCellStyle hsfstyle=workbook.createCellStyle();
		hsfstyle.setBorderBottom((short) 1);
		hsfstyle.setFillBackgroundColor((short)245);
		workbook.write(fos);
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	
	
}
