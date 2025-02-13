package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "groupuser")
@AllArgsConstructor
@NoArgsConstructor
public class GroupUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupName group;

    public GroupUser(User user, GroupName group) {
        this.user = user;
        this.group = group;
    }

}
