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
}