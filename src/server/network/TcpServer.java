package server.network;

import server.service.MonitoringService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {
    private final int port;
    private final MonitoringService monitoringService;

    public TcpServer(int port, MonitoringService monitoringService) {
        this.port = port;
        this.monitoringService = monitoringService;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[TCP] Servidor TCP iniciado na porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, monitoringService);
                handler.start();
            }

        } catch (IOException e) {
            System.out.println("[TCP] Erro no servidor TCP: " + e.getMessage());
        }
    }
}