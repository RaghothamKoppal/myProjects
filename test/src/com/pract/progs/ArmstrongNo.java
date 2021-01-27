package com.pract.progs;

public class ArmstrongNo {
    
    	static int power(int x, long y) 
        { 
            if( y == 0) 
                return 1; 
            if (y%2 == 0) 
                return power(x, y/2)*power(x, y/2); 
            return x*power(x, y/2)*power(x, y/2); 
        } 
      
        /* Function to calculate order of the number */
        static int order(int x) 
        { 
            int n = 0; 
            while (x != 0) 
            { 
                n++; 
                x = x/10; 
            } 
            return n; 
        } 
      
        // Function to check whether the given number is 
        // Armstrong number or not 
        static boolean isArmstrong (int x) 
        { 
            // Calling order function 
            int n = order(x); 
            int temp=x, sum=0; 
            while (temp!=0) 
            { 
                int r = temp%10; 
                sum = sum + power(r,n); 
                temp = temp/10; 
            } 
      
            // If satisfies Armstrong condition 
            return (sum == x); 
        }
    
    public static void main(String[] args){
      
        int number = 375; 
        if(isArmstrong(number))
        System.out.println("given number is an armstrong number"); 
        else
        System.out.println("given number is not an armstrong number"); 
    }

}
