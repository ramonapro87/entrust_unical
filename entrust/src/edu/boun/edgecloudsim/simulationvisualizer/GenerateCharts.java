package edu.boun.edgecloudsim.simulationvisualizer;

import edu.boun.edgecloudsim.utils.Coordinates;

import javax.swing.*;
import java.util.List;

public class GenerateCharts implements IDiagrams{

    @Override
    public void generateMapCharts(List<Coordinates> coordinates) {
        JFrame frame = new JFrame("Host Position Chart,red:died host");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MapCharts(coordinates));
        frame.pack();
        frame.setVisible(true);
    }
}
