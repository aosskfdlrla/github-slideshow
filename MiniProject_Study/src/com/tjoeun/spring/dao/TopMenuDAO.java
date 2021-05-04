package com.tjoeun.spring.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tjoeun.spring.dto.BoardInfoDTO;
import com.tjoeun.spring.mapper.TopMenuMapper;

@Repository
public class TopMenuDAO {
	
	@Autowired
	private TopMenuMapper topMenuMapper;
	
	public List<BoardInfoDTO> getTopMenuList(){
		List<BoardInfoDTO> topMenuList = topMenuMapper.getTopMenuList();
		
		return topMenuList;
	}
}
