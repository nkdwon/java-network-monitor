package server.service;

import server.model.ConnectedMachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitoringService {
    private final Map<String, ConnectedMachine> machines = new ConcurrentHashMap<>();

    public void registerMachine(String name, String ip) {
        machines.put(name, new ConnectedMachine(name, ip));
        System.out.println("[MONITOR] Máquina registrada: " + name + " - " + ip);
    }

    public void updateHeartbeat(String name) {
        ConnectedMachine machine = machines.get(name);

        if (machine != null) {
            machine.updateHeartbeat();
            System.out.println("[MONITOR] Heartbeat recebido de: " + name);
        } else {
            System.out.println("[MONITOR] Heartbeat de máquina não registrada: " + name);
        }
    }

    public void updateStatus(String name, int cpuUsage, int memoryUsage, int diskUsage) {
        ConnectedMachine machine = machines.get(name);

        if (machine != null) {
            machine.updateStatus(cpuUsage, memoryUsage, diskUsage);
            System.out.println("[MONITOR] Status atualizado: " + name
                    + " | CPU: " + cpuUsage + "%"
                    + " | RAM: " + memoryUsage + "%"
                    + " | DISCO: " + diskUsage + "%"); 
            checkAlerts(name, cpuUsage, memoryUsage, diskUsage);
        } else {
            System.out.println("[MONITOR] Status de máquina não registrada: " + name);
        }
    }

    public String getMachineList() {
        if (machines.isEmpty()) {
            return "MACHINE_LIST|EMPTY";
        }

        StringBuilder builder = new StringBuilder("MACHINE_LIST");

        for (ConnectedMachine machine : machines.values()) {
            builder.append("|")
                    .append(machine.getName())
                    .append(",")
                    .append(machine.getIp())
                    .append(",")
                    .append(machine.getCpuUsage())
                    .append(",")
                    .append(machine.getMemoryUsage())
                    .append(",")
                    .append(machine.getDiskUsage());
        }

        return builder.toString();
    }

    public String getMachineDetails(String name) {
        ConnectedMachine machine = machines.get(name);

        if (machine == null) {
            return "ERROR|MACHINE_NOT_FOUND";
        }

        return "MACHINE_DETAILS|" + machine.getDetails();
    }

    private void checkAlerts(String name, int cpuUsage, int memoryUsage, int diskUsage) {
        if (cpuUsage >= 90) {
            System.out.println("[ALERT] CPU alta em " + name + ": " + cpuUsage + "%");
        }

        if (memoryUsage >= 90) {
            System.out.println("[ALERT] RAM alta em " + name + ": " + memoryUsage + "%");
        }

        if (diskUsage >= 90) {
            System.out.println("[ALERT] DISCO alto em " + name + ": " + diskUsage + "%");
        }
    }
}