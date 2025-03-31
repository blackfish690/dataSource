package com.sky.skydemo.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.skydemo.datasource.annotation.SwitchDataSource;
import com.sky.skydemo.web.dao.VersionDao;
import com.sky.skydemo.web.entity.VersionEntity;
import com.sky.skydemo.web.service.VersionService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VersionServiceImpl extends ServiceImpl<VersionDao,VersionEntity> implements VersionService {


    @Override

    public VersionEntity getVersion() {
        List<VersionEntity> versionEntities = this.baseMapper.selectList(null);
        return versionEntities.get(0);
    }
}
