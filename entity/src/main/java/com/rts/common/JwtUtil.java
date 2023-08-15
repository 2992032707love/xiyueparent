package com.rts.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rts.entity.UmsUser;

public class JwtUtil {
    private static final String KEY = "rentingsheng";
    public static String getToken (UmsUser umsUser) {
        return JWT.create()
//                .withClaim("phone", umsUser.getPhone())
                .withClaim("name", umsUser.getName())
                .withClaim("id", umsUser.getId())
                .sign(Algorithm.HMAC256(KEY));
    }
    public static UmsUser decode (String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
//        String phone = verify.getClaim("phone").asString();
        String name = verify.getClaim("name").asString();
        Integer id = verify.getClaim("id").asInt();
        UmsUser umsUser = new UmsUser();
        umsUser.setId(id);
        umsUser.setName(name);
        return umsUser;
    }
}