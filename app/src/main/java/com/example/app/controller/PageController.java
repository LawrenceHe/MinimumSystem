package com.example.app.controller;

import com.example.app.context.AssetsContext;
import com.example.app.entity.PageInfoReq;
import com.example.app.entity.PageInfoResp;
import com.example.app.exception.AppException;
import com.example.app.provider.PageConfigProvider;
import com.example.app.provider.UserAssetProvider;
import com.example.app.service.IAuthService;
import com.example.app.util.NacosUtil;
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
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/page")
@Api("/page")
@Slf4j
public class PageController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageConfigProvider pageConfigProvider;

    @Autowired
    private UserAssetProvider userAssetProvider;

    @Autowired
    private NacosUtil nacosUtil;

    @Data
    static class PagesWithLoginState {
        public List<String> pagesWithLoginState;
    }

//    @RequestMapping(method = RequestMethod.POST
//            , value = "testExpressionLanguage.json"
//            , consumes = "application/json"
//            , produces = "application/json")
//    public Result testExpressionLanguage(@RequestParam Long userId) {
//        PageInfoResp resp = new PageInfoResp();
//        String dataId = "app.test.demo.json";
//
//        try {
//            String pageContent = nacosUtil.getConfig(dataId);
//            EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
//            AssetsContext c = new AssetsContext(userId, userAssetProvider);
//            context.setVariable("asset", c);
//
//            ExpressionParser parser = new SpelExpressionParser();
//            String s = parser.parseExpression(pageContent, new TemplateParserContext()).getValue(context, String.class);
//
//            if (pageContent.isEmpty()) {
//                return Result.fail(AppException.PAGE_NOT_CONFIG);
//            }
//            JsonNode json = objectMapper.readTree(s);
//            resp.setPageInfo(json);
//        } catch(JsonParseException e) {
//            return Result.fail(AppException.PAGE_JSON_ERROR);
//        } catch (IOException | NullPointerException e) {
//            return Result.fail(AppException.PAGE_NOT_EXIST);
//        }
//
//        return Result.success(resp);
//    }

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
        String dataId = "app.demo.page." + page + ".unlogin.json";

        // 判断登录状态
        if (authentication != null && !authentication.isEmpty()) {
            if (authService.validJwtAccessToken(authentication)) {
                String content = nacosUtil.getConfig("app.demo.page.settings");
                try {
                    PagesWithLoginState pages = objectMapper.readValue(content, PagesWithLoginState.class);
                    if (pages.getPagesWithLoginState().contains(page)) {
                        dataId = "app.demo.page." + page + ".login.json";
                    }
                } catch (Exception e) {
                    log.error("Pages With Login State Config Error!");
                }
            } else {
                // Token不为空但校验不通过，可返回非法Token异常
                return Result.fail(SystemErrorType.ILLEGAL_TOKEN);
            }
        }

        EvaluationContext evalContext = new StandardEvaluationContext();
        AssetsContext assetsContext = new AssetsContext(Long.valueOf(req.getHeader().getUserId()), userAssetProvider);
        evalContext.setVariable("asset", assetsContext);
        ExpressionParser parser = new SpelExpressionParser();

        try {
            String pageContent = nacosUtil.getConfig(dataId);
            if (pageContent.isEmpty()) {
                return Result.fail(AppException.PAGE_NOT_CONFIG);
            }
            String formattedPageContent = parser.parseExpression(pageContent, new TemplateParserContext()).getValue(evalContext, String.class);
            JsonNode jsonPageContent = objectMapper.readTree(formattedPageContent);
            resp.setPageInfo(jsonPageContent);
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
        String dataId = "app.demo.page." + req.getData().getPageName() + ".unlogin.json";
        try {
            String pageContent = pageConfigProvider.getPageConfig(dataId, req.getData().getPageName());
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