package com.sky.skydemo.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_info")
@Data
public class VersionEntity {

    private String version;
}
