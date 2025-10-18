package com.example;

import com.example.models.Student;
import com.example.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class StudentTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void findAllStudentsWithGrades() {
        List<Student> students = studentRepository.findAll(); // lazy
        for (Student s : students) {
            System.out.println(s);
        }
    }
}