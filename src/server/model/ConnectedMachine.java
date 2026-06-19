package server.model;

public class ConnectedMachine {
    private String name;
    private String ip;
    private long lastHeartbeat;
    private int cpuUsage;
    private int memoryUsage;
    private int diskUsage;

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

    public int getCpuUsage() {
        return cpuUsage;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public int getDiskUsage() {
        return diskUsage;
    }

    public void updateHeartbeat() {
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public void updateStatus(int cpuUsage, int memoryUsage, int diskUsage) {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        updateHeartbeat();
    }

    public String getDetails() {
        return name + "," + ip + "," + cpuUsage + "," + memoryUsage + "," + diskUsage + "," + lastHeartbeat;
    }
}