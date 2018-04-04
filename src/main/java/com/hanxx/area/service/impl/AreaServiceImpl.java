package com.hanxx.area.service.impl;

import com.hanxx.area.dao.AreaDao;
import com.hanxx.area.entity.Area;
import com.hanxx.area.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

//实现类
@Service
public class AreaServiceImpl implements AreaService{
    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }

    @Override
    public Area getAreaById(int areaId) {
        return areaDao.queryAreaById(areaId);
    }

    @Transactional  //事务控制
    @Override
    public boolean addArea(Area area) {
        if(area.getAreaName() !=null && !"".equals(area.getAreaName())){
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int num =areaDao.insertArea(area);
                if(num>0){
                    return true;
                }else {
                    throw  new RuntimeException("添加地区信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("插入区域信息失败:"+e.getMessage());
            }
        }else {
            throw new RuntimeException("区域信息不能为空！！！");
        }
    }

    @Override
    public boolean modifyArea(Area area) {
        if(area.getAreaId() !=null && area.getAreaId()>0){
            area.setLastEditTime(new Date());
            try {
                int num =areaDao.updateArea(area);
                if(num>0){
                    return true;
                }else {
                    throw new RuntimeException("修改地区信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("修改地区信息失败："+e.getMessage());
            }
        }else {
            throw new RuntimeException("区域信息不能为空！！！");
        }
    }


    @Override
    public boolean deleteArea(int areaId) {
        if(areaId >0){
            try {
                //删除区域信息
                int num =areaDao.deleteAreaById(areaId);
                if(num>0){
                    return true;
                }else {
                    throw new RuntimeException("删除城市信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("删除城市信息失败："+e.getMessage());
            }
        }else {
            throw new RuntimeException("要删除的区域ID不能为空！");
        }
    }
}
