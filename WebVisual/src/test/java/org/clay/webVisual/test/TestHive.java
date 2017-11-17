package org.clay.webVisual.test;

import org.clay.webVisual.domain.CallLog;
import org.clay.webVisual.hive.HiveCallLogService;
import org.junit.Test;

/**
 *
 */
public class TestHive {

    @Test
    public void test1(){
        HiveCallLogService s = new HiveCallLogService();
        CallLog log = s.findLatestCallLog("18258885660");
        System.out.println(log.getCallee());
    }

}
