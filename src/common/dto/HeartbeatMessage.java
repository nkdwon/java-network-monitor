package common.dto;

public class HeartbeatMessage {
    private String machineName;
    private long timestamp;

    public HeartbeatMessage(String machineName, long timestamp) {
        this.machineName = machineName;
        this.timestamp = timestamp;
    }

    public String getMachineName() {
        return machineName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}