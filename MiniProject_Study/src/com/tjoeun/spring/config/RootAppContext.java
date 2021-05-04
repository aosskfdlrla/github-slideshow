package com.tjoeun.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import com.tjoeun.spring.dto.UserDTO;

// Bean을 정의하는 xml 파일 지정하기
// 프로젝트에서 사용하는 Bean을 정의하는 클래스
// (root-context.xml)
@Configuration
public class RootAppContext {
	@Bean("loginUserDTO")
	@SessionScope
	public UserDTO loginUserDTO() {
		return new UserDTO();
	}
}
