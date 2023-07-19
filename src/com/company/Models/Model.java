package com.company.Models;

import com.company.tools.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.*;

public class Model {

    private String Path;
    private  File file;
    private  RandomAccessFile accessFile;
    private  List<Model> modelList;
    protected Map<String, Param> fillables = new HashMap<>();
    private  int ID;
    private boolean modified;

    protected static <T extends Model> T createInstance(Class<T> className , int Id) throws Exception {
        Model out = new Model();
        out.setID(Id);
        out.setPath(DataLinker.getFilePath(className.getSimpleName()));
        out.setFile();
        out.setAccessFile();
        out.setModelList();
        return (T) className.getMethod("createInstance", Model.class).invoke(null, out);
    }

    protected static Model createInstance(Class className) throws Exception {
        return Model.createInstance(className , DataLinker.getNewId(className.getSimpleName()));
    }

    protected Model(){};

    protected void setModel(Model m){
        this.Path = m.Path;
        this.file = m.file;
        this.accessFile = m.accessFile;
        this.modelList = m.modelList;
        this.fillables = m.fillables;
        this.ID = m.ID;
        this.modified = m.modified;
    }

    /***************/

    protected static <T extends Model> List<T> all(Class<T> className , Parser parser) throws IOException , Exception {
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        Model model = (Model) className.getMethod("newInstance").invoke(null);
        return model.pAll(parser , className);
    }

    protected static <E, T extends Model> List<T> where (Class<T> className, List<T> modelList, String col, E key) throws Exception{
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        Model model = (Model) className.getMethod("newInstance").invoke(null);
        model.setModelList((List<Model>) modelList);
        return (List<T>) model.pWhere(col, key);
    }

    protected static <E, T extends Model> List<T> where (Class<T> className, List<T> modelList, String col, String operator, E key) throws Exception{
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        Model model = (Model) className.getMethod("newInstance").invoke(null);
        model.setModelList((List<Model>) modelList);

        return (List<T>) model.pWhere(col, operator, key);
    }

    protected static <T extends Model> T find(Class<T> className, List<T> modelList, int id) throws Exception {
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        Model model = (T) className.getMethod("newInstance").invoke(null);

        model.setModelList((List<Model>) modelList);

        return (T) className.getMethod("createInstance" , Model.class).invoke(null, model.pFind(id));
    }

    /****** Private Methods ********/

    private <T extends Model> List<T> pAll(Parser parser , Class<T> className) throws IOException , Exception {
        accessFile.seek(0);
        accessFile.readLine();

        String line = "";
        while((line = accessFile.readLine()) != null)
        {
            modelList.add(parser.parse(line , className));
        }

        return (List<T>) modelList;
    }

    private Model pFind(int id) {
        for (Model m: modelList ) {
            if (m.ID == id)
                return m;
        }
        return null;
    }

    private  <T> List<Model> pWhere (String col , T key) throws Exception{
        Set<String> fields = fillables.keySet() ;
        List<Model> out = new ArrayList<>();

        if (!fields.contains(col))
            throw new MyExceptions.ColumnDoesNotExistException();

        String val;

        try {
            if (key == null || key.equals("null")) {
                val = switch (getFieldDataType(col)) {
                    case "int" -> "0";
                    case "double" -> "0.0";
                    default -> "null";
                };
            } else
                val = switch (getFieldDataType(col)) {
                    case "int" -> String.valueOf(Integer.parseInt(key.toString()));
                    case "double" -> String.valueOf(Double.parseDouble(key.toString()));
                    default -> key.toString();
                };
        }catch (NumberFormatException e)
        {
            throw new MyExceptions.IncompatibleDataTypeException();
        }

        for (Model m :modelList) {

            if (val.equals( m.get(col)))
                out.add(m);
        }


        return out;
    }

    private <T> List<Model> pWhere(String col, String operator, T key) throws Exception{
        Set<String> fields = fillables.keySet() ;
        List<Model> out = new ArrayList<>();

        String val = switch (getFieldDataType(col))
                {
                    case "int" -> String.valueOf(Integer.parseInt(key.toString()));
                    case "double" -> String.valueOf(Double.parseDouble(key.toString()));
                    default -> key.toString();
                };

        if (!fields.contains(col))
            throw new MyExceptions.ColumnDoesNotExistException();


        for (Model m :modelList) {

            if (compare(operator, getFieldDataType(col), m.get(col), val))
                out.add(m);
        }
        return out;
    }


    /******* Public Methods ********/

    public String get(String fieldName) throws MyExceptions.FieldDoesNotExistException {

        if (fieldName.equals("ID"))
            return String.valueOf(getID());
        try{
            return fillables.get(fieldName).value.toString();
        }catch (NullPointerException e)
        {
            throw new MyExceptions.FieldDoesNotExistException();
        }

    }

