package it.buniva.strage.controller;

import it.buniva.strage.exception.role.RoleExceptionHandling;
import it.buniva.strage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController extends RoleExceptionHandling {

    @Autowired
    private RoleService roleService;


    // ============================= CREATE ===============================


    // ============================== READ ================================


    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
