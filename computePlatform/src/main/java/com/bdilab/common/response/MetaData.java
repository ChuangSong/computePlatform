package com.bdilab.common.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName MetaData
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/9/30 11:29
 * @Version 1.0
 */
@ApiModel(description = "元数据")
public class MetaData {
    /**
     * 是否成功的标志
     */
    @ApiModelProperty("是否成功的标志")
    private boolean success;
    /**
     * 返回码
     */
    @ApiModelProperty("返回码")
    private String code;
    /**
     * 返回消息
     */
    @ApiModelProperty("返回信息")
    private String message;

    public MetaData(){}
    public MetaData(boolean success, String code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setMetaData(boolean success, String code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
