package com.yourhealth.calorieCalculator.entities;

import javax.persistence.*;

@Entity
@Table(name = "food", schema = "public")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "food")
    private String food;

    @Column(name = "kcal")
    private Integer kcal;

    @Column(name = "fat")
    private Integer fat;

    @Column(name = "protein")
    private Integer protein;

    @Column(name = "carbs")
    private Integer carbs;

    public Integer getKcal() {
        return kcal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Food(long id, String food, Integer kcal, Integer fat, Integer protein, Integer carbs) {
        this.id = id;
        this.food = food;
        this.kcal = kcal;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
    }

    public Food() {
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
