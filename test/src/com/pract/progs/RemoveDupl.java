package com.pract.progs;

public class RemoveDupl {
	
	static int removeDuplicate(int[] arr, int n) {
		
		if(n==1 || n==1)
			return n;
		
		int j=0;
		
		for(int i=0;i<n-1;i++)
			if(arr[i]!=arr[i+1]) 
				arr[j++] = arr[i];
			
		arr[j++] = arr[n-1];
		return j;
	}

	
	public static void main(String[] args) {
		
		int[] arr = {0,1,2,2,3,4,4,4,4,5,5};
		int n = arr.length;
		
		n =removeDuplicate(arr,n);
		for(int i=0;i<n;i++)
			System.out.println(arr[i]);
	}
}
