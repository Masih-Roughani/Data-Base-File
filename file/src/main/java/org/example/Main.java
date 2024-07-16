package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        Admin admin = Admin.getAdmin();
        ObjectOutputStream objectOutputStream = getObjectOutputStream();
        Person person1 = new Person("Masih1", "Roughani1");
        Person person2 = new Person("Masih2", "Roughani2");
        Person person3 = new Person("Masih3", "Roughani3");
        admin.recordInfo(person1, objectOutputStream);
        admin.recordInfo(person2, objectOutputStream);
        admin.recordInfo(person3, objectOutputStream);
        admin.searchInfo();
        admin.deleteInfo(person1);
    }

    private static ObjectOutputStream getObjectOutputStream() throws IOException {
        Person TestPerson = new Person("TestMasih", "TestRoughani");
        ObjectMapper objectMapper = new ObjectMapper();
        try (OutputStream outputStream = new FileOutputStream("src/person.json")) {
            objectMapper.writeValue(outputStream, TestPerson);
        }
//      ----------------------------------------------------------------------------------
        try (CSVWriter writer = new CSVWriter(new FileWriter("src/persons.csv"))) {
            String[] data = { TestPerson.name, TestPerson.familyName };
            writer.writeNext(data);
        }
//      ----------------------------------------------------------------------------------
        FileOutputStream fileOutputStream = new FileOutputStream("src/people.txt", true);
        return new ObjectOutputStream(fileOutputStream);
    }
}

