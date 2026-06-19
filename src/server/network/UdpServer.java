package server.network;

import server.service.MonitoringService;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer extends Thread {
    private final int port;
    private final MonitoringService monitoringService;

    public UdpServer(int port, MonitoringService monitoringService) {
        this.port = port;
        this.monitoringService = monitoringService;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("[UDP] Servidor UDP iniciado na porta " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("[UDP] Mensagem recebida: " + message);

                if (message.startsWith("HEARTBEAT:")) {
                    String machineName = message.substring("HEARTBEAT:".length());
                    monitoringService.updateHeartbeat(machineName);
                }
            }

        } catch (Exception e) {
            System.out.println("[UDP] Erro no servidor UDP: " + e.getMessage());
        }
    }
}