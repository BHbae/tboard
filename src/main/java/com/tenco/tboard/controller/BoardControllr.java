package com.tenco.tboard.controller;

import java.io.IOException;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.BoardRepositoryImpl;
import com.tenco.tboard.repository.Interfaces.BoardRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/*")
public class BoardControllr extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardRepository boardRepository;
	
	@Override
	public void init() throws ServletException {
		boardRepository =  new BoardRepositoryImpl();
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("principal") == null) {
			response.sendRedirect(request.getContextPath() +"/user/signin");
			return;
		}
		
		switch (action) {
		case "/create":
			// 게시글 생성페이지 이동처리
			showCreateBoardForm(request, response, session);
			break;
		case "/list":
			handleListBoards(request, response, session);
			break;
		case "/view":
			showViewBoard(request, response, session);
			break;
		case "/delete":
			handleDeleteBoard(request, response, session);
			break;
		case "/update":
			showEditBoardForm(request, response, session);
			break;
		case "/deleteComment":
			handleDeleteComment(request, response, session);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
		
	}
	
	/**
	 * 댓글 삭제 시능(GET방식처리)
	 * @param request
	 * @param response
	 * @param session
	 */
	private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * 상세보기 화면 이동(GET 방식 처리)
	 * @param request
	 * @param response
	 * @param session
	 */
	private void showViewBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Board board = boardRepository.getBoardById(id);
			if(board == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			request.setAttribute("board", board);
			
			// 현재 로그인한 사용자의 ID
			User user = (User)session.getAttribute("principal");
			if (user != null) {
				request.setAttribute("userID", user.getId());
			}
			// TODO - 댓글 조회
			// 댓글 조회 및 권한 확인 추가 예정
			
			
			
			request.getRequestDispatcher("/WEB-INF/views/board/view.jsp").forward(request, response);;
			
		} catch (Exception e) {
			// 잘못된 겁근입니다. back();
		}
		
		
	}


	/**
	 * 수정 폼 화면 이동(인증검사 반드시 처리)
	 * @param request
	 * @param response
	 * @param session
	 */
	private void showEditBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * 게시글 삭제 기능
	 * @param request
	 * @param response
	 * @param session
	 */
	private void handleDeleteBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
	}


	/**
	 * 게시글 생성 화면 이동
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void showCreateBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/views/board/create.jsp").forward(request, response);
	}


	/**
	 * 페이징 처리하기
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void handleListBoards(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		
		
		// 게시글 목록 조회기능
		int page = 1; // 기본페이지번호
		int pageSize = 5; // 한페이지당 보여질 게시글에 수 (limit 로 바라 볼 수 있다.)
		try {
			String pageStr = request.getParameter("page");
			if(pageStr != null) {
				page = Integer.parseInt(pageStr);
			}
		} catch (Exception e) {
			//유효하지 않은 번호를 마음대로 보낼 경우
			page = 1;
		}
		//pageSize --> 3
		// page1,page,2 요청 동적으로 시작값을 계산하는 산수공식넣기
		int offset = (page - 1) * pageSize; // 시작 위치 계산(offset 값 계산)
		
		List<Board> boardList = boardRepository.getAllBoards(pageSize, offset);
		
		// 페이징 처리1단계 (현재 페이지번호, pageSize, offset)
		
		// 전체 게시글 수 조회 
		int totalBoards = boardRepository.getTotalBardCount();
		// 총 페이지 수계산 -->
		int totalPages = (int)Math.ceil((double)totalBoards / pageSize);
		
		request.setAttribute("boardList", boardList);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("currentPage", page);
		
		// 현재 로그인한 사용자 ID 설정
		if(session != null) {
			 User user = (User)session.getAttribute("principal");
			if(user != null) {
				request.setAttribute("userId", user.getId());
			}
		}
		
		request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("principal") == null) {
			response.sendRedirect(request.getContextPath() +"/user/signin");
			return;
		}
		
		switch (action) {
		case "/create":
			handleCreateBoard(request, response, session);
			break;
			
		case "/edit":
			break;
			
		case "/addComment":
			break;	

		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
		
	}

	/**
	 * 게시글 생성 처리
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException 
	 */
	private void handleCreateBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		// 유효성 검사는 생략
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		User user =  (User)session.getAttribute("principal");
		
		Board board = Board.builder().userId(user.getId())
					.title(title)
					.content(content)
					.build();
		
		boardRepository.addBoard(board);
		response.sendRedirect(request.getContextPath() + "/board/list?page=1");
	}

}
