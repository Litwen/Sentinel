package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author per
 * @since 1.6.3
 */
@Validated
@Configuration
public class NacosProperties {

    public static final String PROPERTY_PREFIX = "sentinel.dashboard.nacos";

    @NotEmpty
    @Value("${sentinel.dashboard.nacos.serverAddr}")
    private String serverAddr = "127.0.0.1:8848";

    private String contextPath;

    @Value("${sentinel.dashboard.nacos.username:nacos}")
    private String username = "nacos";

    @Value("${sentinel.dashboard.nacos.password:nacos}")
    private String password = "nacos";

    @NotEmpty
    @Value("${sentinel.dashboard.nacos.groupId:DEFAULT_GROUP}")
    private String groupId = "DEFAULT_GROUP";

    private String dataId;

    private String endpoint;

    @Value("${sentinel.dashboard.nacos.postTimeout:2000}")
    private long postTimeout = 3000;



    @NotEmpty
    @Value("${sentinel.dashboard.nacos.namespace:public}")
    private String namespace = "public";

    private String accessKey;

    private String secretKey;

    public long getPostTimeout() {
        return postTimeout;
    }

    public void setPostTimeout(long postTimeout) {
        this.postTimeout = postTimeout;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
