package com.example.repositories;

import com.example.models.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.grades")
    List<Student> findAllWithGrades();

    @EntityGraph(attributePaths = {"grades"})
    List<Student> findAll();
}

