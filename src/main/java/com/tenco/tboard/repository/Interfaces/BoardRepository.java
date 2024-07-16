package com.tenco.tboard.repository.Interfaces;

import java.util.List;

import com.tenco.tboard.model.Board;

public interface BoardRepository {
	// 게시판 웹 서비스 개발()
	
	// 게시글 추가
	void addBoard(Board board);
	
	// 게시물 수정
	void updateBoard(Board board);
	
	// 게시물 삭제
	void deleteBoard(int id);
	
	// 게시물 상세보기
	Board getBoardById(int id);
	
	// 게시물 목록 (페이징퍼리)
	List<Board> getAllBoards(int limit, int offset);
	
	// 페이지목록
	int getTotalBardCount();
	
}
