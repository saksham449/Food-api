package com.food.api.model;

import java.util.List;
import java.util.Map;

public class MacroResponse {

    private List<FoodItem> items;
    private Map<String, Double> total_macros;

    public MacroResponse(List<FoodItem> items, Map<String, Double> total_macros) {
        this.items = items;
        this.total_macros = total_macros;
    }

    public List<FoodItem> getItems() { return items; }
    public Map<String, Double> getTotal_macros() { return total_macros; }
}