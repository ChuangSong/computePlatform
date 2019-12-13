package com.bdilab.model.HbaseModel;

import java.util.List;

/**
 * @ClassName HColumnFamily
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/22 16:47
 * @Version 1.0
 */
public class HColumnFamily {
    //列族名
    private String ColumnFamily;
    //包含的所有列
    private List<String> columns;

    public String getColumnFamilyName() {
        return ColumnFamily;
    }

    public void setColumnFamilyName(String columnFamilyName) {
        ColumnFamily = columnFamilyName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
