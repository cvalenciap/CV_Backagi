package pe.com.sedapal.agi.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.security.model.Usuario;

@Component
public class RedisUtil {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> userTemplate;

	/*
	 * @Autowired private RedisTemplate<String, Usuario> userTemplate;
	 */
	public ValueOperations<String, Object> getValueOperations() {
		try {
			return userTemplate.opsForValue();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public String getRedisKey(final String userId) {
		return SessionConstants.REDIS_PREFIX_USERS + SessionConstants.REDIS_KEYS_SEPARATOR + userId;
	}

	public Object findByToken(String token) {
		Object user = (Object) getValueOperations().get(token);
		if (user == null) {
			throw new NotFoundException("User does not exist in the DB");
		}
		return user;
	}

	public void saveToken(String token, Usuario usuario) {
		getValueOperations().set(token, usuario);
	}

	public Object FindByToken(HttpServletRequest req) {
		Object user = (Object) getValueOperations().get(getRedisKey(getToken(req)));

		if (user == null) {
			throw new NotFoundException("User does not exist in the DB");
		}
		return user;
	}

	public String getToken(HttpServletRequest req) {

		String token = req.getHeader("Authorization");

		if (token != null && token.startsWith("Bearer ")) {
			token = token.replace("Bearer ", "");
		}

		return token;
	}

	public void save(String key, Object usuario) {
		getValueOperations().set(getRedisKey(key), usuario);
		String var = getRedisKey(key);
	}

	public Object FindById(String key) {
		Object user = (Object) getValueOperations().get(key);
		return user;
	}

	public void deleteKey(String key) {
		userTemplate.delete(getRedisKey(key));
	}
	
	public void deleteKeyToken(HttpServletRequest req) {
		String key = getRedisKey(getToken(req));
		userTemplate.delete(key);
	}
	
}
