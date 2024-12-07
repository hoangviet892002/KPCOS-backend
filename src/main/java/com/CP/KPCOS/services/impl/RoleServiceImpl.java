package com.CP.KPCOS.services.impl;


import com.CP.KPCOS.entity.RoleEntity;
import com.CP.KPCOS.repository.unitofwork.IUnitOfWork;
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
    IUnitOfWork unitOfWork;

    @Override
    public Mono<RoleEntity> getRoleByName(String roleName) {
        return unitOfWork.roleRepository().findByName(roleName);
    }

    @Override
    public Mono<RoleEntity> getCustomerRole() {
        return unitOfWork.roleRepository().findByName("Customer");
    }
}
