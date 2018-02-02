package com.na.user.socketserver.common;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

public class CustomStringRedisSerializer implements RedisSerializer<Object> {

	private final Charset charset;

	public CustomStringRedisSerializer() {
		this(Charset.forName("UTF8"));
	}

	public CustomStringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	public String deserialize(byte[] bytes) {
		return (bytes == null ? null : new String(bytes, charset));
	}

	public byte[] serialize(Object string) {
		String resultString = null;
		if(string instanceof Integer) {
			resultString = string + "";
		} else {
			resultString = (String) string;
		}
		return (resultString == null ? null : resultString.getBytes(charset));
	}
	
	
	

}
