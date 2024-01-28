package com.deepak.userservice.security;

import com.deepak.userservice.models.Role;
import com.deepak.userservice.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    public CustomUserDetails() {
    }

    public CustomUserDetails(User user){
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.active = !user.isDeleted();
        this.authorities = new ArrayList<>();
        for(Role currentRole : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(currentRole));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
