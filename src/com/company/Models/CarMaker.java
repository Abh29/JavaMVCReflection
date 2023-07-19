package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;

import java.io.FileNotFoundException;
import java.util.List;

public class CarMaker extends Model{

    public static final String HEADER = "Id,Maker,FullName,Country";

    {
        fillables.put("Maker", new Param<String>(String.class));
        fillables.put("FullName", new Param<String>(String.class));
        fillables.put("Country", new Param<Integer>(int.class, true));
    }


    public static List<CarMaker> All() throws Exception {
        return Model.all(CarMaker.class, DataParsers::parse);
    }

    public static <T> List<CarMaker> Where(String col, T value) throws Exception {
        return (List<CarMaker>) Model.where(CarMaker.class, CarMaker.All(), col, value);
    }

    public static <T> List<CarMaker> Where(String col,String operator, T value) throws Exception {
        return Model.where(CarMaker.class, CarMaker.All(), col, operator, value);
    }

    public static <T> List<CarMaker> Where(String col, T value, List<CarMaker> dataList) throws Exception {
        return (List<CarMaker>) Model.where(CarMaker.class, dataList, col, value);
    }

    public static <T> List<CarMaker> Where(String col,String operator, T value, List<CarMaker> dataList) throws Exception {
        return Model.where(CarMaker.class, dataList, col, operator, value);
    }


    public static CarMaker find(int id) throws Exception{
        return new CarMaker(Model.find(CarMaker.class, CarMaker.All(), id)); //48 17
    }

    public static CarMaker find(int id, List<CarMaker> dataList) throws Exception{
        return new CarMaker(Model.find(CarMaker.class, dataList, id)); //48 17
    }

    public static CarMaker createInstance(Model baseModel){
        return new CarMaker(baseModel);
    }

    private CarMaker(Model baseModel){
        setModel(baseModel);
    }

    private CarMaker(){};

    public static Model createInstance(int id){
        Model out = null;
        CarMaker carMaker = new CarMaker();
        try{
            out  =  CarName.createInstance(CarMaker.class, id);
            out.fillables = carMaker.fillables;
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
        return  createInstance(DataLinker.getNewId(CarMaker.class.getSimpleName()));
    }


    public Country country() throws Exception{
        return this.belongsTo(Country.class, "Country");
    }

    public List<CarModel> models() throws Exception{
        return this.hasMany(CarModel.class, "Maker");
    }

}
