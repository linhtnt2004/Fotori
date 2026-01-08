package com.example.plusfy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    Long id;
    String name;
    String description;
    BigDecimal price;
    String size;
    String imageUrl;
}
