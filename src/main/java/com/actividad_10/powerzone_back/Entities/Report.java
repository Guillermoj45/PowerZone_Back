package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reports")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private ReportState type;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @JoinColumn(name = "reporter", nullable = false)
    @ManyToOne
    private User reporter;

    @JoinColumn(name = "post_id")
    @ManyToOne
    private Post post;

    @Column(name = "created_at_post", nullable = false)
    private LocalDateTime createdAtPost;

}
