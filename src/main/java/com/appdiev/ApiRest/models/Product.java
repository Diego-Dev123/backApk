package com.appdiev.ApiRest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255, nullable = true)
    private String image1;

    @Column(length = 255, nullable = true)
    private String image2;

    @Column(nullable = false)
    private Double price;

    @Column(name="created_at",nullable = false, updatable = false)
    private LocalDateTime createdAT = LocalDateTime.now();

    @Column(name="updated_at",nullable = false)
    private LocalDateTime updateAT = LocalDateTime.now();



    @PreUpdate
    public void onUpdate(){
        this.updateAT = LocalDateTime.now();
    }


}
