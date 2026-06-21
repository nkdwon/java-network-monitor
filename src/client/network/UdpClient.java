package client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int port;

    public UdpClient(String serverIp, int port) throws IOException {
        this.socket = new DatagramSocket();
        this.serverAddress = InetAddress.getByName(serverIp);
        this.port = port;
    }

    public void sendHeartbeat(String machineName) {
        String message = "HEARTBEAT|" + machineName;
        sendPacket(message);
    }

    public void sendStatus(String machineName, int cpu, int ram, int disk) {
        String message = "STATUS|" + machineName + "|" + cpu + "|" + ram + "|" + disk;
        sendPacket(message);
    }

    private void sendPacket(String message) {
        if (socket == null || socket.isClosed()) return;
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
