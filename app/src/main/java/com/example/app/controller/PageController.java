package com.example.app.controller;

import com.example.app.entity.PageInfoReq;
import com.example.app.entity.PageInfoResp;
import com.example.common.entity.CommonRequest;
import com.example.common.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

@RestController
@RequestMapping("/page")
@Api("/page")
@Slf4j
public class PageController {

    @Value(value="classpath:static/fortune.json")
    Resource fortuneData;

    @Value(value="classpath:static/home.json")
    Resource homeData;

    @Value(value="classpath:static/mine.json")
    Resource mineData;

    @ApiOperation(value = "获取页面配置数据", notes = "获取页面配置数据")
    @RequestMapping(method = RequestMethod.POST
            , value = "getPageInfo.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getPageInfo(@Valid @RequestBody CommonRequest<PageInfoReq> req) {
        String pageName = req.getData().getPageName();
        PageInfoResp resp = new PageInfoResp();

        if (Objects.equals(pageName, "fortune")) {
            try {
                File file = fortuneData.getFile();
                String json = this.jsonRead(file);
                resp.setPageInfo(json);
            } catch (IOException e) {
                return Result.fail();
            }

            return Result.success(resp);
        } else if (Objects.equals(pageName, "home")) {
            try {
                File file = homeData.getFile();
                String json = this.jsonRead(file);
                resp.setPageInfo(json);
            } catch (IOException e) {
                return Result.fail();
            }

            return Result.success(resp);
        } if (Objects.equals(pageName, "mine")) {
            try {
                File file = mineData.getFile();
                String json = this.jsonRead(file);
                resp.setPageInfo(json);
            } catch (IOException e) {
                return Result.fail();
            }

            return Result.success(resp);
        }

        return Result.fail();
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