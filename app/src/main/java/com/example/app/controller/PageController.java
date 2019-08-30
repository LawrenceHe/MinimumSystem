package com.example.app.controller;

import com.example.app.entity.PageInfoReq;
import com.example.app.entity.PageInfoResp;
import com.example.app.exception.AppException;
import com.example.app.provider.PageConfigProvider;
import com.example.common.entity.CommonRequest;
import com.example.common.entity.Result;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


@RestController
@RequestMapping("/page")
@Api("/page")
@Slf4j
public class PageController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageConfigProvider pageConfigProvider;

    @ApiOperation(value = "获取页面配置数据", notes = "获取页面配置数据")
    @RequestMapping(method = RequestMethod.POST
            , value = "getPageInfo.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getPageInfo(@Valid @RequestBody CommonRequest<PageInfoReq> req) {
        PageInfoResp resp = new PageInfoResp();

        try {
            String pageContent = pageConfigProvider.getPageConfig("page.app.demo.json", req.getData().getPageName());
            JsonNode json = objectMapper.readTree(pageContent);
            resp.setPageInfo(json);
        } catch(FeignException e) {
            return Result.fail(AppException.PAGE_NOT_CONFIG);
        } catch(JsonParseException e) {
            return Result.fail(AppException.PAGE_JSON_ERROR);
        } catch (IOException e) {
            return Result.fail(AppException.PAGE_NOT_EXIST);
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