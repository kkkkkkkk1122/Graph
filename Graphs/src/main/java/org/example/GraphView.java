package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.CubicCurve2D;

/**
 * Класс отвечает за визуализацию графа.
 */
public class GraphView {
    private static final Logger logger = LogManager.getLogger(GraphView.class);

    private final GraphModel model;
    private GraphModel.Node nodes;

    /**
     * Конструктор, инициализирующий объект визуализации графа.
     * @param model объект, предоставляющий данные графа для визуализации.
     */
    public GraphView(GraphModel model) {
        this.model = model;
        logger.info("GraphView initialized with model containing {} nodes.", model.getNodes());
    }

    /**
     * Отрисовывает граф на предоставленном графическом контексте.
     * @param g графический контекст.
     * @param width ширина области отрисовки.
     * @param height высота области отрисовки.
     */
    public void draw(Graphics2D g, int width, int height) {
        logger.info("Starting to draw graph with width={} and height={}.", width, height);
        nodes = model.new Node();

        nodes.generateNodes(width, height);
        logger.debug("Generated nodes with coordinates.");

        drawNodes(g);
        drawRibs(g);
        setNodeValues(g);

        logger.info("Graph drawing completed.");
    }

    /**
     * Отрисовывает вершины графа.
     * @param g графический контекст.
     */
    private void drawNodes(Graphics2D g) {
        logger.info("Drawing nodes.");
        g.setStroke(new BasicStroke(4));
        for (int i = 0; i < model.getNodes(); i++) {
            g.setColor(Color.BLACK);
            g.drawOval(nodes.getXCoords()[i] - GraphModel.NODE_RADIUS, nodes.getYCoords()[i] - GraphModel.NODE_RADIUS,
                    GraphModel.NODE_RADIUS * 2, GraphModel.NODE_RADIUS * 2);

            g.setColor(Color.BLUE);
            g.fillOval(nodes.getXCoords()[i] - GraphModel.NODE_RADIUS, nodes.getYCoords()[i] - GraphModel.NODE_RADIUS,
                    GraphModel.NODE_RADIUS * 2, GraphModel.NODE_RADIUS * 2);
            logger.debug("Node {} drawn at ({}, {}).", i + 1, nodes.getXCoords()[i], nodes.getYCoords()[i]);
        }
    }

    /**
     * Отрисовывает ребра графа.
     * @param g графичекий контекст.
     */
    private void drawRibs(Graphics2D g) {
        logger.info("Drawing ribs.");
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2)); // Толщина ребер
        for (int i = 0; i < model.getNodes(); i++) {
            for (int j : model.getAdjacencyList().get(i)) {
                if (i == j) {
                    logger.debug("Drawing self-loop for node {}.", i + 1);
                    drawSelfLoop(g, nodes.getXCoords()[i], nodes.getYCoords()[i]);
                } else {
                    logger.debug("Drawing rib from node {} to node {}.", i + 1, j + 1);
                    drawRib(g, nodes.getXCoords()[i], nodes.getYCoords()[i], nodes.getXCoords()[j], nodes.getYCoords()[j]);
                }
            }
        }
    }

    /**
     * Отрисовывает отдельное ребро между двумя вершинами.
     * @param g графический контекст.
     * @param x1 координата X начальной вершины.
     * @param y1 координата Y начальной вершины.
     * @param x2 координата X конечной вершины.
     * @param y2 координата Y конечной вершины.
     */
    private void drawRib(Graphics2D g, int x1, int y1, int x2, int y2) {
        GraphModel.RibLines lines = new GraphModel.RibLines(x1, y1, x2, y2);
        int[] lineCoordinates = lines.getLineCoordinates();
        int[] arrowCoordinates = lines.getArrowCoordinates();

        g.drawLine(lineCoordinates[0], lineCoordinates[1], lineCoordinates[2], lineCoordinates[3]);
        g.fillPolygon(new int[]{lineCoordinates[2], arrowCoordinates[0], arrowCoordinates[2]},
                new int[]{lineCoordinates[3], arrowCoordinates[1], arrowCoordinates[3]}, 3);

        logger.debug("Rib drawn from ({}, {}) to ({}, {}).", x1, y1, x2, y2);
    }

    /**
     * Устанавливает значения узлов.
     * @param g графический контекст.
     */
    private void setNodeValues(Graphics2D g) {
        logger.info("Setting node values.");
        g.setColor(Color.WHITE); // Цвет текста
        g.setFont(new Font("Arial", Font.BOLD, 14)); // Размер и стиль шрифта
        for (int i = 0; i < model.getNodes(); i++) {
            String valueText = String.valueOf(nodes.getNodeValues()[i]);
            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(valueText);
            int textHeight = metrics.getHeight();

            g.drawString(
                    valueText,
                    nodes.getXCoords()[i] - textWidth / 2,
                    nodes.getYCoords()[i] + textHeight / 4
            );

            logger.debug("Node value {} set at ({}, {}).", valueText, nodes.getXCoords()[i], nodes.getYCoords()[i]);
        }
    }

    /**
     * Отрисовывает петлю для вершины.
     * @param g графический контекст.
     * @param x координата X вершины.
     * @param y координата Y вершины.
     */
    private void drawSelfLoop(Graphics2D g, int x, int y) {
        logger.info("Drawing self-loop at ({}, {}).", x, y);
        int loopSize = 40; // Размер петли (радиус)
        int offset = 15; // Смещение, чтобы линия начиналась от границы узла

        g.draw(new CubicCurve2D.Double(
                x - offset, y - offset,
                x - offset - loopSize, y - offset - loopSize,
                x + offset + loopSize, y - offset - loopSize,
                x + offset, y - offset
        ));

        logger.debug("Self-loop drawn at ({}, {}).", x, y);
    }
}
