package it.buniva.strage.service;

import it.buniva.strage.entity.Role;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.exception.role.RoleNotFoundException;

public interface RoleService {


    // ============================= CREATE ===============================


    // ============================== READ ================================
    Role getRoleByRoleName(RoleName roleNameFromString) throws RoleNotFoundException;

    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== SAVE ==================================


    // ============================== OTHER ==================================

}
