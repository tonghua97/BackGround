package com.foodfun.openuser;

import java.util.List;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Openuser;
import com.foodfun.common.model.Test;
import com.foodfun.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 *  OpenuserController
 */
//@Before(BlogInterceptor.class)
public class OpenuserController extends Controller {
	public void index() {
		setAttr("OpenuserPage", Openuser.dao.paginate(getParaToInt(0, 1), 20));
		render("openuser.html");
	}

//	public void add() {
//		
//	}
	
	public void upload() {
	}
	
	public void save() {
		getModel(Openuser.class).save();
		redirect("/openuser");
	}
	
//	public void edit() {
//		setAttr("user", User.dao.findById(getParaToInt()));
//	}
	
	public void update() {
		getModel(Openuser.class).update();
		redirect("/openuser");
	}
	
	public void delete() {
		Openuser.dao.deleteById(getParaToInt());
		redirect("/openuser");
	}
}