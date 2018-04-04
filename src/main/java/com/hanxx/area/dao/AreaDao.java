package com.hanxx.area.dao;

import com.hanxx.area.entity.Area;

import java.util.List;

public interface AreaDao {
    /**
     * 查询全部
     * @return
     */
    List<Area> queryArea();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Area queryAreaById(int id);

    /**
     * 新增
     * @param area
     * @return
     */
    int insertArea(Area area);

    /**
     * 修改
     * @param area
     * @return
     */
    int updateArea(Area area);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteAreaById(int id);
}
