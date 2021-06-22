package it.buniva.strage.repository;

import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByIdAndEnabledTrueAndDeletedFalse(Long adminId);
    
    Admin findByUserUserIdAndEnabledTrueAndDeletedFalse(Long userId);

    Admin findByIdAndDeletedFalse(Long adminId);

    Admin findByPersonalDataEmailAndEnabledTrueAndDeletedFalse(String email);

    List<Admin> findAllByEnabledTrueAndDeletedFalse();

    boolean existsByPersonalData(PersonalData personalData);

    Admin findAdminByPersonalData(PersonalData personalData);

    Admin findByUserUserIdAndDeletedFalse(Long userId);
}
