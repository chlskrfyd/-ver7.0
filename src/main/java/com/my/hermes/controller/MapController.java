package com.my.hermes.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.hermes.dao.MapDAO;
import com.my.hermes.dao.MemberDAO;
import com.my.hermes.vo.EventVO;
import com.my.hermes.vo.MapVO;

@Controller
public class MapController {
	@Autowired
	private MapDAO dao;
	// 지도 메인화면 이동
	@RequestMapping(value = "/map/mapmain", method = RequestMethod.GET)
	public String mapmain() {
		return "/map/mapmain";
	}
	// 지도 정보 받아오기
	@RequestMapping(value = "/map/mapwrite", method = RequestMethod.POST)
	public String mapwrite(MapVO vo,HttpSession session) {
		String userid = (String)session.getAttribute("userid");
		ArrayList<String> list = vo.getWaypoints_input();
		vo.setWaypoint_inputArr0(list.get(0));
		vo.setWaypoint_inputArr1(list.get(1));
		vo.setWaypoint_inputArr2(list.get(2));
		vo.setWaypoint_inputArr3(list.get(3));
		vo.setWaypoint_inputArr4(list.get(4));
		vo.setUser_id(userid);
		dao.write(vo);	
		
		return "redirect:/map/mapmain";
	}
}
