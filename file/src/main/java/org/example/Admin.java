package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Admin {
    private static Admin admin = new Admin("1274432545");
    private String password;

    private Admin(String password) {
        this.password = password;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public void recordInfo(Person person, ObjectOutputStream objectOutputStream) throws Exception {
        objectOutputStream.writeObject(person);
    }

    public void searchInfo() throws Exception {
        ArrayList<Person> people = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream("src/people.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        people.add((Person) objectInputStream.readObject());
        people.add((Person) objectInputStream.readObject());
        people.add((Person) objectInputStream.readObject());
        List<Person> sortedPeople = people.stream().sorted(Comparator.comparingInt(a -> a.ID)).toList();
        for (Person person : sortedPeople) {
            System.out.println(person.name);
        }
    }

    public void deleteInfo(Person person) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("src/people.txt", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        Person.people.remove(person);
        for (int i = 0; i < Person.people.size(); i++) {
            System.out.println(Person.people.get(i).name);
            objectOutputStream.writeObject(Person.people.get(i));
        }
    }
}
