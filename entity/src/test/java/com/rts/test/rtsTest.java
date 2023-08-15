package com.rts.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class rtsTest {
    static final String KEY = "rents";
    public static void main(String[] args) {
//        String jwt = JWT.create()
//                .withClaim("name", "张三")
//                .withClaim("id", 1)
//                .sign(Algorithm.HMAC256(KEY));
//        System.out.println(jwt);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5byg5LiJIiwiaWQiOjF9.fl0d_tISJe4iM7BApsZJBb2TlVM0CKPUMCQ1_RYd71s";
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(KEY))
                .build();
        DecodedJWT decode = verifier.verify(token);
        String name = decode.getClaim("name").asString();
        Integer id = decode.getClaim("id").asInt();
        System.out.println(id);
        System.out.println(name);
    }
}
