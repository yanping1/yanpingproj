package com.dkha.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkha.common.exception.DkAuthorityException;
import com.dkha.common.exception.DkException;
import com.dkha.common.result.systemcode.SystemCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 后端主要实现用户的验证、token的签发、从http中解析出token串、token的验证、token的刷新等
 * 在本类中并未全部实现，有待完善
 * @Author: Spring
 * Created  on 2018/4/23.
 */
public class JwtHelper {

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    /**
     * 用于HS256 hash加密算法，也可采用非对称的RS256公私钥加密 https://www.cnblogs.com/hehheai/p/6513871.html
     */
    private static final String secret = "dk_employment_2019.10.12";

    /**
     * JWT生成方法，采用id和expiresTime，除保留字段，用户可自定义其余字段
     * 以下为保留字段
     *
     *  iss(Issuser)：如果签发的时候这个claim的值是“a.com”，验证的时候如果这个claim的值不是“a.com”就属于验证失败；
     *  sub(Subject)：如果签发的时候这个claim的值是“liuyunzhuge”，验证的时候如果这个claim的值不是“liuyunzhuge”就属于验证失败；
     *  aud(Audience)：如果签发的时候这个claim的值是“['b.com','c.com']”，验证的时候这个claim的值至少要包含b.com，c.com的其中一个才能验证通过；
     *  exp(Expiration time)：如果验证的时候超过了这个claim指定的时间，就属于验证失败；
     *  nbf(Not Before)：如果验证的时候小于这个claim指定的时间，就属于验证失败；
     *  iat(Issued at)：它可以用来做一些maxAge之类的验证，假如验证时间与这个claim指定的时间相差的时间大于通过maxAge指定的一个值，就属于验证失败；
     *  jti(JWT ID)：如果签发的时候这个claim的值是“1”，验证的时候如果这个claim的值不是“1”就属于验证失败
     *
     * @return
     */
    public static String generateJWT(String userId, String uuid) {

        /**设置JWT头部信息*/
        Map<String, Object> headerMap = new HashMap<String, Object>(2);
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");
        String token = null;
        try {
            token = JWT.create().withHeader(headerMap)
                    .withClaim(JwtConstantEnum.USER_ID.code, userId)
                    .withClaim(JwtConstantEnum.UUID.code, uuid)
                    .sign(Algorithm.HMAC256(secret));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new DkException(e);
        }
        return token;
    }


    /**
     * 验证JWT 该验证只能验证JWT保留字段，具体自定义验证需自定义完成
     * 具体的错误类型可以通过设置求参数通过下面的main函数验证
     * @param token
     * @return
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new DkAuthorityException(SystemCode.TOKEN_ERROR.code.toString());
        } catch (TokenExpiredException e) {
            logger.error(e.getMessage(), e);
            throw new DkAuthorityException(SystemCode.TOKEN_EXPIRED.code.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DkAuthorityException(SystemCode.TOKEN_ERROR.code.toString());
        }
    }

    public static void main(String[] args) {
        //JwtHelper.verifyToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIn0.kEZueQ8xZ_FKvlOxfC01bTaW3EqUGV9HJKKeeor6XQ1");
    }

}
