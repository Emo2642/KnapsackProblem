package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KnapsackSolver {

    public static Solution greedyAlgorithm(KP01 problem) {
        List<Item> sortedItems = new ArrayList<>(problem.getItems());
        sortedItems.sort((a, b) -> Double.compare(
                (double) b.profit() / b.weight(),
                (double) a.profit() / a.weight()
        ));

        List<Item> selectedItems = new ArrayList<>();
        long totalWeight = 0;
        long totalProfit = 0;

        for (Item item : sortedItems) {
            if (totalWeight + item.weight() <= problem.getCapacity()) {
                selectedItems.add(item);
                totalWeight += item.weight();
                totalProfit += item.profit();
            }
        }

        return new Solution(selectedItems, totalProfit, totalWeight);
    }

    public static Solution generateRandomSolution(KP01 problem, Random random) {
        List<Item> itemList = new ArrayList<>(problem.getItems());
        List<Item> selectedItems = new ArrayList<>();
        long totalWeight = 0;
        long totalProfit = 0;

        while (!itemList.isEmpty()) {
            Item item = itemList.remove(random.nextInt(itemList.size()));
            if (totalWeight + item.weight() <= problem.getCapacity()) {
                selectedItems.add(item);
                totalWeight += item.weight();
                totalProfit += item.profit();
            }
        }

        return new Solution(selectedItems, totalProfit, totalWeight);
    }
}