package com.example.ricardogarcia.topfreeapps;

import java.io.Serializable;

/**
 * Represents an application with its name, category, summary and images
 * Created by ricardogarcia on 1/14/16.
 */
public class App implements Serializable {

    String name;
    String category;
    String summary;
    byte[] small;
    byte[] medium;
    byte[] large;

    public App() {
    }

    public App(String name, String category, String summary, byte[] small, byte[] medium, byte[] large) {
        this.name = name;
        this.category = category;
        this.summary = summary;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public byte[] getSmall() {
        return small;
    }

    public void setSmall(byte[] small) {
        this.small = small;
    }

    public byte[] getMedium() {
        return medium;
    }

    public void setMedium(byte[] medium) {
        this.medium = medium;
    }

    public byte[] getLarge() {
        return large;
    }

    public void setLarge(byte[] large) {
        this.large = large;
    }
}
