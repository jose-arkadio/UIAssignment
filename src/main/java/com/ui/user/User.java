package com.ui.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("users")
class User {

    @Id
    private String acct;

    @Column("fullname")
    private String fullName;

    @Column("pwd")
    private String password;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    BasicUserDto toBasicUserDto() {
        BasicUserDto userDto = new BasicUserDto();
        userDto.setAcct(this.acct);
        userDto.setPassword(this.password);
        userDto.setFullName(this.fullName);
        userDto.setCreatedAt(this.createdAt);
        userDto.setUpdatedAt(this.updatedAt);
        return userDto;
    }
}
