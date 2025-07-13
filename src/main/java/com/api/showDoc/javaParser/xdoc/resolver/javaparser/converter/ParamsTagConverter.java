package com.api.showDoc.javaParser.xdoc.resolver.javaparser.converter;

import com.api.showDoc.javaParser.xdoc.model.FieldInfo;
import com.api.showDoc.javaParser.xdoc.model.ObjectInfo;
import com.api.showDoc.javaParser.xdoc.tag.DocTag;
import com.api.showDoc.javaParser.xdoc.tag.SeeTagImpl;
import com.api.showDoc.javaParser.xdoc.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 针对@params的转换器
 *
 * @author huangyuyi
 * @date 2017/3/4
 */
public class ParamsTagConverter extends DefaultJavaParserTagConverterImpl {

    private Logger log = LoggerFactory.getLogger(ParamsTagConverter.class);

    @Override
    public DocTag converter(String comment) {
        DocTag docTag = super.converter(comment);
        String values = (String) docTag.getValues();
        String[] strings  =values.replace("[", "").replace("]", "").split(",");
        List<FieldInfo> fields = new ArrayList<FieldInfo>();
        for(String val:strings){
            if(val.matches("[A-Z][a-zA-Z0-9]*")){
                //如果不是基本数据类型，则是对象  使用@see解析
                if(!Constant.DATA_TYPE.contains(val)){
                    JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter("@see");
                    SeeTagImpl tag = (SeeTagImpl)converter.converter("@see "+val);
                    if(tag!=null){
                        fields.addAll(tag.getValues().getFieldInfos());
                        continue;
                    }
                }
            }
            String[] array = val.trim().split("[ \t]+");
            String paramName = null;
            String paramDesc = "";
            String paramType = "String";
            boolean require = false;

            if (array.length > 0) {
                //先将第一个认为是参数名称
                paramName = array[0];
                if (array.length > 1) {
                    int start = 1;
                    if (array[1].startsWith(":") && array[1].length() > 1) {
                        //获取 :username这种类型的参数名称
                        paramName = array[1].substring(1);
                        start = 2;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = start; i < array.length; i++) {
                        sb.append(array[i]).append(' ');
                    }
                    paramDesc = sb.toString();
                }
            }

            String[] descs = paramDesc.split("\\|");
            if (descs.length > 0) {
                paramDesc = descs[0];
                if (descs.length > 2) {
                    paramType = descs[1];
                    String requireString = descs[descs.length - 1].trim();
                    require = Constant.YES_ZH.equals(requireString) || Constant.YES_EN.equalsIgnoreCase(requireString);
                } else if (descs.length == 2) {
                    String requireString = descs[1].trim();
                    require = Constant.YES_ZH.equals(requireString) || Constant.YES_EN.equalsIgnoreCase(requireString);

                    //如果最后一个不是是否必填的描述,则认为是类型描述
                    if (!require && !(Constant.NOT_EN.equalsIgnoreCase(requireString) || Constant.NOT_ZH.equals(requireString))) {
                        paramType = requireString;
                    }
                }
                //如果不是基本数据类型，则是对象  使用@see解析
                if(!Constant.DATA_TYPE.contains(paramType)){
                    JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter("@see");
                    SeeTagImpl tag = (SeeTagImpl)converter.converter("@see "+paramType);
                    if(tag!=null){
                        fields.addAll(tag.getValues().getFieldInfos());
                        continue;
                    }
                }
            }
            FieldInfo field = new FieldInfo();
            field.setName(paramName);
            field.setRequire(require);
            field.setSimpleTypeName(paramType);
            field.setComment(paramDesc);
            fields.add(field);
        }
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setFieldInfos(fields);
        return new SeeTagImpl(docTag.getTagName(), objectInfo);
    }

}
