package com.api.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("xdoc")
public class XDocProperties {

    /**
     * 是否启动XDOC,此值便于在生产等环境启动程序时增加参数进行控制
     */
    private boolean enable = true;

    /**
     * 界面标题描述
     */
    private String title = "XDoc 接口文档";

    /**
     * 源码相对路径(支持多个,用英文逗号隔开)
     */
    private String sourcePath;

    /**
     * 文档版本号
     */
    private String version;

    /**
     * 接口路径
     */
    private String url;

    /**
     * 接口api_key
     */
    private String apiKey;

    /**
     * 接口api_token
     */
    private String apiToken;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
