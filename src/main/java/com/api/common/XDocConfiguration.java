package com.api.common;

import com.api.showDoc.controller.XDocController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;


public class XDocConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "xdoc", name = "enable", matchIfMissing = true)
    public XDocController xDocController() {
        return new XDocController();
    }
}
