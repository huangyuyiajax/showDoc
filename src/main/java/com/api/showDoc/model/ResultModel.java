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
     * 业务模块的描述
     */
    private String comment;

    private String controName;

    private List<ShowdocModel> list;

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
