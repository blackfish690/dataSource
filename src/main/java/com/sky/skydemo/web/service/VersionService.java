package com.sky.skydemo.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.skydemo.web.entity.VersionEntity;

public interface VersionService extends IService<VersionEntity>{


    VersionEntity getVersion();

}
