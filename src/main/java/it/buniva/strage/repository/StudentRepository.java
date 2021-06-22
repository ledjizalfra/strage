package it.buniva.strage.repository;

import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentByPersonalDataAndEnabledTrueAndDeletedFalse(PersonalData personalData);

    boolean existsByPersonalData(PersonalData personalData);

    Student findStudentByPersonalData(PersonalData personalData);

    List<Student> findAllByEnabledTrueAndDeletedFalse();

    Student findByPersonalDataEmailAndEnabledTrueAndDeletedFalse(String email);

    Student findByIdAndDeletedFalse(Long studentId);

    Student findByUserUserIdAndEnabledTrueAndDeletedFalse(Long userId);

    Student findByIdAndEnabledTrueAndDeletedFalse(Long studentId);

    Student findByUserUserIdAndDeletedFalse(Long userId);
}
