package client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public boolean register(String machineName) {
        if (socket == null || out == null || in == null) return false;
        try {
            out.println("REGISTER|" + machineName);
            String response = in.readLine();
            return response != null && response.startsWith("REGISTER_OK");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> getMachineList() {
        List<String[]> list = new ArrayList<>();
        if (socket == null || out == null || in == null) return list;
        try {
            out.println("GET_MACHINE_LIST");
            String response = in.readLine();
            if (response != null && response.startsWith("MACHINE_LIST|")) {
                if (response.equals("MACHINE_LIST|EMPTY")) {
                    return list;
                }
                String[] parts = response.substring("MACHINE_LIST|".length()).split("\\|");
                for (String part : parts) {
                    // machine format: name,ip,cpu,ram,disk
                    String[] machineData = part.split(",");
                    if (machineData.length >= 5) {
                        list.add(machineData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String[] getMachineDetails(String machineName) {
        if (socket == null || out == null || in == null) return null;
        try {
            out.println("GET_MACHINE_DETAILS|" + machineName);
            String response = in.readLine();
            if (response != null && response.startsWith("MACHINE_DETAILS|")) {
                // response format: MACHINE_DETAILS|name,ip,cpu,ram,disk,lastHeartbeat
                String details = response.substring("MACHINE_DETAILS|".length());
                return details.split(",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        if (socket != null && out != null) {
            try {
                out.println("DISCONNECT");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
