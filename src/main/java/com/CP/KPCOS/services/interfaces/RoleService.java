package com.CP.KPCOS.services.interfaces;

import com.CP.KPCOS.entity.RoleEntity;
import reactor.core.publisher.Mono;


public interface RoleService {
    Mono<RoleEntity> getRoleByName(String roleName);

    Mono<RoleEntity> getCustomerRole();
}
