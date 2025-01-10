package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.Ids.PostId;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "post")
@IdClass(PostId.class)
public class Post implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Id
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "delete")
    private Boolean delete;

}
