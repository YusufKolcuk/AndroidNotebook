package com.yusufkolcuk.notdefterimpinsoft.Oth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    private static final String TAG = "Utility";

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getMonthFromNumber(String monthNumber){
        switch(monthNumber){
            case "01":{
                return "OCAK";
            }
            case "02":{
                return "ŞUBAT";
            }
            case "03":{
                return "MART";
            }
            case "04":{
                return "NİSAN";
            }
            case "05":{
                return "MAYIS";
            }
            case "06":{
                return "HAZİRAN";
            }
            case "07":{
                return "TEMMUZ";
            }
            case "08":{
                return "AĞUSTOS";
            }
            case "09":{
                return "EYLÜL";
            }
            case "10":{
                return "EKİM";
            }
            case "11":{
                return "KASIM";
            }
            case "12":{
                return "ARALIK";
            }

            default:{
                return "ERROR";
            }
        }
    }

}
