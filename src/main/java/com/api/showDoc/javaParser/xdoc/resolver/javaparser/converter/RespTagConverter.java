package com.api.showDoc.javaParser.xdoc.resolver.javaparser.converter;


import com.api.showDoc.javaParser.xdoc.tag.DocTag;
import com.api.showDoc.javaParser.xdoc.tag.ParamTagImpl;
import com.api.showDoc.javaParser.xdoc.tag.RespTagImpl;
import com.api.showDoc.javaParser.xdoc.tag.SeeTagImpl;

/**
 * 针对@resp的转换器
 * @author huangyuyi
 * @date 2017/3/12 0012
 */
public class RespTagConverter extends ParamTagConverter {

    @Override
    public DocTag converter(String comment) {
        DocTag docTag = super.converter(comment);
        if(docTag instanceof SeeTagImpl){
            docTag.setTagName("@see");
            return (SeeTagImpl)docTag;
        }
        ParamTagImpl paramTag = (ParamTagImpl) super.converter(comment);
        RespTagImpl respTag = new RespTagImpl(paramTag.getTagName(), paramTag.getParamName(), paramTag.getParamDesc(),
                paramTag.getParamType(), paramTag.isRequire());
        return respTag;
    }
}
