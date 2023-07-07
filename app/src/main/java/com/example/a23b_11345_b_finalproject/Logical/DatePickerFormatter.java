package com.example.a23b_11345_b_finalproject.Logical;

import java.util.Calendar;

public class DatePickerFormatter {
    
    public DatePickerFormatter() {
    }

    public static String getTodayDate(boolean isHeb)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month ++ ;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year, isHeb);
    }
    
    public static String makeDateString(int day, int month, int year,boolean isHeb)
    {
        if(isHeb)
            return getMonthFormatHeb(month) + " " + day + " " + year;
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private static String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FAB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        // Default, never should arrive here
        return "JAN";
    }

    private static String getMonthFormatHeb(int month) {
        if (month == 1)
            return "ינואר";
        if (month == 2)
            return "פברואר";
        if (month == 3)
            return "מרץ";
        if (month == 4)
            return "אפריל";
        if (month == 5)
            return "מאי";
        if (month == 6)
            return "יוני";
        if (month == 7)
            return "יולי";
        if (month == 8)
            return "אוגוסט";
        if (month == 9)
            return "ספטמבר";
        if (month == 10)
            return "אוקטובר";
        if (month == 11)
            return "נובמבר";
        if (month == 12)
            return "דצמבר";
        // Default, never should arrive here
        return "ינואר";
    }
}
