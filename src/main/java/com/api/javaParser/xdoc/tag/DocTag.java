package com.api.javaParser.xdoc.tag;

import lombok.Getter;

/**
 * 针对注释标签
 *
 * Created by huangyuyi on 2017/3/4.
 */
public abstract class DocTag<T> {

    /**
     * 标签名称
     */
    @Getter
    private String tagName;

    public DocTag(String tagName) {
        this.tagName = tagName;
    }

    public abstract T getValues();

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
