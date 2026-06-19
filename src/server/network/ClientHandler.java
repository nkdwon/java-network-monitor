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

                String response = processMessage(message, clientIp);
                output.println(response);
            }

        } catch (IOException e) {
            System.out.println("[TCP] Cliente desconectado.");
        }
    }

    private String processMessage(String message, String clientIp) {
        String[] parts = message.split("\\|");

        if (parts.length == 0) {
            return "ERROR|INVALID_FORMAT";
        }

        String command = parts[0];

        switch (command) {
            case "REGISTER":
                if (parts.length < 2) {
                    return "ERROR|INVALID_REGISTER_FORMAT";
                }

                String machineName = parts[1];
                monitoringService.registerMachine(machineName, clientIp);
                return "REGISTER_OK|" + machineName;

            case "GET_MACHINE_LIST":
                return monitoringService.getMachineList();

            case "GET_MACHINE_DETAILS":
                if (parts.length < 2) {
                    return "ERROR|INVALID_DETAILS_FORMAT";
                }

                return monitoringService.getMachineDetails(parts[1]);

            case "DISCONNECT":
                return "DISCONNECT_OK";

            default:
                return "ERROR|UNKNOWN_COMMAND";
        }
    }
}