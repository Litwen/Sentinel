package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway.api;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX;

/**
 * @author wenxiaowei
 * @since 2022/4/16
 */
@Component("gatewayApiRuleProvider")
public class GatewayApiRuleProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public List<ApiDefinitionEntity> getRules(String appName, String ip, int port) throws Exception {
        final String rules = configService.getConfig(appName + GATEWAY_API_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                nacosProperties.getPostTimeout());
        if (!StringUtils.hasLength(rules)) {
            return new ArrayList<>();
        }

        final List<ApiDefinitionEntity> list = JSON.parseArray(rules, ApiDefinitionEntity.class);

        return list.stream()
                .map(rule -> {
                    rule.setApp(appName);
                    rule.setIp(ip);
                    rule.setPort(port);
                    return rule;
                }).
                collect(Collectors.toList());
    }
}
