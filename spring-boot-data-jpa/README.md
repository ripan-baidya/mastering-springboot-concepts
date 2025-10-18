## Entity Relationship Mapping

### Entities

1. Student
2. Parent
3. Course

---
### Relationship

1. Student ⎯ Parent [1 : 1]
2. Student ⎯ Course [M : N]
3. Student ⎯ Grade [1 : M]
4. Grade ⎯ Student [M : 1]

--- 

### 1. One to One

```markdown
student --- parent
```

### Our Example
| Concept              | Explanation                                                                                                         |
| -------------------- | ------------------------------------------------------------------------------------------------------------------- |
| **Owning side**      | `Student` is owning side (holds `@JoinColumn`)                                                                      |
| **Inverse side**     | `Parent` uses `mappedBy` → no FK column in `Parent` table                                                           |
| **CascadeType.ALL**  | Persist, merge, remove operations on `Student` will propagate to `Parent`                                           |
| **FetchType.LAZY**   | Parent object will be loaded only when accessed; default is `EAGER` for 1:1, but lazy is often better in production |
| **optional = false** | Student must have a Parent (column `parent_id` NOT NULL)                                                            |
| **JoinColumn**       | Specifies the foreign key column name in `Student` table                                                            |

### Best Practices
- Owning side should be the table where the FK is stored. we use `@JoinColumn` to define the FK.
- Inverse side contains the mappedBy attribute. This contains no FK.
- Lazy fetching is better in production to avoid unnecessary joins.
- Use optional=false if the association is mandatory.
- Carefully choose cascade types. For example:
    - CascadeType.ALL → parent automatically persisted/removed.
    - CascadeType.PERSIST → only when inserting.
    - Avoid REMOVE unless deleting student should delete parent.


### 2. Many to Many

```markdown
student --- course
```
### Our Example

| Concept                         | Explanation                                                                                                                                   |
| ------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **Owning side**                 | `Student` is the owning side (has `@JoinTable`)                                                                                               |
| **Inverse side**                | `Course` uses `mappedBy="courses"`                                                                                                            |
| **Join table**                  | `student_courses` stores the association (`student_id`, `course_id`)                                                                          |
| **CascadeType.PERSIST & MERGE** | Adding/removing courses from student will persist/merge changes automatically; do **not use REMOVE** unless you really want cascading deletes |
| **FetchType.LAZY**              | Many-to-many should **almost always** be lazy to avoid huge joins in production                                                               |
| **Set<> vs List<>**             | Set avoids duplicates; List can be used if order matters                                                                                      |

### Best Practices

1. Owning side matters: Only the owning side can update the join table. Use mappedBy on inverse side.
2. Lazy fetching is critical in M:N to avoid N+1 query problem.
3. Cascade carefully: Usually only PERSIST and MERGE are safe. REMOVE may delete shared entities unintentionally.
4. Use Set for collections to avoid duplicates automatically. 
5. Version-control join table in Flyway if using migrations.

### 3. Ont to Many

```markdown
student --- grade
```

### Our Example

| Concept                | Explanation                                                                  |
| ---------------------- | ---------------------------------------------------------------------------- |
| **Owning side**        | `Grade` is owning side → holds the FK `student_id`                           |
| **Inverse side**       | `Student` uses `mappedBy="student"`                                          |
| **CascadeType.ALL**    | Persisting or removing Student cascades to Grades                            |
| **orphanRemoval=true** | If a Grade is removed from `student.getGrades()`, it will be deleted from DB |
| **FetchType.LAZY**     | Recommended for collections to avoid loading all grades unnecessarily        |
| **optional=false**     | Ensures a Grade must always belong to a Student                              |

### Best Practices

1. Use Set<> for the collection to avoid duplicates.
2. Always make the many side (Grade) the `owning side`.
3. Use LAZY fetching for collections in production.
4. Use orphanRemoval=true if removing child entities should delete them automatically.
5. Use cascade carefully:
    - ALL is okay if deleting a student should delete all their grades.
    - Otherwise, use PERSIST or MERGE only.

### 4. Many to One

```markdown
grade - student 
```

| Concept                | Explanation                                                                |
| ---------------------- | -------------------------------------------------------------------------- |
| **Owning side**        | `Grade` (Many side) → holds the foreign key `student_id`                   |
| **Inverse side**       | `Student` (One side) → uses `mappedBy="student"`                           |
| **FetchType.LAZY**     | Recommended for `@ManyToOne` and `@OneToMany` to avoid N+1 queries         |
| **optional=false**     | Ensures that each Grade must have a Student                                |
| **CascadeType.ALL**    | Allows operations on Student to cascade to Grades (persist, merge, remove) |
| **orphanRemoval=true** | If a Grade is removed from `grades` set, it will be deleted from DB        |

### Best Practices

1. Owning side is always the Many side (Grade).
2. Lazy fetching is critical in production to avoid unnecessary joins.
3. Cascade carefully: cascading from the many side is usually not required; cascade is often defined on the one side (Student) only.
4. Optional=false is good if a Grade cannot exist without a Student.
5. Collections on the one side should usually be Set to avoid duplicates.