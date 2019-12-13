package com.bdilab.model.JobInfo;

public class WorkflowJobWithBLOBs extends WorkflowJob {
    private String graph;

    private String jobXml;

    private String jobProperties;

    private String resourcePath;

    public WorkflowJobWithBLOBs(Integer id, String workflowName, String jobId, String appPath, String graph, String jobXml, String jobProperties, String resourcePath) {
        super(id, workflowName, jobId, appPath);
        this.graph = graph;
        this.jobXml = jobXml;
        this.jobProperties = jobProperties;
        this.resourcePath = resourcePath;
    }

    public WorkflowJobWithBLOBs() {
        super();
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph == null ? null : graph.trim();
    }

    public String getJobXml() {
        return jobXml;
    }

    public void setJobXml(String jobXml) {
        this.jobXml = jobXml == null ? null : jobXml.trim();
    }

    public String getJobProperties() {
        return jobProperties;
    }

    public void setJobProperties(String jobProperties) {
        this.jobProperties = jobProperties == null ? null : jobProperties.trim();
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath == null ? null : resourcePath.trim();
    }
}