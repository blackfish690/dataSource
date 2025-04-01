package com.sky.skydemo.web.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 版本描述
 *
 * @author zzt
 * @since 2024-09-11 15:14:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_version")
public class Version extends BaseEntity {

    private static final long serialVersionUID = 5683495251252601L;

    private String createName;

    private String product;

    private String versionNumber;

    private String platform;

    private String versionStatus;

    private String versionHint;

    private String updateDescription;

    private String hintFrequency;
}

