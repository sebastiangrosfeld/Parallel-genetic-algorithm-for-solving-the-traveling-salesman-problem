package pl.edu.pw.ee;

import java.util.Objects;

public class Node {

    private final double xCoordinate;
    private final double yCoordinate;

    public Node(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public double calculateDistance(Node other) {
        final var xDifference = xCoordinate - other.xCoordinate;
        final var yDifference = yCoordinate - other.yCoordinate;
        return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Node node)) {
            return false;
        }

        return Double.compare(xCoordinate, node.xCoordinate) == 0 && Double.compare(yCoordinate, node.yCoordinate) == 0;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", xCoordinate, yCoordinate);
    }

}
