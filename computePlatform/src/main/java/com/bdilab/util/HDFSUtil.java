package com.bdilab.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ClassName HDFSUtil
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/29 21:01
 * @Version 1.0
 */
@Component
public class HDFSUtil {
    private static Configuration conf = new Configuration();
    private static FileSystem fs = null;
    private static String nameNode;

    public void init(String nameNode){
        this.nameNode = nameNode;
    }
    //获取FileSystem
    public FileSystem getFs(String username){
        try{
            System.setProperty("HADOOP_USER_NAME",username);
            System.setProperty("user.name",username);
            conf.set("fs.defaultFS",nameNode);
            conf.set("dfs.client.use.datanode.hostname","true");
            fs = FileSystem.get(conf);
        }catch (IOException e){
            e.printStackTrace();
        }
        return fs;
    }
    //上传文件到hdfs
    public void uploadXMLFile(String uri, String xml) throws Exception {
        Path path = new Path(uri);
        if (fs.exists(path)){
            fs.delete(path,true);
        }
        FSDataOutputStream out = fs.create(path,false);
        XMLUtil.upLoadXMLFile(xml,out);
        out.close();
    }
    //创建文件
    public OutputStream createFile(String fileName)throws Exception{
        Path path = new Path(fileName);
        if (fs.exists(path)){
            fs.delete(path, true);
        }
        FSDataOutputStream out = fs.create(path);
        return out;
    }
    //下载文件
    public InputStream downloadFile(String filepath) throws IOException{
        Path path = new Path(filepath);
        if (!fs.exists(path)){
            System.out.println("file path not exists");
            return null;
        }
        if (fs.isDirectory(path)){
            return searchFile(path);
        } else {
            FSDataInputStream inputStream = fs.open(path);
            return inputStream;
        }
    }
    //搜索文件
    public InputStream searchFile(Path path)throws IOException{
        FileStatus[] status = fs.listStatus(path);
        InputStream in = null;
        if (status==null||status.length==0){
            System.out.println("the directory is empty");
            return null;
        }
        for (FileStatus s: status){
            if (!fs.isDirectory(s.getPath())){
                if (s.getPath().getName().equals("_SUCCESS")){
                    in = fs.open(s.getPath());
                    return in;
                }
            }else {
                in = searchFile(s.getPath());
                if (in != null) return in;
            }
        }
        System.out.println("no file is found");
        return null;
    }

    //复制hdfs上的文件
    public boolean copyFile(String srcpath,String destpath) throws IOException{
        Path src = new Path(srcpath);
        Path dest = new Path(destpath);
        return FileUtil.copy(fs,src,fs,dest,false,conf);
    }
    public void copyFromLocalFile(String srcpath,String destpath)throws IOException{
        Path src = new Path(srcpath);
        Path dest = new Path(destpath);
        fs.copyFromLocalFile(src,dest);
    }
    //创建文件目录
    public boolean mkdirs(String uri) throws IOException{
        Path path = new Path(uri);
        if (fs.exists(path)){
            fs.delete(path,true);
        }
        FsPermission permission = new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL);
        return fs.mkdirs(path,permission);
    }
    //判断路径是否存在
    public boolean exists(String path)throws IOException{
        return fs.exists(new Path(path));
    }

}

