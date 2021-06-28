package it.buniva.strage.utility;

import it.buniva.strage.constant.PasswordConstant;
import it.buniva.strage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class PasswordUtils {

    /*@Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean passwordMatches(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }*/

    /**
     * GENERATE A RANDOM SECURE PASSWORD
     * @param length must be >=5
     * @return password
     */
    public String generatePassword(Integer length) {
        if(length < 5) length = 5;

        char[] password = new char[length];

        //get the requirements out of the way
        password[0] = PasswordConstant.LOWERCASE[PasswordConstant.rand.nextInt(PasswordConstant.LOWERCASE.length)];
        password[1] = PasswordConstant.UPPERCASE[PasswordConstant.rand.nextInt(PasswordConstant.UPPERCASE.length)];
        password[2] = PasswordConstant.NUMBERS[PasswordConstant.rand.nextInt(PasswordConstant.NUMBERS.length)];
        password[3] = PasswordConstant.SYMBOLS[PasswordConstant.rand.nextInt(PasswordConstant.SYMBOLS.length)];
        password[4] = PasswordConstant.SYMBOLS[PasswordConstant.rand.nextInt(PasswordConstant.SYMBOLS.length)];

        //populate rest of the password with random chars
        for (int i = 5; i < length; i++) {
            password[i] = PasswordConstant.ALL_CHARS[PasswordConstant.rand.nextInt(PasswordConstant.ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = PasswordConstant.rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }

        return new String(password);
    }

    /**
     * VALIDATE THE PASSWORD WITH THIS PATTERN:
     * "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&-+=()])(?=\\S+$).{8,32}$"
     * @param password
     * @return boolean
     */
    public Boolean isPasswordValid(String password) {

        String regex = "^(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[@#$%&-+=()])" +
                "(?=\\S+$).{8,32}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);

        return m.matches();
    }
}
