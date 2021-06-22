package it.buniva.strage.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import it.buniva.strage.constant.SecurityConstant;
import it.buniva.strage.security.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret; // we have to keep it secret, usually it is store on a server


    /**
     * This method create the token from the information user
     * @param userPrincipal
     * @return
     */
    public String generateJwtToken(UserPrincipal userPrincipal) {
        // Claim means all the permission (authorities)
        String[] claims = getClaimsFromUser(userPrincipal);

        return "Bearer " + JWT.create()
                .withIssuer(SecurityConstant.GET_ORGANIZATION_WHO_ISSUE_TOKEN) // The name of the Application or Company
                .withAudience(SecurityConstant.STRAGE_APPLICATION_USER) // Audience are the administration people in this case
                .withIssuedAt(new Date()) // Time of creation of the token
                .withSubject(userPrincipal.getUsername()) // With can pass the username
                .withArrayClaim(SecurityConstant.AUTHORITIES, claims) // Pass the authorities in the claims
                .withExpiresAt( new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME) ) // Expiration time
                .sign(Algorithm.HMAC512(secret.getBytes()));
//                .sign(Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * retrieves authorities of one user from a given token
     * @param token
     * @return
     */
    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Get the authentication of the user once a token is verifier.
     * Once the authentication is retrieves we can process with the request
     * @param username
     * @param authorities
     * @param request
     * @return
     */
    public Authentication getAuthentication(
            String username,
            List<GrantedAuthority> authorities,
            HttpServletRequest request) {

        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(username, null, authorities);

        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return userPasswordAuthToken;
    }

    /**
     * Tell if the token is valid or not returning a boolean
     * @param username
     * @param token
     * @return ture or false
     */
    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    /**
     * Retrieves the subject from the token
     * In this Application Subject content the username.
     * @param token
     * @return the username of the token owner
     */
    public String getSubject(String token) {

        JWTVerifier verifier = getJWTVerifier();


        return verifier.verify(token).getSubject();
    }




    /*
    #######################################################################
    #       HELPER METHOD
    #######################################################################*/

    private boolean isTokenExpired(JWTVerifier verifier, String token){
        Date expiration = verifier.verify(token).getExpiresAt();

        return expiration.before(new Date());
    }

    /**
     * Claims can content the authorities of an user
     * And this method retrieves the claims of an user from a given token.
     * Before we get the token, we have to verifier the token
     * @param token
     * @return
     */
    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token)
                .getClaim(SecurityConstant.AUTHORITIES)
                .asArray(String.class);
    }

    /**
     * Get the verifier of a token
     * Can throw an exception ( JWTVerificationException )
     * @return
     */
    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(SecurityConstant.GET_ORGANIZATION_WHO_ISSUE_TOKEN).build();
        } catch (JWTVerificationException jwtVerificationException) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }

        return verifier;
    }

    /**
     * Generate the claims from the UserPrincipal
     * UserPrincipal is an implementation of UserDetails from Springframework
     * @param user
     * @return
     */
    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();

        for(GrantedAuthority grantedAuthority: user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }

        return authorities.toArray(new String[0]);
    }
}
