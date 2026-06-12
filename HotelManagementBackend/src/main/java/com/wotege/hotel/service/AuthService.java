package com.wotege.hotel.service;

import com.wotege.hotel.dto.auth.LoginRequest;
import com.wotege.hotel.dto.auth.LoginResponse;
import com.wotege.hotel.dto.auth.RegisterRequest;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
