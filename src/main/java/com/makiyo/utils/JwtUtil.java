package com.makiyo.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${eps.jwt.secret}")
    private String secret;

    @Value("${eps.jwt.expire}")
    private int expire;
//创建Token
    public String createToken(int userId){
        Date date=DateUtil.offset(new Date(), DateField.DAY_OF_YEAR,5);
        Algorithm algorithm=Algorithm.HMAC256(secret);
        JWTCreator.Builder builder= JWT.create();
        String token=builder.withClaim("userId",userId).withExpiresAt(date).sign(algorithm);
        return token;
    }

    public int getUserId(String token){
        DecodedJWT jwt=JWT.decode(token);
        int userId=jwt.getClaim("userId").asInt();
        return userId;
    }

    public void verifierToken(String token){
        Algorithm algorithm=Algorithm.HMAC256(secret);
        JWTVerifier verifier=JWT.require(algorithm).build();
        verifier.verify(token);
    }
}
