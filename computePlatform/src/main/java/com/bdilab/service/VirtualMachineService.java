package com.bdilab.service;

import com.bdilab.model.VirtualMachine;

import java.util.List;

public interface VirtualMachineService {
    List<VirtualMachine> findByClusterId(Long id);

    VirtualMachine findByClusterIdAndHostName(Long clusterId, String hostName);
}
