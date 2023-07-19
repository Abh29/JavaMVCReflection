package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;

import java.io.FileNotFoundException;
import java.util.List;

public class CarName extends Model{

    public static final String HEADER = "Id,Model,Make";

    {
        fillables.put("Model", new Param<String>(String.class));
        fillables.put("Make", new Param<String>(String.class));
    }

    public static List<CarName> All() throws Exception {
        return Model.all(CarName.class, DataParsers::parse);
    }

    public static <T> List<CarName> Where(String col, T value) throws Exception {
        return (List<CarName>) Model.where(CarName.class, CarName.All(), col, value);
    }

    public static <T> List<CarName> Where(String col,String operator, T value) throws Exception {
        return Model.where(CarName.class, CarName.All(), col, operator, value);
    }

    public static <T> List<CarName> Where(String col, T value, List<CarName> dataList) throws Exception {
        return (List<CarName>) Model.where(CarName.class, dataList, col, value);
    }

    public static <T> List<CarName> Where(String col,String operator, T value, List<CarName> dataList) throws Exception {
        return Model.where(CarName.class, dataList, col, operator, value);
    }

    public static CarName find(int id) throws Exception{
        return new CarName(Model.find(CarName.class, CarName.All(), id)); //48 17
    }
    public static CarName find(int id, List<CarName> dataList) throws Exception{
        return new CarName(Model.find(CarName.class, dataList, id)); //48 17
    }

    public static CarName createInstance(Model baseModel){
        return new CarName(baseModel);
    }

    private CarName(Model baseModel){
        setModel(baseModel);
    }

    private CarName(){};

    public static Model createInstance(int id){
        Model out = null;
        CarName carName = new CarName();
        try{
            out  =  CarName.createInstance(CarName.class, id);
            out.fillables = carName.fillables;
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
        return  createInstance(DataLinker.getNewId(CarName.class.getSimpleName()));
    }


}
