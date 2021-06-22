package it.buniva.strage.repository;

import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Professor findByIdAndEnabledTrueAndDeletedFalse(Long professorId);

    Professor findByUserUserIdAndEnabledTrueAndDeletedFalse(Long userId);

    Professor findByIdAndDeletedFalse(Long professorId);

    Professor findByPersonalDataEmailAndEnabledTrueAndDeletedFalse(String email);

    List<Professor> findAllByEnabledTrueAndDeletedFalse();

    boolean existsByPersonalData(PersonalData personalData);

    Professor findProfessorByPersonalData(PersonalData personalData);

    Professor findByUserUserIdAndDeletedFalse(Long userId);
}
