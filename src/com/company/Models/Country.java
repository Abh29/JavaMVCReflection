package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;

import java.io.FileNotFoundException;
import java.util.List;

public class Country extends Model{

    public static final String HEADER = "CountryId,CountryName,Continent";

    {
        fillables.put("CountryName", new Param<String>(String.class));
        fillables.put("Continent", new Param<Integer>(int.class));
    }

    public static List<Country> All() throws Exception {
        return Model.all(Country.class, DataParsers::parse);
    }

    public static <T> List<Country> Where(String col, T value) throws Exception {
        return Model.where(Country.class, Country.All(), col, value);
    }

    public static <T> List<Country> Where(String col, String operator, T value) throws Exception {
        return Model.where(Country.class, Country.All(), col,operator, value);
    }

    public static <T> List<Country> Where(String col, T value, List<Country> dataList) throws Exception {
        return  Model.where(Country.class, dataList, col, value);
    }

    public static <T> List<Country> Where(String col,String operator, T value, List<Country> dataList) throws Exception {
        return Model.where(Country.class, dataList, col, operator, value);
    }

    public static Country find(int id) throws Exception{
        return Model.find(Country.class, Country.All(), id);
    }

    public static Country find(int id, List<Country> dataList) throws Exception{
        return Model.find(Country.class, dataList, id);
    }

    public static Country createInstance(Model baseModel){
        return new Country(baseModel);
    }

    public static Model createInstance(int id){
        Model out = null;
        Country country = new Country();
        try{
            out  =  Country.createInstance(Country.class, id);
            out.fillables = country.fillables;
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
        return  createInstance(DataLinker.getNewId(Country.class.getSimpleName()));
    }

    private Country(Model m){
        setModel(m);
    }

    private Country(){};

    public Continent continent() throws Exception {
        return this.belongsTo(Continent.class, "Continent");
    }

    public List<CarMaker> makers() throws Exception{
        return this.hasMany(CarMaker.class, "Country");
    }

}
