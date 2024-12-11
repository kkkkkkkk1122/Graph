package org.example;

/**
* Главный класс приложения расчета времени вычисления количества замкнутых петель направленного графа.
*/
public class Main {
    /**
     * Точка входа для запуска приложения.
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GraphApp app = new GraphApp();
            app.setVisible(true);
        });
    }
}
