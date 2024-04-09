package com.hello.forum.menu.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hello.forum.bbs.web.RestController;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.menu.service.MenuService;
import com.hello.forum.menu.vo.MenuVO;
import com.hello.forum.utils.AjaxResponse;

@RestController
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * DB에서 조회한 메뉴정보를 저장하는 변수
	 * 매번 DB에서 조회하는 트랜잭션을 최소화하기 위함
	 */
	private static List<MenuVO> cachedMenuList;
	
	@GetMapping("/ajax/menu/list")
	private AjaxResponse getMenuList(@SessionAttribute(value = "_LOGIN_USER_", required = false) MemberVO memberVO) {
		
		if(cachedMenuList == null) {
			cachedMenuList = menuService.getAllMenu();
		}
		
		List<MenuVO> menuList = cachedMenuList.stream()
											  .filter((menu) -> {
												  //권한에맞게 메뉴를 가져갈 수 있도록!
												  if(memberVO == null) {
													  //로그인을 안한 사용자
													  return menu.getRole().equals("ALL");
												  }
												  else if (memberVO != null
														  && memberVO.getAdminYn().equals("N")) {
													  //로그인을 한 일반사용자
													  return menu.getRole().equals("ALL") || menu.getRole().equals("USER");
												  }
												  
												  // 관리자라면 모든것을 다 가져가야 할 것이므로 return true해줌
												  return true;
											  })
											  .collect(Collectors.toList());
		
		return new AjaxResponse().append("menu", menuList);
	}
}
