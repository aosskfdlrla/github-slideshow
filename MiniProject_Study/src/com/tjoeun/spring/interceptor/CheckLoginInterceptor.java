package com.tjoeun.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.tjoeun.spring.dto.UserDTO;

public class CheckLoginInterceptor implements HandlerInterceptor{
	private UserDTO loginUserDTO;
	
	// Annotation 방식에서만 가능한 방법
	// interceptor에서는 자동주입으로 Bean을 주입받지 못한다.
	// 따라서 interceptor에서 사용하는 객체들은 다음과 같은 방법으로 가져온다.
	// interceptor를 등록하는 곳에서 <-- ServletAppContext.java
	// Bean을 주입 받아서 생성자로 받아서 사용해야 한다.
	public CheckLoginInterceptor(UserDTO loginUserDTO) {
		this.loginUserDTO = loginUserDTO;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(loginUserDTO.isUserLogin() == false) {
			String contextPath = request.getContextPath();
			System.out.println(contextPath);
			response.sendRedirect(contextPath + "/user/not_login");
			return false;
		}
		return true;
	}
	
}
