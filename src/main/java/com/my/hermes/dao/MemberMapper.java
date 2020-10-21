package com.my.hermes.dao;

import java.util.ArrayList;

import com.my.hermes.vo.MapVO;
import com.my.hermes.vo.MemberVO;

public interface MemberMapper {

	// 로그인 기능
	public MemberVO login(MemberVO vo);
	
	// 회원가입 기능
	public int sign(MemberVO vo);
	
	// 카카오로그인
	public String kakaologin(MemberVO vo);
	
	// 비밀번호 업데이트
	public int pwdUpdate(MemberVO vo);
	
	// 지도에저장된정보를 가져오는 기능
	public ArrayList<MapVO> maptitle(String user_id);
}
