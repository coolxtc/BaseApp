package com.coolxtc.common;

import com.coolxtc.common.util.SPUtils;
import com.coolxtc.common.util.StrUtil;


/**
 * 描 述:
 * 登录用户信息模块，提供用户登录信息以及其他app本地信息的缓存读写接口
 * 1. 登录信息由登录api获取数据以后通过本类进行缓存更新
 * 2. 信息分开存储，暂不做统一保存
 *
 * @author: lihui
 * @date: 2016-03-05 14:16
 */
public class UserInfoModel {
    //设备号
    private final static String DEVICE_ID = "deviceId";
    private final static String NEW_VERSION = "newVersion";

    public static String getDeviceId() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(DEVICE_ID));
    }

    public static void setDeviceId(String deviceid) {
        SPUtils.getInstance().setString(DEVICE_ID, deviceid);
    }

    public static void setNewVersion(String version) {
        SPUtils.getInstance().setString(NEW_VERSION, version);
    }

    public static String getNewVersion() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(NEW_VERSION));
    }
}