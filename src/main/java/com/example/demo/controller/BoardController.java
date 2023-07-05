package com.example.demo.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.BoardDAO;
import com.example.demo.vo.BoardVO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class BoardController {
	@Autowired
	private BoardDAO dao;

	public void setDao(BoardDAO dao) {
		this.dao = dao;
	}
	
	@GetMapping("/listBoard")
	public void list(Model model) {
		model.addAttribute("list",dao.findAll());
	}
	
	@GetMapping("/detailBoard")
	public ModelAndView detail(int no) {
		ModelAndView mav = new ModelAndView();
		//조회수 증가 메소드 
		dao.updateHit(no);
		mav.addObject("b",dao.findByno(no));
		return mav;
	}
	
	@GetMapping("/deleteBoard")
	public void deleteForm (Model model, int no) {
		model.addAttribute("b",dao.findByno(no));
	}
	
	
	@PostMapping("/deleteBoard")
	public ModelAndView deleteSubmit (BoardVO b, String pwd, String fname, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("redirect:/listBoard");
		String path = request.getServletContext().getRealPath("upload");
		try {
			int re = dao.delete(b);
			pwd = b.getPwd();
			if(re == 1) {
				File file = new File(path + "/" + fname);
				file.delete();
			}else {
				mav.setViewName("redirect:/listBoard");
			}
		} catch (Exception e) {
			System.out.println("예외발생 deleteBoard"+e.getMessage());
		}
		return mav;
	}
}