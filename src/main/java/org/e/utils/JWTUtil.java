package org.e.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JWTUtil {

    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;

    // 签名哈希的密钥，对于不同的加密算法来说含义不同
    public static final String APP_SECRET = "ahda4sf151@1sd45f%#&";

    /**
     * 根据用户id和昵称生成token
     * @return JWT规则生成的token
     */
    public static String getJwtToken(Integer id){
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("baobao-user")
                .setIssuedAt(new Date())
                .claim("id", id)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // HS256算法实际上就是MD5加盐值，此时APP_SECRET就代表盐值
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 根据token获取会员id
     * @param jwtToken
     * @return 解析token后获得的用户id
     */
    public static Integer getMemberIdByJwtToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return -1;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (Integer) claims.get("id");
    }
}
