package it.buniva.strage.security;


import it.buniva.strage.entity.User;
import it.buniva.strage.enumaration.RoleName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleNameFromUser().getGrantedAuthorities();
    }

    /**
     * Get the RoleName ( ADMIN, PROFESSOR, STUDENT ) from a given user
     * @return
     */
    private RoleName getRoleNameFromUser() {
        return this.user.getRole().getRoleName();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true ;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
}
