package com.api.showDoc.javaParser.xdoc.tag;

/**
 * 简单文本型注释标签实现
 * <p>
 * Created by huangyuyi on 2017/3/4.
 */
public class DocTagImpl extends DocTag<String> {

    private String value;

    public DocTagImpl(String tagName, String value) {
        super(tagName);
        this.value = value;
    }

    @Override
    public String getValues() {
        return value;
    }
}
