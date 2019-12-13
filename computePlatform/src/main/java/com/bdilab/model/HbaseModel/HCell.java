package com.bdilab.model.HbaseModel;

/**
 * @ClassName HCell
 * @Description 单元数据
 * @Author ChuangSong_Zheng
 * @Date 2019/10/22 20:42
 * @Version 1.0
 */

public class HCell {
    //行键
    private String rowkey;
    //列族
    private String cfName;
    //列名
    private String qualifier;
    //单元值
    private String value;
    //时间戳
    private long timestamp;
    public HCell(){}
    public HCell(String rowkey,String cfName,String qualifier,String value,long timestamp){
        this.rowkey = rowkey;
        this.cfName = cfName;
        this.qualifier = qualifier;
        this.value = value;
        this.timestamp = timestamp;
    }
    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getCfName() {
        return cfName;
    }

    public void setCfName(String cfName) {
        this.cfName = cfName;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
