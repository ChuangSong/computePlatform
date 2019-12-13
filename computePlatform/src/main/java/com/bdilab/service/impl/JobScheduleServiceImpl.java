package com.bdilab.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.mapper.WorkflowJobMapper;
import com.bdilab.model.JobInfo.WorkflowJobWithBLOBs;
import com.bdilab.model.JobInfo.WorkflowInfo;
import com.bdilab.service.JobScheduleService;
import com.bdilab.util.HDFSUtil;
import com.bdilab.util.OozieUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.oozie.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName JobScheduleServiceImpl
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/28 21:32
 * @Version 1.0
 */
@Service
public class JobScheduleServiceImpl implements JobScheduleService {
    @Autowired
    OozieUtil oozieUtil;
    @Autowired
    HDFSUtil hdfsUtil;
    @Autowired
    WorkflowJobMapper workflowJobMapper;

    //默认主工作区
    private static String workspaces = "/oozie/workspaces";
    //任务存放路径
    private static String AppPath = null;
    private static XOozieClient client;
    /**
     * 初始化配置
     * @param nameNode
     * @param oozieUrl
     */
    public void init(String nameNode,String oozieUrl){
        oozieUtil.init(oozieUrl);
        hdfsUtil.init(nameNode);
        client = oozieUtil.getOozieClient();
    }
    /**
     * 保存workflow信息
     * @param job
     * @return
     */
    public Integer saveWorkflow(WorkflowInfo job){
        //workflow名称
        String workflow_name = job.getWorkflow_name();
        //生成的xml文件
        String xml = job.getXml();
        //自定义属性配置
        Map<String,String> extraProperties = job.getProperties();
        //资源路径
        List<String> resourcePathes = job.getResourcePathes();
        //图形信息
        String graph = job.getGraph();
        //转换成JSON字符串进行存储
        String properties = JSONObject.toJSONString(extraProperties);
        String path = JSONObject.toJSONString(resourcePathes);
        WorkflowJobWithBLOBs jobWithBLOBs = new WorkflowJobWithBLOBs(null,workflow_name,null,null,graph,xml,properties,path);
        workflowJobMapper.insertSelective(jobWithBLOBs);
        return jobWithBLOBs.getId();
    }

    /**
     * 获取本地workflow信息
     * @param workflow_id
     * @return
     */
    public WorkflowJobWithBLOBs getLocalWorkflowInfo(Integer workflow_id){
        return workflowJobMapper.selectByPrimaryKey(workflow_id);
    }
    /**
     * 修改workflow信息
     * @param job
     */
    public boolean modifyWorkflow(Integer workflow_id,WorkflowInfo job){
        //workflow名称
        String workflow_name = job.getWorkflow_name();
        //生成的xml文件
        String xml = job.getXml();
        //自定义属性配置
        Map<String,String> extraProperties = job.getProperties();
        //资源路径
        List<String> resourcePathes = job.getResourcePathes();
        //图形信息
        String graph = job.getGraph();
        //转换成JSON字符串进行存储
        String properties = JSONObject.toJSONString(extraProperties);
        String path = JSONObject.toJSONString(resourcePathes);
        WorkflowJobWithBLOBs jobWithBLOBs = new WorkflowJobWithBLOBs(workflow_id,workflow_name,null,null,graph,xml,properties,path);
        int update = workflowJobMapper.updateByPrimaryKeySelective(jobWithBLOBs);
        return update==0?false:true;
    }

    /**
     * 删除workflow信息
     * @param workflow_id
     * @return
     */
    public boolean deleteWorkflow(Integer workflow_id){
        int delete = workflowJobMapper.deleteByPrimaryKey(workflow_id);
        return delete==0?false:true;
    }
    /**
     * 提交并运行Workflow作业
     * @param workflow_id
     * @return
     */
    @Override
    public String submitWorkflow(Integer workflow_id) {
        WorkflowJobWithBLOBs jobWithBLOBs = workflowJobMapper.selectByPrimaryKey(workflow_id);
        //生成的xml文件
        String workflowXml = jobWithBLOBs.getJobXml();
        Map<String, String> xmlMap = new HashMap<>();
        xmlMap.put("workflow",workflowXml);
        //自定义属性配置
        String JSONProperties = jobWithBLOBs.getJobProperties();
        Map<String,String> extraProperties = JSONObject.parseObject(JSONProperties,Map.class);
        //资源路径
        String JSONPath = jobWithBLOBs.getResourcePath();
        List<String> resourcePathes = JSONObject.parseObject(JSONPath,List.class);
        //提交作业
        String jobId = submitJob(resourcePathes,xmlMap,extraProperties);
        //更新jobId和AppPath字段
        com.bdilab.model.JobInfo.WorkflowJob workflowJob = jobWithBLOBs;
        workflowJob.setJobId(jobId);
        workflowJob.setAppPath(AppPath);
        workflowJobMapper.updateByPrimaryKey(workflowJob);
        return jobId;
    }

