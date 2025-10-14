package org.astrobrains.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    private String name;

    private String specialization;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "headOfDepartment")
    private Department departmentHead;

    @ManyToMany(mappedBy = "doctors")
    private Set<Department> departments = new HashSet<>();
}
