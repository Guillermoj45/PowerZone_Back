package com.actividad_10.powerzone_back.Entities.Ids;

import java.util.Objects;

public class GroupUserId {
    // Getters y setters
    private Long groupId;
    private Long userId;

    // Constructor por defecto
    public GroupUserId() {}

    // Constructor con todos los atributos
    public GroupUserId(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }


    // Sobrescribe equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupUserId groupUserId = (GroupUserId) o;
        return Objects.equals(groupId, groupUserId.groupId) && Objects.equals(userId, groupUserId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
