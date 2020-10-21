package com.my.hermes.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.hermes.dao.KakaoDAO;
import com.my.hermes.dao.MemberDAO;
import com.my.hermes.vo.MapVO;
import com.my.hermes.vo.MemberVO;

@Controller
public class MemberController {

	@Autowired
	private MemberDAO dao;

	@Autowired
	private KakaoDAO kakao;

	// 로그인 화면 이동
	@RequestMapping(value = "/member/loginForm", method = RequestMethod.GET)
	public String login() {
		return "/member/loginForm";
	}

	// 회원가입 화면 이동
	@RequestMapping(value = "/member/signup", method = RequestMethod.GET)
	public String signup() {
		return "/member/signup";
	}

	// 이용방법 화면 이동
	@RequestMapping(value = "/member/about", method = RequestMethod.GET)
	public String about() {
		return "/member/about";
	}

	// 로그인 기능
	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public String loginGo(MemberVO vo, HttpSession httpSession) {
		MemberVO result = dao.login(vo);
		if (result == null) {
			return "/member/loginForm";
		} else {
			httpSession.setAttribute("userid", result.getUser_id());
			httpSession.setAttribute("useremail", result.getUser_email());
			return "redirect:/";
		}
	}

	// 회원가입 기능
	@RequestMapping(value = "/member/sign", method = RequestMethod.POST)
	public String sign(MemberVO vo) {
		int result = dao.sign(vo);
		if (result != 1) {
			return "/member/signup";
		} else {
			return "/member/loginForm";
		}
	}

	// 로그아웃 기능
	@RequestMapping(value = "/member/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session.getAttribute("access_Token") != null) {
			kakao.kakaoLogout((String) session.getAttribute("access_Token"));
		}
		dao.logout(session);
		return "redirect:/";
	}

	// 프로필 화면 이동
	@RequestMapping(value = "/member/profile", method = RequestMethod.GET)
	public String profile(HttpSession session, Model model) {
		String userid = (String)session.getAttribute("userid");
		ArrayList<MapVO> map = dao.mapinfo(userid);
		model.addAttribute("mapinfo", map);
		return "/member/profile";
	}

	// 카카오로그인
	@RequestMapping(value = "/member/kakaologin", method = RequestMethod.GET)
	public String login(String code, Model model, HttpSession session, MemberVO vo) {
		String token = kakao.getAccessToken(code);
		HashMap<String, Object> userinfo = kakao.getUserInfo(token);
		String email = (String) userinfo.get("email");
		vo.setUser_email(email);
		String result = dao.kakaologin(vo);
		if (result == null) {
			model.addAttribute("email", email);
			return "/member/signup";			
		}
		else {
			session.setAttribute("userid", result);
			session.setAttribute("useremail", email);
		}
		return "redirect:/";
	}
	
	// 네이버로그인 기능
	@RequestMapping(value = "/naver/callback", method = RequestMethod.GET)
	public String callback() {
		return "/naver/callback";
	}
	
	// 네이버로그인 기능
	@RequestMapping(value = "/naver/login", method = RequestMethod.GET)
	public String naverLogin(String email,MemberVO vo,Model model,HttpSession session) {
		vo.setUser_email(email);
		String result = dao.kakaologin(vo);
		if (result == null) {
			model.addAttribute("email", email);
			return "/member/signup";			
		}
		else {
			session.setAttribute("userid", result);
			session.setAttribute("useremail", email);
		}
		return "redirect:/";
	}
	
	// 회원 탈퇴
	@RequestMapping(value = "/member/signout", method = RequestMethod.GET)
	public String signout() {
		return "redirect:/";
	}
	
	// 비밀번호 수정
	@RequestMapping(value = "/member/pwdUpdate", method = RequestMethod.POST)
	public String pwdUpdate(MemberVO vo, HttpSession session, RedirectAttributes rttr) {
		String user_id = (String)session.getAttribute("userid");
		vo.setUser_id(user_id);
		int result = dao.pwdUpdate(vo);
		if(result == 1) {
			rttr.addFlashAttribute("updateResult", result);
			session.invalidate();
			return "redirect:/";
		} else {
			return "/member/profile";
		}
	}
	
	// 맵보내기
	@RequestMapping(value = "/member/mapSend", method = RequestMethod.GET)
	public String mapSend(MapVO mapinfo, Model model) {
		model.addAttribute("mapinfo", mapinfo );
		return "/map/mapmain";
	}

}
