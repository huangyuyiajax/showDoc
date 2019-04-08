package com.api.util;


import com.api.showDoc.model.ShowdocModel;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by huangyuyi on 2019-3-21. 通过模板来生成数据
 */
public class GroupTemplateHelper
{

    private static GroupTemplateHelper INSTANCE ;

    Logger logger= LoggerFactory.getLogger(this.getClass());


    private GroupTemplate groupTemplate;

    public GroupTemplateHelper() throws IOException
    {
        groupTemplate = new GroupTemplate();
    }

    /**
     * 获取该注册器的单例对象
     */
    public synchronized static GroupTemplateHelper getInstance() throws IOException {
        if(INSTANCE==null){
            synchronized(GroupTemplateHelper.class) {
                if (INSTANCE == null){
                    INSTANCE = new GroupTemplateHelper();
                }
            }
        }
        return INSTANCE;
    }

    public String showdocTpl(ShowdocModel showdocModel) throws IOException
    {
        Template t = groupTemplate.getTemplate("/template/showdocTpl.html");
        t.binding("model",showdocModel);
         return  t.render();
    }
}
