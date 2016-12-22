package com.foodfun.login;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class LoginInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		String user = inv.getController().getSessionAttr("user");
		if(user != null){
			inv.invoke();
			System.out.println("hello");
		}else{
			inv.getController().redirect("/login");
		}

	}

}
