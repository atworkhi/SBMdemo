package com.hanxx.area.service;

import com.hanxx.area.entity.Area;

import java.util.List;

//接口方法
public interface AreaService {
    /**
     * 获取全部列表
     * @return
     */
    List<Area> getAreaList();

    /**
     * 通过ID获取
     * @param areaId
     * @return
     */
    Area getAreaById(int areaId);

    /**
     * 增加
     * @param area
     * @return
     */
    boolean addArea(Area area);

    /**
     * 修改
     * @param area
     * @return
     */
    boolean modifyArea(Area area);

    /**
     * 删除
     * @param areaId
     * @return
     */
    boolean deleteArea(int areaId);
}
