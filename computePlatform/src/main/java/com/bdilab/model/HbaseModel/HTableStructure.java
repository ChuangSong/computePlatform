package com.bdilab.model.HbaseModel;

import java.util.List;

/**
 * @ClassName HTableStructure
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/22 16:46
 * @Version 1.0
 */
public class HTableStructure {
    //表名
    private String tableName;
    //列族名
    private List<String> columnFamily;
    //列族个数
    private int ColumnFamilyCount;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(List<String> columnFamilies) {
        this.columnFamily = columnFamilies;
    }

    public int getColumnFamilyCount() {
        return ColumnFamilyCount;
    }

    public void setColumnFamilyCount(int columnFamilyCount) {
        ColumnFamilyCount = columnFamilyCount;
    }
}
