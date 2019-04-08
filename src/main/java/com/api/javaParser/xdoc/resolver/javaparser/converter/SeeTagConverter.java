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

import javax.validation.Valid;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 针对@see的转换器
 *
 * @author leaf
 * @date 2017/3/4
 */
public class SeeTagConverter extends DefaultJavaParserTagConverterImpl {

    private Logger log = LoggerFactory.getLogger(SeeTagConverter.class);

    @Override
    public DocTag converter(String comment) {
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
            fields.addAll(this.analysisFields(returnClassz, commentMap));
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

    private List<FieldInfo> analysisFields(Class classz, Map<String, String> commentMap) {

        List<FieldInfo> fields = new ArrayList<FieldInfo>();
        Field[] fiel =  classz.getDeclaredFields();
        for(Field propertyDescriptor:fiel){
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
            //排除掉class属性
            if ("class".equals(propertyDescriptor.getName())) {
                continue;
            }
            FieldInfo field = new FieldInfo();
            field.setType(propertyDescriptor.getType());
            field.setSimpleTypeName(propertyDescriptor.getType().getSimpleName());
            field.setName(propertyDescriptor.getName());
            String comment = commentMap.get(propertyDescriptor.getName());
            if (StringUtils.isBlank(comment)) {
                field.setComment("");
                field.setRequire(false);
                fields.add(field);
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
                field.setRequire(require);
                fields.add(field);
            }
        }
        return fields;
    }

}
