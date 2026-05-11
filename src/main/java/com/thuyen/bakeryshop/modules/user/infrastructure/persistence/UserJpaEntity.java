package com.thuyen.bakeryshop.modules.user.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserJpaEntity {

    private UUID id;
    private String fullName;
    private String email;
    private String phone;

}
