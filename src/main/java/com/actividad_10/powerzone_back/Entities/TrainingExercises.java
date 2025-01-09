package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "training_exercises")
public class TrainingExercises implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "training_id", nullable = false)
    private Long trainingId;

    @Id
    @Column(name = "exercises_id", nullable = false)
    private Long exercisesId;

    @Column(name = "muscles", nullable = false)
    private Integer muscles;

}
