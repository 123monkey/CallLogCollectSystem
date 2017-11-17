package org.clay.webVisual.domain;


import org.clay.webVisual.util.CallLogUtil;

import java.text.SimpleDateFormat;

/**
 * CallLog
 */
public class CallLog {
    private String caller ;
    //主叫名字
    private String callerName ;
    private String callee ;
    //被叫名称
    private String calleeName ;

    private String callTime ;
    private String callDuration ;

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCalleeName() {
        return calleeName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    //是否是主叫
    private boolean flag ;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallee() {
        return callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public String getCallTime() {
        if(callTime != null){
            return CallLogUtil.formatDate(callTime);
        }
        return null ;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }
}
