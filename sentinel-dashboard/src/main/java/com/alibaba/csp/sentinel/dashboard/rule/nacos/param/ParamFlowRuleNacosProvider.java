package com.alibaba.csp.sentinel.dashboard.rule.nacos.param;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX;

@Component("paramFlowRuleNacosProvider")
public class ParamFlowRuleNacosProvider implements DynamicRuleProvider<List<ParamFlowRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public List<ParamFlowRuleEntity> getRules(String appName, String ip, int port) throws Exception {

        String rules = configService.getConfig(
                appName + PARAM_FLOW_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                nacosProperties.getPostTimeout()
        );

        if (StringUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        List<ParamFlowRule> paramFlowRules = JSON.parseArray(rules, ParamFlowRule.class);

        return paramFlowRules
                .stream()
                .map(rule -> ParamFlowRuleEntity.fromParamFlowRule(appName, ip, port, rule))
                .collect(Collectors.toList());

    }
}
