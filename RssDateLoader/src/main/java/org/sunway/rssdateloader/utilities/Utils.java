/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sunway.rssdateloader.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.joget.commons.util.LogUtil;
import org.joget.apps.form.model.FormData;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class Utils {
    public static final String internalDate = "InternalDate";
    public static final String internalWD = "InternalWD";
    public static final String externalDate = "ExternalDate";
    public static final String externalWD = "ExternalWD";
    public static final String environment = "Environment";
    //changes for revise target --> rssreviselogic
    public static final String revInternalDate = "RevInternalDate";
    public static final String revExternalDate = "RevExternalDate";
    public static final String managerId = "ManagerId";
    public static final String managerName = "ManagerName";

    public static final String picId = "PICId";
    public static final String picName = "PICName";

//-->ends


    
    public static String getMonthIdFromDate(String date) {
        String columnId = "";
        String[] dateArray = date.split("-");
        String month = dateArray[1];
        
        if(month.equalsIgnoreCase("01")) columnId = "c_Jan";
        else if(month.equalsIgnoreCase("02")) columnId = "c_Feb";
        else if(month.equalsIgnoreCase("03")) columnId = "c_Mar";
        else if(month.equalsIgnoreCase("04")) columnId = "c_Apr";
        else if(month.equalsIgnoreCase("05")) columnId = "c_May";
        else if(month.equalsIgnoreCase("06")) columnId = "c_Jun";
        else if(month.equalsIgnoreCase("07")) columnId = "c_Jul";
        else if(month.equalsIgnoreCase("08")) columnId = "c_Aug";
        else if(month.equalsIgnoreCase("09")) columnId = "c_Sep";
        else if(month.equalsIgnoreCase("10")) columnId = "c_Oct";
        else if(month.equalsIgnoreCase("11")) columnId = "c_Nov";
        else if(month.equalsIgnoreCase("11")) columnId = "c_Dec";

        return columnId;
    }
    
    public static void showError(FormData mData, String elementId, String st) {
        mData.addFormError(elementId, st);
    }
    
    public static String getDateFromWD(String date, int noOfDays, List<String> holidays) {
//        System.out.println("Date before Addition: " + date);
        // Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
        // Setting the date to the given date
            c.setTime(sdf.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // logic for calculating date after adding given days with exception of weekend and holiday
        for (int i = 1; i < noOfDays; i++) {
            int newDate = c.get(Calendar.DAY_OF_WEEK);

            if (newDate == Calendar.FRIDAY) { // If it's Friday, skip to Monday
                c.add(Calendar.DATE, 3);
            } else if (newDate == Calendar.SATURDAY) { // If it's Saturday, skip to Monday
                c.add(Calendar.DATE, 2);
            } else {
                c.add(Calendar.DATE, 1);
            }

                //holiday check
            for (int j = 1; j <= holidays.size(); j++) {
                String dummyDate = sdf.format(c.getTime());
                if (Arrays.asList(holidays).contains(dummyDate)) {
                    c.add(Calendar.DATE, 1);
                }
            }

        }

        /* c.add(Calendar.DAY_OF_MONTH, noOfDays); */
        String convetedDate = sdf.format(c.getTime());
        // Displaying the new Date after addition of Days
//        System.out.println("Date after Addition: " + convetedDate);

        return convetedDate;
    }
    
    public static String arrangedDate (String args) {
        String[] dateArray = {};
        String arrangedStr = "";
                if(args.contains("-")) {
            dateArray = args.split("-");
            arrangedStr = dateArray[2] + "-"+dateArray[1] + "-"+dateArray[0];
        } else if(args.contains("/")) {
            dateArray = args.split("/");
            arrangedStr = dateArray[2] + "-"+dateArray[1] + "-"+dateArray[0];   
        } 
        
        return arrangedStr;
    }
    
    public static void showMsg(String st) {
        LogUtil.info("Debugging QH" + "===> ", st);
    }
}
