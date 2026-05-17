package com.food.api.controller;

import com.food.api.model.MacroResponse;
import com.food.api.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping("/analyze")
    public MacroResponse analyzeFood(@RequestParam("image") MultipartFile file) {
        return foodService.processFoodImage(file);
    }
}