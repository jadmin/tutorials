/*
 * @(#)Java8Lambdas.java	2017-03-28
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.somehowuse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.github.javaclub.java8.beans.Artist;
import com.github.javaclub.java8.beans.DataMocker;
import com.github.javaclub.java8.beans.MusicChapter;
import com.github.javaclub.java8.beans.User;

/**
 * Java8Lambdas
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Java8Lambdas.java 2017-03-28 2017-03-28 14:07:39 Exp $
 */
public class Java8Lambdas extends MusicChapter {
	
	@Test
	public void getNamesOfArtists_Lambda() {
		List<String> list = artists.stream()
                      .map(artist -> artist.getName())
                      .collect(Collectors.toList());
		System.out.println(list);
    }
	
	@Test
	public void getNamesOfArtists_Lambda_Modify() {
		List<String> list = artists.stream().map(artist -> {
			return artist.getName() + "_OKKKKKK";
		}).collect(Collectors.toList());
		System.out.println(list);
	}
	
	@Test
	public void beansPropAsMap() { // 收集实体的属性作为map
		List<User> list = DataMocker.generateUserList();
		Map<Long, String> map = list.stream().collect(Collectors.toMap(User::getId, User::getName));
		System.out.println(map);
	}
	
	@Test
	public void beansAsMap() { // 收集成实体本身map
		List<User> list = DataMocker.generateUserList();
		Map<Long, User> map = list.stream().collect(Collectors.toMap(User::getId, user -> user));
		System.out.println(map);
	}
	
	@Test
	public void beansAsMap_V2() { // 收集成实体本身map
		List<User> list = DataMocker.generateUserList();
		Map<Long, User> map = list.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		System.out.println(map);
	}
    
	@Test
    public void getNamesOfArtists_MethodReference() {
		List<String> list = artists.stream()
                      .map(Artist::getName)
                      .collect(Collectors.toList());
		System.out.println(list);
    }
    
    @Test
    public void artistsLivingInLondon() {
	    	List<Artist> list = artists.stream()
	                      .filter(e -> "London".equals(e.getNationality()))
	                      .collect(Collectors.toList());
	    	System.out.println(list);
    }
    
    @Test
    public void testStream_1() {
	    	artists.stream().filter(artist -> {
	    		System.out.println(artist.getName()); // 因为流没有终止操作，不会输出任何内容
	    		return artist.isFrom("London");
	    	});
    }
    
    @Test
    public void testStream_2() {
	    	long num = artists.stream().filter(artist -> {
	    		System.out.println(artist.getName()); // 有count()终止操作，会输出内容
	    		return artist.isFrom("London");
	    	}).count();
	    	System.out.println("num = " + num);
    }
    
    @Test
    public void collectUse() {
	    	List<String> strs = Stream.of("a", "b", "c", "hello", "world")
	    			.collect(Collectors.toList());
	    	System.out.println(strs);
     }

	@Test
	public void testArrayDefined() {
		String[] strArray = {"hello", "world"};
		System.out.println(strArray + "\n" + Arrays.asList(strArray));
	}

}
