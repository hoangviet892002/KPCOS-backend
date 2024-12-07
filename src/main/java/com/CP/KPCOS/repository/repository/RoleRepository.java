package com.CP.KPCOS.repository.repository;

import com.CP.KPCOS.entity.RoleEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends R2dbcRepository<RoleEntity, Long> {
    Mono<RoleEntity> findByName(String name);
}
