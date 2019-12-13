package com.bdilab.mapper;

import com.bdilab.model.VirtualMachine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VirtualMachineMapper {
    int deleteByPrimaryKey(Long virtualMachineId);

    int insert(VirtualMachine record);

    int insertSelective(VirtualMachine record);

    VirtualMachine selectByPrimaryKey(Long virtualMachineId);

    int updateByPrimaryKeySelective(VirtualMachine record);

    int updateByPrimaryKey(VirtualMachine record);

    List<VirtualMachine> selectByClusterId(Long clusterId);

    VirtualMachine selectByClusterIdAndHostName(@Param("clusterId") Long clusterId, @Param("hostName") String hostName);
}