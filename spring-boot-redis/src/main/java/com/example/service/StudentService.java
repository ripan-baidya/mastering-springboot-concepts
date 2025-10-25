package com.example.service;

import com.example.entity.Student;
import com.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // save student
    public void saveStudent(Student student) {
        studentRepository.save(student);
        System.out.println("student saved successfully");
    }

    // get student by id
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // delete student by id
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    // update student
    public void updateStudent(Student student) {
        studentRepository.save(student);
    }
}
