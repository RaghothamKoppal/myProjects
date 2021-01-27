package API_Testing.Functional.Automation;

public class ReflectionMethods {
	
	static TestUtils tu = new TestUtils();
	
	
	
	
	@SuppressWarnings("static-access")
	public static String getRandomString(String str)
	{
		String randomString = "";
		int v = Integer.parseInt(str);
		randomString = tu.getRandomStringOfLength(v);
		return randomString;
	}
	
	
	
	public static String getIdFromHrefOfAuxiliaryResourceResp(String hRef)
	{
		String[] hRefArray = hRef.split("/");
		int arrSize = hRefArray.length;
		return hRefArray[arrSize-1].trim();
	}
	
	public static String getIdFromResponseHref(String hRef)
	{
		String[] hRefArray = hRef.split("/");
		int arrSize = hRefArray.length;
		return hRefArray[arrSize-1].trim();
	}
	
	public static String getRandomNumbers(String totalDigits)
	{
		String numbers = "";
		int number = Integer.parseInt(totalDigits);
		int n = 0;
		for(int i=1; i<=number; i++){
			n = TestUtils.getSingleDigitRandomInteger();
			if(n==0)
				n=1;
			numbers = numbers +	n;
		}
		return numbers;
	}
	
	
	public static String getRandomNumber()
	{
		return TestUtils.getSingleDigitRandomInteger()+"";
	}
	
	
	public static String getCurrentTimeStampInMillis()
	{
		return System.currentTimeMillis()+"";
	}
	
	public static String concat2Strings(String str1, String str2)
	{
		return str1+str2;
	}
	
	public static String concat3Strings(String str1, String str2, String str3)
	{
		return str1+str2+str3;
	}
	
	public static String concat4Strings(String str1, String str2, String str3, String str4)
	{
		return str1+str2+str3+str4;
	}
	

	public static String returnSameString(String str1)
	{
		return str1;
	}
	
	
	
	
	
	
}
