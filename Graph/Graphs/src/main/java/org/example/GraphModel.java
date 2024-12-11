package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.List;

/**
 * Класс представляет граф с заданным количеством вершин и петель.
 */
public class GraphModel {
    /**
     * Логгер для данного класса.
     */
    private static final Logger logger = LogManager.getLogger(GraphModel.class);

    private final int nodes;
    private final int loops;
    private final List<List<Integer>> adjacencyList;
    private final Set<Integer> selfLoops;
    private final Random rand;
    public static final int NODE_RADIUS = 20;
    public static final int ARROW_SIZE = 10;

    /**
     * Вложенный класс для работы с ребрами графа.
     */
    class Ribs {
        /**
         * Генерирует ребра между вершинами.
         */
        private void generateRibs() {
            logger.info("Starting to generate ribs.");
            for (int i = 0; i < nodes; i++) {
                int edges = rand.nextInt(nodes - 1) + 1; // Количество дуг
                for (int j = 0; j < edges; j++) {
                    int target = rand.nextInt(nodes);
                    if (target != i && !adjacencyList.get(i).contains(target) && !adjacencyList.get(target).contains(i)) {
                        adjacencyList.get(i).add(target); // Добавляем рёбро только в одну сторону
                        logger.debug("Added edge from node {} to node {}.", i, target);
                    }
                }
            }
            logger.info("Finished generating ribs.");
        }

        /**
         * Генерирует петли для вершин графа.
         */
        private void generateLoops() {
            logger.info("Starting to generate loops.");
            for (int i = 0; i < loops; i++) {
                int node;
                do {
                    node = rand.nextInt(nodes); // Выбор случайного узла
                } while (selfLoops.contains(node));
                selfLoops.add(node);
                adjacencyList.get(node).add(node);
                logger.debug("Added loop to node {}.", node);
            }
            logger.info("Finished generating loops.");
        }

        /**
         * Генерируем ребра и петли для графа.
         */
        public void generateEdgesAndLoops() {
            logger.info("Starting to generate edges and loops.");
            generateRibs();
            generateLoops();
            logger.info("Finished generating edges and loops.");
        }
    }

    /**
     * Класс для вычисления координат ребер на графе.
     */
    static class RibLines {

        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;
        private int[] lineCoordinates;
        private int[] arrowCoordinates;

        /**
         * Конструктор для вычисления координат ребер.
         * @param x1 координата X начала.
         * @param y1 координата Y начала.
         * @param x2 координата X конца.
         * @param y2 координата Y конца.
         */
        public RibLines(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            calculateLine();
            calculateArrow();
        }

        /**
         * Вычисляет координаты стрелки.
         */
        private void calculateArrow() { // Размер стрелки
            double angle = Math.atan2(y2 - y1, x2 - x1);
            arrowCoordinates = new int[4];
            arrowCoordinates[0] = (int) (lineCoordinates[2] - ARROW_SIZE * Math.cos(angle - Math.PI / 6));
            arrowCoordinates[1] = (int) (lineCoordinates[3] - ARROW_SIZE * Math.sin(angle - Math.PI / 6));
            arrowCoordinates[2] = (int) (lineCoordinates[2] - ARROW_SIZE * Math.cos(angle + Math.PI / 6));
            arrowCoordinates[3] = (int) (lineCoordinates[3] - ARROW_SIZE * Math.sin(angle + Math.PI / 6));
        }

        /**
         * Вычисляет координаты линии.
         */
        private void calculateLine() {
            lineCoordinates = new int[4];
            double dx = x2 - x1;
            double dy = y2 - y1;
            double length = Math.sqrt(dx * dx + dy * dy);
            double scale = (length - 20) / length;
            lineCoordinates[0] = (int) (x2 - scale * dx);
            lineCoordinates[1] = (int) (y2 - scale * dy);
            lineCoordinates[2] = (int) (x1 + scale * dx);
            lineCoordinates[3] = (int) (y1 + scale * dy);
        }

        /**
         * Возвращает координаты линии.
         * @return массив координат линии.
         */
        public int[] getLineCoordinates() {
            return lineCoordinates;
        }

        /**
         * Возвращает координаты стрелки.
         * @return массив координат стрелки.
         */
        public int[] getArrowCoordinates() {
            return arrowCoordinates;
        }

    }

    /**
     * Класс для управления вершинами графа.
     */
    class Node {
        private int[] nodeValues;
        private int[] xCoords;
        private int[] yCoords;

        /**
         * Генерирует вершины графа.
         * @param width ширина области графа.
         * @param height высота области графа.
         */
        public void generateNodes(int width, int height) {
            logger.info("Starting to generate nodes.");
            int radius = Math.min(width, height) / 3;
            int centerX = width / 2;
            int centerY = height / 2;
            xCoords = new int[nodes];
            yCoords = new int[nodes];

            nodeValues = new int[nodes];
            for (int i = 0; i < nodes; i++) {
                nodeValues[i] = i + 1; // Значения узлов от 1 до model.getNodes()
                double angle = 2 * Math.PI * i / nodes;
                xCoords[i] = (int) (centerX + radius * Math.cos(angle));
                yCoords[i] = (int) (centerY + radius * Math.sin(angle));
                logger.debug("Node {} positioned at ({}, {}).", i + 1, xCoords[i], yCoords[i]);
            }
            logger.info("Finished generating nodes.");
        }

        /**
         * Возвращает массив значений вершин графа.
         * @return массив значений вершин.
         */
        public int[] getNodeValues() {
            return nodeValues;
        }

        /**
         * Возвращает массив координат X вершишн графа.
         * @return массив координат X.
         */
        public int[] getXCoords() {
            return xCoords;
        }

        /**
         * Возвращает массив координат Y вершин графа.
         * @return массив координат Y.
         */
        public int[] getYCoords() {
            return yCoords;
        }
    }

    /**
     * Конструктор, инициализирующий модель графа с заданным количеством вершин и петель.
     * @param nodes количество вершин в графе.
     * @param loops количество петель в графе.
     */
    public GraphModel(int nodes, int loops) {
        this.nodes = nodes;
        this.loops = loops;
        this.adjacencyList = new ArrayList<>();
        this.selfLoops = new HashSet<>();
        rand = new Random();
        logger.info("Initializing GraphModel with {} nodes and {} loops.", nodes, loops);
        for (int i = 0; i < nodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        Ribs ribs = new Ribs();
        ribs.generateEdgesAndLoops();
        logger.info("GraphModel initialization complete.");
    }

    /**
     * Возвращает количество вершин в графе.
     * @return количество вершин.
     */
    public int getNodes() {
        return nodes;
    }

    /**
     * Возвращает список смежности графа.
     * @return список смежности графа.
     */
    public List<List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
}
