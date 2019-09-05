package com.example.app.util;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

@Component
public class NacosUtil {

    final private long TIME_OUT = 2000;

    @Autowired
    private ConfigService configService;

    private ConcurrentMap<String, String> ConfigCache = new ConcurrentHashMap<>();

    public @NotNull String getConfig(String dataId) {

        String content;
        try {
            content = ConfigCache.getOrDefault(dataId, "");
            if (content.isEmpty()) {
                content = configService.getConfig(dataId, "DEFAULT_GROUP", TIME_OUT);
                ConfigCache.put(dataId, content);
                configService.addListener(dataId, "DEFAULT_GROUP", new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        ConfigCache.put(dataId, configInfo);
                    }
                });
            }
        } catch (NacosException e) {
            return "";
        }

        return content != null ? content : "";
    }

    public @NotNull String getConfigWithoutCache(String dataId) {
        String content;
        try {
            content = configService.getConfig(dataId, "DEFAULT_GROUP", TIME_OUT);
        } catch (NacosException e) {
            return "";
        }

        return content != null ? content : "";
    }
}
