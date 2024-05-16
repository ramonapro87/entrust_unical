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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna un cerchio per ogni oggetto
        for (Coordinates obj : objects) {
            int circleX = obj.getX() - 5; // Calcola la coordinata X per il cerchio
            int circleY = obj.getY() - 5; // Calcola la coordinata Y per il cerchio
            Color color = obj.isDead() ? Color.RED : Color.BLUE;

            g.setColor(color);
            g.fillOval(circleX, circleY, 10, 10); // Disegna il cerchio
        }
    }
}
