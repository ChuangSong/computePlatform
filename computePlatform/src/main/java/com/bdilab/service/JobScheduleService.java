package com.bdilab.service;

import com.bdilab.model.JobInfo.WorkflowInfo;
import com.bdilab.model.JobInfo.WorkflowJobWithBLOBs;
import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowJob;

import java.util.List;

public interface JobScheduleService {
    void init(String nameNode,String oozieUrl);
    Integer saveWorkflow(WorkflowInfo job);
    WorkflowJobWithBLOBs getLocalWorkflowInfo(Integer workflow_id);
    boolean modifyWorkflow(Integer workflow_id,WorkflowInfo job);
    boolean deleteWorkflow(Integer workflow_id);
    String submitWorkflow(Integer workflow_id);
    String suspendJob(String jobId);
    String resumeJob(String jobId);
    String killJob(String jobId);
    List<WorkflowJob> selectWorkflowList(String filter, int start, int len);
    List<com.bdilab.model.JobInfo.WorkflowJob> getLocalWorkflowList();
    WorkflowJob getWorkflowInfo(String jobId);
    WorkflowAction getActionInfo(String actionId);
    String getJobDefinition(String jobId);
    String getJobLog(String jobId);
    CoordinatorJob getCoordJobInfo(String jobId);
    List<CoordinatorJob> getCoordJobsInfo(String filter,int start,int len);
}
