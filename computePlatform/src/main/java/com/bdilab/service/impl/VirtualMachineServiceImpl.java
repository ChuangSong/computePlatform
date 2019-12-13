package com.bdilab.service.impl;

import com.bdilab.mapper.VirtualMachineMapper;
import com.bdilab.model.VirtualMachine;
import com.bdilab.service.VirtualMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName VirtualMachineServiceImpl
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/19 20:14
 * @Version 1.0
 */
@Service("virtualMachineService")
public class VirtualMachineServiceImpl implements VirtualMachineService {
    @Autowired
    VirtualMachineMapper mapper;

    @Override
    public List<VirtualMachine> findByClusterId(Long clusterId) {
        return mapper.selectByClusterId(clusterId);
    }

    @Override
    public VirtualMachine findByClusterIdAndHostName(Long clusterId, String hostName) {
        return mapper.selectByClusterIdAndHostName(clusterId, hostName);
    }
}
