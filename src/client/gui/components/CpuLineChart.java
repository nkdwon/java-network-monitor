package client.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class CpuLineChart extends JPanel {
    private final LinkedList<Integer> history;
    private final int MAX_HISTORY = 30; // Guarda os últimos 30 pontos
    private final String title;

    public CpuLineChart(String title) {
        this.title = title;
        this.history = new LinkedList<>();
        setPreferredSize(new Dimension(300, 150));
        setBackground(new Color(40, 42, 54)); // Fundo escuro igual o Nimbus base
        setBorder(BorderFactory.createLineBorder(new Color(68, 71, 90), 2));
    }

    public void addValue(int value) {
        if (history.size() >= MAX_HISTORY) {
            history.removeFirst();
        }
        history.addLast(value);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Anti-aliasing para deixar a linha suave
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Desenhar grid de fundo
        g2d.setColor(new Color(68, 71, 90));
        for (int i = 0; i < height; i += 25) {
            g2d.drawLine(0, i, width, i);
        }

        if (history.size() < 2) return;

        // Calcular escalas
        double xScale = (double) width / (MAX_HISTORY - 1);
        double yScale = (double) (height - 20) / 100.0; // deixa uma margem de 20px no topo/base

        // Pontos para o polígono
        int[] xPoints = new int[history.size()];
        int[] yPoints = new int[history.size()];

        for (int i = 0; i < history.size(); i++) {
            xPoints[i] = (int) (i * xScale);
            // Inverte o Y pois na tela o 0 é no topo
            yPoints[i] = (int) (height - 10 - (history.get(i) * yScale)); 
        }

        // Desenha a linha
        g2d.setColor(new Color(46, 204, 113)); // Verde chamativo
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawPolyline(xPoints, yPoints, history.size());

        // Preenche a área debaixo da linha (opcional para dar um efeito "wow")
        int[] fillXPoints = new int[history.size() + 2];
        int[] fillYPoints = new int[history.size() + 2];
        System.arraycopy(xPoints, 0, fillXPoints, 0, history.size());
        System.arraycopy(yPoints, 0, fillYPoints, 0, history.size());
        
        fillXPoints[history.size()] = xPoints[history.size() - 1];
        fillYPoints[history.size()] = height;
        fillXPoints[history.size() + 1] = 0;
        fillYPoints[history.size() + 1] = height;

        g2d.setColor(new Color(46, 204, 113, 50)); // Verde com transparência
        g2d.fillPolygon(fillXPoints, fillYPoints, fillXPoints.length);

        // Desenha os pontos (bolinhas)
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < history.size(); i++) {
            g2d.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
        }

        // Desenha o título e valor atual
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.drawString(title + ": " + history.getLast() + "%", 10, 15);
    }
}
