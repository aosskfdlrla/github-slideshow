package com.tjoeun.spring.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tjoeun.spring.dto.UserDTO;

public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserDTO.class.isAssignableFrom(clazz);
	}

	// 회원 가입시 비밀번호가 맞는지 검증하는 메소드
	@Override
	public void validate(Object target, Errors errors) {
		UserDTO userDTO = (UserDTO)target;
		String dtoName = errors.getObjectName();
		System.out.println("dtoName : " + dtoName);
		if(dtoName.equals("joinUserDTO")) {
			if(userDTO.getUser_pw().equals(userDTO.getUser_pw2()) == false) {
				errors.rejectValue("user_pw", "NotEquals");
				errors.rejectValue("user_pw2", "NotEquals");
			}
			
			if(userDTO.isInputUserID() == false) {
				errors.rejectValue("user_id", "DontCheckUserIdExist");
			}
		}
	}
	
}
