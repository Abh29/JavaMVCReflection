package com.company.Models;

import com.company.tools.DataLinker;
import com.company.tools.Helper;
import com.company.tools.MyExceptions;
import com.company.tools.Param;
import com.company.tools.Parser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Model {
    private String Path;
    private File file;
    private RandomAccessFile accessFile;
    private List<Model> modelList;
    protected Map<String, Param> fillables = new HashMap();
    private int ID;
    private boolean modified;

    protected static <T extends Model> T createInstance(Class<T> className, int Id) throws Exception {
        Model out = new Model();
        out.setID(Id);
        out.setPath(DataLinker.getFilePath(className.getSimpleName()));
        out.setFile();
        out.setAccessFile();
        out.setModelList();
        return (T) className.getMethod("createInstance", Model.class).invoke((Object)null, out);
    }

    protected static Model createInstance(Class className) throws Exception {
        return createInstance(className, DataLinker.getNewId(className.getSimpleName()));
    }

    protected Model() {
    }

    protected void setModel(Model m) {
        this.Path = m.Path;
        this.file = m.file;
        this.accessFile = m.accessFile;
        this.modelList = m.modelList;
        this.fillables = m.fillables;
        this.ID = m.ID;
        this.modified = m.modified;
    }

    protected static <T extends Model> List<T> all(Class<T> className, Parser parser) throws IOException, Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            Model model = (Model)className.getMethod("newInstance").invoke((Object)null);
            return model.pAll(parser, className);
        }
    }

    protected static <E, T extends Model> List<T> where(Class<T> className, List<T> modelList, String col, E key) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            Model model = (Model)className.getMethod("newInstance").invoke((Object)null);
            model.setModelList((List<Model>) modelList);
            return (List<T>) model.pWhere(col, key);
        }
    }

    protected static <E, T extends Model> List<T> where(Class<T> className, List<T> modelList, String col, String operator, E key) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            Model model = (Model)className.getMethod("newInstance").invoke((Object)null);
            model.setModelList((List<Model>) modelList);
            return (List<T>) model.pWhere(col, operator, key);
        }
    }

    protected static <T extends Model> T find(Class<T> className, List<T> modelList, int id) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            Model model = (Model)className.getMethod("newInstance").invoke((Object)null);
            model.setModelList((List<Model>) modelList);
            return (T) className.getMethod("createInstance", Model.class).invoke((Object)null, model.pFind(id));
        }
    }

    private <T extends Model> List<T> pAll(Parser parser, Class<T> className) throws IOException, Exception {
        this.accessFile.seek(0L);
        this.accessFile.readLine();
        String line = "";

        while((line = this.accessFile.readLine()) != null) {
            this.modelList.add(parser.parse(line, className));
        }

        return (List<T>) this.modelList;
    }

    private Model pFind(int id) {
        Iterator var2 = this.modelList.iterator();

        Model m;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            m = (Model)var2.next();
        } while(m.ID != id);

        return m;
    }

    private <T> List<Model> pWhere(String col, T key) throws Exception {
        Set<String> fields = this.fillables.keySet();
        List<Model> out = new ArrayList();
        if (!fields.contains(col)) {
            throw new MyExceptions.ColumnDoesNotExistException();
        } else {
            String val;
            try {
                String var10000;
                if (key != null && !key.equals("null")) {
                    switch (this.getFieldDataType(col)) {
                        case "int":
                            var10000 = String.valueOf(Integer.parseInt(key.toString()));
                            break;
                        case "double":
                            var10000 = String.valueOf(Double.parseDouble(key.toString()));
                            break;
                        default:
                            var10000 = key.toString();
                    }

                    val = var10000;
                } else {
                    switch (this.getFieldDataType(col)) {
                        case "int":
                            var10000 = "0";
                            break;
                        case "double":
                            var10000 = "0.0";
                            break;
                        default:
                            var10000 = "null";
                    }

                    val = var10000;
                }
            } catch (NumberFormatException var8) {
                throw new MyExceptions.IncompatibleDataTypeException();
            }

            Iterator var9 = this.modelList.iterator();

            while(var9.hasNext()) {
                Model m = (Model)var9.next();
                if (val.equals(m.get(col))) {
                    out.add(m);
                }
            }

            return out;
        }
    }

    private <T> List<Model> pWhere(String col, String operator, T key) throws Exception {
        Set<String> fields = this.fillables.keySet();
        List<Model> out = new ArrayList();
        String var10000;
        switch (this.getFieldDataType(col)) {
            case "int":
                var10000 = String.valueOf(Integer.parseInt(key.toString()));
                break;
            case "double":
                var10000 = String.valueOf(Double.parseDouble(key.toString()));
                break;
            default:
                var10000 = key.toString();
        }

        String val = var10000;
        if (!fields.contains(col)) {
            throw new MyExceptions.ColumnDoesNotExistException();
        } else {
            Iterator var9 = this.modelList.iterator();

            while(var9.hasNext()) {
                Model m = (Model)var9.next();
                if (this.compare(operator, this.getFieldDataType(col), m.get(col), val)) {
                    out.add(m);
                }
            }

            return out;
        }
    }

    public String get(String fieldName) throws MyExceptions.FieldDoesNotExistException {
        if (fieldName.equals("ID")) {
            return String.valueOf(this.getID());
        } else {
            try {
                return ((Param)this.fillables.get(fieldName)).value.toString();
            } catch (NullPointerException var3) {
                throw new MyExceptions.FieldDoesNotExistException();
            }
        }
    }

    public <T> T get(String fieldName, Class type) {
        T out = (T) ((Param)this.fillables.get(fieldName)).value;
        return out.getClass().getTypeName().equals(type.getTypeName()) ? out : null;
    }

    protected void addField(String fieldName, Param param) {
        this.fillables.put(fieldName, param);
        this.modified = true;
    }

    public <T> void updateField(String fieldName, String value) throws Exception {
        Param<T> param = (Param)this.fillables.get(fieldName);
        if (param == null) {
            throw new MyExceptions.FieldDoesNotExistException();
        } else {
            try {
                if (param.type.getSimpleName().equals("int") && !Helper.isInt(value)) {
                    throw new MyExceptions.WrongDataFormatException();
                }

                if (param.type.getSimpleName().equals("double") && !Helper.isFloat(value)) {
                    throw new MyExceptions.WrongDataFormatException();
                }
            } catch (MyExceptions.NullDataException var6) {
                if (!param.nullable) {
                    throw new MyExceptions.WrongDataFormatException();
                }

                value = "0";
            }

            Object var10001;
            switch (param.type.getSimpleName()) {
                case "int":
                    var10001 = Integer.parseInt(value);
                    break;
                case "double":
                    var10001 = Double.parseDouble(value);
                    break;
                default:
                    var10001 = value;
            }

            param.value = (T) var10001;
            this.modified = true;
        }
    }

    private String getFieldDataType(String fieldName) throws MyExceptions.FieldDoesNotExistException {
        try {
            return ((Param)this.fillables.get(fieldName)).type.getSimpleName();
        } catch (NullPointerException var3) {
            throw new MyExceptions.FieldDoesNotExistException();
        }
    }

    public int getID() {
        return this.ID;
    }

    public int getFillableSize() {
        return this.fillables.size() + 1;
    }

    private void setAccessFile() throws FileNotFoundException {
        this.accessFile = new RandomAccessFile(this.file, "rws");
    }

    private void setFile() {
        this.file = new File(this.Path);
    }

    private void setPath(String path) {
        this.Path = path;
    }

    private void setModelList() {
        this.modelList = new ArrayList();
    }

    private void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }

    private void setID(int ID) {
        this.ID = ID;
    }

    private byte checkOperator(String opp) throws Exception {
        if (!opp.equals("=") && !opp.equals("==")) {
            if (!opp.equals("!") && !opp.equals("!=")) {
                if (opp.equals("<")) {
                    return -2;
                } else if (opp.equals("<=")) {
                    return -3;
                } else if (opp.equals(">")) {
                    return 2;
                } else if (opp.equals(">=")) {
                    return 3;
                } else {
                    throw new MyExceptions.UnknownOperatorException(opp);
                }
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    private <T> boolean compareKey(String operator, Comparator<T> cmp, T val1, T val2) throws Exception {
        boolean out = false;
        boolean var10000;
        switch (this.checkOperator(operator)) {
            case -3:
                var10000 = cmp.compare(val1, val2) <= 0;
                break;
            case -2:
                var10000 = cmp.compare(val1, val2) < 0;
                break;
            case -1:
                var10000 = cmp.compare(val1, val2) != 0;
                break;
            case 0:
                var10000 = cmp.compare(val1, val2) == 0;
                break;
            case 1:
            default:
                throw new MyExceptions.UnknownOperatorException(operator);
            case 2:
                var10000 = cmp.compare(val1, val2) > 0;
                break;
            case 3:
                var10000 = cmp.compare(val1, val2) >= 0;
        }

        out = var10000;
        return out;
    }

    private boolean compare(String operator, String type, String val, String key) throws Exception {
        boolean out = false;

        try {
            boolean var10000;
            switch (type) {
                case "int":
                    var10000 = this.compareKey(operator, Integer::compare, Integer.parseInt(val), Integer.parseInt(key));
                    break;
                case "double":
                    var10000 = this.compareKey(operator, Double::compare, Double.parseDouble(val), Double.parseDouble(key));
                    break;
                default:
                    var10000 = this.compareKey(operator, CharSequence::compare, val, key);
            }

            out = var10000;
        } catch (NumberFormatException var8) {
            var8.printStackTrace();
        }

        return out;
    }

    protected <T extends Model> T hasOne(Class<T> className, String foreignKey) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            List<T> out = (List)className.getMethod("Where", String.class, Object.class).invoke((Object)null, foreignKey, this.getID());
            return (T) out.get(0);
        }
    }

    protected <T extends Model> List<T> hasMany(Class<T> className, String foreignKey) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            List<T> out = (List)className.getMethod("Where", String.class, Object.class).invoke((Object)null, foreignKey, this.getID());
            return out;
        }
    }

    protected <T extends Model> List<T> hasMany(Class<T> className, String foreignKey, List<T> dataList) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else {
            List<T> out = (List)className.getMethod("Where", String.class, String.class, Object.class, List.class).invoke((Object)null, foreignKey, "=", this.getID(), dataList);
            return out;
        }
    }

    protected <T extends Model> T belongsTo(Class<T> className, String localKey) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else if (!Helper.isInt(this.get(localKey))) {
            throw new Exception("this key field need to be an id !");
        } else {
            int id = Integer.parseInt(this.get(localKey));
            return (T) className.getMethod("find", Integer.TYPE).invoke((Object)null, id);
        }
    }

    protected <T extends Model> T belongsTo(Class<T> className, String localKey, List<T> dataList) throws Exception {
        if (!className.getSuperclass().equals(Model.class)) {
            throw new MyExceptions.IncompatibleDataHolderException();
        } else if (!Helper.isInt(this.get(localKey))) {
            throw new Exception("this key field need to be an id !");
        } else {
            int id = Integer.parseInt(this.get(localKey));
            return (T) className.getMethod("find", Integer.TYPE, List.class).invoke((Object)null, id, dataList);
        }
    }
}
