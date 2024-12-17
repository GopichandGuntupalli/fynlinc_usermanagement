package com.nss.usermanagement.role.service;

import com.nss.usermanagement.role.entity.User;
import com.nss.usermanagement.role.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
//import java.util.List;
//
//import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
//        System.out.println(user+"user details");
////
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                .authorities(user.getRolePermissionIds().split(",")) // You can customize this with roles or permissions.stream()
//
//                .accountExpired(false)
//                .accountLocked(false)
//                .credentialsExpired(false)
//                .disabled(false)
//                .build();
////        Optional<User> userDetail=userRepository.findByEmail(email);
//        System.out.println(userDetail+"full details of user");
//        return userDetail.map(Userinfo::new)
//        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));


     //    Convert User to UserDetails and add roles/authorities
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Convert User to UserDetails and add roles/authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // Assuming rolePermissions field is now a proper ManyToMany relationship
        String[] roles = user.getRolePermissionIds().split(",");
        System.out.println("Roles for user: " + Arrays.toString(roles));
//        return List.of(roles)
//                .stream().map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
        return Stream.of(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }


}

