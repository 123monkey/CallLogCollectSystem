package org.clay.getdata.test;


import org.clay.callloggen.udp.HeartBeatThread;
import org.junit.Test;

public class TestHeartBeatSender {
    @Test
    public void test1() throws InterruptedException {
        new HeartBeatThread().start();
        while(true){
            Thread.sleep(1000);
        }
    }
}
