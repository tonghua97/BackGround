package com.foodfun.login;

import com.foodfun.common.model.Admin;
import com.foodfun.index.IndexController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

public class LoginController extends Controller{
	@Clear
	public void index(){
		render("login.html");
	}
	@Clear
	public void login(){
		String username = getPara("uname");
		String password = getPara("pwd");
		String pwd =  Admin.dao.findFirst("select password from admin where username="+"\""+username +"\"").getPassword();
		if(pwd != null){
			if(pwd.equals(password)){
				setSessionAttr("user", username);//设置session，保存登录用户的昵称
				setAttr("msg","success");
			}else{
				setAttr("msg","密码错误");
			}
		}else{
			setAttr("msg","无此用户名");
		}
		renderJson();
	}
}
