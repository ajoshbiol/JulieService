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
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
