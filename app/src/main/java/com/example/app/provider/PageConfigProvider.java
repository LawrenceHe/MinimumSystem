package com.example.app.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="nacos", url="http://localhost:8848")
public interface PageConfigProvider {

    @GetMapping(value = "/nacos/v1/cs/configs")
    String getPageConfig(
            @RequestParam("dataId") String dataId,
            @RequestParam("group") String group
    );

}
