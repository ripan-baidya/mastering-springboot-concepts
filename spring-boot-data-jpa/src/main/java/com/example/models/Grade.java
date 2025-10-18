package com.example.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private Double score;

    // ********* Define Relationship *********

    // Owning side
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn( // FK in grades table
            name = "student_id",
            referencedColumnName = "student_id"
    )
    private Student student;
}
