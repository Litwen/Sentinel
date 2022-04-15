package com.alibaba.csp.sentinel.dashboard.rule.nacos.system;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.SYSTEM_DATA_ID_POSTFIX;

@Component("systemRuleNacosProvider")
public class SystemRuleNacosProvider implements DynamicRuleProvider<List<SystemRuleEntity>> {
    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public List<SystemRuleEntity> getRules(String appName, String ip, int port) throws Exception {
        String rules = configService.getConfig(
                appName + SYSTEM_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                nacosProperties.getPostTimeout()
        );
        if (!StringUtils.hasLength(rules)) {
            return new ArrayList<>();
        }

        List<SystemRule> list = JSON.parseArray(rules, SystemRule.class);
        return list.stream()
                .map(rule -> SystemRuleEntity.fromSystemRule(appName, ip, port, rule))
                .collect(Collectors.toList());
    }
}
