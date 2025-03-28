package com.sky.skydemo.web.controller;

import com.sky.skydemo.utils.R;
import com.sky.skydemo.web.entity.VersionEntity;
import com.sky.skydemo.web.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysController {

    @Autowired
    private VersionService versionService;

    @GetMapping("/info")
    public R info() {
        return R.ok("成功");
    }

    @GetMapping("/getVersion")
    public R getVersion() {
        VersionEntity versionEntity =versionService.getVersion();
        return R.ok().put("versionEntity",versionEntity);
    }
}
