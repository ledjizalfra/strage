package it.buniva.strage.repository;

import it.buniva.strage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameAndDeletedFalse(String username);

    Optional<User> findByUserIdAndEnabledTrueAndDeletedFalse(Long userId);

    User findUserByUserIdAndDeletedFalse(Long userId);

    List<User> findAllByEnabledTrueAndDeletedFalse();

    boolean existsByUsername(String username);
}
