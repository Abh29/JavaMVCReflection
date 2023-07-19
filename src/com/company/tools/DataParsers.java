package com.company.tools;

import com.company.Models.Model;

import java.lang.reflect.Method;
import java.util.Arrays;

public class DataParsers {

    private static char separator = ',';


    static public <T extends Model> T parse(String line , Class<T> className) throws Exception {
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        String[] values = line.split(String.valueOf(separator));
        String[] fields = className.getDeclaredField("HEADER").get(null).toString().split(String.valueOf(separator));
        Model out ;

        if (values.length == 0 || !Helper.isInt(values[0])) {
            throw new MyExceptions.CorruptedDataException();
        }

        out = (Model) className.getMethod("createInstance", int.class).invoke(null , Integer.parseInt(values[0]));



        if (out == null)
            throw new MyExceptions.ModelCreateException();


        if (values.length != out.getFillableSize() || values.length != fields.length)
            throw new MyExceptions.WrongDataFormatException();


        for (int i = 1; i < values.length; i++) {

            out.updateField(fields[i], trim(values[i]));
        }

        return (T) out;
    }


    private static String trim(String line) {
        if (line.matches("^[\"\'].+[\"\']"))
            return line.substring(1 , line.length() - 1);
        return line;
    }

    public static void setSeparator(char separator) {
        DataParsers.separator = separator;
    }

    public static char getSeparator() {
        return separator;
    }
}
