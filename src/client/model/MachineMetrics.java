package client.model;

public class MachineMetrics {
    private String machineName;
    private int cpuUsage;
    private int memoryUsage;
    private int diskUsage;

    public MachineMetrics(String machineName) {
        this.machineName = machineName;
        this.cpuUsage = 0;
        this.memoryUsage = 0;
        this.diskUsage = 0;
    }

    public String getMachineName() {
        return machineName;
    }

    public int getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(int cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(int memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
    }
}
