package com.yourhealth.calorieCalculator.entities;

import javax.persistence.*;

@Entity
@Table(name = "lifestyle", schema = "public")
public class Lifestyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Lifestyle(long id, String lifestyleType) {
        this.id = id;
        this.lifestyleType = lifestyleType;
    }

    public Lifestyle() {

    }

    @Column(name = "lifestyle_type")
    private String lifestyleType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLifestyle() {
        return lifestyleType;
    }

    public void setLifestyle(String lifestyleType) {
        this.lifestyleType = lifestyleType;
    }
}
