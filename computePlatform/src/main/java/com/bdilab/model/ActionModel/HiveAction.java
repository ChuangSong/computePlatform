package com.bdilab.model.ActionModel;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HiveAction
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/4 18:45
 * @Version 1.0
 */
public class HiveAction extends WorkflowAction {
    //存放的脚本文件
    private String scriptName;
    //相关参数配置
    private Map<String,String> properties;
    private List<String> params;

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
