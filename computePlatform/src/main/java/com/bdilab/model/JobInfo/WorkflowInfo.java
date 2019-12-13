package com.bdilab.model.JobInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WorkflowInfo
 * @Description 前端传递的workflow对象
 * @Author ChuangSong_Zheng
 * @Date 2019/10/29 10:43
 * @Version 1.0
 */
@ApiModel
public class WorkflowInfo {
    //workflow名称
    @ApiModelProperty("workflow名称")
    private String workflow_name;
    //生成的xml字符串
    @ApiModelProperty("xml字符串")
    private String xml;
    //属性配置
    @ApiModelProperty("自定义的属性配置")
    private Map<String,String> properties;
    //资源路径
    @ApiModelProperty("全部资源路径列表")
    private List<String> resourcePathes;
    //图形信息
    @ApiModelProperty("要保存的图形信息")
    private String graph;




    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Map<String,String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String,String> properties) {
        this.properties = properties;
    }

    public List<String> getResourcePathes() {
        return resourcePathes;
    }

    public void setResourcePathes(List<String> resourcePathes) {
        this.resourcePathes = resourcePathes;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public String getWorkflow_name() {
        return workflow_name;
    }

    public void setWorkflow_name(String workflow_name) {
        this.workflow_name = workflow_name;
    }
}
