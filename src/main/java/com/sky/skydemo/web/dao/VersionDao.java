package com.sky.skydemo.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.skydemo.web.entity.Version;
import com.sky.skydemo.web.entity.VersionEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VersionDao extends BaseMapper<Version> {
}
