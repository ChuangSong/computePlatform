package com.bdilab.common.configuration;

import org.springframework.stereotype.Component;

/**
 * @ClassName ClusterConfig
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/23 15:40
 * @Version 1.0
 */
@Component
public class ClusterConfig {
    private static String hbasenodes;
    private static String hbasemaster;
    private static String oozieUrl;
    private static String nameNode;
    private static String jobTracker;

    public static String getHbasenodes() {
        return hbasenodes;
    }

    public static void setHbasenodes(String hbasenodes) {
        ClusterConfig.hbasenodes = hbasenodes;
    }

    public static String getHbasemaster() {
        return hbasemaster;
    }

    public static void setHbasemaster(String hbasemaster) {
        ClusterConfig.hbasemaster = hbasemaster;
    }

    public static String getOozieUrl() {
        return oozieUrl;
    }

    public static void setOozieUrl(String oozieUrl) {
        ClusterConfig.oozieUrl = oozieUrl;
    }

    public static String getNameNode() {
        return nameNode;
    }

    public static void setNameNode(String nameNode) {
        ClusterConfig.nameNode = nameNode;
    }

    public static String getJobTracker() {
        return jobTracker;
    }

    public static void setJobTracker(String jobTracker) {
        ClusterConfig.jobTracker = jobTracker;
    }
}
