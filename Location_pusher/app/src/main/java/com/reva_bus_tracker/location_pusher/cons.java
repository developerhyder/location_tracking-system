package com.reva_bus_tracker.location_pusher;

public class cons {
    public static String preff_name = "dataaa";
    public static String route_number = "";
    public static int hour_to_break=0;
    public static String vald = "";
    public static boolean scanned = false;

    public static boolean parsee(String dat){
        //math logic comes here
        //TODO:develop the math logic here
        //TODO: enter route number
        String stri = dat;
        char c1,c2;
        int a=1,b=1;

        char[] ch = dat.toCharArray();

        try {
            a = (int)ch[0];
            b = (int)ch[1];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
        int c = a-b;

        if (c > 0 && c <=30){
            try {
                route_number = Integer.toString(c);
            }catch (Exception e){
                return false;
            }
            return true;
        }
        else
            return false;
    }
}
