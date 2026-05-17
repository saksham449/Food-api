package com.food.api.service;

import com.food.api.model.FoodItem;
import com.food.api.model.MacroResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class FoodService {

    private final String API_KEY = "999e24966bae45f2af7bb2deed98c1f6";

    public MacroResponse processFoodImage(MultipartFile file) {

        Map<String, Object> apiResponse = callSpoonacularAPI(file);

        // Extract food name
        String foodName = "Unknown Food";
        Map category = (Map) apiResponse.get("category");

        if (category != null && category.get("name") != null) {
            foodName = category.get("name").toString();
        }

        //  Extract nutrition safely
        Map nutrition = (Map) apiResponse.get("nutrition");

        Map calories = nutrition != null ? (Map) nutrition.get("calories") : null;
        Map protein = nutrition != null ? (Map) nutrition.get("protein") : null;
        Map carbs = nutrition != null ? (Map) nutrition.get("carbs") : null;
        Map fat = nutrition != null ? (Map) nutrition.get("fat") : null;

        double calVal = getValue(calories);
        double proteinVal = getValue(protein);
        double carbsVal = getValue(carbs);
        double fatVal = getValue(fat);

        //  Create item
        FoodItem item = new FoodItem(
                foodName,
                "1 serving",
                calVal,
                proteinVal,
                carbsVal,
                fatVal
        );

        List<FoodItem> items = List.of(item);

        //  Total macros
        Map<String, Double> totals = new HashMap<>();
        totals.put("calories", calVal);
        totals.put("protein_g", proteinVal);
        totals.put("carbs_g", carbsVal);
        totals.put("fat_g", fatVal);

        return new MacroResponse(items, totals);
    }

    //  API CALL
    private Map<String, Object> callSpoonacularAPI(MultipartFile file) {

        try {
            String url = "https://api.spoonacular.com/food/images/analyze?apiKey=" + API_KEY;

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling Spoonacular API");
        }
    }

    //  SAFE VALUE EXTRACTOR
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