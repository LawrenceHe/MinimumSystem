package com.example.app.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-center")
public interface UserAssetProvider {

    @PostMapping(value = "/asset/getFortuneById.json")
    String getFortuneById(@RequestParam("id") Long id);

    @PostMapping(value = "/asset/getLoanById.json")
    String getLoanById(@RequestParam("id") Long id);

}
