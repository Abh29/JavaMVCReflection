package com.company.tools;

import java.lang.reflect.Field;

public final class MyExceptions {

    private MyExceptions(){};

    public static class CorruptedDataException extends Exception
    {
        public CorruptedDataException(){
            super("Corrupted Data !");
        }

    }

    public static class WrongDataFormatException extends Exception
    {
        public WrongDataFormatException(){
            super("Data does not correspond to Model fields !");
        }
    }

      public static class IncompatibleDataHolderException extends Exception
    {
        public IncompatibleDataHolderException(){
            super("parameter Class should be a subClass of Model !");
        }
    }

    public static class ModelCreateException extends Exception
    {
        public ModelCreateException(){
            super("Cannot Create a new Model !");
        }
    }

    public static class ColumnDoesNotExistException extends Exception
    {
        public ColumnDoesNotExistException(){
            super("Column does not exist !");
        }
    }

    public static class ColumnAccessException extends Exception
    {
        public ColumnAccessException(){
            super("Column can not be accessed !");
        }
    }

    public static class FieldDoesNotExistException extends Exception
    {
        public FieldDoesNotExistException(){
            super("FieldName Does Not Exist !");
        }
    }

    public static class NullDataException extends Exception
    {
        public NullDataException(){
            super("This value is null!");
        }
    }

    public static class UnknownOperatorException extends Exception {
        public UnknownOperatorException(){
            super("This Comparision operator is undefined ! it has to be one of < \"=\",\"==\", \"!=\", \"!\", \"<\", \"<=\", \">\", \">=\" > ");
        }

        public UnknownOperatorException(String opperator)
        {
            super("Unknow Comparator!"  + opperator + " . it has to be one of < \"=\",\"==\", \"!=\", \"!\", \"<\", \"<=\", \">\", \">=\" > ");
        }
    }

    public static class IncompatibleDataTypeException extends Exception {

        public IncompatibleDataTypeException(){
            super("Incompatible Data Type !");
        }
    }

}
