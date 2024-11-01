package com.project.shopapp.services;

import com.nimbusds.jose.JOSEException;
import com.project.shopapp.dtos.request.AuthRequest;
import com.project.shopapp.dtos.request.IntrospectRequest;
import com.project.shopapp.dtos.request.LogoutRequest;
import com.project.shopapp.dtos.request.RefreshRequest;
import com.project.shopapp.dtos.respone.AuthResponse;
import com.project.shopapp.dtos.respone.IntrospectResponse;

import java.text.ParseException;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    AuthResponse refreshToken(RefreshRequest request) throws  ParseException, JOSEException;
    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
}
