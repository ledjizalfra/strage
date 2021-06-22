package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.RoleConstant;
import it.buniva.strage.entity.Role;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.repository.RoleRepository;
import it.buniva.strage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================


    // ============================== READ ================================
    @Override
    public Role getRoleByRoleName(RoleName roleName) throws RoleNotFoundException {
        Role roleByName = roleRepository.findRoleByRoleName(roleName);
        if (roleByName == null) {
            throw new RoleNotFoundException(
                    String.format(RoleConstant.ROLE_NOT_FOUND_NAME_MSG, roleName));
        }
        return roleByName;
    }

    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== SAVE ==================================


    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================
}
