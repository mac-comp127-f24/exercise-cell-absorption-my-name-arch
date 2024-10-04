package cellabsorption;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CellSimulation {
    private static final double
        WIGGLINESS = 0.2,
        WANDER_FROM_CENTER = 60000;

    private CanvasWindow canvas;
    private Random rand = new Random();
    private List<Cell> cells;

    public static void main(String[] args) {
        new CellSimulation();
    }

    public CellSimulation() {
        canvas = new CanvasWindow("Cell Absorption", 800, 800);
        populateCells();

        //noinspection InfiniteLoopStatement
        while (true) {
            Point canvasCenter = new Point(canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);

            // Move and grow all cells
            for (Cell cell : cells) {
                cell.moveAround(canvasCenter, WIGGLINESS, WANDER_FROM_CENTER);
                cell.grow(0.02);
            }

            // Handle interactions between cells
            handleCellInteraction();

            canvas.draw();
            canvas.pause(10);
        }
    }

    private void populateCells() {
        cells = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            double size = rand.nextInt(5) + 2;
            Cell cell = new Cell(
                rand.nextDouble() * (canvas.getWidth() - size),
                rand.nextDouble() * (canvas.getHeight() - size),
                size,
                Color.getHSBColor(rand.nextFloat(), rand.nextFloat() * 0.5f + 0.1f, 1));
            
            cells.add(cell);             // Add cell to list
            canvas.add(cell.getShape());  // Add cell's shape to canvas
        }
    }

    private void handleCellInteraction() {
        for (int i = 0; i < cells.size(); i++) {
            Cell cell0 = cells.get(i);
            for (int j = i + 1; j < cells.size(); j++) {
                Cell cell1 = cells.get(j);
                // Call the interaction method between the two cells
                cell0.interactWith(cell1);
            }
        }
    }
}
