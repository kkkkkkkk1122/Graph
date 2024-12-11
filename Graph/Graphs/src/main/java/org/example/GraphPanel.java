package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Класс для отображения графа на панели.
 */
public class GraphPanel extends JPanel {
    private GraphView graph;

    /**
     *  Устанавливает граф для отображения на панели.
     * @param graph граф для отображения.
     */
    public void setGraph(GraphView graph) {
        this.graph = graph;
        repaint();
    }

    /**
     * Переопределяет метод для отрисовки содержимого.
     * @param g графический контекст.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graph.draw(g2d, getWidth(), getHeight());
        }
    }
}