    /**
     * 暂停/挂起作业
     * @param jobId
     */
    public String suspendJob(String jobId){
        try {
            client.suspend(jobId);
            return client.getStatus(jobId);
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 恢复/重新运行作业
     * @param jobId
     */
    public String resumeJob(String jobId){
        try {
            client.resume(jobId);
            return client.getStatus(jobId);
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 杀死作业
     * @param jobId
     */
    public String killJob(String jobId){
        try {
            client.kill(jobId);
            return client.getStatus(jobId);
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 提交作业给Oozie
     * @param resourcePathes
     * @param xmlMap
     * @param propertyString
     * @return
     */
    private String submitJob(List<String> resourcePathes, Map<String,String> xmlMap, Map<String,String> propertyString){
        String jobId = null;

        //任务工作区
        String jobPath = workspaces+"/oozie-job-"+System.currentTimeMillis();

        try{
            //用admin用户连接文件系统
            FileSystem fs = hdfsUtil.getFs("admin");
            String desPath = fs.getWorkingDirectory()+jobPath;
            AppPath = desPath;
            //创建任务工作区
            hdfsUtil.mkdirs(desPath);
            hdfsUtil.mkdirs(desPath+"/lib");
            for (String resourcePath:resourcePathes){
                if (!hdfsUtil.exists(resourcePath)){
                    System.out.println("找不到该资源路径："+resourcePath);
                    return null;
                }
                //上传计算资源(要运行的jar包或者脚本文件)
                hdfsUtil.copyFile(resourcePath,desPath+"/lib");
            }
            //上传workflow.xml到任务工作区
            if (xmlMap.containsKey("workflow")){
                hdfsUtil.uploadXMLFile(desPath+"/workflow.xml",xmlMap.get("workflow"));
            }
            //上传coordinator.xml到任务工作区
            if (xmlMap.containsKey("coordinator")){
                hdfsUtil.uploadXMLFile(desPath+"/coordinator.xml",xmlMap.get("coordinator"));
            }
            //加载默认配置文件
            Properties properties = oozieUtil.getProperties();
            properties.setProperty("oozie.wf.application.path",desPath);
            for (String key:propertyString.keySet()){
                properties.setProperty(key,propertyString.get(key));
            }
            //创建配置文件并上传
            OutputStream outputStream = hdfsUtil.createFile(desPath + "/job.properties");
            IOUtils.copyBytes(new ByteArrayInputStream(properties.toString().getBytes()),outputStream,4096,false);
            IOUtils.closeStream(outputStream);
            //运行任务
            jobId = client.run(properties);
            System.out.println(jobId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jobId;
    }


    /**
     * 过滤查询workflow列表
     * @param filter
     * @param start
     * @param len
     * @return
     */
    public List<WorkflowJob> selectWorkflowList(String filter,int start,int len){
        try{
            List<WorkflowJob> jobsInfo = client.getJobsInfo(filter, start, len);
            return jobsInfo;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询本地workflow列表
     * @return
     */
    public List<com.bdilab.model.JobInfo.WorkflowJob> getLocalWorkflowList(){
        return workflowJobMapper.selectAllWorkflow();
    }
    /**
     * 获取workflow定义
     * @param jobId
     * @return
     */
    public String getJobDefinition(String jobId){
        try{
            String jobDefinition = client.getJobDefinition(jobId);
            return jobDefinition;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取workflow日志
     * @param jobId
     * @return
     */
    public String getJobLog(String jobId){
        try{
            String jobLog = client.getJobLog(jobId);
            return jobLog;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取worklfow作业信息
     * @param jobId
     * @return
     */
    public WorkflowJob getWorkflowInfo(String jobId){
        try{
            WorkflowJob jobInfo = client.getJobInfo(jobId);
            return jobInfo;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取action信息
     * @param actionId
     * @return
     */
    public WorkflowAction getActionInfo(String actionId){
        try{
            WorkflowAction workflowActionInfo = client.getWorkflowActionInfo(actionId);
            return workflowActionInfo;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取coordinator作业信息
     * @param jobId
     * @return
     */
    public CoordinatorJob getCoordJobInfo(String jobId){
        try{
            CoordinatorJob coordJobInfo = client.getCoordJobInfo(jobId);
            return coordJobInfo;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取coordinator作业列表信息
     * @param filter
     * @param start
     * @param len
     * @return
     */
    public List<CoordinatorJob> getCoordJobsInfo(String filter,int start,int len){
        try{
            List<CoordinatorJob> coordJobsInfo = client.getCoordJobsInfo(filter, start, len);
            return coordJobsInfo;
        }catch (OozieClientException e){
            e.printStackTrace();
            return null;
        }
    }
}
