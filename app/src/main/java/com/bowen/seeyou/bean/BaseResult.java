package com.bowen.seeyou.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29.
 */

public class BaseResult implements Serializable {

    /**
     * returnCode : 505
     * returnInfo : 用户名不可为空
     */

    private String returnCode;
    private String returnInfo;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }
}
