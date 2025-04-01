package com.sky.skydemo.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.skydemo.datasource.annotation.SwitchDataSource;
import com.sky.skydemo.utils.R;
import com.sky.skydemo.web.entity.VersionEntity;
import com.sky.skydemo.web.service.VersionService;
import com.sky.skydemo.web.entity.Version;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @GetMapping("/getVersion")
//    @SwitchDataSource
//    public R getVersion() {
//        VersionEntity versionEntity = versionService.getVersion();
//        return R.ok().put("versionEntity", versionEntity);
//    }

    @GetMapping(value = "/newVersion")
    public R getNewVersion() {
        Version version = versionService.getOne(new LambdaQueryWrapper<Version>()
                .eq(Version::getProduct, "优礼多多")
                .eq(Version::getVersionStatus, "最新版本"));

        return R.ok().put("Version",version);
    }
}
