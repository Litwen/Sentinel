package com.alibaba.csp.sentinel.dashboard.rule.nacos.authority;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosProperties;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX;

@Component("authorityRuleNacosProvider")
public class AuthorityRuleNacosProvider implements DynamicRuleProvider<List<AuthorityRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosProperties nacosProperties;

    @Override
    public List<AuthorityRuleEntity> getRules(String appName, String ip, int port) throws Exception {
        String rules = configService.getConfig(
                appName + AUTHORITY_DATA_ID_POSTFIX,
                nacosProperties.getGroupId(),
                nacosProperties.getPostTimeout()
        );
        if (!StringUtils.hasLength(rules)) {
            return new ArrayList<>();
        }

        List<AuthorityRule> list = JSON.parseArray(rules, AuthorityRule.class);
        return list.stream()
                .map(rule -> AuthorityRuleEntity.fromAuthorityRule(appName, ip, port, rule))
                .collect(Collectors.toList());
    }
}
