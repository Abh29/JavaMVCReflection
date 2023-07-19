package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.DataParsers;
import com.company.tools.Param;
import java.io.FileNotFoundException;
import java.util.List;

public class CarData extends Model {
    public static final String HEADER = "Id,MPG,Cylinders,Edispl,Horsepower,Weight,Accelerate,Year";

    public static List<CarData> All() throws Exception {
        return Model.all(CarData.class, DataParsers::parse);
    }

    public static <T> List<CarData> Where(String col, T value) throws Exception {
        return Model.where(CarData.class, All(), col, value);
    }

    public static <T> List<CarData> Where(String col, String operator, T value) throws Exception {
        return Model.where(CarData.class, All(), col, operator, value);
    }

    public static <T> List<CarData> Where(String col, T value, List<CarData> dataList) throws Exception {
        return Model.where(CarData.class, dataList, col, value);
    }

    public static <T> List<CarData> Where(String col, String operator, T value, List<CarData> dataList) throws Exception {
        return Model.where(CarData.class, dataList, col, operator, value);
    }

    public static CarData find(int id) throws Exception {
        return new CarData(Model.find(CarData.class, All(), id));
    }

    public static CarData find(int id, List<CarData> dataList) throws Exception {
        return new CarData(Model.find(CarData.class, dataList, id));
    }

    public static CarData createInstance(Model baseModel) {
        return new CarData(baseModel);
    }

    private CarData(Model baseModel) {
        this.fillables.put("MPG", new Param(Double.TYPE, true));
        this.fillables.put("Cylinders", new Param(Integer.TYPE));
        this.fillables.put("Edispl", new Param(Double.TYPE, true));
        this.fillables.put("Horsepower", new Param(Integer.TYPE, true));
        this.fillables.put("Weight", new Param(Integer.TYPE));
        this.fillables.put("Accelerate", new Param(Double.TYPE));
        this.fillables.put("Year", new Param(Integer.TYPE));
        this.setModel(baseModel);
    }

    private CarData() {
        this.fillables.put("MPG", new Param(Double.TYPE, true));
        this.fillables.put("Cylinders", new Param(Integer.TYPE));
        this.fillables.put("Edispl", new Param(Double.TYPE, true));
        this.fillables.put("Horsepower", new Param(Integer.TYPE, true));
        this.fillables.put("Weight", new Param(Integer.TYPE));
        this.fillables.put("Accelerate", new Param(Double.TYPE));
        this.fillables.put("Year", new Param(Integer.TYPE));
    }

    public static Model createInstance(int id) {
        Model out = null;
        CarData carData = new CarData();

        try {
            out = createInstance(CarData.class, id);
            out.fillables = carData.fillables;
        } catch (FileNotFoundException var4) {
            System.out.println(var4.getMessage());
            return null;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return out;
    }

    public static Model newInstance() {
        return createInstance(DataLinker.getNewId(CarData.class.getSimpleName()));
    }
}
