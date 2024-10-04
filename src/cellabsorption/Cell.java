package cellabsorption;

import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;

import java.awt.Color;

public class Cell {
    private Ellipse shape;
    double radius;  // Make this package-private for interaction with other cells
    private double direction;

    public Cell(double x, double y, double radius, Color color) {
        this.radius = radius;
        shape = new Ellipse(x, y, radius * 2, radius * 2);
        shape.setFillColor(color);
        setRandomDirection();
    }

    public Ellipse getShape() {
        return shape;
    }

    public void grow(double amount) {
        setRadius(radius + amount);
    }

    public void shrink() {
        setRadius(radius * 0.9);  // Shrink by 10% when absorbed
    }

    private void setRadius(double newRadius) {
        if (newRadius < 0) {
            newRadius = 0;
        }
        radius = newRadius;
        Point previousCenter = shape.getCenter();
        shape.setSize(newRadius * 2, newRadius * 2);
        shape.setCenter(previousCenter);
    }

    public void moveAround(Point centerOfGravity, double wiggliness, double wanderFromCenter) {
        shape.moveBy(Math.cos(direction), Math.sin(direction));

        double distToCenter = shape.getCenter().distance(centerOfGravity);
        double angleToCenter = centerOfGravity.subtract(shape.getCenter()).angle();
        double turnTowardCenter = normalizeRadians(angleToCenter - direction);

        direction = normalizeRadians(
            direction
                + (Math.random() - 0.5) * wiggliness
                + turnTowardCenter * Math.tanh(distToCenter / wanderFromCenter)
        );
    }

    public void interactWith(Cell otherCell) {
        // Calculate the distance between the centers of the two cells
        double distance = this.getShape().getCenter().distance(otherCell.getShape().getCenter());

        // Check if the cells are colliding (i.e., distance < sum of radii)
        if (distance < this.radius + otherCell.radius) {
            // The larger cell absorbs the smaller one
            if (this.radius > otherCell.radius) {
                this.grow(otherCell.radius * 0.1);  // Grow by 10% of absorbed cell's radius
                otherCell.shrink();                // Shrink the absorbed cell
            } else {
                otherCell.grow(this.radius * 0.1);
                this.shrink();
            }
        }
    }

    private void setRandomDirection() {
        direction = Math.random() * Math.PI * 2;
    }

    private static double normalizeRadians(double theta) {
        double pi2 = Math.PI * 2;
        return ((theta + Math.PI) % pi2 + pi2) % pi2 - Math.PI;
    }
}
