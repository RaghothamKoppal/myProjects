package API_Testing.Functional.Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
	
	
	static TestUtils utils = new TestUtils();
	
	
	public String propConfig(String key){
		Properties configProp = utils.propConfig();
		return configProp.getProperty(key);
	}
	
	public Properties propConfig() {
		String path=System.getProperty("user.dir")+"//src//insta//files//CONFIG.properties";
		FileInputStream fis = null;
		try{
			Properties config=new Properties();
			fis=new FileInputStream(path);
			config.load(fis);
			return config;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
			} catch (Exception e) {}
		}
	}
	
	public static int getSingleDigitRandomInteger()
	{
		int randomNumber = 0;
		Random r = new Random();
		randomNumber = r.nextInt(9);
		return randomNumber;
	}
	
	public static String getDoubleDigitsRandomNumericalsInStringFormat()
	{
		String randomNumber = "";
		randomNumber = TestUtils.getSingleDigitRandomInteger()+""+TestUtils.getSingleDigitRandomInteger();
		return randomNumber;
	}
	
	public static String getFiveDigitsRandomNumericalsInStringFormat()
	{
		String randomNumber = "";
		randomNumber = TestUtils.getSingleDigitRandomInteger()+""+TestUtils.getDoubleDigitsRandomNumericalsInStringFormat()+TestUtils.getDoubleDigitsRandomNumericalsInStringFormat();
		return randomNumber;
	}
	
	public static String getTenDigitsRandomNumericalsInStringFormat()
	{
		String randomNumber = "";
		randomNumber = TestUtils.getFiveDigitsRandomNumericalsInStringFormat()+""+TestUtils.getFiveDigitsRandomNumericalsInStringFormat();
		return randomNumber;
	}
	
	public static String getRandomStringOfLength(int requiredStringLength)
	{
		String randomString = "";
		Random r = new Random();
		String alphabet = "qwertyuioplkjhgfdsazxcvbnm";
		for (int i = 0; i < requiredStringLength; i++)
		{
			char ch = alphabet.charAt(r.nextInt(alphabet.length()));
			randomString = randomString + ch;
		}
		return randomString;
	}
	
	/**
	 * @category Config_Resable
	 * @param retObjArr - All data retrieved from excel test data table multi dimensional array object
	 * @param columnHdrs - String of array object retrived with column headers of test data table.
	 * @return - It will return the multi array object which is holding an map object data.
	 * @throws IOException
	 */
    public static Object[][] Obj2Map (Object[][] retObjArr, String[] columnHdrs) throws IOException {
    	// This method captures all data received in retObjArr object and puts the each row data as value in one map. Column headers values are going to be treated as keys to each row's map.
        ArrayList<LinkedHashMap<String, String>> tInfo = new ArrayList<LinkedHashMap<String, String>>();
        Object[][] data=new Object[retObjArr.length][1];
        int i;
	    try{
	        for (i=0;i<retObjArr.length;i++)
	        { // for one row of retObjArr, creating new map below.
	        	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		        for(int k=0;k<columnHdrs.length;k++){
		        	// Storing the one row data as values of map. Keys are column headers, and below line code handles it.
		             map.put(columnHdrs[k], retObjArr[i][k].toString());
		        }
		        tInfo.add(map);
		        data[i][0] = map; // each row wise created map is stored in multi dimensional array.
	        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
    
   
    
    public static boolean matchStringWithRegEx(String str, String regEx)
    {
		Pattern p = Pattern.compile(regEx, Pattern.DOTALL);
		Matcher m = p.matcher(String.valueOf(str));
		if(!m.matches())
			return false;
		else
			return true;
    }
    
    public static int getNumberOfSubstringOccurance(String mainString, String subString)
    {
		int lastIndex = 0;
		int count = 0;
		while(lastIndex != -1){
		    lastIndex = mainString.indexOf(subString,lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += subString.length();
		    }
		}
		return  count;
    }
    
    public static LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        
        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            int val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();
            
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                int comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
    
    public static boolean checkStringExistanceInList(List<String>myList, String myStr)
    {
    	for(String str: myList) {
    	    if(str.trim().equalsIgnoreCase(myStr))
    	       return true;
    	}
    	return false;
    }
    
    
    public static boolean isValidMailAddress(String mail)
    {
    	String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(mail);
        return m.matches();
    }
    
    
	public static boolean isJSONValid(String jsonInString ) {
	    try {
	       final ObjectMapper mapper = new ObjectMapper();
	       mapper.readTree(jsonInString);
	       return true;
	    } catch (IOException e) {
	       return false;
	    }
	  }
	
	
	public  static boolean equalLists(List<String> one, List<String> two){     
	    if (one == null && two == null){
	        return true;
	    }

	    if((one == null && two != null) 
	      || one != null && two == null
	      || one.size() != two.size()){
	        return false;
	    }

	    //to avoid messing the order of the lists we will use a copy
	    //as noted in comments by A. R. S.
	    one = new ArrayList<String>(one); 
	    two = new ArrayList<String>(two);   

	    Collections.sort(one);
	    Collections.sort(two);      
	    return one.equals(two);
	}
	
	
	
}
