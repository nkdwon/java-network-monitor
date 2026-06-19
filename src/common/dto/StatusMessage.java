package common.dto;

public class StatusMessage {
    private String machineName;
    private int cpuUsage;
    private int memoryUsage;
    private int diskUsage;

    public StatusMessage(String machineName, int cpuUsage, int memoryUsage, int diskUsage) {
        this.machineName = machineName;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
    }

    public String getMachineName() {
        return machineName;
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
}