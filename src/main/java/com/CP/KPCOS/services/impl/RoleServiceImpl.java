package com.CP.KPCOS.services.impl;


import com.CP.KPCOS.entity.RoleEntity;
import com.CP.KPCOS.repository.RoleRepository;
import com.CP.KPCOS.services.interfaces.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;

    @Override
    public Mono<RoleEntity> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Mono<RoleEntity> getCustomerRole() {
        return roleRepository.findByName("Customer");
    }
}
