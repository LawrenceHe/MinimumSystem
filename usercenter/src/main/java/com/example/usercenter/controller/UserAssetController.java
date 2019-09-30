package com.example.usercenter.controller;

import com.example.usercenter.dao.UserAssetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/asset")
@Slf4j
public class UserAssetController {

    @Autowired
    private UserAssetMapper userAssetMapper;

    @RequestMapping(method = RequestMethod.POST, value = "getFortuneById.json")
    public String getFortuneById(@Valid @RequestParam Long id) {
        return userAssetMapper.getFortuneById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "getLoanById.json")
    public String getLoanById(@Valid @RequestParam Long id) {
        return userAssetMapper.getLoanById(id);
    }

}
