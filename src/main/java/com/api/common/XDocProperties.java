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
     * 源码相对路径(支持多个,用英文逗号隔开)
     */
    private String sourcePath;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
