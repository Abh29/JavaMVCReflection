package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;

import java.io.FileNotFoundException;
import java.util.List;

public class CarModel extends Model{

    public static final String HEADER = "ModelId,Maker,Model";

    {
        fillables.put("Maker", new Param<Integer>(int.class));
        fillables.put("Model", new Param<String>(String.class));
    }

    public static List<CarModel> All() throws Exception {
        return Model.all(CarModel.class, DataParsers::parse);
    }

    public static <T> List<CarModel> Where(String col, T value) throws Exception {
        return (List<CarModel>) Model.where(CarModel.class, CarModel.All(), col, value);
    }

    public static <T> List<CarModel> Where(String col,String operator, T value) throws Exception {
        return Model.where(CarModel.class, CarModel.All(), col, operator, value);
    }

    public static <T> List<CarModel> Where(String col, T value, List<CarModel> dataList) throws Exception {
        return (List<CarModel>) Model.where(CarModel.class, dataList, col, value);
    }

    public static <T> List<CarModel> Where(String col,String operator, T value, List<CarModel> dataList) throws Exception {
        return Model.where(CarModel.class, dataList, col, operator, value);
    }

    public static CarModel find(int id) throws Exception{
        return new CarModel(Model.find(CarModel.class, CarModel.All(), id)); //48 17
    }

    public static CarModel find(int id, List<CarModel> dataList) throws Exception{
        return new CarModel(Model.find(CarModel.class, dataList, id)); //48 17
    }

    public static CarModel createInstance(Model baseModel){
        return new CarModel(baseModel);
    }

    private CarModel(Model baseModel){
        setModel(baseModel);
    }

    private CarModel(){};

    public static Model createInstance(int id){
        Model out = null;
        CarModel carModel = new CarModel();
        try{
            out  =  CarModel.createInstance(CarModel.class, id);
            out.fillables = carModel.fillables;
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
        return  createInstance(DataLinker.getNewId(CarModel.class.getSimpleName()));
    }


    public CarMaker carMaker() throws Exception {
        return this.belongsTo(CarMaker.class, "Maker");
    }


}
