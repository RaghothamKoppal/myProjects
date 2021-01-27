package com.pract.progs;

public class BinarySearch {
	
	 static int search(int arr[], int number) 
	    { 
	        int l = 0, r = arr.length - 1, mid; 
	        while (l <= r) { 
	        	mid = l + (r - l) / 2; 
	  
	            // Check if x is present at mid 
	            if (arr[mid] == number) 
	                return mid; 
	  
	            // If x greater, ignore left half 
	            if (arr[mid] < number) 
	                l = mid + 1; 
	  
	            // If x is smaller, ignore right half 
	            else
	                r = mid - 1; 
	        } 
	  
	        // if we reach here, then element was 
	        // not present 
	        return -1; 
	    }

	public static void main(String args[]) 
    {  
        int arr[] = { 2, 3, 4, 10, 40 }; 
        int number = 11; 
        int result = search(arr, number); 
        if (result == -1) 
            System.out.println("Element not present"); 
        else
            System.out.println("Element found at "
                               + "index " + result); 
    }
}
