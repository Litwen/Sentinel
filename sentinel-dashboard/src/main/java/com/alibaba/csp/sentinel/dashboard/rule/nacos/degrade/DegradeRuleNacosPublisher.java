package com.alibaba.csp.sentinel.dashboard.rule.nacos.degrade;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX;

/**
 * @author Litwen
 * @since 1.4.0
 */
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    private static final Logger logger = LoggerFactory.getLogger(DegradeRuleNacosPublisher.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public boolean isPublish(String appName, List<DegradeRuleEntity> rules) throws NacosException {
        AssertUtil.notEmpty(appName, "app name cannot be empty");
        if (rules == null) {
            return false;
        }

        return configService.publishConfig(
                appName + DEGRADE_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                JSON.toJSONString(rules.stream().map(e -> e.toRule()).collect(Collectors.toList())));

    }
}
