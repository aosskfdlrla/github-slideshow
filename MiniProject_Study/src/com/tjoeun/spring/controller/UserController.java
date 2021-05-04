package com.tjoeun.spring.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tjoeun.spring.dto.UserDTO;
import com.tjoeun.spring.service.UserService;
import com.tjoeun.spring.validator.UserValidator;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Resource(name="loginUserDTO")
	private UserDTO loginUserDTO;
	
	@GetMapping("/login")
	public String user_Login
	(@ModelAttribute("tmpLoginUserDTO") UserDTO loginUserDTO,
	 @RequestParam(value="failure", defaultValue="false") boolean failure, Model model) {
		model.addAttribute("failure", failure);
		return "user/login";
	}
	
	@PostMapping("/login_proc")
	public String loginProc(@Valid @ModelAttribute("tmpLoginUserDTO") UserDTO tmpLoginUserDTO, BindingResult result) {
		if(result.hasErrors()) {
			return "user/login";
		}
		
		userService.getLoginUser(tmpLoginUserDTO);
		
		if(loginUserDTO.isUserLogin() == true) {
			return "user/login_success";
		} else {
			return "user/login_failure";
		}
	}
	
	@GetMapping("/join")
	public String user_Join(@ModelAttribute("joinUserDTO") UserDTO joinUserDTO) {
		return "user/join";
	}
	
	@PostMapping("/join_proc")
	public String joinProc(@Valid @ModelAttribute("joinUserDTO") UserDTO joinUserDTO, BindingResult result) {
		if(result.hasErrors()) {
			return "user/join";
		}
		userService.addUserInfo(joinUserDTO);
		
		return "user/join_success";
	} 
	
	@GetMapping("/logout")
	public String user_Logout() {
		userService.logoutUserInfo(loginUserDTO);
		return "user/logout";
	}
	
	@GetMapping("/not_login")
	public String user_notLogin() {
		return "user/not_login";
	}
	 
	@GetMapping("/modify")
	public String user_Modify(@ModelAttribute("modifyUserDTO") UserDTO modifyUserDTO) {
		
		userService.getModifyUserDTO(modifyUserDTO);
		return "user/modify";
	}
	
	@PostMapping("/modify_proc")
	public String user_ModifyProc(@Valid @ModelAttribute("modifyUserDTO") UserDTO modifyUserDTO, BindingResult result) {
		userService.modifyUserInfo(modifyUserDTO);
		if(result.hasErrors()) {
			return "user/modify";
		}
		
		return "user/modify_success";
	}
	
	// @Valid 어노테이션을 통해서 검증이 필요한 객체를 가져오기 전에 먼저 수행될 메소드를 지정한다.
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		UserValidator validator1 = new UserValidator();
		binder.addValidators(validator1);
	}
}
