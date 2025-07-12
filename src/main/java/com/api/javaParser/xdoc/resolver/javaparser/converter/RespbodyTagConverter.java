package com.api.javaParser.xdoc.resolver.javaparser.converter;

import com.api.javaParser.xdoc.tag.DocTag;
import com.api.javaParser.xdoc.tag.DocTagImpl;
import com.api.javaParser.xdoc.tag.SeeTagImpl;
import com.api.javaParser.xdoc.utils.ClassMapperUtils;
import com.api.javaParser.xdoc.utils.CommentUtils;
import com.api.javaParser.xdoc.utils.Constant;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.util.*;

/**
 * 针对@respbody的转换器
 *
 * @author huangyuyi
 * @date 2017/3/4
 */
public class RespbodyTagConverter extends DefaultJavaParserTagConverterImpl {

    private Logger log = LoggerFactory.getLogger(RespbodyTagConverter.class);

    //递归解析多少层
    private int respbodyLevel = 2;

    @Override
    public DocTagImpl converter(String comment) {
        DocTag docTag = super.converter(comment);

        String values = (String) docTag.getValues();;//  {code:0,list:[ShowdocModel],msg:sad}
        if(values.matches("[A-Z][a-zA-Z]*")){
            values = "|"+values+"|";
        }
        String[] strings = values.split("\\|");
        String replace = values;
        for(String val:strings){
            String value = val.trim();
            Map<String,String> paramValue = new HashMap<>();
            String path = val.trim();
            String param ="";
            int indexValue = value.indexOf("(");
            if(indexValue>0){
                path = value.substring(0,indexValue);
            }
            if(path.matches("[a-zA-Z]+")){
                int indexValue2 = value.indexOf(")");
                if(indexValue2>0){
                    param = value.substring(indexValue+1,value.length()-1);
                }
                String [] strings1 = param.split(",");
                for(String v:strings1){
                    paramValue.put(v.split(":")[0],v.split(":").length>1?v.split(":")[1]:null);
                }
                String parser =  parser(path,1, paramValue);
                if(parser!= null&& parser.contains("{")){
                    parser = parser.substring(parser.indexOf("{"));
                    int index = values.indexOf("|"+val.trim()+"|");
                    if(index>0&&"[".equals(String.valueOf(values.charAt(index-1)))){
                        replace = values.replace("|"+val.trim()+"|", parser+","+parser+",...");
                    }else {
                        replace = values.replace("|"+val.trim()+"|", parser);
                    }
                }
            }
        }
        return new DocTagImpl(docTag.getTagName(),replace);
    }

    private String parser(String values,Integer flag, Map<String,String> paramValue){
        String path = ClassMapperUtils.getPath(values);
        if (StringUtils.isBlank(path)) {
            return values;
        }
        Class<?> returnClassz;
        CompilationUnit cu;
        StringBuilder obj1= new StringBuilder();
        try  {
            FileInputStream in = new FileInputStream(path);
            cu = JavaParser.parse(in);
            if (cu.getTypes().size() <= 0) {
                return null;
            }
            returnClassz = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + cu.getTypes().get(0).getNameAsString());
            List<String> commentMap = this.analysisFieldComments(returnClassz,flag, paramValue);
            obj1.append("{");
            for(int i=0 ;i<commentMap.size();i++){
                if(i!=0){
                    obj1.append(",");
                }
                obj1.append(commentMap.get(i));
            }
            obj1.append("}");
        } catch (Exception e) {
            log.warn("读取java原文件失败:{}", path, e.getMessage());
            return values;
        }
        return obj1.toString();
    }

    private List<String> analysisFieldComments(Class<?> classz,Integer flag,Map<String,String> paramValue) {

        final Map<String, String> commentMap = new HashMap(10);

        List<Class> classes = new LinkedList<Class>();

        Class nowClass = classz;
        //获取所有的属性注释(包括父类的)
        while (true) {
            classes.add(0, nowClass);
            if (Object.class.equals(nowClass) || Object.class.equals(nowClass.getSuperclass())) {
                break;
            }
            nowClass = nowClass.getSuperclass();
        }

        //反方向循环,子类属性注释覆盖父类属性
        for (Class clz : classes) {
            String path = ClassMapperUtils.getPath(clz.getSimpleName());
            if (StringUtils.isBlank(path)) {
                continue;
            }
            try {
                FileInputStream in = new FileInputStream(path);
                CompilationUnit cu = JavaParser.parse(in);
                new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(FieldDeclaration n, Void arg) {
                        String name = n.getVariable(0).getName().asString();

                        String comment = "";
                        if (n.getComment().isPresent()) {
                            comment = n.getComment().get().getContent();
                        }

                        if (name.contains("=")) {
                            name = name.substring(0, name.indexOf("=")).trim();
                        }
                        comment = CommentUtils.parseCommentText(comment);
                        boolean require ;
                        if (comment.contains("|")) {
                            int endIndex = comment.lastIndexOf("|" + Constant.YES_ZH);
                            if (endIndex < 0) {
                                endIndex = comment.lastIndexOf("|" + Constant.YES_EN);
                            }
                            require = endIndex > 0;
                            if (require) {
                                int index = comment.lastIndexOf(":");
                                if (index > 1) {
                                    comment = comment.substring(index+1).split("[ \t]+")[0].split("\n")[0].split("\r")[0];
                                }else {
                                    comment = comment.substring(0, endIndex);
                                }
                            }
                        }
                        commentMap.put(name, comment);
                    }
                }.visit(cu, null);
                return analysisFields(classz,commentMap,flag, paramValue);
            } catch (Exception e) {
                log.warn("读取java原文件失败:{}", path, e.getMessage(), e);
            }
        }

        return new ArrayList<>();
    }

    private List<String> analysisFields(Class classz, Map<String, String> commentMap,Integer flag,Map<String,String> paramValue) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(classz);
        List<String> fields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            //排除掉class属性
            if ("class".equals(propertyDescriptor.getName())) {
                continue;
            }
            String type = propertyDescriptor.getPropertyType().getSimpleName();
            String comment = commentMap.get(propertyDescriptor.getName());
            if(!Constant.DATA_TYPE.contains(type)&&flag<respbodyLevel){
                comment = parser(type,flag+1,paramValue);
            }
            if("List".equals(type)&&flag<respbodyLevel){
                String method= propertyDescriptor.getReadMethod().toGenericString();
                int star = method.indexOf("<");
                int end = method.indexOf(">",star);
                if(paramValue.get(propertyDescriptor.getName())!=null){
                    String  value = parser(paramValue.get(propertyDescriptor.getName()),flag+1,paramValue);
                    comment = "["+value+","+value+",...]";
                }else if(star>0&&end>0){
                    String path = method.substring(star+1,end);
                    String  value = parser(path,flag+1,paramValue);
                    comment = "["+value+value+",...]";
                }
            }
            if(!Constant.TYPE.contains(type)){
                comment = "\""+comment+"\"";
            }
            fields.add("\""+propertyDescriptor.getName()+"\":"+comment);
        }
        return fields;
    }

}
