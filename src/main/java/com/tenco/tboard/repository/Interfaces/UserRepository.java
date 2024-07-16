package com.tenco.tboard.repository.Interfaces;

import java.util.List;

import com.tenco.tboard.model.User;

public interface UserRepository {
	
	// 회원가입
	int addUser(User user);
	// 회원 탈퇴
	void deleteUser(int id);
	// 이름 조회
	User getUserByusername(String username);
	// 로그인
	User getUserByusernameAndPssword(String username, String password);
	
	//관리자
	// 유저 전체조회
	List<User> getAllUsers();
	
}
