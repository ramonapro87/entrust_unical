package edu.boun.edgecloudsim.simulationvisualizer;

import edu.boun.edgecloudsim.core.SimManagerEnergy;
import edu.boun.edgecloudsim.utils.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapCharts extends JPanel {


    private List<Coordinates> objects;

    public MapCharts(List<Coordinates> objects) {
        this.objects = objects;
        setPreferredSize(new Dimension(800, 600)); // Dimensione del pannello
    }



        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            objects.forEach(host -> {
                if (host.isDead()) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLUE);
                }
                g.fillOval(host.getX(), host.getY(), 5, 5); // Disegna un cerchio per ogni host
            });
        }








}

