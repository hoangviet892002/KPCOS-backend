package com.CP.KPCOS.repository.unitofwork;

import com.CP.KPCOS.repository.repository.RoleRepository;
import com.CP.KPCOS.repository.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UnitOfWork implements IUnitOfWork {
    RoleRepository roleRepository;
    UserRepository userRepository;

    @Override
    public RoleRepository roleRepository() {
        return roleRepository;
    }

    @Override
    public UserRepository userRepository() {
        return userRepository;
    }
}
