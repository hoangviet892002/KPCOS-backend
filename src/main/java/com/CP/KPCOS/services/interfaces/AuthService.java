package com.CP.KPCOS.services.interfaces;

import com.CP.KPCOS.dtos.response.object.LoginResponseApi;
import com.CP.KPCOS.dtos.response.object.RegisterResponseApi;
import com.nimbusds.jose.JOSEException;
import reactor.core.publisher.Mono;

import java.text.ParseException;


public interface AuthService {
    Mono<LoginResponseApi> login(String username, String password) throws ParseException, JOSEException;

    Mono<RegisterResponseApi> register(String username, String email, String password) throws ParseException, JOSEException;
}