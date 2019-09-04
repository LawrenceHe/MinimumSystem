package com.example.app.controller;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.example.app.entity.PageInfoReq;
import com.example.app.entity.PageInfoResp;
import com.example.app.exception.AppException;
import com.example.app.provider.PageConfigProvider;
import com.example.app.service.IAuthService;
import com.example.common.entity.CommonRequest;
import com.example.common.entity.CommonRequestHeader;
import com.example.common.entity.Result;
import com.example.common.exception.SystemErrorType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/page")
@Api("/page")
@Slf4j
@RefreshScope
public class PageController {

    // 缓存页面数据
    private ConcurrentMap<String, String> pagesInformation = new ConcurrentHashMap<String, String>();

    private final String DATA_ID = "page.app.demo.json";
    private final String DATA_ID_LOGIN = "page.login.app.demo.json";

    @Autowired
    private IAuthService authService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageConfigProvider pageConfigProvider;

    @Data
    static class PagesWithLoginState {
        public String[] pagesWithLoginState;
    }

    @ApiOperation(value = "获取页面配置数据", notes = "获取页面配置数据")
    @RequestMapping(method = RequestMethod.POST
            , value = "getPageInfo.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getPageInfo(@Valid @RequestBody CommonRequest<PageInfoReq> req) {

        PageInfoResp resp = new PageInfoResp();
        String page = req.getData().getPageName();

        CommonRequestHeader header = req.getHeader();
        String authentication = header.getToken();

        // 判断登录状态
        if (authentication != null && !authentication.isEmpty()) {
            if (authService.validJwtAccessToken(authentication)) {
                try {
                    String content = configService.getConfig(DATA_ID_LOGIN, "DEFAULT_GROUP", 2000);
                    PagesWithLoginState pages = objectMapper.readValue(content, PagesWithLoginState.class);
                    List<String> list = Arrays.asList(pages.getPagesWithLoginState());
                    if (list.contains(page)) {
                        page = page + "_login";
                    }
                } catch (Exception e) {
                    // TODO:
                }
            } else {
                // Token不为空但校验不通过，可返回非法Token异常
                return Result.fail(SystemErrorType.ILLEGAL_TOKEN);
            }
        }

        try {
            final String pageName = page;
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