package server.model;
public class ConnectedMachine {
    private String name;
    private String ip;
    private long lastHeartbeat;

    public ConnectedMachine(String name, String ip) {
        this.name = name;
        this.ip = ip;
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void updateHeartbeat() {
        this.lastHeartbeat = System.currentTimeMillis();
    }
}