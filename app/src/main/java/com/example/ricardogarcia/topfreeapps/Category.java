package com.example.ricardogarcia.topfreeapps;

/**
 * Represents a category with its name and logo
 * Created by ricardogarcia on 1/14/16.
 */
public class Category {
    String name;
    byte[] logo;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, byte[] logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
