package API_Testing.Functional.Automation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.testng.annotations.Test;

public class TestClass {
	
	
	
	@Test
	public void getTimeStamp()
	{
		System.out.println("System current time is : " + System.currentTimeMillis());
		System.out.println("Data us : " + appendTimeStamp("data"));
	}
	
	public static String appendTimeStamp(String data) {

		String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss",
		Locale.US).format(new Date());
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(timeStamp);
		stringBuffer.append("_" + data);
		return stringBuffer.toString();
		}
}
