package com.example.awua;

public class DataClass {

    private String soundURL;
    private String name;

    public DataClass(){

    }

    public DataClass(String soundURL, String name){
        this.soundURL = soundURL;
        this.name = name;
    }
    public String getSoundURL() {
        return soundURL;
    }

    public void setSoundURL(String soundURL) {
        this.soundURL = soundURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
