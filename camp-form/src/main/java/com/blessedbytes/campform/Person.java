package com.blessedbytes.campform;

public class Person{
    private int id;
    private String name;

    public Person(){
        this.id = 1;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}