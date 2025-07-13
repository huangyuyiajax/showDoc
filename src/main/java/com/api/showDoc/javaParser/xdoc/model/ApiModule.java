package com.api.showDoc.javaParser.xdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;

/**
 * 接口业务模块,一个接口类为一个模块
 *
 * @author huangyuyi
 * @date 2017-03-03 10:32
 */
public class ApiModule {

    /**
     * 类简要描述
     */
    private String description = "";

    //类创建人员
    private String author = "";
    /**
     * 类创编写时间
     */
    private String date = "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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
