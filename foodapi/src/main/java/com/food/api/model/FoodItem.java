package com.food.api.model;

public class FoodItem {

    private String name;
    private String quantity;
    private double calories;
    private double protein_g;
    private double carbs_g;
    private double fat_g;

    public FoodItem(String name, String quantity, double calories, double protein_g, double carbs_g, double fat_g) {
        this.name = name;
        this.quantity = quantity;
        this.calories = calories;
        this.protein_g = protein_g;
        this.carbs_g = carbs_g;
        this.fat_g = fat_g;
    }

    public String getName() { return name; }
    public String getQuantity() { return quantity; }
    public double getCalories() { return calories; }
    public double getProtein_g() { return protein_g; }
    public double getCarbs_g() { return carbs_g; }
    public double getFat_g() { return fat_g; }
}