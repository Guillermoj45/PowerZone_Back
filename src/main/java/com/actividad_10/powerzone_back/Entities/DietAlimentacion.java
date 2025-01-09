package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "diet_alimentacion")
public class DietAlimentacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "diet_id", nullable = false)
    private Long dietId;

    @Id
    @Column(name = "alimentacion_id", nullable = false)
    private Long alimentacionId;

}
