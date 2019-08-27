package com.example.app.controller;

import com.example.app.entity.PageInfoReq;
import com.example.app.entity.PageInfoResp;
import com.example.common.entity.CommonRequest;
import com.example.common.entity.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.example.common.exception.SystemErrorType.PAGE_NOT_EXIST;

@RestController
@RequestMapping("/page")
@Api("/page")
@Slf4j
public class PageController {

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "获取页面配置数据", notes = "获取页面配置数据")
    @RequestMapping(method = RequestMethod.POST
            , value = "getPageInfo.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getPageInfo(@Valid @RequestBody CommonRequest<PageInfoReq> req) {
        PageInfoResp resp = new PageInfoResp();

        String resPath = "static/" + req.getData().getPageName() + ".json";
        Resource pageData = new ClassPathResource(resPath);
        try {
            InputStream stream = pageData.getInputStream();
            JsonNode json = objectMapper.readTree(stream);
            resp.setPageInfo(json);
        } catch (IOException e) {
            return Result.fail(PAGE_NOT_EXIST);
        }

        return Result.success(resp);
    }

    private String jsonRead(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }

}