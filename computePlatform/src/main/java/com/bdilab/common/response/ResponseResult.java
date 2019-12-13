package com.bdilab.common.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName ResponseResult
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/9/30 11:29
 * @Version 1.0
 */
@ApiModel(description = "返回给前端的数据格式")
public class ResponseResult {
    /**
     * 后端返回给前端的数据
     */
    @ApiModelProperty("返回的数据")
    private Object data;
    /**
     * 后端返回给前端的元数据，包括成功标识、返回码和返回消息
     */
    @ApiModelProperty("返回的元数据")
    private MetaData meta = new MetaData();
    public ResponseResult(){}

    public ResponseResult(boolean success,String code,String message){
        this.meta = new MetaData(success,code,message);
    }
    public ResponseResult(boolean success,String code,String message,Object data){
        this(success,code,message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public MetaData getMetaData() {
        return meta;
    }

    public void setMetaData(MetaData meta) {
        this.meta = meta;
    }

    public void setMetaData(boolean success,String code,String message){
            this.meta.setMetaData(success,code,message);
    }
}
