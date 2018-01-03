package com.dituhui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.service.HangingTableService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Xugn on 2017/12/19.
 */
@RestController
@RequestMapping("/")
public class HangingTableController {
    @Reference(version = "1.0.0")
    private HangingTableService hangingTableService;

    @RequestMapping(value = "/searchHangingTable", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String searchHangingTable(String ak,String layercode,String custom_id) {
        return null;
    }
}