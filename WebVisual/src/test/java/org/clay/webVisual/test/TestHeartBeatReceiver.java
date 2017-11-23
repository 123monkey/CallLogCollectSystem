package org.clay.webVisual.test;

import org.clay.webVisual.Monitor.MonitorService;
import org.junit.Test;

/**
 *
 */
public class TestHeartBeatReceiver {

    @Test
    public void test1() throws InterruptedException {
        new MonitorService();
        while(true){
            Thread.sleep(5000);
        }
    }
}
