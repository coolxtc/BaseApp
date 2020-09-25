package com.coolxtc.common.bean.ret;

/**
 * Desc:
 * 启动接口返回Json的解析对象
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
public class StartRet {

    /**
     * StopServer : {"Code":0,"Msg":""}
     * Update : {"LowVerCode":"v1.0.0","LowMsg":"","VerCode":"v1.0.0","VerMsg":"","DownUrl":""}
     * AppScreen : {"ImgUrl":"","ClickUrl":"","StartTime":"","EndTime":""}
     * WeiXin : {"Title":null,"Content":"test","ImgUrl":"http://meitetest.oss-cn-hangzhou.aliyuncs.com/app/201910/145da434162e7ab.jpg","LinkUrl":"0"}
     * DeviceId : 20dcde1b5da434ba1a8d8218825719
     */

    private StopServerBean stopServer;
    private UpdateBean update;
    private String deviceId;

    public StopServerBean getStopServer() {
        return stopServer;
    }

    public void setStopServer(StopServerBean stopServer) {
        this.stopServer = stopServer;
    }

    public UpdateBean getUpdate() {
        return update;
    }

    public void setUpdate(UpdateBean update) {
        this.update = update;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public static class StopServerBean {
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    public static class UpdateBean {
        /**
         * LowVerCode : v1.0.0
         * LowMsg :
         * VerCode : v1.0.0
         * VerMsg :
         * DownUrl :
         */

        private String lowVerCode;
        private String lowMsg;
        private String verCode;
        private String verMsg;
        private String downUrl;

        public String getLowVerCode() {
            return lowVerCode;
        }

        public void setLowVerCode(String lowVerCode) {
            this.lowVerCode = lowVerCode;
        }

        public String getLowMsg() {
            return lowMsg;
        }

        public void setLowMsg(String lowMsg) {
            this.lowMsg = lowMsg;
        }

        public String getVerCode() {
            return verCode;
        }

        public void setVerCode(String verCode) {
            this.verCode = verCode;
        }

        public String getVerMsg() {
            return verMsg;
        }

        public void setVerMsg(String verMsg) {
            this.verMsg = verMsg;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }
    }
}
