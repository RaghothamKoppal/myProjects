package API_Testing.Functional.Automation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date_Time {
	public static String getCurrentDateAndTime(){
		return new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	public static String getCurrentDate(){
		return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}
	public static String getCurrentTime(){
		return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
	}
	//Give the next day or previous day date based on the input i.e "+1 means next day" and "-1 means previous day"
	public static String getDatesBasedOnCurrentDate(int no_of_days){
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		cal.add(Calendar.DATE, no_of_days);
		return dateFormat.format(cal.getTime());
	}
	public static String getTimeBasedOncurrentTime(int time){
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		cal.add(Calendar.MINUTE, time);
		return dateFormat.format(cal.getTime());
	}
	public static String getDate(String s){
		if(s.equalsIgnoreCase("today"))
			return getCurrentDate();
		else
			return getDatesBasedOnCurrentDate(Integer.parseInt(s));
	}
	public static String getTime(String s){
		CharSequence negitiveChar="-";
		if(s.equalsIgnoreCase("currentTime") || s.equalsIgnoreCase("c"))
			return getCurrentTime();
		else if(s.contains(negitiveChar))
			return getTimeBasedOncurrentTime(Integer.parseInt("-"+getNumericalValueFromString(s)));
		else
			return getTimeBasedOncurrentTime(Integer.parseInt(getNumericalValueFromString(s)));
	}
//	This method will help to pick only numbers from given string
	public static String getNumericalValueFromString(String str){
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(str);
		String value="";
		while(m.find())
			value=m.group(1);
		return value;
	}
	/** This will give the time difference between given dates*/
	public static HashMap<String,Long> timeDifference(String dateStart,String dateStop)  {
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date d1 = null;
		Date d2 = null;
		HashMap<String,Long> diffDate=new HashMap<String,Long>();
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			diffDate.put("D", diffDays);
			diffDate.put("H", diffHours);
			diffDate.put("M", diffMinutes);
			diffDate.put("S", diffSeconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diffDate;
	}
	
	
	
	//================================================================================================================
	
	/**
	 * 
	 * @param reqDate, Have to pass the std date format like - dd/mm/yyyy
	 * @return , Returns the api format date as - yyyy-mm-dd
	 */
	public static String convertStdDateToAPIDateFormat(String reqDate)
	{
		String[] splittedDates = reqDate.split("/");
		reqDate = splittedDates[2]+"-"+splittedDates[1]+"-"+splittedDates[0];
		return reqDate;
	}
	public static String convertAPIDateFormatToStdDate(String reqDate)
	{
		String[] splittedDates = reqDate.split("-");
		reqDate = splittedDates[2]+"/"+splittedDates[1]+"/"+splittedDates[0];
		return reqDate;
	}
	public static String convertAPIDateFormatDDMMYYYYToYYYYMMDD(String reqDate)
	{
		String[] splittedDates = reqDate.split("-");
		reqDate = splittedDates[2]+"-"+splittedDates[1]+"-"+splittedDates[0];
		return reqDate;
	}
	
	public static String getCurrentDateInAPIDateFormat()
	{
		String date = Date_Time.getCurrentDate();
		String[] splittedDates = date.split("-");
		date = splittedDates[2]+"-"+splittedDates[1]+"-"+splittedDates[0];
		return date;
	}
	
	/**
	 * 
	 * @param dayName : Passing day name like - Sunday, Monday as such
	 * @return : Returns the next immediate date of the passed day, in the format dd/mm/yyyy
	 * @throws ParseException
	 * @author manjunath
	 */
	public static String getNextImmediateDate(String dayName) throws ParseException
	{
		String foundDate="";
		String day = "";
		for(int i=0; i<=6; i++)
		{
			foundDate = Date_Time.getDatesBasedOnCurrentDate(i).replace("-", "/");
			day = getDayOfDate(foundDate);
			if(dayName.contains(day))
				break;
		}
		return foundDate;
	}
	public static String getDayOfDate(String reqDate) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = df.parse(reqDate);
		df.applyPattern("EEE");
		return df.format(date);
	}
	
	
	/**
	 * 
	 * @param actDate : The date of string in the format dd/MM/yyyy
	 * @param daysToAdd : Interger value to add the number of days
	 * @return Returns the added date in the form of dd/mm/yyyy
	 * @throws ParseException
	 */
	public static String addDaysToDate(String actDate, int daysToAdd) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		Date date = sdf.parse(actDate);
		c.setTime(date); // Now use today date.
		c.add(Calendar.DATE, daysToAdd); // Adding 5 days
		String output = sdf.format(c.getTime());
		System.out.println(output);
		return output;
	}
	
	
	/*
	Calendar cal = Calendar.getInstance();
	Date_Time dt = new Date_Time();
	System.out.println("get date method is : " + dt.getCurrentDate());
	System.out.println("Added date is : " + dt.getDatesBasedOnCurrentDate(+12));
	
	Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
	Date currentTime = localCalendar.getTime();
    int currentDay = localCalendar.get(Calendar.DATE);
    int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
    int currentYear = localCalendar.get(Calendar.YEAR);
    int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
    int currentDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
    int CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);
    
    System.out.println("Current Date and time details in local timezone");
    System.out.println("Current Date: " + currentTime);
    System.out.println("Current Day: " + currentDay);
    System.out.println("Current Month: " + currentMonth);
    System.out.println("Current Year: " + currentYear);
    System.out.println("Current Day of Week: " + currentDayOfWeek);
    System.out.println("Current Day of Month: " + currentDayOfMonth);
    System.out.println("Current Day of Year: " + CurrentDayOfYear);
	*/
	

	public static String getIncrementedHourWithMinuteOfCurrent(int incrementableHour)
	{
		String[] currentHourArray = Date_Time.getCurrentTime().split(":");
		int currentHour = Integer.parseInt(currentHourArray[0].trim());
		
		for(int i=0; i<incrementableHour; i++)
		{
			currentHour++;
			if(currentHour>23)
				currentHour = 00;
		}
		String hour = "";
		if(currentHour <= 9)
			hour = "0"+currentHour+"";
		else
			hour = currentHour+"";
		
		return hour+":"+currentHourArray[1].trim();
	}
	
	
	
	
	
	
}
