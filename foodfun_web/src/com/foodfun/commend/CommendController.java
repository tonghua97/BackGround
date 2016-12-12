package com.foodfun.commend;

import java.util.List;

import com.foodfun.common.model.Commend;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Test;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * CommendController
 */
//@Before(BlogInterceptor.class)
public class CommendController extends Controller {
	public void index() {
		setAttr("CommendPage", Commend.dao.paginate(getParaToInt(0, 1), 20));
		render("commend.html");
	}

	public void add() {
		
	}
	
	
	public void save() {
		getModel(Commend.class).save();
		redirect("/commend");
	}
	
	public void edit() {
		setAttr("commend", Commend.dao.findById(getPara()));
	}
	
	public void update() {
		getModel(Commend.class).update();
		redirect("/commend");
	}
	
	public void delete() {
		Commend.dao.deleteById(getPara());
		redirect("/commend");
	}
}