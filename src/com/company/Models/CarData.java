package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;

import java.io.FileNotFoundException;
import java.util.List;

public class CarData extends Model{

    public static final String HEADER = "Id,MPG,Cylinders,Edispl,Horsepower,Weight,Accelerate,Year";

    {
        fillables.put("MPG", new Param<Double>(double.class, true));
        fillables.put("Cylinders", new Param<Integer>(int.class));
        fillables.put("Edispl", new Param<Double>(double.class, true));
        fillables.put("Horsepower", new Param<Integer>(int.class, true));
        fillables.put("Weight", new Param<Integer>(int.class));
        fillables.put("Accelerate", new Param<Double>(double.class));
        fillables.put("Year", new Param<Integer>(int.class));
    }

    public static List<CarData> All() throws Exception {
        return Model.all(CarData.class, DataParsers::parse);
    }

    public static <T> List<CarData> Where(String col, T value) throws Exception {
        return (List<CarData>) Model.where(CarData.class, CarData.All(), col, value);
    }

    public static <T> List<CarData> Where(String col,String operator, T value) throws Exception {
        return Model.where(CarData.class, CarData.All(), col, operator, value);
    }
            // use these inside loops

    public static <T> List<CarData> Where(String col, T value, List<CarData> dataList) throws Exception {
        return (List<CarData>) Model.where(CarData.class, dataList, col, value);
    }

    public static <T> List<CarData> Where(String col,String operator, T value, List<CarData> dataList) throws Exception {
        return Model.where(CarData.class, dataList, col, operator, value);
    }

    public static CarData find(int id) throws Exception{
        return new CarData(Model.find(CarData.class, CarData.All(), id)); //48 17
    }

    public static CarData find(int id, List<CarData> dataList) throws Exception{
        return new CarData(Model.find(CarData.class, dataList, id));
    }

    public static CarData createInstance(Model baseModel){
        return new CarData(baseModel);
    }

    private CarData(Model baseModel){
        setModel(baseModel);
    }

    private CarData(){};

    public static Model createInstance(int id){
        Model out = null;
        CarData carData = new CarData();
        try{
            out  =  CarData.createInstance(CarData.class, id);
            out.fillables = carData.fillables;
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
        return  createInstance(DataLinker.getNewId(CarData.class.getSimpleName()));
    }


}