    public <T> T get(String fieldName , Class type){
        T out = (T) fillables.get(fieldName).value;
        if(out.getClass().getTypeName().equals(type.getTypeName()))
            return out;
        return null;
    }

    protected void addField(String fieldName, Param param) {
        fillables.put(fieldName,param);
        modified = true;
    }

    public <T> void updateField(String fieldName, String value) throws Exception {
        Param<T> param = fillables.get(fieldName) ;
        if (param == null)
            throw new MyExceptions.FieldDoesNotExistException();

        try {
            if (param.type.getSimpleName().equals("int") && !Helper.isInt(value)){
                throw new MyExceptions.WrongDataFormatException();
            }

            if (param.type.getSimpleName().equals("double") && !Helper.isFloat(value))
                throw new MyExceptions.WrongDataFormatException();
        }catch (MyExceptions.NullDataException e)
        {
            if (param.nullable)
                value = "0";
            else
                throw new MyExceptions.WrongDataFormatException();
        }


        param.value = (T) switch (param.type.getSimpleName()){

            case "int" -> Integer.parseInt(value);
            case "double" -> Double.parseDouble(value);
            default -> value;
        };

        modified = true;
        //todo add a modified event listener
    }

    private String getFieldDataType(String fieldName) throws MyExceptions.FieldDoesNotExistException {
        try{
            return fillables.get(fieldName).type.getSimpleName();
        }catch (NullPointerException e)
        {
            throw new MyExceptions.FieldDoesNotExistException();
        }
    }


    /********* Geters ********/

    public int getID() {
        return ID;
    }

    public int getFillableSize(){
        return fillables.size() + 1;
    }

    /******* Seters *********/


    private void setAccessFile() throws FileNotFoundException {
        this.accessFile = new RandomAccessFile(file,"rws");
    }

    private void setFile() {
        this.file = new File(Path);
    }

    private void setPath(String path) {
        Path = path;
    }

    private void setModelList() {
       this.modelList = new ArrayList<>();
    }

    private  void setModelList(List<Model> modelList){
        this.modelList = modelList;
    }

    private void setID(int ID) {
        this.ID = ID;
    }


    /****** private Helpers ***********/

    private byte checkOperator(String opp) throws Exception{
        if (opp.equals("=") || opp.equals("=="))
            return 0;

        if (opp.equals("!") || opp.equals("!="))
            return -1;

        if (opp.equals("<"))
            return -2;

        if (opp.equals("<="))
            return -3;

        if (opp.equals(">"))
            return 2;

        if (opp.equals(">="))
            return 3;

        throw new MyExceptions.UnknownOperatorException(opp);
    }

    private <T> boolean compareKey(String operator,Comparator<T> cmp , T val1 , T val2) throws Exception{

        boolean out = false ;
         out = switch (checkOperator(operator)){

            case 0      ->      cmp.compare(val1, val2) == 0 ;
            case -1     ->      cmp.compare(val1, val2) != 0;
            case -2     ->      cmp.compare(val1, val2) < 0 ;
            case 2     ->      cmp.compare(val1, val2) > 0 ;
            case -3     ->      cmp.compare(val1, val2) <= 0 ;
            case 3     ->      cmp.compare(val1, val2) >= 0 ;

            default -> throw new MyExceptions.UnknownOperatorException(operator);
        };

        return out;
    }

    private boolean compare(String operator, String type, String val, String key) throws Exception{

        boolean out = false;

        try {
            out = switch (type) {
                case "int" -> compareKey(operator, Integer::compare, Integer.parseInt(val), Integer.parseInt(key));
                case "double" -> compareKey(operator, Double::compare, Double.parseDouble(val), Double.parseDouble(key));
                default -> compareKey(operator, CharSequence::compare, val, key);
            };
        }catch (NumberFormatException e){
         //   System.out.println("type: "+ type +" val: " + String.valueOf(val) + " key: " + key + " operator: " + operator );
            e.printStackTrace();
        }

        return out;
    }

    // todo add save delete update methods
    // todo add hasOne hasMany belongTo methods


    /************/

    protected <T extends Model> T hasOne(Class<T> className, String foreignKey) throws Exception{
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        List<T> out = (List<T>) className.getMethod("Where", String.class, Object.class).invoke(null, foreignKey, this.getID());

        return out.get(0);
    }

    protected <T extends Model> List<T> hasMany(Class<T> className, String foreignKey) throws Exception{
        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        List<T> out = (List<T>) className.getMethod("Where", String.class , Object.class).invoke(null, foreignKey, this.getID());

        return out;
    }

    protected <T extends Model> T belongsTo(Class<T> className, String localKey) throws Exception {

        if (!className.getSuperclass().equals(Model.class))
            throw new MyExceptions.IncompatibleDataHolderException();

        if (!Helper.isInt(this.get(localKey)))
            throw new Exception("this key field need to be an id !");

        int id = Integer.parseInt(this.get(localKey));

        return  (T) className.getMethod("find", int.class).invoke(null, id);

    }


}
