package org.clay.webVisual.web.controller;

import com.alibaba.fastjson.JSON;
import org.clay.webVisual.Monitor.MonitorService;
import org.clay.webVisual.domain.HeartBeat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class MonitorController {

    @Resource(name="monitorService")
    private MonitorService ms ;

    @RequestMapping("/monitor/monitorPage")
    public String toMonotorPage(){
        return "monitor/monitor" ;
    }

    @RequestMapping("/json/monitor/getMonotorInfo")
    public String getMonotorInfo() {
        List<HeartBeat> list = ms.getHeartbeats();
        return JSON.toJSONString(list);
    }
}
