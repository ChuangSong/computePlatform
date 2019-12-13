package com.bdilab.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @ClassName HiveUtil
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/25 10:37
 * @Version 1.0
 */
@Component
public class HiveUtil {
    private static String hiveUrl = "";
    private static String user = "";
    private static String password = "";
    private static Connection connection;

    public void init(String hiveUrl){
        this.hiveUrl = hiveUrl;
    }
    public Connection getConnection(){
        try{
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            if (connection==null || connection.isClosed()){
                connection = DriverManager.getConnection(hiveUrl, user, password);
            }
        }catch (ClassNotFoundException e){
            System.out.println("找不到HiveDriver类");
        }catch (SQLException e){
            System.out.println("Hive连接出错");
        }
        return connection;
    }
    public void releaseConnection(){
        if (connection!=null){
            try{
                connection.close();
            }catch (SQLException e){
                System.out.println("Hive连接关闭异常");
            }
        }
    }
}
