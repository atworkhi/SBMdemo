package com.hanxx.area.entity;

import java.util.Date;

//实体类
public class Area {

    private Integer areaId;     //主键ID
    private String areaName;    //名称
    private Integer priority;   //权重 用来排名
    private Date createTime;    //创建时间
    private Date lastEditTime;  //更新时间

    /**
     * get and set
     */
    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
