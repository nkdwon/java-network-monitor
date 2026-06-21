package client.gui;

import client.model.MachineMetrics;
import client.network.TcpClient;
import client.network.UdpClient;
import client.service.MetricsSimulator;
import common.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConnectionFrame extends JFrame {
    private JTextField txtIp;
    private JTextField txtMachineName;
    private JButton btnConnect;
    private JLabel lblStatus;

    public ConnectionFrame() {
        setTitle("Conexão - Monitor de Rede");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblTitle = new JLabel("Configuração do Cliente", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("IP do Servidor:"), gbc);
        
        txtIp = new JTextField("127.0.0.1", 15);
        gbc.gridx = 1;
        panel.add(txtIp, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nome da Máquina:"), gbc);
        
        txtMachineName = new JTextField("PC-01", 15);
        gbc.gridx = 1;
        panel.add(txtMachineName, gbc);

        btnConnect = new JButton("Conectar");
        btnConnect.setBackground(new Color(41, 128, 185));
        btnConnect.setForeground(Color.WHITE);
        btnConnect.setFocusPainted(false);
        btnConnect.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnect.addActionListener(this::onConnect);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnConnect, gbc);

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(new Color(231, 76, 60));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 4;
        panel.add(lblStatus, gbc);

        add(panel);
    }

    private void onConnect(ActionEvent e) {
        String ip = txtIp.getText().trim();
        String name = txtMachineName.getText().trim();

        if (ip.isEmpty() || name.isEmpty()) {
            lblStatus.setText("Preencha todos os campos.");
            return;
        }

        btnConnect.setEnabled(false);
        lblStatus.setForeground(Color.BLACK);
        lblStatus.setText("Conectando...");

        // Usando SwingWorker para não travar a UI
        new SwingWorker<Boolean, Void>() {
            private TcpClient tcpClient;
            private UdpClient udpClient;

            @Override
            protected Boolean doInBackground() {
                try {
                    tcpClient = new TcpClient();
                    tcpClient.connect(ip, Constants.TCP_PORT);
                    
                    if (tcpClient.register(name)) {
                        udpClient = new UdpClient(ip, Constants.UDP_PORT);
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        MachineMetrics metrics = new MachineMetrics(name);
                        MetricsSimulator simulator = new MetricsSimulator(metrics, udpClient);
                        
                        MainFrame mainFrame = new MainFrame(tcpClient, udpClient, metrics, simulator);
                        mainFrame.setVisible(true);
                        
                        dispose(); // Fecha tela de login
                    } else {
                        lblStatus.setForeground(new Color(231, 76, 60));
                        lblStatus.setText("Erro ao conectar ou registrar.");
                        btnConnect.setEnabled(true);
                    }
                } catch (Exception ex) {
                    lblStatus.setForeground(new Color(231, 76, 60));
                    lblStatus.setText("Erro inesperado.");
                    btnConnect.setEnabled(true);
                }
            }
        }.execute();
    }
}
