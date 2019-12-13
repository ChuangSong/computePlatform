package com.bdilab.model.HbaseModel;

import java.util.List;

/**
 * @ClassName HRowData
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/23 10:07
 * @Version 1.0
 */
public class HRowData {
    private String rowkey;
    private List<HCell> cells;

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public List<HCell> getCells() {
        return cells;
    }

    public void setCells(List<HCell> cells) {
        this.cells = cells;
    }
}
