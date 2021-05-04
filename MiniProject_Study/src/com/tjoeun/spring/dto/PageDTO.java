package com.tjoeun.spring.dto;

import lombok.Getter;

@Getter
public class PageDTO {
	// 최소 페이지 번호
	private int min;
	
	// 최대 페이지 번호
	private int max;
	
	// 이전 버튼 누르면 이동하는 페이지 번호
	private int prePage;
	
	// 다음 버튼 누르면 이동하는 페이지 번호
	private int nextPage;
	
	// 전체 페이지 개수
	private int pageCount;
	
	// 현재 페이지 번호
	private int currentPage;
	
	/*
	 * 전체 게시글의 개수, 페이지장 게시글의 개수,
	 * 현재 페이지 번호 
	 * ㄴ이 값을들 가지고 위의 값들을 계산함.
	 * 
	 * 이 작업은 생성자에서 함 생성자의 매개 변수로 
	 * int contentCnt : 전체 게시글의 개수
	 * int currentPage : 현재 페이지 번호
	 * int contentPageCnt : 페이지 당 게시글의 개수
	 * int paginationCnt : 페이지 버튼의 개수
	 * 선언함
	 */
	// contentCnt : database에서 가져옴 (BoardMapper)
	// currentPage : page parameter로 전달함
	// contentPageCnt, paginationCnt : option.properties에 설정함.
	public PageDTO(int contentCnt, int currentPage, int contentPageCnt, int paginationCnt) {
		
		// 현재 페이지 번호
		this.currentPage = currentPage;
		
		// 전체 페이지 개수 = 전체 글개수 / 페이지당 글개수
		pageCount = contentCnt / contentPageCnt;
		if(contentCnt % contentPageCnt	> 0) {
			pageCount++;
		}
		
		// 페이지 네비게이션 버튼 설정하기
		  //  1page 부터 10page까지는 최소 :  1  -- 최대  : 10
		  // 11page 부터 10page까지는 최소 : 11  -- 최대  : 20
		  // 21page 부터 30page까지는 최소 : 21  -- 최대  : 30
		  //   1 - 10 :   1 -- 10
		  //  11 - 20 :  11 -- 20
		  //  21 - 30 :  21 -- 30
		  // ..........
		  
		  // 최대 페이지 번호의 경우 
		  // 최소 페이지 번호에 9를 더해줌
		  // 최솟값만 구하면 됨
		  
		  // 현재 페이지 번호에서 1을 빼면  
		  //   0 -  9 :   1 -- 10
		  //  10 - 19 :  11 -- 20
		  //  20 - 29 :  21 -- 30 
		  
		  
		  // 최소페이지
		  // 최소페이지 = 현재페이지 - 1
		  
		  // 현재 페이지 번호에서 1을 빼고
		  // 10(페이지당 글의 개수)으로 나누어주면
		  
		  //   0 -  :   1 -- 10
		  //   1 -  :  11 -- 20
		  //   2 -  :  21 -- 30 
		  
		  // 이것을 다시 10(페이지당 글의 개수)으로 곱해주면
		  
		  //    0 -  
		  //   10 - 
		  //   20 -  
		  
		  // 여기에 1을 더해 줌
		  
		  //    1 -  
		  //   11 - 
		  //   21 -  
		min = ((currentPage - 1) / contentPageCnt) * contentPageCnt + 1;
		
		max = min + paginationCnt - 1;
		//  마지막 페이지 처리하기
	    //  전체 페이지가 323 페이지인 경우,
	    //   ㄴ 최대페이지 번호가 전체페이지 개수보다 크면
	    //      최대페이지 번호를 전체 페이지로 함
	    // 최대페이지 번호가 전체페이지 개수를 넘어가지 않도록 함
		if(max > pageCount) {
			max = pageCount;
		}
		
		// 1 page 인 경우 prePage가 0이 됨
		// 이런 경우에는 prePage 버튼을 비활성화 해 놓음
		prePage = min - 1;
		nextPage = max + 1;
		
		// 다음페이지버튼 번호가 전체페이지 개수를 넘어가지 않도록 함
		if(nextPage > pageCount) {
			nextPage = pageCount;
		}
	}
	
}
