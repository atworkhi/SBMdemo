package com.hanxx.area.controller;

import com.hanxx.area.entity.Area;
import com.hanxx.area.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")   //根路由
public class AreaController {
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    private Map<String,Object> listArea(){
        Map<String,Object> modeMap =new HashMap<String,Object>();
        List<Area> list = areaService.getAreaList();
        modeMap.put("areaList",list);
        return modeMap;
    }

    @RequestMapping(value = "/getareaid",method = RequestMethod.GET)
    public Map<String,Object> getAreaById(Integer areaId){
        //测试自定义异常
        //int a=1/0;
        Map<String,Object> modeMap = new HashMap<String,Object>();
        Area area = areaService.getAreaById(areaId);
        modeMap.put("area",area);
        return modeMap;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    //@RequestBody可以传入xml json等
    public Map<String,Object> addArea(@RequestBody Area area){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        //添加
        modeMap.put("success",areaService.addArea(area));
        return modeMap;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    //@RequestBody可以传入xml json等
    public Map<String,Object> editArea(@RequestBody Area area){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        //修改
        modeMap.put("success",areaService.modifyArea(area));
        return modeMap;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Map<String,Object> deleteArea(Integer areaId){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        //删除
        modeMap.put("success",areaService.deleteArea(areaId));
        return modeMap;
    }
}
