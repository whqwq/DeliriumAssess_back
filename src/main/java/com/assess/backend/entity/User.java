package com.assess.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String password;
    private String name;
    private String remark;
    private LocalDate createDate;
    private String hospitalName;
    private Boolean deleted;
    public String toString() {
        return "User(id=" + id + ", phone=" + phone + ", password=" + password + ", name=" + name + ", remark=" + remark + ", createDate=" + createDate + ", hospitalName=" + hospitalName + ")";
    }
}
