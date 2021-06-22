package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.PwdResetTokenConstant;
import it.buniva.strage.entity.*;
import it.buniva.strage.exception.pwdresettoken.InvalidPwdResetTokenException;
import it.buniva.strage.exception.pwdresettoken.PwdResetTokenExpiredException;
import it.buniva.strage.repository.PwdResetTokenRepository;
import it.buniva.strage.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional(rollbackFor = Exception.class)
public class PwdResetTokenServiceImpl implements PwdResetTokenService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private PwdResetTokenRepository pwdResetTokenRepository;



    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================


    // ============================= CREATE ===============================
    @Override
    public PwdResetToken createPasswordResetToken(User user) {

        // check if already exists a pwdRestToken for this user
        PwdResetToken pwdResetToken = getPwdResetTokenByUser(user);

        if(pwdResetToken == null) {
            // pwdResetToken Not found, then we create a new one
            pwdResetToken = new PwdResetToken(generatePwdResetToken(), user);
        } else {
            pwdResetToken.updatePwdResetToken(generatePwdResetToken());
        }

        return savePasswordResetToken(pwdResetToken);
    }


    // ============================ READ =================================
    @Override
    public PwdResetToken getPwdResetTokenByUser(User user) {
        return pwdResetTokenRepository.findByUser(user);
    }

    @Override
    public PwdResetToken getPwdResetTokenByTokenAndActivatedTrue(String token)
            throws InvalidPwdResetTokenException, PwdResetTokenExpiredException {

        PwdResetToken pwdResetToken = pwdResetTokenRepository.findByTokenAndActivatedTrue(token);

        if(pwdResetToken == null) {
            throw new InvalidPwdResetTokenException(PwdResetTokenConstant.INVALID_TOKEN_MSG);
        }

        if(pwdResetToken.isExpired()) {
            throw new PwdResetTokenExpiredException(PwdResetTokenConstant.TOKEN_EXPIRED_MSG);
        }

        return pwdResetToken;
    }

    // ============================ UPDATE =================================


    // ============================= DELETE ================================

    // ============================== SAVE ==================================
    @Override
    public PwdResetToken savePasswordResetToken(PwdResetToken pwdResetToken) {
        return pwdResetTokenRepository.save(pwdResetToken);
    }

    // ============================== OTHER ==================================




    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private String generatePwdResetToken() {
        return UUID.randomUUID().toString();
    }

}
