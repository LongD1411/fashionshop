package com.project.shopapp.services.impls;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.shopapp.dtos.request.AuthRequest;
import com.project.shopapp.dtos.request.IntrospectRequest;
import com.project.shopapp.dtos.request.LogoutRequest;
import com.project.shopapp.dtos.request.RefreshRequest;
import com.project.shopapp.dtos.respone.AuthResponse;
import com.project.shopapp.dtos.respone.IntrospectResponse;
import com.project.shopapp.entities.InvalidatedToken;
import com.project.shopapp.entities.User;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.repositories.InvalidatedTokenRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.services.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Override
    public AuthResponse authenticate(AuthRequest request) {
        Optional<User> optional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if(optional.isEmpty()) throw  new AppException(ErrorCode.USER_NOT_EXISTED);
        User user = optional.get();
        boolean matchPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matchPassword) throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);
        AuthResponse authResponse = AuthResponse.builder().token(token.token).expiryTime(token.expiredTime).build();
        return authResponse;
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token,false);
        }catch (AppException e){
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private  Token generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.ofEpochMilli(issueTime.getTime())
                .plus(1, ChronoUnit.DAYS)
                .toEpochMilli()
        );
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getPhoneNumber())
                .expirationTime(expiredTime)
                .issueTime(issueTime)
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return new Token(jwsObject.serialize(), expiredTime);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        user.getRoles().forEach(role -> stringJoiner.add("ROLE_" +role.getName()));
        return stringJoiner.toString();
    }
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        Date expiryTime = (isRefresh) ?
                new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                        .toInstant()
                        .plus(1,ChronoUnit.DAYS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if (!(verified || expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(),false);

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
    @Override
    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(),true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByPhoneNumber(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthResponse.builder()
                .token(token.token)
                .expiryTime(token.expiredTime)
                .build();
    }

    private record Token(String token, Date expiredTime){};
}
