package com.alibaba.csp.sentinel.dashboard.rule.nacos.param;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX;

@Component("paramFlowRuleNacosPublisher")
public class ParamFlowRuleNacosPublisher implements DynamicRulePublisher<List<ParamFlowRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public void publish(String appName, List<ParamFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(appName, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(
                appName + PARAM_FLOW_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                JSON.toJSONString(
                        rules.stream().map(rule -> rule.getRule()).collect(Collectors.toList())
                )
        );
    }
}
