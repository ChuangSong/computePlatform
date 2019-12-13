package com.bdilab.util;

import org.apache.oozie.client.AuthOozieClient;
import org.apache.oozie.client.XOozieClient;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @ClassName OozieUtil
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/24 11:19
 * @Version 1.0
 */
@Component
public class OozieUtil {
    private static String oozieUrl;
    private static String nameNode = "hdfs://cdh-manager:8020";
    private static String jobTracker = "cdh-manager:8032";
    private static XOozieClient wc;
    private static Properties properties;

    public void init(String oozieUrl){
        this.oozieUrl = oozieUrl;
    }

    public XOozieClient getOozieClient(){
        wc = new AuthOozieClient(oozieUrl);
        return wc;
    }
    public Properties getProperties(){
        properties = wc.createConfiguration();
        properties.setProperty("nameNode",nameNode);
        properties.setProperty("jobTracker",jobTracker);
        return properties;
    }

}
