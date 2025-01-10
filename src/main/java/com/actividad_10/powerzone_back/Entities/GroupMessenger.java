package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.Ids.PostId;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "groupmessenger")
@IdClass(PostId.class)
public class GroupMessenger implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Id
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "grupouser", nullable = false)
    private Long grupouser;

}
