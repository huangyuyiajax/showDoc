package com.api.showDoc.javaParser.xdoc.resolver.javaparser.converter;


import com.api.showDoc.javaParser.xdoc.tag.DocTag;
import com.api.showDoc.javaParser.xdoc.tag.DocTagImpl;
import com.api.showDoc.javaParser.xdoc.utils.CommentUtils;

/**
 * 基于JavaParser包的默认注释解析转换器
 *
 * @author huangyuyi
 * @date 2017/3/4
 */
public class DefaultJavaParserTagConverterImpl implements JavaParserTagConverter<String> {

    @Override
    public DocTag converter(String comment) {
        String tagType = CommentUtils.getTagType(comment);
        String coment = comment.substring(tagType.length()).trim();
        return new DocTagImpl(tagType, coment);
    }
    public DocTag converter(String comment,Integer i){
        return converter(comment);
    }
}
