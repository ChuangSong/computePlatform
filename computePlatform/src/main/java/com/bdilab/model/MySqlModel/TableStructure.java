package com.bdilab.model.MySqlModel;

import java.util.List;

/**
 * @ClassName TableStructure
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/21 17:39
 * @Version 1.0
 */
public class TableStructure {
    private String tableName;
    private String location;
    private List<Field> tableField;
    private List<String> tableData;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Field> getTableField() {
        return tableField;
    }

    public void setTableField(List<Field> tableField) {
        this.tableField = tableField;
    }

    public List<String> getTableData() {
        return tableData;
    }

    public void setTableData(List<String> tableData) {
        this.tableData = tableData;
    }
}
