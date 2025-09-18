package top.clarkhg.img_viewer.util;

import java.util.Date;
import java.util.UUID;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import top.clarkhg.img_viewer.controller.PathController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenUtil {
	private static Logger logger = LoggerFactory.getLogger(PathController.class);
	public static String SECRET = "sdjhakdhajdklsl;o653632AFEGasdai2uo39.'l[l;','u8hiuhH";

	public static String genToken(String username, String password) {
		return JWT.create()
				// .withClaim("email", email)
				.withClaim("username", username)
				.withClaim("password", password)
				.withClaim("UUID", UUID.randomUUID().toString()) // 想伪造除非知道这个UUID
				.withIssuedAt(new Date())
				.sign(Algorithm.HMAC256(SECRET));
	}

	// 拿 username
	public static String getUsername(String token) {
		try {
			DecodedJWT djwt = JWT.decode(token);
			// System.out.println("djwt: "+djwt);
			String username=djwt.getClaim("username").asString();
			// System.out.println("Username: "+username);
			return username;
		} catch (Exception e) {
			logger.debug("Token->username failed!");
			return null;
		}
	}

	// role
	public static Integer getRole(String token) {
		try {
			DecodedJWT djwt = JWT.decode(token);
			return djwt.getClaim("role").asInt();
		} catch (Exception e) {
			return null;
		}
	}

}



