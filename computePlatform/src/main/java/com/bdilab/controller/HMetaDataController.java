package com.bdilab.controller;

import com.bdilab.common.response.ResponseResult;
import com.bdilab.model.HbaseModel.HCell;
import com.bdilab.model.HbaseModel.HRowData;
import com.bdilab.model.HbaseModel.HTableStructure;
import com.bdilab.model.VirtualMachine;
import com.bdilab.service.HMetaDataService;
import com.bdilab.service.VirtualMachineService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HMetaDataController
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/23 10:24
 * @Version 1.0
 */
@Api(tags = "Hbase元数据查看API")
@RestController
@RequestMapping("/hmetadata")
public class HMetaDataController {
    @Autowired
    HMetaDataService metaDataService;
    @Autowired
    VirtualMachineService virtualMachineService;


    @GetMapping("/setClusterId")
    @ApiOperation("设置集群ID")
    public ResponseResult setClusterId(@RequestParam String cluster_id){
        Long clusterId = Long.valueOf(cluster_id);
        List<VirtualMachine> machines = virtualMachineService.findByClusterId(clusterId);
        if (machines==null || machines.size()==0){
            return new ResponseResult(false,"failure","集群ID无效，无法获取集群的主机信息",null);

        }
        //若集群id不为空，获取cdh-manager和cdh-agents的ip
        String cdhManager = "";
        String cdhAgents = "";
        for (VirtualMachine machine:machines){
            if (machine.getHostName().equals("cdh-manager")){
                cdhManager += machine.getIp();
            }
            if (machine.getHostName().matches("cdh-agent(.*)")){
                cdhAgents += machine.getIp()+",";
            }
        }
        //初始化集群配置
        String hbasemaster = cdhManager;
        String hbasenodes = cdhAgents+cdhManager;
        String hiveUrl = "jdbc:hive2://"+cdhManager+":10000/default";
        metaDataService.init(hbasemaster,hbasenodes,hiveUrl);
        Map<String, String> data = new HashMap<>();
        data.put("hbasemaster", hbasemaster);
        data.put("hbasenodes", hbasenodes);
        data.put("hiveUrl", hiveUrl);
        return new ResponseResult(true,"success","集群Hbase地址配置成功",data);
    }
    @ApiOperation("hive SQL查询")
    @PostMapping("/hiveQuery")
    public ResponseResult hiveQuery(@RequestParam String sql){
        String queryResult = metaDataService.hiveQuery(sql);
        Map<String, String> data = new HashMap<>();
        data.put("queryResult",queryResult);
        return new ResponseResult(true,"success","hive查询成功",data);
    }

    /**
     * 获取表列表
     * @return
     */
    @ApiOperation("获取表列表")
    @GetMapping("/gettables")
    public ResponseResult getTables(){
        ResponseResult result = new ResponseResult();
        List<HTableStructure> tables = metaDataService.getTables();
        result.setData(tables);
        result.setMetaData(true,"001","查询表列表成功");
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
        return result;
    }
    /**
     * 获取列名
     * @return
     */
    @ApiOperation("获取列名")
    @GetMapping("/getcolumns")
    public ResponseResult getColumns(@ApiParam("表名") @RequestParam(value = "tableName",required = true) String tableName,
                                     @ApiParam("列族名") @RequestParam String columnFamily){
        ResponseResult result = new ResponseResult();
        List<String> columns = metaDataService.getColumns(tableName,columnFamily);
        result.setData(columns);
        result.setMetaData(true,"001","查询列名成功");

        return result;
    }

    /**
     * 获取表中所有数据
     * @param tableName
     * @return
     */
    @ApiOperation("获取表的所有数据")
    @GetMapping("/tableData")
    public ResponseResult getTableData(@ApiParam("表名") @RequestParam(value = "tableName",required = true) String tableName){
        ResponseResult result = new ResponseResult();
        List<HRowData> tableData = metaDataService.getTableData(tableName);
        result.setData(tableData);
        result.setMetaData(true,"001","查询表数据成功");
        return result;
    }

    /**
     * 获取行数据
     * @param tableName
     * @param rowkey
     * @return
     */
    @ApiOperation("获取行数据")
    @GetMapping("/rowData")
    public ResponseResult getRowData(@ApiParam("表名") @RequestParam String tableName,@ApiParam("行键") @RequestParam String rowkey){
        ResponseResult result = new ResponseResult();
        HRowData tableData = metaDataService.getRowData(tableName,rowkey);
        result.setData(tableData);
        result.setMetaData(true,"001","查询表中指定行数据成功");
        return result;
    }

    /**
     * 获取指定行范围数据
     * @param tableName
     * @param
     * @return
     */
    @ApiOperation("获取指定行范围内的数据")
    @GetMapping("/rowRangeData")
    public ResponseResult getRowRangeData(@ApiParam("表名") @RequestParam String tableName,
                                          @ApiParam("起始行键") @RequestParam String startRow,
                                          @ApiParam("行数") @RequestParam int rowNum){
        ResponseResult result = new ResponseResult();
        List<HRowData> tableData = metaDataService.getRowRangeData(tableName,startRow,rowNum);
        result.setData(tableData);
        result.setMetaData(true,"001","查询表中指定行范围数据成功");
        return result;
    }

    /**
     * 获取单元格数据
     * @param tableName
     * @param rowkey
     * @return
     */
    @ApiOperation("获取单元格数据")
    @GetMapping("/cellData")
    public ResponseResult getCellData(@ApiParam("表名") @RequestParam String tableName,@ApiParam("行键") @RequestParam String rowkey,
                                      @ApiParam("列族名") @RequestParam String columnFamily,@ApiParam("列名") @RequestParam String column){
        ResponseResult result = new ResponseResult();
        HCell cellData = metaDataService.getCellData(tableName,rowkey,columnFamily,column);
        result.setData(cellData);
        result.setMetaData(true,"001","查询表中指定单元格数据成功");
        return result;
    }
}
