package com.CP.KPCOS.repository.unitofwork;

import com.CP.KPCOS.repository.repository.RoleRepository;
import com.CP.KPCOS.repository.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public interface IUnitOfWork {
    RoleRepository roleRepository();

    UserRepository userRepository();
}
