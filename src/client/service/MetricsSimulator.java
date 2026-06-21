package client.service;

import client.model.MachineMetrics;
import client.network.UdpClient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

public class MetricsSimulator {
    private final MachineMetrics metrics;
    private final UdpClient udpClient;
    private final Timer timer;
    private final Random random;
    private Runnable uiUpdateCallback;
    private Consumer<String> logCallback;

    public MetricsSimulator(MachineMetrics metrics, UdpClient udpClient) {
        this.metrics = metrics;
        this.udpClient = udpClient;
        this.timer = new Timer(true); // run as daemon
        this.random = new Random();
    }

    public void setUiUpdateCallback(Runnable uiUpdateCallback) {
        this.uiUpdateCallback = uiUpdateCallback;
    }

    public void setLogCallback(Consumer<String> logCallback) {
        this.logCallback = logCallback;
    }

    public void startSimulation() {
        // Heartbeat timer (every 5 seconds)
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                udpClient.sendHeartbeat(metrics.getMachineName());
                if (logCallback != null) {
                    SwingUtilities.invokeLater(() -> logCallback.accept("Heartbeat enviado via UDP."));
                }
            }
        }, 0, 5000);

        // Status update timer (every 2 seconds)
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateMetrics();
                udpClient.sendStatus(metrics.getMachineName(), metrics.getCpuUsage(), metrics.getMemoryUsage(), metrics.getDiskUsage());
                
                if (logCallback != null) {
                    SwingUtilities.invokeLater(() -> logCallback.accept(
                        String.format("Status UDP enviado: CPU %d%% | RAM %d%% | DISCO %d%%", 
                                      metrics.getCpuUsage(), metrics.getMemoryUsage(), metrics.getDiskUsage())
                    ));
                }

                if (uiUpdateCallback != null) {
                    SwingUtilities.invokeLater(uiUpdateCallback);
                }
            }
        }, 1000, 2000);
    }

    private void updateMetrics() {
        // Simulate some smooth variations
        metrics.setCpuUsage(simulateVariation(metrics.getCpuUsage(), 10));
        metrics.setMemoryUsage(simulateVariation(metrics.getMemoryUsage(), 5));
        
        // Disk grows slowly or stays same, rarely decreases
        int disk = metrics.getDiskUsage();
        if (random.nextInt(10) > 7) {
            disk += random.nextInt(3);
        }
        if (disk > 100) disk = 100;
        metrics.setDiskUsage(disk);
    }

    private int simulateVariation(int currentValue, int maxStep) {
        if (currentValue == 0) return random.nextInt(30) + 10; // Initial value
        int change = random.nextInt(maxStep * 2 + 1) - maxStep; // from -maxStep to +maxStep
        int newValue = currentValue + change;
        if (newValue < 0) newValue = 0;
        if (newValue > 100) newValue = 100;
        return newValue;
    }

    public void stopSimulation() {
        timer.cancel();
    }
}
