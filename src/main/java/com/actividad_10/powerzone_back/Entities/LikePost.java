package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "like_post")
@IdClass(LikePost.class)
public class LikePost implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "created_at_post", nullable = false)
    private LocalDate createdAtPost;

}
