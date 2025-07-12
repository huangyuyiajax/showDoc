package com.api.showDoc.javaParser.xdoc.resolver.javaparser.converter;


import com.api.showDoc.javaParser.xdoc.tag.DocTag;
import com.api.showDoc.javaParser.xdoc.tag.ParamTagImpl;
import com.api.showDoc.javaParser.xdoc.tag.SeeTagImpl;
import com.api.showDoc.javaParser.xdoc.utils.Constant;

/**
 * 针对@param的转换器
 * @author huangyuyi
 * @date 2017/3/4
 */
public class ParamTagConverter extends DefaultJavaParserTagConverterImpl {

    @Override
    public DocTag converter(String comment) {
        DocTag docTag = super.converter(comment);
        String val = (String) docTag.getValues();
        if(val.startsWith("[")&&val.endsWith("]")&&val.length()>2){
            ParamsTagConverter paramsTagConverter = new ParamsTagConverter();
            docTag = paramsTagConverter.converter(comment);
            docTag.setTagName("@params");
            return docTag;
        }
        if("@resp".equals(docTag.getTagName())&&val.matches("[A-Z][a-zA-Z]*")){
            //如果不是基本数据类型，则是对象  使用@see解析
            if(!Constant.DATA_TYPE.contains(val)){
                JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter("@see");
                SeeTagImpl tag = (SeeTagImpl)converter.converter("@see "+val);
                if(tag!=null){
                    return tag;
                }
            }
        }
        String[] array = val.split("[ \t]+");
        String paramName = null;
        String paramDesc = "";
        String paramType = "String";
        boolean require = false;
        //解析 "user :username 用户名|必填" 这种注释内容
        //或者 "username 用户名|必填" 这种注释内容
        //或者 "username 用户名|String|必填" 这种注释内容
        //上面的"必填"两个字也可以换成英文的"Y"

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
        }
        String name = paramName;
        String value = paramDesc;
        if (paramName != null) {
            name = paramName.split(":")[0];
            value = paramName.split(":").length>1?paramName.split(":")[1]:"";
        }
        return new ParamTagImpl(docTag.getTagName(), name,value, paramDesc, paramType, require);
    }
}
