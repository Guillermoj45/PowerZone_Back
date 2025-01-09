package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "alimentacion_ingredients")
public class AlimentacionIngredients implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "alimentacion_id", nullable = false)
    private Long alimentacionId;

    @Id
    @Column(name = "ingredients_id", nullable = false)
    private Long ingredientsId;

}
