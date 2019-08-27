package com.example.auth.service;

import com.example.common.entity.Role;
import com.example.common.entity.User;
import com.example.auth.provider.UserCenterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service(value = "userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCenterProvider userCenterProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userCenterProvider.getUserByUsername(username);
        String verifyCode = stringRedisTemplate.opsForValue().get(username);
        stringRedisTemplate.delete(username);
        log.info("Read Verify Code : " + verifyCode);
        String password = Optional.ofNullable(passwordEncoder.encode(verifyCode)).orElse("");
        user.setPassword(password);
        user.setAuthorities(this.obtainGrantedAuthorities(user));
        return user;
    }

    /**
     * 获得登录者所有角色的权限集合.
     *
     * @param user
     * @return Set<GrantedAuthority>
     */
    private Set<GrantedAuthority> obtainGrantedAuthorities(User user) {
        Set<Role> roles = userCenterProvider.queryRolesByUserId(user.getId());
        log.info("user:{},roles:{}", user.getUsername(), roles);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toSet());
    }
}
