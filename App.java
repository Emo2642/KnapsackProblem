package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    private static final String DEFAULT_PATH = "C:\\Users\\Emre\\IdeaProjects\\Odev\\CP2\\knapsackProblemInstances-master";

    public static void main(String[] args) {
        System.out.println("Knapsack Problem Solver");

        String directoryPath = args.length > 0 ? args[0] : DEFAULT_PATH;
        List<String> testFiles = getTestFilePaths(directoryPath);

        if (testFiles.isEmpty()) {
            System.out.println("No test files found in: " + directoryPath);
            return;
        }

        // Select and process random test file
        Random random = new Random();
        String randomFile = testFiles.get(random.nextInt(testFiles.size()));
        System.out.println("\nSelected test file: " + randomFile);

        try {
            processTestFile(randomFile);
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    private static void processTestFile(String filePath) {
        KP01 problem = KP01.fromFile(Paths.get(filePath));
        System.out.println("\nProblem loaded: " + problem.getItems().size() + " items, capacity: " + problem.getCapacity());

        if (problem.isTrivial()) {
            System.out.println("Trivial problem - all items fit in knapsack");
            return;
        }

        // Generate and evaluate greedy solution
        Solution greedySolution = KnapsackSolver.greedyAlgorithm(problem);
        System.out.println("\nGreedy Solution:");
        System.out.println("Total Profit: " + greedySolution.totalProfit());
        System.out.println("Total Weight: " + greedySolution.totalWeight());
        System.out.println("Items selected: " + greedySolution.getItems().size());

        // Generate and evaluate random solutions using parallel processing
        int numRandomSolutions = 1_000_000;
        AtomicInteger betterCount = new AtomicInteger();
        AtomicLong maxProfit = new AtomicLong(Long.MIN_VALUE);
        AtomicLong minProfit = new AtomicLong(Long.MAX_VALUE);
        AtomicLong totalProfit = new AtomicLong();

        IntStream.range(0, numRandomSolutions).parallel().forEach(i -> {
            Solution randomSolution = KnapsackSolver.generateRandomSolution(problem, new Random());
            long profit = randomSolution.totalProfit();

            totalProfit.addAndGet(profit);
            maxProfit.accumulateAndGet(profit, Math::max);
            minProfit.accumulateAndGet(profit, Math::min);

            if (profit > greedySolution.totalProfit()) {
                betterCount.incrementAndGet();
            }
        });

        // Calculate and display statistics
        double avgProfit = (double) totalProfit.get() / numRandomSolutions;
        System.out.println("\nRandom Solutions Analysis:");
        System.out.printf("Minimum Profit: %d%n", minProfit.get());
        System.out.printf("Average Profit: %.2f%n", avgProfit);
        System.out.printf("Maximum Profit: %d%n", maxProfit.get());
        System.out.printf("Solutions better than greedy: %d/%d (%.2f%%)%n",
                betterCount.get(), numRandomSolutions,
                (betterCount.get() * 100.0) / numRandomSolutions);
    }

    private static List<String> getTestFilePaths(String directoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".in"))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}