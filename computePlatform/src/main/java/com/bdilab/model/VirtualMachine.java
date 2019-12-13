package com.bdilab.model;

import java.util.Date;

public class VirtualMachine {
    private Long virtualMachineId;

    private Integer cpu;

    private Integer mem;

    private Integer disk;

    private String hostName;

    private String ip;

    private String username;

    private String password;

    private Long clusterId;

    private Date createTime;

    private Date modifyTime;

    private String creator;

    private String modifier;

    private Integer deleted;

    private String openstackInstanceId;

    private String openstackIpId;

    public VirtualMachine(Long virtualMachineId, Integer cpu, Integer mem, Integer disk, String hostName, String ip, String username, String password, Long clusterId, Date createTime, Date modifyTime, String creator, String modifier, Integer deleted, String openstackInstanceId, String openstackIpId) {
        this.virtualMachineId = virtualMachineId;
        this.cpu = cpu;
        this.mem = mem;
        this.disk = disk;
        this.hostName = hostName;
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.clusterId = clusterId;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.creator = creator;
        this.modifier = modifier;
        this.deleted = deleted;
        this.openstackInstanceId = openstackInstanceId;
        this.openstackIpId = openstackIpId;
    }

    public VirtualMachine() {
        super();
    }

    public Long getVirtualMachineId() {
        return virtualMachineId;
    }

    public void setVirtualMachineId(Long virtualMachineId) {
        this.virtualMachineId = virtualMachineId;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMem() {
        return mem;
    }

    public void setMem(Integer mem) {
        this.mem = mem;
    }

    public Integer getDisk() {
        return disk;
    }

    public void setDisk(Integer disk) {
        this.disk = disk;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getOpenstackInstanceId() {
        return openstackInstanceId;
    }

    public void setOpenstackInstanceId(String openstackInstanceId) {
        this.openstackInstanceId = openstackInstanceId == null ? null : openstackInstanceId.trim();
    }

    public String getOpenstackIpId() {
        return openstackIpId;
    }

    public void setOpenstackIpId(String openstackIpId) {
        this.openstackIpId = openstackIpId == null ? null : openstackIpId.trim();
    }
}