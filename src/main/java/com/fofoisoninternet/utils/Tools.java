package com.fofoisoninternet.utils;

/**
 *
 * @author fofoisoninternet
 */
public class Tools {
    
    public static final String API_KEY = EnvLoader.get("OSU_API_KEY");
    
    /**
     * The minimum between two integers
     * @param a int
     * @param b int
     * @return the smallest number between a and b
     */
    public static int min(int a, int b) {
        if (a > b) {
            return b;
        } else {
            return a;
        }
    }
    
    /**
     * Indicates if a string only contains numbers.
     * Yes : 1, 12, 456
     * No : -1, 2a, 45,3
     * @param strNum String to check 
     * @return true iff the string is numeric.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
}
