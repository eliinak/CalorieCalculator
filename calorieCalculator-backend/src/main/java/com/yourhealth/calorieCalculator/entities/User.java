package com.yourhealth.calorieCalculator.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private Integer age;

    @ManyToOne
    @JoinColumn(name="gender_id", nullable=false)
    private Gender gender;

    @Column(name = "kilograms")
    private Integer kilograms;

    @Column(name = "height")
    private Integer height;

    @ManyToOne
    @JoinColumn(name="lifestyle_id", nullable=false)
    private Lifestyle lifestyle;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "need_kcal")
    private Integer needKcal;

    public Integer getNeedKcal() {
        return needKcal;
    }

    public void setNeedKcal(Integer needKcal) {
        this.needKcal = needKcal;
    }

    public User(long id, String name, String surname, Integer age, Gender gender, Integer kilograms, Integer height, Lifestyle lifestyle, String username, String email, String password, Integer needKcal, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.kilograms = kilograms;
        this.height = height;
        this.lifestyle = lifestyle;
        this.username = username;
        this.email = email;
        this.password = password;
        this.needKcal = needKcal;
        this.roles = roles;
    }

    public User(long id, String name, String surname, Integer age, Gender gender, Integer kilograms, Integer height, Lifestyle lifestyle, String username, String email, String password, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.kilograms = kilograms;
        this.height = height;
        this.lifestyle = lifestyle;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(long id, String name, String surname, Integer age, Gender gender, Integer kilograms, Integer height, Lifestyle lifestyle, String username, String password, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.kilograms = kilograms;
        this.height = height;
        this.lifestyle = lifestyle;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getKilograms() {
        return kilograms;
    }

    public void setKilograms(Integer kilograms) {
        this.kilograms = kilograms;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Lifestyle getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(Lifestyle lifestyle) {
        this.lifestyle = lifestyle;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
