package org.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Person implements Serializable {
    public String name;
    public String familyName;
    public int ID;
    private static int IDMaker = 1;
    public static ArrayList<Person> people = new ArrayList<>();

    public Person(String name, String familyName) {
        this.name = name;
        this.familyName = familyName;
        this.ID = IDMaker++;
        people.add(this);
    }

    public String showInfo() {
        return "Name : " + name + "\r\n" + "Lastname : " + familyName + "ID : " + ID;
    }
}
