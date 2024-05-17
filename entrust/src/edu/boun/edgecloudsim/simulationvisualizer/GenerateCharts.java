package edu.boun.edgecloudsim.simulationvisualizer;

import edu.boun.edgecloudsim.utils.Coordinates;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class GenerateCharts implements IDiagrams{

    private List<Coordinates> data;

    @Override
    public void generateMapCharts(List<Coordinates> coordinates) {
        this.data = coordinates;
        generateMapCharts();
    }

    @Override
    public void generateMapCharts() {
        XYSeries aliveSeries = new XYSeries("Alive");
        XYSeries deadSeries = new XYSeries("Dead");

        for (Coordinates coord : data) {
            if (coord.isDead()) {
                deadSeries.add(coord.getX(), coord.getY());
            } else {
                aliveSeries.add(coord.getX(), coord.getY());
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(aliveSeries);
        dataset.addSeries(deadSeries);

        // Create the chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Coordinates Plot",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize the plot
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Set shape and color for 'alive' series
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesPaint(0, Color.BLUE);

        // Set shape and color for 'dead' series
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesPaint(1, Color.RED);

        // Add tooltips
        renderer.setBaseToolTipGenerator((XYDataset dataset1, int series, int item) -> {
            Coordinates coord = data.get(item);
            return "ID: " + coord.getId() + ", Time: " + coord.getTime() + ", IsDead: " + coord.isDead();
        });

        plot.setRenderer(renderer);

        // Display the chart in a frame
        JFrame frame = new JFrame("Coordinates Plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void addDataToMapChart(Coordinates coordinates) {
        if(data == null)
            data = new LinkedList<>();
        if(coordinates.getX() > 14 || coordinates.getY() > 14)
            System.out.println(coordinates.toString());

        this.data.add(coordinates);
    }
}
