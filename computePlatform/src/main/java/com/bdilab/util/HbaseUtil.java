package com.bdilab.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @ClassName HbaseConnection
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/21 20:28
 * @Version 1.0
 */
@Component
public class HbaseUtil {
    private static Configuration configuration;
    private static Connection connection;
    private static String hbasemaster;
    private static String hbasenodes;

    public void init(String hbasemaster,String hbasenodes){
        this.hbasemaster = hbasemaster;
        this.hbasenodes = hbasenodes;
    }


    public Connection getConnection(){
        if (connection==null || connection.isClosed()){
            try {
                connection = ConnectionFactory.createConnection(getConfiguration());
            }catch (IOException e){
                System.out.println("创建Hbase连接失败");
                e.printStackTrace();
            }
        }
        return connection;
    }
    public Configuration getConfiguration(){
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.master",hbasemaster);
        configuration.set("hbase.zookeeper.quorum",hbasenodes);
        configuration.set("hbase.zookeeper.property.clientPort","2181");
        return configuration;
    }
    public void releaseConnection(){
        if (connection!=null){
            try{
                connection.close();
            }catch (IOException e){
                System.out.println("Hbase连接关闭异常");
            }
        }
    }
}
