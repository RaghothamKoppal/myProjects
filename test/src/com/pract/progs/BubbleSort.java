
package com.pract.progs;

public class BubbleSort {
	
	// An optimized version of Bubble Sort 
    static void bubbleSort(int arr[], int n) 
    {
         
        for (int i = 0; i < n - 1; i++)  
        { 
            boolean swapped = false; 
            for (int j = 0; j < n - i - 1; j++)  
            { 
                if (arr[j] > arr[j + 1])  
                { 
                    int temp = arr[j]; 
                    arr[j] = arr[j + 1]; 
                    arr[j + 1] = temp; 
                    swapped = true; 
                } 
            } 
            if (swapped == false) 
                break; 
        } 
    } 
   
  
    // Driver program 
    public static void main(String args[]) 
    { 
        int arr[] = { 64, 34, 25, 12, 22, 11, 90 }; 
        int n = arr.length; 
        bubbleSort(arr, n); 
        for (int i = 0; i < n; i++) 
            System.out.print(arr[i] + " "); 
    }

}
