package com.pract.progs;

import java.util.Arrays;

public class Anagram {

	static boolean areAnagram(String str1, String str2) {
	
	char[] arrStr1 = str1.replaceAll("\s", "").toLowerCase().toCharArray();
	char[] arrStr2 = str2.replaceAll("\s", "").toLowerCase().toCharArray(); 

	// If length of both strings is not same, 
	// then they cannot be anagram 
	if (arrStr1.length!=arrStr2.length) 
		return false; 

	// Sort both strings 
	Arrays.sort(arrStr1); 
	Arrays.sort(arrStr2); 

	// Compare sorted strings 
	for (int i = 0; i < arrStr1.length; i++) 
		if (arrStr1[i] != arrStr2[i]) 
			return false; 

	return true;
}

public static void main(String[] args) {
	String str1 = "geeksforgeeks";
	String str2 = "For Geeks geEks";

	if (areAnagram(str1, str2))
		System.out.println("The two strings are anagram of each other");
	else
		System.out.println("The two strings are not anagram of each other");

}
}
