package com.api.showDoc.model;

import lombok.Data;

import java.util.List;

/**
 * showdoc 数据
 *
 * @author huangyuyi
 * @date 2019-03-21 10:13
 */
@Data
public class ResultModel {

    /**
     * 业务模块的描述
     */
    private String comment;

    private String controName;

    private List<ShowdocModel> list;



}
