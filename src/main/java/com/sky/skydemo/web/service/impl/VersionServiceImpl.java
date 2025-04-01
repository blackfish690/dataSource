package com.sky.skydemo.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.skydemo.web.dao.VersionDao;
import com.sky.skydemo.web.entity.Version;
import com.sky.skydemo.web.service.VersionService;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl extends ServiceImpl<VersionDao, Version> implements VersionService {


//    @Override
//    public VersionEntity getVersion() {
//        List<VersionEntity> versionEntities = this.baseMapper.selectList(null);
//        return versionEntities.get(0);
//    }
}
