package com.aceper13.research.rules.jexl;

public class Person {
    private String name;
    private int age;
    private boolean isAdult;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {}

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int age() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }
}
