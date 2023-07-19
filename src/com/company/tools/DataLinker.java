package com.company.tools;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class DataLinker {

    static String file_module_table = "src/com/company/Data/module_file_table.csv";
    static private char separator = ',';


    public static String getFilePath(String className) throws Exception{
        // todo get the file from the file_module table file
        Map<String , String > map = new HashMap<>(10);
        File file = new File(file_module_table);
        RandomAccessFile reader = new RandomAccessFile(file , "rwd");
        reader.seek(0);
        String line = reader.readLine();
        String[] sep;

        while ((line = reader.readLine()) != null){
           sep =  line.split(String.valueOf(separator));
           if(sep.length != 4)
               throw new Exception("Corrupted file !");
           map.put(sep[1],sep[2]);
        }

       // String out = "src\\com\\company\\Files\\continents.csv";
        return map.get(className);
    }

    public static int getNewId(String className){
        return 0;
    }

    public static int getLastId(String className) {
        // this should be changed ;
        return 6;
    }
}
