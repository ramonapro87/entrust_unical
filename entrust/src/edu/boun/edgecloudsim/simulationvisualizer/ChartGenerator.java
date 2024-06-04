package edu.boun.edgecloudsim.simulationvisualizer;

import edu.boun.edgecloudsim.utils.Coordinates;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChartGenerator implements IDiagrams {



    public void generateDiagram(Map<Integer, List <Coordinates> >coordinatesById, String scenarioName, String orchestretorPolicy, DiagramType diagramType) {
        try {
            // Crea un dataset
            XYSeriesCollection dataset = new XYSeriesCollection();

            // Crea una serie di dati per ciascun id
            for (Map.Entry<Integer, List<Coordinates>> entry : coordinatesById.entrySet()) {
                XYSeries series = new XYSeries("Host" + entry.getKey());
                for (Coordinates coord : entry.getValue()) {
                    if(diagramType == DiagramType.ENERGY_VS_TIME) {
                        series.add(coord.getTime(), coord.getEnergyConsumed());


                    }

                    else if (diagramType == DiagramType.MAPCHART_LOCALIZATION){
                        double x= (double) (coord.getX()+Math.random() * 0.5);
                        double y=(double)(coord.getY()+Math.random()*0.5);
                        series.add(x,y);
                    }
                }
                dataset.addSeries(series);
            }

            // Crea il grafico
            JFreeChart chart = ChartFactory.createScatterPlot(diagramType.name(), "Time", "Energy Consumed", dataset, PlotOrientation.VERTICAL, true, true, false);

            // Mostra il grafico in una finestra
            SwingUtilities.invokeLater(() -> {
                String title = scenarioName + " - " + orchestretorPolicy;
                JFrame frame = new JFrame(title);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new ChartPanel(chart));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });

            String folder = "sim_results/diagram_result";
            // Save chart as an image
            saveChartAsImage(chart,folder, 800,600);

            System.out.println("Grafico generato con successo.");
        } catch (Exception e) {
            System.out.println("Si è verificato un errore:");
            e.printStackTrace();
        }


    }

    private void saveChartAsImage(JFreeChart chart, String filePath, int width, int height) {
        String uuid = UUID.randomUUID().toString();
        String fileName = filePath + "/FIle_" + uuid + ".png";

        File outputFile = new File(fileName);
        outputFile.getParentFile().mkdirs(); // Create directories if they do not exist
        BufferedImage chartImage = chart.createBufferedImage(width, height);
        try {
            ImageIO.write(chartImage, "png", outputFile);
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }

    @Override
    public void generateEnergyCharts(Map<Integer, List<Coordinates>> coordinatesById, String scenarioName, String orchestretorPolicy) {
        generateDiagram(coordinatesById, scenarioName, orchestretorPolicy, DiagramType.ENERGY_VS_TIME);
    }

    @Override
    public void generateMapChart(Map<Integer, List<Coordinates>> coordinatesById, String scenarioName, String orchestretorPolicy) {
        generateDiagram(coordinatesById, scenarioName, orchestretorPolicy, DiagramType.MAPCHART_LOCALIZATION);
    }


}
