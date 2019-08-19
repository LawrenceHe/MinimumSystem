package com.example.auth.provider;

import com.example.common.entity.Role;
import com.example.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "user-center")
public interface UserCenterProvider {

    @PostMapping(value = "/user/getUserByUsername.json")
    User getUserByUsername(@RequestParam("username") String username);

    @PostMapping(value = "/user/getRolesById.json")
    Set<Role> queryRolesByUserId(@RequestParam("id") Long userId);

}
