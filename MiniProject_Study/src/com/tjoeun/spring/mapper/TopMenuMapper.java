package com.tjoeun.spring.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tjoeun.spring.dto.BoardInfoDTO;

public interface TopMenuMapper {
	
	@Select("SELECT * FROM board_info_table order by board_info_idx")
	List<BoardInfoDTO> getTopMenuList();
}
