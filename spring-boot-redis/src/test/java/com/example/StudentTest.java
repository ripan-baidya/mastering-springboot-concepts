package com.example;

import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentTest {
    @Autowired
    private ProductService productService;

    @Test
    public void save_student_test() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Ripan");
        productService.saveStudent(student);
    }

    @Test
    public void get_student_test() {
        Student student = productService.getStudentById(1L);
        System.out.println(student.getId() + " " + student.getName());
    }
}