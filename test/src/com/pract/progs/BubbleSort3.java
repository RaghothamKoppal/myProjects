package com.pract.progs;

public class BubbleSort3 {
	
	
	static void sort(int[] arr,int n) {
		
		for(int i=0;i<n-1;i++) {
			
			boolean swapped = false;
			for(int j=0;j<n-i-1;j++) {
				
				if(arr[j]>arr[j+1]) {
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
					swapped =true;
				}
			}
			
			if(swapped == false)
				break;
		}
	}
	
	public static void main(String[] args) {
		
		int[] arr = {};
		int n = arr.length;
		sort(arr,n);
		for(int i=0;i<n;i++)
			System.out.println(arr[i]);
	}

}
