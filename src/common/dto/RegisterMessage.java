package common.dto;

public class RegisterMessage {
    private String machineName;
    private String ip;

    public RegisterMessage(String machineName, String ip) {
        this.machineName = machineName;
        this.ip = ip;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getIp() {
        return ip;
    }
}