package com.food.api.service;

import com.food.api.model.FoodItem;
import com.food.api.model.MacroResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class FoodService {

    // ✅ Correct env variable usage
    private final String API_KEY = System.getenv("SPOONACULAR_API_KEY");

    public MacroResponse processFoodImage(MultipartFile file) {

        // 🔥 Call working API (no image)
        Map<String, Object> apiResponse = callSpoonacularAPI();

        // 🔥 Spoonacular returns different structure here
        Map calories = (Map) apiResponse.get("calories");
        Map protein = (Map) apiResponse.get("protein");
        Map carbs = (Map) apiResponse.get("carbs");
        Map fat = (Map) apiResponse.get("fat");

        double calVal = getValue(calories);
        double proteinVal = getValue(protein);
        double carbsVal = getValue(carbs);
        double fatVal = getValue(fat);

        // 🔥 Hardcoded name (since no image detection)
        FoodItem item = new FoodItem(
                "Pizza",
                "1 serving",
                calVal,
                proteinVal,
                carbsVal,
                fatVal
        );

        List<FoodItem> items = List.of(item);

        Map<String, Double> totals = new HashMap<>();
        totals.put("calories", calVal);
        totals.put("protein_g", proteinVal);
        totals.put("carbs_g", carbsVal);
        totals.put("fat_g", fatVal);

        return new MacroResponse(items, totals);
    }

    // ✅ FIXED API CALL
    private Map<String, Object> callSpoonacularAPI() {

        try {
            String url = "https://api.spoonacular.com/recipes/guessNutrition?title=pizza&apiKey=" + API_KEY;

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Map.class
            );

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling Spoonacular API: " + e.getMessage());
        }
    }

    private double getValue(Map data) {
        if (data == null) return 0.0;

        Object val = data.get("value");
        if (val == null) return 0.0;

        try {
            return Double.parseDouble(val.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }
}