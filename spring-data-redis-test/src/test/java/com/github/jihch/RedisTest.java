package com.github.jihch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
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
	
	
}
