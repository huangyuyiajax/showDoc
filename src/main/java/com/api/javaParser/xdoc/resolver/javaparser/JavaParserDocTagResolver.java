package com.api.javaParser.xdoc.resolver.javaparser;

import com.api.javaParser.xdoc.model.ApiAction;
import com.api.javaParser.xdoc.resolver.DocTagResolver;
import com.api.javaParser.xdoc.resolver.javaparser.converter.JavaParserTagConverter;
import com.api.javaParser.xdoc.tag.DocTag;
import com.api.javaParser.xdoc.utils.ClassMapperUtils;
import com.api.javaParser.xdoc.utils.CommentUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.api.javaParser.xdoc.framework.Framework;
import com.api.javaParser.xdoc.model.ApiModule;
import com.api.javaParser.xdoc.resolver.IgnoreApi;
import com.api.javaParser.xdoc.resolver.javaparser.converter.JavaParserTagConverterRegistrar;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于开源JavaParser实现的解析
 * <p>
 *
 * @author huangyuyi
 * @date 2017/4/1 0001
 */
public class JavaParserDocTagResolver implements DocTagResolver {

    private Logger log = LoggerFactory.getLogger(JavaParserDocTagResolver.class);

    @Override
    public List<ApiModule> resolve(List<String> allFiles,List<String> files, Framework framework) {

        //先缓存类文件信息
        for (String file : allFiles) {
            log.info("扫描源码文件:{}", file);
            try {
                FileInputStream in = new FileInputStream(file);
                CompilationUnit cu = JavaParser.parse(in);
                if (cu.getTypes().size() <= 0) {
                    continue;
                }

                TypeDeclaration typeDeclaration = cu.getTypes().get(0);
                final Class<?> moduleType = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + typeDeclaration.getNameAsString());
                IgnoreApi ignoreApi = moduleType.getAnnotation(IgnoreApi.class);
                if (ignoreApi == null) {
                    //缓存"包名+类名"跟对应的.java文件的位置映射关系
                    ClassMapperUtils.put(moduleType.getName(), file);
                    //缓存"类名"跟对应的.java文件的位置映射关系
                    ClassMapperUtils.put(moduleType.getSimpleName(), file);
                }
            } catch (Exception e) {
                log.warn("读取文件失败:{}, {}", file, e.getMessage());
            }
        }

        List<ApiModule> apiModules = new LinkedList<ApiModule>();

        for (String file : files) {
            try {
                FileInputStream in = new FileInputStream(file);
                CompilationUnit cu = JavaParser.parse(in);
                if (cu.getTypes().size() <= 0) {
                    continue;
                }

                TypeDeclaration typeDeclaration = cu.getTypes().get(0);
                final Class<?> moduleType = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + typeDeclaration.getNameAsString());


                if (!framework.support(moduleType)) {
                    continue;
                }

                IgnoreApi ignoreApi = moduleType.getAnnotation(IgnoreApi.class);
                if (ignoreApi != null) {
                    continue;
                }
                log.info("解析源码文件:{}", file);
                final ApiModule apiModule = new ApiModule();
                apiModule.setType(moduleType);
                if (typeDeclaration.getComment().isPresent()) {
                    String commentText = CommentUtils.parseCommentText(typeDeclaration.getComment().get().getContent());
                    commentText = commentText.split("\n")[0].split("\r")[0];
                    apiModule.setComment(commentText);
                }

                new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(MethodDeclaration m, Void arg) {
                        Method method = parseToMenthod(moduleType, m);
                        if (method == null) {
                            log.warn("查找不到方法:{}.{}", moduleType.getSimpleName(), m.getNameAsString());
                            return;
                        }

                        IgnoreApi ignoreApi = method.getAnnotation(IgnoreApi.class);
                        if (ignoreApi != null || !m.getComment().isPresent()) {
                            return;
                        }

                        List<String> comments = CommentUtils.asCommentList(StringUtils.defaultIfBlank(m.getComment().get().getContent(), ""));
                        List<DocTag> docTagList = new ArrayList<DocTag>(comments.size());

                        for (int i = 0; i < comments.size(); i++) {
                            String c = comments.get(i);
                            //把第一个字母转为小写
                            if(c.length()>1){
                                String first = String.valueOf(c.charAt(1));
                                c = c.replaceFirst(first,first.toLowerCase());
                            }
                            String tagType = CommentUtils.getTagType(c);
                            if (StringUtils.isBlank(tagType)) {
                                continue;
                            }
                            JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter(tagType);
                            DocTag docTag = converter.converter(c);
                            if (docTag != null) {
                                docTagList.add(docTag);
                            } else {
                                log.warn("识别不了:{}", c);
                            }
                        }

                        ApiAction apiAction = new ApiAction();
                        if (m.getComment().isPresent()) {
                            apiAction.setComment(CommentUtils.parseCommentText(m.getComment().get().getContent()));
                        }
                        apiAction.setName(m.getNameAsString());
                        apiAction.setDocTags(docTagList);
                        apiAction.setMethod(method);
                        apiModule.getApiActions().add(apiAction);

                        super.visit(m, arg);
                    }
                }.visit(cu, null);

                apiModules.add(apiModule);

            } catch (Exception e) {
                log.warn("解析{}失败:{}", file, e.getMessage());
                continue;
            }
        }
        return apiModules;
    }

    /**
     * 获取指定方法的所有入参类型,便于反射
     *
     * @param declaration
     * @return
     */
    private static Method parseToMenthod(Class type, MethodDeclaration declaration) {
        List<Parameter> parameters = declaration.getParameters();
        parameters = parameters == null ? new ArrayList<Parameter>(0) : parameters;
        Method[] methods = type.getDeclaredMethods();
        for (Method m : methods) {
            if (!m.getName().equals(declaration.getNameAsString())) {
                continue;
            }
            if (m.getParameterTypes().length != parameters.size()) {
                continue;
            }

            boolean b = true;

            for (int j = 0; j < m.getParameterTypes().length; j++) {
                Class<?> paramClass = m.getParameterTypes()[j];
                Type ptype = parameters.get(j).getType();
                if (ptype == null) {
                    continue;
                }
                String paranTypeName = ptype.toString();
                int index = paranTypeName.lastIndexOf(".");
                if (index > 0) {
                    paranTypeName = paranTypeName.substring(index + 1);
                }
                //处理泛型
                index = paranTypeName.indexOf("<");
                if (index > 0) {
                    paranTypeName = paranTypeName.substring(0, index);
                }

                if (!paramClass.getSimpleName().equals(paranTypeName)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                return m;
            }
        }
        return null;
    }
}
