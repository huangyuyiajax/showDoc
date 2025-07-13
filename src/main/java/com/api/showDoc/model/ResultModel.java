package com.api.showDoc.model;


import java.util.List;

/**
 * showdoc 数据
 *
 * @author huangyuyi
 * @date 2019-03-21 10:13
 */
public class ResultModel {

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
     * 业务模块的描述
     */
    private String comment;
    /**
     * 类名称
     */
    private String controName;

    private List<ShowdocModel> list;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getControName() {
        return controName;
    }

    public void setControName(String controName) {
        this.controName = controName;
    }

    public List<ShowdocModel> getList() {
        return list;
    }

    public void setList(List<ShowdocModel> list) {
        this.list = list;
    }
}
