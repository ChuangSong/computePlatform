package com.bdilab.service.impl;

import com.bdilab.model.MySqlModel.Field;
import com.bdilab.model.MySqlModel.TableStructure;
import com.bdilab.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName metaDataServiceImpl
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/9/30 11:22
 * @Version 1.0
 */
@Service
public class MetaDataServiceImpl implements MetaDataService {
    @Autowired
    ApplicationContext applicationContext;

    /**
     * 获取所有的数据库列表
     * @return
     */
    @Override
    public List<String> getDataBaseList() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        List<String> dbNames = new ArrayList<>();
        try{
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getCatalogs();
            while (resultSet.next()){
                dbNames.add(resultSet.getString("TABLE_CAT"));
            }
        }catch (SQLException e){
            System.out.println("数据源连接错误");
        }

        return dbNames;
    }

    /**
     * 从指定数据库中获取所有表，返回表名列表
     * @param dbName
     * @return
     */
    @Override
    public List<String> getTablesFromDB(String dbName){
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        List<String> tableNames = new ArrayList<>();
        try{
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(dbName,null,null,new String[]{"TABLE"});
            while (resultSet.next()){
                tableNames.add(resultSet.getString("TABLE_NAME"));
            }
        }catch (SQLException e){
            System.out.println("数据源连接错误");
        }

        return tableNames;
    }

    /**
     * 从指定数据库中读取所有表的表结构，返回表结构列表
     * @return
     */
    @Override
    public List<TableStructure> getMetaDataFromTables(String dbName) {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        List<TableStructure> tables = new ArrayList<>();
        //获取数据库的所有表名
        List<String> tableNames = getTablesFromDB(dbName);
        ResultSet resultSet = null;
        try{
            Connection connection = dataSource.getConnection();
            //获取数据库元数据
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            //获取数据库地址
            String url = databaseMetaData.getURL();

            //遍历查询每张表信息
            for (String tableName: tableNames){
                //获取查询结果集
                resultSet = connection.prepareStatement("select * from "+tableName).executeQuery();
                //获取结果集元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                //构建表结构
                TableStructure tableStructure = new TableStructure();
                tableStructure.setTableName(tableName);
                tableStructure.setLocation(url);
                //存储表结构的属性列表
                List<Field> tableField = new ArrayList<>();
                //获取表数据的列名，类型名和是否为空
                for (int i=0;i<metaData.getColumnCount();i++){
                    Field field = new Field();
                    field.setName(metaData.getColumnName(i+1));
                    field.setType(metaData.getColumnTypeName(i+1));
                    field.setLength(metaData.getPrecision(i+1));
                    field.setNullable(metaData.isNullable(i+1)==0?false:true);
                    tableField.add(field);
                }
                tableStructure.setTableField(tableField);
                //存储表结构的样例数据
                List<String> tableData = new ArrayList<>();
                //获取表中所有数据
                while (resultSet.next()){
                    String data = "(";
                    for (int i=0;i<metaData.getColumnCount();i++){
                        data = data + resultSet.getObject(i+1) + ",";
                    }
                    data = data.substring(0,data.length()-1)+")";
                    tableData.add(data);
                }
                tableStructure.setTableData(tableData);
                tables.add(tableStructure);
            }
            connection.close();
            resultSet.close();
        }catch (SQLException e){
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * 解析SQL文件，返回数据表结构列表
     * @param filePath
     * @return
     */
    @Override
    public List<TableStructure> getMetaDataFromSQLFile(String filePath) {
        //从指定sql文件中提取所有的建表语句，转为字符串列表
        List<String> tableCreateCommands = new ArrayList<>();
        //从指定sql文件中提取所有的插入表语句，转为字符串列表
        List<String> tableInsertCommands = new ArrayList<>();
        //存储当前建表语句
        StringBuilder tableCreateCommand = new StringBuilder();
        //是否写入建表语句的标志
        boolean writeCreateMessage = false;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;
            //读取SQL文件的每一行
            while ((line=reader.readLine())!=null){
                //若当前行匹配创建表模式，将其写入建表语句
                if (line.toLowerCase().matches("create table(.*)")){
                    writeCreateMessage = true;
                }
                if (writeCreateMessage){
                    tableCreateCommand.append(line);
                    //建表语句结束标志
                    if (line.contains(";")){
                        writeCreateMessage = false;
                        tableCreateCommands.add(tableCreateCommand.toString());
                        tableCreateCommand = new StringBuilder();
                    }
                }
                if (line.toLowerCase().matches("insert into(.*)")){
                    tableInsertCommands.add(line);
                }
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //构建表结构模型列表，并返回
        List<TableStructure> tables = new ArrayList<>();
        //将建表语句转化为表结构模型
        for (String str: tableCreateCommands){
            TableStructure table = new TableStructure();
            List<Field> tableField = new ArrayList<>();
            //过滤出表名
            String tableName = str.trim().split(" ")[2].replaceAll("`","");
            //过滤出字段定义语句
            String[] propertyString = str.substring(str.indexOf("(")+1,str.lastIndexOf(")")).trim().split(",");
            //将字段定义语句转化为字段模型
            for (String prop: propertyString){
                if (prop.toUpperCase().contains("NULL")){
                    String[] fields = prop.trim().split(" ",3);
                    Field field = new Field();
                    field.setName(fields[0].replaceAll("`",""));
                    field.setType(fields[1].replaceAll("[^a-z^A-Z]",""));
                    String length = fields[1].replaceAll("[^0-9]","");
                    field.setLength(Integer.valueOf(length.equals("")?"0":length));
                    field.setNullable(prop.toUpperCase().contains("NOT NULL")?false:true);
                    tableField.add(field);
                }
            }
            table.setTableName(tableName);
            table.setTableField(tableField);
            table.setLocation(filePath);
            tables.add(table);
        }
        //存储表名及插入的数据
        HashMap<String,List<String>> tableData = new HashMap<>();
        //解析插入语句
        for (String str: tableInsertCommands){
            String tableName = str.trim().split(" ")[2].replaceAll("`","");
            String data = str.substring(str.indexOf("("),str.lastIndexOf(")")+1).trim();

            if (tableData.containsKey(tableName)){
                tableData.get(tableName).add(data);
            }else {
                List<String> insertData = new ArrayList<>();
                insertData.add(data);
                tableData.put(tableName,insertData);
            }
        }
        //将整理好的表数据加入到对应的表结构模型中
        for (TableStructure tableStructure: tables){
            String tableName = tableStructure.getTableName();
            if (tableData.containsKey(tableName)){
                tableStructure.setTableData(tableData.get(tableName));
            }
        }

        return tables;
    }

}
