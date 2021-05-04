package com.tjoeun.spring.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.tjoeun.spring.dto.ContentDTO;

public interface BoardMapper {
	
	@SelectKey(statement = "SELECT CONTENT_SEQ.NEXTVAL FROM DUAL", keyProperty = "content_idx", before = true, resultType = int.class)
	@Insert("INSERT INTO CONTENT_TABLE(CONTENT_IDX, CONTENT_SUBJECT, CONTENT_TEXT, " + 
			"CONTENT_FILE, CONTENT_WRITER_IDX, CONTENT_BOARD_IDX, CONTENT_DATE) " + 
			"VALUES (#{content_idx}, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, " +
			"#{content_writer_idx}, #{content_board_idx}, SYSDATE)")
	void addContentInfo(ContentDTO writeContentDTO);
	
	@Select("SELECT BOARD_INFO_NAME " + 
			"FROM BOARD_INFO_TABLE " +
			"WHERE BOARD_INFO_IDX=#{board_info_idx}")
	String getBoardInfoName(int board_info_idx);
	
	@Select(" SELECT C.CONTENT_IDX, C.CONTENT_SUBJECT, U.USER_NAME CONTENT_WRITER_NAME, "
		  + "        TO_CHAR(C.CONTENT_DATE, 'YYYY-MM-DD HH24:MI:SS') CONTENT_DATE "
		  + "  FROM CONTENT_TABLE C, USER_TABLE U "
		  + " WHERE C.CONTENT_WRITER_IDX = U.USER_IDX "
		  + "   AND C.CONTENT_BOARD_IDX = #{board_info_idx} "
		  + " ORDER BY C.CONTENT_IDX DESC")
	List<ContentDTO> getContentList(int board_info_idx, RowBounds rowBounds);
	
	// 수정, 삭제시 어떤 사람이 쓴 글인지 확인하기
	// C.CONTENT_WRITER_IDX 도 SELECT 해서 가져오면 됨
	@Select("SELECT U.USER_NAME CONTENT_WRITER_NAME, "
		  + "TO_CHAR(C.CONTENT_DATE, 'YYYY-MM-DD HH24:MI:SS') CONTENT_DATE, "
		  + "C.CONTENT_SUBJECT, C.CONTENT_TEXT, C.CONTENT_FILE, C.CONTENT_WRITER_IDX "
		  + "FROM CONTENT_TABLE C, USER_TABLE U "
		  + "WHERE C.CONTENT_WRITER_IDX = U.USER_IDX "
		  + "AND C.CONTENT_IDX = #{content_idx}")
	ContentDTO getContentInfo(int content_idx);
	
	@Update("UPDATE CONTENT_TABLE SET CONTENT_SUBJECT=#{content_subject}, "
		  + "CONTENT_TEXT=#{content_text}, "
		  + "CONTENT_FILE=#{content_file, jdbcType=VARCHAR} "
		  + "WHERE CONTENT_IDX=#{content_idx}")
	void modifyContentInfo(ContentDTO modifyContentDTO);
	
	@Select("SELECT COUNT(*) FROM CONTENT_TABLE " + 
			"WHERE CONTENT_BOARD_IDX = ${content_board_idx}")
	int getContentCnt(int content_board_idx);
}
