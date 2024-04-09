package com.hello.forum.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hello.forum.menu.dao.MenuDao;
import com.hello.forum.menu.vo.MenuVO;

@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuDao menuDao;
	
//	@Transactional
	@Override
	public List<MenuVO> getAllMenu() {
		
		List<MenuVO> menuList = this.menuDao.getAllMenu();
		
//		return this.menuDao.getAllMenu();
		return menuList;

	}

}
