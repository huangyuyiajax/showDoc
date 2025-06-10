package com.api.javaParser.xdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 接口业务模块,一个接口类为一个模块
 *
 * @author huangyuyi
 * @date 2017-03-03 10:32
 */
@Data
public class ApiModule {

    /**
     * 源码在哪个类
     */
    @JsonIgnore
    private transient Class<?> type;

    /**
     * 业务模块的描述
     */
    private String comment;

    /**
     * 此业务模块下有哪些接口
     */
    private List<ApiAction> apiActions = new LinkedList<ApiAction>();

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ApiAction> getApiActions() {
        return apiActions;
    }

    public void setApiActions(List<ApiAction> apiActions) {
        this.apiActions = apiActions;
    }
}
