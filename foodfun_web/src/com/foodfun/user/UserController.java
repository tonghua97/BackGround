package com.foodfun.user;

import java.util.List;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Test;
import com.foodfun.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * UserController
 */
//@Before(BlogInterceptor.class)
public class UserController extends Controller {
	public void index() {
		setAttr("UserPage", User.dao.paginate(getParaToInt(0, 1), 20));
		render("user.html");
	}

//	public void add() {
//		
//	}
	
	public void upload() {
	}
	
	public void save() {
		getModel(User.class).save();
		redirect("/user");
	}
	
//	public void edit() {
//		setAttr("user", User.dao.findById(getParaToInt()));
//	}
	
	public void update() {
		getModel(User.class).update();
		redirect("/user");
	}
	
	public void delete() {
		User.dao.deleteById(getParaToInt());
		redirect("/user");
	}
}