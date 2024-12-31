package org.example;

import java.util.List;

public class Solution {
    private final List<Item> items;
    private final long totalProfit;
    private final long totalWeight;

    public Solution(List<Item> items, long totalProfit, long totalWeight) {
        this.items = items;
        this.totalProfit = totalProfit;
        this.totalWeight = totalWeight;
    }

    public List<Item> getItems() {
        return items;
    }

    public long totalProfit() {  // Changed from totalProfit()
        return totalProfit;
    }

    public long totalWeight() {  // Changed from totalWeight()
        return totalWeight;
    }
}