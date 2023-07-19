package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;
import java.io.FileNotFoundException;
import java.util.List;


public class Continent extends Model{

    public static final String HEADER = "ContId,Continent";

    {
        fillables.put("Continent" , new Param<String>(String.class));
    }

    public static List<Continent> All() throws Exception {
        return Model.all(Continent.class, DataParsers::parse);
    }

    public static <T> List<Continent> Where(String col, T value) throws Exception {
        return Model.where(Continent.class, Continent.All(), col, value);
    }

    public static <T> List<Continent> Where(String col, String operator, T value) throws Exception {
        return Model.where(Continent.class, Continent.All(), col,operator, value);
    }

    public static <T> List<Continent> Where(String col, T value, List<Continent> dataList) throws Exception {
        return (List<Continent>) Model.where(Continent.class, dataList, col, value);
    }

    public static <T> List<Continent> Where(String col,String operator, T value, List<Continent> dataList) throws Exception {
        return Model.where(Continent.class, dataList, col, operator, value);
    }

    public static Continent find(int id) throws Exception{
        return Model.find(Continent.class, Continent.All(), id);
    }

    public static Continent find(int id, List<Continent> dataList) throws Exception{
        return Model.find(Continent.class, dataList, id);
    }

    public static Continent createInstance(Model baseModel){
        return new Continent(baseModel);
    }

    public static Model createInstance(int id){
        Model out = null;
        Continent continent = new Continent();
        try{
            out  =  Continent.createInstance(Continent.class, id);
            out.fillables = continent.fillables;
        }catch (FileNotFoundException e ){
            System.out.println(e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public static Model newInstance()
    {
        return  createInstance(DataLinker.getNewId(Continent.class.getSimpleName()));
    }

    private Continent(Model m){
        setModel(m);
    }

    private Continent(){};

    public List<Country> countries() throws Exception {
        return this.hasMany(Country.class, "Continent");
    }


}
