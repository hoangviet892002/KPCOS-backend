package com.CP.KPCOS.controllers;

import com.CP.KPCOS.dtos.request.LoginRequestApi;
import com.CP.KPCOS.dtos.request.RegisterRequestApi;
import com.CP.KPCOS.dtos.response.BaseResponse;
import com.CP.KPCOS.dtos.response.object.LoginResponseApi;
import com.CP.KPCOS.dtos.response.object.RegisterResponseApi;
import com.CP.KPCOS.services.interfaces.AuthService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@RestController
@Tag(name = "Auth Controller", description = "Auth Controller")
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public Mono<BaseResponse<RegisterResponseApi>> register(@RequestBody RegisterRequestApi registerRequestApi) throws ParseException, JOSEException {
        return authService.register(registerRequestApi.getUsername(), registerRequestApi.getEmail(), registerRequestApi.getPassword())
                .map(registerResponseApi -> BaseResponse.<RegisterResponseApi>builder().data(registerResponseApi).build());
    }

    @PostMapping("/login")
    public Mono<BaseResponse<LoginResponseApi>> login(@RequestBody LoginRequestApi loginRequestApi) throws ParseException, JOSEException {
        LoginResponseApi loginResponseApi = authService.login(loginRequestApi.getUsername(), loginRequestApi.getPassword()).block();
        return Mono.just(BaseResponse.<LoginResponseApi>builder().data(loginResponseApi).build());
    }
}
