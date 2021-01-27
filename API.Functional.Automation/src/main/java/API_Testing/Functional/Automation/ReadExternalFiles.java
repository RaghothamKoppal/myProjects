package API_Testing.Functional.Automation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class ReadExternalFiles {
	
	public static String PROJECT_DIR = null;
	public static String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	static long TIMEOUT = 1000;
	static Calendar startTime, timeWatch;
	public static String testdataWorkbookFile = "";
	public static String ipJsonWorkbookFile = "";
	public static String configPropertiesFile = "";
	public static String apiPathsPropertiesFile = "";
	public static String runModesPropertiesFile = "";
	static ReadExternalFiles extLib = new ReadExternalFiles();
	static String serverEndPoint="";
	static Logger logger = API_Header.logger;
	static{
		PROJECT_DIR = getParentDirectory(".");
		configPropertiesFile = PROJECT_DIR+"/Testdata/CONFIG.properties";
		testdataWorkbookFile = PROJECT_DIR+"/Testdata/TestData.xls";
		ipJsonWorkbookFile = PROJECT_DIR+"/Testdata/Input Json.xls";
		apiPathsPropertiesFile = PROJECT_DIR+"/Testdata/API_Path.properties";
		runModesPropertiesFile = PROJECT_DIR+"/Testdata/Runmodes.properties";
		serverEndPoint = readProperty(configPropertiesFile, "ServerEndPoint");
	}
	
	
	/**
	 * @param currentLocation Has be to passed . to get the current location
	 * @return It will return the current location of project from system
	 */
	public static String getParentDirectory (String currentLocation){
		File f1 = new File (currentLocation);
		String parentDirectory = null; 
		try{
			parentDirectory= f1.getCanonicalPath();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return parentDirectory;
	}
	
	
	
	/**
	 * @category Config_Reusable
	 * @param Property - The value which is going to be fetched against which property.
	 * @return Returns the retrieved value from property, and null if not found
	 * @Description Reads the property from  passed property file
	 * @author Manjunath.K
	 */
	public static String readProperty(String file, String Property) 
	{
		Properties p = new Properties();
		try {
			FileInputStream FI = new FileInputStream(new File(file));
			p.load(FI);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (p.getProperty(Property)); 
	}
	
	/**
	 * @category Config_Reusable
	 * @return It will return all properties which are Yes in the RunModes.properties file.
	 * @throws IOException
	 */
	public static List<String> readPropertiesWithActiveRunmode() throws IOException
	{
		logger.info("Started readPropertiesWithActiveRunmode");
		Properties p1 = new Properties();
	    p1.load(new FileInputStream(runModesPropertiesFile));
	    Enumeration e = p1.propertyNames();
	    List<String> keys = new ArrayList<String>();
	    for (; e.hasMoreElements();) 
	    	keys.add(e.nextElement().toString());
	    List<String> propsWithRunmodeYes = new ArrayList<String>();
	    for(int i=0; i<keys.size(); i++){
			if(p1.get(keys.get(i)).toString().contains("Y") || p1.get(keys.get(i)).toString().contains("y"))
				propsWithRunmodeYes.add(keys.get(i));
	    }
	    logger.info("readPropertiesWithActiveRunmode returning is : " + propsWithRunmodeYes);
	    return propsWithRunmodeYes;
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param file - from which properties file, we want to fetch all properties.
	 * @return List of all properties available in the given properties file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> getPropertiesFromFile(String file) throws FileNotFoundException, IOException
	{
		Properties p1 = new Properties();
	    p1.load(new FileInputStream(file));
	    Enumeration e = p1.propertyNames();
	    List<String> keys = new ArrayList<String>();
	    for (; e.hasMoreElements();) 
	    	keys.add(e.nextElement().toString());
	    return keys;
	}
	
	/**
	 * @category Config_Reusable
	 * @param filePath - File name with path should be passed as argument, the same file is going to be created in the system.
	 */
	public static void createTextFile(String filePath)
	{
		logger.info("Started executing createTextFile in the path - " +  filePath);
		BufferedWriter log = null;
		File file = new File(filePath);
		try { log = new BufferedWriter(new FileWriter(file));	} catch (IOException e1) {
			e1.printStackTrace();}finally{if ( log != null )
				try {
					log.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
		logger.info("Executed createTextFile creation");
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param logFilePath - file name with path in which we want to append the text in the last.
	 * @param newLineMessage - What is the text wants to append to the existing text file
	 * @throws IOException - If the file not found or unable to append text, throws an exception successfully.
	 */
    public static void appendTextInFile(String logFilePath, String newLineMessage) throws IOException
    {
    	FileWriter fw = new FileWriter(logFilePath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append("\n"+newLineMessage);
		bw.close();
		fw.close();
    }
    
	/**
	 * @category Config_Reusable
	 * @param propName What is the property name wants to supply to the existing CONFIG.properties file
	 * @param propValue  What is the property value wants to supply along with property name to the existing CONFIG.properties file
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static void setPropertyIntoConfigFile(String propName, String propValue) throws IOException
	{
		Properties p = new Properties();
		FileInputStream FI = new FileInputStream(new File(configPropertiesFile));
		p.load(FI); //Since we are doing the setting new property or changing existing property in Config file, only config file variable is used directly.
		p.setProperty(propName, propValue);
		p.save(new FileOutputStream(configPropertiesFile), null);
	}
	
	
	
	/**
	 * @category Config_Reusable
	 * @param sheetName - To access the workbook sheet, pass the sheet name
	 * @return - It will return the sheet object
	 * @throws Exception
	 * @author Manjunath K
	 */
	public static HSSFSheet getExcelSheet(String sheetName) throws IOException, Exception
	{
		logger.info("Identfying sheet name from work book is - " + sheetName);
		// By default the method will access to the specified location only.
		FileInputStream fis = new FileInputStream(testdataWorkbookFile);
		@SuppressWarnings("resource")
		HSSFSheet sheet = new HSSFWorkbook(fis).getSheet(sheetName);
		logger.info("Returned with sheet object of - " + sheetName);
		return sheet;
	}
	
	
	
	
	/**
	 * @category Config_Reusable
	 * @param s- Pass HSSFSheet object
	 * @param cellVal - Pass required table name
	 * @param columnNum - Reference column index to identify the table name
	 * @return Row value will be returned in which given cell value is available, if no value found from sheet, then it will return -1
	 * @throws Exception
	 * @Description Returns the row number of test table name in a given data sheet
	 * @author Manjunath.K
	 */
	public static int getRowValue(HSSFSheet s, String cellVal, int columnNum) throws IOException, Exception
	{
		int rowNum = s.getLastRowNum(); // It will get the last active row number of that sheet
		int avlRow=-1;
		for (int i=0; i<=rowNum; i++)
		{
			HSSFRow r = null;
			HSSFCell c = null;
			try{
				r = s.getRow(i);  // r will hold the object of full row of index i, where i is row index staring from 0
				c = r.getCell(columnNum); // c will hold the object of cell of which column of row r, columnNum is index of column.
				String val = c.getStringCellValue(); // val will hold the value available in cell,  based on r and c
				if(val.equalsIgnoreCase(cellVal))
				{
					avlRow = i;
					i=rowNum+1;
				}
			}catch(NullPointerException e){}
		}
		return avlRow;	
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param s- Pass HSSFSheet object
	 * @param cellVal - Pass required table name or cell string to identify row index and column index
	 * @return - Return the row & column index integer values in the form of array
	 * @throws Exception
	 * @Description Returns the row & column index integer values of a given string available in the given data sheet
	 * @author Manjunath.K
	 */
	public static int[] getCellValues(HSSFSheet s, String cellVal) throws Exception
	{
		int[] cell = {-1, -1}; // It is just to have 2 values in the array with integer type
		int row = -2; // by default set to -2, since we use -1/0/1 values assigning to this variable later
		
		for (int i=0; i<=1000; i++)
		{
			// It searches the cell string till the 1000th column of one sheet, if we feel, we can reduce it as well. Here used till 1000th value because, no body will have column till 1000th in the test data excel sheet. And more than 1000th column is impossible to store the data.
			row = getRowValue(s, cellVal, i); // It will get the row value. If cell given cell value is not present, it will get -1 value.
			if (row != -1)
			{
				cell[0] = row; // In the successive condition only, the cell values are finalized, and the same is going to be returned.
				cell[1] = i;
				i = 1001;
				break;
			}
			// If no cell value found in sheet, then throws below error.
			if (i == 1000 && row == -1)
			{
				logger.error("Cell is not present in the given sheet with the cell value : " + cellVal);
				throw new Exception("Cell is not present in the given sheet with the cell value : " + cellVal);
			}
		}
		return cell;
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param s - Pass HSSFSheet object
	 * @param rowIndex - row index from where table name  has to start
	 * @param columnIndex - Reference column index to identify the table name
	 * @param strCell - Pass required table name
	 * @return Returns total columns count of test data in the table
	 * @throws Exception
	 * @Description Returns the total number of test data columns available in the given table name of a data sheet exclusing the flag names given to the table in 3 corners of the test data table.
	 * @author Manjunath.K
	 */
	public static int getTableTotalColumns(HSSFSheet s, int rowIndex, int columnIndex, String strCell) throws Exception
	{
		HSSFRow r = s.getRow(rowIndex+1);
		// Just next column to the table name, usually writing test data in the data sheet, so incrementing to 1
		HSSFCell c ;
		String str ;
		int count = 0;
		
		int i = columnIndex;
		do{
			c = r.getCell(i);
			str = c.getStringCellValue();
			if (str.equalsIgnoreCase(strCell))
			{
				i= -1;
				break;
			}
			count ++;
			i++;
		//count variable is used to count the loop, it directly counts the total columns of test data belongs to the specified table name
		}while(i!=-1);
		return count;
	}
	
	
	
	/**
	 * @category Config_Reusable
	 * @param s - Pass HSSFSheet object
	 * @param rowIndex - row index from where table name  has to start
	 * @param columnIndex - Reference column index to identify the table name
	 * @param strCell - Pass required table name
	 * @return - Returns total rows count of test data in the table
	 * @throws Exception
	 * @Description Returns the total number of test data rows available in the given table name of a data sheet
	 * @author Manjunath.K
	 */
	public static int getTableTotalRows(HSSFSheet s, int rowIndex, int columnIndex, String strCell) throws Exception
	{
		HSSFRow r ;
		HSSFCell c ; 
		String str = null ;
		int count = 0;
		int i = rowIndex+2;
		do{
			r = s.getRow(i);
			c = r.getCell(columnIndex);
			//Try-catch and if block executes finds the table name, by incrementing row index from loop
			try{
			str = c.getStringCellValue();
			}catch(NullPointerException e){}
			if ((str != null) && str.equalsIgnoreCase(strCell))
			{
				i = -1;
				break;
			}
			//count variable is used to count the loop, it directly counts the total rows of test data belongs to the specified table name
			count ++;
			i++;
		}while(i!=-1);
		return count;
	}
	
	

	/**
	 * @category Config_Reusable
	 * @param sheetName - Pass  the string of sheet name
	 * @param tcOperationName - Table name available in the sheet to the operation it is utilized
	 * @return - Returns the multi-dimensional array format data fetched from test data table which is passed as tcOperationName.
	 * @throws Exception
	 * @Description Returns all the data from the specified table name available in the sheet name
	 * @author Manjunath.K
	 */
	public static Object[][] getTableData(String sheetName, String tcOperationName) throws Exception
	{
		HSSFSheet sheet = ReadExternalFiles.getExcelSheet(sheetName); // Retrieves the object of given excel sheet.
		int cell[] = getCellValues(sheet, tcOperationName); // Gets the cell row and column values of test data table named cell from first flag given to test data table.
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		int totalColumns = getTableTotalColumns(sheet, rowIndex, columnIndex, tcOperationName); // Will get total columns are there to the given table name.
		int totalRows = getTableTotalRows(sheet, rowIndex, columnIndex, tcOperationName);// Will get total rows are there to the given table name.
		// Below code fetches all data from table traveling each cell keeping above row and column values as boundaries
		Object[ ][ ] obj = new Object[totalRows][totalColumns];
		HSSFRow r;
		HSSFCell c;
		int arrRowIndex = 0;
		String str = "";
		String[][] twoD= new String[totalRows][totalColumns]; 
		int i, j;
		int columnIndexloop=0;
		for(i=0; i<totalRows; i++) {
			columnIndexloop = columnIndex; // Assigning to another integer, since it both used as per below looping statements.
			r = sheet.getRow(rowIndex+2); // It will return the row , where rowIndex holds starting test case flag row, which is going to be incremented with 2 rows, since the test data rows starts from there in test data table of excel sheet.
			for(j=0; j<totalColumns; j++)
			{
				c = r.getCell(columnIndexloop);
				try{
					str = c.getStringCellValue();
				}catch(NullPointerException e){
					str=""; // If the excel cell is not having any content in it, instead of making it null, keeping the str blank.
				}
				if(str.length() < 1)
					str = "";
				twoD[arrRowIndex][j] = str;
				columnIndexloop++;
			}arrRowIndex++;rowIndex++;
		}
		obj = twoD;
		return obj;
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param Property - The name of the property from a config file passed as string
	 * @param value - String - Value is passed to assign to the passed property
	 * @throws IOException
	 * @Description Writes the property in the Config.properties file
	 * @author Manjunath.K
	 */
	public static void WriteProperty(String Property, String value) throws IOException 
	{
		File f = new File(configPropertiesFile);
		// By default I am storing the properties and values in the CONFIG.properties file only, since we don't use any other file in our framework.
		Properties p = new Properties();
		try {
			FileInputStream FI = new FileInputStream(f);
			p.load(FI);
		}catch (IOException e) {
		e.printStackTrace();
	}
		p.setProperty(Property, value);
		FileOutputStream fr=new FileOutputStream(f);
		p.store(fr,"Properties");
		fr.close();
	}
	
	
	
	/**
	 * @category Config_Reusable
	 * @param sheetName - Pass the sheet name in the form of string
	 * @param cellRowIndex - Pass the integer value of row index in which row index wants to set the cell value
	 * @param cellColumnIndex - Pass the integer value of column index in which column index wants to set the cell value
	 * @param strToSet - Pass the string value - whatever the value we want to set in the cell
	 * @throws Exception
	 * Description Method will set the cell value in the given excel sheet based on the row index and column index.
	 * @author Manjunath.K
	 */
	public static void setCellValue(String sheetName, int cellRowIndex, int cellColumnIndex, String strToSet)throws Exception
	{
		try{
		FileInputStream fis = new FileInputStream(testdataWorkbookFile);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow row = sheet.getRow(cellRowIndex);
		HSSFCell cell = row.getCell(cellColumnIndex);
		
		try{
			cell.setCellValue(strToSet);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			cell.setCellValue("The maximum length of cell contents (text) is 32,767 characters, and the value to store is more than the max limit.\nHence not setting the value. Kindly check in logs");
		}
		FileOutputStream fileOut = new FileOutputStream(testdataWorkbookFile);
		workbook.write(fileOut);
	   fileOut.close();
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	
	
	/**
	 * @category Config_Reusable
	 * @param sheetName - Pass the sheet name in the form of string
	 * @param cellStringValue - Pass the table name, for which table name wants to get the row  index
	 * @return - It returns the row index in the form of integer
	 * @Description Method will return the integer value of row index, for the given table name from first column only.
	 * @author Manjunath.K
	 * @throws Exception 
	 */
	public static int getCellRowIndexFromFirstColumn(String sheetName, String cellStringValue) throws Exception{
		HSSFSheet sheet = getExcelSheet(sheetName);
		HSSFRow row = null;
		HSSFCell cell = null;
		int rowIndex = -1;
		String str = "";
		for(int i = 0; i<= sheet.getLastRowNum(); i++)
		{
			row = sheet.getRow(i);
			try{
				cell = row.getCell(0);
				str = cell.getStringCellValue();
			}catch(NullPointerException e){
			str = "";
			}
			if(str.equals(cellStringValue))
			{
				rowIndex = i;
				break;
			}
		}
		return rowIndex;
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param sheetName Pass the sheet name as argument
	 * @param rowIndex - Pass the integer value of table name contained row index 
	 * @param cellValue - pass the cell value, ie header value of the table
	 * @return - Returns the column index where the passed cell value is present in that table
	 * @author Manjunath.K
	 * @throws Exception 
	 * @Method Description - It will return column index of the header string value available in the given table
	 */
	public static int getColumnIndexFromRowIndexWithTableHeaderCellValue(String sheetName, int rowIndex, String cellValue) throws Exception
	{
		HSSFSheet sheet = getExcelSheet(sheetName);
		HSSFRow row = sheet.getRow(rowIndex+1);
		HSSFCell cell = null;
		String str = "";
		int columnIndex = 0;
		for(int i=1; i<=row.getLastCellNum(); i++)
		{
			cell = row.getCell(i);
			try{
			str = cell.getStringCellValue();
			if(str.equals(cellValue)){
				columnIndex = i;
				break;
			}
			}catch(NullPointerException e){
				columnIndex = 0;
			}
		}
		return columnIndex;
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param suiteNameTable - Pass the string argument, of the suitename given as table name in the RunMode sheet
	 * @param testOperationName - Pass the argument, for which test operation required to update the execution status
	 * @return It will return boolean true / false- If the runmode of test operation is set to yes then it will return true
	 * @throws Exception
	 * @author Manjunath.K
	 * @Method Description - Based on the available runMode of test case operation from the given suite-table name in the RunModes sheet, method updates the execution status to SKIPPED/Executed
	 */
	public static boolean setStatusOfOperationInRunModeSheet(String suiteNameTable, String testOperationName) throws Exception{
		String sheetName = "RunModes";
		boolean runModeFlag = false;
		HSSFSheet s = ReadExternalFiles.getExcelSheet(sheetName);
		int val[] = ReadExternalFiles.getCellValues(s, suiteNameTable);
		int rowIndex = val[0];		
		int tcNamesHeaderColumnIndex = ReadExternalFiles.getColumnIndexFromRowIndexWithTableHeaderCellValue(sheetName, rowIndex, "TestOperationClassName");
		int tcRunModeColumnIndex = ReadExternalFiles.getColumnIndexFromRowIndexWithTableHeaderCellValue(sheetName, rowIndex, "RunMode");
		int tcExecutionStatusColumnIndex = ReadExternalFiles.getColumnIndexFromRowIndexWithTableHeaderCellValue(sheetName, rowIndex, "ExecutionStatus");
		int tableTotalRows = ReadExternalFiles.getTableTotalRows(s, rowIndex, 0, suiteNameTable);
		
		HSSFRow rw = null;
		HSSFCell cell = null;
		int dataRowIndex = rowIndex + 2;
		
		// To get the test case row index and run mode based on the dataRowIndex, below for loop is used
		String runMode = "";
		int tcNameRowIndex = dataRowIndex;
		for (int i=1; i<=tableTotalRows ; i++)
		{
			rw = s.getRow(tcNameRowIndex);
			cell = rw.getCell(tcNamesHeaderColumnIndex);
			if(cell.getStringCellValue().trim().equals(testOperationName)){
				cell = rw.getCell(tcRunModeColumnIndex);
				runMode = cell.getStringCellValue().trim();
				break;
			}
			tcNameRowIndex++;
		}
		// If run mode is containing yes, then execution status updates to "Executed"
		// If run mode is containing other than yes, then execution status updates to "SKIPPED"
		// In case, if the table name / test operation name is improper one, it will through an error here only.
		if(runMode.contains("Y") || runMode.contains("y"))
		{
			runModeFlag = true;
			setCellValue(sheetName, tcNameRowIndex, tcExecutionStatusColumnIndex, "Executed");
		}
		else if(runMode.equals("")){
			logger.error("Found error in the given input data; \n suiteNameTable is : " + suiteNameTable + "\n testOperationName is + " + testOperationName);
			throw new Exception("Found error in the given input data; \n suiteNameTable is : " + suiteNameTable + "\n testOperationName is + " + testOperationName);
		}else
			setCellValue(sheetName, tcNameRowIndex, tcExecutionStatusColumnIndex, "SKIPPED");
		// Return flag will be true if the status sets to Executed, else if the status sets to Skipped - returns false
		return runModeFlag;
	}
	
	
	
	
	public static int getCellRowIndexFromKeywordsColumnOfTestDataTable(String sheetName, String dataTableName, String cellStringValue) throws Exception{
		HSSFSheet sheet = getExcelSheet(sheetName);
		HSSFRow row = null;
		HSSFCell cell = null;
		int rowIndex = -1;
		String str = "";
		int rowStartIndex = ReadExternalFiles.getCellRowIndexFromFirstColumn(sheetName, dataTableName);
		int rowEndIndex = ReadExternalFiles.getTableTotalRows(sheet, rowStartIndex, 0, dataTableName) + rowStartIndex + 1;
		
		for(int i = rowStartIndex+1; i<= rowEndIndex; i++)
		{
			row = sheet.getRow(i);
			try{
				cell = row.getCell(3); // Usually 3rd column is a Keywords column in the test data sheet
				str = cell.getStringCellValue();
			}catch(NullPointerException e){
			str = "";
			}
			if(str.equals(cellStringValue))
			{
				rowIndex = i;
				break;
			}
		}
		return rowIndex;
	}
	
	
	public static void setCellDataInKeywordRow(String sheetName, String tableName, String keyword, String columnHeader, String cellValue) throws Exception
	{
		int keywordRowIndex = ReadExternalFiles.getCellRowIndexFromKeywordsColumnOfTestDataTable(sheetName, tableName, keyword);
		int rowStartIndex = ReadExternalFiles.getCellRowIndexFromFirstColumn(sheetName, tableName);
		int columnIndex = ReadExternalFiles.getColumnIndexFromRowIndexWithTableHeaderCellValue(sheetName, rowStartIndex, columnHeader);
		ReadExternalFiles.setCellValue(sheetName, keywordRowIndex, columnIndex, cellValue);
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param sheetName Sheet name from which we want to access the test data
	 * @param tcOperationName Test data table name required to pass, from which we want to get the test data column headers
	 * @return It will return the array of string which hold the column headers
	 * @throws Exception
	 */
	public static String[] getColumnHeaders(String sheetName, String tcOperationName) throws Exception
	{
		logger.info("Started executing getColumnHeaders");
		HSSFSheet sheet = ReadExternalFiles.getExcelSheet(sheetName); //It will return the sheet object of data excel.
		int cell[] = getCellValues(sheet, tcOperationName); // It will return the row and column index of tcOperationName cell
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		logger.info("Returned cell values are : " + rowIndex + " , & " + columnIndex);
		int tableTotalColumns = getTableTotalColumns(sheet, rowIndex, columnIndex, tcOperationName);
		logger.info("Received tableTotalColumns are : " + tableTotalColumns);
		int tableTotalrows = getTableTotalRows(sheet, rowIndex, columnIndex, tcOperationName);
		logger.info("Received tableTotalrows are : " + tableTotalrows);
		String[] columnHeaders= new String [tableTotalColumns]; 
		String str;
		int j;
		HSSFRow r = sheet.getRow(rowIndex+1); // Below row to the test case starting flag named row, column headers will be mentioned in the test data sheet
			for(j=0; j<tableTotalColumns; j++)
			{
				HSSFCell c = r.getCell(columnIndex);
				try{
					str = c.getStringCellValue();
				}catch(NullPointerException e){
					str="";
				}
		    	columnHeaders[j] = str;
				columnIndex++;
			}
			logger.info("Returning column headers successfully");
			return columnHeaders;
	}
	
	
	
	
	
	
	/**
	 * @category Config_Reusable
	 * @param filePath - Result excel file in which we want to store the resulted data 
	 * @param sheetName - sheet name also required to pass to mention in which sheet result data should be updated.
	 * @param values - All values which required to store in the result sheet, has to be passed in the form of string of array. 
	 * @throws IOException
	 */
	public static void setResultsInRow(String filePath, String sheetName, String[] values) throws IOException
	{
		FileInputStream fis = new FileInputStream(filePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow row = sheet.getRow(1);
		HSSFCell cell = row.getCell(4);
		cell.setCellValue("sample data");
		FileOutputStream fileOut = new FileOutputStream(filePath);
		workbook.write(fileOut);
		fileOut.close();
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param filePath - Result excel file in which we want to store the resulted data 
	 * @param sheetName - sheet name also required to pass to mention in which sheet result data should be updated.
	 * @return It will return the row value in which row resulted data has to be stored
	 * @throws IOException
	 */
	public static int getRowIndexOfOutputExcelFile(String filePath, String sheetName) throws IOException
	{
		FileInputStream fis = new FileInputStream(filePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		int rowIndex = 0;
		try{
		rowIndex = sheet.getLastRowNum();
		}catch(Exception e){
			e.printStackTrace();
		}
		rowIndex = rowIndex + 1;
		return rowIndex;
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param outputFilePath - Result excel file in which we want to store the resulted data
	 * @param sheetName - sheet name also required to pass to mention in which sheet result data should be updated.
	 * @param strToSet - All values which required to store in the result sheet, has to be passed in the form of string of array. 
	 * @param cellRowIndex - In which row we want to store the data, that row index should be passed.
	 * @throws IOException
	 */
	public static void setRowValues(String outputFilePath, String sheetName, String[] strToSet, int cellRowIndex) throws IOException
	{
		FileInputStream fis = new FileInputStream(outputFilePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.createRow(cellRowIndex);
		String shortenString = "";
		for(int i=0; i<strToSet.length; i++)
		{
			Cell cell = row.createCell(i);
			try{
				try{
						shortenString = strToSet[i];
					cell.setCellValue(shortenString);
				}catch(IllegalArgumentException e){
					cell.setCellValue("Since the data length is crossing the cell data limit - 32,767 characters, Just logged in the data in reporter log file");
					e.printStackTrace();
				}
			}catch(Exception e){
				cell.setCellValue("Exception other than data size while storing into respective cell, hence not storing data in this cell");
			}
		}
		FileOutputStream fileOut = new FileOutputStream(outputFilePath);
		workbook.write(fileOut);
		fileOut.close();
	}
	
	
	
	/**
	 * @category Config_Reusable
	 * @param outputFilePath - Result excel file in which we want to store the resulted data 
	 * @param sheetName - sheet name also required to pass to mention in which sheet result data should be updated.
	 * @param values - All values which required to store in the result sheet, has to be passed in the form of string of array. 
	 * @throws IOException
	 */
	public static void setResultsInOutputExcel(String outputFilePath, String sheetName, String[] values) throws Exception
	{
		int cellRowIndex = 0;
		
		try{
		cellRowIndex = ReadExternalFiles.getRowIndexOfOutputExcelFile(outputFilePath, sheetName);
		}catch(Exception e){
			logger.error("setResultsInOutputExcel - getRowIndexOfOutputExcelFile exception is : " + e.getMessage());
			throw new Exception("setResultsInOutputExcel - getRowIndexOfOutputExcelFile exception is : " + e.getMessage());
		}
		try{
		ReadExternalFiles.setRowValues(outputFilePath, sheetName, values, cellRowIndex);
		}catch(Exception e){
			logger.error("setResultsInOutputExcel - setRowValues exception is : " + e.getMessage());
			throw new Exception("setResultsInOutputExcel - setRowValues exception is : " + e.getMessage());
		}
	}
	
	
	
	public static boolean isWorkflowScenarioRunnable(String sheetName, String scenarioName) throws Exception
	{
		boolean runnable = false;
		int rowActualIndex = ReadExternalFiles.getCellRowIndexFromFirstColumn(sheetName,scenarioName);
		FileInputStream fis = new FileInputStream(testdataWorkbookFile);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow row = sheet.getRow(rowActualIndex);
		HSSFCell cell = row.getCell(1);
		String cellData = cell.getStringCellValue().trim();
		if(cellData.contains("Y") || cellData.contains("y"))
			runnable = true;
		return runnable;
	}
	
	/**
	 * @category Config_Reusable
	 * @param sheetName - The sheet name from which want to get the data, it is data sheet name which will be same as test class name usually.
	 * @param tcOperationName - From which test data table, we want to fetch the data. It is test case name.
	 * @return - It will return true/false, based on set run mode yes/no respectively to a given test data table.
	 * @throws Exception - If no cell value found, throws an exception.
	 */
	public static boolean isTCRunmodeSet(String sheetName, String tcOperationName) throws Exception
	{
		String runMode = "";
		HSSFSheet sheet = ReadExternalFiles.getExcelSheet(sheetName); // Will get the sheet name.
		int cell[] = getCellValues(sheet, tcOperationName); // Will get the given test table cell row & column values, from the beginning row of the respective test data table.
		HSSFRow r = sheet.getRow(cell[0]); // It will get the respective row in object.
		HSSFCell c = r.getCell(cell[1]+1); // Since very nect column to test table name mentioned column, will have run mode.
		try{
			runMode = c.getStringCellValue().trim();
		}catch(NullPointerException e){
			runMode="";
		}
		boolean set = false;
		if(runMode.contains("Y") || runMode.contains("y"))
			set = true; // Nt particularly looking for Yes text, even if it is typed with Y only, still it is going to be treated as run mode - yes.
		return set;
	}
	
	
	public static String getAPI_URL(String apiName)
	{
		return serverEndPoint+readProperty(apiPathsPropertiesFile, apiName);
	}
	
	
	/**
	 * @category Config_Reusable
	 * @param testSheetName - Have to pass sheet name which is going to be property name in the RunModes.properties file.
	 * @return If the passed property's value is set to Yes, then it will return true, else returns false.
	 * @throws NullPointerException If the passed property is not present in the properties file, then it will throw an error.
	 */
	public static boolean isGroupRunmodeSet(String testSheetName) throws Exception
	{
		boolean set = false;
		String cRunmode = "";
		try{
			cRunmode = readProperty(runModesPropertiesFile, testSheetName).trim(); // It will return the value contained in properties file against passed sheet name.
		}
		catch(NullPointerException e)
		{
			logger.error("Given test sheet name (" + testSheetName +") is not defined in the RunMode.properites file");
			throw new NullPointerException("Given test sheet name (" + testSheetName +") is not defined in the RunMode.properites file\n"+e.getMessage());
		}
		if(cRunmode.contains("Y") || cRunmode.contains("y"))
			set = true;
		return set;
	}
	
	
	public static String getConfigFileValue(String parameter)
	{
		return readProperty(configPropertiesFile, parameter);
	}
	
	
	
	
	// Below code is related to Json input data excel workbook.
	public static HSSFSheet getJsonExcelSheet(String sheetName) throws IOException, Exception
	{
		// By default the method will access to the specified location only.
		FileInputStream fis = new FileInputStream(ipJsonWorkbookFile);
		@SuppressWarnings("resource")
		HSSFSheet sheet = new HSSFWorkbook(fis).getSheet(sheetName);
		return sheet;
	}
	
	/**
	 * @category Config_Reusable
	 * @param sheetName - Sheet name of child excel file from which sheet we want to get json table data.
	 * @param tcOperationName - Table name of the child excel file.
	 * @return - It will return the table json data in the form of multi dimensional array
	 * @throws Exception
	 */
	public static Object[][] getJsonData(String sheetName, String tcOperationName) throws Exception
	{
		tcOperationName = tcOperationName.split(":")[0].trim(); //Child file's table reference would be child table name followed by : and then proceeds.
		HSSFSheet sheet = ReadExternalFiles.getJsonExcelSheet(sheetName); //Will return the json excel file's sheet's object.
		int cell[] = getCellValues(sheet, tcOperationName); // find's out the row and column numbers where the table name is present with start flag of table.s
		int rowIndex = cell[0]; int columnIndex = cell[1];
		int totalColumns = getTableTotalColumns(sheet, rowIndex, columnIndex, tcOperationName); // Gives the total rows of table.
		int totalRows = getTableTotalRows(sheet, rowIndex, columnIndex, tcOperationName); // Gives the total columns of table.
		Object[ ][ ] obj = new Object[totalRows][totalColumns];
		HSSFRow r;HSSFCell c;
		int arrRowIndex = 0;
		String str = "";
		String[][] twoD= new String[totalRows][totalColumns]; 
		int i, j;
		int columnIndexloop=0;
		for(i=0; i<totalRows; i++) {
			columnIndexloop = columnIndex; // Both variables are used below
			r = sheet.getRow(rowIndex+2);
			for(j=0; j<totalColumns; j++)
			{
				c = r.getCell(columnIndexloop);
				try{
					str = c.getStringCellValue();
				}catch(NullPointerException e){
					str=""; // In case of empty cell value, instead of exception, it will consider the string with empty value.
				}
				if(str.length() < 1)
					str = "";
				twoD[arrRowIndex][j] = str;
				columnIndexloop++;
			}arrRowIndex++;rowIndex++;
		}
		obj = twoD;
		return obj;
	}
	
	/**
	 * @category Config_Reusable
	 * @param sheetName - Sheet name of child excel file from which sheet we want to get json table data.
	 * @param tcOperationName - Table name of the child excel file.
	 * @return It will return the string of array containing attributes mentioned in the given child json table
	 * @throws Exception
	 */
	public static String[] getJsonAttributes(String sheetName, String tcOperationName) throws Exception
	{
		HSSFSheet sheet = ReadExternalFiles.getJsonExcelSheet(sheetName);
		int cell[] = getCellValues(sheet, tcOperationName);
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		int tableTotalColumns = getTableTotalColumns(sheet, rowIndex, columnIndex, tcOperationName);
		int tableTotalrows = getTableTotalRows(sheet, rowIndex, columnIndex, tcOperationName);
		logger.info("table total rows and columns are : " + tableTotalrows + " & "  + tableTotalColumns);
		String[] columnHeaders= new String [tableTotalColumns]; 
		String str;
		int j;
		HSSFRow r = sheet.getRow(rowIndex+1);
			for(j=0; j<tableTotalColumns; j++)
			{
				HSSFCell c = r.getCell(columnIndex);
				try{
					str = c.getStringCellValue();
				}catch(NullPointerException e){
					str="";
				}
		    	columnHeaders[j] = str;
				columnIndex++;
				}
			return columnHeaders;
	}
	
	/**
	 * @category Config_Reusables
	 * @param sheetName - Sheet name of child excel file from which sheet we want to get json table data.
	 * @param tcOperationName - Table name of the child excel file.
	 * @return It will return the string with start and end symbols of json body.
	 * @throws IOException
	 * @throws Exception
	 */
	public static String getStartEndpointofJson(String sheetName, String tcOperationName) throws IOException, Exception
	{
		String symbols = "";
		HSSFSheet sheet = ReadExternalFiles.getJsonExcelSheet(sheetName);
		int cell[] = getCellValues(sheet, tcOperationName);
		HSSFRow r = sheet.getRow(cell[0]);
		HSSFCell c = r.getCell(cell[1]+1);
		try{ // Just next to the table named cell flag, there right side next cell value will be havin start and end points of json body.
			symbols = c.getStringCellValue().trim();
		}catch(NullPointerException e){
			logger.error("Given Json body is not having start and end mentioned in the Input Json excel file");
			throw new Exception("Given Json body is not having start and end mentioned in the Input Json excel file");
		}
		return symbols;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
