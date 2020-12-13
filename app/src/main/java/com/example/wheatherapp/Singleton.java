package com.example.wheatherapp;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private static Singleton INSTANCE = null;
    public final static List<String> dataSearch = new ArrayList<>();



    Singleton() {};

    public static  synchronized Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();

        }

        return(INSTANCE);
    }

    public static void Data (String city) {
        dataSearch.add(city);
    }

    public static List<String> getDataSearch() {
        return dataSearch;
    }


}
