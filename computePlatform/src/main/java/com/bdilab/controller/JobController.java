package com.bdilab.controller;

import com.bdilab.common.response.ResponseResult;
import com.bdilab.model.JobInfo.WorkflowInfo;
import com.bdilab.model.JobInfo.WorkflowJobWithBLOBs;
import com.bdilab.model.VirtualMachine;
import com.bdilab.service.JobScheduleService;
import com.bdilab.service.VirtualMachineService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JobController
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/1 15:55
 * @Version 1.0
 */
@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    JobScheduleService jobScheduleService;
    @Autowired
    VirtualMachineService virtualMachineService;

    @GetMapping("/setClusterId")
    @ApiOperation("设置集群ID")
    public ResponseResult setClusterId(@RequestParam String cluster_id){
        Long clusterId = Long.valueOf(cluster_id);
        List<VirtualMachine> machines = virtualMachineService.findByClusterId(clusterId);
        if (machines==null || machines.size()==0){
            return new ResponseResult(false,"failure","集群ID无效，无法获取集群的主机信息",null);

        }
        //若集群id不为空，获取cdh-manager和cdh-agents的ip
        String cdhManager = "";
        for (VirtualMachine machine:machines){
            if (machine.getHostName().equals("cdh-manager")){
                cdhManager += machine.getIp();
            }
        }
        //初始化集群配置文件
        String nameNode = "hdfs://"+cdhManager+":8020";
        String oozieUrl = "http://"+cdhManager+":11000/oozie/";
        jobScheduleService.init(nameNode,oozieUrl);
        Map<String,String> data = new HashMap<>();
        data.put("nameNode", nameNode);
        data.put("oozieUrl", oozieUrl);
        return new ResponseResult(true,"success","集群Oozie和HDFS地址配置成功",data);
    }

    @GetMapping("/getWorkflowList")
    @ApiOperation("获取workflow列表")
    public ResponseResult getWorkflowList(@ApiParam("过滤器(属性=值)")@RequestParam(required = false) String filter,
                                             @ApiParam("起始位置(默认为1)")@RequestParam(required = false,defaultValue = "1") int start,
                                             @ApiParam("长度(默认为50)")@RequestParam(required = false,defaultValue = "50") int len){
        ResponseResult result = new ResponseResult();
        List<WorkflowJob> workflowList = jobScheduleService.selectWorkflowList(filter, start, len);
        //获取数据库中的所有数据，并统计总数
        List<WorkflowJob> allWorkflowJobs = jobScheduleService.selectWorkflowList(filter, 1, -1);
        Map<String,Object> data = new HashMap<>();
        data.put("total",allWorkflowJobs.size());
        data.put("workflowlist",workflowList);
        result.setData(data);
        result.setMetaData(true,"success","获取workflow列表成功");
        return result;
    }

    @GetMapping("/getLocalWorkflowList")
    @ApiOperation("获取本地workflow列表")
    public ResponseResult getLocalWorkflowList(){
        List<com.bdilab.model.JobInfo.WorkflowJob> localWorkflowList = jobScheduleService.getLocalWorkflowList();
        return new ResponseResult(true,"success","获取本地workflow列表成功",localWorkflowList);
    }

    @GetMapping("/getWorkflowInfo")
    @ApiOperation("获取workflow信息")
    public ResponseResult getWorkflowInfo(@ApiParam("作业ID") @RequestParam String jobId){
        ResponseResult result = new ResponseResult();
        WorkflowJob job = jobScheduleService.getWorkflowInfo(jobId);
        result.setData(job);
        result.setMetaData(true,"success","获取workflow信息成功");
        return result;
    }

    @GetMapping("/getActionInfo")
    @ApiOperation("获取action信息")
    public ResponseResult getActionInfo(@ApiParam("动作节点ID") @RequestParam String actionId){
        ResponseResult result = new ResponseResult();
        WorkflowAction action = jobScheduleService.getActionInfo(actionId);
        result.setData(action);
        result.setMetaData(true,"success","获取action信息成功");
        return result;
    }
    @PostMapping("/saveWorkflow")
    @ApiOperation("保存workflow信息")
    public ResponseResult saveWorkflow(@ApiParam("工作流对象") @RequestBody WorkflowInfo job){
        ResponseResult result = new ResponseResult();
        Integer workflow_id = jobScheduleService.saveWorkflow(job);
        Map<String, Integer> data = new HashMap<>();
        data.put("workflow_id", workflow_id);
        result.setMetaData(true,"success","保存workflow信息成功");
        result.setData(data);
        return result;
    }
    @PostMapping("/modifyWorkflow")
    @ApiOperation("修改workflow信息")
    public ResponseResult modifyWorkflow(@ApiParam("工作流ID") @RequestParam Integer workflow_id,
                                         @ApiParam("工作流对象") @RequestBody WorkflowInfo job){
        ResponseResult result = new ResponseResult();
        if (jobScheduleService.modifyWorkflow(workflow_id,job)){
            result.setMetaData(true,"success","修改workflow信息成功");
        }else {
            result.setMetaData(false,"failure","修改workflow信息失败");
        }
        result.setData(null);
        return result;
    }
    @GetMapping("/deleteWorkflow")
    @ApiOperation("删除workflow信息")
    public ResponseResult deleteWorkflow(@ApiParam("工作流ID") @RequestParam Integer workflow_id){
        ResponseResult result = new ResponseResult();
        if (jobScheduleService.deleteWorkflow(workflow_id)){
            result.setMetaData(true,"success","删除workflow信息成功");
        }else {
            result.setMetaData(false,"failure","删除workflow信息失败");
        }
        result.setData(null);
        return result;
    }
    @GetMapping("/getLocalWorkflowInfo")
    @ApiOperation("获取本地workflow信息")
    public ResponseResult getLocalWorkflow(@ApiParam("工作流ID") @RequestParam Integer workflow_id){
        ResponseResult result = new ResponseResult();
        WorkflowJobWithBLOBs localWorkflowInfo = jobScheduleService.getLocalWorkflowInfo(workflow_id);
        result.setData(localWorkflowInfo);
        if (localWorkflowInfo!=null){
            result.setMetaData(true,"success","获取本地workflow信息成功");
        }else {
            result.setMetaData(false,"failure","获取本地workflow信息失败");
        }
        return result;
    }
    @PostMapping("/submitWorkflow")
    @ApiOperation("提交workflow")
    public ResponseResult submitWorkflow(@ApiParam("工作流ID") @RequestParam Integer workflow_id){
        ResponseResult result = new ResponseResult();
        String jobId = jobScheduleService.submitWorkflow(workflow_id);
        Map<String,String> data = new HashMap<>();
        data.put("jobId",jobId);
        result.setData(data);
        if (jobId==null){
            result.setMetaData(false,"failure","作业提交失败");
        }else {
            result.setMetaData(true,"success","作业提交成功");
        }
        return result;
    }

    @GetMapping("/suspendJob")
    @ApiOperation("中止作业")
    public ResponseResult suspendJob(@ApiParam("作业ID") @RequestParam String jobId){
        ResponseResult result = new ResponseResult();
        String status = jobScheduleService.suspendJob(jobId);
        Map<String,String> data = new HashMap<>();
        data.put("status",status);
        result.setData(data);
        result.setMetaData(true,"success","中止作业成功");
        return result;
    }

    @GetMapping("/resumeJob")
    @ApiOperation("重新运行作业")
    public ResponseResult resumeJob(@ApiParam("作业ID") @RequestParam String jobId){
        ResponseResult result = new ResponseResult();
        String status = jobScheduleService.resumeJob(jobId);
        Map<String,String> data = new HashMap<>();
        data.put("status",status);
        result.setData(data);
        result.setMetaData(true,"success","重新运行作业成功");
        return result;
    }

    @GetMapping("/killJob")
    @ApiOperation("杀死作业")
    public ResponseResult killJob(@ApiParam("作业ID") @RequestParam String jobId){
        ResponseResult result = new ResponseResult();
        String status = jobScheduleService.killJob(jobId);
        Map<String,String> data = new HashMap<>();
        data.put("status",status);
        result.setData(data);
        result.setMetaData(true,"success","杀死作业成功");
        return result;
    }

    @GetMapping("/getJobDefination")
    @ApiOperation("获取Job定义（即XML字段）")
    public ResponseResult getJobDefination(@ApiParam("作业ID")@RequestParam String jobId){
        ResponseResult result = new ResponseResult();
        String jobDefinition = jobScheduleService.getJobDefinition(jobId);
        result.setData(jobDefinition);
        result.setMetaData(true,"success","查询workflow定义成功");
        return result;
    }

    @GetMapping("/getJobLog")
    @ApiOperation("获取Job日志")
    public ResponseResult getJobLog(@ApiParam("作业ID")@RequestParam String jobId){
        ResponseResult result = new ResponseResult();
        String jobLog = jobScheduleService.getJobLog(jobId);
        result.setData(jobLog);
        result.setMetaData(true,"success","查询workflow日志成功");
        return result;
    }
}
