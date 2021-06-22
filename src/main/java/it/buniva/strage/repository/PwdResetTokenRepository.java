package it.buniva.strage.repository;

import it.buniva.strage.entity.PwdResetToken;
import it.buniva.strage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PwdResetTokenRepository extends JpaRepository<PwdResetToken, Long> {

    PwdResetToken findByUser(User user);

    PwdResetToken findByTokenAndActivatedTrue(String token);
}
