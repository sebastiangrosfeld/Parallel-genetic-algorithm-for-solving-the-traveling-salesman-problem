package pl.edu.pw.ee;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;

import static pl.edu.pw.ee.FileHandler.readNodesFromFile;
import static pl.edu.pw.ee.FileHandler.writeResultNodesToFile;

public class Main extends Application {

    private static final String WINDOW_TITLE = "Genetic Algorithm";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_PADDING = 50;
    private static final String OUTPUT_FILENAME = "result.txt";
    private static final double NODE_DIAMETER = 10;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final var geneticAlgorithm = prepareGeneticAlgorithm();
        final var nodes = readNodesFromFile("coordinates.txt");

        final var startTime = System.currentTimeMillis();
        final var result = geneticAlgorithm.solve(nodes);
        final var endTime = System.currentTimeMillis();

        drawGraph(stage, result);
        System.out.println("Time taken: " + ((endTime - startTime)) + " milliseconds");
        writeResultNodesToFile(result, OUTPUT_FILENAME);
        printResultNodes(result);
    }

    private TSPSolver prepareGeneticAlgorithm() {
        final var random = new Random();
        final var fitnessFunction = new DistanceFitnessFunction();
        final var selection = new RouletteSelection(random, fitnessFunction);
        final var crossover = new SimpleCrossover(random);
        final var mutation = new SwapNodesMutation(random);

        final var parameters = new ParallelGeneticAlgorithmParameters(500, 1000, 0.7, 0.7, 0.05, 5, 50, 0.2);
        return new ParallelGeneticAlgorithm(random, fitnessFunction, selection, crossover, mutation, parameters);
    }

    private Canvas prepareCanvas(Stage stage) {
        final var root = new Group();
        final var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(WINDOW_TITLE);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.show();

        final var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        final var graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        return canvas;
    }

    private void drawGraph(Stage stage, List<Node> nodes) {
        final var canvas = prepareCanvas(stage);
        final var boundaries = new Boundaries(nodes);
        drawEdges(canvas, nodes, boundaries);
        drawNodes(canvas, nodes, boundaries);
    }

    private void drawEdges(Canvas canvas, List<Node> nodes, Boundaries boundaries) {
        for (int i = 0; i < nodes.size() - 1; i++) {
            final var firstNode = nodes.get(i);
            final var secondNode = nodes.get(i + 1);
            drawEdge(canvas, firstNode, secondNode, boundaries);
        }

        final var node1 = nodes.getLast();
        final var node2 = nodes.getFirst();
        drawEdge(canvas, node1, node2, boundaries);
    }

    private void drawEdge(Canvas canvas, Node firstNode, Node secondNode, Boundaries boundaries) {
        final var graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.RED);

        final var x1 = WINDOW_PADDING + ((firstNode.getXCoordinate() - boundaries.getMinX()) * (WINDOW_WIDTH - WINDOW_PADDING * 2) / (boundaries.getMaxX() - boundaries.getMinX()));
        final var y1 = WINDOW_PADDING + ((firstNode.getYCoordinate() - boundaries.getMinY()) * (WINDOW_HEIGHT - WINDOW_PADDING * 2) / (boundaries.getMaxY() - boundaries.getMinY()));
        final var x2 = WINDOW_PADDING + ((secondNode.getXCoordinate() - boundaries.getMinX()) * (WINDOW_WIDTH - WINDOW_PADDING * 2) / (boundaries.getMaxX() - boundaries.getMinX()));
        final var y2 = WINDOW_PADDING + ((secondNode.getYCoordinate() - boundaries.getMinY()) * (WINDOW_HEIGHT - WINDOW_PADDING * 2) / (boundaries.getMaxY() - boundaries.getMinY()));
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    private void drawNodes(Canvas canvas, List<Node> nodes, Boundaries boundaries) {
        for (final var node : nodes) {
            drawNode(canvas, node, boundaries);
        }
    }

    private void drawNode(Canvas canvas, Node node, Boundaries boundaries) {
        final var graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);

        final var x = WINDOW_PADDING + ((node.getXCoordinate() - boundaries.getMinX()) * (WINDOW_WIDTH - WINDOW_PADDING * 2) / (boundaries.getMaxX() - boundaries.getMinX()));
        final var y = WINDOW_PADDING + ((node.getYCoordinate() - boundaries.getMinY()) * (WINDOW_HEIGHT - WINDOW_PADDING * 2) / (boundaries.getMaxY() - boundaries.getMinY()));
        final var nodeRadius = NODE_DIAMETER / 2;
        graphicsContext.fillOval(x - nodeRadius, y - nodeRadius, NODE_DIAMETER, NODE_DIAMETER);
    }

    private void printResultNodes(List<Node> nodes) {
        for (final var node : nodes) {
            System.out.println(node);
        }

        System.out.println();
    }

    private static class Boundaries {

        private final double minX;
        private final double minY;
        private final double maxX;
        private final double maxY;

        public Boundaries(List<Node> nodes) {
            minX = nodes.stream().map(Node::getXCoordinate).min(Double::compare).orElse(0.0);
            minY = nodes.stream().map(Node::getYCoordinate).min(Double::compare).orElse(0.0);
            maxX = nodes.stream().map(Node::getXCoordinate).max(Double::compare).orElse(0.0);
            maxY = nodes.stream().map(Node::getYCoordinate).max(Double::compare).orElse(0.0);
        }

        public double getMinX() {
            return minX;
        }

        public double getMinY() {
            return minY;
        }

        public double getMaxX() {
            return maxX;
        }

        public double getMaxY() {
            return maxY;
        }

    }

}
