package com.bdilab.mapper;

import com.bdilab.model.JobInfo.WorkflowJob;
import com.bdilab.model.JobInfo.WorkflowJobWithBLOBs;

import java.util.List;

public interface WorkflowJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkflowJobWithBLOBs record);

    int insertSelective(WorkflowJobWithBLOBs record);

    WorkflowJobWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkflowJobWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(WorkflowJobWithBLOBs record);

    int updateByPrimaryKey(WorkflowJob record);

    List<com.bdilab.model.JobInfo.WorkflowJob> selectAllWorkflow();
}