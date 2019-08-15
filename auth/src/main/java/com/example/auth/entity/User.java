package com.example.auth.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private Set<GrantedAuthority> authorities;
}
