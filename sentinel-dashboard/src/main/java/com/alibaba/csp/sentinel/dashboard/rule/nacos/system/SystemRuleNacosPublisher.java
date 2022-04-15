package com.alibaba.csp.sentinel.dashboard.rule.nacos.system;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.SYSTEM_DATA_ID_POSTFIX;

@Component("systemRuleNacosPublisher")
public class SystemRuleNacosPublisher implements DynamicRulePublisher<List<SystemRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public boolean isPublish(String appName, List<SystemRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(appName, "app name cannot be empty");
        if (rules == null) {
            return false;
        }
        return configService.publishConfig(
                appName + SYSTEM_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                JSON.toJSONString(rules.stream()
                        .map(rule -> rule.toRule())
                        .collect(Collectors.toList()))
        );
    }
}
