package com.company.Controllers;

import com.company.Models.*;

import java.util.*;

public class Tasks {

    public static void task1() throws Exception{
        List<Continent> continents = Continent.All();
        Continent c = null ;
        int max = 0;
        int size = 0;

        for (Continent continent : continents) {
            size = continent.countries().size();
            if (size > max){
                c = continent;
                max = size;
            }
        }

        System.out.println("the continent with the biggest number of countries : " + c.get("Continent"));
    }

    public static List<CarMaker> task2(String cnt){

        List<CarMaker> makers = new ArrayList<>();

        try {
            Continent continent = Continent.Where("Continent", cnt).get(0);
            if (continent != null){
             List<Country> countries = continent.countries();

                for (Country country : countries) {
                    makers.addAll(country.makers());
                }

                System.out.print("these all the manufactorer in " + cnt + " : [");
                for (CarMaker maker : makers) {
                    System.out.print("  " + maker.get("FullName") + ",  ");
                }
                System.out.println("]");
            }
        }catch (Exception e){
            System.err.println("this countinent name does not exist !");
        }

        return makers;
    }

    public static void task3() throws Exception{

        List<Country> countries = Country.All();
        List<CarMaker> makers = new ArrayList<>();
        int count;

        System.out.println("the number of models for every country : [");
        for (Country country : countries) {
            count = 0;
            System.out.print(country.get("CountryName") + " => ");
            makers = country.makers();
            for (CarMaker maker : makers) {
                count += maker.models().size();
            }
            System.out.println(count);
        }
        System.out.println("]");

    }

    public static List<CarMaker> task4(int year) throws Exception{

        System.out.print("all the manufacturers who produced cars before "+ year +" : \n[ ");

        List<CarData> carDataList = CarData.Where("Year", "<", year);       // get all the carData where year < specified year

        if (carDataList.size() == 0){
            System.out.println("]");
            return null;
        }

        Set<String> modelNames = new LinkedHashSet<>();

        List<CarName> carNames = CarName.All();         // get

        for (CarData carData : carDataList) {
            modelNames.add( CarName.find(carData.getID() , carNames).get("Model") );
        }

        List<CarModel> models = new ArrayList<>();

        for (String model : modelNames) {
            models.addAll(CarModel.Where("Model", model));
        }

        Set<Integer> makerIDs = new LinkedHashSet<>();

        for (CarModel model : models) {
            makerIDs.add(Integer.valueOf(model.get("Maker")));
        }

        List<CarMaker> allMakers =  CarMaker.All();
        List<CarMaker> makers = new ArrayList<>();


        for (Integer makerID : makerIDs) {
           CarMaker carMaker = CarMaker.find(makerID, allMakers);
           makers.add(carMaker);
           System.out.print(carMaker.get("FullName") + ", ");
        }
        System.out.println("]");

        return makers;
    }

    public static void task5() throws Exception {
        Map<String, Double> modelHpMap = new HashMap(36);
        Map<String, Double> makerHpMap = new HashMap(36);
        List<CarName> allNames = CarName.All();
        List<CarData> allData = CarData.All();
        List<CarModel> allModels = CarModel.All();
        List<CarMaker> allMakers = CarMaker.All();
        System.out.println("the avg hp by model :");
        Iterator it = allModels.iterator();

        List carNames;
        double hp;
        int sizeNames;
        Iterator it2;
        while(it.hasNext()) {
            CarModel allModel = (CarModel)it.next();
            carNames = CarName.Where("Model", "==", allModel.get("Model"), allNames);
            sizeNames = carNames.size();
            hp = 0.0;

            CarName carName;
            for(it2 = carNames.iterator(); it2.hasNext(); hp += (double)Integer.parseInt(CarData.find(carName.getID(), allData).get("Horsepower"))) {
                carName = (CarName)it2.next();
            }

            hp /= (double)sizeNames;
            if (hp > 0.0) {
                modelHpMap.put(allModel.get("Model"), hp);
            }
        }

        System.out.println(modelHpMap);
        System.out.println("the avg hp by maker :");
        it = allMakers.iterator();

        while(it.hasNext()) {
            CarMaker allMaker = (CarMaker)it.next();
            List<CarModel> models = allMaker.models(allModels);
            sizeNames = 0;
            hp = 0.0;
            it2 = models.iterator();

            while(it2.hasNext()) {
                CarModel allModel = (CarModel)it2.next();
                carNames = CarName.Where("Model", "==", allModel.get("Model"), allNames);
                sizeNames += carNames.size();

                CarName carName;
                for(Iterator var21 = carNames.iterator(); var21.hasNext(); hp += (double)Integer.parseInt(CarData.find(carName.getID(), allData).get("Horsepower"))) {
                    carName = (CarName)var21.next();
                }
            }

            hp /= (double)sizeNames;
            if (hp > 0.0) {
                makerHpMap.put(allMaker.get("FullName"), hp);
            }
        }

        System.out.println(makerHpMap);
    }


}
