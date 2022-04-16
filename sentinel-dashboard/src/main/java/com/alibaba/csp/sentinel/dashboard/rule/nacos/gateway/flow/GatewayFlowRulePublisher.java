package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway.flow;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX;

/**
 * @author wenxiaowei
 * @since 2022/4/16
 */
@Component("gatewayFlowRulePublisher")
public class GatewayFlowRulePublisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public boolean isPublish(String appName, List<GatewayFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(appName, "app name cannot be empty");
        if (rules == null) {
            return false;
        }

        return configService.publishConfig(
                appName + GATEWAY_FLOW_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                JSON.toJSONString(rules.stream()
                        .map(rule -> rule.toGatewayFlowRule())
                        .collect(Collectors.toList())
                )
        );
    }
}
