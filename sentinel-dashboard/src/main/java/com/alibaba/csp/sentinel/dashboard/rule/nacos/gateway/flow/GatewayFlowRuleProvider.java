package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway.flow;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX;

/**
 * @author wenxiaowei
 * @since 2022/4/16
 */
@Component("gatewayFlowRuleProvider")
public class GatewayFlowRuleProvider implements DynamicRuleProvider<List<GatewayFlowRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public List<GatewayFlowRuleEntity> getRules(String appName, String ip, int port) throws Exception {
        final String rules = configService.getConfig(appName + GATEWAY_FLOW_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                nacosProperties.getPostTimeout());
        if (!StringUtils.hasLength(rules)) {
            return new ArrayList<>();
        }

        final List<GatewayFlowRule> list = JSON.parseArray(rules, GatewayFlowRule.class);

        return list.stream()
                .map(rule -> GatewayFlowRuleEntity.fromGatewayFlowRule(appName, ip, port, rule))
                .collect(Collectors.toList());
    }
}
