package com.julieservice.app;

import java.util.*;
import java.text.*;

public final class Utils 
{
    // Function to answer whether passed string is a valid date or not
    public static boolean isValidDate(String d) {
    
        try {
            DateFormat format = 
                new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = format.parse(d);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    // Function to convert date string into Date object
    public static Date convertStringToDate(String d) {
        try {
            DateFormat format = 
                new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return format.parse(d);
        }
        catch (Exception ex) {
            return null;
        }
    }

    // Function to determine if a passed string is a valid double or not
    public static boolean isValidDouble(String d) {
        
        try {
            Double.parseDouble(d); 
            return true; 
        }
        catch (Exception ex) {
            return false;
        } 
    }
}
