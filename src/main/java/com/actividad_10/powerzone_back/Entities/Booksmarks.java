package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.Ids.LikeId;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booksmarks")
@IdClass(LikeId.class)
public class Booksmarks implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "created_at_post", nullable = false)
    private LocalDateTime createdAtPost;


}
