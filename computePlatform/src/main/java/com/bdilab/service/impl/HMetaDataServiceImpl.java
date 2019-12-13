package com.bdilab.service.impl;

import com.bdilab.model.HbaseModel.HCell;
import com.bdilab.model.HbaseModel.HRowData;
import com.bdilab.model.HbaseModel.HTableStructure;
import com.bdilab.service.HMetaDataService;
import com.bdilab.util.HbaseUtil;
import com.bdilab.util.HiveUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName HMetaDataServiceImpl
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/22 17:21
 * @Version 1.0
 */
@Service
public class HMetaDataServiceImpl implements HMetaDataService {
    @Autowired
    HbaseUtil hbaseUtil;
    @Autowired
    HiveUtil hiveUtil;
    private static org.apache.hadoop.hbase.client.Connection hbaseConnection;
    private static Connection hiveConnection;

    /**
     * 初始化配置
     * @param hbasemaster
     * @param hbasenodes
     * @param hiveUrl
     */
    public void init(String hbasemaster,String hbasenodes,String hiveUrl){
        //关闭连接
        hbaseUtil.releaseConnection();
        hiveUtil.releaseConnection();
        //初始化参数
        hbaseUtil.init(hbasemaster,hbasenodes);
        hiveUtil.init(hiveUrl);
        //重新获取连接
        hbaseConnection = hbaseUtil.getConnection();
        hiveConnection = hiveUtil.getConnection();
    }
    /**
     * hive sql查询
     * @param sql
     * @return
     */
    public String hiveQuery(String sql){
        String queryResult = "";
        String[] sqls = sql.trim().split(";");
        if (sqls.length>0){
            String lastSQL = sqls[sqls.length-1];
            try{
                PreparedStatement preparedStatement = hiveConnection.prepareStatement(lastSQL);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int i=0;i<columnCount;i++){
                        queryResult += resultSet.getString(i+1)+"\t";
                    }
                    queryResult += "\n";
                }
                resultSet.close();
                preparedStatement.close();
            }catch (SQLException e){
                e.printStackTrace();
                queryResult = e.getMessage();
            }
        }
        return queryResult;

    }

    /**
     * 获取所有表
     * @return
     */
    @Override
    public List<HTableStructure> getTables() {
        List<HTableStructure> tables = new ArrayList<>();
        try{
            Admin admin = hbaseConnection.getAdmin();
            List<TableDescriptor> tableDescriptors = admin.listTableDescriptors();
            for (TableDescriptor tableDescriptor:tableDescriptors){
                List<String> cfnames = new ArrayList<>();
                String tableName = tableDescriptor.getTableName().toString();
                int ColumnFamilyCount = tableDescriptor.getColumnFamilyCount();
                tableDescriptor.getColumnFamilyNames().forEach(item->cfnames.add(Bytes.toString(item)));
                HTableStructure tableStructure = new HTableStructure();
                tableStructure.setTableName(tableName);
                tableStructure.setColumnFamilyCount(ColumnFamilyCount);
                tableStructure.setColumnFamily(cfnames);
                tables.add(tableStructure);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * 获取列名
     * @param tableName
     * @param cfName
     * @return
     */
    @Override
    public List<String> getColumns(String tableName,String cfName){
        List<String> columns = new ArrayList<>();
        try{
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.addFamily(cfName.getBytes());
            scan.setOneRowLimit();
            ResultScanner scanner = table.getScanner(scan);
            for (Result result:scanner){
                result.getFamilyMap(cfName.getBytes()).keySet().forEach(item->columns.add(Bytes.toString(item)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return columns;
    }

    /**
     * 获取指定行范围内的数据
     * @param tableName
     * @param startRow
     * @param rowNum
     * @return
     */
    public List<HRowData> getRowRangeData(String tableName, String startRow, int rowNum){
        List<HRowData> rows = new ArrayList<>();
        try{
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.withStartRow(startRow.getBytes());
            scan.setLimit(rowNum);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result:scanner) {
                HRowData rowData = new HRowData();
                List<HCell> cells = new ArrayList<>();
                String rowkey = Bytes.toString(result.getRow());
                for (Cell cell : result.rawCells()) {
                    String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                    String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    long timestamp = cell.getTimestamp();
                    HCell hCell = new HCell(row, family, qualifier, value, timestamp);
                    cells.add(hCell);
                }
                rowData.setRowkey(rowkey);
                rowData.setCells(cells);
                rows.add(rowData);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 获取某一行数据
     * @param tableName
     * @param rowkey
     * @return
     */
    @Override
    public HRowData getRowData(String tableName,String rowkey) {
        HRowData rowData = new HRowData();
        try{
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowkey.getBytes());
            Result result = table.get(get);
            List<HCell> cells = new ArrayList<>();
            for (Cell cell:result.rawCells()) {
                String row = Bytes.toString(cell.getRowArray(),cell.getRowOffset(),cell.getRowLength());
                String family = Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                String qualifier = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                String value = Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength());
                long timestamp = cell.getTimestamp();
                HCell hCell = new HCell(row,family,qualifier,value,timestamp);
                cells.add(hCell);
            }
            rowData.setRowkey(rowkey);
            rowData.setCells(cells);
        }catch (IOException e){
            e.printStackTrace();
        }
        return rowData;
    }

    /**
     * 获取单元格数据
     * @param tableName
     * @param rowkey
     * @param columnFamily
     * @param column
     * @return
     */
    @Override
    public HCell getCellData(String tableName, String rowkey, String columnFamily, String column){
        HCell hCell = new HCell();
        try{
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowkey.getBytes());
            get.addColumn(columnFamily.getBytes(),column.getBytes());
            Result result = table.get(get);
            Cell cell = result.getColumnLatestCell(columnFamily.getBytes(),column.getBytes());
            String row = Bytes.toString(cell.getRowArray(),cell.getRowOffset(),cell.getRowLength());
            String family = Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
            String qualifier = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
            String value = Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength());
            long timestamp = cell.getTimestamp();
            hCell.setRowkey(row);
            hCell.setCfName(family);
            hCell.setQualifier(qualifier);
            hCell.setValue(value);
            hCell.setTimestamp(timestamp);
        }catch (IOException e){
            e.printStackTrace();
        }
        return hCell;
    }

    /**
     * 根据表名获取表数据
     * @param tableName
     * @return
     */
    @Override
    public List<HRowData> getTableData(String tableName) {
        List<HRowData> resultData = new ArrayList<>();
        try{
            if (!hbaseConnection.getAdmin().tableExists(TableName.valueOf(tableName))){
                System.out.println("表"+tableName+"不存在");
                return resultData;
            }
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            ResultScanner scanner = table.getScanner(new Scan());
            for (Result result:scanner){
                HRowData rowData = new HRowData();
                List<HCell> cells = new ArrayList<>();
                String rowkey = Bytes.toString(result.getRow());
                for (Cell cell:result.rawCells()){
                    String row = Bytes.toString(cell.getRowArray(),cell.getRowOffset(),cell.getRowLength());
                    String family = Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                    String qualifier = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength());
                    long timestamp = cell.getTimestamp();
                    HCell hCell = new HCell(row,family,qualifier,value,timestamp);
                    cells.add(hCell);
                }
                rowData.setRowkey(rowkey);
                rowData.setCells(cells);
                resultData.add(rowData);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return resultData;
    }
}
