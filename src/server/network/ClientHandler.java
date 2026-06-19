package server.network;

import server.service.MonitoringService;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final MonitoringService monitoringService;

    public ClientHandler(Socket socket, MonitoringService monitoringService) {
        this.socket = socket;
        this.monitoringService = monitoringService;
    }

    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
            String clientIp = socket.getInetAddress().getHostAddress();

            if (clientIp.equals("0:0:0:0:0:0:0:1")) {
                clientIp = "127.0.0.1";
            }
            System.out.println("[TCP] Cliente conectado: " + clientIp);

            String message;

            while ((message = input.readLine()) != null) {
                System.out.println("[TCP] Mensagem recebida: " + message);

                if (message.startsWith("REGISTER:")) {
                    String machineName = message.substring("REGISTER:".length());
                    monitoringService.registerMachine(machineName, clientIp);
                    output.println("REGISTER_OK");
                } else {
                    output.println("UNKNOWN_COMMAND");
                }
            }

        } catch (IOException e) {
            System.out.println("[TCP] Cliente desconectado.");
        }
    }
}