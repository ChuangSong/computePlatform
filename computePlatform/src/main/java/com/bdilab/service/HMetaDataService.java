package com.bdilab.service;

import com.bdilab.model.HbaseModel.HCell;
import com.bdilab.model.HbaseModel.HRowData;
import com.bdilab.model.HbaseModel.HTableStructure;

import java.util.List;

public interface HMetaDataService {
    List<HTableStructure> getTables();
    List<String> getColumns(String tableName,String cfName);
    HRowData getRowData(String tableName,String rowkey);
    List<HRowData> getRowRangeData(String tableName,String startRow,int rowNum);
    HCell getCellData(String tableName,String rowkey,String columnFamily,String column);
    List<HRowData> getTableData(String tableName);
    String hiveQuery(String sql);
    void init(String hbasemaster,String hbasenodes,String hiveUrl);
}
