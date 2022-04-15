package com.alibaba.csp.sentinel.dashboard.rule.nacos.degrade;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Litwen
 * @since 1.4.0
 */
@Component("degradeRuleNacosProvider")
public class DegradeRuleNacosProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public List<DegradeRuleEntity> getRules(String appName, String ip, int port) throws Exception {
        String rules = configService
                .getConfig(
                        appName + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX,
                        nacosProperties.getGroupId(), nacosProperties.getPostTimeout());
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        List<DegradeRule> list = JSON.parseArray(rules, DegradeRule.class);
        return list
                .stream()
                .map(entity -> DegradeRuleEntity.fromDegradeRule(appName, ip, port, entity))
                .collect(Collectors.toList());
    }
}
