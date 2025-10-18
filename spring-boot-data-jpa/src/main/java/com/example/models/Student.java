package com.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "students",
        /**
         * We can make multiple columns unique by specifying them in the @UniqueConstraint annotation. This
         * ensures that each combination of values in the specified columns is unique, preventing duplicate records
         * from being inserted into the table. If an attempt is made to insert a duplicate record, an exception will
         * be thrown.
         */
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"}),

        /**
         * An index is a data structure (like a sorted lookup table or B-tree) maintained by the database to make
         * searches, joins, and filtering faster.
         *
         * Use index on columns that are frequently used in WHERE clauses, JOIN conditions, or ORDER BY clauses.
         *
         * Note: Make sure not to over-index. Because each index costs extra write time + disk space because the
         * DB must update indexes every time you INSERT, UPDATE, or DELETE.
         */
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone_number", columnList = "phone_number"),
                @Index(name = "idx_user_created_at", columnList = "created_at"),
                @Index(name = "idx_user_status", columnList = "student_id, status")
        }
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Size(min = 10, max = 15, message = "Phone number must be between 10–15 digits")
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDateTime dob;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    // ********* Define Relationship *********

    // 1️⃣ ⎯ 1️⃣
    // Student: is the Owning side (holds @JoinColumn() or the foreign key)
    @OneToOne(
            // Persist, merge, remove operations on Student will propagate to Parent
            cascade = CascadeType.ALL,
            // object will be loaded only when accessed; default is EAGER for 1:1,
            // but lazy is often better in production
            fetch = FetchType.LAZY,
            // non-null relationship always exist.
            optional = false
    )
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Parent parent;


    // M ⎯ N (many to many)
    // Student is the Owning Side, should contain @JoinTable()
    @ManyToMany(
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "student_course",
            // the referencedColumnName is optional, but it should match with the DB column name,
            // not the Java field name.
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    )
    private Set<Course> courses = new HashSet<>();


    // 1️⃣ ⎯ M : One to Many
    @OneToMany(
            cascade = CascadeType.ALL, // persist, merge, remove
            fetch = FetchType.LAZY, // lazy loading (recommended)
            orphanRemoval = true, // remove grades not associated anymore
            mappedBy = "student" // Inverse Side
    )
    private Set<Grade> grades = new HashSet<>();
}
