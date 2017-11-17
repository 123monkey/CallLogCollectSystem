package org.clay.webVisual.web.controller;

import org.clay.webVisual.domain.CallLog;
import org.clay.webVisual.domain.CallLogRange;
import org.clay.webVisual.service.CallLogService;
import org.clay.webVisual.util.CallLogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
@Controller
public class CallLogController {

    @Resource(name="callLogService")
    private CallLogService cs ;

    @RequestMapping("/callLog/findAll")
    public String findAll(Model m){
        List<CallLog> list = cs.findAll();
        m.addAttribute("callLogs",list);
        return "callLog/callLogList" ;
    }

    /**
     * 进入查询通话记录的页面,form
     */
    @RequestMapping("/callLog/toFindCallLogPage")
    public String toFindCallLogPage(){
        return "callLog/findCallLog" ;
    }

    @RequestMapping(value = "/callLog/findCallLog",method = RequestMethod.POST)
    public String findCallLog(Model m , @RequestParam("caller") String caller, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        List<CallLogRange> list = CallLogUtil.getCallLogRanges(startTime, endTime);
        List<CallLog> logs = cs.findCallogs(caller,list);
        m.addAttribute("callLogs", logs);
        return "callLog/callLogList" ;
    }
}
