package com.nss.usermanagement.role.service;

import com.nss.usermanagement.role.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Userinfo implements UserDetails {
    private String userEmail;
    private String password;
    private List<GrantedAuthority> authorities;
    public Userinfo(User user, Collection<? extends GrantedAuthority> authorities){
        this.userEmail=user.getEmail();
        this.password=user.getPassword();
        this.authorities= Stream.of(user.getRolePermissionIds().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
        //" "
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        //UserDetails.super.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //UserDetails.super.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
        //UserDetails.super.isEnabled;
    }
}
