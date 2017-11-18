package org.clay.webVisual.web.controller;

import com.alibaba.fastjson.JSON;
import org.clay.webVisual.domain.CallLog;
import org.clay.webVisual.domain.CallLogRange;
import org.clay.webVisual.domain.CallLogStat;
import org.clay.webVisual.hive.HiveCallLogService;
import org.clay.webVisual.service.CallLogService;
import org.clay.webVisual.util.CallLogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */
@Controller
public class CallLogController {

    //注入hiveservice
    @Resource(name="hiveCallLogService")
    private HiveCallLogService hcs ;

    @Resource(name="callLogService")
    private CallLogService cs ;

    @RequestMapping("/callLog/findAll")
    public String findAll(Model m){
        List<CallLog> list = cs.findAll();

        m.addAttribute("callLogs",list);
        return "callLog/callLogList" ;
    }

    @RequestMapping("/callLog/json/findAll")
    public String findAllJson(HttpServletResponse response) {
        List<CallLog> list = cs.findAll();
        String json = JSON.toJSONString(list);
        // 通知浏览器接受到数据类型字符集
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            OutputStream out = response.getOutputStream();
            //  json 文件采用的编码
            out.write(json.getBytes("utf-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
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

    /**
     * 查询最近通话记录
     */
    @RequestMapping(value = "/callLog/findLatestCallLog",method = RequestMethod.POST)
    public String findLatestCallLog(Model m , @RequestParam("caller") String caller){
        CallLog log = hcs.findLatestCallLog(caller);
        if(log != null){
            m.addAttribute("log", log);
        }
        return "callLog/latestCallLog" ;
    }

    /**
     *  查询最近通话记录  的页面
     */
    @RequestMapping(value = "/callLog/toFindLatestCallLog")
    public String toFindLatestCallLog(){
        return "callLog/findLatestCallLog" ;
    }

    /**
     * 进入 统计指定年份中各月份通话次数统计 的页面
     */
    @RequestMapping("/callLog/toStatCallLog")
    public String toStatCallLog(){
        return "callLog/statCallLog" ;
    }

    /**
     * 统计指定人员，指定年份中各月份通话次数统计
     */
    @RequestMapping("/callLog/statCallLog")
    public String statCallLog(Model m ,@RequestParam("caller") String caller ,@RequestParam("year") String year){
        List<CallLogStat> list = hcs.statCallLogsCount(caller, year);

        if(list != null && !list.isEmpty()){
            List<String> months = new ArrayList<String>();      //月份
            List<Integer> counts = new ArrayList<Integer>();    //次数
            for(CallLogStat cls : list){
                months.add(cls.getYearMonth());
                counts.add(cls.getCount());
            }

            m.addAttribute("title", caller + "在" + year + "年各月份通话次数统计");
            m.addAttribute("list", list);
        }

        return "callLog/statCallLog" ;
    }
}
