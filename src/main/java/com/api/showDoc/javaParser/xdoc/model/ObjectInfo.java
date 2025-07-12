package com.api.showDoc.javaParser.xdoc.model;


import java.util.LinkedList;
import java.util.List;

/**
 * 针对@see标签指向的类具体信息
 *
 * @author huangyuyi
 * @date 2017-03-03 12:14
 */
public class ObjectInfo {

    /**
     * 返回的参数名称
     */
    private String name;

    /**
     * 源码在哪个类
     */
    private Class<?> type;

    /**
     * 上面的注释
     */
    private String comment;

    /**
     * 对象的属性
     */
    private List<FieldInfo> fieldInfos = new LinkedList<FieldInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
}
