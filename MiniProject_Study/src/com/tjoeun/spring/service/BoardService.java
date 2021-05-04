package com.tjoeun.spring.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.spring.dao.BoardDAO;
import com.tjoeun.spring.dto.ContentDTO;
import com.tjoeun.spring.dto.PageDTO;
import com.tjoeun.spring.dto.UserDTO;

@Service
@PropertySource("/WEB-INF/properties/option.properties")
public class BoardService {
	
	@Value("${path.load}")
	private String path_load;
	
	// 한 페이지당 보여주는 글의 개수
	@Value("${page.listcnt}")
	private int page_listcnt;
	
	// 한 페이지당 보여주는 페이지 버튼 개수
	@Value("${page.paginationcnt}")
	private int page_paginationcnt;
	
	@Autowired
	private BoardDAO boardDAO;
	
	@Resource(name="loginUserDTO")
	private UserDTO loginUserDTO;
	
	private String saveUploadFile(MultipartFile upload_file) {
		String file_name = System.currentTimeMillis() + "_" + upload_file.getOriginalFilename();
		
		try {
			upload_file.transferTo(new File(path_load + "/" + file_name));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return file_name;
	}
	
	// parameter로 전달되어 오는 data 확인하기
	public void addContentInfo(ContentDTO writeContentDTO) {
		System.out.println(writeContentDTO.getContent_subject());
		System.out.println(writeContentDTO.getContent_text());
		System.out.println(writeContentDTO.getUpload_file());
		System.out.println(writeContentDTO.getUpload_file().getSize());
		
		MultipartFile upload_file = writeContentDTO.getUpload_file();
		
		if(upload_file.getSize() > 0) {
			String file_name = saveUploadFile(upload_file);
			writeContentDTO.setContent_file(file_name);
		}
		
		writeContentDTO.setContent_writer_idx(loginUserDTO.getUser_idx());
		boardDAO.addContentInfo(writeContentDTO);
	}
	
	public String getBoardInfoName(int board_info_idx) {
		String board_info_name = boardDAO.getBoardInfoName(board_info_idx);
		return board_info_name;
	}
	
	//페이징 작업
	public List<ContentDTO> getContentList(int board_info_idx, int page){
		
		int start = (page - 1) * page_listcnt;
		RowBounds rowBounds = new RowBounds(start, page_listcnt);
		
		List<ContentDTO> contentList = boardDAO.getContentList(board_info_idx, rowBounds);
		return contentList;
	}
	
	public ContentDTO getContentInfo(int content_idx) {
		ContentDTO contentDTO = boardDAO.getContentInfo(content_idx);
		return contentDTO;
	}
	
	public void modifyContentInfo(ContentDTO modifyContentDTO) {
		MultipartFile upload_file = modifyContentDTO.getUpload_file();
		if(upload_file.getSize() > 0) {
			String file_name = saveUploadFile(upload_file);
			modifyContentDTO.setContent_file(file_name);
		}
		
		boardDAO.modifyContentInfo(modifyContentDTO);
	}
	
	// content_board_idx : 게시판 index 번호
	// currentPage : 현재 페이지 번호
	public PageDTO getContentCnt(int content_board_idx, int currentPage) {
		int contentCnt = boardDAO.getContentCnt(content_board_idx);
		PageDTO pageDTO = new PageDTO(contentCnt, currentPage, page_listcnt, page_paginationcnt);
		
		return pageDTO;
	}
	
}
