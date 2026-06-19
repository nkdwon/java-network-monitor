package server;

import common.utils.Constants;
import server.network.TcpServer;
import server.network.UdpServer;
import server.service.MonitoringService;

public class ServerMain {
    public static void main(String[] args) {
        MonitoringService monitoringService = new MonitoringService();

        TcpServer tcpServer = new TcpServer(Constants.TCP_PORT, monitoringService);
        UdpServer udpServer = new UdpServer(Constants.UDP_PORT, monitoringService);

        tcpServer.start();
        udpServer.start();

        System.out.println("[SERVER] Java Network Monitor iniciado.");
    }
}