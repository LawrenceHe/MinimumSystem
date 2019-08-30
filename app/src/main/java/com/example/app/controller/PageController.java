package com.example.app.controller;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
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
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/page")
@Api("/page")
@Slf4j
public class PageController {

    // 缓存页面数据
    private ConcurrentMap<String, String> pagesInformation = new ConcurrentHashMap<String, String>();

    private final String DATA_ID = "page.app.demo.json";

    @Autowired
    private ConfigService configService;

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
        String pageName = req.getData().getPageName();

        try {
            String pageContent = pagesInformation.getOrDefault(pageName, "");
            // 若从缓存中取不到数据，则从配置系统中读取并监听修改
            if (pageContent.isEmpty()) {
                pageContent = configService.getConfig(DATA_ID, pageName, 2000);
                if (pageContent == null) {
                    return Result.fail(AppException.PAGE_NOT_CONFIG);
                }
                pagesInformation.put(pageName, pageContent);
                configService.addListener(DATA_ID, pageName, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        pagesInformation.put(pageName, configInfo);
                    }
                });
            }
            JsonNode json = objectMapper.readTree(pageContent);
            resp.setPageInfo(json);
        } catch(NacosException e) {
            return Result.fail(AppException.PAGE_NOT_CONFIG);
        } catch(JsonParseException e) {
            return Result.fail(AppException.PAGE_JSON_ERROR);
        } catch (IOException | NullPointerException e) {
            return Result.fail(AppException.PAGE_NOT_EXIST);
        }

        return Result.success(resp);
    }

    @ApiOperation(value = "获取页面配置数据", notes = "获取页面配置数据")
    @RequestMapping(method = RequestMethod.POST
            , value = "getPageInfoWithNoCache.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getPageInfoWithNoCache(@Valid @RequestBody CommonRequest<PageInfoReq> req) {
        PageInfoResp resp = new PageInfoResp();

        try {
            String pageContent = pageConfigProvider.getPageConfig(DATA_ID, req.getData().getPageName());
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
}