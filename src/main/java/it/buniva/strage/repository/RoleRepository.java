package it.buniva.strage.repository;

import it.buniva.strage.entity.Role;
import it.buniva.strage.enumaration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(RoleName roleName);
}
