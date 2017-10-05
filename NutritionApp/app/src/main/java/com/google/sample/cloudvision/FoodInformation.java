package com.google.sample.cloudvision;

/**
 * Created by Mikey Stewart on 6/10/2017.
 */

public class FoodInformation {

    public String calories;
    public String cholesterol;
    public String potassium;
    public String protein;
    public String sodium;
    public String carbohydrates;
    public String fat;

    public FoodInformation() {
    }

    public FoodInformation(String calories, String cholesterol, String potassium, String protein, String sodium, String carbohydrates, String fat) {
        this.calories = calories;
        this.cholesterol = cholesterol;
        this.potassium = potassium;
        this.protein = protein;
        this.sodium = sodium;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getPotassium() {
        return potassium;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getCarbohydratee() {
        return carbohydrates;
    }

    public void setCarbohydratee(String carbohydratee) {
        this.carbohydrates = carbohydratee;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }
}
