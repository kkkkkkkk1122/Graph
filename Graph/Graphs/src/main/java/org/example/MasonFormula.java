package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс позволяет анализировать граф и вычислять количество петель с помощью формулы Мейсона.
 */
public class MasonFormula {
    private static final Logger logger = LogManager.getLogger(MasonFormula.class);

    private final int nodes;
    private final List<List<Integer>> adjacencyList;
    private final List<Double> loopWeights; // Список весов петель

    /**
     * Конструктор, инициализирующий объект для обработки.
     * @param nodes количество вершин в графе.
     * @param adjacencyList список, предоставляющий связи между вершинами.
     */
    public MasonFormula(int nodes, List<List<Integer>> adjacencyList) {
        this.nodes = nodes;
        this.adjacencyList = adjacencyList;
        this.loopWeights = new ArrayList<>();
        logger.info("MasonFormula initialized with {} nodes.", nodes);
    }

    /**
     * Метод для поиска всех петель и их весов.
     */
    public void findAllSelfLoops() {
        logger.info("Starting to find all self-loops.");
        for (int i = 0; i < nodes; i++) {
            for (int neighbor : adjacencyList.get(i)) {
                if (neighbor == i) {
                    loopWeights.add(1.0); // Вес петли (по умолчанию 1, если не указано другое)
                    logger.debug("Self-loop found at node {} with weight {}.", i, 1.0);
                }
            }
        }
        logger.info("Completed finding self-loops. Total self-loops found: {}.", loopWeights.size());
    }

    /**
     * Метод для вычисления детерминанта Delta по формуле Мейсона.
     */
    public void calculateDelta() {
        logger.info("Starting calculation of Delta.");
        findAllSelfLoops();

        double delta = 1.0;

        // Учитываем сумму весов всех петель
        for (double weight : loopWeights) {
            delta -= weight;
            logger.debug("Updated Delta after subtracting weight {}: {}.", weight, delta);
        }

        logger.info("Calculation of Delta completed. Final Delta: {}.", delta);
    }

    /**
     * Метод для получения количества петель.
     * @return количество петель в графе.
     */
    public int getLoopCount() {
        int loopCount = loopWeights.size();
        logger.info("Returning the loop count: {}.", loopCount);
        return loopCount;
    }
}
