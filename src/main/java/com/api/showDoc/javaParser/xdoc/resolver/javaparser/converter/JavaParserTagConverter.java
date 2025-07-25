package com.api.showDoc.javaParser.xdoc.resolver.javaparser.converter;


import com.api.showDoc.javaParser.xdoc.tag.DocTag;

/**
 * 针对JavaParser语法解析包解析出来的文本转换器,负责将文本转转DocTag
 *
 * @author huangyuyi
 * @date 2017/4/3 0003
 */
public interface JavaParserTagConverter<T extends String> {

    /**
     * 将指定的文本转义为DocTag
     *
     * @param o 文本
     * @return DocTag对象
     */
    DocTag converter(T o) ;

    DocTag converter(T o,String tType) ;

    DocTag converter(T o,Integer i) ;

    DocTag converter(String comment,Integer i,String tType);
}
