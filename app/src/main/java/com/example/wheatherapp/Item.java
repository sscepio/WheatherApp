package com.example.wheatherapp;

import java.util.Objects;

public class Item {
   private String City;

    public Item(String city) {
        City = city;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(City, item.City);
    }

    @Override
    public int hashCode() {
        return Objects.hash(City);
    }
}
