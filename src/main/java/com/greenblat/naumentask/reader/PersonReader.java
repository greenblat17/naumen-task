package com.greenblat.naumentask.reader;

import com.greenblat.naumentask.exception.PersonFileException;
import com.greenblat.naumentask.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonReader implements Reader<Person> {

    @Value("${person.reader.file.path}")
    private String path;

    @Value("${person.reader.file.name}")
    private String fileName;

    @Override
    public List<Person> readFile() {
        List<Person> people = new ArrayList<>();

        File file = new File(path + fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] personInfo = line.split("_");
                Person person = getPerson(personInfo);

                people.add(person);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("The data in the file is in the wrong format");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Age is in the wrong format");
        }  catch (IOException e) {
            throw new PersonFileException(String.format("File with name %s not found", path + fileName));
        }

        return people;
    }

    private Person getPerson(String[] personInfo) {
        return Person.builder()
                .name(personInfo[0])
                .age(Integer.parseInt(personInfo[1]))
                .build();
    }
}
