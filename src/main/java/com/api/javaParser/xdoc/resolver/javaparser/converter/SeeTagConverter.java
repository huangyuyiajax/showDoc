package com.api.javaParser.xdoc.resolver.javaparser.converter;

import com.api.javaParser.xdoc.model.FieldInfo;
import com.api.javaParser.xdoc.model.ObjectInfo;
import com.api.javaParser.xdoc.tag.DocTag;
import com.api.javaParser.xdoc.tag.SeeTagImpl;
import com.api.javaParser.xdoc.utils.ClassMapperUtils;
import com.api.javaParser.xdoc.utils.CommentUtils;
import com.api.javaParser.xdoc.utils.Constant;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 针对@see的转换器
 *
 * @author huangyuyi
 * @date 2017/3/4
 */
public class SeeTagConverter extends DefaultJavaParserTagConverterImpl {

    private Logger log = LoggerFactory.getLogger(SeeTagConverter.class);

    //递归解析多少层
    private int level = 3;

    @Override
    public DocTag converter(String comment) {
        return  converter(comment,0);
    }
    @Override
    public DocTag converter(String comment,Integer i) {
        DocTag docTag = super.converter(comment);
        String value = (String) docTag.getValues();
        String [] paths =new String[]{value};
        List<FieldInfo> fields = new ArrayList<>();
        if(value.startsWith("[")&&value.endsWith("]")&&value.length()>2){
            paths = value.substring(1,value.length()-1).split(",");
        }
        for(String val:paths){
            String path = ClassMapperUtils.getPath(val);
            if (StringUtils.isBlank(path)) {
                continue;
            }

            Class<?> returnClassz;
            CompilationUnit cu;
            try  {
                FileInputStream in = new FileInputStream(path);
                cu = JavaParser.parse(in);
                if (cu.getTypes().size() <= 0) {
                    continue;
                }
                returnClassz = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + cu.getTypes().get(0).getNameAsString());

            } catch (Exception e) {
                log.warn("读取java原文件失败:{}", path, e.getMessage());
                continue;
            }

            String text = cu.getComment().isPresent() ? CommentUtils.parseCommentText(cu.getComment().get().getContent()) : "";

            Map<String, String> commentMap = this.analysisFieldComments(returnClassz);
            fields.addAll(this.analysisFields(returnClassz, commentMap,i));
        }

        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setFieldInfos(fields);
        return new SeeTagImpl(docTag.getTagName(), objectInfo);
    }

    private Map<String, String> analysisFieldComments(Class<?> classz) {

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
                        if (StringUtils.isBlank(comment)) {
                            if (n.getVariable(0).getComment().isPresent()) {
                                comment = n.getVariable(0).getComment().get().getContent();
                            }
                        }

                        if (name.contains("=")) {
                            name = name.substring(0, name.indexOf("=")).trim();
                        }

                        commentMap.put(name, CommentUtils.parseCommentText(comment));
                    }
                }.visit(cu, null);

            } catch (Exception e) {
                log.warn("读取java原文件失败:{}", path, e.getMessage(), e);
            }
        }

        return commentMap;
    }

    private List<FieldInfo> analysisFields(Class classz, Map<String, String> commentMap,Integer i) {

        List<FieldInfo> fields = new ArrayList<FieldInfo>();
        Field[] fiel =  classz.getDeclaredFields();
        for(Field propertyDescriptor:fiel){
            //排除掉class属性
            if ("class".equals(propertyDescriptor.getName())) {
                continue;
            }
            Annotation[] annotation = propertyDescriptor.getDeclaredAnnotations();
            boolean require = false;
            for(Annotation a:annotation){
                if(a!=null){
                    require = a.toString().contains("NotEmpty");
                    if(require){
                        break;
                    }
                    require = a.toString().contains("NotBlank");
                    if(require){
                        break;
                    }
                }
            }
            FieldInfo field = new FieldInfo();
            field.setType(propertyDescriptor.getType());
            field.setSimpleTypeName(propertyDescriptor.getType().getSimpleName());
            field.setName(propertyDescriptor.getName());
            String comment = commentMap.get(propertyDescriptor.getName());
            if (StringUtils.isBlank(comment)) {
                field.setComment("");
                require = false;
            } else {
                if (comment.contains("|")) {
                    int endIndex = comment.lastIndexOf("|" + Constant.YES_ZH);
                    if (endIndex < 0) {
                        endIndex = comment.lastIndexOf("|" + Constant.YES_EN);
                    }
                    require = endIndex > 0;
                    if (require) {
                        int index = comment.lastIndexOf(":");
                        if (index > 1) {
                            field.setValue(comment.substring(index+1).split("[ \t]+")[0].split("\n")[0].split("\r")[0]);
                        }
                        comment = comment.substring(0, endIndex);
                    }
                }
                field.setComment(comment);
                if(field.getValue()==null||"".equals(field.getValue())){
                    field.setValue(comment);
                }
            }
            field.setRequire(require);
            String returnTypeSimpleNameType = propertyDescriptor.getType().getSimpleName();

            if(!Constant.DATA_TYPE.contains(returnTypeSimpleNameType)&&i<level){
                JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter("@see");
                SeeTagImpl tag = (SeeTagImpl)converter.converter("@see "+returnTypeSimpleNameType,i+1);
                if(tag!=null){
                    field.setFieldInfos(tag.getValues().getFieldInfos());
                }
            }
            if(Constant.LIST_TYPE.contains(returnTypeSimpleNameType)&&i<level){
                // 获取泛型参数类型
                Type genericType = propertyDescriptor.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) genericType;
                    Type[] actualTypeArgs = pType.getActualTypeArguments();
                    String entyType = actualTypeArgs[0].getTypeName();
                    if(!Constant.DATA_TYPE.contains(entyType)){
                        JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter("@see");
                        SeeTagImpl tag = (SeeTagImpl)converter.converter("@see "+entyType,i+1);
                        if(tag!=null){
                            field.setSimpleTypeName(returnTypeSimpleNameType+"<"+entyType.substring(entyType.lastIndexOf(".")+1)+">");
                            field.setFieldInfos(tag.getValues().getFieldInfos());
                        }
                    }
                }
            }
            fields.add(field);
        }
        return fields;
    }

}
