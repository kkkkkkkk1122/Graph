package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;


/**
 * Класс для генерации графов и вычисления петель.
 * Использует логгер для отслеживания событий.
 */
public class GraphApp extends JFrame {
    /**
     *  Логгер для данного класса
     */
    private static final Logger logger = LogManager.getLogger(GraphApp.class);
    private JTextField nodeCountField;
    private JTextField loopCountField;
    private JButton generateButton;
    private JButton calculateLoopsButton;

    private GraphPanel graphPanel;
    private JLabel loopCountLabel;
    private JLabel executionTimeLabel;
    private GraphModel model;

    /**
     * Конструктор приложения, выполняет инициализацию интерфейса, панелей управления.
     */
    public GraphApp() {
        // Настройка окна
        windowInit();
        logger.info("Window initialized.");

        // Панель управления
        controlPanelInit();
        logger.info("Control panel initialized.");

        // Панель для отображения графа
        graphPanelInit();
        logger.info("Graph panel initialized.");

        // Панель для отображения результатов
        resultPanelInit();
        logger.info("Result panel initialized.");

        // Слушатели для кнопок
        setActionListeners();
        logger.info("Action listeners set.");
    }

    /**
     * Генерирует граф на основе введенного количества вершин и петель.
     * Если количество петель превышает количество узлов, выдает ошибку.
     */
    private void generateGraph() {
        try {
            int nodes = Integer.parseInt(nodeCountField.getText());
            int loops = Integer.parseInt(loopCountField.getText());

            if (loops > nodes) {
                JOptionPane.showMessageDialog(this, "Number of loops cannot exceed the number of nodes.");
                logger.warn("Number of loops exceeds number of nodes.");
                return;
            }
            model = new GraphModel(nodes, loops);
            GraphView graph = new GraphView(model);
            graphPanel.setGraph(graph);
            logger.info("Graph generated with {} nodes and {} loops.", nodes, loops);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            logger.error("Invalid input entered for nodes or loops.", ex);
        }
    }

    /**
     * Вычисляет количество петель в графе с использованием формулы Мейсона.
     * Если граф не сгенерирован, выдает ошибку.
     * Измеряет время выполнения операции.
     */
    private void calculateLoops() {
        if (model == null) {
            logger.warn("Graph model is not generated yet.");
            JOptionPane.showMessageDialog(this, "Please generate the graph first.");
            return;
        }

        try {
            MasonFormula masonFormula = new MasonFormula(model.getNodes(), model.getAdjacencyList());

            // Засекаем время выполнения
            long startTime = System.nanoTime();
            masonFormula.calculateDelta();
            long endTime = System.nanoTime();

            double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;

            // Обновляем метки с результатами
            loopCountLabel.setText("Loop Count: " + masonFormula.getLoopCount());
            executionTimeLabel.setText("Execution Time: " + durationInMilliseconds + " ms");

            logger.info("Loop count calculated: {}", masonFormula.getLoopCount());
            logger.info("Execution time: {} ms", durationInMilliseconds);
        } catch (Exception ex) {
            logger.error("Error calculating loops.", ex);
            JOptionPane.showMessageDialog(this, "An error occurred while calculating loops.");
        }
    }

    /**
     * Инициализирует параметры окна приложения.
     */
    private void windowInit(){
        setTitle("Graph Generator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        logger.debug("Window size set to 800x600 and layout set.");
    }

    /**
     * Инициализирует панель управления с полями ввода параметров графа и кнопками
     */
    private void controlPanelInit(){
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2));

        addLabeledField(controlPanel, "Number of Nodes:", nodeCountField = new JTextField("5"));
        addLabeledField(controlPanel, "Number of Loops:", loopCountField = new JTextField("2"));

        generateButton = new JButton("Generate Graph");
        controlPanel.add(generateButton);

        calculateLoopsButton = new JButton("Calculate Loops");
        controlPanel.add(calculateLoopsButton);

        add(controlPanel, BorderLayout.NORTH);
        logger.debug("Control panel added to window.");
    }

    /**
     * Инициализирует панель результатов (отображение количества петель и время выполнения).
     */
    private void resultPanelInit(){
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(2, 1));
        loopCountLabel = new JLabel("Loop Count: ");
        executionTimeLabel = new JLabel("Execution Time: ");
        resultPanel.add(loopCountLabel);
        resultPanel.add(executionTimeLabel);
        add(resultPanel, BorderLayout.SOUTH);
        logger.debug("Result panel added to window.");
    }

    /**
     * Устанавливает слушатели событий для кнопок
     */
    private void setActionListeners(){
        generateButton.addActionListener(e -> {
            logger.info("Generate Graph button clicked.");
            generateGraph();
        });
        calculateLoopsButton.addActionListener(e -> {
            logger.info("Calculate Loops button clicked.");
            calculateLoops();
        });
    }

    /**
     * Инициализирует панель для отображения графа.
     */
    private void graphPanelInit(){
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);
        logger.debug("Graph panel initialized and added.");
    }

    /**
     * Добавляет метку и текстовое на панель.
     * @param panel панель, на которую добавляется метка и текстовое поле.
     * @param label текст метки.
     * @param field текстовое поле.
     */
    private void addLabeledField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }
}
