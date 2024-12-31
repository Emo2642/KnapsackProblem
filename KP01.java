package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class KP01 {
    private final long capacity;
    private final Set<Item> items;
    private final Random random = new Random();

    public KP01(long capacity, Set<Item> items) {
        this.capacity = capacity;
        this.items = new HashSet<>(items);
    }

    public long getCapacity() {
        return capacity;
    }

    public Set<Item> getItems() {
        return items;
    }

    public boolean isTrivial() {
        long totalWeight = items.stream().mapToLong(Item::weight).sum();
        return totalWeight <= capacity;
    }

    public Solution generateGreedySolution() {
        return KnapsackSolver.greedyAlgorithm(this);
    }

    public Solution generateRandomSolution() {
        return KnapsackSolver.generateRandomSolution(this, random);
    }

    public static KP01 fromFile(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("Empty file!");
            }

            long capacity = 0;
            Set<Item> items = new HashSet<>();
            int lineNumber = 0;

            for (String line : lines) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");

                if (parts.length == 1) {
                    try {
                        capacity = Long.parseLong(parts[0]);
                        continue;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid capacity (Line " + lineNumber + "): " + line);
                        continue;
                    }
                }

                if (parts.length >= 3) {
                    try {
                        items.add(new Item(
                                Long.parseLong(parts[0]),
                                Long.parseLong(parts[2]),
                                Long.parseLong(parts[1])
                        ));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid format (Line " + lineNumber + "): " + line);
                    }
                }
            }

            if (capacity == 0) {
                throw new IllegalArgumentException("Capacity not found in file.");
            }

            return new KP01(capacity, items);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}