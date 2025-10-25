package com.example;

import com.example.entity.Student;
import com.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentTest {
    @Autowired
    private StudentService studentService;

    @Test
    public void save_student_test() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Ripan");
        studentService.saveStudent(student);
    }

    @Test
    public void get_student_test() {
        Student student = studentService.getStudentById(1L);
        System.out.println(student.getId() + " " + student.getName());
    }
}