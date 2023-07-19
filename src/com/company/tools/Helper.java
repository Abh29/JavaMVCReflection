package com.company.tools;

public class Helper {


    public static boolean isInt(String str) throws  MyExceptions.NullDataException {

        if (str.equals("null"))
            throw new MyExceptions.NullDataException();

            if (str == null) {
                return false;
            }
            try {
                int d = Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
    }

    public static boolean isFloat(String str) throws MyExceptions.NullDataException {

        if (str.equals("null"))
            throw new MyExceptions.NullDataException();

        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



}
