package com.bdilab.model.ActionModel;

/**
 * @ClassName WorkflowAction
 * @Description 工作流中的动作节点（前端配置）
 * @Author ChuangSong_Zheng
 * @Date 2019/11/1 16:00
 * @Version 1.0
 */
public class WorkflowAction {
    //action编号
    private int actionId;
    //动作节点名称
    private String actionName;
    //action类型
    private String actionType;

    //执行成功后的下一个节点
    private String ok;
    //执行失败后的下一个节点
    private String error;



    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
