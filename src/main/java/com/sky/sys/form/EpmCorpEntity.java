package com.sky.sys.form;


import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业信息
 *
 * @author el
 * @email el@elawoffice.net
 * @date 2021-01-04 10:11:16
 */
@TableName("EPM_Corp")
public class EpmCorpEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String corpid;
    /**
     * 公司名称
     */
    private String corpname;
    /**
     * 公司地址
     */
    private String corpaddress;
    /**
     * 联系人
     */
    private String corplinkpsn;
    /**
     * 联系电话
     */
    private String corplinktel;
    /**
     * 数据库连接字符串
     */
    private String dbconnstring;
    /**
     * 后端服务地址
     */
    private String serviceurl;
    /**
     * 开始服务日期
     */
    private Date servicebegin;
    /**
     * 结束服务日期
     */
    private Date serviceend;
    /**
     * 服务器到期日期
     */
    private Date serverexpiredate;
    /**
     * 人员数量
     */
    private Integer personcount;
    /**
     * 0：试用  1：正式  2：停用
     */
    private Integer servicestate;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String linkmobile;
    /**
     * 部署   0：通用版 1：独立域名 2：自有服务器
     */
    private Integer deployflag;
    /**
     * $column.comments
     */
    private Integer appflag;
    /**
     * 分所开关：默认为0，1为开启
     */
    private Boolean isopenbranch;
    /**
     * $column.comments
     */
    private Boolean isonlineedit;
    /**
     * $column.comments
     */
    private Boolean isdms;
    /**
     * $column.comments
     */
    private Boolean isoss;
    /**
     * 创建日期
     */
    private Date adddate;
    /**
     * $column.comments
     */
    private String remark;
    /**
     * 访问地址，多个地址用逗号分隔
     */
    private String websites;
    /**
     * 系统类型：EPMS->智慧律所
     */
    private String systemtype;
    /**
     * 系统当前版本号
     */
    private String systemver;
    /**
     * 关联的总所ID
     */
    private String fatherid;

    /**
     * 服务号AppID
     */
    private String svrmpappid;
    /**
     * 服务号密钥
     */
    private String svrmpappsec;
    /**
     * 小程序AppID
     */
    private String miniproappid;
    /**
     * 小程序密钥
     */
    private String miniproappsec;
    /**
     * 开放平台appId
     */
    private String openappid;
    /**
     * 开放平台密钥
     */
    private String openappsec;
    /**
     * 微信公众号配置名称
     */
    private String wxsvrname;
    /**
     * 微信小程序配置名称
     */
    private String wxminiproname;
    /**
     * 微信开放平台名称
     */
    private String wxopenname;

    public String getSvrmpappid() {
        return svrmpappid;
    }

    public void setSvrmpappid(String svrmpappid) {
        this.svrmpappid = svrmpappid;
    }

    public String getSvrmpappsec() {
        return svrmpappsec;
    }

    public void setSvrmpappsec(String svrmpappsec) {
        this.svrmpappsec = svrmpappsec;
    }

    public String getMiniproappid() {
        return miniproappid;
    }

    public void setMiniproappid(String miniproappid) {
        this.miniproappid = miniproappid;
    }

    public String getMiniproappsec() {
        return miniproappsec;
    }

    public void setMiniproappsec(String miniproappsec) {
        this.miniproappsec = miniproappsec;
    }

    public String getOpenappid() {
        return openappid;
    }

    public void setOpenappid(String openappid) {
        this.openappid = openappid;
    }

    public String getOpenappsec() {
        return openappsec;
    }

    public void setOpenappsec(String openappsec) {
        this.openappsec = openappsec;
    }

    public String getWxsvrname() {
        return wxsvrname;
    }

    public void setWxsvrname(String wxsvrname) {
        this.wxsvrname = wxsvrname;
    }

    public String getWxminiproname() {
        return wxminiproname;
    }

    public void setWxminiproname(String wxminiproname) {
        this.wxminiproname = wxminiproname;
    }

    public String getWxopenname() {
        return wxopenname;
    }

    public void setWxopenname(String wxopenname) {
        this.wxopenname = wxopenname;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    public String getCorpaddress() {
        return corpaddress;
    }

    public void setCorpaddress(String corpaddress) {
        this.corpaddress = corpaddress;
    }

    public String getCorplinkpsn() {
        return corplinkpsn;
    }

    public void setCorplinkpsn(String corplinkpsn) {
        this.corplinkpsn = corplinkpsn;
    }

    public String getCorplinktel() {
        return corplinktel;
    }

    public void setCorplinktel(String corplinktel) {
        this.corplinktel = corplinktel;
    }

    public String getDbconnstring() {
        return dbconnstring;
    }

    public void setDbconnstring(String dbconnstring) {
        this.dbconnstring = dbconnstring;
    }

    public String getServiceurl() {
        return serviceurl;
    }

    public void setServiceurl(String serviceurl) {
        this.serviceurl = serviceurl;
    }

    public Date getServicebegin() {
        return servicebegin;
    }

    public void setServicebegin(Date servicebegin) {
        this.servicebegin = servicebegin;
    }

    public Date getServiceend() {
        return serviceend;
    }

    public void setServiceend(Date serviceend) {
        this.serviceend = serviceend;
    }

    public Date getServerexpiredate() {
        return serverexpiredate;
    }

    public void setServerexpiredate(Date serverexpiredate) {
        this.serverexpiredate = serverexpiredate;
    }

    public Integer getPersoncount() {
        return personcount;
    }

    public void setPersoncount(Integer personcount) {
        this.personcount = personcount;
    }

    public Integer getServicestate() {
        return servicestate;
    }

    public void setServicestate(Integer servicestate) {
        this.servicestate = servicestate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkmobile() {
        return linkmobile;
    }

    public void setLinkmobile(String linkmobile) {
        this.linkmobile = linkmobile;
    }

    public Integer getDeployflag() {
        return deployflag;
    }

    public void setDeployflag(Integer deployflag) {
        this.deployflag = deployflag;
    }

    public Integer getAppflag() {
        return appflag;
    }

    public void setAppflag(Integer appflag) {
        this.appflag = appflag;
    }

    public Boolean getIsopenbranch() {
        return isopenbranch;
    }

    public void setIsopenbranch(Boolean isopenbranch) {
        this.isopenbranch = isopenbranch;
    }

    public Boolean getIsonlineedit() {
        return isonlineedit;
    }

    public void setIsonlineedit(Boolean isonlineedit) {
        this.isonlineedit = isonlineedit;
    }

    public Boolean getIsdms() {
        return isdms;
    }

    public void setIsdms(Boolean isdms) {
        this.isdms = isdms;
    }

    public Boolean getIsoss() {
        return isoss;
    }

    public void setIsoss(Boolean isoss) {
        this.isoss = isoss;
    }

    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWebsites() {
        return websites;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

    public String getSystemtype() {
        return systemtype;
    }

    public void setSystemtype(String systemtype) {
        this.systemtype = systemtype;
    }

    public String getSystemver() {
        return systemver;
    }

    public void setSystemver(String systemver) {
        this.systemver = systemver;
    }

    public String getFatherid() {
        return fatherid;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }
}

