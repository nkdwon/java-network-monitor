package client;

import client.gui.ConnectionFrame;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        // Tentativa de definir um Look and Feel mais moderno e bonito
        try {
            // Tenta usar o Nimbus (Look and Feel padrão do Java que é mais moderno que o Metal)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            // Customizando um pouco das cores do Nimbus
            UIManager.put("nimbusBase", new java.awt.Color(40, 42, 54));
            UIManager.put("nimbusBlueGrey", new java.awt.Color(68, 71, 90));
            UIManager.put("control", new java.awt.Color(248, 248, 242));
            
        } catch (Exception e) {
            // Se falhar, usa o padrão do sistema operacional
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            ConnectionFrame frame = new ConnectionFrame();
            frame.setVisible(true);
        });
    }
}
