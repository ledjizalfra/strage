package it.buniva.strage.service.implementation;

import it.buniva.strage.repository.PermissionRepository;
import it.buniva.strage.service.AdminService;
import it.buniva.strage.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================


    // ============================== READ ================================


    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== SAVE ==================================


    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================
}
