package it.buniva.strage.repository;

import it.buniva.strage.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByIdAndEnabledTrueAndDeletedFalse(Long id);

    Subject findBySubjectCodeAndEnabledTrueAndDeletedFalse(String subjectCode);

    Subject findBySubjectCodeAndDeletedFalse(String subjectCode);

    Subject findBySubjectCode(String subjectCode);

    List<Subject> findAllByDeletedFalseAndEnabledTrue();

    Subject findByIdAndDeletedFalse(Long subjectId);
}
