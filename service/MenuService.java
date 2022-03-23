package com.mhl.service;

import com.mhl.dao.MenuDAO;
import com.mhl.domain.Menu;

import java.util.List;

/*
完成对menu表的操作(通过调用MenuDAO)
 */
public class MenuService {
    //定义一个MenuDAO属性
    private MenuDAO menuDAO=new MenuDAO();


    //返回所有的菜品
    public List<Menu> list(){
        return menuDAO.queryMulti("select * from menu",Menu.class);
    }

    //需要方法 根据id 返回Menu对象
    public Menu getMenuById(int id){
        return menuDAO.querysingle("select * from menu where id=?",Menu.class,id);
    }
}
