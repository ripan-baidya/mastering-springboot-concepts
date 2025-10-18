package com.example.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fatherName;
    private String motherName;

    // ********* Define Relationship *********

    // Inverse side contains the mappedBy attribute. This contains no FK.
    @OneToOne(mappedBy = "parent")
    private Student student;
}
