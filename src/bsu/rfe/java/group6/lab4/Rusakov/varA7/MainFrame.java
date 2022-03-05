package bsu.rfe.java.group6.lab4.Rusakov.varA7;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;
    private JMenuItem showTurnLeftMenuItem;
    private JMenuItem showTurnRightMenuItem;

    public  MainFrame(){
        super("Построение графиков функции на основе заранее подготовленных файлов");
        setSize(WIDTH,HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit(); // отцентровка
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setDialogTitle("Our Directory");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if(fileChooser.showOpenDialog(MainFrame.this) /*Функция открытия диалогового окна «Открыть файл*/ == JFileChooser.APPROVE_OPTION){
                    openGraphics(fileChooser.getSelectedFile());


                }

            }
        };
        fileMenu.add(openGraphicsAction);

        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            @Override
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showMarkerAction = new AbstractAction("Показывать маркеры точек") {
            @Override
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkerAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);











    }
     public static void main(String args[]){
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


     }
    protected void openGraphics (File selectedFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            ArrayList<String> strings = new ArrayList<String>();

            while (reader.ready()) {
                strings.add(reader.readLine());
            }

            Double[][] graphicsData = new Double[strings.size()][2];
            boolean[] pointsCon = new boolean[graphicsData.length];

            for (int i = 0; i < strings.size(); i++) {
                String[] str = strings.get(i).split(" ");
                for (int j = 0; j < str.length; j++) {
                    String s = str[j];
                    if (j == 1) {
                        str[j] = str[j].replace(".", "");
                        boolean f = true;
                        for (int h = 0; h < str[j].length() - 1; h++) {
                            if (str[j].charAt(h) > str[j].charAt(h + 1)) {
                                f = false;
                                break;
                            }
                        }
                        pointsCon[i] = f;
                    }
                    graphicsData[i][j] = Double.valueOf(s);
                }
            }

            if (graphicsData != null && graphicsData.length > 0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
                showTurnLeftMenuItem.setEnabled(true);
                showTurnRightMenuItem.setEnabled(true);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтеничя координа точек из файла",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }

        public void menuCanceled(MenuEvent e) {
        }
    }
}



