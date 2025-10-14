package org.astrobrains.repository;

import jakarta.transaction.Transactional;
import org.astrobrains.dto.PatientNameEmail;
import org.astrobrains.dto.PatientNameGender;
import org.astrobrains.model.Patient;
import org.astrobrains.enums.BloodGroup;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // ------------ Jpa Query Methods -------------

    // find patient by email
    Patient findByEmail(String email);

    // find patient by full name or email
    List<Patient> findByFullNameOrEmail(String fullName, String email);

    // find patients who were born between two specific dates
    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    // print all the names starts with specific letter
    List<Patient> findByFullNameIgnoreCaseStartingWith(String prefix);

    // print all the patients by gender and sort them based on their id in desc
    List<Patient> findByGenderOrderByIdDesc(String gender);


    // ------------ Custom/ NativeQuery -------------

    // JPQL - find patients by blood group
    @Query(value = "select p from Patient p where p.bloodGroup = ?1", nativeQuery = false)
    List<Patient> findByBloodGroup(@Param("bloodGroup") BloodGroup bloodGroup);

    // JPQL - find patients who were born after specific date
    @Query("select p from Patient p where p.birthDate > :birthDate")
    List<Patient> findPatientBornAfter(@Param("birthDate") LocalDate birthDate);

    // Native - find all patients
    @Query(value = "select * from patients", nativeQuery = true)
    List<Patient> findAllPatients();

    // Native - count patients by blood group
    @Query(value = "select p.blood_group, count(*) from patients p group by p.blood_group", nativeQuery = true)
    List<Object[]> countPatientByBloodGroup();

    // ------------- Update / Delete ------------

    /**
     * Jpa doesn't support Insert operation directly, but we can use Native Sql query to insert a Record. but
     * this is not recommended we should use 'save()' method provide by Jpa.
     * Jpa only supports 'SELECT, UPDATE and 'DELETE' operations.
     * while using 'update' and 'delete' the return type should 'int' as it represent the number of row affected.
     * while performing 'update' and 'delete' operation we use Annotations - @Modifying and @Transactional.
     * as we are using 'update' and 'delete' operation we need to make sure that our transaction is open.
     * @Transactional annotation is used to make sure that our transaction is open.
     */
    @Transactional
    @Modifying
    @Query("update Patient p set p.fullName = :fullName where p.id = :id")
    int updatePatientName(@Param("id") Long id, @Param("fullName") String fullName);
    // Note: @Param is only used for named params, but for positional params we don't need to mention @Param

    @Transactional
    @Modifying
    @Query(value = "delete from patients p where p.id = ?1", nativeQuery = true)
    int deletePatientById(Long id);

    // ------------ Projection --------------
    /**
     * Projection is a way to retrieve only a subset of entity attributes instead of fetching the entire entity.
     * This helps improve performance (less data from the database, less memory usage) and also keeps APIs cleaner
     * when you don’t need the full object.
     * There are several ways to achieve projection in Spring Data JPA.
     * 1. Interface based
     * 2. Dto based (Using Constructor)
     * 3. Dynamic(Open()
     */

    // Interface based projection
    @Query("select p.fullName as fullName, p.email as email from Patient p")
    List<PatientNameEmail> findPatientNameEmail();

    // Dto Based
    @Query("select new org.astrobrains.dto.PatientNameGender(p.fullName, p.gender) from Patient p")
    List<PatientNameGender> findPatientNameGender();

    /**
     * Dynamic projection
     *
     * <T> List<T> findByAgeGreaterThan(int age, Class<T> type);
     *
     * List<PatientView> patientViews = patientRepository.findByAgeGreaterThan(20, PatientView.class);
     * List<PatientDTO> patientDTOs = patientRepository.findByAgeGreaterThan(20, PatientDTO.class);
     */


    // --------- Sorting & Pagination ------------

    /**
     * APIs taking Sort, Pageable and Limit expect non-null values to be handed into methods.
     * If you do not want to apply any sorting or pagination, use Sort.unsorted(),
     * Pageable.unpaged() and Limit.unlimited()
     */
//    List<Patient> findByFullNameIgnoreCaseContaining(String fullName, Sort sort);
    Page<Patient> findAll(Pageable pageable);

//    Slice<Patient>findByFullName(String fullName, Pageable pageable, Sort sort);
//    Window<Patient> findTop5ByFullName(String fullName, ScrollPosition position, Sort sort);

}
