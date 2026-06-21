package client.gui;

import client.gui.components.CpuLineChart;
import client.gui.components.MetricBar;
import client.model.MachineMetrics;
import client.network.TcpClient;
import client.network.UdpClient;
import client.service.MetricsSimulator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainFrame extends JFrame {
    private final TcpClient tcpClient;
    private final UdpClient udpClient;
    private final MachineMetrics metrics;
    private final MetricsSimulator simulator;

    private MetricBar cpuBar;
    private MetricBar ramBar;
    private MetricBar diskBar;
    private CpuLineChart cpuChart;
    private DefaultTableModel tableModel;
    private JTable table;
    
    private JTextArea logArea;
    private JLabel lblHeader;
    
    private final long startTime;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public MainFrame(TcpClient tcpClient, UdpClient udpClient, MachineMetrics metrics, MetricsSimulator simulator) {
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        this.metrics = metrics;
        this.simulator = simulator;
        this.startTime = System.currentTimeMillis();

        setTitle("Network Monitor - " + metrics.getMachineName());
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectAndExit();
            }
        });

        initUI();

        // Configurar o callback do simulador para atualizar nossa UI local
        simulator.setUiUpdateCallback(this::updateLocalMetricsUI);
        
        // Configurar o callback de logs
        simulator.setLogCallback(this::appendLog);
        
        // Iniciar simulação e updates
        simulator.startSimulation();
        
        // Auto-refresh timer (Uptime a cada 1s e Tabela a cada 3s)
        Timer autoRefreshTimer = new Timer(1000, e -> updateUptime());
        autoRefreshTimer.start();
        
        Timer tableRefreshTimer = new Timer(3000, e -> refreshMachineList());
        tableRefreshTimer.start();
        
        // Primeira carga
        refreshMachineList();
        appendLog("Sistema iniciado e conectado com sucesso.");
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Header
        lblHeader = new JLabel();
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        updateUptime(); // Define o texto inicial
        add(lblHeader, BorderLayout.NORTH);

        // Center Panel (Split in two)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));

        // Left Panel (My Metrics)
        JPanel myMetricsPanel = new JPanel(new BorderLayout(0, 10));
        myMetricsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Meu Status Atual"));
        
        JPanel barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        cpuBar = new MetricBar("CPU Usage", new Color(46, 204, 113));
        ramBar = new MetricBar("RAM Usage", new Color(52, 152, 219));
        diskBar = new MetricBar("Disk Usage", new Color(155, 89, 182));

        barsPanel.add(Box.createVerticalStrut(10));
        barsPanel.add(cpuBar);
        barsPanel.add(Box.createVerticalStrut(15));
        barsPanel.add(ramBar);
        barsPanel.add(Box.createVerticalStrut(15));
        barsPanel.add(diskBar);
        
        myMetricsPanel.add(barsPanel, BorderLayout.NORTH);
        
        // Chart
        cpuChart = new CpuLineChart("Histórico de CPU");
        myMetricsPanel.add(cpuChart, BorderLayout.CENTER);

        centerPanel.add(myMetricsPanel);

        // Right Panel (Network Dashboard)
        JPanel networkPanel = new JPanel(new BorderLayout());
        networkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Visão Geral da Rede"));

        String[] columns = {"Máquina", "IP", "CPU", "RAM", "Disco"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Evento de duplo-clique na tabela
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable t = (JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && t.getSelectedRow() != -1) {
                    String machineName = (String) t.getValueAt(t.getSelectedRow(), 0);
                    showMachineDetails(machineName);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        networkPanel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel lblHint = new JLabel("Dica: Duplo-clique para mais detalhes", SwingConstants.CENTER);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(Color.GRAY);
        networkPanel.add(lblHint, BorderLayout.SOUTH);

        centerPanel.add(networkPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel (Logs & Disconnect)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 120));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Terminal de Eventos"));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(46, 204, 113));
        
        JScrollPane logScroll = new JScrollPane(logArea);
        bottomPanel.add(logScroll, BorderLayout.CENTER);
        
        JButton btnDisconnect = new JButton("Desconectar");
        btnDisconnect.setBackground(new Color(231, 76, 60));
        btnDisconnect.setForeground(Color.WHITE);
        btnDisconnect.setFocusPainted(false);
        btnDisconnect.addActionListener(e -> disconnectAndExit());
        
        JPanel pnlBtn = new JPanel(new BorderLayout());
        pnlBtn.add(btnDisconnect, BorderLayout.SOUTH);
        bottomPanel.add(pnlBtn, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateUptime() {
        long elapsed = System.currentTimeMillis() - startTime;
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / (1000 * 60)) % 60;
        long hours = (elapsed / (1000 * 60 * 60));
        
        String uptimeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        lblHeader.setText(String.format("<html>🟢 <font color='green'>Online</font> | Máquina: %s | Uptime: %s</html>", 
                                        metrics.getMachineName(), uptimeStr));
    }

    private void appendLog(String message) {
        String time = timeFormat.format(new Date());
        logArea.append(String.format("[%s] %s\n", time, message));
        // Auto-scroll para baixo
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void updateLocalMetricsUI() {
        cpuBar.setValue(metrics.getCpuUsage());
        ramBar.setValue(metrics.getMemoryUsage());
        diskBar.setValue(metrics.getDiskUsage());
        cpuChart.addValue(metrics.getCpuUsage());
    }

    private void refreshMachineList() {
        new SwingWorker<List<String[]>, Void>() {
            @Override
            protected List<String[]> doInBackground() {
                return tcpClient.getMachineList();
            }

            @Override
            protected void done() {
                try {
                    List<String[]> list = get();
                    // Para evitar flicker na UI, poderíamos atualizar apenas as linhas alteradas,
                    // mas setRowCount(0) é rápido o suficiente para listas pequenas.
                    int selectedRow = table.getSelectedRow();
                    
                    tableModel.setRowCount(0);
                    for (String[] rowData : list) {
                        if (rowData.length >= 5) {
                            rowData[2] = rowData[2] + "%";
                            rowData[3] = rowData[3] + "%";
                            rowData[4] = rowData[4] + "%";
                        }
                        tableModel.addRow(rowData);
                    }
                    
                    if (selectedRow != -1 && selectedRow < tableModel.getRowCount()) {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                } catch (Exception e) {
                    appendLog("Erro ao atualizar lista de máquinas.");
                }
            }
        }.execute();
    }
    
    private void showMachineDetails(String machineName) {
        new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() {
                return tcpClient.getMachineDetails(machineName);
            }

            @Override
            protected void done() {
                try {
                    String[] details = get();
                    if (details != null && details.length >= 6) {
                        // details format: name,ip,cpu,ram,disk,lastHeartbeat
                        long heartbeatTs = Long.parseLong(details[5]);
                        String lastSeen = timeFormat.format(new Date(heartbeatTs));
                        
                        String message = String.format(
                            "Detalhes do Servidor para a Máquina:\n\n" +
                            "Nome: %s\n" +
                            "IP: %s\n" +
                            "CPU: %s%%\n" +
                            "RAM: %s%%\n" +
                            "Disco: %s%%\n" +
                            "Último Sinal de Vida: %s",
                            details[0], details[1], details[2], details[3], details[4], lastSeen
                        );
                        JOptionPane.showMessageDialog(MainFrame.this, message, "Detalhes da Máquina", JOptionPane.INFORMATION_MESSAGE);
                        appendLog("Consultou detalhes de: " + machineName);
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "Erro ao buscar detalhes.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    appendLog("Erro ao buscar detalhes da máquina " + machineName);
                }
            }
        }.execute();
    }

    private void disconnectAndExit() {
        simulator.stopSimulation();
        appendLog("Desconectando...");
        new Thread(() -> {
            tcpClient.disconnect();
            udpClient.close();
            System.exit(0);
        }).start();
    }
}
