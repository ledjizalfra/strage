package it.buniva.strage.repository;

import it.buniva.strage.entity.Argument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArgumentRepository extends JpaRepository<Argument, Long> {
    Argument findByIdAndEnabledTrueAndDeletedFalse(Long id);

    Argument findByArgumentCodeAndEnabledTrueAndDeletedFalse(String argumentCode);

    Argument findByArgumentName(String argumentName);

    Argument findByArgumentCode(String argumentCode);

    Argument findByArgumentCodeAndDeletedFalse(String argumentCode);

    List<Argument> findAllBySubjectAndEnabledTrueAndDeletedFalse(String subjectCode);

    List<Argument> findAllByEnabledTrueAndDeletedFalse();

    Argument findByIdAndDeletedFalse(Long argumentId);
}
