package com.bdilab.model.ActionModel;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MapReduceAction
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/4 17:19
 * @Version 1.0
 */
public class MapReduceAction extends WorkflowAction {
    //要运行的jar包的存放路径
    private String JarName;
    //相关参数配置
    private Map<String,String> properties;
    //文件列表
    private List<String> files;
    //archives列表
    private List<String> archives;

    public String getJarName() {
        return JarName;
    }

    public void setJarName(String jarName) {
        JarName = jarName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getArchives() {
        return archives;
    }

    public void setArchives(List<String> archives) {
        this.archives = archives;
    }
}
