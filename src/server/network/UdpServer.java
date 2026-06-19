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

                processMessage(message);
            }

        } catch (Exception e) {
            System.out.println("[UDP] Erro no servidor UDP: " + e.getMessage());
        }
    }

    private void processMessage(String message) {
        String[] parts = message.split("\\|");

        if (parts.length == 0) {
            System.out.println("[UDP] Formato inválido.");
            return;
        }

        String command = parts[0];

        switch (command) {
            case "HEARTBEAT":
                if (parts.length < 2) {
                    System.out.println("[UDP] HEARTBEAT inválido.");
                    return;
                }

                monitoringService.updateHeartbeat(parts[1]);
                break;

            case "STATUS":
                if (parts.length < 5) {
                    System.out.println("[UDP] STATUS inválido.");
                    return;
                }

                try {
                    String machineName = parts[1];
                    int cpuUsage = Integer.parseInt(parts[2]);
                    int memoryUsage = Integer.parseInt(parts[3]);
                    int diskUsage = Integer.parseInt(parts[4]);

                    monitoringService.updateStatus(machineName, cpuUsage, memoryUsage, diskUsage);
                } catch (NumberFormatException e) {
                    System.out.println("[UDP] Métricas inválidas.");
                }

                break;

            default:
                System.out.println("[UDP] Comando desconhecido: " + command);
        }
    }
}