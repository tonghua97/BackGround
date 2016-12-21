package com.foofun.login;

import com.jfinal.core.Controller;

public class LoginController extends Controller{
	public void index(){
		render("login.html");
	}
	
	public void login(){
		String username = getPara("uname");
		String password = getPara("pwd");
		if(username.equals("admin")||password.equals("123456")){
			setAttr("msg","success");
		}else{
			setAttr("msg","fail");
		}
		renderJson();
	}
}
