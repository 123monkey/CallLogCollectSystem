package org.clay.webVisual.Monitor;

import org.clay.webVisual.domain.HeartBeat;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service("monitorService")
public class MonitorService {

    private DatagramSocket sock;
    private ReceiveThread t;

    public MonitorService() {
        t = new ReceiveThread();
        t.start();
    }

    public List<HeartBeat> getHeartbeats() {
        return new ArrayList<HeartBeat>(t.map.values());
    }

}
