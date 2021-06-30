package it.buniva.strage.repository;

import it.buniva.strage.entity.Configuration;
import it.buniva.strage.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    
    Configuration findByConfigurationCodeAndDeletedFalse(String configurationCode);

    Configuration findByConfigurationCodeAndEnabledTrueAndDeletedFalse(String configurationCode);

    List<Configuration> findAllBySubjectSubjectCode(String subjectCode);

    Configuration findByConfigurationCode(String configurationCode);

    Configuration findByConfigurationName(String configurationName);
}
