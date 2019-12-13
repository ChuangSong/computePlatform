package com.bdilab.controller;

import com.bdilab.common.response.ResponseResult;
import com.bdilab.model.MySqlModel.TableStructure;
import com.bdilab.service.MetaDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName metaDataController
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/9/30 11:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/metadata")
@Api(tags = "Mysql元数据查看API")
public class metaDataController {
    @Autowired
    MetaDataService metaDataService;

    /**
     * 获取数据库列表
     * @return
     */
    @ApiOperation(value = "获取数据库列表")
    @ResponseBody
    @GetMapping("/dblist")
    public ResponseResult getDataBaseList(){
        ResponseResult result = new ResponseResult();
        List<String> data = metaDataService.getDataBaseList();
        result.setMetaData(true,"001","数据库列表查询成功");
        result.setData(data);
        return result;
    }

    /**
     * 从指定数据库中获取表名列表
     * @param dbName
     * @return
     */
    @ApiOperation(value = "从指定数据库中获取表名列表")
    @ResponseBody
    @GetMapping("/tablelist")
    public ResponseResult getTablesFromDataBase(@ApiParam(value = "数据库名称",required = true)
                                                    @RequestParam(required = true) String dbName){
        ResponseResult result = new ResponseResult();
        List<String> data = metaDataService.getTablesFromDB(dbName);
        result.setMetaData(true,"001","表列表查询成功");
        result.setData(data);
        return result;
    }

    /**
     * 从指定数据库中获取所有表的表结构，返回表结构列表
     * @param dbName
     * @return
     */
    @ApiOperation(value = "从指定数据库中获取所有表的表结构，返回表结构列表")
    @ResponseBody
    @GetMapping("/tableMetaData")
    public ResponseResult getMetaDataFromTable(@ApiParam("数据库名称") @RequestParam String dbName){
        ResponseResult result = new ResponseResult();
        List<TableStructure> data = metaDataService.getMetaDataFromTables(dbName);
        result.setMetaData(true,"001","元数据查询成功");
        result.setData(data);
        return result;
    }
    /**
     * 从指定SQL文件中获取所有建表语句的表结构，返回表结构列表
     * @param filePath
     * @return
     * @throws Exception
     */
    @ApiOperation("从指定SQL文件中获取所有建表语句的表结构，返回表结构列表")
    @ResponseBody
    @GetMapping("/sqlfile")
    public ResponseResult getMetaDataFromSQLFile(@ApiParam(value = "SQL文件路径",required = true)
                                                     @RequestParam String filePath) throws Exception{
        List<TableStructure> data = metaDataService.getMetaDataFromSQLFile(filePath);
        ResponseResult result = new ResponseResult();
        result.setMetaData(true,"001","从SQL文件中获取元数据成功");
        result.setData(data);
        return result;
    }


}
