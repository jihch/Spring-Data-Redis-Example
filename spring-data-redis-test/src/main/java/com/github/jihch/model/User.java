package com.github.jihch.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author 姬鸿昌
 * 2020年3月21日
 */
@Data
public class User implements Serializable {
	
	private Integer id;
	private String name;
	private Integer age;
	
}
