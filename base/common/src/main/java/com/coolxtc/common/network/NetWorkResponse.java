package com.coolxtc.common.network;


import com.coolxtc.common.NoProguard;

/**
 * Desc:
 * 接口返回底层统一解析基类
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
public class NetWorkResponse<T> implements NoProguard {

    public int errcode = NetWorkCode.SUCCESS;
    public long serverTime;
    public T data;
    public String errmsg = "";
    public Boolean success = true;
    public Boolean isCache = false;

    @Override
    public String toString() {
        return "NetWorkResponse{" +
                "errcode=" + errcode +
                ", serverTime=" + serverTime +
                ", data=" + data +
                ", errmsg='" + errmsg + '\'' +
                ", success=" + success +
                ", isCache=" + isCache +
                '}';
    }
}
