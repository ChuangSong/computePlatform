package com.bdilab.model.MySqlModel;

/**
 * @ClassName Field
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/21 17:41
 * @Version 1.0
 */
public class Field {
    private String name;
    private String type;
    private int length;
    private boolean nullable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
