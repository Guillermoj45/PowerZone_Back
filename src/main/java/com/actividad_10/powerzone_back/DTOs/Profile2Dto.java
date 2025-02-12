package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Profile2Dto {
    private Long id;
    private String name;
    private String email;
    private LocalDate bornDate;
    private String nickName;
    private String avatar;
    private int followers;
    private int following;

    public Profile2Dto(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.email = profile.getUser().getEmail();
        this.bornDate = profile.getBornDate();
        this.nickName = profile.getNickname();
        this.avatar = profile.getAvatar();
    }
}
