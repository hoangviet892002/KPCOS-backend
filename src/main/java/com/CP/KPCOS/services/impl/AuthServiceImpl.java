package com.CP.KPCOS.services.impl;


import com.CP.KPCOS.dtos.response.object.LoginResponseApi;
import com.CP.KPCOS.dtos.response.object.RegisterResponseApi;
import com.CP.KPCOS.entity.UserEntity;
import com.CP.KPCOS.exceptions.AppException;
import com.CP.KPCOS.repository.UserRepository;
import com.CP.KPCOS.services.interfaces.AuthService;
import com.CP.KPCOS.services.interfaces.RoleService;
import com.CP.KPCOS.shared.enums.ResponseEnum;
import com.CP.KPCOS.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    RoleService roleService;

    @Override
    public Mono<LoginResponseApi> login(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(userEntity -> {
                    if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                        return Mono.error(new AppException(ResponseEnum.WRONG_PASSWORD_OR_USERNAME));
                    }
                    String token = jwtUtils.generateToken(userEntity);
                    return Mono.just(new LoginResponseApi(token));
                })
                .switchIfEmpty(Mono.error(new AppException(ResponseEnum.WRONG_PASSWORD_OR_USERNAME)));
    }

    @Override
    public Mono<RegisterResponseApi> register(String username, String email, String password) {
        return userRepository.findByUsername(username)
                .flatMap(existingUser -> Mono.<RegisterResponseApi>error(new AppException(ResponseEnum.USER_ALREADY_EXISTS)))
                .switchIfEmpty(userRepository.findByEmail(email)
                        .flatMap(existingEmail -> Mono.<RegisterResponseApi>error(new AppException(ResponseEnum.EMAIL_ALREADY_EXISTS)))
                        .switchIfEmpty(roleService.getCustomerRole()
                                .flatMap(customerRole -> {
                                    UserEntity userEntity = new UserEntity(username, passwordEncoder.encode(password), email, customerRole);
                                    return userRepository.save(userEntity)
                                            .map(savedUser -> {
                                                RegisterResponseApi registerResponseApi = new RegisterResponseApi();
                                                registerResponseApi.setUsername(savedUser.getUsername());
                                                registerResponseApi.setEmail(savedUser.getEmail());
                                                registerResponseApi.setRole(savedUser.getRole().getName());
                                                return registerResponseApi;
                                            });
                                })
                        )
                );
    }
}
