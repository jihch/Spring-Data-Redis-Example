package com.github.jihch;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.jihch.model.User;

/**
 * 
 * @author 姬鸿昌
 * 2020年3月21日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 添加键值对
	 */
	@Test
	public void test1() {
		redisTemplate.opsForValue().set("key", "test");
	}
	
	/**
	 * 获取redis中的数据
	 */
	@Test
	public void test2() {
		String str = (String) redisTemplate.opsForValue().get("key");
		System.out.println(str);
	}
	
	@Test
	public void test3() {
		User user = new User();
		user.setAge(30);
		user.setId(1);
		user.setName("张三");
		//更换序列化器
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.opsForValue().set("user", user);
	}
	
	/**
	 * 获取 User
	 */
	@Test
	public void test4() {
		//更换序列化器
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		User user = (User) redisTemplate.opsForValue().get("user");
		System.out.println(user);
	}
	
	/**
	 * 添加 User JSON格式
	 */
	@Test
	public void test5() {
		User user = new User();
		user.setAge(23);
		user.setId(2);
		user.setName("李四");
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
		redisTemplate.opsForValue().set("user", user);
	}
	
	@Test
	public void test6() {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
		User user = (User) redisTemplate.opsForValue().get("user");
		System.out.println(user);
	}
	
	/**
	 * 通过RedisTemplate测试pipeline
	 */
	@Test
	public void test7() {
		Long start = System.currentTimeMillis();
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<User> valueSerializer = new Jackson2JsonRedisSerializer<>(User.class);
		Map<byte[], byte[]> tuple = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			User user = new User();
			user.setAge(23);
			user.setId(2);
			user.setName("李四");
			byte[] key =  stringSerializer.serialize("user:"+i);
			byte[] value = valueSerializer.serialize(user);
			tuple.put(key, value);
		}
		Long end = System.currentTimeMillis();
		System.out.println("cost: " + (end - start) + " ms" );
		
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.openPipeline();
				connection.mSet(tuple);
				connection.closePipeline();
				return null;
			}
		});
		end = System.currentTimeMillis();
		System.out.println("cost: " + (end - start) + " ms" );
	}
	
	
}
