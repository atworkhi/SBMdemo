package com.hanxx.area.dao;

import com.hanxx.area.entity.Area;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {

    @Autowired
    private  AreaDao areaDao;

    @Test
    @Ignore
    public void queryArea() {
        List<Area> areaList=areaDao.queryArea();
        //因为数据库只有两条信息，希望返回值是3
        assertEquals(3,areaList.size());
    }

    @Test
    @Ignore
    public void queryAreaById() {
        //查询为1的数据
        Area area =areaDao.queryAreaById(1);
        assertEquals("北京",area.getAreaName());

    }

    @Test
    @Ignore
    public void insertArea() {
        Area area = new Area();
        area.setAreaName("郑州");
        area.setPriority(5);
        int num=areaDao.insertArea(area);
        //影响行数
        assertEquals(1,num);
    }

    @Test
    @Ignore
    public void updateArea() {
        Area area=new Area();
        area.setAreaName("郑州");
        area.setAreaId(4);
        area.setLastEditTime(new Date());
        int num=areaDao.updateArea(area);
        assertEquals(1,num);
    }

    @Test
    public void deleteAreaById() {
       int num = areaDao.deleteAreaById(4);
       assertEquals(1,num);
    }
}