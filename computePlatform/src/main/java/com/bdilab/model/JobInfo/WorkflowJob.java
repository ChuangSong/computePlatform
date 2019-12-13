package com.bdilab.model.JobInfo;

public class WorkflowJob {
    private Integer id;

    private String workflowName;

    private String jobId;

    private String appPath;

    public WorkflowJob(Integer id, String workflowName, String jobId, String appPath) {
        this.id = id;
        this.workflowName = workflowName;
        this.jobId = jobId;
        this.appPath = appPath;
    }

    public WorkflowJob() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName == null ? null : workflowName.trim();
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath == null ? null : appPath.trim();
    }
}