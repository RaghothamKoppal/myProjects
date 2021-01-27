package com.pract.progs;

public class Fibonacci {
	
	static int fib(int n) 
    { 
        int a = 0, b = 1, c; 
        if (n == 0) 
            return a; 
        for (int i = 2; i <= n; i++) 
        { 
            c = a + b; 
            a = b; 
            b = c; 
        } 
        return b; 
    } 
	
	static void printFibonacciNumbers(int n) 
    { 
        int a = 0, b = 1, c; 
      
        if (n == 0) 
            return ; 
      
        for (int i = 1; i <= n; i++) 
        { 
            System.out.print(b+" "); 
            c = a + b; 
            a = b; 
            b = c; 
        } 
    } 
  
    public static void main (String args[]) 
    { 
        int n = 10; 
        System.out.println(fib(n));
        printFibonacciNumbers(n);
    } 

}
