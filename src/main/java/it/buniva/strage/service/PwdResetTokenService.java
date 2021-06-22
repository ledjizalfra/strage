package it.buniva.strage.service;

import it.buniva.strage.entity.PwdResetToken;
import it.buniva.strage.entity.User;
import it.buniva.strage.exception.pwdresettoken.InvalidPwdResetTokenException;
import it.buniva.strage.exception.pwdresettoken.PwdResetTokenExpiredException;

public interface PwdResetTokenService {

    // ============================= CREATE ===============================
    PwdResetToken createPasswordResetToken(User user);

    // ============================== READ ================================
    PwdResetToken getPwdResetTokenByUser(User user);

    PwdResetToken getPwdResetTokenByTokenAndActivatedTrue(String token) throws InvalidPwdResetTokenException, PwdResetTokenExpiredException;

    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== SAVE ==================================
    PwdResetToken savePasswordResetToken(PwdResetToken pwdResetToken);



    // ============================== OTHER ==================================

}
