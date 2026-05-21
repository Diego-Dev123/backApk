package com.appdiev.ApiRest.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(length = 255, nullable = true)
    private String image;


    @Column(name="created_at",nullable = false, updatable = false)
    private LocalDateTime createdAT = LocalDateTime.now();

    @Column(name="updated_at",nullable = false)
    private LocalDateTime updateAT = LocalDateTime.now();



    @PreUpdate
    public void onUpdate(){
        this.updateAT = LocalDateTime.now();
    }


}
