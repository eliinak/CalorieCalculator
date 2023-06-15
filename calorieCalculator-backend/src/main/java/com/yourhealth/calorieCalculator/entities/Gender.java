package com.yourhealth.calorieCalculator.entities;

import javax.persistence.*;

@Entity
@Table(name = "gender", schema = "public")
public class Gender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "gender_type")
    private String genderType;

    public Gender(long id, String genderType) {
        this.id = id;
        this.genderType = genderType;
    }

    public Gender() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }
}
